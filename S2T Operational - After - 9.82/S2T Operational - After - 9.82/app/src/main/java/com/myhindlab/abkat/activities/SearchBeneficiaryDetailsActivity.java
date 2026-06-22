package com.myhindlab.abkat.activities;

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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.Dependent_Adapter_For_Search_Beneficiary;
import com.myhindlab.abkat.adapters.TeamCallingForSearchBeneficiaryAdapter;
import com.myhindlab.abkat.adapters.doortodoor.DependentSearchAdapter;
import com.myhindlab.abkat.models.DependentForSearchBeneficiaryModel;
import com.myhindlab.abkat.models.PatientDetailsModel;
import com.myhindlab.abkat.models.doortodoor.AgeCalculation;
import com.myhindlab.abkat.models.doortodoor.DocumentTypeModel;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.pojos.doortodoor.DependentPojo;
import com.myhindlab.abkat.pojos.doortodoor.LiveHealthRequest;
import com.myhindlab.abkat.pojos.doortodoor.RelationPojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchBeneficiaryDetailsActivity extends AppCompatActivity implements Dependent_Adapter_For_Search_Beneficiary.DepedentDetails {

    private Context context;
    private UserSessionManager session;

    private List<DependentForSearchBeneficiaryModel> dependentList;

    private TextView tvHealthCard,tv_workerName,tv_wokerAge,tv_nextRenewalDate,tv_workerGender,tv_activeOnBoard,
            tv_workerPincode,tv_workerMobileNumber, tvRegdNo;
    private LinearLayout ll_usersdetails;

    private EditText tv_workerLocalAddress,tv_worker_permanentAddress;
    private MaterialEditText edt_workerregno, edt_fname, edt_moblieno, edt_aadhaarno, edt_dob, edt_age,
            edt_address, edt_local_address, edt_pincode, edt_renewal_date, edt_relation, edtIdentity, edt_education, txt_beneficiary_name;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female;
    private CircleImageView imv_patient
            ;
    private ImageView imv_search, imv_health_card, imv_renewal_form, imv_hiv_concern, imv_self_declaration;
    private LinearLayout ll_renewal_photo, llSelfDeclaration, llHealthCard;
    private RadioGroup rg_gender;
    private Switch sw_renewed;
    private RecyclerView rv_dependent;
    private Button btn_register;
    private RadioButton rg_dep_yes, rg_dep_no;
    String regdID = "", registeredPatientregdid = "";
    ImageButton barCodeScannerBtn;
    private String IsPrimaryOTPVerify = "0", IsAdharDataVerify = "0",DESID;


    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private Uri patientURI, healthCardURI, renewalURI, hivConcernURI;
    private Bitmap patientPicBm = null, healthCardPicBm = null, IdentityPicBm = null, renewalPicBm = null, hivConcernBm = null;
    private final int PATIENT_CAMERA_REQUEST = 100, HEALTHCARD_CAMERA_REQUEST = 200, RENEWAL_CAMERA_REQUEST = 300, HIV_CONCERN_CAMERA_REQUEST = 400, IDENTITY_CAMERA_REQUEST = 500;
    private String userId = "", campId = "", siteId = "", genderId = "", title = "", Latitude = "", Longitude = "", RegId = "",
            patientImagePath = "", healthCardImagePath = "", renewalImagePath = "", hivletterPath = "", isRenewalFlag = "0", relationId = "20", identityId = "0";
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
        setContentView(R.layout.activity_search_beneficiary_details);

        init();
        disableViewAfterFill();
        requestPermission();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = SearchBeneficiaryDetailsActivity.this;
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
        llHealthCard = findViewById(R.id.llHealthCard);
        tvHealthCard = findViewById(R.id.tvHealthCard);
        rv_dependent = findViewById(R.id.rv_dependent);

        rv_dependent.setLayoutManager(new LinearLayoutManager(this));
        rv_dependent.setHasFixedSize(true);

        tvRegdNo = findViewById(R.id.tvRegdNo);

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

        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        rg_gender = findViewById(R.id.rg_gender);
        btn_register = findViewById(R.id.btn_register);
        rg_dep_yes = findViewById(R.id.rg_dep_yes);
        rg_dep_no = findViewById(R.id.rg_dep_no);
        tv_workerName = findViewById(R.id.tv_workerName);
        tv_wokerAge = findViewById(R.id.tv_wokerAge);
        tv_workerGender = findViewById(R.id.tv_workerGender);
        tv_nextRenewalDate = findViewById(R.id.tv_nextRenewalDate);
        tv_activeOnBoard = findViewById(R.id.tv_activeOnBoard);
        tv_worker_permanentAddress = findViewById(R.id.tv_worker_permanentAddress);
        tv_workerLocalAddress = findViewById(R.id.tv_workerLocalAddress);
        tv_workerPincode = findViewById(R.id.tv_workerPincode);
        tv_workerMobileNumber = findViewById(R.id.tv_workerMobileNumber);
        barCodeScannerBtn = findViewById(R.id.barcode_scanner_btn);

        title = "Mr.";
        txt_beneficiary_name = findViewById(R.id.txt_beneficiary_name);

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
                DESID =  json.getString("DESGID");
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

    }

    private void setEventHandler() {
        barCodeScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);
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
                    edt_workerregno.setText("");
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
                                if (DESID.equalsIgnoreCase("30")){
                                    new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());

                                    new GetDependentInfo().execute("1",edt_workerregno.getText().toString().trim());

                                    new GetDependentInfo().execute("1",edt_workerregno.getText().toString().trim());
                                    new GetWorkerRenewalDate().execute("2",edt_workerregno.getText().toString().trim());
                                }
                                new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
                                new GetDependentInfo().execute("1",edt_workerregno.getText().toString().trim());
                                new GetWorkerRenewalDate().execute("2",edt_workerregno.getText().toString().trim());


