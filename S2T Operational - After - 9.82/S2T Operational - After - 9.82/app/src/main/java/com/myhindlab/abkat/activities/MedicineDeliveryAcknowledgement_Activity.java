package com.myhindlab.abkat.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.confirmatoryTest.HealthScreeningSamplecollectionConfirmatory_Activity;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.GetMobileNumberModel;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceActivity;
import com.myhindlab.abkat.activities.payout.model.MonthModel;
import com.myhindlab.abkat.facedetection.FaceDetectionActivity;
import com.myhindlab.abkat.models.DeliveryRemarkStatusModel;
import com.myhindlab.abkat.models.DeliveryStatusModel;
import com.myhindlab.abkat.models.FaceDetectionModel;
import com.myhindlab.abkat.models.GenerateOtpModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineDeliveryAcknowledgement_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private String address;
    private MaterialEditText edt_mobile_no, edt_mobile_no_alternate;

    private boolean isPatientPhotoAvailable = false;

    private FusedLocationProviderClient mFusedLocationClient;

    //    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_REQUEST_CODE = 1000;
    private Location mGetedLocation;

    private double latitude = 0.00;
    private double longitude = 0.00;


    private Switch switch_button;


    private RadioButton rb_by_alternate_number, rb_by_worker_number, rb_by_sms_striker, rb_by_sms_91;

    private ImageView imv_barcode1;
    private CardView cv_finger_print, cv_signature, cv_uploaded_details;
    private RadioGroup rg_selection, rg_selection_for_mobile_number, rg_selection_for_otp_vendor;
    private RadioButton rb_thumb, rb_signature;
    private MaterialEditText edt_beneficiaryname, tv_pincode, edt_address, edt_barcode1, tv_prescriptionId, edt_MedicalId;
    private EditText edt_gender, edt_remarkOther, edt_age;
    private ImageView iv_biomatric, iv_uploaded_image, imvLetter,imvLetter_delivery_challan, imvDeliveryAck, imvCall, imvCallNew,imvCall_select_number;
    private TextView tv_finger_print_not_available, tv_deliveryStatus, edt_remark, tv_consentForm,tv_delivery_challan, tv_remark, tv_beneficiaryName,tvHealthCard_delivery_challan,
            tvHealthCard, edtVerifySuccess, edtNotAvailaible, edtDenied, tvDeliveryAck,tv_send_OTP;
    private Button btn_clearpad, btn_register, btn_sendOtp, btnSubmitPhoto, btnSubmitDeliveryAck;
    private Uri letterUri, ackUri,deliveryUri;
    private final int LETTER_CAMERA_REQUEST = 111;
    private final int ACK_CAMERA_REQUEST = 112;
    private final int DELIVERY_CHALLAN_REQUEST = 113;
    private File patientPicsFolder;

    private LinearLayout ll_main_vendor;


    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    private PostCampBeneficiaryListModel.OutputBean beneficiaryDetails;

    private String userId, RegId, campId, isAdmin, otpnumber, organizationId = "0", alternateMobileNo, workerMobileNo, smsId = "2",mobilenumber;
    private String DESGID, deliveryStatusId = "1", isOtpVrified = "0", remarkId = "0", LabCode, CampDATE, DISTLGDCODE, BMobile, district, TALLGDCODE, taluka, STATELGDCODE = "2", selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId, AssignedId = "", IsTeam;

    private File signatureImageFile, patientSignFolder, fingerPrintFolder;

    private String faceDetectionCompulsory = "0";

    private String TAG = getClass().getName();
    private String fingerImage = "";
    private boolean isFingerPrint = false;
    private boolean isSignedByUser = false;

    String deviceMan = Build.MANUFACTURER;
    private boolean isCaptureRunning = false;
    private boolean isCaptured = false;
    private String letterImagePath, isfaceDetection = "0", ackImagePath,deliveryPath;
    private Bitmap letterPicBm, ackPicBm,deliveryChallanPicBm;
    private int imageType = 0;

    private LinearLayout mainllDc,LL_skipFaceDetection, ll_main_other_description, llmain_remark,ll_main_slectNumber, llDeliveryAck,llHealthCard_delivery_challan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_delivery_acknowledgement);

        init();
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
        TextView tv_timer = alertLayout.findViewById(R.id.tv_timer); // <- New Timer TextView

        resendOtpBtn.setVisibility(View.GONE); // Hide resend initially
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        startResendTimer(resendOtpBtn, tv_timer); // Updated to pass TextView

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


            ///new change


//
//        if (BuildConfig.isBeta){
//            new VerifyOtp(alertDialog).execute(mobilenumber, edt_Otp.getText().toString().trim());
//
//        }




            if (rb_by_worker_number.isChecked()){


                new VerifyOtp(alertDialog).execute(workerMobileNo, edt_Otp.getText().toString().trim());

            } else if (rb_by_alternate_number.isChecked()) {


                new VerifyOtp(alertDialog).execute(alternateMobileNo, edt_Otp.getText().toString().trim());


            }


