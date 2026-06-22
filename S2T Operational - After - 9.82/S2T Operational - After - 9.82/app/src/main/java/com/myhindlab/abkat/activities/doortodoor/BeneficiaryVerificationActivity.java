package com.myhindlab.abkat.activities.doortodoor;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import static com.myhindlab.abkat.utilities.Utilities.compressImage;

import android.Manifest;
import android.app.Activity;
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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.CreateCamp_Activity_v3;
import com.myhindlab.abkat.activities.PostCampThumbSignatureUpload_Activity;
import com.myhindlab.abkat.adapters.CampPatientStatusAdapter;
import com.myhindlab.abkat.adapters.CampPatientStatusForBeneficiaryVerificationAdapter;
import com.myhindlab.abkat.adapters.DoctorListAdapter;
import com.myhindlab.abkat.adapters.WorkerDependentAdapter;
import com.myhindlab.abkat.adapters.doortodoor.CampCoordinatorNotWorkingTeamAdapter;
import com.myhindlab.abkat.models.CallTypeModel;
import com.myhindlab.abkat.models.CampBeneficiaryListModel;
import com.myhindlab.abkat.models.DoctorMappingModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PatientStatusModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
import com.myhindlab.abkat.models.ScreeningTestModel;
import com.myhindlab.abkat.models.UserAttendanceForPhysicalExamModel;
import com.myhindlab.abkat.models.WorkerDependentModel;
import com.myhindlab.abkat.models.doortodoor.AudioScreeningTestModel;
import com.myhindlab.abkat.models.doortodoor.D2dWorkingTeamModel;
import com.myhindlab.abkat.models.doortodoor.LungFunctionScreeningTestModel;
import com.myhindlab.abkat.models.doortodoor.VisionScreeningTestModel;
import com.myhindlab.abkat.pojos.PatientStatusPojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeneficiaryVerificationActivity extends AppCompatActivity {

    private TextView tvDoctor,tv_patient_Image,tvBarcode,tvHealthCard,tvAudioScreeningheader,et_phlebotomistName,tvVisionScreeningHeader,tvbloodSugarheader,tvGlucose,tvBeneficiaryInfoHeader,tvBeneficiaryName,
            tvAge,tvRelationWith,tvHeight,tvWeight,tvLftheader,tvBloodPressure,tvTestList,tvGender,tvNormalHearing,tvLeftEar,tvSnellenRightEye,tvSnellenLeftEye,tvFev1Count,tvInvestigationCount,
            tvNearViosionRightEye,tvNearVisionLeftEye,tvObervationRightEye,tvSystolicCount,tvDistolicCount,tvRandomSugarCount, tvObservationLeftEye,tv_call_type;
    private static Context context;
    private ProgressDialog pd;
    private static ArrayList<PatientStatusModel> patientList;
//    private boolean isAudioByUser = false;
//    private boolean isVisionByUser = false;

    private List<AudioScreeningTestModel.Output> audiolist;
    private List<VisionScreeningTestModel.Output> visionlist;
    private List<LungFunctionScreeningTestModel.Output> lftlist;

    private static final int GALLERY_REQUEST = 200;



    private File file, serviceCertiFolder;

    private Uri photoURI;
    int i = 0;






    private List<ScreeningTestModel.OutputBean> screeningTestList;
    private String DESGID, EmpCode,mobileNo,ReasonId ="0",testId = "0", LabCode, CampDATE, DISTLGDCODE, BMobile, district, TALLGDCODE, taluka, STATELGDCODE = "2", selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId, AssignedId = "", IsTeam;

    private final int LETTER_CAMERA_REQUEST = 111;

    private File signatureImageFile, patientSignFolder, fingerPrintFolder;


    private String letterImagePath, ackImagePath;

    private final int ACK_CAMERA_REQUEST = 112;

    private UserSessionManager session;
    private Bitmap letterPicBm, ackPicBm;

    private File patientPicsFolder;
    private CampBeneficiaryListModel.OutputBean beneficiary;


    private final int PATIENT_CAMERA_REQUEST = 100, HEALTHCARD_CAMERA_REQUEST = 200, RENEWAL_CAMERA_REQUEST = 300, HIV_CONCERN_CAMERA_REQUEST = 400, IDENTITY_CAMERA_REQUEST = 500;


    private Uri letterUri, ackUri;

    private int imageType = 0;
    private String callTypeId = "1", patientImagePath = "", healthCardImagePath = "", renewalImagePath = "", hivletterPath = "";

    private ImageView imv_search, imv_patient, imv_health_card, imv_renewal_form, imv_hiv_concern, imv_self_declaration;

    private ArrayList<WorkerDependentModel.Output> workerDependentModelArrayList;
    private ApiInterface apiInterface;
    private UserAttendanceForPhysicalExamModel.Output patientDetails;
    private ProgressDialog progressDialog;
    private static RecyclerView rv_patientlist;
    private LinearLayout ll_phleboName,mainll_beneficiary_info,mainllOther,mainll_Lft_info,mainll_Blood_Pressure_info,mainll_Vision_info,mainll_Audio_info;
    private int doctorId = 0;
    private Button btnApprove,btnReject;
    private String campId = "", teamId = "";
    private EditText et_ReasonDescription;
    private TextView et_Reason;


    private ArrayList<DoctorMappingModel.Output> doctorList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_verification_);

        initView();
        setEventHandlers();
        getSessionData();
        setUpToolbar();
        setDefault();
    }


    private void initView() {
        context = BeneficiaryVerificationActivity.this;
        session = new UserSessionManager(context);
        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(context);
        workerDependentModelArrayList = new ArrayList<>();
        pd = new ProgressDialog(context);
        tvDoctor = findViewById(R.id.tvDoctor);
        tv_call_type = findViewById(R.id.tv_call_type);
        btnApprove = findViewById(R.id.btnApprove);
        imv_patient = findViewById(R.id.imv_patient);
        imv_health_card = findViewById(R.id.imv_health_card);
        tvAge = findViewById(R.id.tvAge);
        tvRelationWith = findViewById(R.id.tvRelationWith);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvBloodPressure = findViewById(R.id.tvBloodPressure);
        tv_patient_Image = findViewById(R.id.tv_patient_Image);
        tvHealthCard = findViewById(R.id.tvHealthCard);
        et_phlebotomistName = findViewById(R.id.et_phlebotomistName);
        mainllOther = findViewById(R.id.mainllOther);
        tvGlucose = findViewById(R.id.tvGlucose);
        ll_phleboName = findViewById(R.id.ll_phleboName);
        btnReject = findViewById(R.id.btnReject);
        et_Reason = findViewById(R.id.et_Reason);
        et_ReasonDescription = findViewById(R.id.et_ReasonDescription);
        rv_patientlist = findViewById(R.id.rv_patientlist);
        rv_patientlist.hasFixedSize();
        rv_patientlist.setLayoutManager(new LinearLayoutManager(context));
        tvTestList = findViewById(R.id.tvTestList);
        tvBeneficiaryInfoHeader = findViewById(R.id.tvBeneficiaryInfoHeader);
        mainll_beneficiary_info = findViewById(R.id.mainll_beneficiary_info);
        tvGender = findViewById(R.id.tvGender);
        tvBeneficiaryName = findViewById(R.id.tvBeneficiaryName);
        tvLeftEar = findViewById(R.id.tvLeftEar);
        tvNormalHearing = findViewById(R.id.tvNormalHearing);
        tvSnellenRightEye = findViewById(R.id.tvSnellenRightEye);
        tvSnellenLeftEye = findViewById(R.id.tvSnellenLeftEye);
        tvNearViosionRightEye = findViewById(R.id.tvNearViosionRightEye);
        tvNearVisionLeftEye = findViewById(R.id.tvNearVisionLeftEye);
        tvObervationRightEye = findViewById(R.id.tvObervationRightEye);
        tvObservationLeftEye = findViewById(R.id.tvObservationLeftEye);
        tvSystolicCount = findViewById(R.id.tvSystolicCount);
        tvDistolicCount = findViewById(R.id.tvDistolicCount);
        tvRandomSugarCount = findViewById(R.id.tvRandomSugarCount);
        tvFev1Count = findViewById(R.id.tvFev1Count);
        tvInvestigationCount = findViewById(R.id.tvInvestigationCount);
        mainll_Lft_info = findViewById(R.id.mainll_Lft_info);
        mainll_Blood_Pressure_info = findViewById(R.id.mainll_Blood_Pressure_info);
        mainll_Vision_info = findViewById(R.id.mainll_Vision_info);
        mainll_Audio_info = findViewById(R.id.mainll_Audio_info);
        tvAudioScreeningheader = findViewById(R.id.tvAudioScreeningheader);
        tvVisionScreeningHeader = findViewById(R.id.tvVisionScreeningHeader);
        tvbloodSugarheader = findViewById(R.id.tvbloodSugarheader);
        tvLftheader = findViewById(R.id.tvLftheader);
        tvBarcode = findViewById(R.id.tvBarcode);
        Intent intent = getIntent();


      //  mainll_beneficiary_info.setVisibility(View.GONE);
        mainll_Audio_info.setVisibility(View.GONE);
        mainll_Vision_info.setVisibility(View.GONE);
        mainll_Blood_Pressure_info.setVisibility(View.GONE);
        mainll_Lft_info.setVisibility(View.GONE);

        beneficiary = (CampBeneficiaryListModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");


        et_phlebotomistName.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tvHeight.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tvWeight.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tvAge.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tvBloodPressure.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tvGlucose.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tvRelationWith.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tvGender.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tvBeneficiaryName.setBackgroundColor(Color.parseColor("#CCCCCC"));

//        tvBarcode.setBackgroundColor(Color.parseColor("#CCCCCC"));



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



//        rv_worker.setHasFixedSize(true);
//        rv_worker.setLayoutManager(new LinearLayoutManager(context));

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
            }
        } catch (Exception e) {
            e.printStackTrace();

        }



    }

    void setDefault() {
        beneficiary = (CampBeneficiaryListModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");
//        campId = getIntent().getStringExtra("campId");

        if (DESGID.equalsIgnoreCase("35")|| DESGID.equalsIgnoreCase("146")|| DESGID.equalsIgnoreCase("129")) {
            ll_phleboName.setVisibility(View.GONE);
            btnApprove.setVisibility(View.GONE);
        } else {
            ll_phleboName.setVisibility(View.VISIBLE);
            btnApprove.setVisibility(View.VISIBLE);

        }

        tvAge.setText(beneficiary.getAGE());
        tvRelationWith.setText(beneficiary.getRelName());
        tvBeneficiaryName.setText(beneficiary.getWorkerName()+" "+"["+beneficiary.getWorkerAge()+"/"+beneficiary.getWorkerGender()+"]");
        btnReject.setBackgroundColor(Color.parseColor("#FFA500"));
        tvBarcode.setText(beneficiary.getSampleCollectedBarcode());
        btnReject.setTextColor(Color.BLACK);
        tvGender.setText(beneficiary.getGENDER());
        et_ReasonDescription.setText(beneficiary.getOtherDescription());
        btnApprove.setBackgroundColor(Color.parseColor("#5DBB63"));
        btnApprove.setTextColor(Color.BLACK);

        patientImagePath = beneficiary.getRegdImagePath();
        healthCardImagePath = beneficiary.getCardImagePath();
        tvHeight.setText(beneficiary.getHeight_CMs());
        tvWeight.setText(beneficiary.getWeight_KGs());
        et_phlebotomistName.setText(beneficiary.getPhleboName());
        mobileNo = beneficiary.getPhleboMobNo();
        tvBloodPressure.setText(beneficiary.getSystolic() + "/" + beneficiary.getDiastolic());
        tvGlucose.setText(beneficiary.getBloodSugar_R());

        letterImagePath = beneficiary.getRegdImageName();
        ackImagePath = beneficiary.getCardImageName();

//        WorkerDependentModel workerDependentModel = new WorkerDependentModel();
//        WorkerDependentModel.Output output = workerDependentModel.new Output(Integer.valueOf(campId), "", patientDetails.getRegdId(), patientDetails.getEnglishName(), patientDetails.getRegdId(), "W", String.valueOf(patientDetails.getRegdNo()), 0, 0);
//        workerDependentModelArrayList.add(output);

//        getDependents();

        if (BuildConfig.isBeta) {
            // String url = BuildConfig.domain + "/ReportDelivery/" + patientImagePath;
            String url = patientImagePath;
            Picasso.with(context).load(url).into(imv_patient);

            imv_patient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                }
            });


        }else {
            String url = patientImagePath;
            Picasso.with(context).load(url).into(imv_patient);

            imv_patient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                }
            });


        }

        new CAMPPatientCheckupAnalysis_Report().execute(beneficiary.getRegdId());
        new AudioScreeningData().execute(beneficiary.getRegdId());
        new GetVisionScreeningDetails().execute(beneficiary.getRegdId());
        new GetLungFunctionTestDetails().execute(beneficiary.getRegdId());


        if (BuildConfig.isBeta) {
            // String url = BuildConfig.domain + "/ReportDelivery/" + patientImagePath;
            String url = healthCardImagePath;
            Picasso.with(context).load(url).into(imv_health_card);

            imv_health_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                }
            });

        }else {
            String url = healthCardImagePath;
            Picasso.with(context).load(url).into(imv_health_card);

            imv_health_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                }
            });

        }

        if (beneficiary.getTestId()!=null){

            tvTestList.setEnabled(false);
            if (beneficiary.getTestId().equalsIgnoreCase("2")){
                tvTestList.setText("Basic Details");
            } else if (beneficiary.getTestId().equalsIgnoreCase("3")) {
                tvTestList.setText("Physical Examination");

            } else if (beneficiary.getTestId().equalsIgnoreCase("4")) {
                tvTestList.setText("Lung Functioin Test");

            } else if (beneficiary.getTestId().equalsIgnoreCase("5")) {
                tvTestList.setText("Audio Screening Test");

            } else if (beneficiary.getTestId().equalsIgnoreCase("6")) {
                tvTestList.setText("Vision Screening");

            } else if (beneficiary.getTestId().equalsIgnoreCase("7")) {
                tvTestList.setText("Sample Collection");

            } else if (beneficiary.getTestId().equalsIgnoreCase("8")) {
                tvTestList.setText("Random Sugar Test");
            } else if (beneficiary.getTestId().equalsIgnoreCase("9")) {

                tvTestList.setText("Ackowledgement");
            }
        }

        if (beneficiary.getReason()!=null){
            et_Reason.setText(beneficiary.getReason());
        }


        if (beneficiary.getIsApproved() != null) {

            if (beneficiary.getIsApproved().equalsIgnoreCase("1")) {

                tv_patient_Image.setEnabled(false);
                tv_patient_Image.setVisibility(View.GONE);
                tvHealthCard.setEnabled(false);
                tvHealthCard.setVisibility(View.GONE);


                et_Reason.setEnabled(false);
                btnReject.setVisibility(View.GONE);


            } else if (beneficiary.getIsApproved().equalsIgnoreCase("2")) {
                et_Reason.setEnabled(false);
                btnReject.setVisibility(View.GONE);
                tv_patient_Image.setEnabled(false);
                tv_patient_Image.setVisibility(View.GONE);
                tvHealthCard.setEnabled(false);
                tvHealthCard.setVisibility(View.GONE);


                if (beneficiary.getReason()!=null){
                    if (beneficiary.getReason().contains("Other")){
                        mainllOther.setVisibility(View.VISIBLE);

                    }
                }

                et_ReasonDescription.setEnabled(false);
            }
        }


        if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29") || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162") || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77") || DESGID.equalsIgnoreCase("128")||DESGID.equalsIgnoreCase("108"))))) {
            if (beneficiary.getIsApproved() != null) {
                if (beneficiary.getIsApproved().equalsIgnoreCase("1")) {
                    tv_patient_Image.setEnabled(false);
                    tvHealthCard.setEnabled(false);
                    btnReject.setVisibility(View.GONE);
                    btnApprove.setVisibility(View.GONE);

                } else if (beneficiary.getIsApproved().equalsIgnoreCase("2")) {

                    btnApprove.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    tvHealthCard.setEnabled(false);
                    tvHealthCard.setVisibility(View.GONE);
                    tv_patient_Image.setVisibility(View.GONE);
                }
            }

            if (!(beneficiary.getCampCreatedBy().equalsIgnoreCase(UserId))){
                btnApprove.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                tvHealthCard.setVisibility(View.GONE);
                tv_patient_Image.setVisibility(View.GONE);

            }

        }

        if (DESGID.equalsIgnoreCase("77")||DESGID.equalsIgnoreCase("84")||DESGID.equalsIgnoreCase("30")){
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
            tvHealthCard.setVisibility(View.GONE);
            tv_patient_Image.setVisibility(View.GONE);
        }

    }
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Beneficiary Verification");

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        btn_save_accordian.setVisibility(View.GONE);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        btn_save_accordian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.list_info);
                ;
                dialog.show();
            }

        });
    }

    private void setEventHandlers() {



        tvBeneficiaryInfoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainll_beneficiary_info.setVisibility(mainll_beneficiary_info.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (mainll_beneficiary_info.getVisibility() == View.VISIBLE) {
                    tvBeneficiaryInfoHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvBeneficiaryInfoHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);
                }
            }
        });

        tvAudioScreeningheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainll_Audio_info.setVisibility(mainll_Audio_info.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (mainll_Audio_info.getVisibility() == View.VISIBLE) {
                    tvAudioScreeningheader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvAudioScreeningheader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);
                }
            }
        });

        tvVisionScreeningHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainll_Vision_info.setVisibility(mainll_Vision_info.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (mainll_Vision_info.getVisibility() == View.VISIBLE) {
                    tvVisionScreeningHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvVisionScreeningHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);
                }
            }
        });

        tvbloodSugarheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainll_Blood_Pressure_info.setVisibility(mainll_Blood_Pressure_info.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (mainll_Blood_Pressure_info.getVisibility() == View.VISIBLE) {
                    tvbloodSugarheader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvbloodSugarheader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);
                }
            }
        });

        tvLftheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainll_Lft_info.setVisibility(mainll_Lft_info.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (mainll_Lft_info.getVisibility() == View.VISIBLE) {
                    tvLftheader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvLftheader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);
                }
            }
        });


        tvTestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetScreeningTest().execute(beneficiary.getRegdId());
            }
        });

        et_Reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetReason().execute(beneficiary.getRegdId());
            }
        });

        tv_patient_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                selectImage();




                imageType = 0;
                if (SDK_INT < Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
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
                    letterUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_PR.png");
                    letterUri = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);
                }
            }
        });



        tvHealthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageType = 1;
                if (SDK_INT < Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
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
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_ACK.png");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    ackUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, ackUri);
                    startActivityForResult(intent, ACK_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_ACK.png");
                    ackUri = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, ackUri);
                    startActivityForResult(intent, ACK_CAMERA_REQUEST);
                }


            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (beneficiary.getALLTESTDONE().equalsIgnoreCase("0")){
                    Utilities.showToastMessage("Beneficiary test are pending", context, false);
                    return;
                }

                if (letterImagePath == null) {
                    Utilities.showToastMessage("Please capture beneficiary photo", context, false);
                    return;
                }

