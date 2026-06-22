package com.myhindlab.abkat.activities.couriermodule;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.getAmPmFrom24Hour;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampListCheckAdapter;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.SendCourierToLabNewModel;
import com.myhindlab.abkat.models.couriermodule.BarcodeListModel;
import com.myhindlab.abkat.models.couriermodule.BarcodeListNewModel;
import com.myhindlab.abkat.models.couriermodule.CamplistOnLandingLabModel;
import com.myhindlab.abkat.models.couriermodule.ClientListModel;
import com.myhindlab.abkat.models.couriermodule.CourierCategoryModel;
import com.myhindlab.abkat.models.couriermodule.CourierForwardedModel;
import com.myhindlab.abkat.models.couriermodule.CourierTransportModeModel;
import com.myhindlab.abkat.models.couriermodule.HLLDCWiseLab;
import com.myhindlab.abkat.models.couriermodule.LBMWiseLab;
import com.myhindlab.abkat.models.couriermodule.LabListModel;
import com.myhindlab.abkat.models.couriermodule.LandingLabNewModel;
import com.myhindlab.abkat.models.couriermodule.RunnerBoyOnMultipleLabCodeModel;
import com.myhindlab.abkat.models.couriermodule.SendCourierToLabModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.FileUtils;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourierSendStep1_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private int campTypeId;

    private TextView tvBarcode, tvDate,tvFromLab, tvLandingLab, tvCampType, tvSendCourierTo, tvCampList, tv_select_courier_to_lab, tv_select_courier_category,
            tv_select_date, tv_select_time, tv_select_mode_of_transport, tv_select_runner_boy, tv_bus_vehicle, tv_expected_arrival_date, tv_expected_arrival_time;
    private EditText edt_docket_no, edt_company_name, edt_bus_no, edt_mobile_no, edt_amount, edt_sample_box, edt_sample_count, edt_barcode;
    private ImageView imv_barcode_scan, imv_photo;
    private LinearLayout ll_courier, ll_runner_boy, ll_bus_vehicle, ll_mobile, llBarcode;
    private Button btn_submit;

    private File imageFile, imageFolder;
    private Uri photoURI;
    private ArrayList<BarcodeListModel.Output> selectedBarcodeList;
    private List<BarcodeListModel.Output> barcodeList;
    private String userId, projectId, desigId = "",fromlabId, selectedLabID, couriertolabId, sendcouriertolabId, fromLabName, toLabCode, CampId, categoryId, CampDate,
            isSampleCategory, modeOfTransportId, runnerBoyId = "0", fileName, forwardedCourierIds, destClientCode = "", sourceLabCode = "", sourceClientCode;
    private int mYear, mMonth, mDay;
    private ArrayList<CamplistOnLandingLabModel.Output> campList;
    private ArrayList<CamplistOnLandingLabModel.Output> selectedcamplist;
    private  ArrayList<SendCourierToLabModel.Output> mailist;
    private Bitmap photoBm;
    List<CourierForwardedModel.OutputBean> selectedCouriersList = new ArrayList<>();

    private LocalBroadcastManager localBroadcastManager;
    private Uri patientURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_send_step1);

        init();
        getSessionDetails();
        setDefaults();
        setEventListener();
        setUpToolBar();
    }

    private void init() {
        context = CourierSendStep1_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        //tvClientList = findViewById(R.id.tvClientList);
       // tv_select_registration_lab = findViewById(R.id.tv_select_registration_lab);
//        tv_registration_lab = findViewById(R.id.tv_registration_lab);
        tv_select_courier_to_lab = findViewById(R.id.tv_select_courier_to_lab);
        //  tv_select_processing_lab = findViewById(R.id.tv_select_processing_lab);
        tv_select_courier_category = findViewById(R.id.tv_select_courier_category);
        tv_select_date = findViewById(R.id.tv_select_date);
        tv_select_time = findViewById(R.id.tv_select_time);
        tv_select_mode_of_transport = findViewById(R.id.tv_select_mode_of_transport);
        tv_select_runner_boy = findViewById(R.id.tv_select_runner_boy);
        tv_bus_vehicle = findViewById(R.id.tv_bus_vehicle);
        tv_expected_arrival_date = findViewById(R.id.tv_expected_arrival_date);
        tv_expected_arrival_time = findViewById(R.id.tv_expected_arrival_time);
        edt_docket_no = findViewById(R.id.edt_docket_no);
        edt_company_name = findViewById(R.id.edt_company_name);
        edt_bus_no = findViewById(R.id.edt_bus_no);
        edt_mobile_no = findViewById(R.id.edt_mobile_no);
        edt_amount = findViewById(R.id.edt_amount);
        edt_sample_box = findViewById(R.id.edt_sample_box);
        edt_sample_count = findViewById(R.id.edt_sample_count);
        edt_barcode = findViewById(R.id.edt_barcode);
        imv_barcode_scan = findViewById(R.id.imv_barcode_scan);
        imv_photo = findViewById(R.id.imv_photo);
//        tvDestClientList = findViewById(R.id.tvDestClientList);
        tvBarcode = findViewById(R.id.tvBarcode);
        llBarcode = findViewById(R.id.llBarcode);




        tvDate = findViewById(R.id.tvDate);
        tvLandingLab = findViewById(R.id.tvLandingLab);
        tvCampType = findViewById(R.id.tvCampType);
        tvCampList = findViewById(R.id.tvCampList);
        tvSendCourierTo = findViewById(R.id.tvSendCourierTo);

        ll_courier = findViewById(R.id.ll_courier);
        ll_runner_boy = findViewById(R.id.ll_runner_boy);
        ll_bus_vehicle = findViewById(R.id.ll_bus_vehicle);
        ll_mobile = findViewById(R.id.ll_mobile);
        btn_submit = findViewById(R.id.btn_submit);
        tvFromLab = findViewById(R.id.tvFromLab);


        tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

//        imageFolder = new File(Environment.getExternalStorageDirectory() + "/HLL-Connect/" + "Courier");
//        if (!imageFolder.exists())
//            imageFolder.mkdirs();


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            imageFolder = new File(Environment.getExternalStorageDirectory() + "/HLL-Connect/Courier");
            if (!imageFolder.exists()) {
                boolean mkdirs = imageFolder.mkdirs();
                Log.e("", "init: " + mkdirs);
            }
        } else {
            imageFolder = new File(getApplicationContext().getExternalFilesDir("").getPath() + "/imageFile");
            if (!imageFolder.exists()) {
                boolean mkdirs = imageFolder.mkdirs();
                Log.e("", "init: " + mkdirs);
            }
        }
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                desigId = json.getString("DESGID");
                Log.d("Desig Id", "getSessionDetails: " + desigId);
                projectId = json.getString("ProjectId");
//                fromLabName = json.getString("MaplabName");
//                fromlabId = json.getString("MaplabCode");
//                sourceLabCode = json.getString("MaplabCode");
//                tv_select_registration_lab.setText(fromLabName);
//                sendcouriertolabId = getIntent().getStringExtra("sendcouriertolabId");
//                tv_select_processing_lab.setText(getIntent().getStringExtra("processingLabName"));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
//        tvClientList.setText(ApplicationConstants.ClientName);
//        tvDestClientList.setText(ApplicationConstants.ClientName);
//        sourceClientCode = ApplicationConstants.ClientCode;
//        destClientCode = ApplicationConstants.ClientCode;

        forwardedCourierIds = getIntent().getStringExtra("forwardedCourierIds");
        if (!forwardedCourierIds.equals("0")) {
//            fromlabId = getIntent().getStringExtra("fromlabId");
//            tv_select_registration_lab.setText(getIntent().getStringExtra("fromLabName"));
//            sendcouriertolabId = getIntent().getStringExtra("sendcouriertolabId");
//            tv_select_processing_lab.setText(getIntent().getStringExtra("processingLabName"));
            selectedCouriersList = (List<CourierForwardedModel.OutputBean>)
                    getIntent().getSerializableExtra("selectedCouriersList");
        }

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        tv_select_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear));
        tv_select_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) + ":" + String.format("%02d", Integer.valueOf(calendar.get(Calendar.MINUTE)))));

