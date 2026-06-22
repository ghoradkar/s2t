package com.myhindlab.abkat.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.myhindlab.abkat.utilities.Utilities.isLocationEnabled;
import static com.myhindlab.abkat.utilities.Utilities.provideLocationAccess;
import static com.myhindlab.abkat.utilities.Utilities.turnOnLocation;

public class PatientAttendance_Activity extends AppCompatActivity implements AuthBfdCap, MFS100Event {

    private Context context;
    private ProgressDialog pd;
    private String TAG = getClass().getName();

    private String campId, registrationNo = "";
    private ImageView imv_search, iv_biomatric;
    private RadioButton rb_mr, rb_mrs, rb_ms;
    private MaterialEditText edt_mcobcwwbno, edt_name, edt_gender, edt_age, edt_aadhaarno, edt_moblieno,
            edt_dob, edt_address, edt_city, edt_pincode;
    private TextView tv_capturetext;
    private Button btn_markattendance;
    private String RegId = "", EmpCode, LATITUDE = "0", LONGITUDE = "0", isThumbExist = "0";
    private UserSessionManager session;
    private File folder;

    private String fingerImage = "",
            registerTemp = "";
    private boolean isFingerPrint = false;

    String deviceMan = android.os.Build.MANUFACTURER;
    private boolean isCaptureRunning = false;
    private boolean isCaptured = false;
    MFS100 mfs100 = null;
    ScannerAction scannerAction = ScannerAction.Capture;
    int timeout = 10000;

    byte[] Enroll_Template;
    private FingerData lastCapFingerData = null;

    private static long mLastClkTime = 0;
    private static long Threshold = 1500;

    private boolean isFingerPrintNotUploaded = true;

    private enum ScannerAction {
        Capture, Verify
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_attendance);