//                if (tvObervationRightEye.getText().toString().equalsIgnoreCase("Right Eye Blind")||(tvObservationLeftEye.getText().toString().equalsIgnoreCase("Left Eye Blind"))){
//                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
//                            .setTitle("लाभार्थी डोळ्याने अंध आहे. \n" +
//                                    "बरोबर असल्याची खात्री करा.\n" +
//                                    "")
//                            .setIcon(R.drawable.icon_alertred)
//                            .setPositiveButton("Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
////                                            isVisionByUser = true;
//                                            // do something...
//                                            dialog.dismiss();
//
//
//                                        }
//                                    }
//
//                            );
//
//                    b.show();
//
//                }
//                if (tvObservationLeftEye.getText().toString().equalsIgnoreCase("Left Eye Blind")){
//                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
//                            .setTitle("लाभार्थी डाव्या डोळ्याने अंध आहे. \n" +
//                                    "बरोबर असल्याची खात्री करा.\n" +
//                                    "")
//                            .setIcon(R.drawable.icon_alertred)
//                            .setPositiveButton("Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            // do something...
//                                            dialog.dismiss();
//                                        }
//                                    }
//
//                            );
//
//                    b.show();
//
//                }


//                if (tvNormalHearing.getText().toString().equalsIgnoreCase("Deafness")||(tvLeftEar.getText().toString().equalsIgnoreCase("Deafness"))){
//
//                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
//                            .setTitle("लाभार्थी  कानाने मूकबधिर आहे. \n" +
//                                    "बरोबर असल्याची खात्री करा.\n" +
//                                    "")
//                            .setIcon(R.drawable.icon_alertred)
//                            .setPositiveButton("Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
////                                            isAudioByUser = true;
//                                            // do something...
//                                            dialog.dismiss();
//                                        }
//                                    }
//
//                            );
//                    b.show();
//
//
//                }
//                if (tvLeftEar.getText().toString().equalsIgnoreCase("Deafness")){
//                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
//                            .setTitle("लाभार्थी डाव्या कानाने मूकबधिर आहे. \n" +
//                                    "बरोबर असल्याची खात्री करा\n" +
//                                    "")
//                            .setIcon(R.drawable.icon_alertred)
//                            .setPositiveButton("Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            // do something...
//                                            dialog.dismiss();
//                                        }
//                                    }
//
//                            );
//
//                    b.show();
//
//                }

                if (ackImagePath == null) {
                    Utilities.showToastMessage("Please capture health card photo", context, false);
                    return;
                }
                new InsertRejectOrAccept().execute(beneficiary.getRegdId(),beneficiary.getCampId(),testId,et_Reason.getText().toString().trim(),"1",UserId,ReasonId,et_ReasonDescription.getText().toString().trim());
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (tvTestList.getText().toString().isEmpty()){
//                    Utilities.showToastMessage("Please select reject test", context, false);
//                   return;
//                }



                if (et_Reason.getText().toString().isEmpty()){
                    Utilities.showToastMessage("Please select reason", context, false);
                   return;
                }

                if (mainllOther.getVisibility() == View.VISIBLE){
                    if (et_ReasonDescription.getText().toString().isEmpty()){
                        Utilities.showToastMessage("Please enter other description", context, false);
                        return;
                    }
                }

                if (letterImagePath == null) {
                    Utilities.showToastMessage("Please capture beneficiary photo", context, false);
                    return;
                }

                if (ackImagePath == null) {
                    Utilities.showToastMessage("Please capture health card photo", context, false);
                    return;
                }
                new InsertRejectOrAccept().execute(beneficiary.getRegdId(),beneficiary.getCampId(),testId,et_Reason.getText().toString().trim(),"2",UserId,ReasonId,et_ReasonDescription.getText().toString().trim());
            }
        });


        et_phlebotomistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phoneNumber = mobileNo;
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

    }


    private void showAssignType(final ArrayList<CallTypeModel> callTypeModelArrayList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Choose Call Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < callTypeModelArrayList.size(); i++) {
            arrayAdapter.add(String.valueOf(callTypeModelArrayList.get(i).getTypeName()));
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
                tv_call_type.setText(callTypeModelArrayList.get(which).getTypeName());
                callTypeId = String.valueOf(callTypeModelArrayList.get(which).getTypeId());
                //  refreshCalendar();

            }
        });
        builderSingle.show();
    }