//        if (desigId != null)
//            if (desigId.equals("27") || desigId.equals("36") || desigId.equals("38") || desigId.equals("78") || desigId.equals("11") ||
//                    desigId.equals("12") || desigId.equals("18")) {
//                tv_select_registration_lab.setVisibility(View.GONE);
//                tv_registration_lab.setVisibility(View.VISIBLE);
//            } else if (desigId.equals("10") || desigId.equals("23") || desigId.equals("84")) {
//                tv_select_registration_lab.setVisibility(View.VISIBLE);
//                tv_registration_lab.setVisibility(View.GONE);
//            }

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("FinishCourierSend1_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        ll_courier.setVisibility(View.GONE);
        ll_runner_boy.setVisibility(View.GONE);
        ll_bus_vehicle.setVisibility(View.GONE);
        ll_mobile.setVisibility(View.GONE);
    }

    private void setEventListener() {
        if (forwardedCourierIds.equals("0")) {
            tvFromLab.setOnClickListener(this);
//            tv_select_processing_lab.setOnClickListener(this);
        }
        tv_select_courier_to_lab.setOnClickListener(this);
        tv_select_courier_category.setOnClickListener(this);
        tv_select_time.setOnClickListener(this);
        tv_select_mode_of_transport.setOnClickListener(this);
        tv_select_runner_boy.setOnClickListener(this);
        tv_expected_arrival_date.setOnClickListener(this);
        tv_expected_arrival_time.setOnClickListener(this);
        imv_barcode_scan.setOnClickListener(this);
        imv_photo.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
//        tvClientList.setOnClickListener(this);
//        tvDestClientList.setOnClickListener(this);
        tvBarcode.setOnClickListener(this);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));


                                //

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

//                                if (Utilities.isDateBeforeCurrentDate(CampDate)) {
//                                    btnAssign.setEnabled(true);
//                                } else {
//                                    btnAssign.setEnabled(false);
//                                }

                                selectedLabID = "";
                                campTypeId = 0;
                                tvCampType.setText("");

                                tvLandingLab.setText("");
                                tvCampList.setText("");
//
                                if (campList!=null){
                                    campList.clear();
                                }


                                //                                if (Utilities.isNetworkAvailable(context)) {
                                //                                } else {
                                //                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                //                                }
                            }

                        }, mYear, mMonth, mDay);
                try {
                  //  dpd1.getDatePicker().setCalendarViewShown(false);

                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -2);
                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());
                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });

        tvCampList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (CampDate != null) {
                    if (Utilities.isNetworkAvailable(context)) {

                        if (campList != null&&!campList.isEmpty()) {
                            showCampListDialog(campList);
                        } else {
                            new GetCampList().execute(CampDate, String.valueOf(campTypeId), selectedLabID);
                        }

                    } else {
                        Utilities.showToastMessage("Please Check Your Connection", context, false);
                    }
                } else {
                    tvDate.setError("Select Camp Date");

                }


            }
        });

        tv_select_courier_to_lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {

                    JsonArray Type_CW_HomeandHublab = new JsonArray();
                    JsonArray campArr = new JsonArray();

                    if (campList != null) {
                        for (CamplistOnLandingLabModel.Output o : campList) {
                            if (o.isChecked()) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("CampID", o.getCampid());
                                campArr.add(jsonObject);

                            }
                        }
                    }

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("Campdate", CampDate);
                        jsonObject.addProperty("CampType", campTypeId);
                        jsonObject.addProperty("Labcode", selectedLabID);
                        jsonObject.add("CampID", campArr);
                        Type_CW_HomeandHublab.add(jsonObject);


                       if (sendcouriertolabId.equalsIgnoreCase("1")){
                           new GetSendCourierToLab().execute(campArr.toString(), "1");
                       }else if (sendcouriertolabId.equalsIgnoreCase("2")){
                           new GetSendCourierToLab().execute(campArr.toString(), "2");

                       }
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }


        });

        tvSendCourierTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<SendCourierToLabNewModel> sendCourierToLabNewModelArrayListArrayList = new ArrayList<>();
                sendCourierToLabNewModelArrayListArrayList.add(new SendCourierToLabNewModel("Home Lab", 1));
                sendCourierToLabNewModelArrayListArrayList.add(new SendCourierToLabNewModel("Hub Lab", 2));


                showSendCourierToLab(sendCourierToLabNewModelArrayListArrayList);


//                tvCampList.setText("");
//                if (campList != null) {
//                    campList.clear();
//                }


            }
        });


        tvCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();

                campTypeModelArrayList.add(new CampTypeModel("All", 0));
                campTypeModelArrayList.add(new CampTypeModel("REGULAR", 1));
                campTypeModelArrayList.add(new CampTypeModel("CSC CAMP", 2));
                campTypeModelArrayList.add(new CampTypeModel("Door To Door", 3));
                campTypeModelArrayList.add(new CampTypeModel("CSC D2D", 4));


                showCampType(campTypeModelArrayList);