        init();
        setUpToolbar();
        setDefault();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = PatientAttendance_Activity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);
        edt_mcobcwwbno = findViewById(R.id.edt_mcobcwwbno);
        edt_name = findViewById(R.id.edt_name);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_aadhaarno = findViewById(R.id.edt_aadhaarno);
        edt_moblieno = findViewById(R.id.edt_moblieno);
        edt_dob = findViewById(R.id.edt_dob);
        edt_address = findViewById(R.id.edt_address);
        edt_city = findViewById(R.id.edt_city);
        edt_pincode = findViewById(R.id.edt_pincode);
        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        imv_search = findViewById(R.id.imv_search);
        iv_biomatric = findViewById(R.id.iv_biomatric);
        tv_capturetext = findViewById(R.id.tv_capturetext);
        btn_markattendance = findViewById(R.id.btn_markattendance);
    }

    private void setDefault() {
        folder = new File(Environment.getExternalStorageDirectory(),
                "/Health Checkup/ThumbData/");
        if (!folder.exists()) {
            // Make it, if it doesn't exit
            folder.mkdirs();
        }

//        ConstructionSitesList_Model siteDetails = new ConstructionSitesList_Model();
//        siteDetails = (ConstructionSitesList_Model) getIntent().getSerializableExtra("siteDetails");

        campId = getIntent().getStringExtra("campId");
        registrationNo = getIntent().getStringExtra("registrationNo");

        if (registrationNo != null && !registrationNo.equalsIgnoreCase("")) {
            if (Utilities.isNetworkAvailable(context)) {
                new GetWorkerInfroFromWorkerRegid().execute(registrationNo);
                btn_markattendance.setEnabled(false);
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
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
                    btn_markattendance.setEnabled(false);
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        btn_markattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }

//                if (!isFingerPrint) {
//                    Utilities.showToastMessage("Please capture patient finger print.", context, false);
//                    return;
//                }

                submitData();
//                InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "2", EmpCode,
//                        folder.toString() + "/" + registerTemp);
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
                    if (isFingerPrintNotUploaded)
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

    private void submitData() {
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            provideLocationAccess(context);
        } else {
            if (!isLocationEnabled(context)) {
                turnOnLocation(context);
            } else {
                startLocationUpdates();
            }
        }

        if (edt_mcobcwwbno.getText().toString().trim().isEmpty()) {
            edt_mcobcwwbno.setError("Please enter worker registration number");
            return;
        }

        if (isFingerPrintNotUploaded) {
            Utilities.showAlertDialog(context, "Alert", "Please upload fingerprint of beneficiary", false);
            return;
        }

        if (deviceMan.equalsIgnoreCase("Hena")) {
            if (Utilities.isNetworkAvailable(context)) {
                new InsertUserAttendanceForConstructionWorker().execute(
                        RegId,
                        LATITUDE,
                        LONGITUDE,
                        EmpCode,
                        "0",
                        campId);
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        } else {
            if (isCaptured) {
                File file = new File(folder, registerTemp);
                InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "2", EmpCode,
                        file.toString());
            } else {
                if (Utilities.isNetworkAvailable(context)) {
                    new InsertUserAttendanceForConstructionWorker().execute(
                            RegId,
                            LATITUDE,
                            LONGITUDE,
                            EmpCode,
                            "0",
                            campId);
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        }

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
                    ArrayList<PatientDetailsOnRegNo_Model> patientDetailsList = new ArrayList<>();
                    PatientDetailsOnRegNo_Pojo pojoDetails = new Gson().fromJson(result, PatientDetailsOnRegNo_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        patientDetailsList = pojoDetails.getOutput();
                        if (patientDetailsList.size() > 0) {
                            PatientDetailsOnRegNo_Model patientDetails = patientDetailsList.get(patientDetailsList.size() - 1);
                            isThumbExist = patientDetails.getIsThumbExist();
                            if (isThumbExist.equalsIgnoreCase("1")) {
                                iv_biomatric.setVisibility(View.GONE);
                                tv_capturetext.setVisibility(View.GONE);
                                isFingerPrintNotUploaded = false;
                            } else {
                                iv_biomatric.setVisibility(View.VISIBLE);
                                tv_capturetext.setVisibility(View.VISIBLE);
                                isFingerPrintNotUploaded = true;
                            }

                            iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));
                            isWorking = false;
                            isCaptureRunning = false;
                            isCaptured = false;

                            edt_mcobcwwbno.setText(patientDetails.getRegdNo());

                            if (patientDetails.getTitle().equalsIgnoreCase("Mr.")) {
                                rb_mr.setChecked(true);
                            } else if (patientDetails.getTitle().equalsIgnoreCase("Mrs.")) {
                                rb_mrs.setChecked(true);
                            } else if (patientDetails.getTitle().equalsIgnoreCase("Ms.")) {
                                rb_ms.setChecked(true);
                            }

                            if (patientDetails.getGender().equalsIgnoreCase("M")) {
                                edt_gender.setText("Male");
                            } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
                                edt_gender.setText("Female");
                            }

                            edt_name.setText(patientDetails.getEnglishName());

                            edt_aadhaarno.setText(patientDetails.getUID().replace("-", ""));

                            edt_dob.setText(patientDetails.getDOBFormated());

                            edt_age.setText(patientDetails.getAge());

//                            if (edt_age.getText().toString().isEmpty()) {
//                                String age = patientDetails.getDOBFormated();
//                                String[] ageparts = age.split("-");
//                                String year = ageparts[0];
//                                String month = ageparts[1];
//                                String day = ageparts[2];
//
//                                edt_age.setText(getAge(Integer.parseInt(year),
//                                        Integer.parseInt(month),
//                                        Integer.parseInt(day)));
//                            }

                            edt_moblieno.setText(patientDetails.getMobileNo());

                            edt_address.setText(patientDetails.getPermanentAddress());

                            edt_city.setText(patientDetails.getLocation());

                            edt_pincode.setText(patientDetails.getPincode());

                            RegId = patientDetails.getRegdId();
                            btn_markattendance.setEnabled(true);
                            isFingerPrint = false;
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

    public class InsertUserAttendanceForConstructionWorker extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("LATITUDE", params[1]));
            param.add(new ParamsPojo("LONGITUDE", params[2]));
            param.add(new ParamsPojo("ATTENDANCEMARKBY", params[3]));
            param.add(new ParamsPojo("SiteDetailId", params[4]));
            param.add(new ParamsPojo("CampId", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertUserAttendanceForConstructionWorker, ApplicationConstants.webservice, param);
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
                        builder.setMessage("Worker attendance marked successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mark Patient Attendance");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//////////////////////////////////////////LOCATION////////////////////////////////////////////////////////////

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LatLng latLng;

    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LATITUDE = String.valueOf(latLng.latitude);
        LONGITUDE = String.valueOf(latLng.longitude);
    }

    public static String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);

        return ageInt.toString();
    }