//            new VerifyOtp(alertDialog).execute(edt_mobile_no.getText().toString().trim(), edt_Otp.getText().toString().trim());
//            Utilities.showToastMessage("OTP Verified Successfully", context, true);
//            btn_register.setVisibility(View.VISIBLE);



            // alertDialog.dismiss();
        });

        resendOtpBtn.setOnClickListener(view -> {
            //verify api call
            // alertDialog.dismiss();



///new change
//
//        if (BuildConfig.isBeta){
//
//
//            if (rb_by_sms_striker.isChecked()) {
//                smsId = "2";
//            } else if (rb_by_sms_91.isChecked()) {
//                smsId = "1";
//
//            }
//
//
//            new GetOtp(2).execute(mobilenumber, otpnumber, RegId, UserId, UserId, smsId);
//
//
//        }
//
//





            if (rb_by_worker_number.isChecked()) {

                if (rb_by_sms_striker.isChecked()) {
                    smsId = "2";
                } else if (rb_by_sms_91.isChecked()) {
                    smsId = "1";

                }


                new GetOtp(2).execute(workerMobileNo, otpnumber, RegId, UserId, UserId, smsId);

            } else if (rb_by_alternate_number.isChecked()) {


                if (rb_by_sms_striker.isChecked()) {
                    smsId = "2";
                } else if (rb_by_sms_91.isChecked()) {
                    smsId = "1";

                }
                new GetOtp(2).execute(alternateMobileNo, otpnumber, RegId, UserId, UserId, smsId);

            }

//            new GetOtp(2).execute(edt_mobile_no.getText().toString(), otpnumber, RegId, UserId, UserId);
            //  new GetOtp(2).execute(edt_mobile_no.getText().toString(), RegId, UserId, "1");
            edt_Otp.setText("");


            resendOtpBtn.setVisibility(View.GONE);
            startResendTimer(resendOtpBtn, tv_timer);

        });

    }


    private void startResendTimer(Button resendOtpBtn, TextView tv_timer) {
        tv_timer.setVisibility(View.VISIBLE); // Show timer
//        new CountDownTimer(2 * 60 * 1000, 1000) { // 2 minutes
//            public void onTick(long millisUntilFinished) {
//                int minutes = (int) (millisUntilFinished / 1000) / 60;
//                int seconds = (int) (millisUntilFinished / 1000) % 60;
//                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
//                tv_timer.setText(timeFormatted);
//                btn_sendOtp.setEnabled(false);
//
//            }
//
//            public void onFinish() {
//                tv_timer.setVisibility(View.GONE);
//                resendOtpBtn.setVisibility(View.VISIBLE);
//                btn_sendOtp.setEnabled(true);
//
//
//            }
//        }.start();


        new CountDownTimer(120 * 1000, 1000) { // 120 seconds
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tv_timer.setText("Resend OTP in : " + String.valueOf(secondsRemaining) + " sec");
                btn_sendOtp.setEnabled(false);
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                resendOtpBtn.setVisibility(View.VISIBLE);
                btn_sendOtp.setEnabled(true);
            }
        }.start();

    }


    private void init() {
        context = MedicineDeliveryAcknowledgement_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        tvHealthCard = findViewById(R.id.tvHealthCard);
        cv_finger_print = findViewById(R.id.cv_finger_print);
        cv_signature = findViewById(R.id.cv_signature);
        cv_uploaded_details = findViewById(R.id.cv_uploaded_details);
        rg_selection = findViewById(R.id.rg_selection);
        rg_selection_for_mobile_number = findViewById(R.id.rg_selection_for_mobile_number);
        rg_selection_for_otp_vendor = findViewById(R.id.rg_selection_for_otp_vendor);
        rb_by_alternate_number = findViewById(R.id.rb_by_alternate_number);
        rb_by_worker_number = findViewById(R.id.rb_by_worker_number);
        ll_main_slectNumber = findViewById(R.id.ll_main_slectNumber);
        rb_by_sms_striker = findViewById(R.id.rb_by_sms_striker);
        rb_thumb = findViewById(R.id.rb_thumb);
        rb_by_sms_91 = findViewById(R.id.rb_by_sms_91);
        rb_signature = findViewById(R.id.rb_signature);
        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_remarkOther = findViewById(R.id.edt_remarkOther);
        llmain_remark = findViewById(R.id.llmain_remark);
        ll_main_other_description = findViewById(R.id.ll_main_other_description);
        edt_age = findViewById(R.id.edt_age);
        iv_biomatric = findViewById(R.id.iv_biomatric);
        iv_uploaded_image = findViewById(R.id.iv_uploaded_image);
        imvLetter = findViewById(R.id.imvLetter);
        imvLetter_delivery_challan = findViewById(R.id.imvLetter_delivery_challan);
        tvHealthCard_delivery_challan = findViewById(R.id.tvHealthCard_delivery_challan);
        btnSubmitPhoto = findViewById(R.id.btnSubmitPhoto);
        btnSubmitDeliveryAck = findViewById(R.id.btnSubmitDeliveryAck);
        imvDeliveryAck = findViewById(R.id.imvDeliveryAck);
        imvCall_select_number = findViewById(R.id.imvCall_select_number);
        switch_button = findViewById(R.id.switch_button);
        imvCall = findViewById(R.id.imvCall);
        imvCallNew = findViewById(R.id.imvCallNew);
        tvDeliveryAck = findViewById(R.id.tvDeliveryAck);
        tv_send_OTP = findViewById(R.id.tv_send_OTP);
        tv_prescriptionId = findViewById(R.id.tv_prescriptionId);
        edt_address = findViewById(R.id.edt_address);
        edt_MedicalId = findViewById(R.id.edt_MedicalId);
        LL_skipFaceDetection = findViewById(R.id.LL_skipFaceDetection);
        tv_beneficiaryName = findViewById(R.id.tv_beneficiaryName);
        tv_pincode = findViewById(R.id.tv_pincode);
        edt_barcode1 = findViewById(R.id.edt_barcode1);
        imv_barcode1 = findViewById(R.id.imv_barcode1);

        tv_finger_print_not_available = findViewById(R.id.tv_finger_print_not_available);
        btn_register = findViewById(R.id.btn_register);
        btn_sendOtp = findViewById(R.id.btn_sendOtp);
        edt_mobile_no = findViewById(R.id.edt_mobile_no);
        edt_mobile_no_alternate = findViewById(R.id.edt_mobile_no_alternate);
        edtVerifySuccess = findViewById(R.id.edtVerifySuccess);
        mainllDc = findViewById(R.id.mainllDc);
        llDeliveryAck = findViewById(R.id.llDeliveryAck);
        llHealthCard_delivery_challan = findViewById(R.id.llHealthCard_delivery_challan);
        tv_deliveryStatus = findViewById(R.id.tv_deliveryStatus);
        edt_remark = findViewById(R.id.edt_remark);
        edtNotAvailaible = findViewById(R.id.edtNotAvailaible);
        edtDenied = findViewById(R.id.edtDenied);
        tv_consentForm = findViewById(R.id.tv_consentForm);
        tv_delivery_challan = findViewById(R.id.tv_delivery_challan);
        tv_remark = findViewById(R.id.tv_remark);
        ll_main_vendor = findViewById(R.id.ll_main_vendor);


        btnSubmitDeliveryAck.setVisibility(View.GONE);
        edt_barcode1.setEnabled(false);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // 10 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
//                    Toast.makeText(context, "Lati"+latitude+" "+"Longi"+longitude, Toast.LENGTH_SHORT).show();
                }

                Geocoder geocoder = new Geocoder(context);
                try {
                    List<Address> geocode = geocoder.getFromLocation(latitude, longitude, 1);

                     address = geocode.get(0).getAddressLine(0);

//                    tvGoogleLocation.setText(address);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        if (SDK_INT >= Build.VERSION_CODES.Q) {
            patientPicsFolder = getExternalCacheDir();
        } else {
            patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Photos/");

            if (!patientPicsFolder.exists())
                patientPicsFolder.mkdirs();
        }

//        patientSignFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Signature/");
        patientSignFolder = getExternalCacheDir();
//        if (!patientSignFolder.exists())
//            patientSignFolder.mkdirs();

//        fingerPrintFolder = new File(Environment.getExternalStorageDirectory(), "/Health Checkup/ThumbData/");
        fingerPrintFolder = getExternalCacheDir();