//                                new GetDependentInfo().execute("1",edt_workerregno.getText().toString().trim());
//                                new GetWorkerRenewalDate().execute("2",edt_workerregno.getText().toString().trim());


                                edt_workerregno.clearFocus();

//                            new GetWorkerInfro().execute("MH" + edt_workerregno.getText().toString().trim());
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
                                                                           if (age < 1 || age > 100) {
                                                                               Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 10 years or more than 100 years", false);
                                                                               edt_age.setText("");
                                                                           } else {
                                                                               edt_age.setText(String.valueOf(age));
                                                                           }
                                                                       } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
                                                                               || relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
                                                                               relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
                                                                           if (age < 18 || age > 100) {
                                                                               Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 100 years", false);
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
//                        new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
                        new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
                        new GetDependentInfo().execute("1",edt_workerregno.getText().toString().trim());
                        new GetWorkerRenewalDate().execute("2",edt_workerregno.getText().toString().trim());


//                        enableViewAfterFill();
                    } else {
                        isReregistration = false;
                        edt_workerregno.setText("");
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                } else {
                    if (Utilities.isNetworkAvailable(context)) {
                        new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
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
        imv_hiv_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 3;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
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

    @Override
    public void onCallClick(DependentForSearchBeneficiaryModel team) {


        new GetWorkerIndividualDetails().execute("3",String.valueOf(team.getRegdNo()));

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
                                Utilities.showAlertDialog(context, "Status", "Screening is already done on" + patientDetails.getRegistrationDate(), false);

                                clearPatientDetails();
                                return;
                            }
                            isReregistration = true;
//                            loadPatientDetails(patientDetails);
                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

                        } else {
                            clearPatientDetails();
                            relationId = "20";
                            edt_relation.setText("Self");
                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

                        }
                    } else {
                        clearPatientDetails();
                        relationId = "20";
                        edt_relation.setText("Self");
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
                        tv_workerName.setText(fName + " " + mName + " " + lName);
                        edt_moblieno.setText(mobile);
                        tv_workerMobileNumber.setText(mobile);
                        edt_aadhaarno.setText(aadhar);
                        edt_age.setText(age);
                        tv_wokerAge.setText(age);
                        String permAddress = permHouseNo + permArea + "," + permPostOffice + "," + permTaluka + "," + permDist + "," + permState + "," + permPincode;
                        edt_address.setText(permAddress);
                        tv_worker_permanentAddress.setText(permAddress);
                        String localAddress = localHouseNo + localArea + "," + localPostOffice + "," + localTaluka + "," + localDist + "," + localState + "," + localPincode;
                        edt_local_address.setText(localAddress);
                        tv_workerLocalAddress.setText(localAddress);
                        edt_pincode.setText(localPincode);
                        tv_workerPincode.setText(localPincode);


//                        new GetDependentInfo().execute("1",edt_workerregno.getText().toString().trim());
//                        new GetWorkerRenewalDate().execute("2",edt_workerregno.getText().toString().trim());



                        if (jsonArray.getJSONObject(0).getInt("age") != 0) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.YEAR, -jsonArray.getJSONObject(0).getInt("age"));
                            edt_dob.setText(new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()));
                        }
                        String gender = jsonArray.getJSONObject(0).getString("gender");
                        tv_workerGender.setText(gender);

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

                        if (isDependent.equalsIgnoreCase("0")) {
                            disableViewAfterFill();
                        }

                    } else {
                        clearPatientDetails();
                        enableViewAfterFill();
                        relationId = "20";
                        edt_relation.setText("Self");


                    }
