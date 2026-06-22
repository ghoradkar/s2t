package com.myhindlab.abkat.activities.confirmatoryTest;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;
import static com.myhindlab.abkat.utilities.Utilities.isBarCodeValid;
import static com.myhindlab.abkat.utilities.Utilities.isBarCodeValidConfirmaatory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.graphics.Color;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.BarcodeScanner_Activity;
import com.myhindlab.abkat.activities.Login_Activity;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgement_Activity;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.GetMobileNumberModel;
import com.myhindlab.abkat.activities.couriermodule.CourierSendStep2_Activity;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.activities.payout.model.MonthModel;
import com.myhindlab.abkat.adapters.Dependent_Adapter_For_Search_Beneficiary_Confirmatory;
import com.myhindlab.abkat.adapters.LabListAdapter;
import com.myhindlab.abkat.adapters.Test_List_Adapter_Confirmatory;
import com.myhindlab.abkat.adapters.Test_List_Adapter_Tube_Count_Confirmatory;
import com.myhindlab.abkat.facedetection.FaceDetectionActivity;
import com.myhindlab.abkat.models.DependentForSearchBeneficiaryModel;
import com.myhindlab.abkat.models.FaceDetectionModel;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.models.Remark.CtRemarkResponse;
import com.myhindlab.abkat.models.Remark.OutputItem;
import com.myhindlab.abkat.models.SpecimenTypeListModel;
import com.myhindlab.abkat.models.couriermodule.CourierForwardedModel;
import com.myhindlab.abkat.models.couriermodule.CourierTubesModel;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;
import com.myhindlab.abkat.pojos.Lab_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthScreeningSamplecollectionConfirmatory_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private RadioGroup rg_selection_for_mobile_number;

    private Uri patientURI, ackUri, healthCardURI, renewalURI, hivConcernURI;

    private List<CourierTubesModel.OutputBean> tubeList;

    private final int ACK_CAMERA_REQUEST = 112;
    private File patientPicsFolder;
    private RadioButton rb_by_worker_number, rb_by_alternate_number, rb_by_sms_striker, rb_by_sms_91;

    private final int PATIENT_CAMERA_REQUEST =
            0, HEALTHCARD_CAMERA_REQUEST = 200, RENEWAL_CAMERA_REQUEST = 300, HIV_CONCERN_CAMERA_REQUEST = 400, IDENTITY_CAMERA_REQUEST = 500;

    private List<DependentForSearchBeneficiaryModel> dependentList;
    private RecyclerView rv_testdetails, rv_testdetailsCount;
    private String faceDetectionCompulsory = "0";
    private MaterialEditText edt_beneficiaryname, edt_sampleCount,

    edt_lab, edt_gender, edt_age, edt_sample_count, edt_barcode1, edt_enter_otp, edt_timer,

    edt_GlucoseBarcode, edt_barcode2, edt_height, edt_weight, edt_specimen, edt_beneficiary_mobile_number, edt_beneficiary_mobile_number_alternate,
            edt_sample_collection_date, edt_sample_collection_time;
    private ImageView imv_barcode1, imv_GlucoseBarcode, imv_barcode2, imvDeliveryAck, imv_patient,imvCall_select_number;

    private Bitmap patientPicBm = null, healthCardPicBm = null, ackPicBm = null, IdentityPicBm = null, renewalPicBm = null, hivConcernBm = null;

    private int imageType = 0;


    private boolean isPatientPhotoAvailable = false;
    private Button btn_register, btn_send_otp, btn_enter_otp;
    private PresentPatientList_Model patientDetails;
    private String userID, beneficiary, beni_no, reg_no, regNo, labCode = "", patientImagePath = "", ackImagePath = "", teamid, DESGID, appointmentConfirm, isSampleCollected, name, campId, healthScreentype, antigenResult, specimen_id, DISTLGDCODE, DISTLGDCODEBen, selectedLabID = "", selectedLabName = "";
    private int mYear1, mMonth1, mDay1;
    private String workerNumber, alternateNumber, isfaceDetection = "0", smsId = "2",mobilenumber;

    private Switch switch_button;


    private String subOrgId;

    private boolean isOtpVerify = false;
    private int flag = 1;
    String date, remark_Id = "1", fromdate = "0", Todate = "0", remark;
    private TextView tv_bloodsample, tv_OTP_Verification, tvBenficiaryName, tv_bloodsampleCount, tv_test, tv_filterremark,tv_send_OTP;
    private LinearLayout mainllTest, mainllTestTubeCount, ll_main_vendor, ll_bloodsample, ll_main_enter_otp, ll_main_mobile, LL_skipFaceDetection, ll_main_photo,ll_main_slectNumber;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_samplecollection_confirmatory);

        init();
        setUpToolbar();
        if (session.isHllUser()) {
            getHLLSessionData();
        } else {
            getSessionData();
        }
        setDefaults();
        setEventHandler();

    }

    private void init() {

        context = HealthScreeningSamplecollectionConfirmatory_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);


        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_sample_count = findViewById(R.id.edt_sample_count);
        edt_barcode1 = findViewById(R.id.edt_barcode1);
        edt_timer = findViewById(R.id.edt_timer);
        edt_enter_otp = findViewById(R.id.edt_enter_otp);
        edt_barcode2 = findViewById(R.id.edt_barcode2);
        rg_selection_for_mobile_number = findViewById(R.id.rg_selection_for_mobile_number);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        edt_sampleCount = findViewById(R.id.edt_sampleCount);
        edt_lab = findViewById(R.id.edt_lab);
        rv_testdetails = findViewById(R.id.rv_testdetails);
        rv_testdetails.setLayoutManager(new LinearLayoutManager(context));

        rv_testdetailsCount = findViewById(R.id.rv_testdetailsCount);
        rv_testdetailsCount.setLayoutManager(new LinearLayoutManager(context));

        imv_barcode1 = findViewById(R.id.imv_barcode1);
        imv_patient = findViewById(R.id.imv_patient);
        imvCall_select_number = findViewById(R.id.imvCall_select_number);
        tv_send_OTP = findViewById(R.id.tv_send_OTP);
        imvDeliveryAck = findViewById(R.id.imvDeliveryAck);
        switch_button = findViewById(R.id.switch_button);
        imv_GlucoseBarcode = findViewById(R.id.imv_GlucoseBarcode);
        imv_barcode2 = findViewById(R.id.imv_barcode2);
        edt_GlucoseBarcode = findViewById(R.id.edt_GlucoseBarcode);
        edt_beneficiary_mobile_number = findViewById(R.id.edt_beneficiary_mobile_number);
        rb_by_worker_number = findViewById(R.id.rb_by_worker_number);
        rb_by_sms_striker = findViewById(R.id.rb_by_sms_striker);
        rb_by_alternate_number = findViewById(R.id.rb_by_alternate_number);
        rb_by_sms_91 = findViewById(R.id.rb_by_sms_91);
        edt_beneficiary_mobile_number_alternate = findViewById(R.id.edt_beneficiary_mobile_number_alternate);

        btn_register = findViewById(R.id.btn_register);
        btn_send_otp = findViewById(R.id.btn_send_otp);
        LL_skipFaceDetection = findViewById(R.id.LL_skipFaceDetection);
        btn_enter_otp = findViewById(R.id.btn_enter_otp);
        edt_specimen = findViewById(R.id.edt_specimen);
        edt_sample_collection_date = findViewById(R.id.edt_sample_collection_date);
        edt_sample_collection_time = findViewById(R.id.edt_sample_collection_time);
        tv_bloodsample = findViewById(R.id.tv_bloodsample);
        tv_OTP_Verification = findViewById(R.id.tv_OTP_Verification);
        tvBenficiaryName = findViewById(R.id.tvBenficiaryName);
        tv_bloodsampleCount = findViewById(R.id.tv_bloodsampleCount);
        mainllTestTubeCount = findViewById(R.id.mainllTestTubeCount);
        ll_main_vendor = findViewById(R.id.ll_main_vendor);
        ll_main_mobile = findViewById(R.id.ll_main_mobile);
        ll_main_photo = findViewById(R.id.ll_main_photo);
        ll_main_slectNumber = findViewById(R.id.ll_main_slectNumber);
        mainllTest = findViewById(R.id.mainllTest);
        tv_test = findViewById(R.id.tv_test);
        // tv_from_date_new = findViewById(R.id.tv_from_date_new);
        // tv_to_date_new = findViewById(R.id.tv_to_date_new);
        tv_filterremark = findViewById(R.id.tv_filterremark);
        ll_bloodsample = findViewById(R.id.ll_bloodsample);
        ll_main_enter_otp = findViewById(R.id.ll_main_enter_otp);

