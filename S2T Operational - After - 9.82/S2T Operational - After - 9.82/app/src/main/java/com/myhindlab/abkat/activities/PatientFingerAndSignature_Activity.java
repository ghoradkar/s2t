package com.myhindlab.abkat.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.calling_dashboard.adapter.RationCardPhotoAdapter;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.camera.CameraActivity;
import com.myhindlab.abkat.camera.utils.CaptureMode;
import com.myhindlab.abkat.fingerprint.dao.AuthBfdCap;
import com.myhindlab.abkat.fingerprint.fps.MorphoTabletFPSensorDevice;
import com.myhindlab.abkat.models.PatientDetailsOnRegNo_Model;
import com.myhindlab.abkat.models.RationCardPhotoModel;
import com.myhindlab.abkat.pojos.PatientDetailsOnRegNo_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PatientFingerAndSignature_Activity extends AppCompatActivity implements AuthBfdCap, MFS100Event {

    private Context context;
    private ProgressDialog pd;

    private int uploadedCount = 0;
    private int totalUploadCount = 0;
    private String TAG = getClass().getName();

    private String campId, siteId, registrationNo = "", beneficiaryNo, relation, rejRegdID = "0", dependentBocId = "", rationCardNumber = "", regIdAfterReg = "", registerdCampId, beneficaryName, Type = "0";
    private CheckBox cb_finger_issue, cb_signature_applicable;
    private ImageView imv_search, iv_biomatric, imv_self_declaration;
    private LinearLayout ll_signature;
    private CardView llMain_RationCard_no;
    private SignaturePad signature_pad;
    private Button btn_clearpad, btn_UploadRationCard;
    private RadioButton rb_mr, rb_mrs, rb_ms;
    private MaterialEditText edt_mcobcwwbno, edt_name, edt_gender, edt_age, edt_aadhaarno, edt_moblieno,
            edt_dob, edt_address, edt_city, edt_pincode;
    private Button btn_markattendance;
    private String RegId = "", EmpCode, LATITUDE = "0", LONGITUDE = "0", isThumbExist = "0", fingerPrintPath = "", signaturePath = "", rationCardImagePath = "";
    private UserSessionManager session;
    private File signatureImageFile, patientSignFolder, fingerPrintFolder;

    private RecyclerView recyclerPhotos;


    private ArrayList<RationCardPhotoModel> rationCardPhotoList =
            new ArrayList<>();

    private RationCardPhotoAdapter adapter;

    private String fingerImage = ""/*,registerTemp = ""*/;
    private boolean isFingerPrint = false, isFingerPrintIssue = false, isSignatureApplicable = true, isRationCardPhotoAvailable;
    private boolean isSignedByUser = false;
    String deviceMan = Build.MANUFACTURER;
    private boolean isCaptureRunning = false;
    private boolean isCaptured = false;
    MFS100 mfs100 = null;
    PatientFingerAndSignature_Activity.ScannerAction scannerAction = PatientFingerAndSignature_Activity.ScannerAction.Capture;
    int timeout = 10000;

    byte[] Enroll_Template;
    private FingerData lastCapFingerData = null;

    private static long mLastClkTime = 0;
    private static long Threshold = 1500;

    private Uri thumbURI;

    private RadioGroup radioGroupArea;

    private RadioButton radioManual, radioDigital;
    private final int THUMB_CAMERA_REQUEST = 100;
    private Bitmap patientPicBm = null, rationCardPicBm = null;

    private enum ScannerAction {
        Capture, Verify
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_finger_and_signature);

        init();
        getSessionData();
        setUpToolbar();
        setDefault();
        setEventHandler();

    }

    private void init() {
        context = PatientFingerAndSignature_Activity.this;
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
        llMain_RationCard_no = findViewById(R.id.llMain_RationCard_no);
        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        imv_search = findViewById(R.id.imv_search);
        cb_finger_issue = findViewById(R.id.cb_finger_issue);
        iv_biomatric = findViewById(R.id.iv_biomatric);
        imv_self_declaration = findViewById(R.id.imv_self_declaration);
        radioGroupArea = findViewById(R.id.radioGroupArea);
        radioManual = findViewById(R.id.radioManual);
        radioDigital = findViewById(R.id.radioDigital);

        ll_signature = findViewById(R.id.ll_signature);
        signature_pad = findViewById(R.id.signature_pad);
        btn_clearpad = findViewById(R.id.btn_clearpad);
        btn_UploadRationCard = findViewById(R.id.btn_UploadRationCard);
        recyclerPhotos = findViewById(R.id.recyclerPhotos);
//        cb_signature_applicable = findViewById(R.id.cb_signature_applicable);

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


        adapter = new RationCardPhotoAdapter(
                this,
                rationCardPhotoList
        );

        recyclerPhotos.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerPhotos.setAdapter(adapter);

    }


    private void setDefault() {
//        ConstructionSitesList_Model siteDetails = new ConstructionSitesList_Model();
//        siteDetails = (ConstructionSitesList_Model) getIntent().getSerializableExtra("siteDetails");


        campId = getIntent().getStringExtra("campId");
        siteId = getIntent().getStringExtra("siteId");
        registrationNo = getIntent().getStringExtra("registrationNo");
        beneficiaryNo = getIntent().getStringExtra("beneficiaryNo");
        relation = getIntent().getStringExtra("relation");
        rejRegdID = getIntent().getStringExtra("regId");
        registerdCampId = getIntent().getStringExtra("rejCampId");
        beneficaryName = getIntent().getStringExtra("beneficiarName");
        Type = getIntent().getStringExtra("Type");
        dependentBocId = getIntent().getStringExtra("dependentBocId");
        rationCardNumber = getIntent().getStringExtra("rationCardNumber");
        regIdAfterReg = getIntent().getStringExtra("regIdAfterReg");


        Log.d(TAG, "dependentBocId + rationCardNumber + regIdAfterReg : " + dependentBocId + " " + rationCardNumber + " " + regIdAfterReg);

        if (dependentBocId != null) {
            if (!dependentBocId.equals("0")) {
                llMain_RationCard_no.setVisibility(View.VISIBLE);
                btn_markattendance.setEnabled(false);
                btn_markattendance.setVisibility(View.GONE);
            } else {
                llMain_RationCard_no.setVisibility(View.GONE);
                btn_markattendance.setVisibility(View.VISIBLE);


            }
        }


//        .putExtra("dependentBocId",dependentBocId)
//                .putExtra("rationCardNumber",binding.edtRationCard.getText().toString().trim())
//                .putExtra("regIdAfterReg",registeredPatientregdid)


        if (registrationNo != null && !registrationNo.equalsIgnoreCase("")) {
            edt_mcobcwwbno.setFocusable(false);
            edt_mcobcwwbno.setClickable(false);
            if (Utilities.isNetworkAvailable(context)) {
                new GetWorkerInfroFromWorkerRegid().execute(registrationNo);
                btn_markattendance.setEnabled(true);
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
//                if (edt_mcobcwwbno.getText().toString().isEmpty()) {
//                    edt_mcobcwwbno.setError("Please enter beneficiary registration number");
//                    edt_mcobcwwbno.requestFocus();
//                    return;
//                }

                if (Utilities.isNetworkAvailable(context)) {
                    new GetWorkerInfroFromWorkerRegid().execute(edt_mcobcwwbno.getText().toString().trim());
                    btn_markattendance.setEnabled(true);
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        View.OnClickListener radioClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rationCardPhotoList.clear();

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        };

        radioManual.setOnClickListener(radioClickListener);
        radioDigital.setOnClickListener(radioClickListener);

        imv_self_declaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (radioGroupArea.getCheckedRadioButtonId() == -1) {

                    Utilities.showMessageString("Please select any option", context);

                    return;
                }

                int maxPhotos = 0;

                if (radioManual.isChecked()) {

                    maxPhotos = 2;

                } else if (radioDigital.isChecked()) {

                    maxPhotos = 1;
                }

                if (rationCardPhotoList.size() >= maxPhotos) {

                    Utilities.showToastMessage(
                            "Maximum " + maxPhotos + " photo" +
                                    (maxPhotos > 1 ? "s" : "") + " allowed",
                            context,
                            false
                    );

                    return;
                }


                Intent cameraIntent = new Intent(context, CameraActivity.class);
                cameraIntent.putExtra("CAPTURE_MODE", CaptureMode.DOCUMENT.name());
                cameraLauncherForRationCard.launch(cameraIntent);


            }
        });


        btn_UploadRationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (patientPicBm == null) {
                    Utilities.showMessageString("Please capture thumb photo", context);
                    return;
                }


                if (radioGroupArea.getCheckedRadioButtonId() == -1) {

                    Utilities.showMessageString("Please select any option", context);

                    return;
                }


