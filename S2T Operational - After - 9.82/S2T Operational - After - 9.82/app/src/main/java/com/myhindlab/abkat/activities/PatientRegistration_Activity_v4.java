package com.myhindlab.abkat.activities;

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
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.InstantPatientDetailsModel;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;
import static com.myhindlab.abkat.utilities.Utilities.isLocationEnabled;
import static com.myhindlab.abkat.utilities.Utilities.provideCameraAndStorageAccess;
import static com.myhindlab.abkat.utilities.Utilities.provideLocationAccess;
import static com.myhindlab.abkat.utilities.Utilities.turnOnLocation;

public class PatientRegistration_Activity_v4 extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;

    private LinearLayout ll_usersdetails;
    private MaterialEditText edt_workerregno, edt_fname, edt_moblieno, edt_aadhaarno, edt_dob, edt_age,
            edt_address, edt_local_address, edt_city, edt_pincode, edt_renewal_date;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female;
    private CircleImageView imv_patient;
    private ImageView imv_search, imv_health_card, imv_renewal_form;
    private LinearLayout ll_renewal_photo;
    private Switch sw_renewed;
    private Button btn_register;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private Uri patientURI, healthCardURI, renewalURI;
    private Bitmap patientPicBm = null, healthCardPicBm = null, renewalPicBm = null;
    private final int PATIENT_CAMERA_REQUEST = 100, HEALTHCARD_CAMERA_REQUEST = 200, RENEWAL_CAMERA_REQUEST = 300;
    private String userId = "", campId = "", siteId = "", genderId = "", title = "", Latitude = "", Longitude = "", RegId = "",
            patientImagePath, healthCardImagePath, renewalImagePath;

    private File patientPicsFolder;
    private ProgressDialog pd;
    private int imageType = 0;
    String workregno = "", englishname = "", mobileno = "", uid = "", dob = "", age = "", paddress = "", laddress = "", pcode = "", r_id = "", IsHCRenewal,
            RenewalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration_v4);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = PatientRegistration_Activity_v4.this;
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
        edt_city = findViewById(R.id.edt_city);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_renewal_date = findViewById(R.id.edt_renewal_date);

        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
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

        imv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_workerregno.getText().toString().isEmpty()) {
                    edt_workerregno.setError("Please enter worker registration number");
                    return;
                }

                if (Utilities.isNetworkAvailable(context)) {
                    new BeneficiearyStatus().execute(edt_workerregno.getText().toString().trim());
                } else {
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
                patientURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, patientURI);
                startActivityForResult(intent, PATIENT_CAMERA_REQUEST);
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
//                File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_HC.png");
//
//                healthCardURI = Uri.fromFile(patientImageFile);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
//                startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);

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