//////////////////////////////////////////MORPH FINGETPRINT////////////////////////////////////////////////////////////

    private static MorphoTabletFPSensorDevice fpSensorCap;

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            provideLocationAccess(context);
        } else {
            if (!isLocationEnabled(context)) {
                turnOnLocation(context);
            } else {
                startLocationUpdates();
            }
        }

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
                mfs100.SetApplicationContext(PatientAttendance_Activity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (mfs100 == null) {
                    mfs100 = new MFS100(this);
                    mfs100.SetApplicationContext(PatientAttendance_Activity.this);
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

//                if (imgPreview != null) {
//                    imgPreview.setImageBitmap(previewBitmap);
//                }

                if (captureError == ErrorCodes.MORPHOERR_TIMEOUT) {
                    Utilities.showToastMessage("Capture Timeout", context, false);
//                    tost = Toast.makeText(getApplicationContext(), "Capture Timeout", Toast.LENGTH_SHORT);
//                    tost.show();

                    setButtonEnabled(true);
                    Log.e(this.getClass().toString(), "Capture Timeout ErrorCodes = " + captureError);
                    return;

                } else if (captureError == ErrorCodes.MORPHOERR_CMDE_ABORTED) {
                    Log.e(this.getClass().toString(), "MORPHOERR_CMDE_ABORTED ErrorCodes = " + captureError);
                    setButtonEnabled(true);
                    return;
                }

                if (flagComplete && captureError == ErrorCodes.MORPHO_OK) {
//                    Utilities.showToastMessage("Thumb saved successfully", context, true);
                    setButtonEnabled(true);
//                    saveToDb(fpSensorCap.templateBuffer);

                    if (storeImage(fingerImage, previewBitmap)) {
                        if (DumpFile(registerTemp, fpSensorCap.templateBuffer)) {

//                            Bitmap bmp = BitmapFactory.decodeFile(folder.toString() + "/" + fingerImage);
//                            bmp = Bitmap.createScaledBitmap(bmp, 150, 150, false);
//                            iv_biomatric.setImageBitmap(bmp);
                            File file = new File(folder, registerTemp);

                            InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "2", EmpCode,
                                    file.toString());
                        }
                    }
                }
            }
        });
    }

    private boolean storeImage(String fileName, Bitmap image) {
        File file = new File(folder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            return false;
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

//        RequestBody reraID_body = RequestBody.create(MediaType.parse("text/plain"), reraID);
//        RequestBody siteDetailId_body = RequestBody.create(MediaType.parse("text/plain"), siteDetailId);
//        RequestBody userID_body = RequestBody.create(MediaType.parse("text/plain"), userID);
//        RequestBody fileType_body = RequestBody.create(MediaType.parse("text/plain"), fileType);
//        RequestBody builderID_body = RequestBody.create(MediaType.parse("text/plain"), builderID);
//
//        Call<InsertConstructionWorkDetailsResponse> call = apiService.InsertConstructionWorkDetails(reraID_body, siteDetailId_body, userID_body,
//                fileType_body, builderID_body, imageFile);

        Call<InsertFingerPrintDetailsResponse> call = apiService.InsertFingerPrintDetails("1", regId, fileType,
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
//                                Bitmap bmp = BitmapFactory.decodeFile(filePath);
                                Bitmap bmp = BitmapFactory.decodeFile(fPath.getAbsolutePath());
                                bmp = Bitmap.createScaledBitmap(bmp, 150, 150, false);
                                iv_biomatric.setImageBitmap(bmp);
                                Utilities.showToastMessage("Finger print uploaded successfully", context, true);
                                isFingerPrint = true;
                                isWorking = false;
                                isFingerPrintNotUploaded = false;
                                if (!deviceMan.equalsIgnoreCase("Hena")) {
                                    if (Utilities.isNetworkAvailable(context)) {
                                        new InsertUserAttendanceForConstructionWorker().execute(
                                                RegId,
                                                LATITUDE,
                                                LONGITUDE,
                                                EmpCode,
                                                "0",
                                                campId);
                                    } else {
                                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                    }
                                }
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

    private long mLastAttTime = 0l;

    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {

        if (SystemClock.elapsedRealtime() - mLastAttTime < Threshold) {
            return;
        }
        mLastAttTime = SystemClock.elapsedRealtime();
        int ret;
        if (!hasPermission) {
            SetTextOnUIThread("Permission denied");
            return;
        }
        try {
            if (vid == 1204 || vid == 11279) {
                if (pid == 34323) {
                    ret = mfs100.LoadFirmware();
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        SetTextOnUIThread("Load firmware success");
                    }
                } else if (pid == 4101) {
                    String key = "Without Key";
                    ret = mfs100.Init();
                    if (ret == 0) {
                        showSuccessLog(key);
                    } else {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuccessLog(String key) {
        try {
            SetTextOnUIThread("Init success");
            String info = "\nKey: " + key + "\nSerial: "
                    + mfs100.GetDeviceInfo().SerialNo() + " Make: "
                    + mfs100.GetDeviceInfo().Make() + " Model: "
                    + mfs100.GetDeviceInfo().Model()
                    + "\nCertificate: " + mfs100.GetCertification();
            SetTextOnUIThread(info);
        } catch (Exception e) {
        }
    }

    long mLastDttTime = 0l;

    @Override
    public void OnDeviceDetached() {
        try {

            if (SystemClock.elapsedRealtime() - mLastDttTime < Threshold) {
                return;
            }
            mLastDttTime = SystemClock.elapsedRealtime();
            UnInitScanner();

            SetTextOnUIThread("Device removed");
        } catch (Exception e) {
        }
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            SetTextOnUIThread(err);
            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetTextOnUIThread("Init success");
                String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
//                SetLogOnUIThread(info);
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Init failed, unhandled exception",
                    Toast.LENGTH_LONG).show();
            SetTextOnUIThread("Init failed, unhandled exception");
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                SetTextOnUIThread("Uninit Success");
                SetTextOnUIThread("Uninit Success");
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
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, false);
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        lastCapFingerData = fingerData;

                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        PatientAttendance_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_biomatric.setImageBitmap(bitmap);
                            }
                        });

//                        Log.e("RawImage", Base64.encodeToString(fingerData.RawData(), Base64.DEFAULT));
//                        Log.e("FingerISOTemplate", Base64.encodeToString(fingerData.ISOTemplate(), Base64.DEFAULT));
                        SetTextOnUIThread("Capture Success");
                        SetData2(fingerData, bitmap);
                    }
                } catch (Exception ex) {
                    SetTextOnUIThread("Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    public void SetData2(FingerData fingerData, Bitmap bitmap) {
        try {
            isFingerPrintNotUploaded = false;
            WriteFileRaw(registerTemp, fingerData.ISOTemplate());
            WriteFileImage(fingerImage, bitmap);

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
            fos.close();
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

        } catch (IOException e) {
            e.printStackTrace();
            isCaptured = false;
            Utilities.showToastMessage("File write failed: " + e.toString(), context, true);
        }
    }

    private void SetTextOnUIThread(String capture_success) {
//        Toast.makeText(context, capture_success, Toast.LENGTH_SHORT).show();
    }


}