//
//                if (radioManual.isChecked()){
//                    if (rationCardPhotoList.size() < 2) {
//
//                        Toast.makeText(
//                                context,
//                                "Minimum 2 images required",
//                                Toast.LENGTH_SHORT
//                        ).show();
//
//                        return;
//                    }
//
//                }else if (radioDigital.isChecked()){
//
//                    if (rationCardPhotoList.size() < 1 ) {
//
//                        Toast.makeText(
//                                context,
//                                "Minimum 1 images required",
//                                Toast.LENGTH_SHORT
//                        ).show();
//
//                        return;
//                    }
//                }


                int minPhotos = 0;

                if (radioManual.isChecked()) {

                    minPhotos = 2;

                } else if (radioDigital.isChecked()) {

                    minPhotos = 1;
                }

                if (rationCardPhotoList.size() < minPhotos) {

                    if (radioManual.isChecked()){
                        Toast.makeText(
                                context,
                                "Minimum " + "two" + " image" +
                                        (minPhotos > 1 ? "s" : "") + " required for manual ration card",
                                Toast.LENGTH_SHORT
                        ).show();

                    }else if (radioDigital.isChecked()){
                        Toast.makeText(
                                context,
                                "Minimum " + "one" + " image" +
                                        (minPhotos > 1 ? "s" : "") + " required for digital ration card",
                                Toast.LENGTH_SHORT
                        ).show();


                    }

                    return;
                }


                if (!Utilities.isNetworkAvailable(context)) {

                    Utilities.showToastMessage(
                            R.string.msgt_nointernetconnection,
                            context,
                            false
                    );

                    return;
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert!");
                builder.setMessage("Please confirm the beneficiary's details before submitting");
                builder.setCancelable(false);
                builder.setPositiveButton("Proceed", (dialog, which) -> {

                    uploadedCount = 0;

                    totalUploadCount = rationCardPhotoList.size();

                    for (int i = 0; i < rationCardPhotoList.size(); i++) {

                        String imagePath =
                                rationCardPhotoList.get(i).getImagePath();


                        new InsertRationCardDetailsDetails().execute(
                                regIdAfterReg,
                                EmpCode,
                                dependentBocId,
                                rationCardNumber,
                                "0",
                                imagePath
                        );


                    }

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.create().show();


            }
        });

        cb_finger_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_finger_issue.isChecked()) {
//                    iv_biomatric.setVisibility(View.GONE);
                    iv_biomatric.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_camera));
                    isFingerPrintIssue = true;
                } else {
//                    iv_biomatric.setVisibility(View.VISIBLE);
                    iv_biomatric.setImageDrawable(context.getResources().getDrawable(R.drawable.vector_fingerprint));

                    isFingerPrintIssue = false;

                }
            }
        });
