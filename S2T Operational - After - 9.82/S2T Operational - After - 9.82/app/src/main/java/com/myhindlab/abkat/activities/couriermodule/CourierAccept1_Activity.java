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
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.BarcodeViewListModel;
import com.myhindlab.abkat.models.couriermodule.CourierReceivedDetailsModel;
import com.myhindlab.abkat.models.couriermodule.CourierRemarkModel;
import com.myhindlab.abkat.models.couriermodule.CourierSampleTemperatureModel;
import com.myhindlab.abkat.models.couriermodule.CourierTubeDetailsModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.FileUtils;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourierAccept1_Activity extends Activity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private TextView tv_sender_info, tv_date, tv_time, tvBarcode,tvSampleTeperature;
    private EditText edt_sample_count, edt_amount, edt_remark;
    private LinearLayout ll_tube_count, llBarcode;
    private RecyclerView rv_tube_counts;
    private ImageView imv_photo;
    private Button btn_submit;
    private List<BarcodeViewListModel.Output> barcodeList;
    private CourierReceivedDetailsModel.OutputBean selectedCourierDetails;
    private List<CourierTubeDetailsModel.OutputBean> tubeList;
    private String userId, fileName,temperatureId;
    private int mYear, mMonth, mDay, remarkId;
    private File imageFile, imageFolder;
    private Bitmap photoBm;
    private Uri photoURI;
    AlertDialog labDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_accept1);

        init();
        getSessionDetails();
        setDefaults();
        setEventListener();
        setUpToolBar();
    }

    private void init() {
        context = CourierAccept1_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        tv_sender_info = findViewById(R.id.tv_sender_info);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        edt_sample_count = findViewById(R.id.edt_sample_count);
        edt_amount = findViewById(R.id.edt_amount);
        ll_tube_count = findViewById(R.id.ll_tube_count);
        imv_photo = findViewById(R.id.imv_photo);
        btn_submit = findViewById(R.id.btn_submit);
      //  tv_remark = findViewById(R.id.tv_remark);
        edt_remark = findViewById(R.id.edt_remark);
        tvBarcode = findViewById(R.id.tvBarcode);
        llBarcode = findViewById(R.id.llBarcode);
        tvSampleTeperature = findViewById(R.id.tvSampleTeperature);
        rv_tube_counts = findViewById(R.id.rv_tube_counts);
        rv_tube_counts.setLayoutManager(new LinearLayoutManager(context));
        rv_tube_counts.setLayoutManager(new LinearLayoutManager(context));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        imageFolder = new File(Environment.getExternalStorageDirectory() + "/HLL-Connect/" + "Courier");
        if (!imageFolder.exists())
            imageFolder.mkdirs();

    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        selectedCourierDetails = (CourierReceivedDetailsModel.OutputBean) getIntent().getSerializableExtra("selectedCourierDetails");

        if (selectedCourierDetails.getIsSampleType().equals("1")) {
            new GetTubeJson().execute(selectedCourierDetails.getCourierID());
        } else
            ll_tube_count.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (selectedCourierDetails.getCategoryID().equalsIgnoreCase("1")) {
            llBarcode.setVisibility(View.VISIBLE);
        } else {
            llBarcode.setVisibility(View.GONE);
        }

        tv_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear));
        tv_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) + ":" + String.format("%02d", Integer.valueOf(calendar.get(Calendar.MINUTE)))));
    }

    private void setEventListener() {
        tv_sender_info.setOnClickListener(v -> {
            startActivity(new Intent(context, CourierAccept2_Activity.class)
                    .putExtra("selectedCourierDetails", selectedCourierDetails));
        });

        tv_date.setOnClickListener(v -> {
            DatePickerDialog dpd1 = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                try {
                    Date courierSentDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedCourierDetails.getCourierDate());
                    Date selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                    if (courierSentDate.after(selectedDate)) {
                        Utilities.showMessageString("Accept date cannot be less than sent date", context);
                        return;
                    }

                    tv_time.setText("");
                    tv_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }, mYear, mMonth, mDay);
            try {
                dpd1.getDatePicker().setCalendarViewShown(false);
                dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dpd1.show();
        });

        tv_time.setOnClickListener(v -> {
            try {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(context, (view, selectedHour, selectedMinute) -> {
                    try {
                        Date sendDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedCourierDetails.getCourierDate());
                        Date sendTime = new SimpleDateFormat("hh:mm a").parse(selectedCourierDetails.getCourierTime());
                        Date arrivalDate = new SimpleDateFormat("yyyy-MM-dd").parse(tv_date.getText().toString().trim());

                        Calendar rightNow = Calendar.getInstance();
                        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = rightNow.get(Calendar.MINUTE);

                        if (sendDate.equals(arrivalDate)) {
                            int sentHour = Integer.parseInt(new SimpleDateFormat("HH").format(sendTime));
                            int sentMinute = Integer.parseInt(new SimpleDateFormat("mm").format(sendTime));

                            if (selectedHour < sentHour) {
                                tv_time.setText("");
                                Utilities.showMessageString("Accept time should not be less than sent time", context);
                                return;
                            } else if (selectedHour == sentHour) {
                                if (selectedMinute < sentMinute) {
                                    tv_time.setText("");
                                    Utilities.showMessageString("Accept time should not be less than sent time", context);
                                    return;
                                }
                            }

                            if (selectedHour > currentHourIn24Format) {
                                tv_time.setText("");
                                Utilities.showMessageString("Accept time should not be greater than current time", context);
                                return;
                            } else if (selectedHour == currentHourIn24Format) {
                                if (selectedMinute > currentMinute) {
                                    tv_time.setText("");
                                    Utilities.showMessageString("Accept time should not be greater than current time", context);
                                    return;
                                }
                            }

                        }
                        tv_time.setText(getAmPmFrom24Hour(String.format("%02d", Integer.valueOf(selectedHour)) + ":" + String.format("%02d", Integer.valueOf(selectedMinute))));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }, hour, minute, false);

                mTimePicker.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tvBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetBarcodeList().execute(selectedCourierDetails.getCourierID());
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
        });
        tvSampleTeperature.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetSampleTemperature().execute();
            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);
            }
        });


        imv_photo.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                return;
            }

            if (Utilities.isNetworkAvailable(context)) {
                int ramdom = (int) (Math.random() * 9999999 + 1);


                fileName = "Receive" + "_" + selectedCourierDetails.getRegistrationLabID() + "_" + selectedCourierDetails.getCourierToLabID() + "_" + selectedCourierDetails.getProcessLabID() + "_" + ramdom;

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

//            if (Utilities.isNetworkAvailable(context)) {
//                int ramdom = (int) (Math.random() * 9999999 + 1);
//                fileName = "Receive" + "_" + selectedCourierDetails.getRegistrationLabID() + "_" + selectedCourierDetails.getCourierToLabID() + "_" + selectedCourierDetails.getProcessLabID() + "_" + ramdom;
//
//                imageFile = new File(imageFolder, fileName + ".png");
//
//                Uri photoURI = Uri.fromFile(imageFile);
//                Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                pickImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(pickImage, 100);
//            }
        });

        btn_submit.setOnClickListener(v -> {
            submitData();
        });

