package com.myhindlab.abkat.activities;


import static com.myhindlab.abkat.utilities.Utilities.compressImage;

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
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.fingerprint.dao.AuthBfdCap;
import com.myhindlab.abkat.fingerprint.fps.MorphoTabletFPSensorDevice;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class PatientFingerAndSignature_Signature_Activity extends AppCompatActivity implements AuthBfdCap, MFS100Event {

    private Context context;
    private ProgressDialog pd;
    private String TAG = getClass().getName();

    private String campId, siteId, registrationNo = "",distlgdcode,isRTPCR,campLatitude,campLongitude,campType,beneficiaryNo,iscampType,district;
    private CheckBox cb_finger_issue, cb_signature_applicable;
    private ImageView imv_search, iv_biomatric;
    private LinearLayout ll_signature;
    private SignaturePad signature_pad;
    private Button btn_clearpad;
    private RadioButton rb_mr, rb_mrs, rb_ms;

    private Button btn_markattendance;
    private String RegId = "", EmpCode, LATITUDE = "0", LONGITUDE = "0", isThumbExist = "0", fingerPrintPath = "", signaturePath = "";
    private UserSessionManager session;
    private File signatureImageFile, patientSignFolder, fingerPrintFolder;

    private String fingerImage = ""/*,registerTemp = ""*/,rejRegdID = "0",relation,registerdCampId,beneficaryName,Type="0";
    private boolean isFingerPrint = false, isFingerPrintIssue = false, isSignatureApplicable = true;
    private boolean isSignedByUser = false;
    String deviceMan = Build.MANUFACTURER;
    private boolean isCaptureRunning = false;
    private boolean isCaptured = false;
    MFS100 mfs100 = null;
    ScannerAction scannerAction = ScannerAction.Capture;
    int timeout = 10000;

    byte[] Enroll_Template;
    private FingerData lastCapFingerData = null;

    private static long mLastClkTime = 0;
    private static long Threshold = 1500;

    private Uri thumbURI;
    private final int THUMB_CAMERA_REQUEST = 100;
    private Bitmap patientPicBm = null;

    private enum ScannerAction {
        Capture, Verify
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_finger_and_signature_for_signature);

        init();
        setUpToolbar();
        setDefault();
        getSessionData();
        setEventHandler();

    }

    private void init() {
        context = PatientFingerAndSignature_Signature_Activity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        imv_search = findViewById(R.id.imv_search);
        cb_finger_issue = findViewById(R.id.cb_finger_issue);
        iv_biomatric = findViewById(R.id.iv_biomatric);

        ll_signature = findViewById(R.id.ll_signature);
        signature_pad = findViewById(R.id.signature_pad);
        btn_clearpad = findViewById(R.id.btn_clearpad);
        cb_signature_applicable = findViewById(R.id.cb_signature_applicable);

        btn_markattendance = findViewById(R.id.btn_markattendance);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            patientSignFolder = getExternalCacheDir();

        } else {
            patientSignFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Signature/");
            if (!patientSignFolder.exists())
                patientSignFolder.mkdirs();

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            fingerPrintFolder = getExternalCacheDir();

        } else {
            fingerPrintFolder = new File(Environment.getExternalStorageDirectory(), "/Health Checkup/ThumbData/");
            if (!fingerPrintFolder.exists()) {
                fingerPrintFolder.mkdirs();
            }
        }

    }

    private void setDefault() {
//        ConstructionSitesList_Model siteDetails = new ConstructionSitesList_Model();
//        siteDetails = (ConstructionSitesList_Model) getIntent().getSerializableExtra("siteDetails");
//        campId = getIntent().getStringExtra("campId");
//        siteId = getIntent().getStringExtra("siteId");
//        registrationNo = getIntent().getStringExtra("registrationNo");


        Intent intent = getIntent();

         campId = intent.getStringExtra("campId");
         siteId = intent.getStringExtra("siteId");
         registrationNo = intent.getStringExtra("registrationNo");
         EmpCode = intent.getStringExtra("EmpCode");
         fingerPrintPath = intent.getStringExtra("fingerPrintPath");

         isSignatureApplicable = intent.getBooleanExtra("isSignatureApplicable", false);
         isFingerPrintIssue = intent.getBooleanExtra("isFingerPrintIssue", false);


        beneficiaryNo = getIntent().getStringExtra("beneficiaryNo");
        relation = getIntent().getStringExtra("relation");
        rejRegdID = getIntent().getStringExtra("regId");
        registerdCampId = getIntent().getStringExtra("rejCampId");
        beneficaryName = getIntent().getStringExtra("beneficiarName");
        Type = getIntent().getStringExtra("Type");


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

//
//        cb_finger_issue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (cb_finger_issue.isChecked()) {
////                    iv_biomatric.setVisibility(View.GONE);
//                    iv_biomatric.setImageDrawable(context.getDrawable(R.drawable.icon_camera));
//                    isFingerPrintIssue = true;
//
//                } else {
////                    iv_biomatric.setVisibility(View.VISIBLE);
//                    iv_biomatric.setImageDrawable(context.getDrawable(R.drawable.vector_fingerprint));
//
//                    isFingerPrintIssue = false;
//
//                }
//            }
//        });

        cb_signature_applicable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_signature_applicable.isChecked()) {
                    ll_signature.setVisibility(View.GONE);
                    isSignatureApplicable = false;
                } else {
                    ll_signature.setVisibility(View.VISIBLE);
                    isSignatureApplicable = true;

                }
            }
        });

        signature_pad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                isSignedByUser = true;
                btn_clearpad.setEnabled(true);
            }

            @Override
            public void onClear() {
                isSignedByUser = false;
                btn_clearpad.setEnabled(false);
            }
        });

        btn_clearpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_pad.clear();
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
//
//        iv_biomatric.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //need to revert
////                if (RegId.equalsIgnoreCase("")) {
////                    edt_mcobcwwbno.setError("Please search patient first");
////                    edt_mcobcwwbno.requestFocus();
////                    return;
////                }
//                if (cb_finger_issue.isChecked()) {
////                    if (session.isHllUser()) {
//                    if (SDK_INT < Build.VERSION_CODES.S) {
//                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
//                            return;
//                        }
//
//                    } else {
//                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
//                            return;
//                        }
//                    }
//
//
//                    int randomEndtNo = (int) (Math.random() * 99999 + 1);
//                        if (SDK_INT >= Build.VERSION_CODES.Q) {
//                            ContentResolver resolver = context.getContentResolver();
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_FR.png");
//                            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
//                            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//                            thumbURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, thumbURI);
//                            startActivityForResult(intent, THUMB_CAMERA_REQUEST);
//                        } else {
//                            File patientImageFile = new File(fingerPrintFolder, randomEndtNo + "_FR.png");
//                            thumbURI = Uri.fromFile(patientImageFile);
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, thumbURI);
//                            startActivityForResult(intent, THUMB_CAMERA_REQUEST);
//                        }
////                    }
//                } else {
//
//                    if (deviceMan.equalsIgnoreCase("Hena")) {
//                        setDetaminiParams();
//                        if (!isCaptured)
//                            if (isWorking) {
//                                Utilities.showToastMessage("Please let process to complete first", context, false);
//                            } else {
//                                fingerImage = RegId + "_1_" + EmpCode + "_FR.png";
////                            registerTemp = RegId + "_2_" + EmpCode + "_registerTemp.raw";
//
//                                capture();
////                            Utilities.showToastMessage("Place your finger on the sensor", context, false);
//                            }
//                    } else {
//
//                        fingerImage = RegId + "_1_" + EmpCode + "_FR.png";
////                    registerTemp = RegId + "_2_" + EmpCode + "_registerTemp.iso";
//
////                    Utilities.showToastMessage("Place your finger on the sensor", context, false);
//
//                        scannerAction = PatientFingerAndSignature_Signature_Activity.ScannerAction.Capture;
//                        if (!isCaptureRunning) {
//                            StartSyncCapture();
//                        }
//                    }
//                }
//
//            }
//        });
    }

    private void submitData() {


        ///Need to revert

//Need to revert
        if (!session.isHllUser()) {
//            if (!isCaptured) {
//                Utilities.showAlertDialog(context, "Alert", "Please capture finger print of beneficiary", false);
//                return;
//            }
        } else {
//            if (!isCaptured) {
//                Utilities.showAlertDialog(context, "Alert", "Please capture finger print of beneficiary", false);
//                return;
//            }
        }


        if (cb_signature_applicable.isChecked()) {

            if ( isSignatureApplicable&& !isSignedByUser) {
                Utilities.showAlertDialog(context, "Alert", "Please sign on the signature pad", false);
                return;
            }
        }


        if (!session.isHllUser()) {
            File fingerPrintFile = new File(fingerPrintFolder, fingerImage);    // File Type 1 for Finger print image

            if (isFingerPrintIssue){
//                fingerPrintPath = "";
            }

            else
                fingerPrintPath = fingerPrintFile.getAbsolutePath();
        }


//
//        startActivity(new Intent(context, PatientFingerAndSignature_Signature_Activity.class)
//                .putExtra("campId", campId)
//                .putExtra("siteId", siteId)
//                .putExtra("registrationNo",RegId)
//                .putExtra("isSignatureApplicable",isSignatureApplicable)
//                .putExtra("isFingerPrintIssue",isFingerPrintIssue)
//                .putExtra("EmpCode",EmpCode)
//                .putExtra("fingerPrintPath",fingerPrintPath)


//        );
        



//        isSignatureApplicable ? "1" : "0",
//                            isFingerPrintIssue ? "0" : "1",
//                            EmpCode,
//                            fingerPrintPath,


        Bitmap signatureBitmap = signature_pad.getSignatureBitmap();
        if (isSignedByUser) {
            if (addJpgSignatureToGallery(signatureBitmap)) {


                signaturePath = signatureImageFile.getAbsolutePath();


                if (Utilities.isNetworkAvailable(context)) {
                    new InsertSignatureandThumbDetails().execute(
                            registrationNo,
                            siteId,
                            campId,
                            isSignatureApplicable ? "1" : "0",
                            isFingerPrintIssue ? "0" : "1",
                            EmpCode,
                            fingerPrintPath,
                            signaturePath
                    );
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            } else {
                Toast.makeText(context, "Unable to store the signature", Toast.LENGTH_SHORT).show();
            }

        } else {
//            if (Utilities.isNetworkAvailable(context)) {
//                new InsertSignatureandThumbDetails().execute(
//                        RegId,
//                        siteId,
//                        campId,
//                        isSignatureApplicable?"1":"0",
//                        isFingerPrintIssue?"0":"1",
//                        EmpCode,
//                        fingerPrintPath,
//                        signaturePath
//                );
//            } else {
//                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//            }

            Utilities.showAlertDialog(context, "Alert", "Please sign on the signature pad", false);

        }

    }


    public class InsertSignatureandThumbDetails extends AsyncTask<String, Void, String> {

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

            Log.d("insert sign", Arrays.toString(params));
            try {
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.InsertSignatureandThumbDetails, "UTF-8");

                multipart.addFormField("RegdId", params[0]);
                multipart.addFormField("SiteId", params[1]);
                multipart.addFormField("CampId", params[2]);
                multipart.addFormField("IsSignature", params[3]);
                multipart.addFormField("IsDeviceIssue", params[4]);
                multipart.addFormField("CreatedBy", params[5]);
                if (!params[6].equals(""))
                    multipart.addFilePart("File1", new File(params[6]));
                if (!params[7].equals(""))
                    multipart.addFilePart("File2", new File(params[7]));

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
            Log.d("InsertSign", result);
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
                        builder.setMessage("Beneficiary Registered Successfully.");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(context, D2DPatientRegistration_Activity.class)
                                        .putExtra("campId", campId)
                                        .putExtra("district", district)
                                        .putExtra("siteId", siteId)
                                        .putExtra("beneficiaryNo", beneficiaryNo)
                                        .putExtra("relation", relation)
                                        .putExtra("regId", rejRegdID)
                                        .putExtra("rejCampId", registerdCampId)
                                        .putExtra("beneficiarName", beneficaryName)
                                        .putExtra("Type", Type)
                                );

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
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Signature");

//        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

////////////////////////////////////////////Signature Pad///////////////////////////////////////////////////////////////

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        signatureImageFile = new File(patientSignFolder,
                RegId + "_0_" + campId + "_SG.png");

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
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

//////////////////////////////////////////MORPH FINGER PRINT////////////////////////////////////////////////////////////

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
                mfs100.SetApplicationContext(PatientFingerAndSignature_Signature_Activity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (mfs100 == null) {
                    mfs100 = new MFS100(this);
                    mfs100.SetApplicationContext(PatientFingerAndSignature_Signature_Activity.this);
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
//        if (fpSensorCap != null) {
//            fpSensorCap.cancelLiveAcquisition();
//            fpSensorCap.release();
//        }
//        finish();
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
            Utilities.showToastMessage("Place your finger on the sensor", context, false);

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
                    setButtonEnabled(true);

                    if (storeImage(fingerImage, previewBitmap)) {
//                        if (DumpFile(registerTemp, fpSensorCap.templateBuffer)) {
//                            File file = new File(folder, registerTemp);
//                            InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "2", EmpCode,
//                                    file.toString());
//                        }
                        isCaptured = true;
                    }
                }
            }
        });
    }

    private boolean storeImage(String fileName, Bitmap image) {
        File file = new File(fingerPrintFolder, fileName);
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
        File file = new File(fingerPrintFolder, fileName);
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
        Utilities.showToastMessage("Place your finger on the sensor", context, false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SetTextOnUIThread("Capture Started");
                isCaptureRunning = true;

                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, false);
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                        Log.e("Capture error", mfs100.GetErrorMsg(ret));

                    } else {
                        lastCapFingerData = fingerData;

                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        PatientFingerAndSignature_Signature_Activity.this.runOnUiThread(new Runnable() {
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
                    SetTextOnUIThread("Error " + ex.getMessage());
                    Log.e("Capture error", ex.getMessage());
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    public void SetData2(FingerData fingerData, Bitmap bitmap) {
        try {
            isCaptured = true;
//            WriteFileRaw(registerTemp, fingerData.ISOTemplate());
            WriteFileImage(fingerImage, bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void WriteFileImage(String fingerImage, Bitmap bitmap) {
        File file = new File(fingerPrintFolder, fingerImage);
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
        File file = new File(fingerPrintFolder, filename);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(context, capture_success, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
//    private void SetTextOnUIThread(final String str) {
//        this.lblMessage.post(new Runnable() {
//            /* class MFS100Test.AnonymousClass3 */
//            public void run() {
//                try {
//                    MFS100CodeHubs.this.lblMessage.setText(str);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//    private void SetLogOnUIThread(final String str) {
//        this.txtEventLog.post(new Runnable() {
//            /* class MFS100Test.AnonymousClass4 */
//            public void run() {
//                try {
//                    EditText editText = MFS100CodeHubs.this.txtEventLog;
//                    editText.append("\n" + str);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    // Use the cropped image URI.
                    String path = result.getUriFilePath(context, true);
                    savefile(result.getUriContent());

                    Log.i("cropImageLauncher", ": " + path);
                    // Process the cropped image URI as needed.
                } else {
                    // An error occurred.
                    Exception exception = result.getError();
                    // Handle the error.
                }
            }
    );
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == THUMB_CAMERA_REQUEST) {
//                CropImage.activity(thumbURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientFingerAndSignature_Activity.this);


                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                CropImageContractOptions options = new CropImageContractOptions(thumbURI, cropImageOptions);
                cropImageLauncher.launch(options);
            }

//            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == RESULT_OK) {
//                    Uri resultUri = result.getUri();
//                    savefile(resultUri);
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
//                }
//            }
        }
    }

//    String destinationFilename = "";
//
//    public void savefile(Uri sourceuri) {
//        pd.setMessage("Saving Image,\n Please wait...");
//        pd.setCancelable(false);
//        pd.show();
//
//
//        Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message message) {
////                Log.i("TAG", "handleMessage: " + new Gson().toJson(message));
//                pd.dismiss();
//                return false;
//            }
//        });
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//                Log.i("sourceuri1", "" + sourceuri);
//
////                String destinationFilename = "";
//                String filename = (int) (Math.random() * 99999 + 1) + "_FR.png";
//                destinationFilename = fingerPrintFolder + "/" + RegId + "_1_" + EmpCode + "_FR.png";
//                //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
//
//
//
//                String sourceFilename = sourceuri.getPath();
//                BufferedInputStream bis = null;
//                BufferedOutputStream bos = null;
//
//                Log.i("sourceFilename", "run: " + sourceFilename);
//                try {
//                    InputStream inputStream = context.getContentResolver().openInputStream(sourceuri);
//
//                    bis = new BufferedInputStream(inputStream);
//                    bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
//                    byte[] buf = new byte[1024];
//                    bis.read(buf);
//                    do {
//                        bos.write(buf);
//                    } while (bis.read(buf) != -1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (bis != null) bis.close();
//                        if (bos != null) bos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                try {
//
//                    patientPicBm = BitmapFactory.decodeFile(destinationFilename);
//                    Utilities.getBitmapAsByteArray(patientPicBm);
//                    destinationFilename = compressImage(destinationFilename);
//                    patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                iv_biomatric.setImageBitmap(patientPicBm);
//                                pd.dismiss();
//
//                                Bundle bundle = new Bundle();
//                                bundle.putString("message", "Success");
//                                Message message = new Message();
//                                message.setData(bundle);
////                            handler.sendMessage(message);
////
////
////                            handler.removeCallbacks(this);
//
//                            }
//                        });
//
//
//                    fingerPrintPath = destinationFilename;
//
//
//
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    pd.dismiss();
//                }
//
//            }
//        };
////        handler.post(runnable);
//
//        new Thread(runnable).start();
//
//    }




    String destinationFilename = "";

    private void savefile(Uri sourceuri) {
        pd.setMessage("Saving Image,\n Please wait...");
        pd.setCancelable(false);
        pd.show();



        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
//                Log.i("TAG", "handleMessage: " + new Gson().toJson(message));
                pd.dismiss();
                return false;
            }
        });


        Runnable runnable = new Runnable() {
            @Override
            public void run() {


                Log.i("sourceuri1", "" + sourceuri);
                String destinationFilename = "";
                String filename = (int) (Math.random() * 99999 + 1) + "_FR.png";
                destinationFilename = fingerPrintFolder + "/" + RegId + "_1_" + EmpCode + "_FR.png";


                String sourceFilename = sourceuri.getPath();
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                Log.i("sourceFilename", "run: " + sourceFilename);
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(sourceuri);

                    bis = new BufferedInputStream(inputStream);
                    bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
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

                try {

                    patientPicBm = BitmapFactory.decodeFile(destinationFilename);
                    Utilities.getBitmapAsByteArray(patientPicBm);
                    destinationFilename = compressImage(destinationFilename);
                    patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_biomatric.setImageBitmap(patientPicBm);
                            pd.dismiss();

                            Bundle bundle = new Bundle();
                            bundle.putString("message", "Success");
                            Message message = new Message();
                            message.setData(bundle);

                            if (!fingerPrintPath.isEmpty()) {
                                isCaptured = true;
                            }
//                            handler.sendMessage(message);
//
//
//                            handler.removeCallbacks(this);

                        }
                    });


                    fingerPrintPath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }

//
//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        String filename = (int) (Math.random() * 99999 + 1) + "_FR.png";
//        destinationFilename = fingerPrintFolder + "/" + RegId + "_1_" + EmpCode + "_FR.png";
//        //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
//
//        String sourceFilename = sourceuri.getPath();
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//
//        try {
//            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
//            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
//            byte[] buf = new byte[1024];
//            bis.read(buf);
//            do {
//                bos.write(buf);
//            } while (bis.read(buf) != -1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bis != null) bis.close();
//                if (bos != null) bos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        patientPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
//        iv_biomatric.setImageBitmap(patientPicBm);
//        fingerPrintPath = destinationFilename;
//        Log.d("Thumb", "savefile: " + fingerPrintPath);
//
//        if (!fingerPrintPath.isEmpty()) {
//            isCaptured = true;
//        }
//
//    }


    @Override
    public void onBackPressed() {
        Utilities.showToastMessage("Please upload details", context, false);
    }
}