package com.myhindlab.abkat.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;
import static com.myhindlab.abkat.utilities.Utilities.isBarCodeValid;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PresentPatientList_Model;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HealthscreeningAntigenTest_Activity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private String TAG = getClass().getName();
    private UserSessionManager session;
    private MaterialEditText edt_beneficiaryname, edt_gender, edt_age, edt_result, edt_comment, edt_barcode1;
    private String userId, campId, bmiStatus = "0", genderId, healthScreentype, MARITALSTATUSID, familyOperationStatus,
            smokingStatus, alcoholStatus, tobaccoStatus, drugsStatus;
    private PresentPatientList_Model patientDetails;
    private Button btn_register;
    private ImageView imv_samplephoto;
    private Uri imageURI;
    private final int CAMERA_REQUEST = 100;
    private String imagePath = "", labCode = "";
    private ImageView imv_barcode1;

    private File patientPicsFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_antigen_test);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = HealthscreeningAntigenTest_Activity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_result = findViewById(R.id.edt_result);
        edt_comment = findViewById(R.id.edt_comment);
        btn_register = findViewById(R.id.btn_register);
        imv_samplephoto = findViewById(R.id.imv_samplephoto);
        imv_barcode1 = findViewById(R.id.imv_barcode1);
        edt_barcode1 = findViewById(R.id.edt_barcode1);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Antigen Test");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");
        healthScreentype = getIntent().getStringExtra("healthScreentype");

        edt_beneficiaryname.setText(patientDetails.getEnglishName());
//        edt_beneficiaryno.setText(patientDetails.getRegdNo());
//        edt_mobileno.setText(patientDetails.getMobileNo());

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            edt_gender.setText("Male");
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            edt_gender.setText("Female");
        } else {
            edt_gender.setText("");
        }

        genderId = patientDetails.getGender();
        edt_age.setText(patientDetails.getAge());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Camp Approval Letter/");
        if (!patientPicsFolder.exists())
            patientPicsFolder.mkdirs();
        if (Utilities.isNetworkAvailable(context)) {
            new GetLabCode().execute(campId);
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

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

    private void setEventHandler() {
        imv_barcode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
                    return;
                }

                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);
            }
        });

        edt_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> resultList = new ArrayList<>();
                resultList.add(0, "Positive");
                resultList.add(1, "Negative");
                showPaymentModeDialog(resultList);
            }
        });

        imv_samplephoto.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                return;
            }