//        tv_remark.setOnClickListener(v -> {
//            if (Utilities.isNetworkAvailable(context)) {
//                new GetCourierRemarks_New().execute();
//            } else {
//                Utilities.showMessage(R.string.msgt_nointernetconnection, context);
//            }
//        });
    }

    private void submitData() {
        String acceptRemark = "";

        if (tv_date.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select date", context);
            return;
        }

        if (tv_time.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select time", context);
            return;
        }

        if (edt_sample_count.getText().toString().trim().equals("")) {
            edt_sample_count.setError("Enter Total No.of samples");
            return;
        }

        if (edt_amount.getText().toString().trim().equals("")) {
            edt_amount.setError("Enter Received Amount");
            return;
        }
        if (tvBarcode.getText().toString().trim().equals("")) {
            tvBarcode.setError("Please Select Sample Barcode");
            return;
        }

//        if (remarkId == 0) {
//            if (edt_remark.getText().toString().trim().equals("")) {
//                Utilities.showMessageString("Please enter remark", context);
//                return;
//            }
//
//            acceptRemark = edt_remark.getText().toString().trim();
//        } else {
//            acceptRemark = tv_remark.getText().toString().trim();
//        }


        if (photoBm == null) {
            Utilities.showMessageString("Please click photo", context);
            return;
        }

        JsonObject CourierDetailsByRecJsonObject = new JsonObject();
        JsonObject CourierSampleDetailsByRecJsonObject = new JsonObject();

        JsonArray CourierDetailsJsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("CourierID", selectedCourierDetails.getCourierID());
        jsonObject.addProperty("SampReceivedDate", tv_date.getText().toString().trim());
        jsonObject.addProperty("SampReceivedTime", tv_time.getText().toString().trim());
        jsonObject.addProperty("ReceivedNoOfSamples", edt_sample_count.getText().toString().trim());
        jsonObject.addProperty("AmountPaidbyRec", edt_amount.getText().toString().trim());
        jsonObject.addProperty("PhotoPathByRec", fileName);
        jsonObject.addProperty("CreatedBy", userId);
        jsonObject.addProperty("AcceptedRemark", edt_remark.getText().toString().trim());
         jsonObject.addProperty("SampleTeperature", temperatureId);
        CourierDetailsJsonArray.add(jsonObject);
        CourierDetailsByRecJsonObject.add("input", CourierDetailsJsonArray);

        int count = 0;

        if (selectedCourierDetails.getIsSampleType().equals("1")) {
            if (tubeList.size() == 0) {
                Utilities.showMessageString("Tube details not found, please reload the screen", context);
                return;
            }

            JsonArray SampleDetailsJsonArray = new JsonArray();
            for (int i = 0; i < tubeList.size(); i++) {

                TubeCountAdapter.MyViewHolder myViewHolder = (TubeCountAdapter.MyViewHolder) rv_tube_counts.findViewHolderForAdapterPosition(i);

                if (myViewHolder.edt_count.getText().toString().equals("")) {
                    myViewHolder.edt_count.setError("Enter tube count");
                    return;
                }
                count = count + Integer.parseInt(tubeList.get(i).getCount());
                JsonObject tubeDetails = new JsonObject();
                tubeDetails.addProperty("TubeId", tubeList.get(i).getTubeId());
                tubeDetails.addProperty("TotalCount", tubeList.get(i).getCount());
                tubeDetails.addProperty("CreatedBy", userId);
                SampleDetailsJsonArray.add(tubeDetails);
            }

//            if (count == 0) {
//                Utilities.showMessageString("All tube counts cannot be zero", context);
//                return;
//            }

            if (count != Integer.parseInt(edt_sample_count.getText().toString().trim())) {
                Utilities.showMessageString("Total no. of tubes should match with total addition of individual test counts", context);
                return;
            }

            CourierSampleDetailsByRecJsonObject.add("input", SampleDetailsJsonArray);
        } else {
            String TubeId = null, TotalCount = null, CreatedBy = null;
            JsonArray SampleDetailsJsonArray = new JsonArray();
            JsonObject tubeDetails = new JsonObject();
            tubeDetails.addProperty("TubeId", TubeId);
            tubeDetails.addProperty("TotalCount", TotalCount);
            tubeDetails.addProperty("CreatedBy", CreatedBy);
            SampleDetailsJsonArray.add(tubeDetails);

            CourierSampleDetailsByRecJsonObject.add("input", SampleDetailsJsonArray);
        }

        if (Utilities.isNetworkAvailable(context)) {
            new InsertDataForReceivedCourier().execute(
                    CourierDetailsByRecJsonObject.toString(),
                    CourierSampleDetailsByRecJsonObject.toString()
//                    selectedCourierDetails.getFromClient(),
//                    selectedCourierDetails.getToClient()
            );
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
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
                        new uploadCourierImg().execute(fileName, "2", imagePath);
                    else
                        Utilities.showMessage(R.string.msg_nointernetconnection,
                                context);


                } else {
                    String compressedImagePath = Utilities.compressImage(imageFile.toString());
                    imageFile = new File(compressedImagePath);
                    photoBm = BitmapFactory.decodeFile(imageFile.toString());

                    String imagePath = imageFile.toString();

                    if (Utilities.isNetworkAvailable(context))
                        new uploadCourierImg().execute(fileName, "2", imagePath);
                    else
                        Utilities.showMessage(R.string.msg_nointernetconnection,
                                context);
                }
            }
        }
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

    private class GetTubeJson extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();

            param.add(new ParamsPojo("CourierID", params[0]));