//        if (!fingerPrintFolder.exists()) {
//            fingerPrintFolder.mkdirs();
//        }

    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                UserId = json.getString("EmpCode");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                LabCode = json.getString("LabCode");
                BMobile = json.getString("BMobile");
                organizationId = json.getString("SubOrgId");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setDefaults() {


        getFaceDetectionFlag();



        isAdmin = getIntent().getStringExtra("isAdmin");
        beneficiaryDetails = (PostCampBeneficiaryListModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");
        RegId = beneficiaryDetails.getRegdId();
        campId = beneficiaryDetails.getCAMPID();
//        mobileno = beneficiaryDetails.getMobileNo();


            alternateMobileNo = beneficiaryDetails.getAlternateMobNo();
            workerMobileNo = beneficiaryDetails.getWorkersMob();
//            workerMobileNo = "9665253245";


            if (!TextUtils.isEmpty(alternateMobileNo)) {
                rb_by_alternate_number.setChecked(true);
            } else {
                rb_by_worker_number.setChecked(true);
                rb_by_alternate_number.setEnabled(false);
            }




        tv_prescriptionId.setText(beneficiaryDetails.getBeneficiry_Number());
        edt_MedicalId.setText(beneficiaryDetails.getTreatmentID());
        tv_pincode.setText(beneficiaryDetails.getPincode());
        edt_address.setText(beneficiaryDetails.getBeneficiry_Address());
        edt_barcode1.setText(beneficiaryDetails.getDeliveryChallanID());

        if (beneficiaryDetails.getDelivarystatus().equals("Y")) {
            btn_sendOtp.setVisibility(View.GONE);
            rg_selection_for_mobile_number.setVisibility(View.GONE);
            ll_main_vendor.setVisibility(View.GONE);
            btnSubmitPhoto.setVisibility(View.GONE);
            btnSubmitDeliveryAck.setVisibility(View.GONE);
            tvHealthCard.setText("View Beneficiary Photo");
            tvDeliveryAck.setText("View Consent form");
            tvHealthCard_delivery_challan.setText("View Delivery Challan");
            mainllDc.setVisibility(View.GONE);


            tvHealthCard.setTextColor(this.getResources().getColor(R.color.blue));
            tvDeliveryAck.setTextColor(this.getResources().getColor(R.color.blue));
            tvHealthCard_delivery_challan.setTextColor(this.getResources().getColor(R.color.blue));

            imvLetter.setEnabled(false);
            imvLetter_delivery_challan.setEnabled(false);
            imvDeliveryAck.setEnabled(false);
            edtVerifySuccess.setVisibility(View.VISIBLE);

            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/MedicineDelivery/BeneficiaryPhoto/" + beneficiaryDetails.getBeneficiary_photoPath();
                Picasso.with(context).load(url).into(imvLetter);

//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });
                tvHealthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            } else {
                String url = BuildConfig.domain + "/MedicineDelivery/BeneficiaryPhoto/" + beneficiaryDetails.getBeneficiary_photoPath();
                Picasso.with(context).load(url).into(imvLetter);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvHealthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            }

            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/MedicineDelivery/ConsentForm/" + beneficiaryDetails.getConsent_Form_PhotoPath();
                Picasso.with(context).load(url).into(imvDeliveryAck);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvDeliveryAck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            } else {
                String url = BuildConfig.domain + "/MedicineDelivery/ConsentForm/" + beneficiaryDetails.getConsent_Form_PhotoPath();
                Picasso.with(context).load(url).into(imvDeliveryAck);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvDeliveryAck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            }



            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/MedicineDelivery/DeliveryChallan/" + beneficiaryDetails.getDeliveryChallan_PhotoPath();
                Picasso.with(context).load(url).into(imvLetter_delivery_challan);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvHealthCard_delivery_challan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            } else {
                String url = BuildConfig.domain + "/MedicineDelivery/DeliveryChallan/" + beneficiaryDetails.getDeliveryChallan_PhotoPath();
                Picasso.with(context).load(url).into(imvLetter_delivery_challan);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvHealthCard_delivery_challan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            }


        } else {
            btn_sendOtp.setVisibility(View.VISIBLE);
            // btnSubmitPhoto.setVisibility(View.VISIBLE);
            tvHealthCard.setVisibility(View.VISIBLE);
            edtVerifySuccess.setVisibility(View.GONE);
            edtNotAvailaible.setVisibility(View.GONE);
            edtDenied.setVisibility(View.GONE);
        }

        if (beneficiaryDetails.getDeliveryStatusRemarkID().equals("1")) {

            remarkId = beneficiaryDetails.getDeliveryRemarkID();

            deliveryStatusId = beneficiaryDetails.getDeliveryStatusRemarkID();
            btn_sendOtp.setVisibility(View.GONE);
            rg_selection_for_mobile_number.setVisibility(View.GONE);
            ll_main_vendor.setVisibility(View.GONE);
            btnSubmitPhoto.setVisibility(View.GONE);
            btnSubmitDeliveryAck.setVisibility(View.GONE);
            tvHealthCard.setText("View Beneficiary Photo");
            tvDeliveryAck.setText("View Consent form");
            tvHealthCard_delivery_challan.setText("View Delivery Challan");
            mainllDc.setVisibility(View.GONE);
            tv_deliveryStatus.setEnabled(false);
            edtNotAvailaible.setVisibility(View.GONE);
            edtDenied.setVisibility(View.GONE);


            tvHealthCard.setTextColor(this.getResources().getColor(R.color.blue));
            tvDeliveryAck.setTextColor(this.getResources().getColor(R.color.blue));
            tvHealthCard_delivery_challan.setTextColor(this.getResources().getColor(R.color.blue));

            imvLetter.setEnabled(false);
            imvLetter_delivery_challan.setEnabled(false);
            imvDeliveryAck.setEnabled(false);
            edtVerifySuccess.setVisibility(View.VISIBLE);

            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/MedicineDelivery/BeneficiaryPhoto/" + beneficiaryDetails.getBeneficiary_photoPath();
                Picasso.with(context).load(url).into(imvLetter);

//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });



                tvHealthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            } else {
                String url = BuildConfig.domain + "/MedicineDelivery/BeneficiaryPhoto/" + beneficiaryDetails.getBeneficiary_photoPath();
                Picasso.with(context).load(url).into(imvLetter);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvHealthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            }

            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/MedicineDelivery/ConsentForm/" + beneficiaryDetails.getConsent_Form_PhotoPath();
                Picasso.with(context).load(url).into(imvDeliveryAck);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvDeliveryAck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            } else {
                String url = BuildConfig.domain + "/MedicineDelivery/ConsentForm/" + beneficiaryDetails.getConsent_Form_PhotoPath();
                Picasso.with(context).load(url).into(imvDeliveryAck);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvDeliveryAck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            }


            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/MedicineDelivery/DeliveryChallan/" + beneficiaryDetails.getDeliveryChallan_PhotoPath();
                Picasso.with(context).load(url).into(imvLetter_delivery_challan);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvHealthCard_delivery_challan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            } else {
                String url = BuildConfig.domain + "/MedicineDelivery/DeliveryChallan/" + beneficiaryDetails.getDeliveryChallan_PhotoPath();
                Picasso.with(context).load(url).into(imvLetter_delivery_challan);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvHealthCard_delivery_challan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            }

        } else if (beneficiaryDetails.getDeliveryStatusRemarkID().equals("2")) {

            deliveryStatusId = beneficiaryDetails.getDeliveryStatusRemarkID();
            remarkId = beneficiaryDetails.getDeliveryRemarkID();

            edtNotAvailaible.setVisibility(View.VISIBLE);
            tv_deliveryStatus.setText(beneficiaryDetails.getDeliveryStatusName());

            if (beneficiaryDetails.getBeneficiary_photoPath() != null) {
                tvHealthCard.setText("View Beneficiary Photo");

                tvHealthCard.setTextColor(this.getResources().getColor(R.color.blue));
//                tvDeliveryAck.setTextColor(this.getResources().getColor(R.color.blue));

            }

            btn_sendOtp.setVisibility(View.GONE);
            rg_selection_for_mobile_number.setVisibility(View.GONE);
            ll_main_vendor.setVisibility(View.GONE);
            llDeliveryAck.setVisibility(View.GONE);
            llHealthCard_delivery_challan.setVisibility(View.GONE);
            tv_consentForm.setVisibility(View.GONE);
            tv_delivery_challan.setVisibility(View.GONE);
            btnSubmitPhoto.setVisibility(View.VISIBLE);
            llmain_remark.setVisibility(View.VISIBLE);

            if (beneficiaryDetails.getDeliveryRemarkID() == "0") {
                llmain_remark.setVisibility(View.VISIBLE);
                edt_remark.setText("Other");
                edt_remark.setText(beneficiaryDetails.getDeliveryRemark());
            } else {
                edt_remark.setText(beneficiaryDetails.getDeliveryRemark());
            }

            if (beneficiaryDetails.getOtherRemark() != null && !beneficiaryDetails.getOtherRemark().equals("")) {
                ll_main_other_description.setVisibility(View.VISIBLE);
                edt_remarkOther.setText(beneficiaryDetails.getOtherRemark());
                edt_remark.setText("Other");
            }

//            btn_sendOtp.setVisibility(View.GONE);
//            btnSubmitPhoto.setVisibility(View.GONE);
//            btnSubmitDeliveryAck.setVisibility(View.GONE);
//            tvHealthCard.setText("View Beneficiary Photo");
//            tvDeliveryAck.setText("View Consent form");
//            mainllDc.setVisibility(View.GONE);
//            tv_deliveryStatus.setEnabled(false);


            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/MedicineDelivery/BeneficiaryPhoto/" + beneficiaryDetails.getBeneficiary_photoPath();
                Picasso.with(context).load(url).into(imvLetter);

//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });
                tvHealthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            } else {
                String url = BuildConfig.domain + "/MedicineDelivery/BeneficiaryPhoto/" + beneficiaryDetails.getBeneficiary_photoPath();
                Picasso.with(context).load(url).into(imvLetter);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvHealthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            }

            if (beneficiaryDetails.getBeneficiary_photoPath() == null) {
                imvLetter.setImageResource(R.drawable.icon_colorcamera);
            }
//            if (beneficiaryDetails.getBeneficiary_photoPath() == null) {
//                imvLetter_delivery_challan.setImageResource(R.drawable.icon_colorcamera);
//            }


        } else if (beneficiaryDetails.getDeliveryStatusRemarkID().equals("3")) {

            remarkId = beneficiaryDetails.getDeliveryRemarkID();

            deliveryStatusId = beneficiaryDetails.getDeliveryStatusRemarkID();

            if (beneficiaryDetails.getBeneficiary_photoPath() != null) {
                tvHealthCard.setText("View Beneficiary Photo");

                tvHealthCard.setTextColor(this.getResources().getColor(R.color.blue));
                tvDeliveryAck.setTextColor(this.getResources().getColor(R.color.blue));

            } else {

                tvHealthCard.setEnabled(false);
                tvHealthCard.setText("Image Not Available");

            }

            imvLetter.setEnabled(false);
            edtDenied.setVisibility(View.VISIBLE);
            btnSubmitPhoto.setVisibility(View.GONE);

            tv_deliveryStatus.setText(beneficiaryDetails.getDeliveryStatusName());

            btn_sendOtp.setVisibility(View.GONE);
            rg_selection_for_mobile_number.setVisibility(View.GONE);
            ll_main_vendor.setVisibility(View.GONE);
            llDeliveryAck.setVisibility(View.GONE);
            llHealthCard_delivery_challan.setVisibility(View.GONE);
            tv_consentForm.setVisibility(View.GONE);
            tv_delivery_challan.setVisibility(View.GONE);
//            btnSubmitPhoto.setVisibility(View.VISIBLE);
            llmain_remark.setVisibility(View.VISIBLE);

            if (beneficiaryDetails.getDeliveryRemarkID() == "0") {
                llmain_remark.setVisibility(View.VISIBLE);
                edt_remark.setText("Other");
                edt_remark.setText(beneficiaryDetails.getDeliveryRemark());
            } else {
                edt_remark.setText(beneficiaryDetails.getDeliveryRemark());
            }

            if (beneficiaryDetails.getOtherRemark() != null) {
                ll_main_other_description.setVisibility(View.VISIBLE);
                edt_remarkOther.setText(beneficiaryDetails.getOtherRemark());
                edt_remark.setText("Other");
            }

//            btn_sendOtp.setVisibility(View.GONE);
//            btnSubmitPhoto.setVisibility(View.GONE);
//            btnSubmitDeliveryAck.setVisibility(View.GONE);
//            tvHealthCard.setText("View Beneficiary Photo");
//            tvDeliveryAck.setText("View Consent form");
//            mainllDc.setVisibility(View.GONE);
//            tv_deliveryStatus.setEnabled(false);


            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/MedicineDelivery/BeneficiaryPhoto/" + beneficiaryDetails.getBeneficiary_photoPath();
                Picasso.with(context).load(url).into(imvLetter);

//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });
                tvHealthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            } else {
                String url = BuildConfig.domain + "/MedicineDelivery/BeneficiaryPhoto/" + beneficiaryDetails.getBeneficiary_photoPath();
                Picasso.with(context).load(url).into(imvLetter);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });

                tvHealthCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    }
                });


            }

            if (beneficiaryDetails.getBeneficiary_photoPath() == null) {
                imvLetter.setImageResource(R.drawable.icon_colorcamera);
            }

            tv_deliveryStatus.setEnabled(false);
            edt_remark.setEnabled(false);
            edt_remarkOther.setEnabled(false);
        }


        edt_beneficiaryname.setText(beneficiaryDetails.getPatient_Name());
        tv_beneficiaryName.setText(beneficiaryDetails.getPatient_Name());
        edt_gender.setText(beneficiaryDetails.getGender());
        edt_age.setText(beneficiaryDetails.getAge());
