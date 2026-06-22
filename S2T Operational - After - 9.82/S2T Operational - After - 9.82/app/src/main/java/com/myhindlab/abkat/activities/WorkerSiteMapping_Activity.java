package com.myhindlab.abkat.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.CAMERA_REQUEST;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

public class WorkerSiteMapping_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private String EmpCode, DISTLGDCODE;
    private MaterialEditText edt_mcobcwwbno, edt_fname, edt_mname, edt_lname, edt_marathi_name, edt_aadhaarno, edt_moblieno, edt_dob, edt_age, edt_permanant_address,
            edt_local_address, edt_city, edt_pincode;
    private ImageView imv_search;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female;
    private Button btn_markattendance;
    private ProgressDialog pd;
    private ImageView imv_picphoto;
    private String RegId, SiteDetailId, imagePath;
    private Uri photoURI;
    private File cardPhotoFolder;
    private Bitmap cardPhotoBm;
    private String genderId = "", title = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workersite_mapping);

        init();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = WorkerSiteMapping_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        edt_mcobcwwbno = findViewById(R.id.edt_mcobcwwbno);
        edt_fname = findViewById(R.id.edt_fname);
        edt_mname = findViewById(R.id.edt_mname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_marathi_name = findViewById(R.id.edt_marathi_name);
        edt_aadhaarno = findViewById(R.id.edt_aadhaarno);
        edt_moblieno = findViewById(R.id.edt_moblieno);
        edt_age = findViewById(R.id.edt_age);
        edt_dob = findViewById(R.id.edt_dob);
        edt_permanant_address = findViewById(R.id.edt_permanant_address);
        edt_local_address = findViewById(R.id.edt_local_address);
        edt_city = findViewById(R.id.edt_city);
        edt_pincode = findViewById(R.id.edt_pincode);
        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        imv_search = findViewById(R.id.imv_search);
        imv_picphoto = findViewById(R.id.imv_picphoto);
        btn_markattendance = findViewById(R.id.btn_markattendance);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        cardPhotoFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Card Photos/");
        if (!cardPhotoFolder.exists())
            cardPhotoFolder.mkdirs();

    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                EmpCode = json.getString("EmpCode");
                DISTLGDCODE = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDefaults() {
        SiteDetailId = getIntent().getStringExtra("SiteDetailId");

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
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

        edt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int age = Integer.parseInt(getAge(Integer.parseInt(String.format("%02d", year)),
                                        Integer.parseInt(String.format("%02d", monthOfYear)),
                                        Integer.parseInt(String.format("%02d", dayOfMonth))));

                                if (age < 18) {
                                    Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years", false);
                                    return;
                                }
                                edt_dob.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                edt_age.setText(getAge(Integer.parseInt(String.format("%02d", year)),
                                        Integer.parseInt(String.format("%02d", monthOfYear)),
                                        Integer.parseInt(String.format("%02d", dayOfMonth))));

                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dpd1.getDatePicker().setCalendarViewShown(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        btn_markattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_mcobcwwbno.getText().toString().trim().isEmpty()) {
                    edt_mcobcwwbno.setError("Please enter worker registration number");
                    return;
                }

                if (rb_mr.isChecked()) {
                    title = "Mr.";
                } else if (rb_mrs.isChecked()) {
                    title = "Mrs.";
                } else if (rb_ms.isChecked()) {
                    title = "Ms.";
                } else {
                    Utilities.showToastMessage("Please select title", context, false);
                    return;
                }

                if (edt_fname.getText().toString().trim().isEmpty()) {
                    edt_fname.setError("Please enter first name");
                    return;
                }

//                if (edt_lname.getText().toString().trim().isEmpty()) {
//                    edt_lname.setError("Please enter last name");
//                    return;
//                }

//                if (edt_marathi_name.getText().toString().trim().isEmpty()) {
//                    edt_marathi_name.setError("Please enter marathi name");
//                    return;
//                }

//                if (edt_aadhaarno.getText().toString().trim().isEmpty()) {
//                    edt_aadhaarno.setError("Please enter aadhar number");
//                    return;
//                }

                if (!edt_aadhaarno.getText().toString().trim().isEmpty()) {
                    if (!Utilities.isaadharNumberValidate(edt_aadhaarno.getText().toString().trim())) {
                        edt_aadhaarno.setError("Please enter valid aadhar number");
                        return;
                    }
                }

//                if (edt_dob.getText().toString().trim().isEmpty()) {
//                    edt_dob.setError("Please select date of birth");
//                    return;
//                }

//                if (edt_age.getText().toString().trim().isEmpty()) {
//                    edt_age.setError("Please enter age");
//                    return;
//                }

//                if (edt_moblieno.getText().toString().trim().isEmpty()) {
//                    edt_moblieno.setError("Please enter mobile no.");
//                    return;
//                }

                if (!edt_moblieno.getText().toString().trim().isEmpty()) {
                    if (!Utilities.isMobileNo(edt_moblieno.getText().toString().trim())) {
                        edt_moblieno.setError("Please enter valid mobile no.");
                        return;
                    }
                }

                if (rb_male.isChecked()) {
                    genderId = "M";
                } else if (rb_female.isChecked()) {
                    genderId = "F";
                } else {
                    Utilities.showToastMessage("Please select gender", context, false);
                    return;
                }

//                if (edt_permanant_address.getText().toString().trim().isEmpty()) {
//                    edt_permanant_address.setError("Please enter permanant address");
//                    return;
//                }

//                if (edt_local_address.getText().toString().trim().isEmpty()) {
//                    edt_local_address.setError("Please enter local address");
//                    return;
//                }

//                if (edt_city.getText().toString().trim().isEmpty()) {
//                    edt_city.setError("Please enter locality");
//                    return;
//                }

//                if (edt_pincode.getText().toString().trim().isEmpty()) {
//                    edt_pincode.setError("Please enter pincode");
//                    return;
//                }

                if (!edt_pincode.getText().toString().trim().isEmpty()) {
                    if (!Utilities.isValidPincode(edt_pincode.getText().toString().trim())) {
                        edt_pincode.setError("Please enter valid pincode");
                        return;
                    }
                }

                if (cardPhotoBm == null) {
                    Utilities.showToastMessage("Please capture card photo", context, false);
                    return;
                }

                if (Utilities.isNetworkAvailable(context)) {
                    new GLO_ApprovedWorkerSiteMapping().execute(
                            SiteDetailId,
                            RegId,
                            "0",
                            EmpCode,
                            DISTLGDCODE,
                            title,
                            edt_fname.getText().toString().trim() + " " + edt_mname.getText().toString().trim() + " " + edt_lname.getText().toString().trim(),
                            edt_marathi_name.getText().toString().trim(),
                            edt_moblieno.getText().toString().trim(),
                            edt_dob.getText().toString().trim(),
                            edt_age.getText().toString().trim(),
                            genderId,
                            edt_permanant_address.getText().toString().trim(),
                            edt_local_address.getText().toString().trim(),
                            edt_pincode.getText().toString().trim(),
                            edt_city.getText().toString().trim(),
                            edt_aadhaarno.getText().toString().trim(),
                            imagePath);
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        imv_picphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }

                File patientImageFile = new File(cardPhotoFolder, "healthcard.png");

                photoURI = Uri.fromFile(patientImageFile);
                Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pickImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pickImage, CAMERA_REQUEST);
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
                            edt_mcobcwwbno.setText(patientDetails.get(0).getRegdNo());
                            btn_markattendance.setEnabled(true);

                            if (patientDetails.get(0).getTitle().equalsIgnoreCase("Mr.")) {
                                rb_mr.setChecked(true);
                            } else if (patientDetails.get(0).getTitle().equalsIgnoreCase("Mrs.")) {
                                rb_mrs.setChecked(true);
                            } else if (patientDetails.get(0).getTitle().equalsIgnoreCase("Ms.")) {
                                rb_ms.setChecked(true);
                            } else {
                                rb_mr.setChecked(true);
                            }

                            if (patientDetails.get(0).getGender().equalsIgnoreCase("M")) {
                                rb_male.setChecked(true);
                            } else if (patientDetails.get(0).getGender().equalsIgnoreCase("F")) {
                                rb_female.setChecked(true);
                            } else {
                                rb_male.setChecked(true);
                            }

//                            String string = patientDetails.get(0).getEnglishName();
//                            String[] parts = string.split(" ");
//                            String fname = parts[0];
//                            String mname = parts[1];
//                            String mlname = parts[2];
                            edt_fname.setText(patientDetails.get(0).getEnglishName());
//                            edt_mname.setText(mname);
//                            edt_lname.setText(mlname);

                            edt_marathi_name.setText(patientDetails.get(0).getMaratiName());

                            edt_aadhaarno.setText(patientDetails.get(0).getUID().replace("-", ""));

                            edt_dob.setText(patientDetails.get(0).getDOBFormated());

                            edt_age.setText(patientDetails.get(0).getAge());

                            edt_moblieno.setText(patientDetails.get(0).getMobileNo());

                            edt_permanant_address.setText(patientDetails.get(0).getPermanentAddress());

                            edt_local_address.setText(patientDetails.get(0).getLocalAddress());

                            edt_city.setText(patientDetails.get(0).getLocation());

                            edt_pincode.setText(patientDetails.get(0).getPincode());

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

    private class GLO_ApprovedWorkerSiteMapping extends AsyncTask<String, Integer, String> {

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
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.siteMappingHandler, "UTF-8");

                multipart.addFormField("SiteId", params[0]);
                multipart.addFormField("RegdId", params[1]);
                multipart.addFormField("IsApproved", params[2]);
                multipart.addFormField("ApprovedBy", params[3]);
                multipart.addFormField("DISTLGDCODE", params[4]);
                multipart.addFormField("Title", params[5]);
                multipart.addFormField("EnglishName", params[6]);
                multipart.addFormField("MaratiName", params[7]);
                multipart.addFormField("MobileNo", params[8]);
                multipart.addFormField("DOB", params[9]);
                multipart.addFormField("Age", params[10]);
                multipart.addFormField("Gender", params[11]);
                multipart.addFormField("PermanentAddress", params[12]);
                multipart.addFormField("LocalAddress", params[13]);
                multipart.addFormField("PinCode", params[14]);
                multipart.addFormField("Location", params[15]);
                multipart.addFormField("UID", params[16]);
                multipart.addFilePart("File", new File(params[17]));

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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Beneficiary successfully mapped with site");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edt_mcobcwwbno.setText("");
                                cardPhotoBm = null;
                                edt_fname.setText("");
                                edt_mname.setText("");
                                edt_lname.setText("");
                                edt_marathi_name.setText("");
                                edt_aadhaarno.setText("");
                                edt_moblieno.setText("");
                                edt_dob.setText("");
                                edt_age.setText("");
                                edt_permanant_address.setText("");
                                edt_local_address.setText("");
                                edt_city.setText("");
                                edt_pincode.setText("");

                                rb_mr.setChecked(true);
                                rb_mrs.setChecked(false);
                                rb_ms.setChecked(false);
                                rb_male.setChecked(true);
                                rb_female.setChecked(false);
                                imv_picphoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
                                btn_markattendance.setEnabled(false);
                            }
                        });
                        builder.show();

                    } else {

                        if (message.equalsIgnoreCase("SiteMapping Details Failed")) {
                            Utilities.showAlertDialog(context, "Fail", "Beneficiary is already mapped", false);
                        } else {
                            Utilities.showAlertDialog(context, "Fail", message, false);
                        }
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
//                    CropImage.activity(photoURI).setGuidelines(CropImageView.Guidelines.ON).start(WorkerSiteMapping_Activity.this);
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
        String destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Card Photos/" + filename;

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
        imagePath = destinationFilename;
        cardPhotoBm = Bitmap.createScaledBitmap(cardPhotoBm, 150, 150, false);
        imv_picphoto.setImageBitmap(cardPhotoBm);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Worker Site Mapping");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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


//    public class GLO_ApprovedWorkerSiteMapping extends AsyncTask<String, Void, String> {
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
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("SiteId", params[0]));
//            param.add(new ParamsPojo("RegdId", params[1]));
//            param.add(new ParamsPojo("IsApproved", params[2]));
//            param.add(new ParamsPojo("ApprovedBy", params[3]));
//            res = WebServiceCall.APICall(ApplicationConstants.GLO_ApprovedWorkerSiteMapping, param);
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
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setIcon(R.drawable.icon_success);
//                        builder.setTitle("Success");
//                        builder.setCancelable(false);
//                        builder.setMessage("Beneficiary successfully mapped with site");
//                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                edt_mcobcwwbno.setText("");
//                                edt_name.setText("");
//                                edt_gender.setText("");
//                                edt_age.setText("");
//                                edt_mobileno.setText("");
//                                btn_markattendance.setEnabled(false);
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
}