//                    if (list.size() > 0) {
//                    } else {
//                        clearPatientDetails();
//                        isReregistration = false;
//                        new GetWorkerInfro().execute("MH" + edt_workerregno.getText().toString().trim());
//                    }
                } else {
                    clearPatientDetails();
//                    enableViewAfterFill();
                    relationId = "20";
                    edt_relation.setText("Self");

                }
            } catch (Exception e) {
                e.printStackTrace();
                clearPatientDetails();
//                enableViewAfterFill();
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
        edt_local_address.setEnabled(false);
        edt_pincode.setEnabled(false);
        edt_dob.setEnabled(false);
        rb_male.setEnabled(false);
        rb_female.setEnabled(false);
        rg_gender.setEnabled(false);

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



    public class GetDependentInfo extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("SearchType", params[0]));
            param.add(new ParamsPojo("RegdNo", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryInformationByRegdNo, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    DependentForSearchBeneficiaryModel dependentForSearchBeneficiaryModel = new Gson().fromJson(result, DependentForSearchBeneficiaryModel.class);
//                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);


                    type = dependentForSearchBeneficiaryModel.getStatus();
                    message = dependentForSearchBeneficiaryModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        dependentList = dependentForSearchBeneficiaryModel.getOutput();
                        if (dependentList != null && dependentList.size() > 0) {


//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());

                            rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary(context, dependentList, SearchBeneficiaryDetailsActivity.this::onCallClick));
                            int t = 0;

                            for (DependentForSearchBeneficiaryModel  output :
                                    dependentList) {

                                // t = t + Integer.valueOf(output.getaGE());

                                tv_nextRenewalDate.setText(output.getNext_renewal_date());
                                tv_activeOnBoard.setText(output.getIsActive());


                            }

                            //   tvTotalCount.setText("" + t);


////                            if (adminActiveInactiveModel.getOutput().size() > 0) {
////                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
////                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
////                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
////                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {
                            rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary(context, new ArrayList<>(), SearchBeneficiaryDetailsActivity.this::onCallClick));

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary(context, new ArrayList<>(), SearchBeneficiaryDetailsActivity.this::onCallClick));

                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }

    public class GetWorkerRenewalDate extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("SearchType", params[0]));
            param.add(new ParamsPojo("RegdNo", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryInformationByRegdNo, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    DependentForSearchBeneficiaryModel dependentForSearchBeneficiaryModel = new Gson().fromJson(result, DependentForSearchBeneficiaryModel.class);
//                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);


                    type = dependentForSearchBeneficiaryModel.getStatus();
                    message = dependentForSearchBeneficiaryModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        dependentList = dependentForSearchBeneficiaryModel.getOutput();
                        if (dependentList != null && dependentList.size() > 0) {


//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());



                            for (DependentForSearchBeneficiaryModel  output :
                                    dependentList) {

                                // t = t + Integer.valueOf(output.getaGE());

                                tv_nextRenewalDate.setText(output.getNext_renewal_date());
                                tv_activeOnBoard.setText(output.getIsActive());


                            }

                            //   tvTotalCount.setText("" + t);


////                            if (adminActiveInactiveModel.getOutput().size() > 0) {
////                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
////                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
////                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
////                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {

                            tv_nextRenewalDate.setText("NA");
                            tv_activeOnBoard.setText("NA");
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {

                        tv_nextRenewalDate.setText("NA");
                        tv_activeOnBoard.setText("NA");
                        //   Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }
    public class GetWorkerIndividualDetails extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("SearchType", params[0]));
            param.add(new ParamsPojo("RegdNo", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryInformationByRegdNo, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    TeamCallingModel teamCallingModel = new Gson().fromJson(result, TeamCallingModel.class);
                    type = teamCallingModel.getStatus();
                    message = teamCallingModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        View v = LayoutInflater.from(context).inflate(R.layout.d2d_team_calling_for_search_beneficiary, null, false);
                        builder.setView(v);
                        builder.setTitle("Screening Details");
//                        builder.setIcon(R.drawable.icon_campcreation);
                        RecyclerView recyclerView = v.findViewById(R.id.rv_d2dTeams);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setHasFixedSize(false);
                        TeamCallingForSearchBeneficiaryAdapter teamCallingForSearchBeneficiaryAdapter = new TeamCallingForSearchBeneficiaryAdapter(context, teamCallingModel.getOutput());
                        recyclerView.setAdapter(teamCallingForSearchBeneficiaryAdapter);

                        android.app.AlertDialog alertDialog = builder.create();
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                } else {
                    Utilities.showAlertDialog(context, "Fail", message, false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
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
                                regdID = edt_workerregno.getText().toString().trim() + "" + count;
                                flag = "1";

                                tvRegdNo.setText(regdID);

//                        edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});

                                edt_workerregno.setText(regdID);
                                txt_beneficiary_name.setVisibility(View.VISIBLE);
                                txt_beneficiary_name.setText(name);

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
                            txt_beneficiary_name.setVisibility(View.VISIBLE);
                            txt_beneficiary_name.setText(name);

                            if (Utilities.isNetworkAvailable(context)) {
                                new GetDependentListFromRegdId().execute(OriginalBeneficiaryNo);
                            } else {
                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                            }
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
                        break;
                    default:
                        rb_female.setChecked(false);
                        rb_male.setChecked(false);
                        rb_male.setEnabled(true);
                        rb_female.setEnabled(true);
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

        if (!Utilities.isMobileNo(edt_moblieno.getText().toString().trim())) {
            edt_moblieno.setError("Please enter valid mobile");
            return;
        }

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
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 10 || Integer.parseInt(edt_age.getText().toString().trim()) > 100) {
                    Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 10 years or more than 100 years", false);
                    return;
                }
            } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
                    || relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
                    relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 100) {
                    Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 100 years", false);
                    return;
                }
            } else {
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
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
                            hivletterPath,
                            isDependent,
                            edt_education.getText().toString(),
                            relationId,
                            DependREGID,
                            identityId
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
                multipart = new MultipartUtility(ApplicationConstants.D2DBeneficiaryRe_Registration, "UTF-8");
//                }else{
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
//                CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(SearchBeneficiaryDetailsActivity.this);
            }
            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
//                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(SearchBeneficiaryDetailsActivity.this);
            }
            if (requestCode == RENEWAL_CAMERA_REQUEST) {
//                CropImage.activity(renewalURI).setGuidelines(CropImageView.Guidelines.ON).start(SearchBeneficiaryDetailsActivity.this);
            }
            if (requestCode == HIV_CONCERN_CAMERA_REQUEST) {
//                CropImage.activity(hivConcernURI).setGuidelines(CropImageView.Guidelines.ON).start(SearchBeneficiaryDetailsActivity.this);
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
    }

    private void clearPatientDetails() {
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


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Beneficiary");

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