//        edt_mobile_no.setEnabled(false);
        edt_mobile_no.setText(workerMobileNo);
        edt_mobile_no_alternate.setText(alternateMobileNo);


        if (beneficiaryDetails.getPostCampAcknowlegedmentFile() != null && !beneficiaryDetails.getPostCampAcknowlegedmentFile().equals("NA")) {
            String url = BuildConfig.domain + "/CampDocs/PostCampAcknowlegedmentImage/" + beneficiaryDetails.getPostCampAcknowlegedmentFile();
            Picasso.with(context)
                    .load(url)
                    .into(iv_uploaded_image);

            rb_thumb.setChecked(true);
            rg_selection.setVisibility(View.VISIBLE);
            cv_finger_print.setVisibility(View.VISIBLE);
            cv_signature.setVisibility(View.GONE);
            cv_uploaded_details.setVisibility(View.GONE);
            btn_register.setVisibility(View.GONE);
//            btn_sendOtp.setVisibility(View.VISIBLE);
            edt_mobile_no.setVisibility(View.VISIBLE);
            edt_mobile_no_alternate.setVisibility(View.VISIBLE);
            tv_finger_print_not_available.setVisibility(View.GONE);

        } else {
            if (isAdmin.equals("1")) {
                rg_selection.setVisibility(View.GONE);
                cv_finger_print.setVisibility(View.GONE);
                cv_signature.setVisibility(View.GONE);
                cv_uploaded_details.setVisibility(View.GONE);
                btn_register.setVisibility(View.GONE);
                tv_finger_print_not_available.setVisibility(View.VISIBLE);
            } else {

                rg_selection.setOnCheckedChangeListener((group, checkedId) -> {
                    switch (checkedId) {
                        case R.id.rb_thumb:
                            cv_finger_print.setVisibility(View.VISIBLE);
                            cv_signature.setVisibility(View.GONE);
                            edt_mobile_no.setVisibility(View.VISIBLE);
                            edt_mobile_no_alternate.setVisibility(View.VISIBLE);
                            // btn_sendOtp.setVisibility(View.VISIBLE);
                            btn_register.setVisibility(View.GONE);

                            break;

                        case R.id.rb_signature:
                            cv_finger_print.setVisibility(View.GONE);
                            cv_signature.setVisibility(View.VISIBLE);
                            edt_mobile_no.setVisibility(View.GONE);
                            edt_mobile_no_alternate.setVisibility(View.GONE);
                            //  btn_sendOtp.setVisibility(View.GONE);
                            btn_register.setVisibility(View.VISIBLE);
                            break;
                    }
                });

                rg_selection.check(R.id.rb_thumb);
            }
        }

    }


    private void setEventHandler() {
        btnSubmitPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (letterImagePath == null) {
//                    Utilities.showToastMessage("Please capture image", context, false);
//                    return;
//                }

//                new UploadPatientDetails().execute(letterImagePath, beneficiaryDetails.getRegdId(), beneficiaryDetails.getMobileNo(), DISTLGDCODE);


                if (deliveryStatusId.equals("3")) {
                    if (edt_remark.getText().toString().trim().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please select remark", false);
                        return;
                    }

                }

                if (ll_main_other_description.getVisibility() == View.VISIBLE) {
                    if (edt_remarkOther.getText().toString().trim().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please enter other description", false);
                        return;
                    }

                }

                if (letterImagePath == null) {
                    letterImagePath = "";
                }
//
//                if (deliveryPath == null) {
//                    deliveryPath = "";
//                }

                new ReportDeliveryAck().execute(beneficiaryDetails.getRegdId(), "", letterImagePath,"");

            }
        });

        tv_send_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMobileNumber();

            }
        });

        switch_button.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch is ON
//                Toast.makeText(this, "Switch is ON", Toast.LENGTH_SHORT).show();
                isfaceDetection = "1";
            } else {
//                // Switch is OFF
//                Toast.makeText(this, "Switch is OFF", Toast.LENGTH_SHORT).show();

                isfaceDetection = "0";

            }
        });



//        if (l)

        imvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_mobile_no.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Worker Number Not Found", false);
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_DIAL);
                ///new change
                if (BuildConfig.isBeta){
                    intent.setData(Uri.parse("tel:" + mobilenumber));

                }else {
                    intent.setData(Uri.parse("tel:" + workerMobileNo));
                }
                startActivity(intent);
            }
        });


        imvCallNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edt_mobile_no_alternate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Alternate Number Not Found", false);
                    return;
                }


                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + alternateMobileNo));
                startActivity(intent);
            }
        });


        tv_deliveryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetDeliveryStatus().execute();

            }
        });
        edt_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_deliveryStatus.getText().toString().trim().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Please select delivery status", false);
                    return;
                }
                new GetRemark().execute(deliveryStatusId);

            }
        });

        imv_barcode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
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
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
                        return;
                    }
                }

                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);
            }
        });


        btnSubmitDeliveryAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (letterImagePath == null) {
                    Utilities.showToastMessage("Please capture beneficiary photo", context, false);
                    return;
                }

                if (ackImagePath == null) {
                    Utilities.showToastMessage("Please capture consent form", context, false);
                    return;
                }
                if (deliveryPath == null) {
                    Utilities.showToastMessage("Please capture delivery challan photo", context, false);
                    return;
                }

                Dialog dialog;
                dialog = new Dialog(MedicineDeliveryAcknowledgement_Activity
                        .this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_popup_dialoge);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                Button btnYes = dialog.findViewById(R.id.btnYes);
                Button btnNo = dialog.findViewById(R.id.btnNo);
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new ReportDeliveryAck().execute(beneficiaryDetails.getRegdId(), ackImagePath, letterImagePath,deliveryPath);

                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

//                new ReportDeliveryAck().execute(beneficiaryDetails.getRegdId(), ackImagePath, letterImagePath);
            }
        });

        imvLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageType = 0;


                if (isfaceDetection.equals("0")) {
                    try {
                        Intent intent = new Intent(context, FaceDetectionActivity.class);
                        intent.putExtra("campId", campId);
                        intent.putExtra("userID", String.valueOf(session.getUserDetailsJson().getEmpCode()));
                        faceDetectionResult.launch(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Face Recognition App not installed",
                                "Please install Face Recognition App first \n" + e.getMessage(), false);
                    }

                } else if (isfaceDetection.equals("1")) {

                    if (SDK_INT < Build.VERSION_CODES.S) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                            return;
                        }

                    } else {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
                            return;
                        }
                    }
                    int randomEndtNo = (int) (Math.random() * 99999 + 1);
                    if (SDK_INT >= Build.VERSION_CODES.Q) {
                        ContentResolver resolver = context.getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_BF.png");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                        letterUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
                        startActivityForResult(intent, LETTER_CAMERA_REQUEST);
                    } else {
                        File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_BF.png");
                        letterUri = Uri.fromFile(patientImageFile);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
                        startActivityForResult(intent, LETTER_CAMERA_REQUEST);
                    }

                }


            }
        });
        imvDeliveryAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageType = 1;
                if (SDK_INT < Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                        return;
                    }

                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
                        return;
                    }
                }
                int randomEndtNo = (int) (Math.random() * 99999 + 1);
                if (SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_CF.png");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    ackUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, ackUri);
                    startActivityForResult(intent, ACK_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_CF.png");
                    ackUri = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, ackUri);
                    startActivityForResult(intent, ACK_CAMERA_REQUEST);
                }


            }
        });
        imvLetter_delivery_challan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageType = 2;
                if (SDK_INT < Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                        return;
                    }

                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
                        return;
                    }
                }
                int randomEndtNo = (int) (Math.random() * 99999 + 1);
                if (SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_DC.png");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    deliveryUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, deliveryUri);
                    startActivityForResult(intent, DELIVERY_CHALLAN_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_DC.png");
                    deliveryUri = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, deliveryUri);
                    startActivityForResult(intent, DELIVERY_CHALLAN_REQUEST);
                }


            }
        });


        rb_by_worker_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_by_alternate_number.setChecked(false);

            }
        });

        rb_by_alternate_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rb_by_worker_number.setChecked(false);
            }
        });
//
//        if (rb_by_worker_number.isChecked()){
//            rb_by_alternate_number.setChecked(false);
//        } else if (rb_by_alternate_number.isChecked()) {
//
//        }


        btn_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //api call api success

