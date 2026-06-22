package com.myhindlab.abkat.activities;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PatientDetailsOnRegNo_Model;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadHealthCardPhoto_Activity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private CardView cv_info;
    private MaterialEditText edt_mcobcwwbno;
    private ImageView imv_search, imv_health_card;
    private TextView tv_name, tv_agegender, tv_city;
    private Uri healthCardURI;
    private Bitmap healthCardPicBm = null;
    private final int HEALTHCARD_CAMERA_REQUEST = 100;

    private Bitmap patientPicBm = null, renewalPicBm = null;
    private final int PATIENT_CAMERA_REQUEST = 100, RENEWAL_CAMERA_REQUEST = 300;


    private int imageType = 1;


    private File patientPicsFolder;

    private String userId, RegId, healthCardImagePath, campId, siteId, resigNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcard_photoupload);

        init();
        setUpToolbar();
        getSessionData();
        setDefault();
        setEventHandler();
    }

    private void init() {
        context = UploadHealthCardPhoto_Activity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

        cv_info = findViewById(R.id.cv_info);
        edt_mcobcwwbno = findViewById(R.id.edt_mcobcwwbno);
        imv_search = findViewById(R.id.imv_search);
        imv_health_card = findViewById(R.id.imv_health_card);
        tv_name = findViewById(R.id.tv_name);
        tv_agegender = findViewById(R.id.tv_agegender);
        tv_city = findViewById(R.id.tv_city);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Photos/");
        if (!patientPicsFolder.exists())
            patientPicsFolder.mkdirs();
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

    private void setDefault() {
        campId = getIntent().getStringExtra("campId");
        siteId = getIntent().getStringExtra("siteId");
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

//        imv_health_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
//                    return;
//                }
//
//                int randomEndtNo = (int) (Math.random() * 99999 + 1);
//                File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_HC.png");
//
//                healthCardURI = Uri.fromFile(patientImageFile);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
//                startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);
//            }
//        });

        imv_health_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 0;
//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
//                    return;
//                }

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
//                int randomEndtNo = (int) (Math.random() * 99999 + 1);
//                File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_PR.png");
//                patientURI = Uri.fromFile(patientImageFile);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, patientURI);
//                startActivityForResult(intent, PATIENT_CAMERA_REQUEST);

                int randomEndtNo = (int) (Math.random() * 999999 + 1);

                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                healthCardURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
                startActivityForResult(intent, PATIENT_CAMERA_REQUEST);
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

                            tv_name.setText(patientDetails.get(0).getEnglishName());

                            String gender = "";

                            if (patientDetails.get(0).getGender().equalsIgnoreCase("M")) {
                                gender = "Male";
                            } else if (patientDetails.get(0).getGender().equalsIgnoreCase("F")) {
                                gender = "Female";
                            }

                            tv_agegender.setText(patientDetails.get(0).getAge() + " / " + gender);
                            tv_city.setText(patientDetails.get(0).getLocalAddress() + " - " + patientDetails.get(0).getPincode());

                            imv_health_card.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));

                            RegId = patientDetails.get(0).getRegdId();

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
            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
//                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(UploadHealthCardPhoto_Activity.this);

                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                CropImageContractOptions options = new CropImageContractOptions(healthCardURI, cropImageOptions);
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

//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String sourceFilename = sourceuri.getPath();
//        String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
//        String destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Photos/" + filename;
//
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
//        healthCardPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        healthCardPicBm = Bitmap.createScaledBitmap(healthCardPicBm, 150, 150, false);
//        imv_health_card.setImageBitmap(healthCardPicBm);
//        healthCardImagePath = destinationFilename;
//
//        new UploadPatientHealthCardPhoto().execute();
//    }

//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//
//        String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
//        destinationFilename = getExternalCacheDir() + "/" + filename;
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
//        destinationFilename = compressImage(destinationFilename);
//        patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
//        imv_health_card.setImageBitmap(patientPicBm);
//        healthCardImagePath = destinationFilename;
//
//        new UploadPatientHealthCardPhoto().execute();
//
//
//    }
//


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

                String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
                destinationFilename = getExternalCacheDir() + "/" + filename;


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
                            imv_health_card.setImageBitmap(patientPicBm);
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


                    healthCardImagePath = destinationFilename;

                    new UploadPatientHealthCardPhoto().execute();



                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }


    private class UploadPatientHealthCardPhoto extends AsyncTask<String, Integer, String> {

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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.patientRegistrationHandlerHealthCard, "UTF-8");
                multipart.addFormField("RegdId", RegId);
                multipart.addFormField("SiteId", siteId);
                multipart.addFormField("CreatedBy", userId);
                multipart.addFilePart("FileName", new File(healthCardImagePath));
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

                    if (status.equalsIgnoreCase("Success")) {
                        Utilities.showAlertDialog(context, "Success", "Photo uploaded successfully", true, "OK", (dialog, which) -> {
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("HealthScreeningAcknowledgementConfirmation_Activity"));
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
                            finish();
                        });
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

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Photo");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