//    void getDependents() {
//        progressDialog.setMessage("Getting Dependent..");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        apiInterface.getBeneficiaryListByRegID(patientDetails.getRegdId()).enqueue(new Callback<WorkerDependentModel>() {
//            @Override
//            public void onResponse(Call<WorkerDependentModel> call, Response<WorkerDependentModel> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful()) {
//                    if (response.body().getStatus().equalsIgnoreCase("success")) {
//                        workerDependentModelArrayList.addAll(response.body().getOutput());
//                        rv_worker.setAdapter(new WorkerDependentAdapter(workerDependentModelArrayList));
//
//                    } else {
//                        Utilities.showToastMessage(response.body().getMessage(), context, false);
//                    }
//                } else {
//                    Utilities.showToastMessage("" + response.message(), context, false);
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<WorkerDependentModel> call, Throwable t) {
//                progressDialog.dismiss();
//                Utilities.showToastMessage(t.getMessage(), context, false);
//
//            }
//        });
//    }

    private class GetDoctorList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", campId));
            param.add(new ParamsPojo("TeamId", teamId));
            res = WebServiceCall.APICall(ApplicationConstants.GetD2DCampMappedDoctorList, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    doctorList = new ArrayList<>();
                    DoctorMappingModel pojoDetails = new Gson().fromJson(result, DoctorMappingModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        doctorList.addAll(pojoDetails.getOutput());
                        if (doctorList.size() > 0) {
                            showDoctorListDialog(doctorList);
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


    private void showDoctorListDialog(final ArrayList<DoctorMappingModel.Output> doctorList) {


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Doctor");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);


        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        DoctorListAdapter doctorListAdapter = new DoctorListAdapter(doctorList);
        rvList.setAdapter(doctorListAdapter);

        ArrayList<DoctorMappingModel.Output> filteredList = new ArrayList<>();

        //        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        //        for (Lab_OutPut_Pojo subTrenchModel : lab_List) {
        //            arrayAdapter.add(subTrenchModel.getLabName());
        //        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

//            @Override
//            public void onTextChanged(CharSequence query, int start, int before, int count) {
//
//                if (filteredList != null)
//                    filteredList.clear();
//
//                if (doctorList != null) {
//                    if (edt_search.getText().toString().equals("")) {
//                        filteredList.addAll(doctorList);
//                        rvList.setAdapter(new DoctorListAdapter(filteredList));
//
//                    } else {
//                        if (doctorList.size() > 0) {
//                            for (DoctorMappingModel.Output pojo : doctorList) {
//                                String siteDetails = pojo.getFullname();
//                                if (siteDetails != null && siteDetails.toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
//                                    filteredList.add(pojo);
//                                }
//                            }
//
//                            if (filteredList.size() == 0) {
//                                filteredList.addAll(doctorList);
//                                rvList.setAdapter(new DoctorListAdapter(filteredList));
//                            } else {
//                                rvList.setAdapter(new DoctorListAdapter(filteredList));
//
//                            }
//                        }
//                    }
//                }
//            }


            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new DoctorListAdapter(doctorList));
                    return;
                }

                if (doctorList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<DoctorMappingModel.Output> searchedTestList = new ArrayList<>();
                    for (DoctorMappingModel.Output clientDetails : doctorList) {

                        String countryToBeSearched = clientDetails.getFullname().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new DoctorListAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new DoctorListAdapter(doctorList));
                }

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog alertDialog = builderSingle.create();

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


//                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        selectedLabID = lab_List.get(which).getLabCode();
//                        selectedLabName = lab_List.get(which).getLabName();
//                        tvLab.setText(selectedLabName);
//                    }
//                });


        rvList.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                if (filteredList.size() > 0) {
                tvDoctor.setText(doctorList.get(position).getFullname());

                doctorId = doctorList.get(position).getUserid();
//                }
                alertDialog.dismiss();

            }
        }));

        alertDialog.show();

    }


    private class GetTeamId extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", campId));
            param.add(new ParamsPojo("UserID", String.valueOf(session.getUserDetailsJson().getEmpCode())));
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        teamId = jsonObject1.getString("TeamNumber");
                        new GetDoctorList().execute();
                    } else {
                        Utilities.showAlertDialog(context, "Error", "Unable to get team id", false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    private static class CAMPPatientCheckupAnalysis_Report extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  swipeRefreshLayout.setRefreshing(false);
              pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("RegdId", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.CAMPPatientCheckupAnalysis_Report_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            try {
                if (!result.equals("")) {
                    PatientStatusPojo pojoDetails = new Gson().fromJson(result, PatientStatusPojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        patientList = pojoDetails.getOutput();
                        if (patientList.size() > 0) {
                            rv_patientlist.setVisibility(View.VISIBLE);
//                            ll_lables.setVisibility(View.VISIBLE);
//                            ll_nothingtoshow.setVisibility(View.GONE);
                            rv_patientlist.setAdapter(new CampPatientStatusForBeneficiaryVerificationAdapter(context, patientList));
                        } else {
                            rv_patientlist.setVisibility(View.GONE);
//                            ll_lables.setVisibility(View.GONE);
//                            ll_nothingtoshow.setVisibility(View.VISIBLE);
//                            tv_message.setText("Beneficiary not mapped with this camp");
                        }
                    } else {
                        rv_patientlist.setVisibility(View.GONE);
//                        ll_lables.setVisibility(View.GONE);
//                        ll_nothingtoshow.setVisibility(View.VISIBLE);
//                        tv_message.setText("Beneficiary not mapped with this camp");
                    }
                } else {
                    rv_patientlist.setVisibility(View.GONE);
//                    ll_lables.setVisibility(View.GONE);
//                    ll_nothingtoshow.setVisibility(View.VISIBLE);
//                    tv_message.setText("Beneficiary not mapped with this camp");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

          //  searchView.clearFocus();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

//            if (requestCode == GALLERY_REQUEST) {
//                Uri imageUri = data.getData();
//                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).start(BeneficiaryVerificationActivity.this);
//            }


            if (requestCode == LETTER_CAMERA_REQUEST) {
//                CropImage.activity(letterUri).setGuidelines(CropImageView.Guidelines.ON).start(BeneficiaryVerificationActivity.this);
            }
            if (requestCode == ACK_CAMERA_REQUEST) {
//                CropImage.activity(ackUri).setGuidelines(CropImageView.Guidelines.ON).start(BeneficiaryVerificationActivity.this);
            }


        }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                if (imageType == 0) {
//                    savefile(resultUri);
//
//
//
//                } else if (imageType == 1) {
//                    saveAckfile(resultUri);
//
//
//                }
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
    }

    public void savefile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String destinationFilename = "";
        String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
        destinationFilename = patientPicsFolder + filename;


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

        letterPicBm = BitmapFactory.decodeFile(destinationFilename);
        destinationFilename = compressImage(destinationFilename);
        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
        imv_patient.setImageBitmap(letterPicBm);
        letterImagePath = destinationFilename;

        new ApproveBeneficiary().execute(letterImagePath,"1");



    }

    public void saveAckfile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String destinationFilename = "";
        String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
        destinationFilename = patientPicsFolder + filename;


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

        ackPicBm = BitmapFactory.decodeFile(destinationFilename);
        destinationFilename = compressImage(destinationFilename);
        ackPicBm = Bitmap.createScaledBitmap(ackPicBm, 150, 150, false);
        imv_health_card.setImageBitmap(ackPicBm);
        ackImagePath = destinationFilename;

        new ApproveBeneficiary().execute(ackImagePath,"2");



    }



    private class ApproveBeneficiary extends AsyncTask<String, Integer, String> {

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
                multipart = new MultipartUtility(ApplicationConstants.BeneficiaryVerification, "UTF-8");

                multipart.addFormField("RegdNo", beneficiary.getRegdId());
                if (!params[0].isEmpty())
                    multipart.addFilePart("FilePath", new File(params[0]));
                multipart.addFormField("IsType", params[1]);
                multipart.addFormField("CreatedBy",UserId );
                multipart.addFormField("SiteId",beneficiary.getSiteDetailId() );


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
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("CampBeneficiaryList_Fragment").putExtra("campId", beneficiary.getCampId()));
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));

                        Utilities.showAlertDialog(context, "Success", "Photo updated successfully", true, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            //    finish();

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


    private class GetScreeningTest extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.getTestListForReject, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    ScreeningTestModel pojoDetails = new Gson().fromJson(result, ScreeningTestModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        screeningTestList = pojoDetails.getOutput();
                        if (screeningTestList.size() > 0) {
                            showScreeningTestDialog(screeningTestList);
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
    private class GetReason extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetOtherReasonForPatientRejection, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    ScreeningTestModel pojoDetails = new Gson().fromJson(result, ScreeningTestModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        screeningTestList = pojoDetails.getOutput();
                        if (screeningTestList.size() > 0) {
                            showReasonDialog(screeningTestList);
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

    private void showScreeningTestDialog(final List<ScreeningTestModel.OutputBean> landinglablist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Test");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < landinglablist.size(); i++) {
            arrayAdapter.add(String.valueOf(landinglablist.get(i).getTestName()));
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
                tvTestList.setText(landinglablist.get(which).getTestName());
                testId = String.valueOf(landinglablist.get(which).getTestId());



//                if (testId.equalsIgnoreCase("500")){
//                    mainllOther.setVisibility(View.VISIBLE);
//                }else {
//                    mainllOther.setVisibility(View.GONE);
//                }
            }
        });
        builderSingle.show();

    }
    private void showReasonDialog(final List<ScreeningTestModel.OutputBean> landinglablist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Test");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < landinglablist.size(); i++) {
            arrayAdapter.add(String.valueOf(landinglablist.get(i).getReasonDescription()));
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
                et_Reason.setText(landinglablist.get(which).getReasonDescription());
                ReasonId = String.valueOf(landinglablist.get(which).getReasonId());



//
                if (ReasonId.equalsIgnoreCase("9")){
                    mainllOther.setVisibility(View.VISIBLE);
                }else {
                    mainllOther.setVisibility(View.GONE);
                }
            }
        });
        builderSingle.show();

    }


    private class InsertRejectOrAccept extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdNo", params[0]));
            param.add(new ParamsPojo("CampId", params[1]));
            param.add(new ParamsPojo("TestId", params[2]));
            param.add(new ParamsPojo("Reason", params[3]));
            param.add(new ParamsPojo("IsApproved", params[4]));
            param.add(new ParamsPojo("CreatedBy", params[5]));
            param.add(new ParamsPojo("ReasonId", params[6]));
            param.add(new ParamsPojo("OtherDescription", params[7]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertPatientRejectionInCamp, ApplicationConstants.webservice_d2d, param);
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
                        builder.setMessage(message);
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


    public class AudioScreeningData extends AsyncTask<String, Void, String> {


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
            // res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeams, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetAudioScreeningDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    AudioScreeningTestModel audioScreeningTestModel = new Gson().fromJson(result, AudioScreeningTestModel.class);
//                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);


                    type = audioScreeningTestModel.getStatus();
                    message = audioScreeningTestModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        audiolist = audioScreeningTestModel.getOutput();
                        if (audiolist != null && audiolist.size() > 0) {


//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());

                          //  rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, audiolist,));
                            int t = 0;

                            for (AudioScreeningTestModel.Output output :
                                    audiolist) {

                                tvNormalHearing.setText(output.getRightRemark());
                                tvLeftEar.setText(output.getRemark());

                                if (tvNormalHearing.getText().toString().equalsIgnoreCase("Deafness")){

                                    {

                                        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
                                                .setTitle("लाभार्थी उजव्या कानाने मूकबधिर आहे. \n" +
                                                        "बरोबर असल्याची खात्री करा.\n" +
                                                        "")
                                                .setIcon(R.drawable.icon_alertred)
                                                .setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                            isAudioByUser = true;
                                                                // do something...
                                                                dialog.dismiss();
                                                            }
                                                        }

                                                );
                                        b.show();


                                    }

//                                    isAudioByUser = false;


                                    tvNormalHearing.setTextColor(Color.parseColor("#FF474C"));
//                                    tvNormalHearing.setBackgroundColor(Color.parseColor("#FF474C"));
                                }
                                if (tvLeftEar.getText().toString().equalsIgnoreCase("Deafness")){
//                                    isAudioByUser = false;

                                    tvLeftEar.setTextColor(Color.parseColor("#FF474C"));
//                                    tvLeftEar.setBackgroundColor(Color.parseColor("#FF474C"));

                                    {

                                        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
                                                .setTitle("लाभार्थी डाव्या कानाने मूकबधिर आहे. \n" +
                                                        "बरोबर असल्याची खात्री करा.\n" +
                                                        "")
                                                .setIcon(R.drawable.icon_alertred)
                                                .setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                            isAudioByUser = true;
                                                                // do something...
                                                                dialog.dismiss();
                                                            }
                                                        }

                                                );
                                        b.show();


                                    }
                                }
                            }


                        //    tvTotalCount.setText("" + t);


