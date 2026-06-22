package com.myhindlab.abkat.activities;

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
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.UnregisteredWorkersModel;
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
import static com.myhindlab.abkat.utilities.Utilities.CAMERA_REQUEST;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

public class UnregisteredWorkerRegNumEntry_Activity extends AppCompatActivity {
    private Context context;
    private UserSessionManager session;
    private MaterialEditText edt_fname, edt_mname, edt_lname, edt_marathi_name, edt_aadhaarno, edt_moblieno, edt_dob, edt_age, edt_permanant_address,
            edt_local_address, edt_city, edt_pincode, edt_workerregno;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female;
    private Button btn_register;
    private ProgressDialog pd;
    private String USERID = "", SiteDetailId = "", genderId = "", title = "", imagePath;
    private UnregisteredWorkersModel patientDetails;
    private ImageView imv_picphoto;
    private Uri photoURI;
    private File cardPhotoFolder;
    private Bitmap cardPhotoBm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregisteredworker_regnumentry);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();

    }

    private void init() {
        context = UnregisteredWorkerRegNumEntry_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        edt_fname = findViewById(R.id.edt_fname);
        edt_mname = findViewById(R.id.edt_mname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_marathi_name = findViewById(R.id.edt_marathi_name);
        edt_aadhaarno = findViewById(R.id.edt_aadhaarno);
        edt_moblieno = findViewById(R.id.edt_moblieno);
        edt_dob = findViewById(R.id.edt_dob);
        edt_age = findViewById(R.id.edt_age);
        edt_permanant_address = findViewById(R.id.edt_permanant_address);
        edt_local_address = findViewById(R.id.edt_local_address);
        edt_city = findViewById(R.id.edt_city);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_workerregno = findViewById(R.id.edt_workerregno);
        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        imv_picphoto = findViewById(R.id.imv_picphoto);
        btn_register = findViewById(R.id.btn_register);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        cardPhotoFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Card Photos/");
        if (!cardPhotoFolder.exists())
            cardPhotoFolder.mkdirs();

    }

    private void setDefaults() {
        SiteDetailId = getIntent().getStringExtra("SiteDetailId");

        patientDetails = (UnregisteredWorkersModel) getIntent().getSerializableExtra("patientDetails");

        edt_fname.setText(patientDetails.getFname());
        edt_mname.setText(patientDetails.getMname());
        edt_lname.setText(patientDetails.getLname());
        edt_marathi_name.setText(patientDetails.getMaratiName());
        edt_aadhaarno.setText(patientDetails.getUID());
        edt_moblieno.setText(patientDetails.getMobileNo());
        edt_dob.setText(patientDetails.getBirth_Date());
        edt_age.setText(patientDetails.getAge());
        edt_permanant_address.setText(patientDetails.getPermanentAddress());
        edt_local_address.setText(patientDetails.getLocalAddress());
        edt_city.setText(patientDetails.getLocation());
        edt_pincode.setText(patientDetails.getPinCode());


        if (patientDetails.getTitle().equalsIgnoreCase("Mr.")) {
            rb_mr.setChecked(true);
        } else if (patientDetails.getTitle().equalsIgnoreCase("Mrs.")) {
            rb_mrs.setChecked(true);
        } else if (patientDetails.getTitle().equalsIgnoreCase("Ms.")) {
            rb_ms.setChecked(true);
        }

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            rb_male.setChecked(true);
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            rb_female.setChecked(true);
        }
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                USERID = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        imv_picphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }

//                File patientImageFile = new File(cardPhotoFolder, "healthcard.png");
//
//                photoURI = Uri.fromFile(patientImageFile);
//                Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                pickImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(pickImage, CAMERA_REQUEST);



                int randomEndtNo = (int) (Math.random() * 999999 + 1);

                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                photoURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
//                    CropImage.activity(photoURI).setGuidelines(CropImageView.Guidelines.ON).start(UnregisteredWorkerRegNumEntry_Activity.this);
                }
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void savefile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String sourceFilename = sourceuri.getPath();
        String filename = "healthcard.png";
//        String destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Card Photos/" + filename;
        String destinationFilename = getExternalCacheDir()+"/" + filename;

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

        cardPhotoBm = BitmapFactory.decodeFile(destinationFilename);
        destinationFilename = compressImage(destinationFilename);
        long fileSizeInBytes = new File(destinationFilename).length();
        fileSizeInBytes = fileSizeInBytes / 1024;
        Log.i("fileSizeInBytes", fileSizeInBytes + "");
        new File(destinationFilename);
        imagePath = destinationFilename;
        cardPhotoBm = Bitmap.createScaledBitmap(cardPhotoBm, 150, 150, false);
        imv_picphoto.setImageBitmap(cardPhotoBm);
    }

    private void submitData() {
        if (edt_workerregno.getText().toString().trim().isEmpty()) {
            edt_workerregno.setError("Please enter valid registration number");
            return;
        }

        if (cardPhotoBm == null) {
            Utilities.showToastMessage("Please capture card photo", context, false);
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new InsCW_RegistrationDetails().execute(
                    patientDetails.getUnRegWorkerId(),
                    edt_workerregno.getText().toString(),
                    USERID,
                    imagePath
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    public class InsCW_RegistrationDetails extends AsyncTask<String, Void, String> {

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
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.workerRegNoAssignHandler, "UTF-8");

                multipart.addFormField("UnRegWorkerId", params[0]);
                multipart.addFormField("RegdNo", params[1]);
                multipart.addFormField("USERID", params[2]);
                multipart.addFilePart("File", new File(params[3]));

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
                        new UnregisteredRegisteredWorkerList_Activity.GetUnRegisterUserDetails().execute(SiteDetailId);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Worker registration number submitted successfully");
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

//    public class InsCW_RegistrationDetails extends AsyncTask<String, Void, String> {
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
//            param.add(new ParamsPojo("UnRegWorkerId", params[0]));
//            param.add(new ParamsPojo("RegdNo", params[1]));
//            param.add(new ParamsPojo("USERID", params[2]));
//            res = WebServiceCall.APICall(ApplicationConstants.InsCW_RegistrationDetails, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            try {
//                pd.dismiss();
//
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                    JSONObject obj = new JSONObject(result);
//                    String status = obj.getString("status");
//                    String message = obj.getString("message");
//                    if (status.equalsIgnoreCase("Success")) {
//                        new UnregisteredRegisteredWorkerList_Activity.GetUnRegisterUserDetails().execute(SiteDetailId);
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setIcon(R.drawable.icon_success);
//                        builder.setTitle("Success");
//                        builder.setCancelable(false);
//                        builder.setMessage("Worker registration number submitted successfully");
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
//            }
//        }
//    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Unregistered Worker's List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
