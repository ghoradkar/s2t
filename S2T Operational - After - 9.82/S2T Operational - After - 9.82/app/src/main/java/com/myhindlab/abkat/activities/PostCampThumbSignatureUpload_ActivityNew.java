package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.fingerprint.dao.AuthBfdCap;
import com.myhindlab.abkat.fingerprint.fps.MorphoTabletFPSensorDevice;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PostCampThumbSignatureUpload_ActivityNew extends AppCompatActivity implements AuthBfdCap, MFS100Event {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private CardView cv_finger_print, cv_signature, cv_uploaded_details;
    private RadioGroup rg_selection;
    private RadioButton rb_thumb, rb_signature;
    private EditText edt_beneficiaryname, edt_gender, edt_age;
    private ImageView iv_biomatric, iv_uploaded_image;
    private SignaturePad signature_pad;
    private TextView tv_finger_print_not_available;
    private Button btn_clearpad, btn_register;

    private PostCampBeneficiaryListModel.OutputBean beneficiaryDetails;

    private String userId, RegId, campId, isAdmin;

    private File signatureImageFile, patientSignFolder, fingerPrintFolder;

    private String TAG = getClass().getName();
    private String fingerImage = "";
    private boolean isFingerPrint = false;
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

    private enum ScannerAction {
        Capture, Verify
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_camp_thumb_signature_upload_new);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = PostCampThumbSignatureUpload_ActivityNew.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        cv_finger_print = findViewById(R.id.cv_finger_print);
        cv_signature = findViewById(R.id.cv_signature);
        cv_uploaded_details = findViewById(R.id.cv_uploaded_details);
        rg_selection = findViewById(R.id.rg_selection);
        rb_thumb = findViewById(R.id.rb_thumb);
        rb_signature = findViewById(R.id.rb_signature);
        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        iv_biomatric = findViewById(R.id.iv_biomatric);
        iv_uploaded_image = findViewById(R.id.iv_uploaded_image);

        tv_finger_print_not_available = findViewById(R.id.tv_finger_print_not_available);
        signature_pad = findViewById(R.id.signature_pad);
        btn_clearpad = findViewById(R.id.btn_clearpad);
        btn_register = findViewById(R.id.btn_register);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }


//        patientSignFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Signature/");
        patientSignFolder =getExternalCacheDir();
//        if (!patientSignFolder.exists())
//            patientSignFolder.mkdirs();

//        fingerPrintFolder = new File(Environment.getExternalStorageDirectory(), "/Health Checkup/ThumbData/");
        fingerPrintFolder = getExternalCacheDir();
//        if (!fingerPrintFolder.exists()) {
//            fingerPrintFolder.mkdirs();
//        }

    }

    private void getSessionData() {
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
        isAdmin = getIntent().getStringExtra("isAdmin");
        beneficiaryDetails = (PostCampBeneficiaryListModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");
        RegId = beneficiaryDetails.getRegdId();
        campId = beneficiaryDetails.getCAMPID();

        edt_beneficiaryname.setText(beneficiaryDetails.getBeneficiaryName());
        edt_gender.setText(beneficiaryDetails.getGender());
        edt_age.setText(beneficiaryDetails.getAge());

        if (beneficiaryDetails.getPostCampAcknowlegedmentFile()!=null && !beneficiaryDetails.getPostCampAcknowlegedmentFile().equals("NA")) {
            String url = BuildConfig.domain + "/CampDocs/PostCampAcknowlegedmentImage/" + beneficiaryDetails.getPostCampAcknowlegedmentFile();
            Picasso.with(context)
                    .load(url)
                    .into(iv_uploaded_image);

            rg_selection.setVisibility(View.GONE);
            cv_finger_print.setVisibility(View.GONE);
            cv_signature.setVisibility(View.GONE);
            cv_uploaded_details.setVisibility(View.VISIBLE);
            btn_register.setVisibility(View.GONE);
            tv_finger_print_not_available.setVisibility(View.GONE);

        } else {
            if (isAdmin.equals("1")) {
                rg_selection.setVisibility(View.GONE);
                cv_finger_print.setVisibility(View.GONE);
                cv_signature.setVisibility(View.GONE);
                cv_uploaded_details.setVisibility(View.GONE);
                btn_register.setVisibility(View.GONE);
                tv_finger_print_not_available.setVisibility(View.VISIBLE);
            } else {

                rg_selection.setOnCheckedChangeListener((group, checkedId) -> {
                    switch (checkedId) {
                        case R.id.rb_thumb:
                            cv_finger_print.setVisibility(View.VISIBLE);
                            cv_signature.setVisibility(View.GONE);
                            break;

                        case R.id.rb_signature:
                            cv_finger_print.setVisibility(View.GONE);
                            cv_signature.setVisibility(View.VISIBLE);
                            break;
                    }
                });

                rg_selection.check(R.id.rb_thumb);
            }
        }

    }


    private void setEventHandler() {


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

        iv_biomatric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (deviceMan.equalsIgnoreCase("Hena")) {
                    setDetaminiParams();
                    if (!isCaptured)
                        if (isWorking) {
                            Utilities.showToastMessage("Please let process to complete first", context, false);
                        } else {
                            fingerImage = RegId + "_1_" + userId + "_FR.png";
                            capture();
                            Utilities.showToastMessage("Place your finger on the sensor", context, false);
                        }
                } else {
                    fingerImage = RegId + "_1_" + userId + "_FR.png";

                    Utilities.showToastMessage("Place your finger on the sensor", context, false);

                    scannerAction = ScannerAction.Capture;
                    if (!isCaptureRunning) {
                        StartSyncCapture();
                    }
                }

            }
        });

        btn_register.setOnClickListener(v -> {
            String fileName = "", filePath = "", type = "";

            if (rb_thumb.isChecked()) {
                if (!isCaptured) {
                    Utilities.showAlertDialog(context, "Alert", "Please capture finger print of beneficiary", false);
                    return;
                }
                type = "1";
                File fingerPrintFile = new File(fingerPrintFolder, fingerImage);
                fileName = fingerImage;
                filePath = fingerPrintFile.getAbsolutePath();
            } else if (rb_signature.isChecked()) {
                if (!isSignedByUser) {
                    Utilities.showAlertDialog(context, "Alert", "Please sign on the signature pad", false);
                    return;
                }
                type = "2";
                Bitmap signatureBitmap = signature_pad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    fileName = signatureImageFile.getName();
                    filePath = signatureImageFile.getAbsolutePath();
                }
            }

            if (Utilities.isNetworkAvailable(context)) {
                new PostCampAcknowlegedment().execute(
                        RegId,
                        userId,
                        type,
                        filePath
                );
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }

        });
    }

    public class PostCampAcknowlegedment extends AsyncTask<String, Void, String> {

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
            try {
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.PostCampAcknowlegedment, "UTF-8");

                multipart.addFormField("RegdId", params[0]);
                multipart.addFormField("CreatedBy", params[1]);
                multipart.addFormField("ackType", params[2]);
                multipart.addFilePart("FileName", new File(params[3]));

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
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Data uploaded successfully");
                        builder.setPositiveButton("ok", (dialog, which) -> finish());
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

        getSupportActionBar().setTitle("Ack. and Thumb");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
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
                mfs100.SetApplicationContext(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (mfs100 == null) {
                    mfs100 = new MFS100(this);
                    mfs100.SetApplicationContext(this);
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
                        runOnUiThread(new Runnable() {
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
//        Toast.makeText(context, capture_success, Toast.LENGTH_SHORT).show();
    }
}