//        edt_lab.setEnabled(false);


//        edt_beneficiaryname.setTextColor(Color.parseColor("#c0c0c0"));

//        edt_beneficiaryname.setBackgroundColor(Color.parseColor("#c0c0c0"));


        mainllTest.setVisibility(View.VISIBLE);

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        Calendar todayCal = Calendar.getInstance();
        todayCal.add(Calendar.DAY_OF_MONTH, -22);

        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -7);


//        tv_from_date.setText(Utilities.dfDate6.format(todayCal.getTime()));
//        CampDate = Utilities.dfDate6.format(new Date());
//        CampDateToDate = Utilities.dfDate6.format(new Date());
        // CampDate = Utilities.dfDate6.format(new Date());
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyy");
//        tv_to_date_new.setText(Utilities.dfDate4.format(new Date()));
        // tv_from_date_new.setText(Utilities.dfDate4.format(toCal.getTime()));


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


///////New Change
//            if (BuildConfig.isBeta){
//                insertVerifyOtp(edt_Otp.getText().toString().trim(), mobilenumber, alertDialog);
//
//            }else {
//
//
//                if (rb_by_worker_number.isChecked()) {
//                    insertVerifyOtp(edt_Otp.getText().toString().trim(), workerNumber, alertDialog);
//
//                } else if (rb_by_alternate_number.isChecked()) {
//                    insertVerifyOtp(edt_Otp.getText().toString().trim(), alternateNumber, alertDialog);
//                }
//
//            }


            if (rb_by_worker_number.isChecked()) {
                insertVerifyOtp(edt_Otp.getText().toString().trim(), workerNumber, alertDialog);

            } else if (rb_by_alternate_number.isChecked()) {
                insertVerifyOtp(edt_Otp.getText().toString().trim(), alternateNumber, alertDialog);
            }




        });

        resendOtpBtn.setOnClickListener(view -> {

            edt_Otp.setText("");


            if (rb_by_worker_number.isChecked()) {
                if (edt_beneficiary_mobile_number.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Worker Number Not Found", false);
                    return;
                }

            } else if (rb_by_alternate_number.isChecked()) {

                if (edt_beneficiary_mobile_number_alternate.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Alternate Number Not Found", false);
                    return;
                }

            }


            ////New Change
//            if (BuildConfig.isBeta){
//                if (tv_send_OTP.getText().toString().isEmpty()){
//                    Utilities.showAlertDialog(context, "Alert", "Please Select Mobile Number", false);
//                    return;
//                }
//
//
//
//                if (rb_by_sms_striker.isChecked()) {
//                    smsId = "2";
//                } else if (rb_by_sms_91.isChecked()) {
//                    smsId = "1";
//
//                }
//
//
//                getOTPForSampleCollection("2", mobilenumber);
//
//
//
//
//
//            }


            if (rb_by_worker_number.isChecked()) {


                if (rb_by_sms_striker.isChecked()) {
                    smsId = "2";
                } else if (rb_by_sms_91.isChecked()) {
                    smsId = "1";

                }

                getOTPForSampleCollection("2", workerNumber);

            } else if (rb_by_alternate_number.isChecked()) {


                if (rb_by_sms_striker.isChecked()) {
                    smsId = "2";
                } else if (rb_by_sms_91.isChecked()) {
                    smsId = "1";

                }

                getOTPForSampleCollection("2", alternateNumber);

            }



            resendOtpBtn.setVisibility(View.GONE);
            startResendTimer(resendOtpBtn, tv_timer);

        });

    }


    private void setDefaults() {

        getFaceDetectionFlag();


//        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
//        campId = getIntent().getStringExtra("campId");
//        healthScreentype = getIntent().getStringExtra("healthScreentype");
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 1);

        if (flag == 2) {

            beni_no = intent.getStringExtra("beni_no");
            reg_no = intent.getStringExtra("reg_no");
            ll_bloodsample.setVisibility(View.GONE);
            new GetTestInfo().execute(userID, "0", "0", "9", reg_no, beni_no);
            new GetTestInfoCount().execute(userID, "0", "0", "10", reg_no, beni_no);
        }


        if (flag == 1) {
            beneficiary = intent.getStringExtra("regdId");
            regNo = intent.getStringExtra("regNo");
            DISTLGDCODEBen = intent.getStringExtra("distCode");
            appointmentConfirm = intent.getStringExtra("appointmentConfirm");
            isSampleCollected = intent.getStringExtra("isSampleCollected");
            remark = intent.getStringExtra("remark");
            remark_Id = intent.getStringExtra("remarkId");


            if (remark_Id.equalsIgnoreCase("3")) {
                remark_Id = "1";
            }


//            mobileNo =
//            .getStringExtra("mobileNo");
            workerNumber = intent.getStringExtra("workerMobile");
            alternateNumber = intent.getStringExtra("alternateMobile");


            if (!TextUtils.isEmpty(alternateNumber)) {
                rb_by_alternate_number.setChecked(true);
            } else {
                rb_by_worker_number.setChecked(true);
                rb_by_alternate_number.setEnabled(false);
            }

//            if (alternateNumber==null||alternateNumber.equalsIgnoreCase("")){
//                edt_beneficiary_mobile_number_alternate.setVisibility(View.GONE);
//                rb_by_alternate_number.setVisibility(View.GONE);
//
//            }


            ll_bloodsample.setVisibility(View.VISIBLE);
            new GetTestInfo().execute(userID, "0", "0", "4", regNo, beneficiary);
            new GetTestInfoCount().execute(userID, "0", "0", "5", regNo, beneficiary);
            if (isSampleCollected.equalsIgnoreCase("Y") || DESGID.equalsIgnoreCase("84")) {
                btn_register.setVisibility(View.GONE);
                btn_send_otp.setVisibility(View.GONE);
                rg_selection_for_mobile_number.setVisibility(View.GONE);
                ll_main_vendor.setVisibility(View.GONE);

                tv_OTP_Verification.setVisibility(View.GONE);
                edt_timer.setText("Sample Already Collected");
                edt_timer.setVisibility(View.VISIBLE);

                edt_barcode1.setVisibility(View.GONE);
                edt_lab.setVisibility(View.GONE);
                edt_sampleCount.setVisibility(View.GONE);
                imv_barcode1.setVisibility(View.GONE);
                ll_main_mobile.setVisibility(View.GONE);
                ll_main_photo.setVisibility(View.GONE);
                tv_filterremark.setEnabled(false);
            }

            if (appointmentConfirm.equalsIgnoreCase("N")) {
                Utilities.showAlertDialog(context, "Alert", "Please confirm appointment date", false);

                edt_timer.setText("Appointment Confirmation Pending");
                edt_timer.setVisibility(View.VISIBLE);

                btn_register.setVisibility(View.GONE);
                btn_send_otp.setVisibility(View.GONE);
                rg_selection_for_mobile_number.setVisibility(View.GONE);
                ll_main_vendor.setVisibility(View.GONE);

                tv_OTP_Verification.setVisibility(View.GONE);
                edt_timer.setVisibility(View.VISIBLE);

                edt_barcode1.setVisibility(View.GONE);
                edt_lab.setVisibility(View.GONE);
                edt_sampleCount.setVisibility(View.GONE);
                imv_barcode1.setVisibility(View.GONE);
                ll_main_mobile.setVisibility(View.GONE);
                ll_main_photo.setVisibility(View.GONE);


                btn_send_otp.setVisibility(View.GONE);
                rg_selection_for_mobile_number.setVisibility(View.GONE);
                ll_main_vendor.setVisibility(View.GONE);
                tv_filterremark.setEnabled(false);

            }
            if (remark_Id.equalsIgnoreCase("6")) {


                tv_filterremark.setText(remark);
                tv_OTP_Verification.setVisibility(View.GONE);
                edt_timer.setVisibility(View.GONE);

                edt_barcode1.setVisibility(View.GONE);
                edt_lab.setVisibility(View.GONE);
                edt_sampleCount.setVisibility(View.GONE);
                imv_barcode1.setVisibility(View.GONE);
                ll_main_mobile.setVisibility(View.GONE);
                ll_main_photo.setVisibility(View.GONE);
                ll_main_vendor.setVisibility(View.GONE);
                btn_send_otp.setVisibility(View.GONE);


            }

        }