//                int randomEndtNo = (int) (Math.random() * 99999 + 1);
//                File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_RHC.png");
//
//                renewalURI = Uri.fromFile(patientImageFile);
//                Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                pickImage.putExtra(MediaStore.EXTRA_OUTPUT, renewalURI);
//                startActivityForResult(pickImage, RENEWAL_CAMERA_REQUEST);

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
            }
        });

        sw_renewed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_renewal_date.setVisibility(View.VISIBLE);
                    ll_renewal_photo.setVisibility(View.VISIBLE);
                } else {
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
    }

    public class BeneficiearyStatus extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegNo", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.BeneficiearyStatus, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<InstantPatientDetailsModel.OutputBean> patientlist = new ArrayList<>();
                    InstantPatientDetailsModel pojoDetails = new Gson().fromJson(result, InstantPatientDetailsModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        patientlist = pojoDetails.getOutput();
                        if (patientlist.size() > 0) {

                            InstantPatientDetailsModel.OutputBean patientDetails = patientlist.get(0);

                            if (patientDetails.getStatus().equals("1")) {
                                ll_usersdetails.setVisibility(View.VISIBLE);
                                clearPatientDetails();
                                return;
                            } else if (patientDetails.getStatus().equals("2")) {
                                ll_usersdetails.setVisibility(View.GONE);
                                clearPatientDetails();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.icon_warning);
                                builder.setTitle("Alert");
                                builder.setMessage("Beneficiary is already screened");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        edt_workerregno.setText("");
                                    }
                                });
                                builder.show();
                                return;
                            } else if (patientDetails.getStatus().equals("3")) {
                                //Toast.makeText(context, "Here I am"+3, Toast.LENGTH_SHORT).show();
//                                ll_usersdetails.setVisibility(View.VISIBLE);
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context,1);
//                                builder.setIcon(R.drawable.icon_warning);
//                                builder.setTitle("Alert");
//                                builder.setMessage("Beneficiary is already mapped to other site. Do you want to change mapping?");
//                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        ShowDetails(patientDetails);
//                                    }
//                                });
//                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        ll_usersdetails.setVisibility(View.GONE);
//                                        clearPatientDetails();
//                                        edt_workerregno.setText("");
//                                    }
//                                });
//                                builder.show();

                            } else if (patientDetails.getStatus().equals("4")) {

                            }
                            ShowDetails(patientDetails);

                            imv_patient.setImageDrawable(getResources().getDrawable(R.drawable.icon_patientcamera));
                            patientPicBm = null;
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

    private void ShowDetails(InstantPatientDetailsModel.OutputBean patientDetails) {
        RegId = patientDetails.getRegdId();
        btn_register.setEnabled(true);
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

        edt_fname.setText(patientDetails.getEnglishName());

        edt_aadhaarno.setText(patientDetails.getUID().replace("-", ""));

        edt_dob.setText(patientDetails.getDOBFormated());

        edt_age.setText(patientDetails.getAge());

        edt_moblieno.setText(patientDetails.getMobileNo());

        edt_address.setText(patientDetails.getPermanentAddress());

        edt_city.setText(patientDetails.getLocation());

        edt_pincode.setText(patientDetails.getPincode());
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

        if (!Utilities.isaadharNumberValidate(edt_aadhaarno.getText().toString().trim())) {
            edt_aadhaarno.setError("Please enter valid Aadhar Card No.");
            return;
        }

//        if (edt_aadhaarno.getText().toString().trim().length() != 12) {
//            edt_aadhaarno.setError("Please enter valid Aadhar Card No.");
//            return;
//        }

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

//        if (edt_city.getText().toString().trim().isEmpty()) {
//            edt_city.setError("Please enter city");
//            return;
//        }

        if (!Utilities.isValidPincode(edt_pincode.getText().toString().trim())) {
            edt_pincode.setError("Please enter valid pincode");
            return;
        }

        if (patientPicBm == null) {
            Utilities.showToastMessage("Please click patient photo", context, false);
            return;
        }

        if (healthCardPicBm == null) {
            Utilities.showToastMessage("Please click health card", context, false);
            return;
        }

        if (sw_renewed.isChecked()) {
            if (edt_renewal_date.getText().toString().trim().isEmpty()) {
                edt_renewal_date.setError("Please renewal date");
                return;
            }

            if (renewalPicBm == null) {
                Utilities.showToastMessage("Please click renewal slip photo", context, false);
                return;
            }
        }

        if (Latitude == null) {
            Latitude = "0.0";
        }

        if (Longitude == null) {
            Longitude = "0.0";
        }

        if (Utilities.isNetworkAvailable(context)) {
            workregno = /*"0" +*/ edt_workerregno.getText().toString().trim();
            englishname = edt_fname.getText().toString().trim();
            mobileno = edt_moblieno.getText().toString().trim();
            uid = edt_aadhaarno.getText().toString().trim();
            dob = edt_dob.getText().toString().trim();
            age = edt_age.getText().toString().trim();
            paddress = edt_address.getText().toString().trim();
            laddress = edt_local_address.getText().toString().trim();
            pcode = edt_pincode.getText().toString().trim();
            IsHCRenewal = sw_renewed.isChecked() ? "1" : "0";
            RenewalDate = sw_renewed.isChecked() ? edt_renewal_date.getText().toString().trim() : "";

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert!");
            builder.setMessage("Please confirm the beneficiary's details before submitting");
            builder.setCancelable(false);
            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (sw_renewed.isChecked()) new UploadPatientDetailsWithRenewalDate().execute();
                    else new UploadPatientDetails().execute();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PATIENT_CAMERA_REQUEST) {
//                CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v4.this);
            }
            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
//                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v4.this);
            }
            if (requestCode == RENEWAL_CAMERA_REQUEST) {
//                CropImage.activity(renewalURI).setGuidelines(CropImageView.Guidelines.ON).start(PatientRegistration_Activity_v4.this);
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
            String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
//            destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
            destinationFilename = getExternalCacheDir()+"/" + filename;
        } else if (imageType == 1) {
            String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
//            destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
            destinationFilename = getExternalCacheDir()+"/" + filename;
        } else if (imageType == 2) {
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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.PatientRegistrationAndHealthCard, "UTF-8");
                multipart.addFormField("SiteId", siteId);
                multipart.addFormField("CampId", campId);
                multipart.addFormField("RegdNo", workregno);
                multipart.addFormField("Title", title);
                multipart.addFormField("EnglishName", englishname);
                multipart.addFormField("MobileNo", mobileno);
                multipart.addFormField("UID", uid);
                multipart.addFormField("DOB", dob);
                multipart.addFormField("Age", age);
                multipart.addFormField("Gender", genderId);
                multipart.addFormField("PermanentAddress", paddress);
                multipart.addFormField("LocalAddress", laddress);
                multipart.addFormField("PinCode", pcode);
                multipart.addFormField("CreatedBy", userId);
                multipart.addFormField("IsHCRenewal", "0");
                multipart.addFormField("RenewalDate", "");
                multipart.addFilePart("file1", new File(patientImagePath));
                multipart.addFilePart("file2", new File(healthCardImagePath));
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
                        builder.setMessage("Patient registered Successfully!");
                        builder.setTitle("Success");
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ClearForm();
                                startActivity(new Intent(context, PatientAttendance_Activity.class)
                                        .putExtra("campId", campId)
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




    private class UploadPatientDetailsWithRenewalDate extends AsyncTask<String, Integer, String> {

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

                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.PatientRegistrationAndHealthCard, "UTF-8");
                multipart.addFormField("SiteId", siteId);
                multipart.addFormField("CampId", campId);
                multipart.addFormField("RegdNo", workregno);
                multipart.addFormField("Title", title);
                multipart.addFormField("EnglishName", englishname);
                multipart.addFormField("MobileNo", mobileno);
                multipart.addFormField("UID", uid);
                multipart.addFormField("DOB", dob);
                multipart.addFormField("Age", age);
                multipart.addFormField("Gender", genderId);
                multipart.addFormField("PermanentAddress", paddress);
                multipart.addFormField("LocalAddress", laddress);
                multipart.addFormField("PinCode", pcode);
                multipart.addFormField("CreatedBy", userId);
                multipart.addFormField("IsHCRenewal", "1");
                multipart.addFormField("RenewalDate", RenewalDate);
                multipart.addFilePart("file1", new File(patientImagePath));
                multipart.addFilePart("file2", new File(healthCardImagePath));
                multipart.addFilePart("file3", new File(renewalImagePath));
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
                        builder.setMessage("Patient registered Successfully!");
                        builder.setTitle("Success");
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ClearForm();
                                startActivity(new Intent(context, PatientAttendance_Activity.class)
                                        .putExtra("campId", campId)
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

    private void ClearForm() {
        edt_fname.setText("");
        edt_moblieno.setText("");
        edt_aadhaarno.setText("");
        edt_dob.setText("");
        edt_age.setText("");
        edt_address.setText("");
        edt_local_address.setText("");
        edt_city.setText("");
        edt_renewal_date.setText("");
        sw_renewed.setChecked(false);
        edt_pincode.setText("");
        rb_mr.setChecked(true);
        rb_mrs.setChecked(false);
        rb_ms.setChecked(false);
        rb_male.setChecked(true);
        rb_female.setChecked(false);
        imv_patient.setImageDrawable(getResources().getDrawable(R.drawable.icon_patientcamera));
        imv_health_card.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
        imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
    }

    private void clearPatientDetails() {
        edt_fname.setText("");
        edt_moblieno.setText("");
        edt_aadhaarno.setText("");
        edt_dob.setText("");
        edt_age.setText("");
        edt_address.setText("");
        edt_local_address.setText("");
        edt_city.setText("");
        edt_pincode.setText("");
        edt_renewal_date.setText("");
        sw_renewed.setChecked(false);
        rb_mr.setChecked(true);
        rb_mrs.setChecked(false);
        rb_ms.setChecked(false);
        rb_male.setChecked(true);
        rb_female.setChecked(false);
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
        r_id = "";
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

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LatLng latLng;

    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
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

//    TextWatcher textWatcher = new TextWatcher() {
//
//        private String current = "";
//        private String yyyymmdd = "YYYYMMDD";
//        private Calendar cal = Calendar.getInstance();
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            if (!s.toString().equals(current)) {
//                int year = 0, mon = 0, day = 0;
//
//                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
//                String cleanC = current.replaceAll("[^\\d.]|\\.", "");
//
//                int cl = clean.length();
//                int sel = cl;
//                for (int i = 4; i <= cl && i < 8; i += 2) {
//                    sel++;
//                }
//                //Fix for pressing delete next to a forward slash
//                if (clean.equals(cleanC)) sel--;
//
//                if (clean.length() < 8) {
//                    clean = clean + yyyymmdd.substring(clean.length());
//                } else {
//                    //This part makes sure that when we finish entering numbers
//                    //the date is correct, fixing it otherwise
//                    year = Integer.parseInt(clean.substring(0, 4));
//                    mon = Integer.parseInt(clean.substring(4, 6));
//                    day = Integer.parseInt(clean.substring(6, 8));
//
//                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
//                    day = day < 1 ? 1 : day > 31 ? 31 : day;
//                    cal.set(Calendar.MONTH, mon - 1);
////                    year = (year < 1900) ? 1900 : (year > 2100) ? 1900 : year;
//                    cal.set(Calendar.YEAR, year);
//                    // ^ first set year for the line below to work correctly
//                    //with leap years - otherwise, date e.g. 29/02/2012
//                    //would be automatically corrected to 28/02/2012
//
//                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
//                    clean = String.format("%02d%02d%02d", year, mon, day);
//                }
//
//                clean = String.format("%s/%s/%s", clean.substring(0, 4),
//                        clean.substring(4, 6),
//                        clean.substring(6, 8));
//
//                sel = sel < 0 ? 0 : sel;
//                current = clean;
//
//                edt_dob.setText(current);
//                edt_dob.setSelection(sel < current.length() ? sel : current.length());
//
//                if (year != 0 && mon != 0 && day != 0) {
//                    int age = Utilities.getAge(Integer.parseInt(String.format("%02d", year)),
//                            Integer.parseInt(String.format("%02d", mon)),
//                            Integer.parseInt(String.format("%02d", day)));
//                    if (age < 18 || age > 60) {
//                        Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years or more than 60 years", false);
//                        edt_age.setText("");
//                        return;
//                    }
//                    edt_age.setText(String.valueOf(age));
//                } else {
//                    edt_age.setText("");
//                }
//
//            }
//        }
//    };
}
