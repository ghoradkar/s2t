package com.myhindlab.abkat.activities;

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
import static com.myhindlab.abkat.utilities.Utilities.showMessageString;
import static com.myhindlab.abkat.utilities.Utilities.turnOnLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PatientDetailsModel;
import com.myhindlab.abkat.models.WorkerInfoForRegistrationModel;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientRegistration_Activity_v5 extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;

    private LinearLayout ll_usersdetails;
    private MaterialEditText edt_workerregno, edt_fname, edt_moblieno, edt_aadhaarno, edt_dob, edt_age,
            edt_address, edt_local_address, edt_pincode, edt_renewal_date, edt_SecondaryMoblieno;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female;
    private CircleImageView imv_patient;
    private ImageView imv_search, imv_health_card, imv_renewal_form;
    private LinearLayout ll_renewal_photo;
    private RadioGroup rg_gender;
    private Switch sw_renewed;
    private Button btn_register, btn_verify;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private Uri patientURI, healthCardURI, renewalURI;
    private Bitmap patientPicBm = null, healthCardPicBm = null, renewalPicBm = null;
    private final int PATIENT_CAMERA_REQUEST = 100, HEALTHCARD_CAMERA_REQUEST = 200, RENEWAL_CAMERA_REQUEST = 300;
    private String userId = "", campId = "", siteId = "", genderId = "", title = "", Latitude = "", Longitude = "", RegId = "", labCode = "",
            patientImagePath = "", healthCardImagePath = "", renewalImagePath = "", isRenewalFlag = "0";
    private boolean isPatientPhotoAvailable = false, isHealthCardPhotoAvailable = false, isRenewalSlipPhotoAvailable = false;
    private boolean isReregistration = false;

    private File patientPicsFolder;
    private ProgressDialog pd;
    private int imageType = 0;
    ImageButton barCodeScannerBtn;
    private String IsPrimaryOTPVerify = "0", IsAdharDataVerify = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration__v5);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = PatientRegistration_Activity_v5.this;
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
        edt_SecondaryMoblieno = findViewById(R.id.edt_SecondaryMoblieno);

        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        rg_gender = findViewById(R.id.rg_gender);
        btn_register = findViewById(R.id.btn_register);
        btn_verify = findViewById(R.id.btn_verify);
        barCodeScannerBtn = findViewById(R.id.barcode_scanner_btn);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            patientPicsFolder = getExternalCacheDir();
        } else {

            patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Photos/");
            if (!patientPicsFolder.exists())
                patientPicsFolder.mkdirs();
        }
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                labCode = json.getString("Labcode");
                labCode = "28";

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

        barCodeScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);
            }
        });

        edt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int age = Utilities.getAge(Integer.parseInt(String.format("%02d", year)),
                                Integer.parseInt(String.format("%02d", month)),
                                Integer.parseInt(String.format("%02d", dayOfMonth)));

                        if (age < 18 || age > 60) {
                            Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years or more than 60 years", false);
                            return;
                        }

                        edt_dob.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, month + 1, year));
                        edt_age.setText(String.valueOf(age));
                    }
                }, mYear, mMonth, mDay);
                try {
                    dialog.getDatePicker().setCalendarViewShown(false);
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.show();
            }
        });

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

        // edt_dob.addTextChangedListener(textWatcher);
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
                            new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
//                            new GetWorkerInfro().execute("MH" + edt_workerregno.getText().toString().trim());
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
                        edt_dob.setEnabled(true);
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
                                edt_dob.setEnabled(true);

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
                            edt_dob.setEnabled(true);

                        }
                    } else {
                        edt_age.setText("");
                    }
                }
            }
        });

        imv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_workerregno.getText().toString().isEmpty()) {
                    edt_workerregno.setError("Please enter worker registration number");
                    return;
                }

                if (Utilities.isNetworkAvailable(context)) {
                    new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
//                    new GetWorkerInfro().execute("MH" + edt_workerregno.getText().toString().trim());
                } else {
                    isReregistration = false;
                    edt_workerregno.setText("");
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        imv_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 0;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }
                int randomEndtNo = (int) (Math.random() * 999999 + 1);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
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

        imv_health_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 1;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }

//                int randomEndtNo = (int) (Math.random() * 99999 + 1);

                int randomEndtNo = (int) (Math.random() * 999999 + 1);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    healthCardURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
                    startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_HC.png");
                    healthCardURI = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
                    startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);
                }
            }
        });

        imv_renewal_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 2;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
                    return;
                }

                int randomEndtNo = (int) (Math.random() * 999999 + 1);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {

                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    renewalURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, renewalURI);
                    startActivityForResult(intent, RENEWAL_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_RHC.png");

                    renewalURI = Uri.fromFile(patientImageFile);
                    Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    pickImage.putExtra(MediaStore.EXTRA_OUTPUT, renewalURI);
                    startActivityForResult(pickImage, RENEWAL_CAMERA_REQUEST);
                }
            }
        });

        sw_renewed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
            }
        });

        edt_renewal_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edt_renewal_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, month + 1, year));
                    }
                }, mYear1, mMonth1, mDay1);
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
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_moblieno.getText().toString().equals("")) {
                    showMessageString("Please Enter Mobile Number first.", context);
                } else if (edt_moblieno.length() == 10) {
                    if (Utilities.isNetworkAvailable(context))
                        new SendOtp().execute(edt_moblieno.getText().toString(), userId);
                    else
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                } else {
                    showMessageString("Please Enter 10 digit Mobile Number.", context);
                }
                //openDialog();
            }
        });
    }

    private class GetWorkerInfroRe_Registration extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdNo", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetWorkerInfroRe_Registration, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            Log.d("TAG", "GetWorkerInfroRe_Registration: " + result);
            try {
                if (!result.equals("")) {
                    PatientDetailsModel pojoDetails = new Gson().fromJson(result, PatientDetailsModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<PatientDetailsModel.OutputBean> patientlist = pojoDetails.getOutput();
                        if (patientlist.size() > 0) {
                            PatientDetailsModel.OutputBean patientDetails = patientlist.get(0);

                            if (Utilities.diffBetweenTwoDates(patientDetails.getRegistrationDate(), Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear)) < 365) {
                                edt_workerregno.setText("");
//                                Utilities.showToastMessage("Cannot re-register the Labour", context, false);
                                Utilities.showAlertDialog(context, "Cannot re-register the Labour", "You have done screening on " + patientDetails.getRegistrationDate(), false);

                                clearPatientDetails();
                                return;
                            }

                            enableViewAfterFill();
                            isReregistration = true;
//                            loadPatientDetails(patientDetails);
                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

                        } else {
                            clearPatientDetails();
//                            relationId = "20";
//                            edt_relation.setText("Self");
                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

                        }
                    } else {
                        clearPatientDetails();
//                        relationId = "20";
//                        edt_relation.setText("Self");
                        new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

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

    private class GetWorkerInfo extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.MahabocwAPICall(ApplicationConstants.GetWorkerInfo + params[0]);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            Log.d("worker info", result);
            try {
                if (!result.equals("")) {
//                    WorkerList workerInfoModel = new Gson().fromJson(result, WorkerList.class);

                    JSONArray jsonArray = new JSONArray(result);

                    if (jsonArray.length() > 0) {
                        String fName = jsonArray.getJSONObject(0).getString("firstNamePersonal") == "null" ? "" : jsonArray.getJSONObject(0).getString("firstNamePersonal");
                        String mName = jsonArray.getJSONObject(0).getString("middleNamePersonal") == "null" ? "" : jsonArray.getJSONObject(0).getString("middleNamePersonal");
                        String lName = jsonArray.getJSONObject(0).getString("lastNamePersonal") == "null" ? "" : jsonArray.getJSONObject(0).getString("lastNamePersonal");
                        String mobile = jsonArray.getJSONObject(0).getString("mobile") == "null" ? "" : jsonArray.getJSONObject(0).getString("mobile");
                        String aadhar = jsonArray.getJSONObject(0).getString("aadhaar") == "null" ? "" : jsonArray.getJSONObject(0).getString("aadhaar");
                        String age = String.valueOf(jsonArray.getJSONObject(0).getInt("age")) == "null" ? "" : String.valueOf(jsonArray.getJSONObject(0).getInt("age"));

                        String permHouseNo = jsonArray.getJSONObject(0).getString("permanent_address_houseNo") == "null" ? "" : "House No.," + jsonArray.getJSONObject(0).getString("permanent_address_houseNo");
                        String permArea = jsonArray.getJSONObject(0).getString("permanent_address_area") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_area");
                        String permPostOffice = jsonArray.getJSONObject(0).getString("permanent_address_postOffice") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_postOffice");
                        String permTaluka = jsonArray.getJSONObject(0).getString("permanent_address_taluka") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_taluka");
                        String permDist = jsonArray.getJSONObject(0).getString("permanent_address_district") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_district");
                        String permState = jsonArray.getJSONObject(0).getString("permanent_address_state") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_state");
                        String permPincode = jsonArray.getJSONObject(0).getString("permanent_address_pincode") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_pincode");

                        String localHouseNo = jsonArray.getJSONObject(0).getString("residential_address_houseNo") == "null" ? "" : "House No.," + jsonArray.getJSONObject(0).getString("residential_address_houseNo");
                        String localArea = jsonArray.getJSONObject(0).getString("residential_address_area") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_area");
                        String localPostOffice = jsonArray.getJSONObject(0).getString("residential_address_postOffice") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_postOffice");
                        String localTaluka = jsonArray.getJSONObject(0).getString("residential_address_taluka") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_taluka");
                        String localDist = jsonArray.getJSONObject(0).getString("residential_address_district") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_district");
                        String localState = jsonArray.getJSONObject(0).getString("residential_address_state") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_state");
                        String localPincode = jsonArray.getJSONObject(0).getString("residential_address_pincode") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_pincode");
                        edt_fname.setText(fName + " " + mName + " " + lName);
                        edt_moblieno.setText(mobile);
                        edt_aadhaarno.setText(aadhar);
                        edt_age.setText(age);
                        String permAddress = permHouseNo + permArea + "," + permPostOffice + "," + permTaluka + "," + permDist + "," + permState + "," + permPincode;
                        edt_address.setText(permAddress);
                        String localAddress = localHouseNo + localArea + "," + localPostOffice + "," + localTaluka + "," + localDist + "," + localState + "," + localPincode;
                        edt_local_address.setText(localAddress);
                        edt_pincode.setText(localPincode);
                        if (jsonArray.getJSONObject(0).getInt("age") != 0) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.YEAR, -jsonArray.getJSONObject(0).getInt("age"));
                            edt_dob.setText(new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()));
                        }
                        String gender = jsonArray.getJSONObject(0).getString("gender");

                        if (gender != null) {
                            if (gender.equalsIgnoreCase("male")) {
                                rb_male.setChecked(true);
                                rb_female.setChecked(false);
                                rb_mr.setChecked(true);
                                rb_mrs.setChecked(false);
                                rb_ms.setChecked(false);

                            } else {
                                rb_male.setChecked(false);
                                rb_female.setChecked(true);

                                rb_mrs.setChecked(true);
                                rb_mr.setChecked(false);
                                rb_ms.setChecked(false);

                            }
                        }
                        disableViewAfterFill();
                    } else {
                        clearPatientDetails();
                        enableViewAfterFill();
//                        relationId = "20";
//                        edt_relation.setText("Self");


                    }
//                    if (list.size() > 0) {
//                    } else {
//                        clearPatientDetails();
//                        isReregistration = false;
//                        new GetWorkerInfro().execute("MH" + edt_workerregno.getText().toString().trim());
//                    }
                } else {
                    clearPatientDetails();
                    enableViewAfterFill();
//                    relationId = "20";
//                    edt_relation.setText("Self");

                }
            } catch (Exception e) {
                e.printStackTrace();
                clearPatientDetails();
                enableViewAfterFill();
//                relationId = "20";
//                edt_relation.setText("Self");
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    void disableViewAfterFill() {
        if (!edt_fname.getText().toString().isEmpty()) {
            edt_fname.setEnabled(false);
        } else {
            edt_fname.setEnabled(true);
        }

        if (!edt_moblieno.getText().toString().isEmpty()) {
            edt_moblieno.setEnabled(false);
        } else {
            edt_moblieno.setEnabled(true);
        }
        if (!edt_aadhaarno.getText().toString().isEmpty()) {
            edt_aadhaarno.setEnabled(false);
        } else {
            edt_aadhaarno.setEnabled(true);
        }

        if (!edt_age.getText().toString().isEmpty()) {
            edt_age.setEnabled(false);
        } else {
            edt_age.setEnabled(true);
        }

        if (!edt_address.getText().toString().isEmpty()) {
            edt_address.setEnabled(false);
        } else {
            edt_address.setEnabled(true);
        }

        if (!edt_local_address.getText().toString().isEmpty()) {
            edt_local_address.setEnabled(false);
        } else {
            edt_local_address.setEnabled(true);
        }

        if (!edt_pincode.getText().toString().isEmpty()) {
            edt_pincode.setEnabled(false);
        } else {
            edt_pincode.setEnabled(true);
        }

        if (!edt_dob.getText().toString().isEmpty()) {
            edt_dob.setEnabled(false);
        } else {
            edt_dob.setEnabled(true);
        }

        if (!rb_male.isChecked()) {
            rb_male.setEnabled(false);
        } else {
            rb_male.setEnabled(true);
        }

        if (!rb_female.isChecked()) {
            rb_female.setEnabled(false);
        } else {
            rb_female.setEnabled(true);
        }
        if (!rb_mr.isChecked()) {
            rb_mr.setEnabled(false);
        } else {
            rb_mr.setEnabled(true);
        }

        if (!rb_mrs.isChecked()) {
            rb_mrs.setEnabled(false);
            rb_ms.setEnabled(false);
        } else {
            rb_mrs.setEnabled(true);
            rb_ms.setEnabled(true);
        }
//        edt_fname.setLongClickable(false);
//        edt_moblieno.setLongClickable(false);
//        edt_aadhaarno.setLongClickable(false);
//        edt_age.setLongClickable(false);
//        edt_address.setLongClickable(false);
//        edt_local_address.setLongClickable(false);
//        edt_pincode.setLongClickable(false);
//        edt_dob.setLongClickable(false);
    }

    void enableViewAfterFill() {
        edt_fname.setEnabled(true);
//        edt_fname.setLongClickable(true);
        edt_moblieno.setEnabled(true);
//        edt_moblieno.setLongClickable(true);
        edt_aadhaarno.setEnabled(true);
//        edt_aadhaarno.setLongClickable(true);
        edt_age.setEnabled(true);
//        edt_age.setLongClickable(true);
        edt_address.setEnabled(true);
//        edt_address.setLongClickable(true);
        edt_local_address.setEnabled(true);
//        edt_local_address.setLongClickable(true);
        edt_pincode.setEnabled(true);
//        edt_pincode.setLongClickable(true);
        edt_dob.setEnabled(true);
//        edt_dob.setLongClickable(true);
        rb_male.setEnabled(true);
        rb_female.setEnabled(true);
        rb_mrs.setEnabled(true);
        rb_ms.setEnabled(true);
        rb_mr.setEnabled(true);
    }


    private class GetWorkerInfoForRegistration extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdNo", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryInfoHLL, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("WorkerInfo", result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    WorkerInfoForRegistrationModel pojoDetails = new Gson().fromJson(result, WorkerInfoForRegistrationModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
//                        List<PatientDetailsModel.OutputBean> patientlist = pojoDetails.getOutput();
                        if (pojoDetails.getOutput().size() > 0) {
                            WorkerInfoForRegistrationModel.Output patientDetails = pojoDetails.getOutput().get(0);
                            isReregistration = false;
                            loadPatientDetailsForRegistration(patientDetails);
                        } else {
                            clearPatientDetails();
                        }
                    } else {
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

    private void openDialog(String otp) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_verifyotp, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Verify OTP");
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);

        EditText edt_otp = promptView.findViewById(R.id.edt_otp);
        TextView text = promptView.findViewById(R.id.text);
        TableRow row_msg = promptView.findViewById(R.id.row_msg);

        alertDialogBuilder.setPositiveButton("Ok", null);
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        android.app.AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        Button okBtn = alertD.getButton(DialogInterface.BUTTON_POSITIVE);
        okBtn.setOnClickListener(v -> {
            if (edt_otp.getText().toString().trim().equals(otp)) {
                Utilities.showToastMessage("OTP Matched Successfully.", context, true);
                IsPrimaryOTPVerify = "1";
                edt_moblieno.setEnabled(false);
                edt_moblieno.setClickable(false);
                btn_verify.setEnabled(false);
                btn_verify.setClickable(false);
                alertD.dismiss();

            } else {
                edt_otp.setText("");
                Utilities.showToastMessage("OTP Not Matched.", context, false);
                IsPrimaryOTPVerify = "0";
            }
        });

    }

    private class SendOtp extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("MOBNO", params[0]));
            param.add(new ParamsPojo("CreatedBy", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.VerifyOTP, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                JSONObject obj1 = new JSONObject(result);
                String status = obj1.getString("status");
                String message = obj1.getString("message");
                if (status.equalsIgnoreCase("Success")) {
                    openDialog(message);
                } else {
                    Utilities.showToastMessage(message, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void loadPatientDetails(PatientDetailsModel.OutputBean patientDetails) {
        disableUI();

        if (patientDetails.getTitle().equalsIgnoreCase("Mr.")) {
            rb_mr.setChecked(true);
        } else if (patientDetails.getTitle().equalsIgnoreCase("Mrs.")) {
            rb_mrs.setChecked(true);
        } else if (patientDetails.getTitle().equalsIgnoreCase("Ms.")) {
            rb_ms.setChecked(true);
        }

        edt_fname.setText(patientDetails.getPatientname());

        if (Utilities.isValidMobileno(patientDetails.getMobileNo())) {
            edt_moblieno.setText(patientDetails.getMobileNo());
            edt_moblieno.setEnabled(false);
        } else {
            edt_moblieno.setEnabled(true);
        }

        edt_aadhaarno.setText(patientDetails.getUID().replace("-", ""));

        if (!edt_aadhaarno.getText().toString().trim().isEmpty())
            edt_aadhaarno.setFocusable(false);
        else {
            edt_aadhaarno.setFocusable(true);
            edt_aadhaarno.setFocusableInTouchMode(true);
            edt_aadhaarno.setEnabled(true);
        }


        Log.d("Age", String.valueOf(patientDetails.getAge()));

        if (!Utilities.isDateValid(changeDateFormat("yyyy-MM-dd", "yyyy/MM/dd", patientDetails.getDOB()))) {
//            edt_dob.setError("Enter valid date of birth");
            edt_dob.setEnabled(true);
        } else {
            edt_dob.setEnabled(false);
            edt_dob.setText(changeDateFormat("yyyy-MM-dd", "yyyy/MM/dd", patientDetails.getDOB()));

        }
        edt_age.setText(String.valueOf(patientDetails.getAge()));


//        if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
//            Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years or more than 60 years", false);
//            return;
//        }

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            rb_male.setChecked(true);
            rb_male.setEnabled(false);
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            rb_female.setChecked(true);
            rb_female.setEnabled(false);

        } else {
            rb_female.setEnabled(true);
            rb_male.setEnabled(true);
        }

        if (patientDetails.getPermanentAddress().isEmpty()) {
            edt_address.setEnabled(true);
        } else {
            edt_address.setText(patientDetails.getPermanentAddress());
            edt_address.setEnabled(false);

        }
        if (patientDetails.getLocalAddress().isEmpty()) {
            edt_local_address.setEnabled(true);
        } else {
            edt_local_address.setText(patientDetails.getLocalAddress());
            edt_local_address.setEnabled(false);

        }


        if (patientDetails.getPincode().isEmpty()) {
            edt_pincode.setEnabled(true);
        } else {
            edt_pincode.setEnabled(false);
            edt_pincode.setText(patientDetails.getPincode());
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

    private void loadPatientDetailsForRegistration(WorkerInfoForRegistrationModel.Output patientDetails) {
        if (patientDetails.getGender().equalsIgnoreCase("Male")) {
            rb_mr.setChecked(true);
        } else if (patientDetails.getGender().equalsIgnoreCase("Female")) {
            rb_mrs.setChecked(true);
        }

        edt_fname.setText(patientDetails.getFirstNamePersonal() + " " + patientDetails.getMiddleNamePersonal() + " " + patientDetails.getLastNamePersonal());

        edt_moblieno.setText(patientDetails.getMobile());

        edt_aadhaarno.setText(patientDetails.getAadhaar().replace("-", ""));
//
//        if (!edt_aadhaarno.getText().toString().trim().isEmpty())
//            edt_aadhaarno.setFocusable(false);
//        else {
//            edt_aadhaarno.setFocusable(true);
//            edt_aadhaarno.setFocusableInTouchMode(true);
//        }

        edt_age.setText(String.valueOf(patientDetails.getAge()));

//        edt_dob.setText(changeDateFormat("yyyy-MM-dd", "yyyy/MM/dd", patientDetails.getDOB()));

        if (patientDetails.getGender().equalsIgnoreCase("Male")) {
            rb_male.setChecked(true);
        } else if (patientDetails.getGender().equalsIgnoreCase("Female")) {
            rb_female.setChecked(true);
        }

        edt_address.setText(patientDetails.getPermanentAddressHouseNo() + ", " + patientDetails.getPermanentAddressArea()
                + " " + patientDetails.getPermanentAddressTaluka() + ", " + patientDetails.getPermanentAddressDistrict() + ", " +
                patientDetails.getResidentialAddressState());

        edt_local_address.setText(patientDetails.getResidentialAddressHouseNo() + ", " + patientDetails.getResidentialAddressArea() + ", " +
                patientDetails.getResidentialAddressTaluka() + ", " + patientDetails.getResidentialAddressDistrict() + ", " + patientDetails.getResidentialAddressState());

        edt_pincode.setText(String.valueOf(patientDetails.getPermanentAddressPincode()));

//        if (patientDetails.getIsHCRenewal().equalsIgnoreCase("yes")) {
//            sw_renewed.setChecked(true);
//            edt_renewal_date.setText(changeDateFormat("yyyy-MM-dd", "yyyy/MM/dd", patientDetails.getRenewalDate()));
//
//            if (!patientDetails.getHCRenewalFilePath().trim().isEmpty()) {
//                Picasso.with(context)
//                        .load(patientDetails.getHCRenewalFilePath())
//                        .placeholder(R.drawable.icon_colorcamera)
//                        .into(imv_renewal_form, new Callback() {
//                            @Override
//                            public void onSuccess() {
//                                isRenewalSlipPhotoAvailable = true;
//                            }
//
//                            @Override
//                            public void onError() {
//                                isRenewalSlipPhotoAvailable = false;
//                            }
//                        });
//            } else {
//                isRenewalSlipPhotoAvailable = false;
//            }
//        } else if (patientDetails.getIsHCRenewal().equalsIgnoreCase("No")) {
//            sw_renewed.setChecked(false);
//            edt_renewal_date.setText("");
//            isRenewalSlipPhotoAvailable = false;
//        }

//        if (!patientDetails.getPatientPhoto().trim().isEmpty()) {
//            Picasso.with(context)
//                    .load(patientDetails.getPatientPhoto())
//                    .placeholder(R.drawable.icon_patientcamera)
//                    .into(imv_patient, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            isPatientPhotoAvailable = true;
//                        }
//
//                        @Override
//                        public void onError() {
//                            isPatientPhotoAvailable = false;
//                        }
//                    });
//        } else {
//            isPatientPhotoAvailable = false;
//        }

//        if (!patientDetails.getHealthCardPath().trim().isEmpty()) {
//            Picasso.with(context)
//                    .load(patientDetails.getHealthCardPath())
//                    .placeholder(R.drawable.icon_colorcamera)
//                    .into(imv_health_card, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            isHealthCardPhotoAvailable = true;
//                        }
//
//                        @Override
//                        public void onError() {
//                            isHealthCardPhotoAvailable = false;
//                        }
//                    });
//        } else {
//            isHealthCardPhotoAvailable = false;
//        }

        enableUI();

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

        if (sw_renewed.isChecked()) {
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
                            "0",
                            IsPrimaryOTPVerify,
                            "0",
                            IsAdharDataVerify
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

                Log.d("UploadPatientDetails", "doInBackground: " + Arrays.toString(params));
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.BeneficiaryRe_RegistrationOTP, "UTF-8");
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

                if (!params[16].equals(""))
                    multipart.addFilePart("file1", new File(params[16]));
                if (!params[17].equals(""))
                    multipart.addFilePart("file2", new File(params[17]));
                if (!params[18].equals(""))
                    multipart.addFilePart("file3", new File(params[18]));

                multipart.addFormField("SecondaryMobileNo", params[19]);
                multipart.addFormField("IsPrimaryOTPVerify", params[20]);
                multipart.addFormField("IsSecondaryOTPVerify", params[21]);
                multipart.addFormField("IsAdharDataVerify", params[22]);
//                multipart.addFormField("Labcode", labCode);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(msg);
                        builder.setTitle("Success");
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(context, PatientAttendance_Activity.class)
//                                        .putExtra("campId", campId)
//                                        .putExtra("registrationNo", edt_workerregno.getText().toString().trim()));
                                startActivity(new Intent(context, PatientFingerAndSignature_Activity.class)
                                        .putExtra("campId", campId)
                                        .putExtra("siteId", siteId)
                                        .putExtra("registrationNo", edt_workerregno.getText().toString().trim()));
                                clearPatientDetails();
                                edt_workerregno.setText("");

                            }
                        });
                        AlertDialog alertD = builder.create();
                        alertD.show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PATIENT_CAMERA_REQUEST) {
//                CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v5.this);
            }
            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
//                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v5.this);
            }
            if (requestCode == RENEWAL_CAMERA_REQUEST) {
//                CropImage.activity(renewalURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v5.this);
            }
            if (requestCode == 10001) {
                IsAdharDataVerify = "0";
                String requiredValue = data.getStringExtra("key");
                Document doc = convertStringToDocument(requiredValue);

                if (doc != null) {
                    IsAdharDataVerify = "1";
                    parseXmlDocument(doc);
                } else {
                    IsAdharDataVerify = "0";
                    Utilities.showMessageString("Please try again", context);
                }
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

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseXmlDocument(Document outputDoc) {
        Element printLetterBarcodeDataElm = outputDoc.getDocumentElement();
        String co = printLetterBarcodeDataElm.getAttribute("co");
        String dist = printLetterBarcodeDataElm.getAttribute("dist");
        String dob = printLetterBarcodeDataElm.getAttribute("dob");
        String agender = printLetterBarcodeDataElm.getAttribute("gender");
        String house = printLetterBarcodeDataElm.getAttribute("house");
        String lm = printLetterBarcodeDataElm.getAttribute("lm");
        String loc = printLetterBarcodeDataElm.getAttribute("loc");
        String name = printLetterBarcodeDataElm.getAttribute("name");
        String pc = printLetterBarcodeDataElm.getAttribute("pc");
        String po = printLetterBarcodeDataElm.getAttribute("po");
        String state = printLetterBarcodeDataElm.getAttribute("state");
        String street = printLetterBarcodeDataElm.getAttribute("street");
        String subdist = printLetterBarcodeDataElm.getAttribute("subdist");
        String uid = printLetterBarcodeDataElm.getAttribute("uid");
        String vtc = printLetterBarcodeDataElm.getAttribute("vtc");
        String yob = printLetterBarcodeDataElm.getAttribute("yob");

        if (name != null && !name.equals("")) {
            edt_fname.setText(name);
        }

        if (agender != null && !agender.equals("")) {
            if (agender.equalsIgnoreCase("M")) {
                rg_gender.clearCheck();
                rb_male.setChecked(true);
                genderId = "M";
            } else if (agender.equalsIgnoreCase("F")) {
                rg_gender.clearCheck();
                rb_female.setChecked(true);
                genderId = "F";
            }
        }

        if (dob != null && !dob.equals("")) {
            edt_dob.setText(changeDateFormat("dd/MM/yyyy", "yyyy/MM/dd", dob));
        }

        if (uid != null && !uid.equals(""))
            edt_aadhaarno.setText(uid);

        if (pc != null && !pc.equals(""))
            edt_pincode.setText(pc);

        StringBuilder addressStrBuilder = new StringBuilder();

        if (house != null && !house.equals(""))
            addressStrBuilder.append(house + ", ");
        if (street != null && !street.equals(""))
            addressStrBuilder.append(street + ", ");
        if (lm != null && !lm.equals(""))
            addressStrBuilder.append(lm + ", ");
        if (po != null && !po.equals(""))
            addressStrBuilder.append(po + ", ");
        if (subdist != null && !subdist.equals(""))
            addressStrBuilder.append(subdist + ", ");
        if (dist != null && !dist.equals(""))
            addressStrBuilder.append(dist + ", ");
        if (state != null && !state.equals(""))
            addressStrBuilder.append(state);

        edt_local_address.setText(addressStrBuilder.toString().toUpperCase());
        edt_address.setText(addressStrBuilder.toString().toUpperCase());
    }

    public void savefile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String destinationFilename = "";
        if (imageType == 0) {
            isPatientPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
//            destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
            destinationFilename = getExternalCacheDir() + "/" + filename;
        } else if (imageType == 1) {
            isHealthCardPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
//            destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
            destinationFilename = getExternalCacheDir() + "/" + filename;
        } else if (imageType == 2) {
            isRenewalSlipPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_RHC.png";
//            destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
            destinationFilename = getExternalCacheDir() + "/" + filename;
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
        edt_fname.setText("");
        edt_moblieno.setText("");
        IsAdharDataVerify = "0";
        IsPrimaryOTPVerify = "0";
        edt_moblieno.setClickable(true);
        edt_moblieno.setEnabled(true);
        edt_aadhaarno.setText("");
        edt_aadhaarno.setFocusable(true);
        btn_verify.setEnabled(true);
        btn_verify.setClickable(true);
        edt_aadhaarno.setFocusableInTouchMode(true);
        edt_dob.setText("");
        edt_age.setText("");
        edt_address.setText("");
        edt_local_address.setText("");
        edt_pincode.setText("");
        edt_renewal_date.setText("");
        sw_renewed.setChecked(false);
        rb_mr.setChecked(true);
        rb_mrs.setChecked(false);
        rb_ms.setChecked(false);
        rb_male.setChecked(false);
        rb_female.setChecked(false);
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
    }

    private void disableUI() {
        isReregistration = true;
        edt_moblieno.setClickable(false);
        edt_moblieno.setEnabled(false);
        edt_aadhaarno.setEnabled(false);
        btn_verify.setEnabled(false);
        btn_verify.setClickable(false);
        sw_renewed.setEnabled(false);
        rb_mr.setEnabled(false);
        rb_mrs.setEnabled(false);
        rb_ms.setEnabled(false);
        rb_male.setEnabled(false);
        rb_female.setEnabled(false);
        edt_address.setEnabled(false);
        edt_fname.setEnabled(false);
        edt_dob.setEnabled(false);
        edt_local_address.setEnabled(false);
        edt_pincode.setEnabled(false);
        edt_age.setEnabled(false);
    }

    private void enableUI() {
        isReregistration = false;
        edt_moblieno.setClickable(true);
        edt_moblieno.setEnabled(true);
        edt_aadhaarno.setEnabled(true);
        btn_verify.setEnabled(true);
        btn_verify.setClickable(true);
        sw_renewed.setEnabled(true);
        rb_mr.setEnabled(true);
        rb_mrs.setEnabled(true);
        rb_ms.setEnabled(true);
        rb_male.setEnabled(true);
        rb_female.setEnabled(true);
        edt_address.setEnabled(true);
        edt_fname.setEnabled(true);
        edt_dob.setEnabled(true);
        edt_local_address.setEnabled(true);
        edt_pincode.setEnabled(true);
        edt_age.setEnabled(true);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

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


}