//
//        cb_signature_applicable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (cb_signature_applicable.isChecked()) {
//                    ll_signature.setVisibility(View.GONE);
//                    isSignatureApplicable = false;
//                } else {
//                    ll_signature.setVisibility(View.VISIBLE);
//                    isSignatureApplicable = true;
//
//                }
//            }
//        });
//
//        signature_pad.setOnSignedListener(new SignaturePad.OnSignedListener() {
//            @Override
//            public void onStartSigning() {
//
//            }
//
//            @Override
//            public void onSigned() {
//                isSignedByUser = true;
//                btn_clearpad.setEnabled(true);
//            }
//
//            @Override
//            public void onClear() {
//                isSignedByUser = false;
//                btn_clearpad.setEnabled(false);
//            }
//        });
//
//        btn_clearpad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signature_pad.clear();
//            }
//        });

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

                if (cb_finger_issue.isChecked()) {
                    if (SDK_INT < Build.VERSION_CODES.S) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                            return;
                        }

                    } else {
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
                            return;
                        }
                    }

                    Intent cameraIntent = new Intent(context, CameraActivity.class);
                    cameraIntent.putExtra("CAPTURE_MODE", CaptureMode.ID_CARD.name());
                    cameraLauncherForThumbImage.launch(cameraIntent);

                } else {

                    if (deviceMan.equalsIgnoreCase("Hena")) {
//                        setDetaminiParams();
                        if (!isCaptured)
                            if (isWorking) {
                                Utilities.showToastMessage("Please let process to complete first", context, false);
                            } else {
                                fingerImage = RegId + "_1_" + EmpCode + "_FR.png";
//                            registerTemp = RegId + "_2_" + EmpCode + "_registerTemp.raw";

                                capture();
//                            Utilities.showToastMessage("Place your finger on the sensor", context, false);
                            }
                    } else {

                        fingerImage = RegId + "_1_" + EmpCode + "_FR.png";


                        scannerAction = PatientFingerAndSignature_Activity.ScannerAction.Capture;
                        if (!isCaptureRunning) {
                            StartSyncCapture();
                        }
                    }
                }

            }
        });
    }

    private void submitData() {

//        if (edt_mcobcwwbno.getText().toString().trim().isEmpty()) {
//            edt_mcobcwwbno.setError("Please enter beneficiary registration number");
//            return;
//        }
//
//
//        if (!session.isHllUser()) {
//            if (!isCaptured) {
//                Utilities.showAlertDialog(context, "Alert", "Please capture finger print of beneficiary", false);
//                return;
//            }
//        } else {
//            if (!isCaptured) {
//                Utilities.showAlertDialog(context, "Alert", "Please capture finger print of beneficiary", false);
//                return;
//            }
//        }


//        if (cb_signature_applicable.isChecked()) {
//
//            if ( isSignatureApplicable&& !isSignedByUser) {
//                Utilities.showAlertDialog(context, "Alert", "Please sign on the signature pad", false);
//                return;
//            }
//        }


        if (patientPicBm == null) {
            Utilities.showMessageString("Please capture image photo", context);
            return;
        }

//        if (!session.isHllUser()) {
//            File fingerPrintFile = new File(fingerPrintFolder, fingerImage);    // File Type 1 for Finger print image
//
//            if (isFingerPrintIssue)
//                fingerPrintPath =  fingerPrintFile.getAbsolutePath();
//            else
//                fingerPrintPath = fingerPrintFile.getAbsolutePath();
//        }


        startActivity(new Intent(context, PatientFingerAndSignature_Signature_Activity.class)
                .putExtra("campId", campId)
                .putExtra("siteId", siteId)
                .putExtra("registrationNo", RegId)
                .putExtra("isSignatureApplicable", isSignatureApplicable)
                .putExtra("isFingerPrintIssue", isFingerPrintIssue)
                .putExtra("EmpCode", EmpCode)
                .putExtra("fingerPrintPath", fingerPrintPath)
                .putExtra("beneficiaryNo", beneficiaryNo)
                .putExtra("relation", relation)
                .putExtra("regId", rejRegdID)
                .putExtra("rejCampId", registerdCampId)
                .putExtra("beneficiarName", beneficaryName)
                .putExtra("Type", Type)

        );

        finish();
//
//        Bitmap signatureBitmap = signature_pad.getSignatureBitmap();
//        if (isSignedByUser) {
//            if (addJpgSignatureToGallery(signatureBitmap)) {
//
//
//                signaturePath = signatureImageFile.getAbsolutePath();
//
//
//                if (Utilities.isNetworkAvailable(context)) {
//                    new InsertSignatureandThumbDetails().execute(
//                            RegId,
//                            siteId,
//                            campId,
//                            isSignatureApplicable ? "1" : "0",
//                            isFingerPrintIssue ? "0" : "1",
//                            EmpCode,
//                            fingerPrintPath,
//                            signaturePath
//                    );
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                }
//            } else {
//                Toast.makeText(context, "Unable to store the signature", Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
////            if (Utilities.isNetworkAvailable(context)) {
////                new InsertSignatureandThumbDetails().execute(
////                        RegId,
////                        siteId,
////                        campId,
////                        isSignatureApplicable?"1":"0",
////                        isFingerPrintIssue?"0":"1",
////                        EmpCode,
////                        fingerPrintPath,
////                        signaturePath
////                );
////            } else {
////                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
////            }
//
//            Utilities.showAlertDialog(context, "Alert", "Please sign on the signature pad", false);
//
//        }

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
                            isThumbExist = patientDetails.get(patientDetails.size() - 1).getIsThumbExist();
                            if (isThumbExist.equalsIgnoreCase("1")) {
                                iv_biomatric.setVisibility(View.GONE);
                                isCaptured = true;
                            } else {
                                iv_biomatric.setVisibility(View.VISIBLE);
                                isCaptured = false;
                            }

                            iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));
                            isWorking = false;
                            isCaptureRunning = false;
                            isCaptured = false;

                            edt_mcobcwwbno.setText(patientDetails.get(patientDetails.size() - 1).getRegdNo());

                            if (patientDetails.get(patientDetails.size() - 1).getTitle().equalsIgnoreCase("Mr.")) {
                                rb_mr.setChecked(true);
                            } else if (patientDetails.get(patientDetails.size() - 1).getTitle().equalsIgnoreCase("Mrs.")) {
                                rb_mrs.setChecked(true);
                            } else if (patientDetails.get(patientDetails.size() - 1).getTitle().equalsIgnoreCase("Ms.")) {
                                rb_ms.setChecked(true);
                            }

                            if (patientDetails.get(patientDetails.size() - 1).getGender().equalsIgnoreCase("M")) {
                                edt_gender.setText("Male");
                            } else if (patientDetails.get(patientDetails.size() - 1).getGender().equalsIgnoreCase("F")) {
                                edt_gender.setText("Female");
                            }

                            edt_name.setText(patientDetails.get(patientDetails.size() - 1).getEnglishName());

                            edt_aadhaarno.setText(patientDetails.get(patientDetails.size() - 1).getUID().replace("-", ""));

                            edt_dob.setText(patientDetails.get(patientDetails.size() - 1).getDOBFormated());

                            edt_age.setText(patientDetails.get(patientDetails.size() - 1).getAge());

