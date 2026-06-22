package com.myhindlab.abkat.activities.doortodoor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.myhindlab.abkat.utilities.Utilities.changeDateFormat;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;
import static com.myhindlab.abkat.utilities.Utilities.isLocationEnabled;
import static com.myhindlab.abkat.utilities.Utilities.provideCameraAndStorageAccess;
import static com.myhindlab.abkat.utilities.Utilities.provideLocationAccess;
import static com.myhindlab.abkat.utilities.Utilities.turnOnLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.BarcodeScanner_Activity;
import com.myhindlab.abkat.activities.PatientFingerAndSignature_Activity;
import com.myhindlab.abkat.adapters.doortodoor.DependentSearchAdapter;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.PatientDetailsModel;
import com.myhindlab.abkat.models.RealationCheckModel;
import com.myhindlab.abkat.models.doortodoor.AgeCalculation;
import com.myhindlab.abkat.models.doortodoor.DocumentTypeModel;
import com.myhindlab.abkat.pojos.doortodoor.DependentPojo;
import com.myhindlab.abkat.pojos.doortodoor.LiveHealthRequest;
import com.myhindlab.abkat.pojos.doortodoor.RelationPojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.PermissionUtil;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class D2DPatientRegistration_Activity_Old extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;

    private TextView tvHealthCard, tvRegdNo;
    private LinearLayout ll_usersdetails, mainCbLL, rbll;
    private MaterialEditText edt_workerregno,txt_beneficiary_gender_change, edt_fname,txt_beneficiary_Fname,txt_beneficiary_Mname,txt_beneficiary_Lname, edt_moblieno, edt_aadhaarno, edt_renewalDate, edt_taluka, edt_district, edt_dob, edt_age, edt_postOffice, edt_alternetMoblieno, edt_landMark, edt_current_address,
            edt_address, edt_local_address, edt_pincode, edt_renewal_date, edt_relation, edtIdentity, edt_education, txt_beneficiary_name,txt_beneficiary_age,txt_beneficiary_gender;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female, rb_self, rb_spouse, rb_chlid;
    private CircleImageView imv_patient;
    private ImageView imv_search, imv_health_card, imv_renewal_form, imv_hiv_concern, imv_self_declaration;
    private LinearLayout ll_renewal_photo, llSelfDeclaration, llHealthCard;
    private RadioGroup rg_gender, rg_selection;
    private Switch sw_renewed;
    private Button btn_register, btn_VerifyOtp, btn_VerifyOtpContact;
    private CheckBox cbIsNumberBgs;
    private String isAdmin, otpnumber,teamId ="0", mobileno;

    private RadioButton rg_dep_yes, rg_dep_no;
    String regdID = "", registeredPatientregdid = "";
    ImageButton barCodeScannerBtn;
    private String IsPrimaryOTPVerify = "0", IsAdharDataVerify = "0";

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private Uri patientURI, healthCardURI, renewalURI, hivConcernURI;
    private Bitmap patientPicBm = null, healthCardPicBm = null, IdentityPicBm = null, renewalPicBm = null, hivConcernBm = null;
    private final int PATIENT_CAMERA_REQUEST =
            0, HEALTHCARD_CAMERA_REQUEST = 200, RENEWAL_CAMERA_REQUEST = 300, HIV_CONCERN_CAMERA_REQUEST = 400, IDENTITY_CAMERA_REQUEST = 500;
    private String userId = "", campId = "", siteId = "", genderId = "", title = "", Latitude = "", Longitude = "", RegId = "",
            patientImagePath = "", healthCardImagePath = "", renewalImagePath = "", hivletterPath = "", isRenewalFlag = "0", relationId = "20", relationGenderId = "",relationWorker ="",labourage, identityId = "0";
    private boolean isPatientPhotoAvailable = false, isHealthCardPhotoAvailable = false, isHivConcernPhotoAvailable = false, isRenewalSlipPhotoAvailable = false;
    private boolean isReregistration = false;

    private File patientPicsFolder;
    private ProgressDialog pd;
    private int imageType = 0;
    private String isDependent = "0";
    private RecyclerView dependent_list_recycler_view;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter, mAdapter1;
    String count = "", DependREGID = "0", flag = "0";
    String isRTPCR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2d_patient_registration);

        init();
        disableViewAfterFill();
//        requestPermission();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();

    }


    private void verifyOtp(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_verify_otp, null);
        alertBuilder.setView(alertLayout);
        TextInputEditText edt_Otp = alertLayout.findViewById(R.id.edt_Otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        verifyOtpBtn.setOnClickListener(view -> {
            //verify api call

            if (edt_Otp.getText().toString().trim().matches("")) {
                edt_Otp.setError("Please enter otp");
                return;
            }
//            if (edt_Otp.getText().toString().trim().length() != 10) {
//                edt_Otp.setError("Please enter valid otp");
//                return;
//            }


//            if (!edt_Otp.getText().toString().trim().equals(otpnumber)) {
//                edt_Otp.setError("Entered OTP is not matched");
//                return;
//            }
            if (cbIsNumberBgs.isChecked()) {
                new VerifyOtp(alertDialog).execute(edt_alternetMoblieno.getText().toString().trim(), edt_Otp.getText().toString().trim());

            } else {
                new VerifyOtpContact(alertDialog).execute(edt_moblieno.getText().toString(), edt_Otp.getText().toString().trim());

            }

//            Utilities.showToastMessage("OTP Verified Successfully", context, true);
//            btn_register.setVisibility(View.VISIBLE);


            //
            // alertDialog.dismiss();
        });

        resendOtpBtn.setOnClickListener(view -> {
            //verify api call
            // alertDialog.dismiss();
            if (cbIsNumberBgs.isChecked()) {
                new GetOtp(2).execute(edt_alternetMoblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0");
                edt_Otp.setText("");

            } else {
                new GetOtp(2).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0");
                edt_Otp.setText("");
            }
        });

    }


    private void init() {
        context = D2DPatientRegistration_Activity_Old.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        ll_usersdetails = findViewById(R.id.ll_usersdetails);
        imv_patient = findViewById(R.id.imv_patient);
        imv_search = findViewById(R.id.imv_search);
        imv_health_card = findViewById(R.id.imv_health_card);
        imv_self_declaration = findViewById(R.id.imv_self_declaration);
        imv_hiv_concern = findViewById(R.id.imv_hiv_concern);
        imv_renewal_form = findViewById(R.id.imv_renewal_form);
        sw_renewed = findViewById(R.id.sw_renewed);
        ll_renewal_photo = findViewById(R.id.ll_renewal_photo);
        llSelfDeclaration = findViewById(R.id.llSelfDeclaration);
        rg_selection = findViewById(R.id.rg_selection);
        llHealthCard = findViewById(R.id.llHealthCard);
        tvHealthCard = findViewById(R.id.tvHealthCard);
        edt_current_address = findViewById(R.id.edt_current_address);
        edt_landMark = findViewById(R.id.edt_landMark);
        edt_alternetMoblieno = findViewById(R.id.edt_alternetMoblieno);
        rb_self = findViewById(R.id.rb_self);
        rb_spouse = findViewById(R.id.rb_spouse);
        txt_beneficiary_Fname = findViewById(R.id.txt_beneficiary_Fname);
        txt_beneficiary_Mname = findViewById(R.id.txt_beneficiary_Mname);
        txt_beneficiary_Lname = findViewById(R.id.txt_beneficiary_Lname);
        txt_beneficiary_age = findViewById(R.id.txt_beneficiary_age);
        txt_beneficiary_gender = findViewById(R.id.txt_beneficiary_gender);
        txt_beneficiary_gender_change = findViewById(R.id.txt_beneficiary_gender_change);
        rb_chlid = findViewById(R.id.rb_chlid);
        rg_selection.check(R.id.rb_self);


        tvRegdNo = findViewById(R.id.tvRegdNo);
        mainCbLL = findViewById(R.id.mainCbLL);
        cbIsNumberBgs = findViewById(R.id.cbIsNumberBgs);

        edt_renewalDate = findViewById(R.id.edt_renewalDate);
        edt_taluka = findViewById(R.id.edt_taluka);
        edt_district = findViewById(R.id.edt_district);
        edt_postOffice = findViewById(R.id.edt_postOffice);

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
        edt_relation = findViewById(R.id.edt_relation);
        edtIdentity = findViewById(R.id.edtIdentity);
        edt_education = findViewById(R.id.edt_education);
        rbll = findViewById(R.id.rbll);

        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        rg_gender = findViewById(R.id.rg_gender);
        btn_register = findViewById(R.id.btn_register);
        rg_dep_yes = findViewById(R.id.rg_dep_yes);
        rg_dep_no = findViewById(R.id.rg_dep_no);
        btn_VerifyOtp = findViewById(R.id.btn_VerifyOtp);
        btn_VerifyOtpContact = findViewById(R.id.btn_VerifyOtpContact);
        barCodeScannerBtn = findViewById(R.id.barcode_scanner_btn);

        title = "Mr.";
        txt_beneficiary_name = findViewById(R.id.txt_beneficiary_name);

//       mainCbLL.setVisibility(View.GONE);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

//        patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Photos/");
//        if (!patientPicsFolder.exists())
//            patientPicsFolder.mkdirs();

        if (SDK_INT >= Build.VERSION_CODES.Q) {
            patientPicsFolder = getExternalCacheDir();
        } else {
            patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Photos/");

            if (!patientPicsFolder.exists())
                patientPicsFolder.mkdirs();
        }
//        patientPicsFolder = getExternalCacheDir();
//        if (!patientPicsFolder.exists())
//            patientPicsFolder.mkdirs();
    }

    private void requestPermission() {
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            if (Environment.isExternalStorageManager()) {
//                //todo when permission is granted
//            } else {
//                try {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                    intent.addCategory("android.intent.category.DEFAULT");
//                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
//                    startActivityForResult(intent, 2296);
//                } catch (Exception e) {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    startActivityForResult(intent, 2296);
//                }
//            }
//
//        } else {
        //below android 11
        if (!PermissionUtil.askPermissions(this)) {
        }
        // }
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
        isRTPCR = getIntent().getStringExtra("IsRtpcr");
        edt_relation.setText("Self");

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mYear1 = cal.get(Calendar.YEAR);
        mMonth1 = cal.get(Calendar.MONTH);
        mDay1 = cal.get(Calendar.DAY_OF_MONTH);

//        edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

        if (session.isHllUser()){
            new GetTeamId().execute();
        }


        edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() +" "+ txt_beneficiary_Mname.getText().toString().trim() +" "+ txt_beneficiary_Lname.getText().toString().trim());
    }

    private void setEventHandler() {
        barCodeScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);
            }
        });

        txt_beneficiary_gender_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("Male", 1));
                campTypeModelArrayList.add(new CampTypeModel("Female", 2));
                //                campTypeModelArrayList.add(new CampTypeModel("CSC", 2));

                showCampType(campTypeModelArrayList);


            }
        });



        cbIsNumberBgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cbIsNumberBgs.isChecked()) {
                    btn_VerifyOtp.setVisibility(View.VISIBLE);
                    edt_alternetMoblieno.setVisibility(View.VISIBLE);
                    btn_VerifyOtpContact.setVisibility(View.GONE);
                    rbll.setVisibility(View.VISIBLE);


                } else {
                    btn_VerifyOtp.setVisibility(View.GONE);
                    edt_alternetMoblieno.setVisibility(View.GONE);
                    btn_VerifyOtpContact.setVisibility(View.VISIBLE);
                    rbll.setVisibility(View.GONE);
                }

            }
        });

        btn_VerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //api call api success
                if (edt_alternetMoblieno.getText().toString().trim().matches("")) {
                    edt_alternetMoblieno.setError("Please enter mobile number");
                    return;
                }
                if (edt_alternetMoblieno.getText().toString().trim().length() != 10) {
                    edt_alternetMoblieno.setError("Please enter valid 10 digit mobile number");
                    return;
                }
                //  Utilities.showToastMessage("OTP Send Successfully", context, true);