//                tvCampList.setText("");
//                if (campList != null) {
//                    campList.clear();
//                }


            }
        });


        tv_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (forwardedCourierIds.equals("0")) {
                            tv_select_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                            tv_select_time.setText("");
                        } else {
                            try {
                                Date selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                                for (int i = 0; i < selectedCouriersList.size(); i++) {
                                    Date courierReceivedDate = new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(selectedCouriersList.get(i).getReceivedDate());
                                    if (courierReceivedDate.after(selectedDate)) {
                                        Utilities.showMessageString("Send date cannot be less than received date", context);
                                        return;
                                    }
                                }
                                tv_select_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                                tv_select_time.setText("");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
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

        tvLandingLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetLandingLab().execute(userId);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                    return;
                }
            }
        });


        tv_select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                            if (forwardedCourierIds.equals("0")) {
                                try {
                                    Date sendDate = new SimpleDateFormat("yyyy-MM-dd").parse(tv_select_date.getText().toString().trim());
                                    Calendar rightNow = Calendar.getInstance();
                                    int currentYear = rightNow.get(Calendar.YEAR);
                                    int currentMonth = rightNow.get(Calendar.MONTH);
                                    int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);
                                    int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                                    int currentMinute = rightNow.get(Calendar.MINUTE);

                                    Date currentTime = new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(Utilities.ConvertDateFormat(Utilities.dfDate, currentDay, currentMonth + 1, currentYear));

                                    if (sendDate.equals(currentTime)) {

                                        if (selectedHour > currentHourIn24Format) {
                                            tv_select_time.setText("");
                                            Utilities.showMessageString("Send time should not be greater than current time", context);
                                            return;
                                        } else if (selectedHour == currentHourIn24Format) {
                                            if (selectedMinute > currentMinute) {
                                                tv_select_time.setText("");
                                                Utilities.showMessageString("Send time should not be greater than current time", context);
                                                return;
                                            }
                                        }
                                    }

                                    tv_select_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(selectedHour)) + ":" + String.format("%02d", Integer.valueOf(selectedMinute))));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Date selectedDate = new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(tv_select_date.getText().toString().trim());
                                    for (int i = 0; i < selectedCouriersList.size(); i++) {
                                        Date courierReceivedDate = new SimpleDateFormat("yyyy-MM-dd")
                                                .parse(selectedCouriersList.get(i).getReceivedDate());
                                        if (courierReceivedDate.equals(selectedDate)) {
                                            Date receivedTime = new SimpleDateFormat("hh:mm a")
                                                    .parse(selectedCouriersList.get(i).getReceivedTime());
                                            int sentHour = Integer.parseInt(new SimpleDateFormat("HH").format(receivedTime));
                                            int sentMinute = Integer.parseInt(new SimpleDateFormat("mm").format(receivedTime));

                                            if (selectedHour < sentHour) {
                                                tv_select_time.setText("");
                                                Utilities.showMessageString("Send time cannot be less than received time", context);
                                                return;
                                            } else if (selectedHour == sentHour) {
                                                if (selectedMinute < sentMinute) {
                                                    tv_select_time.setText("");
                                                    Utilities.showMessageString("Send time cannot be less than received time", context);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    tv_select_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(selectedHour)) + ":" + String.format("%02d", Integer.valueOf(selectedMinute))));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }, hour, minute, false);


                    mTimePicker.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpToolBar() {
        ImageButton back_btn = findViewById(R.id.tool_leftbtn);
        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        TextView Title = findViewById(R.id.tool_titile);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Send Courier");

        back_btn.setOnClickListener(view -> finish());

        btn_save_accordian.setOnClickListener(v -> {
            startActivity(new Intent(context, CourierSentList_Activity.class));
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBarcode: {
                if (barcodeList == null || barcodeList.size() == 0) {
                    if (Utilities.isNetworkAvailable(context)) {

//                        JsonArray Type_CW_HomeandHublab = new JsonArray();
                        JsonArray campArr = new JsonArray();

                        if (campList != null) {
                            for (CamplistOnLandingLabModel.Output o : campList
                            ) {
                                if (o.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("CampID", o.getCampId());
                                    campArr.add(jsonObject);

                                }
                            }
                        }



                        new GetBarcodeList().execute(CampDate, String.valueOf(campTypeId), selectedLabID,campArr.toString(),"0","1");
                    } else {
                        Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                    }
                } else {
                    BarcodeListDialog(barcodeList);
                }
            }
            break;
            case R.id.tvFromLab: {
                if (Utilities.isNetworkAvailable(context)) {
//                    if (desigId.equals("23")) {
                    new GetFromLab().execute(userId);
//                        new GetLBMLabsDetails().execute(userId);
//                    } else if (desigId.equals("10") || desigId.equals("84")) {
//                        new GetDCToLabMapping().execute(userId);
//                    }
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
            case R.id.tv_select_courier_to_lab: {
                if (Utilities.isNetworkAvailable(context)) {
                    //  new GetFromOutSource("1").execute(sourceClientCode, destClientCode, sourceLabCode);
//                    new GetCenterList().execute("1");
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
            case R.id.tvDestClientList: {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetClientlist("0").execute("0");
//                    new GetCenterList().execute("1");
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
//            case R.id.tv_select_processing_lab: {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetFromOutSource("2").execute(sourceClientCode, destClientCode, sourceLabCode);
//                } else {
//                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
//                }
//            }
//            break;
            case R.id.tv_select_courier_category: {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetCourierCategory().execute();
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
            case R.id.tv_select_mode_of_transport: {
                if (tv_select_courier_to_lab.getText().toString().trim().equals("")) {
                    Utilities.showMessageString("Please select courier to lab", context);
                    return;
                }

//                if (tv_select_processing_lab.getText().toString().trim().equals("")) {
//                    Utilities.showMessageString("Please select processing lab", context);
//                    return;
//                }

                if (Utilities.isNetworkAvailable(context)) {
                    new GetCourierTransportMode().execute();
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
            case R.id.tv_select_runner_boy: {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetRunnerBoyOnMultipleLabcode().execute(fromlabId, toLabCode);
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
            case R.id.tv_expected_arrival_date: {
                DatePickerDialog dpd = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

                    tv_expected_arrival_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                    tv_expected_arrival_time.setText("");
                }, mYear, mMonth, mDay);
                try {
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                    dpd.getDatePicker().setCalendarViewShown(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd.show();
            }
            break;
            case R.id.tv_expected_arrival_time: {
                try {

                    if (tv_select_date.getText().toString().trim().isEmpty()) {
                        Utilities.showMessageString("Please select courier send date", context);
                        return;
                    }

                    if (tv_expected_arrival_date.getText().toString().trim().isEmpty()) {
                        Utilities.showMessageString("Please select expected arrival date", context);
                        return;
                    }

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> {
                        try {
                            Date sendDate = new SimpleDateFormat("yyyy-MM-dd").parse(tv_select_date.getText().toString().trim());
                            Date expectedArrivalDate = new SimpleDateFormat("yyyy-MM-dd").parse(tv_expected_arrival_date.getText().toString().trim());

                            final String time = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute);
                            if (sendDate.equals(expectedArrivalDate)) {
                                Calendar rightNow = Calendar.getInstance();
                                int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                                int currentMinute = rightNow.get(Calendar.MINUTE);

                                if (selectedHour < currentHourIn24Format) {
                                    tv_expected_arrival_time.setText("");
                                    Utilities.showMessageString("Expected arrival time should not be less than current time", context);
                                    return;
                                } else if (selectedHour == currentHourIn24Format) {
                                    if (selectedMinute < currentMinute) {
                                        tv_expected_arrival_time.setText("");
                                        Utilities.showMessageString("Expected arrival time should not be less than current time", context);
                                        return;
                                    }
                                }
                            }
                            tv_expected_arrival_time.setText(getAmPmFrom24Hour(time));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, hour, minute, false);
                    mTimePicker.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.imv_barcode_scan: {
                edt_barcode.setError(null);
                Intent i = new Intent(context, BarcodeScannerZxingActivity.class);
                startActivityForResult(i, 101);
            }
            break;
            case R.id.imv_photo: {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }

                if (Utilities.isNetworkAvailable(context)) {
                    int ramdom = (int) (Math.random() * 9999999 + 1);


                    fileName = "Send" + "_" + fromlabId + "_" + toLabCode + "_" + sendcouriertolabId + "_" + ramdom;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ContentResolver resolver = context.getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                        photoURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 100);
                    } else {
                        imageFile = new File(imageFolder, fileName + ".png");
                        photoURI = Uri.fromFile(imageFile);
                        Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        pickImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(pickImage, 100);
                    }

                } else
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
            }
            break;
            case R.id.btn_submit: {
                submitData();
            }
            break;
        }


    }

    private void submitData() {
//        if (tv_select_registration_lab.getText().toString().trim().equals("")) {
//            Utilities.showMessageString("Please select from lab", context);
//            return;
//        }

        if (tvDate.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select Date", context);
            return;
        }

        if (tvLandingLab.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select Landing Lab", context);
            return;
        }

        if (tvCampType.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select Camp Type", context);
            return;
        }
        if (tvCampList.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select Camp Id", context);
            return;
        }
        if (tvSendCourierTo.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select Send Courier To Lab", context);
            return;
        }


        if (tv_select_courier_to_lab.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select courier to lab", context);
            return;
        }

//        if (tv_select_processing_lab.getText().toString().trim().equals("")) {
//            Utilities.showMessageString("Please select processing lab", context);
//            return;
//        }
        if (tv_select_courier_category.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select courier category", context);
            return;
        }
        if (isSampleCategory.equalsIgnoreCase("1")) {
            if (selectedBarcodeList == null || selectedBarcodeList.isEmpty()) {
                tvBarcode.setError("Please select barcodes to send");
                return;
            }
//            if (edt_sample_count.getText().toString().trim().equals("")) {
//                edt_sample_count.setError("Enter Total units or tubes count");
//                return;
//            }
            else {
//                int count = Integer.parseInt(edt_sample_count.getText().toString());
//                if (count < selectedBarcodeList.size()) {
//                    edt_sample_count.setError("Enter units or tubes count greater than or equals to selected barcodes");
//                    return;
//                }
            }

        }
        if (tv_select_date.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select courier date", context);
            return;
        }

        if (tv_select_time.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select courier time", context);
            return;
        }

        if (tv_select_mode_of_transport.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select mode of transport", context);
            return;
        }

        if (modeOfTransportId.equals("1")) {
            if (edt_docket_no.getText().toString().trim().equals("")) {
                edt_docket_no.setError("Enter docket number");
                return;
            }
            if (edt_company_name.getText().toString().trim().equals("")) {
                edt_company_name.setError("Enter company name");
                return;
            }
        } else if (modeOfTransportId.equals("2")) {
            if (edt_bus_no.getText().toString().trim().equals("")) {
                edt_bus_no.setError("Enter bus no.");
                return;
            }
        } else if (modeOfTransportId.equals("3")) {
            if (tv_select_runner_boy.getText().toString().trim().equals("")) {
                Utilities.showMessageString("Please select runner boy", context);
                return;
            }
        } else if (modeOfTransportId.equals("4")) {
            if (edt_bus_no.getText().toString().trim().equals("")) {
                edt_bus_no.setError("Enter vehicle no.");
                return;
            }
        }
        if (edt_mobile_no.getText().toString().trim().length() != 10) {
            edt_mobile_no.setError("Enter valid mobile number");
            return;
        }


        if (edt_mobile_no.getText().toString().trim().equals("")) {
            edt_mobile_no.setError("Enter mobile number");
            return;
        }

        if (edt_amount.getText().toString().trim().equals("")) {
            edt_amount.setError("Enter amount");
            return;
        } else {
//            if (edt_sample_count.getText().toString().trim().equals("")) {
//                edt_sample_count.setError("Enter Total No. units or tubes count");
//                return;
//            }
        }

        if (tv_expected_arrival_date.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select expected arrival date", context);
            return;
        }

        if (tv_expected_arrival_time.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select expected arrival time", context);
            return;
        }

        if (edt_barcode.getText().toString().trim().length() < 12) {
            Utilities.showMessageString("Please enter valid courier barcode", context);
            return;
        }

        int labcode = Integer.parseInt(fromlabId);
        String labcodeStr = fromlabId;
        if (labcode < 99) {
            labcodeStr = "0" + fromlabId;
        }
        String barCodeShouldContains = "CMH" + labcodeStr;
        if (!edt_barcode.getText().toString().trim().contains(barCodeShouldContains)) {
            Utilities.showMessageString("Please enter valid barcode", context);
            return;
        }

        if (photoBm == null) {
            Utilities.showMessageString("Please click photo", context);
            return;
        }

        if (fromlabId == toLabCode){
            Utilities.showToastMessage("You can't send courier to same from lab to courier to lab",context,false);
            return;
        }

        String forwardedIds = "";

        if (!forwardedCourierIds.equals("0")) {
//            JsonObject forwardedCouriers = new JsonObject();
//            String[] elements = forwardedCourierIds.split(",");
//            JsonArray jsonArray = new JsonArray();
//            for (String element : elements) {
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("RefCourierID", element);
//                jsonArray.add(jsonObject);
//            }
//            forwardedCouriers.add("input", jsonArray);
//            forwardedIds = forwardedCouriers.toString();

            JsonObject forwardedCouriers = new JsonObject();

            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < selectedCouriersList.size(); i++) {
                String[] elements = selectedCouriersList.get(i).getInitCourierID().split(",");
                for (String element : elements) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("RefCourierID", selectedCouriersList.get(i).getCourierID());
                    jsonObject.addProperty("InitCourierID", element);
                    jsonArray.add(jsonObject);
                }
            }
            forwardedCouriers.add("input", jsonArray);
            forwardedIds = forwardedCouriers.toString();
        } else {
            JsonObject forwardedCouriers = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("RefCourierID", "0");
            jsonObject.addProperty("InitCourierID", "0");
            jsonArray.add(jsonObject);
            forwardedCouriers.add("input", jsonArray);
            forwardedIds = forwardedCouriers.toString();
        }

        if (isSampleCategory.equals("1")) {
//            Bundle args = new Bundle();
//            args.putParcelableArrayList("barcodeArrList", selectedBarcodeList);
            JsonArray campArr = new JsonArray();

            if (campList != null) {
                for (CamplistOnLandingLabModel.Output o : campList
                ) {
                    if (o.isChecked()) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("CampID", o.getCampId());
                        campArr.add(jsonObject);

                    }
                }
            }

            startActivity(new Intent(context, CourierSendStep2_Activity.class)
                    .putExtra("fromLab", fromlabId)
                    .putExtra("toLabCode", toLabCode)
                    .putExtra("categoryId", categoryId)
                    .putExtra("courierDate", tv_select_date.getText().toString().trim())
                    .putExtra("courierTime", tv_select_time.getText().toString().trim())
                    .putExtra("modeOfTransportId", modeOfTransportId)
                    .putExtra("docketNo", edt_docket_no.getText().toString().trim())
                    .putExtra("companyName", edt_company_name.getText().toString().trim())
                    .putExtra("runnerBoyId", runnerBoyId)
                    .putExtra("busNo", edt_bus_no.getText().toString().trim())
                    .putExtra("mobileNo", edt_mobile_no.getText().toString().trim())
                    .putExtra("amount", edt_amount.getText().toString().trim())
                    .putExtra("sampleBox", edt_sample_box.getText().toString().trim())
                  //  .putExtra("sampleCount", edt_sample_count.getText().toString().trim())
                    .putExtra("expectedArrivalDate", tv_expected_arrival_date.getText().toString().trim())
                    .putExtra("expectedArrivalTime", tv_expected_arrival_time.getText().toString().trim())
                    .putExtra("barcode", edt_barcode.getText().toString().trim())
                    .putExtra("fileName", fileName)
                    .putExtra("forwardedCourierIds", forwardedCourierIds)
                    .putExtra("selectedCouriersHavingTubeCountsList", getIntent().getSerializableExtra("selectedCouriersHavingTubeCountsList"))
                    .putExtra("selectedCouriersList", getIntent().getSerializableExtra("selectedCouriersList"))
                    .putExtra("selectedBarcodeList", selectedBarcodeList)
                    .putExtra("campList", campList)
                    .putExtra("Type_CW_CourierCampID",campArr.toString())
                    .putExtra("sendcouriertolabId", sendcouriertolabId)
                    .putExtra("selectedLabID", selectedLabID)
                    .putExtra("CampType", String.valueOf(campTypeId))

            );
        } else {
            String TubeId = null, TotalCount = null, CreatedBy = null;
            JsonArray SampleDetailsJsonArray = new JsonArray();
            JsonObject tubeDetails = new JsonObject();
            tubeDetails.addProperty("TubeId", TubeId);
            tubeDetails.addProperty("TotalCount", TotalCount);
            tubeDetails.addProperty("CreatedBy", CreatedBy);
            SampleDetailsJsonArray.add(tubeDetails);

            JsonObject sampleDetails = new JsonObject();
            sampleDetails.add("input", SampleDetailsJsonArray);

            JsonObject barcodeDetails = new JsonObject();
            JsonArray barcodeArray = new JsonArray();


            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Barcode", "");
            jsonObject.addProperty("ServiceCode", "");
            barcodeArray.add(jsonObject);

            barcodeDetails.add("input", barcodeArray);


            if (Utilities.isNetworkAvailable(context)) {


                JsonArray campArr = new JsonArray();




                if (campList != null) {
                    for (CamplistOnLandingLabModel.Output o : campList
                    ) {
                        if (o.isChecked()) {
                            JsonObject jsonObject1= new JsonObject();
                            jsonObject1.addProperty("CampID", o.getCampId());
                            campArr.add(jsonObject1);

                        }
                    }
                }

                new InsertCourierSentDetails_Updated().execute(
                        fromlabId,
                        sendcouriertolabId,
                        toLabCode,
                        categoryId,
                        tv_select_date.getText().toString().trim(),
                        tv_select_time.getText().toString().trim(),
                        modeOfTransportId,
                        edt_company_name.getText().toString().trim(),
                        edt_docket_no.getText().toString().trim(),
                        edt_mobile_no.getText().toString().trim(),
                        edt_amount.getText().toString().trim(),
                        edt_sample_box.getText().toString().trim(),
                        runnerBoyId,
                        edt_sample_count.getText().toString().trim(),
                        tv_expected_arrival_date.getText().toString().trim(),
                        tv_expected_arrival_time.getText().toString().trim(),
                        edt_barcode.getText().toString().trim(),
                        "0",
                        fileName,
                        edt_bus_no.getText().toString().trim(),
                        projectId,
                        forwardedIds,
                        userId,
                        sampleDetails.toString(),
                        barcodeDetails.toString(),
                        campArr.toString(),
                        selectedLabID,
                        String.valueOf(campTypeId)


                );

            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);
            }
        }
    }

    private class GetLBMLabsDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("LMBUserid", strings[0]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetLBMLabsDetails, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status, message;
            try {
                pd.dismiss();
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        List<LBMWiseLab> mainlist = new ArrayList<>();
                        JSONArray jsonarr = mainObj.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {
                                LBMWiseLab summary = new LBMWiseLab();
                                JSONObject jsonObj = jsonarr.getJSONObject(i);
                                summary.setLabcode(jsonObj.getString("labcode"));
                                summary.setLabName(jsonObj.getString("LabName"));
                                summary.setDISTLGDCODE(jsonObj.getString("DISTLGDCODE"));
                                mainlist.add(summary);
                            }
                            listLBMLabDialogCreater(mainlist);
                        }
                    } else if (status.equalsIgnoreCase("fail")) {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server not connected", false);
                e.printStackTrace();
            }
        }
    }

    private class GetFromLab extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("UserId", strings[0]));
            // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetFromLab, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetFromLab, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status, message;
            try {
                pd.dismiss();
                if (!result.equals("")) {
//                    JSONObject mainObj = new JSONObject(result);
//                    status = mainObj.getString("status");
//                    message = mainObj.getString("message");
//                    if (status.equalsIgnoreCase("success")) {
//                        List<LBMWiseLab> mainlist = new ArrayList<>();
//                        JSONArray jsonarr = mainObj.getJSONArray("output");
//                        if (jsonarr.length() > 0) {
//                            for (int i = 0; i < jsonarr.length(); i++) {
//                                LBMWiseLab summary = new LBMWiseLab();
//                                JSONObject jsonObj = jsonarr.getJSONObject(i);
//                                summary.setLabcode(jsonObj.getString("labcode"));
//                                summary.setLabName(jsonObj.getString("LabName"));
//                                summary.setDISTLGDCODE(jsonObj.getString("DISTLGDCODE"));
//                                mainlist.add(summary);
//                            }
//                            listLBMLabDialogCreater(mainlist);
//                        }
//                    } else if (status.equalsIgnoreCase("fail")) {
//                        Utilities.showAlertDialog(context, status, message, false);
//                    }


                    LabListModel labListModel = new Gson().fromJson(result, LabListModel.class);

                    if (labListModel.getStatus().equalsIgnoreCase("success")) {
                        LabListDialog(labListModel.getOutput());
                    } else {
                        Utilities.showAlertDialog(context, labListModel.getStatus(), labListModel.getMessage(), false);
                    }

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server not connected", false);
                e.printStackTrace();
            }
        }
    }

    private void listLBMLabDialogCreater(final List<LBMWiseLab> mainlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            LBMWiseLab objMain = mainlist.get(which);
//            tv_select_registration_lab.setText(objMain.getLabName());
          //  fromlabId = objMain.getLabcode();
            tv_select_courier_to_lab.setText("");
//            tv_select_processing_lab.setText("");
        });
        builderSingle.show();
    }

    private void LabListDialog(final List<LabListModel.Output> mainlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select From Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            LabListModel.Output objMain = mainlist.get(which);
           // tvFromLab.setText(objMain.getLabName());
//            fromlabId = String.valueOf(objMain.getLabCode());
//            sourceLabCode = String.valueOf(objMain.getLabCode());
            fromlabId = String.valueOf(objMain.getLabCode());

//            tv_select_courier_to_lab.setText("");
//            tv_select_processing_lab.setText("");
            edt_barcode.setText("CMH0" + objMain.getLabCode());
        });
        builderSingle.show();

    }


    private class GetDCToLabMapping extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("UserId", strings[0]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetDCToLabMapping, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            try {
                pd.dismiss();
                if (!result.equals("")) {

                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        List<HLLDCWiseLab> mainlist = new ArrayList<HLLDCWiseLab>();
                        JSONArray jsonarr = mainObj.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {
                                HLLDCWiseLab summary = new HLLDCWiseLab();
                                JSONObject jsonObj = jsonarr.getJSONObject(i);
                                summary.setLabCode(jsonObj.getString("LabCode"));
                                summary.setLabName(jsonObj.getString("LabName"));
                                mainlist.add(summary);
                            }

                            Collections.sort(mainlist, new Comparator<HLLDCWiseLab>() {
                                @Override
                                public int compare(HLLDCWiseLab o1, HLLDCWiseLab o2) {
                                    return o1.getLabName().compareTo(o2.getLabName());
                                }
                            });
                            listHLLDCLabDialogCreater(mainlist);
                        }
                    } else if (status.equalsIgnoreCase("fail")) {
                        if (message.equalsIgnoreCase("Lab Not Map For This User ")) {
                            Utilities.showAlertDialog(context, "Success", message, true);
                        } else {
                            Utilities.showAlertDialog(context, status, message, true);
                        }
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server not connected", false);
                e.printStackTrace();
            }
        }
    }

    private void listHLLDCLabDialogCreater(final List<HLLDCWiseLab> mainlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            HLLDCWiseLab objMain = mainlist.get(which);
//            tv_select_registration_lab.setText(objMain.getLabName());
            fromlabId = objMain.getLabCode();
            tv_select_courier_to_lab.setText("");
//            tv_select_processing_lab.setText("");

        });
        builderSingle.show();

    }

