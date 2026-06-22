package com.myhindlab.abkat.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.MahabocwPatientDetailsModel;
import com.myhindlab.abkat.models.PatientDetailsModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.myhindlab.abkat.utilities.Utilities.changeDateFormat;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;
import static com.myhindlab.abkat.utilities.Utilities.isLocationEnabled;
import static com.myhindlab.abkat.utilities.Utilities.provideCameraAndStorageAccess;
import static com.myhindlab.abkat.utilities.Utilities.provideLocationAccess;
import static com.myhindlab.abkat.utilities.Utilities.turnOnLocation;

public class PatientRegistration_Activity_v6 extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;

    private LinearLayout ll_usersdetails;
    private MaterialEditText edt_workerregno, edt_fname, edt_moblieno, edt_aadhaarno, edt_dob, edt_age,
            edt_address, edt_local_address, edt_pincode, edt_renewal_date, edt_card_registration_date, edt_card_expiry_date;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female;
    private CircleImageView imv_patient;
    private ImageView imv_search, imv_health_card, imv_renewal_form;
    private LinearLayout ll_renewal_photo;
    private RadioGroup rg_gender;
    private Switch sw_renewed;
    private Button btn_register;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private Uri patientURI, healthCardURI, renewalURI;
    private Bitmap patientPicBm = null, healthCardPicBm = null, renewalPicBm = null;
    private final int PATIENT_CAMERA_REQUEST = 100, HEALTHCARD_CAMERA_REQUEST = 200, RENEWAL_CAMERA_REQUEST = 300;
    private String userId = "", campId = "", siteId = "", genderId = "", title = "", Latitude = "", Longitude = "", RegId = "",
            patientImagePath = "", healthCardImagePath = "", renewalImagePath = "", isRenewalFlag = "0";
    private boolean isPatientPhotoAvailable = false, isHealthCardPhotoAvailable = false, isRenewalSlipPhotoAvailable = false;
    private boolean isReregistration = false, isPatientfoundInMahabocw = false;

    private File patientPicsFolder;
    private ProgressDialog pd;
    private int imageType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration_v6);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = PatientRegistration_Activity_v6.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        ll_usersdetails = findViewById(R.id.ll_usersdetails);
        imv_patient = findViewById(R.id.imv_patient);
        imv_search = findViewById(R.id.imv_search);
        imv_health_card = findViewById(R.id.imv_health_card);
        imv_renewal_form = findViewById(R.id.imv_renewal_form);
        sw_renewed = findViewById(R.id.sw_renewed);
        ll_renewal_photo = findViewById(R.id.ll_renewal_photo);

        edt_workerregno = findViewById(R.id.edt_workerregno);
        edt_fname = findViewById(R.id.edt_fname);
        edt_moblieno = findViewById(R.id.edt_moblieno);
        edt_aadhaarno = findViewById(R.id.edt_aadhaarno);
        edt_dob = findViewById(R.id.edt_dob);
        edt_age = findViewById(R.id.edt_age);
        edt_local_address = findViewById(R.id.edt_local_address);
        edt_address = findViewById(R.id.edt_address);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_renewal_date = findViewById(R.id.edt_renewal_date);
        edt_card_registration_date = findViewById(R.id.edt_card_registration_date);
        edt_card_expiry_date = findViewById(R.id.edt_card_expiry_date);

        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        rg_gender = findViewById(R.id.rg_gender);
        btn_register = findViewById(R.id.btn_register);

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

    private void setDefaults() {

        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
            provideLocationAccess(context);
        } else {
            if (!isLocationEnabled(context)) {
                turnOnLocation(context);
            } else {
                startLocationUpdates();
            }
        }

        campId = getIntent().getStringExtra("campId");
        siteId = getIntent().getStringExtra("siteId");

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mYear1 = cal.get(Calendar.YEAR);
        mMonth1 = cal.get(Calendar.MONTH);
        mDay1 = cal.get(Calendar.DAY_OF_MONTH);

    }

    private void setEventHandler() {

//        edt_workerregno.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.toString().equals("")) {
//                    edt_workerregno.setText("0");
//                }
//            }
//        });

//        edt_dob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        int age = Utilities.getAgeInDays(Integer.parseInt(String.format("%02d", year)),
//                                Integer.parseInt(String.format("%02d", month)),
//                                Integer.parseInt(String.format("%02d", dayOfMonth)));
//
//                        if (age < 18 || age > 60) {
//                            Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years or more than 60 years", false);
//                            return;
//                        }
//
//                        edt_dob.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, month + 1, year));
//                        edt_age.setText(String.valueOf(age));
//                    }
//                }, mYear, mMonth, mDay);
//                try {
//                    dialog.getDatePicker().setCalendarViewShown(false);
//                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                dialog.show();
//            }
//        });

//        edt_dob.addTextChangedListener(textWatcher);
        edt_workerregno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edt_workerregno.getText().toString().trim().equals("")) {
                    if (edt_workerregno.getText().toString().trim().length() == 12) {
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetBeneficiaryDetailsFromMahabocw().execute(edt_workerregno.getText().toString().trim());
                        } else {
                            isReregistration = false;
                            edt_workerregno.setText("");
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
                    }
                }
            }
        });

        edt_dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String dateStr = s.toString();
                if (!dateStr.equals("")) {
                    String clean = dateStr.replaceAll("/", "");
                    if (clean.length() > 8) {
                        edt_dob.setError("Enter valid date of birth");
                        return;
                    }

                    if (clean.length() == 8) {
                        if (Utilities.isDateValid(dateStr)) {
                            int year = Integer.parseInt(clean.substring(0, 4));
                            int mon = Integer.parseInt(clean.substring(4, 6));
                            int day = Integer.parseInt(clean.substring(6, 8));

                            if (year < 1950) {
                                edt_age.setText("");
                                edt_dob.setError("Enter valid date of birth");
                                return;
                            }

                            if (mon < 1 || mon > 12) {
                                edt_age.setText("");
                                edt_dob.setError("Enter valid month");
                                return;
                            }

                            if (day < 1 || day > 31) {
                                edt_age.setText("");
                                edt_dob.setError("Enter valid day");
                                return;
                            }

                            int age = Utilities.getAge(Integer.parseInt(String.format("%02d", year)),
                                    Integer.parseInt(String.format("%02d", mon)),
                                    Integer.parseInt(String.format("%02d", day)));
                            if (age < 18 || age > 60) {
                                Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years or more than 60 years", false);
                                edt_age.setText("");
                            } else {
                                edt_age.setText(String.valueOf(age));
                            }
                        } else {
                            edt_dob.setError("Enter valid date of birth");
                        }
                    } else {
                        edt_age.setText("");
                    }
                }
            }
        });

        imv_search.setOnClickListener(v -> {

            if (edt_workerregno.getText().toString().isEmpty()) {
                edt_workerregno.setError("Please enter worker registration number");
                return;
            }

            if (Utilities.isNetworkAvailable(context)) {
                new GetBeneficiaryDetailsFromMahabocw().execute(edt_workerregno.getText().toString().trim());
            } else {
                isReregistration = false;
                edt_workerregno.setText("");
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });

        imv_patient.setOnClickListener(v -> {
            imageType = 0;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                return;
            }
//            int randomEndtNo = (int) (Math.random() * 99999 + 1);
//            File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_PR.png");
//            patientURI = Uri.fromFile(patientImageFile);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, patientURI);
//            startActivityForResult(intent, PATIENT_CAMERA_REQUEST);


            int randomEndtNo = (int) (Math.random() * 999999 + 1);

            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            patientURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, patientURI);
            startActivityForResult(intent, PATIENT_CAMERA_REQUEST);
        });

        imv_health_card.setOnClickListener(v -> {
            imageType = 1;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                return;
            }

//            int randomEndtNo = (int) (Math.random() * 99999 + 1);
//            File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_HC.png");
//
//            healthCardURI = Uri.fromFile(patientImageFile);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
//            startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);

            int randomEndtNo = (int) (Math.random() * 999999 + 1);

            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            healthCardURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
            startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);
        });

        imv_renewal_form.setOnClickListener(v -> {
            imageType = 2;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
                return;
            }

//            int randomEndtNo = (int) (Math.random() * 99999 + 1);
//            File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_RHC.png");
//
//            renewalURI = Uri.fromFile(patientImageFile);
//            Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            pickImage.putExtra(MediaStore.EXTRA_OUTPUT, renewalURI);
//            startActivityForResult(pickImage, RENEWAL_CAMERA_REQUEST);

            int randomEndtNo = (int) (Math.random() * 999999 + 1);

            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            renewalURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, renewalURI);
            startActivityForResult(intent, RENEWAL_CAMERA_REQUEST);
        });