//                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdId()));
//
//                finish();
//                new GenerateOtp().execute();
                new GetOtp(1).execute(edt_alternetMoblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0");

            }
        });

        btn_VerifyOtpContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                mainCbLL.setVisibility(View.VISIBLE);
                //api call api success
                if (edt_moblieno.getText().toString().trim().matches("")) {
                    edt_moblieno.setError("Please enter mobile number");
                    return;
                }
                if (edt_moblieno.getText().toString().trim().length() != 10) {
                    edt_moblieno.setError("Please enter valid 10 digit mobile number");
                    return;
                }
                //  Utilities.showToastMessage("OTP Send Successfully", context, true);
//                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdId()));
//
//                finish();
//                new GenerateOtp().execute();

                new GetOtp(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0");

            }
        });


        edt_relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDependent.equalsIgnoreCase("1"))
                    new GetRelationList().execute();
            }
        });
        edtIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDependent.equalsIgnoreCase("1"))
                    new GetIdentityList().execute();
            }
        });


        rg_dep_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rg_dep_yes.isChecked()) {
                    clearPatientDetails();
                    enableViewAfterFill();
                    edt_current_address.setVisibility(View.VISIBLE);
                    edt_local_address.setVisibility(View.VISIBLE);
                    edt_landMark.setVisibility(View.VISIBLE);
                    edt_taluka.setVisibility(View.VISIBLE);
                    edt_taluka.setEnabled(true);
                    edt_district.setEnabled(true);
                    edt_district.setVisibility(View.VISIBLE);
//                    btn_VerifyOtp.setVisibility(View.VISIBLE);
                    btn_VerifyOtpContact.setVisibility(View.VISIBLE);
//                    btn_register.setVisibility(View.VISIBLE);
//                    btn_VerifyOtp.setVisibility(View.GONE);
//                    edt_alternetMoblieno.setVisibility(View.VISIBLE);

                    rg_gender.setEnabled(true);
                    rb_male.setEnabled(true);
                    rb_female.setEnabled(true);
//                    rbll.setVisibility(View.VISIBLE);
                    mainCbLL.setVisibility(View.VISIBLE);

                    txt_beneficiary_Fname.setText("");
                    txt_beneficiary_Fname.setEnabled(true);
                    txt_beneficiary_Mname.setText("");
                    txt_beneficiary_Mname.setEnabled(true);
                    txt_beneficiary_Lname.setText("");
                    txt_beneficiary_gender_change.setVisibility(View.VISIBLE);


                    edt_workerregno.setText("");
                    edt_workerregno.setEnabled(true);
                    isDependent = "1";
//                    edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    edt_relation.setText("");
                    relationId = "0";
                    rg_dep_yes.setChecked(true);
                    rb_female.setEnabled(true);
                    rb_male.setEnabled(true);
                    edtIdentity.setVisibility(View.VISIBLE);
                    tvRegdNo.setVisibility(View.GONE);
//                    llSelfDeclaration.setVisibility(View.VISIBLE);
//                    llHealthCard.setVisibility(View.GONE);
                    tvHealthCard.setText("Indentity Card");
                    edt_aadhaarno.setHint("Aadhar Number");

                }
            }
        });

        rg_dep_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rg_dep_no.isChecked()) {
                    clearPatientDetails();
                    disableViewAfterFill();
                    btn_register.setVisibility(View.GONE);
                    edt_current_address.setVisibility(View.GONE);
                    edt_local_address.setVisibility(View.GONE);
                    edt_landMark.setVisibility(View.GONE);
                    edt_taluka.setVisibility(View.GONE);
                    edt_district.setVisibility(View.GONE);
                    btn_VerifyOtp.setVisibility(View.GONE);
                    btn_VerifyOtp.setText("Verify Otp");
                    btn_VerifyOtp.setEnabled(true);
                    rbll.setVisibility(View.GONE);
                    edt_workerregno.setEnabled(true);
                    btn_VerifyOtpContact.setVisibility(View.GONE);
                    mainCbLL.setVisibility(View.GONE);

                    //  txt_beneficiary_age.setVisibility(View.GONE);
                    txt_beneficiary_gender_change.setVisibility(View.GONE);
                    txt_beneficiary_Fname.setText("");
                    txt_beneficiary_Mname.setText("");
                    txt_beneficiary_Lname.setText("");


                    rg_gender.setEnabled(true);
                    rb_male.setEnabled(true);
                    rb_female.setEnabled(true);


                    edt_workerregno.setText("");
                    isDependent = "0";
//                    edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    if (txt_beneficiary_name.getVisibility() == View.VISIBLE)
                        txt_beneficiary_name.setVisibility(View.GONE);
                    relationId = "20";
                    edt_relation.setText("Self");
                    rg_dep_no.setChecked(true);

                    tvHealthCard.setText("Benificiary Card");
                    edt_aadhaarno.setHint("Aadhar Number*");
                    edtIdentity.setVisibility(View.GONE);
                    tvRegdNo.setVisibility(View.GONE);

//                    llSelfDeclaration.setVisibility(View.GONE);
//                    llHealthCard.setVisibility(View.VISIBLE);


                } else {

                }
            }
        });

        rg_dep_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    clearPatientDetails();
                    disableViewAfterFill();
                    btn_VerifyOtp.setText("Verify Otp");
                    btn_VerifyOtp.setEnabled(true);
                    edt_workerregno.setText("");
                    isDependent = "0";
//                    edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    if (txt_beneficiary_name.getVisibility() == View.VISIBLE)
                        txt_beneficiary_name.setVisibility(View.GONE);
                    relationId = "20";
                    edt_relation.setText("Self");
                } else {
                    enableViewAfterFill();
                }
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


        txt_beneficiary_Fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() +" "+ txt_beneficiary_Mname.getText().toString().trim() +" "+ txt_beneficiary_Lname.getText().toString().trim());
            }
        });

        txt_beneficiary_Mname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() +" "+ txt_beneficiary_Mname.getText().toString().trim() +" "+ txt_beneficiary_Lname.getText().toString().trim());
            }
        });
        txt_beneficiary_Lname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() +" "+ txt_beneficiary_Mname.getText().toString().trim() +" "+ txt_beneficiary_Lname.getText().toString().trim());
            }
        });

        edt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        AgeCalculation age1 = new AgeCalculation();
//                        age1.setDateOfBirth(year, (month + 1), dayOfMonth);
//                        age1.setCurrentDate();
//
//                        age1.calcualteYear();
//                        age1.calcualteMonth();
//                        age1.calcualteDay();

                        //   int age= Integer.parseInt(age1.getResYear());
//                        int age = Utilities.getAge(Integer.parseInt(String.format("%02d", year)),
//                                Integer.parseInt(String.format("%02d", month)),
//                                Integer.parseInt(String.format("%02d", dayOfMonth)));
//                        if (isDependent.equalsIgnoreCase("0")) {
//                            if (age < 18 || age > 60) {
//                                Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 18 years or more than 60 years", false);
//                                return;
//                            }
//                        } else {
//                            if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8") ||
//                                    relationId.equalsIgnoreCase("5") || relationId.equalsIgnoreCase("6")) {
//                                if (age < 1 || age > 100) {
//                                    Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 1 years or more than 100 years", false);
//                                    return;
//                                }
//                            } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
//                                    || relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
//                                    relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
//                                if (age < 18 || age > 100) {
//                                    Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 18 years or more than 100 years", false);
//                                    return;
//                                }
//                            }
//                        }

                        edt_dob.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, month + 1, year));
                        // edt_age.setText(String.valueOf(age));
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

        //     edt_dob.addTextChangedListener(textWatcher);
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
                        if (isDependent.equalsIgnoreCase("0")) {
                            if (Utilities.isNetworkAvailable(context)) {
                                new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
//                                new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
                                edt_workerregno.clearFocus();

                                //       new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());


                            } else {
                                isReregistration = false;
                                edt_workerregno.setText("");
                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                            }
                        }