//    private class GetCenterList extends AsyncTask<String, Void, String> {
//
//        private String TYPE = "";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait ...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String res = "[]";
//            TYPE = strings[0];
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("UserID", userId));
//            res = WebServiceCall.HLLAPICall(ApplicationConstants.getCenterList, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            String status = "", message = "";
//            try {
//                pd.dismiss();
//                if (!result.equals("")) {
//
//                    JSONObject mainObj = new JSONObject(result);
//                    status = mainObj.getString("status");
//                    message = mainObj.getString("message");
//                    if (status.equalsIgnoreCase("success")) {
//                        List<CenterListPojo> mainlist = new ArrayList<CenterListPojo>();
//                        JSONArray jsonarr = mainObj.getJSONArray("output");
//                        if (jsonarr.length() > 0) {
//                            for (int i = 0; i < jsonarr.length(); i++) {
//                                CenterListPojo summary = new CenterListPojo();
//                                JSONObject jsonObj = jsonarr.getJSONObject(i);
//                                summary.setCenterId(jsonObj.getString("centerId"));
//                                summary.setCenterName(jsonObj.getString("centername"));
//                                if (!summary.getCenterId().equals(fromlabId))
//                                    mainlist.add(summary);
//                            }
//
//                            Collections.sort(mainlist, (o1, o2) -> o1.getCenterName().compareTo(o2.getCenterName()));
//
//                            listLabDialogCreater(mainlist, TYPE);
//                        }
//                    } else if (status.equalsIgnoreCase("fail")) {
//                        Utilities.showAlertDialog(context, status, message, false);
//                    }
//                }
//            } catch (Exception e) {
//                Utilities.showAlertDialog(context, "Fail",
//                        "Server Not Responding", false);
//                e.printStackTrace();
//            }
//        }
//    }

    private class GetClientlist extends AsyncTask<String, Void, String> {

        private String TYPE = "";

        public GetClientlist(String TYPE) {
            this.TYPE = TYPE;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            Log.d("ClientList Params", Arrays.toString(strings));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("ClientCode", strings[0]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.getClientlist, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            Log.d("ClientList Resp", result);

            try {
                pd.dismiss();
                if (!result.equals("")) {


                    ClientListModel clientListModel = new Gson().fromJson(result, ClientListModel.class);
                    if (clientListModel.getStatus().equalsIgnoreCase("success")) {
                        Collections.sort(clientListModel.getOutput(), (o1, o2) -> o1.getClientName().compareTo(o2.getClientName()));
                        listClientDialog(clientListModel.getOutput(), TYPE);
                    } else
                        Utilities.showAlertDialog(context, status, message, false);

                } else {
                    Utilities.showAlertDialog(context, "Fail", "Empty response", false);

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server Not Responding", false);
                e.printStackTrace();
            }
        }
    }

    private class GetBarcodeList extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            Log.d("GetBarcodelist Params", Arrays.toString(strings));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("campdate", strings[0]));
            param.add(new ParamsPojo("camptype", strings[1]));
            param.add(new ParamsPojo("LandingLab", strings[2]));
            param.add(new ParamsPojo("campID", strings[3]));
            param.add(new ParamsPojo("Teamid", strings[4]));
            param.add(new ParamsPojo("type", strings[5]));
           // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetBarcodeList, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetSampleBarcodeListForSend, ApplicationConstants.webservice_d2d, param);


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            Log.d("GetBarcodelist Resp", result);

            try {
                pd.dismiss();
                if (!result.equals("")) {
                    BarcodeListModel barcodeListModel = new Gson().fromJson(result, BarcodeListModel.class);
                    if (barcodeListModel.getStatus().equalsIgnoreCase("success")) {
//                        Collections.sort(clientListModel.getOutput(), (o1, o2) -> o1.getClientName().compareTo(o2.getClientName()));
                        if (barcodeListModel.getOutput().size() > 0) {
                            barcodeList = new ArrayList<>();
                            selectedBarcodeList = new ArrayList<>();

//                            for (BarcodeListModel.Output o : barcodeListModel.getOutput()
//                            ) {
//                                o.setChecked(true);
//                                barcodeList.add(o);
//                            }

                            barcodeList = barcodeListModel.getOutput();
                            BarcodeListDialog(barcodeList);
                        } else {
                            Utilities.showAlertDialog(context, "No Barcodes Available", "", false);

                        }
                    } else
                        Utilities.showAlertDialog(context, "Fail", "Barcode Not Found", false);

                } else {
                    Utilities.showAlertDialog(context, "Fail", "Empty response", false);

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Data Not Found", false);
                e.printStackTrace();
            }
        }
    }

    private class GetFromOutSource extends AsyncTask<String, Void, String> {

        private String TYPE = "";


        public GetFromOutSource(String TYPE) {
            this.TYPE = TYPE;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            Log.d("GetFromOutSource", Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("SourceClientCode", params[0]));
            param.add(new ParamsPojo("DestClientCode", params[1]));
            param.add(new ParamsPojo("SourceLabCode", params[2]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetFromOutSource, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            Log.d("GetFromOutSource Resp", result);

            try {
                pd.dismiss();
                if (!result.equals("")) {

//                    JSONObject mainObj = new JSONObject(result);
//                    status = mainObj.getString("status");
//                    message = mainObj.getString("message");
//                    if (status.equalsIgnoreCase("success")) {
//                        List<CenterListPojo> mainlist = new ArrayList<CenterListPojo>();
//                        JSONArray jsonarr = mainObj.getJSONArray("output");
//                        if (jsonarr.length() > 0) {
//                            for (int i = 0; i < jsonarr.length(); i++) {
//                                CenterListPojo summary = new CenterListPojo();
//                                JSONObject jsonObj = jsonarr.getJSONObject(i);
//                                summary.setCenterId(jsonObj.getString("centerId"));
//                                summary.setCenterName(jsonObj.getString("centername"));
//                                if (!summary.getCenterId().equals(fromlabId))
//                                    mainlist.add(summary);
//                            }
//
//                            Collections.sort(mainlist, (o1, o2) -> o1.getCenterName().compareTo(o2.getCenterName()));
//
//                            listLabDialogCreater(mainlist, TYPE);
//                        }
//                    } else if (status.equalsIgnoreCase("fail")) {
//                        Utilities.showAlertDialog(context, status, message, false);
//                    }

                    LabListModel labListModel = new Gson().fromJson(result, LabListModel.class);
                    if (labListModel.getStatus().equalsIgnoreCase("success")) {
                        listLabDialogCreater(labListModel.getOutput(), TYPE);
                    } else {
                        Utilities.showAlertDialog(context, labListModel.getStatus(), labListModel.getMessage(), false);
                    }


                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Data Not Found", false);
                e.printStackTrace();
            }
        }
    }

//    private void listLabDialogCreater(final List<CenterListPojo> mainlist, String type) {
//        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setTitle("Select Lab");
//        builderSingle.setCancelable(false);
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row_city_state);
//
//        for (int i = 0; i < mainlist.size(); i++) {
//            arrayAdapter.add(String.valueOf(mainlist.get(i).getCenterName()));
//        }
//
//        builderSingle.setNegativeButton(
//                "Cancel", (dialog, which) -> dialog.dismiss());
//
//        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
//            CenterListPojo objMain = mainlist.get(which);
//
//            tv_select_runner_boy.setText("");
//            runnerBoyId = "0";
//
//            if (type.equals("1")) {
//                tv_select_courier_to_lab.setText(objMain.getCenterName());
//                toLabCode = objMain.getCenterId();
//            } else if (type.equals("2")) {
//                tv_select_processing_lab.setText(objMain.getCenterName());
//                sendcouriertolabId = objMain.getCenterId();
//            }
//        });
//        builderSingle.show();
//    }

    AlertDialog labDialog;

    private void listLabDialogCreater(List<LabListModel.Output> mainlist, String type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search_test, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        if (type.equalsIgnoreCase("1")) {
            builder.setTitle("Select To Lab");

        } else {
            builder.setTitle("Select Processing Lab");

        }
        builder.setCancelable(false);

        final RecyclerView rv_testlist = view.findViewById(R.id.rv_testlist);
        EditText edt_search = view.findViewById(R.id.edt_search);
        rv_testlist.setLayoutManager(new LinearLayoutManager(context));
        rv_testlist.setAdapter(new LabAdapter(mainlist, type));

//        tv_select_processing_lab.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {
//            }
//
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                tv_select_courier_category.setText("");
//                edt_mobile_no.setText("");
//            }
//        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rv_testlist.setAdapter(new LabAdapter(mainlist, type));
                    return;
                }

                if (mainlist.size() == 0) {
                    rv_testlist.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<LabListModel.Output> searchedTestList = new ArrayList<>();
                    for (LabListModel.Output labDetails : mainlist) {

                        String countryToBeSearched = labDetails.getLabName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(labDetails);
                        }
                    }
                    rv_testlist.setAdapter(new LabAdapter(searchedTestList, type));
                } else {
                    rv_testlist.setAdapter(new LabAdapter(mainlist, type));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setNegativeButton("cancel", (dialog, which) -> {

        });

        labDialog = builder.create();
        labDialog.show();
    }

    private void listClientDialog(List<ClientListModel.Output> mainlist, String type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search_test, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Client");
        builder.setCancelable(false);

        final RecyclerView rv_testlist = view.findViewById(R.id.rv_testlist);
        EditText edt_search = view.findViewById(R.id.edt_search);
        rv_testlist.setLayoutManager(new LinearLayoutManager(context));
        rv_testlist.setAdapter(new ClientListAdapter(mainlist, type));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rv_testlist.setAdapter(new ClientListAdapter(mainlist, type));
                    return;
                }

                if (mainlist.size() == 0) {
                    rv_testlist.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<ClientListModel.Output> searchedTestList = new ArrayList<>();
                    for (ClientListModel.Output clientDetails : mainlist) {

                        String countryToBeSearched = clientDetails.getClientName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rv_testlist.setAdapter(new ClientListAdapter(searchedTestList, type));
                } else {
                    rv_testlist.setAdapter(new ClientListAdapter(mainlist, type));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setNegativeButton("cancel", (dialog, which) -> {

        });

        labDialog = builder.create();
        labDialog.show();
    }

    private void BarcodeListDialog(List<BarcodeListModel.Output> mainlist) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search_test, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Barcodes");
        builder.setCancelable(false);

        final RecyclerView rv_testlist = view.findViewById(R.id.rv_testlist);
        EditText edt_search = view.findViewById(R.id.edt_search);
        rv_testlist.setLayoutManager(new LinearLayoutManager(context));
        rv_testlist.setAdapter(new BarcodeListAdapter(mainlist));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rv_testlist.setAdapter(new BarcodeListAdapter(mainlist));
                    return;
                }

                if (mainlist.size() == 0) {
                    rv_testlist.setVisibility(View.GONE);
                     return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<BarcodeListModel.Output> searchedTestList = new ArrayList<BarcodeListModel.Output>();
                    for (BarcodeListModel.Output clientDetails : mainlist) {

                        String countryToBeSearched = clientDetails.getBarcode().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rv_testlist.setAdapter(new BarcodeListAdapter(searchedTestList));
                } else {
                    rv_testlist.setAdapter(new BarcodeListAdapter(mainlist));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setNeutralButton("cancel", (dialog, which) -> {

        });
        builder.setPositiveButton("Okay", (dialog, which) -> {
            Log.d("Selected Barcode", new Gson().toJson(selectedBarcodeList));
            tvBarcode.setText("Total sample Barcodes " + selectedBarcodeList.size());
          //  edt_sample_count.setText(String.valueOf(selectedBarcodeList.size()));


        });


//            builder.setPositiveButton("Okay", (dialog, which) -> {
//            Log.d("Selected Barcode", new Gson().toJson(selectedBarcodeList));
//            tvBarcode.setText("Total sample Barcodes " + selectedBarcodeList.size());
//            edt_sample_count.setText(String.valueOf(selectedBarcodeList.size()));
//            int barcodeCount = 0;
////
//            for (BarcodeListModel.Output output :
//                    barcodeList) {
//                if (output.isChecked()){
//                    barcodeCount+= 1;
//
//                }
//
//            }
//
//            edt_sample_count.setText("" + barcodeCount);
//            tvBarcode.setText("Total sample Barcodes" +barcodeCount);
//
//
//        });

//        builder.setPositiveButton("Okay", (dialog, which) -> {
//            Log.d("Selected Barcode", new Gson().toJson(selectedBarcodeList));
//            int barcodeCount = 0;
//
//            for (BarcodeListModel.Output output :
//                    selectedBarcodeList) {
//                if (output.isChecked()){
//                    barcodeCount+= 1;
//
//                }
//
//            }
//
//            edt_sample_count.setText("Total sample Barcodes " +selectedBarcodeList.size());
//            //  edt_sample_count.setText(String.valueOf(selectedBarcodeList.size()));
//
//
//        });


        builder.setNeutralButton("Select All", (dialogInterface, i) -> {
            selectedBarcodeList.clear();
            for (BarcodeListModel.Output output :
                    barcodeList) {
                output.setChecked(true );

            }
            selectedBarcodeList.addAll(barcodeList);
            tvBarcode.setText("Total Barcodes " + selectedBarcodeList.size());
            edt_sample_count.setText(String.valueOf(selectedBarcodeList.size()));

        });

        labDialog = builder.create();
        labDialog.show();
    }

    private class LabAdapter extends RecyclerView.Adapter<LabAdapter.MyViewHolder> {

        private List<LabListModel.Output> labList;
        private String TYPE;

        public LabAdapter(List<LabListModel.Output> labList, String TYPE) {
            this.labList = labList;
            this.TYPE = TYPE;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_1, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int pos) {
            final int position = holder.getAdapterPosition();

            holder.tv_name.setText(labList.get(position).getLabName());

            holder.tv_name.setOnClickListener(v -> {
                tv_select_runner_boy.setText("");
                runnerBoyId = "0";

                if (TYPE.equals("1")) {
                  //  tv_select_courier_to_lab.setText(labList.get(position).getLabName());
                    toLabCode = labList.get(position).getLabCode().toString();

//                    tv_select_processing_lab.setText(labList.get(position).getLabName());
                    sendcouriertolabId = labList.get(position).getLabCode().toString();

                } else if (TYPE.equals("2")) {
//                    tv_select_processing_lab.setText(labList.get(position).getLabName());
                    sendcouriertolabId = labList.get(position).getLabCode().toString();
                }
                labDialog.dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return labList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_name = view.findViewById(R.id.tv_name);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.MyViewHolder> {

        private List<ClientListModel.Output> labList;
        private String TYPE;

        public ClientListAdapter(List<ClientListModel.Output> labList, String TYPE) {
            this.labList = labList;
            this.TYPE = TYPE;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_1, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int pos) {
            final int position = holder.getAdapterPosition();

            holder.tv_name.setText(labList.get(position).getClientName());

            holder.tv_name.setOnClickListener(v -> {
                tv_select_runner_boy.setText("");
                runnerBoyId = "0";
                tv_select_courier_to_lab.setText("");
                toLabCode = "";
//                tv_select_processing_lab.setText("");
                sendcouriertolabId = "";
                if (barcodeList != null && selectedBarcodeList != null) {
                    selectedBarcodeList.clear();
                    barcodeList.clear();
                }
                llBarcode.setVisibility(View.GONE);
                tv_select_courier_category.setText("");


//                tvDestClientList.setText(labList.get(position).getClientName());

               // destClientCode = String.valueOf(labList.get(position).getClientCode());

                labDialog.dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return labList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_name = view.findViewById(R.id.tv_name);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class BarcodeListAdapter extends RecyclerView.Adapter<BarcodeListAdapter.MyViewHolder> {

        private List<BarcodeListModel.Output> barcodeList;

        public BarcodeListAdapter(List<BarcodeListModel.Output> barcodeList) {
            this.barcodeList = barcodeList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.barcode_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int pos) {
            final int position = holder.getAdapterPosition();

            if (barcodeList.get(position).isChecked()) {
                holder.cbBarcode.setChecked(true);
            } else {
                holder.cbBarcode.setChecked(false);

            }
            holder.cbBarcode.setText(barcodeList.get(position).getBarcode());

//            holder.tv_name.setOnClickListener(v -> {
//                tv_select_runner_boy.setText("");
//                runnerBoyId = "0";
//
//                tvDestClientList.setText(barcodeList.get(position).getBarcode());
////                destClientCode = String.valueOf(labList.get(position).getClientCode());
//
//                labDialog.dismiss();
//            });

            holder.cbBarcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        selectedBarcodeList.add(barcodeList.get(position));
                        barcodeList.get(position).setChecked(b);

                    } else {
                        selectedBarcodeList.remove(barcodeList.get(position));
                        barcodeList.get(position).setChecked(b);


                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return barcodeList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;
            private CheckBox cbBarcode;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_name = view.findViewById(R.id.tv_name);
                cbBarcode = view.findViewById(R.id.cbBarcode);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class GetCourierCategory extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
           // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierCategory, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetCourierCategory, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierCategoryModel pojo = new Gson().fromJson(result, CourierCategoryModel.class);
                    type = pojo.getStatus();
                    message = pojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<CourierCategoryModel.OutputBean> categoryList = pojo.getOutput();
                        List<CourierCategoryModel.OutputBean> filteredCategoryList = new ArrayList<>();
                        if (forwardedCourierIds.equals("0")) {
                            for (CourierCategoryModel.OutputBean outputBean : categoryList) {
                                if (!outputBean.getCategoryId().equals("5")) {
                                    filteredCategoryList.add(outputBean);
                                }
                            }
                            categoryList.clear();
                            categoryList.addAll(filteredCategoryList);
                        }

                        listCourierCategoryDialog(categoryList);
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }

        }
    }

    private void listCourierCategoryDialog(List<CourierCategoryModel.OutputBean> categoryList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Courier Category");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < categoryList.size(); i++) {
            arrayAdapter.add(categoryList.get(i).getCategoryName());
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            CourierCategoryModel.OutputBean objMain = categoryList.get(which);
            categoryId = objMain.getCategoryId();
            isSampleCategory = objMain.getIsSampleType();
            tv_select_courier_category.setText(objMain.getCategoryName());


            if (objMain.getCategoryName().equalsIgnoreCase("samples")) {
                llBarcode.setVisibility(View.VISIBLE);
                tvBarcode.setText("");
                if (selectedBarcodeList != null) {
                    selectedBarcodeList.clear();
                    barcodeList.clear();
                }

            } else {
                llBarcode.setVisibility(View.GONE);

            }

            tv_select_courier_category.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    llBarcode.setVisibility(View.GONE);
                }
            });


            if (isSampleCategory.equals("1")) {
                btn_submit.setText("SAVE");
            } else {
                btn_submit.setText("SUBMIT");
            }
        });
        builderSingle.show();
    }

    private class GetCourierTransportMode extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
          //  res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierTransportMode, param);GetCourierTransportMode
            res = WebServiceCall.APICall(ApplicationConstants.GetCourierTransportMode, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierTransportModeModel pojo = new Gson().fromJson(result, CourierTransportModeModel.class);
                    type = pojo.getStatus();
                    message = pojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<CourierTransportModeModel.OutputBean> transportModeList = pojo.getOutput();
                        listTransportModeDialog(transportModeList);
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }

        }
    }

    private void listTransportModeDialog(List<CourierTransportModeModel.OutputBean> transportModeList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Transport Mode");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < transportModeList.size(); i++) {
            arrayAdapter.add(transportModeList.get(i).getModeName());
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            CourierTransportModeModel.OutputBean objMain = transportModeList.get(which);
            modeOfTransportId = objMain.getModeId();
            tv_select_mode_of_transport.setText(objMain.getModeName());

            edt_docket_no.setText("");
            edt_company_name.setText("");
            tv_select_runner_boy.setText("");
            edt_bus_no.setText("");
            edt_mobile_no.setText("");

            switch (modeOfTransportId) {
                case "1":
                    ll_courier.setVisibility(View.VISIBLE);
                    ll_runner_boy.setVisibility(View.GONE);
                    ll_bus_vehicle.setVisibility(View.GONE);
                    ll_mobile.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    ll_courier.setVisibility(View.GONE);
                    ll_runner_boy.setVisibility(View.GONE);
                    ll_bus_vehicle.setVisibility(View.VISIBLE);
                    ll_mobile.setVisibility(View.VISIBLE);
                    tv_bus_vehicle.setText(R.string.bus_no);
                    edt_bus_no.setHint("Enter Bus No.");
                    break;
                case "3":
                    ll_courier.setVisibility(View.GONE);
                    ll_runner_boy.setVisibility(View.VISIBLE);
                    ll_bus_vehicle.setVisibility(View.GONE);
                    ll_mobile.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    ll_courier.setVisibility(View.GONE);
                    ll_runner_boy.setVisibility(View.GONE);
                    ll_bus_vehicle.setVisibility(View.VISIBLE);
                    ll_mobile.setVisibility(View.VISIBLE);
                    tv_bus_vehicle.setText(R.string.vehicle_no);
                    edt_bus_no.setHint("Enter Vehicle No.");
                    break;
                default:
                    tv_select_mode_of_transport.setText("");
                    ll_courier.setVisibility(View.GONE);
                    ll_runner_boy.setVisibility(View.GONE);
                    ll_bus_vehicle.setVisibility(View.GONE);
                    ll_mobile.setVisibility(View.GONE);
                    break;
            }
        });
        builderSingle.show();
    }

    private class GetRunnerBoyOnMultipleLabcode extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("LabCode", strings[0] + "," + strings[1]));