//                Utilities.showToastMessage("OTP Send Successfully", context, true);
//                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdId()));
//
//                finish();
//                new GenerateOtp().execute();
//              Live  new GetOtp(1).execute(edt_mobile_no.getText().toString(), otpnumber, RegId, userId, "1", session.getSessionMobile(), session.getSessionDistrict());


                ///new change
//                if (BuildConfig.isBeta){
//                    if (tv_send_OTP.getText().toString().isEmpty()){
//                        tv_send_OTP.setError("Please select mobile number");
//                        return;
//                    }
//
//
//                    if (rb_by_sms_striker.isChecked()) {
//                        smsId = "2";
//                    } else if (rb_by_sms_91.isChecked()) {
//                        smsId = "1";
//
//                    }
//
//
//                    new GetOtp(1).execute(mobilenumber, otpnumber, RegId, UserId, UserId, smsId);
//
//
//
//                }



                if (rb_by_worker_number.isChecked()) {

                    if (edt_mobile_no.getText().toString().trim().matches("")) {
                        edt_mobile_no.setError("Worker Number Not Found");
                        return;
                    }
                    if (edt_mobile_no.getText().toString().trim().length() != 10) {
                        edt_mobile_no.setError("Please enter valid 10 digit mobile number");
                        return;
                    }

                    if (rb_by_sms_striker.isChecked()) {
                        smsId = "2";
                    } else if (rb_by_sms_91.isChecked()) {
                        smsId = "1";

                    }

                    new GetOtp(1).execute(workerMobileNo, otpnumber, RegId, UserId, UserId, smsId);

                } else if (rb_by_alternate_number.isChecked()) {

                    if (edt_mobile_no_alternate.getText().toString().trim().matches("")) {
                        edt_mobile_no_alternate.setError("Alternate Number Not Found");
                        return;
                    }


                    if (edt_mobile_no_alternate.getText().toString().trim().length() != 10) {
                        edt_mobile_no_alternate.setError("Please enter valid 10 digit mobile number");
                        return;
                    }

                    if (rb_by_sms_striker.isChecked()) {
                        smsId = "2";
                    } else if (rb_by_sms_91.isChecked()) {
                        smsId = "1";

                    }

                    new GetOtp(1).execute(alternateMobileNo, otpnumber, RegId, UserId, UserId, smsId);

                }