//                        else {
//                            if (Utilities.isNetworkAvailable(context)) {
//                                new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());
//                                edt_workerregno.clearFocus();
//                            } else {
//                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                            }
//                        }
                    } else {
                        edt_workerregno.setError("Enter valid 12 digit worker registration number");
                    }
                }
            }
        });

        edt_dob.addTextChangedListener(new

                                               TextWatcher() {
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

                                                                   if (year < 1921) {
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

//                                                                   AgeCalculation age1 = new AgeCalculation();
//                                                                   age1.setDateOfBirth(year, (mon), day);
//                                                                   age1.setCurrentDate();
//
//                                                                   age1.calcualteYear();
//                                                                   age1.calcualteMonth();
//                                                                   age1.calcualteDay();


                                                                   AgeCalculation age1 = new AgeCalculation();
                                                                   age1.setDateOfBirth(year, mon, day); // mon should be 1-based (e.g., Jan = 1, Dec = 12)
                                                                   age1.calculateAge();

                                                                   int age = Integer.parseInt(age1.getResYear());
                                                                   edt_age.setText(String.valueOf(age));


//
//                                                                   int age = Integer.parseInt(age1.getResYear());
//                                                                   edt_age.setText(String.valueOf(age));

//                                                                   int age = Utilities.getAge(Integer.parseInt(String.format("%02d", year)),
//                                                                           Integer.parseInt(String.format("%02d", mon)),
//                                                                           Integer.parseInt(String.format("%02d", day)));
//                                                                   if (age < 18 || age > 60) {
//                                                                       Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 18 years or more than 60 years", false);
//                                                                       edt_age.setText("");
//                                                                   } else {
//                                                                       edt_age.setText(String.valueOf(age));
//                                                                   }

                                                                   if (isDependent.equalsIgnoreCase("0")) {
                                                                       if (age < 18 || age > 60) {
                                                                           Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 60 years", false);
                                                                           edt_age.setText("");
                                                                       } else {
                                                                           edt_age.setText(String.valueOf(age));
                                                                       }
                                                                   } else {
                                                                       if (relationId.equals("0")) {
                                                                           Utilities.showAlertDialog(context, "Alert", "Please select relation first", false);
                                                                           edt_age.setText("");
                                                                           return;
                                                                       }
                                                                       if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")
                                                                               || relationId.equalsIgnoreCase("5") || relationId.equalsIgnoreCase("6")) {
                                                                           if (!(age >= 10 && age <= Integer.parseInt(labourage)-18)) {

                                                                               // int a =  Integer.parseInt(labourage)-18;

                                                                               Utilities.showAlertDialog(context, "Alert", "Age of Son and Daughter should be \n" +
                                                                                       "1. Greater than 10 years.\n" +
                                                                                       "2. Lesser than Labour age - 18 years", false);
                                                                               edt_age.setText("");
                                                                           } else {
                                                                               edt_age.setText(String.valueOf(age));
                                                                           }
                                                                       } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
                                                                               || relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
                                                                               relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
                                                                           if (age < 18 || age > 75) {
                                                                               Utilities.showAlertDialog(context, "Alert", "Dependent age should not be less than 18 years or more than 75 years", false);
                                                                               edt_age.setText("");
                                                                           } else {
                                                                               edt_age.setText(String.valueOf(age));
                                                                           }
                                                                       }
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

                if (edt_workerregno.getText().toString().isEmpty() || edt_workerregno.getText().toString().length() != 12) {
                    edt_workerregno.setError("Please enter 12 digit worker registration number");
                    return;
                }
                if (isDependent.equalsIgnoreCase("0")) {
                    if (Utilities.isNetworkAvailable(context)) {
//                        new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
                        new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

//                        enableViewAfterFill();
                    } else {
                        isReregistration = false;
                        edt_workerregno.setText("");
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                } else {
                    new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());


                }
            }
        });

        imv_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 0;
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
                if (SDK_INT >= Build.VERSION_CODES.Q) {
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

        imv_health_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 1;
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
        imv_hiv_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 3;
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
//                File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_HIV.png");
//
//                hivConcernURI = Uri.fromFile(patientImageFile);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, hivConcernURI);
//                startActivityForResult(intent, HIV_CONCERN_CAMERA_REQUEST);

                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_HIV.png");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                hivConcernURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, hivConcernURI);
                startActivityForResult(intent, HIV_CONCERN_CAMERA_REQUEST);
            }
        });

        imv_self_declaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 1;
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


                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_RHC.png");
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
                                Utilities.showAlertDialog(context, "Cannot re-register the Labour", "You have done registration  on " + patientDetails.getRegistrationDate(), false);

                                clearPatientDetails();
                                return;
                            }
                            isReregistration = true;