//        if (BuildConfig.isBeta){
//            btn_register.setVisibility(View.GONE);
//
//        }else {
//            btn_register.setVisibility(View.VISIBLE);
//
//        }




       /* if (appointmentConfirm.equalsIgnoreCase("N") || isSampleCollected.equalsIgnoreCase("Y")|| DESGID.equalsIgnoreCase("84")) {
            btn_register.setVisibility(View.GONE);

            if (appointmentConfirm.equalsIgnoreCase("N")){
                Utilities.showAlertDialog(context,"Alert","Please confirm appointment date",false);
            }
        }*/
        /*Intent intent=getIntent();
        beni_no = intent.getStringExtra("beni_no");
        reg_no = intent.getStringExtra("reg_no");
        flage = intent.getStringExtra("flage");
        if (flage.equals("1")){
            ll_bloodsample.setVisibility(View.GONE);
            new GetTestInfo().execute(userID, "0", "0", "4", reg_no, beni_no);
            new GetTestInfoCount().execute(userID, "0", "0", "5", reg_no, beni_no);

        }else {
            ll_bloodsample.setVisibility(View.VISIBLE);
            new GetTestInfo().execute(userID, "0", "0", "4", regNo, beneficiary);
            new GetTestInfoCount().execute(userID, "0", "0", "5", regNo, beneficiary);
        }*/


        edt_beneficiary_mobile_number.setText(workerNumber);
        edt_beneficiary_mobile_number_alternate.setText(alternateNumber);

        edt_timer.setTextColor(ContextCompat.getColor(this, R.color.green));
        edt_timer.setMetHintTextColor(ContextCompat.getColor(this, R.color.green));

    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                userID = json.getString("EmpCode");
                name = json.getString("name");
                subOrgId = json.getString("SubOrgId");
//                labCode = json.getString("LabCode");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getHLLSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);

                DESGID = json.getString("DESGID");
                userID = json.getString("EmpCode");
                name = json.getString("name");
                subOrgId = json.getString("SubOrgId");
                DISTLGDCODE = String.valueOf(json.getInt("DISTLGDCODE"));

//                Labcode = String.valueOf(json.getInt("Labcode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
//        edt_sample_collection_date.setOnClickListener(v -> {
//            Calendar c1 = Calendar.getInstance();
//            mYear1 = c1.get(Calendar.YEAR);
//            mMonth1 = c1.get(Calendar.MONTH);
//            mDay1 = c1.get(Calendar.DAY_OF_MONTH);
//            DatePickerDialog dpd = new DatePickerDialog(context,
//                    (view1, year, monthOfYear, dayOfMonth) -> {
//                        edt_sample_collection_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate2, dayOfMonth, monthOfYear + 1, year));
//                        edt_sample_collection_time.setText("");
//                        mYear1 = year;
//                        mMonth1 = monthOfYear;
//                        mDay1 = dayOfMonth;
//                    }, mYear1, mMonth1, mDay1);
//            Calendar c0 = Calendar.getInstance();
//            c0.set(mYear1, mMonth1, mDay1);
//            try {
//                dpd.getDatePicker().setCalendarViewShown(false);
//                c1.add(Calendar.DATE, -2);
//                dpd.getDatePicker().setMinDate(c1.getTimeInMillis());
//                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            dpd.show();
//        });


        rb_by_worker_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_by_alternate_number.setChecked(false);

            }
        });

        tv_send_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMobileNumber();
            }
        });

        rb_by_alternate_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rb_by_worker_number.setChecked(false);
            }
        });


        btn_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ///New Change

//                if (BuildConfig.isBeta){
//                    if (tv_send_OTP.getText().toString().isEmpty()) {
//                        Utilities.showAlertDialog(context, "Alert", "Please Select Mobile Number", false);
//                        return;
//                    }
//
//
//                    getOTPForSampleCollection("1", mobilenumber);
//
//                }






                if (rb_by_worker_number.isChecked()) {

                    if (edt_beneficiary_mobile_number.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Worker Number Not Found", false);
                        return;
                    }


                    if (rb_by_sms_striker.isChecked()) {
                        smsId = "2";
                    } else if (rb_by_sms_91.isChecked()) {
                        smsId = "1";

                    }


                    getOTPForSampleCollection("1", workerNumber);

                } else if (rb_by_alternate_number.isChecked()) {


                    if (edt_beneficiary_mobile_number_alternate.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Alternate Number Not Found", false);
                        return;
                    }


                    if (rb_by_sms_striker.isChecked()) {
                        smsId = "2";
                    } else if (rb_by_sms_91.isChecked()) {
                        smsId = "1";
                    }

                    getOTPForSampleCollection("1", alternateNumber);


                }


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


        edt_lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetLab().execute(DISTLGDCODE);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                    return;
                }
            }
        });


//        btn_enter_otp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (edt_enter_otp.getText().toString().isEmpty()) {
//                    Utilities.showAlertDialog(context, "Alert", "Please enter otp", false);
//                    return;
//                }
//
//                if (edt_enter_otp.getText().toString().length() < 5) {
//                    Utilities.showAlertDialog(context, "Alert", "Please enter valid otp", false);
//                    return;
//                }
//
//
////                insertVerifyOtp(edt_enter_otp.getText().toString().trim(), mobileNo);
//            }
//        });

        imv_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageType = 0;


//                if (isfaceDetection.equals("0")) {
//                    try {
//                        Intent intent = new Intent(context, FaceDetectionActivity.class);
//                        intent.putExtra("campId", campId);
//                        intent.putExtra("userID", String.valueOf(session.getUserDetailsJson().getEmpCode()));
//                        faceDetectionResult.launch(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Utilities.showAlertDialog(context, "Face Recognition App not installed",
//                                "Please install Face Recognition App first \n" + e.getMessage(), false);
//                    }
//
//                }else if (isfaceDetection.equals("1")){
//
//
//                }


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


        tv_bloodsample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainllTest.setVisibility(mainllTest.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (mainllTest.getVisibility() == View.VISIBLE) {
                    tv_bloodsample.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tv_bloodsample.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });

        tv_bloodsampleCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainllTestTubeCount.setVisibility(mainllTestTubeCount.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (mainllTestTubeCount.getVisibility() == View.VISIBLE) {
                    tv_bloodsampleCount.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tv_bloodsampleCount.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });

//        edt_sample_collection_time.setOnClickListener(v -> {
//            try {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//                    @SuppressLint("DefaultLocale")
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        String selTime = String.format("%02d",
//                                Integer.valueOf(selectedHour)) + ":" + String.format("%02d",
//                                Integer.valueOf(selectedMinute));
//                        if ((edt_sample_collection_date.getText().toString().trim()).equals(date)) {
//                            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
//                            try {
//                                Date ten = parser.parse(+hour + ":" + minute);
//                                Date eighteen = parser.parse(+selectedHour + ":" + selectedMinute);
//                                if (eighteen != null && ten != null)
//                                    if (eighteen.after(ten)) {
//                                        Utilities.showAlertDialog(context, "Alert", "You can not select Future Time.", false);
//                                        return;
//                                    }
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        edt_sample_collection_time.setText(selTime);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

//        edt_specimen.setOnClickListener(v -> {
//            new GetSpecimenType().execute();
//        });

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


        imv_GlucoseBarcode.setOnClickListener(new View.OnClickListener() {
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
                startActivityForResult(i, 10003);
            }
        });

        imv_barcode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
                    return;
                }

                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10002);
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
        /*tv_from_date_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv_from_date_new.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                //  fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

//                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate6, dayOfMonth, monthOfYear + 1, year);

                                tv_to_date_new.setText("");

                            }

                        }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                //  c.add(Calendar.DAY_OF_MONTH, 15);


                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

//                    dpd1.getDatePicker().setCalendarViewShown(false);
//                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
//                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });
        tv_to_date_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_from_date_new.getText().toString().equals(""))
                    Utilities.showMessageString("Please Select From Date", context);
                else {

                    DatePickerDialog dpd1 = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    tv_to_date_new.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));


                                    //  CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


//                                    tv_teamNumber.setText("");
//                                    tv_district.setText("");
//                                    tv_lab.setText("");

                                    if (Utilities.isNetworkAvailable(context)) {

//                                        new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE,TALLGDCODE,"411025", "1");

                                    }

//                                    if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29")||(DESGID.equalsIgnoreCase("104")||(DESGID.equalsIgnoreCase("162")||DESGID.equalsIgnoreCase("78")||DESGID.equalsIgnoreCase("77")||DESGID.equalsIgnoreCase("128")||DESGID.equalsIgnoreCase("131"))))) {
//
//
//                                    } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35")||DESGID.equalsIgnoreCase("86"))) {
//                                        llTeamNumber.setVisibility(View.VISIBLE);
//                                    }


                                }

                            }, mYear, mMonth, mDay);

                    Calendar c = Calendar.getInstance();
//                    try {
//                        c.setTime(Utilities.dfDate4.parse(CampDate));
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                    c.add(Calendar.DAY_OF_MONTH, 14);

                    try {
                        dpd1.getDatePicker().setCalendarViewShown(false);
                        dpd1.getDatePicker().setMaxDate(c.getTimeInMillis());

                        // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dpd1.show();
                }
            }
        });*/

        tv_filterremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetRemarklist().execute("4");
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });
    }

    public class GetRemarklist extends AsyncTask<String, Void, String> {
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
            param.add(new ParamsPojo("Type", "4"));
            res = WebServiceCall.APICall(ApplicationConstants.Get_T2T_CT_AssignmentRemarks, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    CtRemarkResponse ctRemarkResponse = new Gson().fromJson(result, CtRemarkResponse.class);
                    type = ctRemarkResponse.getStatus();
                    message = ctRemarkResponse.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<OutputItem> camptypelist = ctRemarkResponse.getOutput();
                        if (camptypelist.size() > 0) {

                            // camptypelist.add(0, new TalukaModel().new Output("ALL", 0));

                            showTalukaDialogue(camptypelist);

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
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

        private void showTalukaDialogue(final List<OutputItem> talukalist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Remark");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < talukalist.size(); i++) {
                arrayAdapter.add(String.valueOf(talukalist.get(i).getAssignmentRemarks()));
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
                    tv_filterremark.setText(talukalist.get(which).getAssignmentRemarks());
                    remark_Id = String.valueOf(talukalist.get(which).getArId());
                    if (remark_Id.equals("1")) {
                        edt_barcode1.setVisibility(View.VISIBLE);
                        edt_lab.setVisibility(View.VISIBLE);
                        edt_sampleCount.setVisibility(View.VISIBLE);
                        imv_barcode1.setVisibility(View.VISIBLE);

                        if (BuildConfig.isBeta) {
                            ll_main_mobile.setVisibility(View.VISIBLE);
                            ll_main_photo.setVisibility(View.VISIBLE);
                            tv_OTP_Verification.setVisibility(View.VISIBLE);
                            tv_OTP_Verification.setVisibility(View.VISIBLE);
                            ll_main_vendor.setVisibility(View.VISIBLE);
                            btn_send_otp.setVisibility(View.VISIBLE);

                        } else {
//                            ll_main_mobile.setVisibility(View.GONE);
//                            ll_main_photo.setVisibility(View.GONE);
//                            tv_OTP_Verification.setVisibility(View.GONE);
//                            tv_OTP_Verification.setVisibility(View.GONE);

                            ll_main_mobile.setVisibility(View.VISIBLE);
                            ll_main_photo.setVisibility(View.VISIBLE);
                            tv_OTP_Verification.setVisibility(View.VISIBLE);
                            tv_OTP_Verification.setVisibility(View.VISIBLE);
                            ll_main_vendor.setVisibility(View.VISIBLE);
                            btn_send_otp.setVisibility(View.VISIBLE);

                        }


                    } else {
                        edt_sampleCount.setText("");
                        edt_barcode1.setText("");
                        edt_lab.setText("");
                        labCode = "";
                        edt_barcode1.setVisibility(View.GONE);
                        edt_lab.setVisibility(View.GONE);
                        edt_sampleCount.setVisibility(View.GONE);
                        imv_barcode1.setVisibility(View.GONE);
                        ll_main_mobile.setVisibility(View.GONE);
                        ll_main_photo.setVisibility(View.GONE);
                        tv_OTP_Verification.setVisibility(View.GONE);
                        ll_main_vendor.setVisibility(View.GONE);
                        btn_send_otp.setVisibility(View.GONE);


                        edt_timer.setVisibility(View.GONE);

                        patientImagePath = "";

                        imv_patient.setImageResource(R.drawable.icon_patientcamera);

                    }


                    if (remark_Id.equals("4")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_alertred);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage("This beneficiary will not be available for CT screening.\n" +
                                "Please verify before proceeding with the submission.");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

//                                new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");

                                dialog.dismiss();


                            }

//                                finish();

                            //  ll_assigned.setVisibility(View.VISIBLE);


                        });
                        builder.show();

                    }
                    if (remark_Id.equals("6")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_alertred);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage("This beneficiary can be re-attempted for CT screening.");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

//                                new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");

                                dialog.dismiss();


                            }

//                                finish();

                            //  ll_assigned.setVisibility(View.VISIBLE);


                        });
                        builder.show();

                    }