//                new GetOtp(1).execute(edt_mobile_no.getText().toString(), otpnumber, RegId, UserId, UserId);


                // new GetOtp(2).execute(edt_mobile_no.getText().toString(), RegId, UserId, "1");


            }
        });


    }



    private ActivityResultLauncher<Intent> faceDetectionResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result here
                        Intent data = result.getData();
                        if (data != null) {
                            // Do something with the data (e.g., extract extras)
                            String value = data.getStringExtra("key");
                        }

                        Log.d("FaceDetectionReceiver", "onReceive: " + data.getStringExtra("FaceData"));
                        String filePath = data.getStringExtra("FaceData");
                        try {

                            String filename = (int) (Math.random() * 99999 + 1) + "_PR";

                            String compressedFilePath = Utilities.compressImageNew(context, filePath, filename);
                            saveFileAI(compressedFilePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );




    public static class FaceDetectionReceiver extends BroadcastReceiver {
        public FaceDetectionReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("FaceDetectionReceiver", "onReceive: " + intent.getStringExtra("FaceData"));
            String filePath = intent.getStringExtra("FaceData");

            try {

                String filename = (int) (Math.random() * 99999 + 1) + "_PR";

                String compressedFilePath = Utilities.compressImageNew(context, filePath, filename);
//                new D2DPatientRegistration_Activity().saveFileAI(filePath);
                new HealthScreeningSamplecollectionConfirmatory_Activity().saveFileAI(compressedFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

//        void getFileFromName(Context context, String fileName) {
//            // Specify the file path of the image you want to access
//            File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            File specificImageFile = new File(downloadFolder, fileName);
//
//            // Now you can perform operations on the specific image file
//            if (specificImageFile.exists()) {
//                // The file exists, you can perform operations such as displaying it in an ImageView
//                // or any other operation you want to do with the file
//                Bitmap bitmap = BitmapFactory.decodeFile(specificImageFile.getPath());
//                imv_patient.setImageBitmap(bitmap);
//                Toast.makeText(context, "Got File", Toast.LENGTH_SHORT).show();
//            } else {
//                // The file does not exist or is inaccessible, handle the case accordingly
//            }
//        }
    }



    @SuppressLint("MissingPermission")
    public void saveFileAI(String filePath) {
        if (imageType == 0) {
            isPatientPhotoAvailable = true;
            String destinationFilename = filePath;
            letterPicBm = BitmapFactory.decodeFile(destinationFilename);
            String fName = System.currentTimeMillis() + "_BF.png";
            String outputFilePath = getExternalCacheDir() + fName;
            // Step 2: Compress the Bitmap and store it in a new file
            try {
                // Open a file output stream to save the compressed image
                FileOutputStream fos = new FileOutputStream(outputFilePath);

                // Compress the bitmap as JPEG with 80% quality
                letterPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                // Close the output stream
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imvLetter.setImageBitmap(letterPicBm);
            letterImagePath = outputFilePath;

        }
    }


//    @SuppressLint("MissingPermission")
//    public void saveFileAI(String filePath) {
//        if (imageType == 0) {
//            isPatientPhotoAvailable = true;
//
//            // Load bitmap from given file path
//            String destinationFilename = filePath;
//            letterPicBm = BitmapFactory.decodeFile(destinationFilename);
//
//            // ✅ Add DateTime stamp on image
//            letterPicBm = addDateTimeStamp(letterPicBm);
//
//            // Create output file path
//            String fName = System.currentTimeMillis() + "_BF.jpg"; // use JPG for compression
//            String outputFilePath = getExternalCacheDir() + "/" + fName;
//
//            // ✅ Save compressed image (JPEG, 80%)
//            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
//                letterPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // Show preview in ImageView
//            imvLetter.setImageBitmap(letterPicBm);
//
//            // Save final path
//            letterImagePath = outputFilePath;
//        }
//    }

    /**
     * Draws date & time on the bitmap
     */
//    private Bitmap addDateTimeStamp(Bitmap src) {
//        Bitmap mutableBitmap = src.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(mutableBitmap);
//
//        Paint paint = new Paint();
//        paint.setColor(Color.YELLOW); // bright, noticeable
//        paint.setTextSize(80);        // bigger text
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        paint.setAntiAlias(true);
//        paint.setShadowLayer(5f, 2f, 2f, Color.BLACK);
//
//        String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
//
//        // Position text at bottom-left
//        float x = 40;
//        float y = mutableBitmap.getHeight() - 80;
//        canvas.drawText(dateTime, x, y, paint);
//
//        return mutableBitmap;
//    }

    private void setUpToolbar() {


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(false);


        TextView titleTextView = new TextView(this);


        titleTextView.setText("Medicine Delivery Acknowledgment");
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        titleTextView.setTextColor(Color.WHITE);


        toolbar.addView(titleTextView);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//
        toolbar.setNavigationOnClickListener(v -> finish());

    }


////////////////////////////////////////////Signature Pad///////////////////////////////////////////////////////////////

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        signatureImageFile = new File(patientSignFolder,
                RegId + "_0_" + campId + "_SG.png");

        try {
            saveBitmapToJPG(signature, signatureImageFile);
            scanMediaFile(signatureImageFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        stream.close();
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

//////////////////////////////////////////MORPH FINGER PRINT////////////////////////////////////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();


    }


    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (fpSensorCap != null) {
//            fpSensorCap.cancelLiveAcquisition();
//            fpSensorCap.release();
//        }
//        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (tost != null)
//            tost.cancel();
    }


    private boolean isWorking = false;

    public void setButtonEnabled(boolean enabled) {
        isWorking = !enabled;
        iv_biomatric.setEnabled(enabled);
    }


    private boolean storeImage(String fileName, Bitmap image) {
        File file = new File(fingerPrintFolder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            return false;
        }
    }

    private boolean DumpFile(String fileName, byte[] buffer) {
        File file = new File(fingerPrintFolder, fileName);
        // String imgString = Base64.encodeToString(buffer, Base64.NO_WRAP);
        // Save your stream, don't forget to flush() it before closing it.
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(buffer);
            bos.flush();
            bos.close();

//            Utilities.showToastMessage("File write success to : " + file, context, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.showToastMessage("File write failed: " + e.toString(), context, true);
            return false;
        }
    }

//////////////////////////////////////////MANTRA FINGETPRINT////////////////////////////////////////////////////////////

    private long mLastAttTime = 0l;


    long mLastDttTime = 0l;


    private void WriteFileImage(String fingerImage, Bitmap bitmap) {
        File file = new File(fingerPrintFolder, fingerImage);
        try {
            isCaptured = true;
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            isCaptured = false;
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            isCaptured = true;
        }
    }

    private void WriteFileRaw(String filename, byte[] rawData) {
        File file = new File(fingerPrintFolder, filename);
        isCaptured = true;
        // String imgString = Base64.encodeToString(buffer, Base64.NO_WRAP);
        // Save your stream, don't forget to flush() it before closing it.
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(rawData);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
            isCaptured = false;
            Utilities.showToastMessage("File write failed: " + e.toString(), context, true);
        }
    }

    private void SetTextOnUIThread(String capture_success) {
//        Toast.makeText(context, capture_success, Toast.LENGTH_SHORT).show();
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
            param.add(new ParamsPojo("ReportDeliveredBY", params[4]));
            param.add(new ParamsPojo("SubOrgID", organizationId));
            param.add(new ParamsPojo("Option", params[5]));


            Log.d(TAG, "doInBackground: " + new Gson().toJson(param));
//            res = WebServiceCall.APICall(ApplicationConstants.GetOTPForMedicineDelivery_Org_Test, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetOTPForMedicineDelivery_Org_Option, ApplicationConstants.webservice_d2d, param);
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



///new change
//                        if (BuildConfig.isBeta){
//                            Utilities.showToastMessage("Otp sent" + " " + "on" + " " + mobilenumber + " " + "number successfully", context, true);
//
//                        }


                        if (rb_by_worker_number.isChecked()) {
                            Utilities.showToastMessage("Otp sent" + " " + "on" + " " + workerMobileNo + " " + "number successfully", context, true);

                        } else if (rb_by_alternate_number.isChecked()) {

                            Utilities.showToastMessage("Otp sent" + " " + "on" + " " + alternateMobileNo + " " + "number successfully", context, true);

                        }

//                        ArrayList<ResourcesListModel.OutputBean> resourceList = resourcesListModel.getOutput();
//                        if (resourceList.size() > 0) {
                        if (type == 1) {
//                                // verifyOtp(mno, resourceList.get(0).getDesgId());
                            verifyOtp(edt_mobile_no.getText().toString(), otpnumber);

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
            res = WebServiceCall.APICall(ApplicationConstants.VerifyOTPMedicineDelivery, ApplicationConstants.webservice_d2d, param);
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
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdNo()).putExtra("campId",beneficiaryDetails.getCAMPID()));


                        alertDialog.dismiss();

                        //  btn_register.setVisibility(View.VISIBLE);

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
                                    dialogInterface.dismiss();
//                                    finish();

                                    isOtpVrified = "1";
                                    btn_sendOtp.setText("Verified");
                                    btn_sendOtp.setEnabled(false);

                                    btnSubmitDeliveryAck.setVisibility(View.VISIBLE);


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

//    public class GenerateOtp extends AsyncTask<String, Void, String> {
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
//            res = WebServiceCall.APICall(ApplicationConstants.GenerateOTP, ApplicationConstants.webservice_d2d, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//
//            pd.dismiss();
//            String type = "", message = "", output = "";
//            try {
//                if (!result.equals("")) {
//
//                    GenerateOtpModel generateOtpModel = new Gson().fromJson(result, GenerateOtpModel.class);
//                    type = generateOtpModel.getStatus();
//                    message = generateOtpModel.getMessage();
//                    otpnumber = generateOtpModel.getOutput();
//                    if (type.equalsIgnoreCase("success")) {
//
//                        new GetOtp(1).execute(edt_mobile_no.getText().toString().trim(), otpnumber, RegId, UserId, "1", session.getSessionMobile(), DISTLGDCODE);
//                        ;
//
//
//                        //  otpnumber = generateOtpModel.getOutput();
//                        // ArrayList<GenerateOtpModel> resourceList = generateOtpModel.getOutput();
//
//                        {
//                            //  Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        //  Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                } else {
//                    // Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
//    }

    private class InsertBeneficiary extends AsyncTask<String, Void, String> {

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
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("RegdId", params[0]));
            param.add(new ParamsPojo("ackType", params[1]));
            param.add(new ParamsPojo("CreatedBy", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.Insert_PostCampAcknowledgement, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertSampleAccept", "onPostExecute: " + result);
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
                        builder.setMessage("Acknowledgement done successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                                //  ll_assigned.setVisibility(View.VISIBLE);


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


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (resultCode == RESULT_OK) {
//                if (requestCode == 10001) {
//                    String requiredValue = data.getStringExtra("key");
//                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
//                    edt_barcode1.setText(requiredValue);
//                } else if (requestCode == 10002) {
//                    String requiredValue = data.getStringExtra("key");
//                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
////                    edt_barcode2.setText(requiredValue);
//                } else if (requestCode == 10003) {
//                    String requiredValue = data.getStringExtra("key");
//                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
////                    edt_GlucoseBarcode.setText(requiredValue);
//
//                }
//            }
//
////            if (resultCode == RESULT_OK) {
////                if (requestCode == CAMERA_REQUEST) {
////                    CropImage.activity(photoURI).setGuidelines(CropImageView.Guidelines.ON).start(HealthDetailsForm_Activity.this);
////                }
////            }
////
////            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////                CropImage.ActivityResult result = CropImage.getActivityResult(data);
////                if (resultCode == RESULT_OK) {
////                    Uri resultUri = result.getUri();
////                    savefile(resultUri);
////                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////                    Exception error = result.getError();
////                }
////            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }



    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("FACE_DETECTION".equals(intent.getAction())) {
                String value = intent.getStringExtra("key");
                // Handle the received broadcast
                Log.d("FaceDetectionReceiver", "onReceive: " + intent.getStringExtra("FaceData"));
                String filePath = intent.getStringExtra("FaceData");

                try {

                    String filename = (int) (Math.random() * 99999 + 1) + "_PR";

                    String compressedFilePath = Utilities.compressImageNew(context, filePath, filename);
                    saveFileAI(compressedFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };



    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    // Use the cropped image URI.
                    String path = result.getUriFilePath(context, true);

//                    savefile(result.getUriContent());

                    if (imageType == 0) {
                        savefile(result.getUriContent());
                    } else if (imageType == 1) {
                        saveAckfile(result.getUriContent());
                    }else if (imageType == 2){
                        saveFileDelivery(result.getUriContent());
                    }

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
            if (requestCode == LETTER_CAMERA_REQUEST) {
//                CropImage.activity(letterUri).setGuidelines(CropImageView.Guidelines.ON).start(MedicineDeliveryAcknowledgement_Activity.this);


                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
//                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;
                CropImageContractOptions options = new CropImageContractOptions(letterUri, cropImageOptions);
                cropImageLauncher.launch(options);

            }
            if (requestCode == ACK_CAMERA_REQUEST) {
//                CropImage.activity(ackUri).setGuidelines(CropImageView.Guidelines.ON).start(MedicineDeliveryAcknowledgement_Activity.this);


                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
//                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                CropImageContractOptions options = new CropImageContractOptions(ackUri, cropImageOptions);
                cropImageLauncher.launch(options);

            }

            if (requestCode == DELIVERY_CHALLAN_REQUEST) {
//                CropImage.activity(deliveryUri).setGuidelines(CropImageView.Guidelines.ON).start(MedicineDeliveryAcknowledgement_Activity.this);


                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
//                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                CropImageContractOptions options = new CropImageContractOptions(deliveryUri, cropImageOptions);
                cropImageLauncher.launch(options);

            }

            if (requestCode == 10001) {
                String requiredValue = data.getStringExtra("key");
                Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                edt_barcode1.setText(requiredValue);

                btnSubmitDeliveryAck.setVisibility(View.VISIBLE);
            }


        }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                if (imageType == 0) {
//                    savefile(resultUri);
//                } else if (imageType == 1) {
//                    saveAckfile(resultUri);
//                }else if (imageType == 2){
//                    saveFileDelivery(resultUri);
//
//                }
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
    }

//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        String filename = (int) (Math.random() * 99999 + 1) + "_BF.png";
//        destinationFilename = patientPicsFolder + filename;
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
//        letterPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
//        imvLetter.setImageBitmap(letterPicBm);
//        letterImagePath = destinationFilename;
//
//
//    }



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
                String destinationFilename = "";
                String filename = (int) (Math.random() * 99999 + 1) + "_BF.png";
                destinationFilename = patientPicsFolder + filename;



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

                    letterPicBm = BitmapFactory.decodeFile(destinationFilename);
                    Utilities.getBitmapAsByteArray(letterPicBm);
                    destinationFilename = compressImage(destinationFilename);
                    letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imvLetter.setImageBitmap(letterPicBm);
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


                    letterImagePath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }


//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//
//        // Generate unique filename
//        String filename = (int) (Math.random() * 99999 + 1) + "_BF.jpg"; // use JPG for compression
//        String destinationFilename = patientPicsFolder + filename;
//
//        String sourceFilename = sourceuri.getPath();
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//
//        try {
//            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
//            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = bis.read(buf)) != -1) {
//                bos.write(buf, 0, len);
//            }
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
//        // Load bitmap from saved file
//        letterPicBm = BitmapFactory.decodeFile(destinationFilename);
//
//        // ✅ Add DateTime text on image
//        letterPicBm = addDateTimeStamp(letterPicBm);
//
//        // ✅ Resize to reduce file size (e.g., max 1024px width/height)
//        int maxSize = 1024;
//        int width = letterPicBm.getWidth();
//        int height = letterPicBm.getHeight();
//
//        if (width > maxSize || height > maxSize) {
//            float scale = Math.min((float) maxSize / width, (float) maxSize / height);
//            width = Math.round(width * scale);
//            height = Math.round(height * scale);
//            letterPicBm = Bitmap.createScaledBitmap(letterPicBm, width, height, true);
//        }
//
//        // ✅ Show thumbnail in ImageView (150px preview)
//        Bitmap thumbnail = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
//        imvLetter.setImageBitmap(thumbnail);
//
//        // ✅ Save modified bitmap again (JPEG, compressed to reduce KB size)
//        try (FileOutputStream out = new FileOutputStream(destinationFilename)) {
//            letterPicBm.compress(Bitmap.CompressFormat.JPEG, 85, out); // 85% = high quality + smaller file
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        letterImagePath = destinationFilename;
//    }

    /**
     * Draws date & time on the bitmap
     */
//    private Bitmap addDateTimeStamp(Bitmap src) {
//        Bitmap mutableBitmap = src.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(mutableBitmap);
//
//        Paint paint = new Paint();
//        paint.setColor(Color.YELLOW);
//        paint.setTextSize(60);
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        paint.setAntiAlias(true);
//        paint.setShadowLayer(5f, 2f, 2f, Color.BLACK);
//
//        // Date-Time
//        String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
//
//        // Format Lat/Lon
//        String latLong = String.format(Locale.getDefault(), "Lat: %.6f  Lon: %.6f", latitude, longitude);
//
//        // Draw on image
//        float x = 40;
//        float y = mutableBitmap.getHeight() - 80;
//
//        canvas.drawText(dateTime, x, y, paint);
//        canvas.drawText(latLong, x, y - 70, paint); // print above date-time
//
//        return mutableBitmap;
//    }


    private Bitmap addDateTimeStamp(Bitmap src) {
        Bitmap mutableBitmap = src.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setAntiAlias(true);
        paint.setShadowLayer(5f, 2f, 2f, Color.BLACK);

        // ✅ Date-Time
        String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // ✅ Address wrapping
        TextPaint textPaint = new TextPaint(paint);
        int textWidth = mutableBitmap.getWidth() - 80;

        StaticLayout staticLayout = new StaticLayout(
                address,
                textPaint,
                textWidth,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0,
                false
        );

        // ✅ Compute safe Y position so text doesn’t go outside
        float totalTextHeight = paint.getTextSize() + 20 + staticLayout.getHeight();
        float y = mutableBitmap.getHeight() - totalTextHeight - 40; // leave margin from bottom
        float x = 40;

        // Draw Date-Time
        canvas.drawText(dateTime, x, y, paint);

        // Draw Address below Date-Time
        canvas.save();
        canvas.translate(x, y + paint.getTextSize() + 20);
        staticLayout.draw(canvas);
        canvas.restore();

        return mutableBitmap;
    }


    /**
     * Draws date & time on the bitmap
     */
//    private Bitmap addDateTimeStamp(Bitmap src) {
//        Bitmap mutableBitmap = src.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(mutableBitmap);
//
//        Paint paint = new Paint();
//        paint.setColor(Color.WHITE);
//        paint.setTextSize(40);
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        paint.setAntiAlias(true);
//        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);
//
//        String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
//
//        // Position text at bottom-right
//        float x = 20;
//        float y = mutableBitmap.getHeight() - 40;
//        canvas.drawText(dateTime, x, y, paint);
//
//        return mutableBitmap;
//    }

//
//    public void saveAckfile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        String filename = (int) (Math.random() * 99999 + 1) + "_CF.png";
//        destinationFilename = patientPicsFolder + filename;
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
//        ackPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        ackPicBm = Bitmap.createScaledBitmap(ackPicBm, 150, 150, false);
//        imvDeliveryAck.setImageBitmap(ackPicBm);
//        ackImagePath = destinationFilename;
//
//
//    }



    private void saveAckfile(Uri sourceuri) {
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
                String destinationFilename = "";
                String filename = (int) (Math.random() * 99999 + 1) + "_CF.png";
                destinationFilename = patientPicsFolder + filename;


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

                    ackPicBm = BitmapFactory.decodeFile(destinationFilename);
                    Utilities.getBitmapAsByteArray(ackPicBm);
                    destinationFilename = compressImage(destinationFilename);
                    ackPicBm = Bitmap.createScaledBitmap(ackPicBm, 150, 150, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imvDeliveryAck.setImageBitmap(ackPicBm);
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


                    ackImagePath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }




//
//    public void saveFileDelivery(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        String filename = (int) (Math.random() * 99999 + 1) + "_DC.png";
//        destinationFilename = patientPicsFolder + filename;
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
//        deliveryChallanPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        deliveryChallanPicBm = Bitmap.createScaledBitmap(deliveryChallanPicBm, 150, 150, false);
//        imvLetter_delivery_challan.setImageBitmap(deliveryChallanPicBm);
//        deliveryPath = destinationFilename;
//
//
//    }
//



    private void saveFileDelivery(Uri sourceuri) {
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
                String destinationFilename = "";
                String filename = (int) (Math.random() * 99999 + 1) + "_DC.png";
                destinationFilename = patientPicsFolder + filename;



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

                    deliveryChallanPicBm = BitmapFactory.decodeFile(destinationFilename);
                    Utilities.getBitmapAsByteArray(deliveryChallanPicBm);
                    destinationFilename = compressImage(destinationFilename);
                    deliveryChallanPicBm = Bitmap.createScaledBitmap(deliveryChallanPicBm, 150, 150, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imvLetter_delivery_challan.setImageBitmap(deliveryChallanPicBm);
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


                    deliveryPath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

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
                multipart = new MultipartUtility(ApplicationConstants.CW_ReportDelivery_V1, "UTF-8");

                if (!params[0].isEmpty())
                    multipart.addFilePart("PhotoPath", new File(params[0]));

                multipart.addFormField("UserId", UserId);
                multipart.addFormField("RegdID", params[1]);
                multipart.addFormField("MOBNO", params[2]);
                multipart.addFormField("ReportDeliveredBY", BMobile == null ? userId : BMobile);
                multipart.addFormField("LGDCode", params[3]);

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
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdNo()));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));

                        Utilities.showAlertDialog(context, "Success", "Photo uploaded successfully", true, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
                                dialogInterface.dismiss();

                            }
                        });

                    } else {

                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class ReportDeliveryAck extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please Wait . . .");
            pd.setCancelable(false);
            pd.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String res = "";
            Log.d("Reg Params", Arrays.toString(params));
            try {
                MultipartUtility multipart;
                multipart = new MultipartUtility(ApplicationConstants.CW_MedicineDeliveryAckNew, "UTF-8");

                multipart.addFormField("RegdID", params[0]);
                multipart.addFormField("userId", UserId);
                multipart.addFormField("TreatmentID", beneficiaryDetails.getTreatmentID());
                multipart.addFormField("OTPVerified", isOtpVrified);
                multipart.addFormField("Delivarystatus", "Y");
                if (!params[1].isEmpty()) {
                    multipart.addFilePart("Consent_Form_PhotoPath", new File(params[1]));
                }
                if (!params[2].isEmpty()) {
                    multipart.addFilePart("Beneficiary_photoPath", new File(params[2]));
                }
                if (!params[3].isEmpty()) {
                    multipart.addFilePart("DeliveryChallan_PhotoPath", new File(params[3]));
                }
                multipart.addFormField("MedicalDelivaryID", beneficiaryDetails.getMedicalDelivaryID());
                multipart.addFormField("Dc_invoice_no", edt_barcode1.getText().toString().trim());
                multipart.addFormField("DeliveryStatusRemarkID", deliveryStatusId);
                multipart.addFormField("DeliveryRemarkID", remarkId);
                multipart.addFormField("Remark", edt_remarkOther.getText().toString().trim());


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
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdNo()).putExtra("campId", beneficiaryDetails.getCAMPID()));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("MedicineDeliveryActivity"));

                        if (deliveryStatusId.equals("1")) {
                            Utilities.showAlertDialog(context, "Success", "Medicines Delivered Successfully", true, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                }
                            });

                        } else if (deliveryStatusId.equals("2")) {
                            Utilities.showAlertDialog(context, "Success", "Medicines Not Delivered,Need To Re-Attempt", true, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                }
                            });

                        } else if (deliveryStatusId.equals("3")) {
                            Utilities.showAlertDialog(context, "Success", "Medicines Denied Successfully", true, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                }
                            });

                        }
