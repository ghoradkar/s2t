package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.fingerprint.dao.AuthBfdCap;
import com.myhindlab.abkat.fingerprint.fps.MorphoTabletFPSensorDevice;
import com.myhindlab.abkat.models.PatientDetailsOnRegNo_Model;
import com.myhindlab.abkat.pojos.InsertFingerPrintDetailsResponse;
import com.myhindlab.abkat.pojos.PatientDetailsOnRegNo_Pojo;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.rest.HandlerClient;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFingerPrint_Activity extends AppCompatActivity implements AuthBfdCap, MFS100Event {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private String TAG = getClass().getName();

    private CardView cv_info;
    private MaterialEditText edt_mcobcwwbno;
    private ImageView imv_search, iv_biomatric;
    private TextView tv_name, tv_address, tv_mobile, tv_agegender, tv_city;

    private File folder;

    private String EmpCode, RegId = "", fingerImage = "", registerTemp = "", resigNo;

    private String deviceMan = android.os.Build.MANUFACTURER;
    private boolean isCaptureRunning = false, isCaptured = false, isFingerPrint = false;
    private MFS100 mfs100 = null;
    private ScannerAction scannerAction = ScannerAction.Capture;
    private int timeout = 10000;

    private byte[] Enroll_Template;
    private FingerData lastCapFingerData = null;

    private static long mLastClkTime = 0;
    private static long Threshold = 1500;

    private enum ScannerAction {
        Capture, Verify
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_upload);

        init();
        setUpToolbar();
        setDefault();
        getSessionData();
        setEventHandler();

    }

    private void init() {
        context = UploadFingerPrint_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        cv_info = findViewById(R.id.cv_info);
        edt_mcobcwwbno = findViewById(R.id.edt_mcobcwwbno);
        imv_search = findViewById(R.id.imv_search);
        iv_biomatric = findViewById(R.id.iv_biomatric);
        tv_name = findViewById(R.id.tv_name);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_address = findViewById(R.id.tv_address);
        tv_agegender = findViewById(R.id.tv_agegender);
        tv_city = findViewById(R.id.tv_city);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        folder = new File(Environment.getExternalStorageDirectory(), "/Health Checkup/ThumbData/");
        if (!folder.exists())
            folder.mkdirs();

    }

    private void setDefault() {
        resigNo = getIntent().getStringExtra("resigNo");

        edt_mcobcwwbno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_mcobcwwbno.getText().toString().trim().length() == 12) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetWorkerInfroFromWorkerRegid().execute(edt_mcobcwwbno.getText().toString().trim());
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                }
            }
        });


        if (resigNo != null) {
            edt_mcobcwwbno.setText(resigNo);
        }

    }

    private void getSessionData() {

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

    private void setEventHandler() {
        imv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_mcobcwwbno.getText().toString().isEmpty()) {
                    edt_mcobcwwbno.setError("Please enter worker registration number");
                    edt_mcobcwwbno.requestFocus();
                    return;
                }

                if (Utilities.isNetworkAvailable(context)) {
                    new GetWorkerInfroFromWorkerRegid().execute(edt_mcobcwwbno.getText().toString().trim());
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        iv_biomatric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegId.equalsIgnoreCase("")) {
                    edt_mcobcwwbno.setError("Please search patient first");
                    edt_mcobcwwbno.requestFocus();
                    return;
                }

                if (deviceMan.equalsIgnoreCase("Hena")) {
                    setDetaminiParams();
                    if (isWorking) {
                        Utilities.showToastMessage("Please let process to complete first", context, false);
                    } else {
                        fingerImage = RegId + "_1_" + EmpCode + "_fingerImage.png";
                        registerTemp = RegId + "_2_" + EmpCode + "_registerTemp.raw";

                        capture();
                        Utilities.showToastMessage("Place your finger on the sensor", context, false);
                    }
                } else {

                    fingerImage = RegId + "_1_" + EmpCode + "_fingerImage.png";
                    registerTemp = RegId + "_2_" + EmpCode + "_registerTemp.iso";

                    Utilities.showToastMessage("Place your finger on the sensor", context, false);

                    scannerAction = ScannerAction.Capture;
                    if (!isCaptureRunning) {
                        StartSyncCapture();
                    }
                }
            }
        });


    }

    public class GetWorkerInfroFromWorkerRegid extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("EmpCode", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetWorkerInfroFromWorkerRegid, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<PatientDetailsOnRegNo_Model> patientDetails = new ArrayList<>();
                    PatientDetailsOnRegNo_Pojo pojoDetails = new Gson().fromJson(result, PatientDetailsOnRegNo_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        patientDetails = pojoDetails.getOutput();
                        if (patientDetails.size() > 0) {
                            cv_info.setVisibility(View.VISIBLE);
                            String isThumbExist = patientDetails.get(0).getIsThumbExist();
                            if (isThumbExist.equalsIgnoreCase("1")) {
                                iv_biomatric.setVisibility(View.GONE);
                            } else {
                                iv_biomatric.setVisibility(View.VISIBLE);
                            }

                            tv_name.setText(patientDetails.get(0).getEnglishName());
                            tv_address.setText("Address - " + patientDetails.get(0).getLocalAddress());
                            tv_mobile.setText("Mobile - " + patientDetails.get(0).getMobileNo());

                            String gender = "";

                            if (patientDetails.get(0).getGender().equalsIgnoreCase("M")) {
                                gender = "Male";
                            } else if (patientDetails.get(0).getGender().equalsIgnoreCase("F")) {
                                gender = "Female";
                            }

                            tv_agegender.setText("Age - " + patientDetails.get(0).getAge() + "    Gender - " + gender);
                            tv_city.setText("Pincode" +
                                    " - " + patientDetails.get(0).getPincode());

                            iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));

                            RegId = patientDetails.get(0).getRegdId();
                            isFingerPrint = false;
                            isCaptureRunning = false;
                            isCaptured = false;

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                            isFingerPrint = true;

                            edt_mcobcwwbno.setText("");
                            cv_info.setVisibility(View.GONE);
                            tv_name.setText("");
                            tv_address.setText("");
                            tv_mobile.setText("");
                            tv_agegender.setText("");
                            tv_city.setText("");

                            isCaptureRunning = false;
                            isCaptured = false;
                            isFingerPrint = false;
                            iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                        isFingerPrint = true;

                        edt_mcobcwwbno.setText("");
                        cv_info.setVisibility(View.GONE);
                        tv_name.setText("");
                        tv_address.setText("");
                        tv_mobile.setText("");
                        tv_agegender.setText("");
                        tv_city.setText("");

                        isCaptureRunning = false;
                        isCaptured = false;
                        isFingerPrint = false;
                        iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

                isFingerPrint = true;

                edt_mcobcwwbno.setText("");
                cv_info.setVisibility(View.GONE);
                tv_name.setText("");
                tv_address.setText("");
                tv_mobile.setText("");
                tv_agegender.setText("");
                tv_city.setText("");

                isCaptureRunning = false;
                isCaptured = false;
                isFingerPrint = false;
                iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fingerprint Upload");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//////////////////////////////////////////MORPH FINGETPRINT////////////////////////////////////////////////////////////

    private static MorphoTabletFPSensorDevice fpSensorCap;

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("deviceMan", deviceMan);
        if (deviceMan.equalsIgnoreCase("Hena")) {
            setDetaminiParams();
        }
    }

    @Override
    protected void onStart() {
        if (!deviceMan.equalsIgnoreCase("Hena")) {

            try {
                mfs100 = new MFS100(this);
                mfs100.SetApplicationContext(UploadFingerPrint_Activity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (mfs100 == null) {
                    mfs100 = new MFS100(this);
                    mfs100.SetApplicationContext(UploadFingerPrint_Activity.this);
                } else {
                    InitScanner();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onStart();
    }

    protected void onStop() {

        if (!deviceMan.equalsIgnoreCase("Hena")) {
            try {
                if (isCaptureRunning) {
                    int ret = mfs100.StopAutoCapture();
                }
                Thread.sleep(500);
                //            UnInitScanner();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fpSensorCap != null) {
            fpSensorCap.cancelLiveAcquisition();
            fpSensorCap.release();
        }
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fpSensorCap != null) {
            fpSensorCap.release();
        }

        if (!deviceMan.equalsIgnoreCase("Hena")) {
            try {
                if (mfs100 != null) {
                    mfs100.Dispose();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        if (tost != null)
//            tost.cancel();
    }

    private void setDetaminiParams() {
        fpSensorCap = new MorphoTabletFPSensorDevice(this);
        Log.i("initFP", "Object Created");

        int i = fpSensorCap.open(this);
//        if (i == 0) {
//            if (registrationNo != null && !registrationNo.equalsIgnoreCase("")) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetWorkerInfroFromWorkerRegid().execute(registrationNo);
//                    btn_markattendance.setEnabled(false);
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                }
//            }
//        }
//        else {
//            Utilities.showAlertDialog(context, "Alert",
//                    "Finger print scanner is not detected, Please remove all connected cables, go back and try again.",
//                    false, "OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//        }
        Log.i("initFP", "Opened");

        fpSensorCap.setViewToUpdate(iv_biomatric);
    }

    private void capture() {
        try {
            fpSensorCap.startCapture();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().toString(), "capture", e);
        }
    }

    private boolean isWorking = false;

    public void setButtonEnabled(boolean enabled) {
        isWorking = !enabled;
        iv_biomatric.setEnabled(enabled);
    }

    @Override
    public void updateImageView(final ImageView imgPreview,
                                final Bitmap previewBitmap,
                                final String message,
                                final boolean flagComplete,
                                final int captureError) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (captureError == ErrorCodes.MORPHOERR_TIMEOUT) {
                    Utilities.showToastMessage("Capture Timeout", context, false);
                    setButtonEnabled(true);
                    Log.e(this.getClass().toString(), "Capture Timeout ErrorCodes = " + captureError);
                    return;

                } else if (captureError == ErrorCodes.MORPHOERR_CMDE_ABORTED) {
                    Log.e(this.getClass().toString(), "MORPHOERR_CMDE_ABORTED ErrorCodes = " + captureError);
                    setButtonEnabled(true);
                    return;
                }

                if (flagComplete && captureError == ErrorCodes.MORPHO_OK) {
                    setButtonEnabled(true);

                    storeImage(fingerImage, previewBitmap);
                }
            }
        });
    }

    private void storeImage(String fileName, Bitmap image) {
        File file = new File(folder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "1", EmpCode,
                    file.toString());

        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private boolean DumpFile(String fileName, byte[] buffer) {
        File file = new File(folder, fileName);
        // String imgString = Base64.encodeToString(buffer, Base64.NO_WRAP);
        // Save your stream, don't forget to flush() it before closing it.
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(buffer);
            bos.flush();
            bos.close();

//            Utilities.showToastMessage("File write success to : " + file, context, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.showToastMessage("File write failed: " + e.toString(), context, true);
            return false;
        }
    }

    private void InsertConstructionWorkDetailsImage_HandlerAPICall(String regId, final String fileType,
                                                                   String createdBy, final String filePath) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait . . .");
        progressDialog.show();

        ApiInterface apiService = HandlerClient.getClient().create(ApiInterface.class);

        final File fPath = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fPath);
        MultipartBody.Part imageFile = MultipartBody.Part.createFormData("file", fPath.getName(), requestBody);

        Call<InsertFingerPrintDetailsResponse> call = apiService.InsertFingerPrint("1", regId, fileType,
                createdBy, imageFile);
        call.enqueue(new Callback<InsertFingerPrintDetailsResponse>() {
            @Override
            public void onResponse(Call<InsertFingerPrintDetailsResponse> call, Response<InsertFingerPrintDetailsResponse> response) {
                progressDialog.dismiss();

                int surverCode = response.code();
                String surverMessage = response.message();

                if (surverCode == ApplicationConstants.OKSUCCESS) {
                    try {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (status.equalsIgnoreCase("Success")) {

                            if (fileType.equalsIgnoreCase("2")) {
                                File file = new File(folder, fingerImage);

                                InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "1", EmpCode,
                                        file.toString());

                            } else {
                                Bitmap bmp = BitmapFactory.decodeFile(fPath.getAbsolutePath());
                                bmp = Bitmap.createScaledBitmap(bmp, 150, 150, false);
                                iv_biomatric.setImageBitmap(bmp);

                                Utilities.showToastMessage("Finger print uploaded successfully", context, true);
                                isFingerPrint = true;

                                edt_mcobcwwbno.setText("");
                                cv_info.setVisibility(View.GONE);
                                tv_name.setText("");
                                tv_address.setText("");
                                tv_mobile.setText("");
                                tv_agegender.setText("");
                                tv_city.setText("");

                                isCaptureRunning = false;
                                isCaptured = false;
                                isFingerPrint = false;
                                iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));

                            }
                        } else {
                            Utilities.showAlertDialog(context, status, message, false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context,
                                "Alert", "Exception " + e.toString(), false);
                    }
                } else {
                    Utilities.showAlertDialog(context,
                            "Alert", "Server returns : " + surverCode + " " + surverMessage, false);
                }
            }

            @Override
            public void onFailure(Call<InsertFingerPrintDetailsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                Utilities.showAlertDialog(context,
                        "Alert", t.toString(), false);
            }
        });
    }

