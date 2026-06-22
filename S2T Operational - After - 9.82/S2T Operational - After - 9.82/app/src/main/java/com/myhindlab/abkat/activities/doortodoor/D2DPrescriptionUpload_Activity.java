package com.myhindlab.abkat.activities.doortodoor;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import static com.myhindlab.abkat.utilities.Utilities.compressImage;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.rengwuxian.materialedittext.MaterialEditText;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class D2DPrescriptionUpload_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private String TAG = getClass().getName();

    private String campId, siteId, registrationNo = "";
    private CheckBox cb_finger_issue, cb_signature_applicable;
    private ImageView imv_search, iv_biomatric;
    private LinearLayout ll_signature;
    private SignaturePad signature_pad;
    private Button btn_clearpad;
    private RadioButton rb_mr, rb_mrs, rb_ms;
    private MaterialEditText edt_mcobcwwbno, edt_name, edt_gender, edt_age, edt_aadhaarno, edt_moblieno,
            edt_dob, edt_address, edt_city, edt_pincode;
    private Button btnUploadPrescription;
    private String RegId = "", EmpCode, LATITUDE = "0", LONGITUDE = "0", isThumbExist = "0", fingerPrintPath = "", signaturePath = "";
    private UserSessionManager session;
    private File signatureImageFile, patientSignFolder, fingerPrintFolder;

    private String fingerImage = ""/*,registerTemp = ""*/;
    private boolean isFingerPrint = false, isFingerPrintIssue = false, isSignatureApplicable = true;
    private boolean isSignedByUser = false;
    private Bitmap patientPicBm;
    private String patientImagePath = "";
    private ImageView imvPrescription;
    private Uri patientURI;
    private int imageType = 0;
    private final int PATIENT_CAMERA_REQUEST = 100;
    private File patientPicsFolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2d_prescription_upload);

        init();
        setUpToolbar();
        setDefault();
        getSessionData();
        setEvenListener();
    }

    private void init() {
        context = D2DPrescriptionUpload_Activity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);
        imvPrescription = findViewById(R.id.imvPrescription);
        btnUploadPrescription = findViewById(R.id.btnUploadPrescription);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            patientPicsFolder = getExternalCacheDir();
        } else {
            patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Prescription/");
            if (!patientPicsFolder.exists())
                patientPicsFolder.mkdirs();

        }


    }

    void setEvenListener() {
        btnUploadPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                submitData();
            }
        });
        imvPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                int randomEndtNo = (int) (Math.random() * 99999 + 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_PR.png");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    patientURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, patientURI);
                    startActivityForResult(intent, PATIENT_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_PR.png");
                    patientURI = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, patientURI);
                    startActivityForResult(intent, PATIENT_CAMERA_REQUEST);
                }

            }
        });
    }


    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    // Use the cropped image URI.
                    String path = result.getUriFilePath(context, true);

                    savefile(result.getUriContent());

//                    if (imageType == 0) {
//                        savefile(result.getUriContent());
//                    } else if (imageType == 1) {
//                        saveAckfile(result.getUriContent());
//                    }

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
            if (requestCode == PATIENT_CAMERA_REQUEST) {
//                CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPrescriptionUpload_Activity.this);

                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                CropImageContractOptions options = new CropImageContractOptions(patientURI, cropImageOptions);
                cropImageLauncher.launch(options);

            }
        }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                savefile(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }

    }
//
//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
//        destinationFilename = patientPicsFolder + filename;
//        //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
//
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
//        destinationFilename = Utilities.compressImage(destinationFilename);
//        patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
//        imvPrescription.setImageBitmap(patientPicBm);
//        patientImagePath = destinationFilename;
//
//
//    }
//


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
                String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
                destinationFilename = patientPicsFolder + filename;



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
                            imvPrescription.setImageBitmap(patientPicBm);
                            pd.dismiss();

                            Bundle bundle = new Bundle();
                            bundle.putString("message", "Success");
                            Message message = new Message();
                            message.setData(bundle);
