package com.myhindlab.abkat.activities.couriermodule;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;
import static com.myhindlab.abkat.utilities.Utilities.getAmPmFrom24Hour;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;


import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.CenterListPojo;
import com.myhindlab.abkat.models.couriermodule.CourierRemarkModel;
import com.myhindlab.abkat.models.couriermodule.CourierSampleTypeModel;
import com.myhindlab.abkat.models.couriermodule.HLLDCWiseLab;
import com.myhindlab.abkat.models.couriermodule.LBMWiseLab;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SendCourier_Activity extends Activity implements View.OnClickListener {
    private TextView txt_fromdate, tv_fromlab, txt_time, spinner_fromlab, spinner_send_to_lab, sampleType, tv_FromLab, tv_send_to_lab,
            tv_date, tv_courier_no, tv_time, tv_communication_mode, tv_courier_company_name, tv_box, tv_sample, tv_amount,
            tv_bus_no, tv_mobile_no, tv_sample_type, tv_remark, tv_expected_date, tv_expected_time, txt_expected_date, txt_expected_time;
    private EditText edtCourierNo, edt_remark, edt_company_name,
            edt_sample_box, edt_sample_count, edt_amount, edt_bus_no, edt_mobile_no;
    private RadioGroup rg_communication_mode;
    private RadioButton rb_courier, rb_bus;
    private LinearLayout ll_bus, ll_courier;
    private Button btnUpload, btnSubmit;
    private UserSessionManager session;
    private Context context;
    private int mYear, mMonth, mDay;
    private ProgressDialog progressDialog;
    List<LBMWiseLab> mainlist;
    private String EmpCode, fromLabId, toLabId, FacilityCode, labName;
    boolean selectAll = true;
    private ImageView imv_comp;
    private String fileName = "NA", DESIGID;
    private File imageFile, imageFolder;
    private Uri photoURI;
    private Bitmap photoBm = null;
    private String imagePath = "";
    private CheckBox cb_select_all;
    private List<CourierSampleTypeModel.OutputBean> sampleTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_courier);
        init();
        setUpToolBar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = SendCourier_Activity.this;
        session = new UserSessionManager(context);
        progressDialog = new ProgressDialog(context);
        txt_fromdate = findViewById(R.id.txt_fromdate);
        txt_time = findViewById(R.id.txt_time);
        tv_fromlab = findViewById(R.id.tv_fromlab);
        spinner_fromlab = findViewById(R.id.spinner_fromlab);
        spinner_send_to_lab = findViewById(R.id.spinner_send_to_lab);
        sampleType = findViewById(R.id.spinner_sample_type);
