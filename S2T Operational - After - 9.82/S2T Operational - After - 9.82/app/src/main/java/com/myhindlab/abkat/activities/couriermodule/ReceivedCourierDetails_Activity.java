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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.ReceivedCourierModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReceivedCourierDetails_Activity extends Activity implements View.OnClickListener {
    private ReceivedCourierModel courierModel;
    private TextView fromDate, txtTime, fromLab, sampleType, txt_received_time, txt_received_date, spinner_to_lab, time,
            txt_expected_date, txt_expected_time, txt_receiveddate, txt_receivedtime;
    private EditText edtCourierNo, edt_remark, edt_company_name,
            edt_sample_box, edt_sample_count, edt_sender_amount, edt_receiver_amount, edt_amount1, edt_receieved_amount, edt_bus_no, edt_mobile_no, edt_mode_of_transport;
    private Button btnAccept;
    private LinearLayout layout_accept, ll_courier, ll_bus;
    private TableRow tr_received_details;
    private String courierStatus, courierId, EmpCode, type, FacilityCode, ReceivedimagePath = "";
    private Context context;
    private int mYear, mMonth, mDay;
    private UserSessionManager session;
    private TableRow row_to_lab, row_from_lab;
    private ImageView imv_comp, imv_courier, imv_labphoto;
    private String fileName = "NA";
    private File imageFile, imageFolder;
    private Uri photoURI;
    private Bitmap photoBm = null;
    String userId,
            desgId,
            labcode,
            intentFromDate,
            toDate;
    private String imagePath = "";
    String completePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_courier_details);
        init();
        setEventHandler();
        setUpToolBar();
        setDefault();
    }

    private void init() {
        context = ReceivedCourierDetails_Activity.this;
        session = new UserSessionManager(context);
        Intent i = getIntent();
        courierModel = (ReceivedCourierModel) i.getSerializableExtra("CourierModel");
        userId = i.getStringExtra("userId");
        desgId = i.getStringExtra("desgId");
        labcode = i.getStringExtra("labcode");
        intentFromDate = i.getStringExtra("fromDate");
        toDate = i.getStringExtra("toDate");
        type = i.getStringExtra("type");
        fromDate = findViewById(R.id.txt_fromdate);
        txtTime = findViewById(R.id.txt_time);
        txt_expected_date = findViewById(R.id.txt_expected_date);
        txt_expected_time = findViewById(R.id.txt_expected_time);
        edt_receiver_amount = findViewById(R.id.edt_receiver_amount);
        fromLab = findViewById(R.id.spinner_fromlab);
        sampleType = findViewById(R.id.spinner_sample_type);
        edtCourierNo = findViewById(R.id.edt_courier_no);
        edt_remark = findViewById(R.id.edt_remark);
        edt_bus_no = findViewById(R.id.edt_bus_no);
        edt_mobile_no = findViewById(R.id.edt_mobile_no);
        edt_mode_of_transport = findViewById(R.id.edt_mode_of_transport);
        spinner_to_lab = findViewById(R.id.spinner_to_lab);
        edt_company_name = findViewById(R.id.edt_company_name);
        time = findViewById(R.id.time);
        edt_sample_count = findViewById(R.id.edt_sample_count);
        edt_sample_box = findViewById(R.id.edt_sample_box);
        txt_received_date = findViewById(R.id.txt_received_date);
        txt_received_time = findViewById(R.id.txt_received_time);

        txt_received_time = findViewById(R.id.txt_received_time);
        txt_receiveddate = findViewById(R.id.txt_receiveddate);
        txt_receivedtime = findViewById(R.id.txt_receivedtime);
        tr_received_details = findViewById(R.id.tr_received_details);
        edt_sender_amount = findViewById(R.id.edt_sender_amount);
        edt_amount1 = findViewById(R.id.edt_amount1);
        edt_receieved_amount = findViewById(R.id.edt_receieved_amount);
        btnAccept = findViewById(R.id.btn_accept);
        layout_accept = findViewById(R.id.layout_accept);
        ll_courier = findViewById(R.id.ll_courier);
        ll_bus = findViewById(R.id.ll_bus);
        row_to_lab = findViewById(R.id.row_to_lab);
        row_from_lab = findViewById(R.id.row_from_lab);
        imv_comp = findViewById(R.id.imv_comp);
        imv_courier = findViewById(R.id.imv_courier);
        courierStatus = courierModel.getCourierStatus();
        courierId = courierModel.getCourierDetailsId();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        imageFolder = new File(Environment.getExternalStorageDirectory() + "/HLL-Connect/" + "Courier");
        if (!imageFolder.exists())
            imageFolder.mkdirs();

        if (!courierStatus.equals("Courier Accepted") && !type.equals("sent")) {
            layout_accept.setVisibility(View.VISIBLE);
            edt_amount1.setClickable(false);
            edt_amount1.setLongClickable(false);
            edt_amount1.setFocusable(false);
            edt_amount1.setText(courierModel.getAmount());

        }

        if (type.equals("received")) {
            fromLab.setText(courierModel.getSentByLab());
            row_from_lab.setVisibility(View.VISIBLE);
        }
//        } else
//            time.setText("Received Time");

        if (courierModel.getSentFilePath() != null)
            ReceivedimagePath = courierModel.getSentFilePath();
//        } else {
//            if (courierModel.getReceivedFilePath() != null)
//                ReceivedimagePath = courierModel.getReceivedFilePath();
//        }
        setData(courierModel);
    }

    private void setData(ReceivedCourierModel courierModel) {
        fromDate.setText(courierModel.getCourierDate());
        tr_received_details.setVisibility(View.VISIBLE);
        txt_receivedtime.setText(courierModel.getReceivedTime());
        txt_receiveddate.setText(courierModel.getCourierRecievedDate());
        if (type.equals("received")) {
            txtTime.setText(courierModel.getCourierSentTime());
        } else {
            txtTime.setText(courierModel.getSentTime());
        }
        spinner_to_lab.setText(courierModel.getSentToLab());
        sampleType.setText(courierModel.getSampleType());
        edtCourierNo.setText(courierModel.getCourierNo());
        edt_company_name.setText(courierModel.getCourierCompany());
        edt_sample_box.setText(courierModel.getBoxCount());
        edt_sender_amount.setText(courierModel.getAmount());
        if (courierModel.getRecievedStatus().equals("1")) {
            edt_receiver_amount.setText(courierModel.getAmountPaidByReceiver());
        } else {
            edt_receiver_amount.setText("NA");
        }
        edt_remark.setText(courierModel.getRemark());
        edt_sample_count.setText(courierModel.getSampleCount());
        edt_bus_no.setText(courierModel.getBusNo());
        edt_mobile_no.setText(courierModel.getDriverMobNo());
        edt_mode_of_transport.setText(courierModel.getCourierMode());
        txt_expected_date.setText(courierModel.getExpectedArrivalDate());
        txt_expected_time.setText(courierModel.getExpectedArrivalTime());


        if (courierModel.getCourierMode().equalsIgnoreCase("by bus")) {
            ll_bus.setVisibility(View.VISIBLE);
            ll_courier.setVisibility(View.GONE);
        } else if (courierModel.getCourierMode().equalsIgnoreCase("by courier")) {
            ll_bus.setVisibility(View.GONE);
            ll_courier.setVisibility(View.VISIBLE);
        }

//        if (!ReceivedimagePath.equals("")) {
//
////            if (type.equals("sent")) {
//            if (isBeta)
//                completePath = "http://beta.hllconnect.in/HLLCONNECTERPDOCS_BETA/CourierAttachments/SentCourier/" + ReceivedimagePath;
//            else
//                completePath = "https://erp.hllconnect.in/HLLCONNECTERPDOCS/CourierAttachments/SentCourier/" + ReceivedimagePath;
////            } else {
////                if (isBeta)
////                    completePath = "http://beta.hllconnect.in/HLLCONNECTERPDOCS_BETA/CourierAttachments/ReceivedCourier/" + ReceivedimagePath;
////                else
////                    completePath = "https://erp.hllconnect.in/HLLCONNECTERPDOCS/CourierAttachments/ReceivedCourier/" + ReceivedimagePath;
////            }
//            Picasso.with(context).load(completePath).into(imv_courier);
//        }
    }

    private void setUpToolBar() {
        ImageButton back_btn = (ImageButton) findViewById(R.id.tool_leftbtn);
        TextView Title = (TextView) findViewById(R.id.tool_titile);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        if (type.equals("received"))
            Title.setText("Accept Courier");
        else
            Title.setText("Sent Courier");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setEventHandler() {
        btnAccept.setOnClickListener(this);
        txt_received_time.setOnClickListener(this);
        txt_received_date.setOnClickListener(this);
        imv_courier.setOnClickListener(this);
        imv_comp.setOnClickListener(this);
    }

    private void setDefault() {


        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(courierModel.getCourierDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                EmpCode = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept: {
                acceptData();
                break;
            }
            case R.id.txt_received_time: {
                try {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            txt_received_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(selectedHour)) + ":" + String.format("%02d", Integer.valueOf(selectedMinute))));
                        }
                    }, hour, minute, false);
                    mTimePicker.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
            case R.id.txt_received_date: {
                DatePickerDialog dpd1 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txt_received_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                    }
                }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
                break;
            }
            case R.id.imv_comp: {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }

                if (Utilities.isNetworkAvailable(context)) {

                    fileName = "Receive" + "_" + edtCourierNo.getText().toString().trim() + "_" + courierModel.getSentByLabCode() + "_" + courierModel.getRecievedByLabCode();

                    imageFile = new File(imageFolder, fileName + ".png");

                    photoURI = Uri.fromFile(imageFile);
                    Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    pickImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(pickImage, 100);
                } else
                    Toast.makeText(context, "Please check your internet connection availability", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.imv_courier: {
                if (completePath.equals("")) {
                    Utilities.showMessageString("Photo Not Available", context);
                } else {
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View promptView = layoutInflater.inflate(R.layout.prompt_facilityimg, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(promptView);

                    imv_labphoto = promptView.findViewById(R.id.imv_labphoto);
                    Picasso.with(context).load(completePath).into(imv_labphoto);
                    AlertDialog alertD = alertDialogBuilder.create();
                    alertD.show();
                }
                break;
            }
        }
    }

    private void acceptData() {
        if (txt_received_time.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select received time", context);
            return;
        }

        if (txt_received_date.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please Select received date", context);
            return;
        }

        if (photoBm == null) {
            Utilities.showMessageString("Please capture courier photo", context);
            return;
        }

        if (edt_receieved_amount.getText().toString().trim().equals("")) {
            edt_receieved_amount.setText("0");
        }

        if (Utilities.isNetworkAvailable(context)) {
            new InsertCourierReceivedDetails().execute(courierId,
                    txt_received_date.getText().toString().trim(),
                    EmpCode,
                    txt_received_time.getText().toString().trim(),
                    edt_receieved_amount.getText().toString().trim(),
                    fileName + ".png");

        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }

    public class InsertCourierReceivedDetails extends AsyncTask<String, Integer, String> {
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
            param.add(new ParamsPojo("CourierDetailsId", params[0]));
            param.add(new ParamsPojo("CourierRecievedDate", params[1]));
            param.add(new ParamsPojo("CreatedBy", params[2]));
            param.add(new ParamsPojo("ReceivedTime", params[3]));
            param.add(new ParamsPojo("Amount", params[4]));
            param.add(new ParamsPojo("ReceiveFilePath", params[5]));

            res = WebServiceCall.HLLAPICall(ApplicationConstants.InsertCourierReceivedDetailsForApp, param);
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
                    int c = 0;
                    if (status.equalsIgnoreCase("Success")) {
                        if (Utilities.isNetworkAvailable(context)) {
                            new ReceivedCourier_Activity.GetReceivedCourierDetails().execute(userId,
                                    desgId,
                                    labcode,
                                    intentFromDate,
                                    toDate);

                            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                            alertDialog.setTitle("Success");
                            alertDialog.setMessage("Courier accepted successfully!");
                            if (status != null)
                                alertDialog.setIcon((R.drawable.icon_success));
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                    finish();
                                }
                            });
                            alertDialog.show();
                        } else
                            Utilities.showMessage(R.string.msg_nointernetconnection, context);
                    } else {
                        Utilities.showAlertDialog(context, "Empty", "List is empty ...", false);
                    }
                } else {
                    Utilities.showAlertDialog(context, status, message, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                        Utilities.showMessageString(message, context);
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
                    new uploadCourierImg().execute(fileName, "2", imagePath);
                else
                    Utilities.showMessage(R.string.msg_nointernetconnection,
                            context);
            }
        }
    }

}