//                    new ConfirmatoryTestActivity.GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","2","0","0");

                    //  refreshCalendar();


                }
            });
            builderSingle.show();

        }

    }


    private class GetSpecimenType extends AsyncTask<String, Void, String> {

        private final ProgressDialog pd = new ProgressDialog(context);

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
            res = WebServiceCall.APICall(ApplicationConstants.GetSpecimenType, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    SpecimenTypeListModel pojoDetails = new Gson().fromJson(result, SpecimenTypeListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        List<SpecimenTypeListModel.OutputBean> specimenList = pojoDetails.getOutput();
                        if (specimenList.size() > 0) {
                            showSpecimenList(specimenList);
                        }
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

    private void showSpecimenList(List<SpecimenTypeListModel.OutputBean> specimenList) {

        final android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(context);

        builderSingle.setTitle("Select Specimen Type");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (SpecimenTypeListModel.OutputBean subTrenchModel : specimenList) {
            arrayAdapter.add(subTrenchModel.getSPECTYPE());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            specimen_id = specimenList.get(which).getSPECTYPEID();
            edt_specimen.setText(specimenList.get(which).getSPECTYPE());
        });
        builderSingle.show();
    }

    private void submitData() {


        if (remark_Id.equals("1")) {
            if (!isOtpVerify) {
                Utilities.showToastMessage("Please Verify OTP", context, false);
                return;
            }

        }


        int count = 0;

        JsonArray SampleDetailsJsonArray = new JsonArray();
        for (int i = 0; i < tubeList.size(); i++) {

            TubeCountAdapterNew.MyViewHolder myViewHolder = (TubeCountAdapterNew.MyViewHolder) rv_testdetailsCount.findViewHolderForAdapterPosition(i);

            if (myViewHolder.edt_tubeCount.getText().toString().equals("")) {
                myViewHolder.edt_tubeCount.setError("Enter tube count");
                return;
            }

            if (!tubeList.get(i).getCount().isEmpty()) {
                count = count + Integer.parseInt(tubeList.get(i).getCount());
            }

//                    count = count + Integer.parseInt(tubeList.get(i).getTotalCount());


            JsonObject tubeDetails = new JsonObject();
            tubeDetails.addProperty("TubeId", tubeList.get(i).getTubeId());
            tubeDetails.addProperty("TotalCount", tubeList.get(i).getCount().isEmpty() ? "0" : tubeList.get(i).getCount());
            tubeDetails.addProperty("CreatedBy", userID);
            SampleDetailsJsonArray.add(tubeDetails);
        }
        JsonObject sampleDetails = new JsonObject();
        sampleDetails.add("input", SampleDetailsJsonArray);


        if (remark_Id.equals("1") && edt_lab.getText().toString().trim().isEmpty()) {
            edt_lab.setError("Please select lab");
            return;
        }


        if (remark_Id.equals("1") && edt_sampleCount.getText().toString().trim().isEmpty()) {
            edt_sampleCount.setError("Please enter Sample count.");
            return;
        }

        if (remark_Id.equals("1") && !isBarCodeValidConfirmaatory(edt_barcode1.getText().toString().trim())) {
            edt_barcode1.setError("Invalid Barcode... Please try with another barcode");
            return;
        }


        if (BuildConfig.isBeta) {

            if (remark_Id.equals("1")) {
                if (!isPatientPhotoAvailable) {
                    if (patientImagePath.equals("")) {
                        Utilities.showToastMessage("Please click patient photo", context, false);
                        return;
                    }
                }

                if (ackImagePath.equals("")) {
                    Utilities.showToastMessage("Please capture consent form", context, false);
                    return;
                }


            }


        } else {

            if (remark_Id.equals("1")) {
                if (!isPatientPhotoAvailable) {
                    if (patientImagePath.equals("")) {
                        Utilities.showToastMessage("Please click patient photo", context, false);
                        return;
                    }
                }

                if (ackImagePath.equals("")) {
                    Utilities.showToastMessage("Please capture consent form", context, false);
                    return;
                }


            }


        }


//        JsonArray campArr = new JsonArray();
//        if (dependentList != null) {
//            for (DependentForSearchBeneficiaryModel o : dependentList) {
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("TubeName", o.getTubName());
//                jsonObject.addProperty("TubeCount", o.getCount());
//                campArr.add(jsonObject);
//
//            }
//        }

        int t = 0;
        if (tubeList != null) {
            for (CourierTubesModel.OutputBean o : tubeList) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("TubeName", o.getTubName());
                jsonObject.addProperty("TubeCount", o.getCount());

                t = t + Integer.valueOf(o.getCount());

            }
        }
        if (remark_Id.equals("1") && !edt_sampleCount.getText().toString().equalsIgnoreCase(String.valueOf(t))) {
            Utilities.showAlertDialog(context, "Alert", "Entered sample count is not match with addition of individual tubes", false);
            return;
        }


        String Labcode = "0";
        String sample_count = "0";
          if (remark_Id.equals("1")) {
            Labcode = labCode;
            sample_count = edt_sampleCount.getText().toString().trim();
        } else {
            Labcode = "0";
            sample_count = "0";
        }


        if (BuildConfig.isBeta) {

            if (remark_Id.equals("1")) {
                new InsertCW_PatientBarcodeDetailsNew().execute(
                        beneficiary,
                        regNo,
                        edt_barcode1.getText().toString().trim(),
                        Labcode,
                        sample_count,
                        userID,
                        remark_Id,
                        patientImagePath,
                        ackImagePath
                );

            } else {

                new InsertCW_PatientBarcodeDetails().execute(
                        beneficiary,
                        regNo,
                        edt_barcode1.getText().toString().trim(),
                        Labcode,
                        sample_count,
                        userID,
                        SampleDetailsJsonArray.toString()
                );

            }


        } else {


            if (remark_Id.equals("1")) {
                new InsertCW_PatientBarcodeDetailsNew().execute(
                        beneficiary,
                        regNo,
                        edt_barcode1.getText().toString().trim(),
                        Labcode,
                        sample_count,
                        userID,
                        remark_Id,
                        patientImagePath,
                        ackImagePath
                );

            } else {

                new InsertCW_PatientBarcodeDetails().execute(
                        beneficiary,
                        regNo,
                        edt_barcode1.getText().toString().trim(),
                        Labcode,
                        sample_count,
                        userID,
                        SampleDetailsJsonArray.toString()
                );

            }

//            new InsertCW_PatientBarcodeDetails().execute(
//                    beneficiary,
//                    regNo,
//                    edt_barcode1.getText().toString().trim(),
//                    Labcode,
//                    sample_count,
//                    userID,
//                    SampleDetailsJsonArray.toString()
//            );

        }

    }

    private class InsertCW_PatientBarcodeDetails extends AsyncTask<String, Void, String> {
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

            Log.d("barcode params", Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Regdid", params[0]));
            param.add(new ParamsPojo("Regdno", params[1]));
            param.add(new ParamsPojo("Barcode", params[2]));
            param.add(new ParamsPojo("Labcode", params[3]));
            param.add(new ParamsPojo("Samplecount", params[4]));
            param.add(new ParamsPojo("USERID", params[5]));
            param.add(new ParamsPojo("ArId", remark_Id));
            //.add(new ParamsPojo("Type","1" ));
            res = WebServiceCall.APICall(ApplicationConstants.InsertT2TBarcodeCollectionDetails_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("PatientBarcodeIns", result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
//                    String ExceptionValue = obj.getString("ExceptionValue");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
//
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AppointmentConfirmationConfirmatoryActivity").putExtra("regNo", regNo));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Details Submitted Successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();

                    } else {
//                        if (message.equalsIgnoreCase("1")){
//                            Utilities.showAlertDialog(context, status, "Barcode is already exists. You can try with another barcode", false);
//                        }else if (message.equalsIgnoreCase("2")){
//                            Utilities.showAlertDialog(context, status, "\"You're not allowed to collect sample after 10 P.M. You can try after 12 A.M. OR\n" +
//                                    "It is mandatory to collect sample in front of beneficiary. You can collect sample in morning\"", false);
//                        }
                        Utilities.showAlertDialog(context, status, message, false);

                    }


                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    private class InsertCW_PatientBarcodeDetailsNew extends AsyncTask<String, Integer, String> {

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
                multipart = new MultipartUtility(ApplicationConstants.CW_SampleCollectioCT_Consent, "UTF-8");

                multipart.addFormField("Regdid", params[0]);
                multipart.addFormField("Regdno", params[1]);
                multipart.addFormField("Barcode", params[2]);
                multipart.addFormField("Labcode", params[3]);
                multipart.addFormField("Samplecount", params[4]);
                multipart.addFormField("USERID", params[5]);
                multipart.addFormField("ArId", params[6]);
                if (!params[7].isEmpty()) {
                    multipart.addFilePart("BarcodeImagePath", new File(params[7]));
                }
                if (!params[8].isEmpty()) {
                    multipart.addFilePart("ConsentPath", new File(params[8]));
                }

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
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AppointmentConfirmationConfirmatoryActivity").putExtra("regNo", regNo));


                        Utilities.showAlertDialog(context, "Success", "Details Submitted Successfully", true, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
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


    private class D2DInsertCW_PatientBarcodeDetails extends AsyncTask<String, Void, String> {

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

            Log.d("barcode params", Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("RegdId", params[0]));
            param.add(new ParamsPojo("SiteId", params[1]));
            param.add(new ParamsPojo("CampId", params[2]));
            param.add(new ParamsPojo("SampleCount", params[3]));
            param.add(new ParamsPojo("Barcode1", params[4]));
            param.add(new ParamsPojo("Barcode2", params[5]));
            param.add(new ParamsPojo("CreatedBy", params[6]));
            param.add(new ParamsPojo("Type", params[7]));
            param.add(new ParamsPojo("sampledate", params[8]));
            param.add(new ParamsPojo("sampletime", params[9]));
            param.add(new ParamsPojo("SPECTYPEID", params[10]));
            param.add(new ParamsPojo("Labcode", params[11]));

            // res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_New_D2D, ApplicationConstants.webservice, param);
            res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_New_D2D_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("PatientBarcodeIns", result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        if (antigenResult.equals("1"))
                            new SubmitRTPCRData().execute(String.valueOf(patientDetails.getRegdId()), String.valueOf(patientDetails.getSiteId()), campId, userID);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Sample collection details submitted Successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();

                    } else {

//                        if (message.equalsIgnoreCase("1")){
//                            Utilities.showAlertDialog(context, status, "Barcode is already exists. You can try with another barcode", false);
//                        }else if (message.equalsIgnoreCase("2")){
//                            Utilities.showAlertDialog(context, status, "\"You're not allowed to collect sample after 10 P.M. You can try after 12 A.M. OR\n" +
//                                    "It is mandatory to collect sample in front of beneficiary. You can collect sample in morning\"", false);
//                        }

                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 10001) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                    edt_barcode1.setText(requiredValue);
                } else if (requestCode == 10002) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                    edt_barcode2.setText(requiredValue);
                } else if (requestCode == 10003) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                    edt_GlucoseBarcode.setText(requiredValue);

                } else if (requestCode == PATIENT_CAMERA_REQUEST) {
//                    CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(HealthScreeningSamplecollectionConfirmatory_Activity.this);

                    CropImageOptions cropImageOptions = new CropImageOptions();
                    cropImageOptions.guidelines = CropImageView.Guidelines.ON;
//                    cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                    CropImageContractOptions options = new CropImageContractOptions(patientURI, cropImageOptions);
                    cropImageLauncher.launch(options);


                } else if (requestCode == ACK_CAMERA_REQUEST) {
//                    CropImage.activity(ackUri).setGuidelines(CropImageView.Guidelines.ON).start(HealthScreeningSamplecollectionConfirmatory_Activity.this);

                    CropImageOptions cropImageOptions = new CropImageOptions();
                    cropImageOptions.guidelines = CropImageView.Guidelines.ON;
//                    cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                    CropImageContractOptions options = new CropImageContractOptions(ackUri, cropImageOptions);
                    cropImageLauncher.launch(options);


                }

            }


//            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == RESULT_OK) {
//                    Uri resultUri = result.getUri();
////                    savefile(resultUri);
//
//                    if (imageType == 0) {
//                        savefile(resultUri);
//                    } else if (imageType == 1) {
//                        saveAckfile(resultUri);
//                    }
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
//                }
//            }

//            if (resultCode == RESULT_OK) {
//                if (requestCode == PATIENT_CAMERA_REQUEST) {
//                    CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(HealthScreeningSamplecollectionConfirmatory_Activity.this);
//                }
////            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
////                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity.this);
////            }
////            if (requestCode == RENEWAL_CAMERA_REQUEST) {
////                CropImage.activity(renewalURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity.this);
////            }
////            if (requestCode == HIV_CONCERN_CAMERA_REQUEST) {
////                CropImage.activity(hivConcernURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity.this);
////            }
//////            if (requestCode == 10001) {
////                IsAdharDataVerify = "0";
////                String requiredValue = data.getStringExtra("key");
////                Document doc = convertStringToDocument(requiredValue);
////
////                if (doc != null) {
////                    IsAdharDataVerify = "1";
////                    parseXmlDocument(doc);
////                } else {
////                    IsAdharDataVerify = "0";
////                    Utilities.showMessageString("Please try again", context);
////                }
////            }
//
//            }

//            if (resultCode == RESULT_OK) {
//                if (requestCode == CAMERA_REQUEST) {
//                    CropImage.activity(photoURI).setGuidelines(CropImageView.Guidelines.ON).start(HealthDetailsForm_Activity.this);
//                }
//            }
//
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

    public class SubmitRTPCRData extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("TestId", "14"));

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

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sample Collection");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public class GetLab extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetT2TLabDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // lab_List = new ArrayList<>();

                    Lab_Pojo pojoDetails = new Gson().fromJson(result, Lab_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        ArrayList<Lab_OutPut_Pojo> labList = pojoDetails.getOutput();
                        if (labList.size() > 0) {
                            showLabListDialog(labList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
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

    private void showLabListDialog(final ArrayList<Lab_OutPut_Pojo> lab_List) {


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);


        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        edt_search.setVisibility(View.GONE);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        LabListAdapter labListAdapter = new LabListAdapter(lab_List);
        rvList.setAdapter(labListAdapter);

        ArrayList<Lab_OutPut_Pojo> filteredList = new ArrayList<>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : lab_List) {
            arrayAdapter.add(subTrenchModel.getLabName());
        }

//        edt_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence query, int start, int before, int count) {
//
//                if (filteredList != null)
//                    filteredList.clear();
//
//                if (lab_List != null) {
//                    if (edt_search.getText().toString().equals("")) {
//                        filteredList.addAll(lab_List);
//                        rvList.setAdapter(new LabListAdapter(filteredList));
//
//                    } else {
//                        if (lab_List.size() > 0) {
//                            for (Lab_OutPut_Pojo pojo : lab_List) {
//                                String siteDetails = pojo.getLabName();
//                                if (siteDetails != null && siteDetails.toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
//                                    filteredList.add(pojo);
//                                }
//                            }
//
//                            if (filteredList.size() == 0) {
//                                filteredList.addAll(lab_List);
//                                rvList.setAdapter(new LabListAdapter(filteredList));
//                            } else {
//                                rvList.setAdapter(new LabListAdapter(filteredList));
//
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                labCode = lab_List.get(which).getLabCode();
//                selectedLabName = lab_List.get(which).getLabName();
//                edt_lab.setText(selectedLabName);
//                dialog.dismiss();
//            }
//        });

        AlertDialog alertDialog = builderSingle.create();

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        rvList.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                labCode = lab_List.get(position).getLabCode();
                selectedLabName = lab_List.get(position).getLabName();
                edt_lab.setText(selectedLabName);
                alertDialog.dismiss();

            }
        }));
        alertDialog.show();
    }

    public class GetTestInfo extends AsyncTask<String, Void, String> {
        //        String fromdate_new=tv_from_date_new.getText().toString();
//        String todate_new=tv_to_date_new.getText().toString();
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
            param.add(new ParamsPojo("USERID", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("AREA", params[2]));
            param.add(new ParamsPojo("Type", params[3]));
            param.add(new ParamsPojo("REDNO", params[4]));
            param.add(new ParamsPojo("Regdid", params[5]));
            param.add(new ParamsPojo("FROMDATE", "2024/01/01"));
            param.add(new ParamsPojo("TODATE", "2027/07/24"));

            res = WebServiceCall.APICall(ApplicationConstants.GetConfirmatoryTestsScreeningAppointmentDetails_V1, ApplicationConstants.webservice_d2d, param);
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


                            if (dependentForSearchBeneficiaryModel.getOutput().size() > 0) {
                                DependentForSearchBeneficiaryModel output = dependentForSearchBeneficiaryModel.getOutput().get(0);
                                edt_beneficiaryname.setText(String.valueOf(output.getBeneficiaryName()));
                                tvBenficiaryName.setText(String.valueOf(output.getBeneficiaryName()));
                                edt_gender.setText(output.getGender());
                                edt_age.setText(String.valueOf(output.getPatAge()));

                                teamid = output.getTeamid();

//                                new GetLabDetails().execute(teamid);

                            }

                            rv_testdetails.setAdapter(new Test_List_Adapter_Confirmatory(context, dependentList));


                        } else {
                            rv_testdetails.setAdapter(new Test_List_Adapter_Confirmatory(context, new ArrayList<>()));

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        rv_testdetails.setAdapter(new Test_List_Adapter_Confirmatory(context, new ArrayList<>()));
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

    public class GetLabDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Teamid", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetT2T_CT_LabNameDetails, ApplicationConstants.webservice_d2d, param);
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


                            if (dependentForSearchBeneficiaryModel.getOutput().size() > 0) {
                                DependentForSearchBeneficiaryModel output = dependentForSearchBeneficiaryModel.getOutput().get(0);
//                                edt_beneficiaryname.setText(String.valueOf(output.getBeneficiaryName()));
//                                edt_gender.setText(output.getGender());
//                                edt_age.setText(String.valueOf(output.getPatAge()));

                                edt_lab.setText(output.getLabName());
                                labCode = output.getLabCode();

                            }

//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());

//                            rv_testdetails.setAdapter(new Test_List_Adapter_Confirmatory(context, dependentList));
                            int t = 0;

                            for (DependentForSearchBeneficiaryModel output :
                                    dependentList) {

                                // t = t + Integer.valueOf(output.getaGE());


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
//                            rv_testdetails.setAdapter(new Test_List_Adapter_Confirmatory(context, new ArrayList<>()));

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
//                        rv_testdetails.setAdapter(new Test_List_Adapter_Confirmatory(context, new ArrayList<>()));

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
//    public class GetTestInfoCount extends AsyncTask<String, Void, String> {
//
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
//            param.add(new ParamsPojo("USERID", params[0]));
//            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
//            param.add(new ParamsPojo("AREA", params[2]));
//            param.add(new ParamsPojo("Type", params[3]));
//            param.add(new ParamsPojo("REDNO", params[4]));
//            param.add(new ParamsPojo("Regdid", params[5]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetConfirmatoryTestsScreeningAppointmentDetails, ApplicationConstants.webservice_d2d, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//
//                    DependentForSearchBeneficiaryModel dependentForSearchBeneficiaryModel = new Gson().fromJson(result, DependentForSearchBeneficiaryModel.class);
////                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);
//
//
//                    type = dependentForSearchBeneficiaryModel.getStatus();
//                    message = dependentForSearchBeneficiaryModel.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        dependentList = dependentForSearchBeneficiaryModel.getOutput();
//                        if (dependentList != null && dependentList.size() > 0) {
//
//
////                            if (dependentForSearchBeneficiaryModel.getOutput().size() > 0) {
////                                DependentForSearchBeneficiaryModel output = dependentForSearchBeneficiaryModel.getOutput().get(0);
////                                edt_beneficiaryname.setText(String.valueOf(output.getBeneficiaryName()));
////                                edt_gender.setText(output.getGender());
////                                edt_age.setText(String.valueOf(output.getPatAge()));
////
////                            }
//
////                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());
//
//                            rv_testdetailsCount.setAdapter(new Test_List_Adapter_Tube_Count_Confirmatory(context, dependentList));
//                            int t = 0;
//
//                            for (DependentForSearchBeneficiaryModel output :
//                                    dependentList) {
//
//                                // t = t + Integer.valueOf(output.getaGE());
//
//
//                            }
//
//                            //   tvTotalCount.setText("" + t);
//
//
//////                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//////                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//////                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//////                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//////                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
////
////                            }
//
//                        } else {
//                            rv_testdetailsCount.setAdapter(new Test_List_Adapter_Tube_Count_Confirmatory(context, new ArrayList<>()));
//
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        rv_testdetailsCount.setAdapter(new Test_List_Adapter_Tube_Count_Confirmatory(context, new ArrayList<>()));
//
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
//            }
//        }
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            if (requestCode == PATIENT_CAMERA_REQUEST) {
//                CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(HealthScreeningSamplecollectionConfirmatory_Activity.this);
//            }
////            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
////                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity.this);
////            }
////            if (requestCode == RENEWAL_CAMERA_REQUEST) {
////                CropImage.activity(renewalURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity.this);
////            }
////            if (requestCode == HIV_CONCERN_CAMERA_REQUEST) {
////                CropImage.activity(hivConcernURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistration_Activity.this);
////            }
//////            if (requestCode == 10001) {
////                IsAdharDataVerify = "0";
////                String requiredValue = data.getStringExtra("key");
////                Document doc = convertStringToDocument(requiredValue);
////
////                if (doc != null) {
////                    IsAdharDataVerify = "1";
////                    parseXmlDocument(doc);
////                } else {
////                    IsAdharDataVerify = "0";
////                    Utilities.showMessageString("Please try again", context);
////                }
////            }
//
//        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                savefile(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
//    }


//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        if (imageType == 0) {
//            isPatientPhotoAvailable = false;
//            String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
//            destinationFilename = patientPicsFolder + filename;
//            //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
//        }
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
//        if (imageType == 0) {
//            patientPicBm = BitmapFactory.decodeFile(destinationFilename);
//            destinationFilename = compressImage(destinationFilename);
//            patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
//            imv_patient.setImageBitmap(patientPicBm);
//            patientImagePath = destinationFilename;
//
//        }
//    }
//



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

                    isPatientPhotoAvailable = false;
                    String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
                    destinationFilename = patientPicsFolder + filename;
                    //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;



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

                    patientPicBm = BitmapFactory.decodeFile(destinationFilename);
                    Utilities.getBitmapAsByteArray(patientPicBm);
                    destinationFilename = compressImage(destinationFilename);
                    patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imv_patient.setImageBitmap(patientPicBm);
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


                    patientImagePath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }

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

    @SuppressLint("MissingPermission")
    public void saveFileAI(String filePath) {
        if (imageType == 0) {
            isPatientPhotoAvailable = true;
            String destinationFilename = filePath;
            patientPicBm = BitmapFactory.decodeFile(destinationFilename);
            String fName = System.currentTimeMillis() + "_PR.jpg";
            String outputFilePath = getExternalCacheDir() + fName;
            // Step 2: Compress the Bitmap and store it in a new file
            try {
                // Open a file output stream to save the compressed image
                FileOutputStream fos = new FileOutputStream(outputFilePath);

                // Compress the bitmap as JPEG with 80% quality
                patientPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                // Close the output stream
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imv_patient.setImageBitmap(patientPicBm);
            patientImagePath = outputFilePath;

        }
    }


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


    interface TubeEventsNew {
        void onChangeTubeCount(int position);
    }

    private class TubeCountAdapterNew extends RecyclerView.Adapter<TubeCountAdapterNew.MyViewHolder> {

        TubeEventsNew tubeEventsNew;

        public TubeCountAdapterNew(TubeEventsNew tubeEventsNew) {
            this.tubeEventsNew = tubeEventsNew;
        }

        @Override
        public TubeCountAdapterNew.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_test_list_tube_countconfirmatory, parent, false);
            return new TubeCountAdapterNew.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TubeCountAdapterNew.MyViewHolder holder, int pos) {
            int position = holder.getAbsoluteAdapterPosition();

            holder.tv_patientname.setText(tubeList.get(position).getTubName());
            holder.edt_tubeCount.setText("1");
            tubeList.get(position).setCount("1");

            holder.edt_tubeCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (!s.toString().equalsIgnoreCase(""))
                        tubeList.get(position).setCount(s.toString());
                    else tubeList.get(position).setCount("0");


                    int first = 0;
                    int second = 0;
                    int third = 0;
                    int fourth = 0;
                    int fifth = 0;
                    int sixth = 0;
                    int seventh = 0;
                    int eight = 0;
                    int nineth = 0;

                    try {

                        // if (!(tvTotalCentifigeTube.getText().toString().isEmpty() && tvSampleRejected.getText().toString().isEmpty())){

                        first = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(0).getCount())));
                        second = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(1).getCount())));
                        third = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(2).getCount())));
                        fourth = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(3).getCount())));
                        fifth = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(4).getCount())));
                        sixth = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(5).getCount())));
                        seventh = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(6).getCount())));
                        eight = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(7).getCount())));
                        nineth = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(8).getCount())));