//////////////////////////////////////////MANTRA FINGETPRINT////////////////////////////////////////////////////////////

    private long mLastAttTime = 0l, mLastDttTime = 0l;

    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {

        if (SystemClock.elapsedRealtime() - mLastAttTime < Threshold) {
            return;
        }
        mLastAttTime = SystemClock.elapsedRealtime();
        int ret;
        if (!hasPermission) {
            return;
        }
        try {
            if (vid == 1204 || vid == 11279) {
                if (pid == 34323) {
                    ret = mfs100.LoadFirmware();
                    if (ret != 0) {
                    } else {
                    }
                } else if (pid == 4101) {
                    String key = "Without Key";
                    ret = mfs100.Init();
                    if (ret == 0) {
                        showSuccessLog(key);
                    } else {
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuccessLog(String key) {
        try {
            String info = "\nKey: " + key + "\nSerial: "
                    + mfs100.GetDeviceInfo().SerialNo() + " Make: "
                    + mfs100.GetDeviceInfo().Make() + " Model: "
                    + mfs100.GetDeviceInfo().Model()
                    + "\nCertificate: " + mfs100.GetCertification();
        } catch (Exception e) {
        }
    }

    @Override
    public void OnDeviceDetached() {
        try {

            if (SystemClock.elapsedRealtime() - mLastDttTime < Threshold) {
                return;
            }
            mLastDttTime = SystemClock.elapsedRealtime();
            UnInitScanner();
        } catch (Exception e) {
        }
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
            } else {
                String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
//                SetLogOnUIThread(info);
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Init failed, unhandled exception",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
            } else {
                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }

    private void StartSyncCapture() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, false);
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                    } else {
                        lastCapFingerData = fingerData;

                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        UploadFingerPrint_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_biomatric.setImageBitmap(bitmap);
                            }
                        });
                        SetData2(fingerData, bitmap);
                    }
                } catch (Exception ex) {
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    public void SetData2(FingerData fingerData, Bitmap bitmap) {
        try {
            WriteFileImage(fingerImage, bitmap);
//            WriteFileRaw(registerTemp, fingerData.ISOTemplate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void WriteFileImage(String fingerImage, Bitmap bitmap) {
        File file = new File(folder, fingerImage);
        try {
            isCaptured = true;
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "1", EmpCode,
                            file.toString());
                }
            });
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            isCaptured = false;
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            isCaptured = true;
        }
    }

    private void WriteFileRaw(String filename, byte[] rawData) {
        File file = new File(folder, filename);
        isCaptured = true;
        // String imgString = Base64.encodeToString(buffer, Base64.NO_WRAP);
        // Save your stream, don't forget to flush() it before closing it.
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(rawData);
            bos.flush();
            bos.close();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "2", EmpCode,
                            file.toString());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            isCaptured = false;
            Utilities.showToastMessage("File write failed: " + e.toString(), context, true);
        }

    }
}