//            int randomEndtNo = (int) (Math.random() * 999999 + 1);
//            File patientImageFile = new File(patientPicsFolder, randomEndtNo + ".png");
//            imageURI = Uri.fromFile(patientImageFile);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
//            startActivityForResult(intent, CAMERA_REQUEST);

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
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    private void submitData() {
        if (edt_result.getText().toString().trim().isEmpty()) {
            edt_result.setError("Select Result");
            return;
        }
        if (edt_barcode1.getText().toString().trim().isEmpty()) {
            edt_barcode1.setError("enter Barcode");
            return;
        }

        if (!isBarCodeValid(edt_barcode1.getText().toString().trim())) {
            edt_barcode1.setError("Please enter valid barcode");
            return;
        }


        if (imagePath.equals("")) {
            Utilities.showToastMessage("Please click Antigen Test sample photo", context, false);
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new CheckBarocde().execute();
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }


    }

    private class CheckBarocde extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;
        TextView et = null;

        private CheckBarocde() {
            this.et = et;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");
                    if (status.equalsIgnoreCase("Success")) {//wrong spelling from server
//                        JSONArray jsonarr = obj1.getJSONArray("output");
//                        if (jsonarr.length() > 0) {
//                            for (int i = 0; i < jsonarr.length(); i++) {
//                                JSONObject jsonObj = jsonarr.getJSONObject(i);
//                                String isExist = jsonObj.getString("Result");
//                                if (isExist.equalsIgnoreCase("YES")) {
//                                    Utilities.showAlertDialog(context, "Alert", "Barcode Already Exist",false);
//                                    return;
//                                } else {
                        if (Utilities.isNetworkAvailable(context)) {
                            new SubmitTest().execute(
                                    String.valueOf(patientDetails.getRegdId()),
                                    edt_barcode1.getText().toString(),
                                    edt_beneficiaryname.getText().toString(),
                                    edt_age.getText().toString(),
                                    edt_gender.getText().toString(),
                                    imagePath,
                                    edt_result.getText().toString(),
                                    edt_comment.getText().toString(),
                                    labCode,
                                    userId
                            );
//                                    } else {
//                                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                                    }
//                                }
//                            }
//
//                        } else {
//                            Utilities.showAlertDialog(context, "Empty", "List is empty ...",false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);

                    }
                } else {
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverResponse1 = "";
            try {

                RequestBody formBody1 = new FormBody.Builder()
                        .add("Barcode", edt_barcode1.getText().toString().trim())

                        .build();

                // String distwiseCountUrl = ApplicationConstants.surveyBaseUrl + "date_wise_count_total.php";
                String url = ApplicationConstants.webservice + "GetBarcodeExistInAntigen";


                OkHttpClient client1 = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()

                        .url(url)
                        .post(formBody1)
                        .build();

                Response response1 = client1.newCall(request).execute();
                serverResponse1 = response1.body().string();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResponse1;

        }
    }

    private class SubmitTest extends AsyncTask<String, Integer, String> {

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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.AntigenTestDetails, "UTF-8");
                multipart.addFormField("Regid", params[0]);
                multipart.addFormField("Barcode", params[1]);
                multipart.addFormField("PatientName", params[2]);
                multipart.addFormField("Age", params[3]);
                multipart.addFormField("Gender", params[4]);
                multipart.addFilePart("PhotoPath", new File(params[5]));
                multipart.addFormField("Result", params[6]);
                multipart.addFormField("Comment", params[7]);
                multipart.addFormField("LabCode", params[8]);
                multipart.addFormField("CreatedBy", params[9]);
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
                        new SubmitAntigenData().execute(String.valueOf(patientDetails.getRegdId()), String.valueOf(patientDetails.getSiteId()), campId, userId);
                        Utilities.showAlertDialog(context, status, message, false);
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.icon_success);
                    builder.setTitle("Success");
                    builder.setCancelable(false);
                    builder.setMessage("Antigen Test result submitted successfully.");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();

                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);

            }
        }

    }

    public class SubmitAntigenData extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdID", params[0]));
            param.add(new ParamsPojo("SiteId", params[1]));
            param.add(new ParamsPojo("CampID", params[2]));
            param.add(new ParamsPojo("CreatedBy", params[3]));
            param.add(new ParamsPojo("TestId", "15"));

            res = WebServiceCall.APICall(ApplicationConstants.InsertBasicHealthtestForAntigen, ApplicationConstants.webservice, param);
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

    private void showPaymentModeDialog(ArrayList<String> resultList) {

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Result");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < resultList.size(); i++) {
            arrayAdapter.add(resultList.get(i));
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            edt_result.setText(resultList.get(which));

        });
        builderSingle.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 10001) {
                String requiredValue = data.getStringExtra("key");
                Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                edt_barcode1.setText(requiredValue);
            }
            if (requestCode == CAMERA_REQUEST) {
//                CropImage.activity(imageURI).setGuidelines(CropImageView.Guidelines.ON).start(HealthscreeningAntigenTest_Activity.this);
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
        Log.i("sourceuri1", "" + sourceuri);
        String destinationFilename = "";
        String filename = (int) (Math.random() * 999999 + 1) + ".png";
//        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Camp Approval Letter/" + filename;
        destinationFilename = getExternalCacheDir() + "/" + filename;

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
        imv_samplephoto.setImageBitmap(imagePicBm);
        imagePath = destinationFilename;
    }

    private class GetLabCode extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetAntigenLabCode, ApplicationConstants.webservice, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = obj.getJSONArray("output");
                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                        labCode = String.valueOf(jsonObject.getInt("Labcode"));
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }
}