////                            if (adminActiveInactiveModel.getOutput().size() > 0) {
////                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
////                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
////                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
////                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {
//                            rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
//                            tvTotalCount.setText("");
//                            WorkingTeamCount.setText("");
//                            NotWorkingTeamCount.setText("");
//                            TotalTeamCount.setText("");
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
//                        rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
//                        tvTotalCount.setText("");
//                        WorkingTeamCount.setText("");
//                        NotWorkingTeamCount.setText("");
//                        TotalTeamCount.setText("");
                      //  Utilities.showAlertDialog(context, "Fail", message, false);
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


    public class GetLungFunctionTestDetails extends AsyncTask<String, Void, String> {


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
            // res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeams, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetLungFunctionTestDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    LungFunctionScreeningTestModel lungFunctionScreeningTestModel = new Gson().fromJson(result, LungFunctionScreeningTestModel.class);
//                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);


                    type = lungFunctionScreeningTestModel.getStatus();
                    message = lungFunctionScreeningTestModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        lftlist = lungFunctionScreeningTestModel.getOutput();
                        if (lftlist != null && lftlist.size() > 0) {


//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());

                          //  rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, audiolist,));
                            int t = 0;

                            for (LungFunctionScreeningTestModel.Output output :
                                    lftlist) {

                                tvFev1Count.setText(""+output.getFeviFvc());
                                tvInvestigationCount.setText(output.getResult());

                            }

                        //    tvTotalCount.setText("" + t);


////                            if (adminActiveInactiveModel.getOutput().size() > 0) {
////                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
////                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
////                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
////                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {
//                            rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
//                            tvTotalCount.setText("");
//                            WorkingTeamCount.setText("");
//                            NotWorkingTeamCount.setText("");
//                            TotalTeamCount.setText("");
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
//                        rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
//                        tvTotalCount.setText("");
//                        WorkingTeamCount.setText("");
//                        NotWorkingTeamCount.setText("");
//                        TotalTeamCount.setText("");
                      //  Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                  //  Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }

    public class GetVisionScreeningDetails extends AsyncTask<String, Void, String> {


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
            // res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeams, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetVisionScreeningDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    VisionScreeningTestModel visionScreeningTestModel = new Gson().fromJson(result, VisionScreeningTestModel.class);
//                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);


                    type = visionScreeningTestModel.getStatus();
                    message = visionScreeningTestModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        visionlist = visionScreeningTestModel.getOutput();
                        if (visionlist != null && visionlist.size() > 0) {


//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());

                          //  rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, audiolist,));
                            int t = 0;

                            for (VisionScreeningTestModel.Output output :
                                    visionlist) {

                                if (output.getVisionSnellelchartR()!=null){
                                    tvSnellenRightEye.setText(""+output.getVisionSnellelchartR());
                                }
                                if (output.getVisionSnellelchartL()!=null){
                                    tvSnellenLeftEye.setText(""+output.getVisionSnellelchartL());

                                }
                                if (output.getVisionSnellelchartL1()!=null){
                                    tvNearViosionRightEye.setText(""+output.getVisionSnellelchartL1());

                                }
                                if (output.getVisionSnellelchartL1()!=null){
                                    tvNearVisionLeftEye.setText(""+output.getVisionSnellelchartL1());

                                }
                                if (output.getRightRemark()!=null){
                                    tvObervationRightEye.setText(""+output.getRightRemark());

                                }
                                if (output.getLeftRemark()!=null){
                                    tvObservationLeftEye.setText(""+output.getLeftRemark());

                                }

                                tvSystolicCount.setText(""+output.getSystolic()+" "+"("+"mmHg"+")");
                                tvDistolicCount.setText(""+output.getDiastolic()+" "+"("+"mmHg"+")");
                                tvRandomSugarCount.setText(""+output.getBloodSugarR());


                            }

                            if (tvObervationRightEye.getText().toString().equalsIgnoreCase("Right Eye Blind")){

//                                 isVisionByUser = false;

                                tvObervationRightEye.setTextColor(Color.parseColor("#FF474C"));
//                                tvObervationRightEye.setBackgroundColor(Color.parseColor("#FF474C"));


                                {
                                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
                                            .setTitle("लाभार्थी उजव्या  डोळ्याने अंध आहे. \n" +
                                                    "बरोबर असल्याची खात्री करा.\n" +
                                                    "")
                                            .setIcon(R.drawable.icon_alertred)
                                            .setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            isVisionByUser = true;
                                                            // do something...
                                                            dialog.dismiss();

                                                        }
                                                    }

                                            );

                                    b.show();

                                }


                            }
                            if (tvObservationLeftEye.getText().toString().equalsIgnoreCase("Left Eye Blind")){
//                                isVisionByUser = false;

                                tvObservationLeftEye.setTextColor(Color.parseColor("#FF474C"));
//                                tvObservationLeftEye.setBackgroundColor(Color.parseColor("#FF474C"));


                                {
                                    android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
                                            .setTitle("लाभार्थी डाव्या डोळ्याने अंध आहे. \n" +
                                                    "बरोबर असल्याची खात्री करा.\n" +
                                                    "")
                                            .setIcon(R.drawable.icon_alertred)
                                            .setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            isVisionByUser = true;
                                                            // do something...
                                                            dialog.dismiss();

                                                        }
                                                    }

                                            );

                                    b.show();

                                }


                            }

                        //    tvTotalCount.setText("" + t);


////                            if (adminActiveInactiveModel.getOutput().size() > 0) {
////                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
////                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
////                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
////                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {
//                            rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
//                            tvTotalCount.setText("");
//                            WorkingTeamCount.setText("");
//                            NotWorkingTeamCount.setText("");
//                            TotalTeamCount.setText("");
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
//                        rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
//                        tvTotalCount.setText("");
//                        WorkingTeamCount.setText("");
//                        NotWorkingTeamCount.setText("");
//                        TotalTeamCount.setText("");
                      //  Utilities.showAlertDialog(context, "Fail", message, false);
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


//    ChoosePhoto.setOnClickListener(new View.OnClickListener()
//    {
//        @Override
//        public void onClick (View v){
//        try {
//            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
//            } else {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    });


    private void selectImage() {
        final CharSequence[] options = {"Take a Photo", "Choose from Gallery"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Select Photo");
        builder.setCancelable(false);
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take a Photo")) {
                file = new File(serviceCertiFolder, "doc_image.png");
                photoURI = Uri.fromFile(file);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                int randomEndtNo = (int) (Math.random() * 99999 + 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "doc_image.png");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    photoURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);
                }


            } else if (options[item].equals("Choose from Gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });
        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }




}