//        sw_renewed.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                isRenewalFlag = "1";
//                edt_renewal_date.setVisibility(View.VISIBLE);
//                ll_renewal_photo.setVisibility(View.VISIBLE);
//            } else {
//                isRenewalFlag = "0";
//                edt_renewal_date.setVisibility(View.GONE);
//                ll_renewal_photo.setVisibility(View.GONE);
//                imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
//                edt_renewal_date.setText("");
//            }
//        });

        edt_renewal_date.setOnClickListener(renewalDateOnClickListener);

        edt_card_registration_date.setOnClickListener(registrationDateOnClickListener);

        edt_card_expiry_date.setOnClickListener(expiryDateOnClickListener);

        btn_register.setOnClickListener(v -> submitData());
    }

    private class GetBeneficiaryDetailsFromMahabocw extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.MahabocwAPICall(ApplicationConstants.mahabocwBaseURL + "/" +
                    ApplicationConstants.bocwRegistration + "/" + params[0]);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            isPatientfoundInMahabocw = false;
            pd.dismiss();
            try {
                if (!result.equals("")) {
                    MahabocwPatientDetailsModel pojoDetails = new Gson().fromJson(result, MahabocwPatientDetailsModel.class);
                    if (pojoDetails.getMESSAGE().equals("")) {
                        isPatientfoundInMahabocw = true;
                        loadPatientMahabocwDetails(pojoDetails);
                        new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim(), "1");
                    } else {
                        new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim(), "2");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                isReregistration = false;
                clearPatientDetails();
                new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim(), "2");
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private class GetWorkerInfroRe_Registration extends AsyncTask<String, Void, String> {

        private String callType = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            callType = params[1];
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("RegdNo", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetWorkerInfroRe_Registration, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "";
            try {
                if (!result.equals("")) {
                    PatientDetailsModel pojoDetails = new Gson().fromJson(result, PatientDetailsModel.class);
                    type = pojoDetails.getStatus();
                    if (type.equalsIgnoreCase("success")) {
                        List<PatientDetailsModel.OutputBean> patientlist = pojoDetails.getOutput();
                        if (patientlist.size() > 0) {
                            PatientDetailsModel.OutputBean patientDetails = patientlist.get(0);

                            if (Utilities.diffBetweenTwoDates(patientDetails.getRegistrationDate(), Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear)) < 365) {
                                edt_workerregno.setText("");
                                Utilities.showToastMessage("Cannot re-register the beneficiary", context, false);
                                clearPatientDetails();
                                return;
                            }
                            isReregistration = true;

                            if (!isPatientfoundInMahabocw)
                                loadPatientDetails(patientDetails);
                            else
                                loadPatientPhotoDetailsOnly(patientDetails);
                        } else {
                            if (!isPatientfoundInMahabocw)
                                clearPatientDetails();
                        }
                    } else {
                        if (!isPatientfoundInMahabocw)
                            clearPatientDetails();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                isReregistration = false;
                edt_workerregno.setText("");
                clearPatientDetails();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void loadPatientMahabocwDetails(MahabocwPatientDetailsModel patientDetails) {
        edt_fname.setText(patientDetails.getFirst_name().trim() + " " + patientDetails.getMiddle_name().trim() + " " + patientDetails.getLast_name());
        edt_fname.setFocusable(false);

        edt_moblieno.setText(patientDetails.getContact());
        edt_moblieno.setFocusable(false);

        edt_aadhaarno.setText(patientDetails.getAadhar());
        edt_aadhaarno.setFocusable(false);

        edt_aadhaarno.setText(patientDetails.getAadhar());
        edt_aadhaarno.setFocusable(false);

        edt_age.setText(patientDetails.getCurrent_age());
        edt_age.setFocusable(false);

        edt_dob.setText(changeDateFormat("yyyy-MM-dd", "yyyy/MM/dd", patientDetails.getDob()));
        edt_dob.setFocusable(false);

        if (patientDetails.getGender().equalsIgnoreCase("Male")) {
            rb_male.setChecked(true);
        } else if (patientDetails.getGender().equalsIgnoreCase("Female")) {
            rb_female.setChecked(true);
        }

        rb_male.setClickable(false);
        rb_female.setClickable(false);

        edt_card_registration_date.setText(patientDetails.getRegistration_date());
        edt_card_registration_date.setFocusable(false);

        edt_card_expiry_date.setText(patientDetails.getDue_renewal_date());
        edt_card_expiry_date.setFocusable(false);

        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(edt_card_expiry_date.getText().toString().trim());
            Calendar calendar = Calendar.getInstance();
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(
                    Utilities.ConvertDateFormat(Utilities.dfDate, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
            );
            if (date1.before(date2)) {
                isRenewalFlag = "1";
                edt_renewal_date.setVisibility(View.VISIBLE);
                ll_renewal_photo.setVisibility(View.VISIBLE);
            } else {
                isRenewalFlag = "0";
                edt_renewal_date.setVisibility(View.GONE);
                ll_renewal_photo.setVisibility(View.GONE);
                imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
                edt_renewal_date.setText("");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        edt_card_registration_date.setOnClickListener(null);
        edt_card_expiry_date.setOnClickListener(null);

        StringBuilder permanentAddress = new StringBuilder();
        if (!patientDetails.getPermanent_address_houseNo().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_houseNo() + ", ");
        if (!patientDetails.getPermanent_address_road().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_road() + ", ");
        if (!patientDetails.getPermanent_address_area().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_area() + ", ");
        if (!patientDetails.getPermanent_address_city().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_city() + ", ");
        if (!patientDetails.getPermanent_address_landmark().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_landmark() + ", ");
        if (!patientDetails.getPermanent_address_postOffice().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_postOffice() + ", ");
        if (!patientDetails.getPermanent_address_taluka().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_taluka() + ", ");
        if (!patientDetails.getPermanent_address_district().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_district() + ", ");
        if (!patientDetails.getPermanent_address_state().equals(""))
            permanentAddress.append(patientDetails.getPermanent_address_state() + ", ");

        edt_address.setText(permanentAddress.toString().substring(0, permanentAddress.toString().length() - 1));
        edt_address.setFocusable(false);

        StringBuilder localAddress = new StringBuilder();
        if (!patientDetails.getResidential_address_houseNo().equals(""))
            localAddress.append(patientDetails.getResidential_address_houseNo() + ", ");
        if (!patientDetails.getResidential_address_road().equals(""))
            localAddress.append(patientDetails.getResidential_address_road() + ", ");
        if (!patientDetails.getResidential_address_area().equals(""))
            localAddress.append(patientDetails.getResidential_address_area() + ", ");
        if (!patientDetails.getResidential_address_city().equals(""))
            localAddress.append(patientDetails.getResidential_address_city() + ", ");
        if (!patientDetails.getResidential_address_landmark().equals(""))
            localAddress.append(patientDetails.getResidential_address_landmark() + ", ");
        if (!patientDetails.getResidential_address_postOffice().equals(""))
            localAddress.append(patientDetails.getResidential_address_postOffice() + ", ");
        if (!patientDetails.getResidential_address_taluka().equals(""))
            localAddress.append(patientDetails.getResidential_address_taluka() + ", ");
        if (!patientDetails.getResidential_address_district().equals(""))
            localAddress.append(patientDetails.getResidential_address_district() + ", ");
        if (!patientDetails.getResidential_address_state().equals(""))
            localAddress.append(patientDetails.getResidential_address_state() + ", ");

        edt_local_address.setText(localAddress.toString().substring(0, localAddress.toString().length() - 1));
        edt_local_address.setFocusable(false);

        edt_pincode.setText(patientDetails.getResidential_address_pincode());
        edt_pincode.setFocusable(false);
    }

    private void loadPatientDetails(PatientDetailsModel.OutputBean patientDetails) {
        if (patientDetails.getTitle().equalsIgnoreCase("Mr.")) {
            rb_mr.setChecked(true);
        } else if (patientDetails.getTitle().equalsIgnoreCase("Mrs.")) {
            rb_mrs.setChecked(true);
        } else if (patientDetails.getTitle().equalsIgnoreCase("Ms.")) {
            rb_ms.setChecked(true);
        }

        edt_fname.setText(patientDetails.getPatientname());

        edt_moblieno.setText(patientDetails.getMobileNo());

        edt_aadhaarno.setText(patientDetails.getUID().replace("-", ""));

        if (!edt_aadhaarno.getText().toString().trim().isEmpty())
            edt_aadhaarno.setFocusable(false);
        else {
            edt_aadhaarno.setFocusable(true);
            edt_aadhaarno.setFocusableInTouchMode(true);
        }

        edt_age.setText(patientDetails.getAge());

        edt_dob.setText(changeDateFormat("yyyy-MM-dd", "yyyy/MM/dd", patientDetails.getDOB()));

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            rb_male.setChecked(true);
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            rb_female.setChecked(true);
        }

        edt_address.setText(patientDetails.getPermanentAddress());

        edt_local_address.setText(patientDetails.getLocalAddress());

        edt_pincode.setText(patientDetails.getPincode());


        edt_card_registration_date.setText(patientDetails.getRegistrationDate_New());
        edt_card_expiry_date.setText(patientDetails.getExpirtyDate());
        if (!patientDetails.getExpirtyDate().equals("")) {
            try {
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(edt_card_expiry_date.getText().toString().trim());
                Calendar calendar = Calendar.getInstance();
                Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(
                        Utilities.ConvertDateFormat(Utilities.dfDate, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
                );
                if (date1.before(date2)) {
                    isRenewalFlag = "1";
                    edt_renewal_date.setVisibility(View.VISIBLE);
                    ll_renewal_photo.setVisibility(View.VISIBLE);
                } else {
                    isRenewalFlag = "0";
                    edt_renewal_date.setVisibility(View.GONE);
                    ll_renewal_photo.setVisibility(View.GONE);
                    imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
                    edt_renewal_date.setText("");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
//        edt_card_registration_date.setText(patientDetails.getPincode());
//
//        edt_card_expiry_date.setText(patientDetails.getPincode());

        if (patientDetails.getIsHCRenewal().equalsIgnoreCase("yes")) {
            sw_renewed.setChecked(true);
            edt_renewal_date.setText(changeDateFormat("yyyy-MM-dd", "yyyy/MM/dd", patientDetails.getRenewalDate()));

            if (!patientDetails.getHCRenewalFilePath().trim().isEmpty()) {
                Picasso.with(context)
                        .load(patientDetails.getHCRenewalFilePath())
                        .placeholder(R.drawable.icon_colorcamera)
                        .into(imv_renewal_form, new Callback() {
                            @Override
                            public void onSuccess() {
                                isRenewalSlipPhotoAvailable = true;
                            }

                            @Override
                            public void onError() {
                                isRenewalSlipPhotoAvailable = false;
                            }
                        });
            } else {
                isRenewalSlipPhotoAvailable = false;
            }
        } else if (patientDetails.getIsHCRenewal().equalsIgnoreCase("No")) {
            sw_renewed.setChecked(false);
            edt_renewal_date.setText("");
            isRenewalSlipPhotoAvailable = false;
        }

        if (!patientDetails.getPatientPhoto().trim().isEmpty()) {
            Picasso.with(context)
                    .load(patientDetails.getPatientPhoto())
                    .placeholder(R.drawable.icon_patientcamera)
                    .into(imv_patient, new Callback() {
                        @Override
                        public void onSuccess() {
                            isPatientPhotoAvailable = true;
                        }

                        @Override
                        public void onError() {
                            isPatientPhotoAvailable = false;
                        }
                    });
        } else {
            isPatientPhotoAvailable = false;
        }

        if (!patientDetails.getHealthCardPath().trim().isEmpty()) {
            Picasso.with(context)
                    .load(patientDetails.getHealthCardPath())
                    .placeholder(R.drawable.icon_colorcamera)
                    .into(imv_health_card, new Callback() {
                        @Override
                        public void onSuccess() {
                            isHealthCardPhotoAvailable = true;
                        }

                        @Override
                        public void onError() {
                            isHealthCardPhotoAvailable = false;
                        }
                    });
        } else {
            isHealthCardPhotoAvailable = false;
        }
    }

    private void loadPatientPhotoDetailsOnly(PatientDetailsModel.OutputBean patientDetails) {
        if (patientDetails.getTitle().equalsIgnoreCase("Mr.")) {
            rb_mr.setChecked(true);
        } else if (patientDetails.getTitle().equalsIgnoreCase("Mrs.")) {
            rb_mrs.setChecked(true);
        } else if (patientDetails.getTitle().equalsIgnoreCase("Ms.")) {
            rb_ms.setChecked(true);
        }

        if (patientDetails.getIsHCRenewal().equalsIgnoreCase("yes")) {
            sw_renewed.setChecked(true);
            edt_renewal_date.setText(changeDateFormat("yyyy-MM-dd", "yyyy/MM/dd", patientDetails.getRenewalDate()));

            if (!patientDetails.getHCRenewalFilePath().trim().isEmpty()) {
                Picasso.with(context)
                        .load(patientDetails.getHCRenewalFilePath())
                        .placeholder(R.drawable.icon_colorcamera)
                        .into(imv_renewal_form, new Callback() {
                            @Override
                            public void onSuccess() {
                                isRenewalSlipPhotoAvailable = true;
                            }

                            @Override
                            public void onError() {
                                isRenewalSlipPhotoAvailable = false;
                            }
                        });
            } else {
                isRenewalSlipPhotoAvailable = false;
            }
        } else if (patientDetails.getIsHCRenewal().equalsIgnoreCase("No")) {
            sw_renewed.setChecked(false);
            edt_renewal_date.setText("");
            isRenewalSlipPhotoAvailable = false;
        }

        if (!patientDetails.getPatientPhoto().trim().isEmpty()) {
            Picasso.with(context)
                    .load(patientDetails.getPatientPhoto())
                    .placeholder(R.drawable.icon_patientcamera)
                    .into(imv_patient, new Callback() {
                        @Override
                        public void onSuccess() {
                            isPatientPhotoAvailable = true;
                        }

                        @Override
                        public void onError() {
                            isPatientPhotoAvailable = false;
                        }
                    });
        } else {
            isPatientPhotoAvailable = false;
        }

        if (!patientDetails.getHealthCardPath().trim().isEmpty()) {
            Picasso.with(context)
                    .load(patientDetails.getHealthCardPath())
                    .placeholder(R.drawable.icon_colorcamera)
                    .into(imv_health_card, new Callback() {
                        @Override
                        public void onSuccess() {
                            isHealthCardPhotoAvailable = true;
                        }

                        @Override
                        public void onError() {
                            isHealthCardPhotoAvailable = false;
                        }
                    });
        } else {
            isHealthCardPhotoAvailable = false;
        }
    }

    private void submitData() {

        if (latLng != null) {
            Latitude = String.valueOf(latLng.latitude);
            Longitude = String.valueOf(latLng.longitude);
        }

        if (edt_workerregno.getText().toString().trim().length() != 12) {
            edt_workerregno.setError("Please enter worker registration no.");
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

        if (!Utilities.isMobileNo(edt_moblieno.getText().toString().trim())) {
            edt_moblieno.setError("Please enter valid mobile");
            return;
        }

//        if (!Utilities.isaadharNumberValidate(edt_aadhaarno.getText().toString().trim())) {
//            edt_aadhaarno.setError("Please enter valid Aadhar Card No.");
//            return;
//        }

        if (edt_aadhaarno.getText().toString().trim().length() != 12) {
            edt_aadhaarno.setError("Please enter valid Aadhar Card No.");
            return;
        }

        String dateStr = edt_dob.getText().toString().trim();

        if (!Utilities.isDateValid(dateStr)) {
            edt_dob.setError("Enter valid date of birth");
            return;
        }

        if (edt_age.getText().toString().trim().isEmpty()) {
            edt_age.setError("Please enter age");
            return;
        }

        if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
            Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years or more than 60 years", false);
            return;
        }

        if (rb_male.isChecked()) {
            genderId = "M";
        } else if (rb_female.isChecked()) {
            genderId = "F";
        } else {
            Utilities.showToastMessage("Please select gender", context, false);
            return;
        }

        if (edt_card_registration_date.getText().toString().trim().isEmpty()) {
            edt_card_registration_date.setError("Please enter card registration date");
            return;
        }

        if (edt_card_expiry_date.getText().toString().trim().isEmpty()) {
            edt_card_expiry_date.setError("Please enter card expiry date");
            return;
        }

        if (edt_local_address.getText().toString().trim().isEmpty()) {
            edt_local_address.setError("Please enter local address");
            return;
        }

        if (!Utilities.isValidPincode(edt_pincode.getText().toString().trim())) {
            edt_pincode.setError("Please enter valid pincode");
            return;
        }

        if (!isPatientPhotoAvailable) {
            if (patientImagePath.equals("")) {
                Utilities.showToastMessage("Please click patient photo", context, false);
                return;
            }
        }

        if (!isHealthCardPhotoAvailable) {
            if (healthCardImagePath.equals("")) {
                Utilities.showToastMessage("Please click health card", context, false);
                return;
            }
        }

        if (edt_renewal_date.getVisibility() == View.VISIBLE) {
            if (edt_renewal_date.getText().toString().trim().isEmpty()) {
                edt_renewal_date.setError("Please renewal date");
                return;
            }

            if (!isRenewalSlipPhotoAvailable) {
                if (renewalImagePath.equals("")) {
                    Utilities.showToastMessage("Please click renewal slip photo", context, false);
                    return;
                }
            }
        }

        if (Latitude == null) {
            Latitude = "0.0";
        }

        if (Longitude == null) {
            Longitude = "0.0";
        }

        if (Utilities.isNetworkAvailable(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert!");
            builder.setMessage("Please confirm the beneficiary's details before submitting");
            builder.setCancelable(false);
            builder.setPositiveButton("Proceed", (dialog, which) -> {
                if (Utilities.isNetworkAvailable(context))
                    new UploadPatientDetails().execute(
                            siteId,
                            campId,
                            edt_workerregno.getText().toString().trim(),
                            title,
                            edt_fname.getText().toString().trim(),
                            edt_moblieno.getText().toString().trim(),
                            edt_aadhaarno.getText().toString().trim(),
                            edt_dob.getText().toString().trim(),
                            edt_age.getText().toString().trim(),
                            genderId,
                            edt_address.getText().toString().trim(),
                            edt_local_address.getText().toString().trim(),
                            edt_pincode.getText().toString().trim(),
                            userId,
                            isRenewalFlag,
                            edt_renewal_date.getText().toString().trim(),
                            patientImagePath,
                            healthCardImagePath,
                            renewalImagePath,
                            edt_card_registration_date.getText().toString().trim(),
                            edt_card_expiry_date.getText().toString().trim()
                    );
                else
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();

        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class UploadPatientDetails extends AsyncTask<String, Integer, String> {

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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.BeneficiaryRe_RegistrationNew, "UTF-8");
                multipart.addFormField("SiteId", params[0]);
                multipart.addFormField("CampId", params[1]);
                multipart.addFormField("RegdNo", params[2]);
                multipart.addFormField("Title", params[3]);
                multipart.addFormField("EnglishName", params[4]);
                multipart.addFormField("MobileNo", params[5]);
                multipart.addFormField("UID", params[6]);
                multipart.addFormField("DOB", params[7]);
                multipart.addFormField("Age", params[8]);
                multipart.addFormField("Gender", params[9]);
                multipart.addFormField("PermanentAddress", params[10]);
                multipart.addFormField("LocalAddress", params[11]);
                multipart.addFormField("PinCode", params[12]);
                multipart.addFormField("CreatedBy", params[13]);
                multipart.addFormField("IsHCRenewal", params[14]);
                multipart.addFormField("RenewalDate", params[15]);
                multipart.addFormField("RegistrationDate", params[19]);
                multipart.addFormField("ExpirtyDate", params[20]);
                if (!params[16].equals(""))
                    multipart.addFilePart("file1", new File(params[16]));
                if (!params[17].equals(""))
                    multipart.addFilePart("file2", new File(params[17]));
                if (!params[18].equals(""))
                    multipart.addFilePart("file3", new File(params[18]));
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
                    String msg;
                    if (isReregistration)
                        msg = "Patient re-registered successfully!";
                    else
                        msg = "Patient registered successfully!";

                    if (status.equalsIgnoreCase("Success")) {
                        if (isPatientfoundInMahabocw) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(msg);
                            builder.setTitle("Success");
                            builder.setIcon(R.drawable.icon_success);
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", (dialog, id) -> {
                                startActivity(new Intent(context, PatientFingerAndSignature_Activity.class)
                                        .putExtra("campId", campId)
                                        .putExtra("siteId", siteId)
                                        .putExtra("registrationNo", edt_workerregno.getText().toString().trim()));
                                clearPatientDetails();
                                edt_workerregno.setText("");

                            });
                            AlertDialog alertD = builder.create();
                            alertD.show();
                        } else {
                            if (Utilities.isNetworkAvailable(context)) {
                                String gender = "";
                                if (rb_male.isChecked())
                                    gender = "Male";
                                else if (rb_female.isChecked())
                                    gender = "Female";

                                JsonObject jsonObject = new JsonObject();

                                jsonObject.addProperty("RegdNo", edt_workerregno.getText().toString().trim());
                                jsonObject.addProperty("RegistrationDate", edt_card_registration_date.getText().toString().trim());
                                jsonObject.addProperty("EnglishName", edt_fname.getText().toString().trim());
                                jsonObject.addProperty("MobileNo", edt_moblieno.getText().toString().trim());
                                jsonObject.addProperty("Aadhar", edt_aadhaarno.getText().toString().trim());
                                jsonObject.addProperty("Dob", changeDateFormat("yyyy/MM/dd", "yyyy-MM-dd", edt_dob.getText().toString().trim()));
                                jsonObject.addProperty("Age", Integer.parseInt(edt_age.getText().toString().trim()));
                                jsonObject.addProperty("Gender", gender);
                                jsonObject.addProperty("DueRenewalDate", edt_card_expiry_date.getText().toString().trim());
                                jsonObject.addProperty("DISTNAME", getIntent().getStringExtra("district"));
                                jsonObject.addProperty("Taluka", getIntent().getStringExtra("district"));
                                jsonObject.addProperty("ResidentialAddress", edt_address.getText().toString().trim());
                                jsonObject.addProperty("PermanentAddress", edt_local_address.getText().toString().trim());

                                new UploadPatientDetailsToBocw().execute(jsonObject.toString());
//                                ApiInterface apiService = ApiClientMahaBocw.getClient().create(ApiInterface.class);
//
//                                Call<MahabocwInsertApiResponse> call = apiService.InsertMahabocwInsertApiResponse(
//                                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.8v_JCIO6_4Mg1LpMDxj9yUUhS2_ipjVy3MQst90Dz4o",
//                                        edt_workerregno.getText().toString().trim(),
//                                        edt_card_registration_date.getText().toString().trim(),
//                                        edt_fname.getText().toString().trim(),
//                                        edt_moblieno.getText().toString().trim(),
//                                        edt_aadhaarno.getText().toString().trim(),
//                                        changeDateFormat("yyyy/MM/dd", "yyyy-MM-dd", edt_dob.getText().toString().trim()),
//                                        Integer.parseInt(edt_age.getText().toString().trim()),
//                                        gender,
//                                        edt_card_expiry_date.getText().toString().trim(),
//                                        getIntent().getStringExtra("district"),
//                                        getIntent().getStringExtra("district"),
//                                        edt_address.getText().toString().trim(),
//                                        edt_local_address.getText().toString().trim());
//
//                                call.enqueue(new retrofit2.Callback<MahabocwInsertApiResponse>() {
//                                    @Override
//                                    public void onResponse(Call<MahabocwInsertApiResponse> call,
//                                                           retrofit2.Response<MahabocwInsertApiResponse> response) {
//                                        String msg = "";
//                                        if (isReregistration)
//                                            msg = "Patient re-registered successfully!";
//                                        else
//                                            msg = "Patient registered successfully!";
//
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                        builder.setMessage(msg);
//                                        builder.setTitle("Success");
//                                        builder.setIcon(R.drawable.icon_success);
//                                        builder.setCancelable(false);
//                                        builder.setPositiveButton("OK", (dialog, id) -> {
//                                            startActivity(new Intent(context, PatientFingerAndSignature_Activity.class)
//                                                    .putExtra("campId", campId)
//                                                    .putExtra("siteId", siteId)
//                                                    .putExtra("registrationNo", edt_workerregno.getText().toString().trim()));
//                                            clearPatientDetails();
//                                            edt_workerregno.setText("");
//                                        });
//                                        AlertDialog alertD = builder.create();
//                                        alertD.show();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<MahabocwInsertApiResponse> call, Throwable t) {
//                                        String msg = "";
//                                        if (isReregistration)
//                                            msg = "Patient re-registered successfully!";
//                                        else
//                                            msg = "Patient registered successfully!";
//
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                        builder.setMessage(msg);
//                                        builder.setTitle("Success");
//                                        builder.setIcon(R.drawable.icon_success);
//                                        builder.setCancelable(false);
//                                        builder.setPositiveButton("OK", (dialog, id) -> {
//                                            startActivity(new Intent(context, PatientFingerAndSignature_Activity.class)
//                                                    .putExtra("campId", campId)
//                                                    .putExtra("siteId", siteId)
//                                                    .putExtra("registrationNo", edt_workerregno.getText().toString().trim()));
//                                            clearPatientDetails();
//                                            edt_workerregno.setText("");
//                                        });
//                                        AlertDialog alertD = builder.create();
//                                        alertD.show();
//                                    }
//                                });
                            } else
                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
                    } else {
                        if (message.toLowerCase().contains("regd")) {
                            Utilities.showAlertDialog(context, "Fail", "Beneficiary No Already Exists", false);
                        } else {
                            Utilities.showAlertDialog(context, "Fail", message, false);
                        }
                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class UploadPatientDetailsToBocw extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            res = WebServiceCall.jsonApiCall(
                    ApplicationConstants.mahabocwBaseURL + "/" + ApplicationConstants.bocwRegistration, params[0]);
            return res.trim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String msg = "";
            if (isReregistration)
                msg = "Patient re-registered successfully!";
            else
                msg = "Patient registered successfully!";

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(msg);
            builder.setTitle("Success");
            builder.setIcon(R.drawable.icon_success);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", (dialog, id) -> {
                startActivity(new Intent(context, PatientFingerAndSignature_Activity.class)
                        .putExtra("campId", campId)
                        .putExtra("siteId", siteId)
                        .putExtra("registrationNo", edt_workerregno.getText().toString().trim()));
                clearPatientDetails();
                edt_workerregno.setText("");
            });
            AlertDialog alertD = builder.create();
            alertD.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PATIENT_CAMERA_REQUEST) {
//                CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v6.this);
            }
            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
//                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v6.this);
            }
            if (requestCode == RENEWAL_CAMERA_REQUEST) {
//                CropImage.activity(renewalURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v6.this);
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
        if (imageType == 0) {
            isPatientPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
//            destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
            destinationFilename = getExternalCacheDir()+"/" + filename;
        } else if (imageType == 1) {
            isHealthCardPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
//            destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
            destinationFilename = getExternalCacheDir()+"/" + filename;
        } else if (imageType == 2) {
            isRenewalSlipPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_RHC.png";
//            destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
            destinationFilename = getExternalCacheDir()+"/" + filename;
        }

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

        if (imageType == 0) {
            patientPicBm = BitmapFactory.decodeFile(destinationFilename);
            destinationFilename = compressImage(destinationFilename);
            patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
            imv_patient.setImageBitmap(patientPicBm);
            patientImagePath = destinationFilename;

        } else if (imageType == 1) {
            healthCardPicBm = BitmapFactory.decodeFile(destinationFilename);
            destinationFilename = compressImage(destinationFilename);
            healthCardPicBm = Bitmap.createScaledBitmap(healthCardPicBm, 150, 150, false);
            imv_health_card.setImageBitmap(healthCardPicBm);
            healthCardImagePath = destinationFilename;
        } else if (imageType == 2) {
            renewalPicBm = BitmapFactory.decodeFile(destinationFilename);
            destinationFilename = compressImage(destinationFilename);
            renewalPicBm = Bitmap.createScaledBitmap(renewalPicBm, 150, 150, false);
            imv_renewal_form.setImageBitmap(renewalPicBm);
            renewalImagePath = destinationFilename;
        }
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

    private void clearPatientDetails() {
        isReregistration = false;
        isPatientfoundInMahabocw = false;
        edt_fname.setText("");
        edt_fname.setFocusable(true);
        edt_fname.setFocusableInTouchMode(true);

        edt_moblieno.setText("");
        edt_moblieno.setFocusable(true);
        edt_moblieno.setFocusableInTouchMode(true);

        edt_aadhaarno.setText("");
        edt_aadhaarno.setFocusable(true);
        edt_aadhaarno.setFocusableInTouchMode(true);

        edt_dob.setText("");
        edt_dob.setFocusable(true);
        edt_dob.setFocusableInTouchMode(true);

        edt_age.setText("");
        edt_age.setFocusable(true);
        edt_age.setFocusableInTouchMode(true);

        edt_address.setText("");
        edt_address.setFocusable(true);
        edt_address.setFocusableInTouchMode(true);

        edt_local_address.setText("");
        edt_local_address.setFocusable(true);
        edt_local_address.setFocusableInTouchMode(true);

        edt_pincode.setText("");
        edt_pincode.setFocusable(true);
        edt_pincode.setFocusableInTouchMode(true);

        edt_renewal_date.setText("");

        edt_card_registration_date.setText("");
        edt_card_registration_date.setOnClickListener(registrationDateOnClickListener);

        edt_card_expiry_date.setText("");
        edt_card_expiry_date.setOnClickListener(expiryDateOnClickListener);

        sw_renewed.setChecked(false);
        rb_mr.setChecked(true);
        rb_mrs.setChecked(false);
        rb_ms.setChecked(false);
        rb_male.setChecked(false);
        rb_female.setChecked(false);
        rb_male.setClickable(true);
        rb_female.setClickable(true);
        sw_renewed.setChecked(false);
        rg_gender.clearCheck();

        imv_patient.setImageDrawable(getResources().getDrawable(R.drawable.icon_patientcamera));
        imv_health_card.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
        imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));

        patientPicBm = null;
        healthCardPicBm = null;
        renewalPicBm = null;

        genderId = "";
        title = "";
        RegId = "";
        patientImagePath = "";
        healthCardImagePath = "";
        renewalImagePath = "";

        isPatientPhotoAvailable = false;
        isHealthCardPhotoAvailable = false;
        isRenewalSlipPhotoAvailable = false;
        isPatientfoundInMahabocw = false;
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient Registration");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private LatLng latLng;

    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        /* 10 secs */
        long UPDATE_INTERVAL = 10 * 1000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        /* 2 sec */
        long FASTEST_INTERVAL = 2000;
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Latitude = String.valueOf(latLng.latitude);
        Longitude = String.valueOf(latLng.longitude);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    provideCameraAndStorageAccess(context);
                }
            }

        }
    }

    OnClickListener renewalDateOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> edt_renewal_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, month + 1, year)), mYear1, mMonth1, mDay1);
            try {
                Calendar c = Calendar.getInstance();
                c.set(mYear1 - 5, mMonth1, mDay1);
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.show();
        }
    };

    int regYear, regMonth, regDay;

    OnClickListener registrationDateOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                edt_card_registration_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, month + 1, year));
                edt_card_expiry_date.setText("");

                isRenewalFlag = "0";
                edt_renewal_date.setVisibility(View.GONE);
                ll_renewal_photo.setVisibility(View.GONE);
                imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
                edt_renewal_date.setText("");

                regYear = year;
                regMonth = month;
                regDay = dayOfMonth;
            }, mYear1, mMonth1, mDay1);
            try {
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.show();
        }
    };

    OnClickListener expiryDateOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                edt_card_expiry_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, month + 1, year));
                try {
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(edt_card_expiry_date.getText().toString().trim());
                    Calendar calendar = Calendar.getInstance();
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(
                            Utilities.ConvertDateFormat(Utilities.dfDate, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
                    );

                    if (date1.before(date2)) {
                        isRenewalFlag = "1";
                        edt_renewal_date.setVisibility(View.VISIBLE);
                        ll_renewal_photo.setVisibility(View.VISIBLE);
                    } else {
                        isRenewalFlag = "0";
                        edt_renewal_date.setVisibility(View.GONE);
                        ll_renewal_photo.setVisibility(View.GONE);
                        imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
                        edt_renewal_date.setText("");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }, mYear1, mMonth1, mDay1);
            try {
                Calendar c = Calendar.getInstance();
                c.set(regYear, regMonth, regDay);
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            } catch (Exception e) {

            }
            dialog.show();
        }
    };
}


//Response{protocol=http/1.1, code=400, message=Bad Request, url=https://healthcamp.mahabocw.in/api/bocw-registration?RegdNo=989797989898&RegistrationDate=2020-10-19&EnglishName=Chdhs&MobileNo=8989749944&Aadhar=949797985959&Dob=1993-12-30&Age=26&Gender=Female&DueRenewalDate=2020-10-19&DISTNAME=PUNE&Taluka=PUNE&ResidentialAddress=Pune&PermanentAddress=Pune}