//                            int num1 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(0).getCount())));
//                            int num2 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(1).getCount())));
//                            int num3 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(2).getCount())));
//                            int num4 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(3).getCount())));
//                            int num5 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(4).getCount())));
//                            int num6 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(5).getCount())));
//                            int num7 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(6).getCount())));
//                            int num8 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(7).getCount())));
//                            int num9 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(8).getCount())));

//                            int addition = num1+num2+num3+num4+num5+num6+num7+num8+num9;


                        int addition = first + second + third + fourth + fifth + sixth + seventh + eight + nineth;


                        edt_sampleCount.setText(String.valueOf(addition));
                        edt_sampleCount.setEnabled(false);


                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }


                @Override
                public void afterTextChanged(Editable s) {

                }
            });

//            holder.edt_tubeCount.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (!s.toString().equalsIgnoreCase("")) {
//                        tubeList.get(position).setCount(s.toString());
//
//
//                    } else tubeList.get(position).setCount("0");
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return tubeList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_patientname;
            private EditText edt_tubeCount;

            MyViewHolder(final View view) {
                super(view);
                tv_patientname = view.findViewById(R.id.tv_patientname);
                edt_tubeCount = view.findViewById(R.id.edt_tubeCount);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


    private void getOTPForSampleCollection(String Type, String mobileNo) {

        final ProgressDialog progressDialog = new ProgressDialog(HealthScreeningSamplecollectionConfirmatory_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String otpnumber = String.valueOf(((1 + new Random(System.currentTimeMillis()).nextInt(2)) * 10000 + new Random().nextInt(10000)));


        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getOtpForSampleCollection(mobileNo, otpnumber, beneficiary, userID, "1", subOrgId, smsId).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        Log.i("TAG", "onResponse: " + responseString);


                        JSONObject jsonObject = new JSONObject(responseString);


                        String status = jsonObject.optString("status");
                        String message = jsonObject.optString("message");

                        if (status.equalsIgnoreCase("Success")) {


//                            startResendTimer(btn_send_otp, edt_timer);

//
//                            ll_main_enter_otp.setVisibility(View.VISIBLE);
//
//                            edt_enter_otp.setText("");
//
//                            edt_timer.setVisibility(View.VISIBLE);


                            Utilities.showToastMessage("Otp sent" + " " + "on" + " " + mobileNo + " " + "number Successfully", context, true);


                            if (Type.equalsIgnoreCase("1")) {
                                verifyOtp(mobileNo, otpnumber);

                            }


                        } else {

                            Utilities.showAlertDialog(context, "Alert", message, false);

                        }


                    } else {
                        Log.e("TAG", "Response error: " + response.code() + " - " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Exception: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", "API call failed: " + t.getMessage());
            }

        });
    }


    void insertVerifyOtp(String otp, String mobile, AlertDialog alertDialog) {
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.insertVerifyOtp(mobile, otp).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    try {
                        pd.dismiss();
                        String result = response.body().string();
                        Log.i("TAG", "onPostExecute: " + result);

                        if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("Success")) {


                                isOtpVerify = true;
                                btn_register.setVisibility(View.VISIBLE);

                                ll_main_enter_otp.setVisibility(View.GONE);
                                edt_enter_otp.setText("");

                                edt_timer.setVisibility(View.VISIBLE);
                                btn_send_otp.setVisibility(View.GONE);
                                rg_selection_for_mobile_number.setVisibility(View.GONE);
                                ll_main_vendor.setVisibility(View.GONE);


                                tv_filterremark.setEnabled(false);

                                alertDialog.dismiss();


//
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.icon_success);
                                builder.setTitle("Success");
                                builder.setCancelable(false);
                                builder.setMessage("OTP Verified Successfully");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


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

                } else {
                    try {
                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();

                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });
    }


    private class GetTestInfoCount extends AsyncTask<String, Void, String> {

        //        String fromdate_new=tv_from_date_new.getText().toString();
//        String todate_new=tv_to_date_new.getText().toString();
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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("USERID", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("AREA", params[2]));
            param.add(new ParamsPojo("Type", params[3]));
            param.add(new ParamsPojo("REDNO", params[4]));
            param.add(new ParamsPojo("Regdid", params[5]));
            param.add(new ParamsPojo("FROMDATE", "2024/01/01"));
            param.add(new ParamsPojo("TODATE", "2025/10/24"));

            res = WebServiceCall.APICall(ApplicationConstants.GetConfirmatoryTestsScreeningAppointmentDetails_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierTubesModel pojo = new Gson().fromJson(result, CourierTubesModel.class);
                    type = pojo.getStatus();
                    message = pojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        tubeList = pojo.getOutput();

//                        if (!forwardedCourierIds.equals("0")) {
//                            List<CourierForwardedModel.OutputBean> selectedCouriersHavingTubeCountsList =
//                                    (List<CourierForwardedModel.OutputBean>) getIntent().getSerializableExtra("selectedCouriersHavingTubeCountsList");
//
//                            for (CourierForwardedModel.OutputBean courierList : selectedCouriersHavingTubeCountsList) {
//                                for (CourierForwardedModel.OutputBean.TubeDetails tubeDetails : courierList.getTubeDetails()) {
//                                    for (int i = 0; i < tubeList.size(); i++) {
//                                        if (tubeList.get(i).getTubeId().equals(tubeDetails.getTubeId())) {
//                                            tubeList.get(i).setCount(String.valueOf(Integer.parseInt(tubeList.get(i).getCount()) + Integer.parseInt(tubeDetails.getTotalCount())));
//                                        }
//                                    }
//                                }
//                            }
//                        } else {
                        {
                            for (int i = 0; i < tubeList.size(); i++) {
                                tubeList.get(i).setCount("");
                            }
                        }
                        TubeCountAdapterNew tubeCountAdapter = new TubeCountAdapterNew(new TubeEventsNew() {
                            @Override
                            public void onChangeTubeCount(int position) {

//                                rv_tube_counts.setAdapter(new TubeCountAdapter(this));
                            }
                        });
                        rv_testdetailsCount.setAdapter(tubeCountAdapter);
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }

        }

    }


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


    private void startResendTimer(Button resendOtpBtn, TextView tv_timer) {
        tv_timer.setVisibility(View.VISIBLE); // Show timer
//        new CountDownTimer(2 * 60 * 1000, 1000) { // 2 minutes
//            public void onTick(long millisUntilFinished) {
//                int minutes = (int) (millisUntilFinished / 1000) / 60;
//                int seconds = (int) (millisUntilFinished / 1000) % 60;
//                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
//                tv_timer.setText(timeFormatted);
//                btn_VerifyOtpContact.setEnabled(false);
//                btn_VerifyOtp.setEnabled(false);
//            }
//
//            public void onFinish() {
//                tv_timer.setVisibility(View.GONE);
//                resendOtpBtn.setVisibility(View.VISIBLE);
//                btn_VerifyOtpContact.setEnabled(true);
//                btn_VerifyOtp.setEnabled(true);
//
//            }
//        }.start();


        new CountDownTimer(120 * 1000, 1000) { // 120 seconds
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tv_timer.setText("Resend OTP in : " + String.valueOf(secondsRemaining) + " sec");
                btn_send_otp.setEnabled(false);
//                btn_VerifyOtp.setEnabled(false);
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                resendOtpBtn.setVisibility(View.VISIBLE);
                btn_send_otp.setEnabled(true);
//                btn_VerifyOtp.setEnabled(true);
            }
        }.start();

    }

    private void getFaceDetectionFlag() {
        final ProgressDialog progressDialog = new ProgressDialog(HealthScreeningSamplecollectionConfirmatory_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<FaceDetectionModel> call = apiService.getFaceDetectionData(userID);
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
        final ProgressDialog progressDialog = new ProgressDialog(HealthScreeningSamplecollectionConfirmatory_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<GetMobileNumberModel> call = apiService.getSelectNumber(regNo);
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

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(HealthScreeningSamplecollectionConfirmatory_Activity.this);
        builderSingle.setTitle("Select Month");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HealthScreeningSamplecollectionConfirmatory_Activity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getMobileNo());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tv_send_OTP.setText(trenchList.get(which).getMobileNo());
                mobilenumber = trenchList.get(which).getMobileNo();

//                getPaymentDetails();


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


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("FACE_DETECTION");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }


}