//                        Utilities.showAlertDialog(context, "Success", " Medicines Delivered Successfully", true, "Okay", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//
//                            }
//                        });

                    } else {

                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public class GetDeliveryStatus extends AsyncTask<String, Void, String> {

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

//            param.add(new ParamsPojo("SubOrgId", params[0]));
//            param.add(new ParamsPojo("UserId", params[1]));
//            param.add(new ParamsPojo("DESGID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.BindMedicineDeliveryStatus, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<DeliveryStatusModel.Output> divisionList = new ArrayList<>();
                    DeliveryStatusModel pojoDetails = new Gson().fromJson(result, DeliveryStatusModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = pojoDetails.getOutput();
                        if (divisionList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDeliveryListDialog(divisionList);
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

    private void showDeliveryListDialog(final List<DeliveryStatusModel.Output> divisionList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Delivery Status");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < divisionList.size(); i++) {
            arrayAdapter.add(String.valueOf(divisionList.get(i).getStatusRemark()));
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
                tv_deliveryStatus.setText(divisionList.get(which).getStatusRemark());
                deliveryStatusId = String.valueOf(divisionList.get(which).getDeliveryStatusRemarkID());

                edt_remark.setText("");

                if (deliveryStatusId.equals("2")) {
                    btn_sendOtp.setVisibility(View.GONE);
                    rg_selection_for_mobile_number.setVisibility(View.GONE);
                    ll_main_vendor.setVisibility(View.GONE);
                    llDeliveryAck.setVisibility(View.GONE);
                    llHealthCard_delivery_challan.setVisibility(View.GONE);
                    tv_consentForm.setVisibility(View.GONE);
                    tv_delivery_challan.setVisibility(View.GONE);
                    btnSubmitPhoto.setVisibility(View.VISIBLE);
                    llmain_remark.setVisibility(View.VISIBLE);
                    btnSubmitDeliveryAck.setVisibility(View.GONE);

                    remarkId = "0";
                    edt_remarkOther.setText("");

                    letterImagePath = null;
                    ll_main_other_description.setVisibility(View.GONE);

                    imvLetter.setImageResource(R.drawable.icon_colorcamera);


                    String edtRemark = "Select Remark";
                    String beneficiaryPhoto = "Capture Beneficiary Photo";

                    tv_remark.setText(edtRemark);
                    tvHealthCard.setText(beneficiaryPhoto);

                    tvHealthCard.setTextColor(getResources().getColor(R.color.black));


                } else if (deliveryStatusId.equals("1")) {
                    btn_sendOtp.setVisibility(View.VISIBLE);
                    llDeliveryAck.setVisibility(View.VISIBLE);
                    llHealthCard_delivery_challan.setVisibility(View.VISIBLE);
                    tv_consentForm.setVisibility(View.VISIBLE);
                    tv_delivery_challan.setVisibility(View.VISIBLE);
                    btnSubmitPhoto.setVisibility(View.GONE);
                    llmain_remark.setVisibility(View.GONE);
                    ll_main_other_description.setVisibility(View.GONE);
                    edtNotAvailaible.setVisibility(View.GONE);

                    ll_main_vendor.setVisibility(View.VISIBLE);

                    remarkId = "0";
//                   btnSubmitDeliveryAck.setVisibility(View.VISIBLE);

                    letterImagePath = null;

                    imvLetter.setImageResource(R.drawable.icon_colorcamera);

                    edt_remark.setText("");
                    edt_remarkOther.setText("");


                    String beneficiaryPhoto = "Capture Beneficiary Photo <font color='red'>*</font>";

                    String edtRemark = "Select Remark <font color='red'>*</font>";

                    tv_remark.setText(Html.fromHtml(edtRemark), TextView.BufferType.SPANNABLE);
                    tvHealthCard.setText(Html.fromHtml(beneficiaryPhoto), TextView.BufferType.SPANNABLE);

                    tvHealthCard.setTextColor(getResources().getColor(R.color.black));


//                   tv_remark.setText(edtRemark);
//                   tvHealthCard.setText(beneficiaryPhoto);

                } else if (deliveryStatusId.equals("3")) {
                    btn_sendOtp.setVisibility(View.GONE);
                    llDeliveryAck.setVisibility(View.GONE);
                    llHealthCard_delivery_challan.setVisibility(View.GONE);
                    tv_consentForm.setVisibility(View.GONE);
                    tv_delivery_challan.setVisibility(View.GONE);
                    btnSubmitPhoto.setVisibility(View.VISIBLE);
                    llmain_remark.setVisibility(View.VISIBLE);
                    btnSubmitDeliveryAck.setVisibility(View.GONE);
                    ll_main_vendor.setVisibility(View.GONE);

                    edt_remarkOther.setText("");
                    letterImagePath = null;

                    imvLetter.setImageResource(R.drawable.icon_colorcamera);

                    ll_main_other_description.setVisibility(View.GONE);


                    String edtRemark = "Select Remark <font color='red'>*</font>";


                    String beneficiaryPhoto = "Capture Beneficiary Photo";

                    tv_remark.setText(Html.fromHtml(edtRemark), TextView.BufferType.SPANNABLE);
//                   tv_remark.setText(edtRemark);
                    tvHealthCard.setText(beneficiaryPhoto);
                    tvHealthCard.setTextColor(getResources().getColor(R.color.black));


                }
            }
        });
        builderSingle.show();
    }



    private void getFaceDetectionFlag() {
        final ProgressDialog progressDialog = new ProgressDialog(MedicineDeliveryAcknowledgement_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<FaceDetectionModel> call = apiService.getFaceDetectionData(UserId);
        call.enqueue(new retrofit2.Callback<FaceDetectionModel>() {
            @Override
            public void onResponse(Call<FaceDetectionModel> call, retrofit2.Response<FaceDetectionModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
//                        campBeneficiaryList = response.body().getOutput();
                        List<FaceDetectionModel.Output> invoiceList = response.body().getOutput();


                        FaceDetectionModel.Output o = invoiceList.get(0);

                        faceDetectionCompulsory = String.valueOf(o.getIsFaceDetetctionEnabled());

                        if (faceDetectionCompulsory.equals("0")) {
                            LL_skipFaceDetection.setVisibility(View.VISIBLE);
                        } else {
                            LL_skipFaceDetection.setVisibility(View.GONE);
                        }


                    } else {
                        Utilities.showAlertDialog(context, "Alert", message, false);

                    }
                } else {
                    Utilities.showAlertDialog(context, "Alert", "Data Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<FaceDetectionModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void getMobileNumber() {
        final ProgressDialog progressDialog = new ProgressDialog(MedicineDeliveryAcknowledgement_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<GetMobileNumberModel> call = apiService.getSelectNumber(beneficiaryDetails.getRegdNo());
        call.enqueue(new Callback<GetMobileNumberModel>() {
            @Override
            public void onResponse(Call<GetMobileNumberModel> call, Response<GetMobileNumberModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<GetMobileNumberModel.Output> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showSelectNumberListDialog(outputItems);
                        }
                    }
//                    if (status.equalsIgnoreCase("Success")) {
//                        campBeneficiaryList = response.body().getOutput();
//                        List<OutputItem> reportdatalist = response.body().getOutput();
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, reportdatalist, RejectedBeneficiaryListActivity.this);
////                        binding.rvReportlist.setAdapter(callListAdaptor);
//
//                    } else {
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, new ArrayList<>(), RejectedBeneficiaryListActivity.this);
////                        binding.rvReportlist.setAdapter(callListAdaptor);
//                    }
                }
            }




            @Override
            public void onFailure(Call<GetMobileNumberModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }



    private void showSelectNumberListDialog(final List<GetMobileNumberModel.Output> trenchList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MedicineDeliveryAcknowledgement_Activity.this);
        builderSingle.setTitle("Select Month");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MedicineDeliveryAcknowledgement_Activity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getMobileNo());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tv_send_OTP.setText(trenchList.get(which).getMobileNo());
                mobilenumber = trenchList.get(which).getMobileNo();



            }
        });

        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }



    public class GetRemark extends AsyncTask<String, Void, String> {

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

            param.add(new ParamsPojo("DeliveryStatusRemarkID", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.BindMedicineDeliveryRemark, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<DeliveryRemarkStatusModel.Output> divisionList = new ArrayList<>();
                    DeliveryRemarkStatusModel pojoDetails = new Gson().fromJson(result, DeliveryRemarkStatusModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = pojoDetails.getOutput();
                        if (divisionList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showRemarkListDialog(divisionList);
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

    private void showRemarkListDialog(final List<DeliveryRemarkStatusModel.Output> divisionList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Remark");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < divisionList.size(); i++) {
            arrayAdapter.add(String.valueOf(divisionList.get(i).getDeliveryRemark()));
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
                edt_remark.setText(divisionList.get(which).getDeliveryRemark());
                remarkId = String.valueOf(divisionList.get(which).getDeliveryRemarkID());

                if (remarkId.equals("0")) {
                    ll_main_other_description.setVisibility(View.VISIBLE);
                } else {
                    ll_main_other_description.setVisibility(View.GONE);

                }

                edt_remarkOther.setText("");

            }
        });
        builderSingle.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("FACE_DETECTION");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

}