//                            loadPatientDetails(patientDetails);
//                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());


                        } else {
//                            clearPatientDetails();
                            relationId = "20";
                            edt_relation.setText("Self");
//                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

                        }
                    } else {
//                        clearPatientDetails();
                        relationId = "20";
                        edt_relation.setText("Self");
//                        new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

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


    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Worker Gender");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txt_beneficiary_gender_change.setText(campTypeModelsList.get(which).getCampTypeName());
                relationWorker = String.valueOf(campTypeModelsList.get(which).getCampTypeId());


                if (relationWorker.equalsIgnoreCase("1")) {
                    relationGenderId = "Male";
                } else if (relationWorker.equalsIgnoreCase("2")) {
                    relationGenderId = "Female";


                }

                edt_relation.setText("");

                //  refreshCalendar();


            }
        });
        builderSingle.show();
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
                        if (rg_dep_yes.isChecked()) {
                            String fName = jsonArray.getJSONObject(0).getString("firstNamePersonal") == "null" ? "" : jsonArray.getJSONObject(0).getString("firstNamePersonal");
                            String mName = jsonArray.getJSONObject(0).getString("middleNamePersonal") == "null" ? "" : jsonArray.getJSONObject(0).getString("middleNamePersonal");
                            String lName = jsonArray.getJSONObject(0).getString("lastNamePersonal") == "null" ? "" : jsonArray.getJSONObject(0).getString("lastNamePersonal");

                            String mobile = jsonArray.getJSONObject(0).getString("mobile") == "null" ? "" : jsonArray.getJSONObject(0).getString("mobile");
                            String renewalDate = jsonArray.getJSONObject(0).getString("next_renewal_date") == "null" ? "" : jsonArray.getJSONObject(0).getString("next_renewal_date");
                            String takuka = jsonArray.getJSONObject(0).getString("residential_address_taluka") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_taluka");
                            String district = jsonArray.getJSONObject(0).getString("residential_address_district") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_district");
                            String postoffice = jsonArray.getJSONObject(0).getString("permanent_address_postOffice") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_postOffice");

                            String localHouseNo = jsonArray.getJSONObject(0).getString("residential_address_houseNo") == "null" ? "" : "House No.," + jsonArray.getJSONObject(0).getString("residential_address_houseNo");
                            String localArea = jsonArray.getJSONObject(0).getString("residential_address_area") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_area");
                            String localPostOffice = jsonArray.getJSONObject(0).getString("residential_address_postOffice") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_postOffice");
                            String localTaluka = jsonArray.getJSONObject(0).getString("residential_address_taluka") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_taluka");
                            String localDist = jsonArray.getJSONObject(0).getString("residential_address_district") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_district");
                            String localState = jsonArray.getJSONObject(0).getString("residential_address_state") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_state");
                            String localPincode = jsonArray.getJSONObject(0).getString("residential_address_pincode") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_pincode");


                            String age = String.valueOf(jsonArray.getJSONObject(0).getInt("age")) == "null" ? "" : String.valueOf(jsonArray.getJSONObject(0).getInt("age"));


                            String permHouseNo = jsonArray.getJSONObject(0).getString("permanent_address_houseNo") == "null" ? "" : "House No.," + jsonArray.getJSONObject(0).getString("permanent_address_houseNo");
                            String permArea = jsonArray.getJSONObject(0).getString("permanent_address_area") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_area");
                            String permPostOffice = jsonArray.getJSONObject(0).getString("permanent_address_postOffice") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_postOffice");
                            String permTaluka = jsonArray.getJSONObject(0).getString("permanent_address_taluka") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_taluka");
                            String permDist = jsonArray.getJSONObject(0).getString("permanent_address_district") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_district");
                            String permState = jsonArray.getJSONObject(0).getString("permanent_address_state") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_state");
                            String permPincode = jsonArray.getJSONObject(0).getString("permanent_address_pincode") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_pincode");





                            String gender = jsonArray.getJSONObject(0).getString("gender");

                            relationGenderId = gender;

                            labourage = age;


                            // new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());

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

                            txt_beneficiary_name.setText(fName + " " + mName + " " + lName+ " / " + age);
                            txt_beneficiary_name.setVisibility(View.VISIBLE);
                            //    txt_beneficiary_age.setText(age + "/" + gender);
                            //  txt_beneficiary_age.setVisibility(View.VISIBLE);
                            txt_beneficiary_gender_change.setVisibility(View.VISIBLE);
                            txt_beneficiary_gender_change.setText(gender);

                            txt_beneficiary_gender.setText(gender);
                            // txt_beneficiary_gender.setVisibility(View.VISIBLE);

                            if (gender.equalsIgnoreCase("Female")){
                                txt_beneficiary_Mname.setText("");
                            }else {
                                txt_beneficiary_Mname.setText(fName);
                            }

                            txt_beneficiary_Mname.setEnabled(true);
                            //  txt_beneficiary_Lname.setVisibility(View.GONE);
                            txt_beneficiary_Lname.setText(lName);
                            txt_beneficiary_Lname.setEnabled(false);



                            edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() +" "+ txt_beneficiary_Mname.getText().toString().trim() +" "+ txt_beneficiary_Lname.getText().toString().trim());


                            btn_register.setEnabled(true);
                            edt_workerregno.setEnabled(false);



                            edt_district.setText(district);
                            edt_taluka.setVisibility(View.VISIBLE);
                            edt_taluka.setText(takuka);
                            String permAddress = permHouseNo + permArea + "," + permPostOffice + "," + permTaluka + "," + permDist + "," + permState + "," + permPincode;
                            edt_address.setText(permAddress);
                            String localAddress = localHouseNo + localArea + "," + localPostOffice + "," + localTaluka + "," + localDist + "," + localState + "," + localPincode;
                            edt_local_address.setText(localAddress);
                            edt_current_address.setText(localAddress);
                            edt_moblieno.setText(mobile);
                            edt_pincode.setText(localPincode);
                            edt_landMark.setText(permArea);
                            edt_postOffice.setText(postoffice);


                            if (Utilities.isNetworkAvailable(context)) {
                                new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());
                            } else {
                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                            }

                        } else {
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


                            String renewalDate = jsonArray.getJSONObject(0).getString("next_renewal_date") == "null" ? "" : jsonArray.getJSONObject(0).getString("next_renewal_date");
                            String takuka = jsonArray.getJSONObject(0).getString("residential_address_taluka") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_taluka");
                            String district = jsonArray.getJSONObject(0).getString("residential_address_district") == "null" ? "" : jsonArray.getJSONObject(0).getString("residential_address_district");
                            String postoffice = jsonArray.getJSONObject(0).getString("permanent_address_postOffice") == "null" ? "" : jsonArray.getJSONObject(0).getString("permanent_address_postOffice");

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
                            edt_renewalDate.setVisibility(View.VISIBLE);
                            edt_age.setText(age);
                            edt_renewalDate.setText(renewalDate);
                            edt_taluka.setText(takuka);
                            edt_landMark.setText(permArea);
                            btn_VerifyOtpContact.setVisibility(View.VISIBLE);
                            //  rbll.setVisibility(View.VISIBLE);
                            mainCbLL.setVisibility(View.VISIBLE);
                            // btn_VerifyOtp.setVisibility(View.VISIBLE);


                            txt_beneficiary_Fname.setText(fName);
                            txt_beneficiary_Fname.setEnabled(false);
                            txt_beneficiary_Mname.setText(mName);
                            txt_beneficiary_Mname.setEnabled(false);
                            // txt_beneficiary_Lname.setVisibility(View.GONE);
                            txt_beneficiary_Lname.setText(lName);
                            txt_beneficiary_Lname.setEnabled(false);


//                            edt_alternetMoblieno.setText(mobile);
                            edt_postOffice.setText(postoffice);
                            edt_postOffice.setVisibility(View.VISIBLE);
                            edt_current_address.setVisibility(View.VISIBLE);
                            //  edt_alternetMoblieno.setVisibility(View.VISIBLE);
                            edt_landMark.setVisibility(View.VISIBLE);
                            btn_VerifyOtp.setEnabled(true);
                            edt_alternetMoblieno.setEnabled(true);
                            edt_local_address.setVisibility(View.VISIBLE);
                            edt_workerregno.setEnabled(false);



                            edt_district.setText(district);
                            edt_district.setVisibility(View.VISIBLE);

                            if (district.equalsIgnoreCase("")){
                                edt_district.setEnabled(true);
                            }else {
                                edt_district.setEnabled(false);
                            }

                            edt_taluka.setVisibility(View.VISIBLE);

                            if (takuka.equalsIgnoreCase("")){
                                edt_taluka.setEnabled(true);
                            }else {
                                edt_taluka.setEnabled(false);
                            }


                            if (localPincode.equalsIgnoreCase("")){
                                edt_pincode.setEnabled(true);
                            }else {
                                edt_pincode.setEnabled(false);
                            }


                            String permAddress = permHouseNo + permArea + "," + permPostOffice + "," + permTaluka + "," + permDist + "," + permState + "," + permPincode;
                            edt_address.setText(permAddress);
                            String localAddress = localHouseNo + localArea + "," + localPostOffice + "," + localTaluka + "," + localDist + "," + localState + "," + localPincode;
                            edt_local_address.setText(localAddress);
                            edt_current_address.setText(localAddress);
                            edt_pincode.setText(localPincode);

                            if (jsonArray.getJSONObject(0).getInt("age") != 0) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.YEAR, -jsonArray.getJSONObject(0).getInt("age"));
                                edt_dob.setText(new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()));
                            }
                            String gender = jsonArray.getJSONObject(0).getString("gender");

                            if (gender != null) {
                                if (gender.equalsIgnoreCase("Male")) {
                                    rb_male.setChecked(true);
                                    rg_gender.setEnabled(true);
                                    //  rb_female.setChecked(true);
//                                    rb_mr.setChecked(true);
//                                    rb_mrs.setChecked(false);
//                                    rb_ms.setChecked(false);

                                } else {
                                    // rb_male.setChecked(true);
                                    rb_female.setChecked(true);
                                    rg_gender.setEnabled(true);

//                                    rb_mrs.setChecked(true);
//                                    rb_mr.setChecked(false);
//                                    rb_ms.setChecked(false);
                                }
                            }

                            new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
                        }
                        if (isDependent.equalsIgnoreCase("0")) {
                            disableViewAfterFill();
                        }
                    } else {
                        clearPatientDetails();
                        disableViewAfterFill();
                        Utilities.showAlertDialogRequired(context, "Warning", "Beneficiary not registered at MCWWB", false, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                relationId = "20";
                                edt_relation.setText("Self");
                                btn_register.setEnabled(true);
                            }
                        });
                    }
