package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.morpho.morphosmart.sdk.Template;
import com.morpho.morphosmart.sdk.TemplateType;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.fingerprint.dao.AuthBfdCap;
import com.myhindlab.abkat.fingerprint.fps.MorphoTabletFPSensorDevice;
import com.myhindlab.abkat.models.GetFingerPrintPathResponce;
import com.myhindlab.abkat.models.GetFingerPrintPath_Model;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthScreeningAcknowledgement_Activity extends AppCompatActivity implements AuthBfdCap, MFS100Event {

    private Context context;
    private UserSessionManager session;
    private String TAG = getClass().getName();
    private ProgressDialog pd;
    private MaterialEditText edt_beneficiaryname, edt_gender, edt_age, edt_height, edt_weight;
    private PresentPatientList_Model patientDetails;
    private String userID, name, campId, healthScreentype, IsDeviceAvilaible,fingerPrintPath;
    private SignaturePad signature_pad;
    private Button btn_clearpad, btn_savesignature;
    private TextView tv_thumbstatus;
    private ImageView iv_biomatric;
    private File signatureImageFile, patientSignFolder, downloadThumbFolder, verifyThumbFolder;

    private String downloadThumbFile = "", verifyThumbFile = "";
    private String fingerImage = "";
    private boolean isFingerPrintVerified = false;

    String deviceMan = android.os.Build.MANUFACTURER;
    private boolean isCaptureRunning = false;
    MFS100 mfs100 = null;
    ScannerAction scannerAction = ScannerAction.Verify;
    int timeout = 10000;

    byte[] Enroll_Template;
    private FingerData lastCapFingerData = null;

    private static long mLastClkTime = 0;
    private static long Threshold = 1500;

    private enum ScannerAction {
        Capture, Verify
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_acknowledgement);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = HealthScreeningAcknowledgement_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        signature_pad = findViewById(R.id.signature_pad);

        iv_biomatric = findViewById(R.id.iv_biomatric);
        tv_thumbstatus = findViewById(R.id.tv_thumbstatus);

        btn_clearpad = findViewById(R.id.btn_clearpad);
        btn_savesignature = findViewById(R.id.btn_savesignature);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            patientSignFolder = getExternalCacheDir();

        } else {
            patientSignFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Signature/");
            if (!patientSignFolder.exists())
                patientSignFolder.mkdirs();
        }
    }

    private void setDefaults() {
        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");
        healthScreentype = getIntent().getStringExtra("healthScreentype");
        IsDeviceAvilaible = getIntent().getStringExtra("IsDeviceAvilaible");

        fingerPrintPath = getIntent().getStringExtra("fingerPrintPath");

        Log.d("TAG","finger Print Path" + fingerPrintPath);


        edt_beneficiaryname.setText(patientDetails.getEnglishName());
        edt_age.setText(patientDetails.getAge());
//        edt_height.setText(String.valueOf(patientDetails.getHeightCMs()));
//        edt_weight.setText(String.valueOf(patientDetails.getWeightKGs()));

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            edt_gender.setText("Male");
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            edt_gender.setText("Female");
        } else if (patientDetails.getGender().equalsIgnoreCase("O")) {
            edt_gender.setText("Other");
        }

        downloadThumbFolder = new File(Environment.getExternalStorageDirectory(),
                "/Health Checkup/ThumbData/download");
        if (!downloadThumbFolder.exists()) {
            // Make it, if it doesn't exit
            downloadThumbFolder.mkdirs();
        }

        verifyThumbFolder = new File(Environment.getExternalStorageDirectory(),
                "/Health Checkup/ThumbData/verify");
        if (!verifyThumbFolder.exists()) {
            // Make it, if it doesn't exit
            verifyThumbFolder.mkdirs();
        }

        if (patientDetails.getRegdId() != null && !String.valueOf(patientDetails.getRegdId()).equalsIgnoreCase("")) {
            if (Utilities.isNetworkAvailable(context)) {
                GetFingerPrintPathAPICall(String.valueOf(patientDetails.getRegdId()));
            } else {
                Utilities.showAlertDialog(context, "Alert", "Please check your internet connection",
                        false, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
            }
        }
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                name = json.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {

        signature_pad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                btn_clearpad.setEnabled(true);
                btn_savesignature.setEnabled(true);
            }

            @Override
            public void onClear() {
                btn_clearpad.setEnabled(false);
                btn_savesignature.setEnabled(false);
            }
        });

        btn_clearpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_pad.clear();
            }
        });

        iv_biomatric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadThumbFile.equalsIgnoreCase("") || downloadThumbFile == null) {
                    Utilities.showAlertDialog(context, "Alert", "We did not find your thumb, Please go back and try again",
                            false, "Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    return;
                }

                File thumb = new File(downloadThumbFile);
                if (!thumb.exists()) {
                    Utilities.showAlertDialog(context, "Alert", "We did not find your thumb, Please go back and try again",
                            false, "Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    return;
                }


                if (deviceMan.equalsIgnoreCase("Hena")) {
                    if (isWorking) {
                        Utilities.showToastMessage("Please let process to complete first", context, false);
                    } else {
                        fingerImage = patientDetails.getRegdId() + "_1_" + userID + "_fingerImage.png";
                        verifyThumbFile = patientDetails.getRegdId() + "_2_" + userID + "_registerTemp.raw";

                        setButtonEnabled(false);
                        capture();
                        Utilities.showToastMessage("Place your finger on the sensor", context, false);
                    }
                } else {
                    fingerImage = patientDetails.getRegdId() + "_1_" + userID + "_fingerImage.png";
                    verifyThumbFile = patientDetails.getRegdId() + "_2_" + userID + "_registerTemp.raw";

                    setButtonEnabled(false);
                    Utilities.showToastMessage("Place your finger on the sensor", context, false);

                    scannerAction = ScannerAction.Capture;
                    if (!isCaptureRunning) {
                        StartSyncCapture();
                    }
                }
            }
        });

        btn_savesignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!isFingerPrintVerified) {
//                    Utilities.showAlertDialog(context, "Alert", "Verify patient thumb first.",
//                            false, "Ok",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            });
//                    return;
//                }
                Bitmap signatureBitmap = signature_pad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                } else {
                    Toast.makeText(context, "Unable to store the signature btn_savesignature", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        signatureImageFile = new File(patientSignFolder,
                patientDetails.getRegdId() + "_0_" + campId + "_SG.png");

        try {
            saveBitmapToJPG(signature, signatureImageFile);
            scanMediaFile(signatureImageFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        stream.close();

        if (Utilities.isNetworkAvailable(context)) {
            new SignatureHandler().execute(

                    String.valueOf(patientDetails.getRegdId()),
                    String.valueOf(patientDetails.getSiteId()),
                    campId,
                    "1",
                    userID,
                    IsDeviceAvilaible,
                    photo.toString(),
                    fingerPrintPath
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class SignatureHandler extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please Wait . . .");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            try {
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.CWPatientSignhandlerNew3, "UTF-8");

                multipart.addFormField("RegdId", params[0]);
                multipart.addFormField("SiteId", params[1]);
                multipart.addFormField("CampId", params[2]);
                multipart.addFormField("IsSignature", params[3]);
                multipart.addFormField("CreatedBy", params[4]);
                multipart.addFormField("IsDeviceIssue", params[5]);
                multipart.addFilePart("UploadedPath", new File(params[6]));
                multipart.addFilePart("ThaumbPath", new File(params[7]));

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
            pd.dismiss();
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
                    if (status.equalsIgnoreCase("Success")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setTitle("Success");
                        builder.setMessage("Patient signature saved successfully");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("HealthScreeningAcknowledgementConfirmation_Activity"));
                                finish();
                            }
                        });

                        builder.show();
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Acknowledgement");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetFingerPrintPathAPICall(String regId) {
//        final ProgressDialog pDialog = new ProgressDialog(context);
//        pDialog.setCancelable(false);
//        pDialog.setMessage("Please Wait . . .");
//        pDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<GetFingerPrintPathResponce> call = apiService.GetFingerPrintPath(regId);
        call.enqueue(new Callback<GetFingerPrintPathResponce>() {
            @Override
            public void onResponse(Call<GetFingerPrintPathResponce> call,
                                   Response<GetFingerPrintPathResponce> response) {
//                pDialog.dismiss();

                int surverCode = response.code();
                String surverMessage = response.message();

                if (surverCode == ApplicationConstants.OKSUCCESS) {
                    try {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (status.equalsIgnoreCase("Success")) {

                            ArrayList<GetFingerPrintPath_Model> fileData = new ArrayList<>();
                            fileData = response.body().getOutput();

                            for (int i = 0; i < fileData.size(); i++) {
                                if (fileData.get(i).getFiletype().equalsIgnoreCase("2")) {
                                    new DownloadFileFromURL().execute(fileData.get(i).getPath());
                                }
                            }

                        } else {
                            iv_biomatric.setEnabled(false);
//                            Utilities.showAlertDialog(context, status, message, false);
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
            public void onFailure(Call<GetFingerPrintPathResponce> call, Throwable t) {
//                pDialog.dismiss();
                Log.e(TAG, t.toString());
                Utilities.showAlertDialog(context,
                        "Alert", t.toString(), false);
            }
        });
    }

    private class DownloadFileFromURL extends AsyncTask<String, String, Boolean> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait while downloading ...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                downloadThumbFile = new File(downloadThumbFolder,
                        patientDetails.getRegdId() + "_2_" + userID + "_registerTemp.iso").toString();
                OutputStream output = new FileOutputStream(downloadThumbFile);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pDialog.dismiss();
            if (result) {
                Utilities.showToastMessage("File save successfully ...", context, true);
            } else {
                Utilities.showToastMessage("Error during saving file ...", context, false);
            }
        }
    }

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
                mfs100.SetApplicationContext(HealthScreeningAcknowledgement_Activity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (mfs100 == null) {
                    mfs100 = new MFS100(this);
                    mfs100.SetApplicationContext(HealthScreeningAcknowledgement_Activity.this);
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
                if (imgPreview != null) {
                    imgPreview.setImageBitmap(previewBitmap);
                }

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

                try {
                    if (flagComplete && captureError == ErrorCodes.MORPHO_OK) {
                        setButtonEnabled(true);

                        byte[] tempDataDownloaded = getBytefromFile();

                        Template tmpl1;
                        tmpl1 = new Template();
                        tmpl1.setData(tempDataDownloaded);
                        tmpl1.setDataIndex(new Integer(0));
                        tmpl1.setTemplateType(TemplateType.MORPHO_PK_ISO_FMR);

                        Template tmpl2;
                        tmpl2 = new Template();
                        tmpl2.setData(fpSensorCap.templateBuffer);
                        tmpl2.setDataIndex(new Integer(0));
                        tmpl2.setTemplateType(TemplateType.MORPHO_PK_ISO_FMR);

                        int err = -1;
                        err = fpSensorCap.verifyMatch(tmpl2, tmpl1);

                        if (err == ErrorCodes.MORPHO_OK) {
                            iv_biomatric.setBackground(getResources().getDrawable(R.drawable.image_border_green));
                            tv_thumbstatus.setText("Finger print match successfully.");
                            tv_thumbstatus.setTextColor(getResources().getColor(R.color.green));
                            isFingerPrintVerified = true;

                        } else {
                            iv_biomatric.setBackground(getResources().getDrawable(R.drawable.image_border_red));
                            tv_thumbstatus.setText("Finger print did not match.");
                            tv_thumbstatus.setTextColor(getResources().getColor(R.color.red));
                            isFingerPrintVerified = false;

                            setButtonEnabled(true);
                            isWorking = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                        HealthScreeningAcknowledgement_Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_biomatric.setImageBitmap(bitmap);
                            }
                        });

                        SetData2(fingerData);
                    }
                } catch (Exception ex) {

                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    public void SetData2(FingerData fingerData) {
        try {
            SetTextOnUIThread("CAPTURING");

            Enroll_Template = new byte[fingerData.ISOTemplate().length];
            byte[] Verify_Template = new byte[getBytefromFile().length];

            System.arraycopy(fingerData.ISOTemplate(), 0, Enroll_Template, 0,
                    fingerData.ISOTemplate().length);

            System.arraycopy(getBytefromFile(), 0, Verify_Template, 0,
                    getBytefromFile().length);

            int ret = mfs100.MatchISO(Enroll_Template, Verify_Template);
            if (ret < 0) {
                tv_thumbstatus.setText(mfs100.GetErrorMsg(ret));
            } else {
                if (ret >= 96) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            iv_biomatric.setBackground(getResources().getDrawable(R.drawable.image_border_green));
                            tv_thumbstatus.setText("Finger print match successfully.");
                            tv_thumbstatus.setTextColor(getResources().getColor(R.color.green));
                            isFingerPrintVerified = true;
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_biomatric.setBackground(getResources().getDrawable(R.drawable.image_border_red));
                            tv_thumbstatus.setText("Finger print did not match.");
                            tv_thumbstatus.setTextColor(getResources().getColor(R.color.red));
                            isFingerPrintVerified = false;

                            setButtonEnabled(true);
                            isCaptureRunning = false;
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytefromFile() {
        File file = new File(downloadThumbFile);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    private void SetTextOnUIThread(String capture_success) {
//        Toast.makeText(context, capture_success, Toast.LENGTH_SHORT).show();

        Log.i("FINGERPRINT", patientDetails.getEnglishName() + " " + capture_success);
    }
}