//            param.add(new ParamsPojo("LabCode", strings[0]));
//            param.add(new ParamsPojo("DesgID", strings[1]));
           // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetRunnerBoyOnMultipleLabcode, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetRunnerBoyOnMultipleLabcode, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    RunnerBoyOnMultipleLabCodeModel pojo = new Gson().fromJson(result, RunnerBoyOnMultipleLabCodeModel.class);
                    type = pojo.getStatus();
                    message = pojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<RunnerBoyOnMultipleLabCodeModel.OutputBean> runnerBoyList = pojo.getOutput();
                        listRunnerBoyListDialog(runnerBoyList);
                    } else {
                        Utilities.showAlertDialog(context, type, "Runner Boy Details Not Found", false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }

        }
    }

    private void listRunnerBoyListDialog(List<RunnerBoyOnMultipleLabCodeModel.OutputBean> runnerBoyList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Runner Boy");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < runnerBoyList.size(); i++) {
            arrayAdapter.add(runnerBoyList.get(i).getRunnerBoyName());
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            RunnerBoyOnMultipleLabCodeModel.OutputBean objMain = runnerBoyList.get(which);
            runnerBoyId = objMain.getUserid();
            tv_select_runner_boy.setText(objMain.getRunnerBoyName());
            edt_mobile_no.setText(objMain.getMOBNO());

        });
        builderSingle.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {

//                saveFile(patientURI);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    String compressedImagePath = Utilities.compressImage(FileUtils.getPath(context, photoURI));
                    imageFile = new File(compressedImagePath);
                    photoBm = BitmapFactory.decodeFile(imageFile.toString());

                    String imagePath = imageFile.toString();

                    if (Utilities.isNetworkAvailable(context))
                        new uploadCourierImg().execute(fileName, "1", imagePath);
                    else
                        Utilities.showMessage(R.string.msg_nointernetconnection,
                                context);


                } else {
                    String compressedImagePath = Utilities.compressImage(imageFile.toString());
                    imageFile = new File(compressedImagePath);
                    photoBm = BitmapFactory.decodeFile(imageFile.toString());

                    String imagePath = imageFile.toString();

                    if (Utilities.isNetworkAvailable(context))
                        new uploadCourierImg().execute(fileName, "1", imagePath);
                    else
                        Utilities.showMessage(R.string.msg_nointernetconnection,
                                context);
                }
            } else if (requestCode == 101) {
                String requiredValue = data.getStringExtra("key");
                Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                edt_barcode.setText(requiredValue);
            }
        }
    }

    public void saveFile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";