//                            if (edt_age.getText().toString().isEmpty()) {
//                                String age = patientDetails.get(0).getDOBFormated();
//                                String[] ageparts = age.split("-");
//                                String year = ageparts[0];
//                                String month = ageparts[1];
//                                String day = ageparts[2];
//
//                                edt_age.setText(getAge(Integer.parseInt(year),
//                                        Integer.parseInt(month),
//                                        Integer.parseInt(day)));
//                            }

                            edt_moblieno.setText(patientDetails.get(patientDetails.size() - 1).getMobileNo());

                            edt_address.setText(patientDetails.get(patientDetails.size() - 1).getPermanentAddress());

                            edt_city.setText(patientDetails.get(patientDetails.size() - 1).getLocation());

                            edt_pincode.setText(patientDetails.get(patientDetails.size() - 1).getPincode());

                            RegId = patientDetails.get(patientDetails.size() - 1).getRegdId();
                            btn_markattendance.setEnabled(true);
                            isFingerPrint = false;

                            Log.d(TAG, "RegID: " + RegId + "registerd Id : " + regIdAfterReg);

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

    public class InsertRationCardDetailsDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd.setMessage("Uploading " + rationCardPhotoList.size() + " files..");
            pd.setCancelable(false);
            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {

            String res = "";

            try {

                MultipartUtility multipart =
                        new MultipartUtility(
                                ApplicationConstants.InsertRationCardDetails,
                                "UTF-8"
                        );

                multipart.addFormField("RegdID", params[0]);

                multipart.addFormField("UserId", params[1]);

                multipart.addFormField("Bocw_Dependent_Id", params[2]);

                multipart.addFormField("RationCardNo", params[3]);
                multipart.addFormField("RCID", params[4]);


                if (!params[4].equals("")) {

                    multipart.addFilePart(
                            "RationCardImage",
                            new File(params[5])
                    );
                }

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
            Log.d("InsertRationCard", result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        uploadedCount++;


                        if (uploadedCount == totalUploadCount) {

                            pd.dismiss();

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(context);

                            builder.setIcon(R.drawable.icon_success);

                            builder.setTitle("Success");

                            builder.setCancelable(false);

                            builder.setMessage(
                                    "Photos Uploaded Successfully."
                            );

                            builder.setPositiveButton(
                                    "OK",
                                    (dialog, which) -> submitData()
                            );

                            builder.show();
                        }

                    } else {

                        pd.dismiss();

                        Utilities.showAlertDialog(
                                context,
                                status,
                                message,
                                false
                        );

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
        getSupportActionBar().setTitle("Finger Print & Ration Card");

//        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    /// /////////////////////////////////////////Signature Pad///////////////////////////////////////////////////////////////

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

    /// ///////////////////////////////////////MORPH FINGER PRINT////////////////////////////////////////////////////////////

    private static MorphoTabletFPSensorDevice fpSensorCap;

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Log.i("deviceMan", deviceMan);
//        if (deviceMan.equalsIgnoreCase("Hena")) {
//            setDetaminiParams();
//        }
//    }

    @Override
    protected void onStart() {
        if (!deviceMan.equalsIgnoreCase("Hena")) {

            try {
                mfs100 = new MFS100(this);
                mfs100.SetApplicationContext(PatientFingerAndSignature_Activity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (mfs100 == null) {
                    mfs100 = new MFS100(this);
                    mfs100.SetApplicationContext(PatientFingerAndSignature_Activity.this);
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

    /// ///////////////////////////////////////MANTRA FINGETPRINT////////////////////////////////////////////////////////////

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
                        PatientFingerAndSignature_Activity.this.runOnUiThread(new Runnable() {
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


    private void launchCropWithDelay(Uri uri) {

        if (uri == null) {
            Utilities.showAlertDialog(context, "Alert", "Image not available. Please retry.", false);
            return;
        }


        pd.setMessage("Please wait..start cropping");
        pd.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            pd.dismiss();
            launchCrop(uri);
        }, 1000);

    }


    private void launchCrop(Uri uri) {
        if (uri == null) return;

        CropImageOptions options = new CropImageOptions();
        options.guidelines = CropImageView.Guidelines.ON;

        // ✅ Supported
        options.outputCompressFormat = Bitmap.CompressFormat.JPEG;
        options.outputCompressQuality = 80;   // reduce memory
        options.allowRotation = true;
        options.allowFlipping = true;

        CropImageContractOptions cropOptions =
                new CropImageContractOptions(uri, options);

        cropImageLauncher.launch(cropOptions);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == THUMB_CAMERA_REQUEST) {

//
//                CropImageOptions cropImageOptions = new CropImageOptions();
//                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
//                CropImageContractOptions options = new CropImageContractOptions(thumbURI, cropImageOptions);
//                cropImageLauncher.launch(options);


                launchCropWithDelay(thumbURI);

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


    String destinationFilename = "";


//
//
//    private void savefile(Uri sourceuri) {
//        pd.setMessage("Saving Image,\n Please wait...");
//        pd.setCancelable(false);
//        pd.show();
//
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
//
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//
//
//
//                Log.i("sourceuri1", "" + sourceuri);
//                String destinationFilename = "";
//                String filename = (int) (Math.random() * 99999 + 1) + "_FR.png";
//                destinationFilename = fingerPrintFolder + "/" + RegId + "_1_" + EmpCode + "_FR.png";
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
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            iv_biomatric.setImageBitmap(patientPicBm);
//                            pd.dismiss();
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("message", "Success");
//                            Message message = new Message();
//                            message.setData(bundle);
//
//                            if (!fingerPrintPath.isEmpty()) {
//                                isCaptured = true;
//                            }
    /// /                            handler.sendMessage(message);
    /// /
    /// /
    /// /                            handler.removeCallbacks(this);
//
//                        }
//                    });
//
//
//                    fingerPrintPath = destinationFilename;
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    pd.dismiss();
//                }
//
//            }
//        };
//
//        new Thread(runnable).start();
//
//    }


    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // new savefile implementation
    private void savefile(final Uri sourceuri) {
        pd.setMessage("Saving Image,\n Please wait...");
        pd.setCancelable(false);
        pd.show();

        ioExecutor.execute(() -> {
            String destinationFilename = "";
            try {
                Log.i("sourceuri1", "" + sourceuri);
                // Build filename/destination as your logic
                // you were using: fingerPrintFolder + "/" + RegId + "_1_" + EmpCode + "_FR.png"
                String filename = (int) (Math.random() * 99999 + 1) + "_FR.png";
                destinationFilename = fingerPrintFolder + "/" + RegId + "_1_" + EmpCode + "_FR.png";

                // Copy content URI to destination file using try-with-resources
                try (InputStream inputStream = context.getContentResolver().openInputStream(sourceuri);
                     BufferedInputStream bis = new BufferedInputStream(inputStream);
                     FileOutputStream fos = new FileOutputStream(destinationFilename, false);
                     BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                    bos.flush();
                }

                // Optionally compress the file (runs off UI thread)
                destinationFilename = compressImage(destinationFilename);

                // Decode a sampled bitmap (fast) for thumbnail (150x150)
                final Bitmap thumbBmp = decodeSampledBitmap(destinationFilename, 150, 150);

                // Save state variables (run on background thread)
                patientPicBm = thumbBmp;
                fingerPrintPath = destinationFilename;
                if (!fingerPrintPath.isEmpty()) {
                    isCaptured = true;
                }

                // Post UI update on main thread
                mainHandler.post(() -> {
                    try {
                        iv_biomatric.setImageBitmap(patientPicBm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        pd.dismiss();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                // ensure progress dialog dismissed on main thread
                mainHandler.post(() -> pd.dismiss());
            }
        });
    }

    // helper: decode sampled bitmap (use this if not already in your code)
    public Bitmap decodeSampledBitmap(String path, int reqW, int reqH) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqW, reqH);
        options.inJustDecodeBounds = false;
        // Use RGB_565 to reduce memory if you don't need alpha
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqW, int reqH) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqH || width > reqW) {
            int halfH = height / 2;
            int halfW = width / 2;

            while ((halfH / inSampleSize) >= reqH && (halfW / inSampleSize) >= reqW) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    public static long getTotalRamBytes(Context context) {
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        // On Android, MemoryInfo.totalMem gives total RAM in bytes (API 16+)
        return memInfo.totalMem;
    }

    /**
     * returns true if device total RAM is <= given GB threshold
     */
    public static boolean isMemoryAtOrBelowGB(Context context, int gbThreshold) {
        long totalBytes = getTotalRamBytes(context);
        long thresholdBytes = (long) gbThreshold * 1024L * 1024L * 1024L;
        return totalBytes <= thresholdBytes;
    }


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


    ActivityResultLauncher<Intent> cameraLauncherForRationCard =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");
                            Log.d(TAG, ":fileURI " + fileURI);
                            Log.d(TAG, ":fName " + fName);
                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_RC";
                                String compressedFilePath = Utilities.compressImageNew(context, fileURI, filename);
                                saveFileRationCard(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

    ActivityResultLauncher<Intent> cameraLauncherForThumbImage =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");
                            Log.d(TAG, ":fileURI " + fileURI);
                            Log.d(TAG, ":fName " + fName);
                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_FR";
                                String compressedFilePath = Utilities.compressImageNew(context, fileURI, filename);
                                saveFileThumbImage(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

    @SuppressLint("MissingPermission")
    public void saveFileThumbImage(String filePath) {
        String destinationFilename = filePath;
        patientPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = RegId + "_1_" + EmpCode + "_FR.jpg";
        String outputFilePath = getExternalCacheDir() + fName;
        try {
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            patientPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv_biomatric.setImageBitmap(patientPicBm);
        fingerPrintPath = outputFilePath;
    }


    @SuppressLint("MissingPermission")
    public void saveFileRationCard(String filePath) {
        isRationCardPhotoAvailable = true;
        String destinationFilename = filePath;
        rationCardPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = System.currentTimeMillis() + "_RC.jpg";
        String outputFilePath = getExternalCacheDir() + fName;
        // Step 2: Compress the Bitmap and store it in a new file
        try {
            // Open a file output stream to save the compressed image
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            // Compress the bitmap as JPEG with 80% quality
            rationCardPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            // Close the output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Add Image To List
        rationCardPhotoList.add(
                new RationCardPhotoModel(
                        outputFilePath,
                        fName
                )
        );

        adapter.notifyDataSetChanged();


        imv_self_declaration.setImageResource(
                R.drawable.icon_colorcamera
        );


        Toast.makeText(
                this,
                "Photo Added",
                Toast.LENGTH_SHORT
        ).show();


//        imv_self_declaration.setImageBitmap(rationCardPicBm);
        rationCardImagePath = outputFilePath;
    }

    @Override
    public void onBackPressed() {
        Utilities.showToastMessage("Please upload details", context, false);
    }
}