//            param.add(new ParamsPojo("FromClient", selectedCourierDetails.getFromClient()));
//            param.add(new ParamsPojo("ToClient", selectedCourierDetails.getToClient()));
          //  res = WebServiceCall.HLLAPICall(ApplicationConstants.GetTubeJson, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetTubeJson , ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierTubeDetailsModel pojoDetails = new Gson().fromJson(result, CourierTubeDetailsModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    tubeList = new ArrayList<>();

                    if (type.equalsIgnoreCase("success")) {
                        tubeList = pojoDetails.getOutput();
                        if (tubeList.size() > 0) {
                            rv_tube_counts.setAdapter(new TubeCountAdapter());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TubeCountAdapter extends RecyclerView.Adapter<TubeCountAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_tube_count_receive, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();

            holder.tv_tube_name.setText(tubeList.get(position).getTubeContent());
            holder.tv_count.setText(tubeList.get(position).getTotalCount());

            holder.edt_count.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tubeList.get(position).setCount(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.edt_count.setText(tubeList.get(position).getTotalCount());
        }

        @Override
        public int getItemCount() {
            return tubeList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_tube_name, tv_count;
            private EditText edt_count;

            MyViewHolder(final View view) {
                super(view);
                tv_tube_name = view.findViewById(R.id.tv_tube_name);
                tv_count = view.findViewById(R.id.tv_count);
                edt_count = view.findViewById(R.id.edt_count);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class InsertDataForReceivedCourier extends AsyncTask<String, Void, String> {

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
            Log.d("Receive Courier", Arrays.toString(params));
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("CourierDetailsByRec", params[0])
                        .add("CourierSampleDetailsByRec", params[1])
//                        .add("FromClient", params[2])
//                        .add("ToClient", params[3])
                        .build();

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.MINUTES)
                        .writeTimeout(5, TimeUnit.MINUTES)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .build();
                String url = "";

                    url = ApplicationConstants.webservice_d2d + ApplicationConstants.InsertDataForReceivedCourier;


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
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("CourierReceivedList_Activity"));
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Courier accepted successfully!");
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

//    private class GetCourierRemarks_New extends AsyncTask<String, Void, String> {
//
//        ProgressDialog pd;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait ...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "[]";
//            List<ParamsPojo> param = new ArrayList<>();
//           // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierRemarks_New, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetCourierRemarks_New , ApplicationConstants.webservice_d2d, param);
//
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (!result.equals("")) {
//                    CourierRemarkModel pojoDetails = new Gson().fromJson(result, CourierRemarkModel.class);
//                    type = pojoDetails.getStatus();
//                    message = pojoDetails.getMessage();
//
//                    if (type.equalsIgnoreCase("success")) {
//                        List<CourierRemarkModel.OutputBean> remarkList = pojoDetails.getOutput();
//                        if (remarkList.size() > 0) {
//                            remarkList.add(new CourierRemarkModel.OutputBean(0, "Other"));
//                            showRemarkDialog(remarkList);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(context, type, message, false);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Alert", "Server Not Responding", false);
//            }
//        }
//    }

//    private void showRemarkDialog(List<CourierRemarkModel.OutputBean> mainlist) {
//        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setTitle("Select Remark");
//
//        builderSingle.setCancelable(false);
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row_city_state);
//
//        for (int i = 0; i < mainlist.size(); i++) {
//            arrayAdapter.add(String.valueOf(mainlist.get(i).getRemarks()));
//        }
//
//        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
//
//        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
//            tv_remark.setText(mainlist.get(which).getRemarks());
//            remarkId = mainlist.get(which).getRemarkId();
//            if (mainlist.get(which).getRemarkId() == 0) {
//                edt_remark.setText("");
//                edt_remark.setVisibility(View.VISIBLE);
//            } else {
//                edt_remark.setVisibility(View.GONE);
//            }
//        });
//        builderSingle.show();
//    }

    private void setUpToolBar() {
        ImageButton back_btn = findViewById(R.id.tool_leftbtn);
        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        TextView Title = findViewById(R.id.tool_titile);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Accept Courier");
        btn_save_accordian.setVisibility(View.GONE);

        back_btn.setOnClickListener(view -> finish());
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
            param.add(new ParamsPojo("CourierID", strings[0]));
//            param.add(new ParamsPojo("Barcode", strings[1]));
//            param.add(new ParamsPojo("FromClient", selectedCourierDetails.getFromClient()));
//            param.add(new ParamsPojo("ToClient", selectedCourierDetails.getToClient()));
          //  res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierBarcodeServicedetail, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetCourierBarcodeServicedetail , ApplicationConstants.webservice_d2d, param);
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
                    BarcodeViewListModel barcodeListModel = new Gson().fromJson(result, BarcodeViewListModel.class);
                    if (barcodeListModel.getStatus().equalsIgnoreCase("success")) {
//                        Collections.sort(clientListModel.getOutput(), (o1, o2) -> o1.getClientName().compareTo(o2.getClientName()));
                        if (barcodeListModel.getOutput().size() > 0) {
                            barcodeList = new ArrayList<>();

                            barcodeList = barcodeListModel.getOutput();
                            tvBarcode.setText("Total Barcodes " + barcodeList.size());
                            BarcodeListDialog(barcodeList);
                        } else {
                            Utilities.showAlertDialog(context, "No Barcodes Available", "", false);

                        }
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

    private void BarcodeListDialog(List<BarcodeViewListModel.Output> mainlist) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search_test, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Barcode Included");
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
                    ArrayList<BarcodeViewListModel.Output> searchedTestList = new ArrayList<BarcodeViewListModel.Output>();
                    for (BarcodeViewListModel.Output clientDetails : mainlist) {

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

//        builder.setNeutralButton("cancel", (dialog, which) -> {
//
//        });
        builder.setPositiveButton("Okay", (dialog, which) -> {
            dialog.dismiss();


        });


        labDialog = builder.create();
        labDialog.show();
    }

    private class BarcodeListAdapter extends RecyclerView.Adapter<BarcodeListAdapter.MyViewHolder> {

        private List<BarcodeViewListModel.Output> barcodeList;

        public BarcodeListAdapter(List<BarcodeViewListModel.Output> barcodeList) {
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

            holder.cbBarcode.setVisibility(View.GONE);
            holder.tv_name.setVisibility(View.VISIBLE);
//            if (barcodeList.get(position).isChecked()) {
//                holder.cbBarcode.setChecked(true);
//            } else {
//                holder.cbBarcode.setChecked(false);
//
//            }
           // holder.tv_name.setText(barcodeList.get(position).getSampleBarcode() + " (" + barcodeList.get(position).getServiceName() + ")");
            holder.tv_name.setText(barcodeList.get(position).getSampleBarcode());

//            holder.tv_name.setOnClickListener(v -> {
//                tv_select_runner_boy.setText("");
//                runnerBoyId = "0";
//
//                tvDestClientList.setText(barcodeList.get(position).getBarcode());
////                destClientCode = String.valueOf(labList.get(position).getClientCode());
//
//                labDialog.dismiss();
//            });

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


    private class GetSampleTemperature extends AsyncTask<String, Void, String> {

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
            // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetSampleTemperature, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetSampleTemperature, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierSampleTemperatureModel pojo = new Gson().fromJson(result, CourierSampleTemperatureModel.class);
                    type = pojo.getStatus();
                    message = pojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<CourierSampleTemperatureModel.OutputBean> temperatureList = pojo.getOutput();
                        listTemperatureDialog(temperatureList);
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

    private void listTemperatureDialog(List<CourierSampleTemperatureModel.OutputBean> temperatureList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Sample Temperature");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < temperatureList.size(); i++) {
            arrayAdapter.add(temperatureList.get(i).getSampleTempName());
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            CourierSampleTemperatureModel.OutputBean objMain = temperatureList.get(which);
            temperatureId = objMain.getSampleTempID();
            tvSampleTeperature.setText(objMain.getSampleTempName());
        });
        builderSingle.show();
    }




}