//        String sourceFilename = Utilities.compressImage(sourceuri.getPath());
        File file = new File(imageFolder, filename);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(new FileOutputStream(file, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        photoBm = BitmapFactory.decodeFile(file.toString());
        Bitmap patientPicBm = Bitmap.createScaledBitmap(photoBm, 150, 150, false);
        imv_photo.setImageBitmap(patientPicBm);
        String imagePath = file.toString();
        if (Utilities.isNetworkAvailable(context))
            new uploadCourierImg().execute(fileName, "1", imagePath);
        else
            Utilities.showMessage(R.string.msg_nointernetconnection, context);
    }

    private class uploadCourierImg extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            dialog.setMessage("Please wait ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            try {
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.courier_handler, "UTF-8");

                multipart.addFormField("FileName", params[0]);
                multipart.addFormField("CourierType", params[1]);
                multipart.addFilePart("file", new File(params[2]));

                List<String> response = multipart.finish();
                for (String line : response) {
                    res = res + line;
                }
                return res;
            } catch (IOException ex) {
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                dialog.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    int c = 0;
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        Utilities.showMessageString(message + "!", context);

                        photoBm = Bitmap.createScaledBitmap(photoBm, 150, 150, false);
                        imv_photo.setImageBitmap(photoBm);
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                        photoBm = null;
                    }

                } else {
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
                    photoBm = null;
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
                e.printStackTrace();
                photoBm = null;
            }
        }
    }

    private class InsertCourierSentDetails_Updated extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("RegistrationLabID", params[0])
                        .add("CourierToLabID", params[1])
                        .add("ProcessLabID", params[2])
                        .add("CategoryID", params[3])
                        .add("CourierDate", params[4])
                        .add("CourierTime", params[5])
                        .add("ModeId", params[6])
                        .add("CompanyName", params[7])
                        .add("DocketNo", params[8])
                        .add("ContactNo", params[9])
                        .add("AmountPaidbySender", params[10])
                        .add("NoOfBoxes", params[11])
                        .add("RunnerBoySendUserID", params[12])
                        .add("NoOfSamples", params[13])
                        .add("ExpArrivalDate", params[14])
                        .add("ExpArrivalTime", params[15])
                        .add("Barcode", params[16])
                        .add("SampleTempID", params[17])
                        .add("PhotoPath", params[18])
                        .add("BusVehicleNumber", params[19])
                        .add("ProjectID", params[20])
                        .add("ForwerdedCourierID", params[21])
                        .add("CreatedBy", params[22])
                        .add("SampleDetails", params[23])
                        .add("BarcodeServices", params[24])
                        .add("Type_CW_CourierCampID", params[25])
                        .add("LandingLab", params[26])
                        .add("CampType", params[26])
                        .build();

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.MINUTES)
                        .writeTimeout(5, TimeUnit.MINUTES)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .build();
                String url = ApplicationConstants.webservice + ApplicationConstants.InsertCourierSentDetails_Updated;
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                res = response.body().string();

            } catch (SocketTimeoutException ste) {
                ste.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            try {
                pd.dismiss();
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("CourierSentList_Activity"));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("CourierForwardList_Activity"));

                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Courier sent successfully!");
                        alertDialog.setIcon((R.drawable.icon_success));
                        alertDialog.setButton("OK", (dialog, which) -> {
                            finish();
                        });
                        alertDialog.show();
                    } else if (status.equalsIgnoreCase("fail")) {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail", "Server not connected", false);
                e.printStackTrace();
            }
        }

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }


    public class GetLandingLab extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetUserMappedLabList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<LandingLabNewModel.Output> labList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    LandingLabNewModel landingLabNewModel = new Gson().fromJson(result, LandingLabNewModel.class);
                    type = landingLabNewModel.getStatus();
                    message = landingLabNewModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        labList = landingLabNewModel.getOutput();

                        if (labList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showLandingDialog(labList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
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

    private void showLandingDialog(final List<LandingLabNewModel.Output> labList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Landing Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < labList.size(); i++) {
            arrayAdapter.add(String.valueOf(labList.get(i).getLabName()));

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
                tvLandingLab.setText(labList.get(which).getLabName());
                selectedLabID = String.valueOf(labList.get(which).getLabCode());

                tvFromLab.setText(labList.get(which).getLabName());
                fromlabId = String.valueOf(labList.get(which).getLabCode());

                edt_barcode.setText("CMH0" + labList.get(which).getLabCode());

                tvFromLab.setEnabled(false);

                //  CampDate = labList.get(which).getCampDate();

                if (campList!=null) {
                    campList.clear();
                }


                //  refreshCalendar();
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
                tvCampType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = campTypeModelsList.get(which).getCampTypeId();



                   if (campList!= null){
                       campList.clear();

                       tvCampList.setText("");
                   }
                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }

    private void showSendCourierToLab(final ArrayList<SendCourierToLabNewModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
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
                tvSendCourierTo.setText(campTypeModelsList.get(which).getCampTypeName());
                sendcouriertolabId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());

                if (mailist!=null){
                    mailist.clear();
                    tv_select_courier_to_lab.setText("");
                }


                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }

    public class GetCampList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampDATE", params[0]));
            param.add(new ParamsPojo("CampType", params[1]));
            param.add(new ParamsPojo("LABCODE", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampListByLandingLab, ApplicationConstants.webservice_d2d, param);
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
                  //  selectedcamplist = new ArrayList<>();
                    //  Log.d("result ",result.toString());
                    CamplistOnLandingLabModel camplistOnLandingLabModel = new Gson().fromJson(result, CamplistOnLandingLabModel.class);
                    type = camplistOnLandingLabModel.getStatus();
                    message = camplistOnLandingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

//

                        if (camplistOnLandingLabModel.getOutput().size() > 0) {
                            campList.addAll(camplistOnLandingLabModel.getOutput());

                            showCampListDialog(campList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Camp I'd not available", false);
            }
        }

    }

    private void showCampListDialog(final ArrayList<CamplistOnLandingLabModel.Output> campList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select CampId");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        CampListCheckAdapter campListCheckAdapter = new CampListCheckAdapter(campList);
        rvList.setAdapter(campListCheckAdapter);

        //        rvList.addOnItemTouchListener(
        //                new RecyclerItemClickListener(context,
        //                        new RecyclerItemClickListener.OnItemClickListener() {
        //                            @Override
        //                            public void onItemClick(View view, final int position) {
        //                                GetTeamsModel.OutputBean team = teamList.get(position);
        //                                selectedTeam = new ArrayList<>();
        //                                selectedTeam.add(team);
        //                                tvTeamId.setText(selectedTeam.get(0).getTeamName());
        //                            }
        //                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new CampListCheckAdapter(campList));
                    return;
                }

                if (campList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<CamplistOnLandingLabModel.Output> searchedTestList = new ArrayList<>();
                    for (CamplistOnLandingLabModel.Output clientDetails : campList) {

                        String countryToBeSearched = clientDetails.getCampId().toString();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new CampListCheckAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new CampListCheckAdapter(campList));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        androidx.appcompat.app.AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (CamplistOnLandingLabModel.Output team : campList
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

                tvCampList.setText(String.valueOf("Selected Camp"+" "+ numOfTeams));

            }
        });


        alertDialog.show();

    }

    public class GetSendCourierToLab extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Type_CW_HomeandHublab", params[0]));
            param.add(new ParamsPojo("type", params[1]));


            res = WebServiceCall.APICall(ApplicationConstants.GetCourierToLab, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    mailist = new ArrayList<>();
                    List<SendCourierToLabModel.Output> mailist = new ArrayList<>();

                    //  Log.d("result ",result.toString());


                    SendCourierToLabModel sendCourierToLabModel = new Gson().fromJson(result, SendCourierToLabModel.class);
                    type = sendCourierToLabModel.getStatus();
                    message = sendCourierToLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        mailist = sendCourierToLabModel.getOutput();

                        if (mailist.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showCourierToLabDialog(mailist);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", " Data Not Found", false);
            }
        }
    }

    private void showCourierToLabDialog(final List<SendCourierToLabModel.Output> mainlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));

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
                tv_select_courier_to_lab.setText(String.valueOf(mainlist.get(which).getLabName()));
                toLabCode = String.valueOf(mainlist.get(which).getLabcode());
                // CampDate = mainlist.get(which).getCampDate();


                if (fromlabId == toLabCode){
                    Utilities.showToastMessage("You can't send courier to same from lab to courier to lab",context,false);
                    tv_select_courier_to_lab.setText("");
                }




                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


}


//        RegistrationLabID:11
//        CourierToLabID:34
//        ProcessLabID:60
//        CategoryID:3
//        CourierDate:2020-09-29
//        CourierTime:06:33 PM
//        ModeId:3
//        CompanyName:
//        DocketNo:
//        ContactNo:7972451204
//        AmountPaidbySender:0
//        NoOfBoxes:10
//        RunnerBoySendUserID:9325
//        NoOfSamples:10
//        ExpArrivalDate:2020-09-29
//        ExpArrivalTime:06:33 PM
//        Barcode:CMH011207890
//        SampleTempID:0
//        PhotoPath:Send_11_34_60_2414213
//        BusVehicleNumber:
//        ProjectID:1
//        ForwerdedCourierID:{"input":[{"RefCourierID":"137"}]}
//        CreatedBy:5489
//        SampleDetails:{"input":[{"TubeId":null,"TotalCount":null,"CreatedBy":null}]}


