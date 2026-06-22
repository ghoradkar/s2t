package com.myhindlab.abkat.activities;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
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
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

public class CloseBreastScreeningCamp extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private MaterialEditText edt_camp_id, edt_camp_date, edt_camp_district, edt_device_id, edt_number_of_tests;
    private ImageView imv_reqister_photo;
    private Button btn_save;
    private Uri imageURI;
    private final int CAMERA_REQUEST = 100;

    private File patientPicsFolder;
    private String userId, districtCode, imagePath = "", breastScreeningDeviceId, breastScreeningDeviceName;
    private static GetApprovedCampListDetailsForAppList selectedCamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_breast_screening_camp);
        init();
        getSessionDetails();
        setDefaults();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = CloseBreastScreeningCamp.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_camp_id = findViewById(R.id.edt_camp_id);
        edt_camp_date = findViewById(R.id.edt_camp_date);
        edt_camp_district = findViewById(R.id.edt_camp_district);
        edt_device_id = findViewById(R.id.edt_device_id);
        edt_number_of_tests = findViewById(R.id.edt_number_of_tests);
        imv_reqister_photo = findViewById(R.id.imv_reqister_photo);
        btn_save = findViewById(R.id.btn_save);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Close Screening/");
        if (!patientPicsFolder.exists())
            patientPicsFolder.mkdirs();
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
        selectedCamp = (GetApprovedCampListDetailsForAppList) getIntent().getSerializableExtra("selectedCamp");
        breastScreeningDeviceId = getIntent().getStringExtra("breastScreeningDeviceId");
        breastScreeningDeviceName = getIntent().getStringExtra("breastScreeningDeviceName");

        edt_camp_id.setText(selectedCamp.getCampId());
        edt_camp_date.setText(selectedCamp.getCampDate());
        edt_camp_district.setText(selectedCamp.getDISTNAME());
        edt_device_id.setText(breastScreeningDeviceName);
    }

    private void setEventHandler() {
        imv_reqister_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }
//                int randomEndtNo = (int) (Math.random() * 999999 + 1);
//                File patientImageFile = new File(patientPicsFolder, randomEndtNo + ".png");
//                imageURI = Uri.fromFile(patientImageFile);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
//                startActivityForResult(intent, CAMERA_REQUEST);

                int randomEndtNo = (int) (Math.random() * 999999 + 1);

                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                imageURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {
        if (edt_number_of_tests.getText().toString().trim().equals("")) {
            edt_number_of_tests.setError("Enter number of tests");
            return;
        }

        if (imagePath.equals("")) {
            Utilities.showToastMessage("Please click register photo", context, false);
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new InsertCloseBreastScreening().execute(
                    selectedCamp.getCampId(),
                    selectedCamp.getDISTLGDCODE(),
                    breastScreeningDeviceId,
                    edt_number_of_tests.getText().toString().trim(),
                    userId,
                    imagePath
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class InsertCloseBreastScreening extends AsyncTask<String, Integer, String> {

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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.InsertCloseBreastScreening, "UTF-8");
                multipart.addFormField("CampId", params[0]);
                multipart.addFormField("District", params[1]);
                multipart.addFormField("DeviceId", params[2]);
                multipart.addFormField("NumberOfTest", params[3]);
                multipart.addFormField("CreatedBy", params[4]);
                multipart.addFilePart("FileName", new File(params[5]));
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Camp screening closed successfully");
                        builder.setTitle("Success");
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", (dialog, id) -> {
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_ActivityFinish"));
                            finish();
                        });
                        AlertDialog alertD = builder.create();
                        alertD.show();
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
//                CropImage.activity(imageURI).setGuidelines(CropImageView.Guidelines.ON).start(CloseBreastScreeningCamp.this);
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

    public void savefile(Uri sourceuri) {
        String destinationFilename = "";
        String filename = (int) (Math.random() * 999999 + 1) + ".png";
//        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Close Screening/" + filename;
        destinationFilename = getExternalCacheDir()+"/" + filename;

        String sourceFilename = sourceuri.getPath();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
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

        Bitmap imagePicBm = BitmapFactory.decodeFile(destinationFilename);
        destinationFilename = compressImage(destinationFilename);
        imagePicBm = Bitmap.createScaledBitmap(imagePicBm, 150, 150, false);
        imv_reqister_photo.setImageBitmap(imagePicBm);
        imagePath = destinationFilename;
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Close Screening");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}