//                    if (list.size() > 0) {
//                    } else {
//                        clearPatientDetails();
//                        isReregistration = false;
//                        new GetWorkerInfro().execute("MH" + edt_workerregno.getText().toString().trim());
//                    }
                } else {
//                    enableViewAfterFill();
                    clearPatientDetails();
                    disableViewAfterFill();
                    relationId = "20";
                    edt_relation.setText("Self");
                    Utilities.showAlertDialogRequired(context, "Warning", "Beneficiary not registered at MCWWB", false, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            relationId = "20";
                            edt_relation.setText("Self");
                            btn_register.setEnabled(true);
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
                clearPatientDetails();
//                enableViewAfterFill();
                disableViewAfterFill();
                relationId = "20";
                edt_relation.setText("Self");
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    void disableViewAfterFill() {
        edt_fname.setEnabled(false);
        edt_moblieno.setEnabled(false);
        edt_education.setEnabled(false);
        edt_aadhaarno.setEnabled(false);
        edt_age.setEnabled(false);
        edt_address.setEnabled(false);
        edt_local_address.setEnabled(true);
        //  edt_pincode.setEnabled(false);
        edt_dob.setEnabled(false);
//        rb_male.setEnabled(false);
//        rb_female.setEnabled(false);
//        rg_gender.setEnabled(false);
        edt_renewalDate.setEnabled(false);
        //  edt_taluka.setEnabled(false);
        // edt_district.setEnabled(false);
        edt_postOffice.setEnabled(false);

//
//        edt_fname.setLongClickable(false);
//        edt_moblieno.setLongClickable(false);
//        edt_aadhaarno.setLongClickable(false);
//        edt_age.setLongClickable(false);
//        edt_address.setLongClickable(false);
//        edt_local_address.setLongClickable(false);
//        edt_pincode.setLongClickable(false);
//        edt_dob.setLongClickable(false);
    }

    void disableIfFilled() {
//        if (edt_fname.getText().toString().isEmpty()) {
//            edt_fname.setEnabled(true);
//        } else {
//            edt_fname.setEnabled(false);
//        }

        if (edt_moblieno.getText().toString().isEmpty()) {
            edt_moblieno.setEnabled(true);
        } else {
            edt_moblieno.setEnabled(false);
        }
//        if (edt_aadhaarno.getText().toString().isEmpty()) {
//            edt_aadhaarno.setEnabled(true);
//        } else {
//            edt_aadhaarno.setEnabled(false);
//        }

//        if (!edt_age.getText().toString().isEmpty()) {
//            edt_age.setEnabled(false);
//        } else {
//            edt_age.setEnabled(true);
//        }

//        if (edt_address.getText().toString().isEmpty()) {
//            edt_address.setEnabled(true);
//        } else {
//            edt_address.setEnabled(false);
//        }
//
//        if (edt_local_address.getText().toString().isEmpty()) {
//            edt_local_address.setEnabled(true);
//        } else {
//            edt_local_address.setEnabled(false);
//        }
//
//        if (edt_pincode.getText().toString().isEmpty()) {
//            edt_pincode.setEnabled(true);
//        } else {
//            edt_pincode.setEnabled(false);
//        }
//
//        if (edt_dob.getText().toString().isEmpty()) {
//            edt_dob.setEnabled(true);
//        } else {
//            edt_dob.setEnabled(false);
//        }
//
//        if (rb_male.isChecked()) {
//            rb_male.setEnabled(true);
//        } else {
//            rb_male.setEnabled(false);
//        }
//
//        if (rb_female.isChecked()) {
//            rb_female.setEnabled(true);
//        } else {
//            rb_female.setEnabled(false);
//        }
//        if (rb_mr.isChecked()) {
//            rb_mr.setEnabled(true);
//        } else {
//            rb_mr.setEnabled(false);
//        }

//        if (rb_mrs.isChecked()) {
//            rb_mrs.setEnabled(true);
//            rb_ms.setEnabled(true);
//        } else {
//            rb_mrs.setEnabled(false);
//            rb_ms.setEnabled(false);
//        }
    }

    void enableViewAfterFill() {
        edt_fname.setEnabled(true);
//        edt_fname.setLongClickable(true);
        edt_moblieno.setEnabled(true);
//        edt_moblieno.setLongClickable(true);
        edt_aadhaarno.setEnabled(true);
        edt_education.setEnabled(true);

//        edt_aadhaarno.setLongClickable(true);
        edt_age.setEnabled(true);
//        edt_age.setLongClickable(true);
        edt_address.setEnabled(true);
//        edt_address.setLongClickable(true);
        edt_local_address.setEnabled(true);
        edt_current_address.setEnabled(true);
        edt_alternetMoblieno.setEnabled(true);
        edt_landMark.setEnabled(true);
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


    private class GetDependentListFromRegdId extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetDependentListFromRegdId, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    DependentPojo pojoDetails = new Gson().fromJson(result, DependentPojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<DependentPojo.Output> dependentlist = pojoDetails.getOutput();
                        if (dependentlist.size() > 0) {
                            CustomPatientDialog(dependentlist);
                        } else {
                            Utilities.showAlertDialog(context, "Fail", message, false);
                        }
                    } else {
//                        Utilities.showAlertDialog(context, "Warning", "No dependent List found still you can register this Labour as dependant.", false);
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

    private class CheckRegidExist extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetBenificiaryRegisterOrNot, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String status = "", message = "";
            Log.d("TAG", "CheckRegidExist: " + result);
            try {
                if (!result.equals("")) {
                    JSONObject obj1 = new JSONObject(result);
                    status = obj1.getString("status");
                    message = obj1.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonarr = obj1.getJSONArray("output");
                        JSONObject jsonObject = (JSONObject) jsonarr.get(0);
                        count = jsonObject.getString("REGDEPCOUNT");
                        DependREGID = jsonObject.getString("RegdId");
                        int withBenef = jsonObject.getInt("regwithbenifaciary");
                        String name = jsonObject.getString("EnglishName");
                        String OriginalBeneficiaryNo = edt_workerregno.getText().toString().trim();
                        if (withBenef != 0) {

                            if (Integer.parseInt(count) >= 3) {
                                Utilities.showAlertDialogRequired(context, "Max Dependant Count Reached", "You can't add dependant more than 3", false, "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        clearPatientDetails();
                                    }
                                });
                            } else {
                                btn_register.setEnabled(true);

                                regdID = edt_workerregno.getText().toString().trim() + "" + count;
                                flag = "1";
                                tvRegdNo.setText(regdID);

//                        edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});

                                edt_workerregno.setText(regdID);
//                                txt_beneficiary_name.setVisibility(View.VISIBLE);
//                                txt_beneficiary_name.setText(name);
                                edt_workerregno.setEnabled(false);


                                if (Utilities.isNetworkAvailable(context)) {
                                    new GetDependentListFromRegdId().execute(OriginalBeneficiaryNo);
                                } else {
                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                }
                            }
                        } else {
                            regdID = edt_workerregno.getText().toString().trim() + "" + count;
                            flag = "1";

                            tvRegdNo.setText(regdID);

//                        edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});

                            edt_workerregno.setText(regdID);
//                            txt_beneficiary_name.setVisibility(View.VISIBLE);
//                            txt_beneficiary_name.setText(name);

//                            new GetWorkerInfo().execute("MH" + OriginalBeneficiaryNo);

//                            Utilities.showAlertDialog(context, "Warning", "Beneficiary not registered", false);
//                            clearPatientDetails();

//                            if (Utilities.isNetworkAvailable(context)) {
//                                new GetDependentListFromRegdId().execute(OriginalBeneficiaryNo);
//                            } else {
//                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                            }
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                        edt_workerregno.setText("");
                        if (txt_beneficiary_name.getVisibility() == View.VISIBLE)
                            txt_beneficiary_name.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetRelationList extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        public GetRelationList() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context, android.app.AlertDialog.THEME_HOLO_LIGHT);
            pd.setCancelable(false);
            pd.setMessage("Please wait..");
            pd.show();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    Gson gson = new Gson();
                    RelationPojo getRelationListPojo = gson.fromJson(result, RelationPojo.class);

                    String status = getRelationListPojo.getStatus();
                    String message = getRelationListPojo.getMessage();

                    if (status.equalsIgnoreCase("Success")) {
                        ArrayList<RelationPojo.Output> relationList = getRelationListPojo.getOutput();
                        if (relationList == null || relationList.size() == 0) {
                            Utilities.showAlertDialog(context,
                                    "Please try again", "Server not responding.", false);
                            return;
                        }
                        openNationalityListDialog(relationList);
                    } else {
                        Utilities.showAlertDialog(context,
                                status, message, false);

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
            String serverResponse = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(ApplicationConstants.webservice + "GetRelation")
                        .build();

                Response response = client.newCall(request).execute();
                serverResponse = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return serverResponse;
        }
    }

    private class GetIdentityList extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        public GetIdentityList() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context, android.app.AlertDialog.THEME_HOLO_LIGHT);
            pd.setCancelable(false);
            pd.setMessage("Please wait..");
            pd.show();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    Gson gson = new Gson();
                    DocumentTypeModel documentTypeModel = gson.fromJson(result, DocumentTypeModel.class);

                    String status = documentTypeModel.getStatus();
                    String message = documentTypeModel.getMessage();

                    if (status.equalsIgnoreCase("Success")) {
                        ArrayList<DocumentTypeModel.Output> documentTypeModelOutput = documentTypeModel.getOutput();
                        if (documentTypeModelOutput == null || documentTypeModelOutput.size() == 0) {
                            Utilities.showAlertDialog(context,
                                    "Please try again", "Server not responding.", false);
                            return;
                        }
                        openIndentityListDialog(documentTypeModelOutput);
                    } else {
                        Utilities.showAlertDialog(context,
                                status, message, false);

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
            String serverResponse = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(ApplicationConstants.webservice + ApplicationConstants.GetDocumenttype)
                        .build();

                Response response = client.newCall(request).execute();
                serverResponse = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return serverResponse;
        }
    }

    private void openNationalityListDialog(final ArrayList<RelationPojo.Output> relationList) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_dialog);
        //dialog.setTitle("Select District");
        ListView listView = (ListView) dialog.findViewById(R.id.dialog_listview_id);
        TextView titleTv = (TextView) dialog.findViewById(R.id.dialog_title_tv);
        titleTv.setText("Select Relation");
        final ArrayList<String> nationalityStringList = new ArrayList<>();
        for (RelationPojo.Output getNationalityListOutputPojo : relationList) {
            String relName = getNationalityListOutputPojo.getRelName();
            if (!relName.equalsIgnoreCase("Self")) {
                nationalityStringList.add(relName);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, nationalityStringList);
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                edt_relation.setText(nationalityStringList.get(position));
                relationId = relationList.get(position).getRelId();

                new GetRelation().execute();

                switch (relationId) {
                    case "1":
                    case "5":
                    case "7":
                    case "9":
                    case "17":
                        genderId = "M";
                        rb_male.setChecked(true);
                        rb_female.setChecked(false);
                        rb_female.setEnabled(false); //Enabled On 05/02/2021 suggested by sumit
                        rb_male.setEnabled(true);
                        edt_dob.setText("");
                        edt_age.setText("");
                        break;

                    case "2":
                    case "6":
                    case "8":
                    case "10":
                    case "18":
                        genderId = "F";
                        rb_male.setChecked(false);
                        rb_female.setChecked(true);
                        rb_male.setEnabled(false); //Enabled On 05/02/2021 suggested by sumit
                        rb_female.setEnabled(true);
                        edt_dob.setText("");
                        edt_age.setText("");

                        break;
                    default:
                        rb_female.setChecked(false);
                        rb_male.setChecked(false);
                        rb_male.setEnabled(true);
                        rb_female.setEnabled(true);
                        edt_dob.setText("");
                        edt_age.setText("");
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    private void openIndentityListDialog(final ArrayList<DocumentTypeModel.Output> documentList) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_dialog);
        //dialog.setTitle("Select District");
        ListView listView = (ListView) dialog.findViewById(R.id.dialog_listview_id);
        TextView titleTv = (TextView) dialog.findViewById(R.id.dialog_title_tv);
        titleTv.setText("Select Document Type");
        final ArrayList<String> nationalityStringList = new ArrayList<>();
        for (DocumentTypeModel.Output documentOutput : documentList) {
            String doc = documentOutput.getDocumentName();
            nationalityStringList.add(doc);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, nationalityStringList);
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                edtIdentity.setText(nationalityStringList.get(position));
                identityId = documentList.get(position).getDocID().toString();
                String idName = documentList.get(position).getDocumentName();
                edt_aadhaarno.setHint(idName);
                edt_aadhaarno.setText("");
                if (idName.equalsIgnoreCase("Adhar Card")) {
                    edt_aadhaarno.setInputType(InputType.TYPE_CLASS_NUMBER);
                    edt_aadhaarno.setMaxCharacters(12);
                    edt_aadhaarno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

                } else if (idName.equalsIgnoreCase("Pan Card")) {
                    edt_aadhaarno.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    edt_aadhaarno.setMaxCharacters(10);
                    edt_aadhaarno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                } else if (idName.equalsIgnoreCase("Driving Licence")) {
                    edt_aadhaarno.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    edt_aadhaarno.setMaxCharacters(16);
                    edt_aadhaarno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

                } else if (idName.equalsIgnoreCase("Passport")) {
                    edt_aadhaarno.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    edt_aadhaarno.setMaxCharacters(16);
                    edt_aadhaarno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

                }
                dialog.dismiss();
            }
        });
    }


    public class VerifyOtp extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        // String otp;

        public VerifyOtp(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
            //  this.otp = otp;
        }

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
            param.add(new ParamsPojo("OTP", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.VerifyRegistrationOTP, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
//                    type = resourcesListModel.getStatus();
//                    message = resourcesListModel.getMessage();
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));
                        //  LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdId()));


                        alertDialog.dismiss();

                        btn_register.setVisibility(View.VISIBLE);

                        {
                            // Utilities.showAlertDialog(context, "Alert", message, true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.icon_success);
                            builder.setTitle("Success");
                            builder.setCancelable(false);
                            builder.setMessage("OTP Verification successfully");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // finish();
                                    btn_VerifyOtp.setText("Verified");
                                    edt_alternetMoblieno.setEnabled(false);
                                    btn_VerifyOtp.setEnabled(false);
                                    edt_moblieno.setEnabled(false);
                                    btn_VerifyOtpContact.setEnabled(false);
                                    btn_VerifyOtpContact.setText("Verified");
                                    cbIsNumberBgs.setEnabled(false);

                                }

                            });
                            builder.show();
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class VerifyOtpContact extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        // String otp;

        public VerifyOtpContact(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
            //  this.otp = otp;
        }

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
            param.add(new ParamsPojo("OTP", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.VerifyRegistrationOTP, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
//                    type = resourcesListModel.getStatus();
//                    message = resourcesListModel.getMessage();
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));
                        //  LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdId()));


                        alertDialog.dismiss();

                        btn_register.setVisibility(View.VISIBLE);

                        {
                            // Utilities.showAlertDialog(context, "Alert", message, true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.icon_success);
                            builder.setTitle("Success");
                            builder.setCancelable(false);
                            builder.setMessage("OTP Verification successfully");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // finish();
                                    btn_VerifyOtp.setText("Verified");
                                    edt_alternetMoblieno.setEnabled(false);
                                    btn_VerifyOtp.setEnabled(false);
                                    edt_moblieno.setEnabled(false);
                                    btn_VerifyOtpContact.setEnabled(false);
                                    btn_VerifyOtpContact.setText("Verified");
                                    cbIsNumberBgs.setEnabled(false);
                                    mainCbLL.setVisibility(View.GONE);
                                }

                            });
                            builder.show();
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    private void CustomPatientDialog(List<DependentPojo.Output> dependentlist) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_patientlist_dialog);
        TextView dialog_title_tv = (TextView) dialog.findViewById(R.id.dialog_title_tv);
        dependent_list_recycler_view = (RecyclerView) dialog.findViewById(R.id.hospital_list_recycler_view);
        dialog_title_tv.setText("Select Dependent");


        setRecyclerViewPatient(dependentlist);
        dialog.show();


        dependent_list_recycler_view.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                DependentPojo.Output type = new DependentPojo.Output();
                                type = dependentlist.get(position);
                                setDependentData(type);
                                dialog.dismiss();
                            }
                        }));

    }

    private void setDependentData(DependentPojo.Output type) {

        if (type.getDependentName() != null)
            edt_fname.setText(type.getDependentName());
        else
            edt_fname.setText("");
        if (type.getEducation() != null)
            edt_education.setText(type.getEducation());
        else
            edt_education.setText("");
        if (type.getReleation() != null)
            edt_relation.setText(type.getReleation());
        else
            edt_relation.setText("");
        if (type.getAge() != null)
            edt_age.setText(type.getAge());
        else
            edt_age.setText("");
        if (type.getSex() != null) {
            if (type.getSex().equalsIgnoreCase("Male")) {
                rb_male.setChecked(true);
            } else if (type.getSex().equalsIgnoreCase("Female")) {
                rb_female.setChecked(true);
            }
        } else {
            rb_male.setChecked(false);
            rb_female.setChecked(false);
        }

        if (type.getRelId() != null) {
            relationId = type.getRelId();
        }
    }

    private void setRecyclerViewPatient(List<DependentPojo.Output> patinetList) {
        dependent_list_recycler_view.setVisibility(View.VISIBLE);
        dependent_list_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        dependent_list_recycler_view.setLayoutManager(mLayoutManager);
        mAdapter = new DependentSearchAdapter(context, patinetList);
        dependent_list_recycler_view.setAdapter(mAdapter);

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

        if (isDependent.equalsIgnoreCase("0")) {
            if (edt_workerregno.getText().toString().trim().isEmpty() || edt_workerregno.getText().toString().length() != 12) {
                edt_workerregno.setError("Please enter valid Labour no.");
                return;
            }
        } else {
            if (flag.equals("0")) {
                Utilities.showAlertDialog(context, "Alert", "Please search Labour No. first and then fill remaining details of dependant", false);
                return;
            } else if (edt_workerregno.getText().toString().trim().isEmpty() || edt_workerregno.getText().toString().length() != 12) {
                edt_workerregno.setError("Please enter valid Labour no.");
                return;
            }

            if (edt_relation.getText().toString().trim().isEmpty()) {
                edt_relation.setError("Please select relation!");
                return;
            }

        }

//        if (rb_mr.isChecked()) {
//            title = "Mr.";
//        } else if (rb_mrs.isChecked()) {
//            title = "Mrs.";
//        } else if (rb_ms.isChecked()) {
//            title = "Ms.";
//        } else {
//            Utilities.showToastMessage("Please select title", context, false);
//            return;
//        }

        if (edt_fname.getText().toString().trim().isEmpty()) {
            edt_fname.setError("Please enter first name");
            return;
        }

        if (txt_beneficiary_Fname.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please enter first name", context, false);
            return;
        }
//        if (txt_beneficiary_Mname.getText().toString().trim().isEmpty()) {
//            Utilities.showToastMessage("Please enter middle name", context, false);
//            return;
//        }
        if (txt_beneficiary_Lname.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please enter last name", context, false);
            return;
        }

        if (!Utilities.isMobileNo(edt_moblieno.getText().toString().trim())) {
            edt_moblieno.setError("Please enter valid mobile");
            return;
        }

        if (edt_alternetMoblieno.getVisibility() == View.VISIBLE) {
            if (edt_alternetMoblieno.getText().toString().trim().isEmpty()) {
                Utilities.showToastMessage("Please enter alternate mobile number", context, false);
                return;
            }
        }

        if (edt_address.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please enter permanent address", context, false);
            return;
        }

//        if (edt_local_address.getText().toString().trim().isEmpty()) {
//            Utilities.showToastMessage("Please enter Local Address", context, false);
//            return;
//        }

//        if (edt_address.getText().toString().length() <= 25){
//            Utilities.showToastMessage("Permanent address should be grater than equal to 25 character length ", context, false);
//            return;
//        }



//        if (!Utilities.isaadharNumberValidate(edt_aadhaarno.getText().toString().trim())) {
//            edt_aadhaarno.setError("Please enter valid Aadhar Card No.");
//            return;
//        }

        if (isDependent.equalsIgnoreCase("0")) {
            if (edt_aadhaarno.getText().toString().trim().length() != 12) {
                edt_aadhaarno.setError("Please enter valid Aadhar Card No.");
                return;
            } else {
                edt_aadhaarno.setError(null);
            }
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

//        if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
//            Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 18 years or more than 60 years", false);
//            return;
//        }

        if (isDependent.equalsIgnoreCase("1")) {
            if (identityId.equalsIgnoreCase("0")) {
                edtIdentity.setError("Please Select Identity Card");
                return;
            }
        }

        if (isDependent.equalsIgnoreCase("0")) {
            if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
                Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 60 years", false);
                return;
            }
        } else {
            if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")
                    || relationId.equalsIgnoreCase("5") || relationId.equalsIgnoreCase("6")) {
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 10 || Integer.parseInt(edt_age.getText().toString().trim()) > 58) {
                    Utilities.showAlertDialog(context, "Alert", "Dependent age should not be less than 10 years or more than 58 years", false);
                    return;
                }
            } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
                    || relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
                    relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 75) {
                    Utilities.showAlertDialog(context, "Alert", "Dependent age should not be less than 18 years or more than 60 years", false);
                    return;
                }
            } else {
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 75) {
                    Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 60 years", false);
                    return;
                }
            }
        }