//        btnUpload = findViewById(R.id.btn_upload);
        btnSubmit = findViewById(R.id.btn_submit);
        edtCourierNo = findViewById(R.id.edt_courier_no);
        tv_FromLab = findViewById(R.id.tv_FromLab);
        edt_remark = findViewById(R.id.edt_remark);
        edt_company_name = findViewById(R.id.edt_company_name);
        edt_sample_count = findViewById(R.id.edt_sample_count);
        edt_sample_box = findViewById(R.id.edt_sample_box);
        edt_amount = findViewById(R.id.edt_amount);
        edt_bus_no = findViewById(R.id.edt_bus_no);
        edt_mobile_no = findViewById(R.id.edt_mobile_no);
        imv_comp = findViewById(R.id.imv_comp);
        tv_send_to_lab = findViewById(R.id.tv_send_to_lab);
        tv_date = findViewById(R.id.tv_date);
        tv_courier_no = findViewById(R.id.tv_courier_no);
        tv_time = findViewById(R.id.tv_time);
        tv_courier_company_name = findViewById(R.id.tv_courier_company_name);
        tv_box = findViewById(R.id.tv_box);
        tv_sample = findViewById(R.id.tv_sample);
        tv_communication_mode = findViewById(R.id.tv_communication_mode);
        tv_amount = findViewById(R.id.tv_amount);
        tv_bus_no = findViewById(R.id.tv_bus_no);
        tv_mobile_no = findViewById(R.id.tv_mobile_no);
        tv_sample_type = findViewById(R.id.tv_sample_type);
        tv_remark = findViewById(R.id.tv_remark);
        tv_expected_date = findViewById(R.id.tv_expected_date);
        tv_expected_time = findViewById(R.id.tv_expected_time);
        txt_expected_date = findViewById(R.id.txt_expected_date);
        txt_expected_time = findViewById(R.id.txt_expected_time);
        ll_bus = findViewById(R.id.ll_bus);
        ll_courier = findViewById(R.id.ll_courier);
        rg_communication_mode = findViewById(R.id.rg_communication_mode);
        rb_courier = findViewById(R.id.rb_courier);
        rb_bus = findViewById(R.id.rb_bus);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }


        imageFolder = new File(Environment.getExternalStorageDirectory() + "/HLL-Connect/" + "Courier");
        if (!imageFolder.exists())
            imageFolder.mkdirs();

        sampleTypeList = new ArrayList<>();
    }

    private void setDefaults() {
        tv_FromLab.setText(Html.fromHtml("<font color='#000000'>From Lab </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_send_to_lab.setText(Html.fromHtml("<font color='#000000'>Send To Lab</font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_date.setText(Html.fromHtml("<font color='#000000'>Date </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_time.setText(Html.fromHtml("<font color='#000000'>Time </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_expected_date.setText(Html.fromHtml("<font color='#000000'>Expected Arrival Date </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_expected_time.setText(Html.fromHtml("<font color='#000000'>Expected Arrival Time </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_courier_no.setText(Html.fromHtml("<font color='#000000'>Courier No. </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_courier_company_name.setText(Html.fromHtml("<font color='#000000'>Courier company name </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_box.setText(Html.fromHtml("<font color='#000000'>Total No. of box </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_sample.setText(Html.fromHtml("<font color='#000000'>Total No. of sample </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_amount.setText(Html.fromHtml("<font color='#000000'>Amount (₹)</font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_bus_no.setText(Html.fromHtml("<font color='#000000'>Bus No.</font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_mobile_no.setText(Html.fromHtml("<font color='#000000'>Driver Mobile No. </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_sample_type.setText(Html.fromHtml("<font color='#000000'>Sample Type </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_remark.setText(Html.fromHtml("<font color='#000000'>Remark </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        tv_communication_mode.setText(Html.fromHtml("<font color='#000000'>Select Mode of Transport </font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>"));
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        txt_fromdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));
        txt_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) + ":" + String.format("%02d", Integer.valueOf(calendar.get(Calendar.MINUTE)))));

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                EmpCode = json.getString("EmpCode");
                labName = json.getString("LabName");
                fromLabId = json.getString("MaplabCode");
                DESIGID = json.getString("DESGID");
                spinner_fromlab.setText(labName);
                tv_fromlab.setText(labName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DESIGID.equals("27") || DESIGID.equals("36") || DESIGID.equals("38") || DESIGID.equals("78") || DESIGID.equals("11") ||
                DESIGID.equals("12") || DESIGID.equals("18")) {
            spinner_fromlab.setVisibility(View.GONE);
            tv_fromlab.setVisibility(View.VISIBLE);
        } else if (DESIGID.equals("10") || DESIGID.equals("23")) {
            spinner_fromlab.setVisibility(View.VISIBLE);
            tv_fromlab.setVisibility(View.GONE);
        }

        edt_mobile_no.setText("0");
    }

    private void setUpToolBar() {
        ImageButton back_btn = (ImageButton) findViewById(R.id.tool_leftbtn);
        TextView Title = (TextView) findViewById(R.id.tool_titile);
        ImageButton btn_save_accordian = (ImageButton) findViewById(R.id.btn_save_accordian);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Send courier");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_save_accordian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SentCourier_Activity.class);
                context.startActivity(intent);
            }
        });
    }

    private void setEventHandler() {
        txt_fromdate.setOnClickListener(this);
        txt_time.setOnClickListener(this);
        txt_expected_date.setOnClickListener(this);
        txt_expected_time.setOnClickListener(this);
//        edt_remark.setOnClickListener(this);
        spinner_fromlab.setOnClickListener(this);
        spinner_send_to_lab.setOnClickListener(this);
        sampleType.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        imv_comp.setOnClickListener(this);

        rg_communication_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_courier:
                        ll_bus.setVisibility(View.GONE);
                        ll_courier.setVisibility(View.VISIBLE);
                        edtCourierNo.setText("");
                        edt_company_name.setText("");
                        edt_bus_no.setText("");
                        edt_mobile_no.setText("0");
                        break;
                    case R.id.rb_bus:
                        ll_bus.setVisibility(View.VISIBLE);
                        ll_courier.setVisibility(View.GONE);
                        edtCourierNo.setText("");
                        edt_company_name.setText("");
                        edt_bus_no.setText("");
                        edt_mobile_no.setText("");
                        break;
                }
            }
        });
    }

    public class GetLBMLabsDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
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
                progressDialog.dismiss();
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        mainlist = new ArrayList<>();
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
                            listDialogCreater(mainlist);
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

    private void listLabDialogCreater(final List<CenterListPojo> mainlist) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getCenterName()));
        }

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CenterListPojo objMain = mainlist.get(which);
                spinner_send_to_lab.setText(objMain.getCenterName());
                toLabId = objMain.getCenterId();
            }
        });
        builderSingle.show();
    }

    public class GetCenterList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("UserID", EmpCode));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.getCenterList, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            try {
                progressDialog.dismiss();
                if (!result.equals("")) {

                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        List<CenterListPojo> mainlist = new ArrayList<CenterListPojo>();
                        JSONArray jsonarr = mainObj.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {
                                CenterListPojo summary = new CenterListPojo();
                                JSONObject jsonObj = jsonarr.getJSONObject(i);
                                summary.setCenterId(jsonObj.getString("centerId"));
                                summary.setCenterName(jsonObj.getString("centername"));
                                mainlist.add(summary);
                            }

                            Collections.sort(mainlist, new Comparator<CenterListPojo>() {
                                @Override
                                public int compare(CenterListPojo o1, CenterListPojo o2) {
                                    return o1.getCenterName().compareTo(o2.getCenterName());
                                }
                            });

                            listLabDialogCreater(mainlist);
                        }
                    } else if (status.equalsIgnoreCase("fail")) {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server Not Responding", false);
                e.printStackTrace();
            }
        }
    }

    private void listDialogCreater(final List<LBMWiseLab> mainlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));
        }

        builderSingle.setNegativeButton(
                "Cancel",
                (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            LBMWiseLab objMain = mainlist.get(which);
            spinner_fromlab.setText(objMain.getLabName());
            fromLabId = objMain.getLabcode();
        });
        builderSingle.show();
    }

    public class GetDCToLabMapping extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
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
                progressDialog.dismiss();
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

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                HLLDCWiseLab objMain = mainlist.get(which);
                spinner_fromlab.setText(objMain.getLabName());
                fromLabId = objMain.getLabCode();

            }
        });
        builderSingle.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_comp: {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }

                if (Utilities.isNetworkAvailable(context)) {

                    fileName = "Send" + "_" + edtCourierNo.getText().toString().trim() + "_" + fromLabId + "_" + toLabId;

                    imageFile = new File(imageFolder, fileName + ".png");

                    photoURI = Uri.fromFile(imageFile);
                    Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    pickImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(pickImage, 100);
                } else
                    Toast.makeText(context, "Please check your internet connection availability", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.txt_fromdate:
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        (view, year, monthOfYear, dayOfMonth) -> txt_fromdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year)), mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
                break;

            case R.id.txt_time: {
                try {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> txt_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(selectedHour)) + ":" + String.format("%02d", Integer.valueOf(selectedMinute)))), hour, minute, false);
                    mTimePicker.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case R.id.txt_expected_date:
                DatePickerDialog dpd = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                    txt_expected_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                    txt_expected_time.setText("");
                }, mYear, mMonth, mDay);
                try {
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                    dpd.getDatePicker().setCalendarViewShown(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd.show();
                break;

            case R.id.txt_expected_time: {
                try {

                    if (txt_fromdate.getText().toString().trim().isEmpty()) {
                        Utilities.showMessageString("Please select courier send date", context);
                        return;
                    }

                    if (txt_expected_date.getText().toString().trim().isEmpty()) {
                        Utilities.showMessageString("Please select expected arrival date", context);
                        return;
                    }

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            try {
                                Date sendDate = new SimpleDateFormat("yyyy/MM/dd").parse(txt_fromdate.getText().toString().trim());
                                Date expectedArrivalDate = new SimpleDateFormat("yyyy/MM/dd").parse(txt_expected_date.getText().toString().trim());

                                if (sendDate.equals(expectedArrivalDate)) {
                                    Calendar rightNow = Calendar.getInstance();
                                    int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                                    int currentMinute = rightNow.get(Calendar.MINUTE);

                                    if (selectedHour < currentHourIn24Format) {
                                        Utilities.showMessageString("Expected arrival time should not be less than current time", context);
                                        return;
                                    } else if (selectedHour == currentHourIn24Format) {
                                        if (selectedMinute < currentMinute) {
                                            Utilities.showMessageString("Expected arrival time should not be less than current time", context);
                                            return;
                                        }
                                    }
                                    txt_expected_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(selectedHour)) + ":" + String.format("%02d", Integer.valueOf(selectedMinute))));
                                } else {
                                    txt_expected_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(selectedHour)) + ":" + String.format("%02d", Integer.valueOf(selectedMinute))));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, hour, minute, false);
                    mTimePicker.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case R.id.spinner_fromlab: {
                if (Utilities.isNetworkAvailable(context)) {
                    if (DESIGID.equals("23")) {
                        new GetLBMLabsDetails().execute(EmpCode);
                    } else if (DESIGID.equals("10")) {
                        new GetDCToLabMapping().execute(EmpCode);
                    }
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
                break;
            }

            case R.id.spinner_send_to_lab: {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetCenterList().execute(EmpCode);
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
                break;
            }

            case R.id.edt_remark: {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetCourierRemarks().execute();
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
                break;
            }

            case R.id.btn_submit: {
                submitData();
                break;
            }

            case R.id.spinner_sample_type: {
                if (sampleTypeList.size() == 0) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetCourierSampleType().execute();
                    } else {
                        Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                    }
                } else {
                    showSampleTypeDialog();
                }
                break;
            }
        }
    }

    private class GetCourierSampleType extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierSampleType, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    sampleTypeList = new ArrayList<>();
                    CourierSampleTypeModel pojoDetails = new Gson().fromJson(result, CourierSampleTypeModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (type.equalsIgnoreCase("success")) {
                        sampleTypeList = pojoDetails.getOutput();
                        if (sampleTypeList.size() > 0) {
                            showSampleTypeDialog();
                        }
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", "Server Not Responding", false);
            }
        }
    }

    private class GetCourierRemarks extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<>();
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierRemarks, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<CourierRemarkModel.OutputBean> remarkList = new ArrayList<>();
                    CourierRemarkModel pojoDetails = new Gson().fromJson(result, CourierRemarkModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (type.equalsIgnoreCase("success")) {
                        remarkList = pojoDetails.getOutput();
                        if (remarkList.size() > 0) {
                            showRemarkDialog(remarkList);
                        }
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", "Server Not Responding", false);
            }
        }
    }

    private void showRemarkDialog(List<CourierRemarkModel.OutputBean> mainlist) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Remark");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getRemarks()));
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                edt_remark.setText(mainlist.get(which).getRemarks());
            }
        });
        builderSingle.show();
    }

    private void showSampleTypeDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_listnew, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Sample Type");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        cb_select_all = view.findViewById(R.id.cb_select_all);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));
        rv_checklist.setAdapter(new SampleTypeListAdapter());

        boolean areAllSamplesChecked = true;

        for (int i = 0; i < sampleTypeList.size(); i++) {
            if (!sampleTypeList.get(i).isChecked()) {
                areAllSamplesChecked = false;
                break;
            }
        }

        cb_select_all.setChecked(areAllSamplesChecked);

        cb_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_select_all.isChecked())
                    for (int i = 0; i < sampleTypeList.size(); i++)
                        sampleTypeList.get(i).setChecked(true);
                else
                    for (int i = 0; i < sampleTypeList.size(); i++)
                        sampleTypeList.get(i).setChecked(false);

                rv_checklist.setAdapter(new SampleTypeListAdapter());
            }
        });

        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sampleType.setText("");

                StringBuilder selectedSample = new StringBuilder();

                for (CourierSampleTypeModel.OutputBean sample : sampleTypeList) {
                    if (sample.isChecked()) {
                        selectedSample.append(sample.getSampleType()).append(", ");
                    }
                }

                if (selectedSample.toString().length() != 0) {
                    String selectedSampleStr = selectedSample.substring(0, selectedSample.toString().length() - 2);
                    sampleType.setText(selectedSampleStr);
                }
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    private class SampleTypeListAdapter extends RecyclerView.Adapter<SampleTypeListAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(sampleTypeList.get(position).getSampleType());

            if (sampleTypeList.get(position).isChecked()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    sampleTypeList.get(position).setChecked(isChecked);

                    boolean areAllSamplesChecked = true;

                    for (int i = 0; i < sampleTypeList.size(); i++) {
                        if (!sampleTypeList.get(i).isChecked()) {
                            areAllSamplesChecked = false;
                            break;
                        }
                    }

                    cb_select_all.setChecked(areAllSamplesChecked);
                }
            });
        }

        @Override
        public int getItemCount() {
            return sampleTypeList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private EditText edt_samplecount;
            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
               // edt_samplecount = view.findViewById(R.id.edt_samplecount);
                edt_samplecount.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private void submitData() {

        if (spinner_fromlab.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select From Lab", context);
            return;
        }

        if (spinner_send_to_lab.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select To Lab", context);
            return;
        }

        if (txt_fromdate.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select Date", context);
            return;
        }

        if (txt_time.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select Time", context);
            return;
        }

        if (rb_courier.isChecked()) {
            if (edtCourierNo.getText().toString().trim().equals("")) {
                Utilities.showMessageString("Please Enter Courier No.", context);
                return;
            }

            if (edt_company_name.getText().toString().trim().equals("")) {
                Utilities.showMessageString("Please Enter Courier Company Name", context);
                return;
            }
        }

        if (rb_bus.isChecked()) {
            if (edt_bus_no.getText().toString().trim().equals("")) {
                Utilities.showMessageString("Please Enter Bus No.", context);
                return;
            }

            if (!Utilities.isValidMobileno(edt_mobile_no.getText().toString().trim())) {
                Utilities.showMessageString("Please Enter Valid Mobile No.", context);
                return;
            }
        }

        if (edt_sample_box.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Enter No. of Box", context);
            return;
        }

        if (edt_sample_count.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Enter Total no.of Sample ", context);
            return;
        }

        if (Integer.parseInt(edt_sample_box.getText().toString()) > Integer.parseInt(edt_sample_count.getText().toString())) {
            Utilities.showAlertDialog(context, "Alert", "Total number of sample boxes cannot be greater than total number of samples", false);
            return;
        }

        if (edt_amount.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Enter Amount", context);
            return;
        }

        if (sampleType.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select Sample Type", context);
            return;
        }

        if (edt_remark.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Enter Remark", context);
            return;
        }

        if (txt_expected_date.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select Expected Arrival Date", context);
            return;
        }

        if (txt_expected_time.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select Expected Arrival Time", context);
            return;
        }

        if (photoBm == null) {
            Utilities.showMessageString("Please Capture Courier Photo", context);
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new InsertCourierSentDetails().execute(
                    edt_remark.getText().toString().trim(),
                    fromLabId,
                    toLabId,
                    edtCourierNo.getText().toString().trim(),
                    edt_company_name.getText().toString().trim(),
                    edt_sample_count.getText().toString().trim(),
                    txt_fromdate.getText().toString().trim(),
                    EmpCode,
                    txt_time.getText().toString().trim(),
                    edt_sample_box.getText().toString().trim(),
                    edt_amount.getText().toString().trim(),
                    edt_bus_no.getText().toString().trim(),
                    edt_mobile_no.getText().toString().trim(),
                    sampleType.getText().toString().trim(),
                    fileName + ".png",
                    txt_expected_date.getText().toString().trim(),
                    txt_expected_time.getText().toString().trim()
            );
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }

    public class InsertCourierSentDetails extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Remark", params[0]));
            param.add(new ParamsPojo("SendFromLabCode", params[1]));
            param.add(new ParamsPojo("SendToLab", params[2]));
            param.add(new ParamsPojo("CourierNumber", params[3]));
            param.add(new ParamsPojo("CommpanyName", params[4]));
            param.add(new ParamsPojo("SampleCount", params[5]));
            param.add(new ParamsPojo("CourierSendDate", params[6]));
            param.add(new ParamsPojo("CreatedBy", params[7]));
            param.add(new ParamsPojo("CourierSentTime", params[8]));
            param.add(new ParamsPojo("SampleBox", params[9]));
            param.add(new ParamsPojo("Amount", params[10]));
            param.add(new ParamsPojo("BusNumber", params[11]));
            param.add(new ParamsPojo("DriverMobileNumber", params[12]));
            param.add(new ParamsPojo("SampleType", params[13]));
            param.add(new ParamsPojo("SentFilePath", params[14]));
            param.add(new ParamsPojo("ExpectedArrivalDate", params[15]));
            param.add(new ParamsPojo("ExpectedArrivalTime", params[16]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.InsertCourierSentDetailsForApp_New, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                dialog.dismiss();
                JSONObject obj1 = new JSONObject(result);
                String status = obj1.getString("status");
                String message = obj1.getString("message");
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    if (status.equalsIgnoreCase("Success")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Courier sent successfully!");
                        alertDialog.setIcon((R.drawable.icon_success));
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                finish();
                            }
                        });
                        alertDialog.show();
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    Utilities.showAlertDialog(context, status, message, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                String compressedImagePath = compressImage(imageFile.toString());
                imageFile = new File(compressedImagePath);
                photoBm = BitmapFactory.decodeFile(imageFile.toString());
                photoBm = Bitmap.createScaledBitmap(photoBm, 150, 150, false);
                imv_comp.setImageBitmap(photoBm);

                imagePath = imageFile.toString();

                if (Utilities.isNetworkAvailable(context))
                    new uploadCourierImg().execute(fileName, "1", imagePath);
                else
                    Utilities.showMessage(R.string.msg_nointernetconnection,
                            context);
            }
        }
    }

    public class uploadCourierImg extends AsyncTask<String, Integer, String> {
        File image;
        ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                dialog.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    int c = 0;
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
//                        if (image.exists()) {
//                            Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
//                            vProfileImage.setImageBitmap(myBitmap);
                        Utilities.showMessageString(message + "!", context);
//                        }
                    } else
                        Utilities.showAlertDialog(context,
                                status, message, false);

                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }

}