//                            handler.sendMessage(message);
//
//
//                            handler.removeCallbacks(this);

                        }
                    });


                    patientImagePath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }
    private void setDefault() {
//        ConstructionSitesList_Model siteDetails = new ConstructionSitesList_Model();
//        siteDetails = (ConstructionSitesList_Model) getIntent().getSerializableExtra("siteDetails");
//        campId = getIntent().getStringExtra("campId");
        siteId = getIntent().getStringExtra("siteId");
        registrationNo = getIntent().getStringExtra("registrationNo");

//        if (registrationNo != null && !registrationNo.equalsIgnoreCase("")) {
//            edt_mcobcwwbno.setFocusable(false);
//            edt_mcobcwwbno.setClickable(false);
//            if (Utilities.isNetworkAvailable(context)) {
////                new GetWorkerInfroFromWorkerRegid().execute(registrationNo);
//                btnUploadPrescription.setEnabled(false);
//            } else {
//                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//            }
//        }
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


    private void submitData() {
        if (patientImagePath.isEmpty()) {
            Utilities.showToastMessage("Please select photo first", context, false);
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new UploadPrescription().execute(registrationNo, EmpCode, EmpCode, patientImagePath);
//            new UploadPrescription().execute(
//                    registrationNo,
//                    EmpCode,
//                    EmpCode,
//                    patientImagePath
//            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class UploadPrescription extends AsyncTask<String, Integer, String> {

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
            Log.d("Prescription Params", Arrays.toString(params));
            try {

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.D2DPrescription, "UTF-8");
                multipart.addFormField("Regdid", params[0]);
                multipart.addFormField("UserID", params[1]);
                multipart.addFormField("CreatedBy", params[2]);
                multipart.addFilePart("PrescriptionImagePath", new File(params[3]));

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

            Log.d("Prescription Res ", result);
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Sucess")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage(message);
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();

                    } else {

                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


//    public class GetWorkerInfroFromWorkerRegid extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("EmpCode", params[0]));
//            res = WebServiceCall.APICall(ApplicationConstants.GetWorkerInfroFromWorkerRegid, param);
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
//                    ArrayList<PatientDetailsOnRegNo_Model> patientDetails = new ArrayList<>();
//                    PatientDetailsOnRegNo_Pojo pojoDetails = new Gson().fromJson(result, PatientDetailsOnRegNo_Pojo.class);
//                    type = pojoDetails.getStatus();
//                    message = pojoDetails.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        patientDetails = pojoDetails.getOutput();
//                        if (patientDetails.size() > 0) {
//                            isThumbExist = patientDetails.get(0).getIsThumbExist();
//                            if (isThumbExist.equalsIgnoreCase("1")) {
//                                iv_biomatric.setVisibility(View.GONE);
//                                isCaptured = true;
//                            } else {
//                                iv_biomatric.setVisibility(View.VISIBLE);
//                                isCaptured = false;
//                            }
//
//                            iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));
//                            isWorking = false;
//                            isCaptureRunning = false;
//                            isCaptured = false;
//
//                            edt_mcobcwwbno.setText(patientDetails.get(patientDetails.size() - 1).getRegdNo());
//
//                            if (patientDetails.get(patientDetails.size() - 1).getTitle().equalsIgnoreCase("Mr.")) {
//                                rb_mr.setChecked(true);
//                            } else if (patientDetails.get(patientDetails.size() - 1).getTitle().equalsIgnoreCase("Mrs.")) {
//                                rb_mrs.setChecked(true);
//                            } else if (patientDetails.get(patientDetails.size() - 1).getTitle().equalsIgnoreCase("Ms.")) {
//                                rb_ms.setChecked(true);
//                            }
//
//                            if (patientDetails.get(patientDetails.size() - 1).getGender().equalsIgnoreCase("M")) {
//                                edt_gender.setText("Male");
//                            } else if (patientDetails.get(patientDetails.size() - 1).getGender().equalsIgnoreCase("F")) {
//                                edt_gender.setText("Female");
//                            }
//
//                            edt_name.setText(patientDetails.get(patientDetails.size() - 1).getEnglishName());
//
//                            edt_aadhaarno.setText(patientDetails.get(patientDetails.size() - 1).getUID().replace("-", ""));
//
//                            edt_dob.setText(patientDetails.get(patientDetails.size() - 1).getDOBFormated());
//
//                            edt_age.setText(patientDetails.get(patientDetails.size() - 1).getAge());
//
////                            if (edt_age.getText().toString().isEmpty()) {
////                                String age = patientDetails.get(0).getDOBFormated();
////                                String[] ageparts = age.split("-");
////                                String year = ageparts[0];
////                                String month = ageparts[1];
////                                String day = ageparts[2];
////
////                                edt_age.setText(getAge(Integer.parseInt(year),
////                                        Integer.parseInt(month),
////                                        Integer.parseInt(day)));
////                            }
//
//                            edt_moblieno.setText(patientDetails.get(patientDetails.size() - 1).getMobileNo());
//
//                            edt_address.setText(patientDetails.get(patientDetails.size() - 1).getPermanentAddress());
//
//                            edt_city.setText(patientDetails.get(patientDetails.size() - 1).getLocation());
//
//                            edt_pincode.setText(patientDetails.get(patientDetails.size() - 1).getPincode());
//
//                            RegId = patientDetails.get(patientDetails.size() - 1).getRegdId();
//                            btnUploadPrescription.setEnabled(true);
//                            isFingerPrint = false;
//                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
//    }

//    public class InsertSignatureandThumbDetails extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//
//            Log.d("insert sign", Arrays.toString(params));
//            try {
//                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.InsertSignatureandThumbDetails, "UTF-8");
//
//                multipart.addFormField("RegdId", params[0]);
//                multipart.addFormField("SiteId", params[1]);
//                multipart.addFormField("CampId", params[2]);
//                multipart.addFormField("IsSignature", params[3]);
//                multipart.addFormField("IsDeviceIssue", params[4]);
//                multipart.addFormField("CreatedBy", params[5]);
//                if (!params[6].equals(""))
//                    multipart.addFilePart("File1", new File(params[6]));
//                if (!params[7].equals(""))
//                    multipart.addFilePart("File2", new File(params[7]));
//
//                List<String> response = multipart.finish();
//                for (String line : response) {
//                    res = res + line;
//                }
//                return res;
//            } catch (IOException ex) {
//                return ex.toString();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.d("InsertSign", result);
//            try {
//                pd.dismiss();
//
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                    JSONObject obj = new JSONObject(result);
//                    String status = obj.getString("status");
//                    String message = obj.getString("message");
//                    if (status.equalsIgnoreCase("Success")) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setIcon(R.drawable.icon_success);
//                        builder.setTitle("Success");
//                        builder.setCancelable(false);
//                        builder.setMessage("Data uploaded successfully");
//                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        });
//                        builder.show();
//
//                    } else {
//                        Utilities.showAlertDialog(context, status, message, false);
//                    }
//                } else
//                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
//            }
//        }
//
//    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Prescription");

    }


    @Override
    protected void onResume() {
        super.onResume();


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

    @Override
    public void onBackPressed() {
        Utilities.showToastMessage("Please upload prescription", context, false);
    }
}