//        if (isDependent.equals("1")) {
//            if (edt_relation.getText().toString().equals("")) {
////                Utilities.showAlertDialog(context, "Alert", "Please select relation", false);
//                edt_relation.setError("Please select relation!");
//                return;
//            }
//        }

//        if (isDependent.equals("1")) {
//            if (flag.equals("0")) {
//                Utilities.showAlertDialog(context, "Alert", "Please search KBOCWWB Labour No. first and then fill remaining details of dependant", false);
//                return;
//            }
//        }
//
//        if (isDependent.equals("1") && (!regdID.equalsIgnoreCase(edt_workerregno.getText().toString().trim()))) {
//            Utilities.showAlertDialog(context, "Alert", "invalid dependant", false);
//            return;
//        }

        if (rb_male.isChecked()) {
            genderId = "M";
        } else if (rb_female.isChecked()) {
            genderId = "F";
        } else {
            Utilities.showToastMessage("Please select gender", context, false);
            return;
        }

        if (edt_local_address.getVisibility() == View.VISIBLE) {
            if (edt_local_address.getText().toString().trim().isEmpty()) {
                Utilities.showToastMessage("Please enter local address", context, false);
                return;
            }

            if (edt_local_address.getText().toString().length() <= 10){
                Utilities.showToastMessage("Local address should be grater than equal to 10 character length ", context, false);
                return;
            }


        }



        if (edt_current_address.getVisibility() == View.VISIBLE) {
            if (edt_current_address.getText().toString().trim().isEmpty()) {
                edt_current_address.setError("Please enter Current Address");
                return;
            }

            if (edt_current_address.getText().toString().length() <= 10){
                Utilities.showToastMessage("Current address should be grater than equal to 10 character length ", context, false);
                return;
            }
        }

        if (edt_landMark.getVisibility() == View.VISIBLE) {
            if (edt_landMark.getText().toString().trim().isEmpty()) {
                edt_landMark.setError("Please enter landmark");
                return;
            }

        }

//        if (edt_taluka.getVisibility() == View.VISIBLE) {
//            if (edt_taluka.getText().toString().trim().isEmpty()) {
//                edt_taluka.setError("Please enter taluka");
//                return;
//            }
//
//        }

        if (edt_district.getVisibility() == View.VISIBLE) {
            if (edt_district.getText().toString().trim().isEmpty()) {
                edt_district.setError("Please enter district");
                return;
            }
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
//        if (!isHivConcernPhotoAvailable) {
//            if (hivletterPath.equals("")) {
//                Utilities.showToastMessage("Please click hiv concern letter photo", context, false);
//                return;
//            }
//        }

        if (isDependent.equalsIgnoreCase("0")) {
            if (!isHealthCardPhotoAvailable) {
                if (healthCardImagePath.equals("")) {
                    Utilities.showToastMessage("Please click health card", context, false);
                    return;
                }
            }
        } else {
            if (!isHealthCardPhotoAvailable) {
                if (healthCardImagePath.equals("")) {
                    Utilities.showToastMessage("Please click Indentity card", context, false);
                    return;
                }
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
            regdID = edt_workerregno.getText().toString().trim() + "" + count;


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert!");
            builder.setMessage("Please confirm the beneficiary's details before submitting");
            builder.setCancelable(false);
            builder.setPositiveButton("Proceed", (dialog, which) -> {
                if (Utilities.isNetworkAvailable(context))
                    new UploadPatientDetails().execute(
                            siteId,
                            campId,
                            regdID,
                            title,
                            txt_beneficiary_Fname.getText().toString().trim() +" "+ txt_beneficiary_Mname.getText().toString().trim() +" "+ txt_beneficiary_Lname.getText().toString().trim(),
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
                            hivletterPath,
                            isDependent,
                            edt_education.getText().toString(),
                            relationId,
                            DependREGID,
                            identityId,
                            txt_beneficiary_name.getText().toString().trim(),
                            edt_renewalDate.getText().toString().trim(),
                            edt_postOffice.getText().toString().trim(),
                            edt_taluka.getText().toString().trim(),
                            edt_district.getText().toString().trim(),
                            edt_current_address.getText().toString().trim(),
                            edt_landMark.getText().toString().trim(),
                            edt_alternetMoblieno.getText().toString().trim(),
                            "0",
                            cbIsNumberBgs.isChecked() ? "0" : "1",
                            rb_self.isChecked() ? "1" : rb_spouse.isChecked() ? "2" : "3"

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
            Log.d("Reg Params", Arrays.toString(params));
            try {
                MultipartUtility multipart;
//                if(session.isHllUser()) {
                multipart = new MultipartUtility(ApplicationConstants.DtoDBeneficiaryRe_RegistrationV6, "UTF-8");
//                }else{ 184346468686
//                    multipart = new MultipartUtility(ApplicationConstants.BeneficiaryRe_RegistrationOTP, "UTF-8");
//
//                }
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
                if (!params[19].equals(""))
                    multipart.addFilePart("file4", new File(params[19]));
                multipart.addFormField("IsDependent", params[20]);
                multipart.addFormField("Education", params[21]);
                multipart.addFormField("ReleationID", params[22]);
                multipart.addFormField("DependREGID", params[23]);
                multipart.addFormField("IndentityId", params[24]);
                multipart.addFormField("CW_WorkerName", params[25]);
                multipart.addFormField("next_renewal_date", params[26]);
                multipart.addFormField("residential_address_postOffice", params[27]);
                multipart.addFormField("residential_address_taluka", params[28]);
                multipart.addFormField("residential_address_district", params[29]);
                multipart.addFormField("CurrentAddress", params[30]);
                multipart.addFormField("LandMark", params[31]);
                multipart.addFormField("AlternateMobNo", params[32]);
                multipart.addFormField("IsMobNoVerified", params[33]);
                multipart.addFormField("IsSelfMobNo", params[34]);
                multipart.addFormField("MobNoOf", params[35]);
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

            Log.d("Reg Res ", result);
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    edt_workerregno.setEnabled(true);

                    if (status.equalsIgnoreCase("Success")) {
                        registeredPatientregdid = obj1.getString("Regdid");

                        //      if (isRTPCR.equals("0")) {
                        openFingerPrintActivity();
//                        } else {
//                            String billString = CreateJsonString();
//                            new UploadPatientDetailsToLab().execute(billString);
//                        }
                    } else {
                        if (message.toLowerCase().contains("regd")) {
                            Utilities.showAlertDialog(context, "Fail", "Labour No Already Exists", false);
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

    private String CreateJsonString() {
        LiveHealthRequest liveHealthRequest = new LiveHealthRequest();
        liveHealthRequest.setMobile(edt_moblieno.getText().toString().trim());
        liveHealthRequest.setEmail("");
        liveHealthRequest.setDesignation(title);
        liveHealthRequest.setFullName(edt_fname.getText().toString().trim());
        liveHealthRequest.setAge(Integer.parseInt(edt_age.getText().toString().trim()));
        if (rb_male.isChecked())
            liveHealthRequest.setGender("Male");
        else if (rb_female.isChecked())
            liveHealthRequest.setGender("Female");
        liveHealthRequest.setArea(edt_local_address.getText().toString().trim());
        liveHealthRequest.setCity(edt_local_address.getText().toString().trim());
        liveHealthRequest.setPatientType("");
        liveHealthRequest.setLabPatientId("");
        liveHealthRequest.setPincode(edt_pincode.getText().toString());
        liveHealthRequest.setPatientId("");
        liveHealthRequest.setDob(changeDateFormat("yyyy/MM/dd", "yyyy-MM-dd", edt_dob.getText().toString().trim()));
        liveHealthRequest.setPassportNo("");
        liveHealthRequest.setPanNumber("");
        liveHealthRequest.setAadharNumber(edt_aadhaarno.getText().toString().trim());
        liveHealthRequest.setInsuranceNo("");
        liveHealthRequest.setNationality("Indian");
        liveHealthRequest.setEthnicity("");
        liveHealthRequest.setNationalIdentityNumber("");
        liveHealthRequest.setWorkerCode(edt_workerregno.getText().toString());
        liveHealthRequest.setDoctorCode("");

        LiveHealthRequest.BillDetails billDetails = new LiveHealthRequest.BillDetails();
        billDetails.setEmergencyFlag(0);
        billDetails.setTotalAmount("0");
        billDetails.setAdvance("0");
        billDetails.setBillDate("");
        billDetails.setPaymentType("");
        billDetails.setReferralName("");
        billDetails.setOtherReferral("");
        billDetails.setSampleId("");
        billDetails.setOrderNumber("");
        billDetails.setReferralIdLH(0);
        billDetails.setOrganisationName("");
        billDetails.setAdditionalAmount("");
        billDetails.setOrganizationIdLH(Integer.valueOf(ApplicationConstants.organisation_id));
        billDetails.setComments("");

        liveHealthRequest.setBillDetails(billDetails);

        ArrayList<LiveHealthRequest.BillDetails.Payment> paymentArrayList = new ArrayList<LiveHealthRequest.BillDetails.Payment>();
        LiveHealthRequest.BillDetails.Payment payment = new LiveHealthRequest.BillDetails.Payment();
        payment.setIssueBank("");
        payment.setPaymentAmount("");
        payment.setPaymentType("");

        paymentArrayList.add(payment);

        billDetails.setPaymentList(paymentArrayList);

        ArrayList<LiveHealthRequest.BillDetails.Test> testArrayList = new ArrayList<LiveHealthRequest.BillDetails.Test>();
        LiveHealthRequest.BillDetails.Test test = new LiveHealthRequest.BillDetails.Test();
        test.setDictionaryId(0);
        test.setIntegrationCode("");
        test.setSampleId("");
        test.setTestCode("");
        test.setTestID(Integer.valueOf(ApplicationConstants.test_id));
        testArrayList.add(test);
        billDetails.setTestList(testArrayList);

        Gson gson1 = new Gson();
        return gson1.toJson(liveHealthRequest);
    }

    private class UploadPatientDetailsToLab extends AsyncTask<String, Void, String> {

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
                    ApplicationConstants.patientRegUrl + ApplicationConstants.token_Id, params[0]);
            return res.trim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                try {
                    JSONObject obj1 = new JSONObject(result);
                    String Message = obj1.getString("Message");
                    String billId = "";
                    if (Message.equalsIgnoreCase("Success")) {
                        try {
                            String patientId = obj1.getString("patientId");
                            if (obj1.has("billId")) {
                                billId = obj1.getString("billId");
                            }
                            JSONArray jsonArray = obj1.getJSONArray("reportDetails");
                            JSONObject json = jsonArray.getJSONObject(0);
                            String testID = json.getString("testID");
                            String testName = json.getString("testName");
                            if (billId != null && !billId.equals(""))
                                new UploadLabDetails().execute(registeredPatientregdid, patientId, billId, testName, testID, userId);

                        } catch (NullPointerException e) {
                            openFingerPrintActivity();
                            e.printStackTrace();
                        }

                    } else {
                        openFingerPrintActivity();
                    }
                } catch (JSONException e) {
                    openFingerPrintActivity();
                    e.printStackTrace();
                }
            } else {
                openFingerPrintActivity();
            }

        }
    }

    private void openFingerPrintActivity() {
        String msg;
        if (isReregistration)
            msg = "Patient re-registered successfully!";
        else if (isDependent.equals("0"))
            msg = "Patient registered successfully!";
        else
            msg = "Dependant registered succesfully !";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle("Success");
        builder.setIcon(R.drawable.icon_success);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                startActivity(new Intent(context, PatientFingerAndSignature_Activity.class)
                        .putExtra("campId", campId)
                        .putExtra("siteId", siteId)
                        .putExtra("registrationNo", regdID));
                clearPatientDetails();
                edt_workerregno.setText("");

            }
        });
        AlertDialog alertD = builder.create();
        alertD.show();
    }

    private class UploadLabDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", params[0]));
            param.add(new ParamsPojo("patientId", params[1]));
            param.add(new ParamsPojo("billId", params[2]));
            param.add(new ParamsPojo("testName", params[3]));
            param.add(new ParamsPojo("testID", params[4]));
            param.add(new ParamsPojo("CreatedBy", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.InsAPIRegistrationResponse, ApplicationConstants.webservice, param);
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
                        openFingerPrintActivity();
                    } else {
                        openFingerPrintActivity();
                    }
                } else {
                    openFingerPrintActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
                openFingerPrintActivity();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PATIENT_CAMERA_REQUEST) {
//                CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity_Old.this);
            }
            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
//                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity_Old.this);
            }
            if (requestCode == RENEWAL_CAMERA_REQUEST) {
//                CropImage.activity(renewalURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity_Old.this);
            }
            if (requestCode == HIV_CONCERN_CAMERA_REQUEST) {
//                CropImage.activity(hivConcernURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity_Old.this);
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


    public class GetOtp extends AsyncTask<String, Void, String> {
        //ProgressDialog pd;

        int type;

        public GetOtp(int type) {
            this.type = type;
        }

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
            Random r = new Random(System.currentTimeMillis());
            otpnumber = String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
            param.add(new ParamsPojo("OTP", otpnumber));
            param.add(new ParamsPojo("RegdId", params[2]));
            param.add(new ParamsPojo("CreatedBy", params[3]));
            param.add(new ParamsPojo("MsgID", params[4]));

            // res = WebServiceCall.APICall(ApplicationConstants.GetOTPforRegistration, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetOTPforRegistration_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
//                        ArrayList<ResourcesListModel.OutputBean> resourceList = resourcesListModel.getOutput();
//                        if (resourceList.size() > 0) {
                        if (type == 1) {
//                                // verifyOtp(mno, resourceList.get(0).getDesgId());
                            verifyOtp(edt_alternetMoblieno.getText().toString(), otpnumber);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Alert", "You Are Not Able To Use This Number Multiple Times.", false);

                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
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
            destinationFilename = patientPicsFolder + filename;
            //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
        } else if (imageType == 1) {
            isHealthCardPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
            destinationFilename = patientPicsFolder + filename;
            //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
        } else if (imageType == 2) {
            isRenewalSlipPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_RHC.png";
            destinationFilename = patientPicsFolder + filename;
            // destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
        } else if (imageType == 3) {
            isHivConcernPhotoAvailable = false;
            String filename = (int) (Math.random() * 99999 + 1) + "_HIV.png";
            destinationFilename = patientPicsFolder + filename;
            //destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
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
        } else if (imageType == 3) {
            hivConcernBm = BitmapFactory.decodeFile(destinationFilename);
            destinationFilename = compressImage(destinationFilename);
            hivConcernBm = Bitmap.createScaledBitmap(hivConcernBm, 150, 150, false);
            imv_hiv_concern.setImageBitmap(hivConcernBm);
            hivletterPath = destinationFilename;
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

    @Override
    protected void onResume() {
        super.onResume();
//        if (isDependent.equalsIgnoreCase("0"))
//            disableIfFilled();

        if (session.isHllUser()){
            new GetTeamId().execute();
        }
    }

    private void
    clearPatientDetails() {
        registeredPatientregdid = "";
        DependREGID = "0";
        flag = "0";
        isDependent = "0";
//        edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        rg_dep_no.setChecked(true);
        rg_dep_yes.setChecked(false);
        count = "";
        regdID = "";
        isReregistration = false;
        edt_fname.setText("");
        edt_renewalDate.setText("");
        edt_current_address.setText("");
        edt_alternetMoblieno.setText("");
        edt_landMark.setText("");
        edt_renewalDate.setVisibility(View.GONE);
        edt_current_address.setVisibility(View.GONE);
        edt_alternetMoblieno.setVisibility(View.GONE);
        edt_landMark.setVisibility(View.GONE);
        edt_taluka.setText("");
        edt_taluka.setVisibility(View.GONE);
        edt_district.setText("");
        edt_postOffice.setText("");
        edt_postOffice.setVisibility(View.GONE);
        edt_district.setVisibility(View.GONE);
        edt_moblieno.setText("");
        edt_aadhaarno.setText("");
        edt_aadhaarno.setFocusable(true);
        edt_aadhaarno.setFocusableInTouchMode(true);
        edt_dob.setText("");
        edt_age.setText("");
        edt_address.setText("");
        edt_local_address.setText("");
        edt_pincode.setText("");
        edt_renewal_date.setText("");
        edt_education.setText("");
        edt_relation.setText("");
        sw_renewed.setChecked(false);
//        rb_mr.setChecked(true);
//        rb_mrs.setChecked(false);
//        rb_ms.setChecked(false);
        rb_male.setEnabled(false);
        rb_female.setEnabled(false);
        sw_renewed.setChecked(false);
        rg_gender.clearCheck();
        imv_patient.setImageDrawable(getResources().getDrawable(R.drawable.icon_patientcamera));
        imv_health_card.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
        imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
        imv_hiv_concern.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
        patientPicBm = null;
        healthCardPicBm = null;
        renewalPicBm = null;
        hivConcernBm = null;
        genderId = "";
        title = "";
        RegId = "";
        patientImagePath = "";
        healthCardImagePath = "";
        renewalImagePath = "";
        hivletterPath = "";
        isPatientPhotoAvailable = false;
        isHealthCardPhotoAvailable = false;
        isRenewalSlipPhotoAvailable = false;
        btn_register.setEnabled(true);

        tvHealthCard.setText("Benificiary Card");
        edt_aadhaarno.setHint("Aadhar Number*");
        edtIdentity.setVisibility(View.GONE);
        tvRegdNo.setVisibility(View.GONE);
        btn_register.setVisibility(View.GONE);
        btn_VerifyOtpContact.setEnabled(true);
        btn_VerifyOtpContact.setText("Verify Otp");
        rbll.setVisibility(View.GONE);
        cbIsNumberBgs.setEnabled(true);
        cbIsNumberBgs.setChecked(false);
        btn_VerifyOtp.setEnabled(true);
        btn_VerifyOtp.setText("Verify Otp");
        btn_VerifyOtp.setVisibility(View.GONE);
        rg_selection.check(R.id.rb_self);
//        cbIsNumberBgs.setVisibility(View.GONE);
        edt_workerregno.setText("");
        edt_workerregno.setEnabled(true);

        rg_gender.setEnabled(true);
        rb_male.setEnabled(true);
        rb_female.setEnabled(true);

        txt_beneficiary_Fname.setText("");
        txt_beneficiary_Mname.setText("");
        txt_beneficiary_Lname.setText("");
        txt_beneficiary_gender_change.setText("");
        //   txt_beneficiary_age.setVisibility(View.GONE);
        txt_beneficiary_gender_change.setVisibility(View.GONE);



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

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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


    private class GetRelation extends AsyncTask<String, Void, String> {

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
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("RegdNo", edt_workerregno.getText().toString()));
            param.add(new ParamsPojo("ReleationID", relationId));
            param.add(new ParamsPojo("Gender", relationGenderId));
            res = WebServiceCall.APICall(ApplicationConstants.DEPRELACOUNT, ApplicationConstants.webservice, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // campList = new ArrayList<>();
                    // selectedCampList = new ArrayList<>();
                    List<RealationCheckModel.Output> campList = new ArrayList<>();

                    RealationCheckModel realationCheckModel = new Gson().fromJson(result, RealationCheckModel.class);
                    type = realationCheckModel.getStatus();
                    message = realationCheckModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (realationCheckModel.getOutput().size() > 0) {
                            RealationCheckModel.Output output = realationCheckModel.getOutput().get(0);

                            if (output.getColumn1() == 0) {

                                Utilities.showAlertDialog(context, "Alert", "Selected Relation Wrong Or Relation Count Reached", false);
                                edt_relation.setText("");
                                relationId = "";

                            }

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", message, false);
            }
        }
    }


    private  class GetTeamId extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("CampId", ConstantData.getInstance().getCalendarCampId()));
            param.add(new ParamsPojo("UserID", userId));
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);

            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            try {
                if (!result.equals("")) {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        teamId = jsonObject1.getString("TeamNumber");
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetTestCount().execute(campId,teamId,userId);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }

                    } else {
                        Utilities.showAlertDialog(context, "Error", " Your Selected Camp Not Mapped To You", false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }



    private class GetTestCount extends AsyncTask<String, Void, String> {

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
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("CAMPID", params[0]));
            param.add(new ParamsPojo("Teamid", params[1]));
            param.add(new ParamsPojo("Userid", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetPatientAndTestValidationCount, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // campList = new ArrayList<>();
                    // selectedCampList = new ArrayList<>();
                    List<TestCountModel.Output> campList = new ArrayList<>();

                    TestCountModel realationCheckModel = new Gson().fromJson(result, TestCountModel.class);
                    type = realationCheckModel.getStatus();
                    message = realationCheckModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (realationCheckModel.getOutput().size() > 0) {
                            TestCountModel.Output output = realationCheckModel.getOutput().get(0);

                            if (output.getAlltestdone().equalsIgnoreCase("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.icon_alertred);
                                builder.setTitle("Alert");
                                builder.setCancelable(false);
                                builder.setMessage("You cannot register new patients until registered beneficiaries screening is completed");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();

                            }

                        } else {
                            //  Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        //   Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //   Utilities.showAlertDialog(context, "Please Try Again", message, false);
            }
        }
    }




}
