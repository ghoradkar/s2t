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
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.chaos.view.PinView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.HealthCheckup;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.abha.activities.ABHACreationActivity;
import com.myhindlab.abkat.abha.activities.ABHACreationViaDemoAuthActivity;
import com.myhindlab.abkat.abha.activities.ABHAHealthIDActivity;
import com.myhindlab.abkat.abha.activities.ABHAPatientQueueActivity;
import com.myhindlab.abkat.abha.models.ABHAQRCodeResponse;
import com.myhindlab.abkat.abha.models.ABHASessionModel;
import com.myhindlab.abkat.abha.models.SendOTPRequestModel;
import com.myhindlab.abkat.abha.models.abha_error.ABHAErrorResponseModel;
import com.myhindlab.abkat.abha.models.account_profile.AccountProfileABHAAddress;
import com.myhindlab.abkat.abha.models.account_profile.AccountProfileResponseModel;
import com.myhindlab.abkat.abha.models.verify_mobile_otp.AuthData;
import com.myhindlab.abkat.abha.models.verify_mobile_otp.Otp;
import com.myhindlab.abkat.abha.models.verify_mobile_otp.VerifyMobileOTPRequestModel;
import com.myhindlab.abkat.abha.utilities.RSAUtil;
import com.myhindlab.abkat.activities.AttendanceMarkedPatients_Activity;
import com.myhindlab.abkat.activities.BarcodeScanner_Activity;
import com.myhindlab.abkat.activities.CampClosingActivity;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgementNewActivity;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgement_Activity;
import com.myhindlab.abkat.activities.PatientFingerAndSignature_Activity;
import com.myhindlab.abkat.activities.PostCampThumbSignatureUpload_Activity;
import com.myhindlab.abkat.activities.TeamCampMappingActivity;
import com.myhindlab.abkat.activities.attendance_details.model.BeneficiaryConsentModel;
import com.myhindlab.abkat.activities.attendance_details.model.BeneficiaryStatusModel;
import com.myhindlab.abkat.activities.attendance_details.model.CampConfirmationModel;
import com.myhindlab.abkat.activities.calling_dashboard.adapter.RationCardPhotoAdapter;
import com.myhindlab.abkat.activities.campApproval.CampApprovalActivity;
import com.myhindlab.abkat.activities.campApproval.adapter.SiteListAdapter;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.GetMobileNumberModel;
import com.myhindlab.abkat.activities.re_registration.model.MaritalStatusModel;
import com.myhindlab.abkat.activities.re_registration.model.RejectionDetailsModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.SiteListModel;
import com.myhindlab.abkat.adapters.Dependent_Adapter;
import com.myhindlab.abkat.adapters.doortodoor.DependentSearchAdapter;
import com.myhindlab.abkat.appointment_confirmation.AppoinmentConfirmationActivity;
import com.myhindlab.abkat.camera.CameraActivity;
import com.myhindlab.abkat.camera.utils.CaptureMode;
import com.myhindlab.abkat.databinding.ActivityD2dPatientRegistrationBinding;
import com.myhindlab.abkat.facedetection.FaceDetectionActivity;
import com.myhindlab.abkat.fragments.CampBeneficiaryList_Fragment;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DependentModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.FaceDetectionModel;
import com.myhindlab.abkat.models.GetDependentListModel;
import com.myhindlab.abkat.models.GetInitiatedByListForCampModel;
import com.myhindlab.abkat.models.GpModel;
import com.myhindlab.abkat.models.HomeLabHublabOnLandingLabModel;
import com.myhindlab.abkat.models.NewCampTypeModel;
import com.myhindlab.abkat.models.PatientDetailsModel;
import com.myhindlab.abkat.models.RationCardPhotoModel;
import com.myhindlab.abkat.models.RealationCheckModel;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.doortodoor.AgeCalculation;
import com.myhindlab.abkat.models.doortodoor.DocumentTypeModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.pojos.doortodoor.DependentPojo;
import com.myhindlab.abkat.pojos.doortodoor.LiveHealthRequest;
import com.myhindlab.abkat.pojos.doortodoor.RelationPojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.GPSTracker;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.PermissionUtil;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.Utility;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tbocwwb.csc_healthcare.abha.models.abha_search.ABHA;
import com.tbocwwb.csc_healthcare.abha.models.abha_search.ABHASearchResponse;
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
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class D2DPatientRegistration_Activity extends AppCompatActivity {
    private String TAG = D2DPatientRegistration_Activity.class.getSimpleName();

    private Context context;
    private UserSessionManager session;
    int reTry = 0;

    private ApiInterface apiService;

    private FusedLocationProviderClient mFusedLocationClient;

    //    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_REQUEST_CODE = 1000;
    private Location mGetedLocation;

    private double latitude = 0.00;
    private double longitude = 0.00;
    private GoogleMap googleM;
    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;


    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private String gpCode = "0";


    private TextView tvHealthCard, txt_versionNumber, txt_beneficiary_married_unmarried, tvGoogleLocation, tvRegdNo, tv_current_lat_long, tv_office_lat_long;
    private LinearLayout ll_usersdetails, mainCbLL, rbll;
    private boolean isOtpVerify = false;

    private MaterialEditText edt_workerregno, txt_beneficiary_gender_change, edt_fname, txt_beneficiary_Fname, edt_dependent_list, txt_beneficiary_Mname, txt_beneficiary_Lname, edt_moblieno, edt_aadhaarno, edt_renewalDate, edt_taluka, edt_district, edt_dob, edt_age, edt_postOffice, edt_alternetMoblieno, edt_landMark, edt_current_address,
            edt_address, edt_local_address, edt_pincode, edt_renewal_date, edt_relation, edtIdentity, edt_education, txt_beneficiary_name, txt_beneficiary_age, txt_beneficiary_gender;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female, rb_self, rb_spouse, rb_chlid;
    private CircleImageView imv_patient;

    private boolean isAlternateMessageShown = false;
    private ImageView imv_search, imv_health_card, imv_renewal_form, imv_hiv_concern, imv_self_declaration;
    private LinearLayout ll_renewal_photo, llSelfDeclaration, llHealthCard;
    private RadioGroup rg_gender, rg_worker_Info, rg_selection;
    private Switch sw_renewed;


    private String originalAadhaar = "";
    private String isCellularPhone = "0";
    private boolean isAadhaarVisible = false;

    private boolean isUpdating = false;

    private boolean isVisible = false;

    private boolean isFormatting;

    private Button btn_register, btn_VerifyOtp, btn_VerifyOtpContact;
    private CheckBox cbIsNumberBgs;
    private String isAdmin, otpnumber, teamId = "0", isOld, isManual, organizationId = "0", executivemobileNumber = "", mobileno;

    private RadioButton rg_dep_yes, rb_board, rb_manual, rg_dep_no;
    String regdID = "", registeredPatientregdid = "";
    ImageButton barCodeScannerBtn;
    private String IsPrimaryOTPVerify = "0", IsAdharDataVerify = "0";

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private Uri patientURI, healthCardURI, renewalURI, hivConcernURI;
    private Bitmap patientPicBm = null, healthCardPicBm = null, consentPicBm = null, rationCardPicBm = null, IdentityPicBm = null, renewalPicBm = null, hivConcernBm = null;
    private final int PATIENT_CAMERA_REQUEST =
            0, HEALTHCARD_CAMERA_REQUEST = 200, RENEWAL_CAMERA_REQUEST = 300, HIV_CONCERN_CAMERA_REQUEST = 400, IDENTITY_CAMERA_REQUEST = 500;
    private String userId = "", campId = "", beneficiaryNo, Type, relation, sessionLat = "0.00", sessionLong = "0.00", versionNumber, siteId = "", genderId = "", genderIdNew = "", title = "", Latitude = "", Longitude = "", RegId = "",
            patientImagePath = "", healthCardImagePath = "", consentPath = "", rationCardImagePath = "", renewalImagePath = "", hivletterPath = "", isRenewalFlag = "0", relationId = "20", relationGenderId = "", relationWorker = "", labourage = "", dependentBocId = "0", identityId = "0", idName = "";
    private boolean isPatientPhotoAvailable = false, isHealthCardPhotoAvailable = false, isConsentAvailable = false, isRationCardPhotoAvailable = false, isHivConcernPhotoAvailable = false, isRenewalSlipPhotoAvailable = false;
    private boolean isReregistration = false;


    private ArrayList<RationCardPhotoModel> rationCardPhotoList =
            new ArrayList<>();

    private RationCardPhotoAdapter adapter;


    private File patientPicsFolder;
    private ProgressDialog pd;
    private GPSTracker gps;

    private int imageType = 0;
    private String isDependent = "0";
    private RecyclerView dependent_list_recycler_view;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter, mAdapter1;
    String count = "", DependREGID = "0", flag = "0";
    String isRTPCR = "";
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    private int isManualEntry = 1;
    private boolean isAadhaarVerified = false;
    private boolean isAadhaar = false;
    private boolean isAadhaarMobVerify = false;
    private LinearLayoutCompat llOTP, llUpdatedMobile;
    private RadioGroup rgUpdatedMobile, rgDoYouHaveAadhaar;
    private RadioButton rbYesAadhaar, rbNoAadhaar, rbYesUpdatedMobile, rbNoUpdatedMobile;

    private int distanceInMeter;
    private ImageView imvQrCode;
    private RadioGroup rgRegType;
    private RadioButton rbWithAbha;
    private RadioButton rbWithoutAbha;
    private LinearLayoutCompat llABHA;
    private boolean isABHANoValid = true;
    private String publicKey;
    private String accessToken;
    private TextInputEditText edtOTP11;
    private TextInputEditText edtOTP21;
    private TextInputEditText edtOTP31;
    private TextInputEditText edtOTP41;
    private TextInputEditText edtOTP51;
    private TextInputEditText edtOTP61;
    //    private LinearLayoutCompat llOTP;
    private Button btnGetOTP;
    private Button btnVerify;
    private Button btnResend;
    private Button btnSearchABHA, btnSearchABHAAddress;
    private Button btnSeeCard;
    private UserSessionManager sessionManager;
    private String otp;
    private String txnId;
    private String authToken;
    private TextView tvTimer, tvOTPMsg;
    private boolean isTimerFinished = false;
    private LinearLayoutCompat llOTPTimer, llOTP1;
    private CountDownTimer cdt;
    LinearLayoutCompat svOTPMsg;
    private TextInputEditText edtABHAMobile;
    private RadioGroup rgValidateOption, rgCreateABHAOption;
    private RadioButton rbValidateUsingMobile, rbValidateUsingAadhaar, rbCreateWithDemoAuth, rbCreateWithAadhaarOTP;
    private PinView otpView;
    private ActivityD2dPatientRegistrationBinding binding;
    private LocalBroadcastManager localBroadcastManager;
    int resendOTPCount = 0;
    boolean isRegUsingQueue;
    int whichABHACard = 1;

    String isRecollectionFlag = "0";
    String rejRegdID = "0", regdNo;

    String registerdCampId = "0";
    String isWhatsAppEnabled = "0";
    String maritalStatus = "1";
    String isfaceDetection = "1";
    String talukaId = "0";
    String distLgdCode = "0";
    String IsRegdByCall = "0";
    String beneficaryName = "";
    private ArrayList<DistrictList_Model> districtList;
    String benefBoardName = "";
    String benefBoardGender = "";
    String abhaGender = "";

    private String faceDetectionCompulsory = "0";
    private String isBoardDataCompalsory = "0";

    private LinearLayout LL_skipFaceDetection;

    int selectedFindABHAIndex = 0;

    private Switch switch_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityD2dPatientRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_SECURE
//        );
//
//        getWindow().setFlags(
//                0,
//                WindowManager.LayoutParams.FLAG_SECURE
//        );


//        setContentView(R.layout.activity_d2d_patient_registration);

//        try {
//            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.map);
//            mapFragment.getMapAsync(this);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        init();
        disableViewAfterFill();
//        requestPermission();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();

    }

//    private void verifyOtp(String mno, String otp) {
//        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.prompot_verify_otp, null);
//        alertBuilder.setView(alertLayout);
//        TextInputEditText edt_Otp = alertLayout.findViewById(R.id.edt_Otp);
//        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
//        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);
//        alertBuilder.setCancelable(false);
//        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
//            }
//        });
//
//
//        AlertDialog alertDialog = alertBuilder.create();
//        alertDialog.show();
//
//        verifyOtpBtn.setOnClickListener(view -> {
//            //verify api call
//
//            if (edt_Otp.getText().toString().trim().matches("")) {
//                edt_Otp.setError("Please enter otp");
//                return;
//            }
////            if (edt_Otp.getText().toString().trim().length() != 10) {
////                edt_Otp.setError("Please enter valid otp");
////                return;
////            }
//
//
////            if (!edt_Otp.getText().toString().trim().equals(otpnumber)) {
////                edt_Otp.setError("Entered OTP is not matched");
////                return;
////            }
//            if (cbIsNumberBgs.isChecked()) {
//                new VerifyOtp(alertDialog).execute(edt_alternetMoblieno.getText().toString().trim(), edt_Otp.getText().toString().trim());
//            } else {
//                new VerifyOtpContact(alertDialog).execute(edt_moblieno.getText().toString(), edt_Otp.getText().toString().trim());
//            }
//

    /// /            Utilities.showToastMessage("OTP Verified Successfully", context, true);
    /// /            btn_register.setVisibility(View.VISIBLE);
//
//
//            //
//            // alertDialog.dismiss();
//        });
//
//        resendOtpBtn.setOnClickListener(view -> {
//            //verify api call
//            // alertDialog.dismiss();
//            if (cbIsNumberBgs.isChecked()) {
//                new GetOtp(2).execute(edt_alternetMoblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0");
//                edt_Otp.setText("");
//
//            } else {
//                new GetOtp(2).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0");
//                edt_Otp.setText("");
//            }
//        });
//
//    }
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
        alertBuilder.setPositiveButton("Cancel", (dialogInterface, i) -> {
            alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        startResendTimer(resendOtpBtn, tv_timer); // Updated to pass TextView

        verifyOtpBtn.setOnClickListener(view -> {
            if (edt_Otp.getText().toString().trim().isEmpty()) {
                edt_Otp.setError("Please enter otp");
                return;
            }


            new VerifyOtpContact(alertDialog).execute(edt_moblieno.getText().toString(), edt_Otp.getText().toString().trim());

            // OTP Successfully Verified!

        });

        resendOtpBtn.setOnClickListener(view -> {

            new GetOtp(2).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString().trim(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);
            edt_Otp.setText("");

            resendOtpBtn.setVisibility(View.GONE);
            startResendTimer(resendOtpBtn, tv_timer);
        });
    }


    private void verifyOtpForAlternate(String mno, String otp) {
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
        alertBuilder.setPositiveButton("Cancel", (dialogInterface, i) -> {
            alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        startResendTimerForAlternate(resendOtpBtn, tv_timer); // Updated to pass TextView

        verifyOtpBtn.setOnClickListener(view -> {
            if (edt_Otp.getText().toString().trim().isEmpty()) {
                edt_Otp.setError("Please enter otp");
                return;
            }

            new VerifyOtp(alertDialog).execute(edt_alternetMoblieno.getText().toString().trim(), edt_Otp.getText().toString().trim());

        });

        resendOtpBtn.setOnClickListener(view -> {

            new GetOtpForAlternate(2).execute(edt_alternetMoblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);
            edt_Otp.setText("");


            resendOtpBtn.setVisibility(View.GONE);
            startResendTimerForAlternate(resendOtpBtn, tv_timer);
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


                btn_VerifyOtpContact.setEnabled(false);
//                btn_VerifyOtp.setEnabled(false);

            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                resendOtpBtn.setVisibility(View.VISIBLE);
                btn_VerifyOtpContact.setEnabled(true);
//                btn_VerifyOtp.setEnabled(true);
            }
        }.start();

    }


    private void startResendTimerForAlternate(Button resendOtpBtn, TextView tv_timer) {
        tv_timer.setVisibility(View.VISIBLE); // Show timer


        new CountDownTimer(120 * 1000, 1000) { // 120 seconds
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tv_timer.setText("Resend OTP in : " + String.valueOf(secondsRemaining) + " sec");


//                btn_VerifyOtpContact.setEnabled(false);
                btn_VerifyOtp.setEnabled(false);


            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                resendOtpBtn.setVisibility(View.VISIBLE);
//                btn_VerifyOtpContact.setEnabled(true);
                btn_VerifyOtp.setEnabled(true);
            }
        }.start();

    }


    private void init() {
        context = D2DPatientRegistration_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        gps = new GPSTracker(context);

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("set_abha_data");
        localBroadcastManager.registerReceiver(abhaReceiver, intentFilter);

        IntentFilter tokenIntentFilter = new IntentFilter("set_token");
        localBroadcastManager.registerReceiver(abhaTokenReceiver, tokenIntentFilter);
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
//            return;
//        }
//        getLastLocation();


        apiService = ApiClient.MahabocwAPICall().create(ApiInterface.class);


        adapter = new RationCardPhotoAdapter(
                this,
                rationCardPhotoList
        );

        binding.recyclerPhotos.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.recyclerPhotos.setAdapter(adapter);

        binding.edtRationCard.setEnabled(false);


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
        txt_beneficiary_married_unmarried = findViewById(R.id.txt_beneficiary_married_unmarried);
        rg_selection = findViewById(R.id.rg_selection);
        llHealthCard = findViewById(R.id.llHealthCard);
        tvHealthCard = findViewById(R.id.tvHealthCard);
        edt_current_address = findViewById(R.id.edt_current_address);
        edt_landMark = findViewById(R.id.edt_landMark);
        edt_alternetMoblieno = findViewById(R.id.edt_alternetMoblieno);
        rb_self = findViewById(R.id.rb_self);
        rb_spouse = findViewById(R.id.rb_spouse);
        txt_beneficiary_Fname = findViewById(R.id.txt_beneficiary_Fname);
        edt_dependent_list = findViewById(R.id.edt_dependent_list);
        txt_beneficiary_Mname = findViewById(R.id.txt_beneficiary_Mname);
        txt_beneficiary_Lname = findViewById(R.id.txt_beneficiary_Lname);
        txt_beneficiary_age = findViewById(R.id.txt_beneficiary_age);
        txt_beneficiary_gender = findViewById(R.id.txt_beneficiary_gender);
        txt_beneficiary_gender_change = findViewById(R.id.txt_beneficiary_gender_change);
        rb_chlid = findViewById(R.id.rb_chlid);
        rg_selection.check(R.id.rb_self);
        switch_button = findViewById(R.id.switch_button);
        LL_skipFaceDetection = findViewById(R.id.LL_skipFaceDetection);


        tvRegdNo = findViewById(R.id.tvRegdNo);
        txt_versionNumber = findViewById(R.id.txt_versionNumber);
        mainCbLL = findViewById(R.id.mainCbLL);
        cbIsNumberBgs = findViewById(R.id.cbIsNumberBgs);
        tvGoogleLocation = findViewById(R.id.tvGoogleLocation);

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
        rb_board = findViewById(R.id.rb_board);
        rb_manual = findViewById(R.id.rb_manual);
        rg_dep_no = findViewById(R.id.rg_dep_no);
        btn_VerifyOtp = findViewById(R.id.btn_VerifyOtp);
        btn_VerifyOtpContact = findViewById(R.id.btn_VerifyOtpContact);
        barCodeScannerBtn = findViewById(R.id.barcode_scanner_btn);
        tv_current_lat_long = findViewById(R.id.tv_current_lat_long);

        title = "Mr.";
        txt_beneficiary_name = findViewById(R.id.txt_beneficiary_name);


        txt_beneficiary_age.setVisibility(View.GONE);

        imvQrCode = findViewById(R.id.imvQrCode);
        rgRegType = findViewById(R.id.rgRegType);
        rbWithoutAbha = findViewById(R.id.rbWithoutAbha);
        rbWithAbha = findViewById(R.id.rbWithAbha);
        rgValidateOption = findViewById(R.id.rgValidateOption);
        rbValidateUsingMobile = findViewById(R.id.rbValidateUsingMobile);
        rbValidateUsingAadhaar = findViewById(R.id.rbValidateUsingAadhaar);
        edtABHAMobile = findViewById(R.id.edtABHAMobile);
        btnSearchABHA = findViewById(R.id.btnSearchABHA);
        btnSeeCard = findViewById(R.id.btnSeeCard);
        btnSearchABHAAddress = findViewById(R.id.btnSearchABHAAddress);
        rbCreateWithDemoAuth = findViewById(R.id.rbCreateWithDemoAuth);
        rbCreateWithAadhaarOTP = findViewById(R.id.rbCreateWithAadhaarOTP);
        rgCreateABHAOption = findViewById(R.id.rgCreateABHAOption);

        llOTP1 = findViewById(R.id.llOTP1);
        btnGetOTP = findViewById(R.id.btnGetOTP);
        btnVerify = findViewById(R.id.btnValidateABHAOTP);
        btnResend = findViewById(R.id.btnResend);
        tvTimer = findViewById(R.id.tvTimer);
        tvOTPMsg = findViewById(R.id.tvOTPMsg);
        llOTPTimer = findViewById(R.id.llOTPTimer);
        svOTPMsg = findViewById(R.id.svOTPMsg);
        rgValidateOption = findViewById(R.id.rgValidateOption);
        binding.tvCMID.setText(BuildConfig.CMID);
        binding.tvCMID1.setText(BuildConfig.CMID);
//--------------------------------------------------------------------Location---------------------------------------------------------------------------------------------------------------
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(1000); // 10 seconds
        locationRequest.setInterval(5000);        // Desired interval: 5 sec
        locationRequest.setFastestInterval(3000); // Fastest update: 3 sec

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

                    String address = geocode.get(0).getAddressLine(0);

                    tvGoogleLocation.setText(address);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);


//        if (checkPlayServices()) {
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//
//            createLocationRequest();
//        }


//--------------------------------------------------------------------------Location---------------------------------------------------------------------------------------------------------------


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mFusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mGetedLocation = task.getResult();
                            latitude = mGetedLocation.getLatitude();
                            longitude = mGetedLocation.getLongitude();
                            //updateUI();

                            Geocoder geocoder = new Geocoder(context);
                            try {
                                List<Address> geocode = geocoder.getFromLocation(latitude, longitude, 1);

                                String address = geocode.get(0).getAddressLine(0);

                                tvGoogleLocation.setText(address);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("Get Location", "no location detected");
                            Log.w("Get Location", "getLastLocation:exception", task.getException());
                        }
                    }
                });


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
                organizationId = json.getString("SubOrgId");
                executivemobileNumber = json.getString("BMobile");


                getFaceDetectionFlag();


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

        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pinfo.versionName.trim();
            txt_versionNumber.setText(versionName);
            versionNumber = versionName;
        } catch (Exception e) {
            e.printStackTrace();

        }


//        new GetOldNewMethod().execute();
        if (getIntent() != null) {
            beneficiaryNo = getIntent().getStringExtra("beneficiaryNo");
            relation = getIntent().getStringExtra("relation");
            rejRegdID = getIntent().getStringExtra("regId") == null ? "0" : getIntent().getStringExtra("regId");
            registerdCampId = getIntent().getStringExtra("rejCampId") == null ? "0" : getIntent().getStringExtra("rejCampId");
            beneficaryName = getIntent().getStringExtra("beneficiarName");


            Type = getIntent().getStringExtra("Type");


            if (Type != null) {
                if (Type.equalsIgnoreCase("7")) {


                    edt_workerregno.setText(beneficiaryNo);
//                    rg_dep_yes.setEnabled(false);
//                    rg_dep_no.setEnabled(false);


                    edt_workerregno.setEnabled(false);


                    IsRegdByCall = "1";

                    rejRegdID = "0";
                    registerdCampId = "0";


                    new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString());


//                        clearPatientDetails();
//                        disableViewAfterFill();
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
//                        edt_workerregno.setEnabled(true);
                    btn_VerifyOtpContact.setVisibility(View.VISIBLE);
                    mainCbLL.setVisibility(View.GONE);

                    txt_beneficiary_age.setVisibility(View.GONE);
                    txt_beneficiary_gender_change.setVisibility(View.GONE);
                    txt_beneficiary_married_unmarried.setVisibility(View.GONE);
                    txt_beneficiary_Fname.setText("");
                    txt_beneficiary_Mname.setText("");
                    txt_beneficiary_Lname.setText("");


                    rg_gender.setEnabled(true);
                    rb_male.setEnabled(true);
                    rb_female.setEnabled(true);


//                        edt_workerregno.setText("");
                    isDependent = "0";
//                    edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    if (txt_beneficiary_name.getVisibility() == View.VISIBLE)
                        txt_beneficiary_name.setVisibility(View.GONE);
                    relationId = "20";
                    edt_relation.setText("Self");
                    rg_dep_no.setChecked(true);

                    tvHealthCard.setText("Beneficiary Card");
                    edt_aadhaarno.setHint("Aadhaar Number*");
                    edtIdentity.setVisibility(View.GONE);
                    tvRegdNo.setVisibility(View.GONE);


//                    llSelfDeclaration.setVisibility(View.GONE);
//                    llHealthCard.setVisibility(View.VISIBLE);


                }

            }

            if (Type != null) {
                if (Type.equalsIgnoreCase("5")) {

                    isRecollectionFlag = "1";

                    rbWithAbha.setEnabled(false);

                    if (relation.equals("Self")) {
                        edt_workerregno.setText(beneficiaryNo);
                        rg_dep_yes.setEnabled(false);
                        rg_dep_no.setEnabled(false);


                        edt_workerregno.setEnabled(false);


//                        clearPatientDetails();
//                        disableViewAfterFill();
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
//                        edt_workerregno.setEnabled(true);
                        btn_VerifyOtpContact.setVisibility(View.GONE);
                        mainCbLL.setVisibility(View.GONE);

                        txt_beneficiary_age.setVisibility(View.GONE);
                        txt_beneficiary_gender_change.setVisibility(View.GONE);
                        txt_beneficiary_married_unmarried.setVisibility(View.GONE);
                        txt_beneficiary_Fname.setText("");
                        txt_beneficiary_Mname.setText("");
                        txt_beneficiary_Lname.setText("");

                        rg_gender.setEnabled(true);
                        rb_male.setEnabled(true);
                        rb_female.setEnabled(true);


//                        edt_workerregno.setText("");
                        isDependent = "0";
//                    edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                        if (txt_beneficiary_name.getVisibility() == View.VISIBLE)
                            txt_beneficiary_name.setVisibility(View.GONE);
                        relationId = "20";
                        edt_relation.setText("Self");
                        rg_dep_no.setChecked(true);

                        tvHealthCard.setText("Beneficiary Card");
                        edt_aadhaarno.setHint("Aadhaar Number*");
                        edtIdentity.setVisibility(View.GONE);
                        tvRegdNo.setVisibility(View.GONE);

//                    llSelfDeclaration.setVisibility(View.GONE);
//                    llHealthCard.setVisibility(View.VISIBLE);


                        new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString());


                    } else {


                        enableViewAfterFill();


                        edt_fname.setVisibility(View.GONE);
                        {
                            edt_fname.setText("");
                        }

                        /// shashank 30/01/26
                        txt_beneficiary_Fname.setText(beneficaryName);

                        txt_beneficiary_Fname.setEnabled(false);
//                        txt_beneficiary_Fname.set("Full Name");
                        txt_beneficiary_Fname.setHint("Full Name");

                        txt_beneficiary_Fname.setHint("Full Name");
                        txt_beneficiary_Fname.setFloatingLabelText("Full Name");
                        txt_beneficiary_Fname.invalidate();

                        txt_beneficiary_Mname.setVisibility(View.VISIBLE);
                        txt_beneficiary_Lname.setVisibility(View.VISIBLE);

                        edt_workerregno.setText(beneficiaryNo);
                        edt_workerregno.setEnabled(false);

                        rg_dep_yes.setEnabled(false);
                        rg_dep_no.setEnabled(false);

//                        clearPatientDetails();
//                        enableViewAfterFill();
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


                        btn_register.setText("Verify Beneficiary Details");

                        /// Dependent change visible to gone
                        mainCbLL.setVisibility(View.GONE);

//                        txt_beneficiary_Fname.setText("");
//                        txt_beneficiary_Fname.setEnabled(true);


                        txt_beneficiary_Mname.setText("");
                        txt_beneficiary_Mname.setEnabled(true);
                        txt_beneficiary_Lname.setText("");


                        /// Visible to Gone
                        txt_beneficiary_gender_change.setVisibility(View.GONE);
                        txt_beneficiary_married_unmarried.setVisibility(View.VISIBLE);


//
//                        edt_workerregno.setText("");
//                        edt_workerregno.setEnabled(true);

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
                        edt_aadhaarno.setHint("Aadhaar Number");


                        new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString());

                    }


                }

            }

        }


        if (Type != null) {
            if (Type.equalsIgnoreCase("6")) {
                rejRegdID = "0";
                registerdCampId = "0";
                IsRegdByCall = "0";
            }

        }


        if (Type == null) {
            Type = "0";
            rejRegdID = "0";
            registerdCampId = "0";
            IsRegdByCall = "0";
        }

        campId = getIntent().getStringExtra("campId");
        siteId = getIntent().getStringExtra("siteId");
        isRTPCR = getIntent().getStringExtra("IsRtpcr");

        if (Type.equalsIgnoreCase("0")) {
            edt_relation.setText("Self");

        }

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mYear1 = cal.get(Calendar.YEAR);
        mMonth1 = cal.get(Calendar.MONTH);
        mDay1 = cal.get(Calendar.DAY_OF_MONTH);

//        edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

        if (session.isHllUser()) {
            new GetTeamId().execute();
        }


        edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim());
    }

    private void setEventHandler() {


        binding.ConsentCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.ConsentCheckbox.isChecked()) {

                    binding.llConsentImage.setVisibility(View.VISIBLE);

                    isCellularPhone = "1";
                } else {

                    binding.llConsentImage.setVisibility(View.GONE);

                    isCellularPhone = "0";


                }


            }
        });

        binding.radioRural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.edtGp.setVisibility(View.VISIBLE);

                binding.edtGp.setText("");

                gpCode = "";

            }
        });


        binding.radioUrban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.edtGp.setVisibility(View.GONE);

                gpCode = "0";

            }
        });

        binding.edtGp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_workerregno.getText().toString().trim().equals("")) {
                    Utilities.showAlertDialog(context, "Alert", "Please search worker number first", false);
                    return;
                }


                if (edt_taluka.getText().toString().trim().equals("")) {
                    Utilities.showAlertDialog(context, "Alert", "Please select taluka", false);
                    return;
                }


                getGramPanchayat();


            }
        });


        edt_aadhaarno.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_UP) {

                if (event.getRawX() >= (edt_aadhaarno.getRight()
                        - edt_aadhaarno.getCompoundDrawables()[2].getBounds().width())) {

                    isVisible = !isVisible;

                    isUpdating = true;

                    if (isVisible) {

                        // Show full Aadhaar
                        edt_aadhaarno.setText(originalAadhaar);

                        // Change icon to eye OFF
                        edt_aadhaarno.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_baseline_eye_24,
                                0
                        );

                        Log.d(TAG, "Original Aadhaar after click" + " " + originalAadhaar);

                    } else {

                        // Show masked Aadhaar
                        edt_aadhaarno.setText(maskAadhaar(originalAadhaar));

                        // Change icon to eye ON
                        edt_aadhaarno.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_eyeclosed,
                                0
                        );

                        Log.d(TAG, "Original Aadhaar unclick click" + " " + originalAadhaar);

                    }

                    edt_aadhaarno.setSelection(
                            edt_aadhaarno.getText().length()
                    );

                    isUpdating = false;

                    return true;
                }
            }

            return false;
        });


        edt_aadhaarno.addTextChangedListener(new TextWatcher() {

            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                String currentText = s.toString();

                // Typing
                if (currentText.length() > previousText.length()) {

                    char newChar = currentText.charAt(currentText.length() - 1);

                    if (Character.isDigit(newChar) && originalAadhaar.length() < 12) {

                        originalAadhaar = originalAadhaar + newChar;
                    }

                }
                // Delete
                else if (currentText.length() < previousText.length()) {

                    if (!originalAadhaar.isEmpty()) {

                        originalAadhaar = originalAadhaar.substring(0,
                                originalAadhaar.length() - 1);
                    }
                }

                edt_aadhaarno.setText(maskAadhaar(originalAadhaar));
                edt_aadhaarno.setSelection(
                        edt_aadhaarno.getText().length()
                );

                isUpdating = false;

                Log.d(TAG, "Original Aadhaar" + " " + originalAadhaar);
            }
        });

//        edt_aadhaarno.addTextChangedListener(new TextWatcher() {
//
//            private String previousText = "";
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                previousText = s.toString();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (isUpdating || isVisible) {
//                    return;
//                }
//
//                isUpdating = true;
//
//                String currentText = s.toString();
//
//                // remove non digits
//                originalAadhaar = currentText.replaceAll("[^0-9]", "");
//
//                // limit 12 digits
//                if (originalAadhaar.length() > 12) {
//                    originalAadhaar = originalAadhaar.substring(0, 12);
//                }
//
//                // Show normal typing until 12 digits
//                if (originalAadhaar.length() == 12) {
//
//                    String masked = maskAadhaar(originalAadhaar);
//
//                    edt_aadhaarno.setText(masked);
//
//                    edt_aadhaarno.setSelection(masked.length());
//                }
//
//                isUpdating = false;
//
//                Log.d(TAG, "Original Aadhaar " + originalAadhaar);
//            }
//        });


        rgCreateABHAOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                clearABHA();
                clearPatientDetails(0);
                resetOTPUI();
            }
        });

        binding.edtDependentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edt_workerregno.getText().toString().trim().equals("")) {
                    Utilities.showAlertDialog(context, "Alert", "Please search Labour No. first", false);
                    return;
                }


                if (flag.equals("0")) {
                    Utilities.showAlertDialog(context, "Alert", "Please search Labour No. first and then fill remaining details of dependant", false);
                    return;
                }


                if (txt_beneficiary_married_unmarried.getText().toString().trim().equals("")) {
                    Utilities.showAlertDialog(context, "Alert", "Please select marital status", false);
                    return;
                }


                if (txt_beneficiary_gender_change.getText().toString().trim().equals("")) {
                    Utilities.showAlertDialog(context, "Alert", "Please select worker gender", false);
                    return;
                }


                getDependentList();

            }
        });


        switch_button.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch is ON
//                Toast.makeText(this, "Switch is ON", Toast.LENGTH_SHORT).show();
                isfaceDetection = "0";
            } else {
//                // Switch is OFF
//                Toast.makeText(this, "Switch is OFF", Toast.LENGTH_SHORT).show();

                isfaceDetection = "1";

            }
        });


        txt_beneficiary_married_unmarried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<CampTypeModel> campTypeModelArrayLists = new ArrayList<>();
//                campTypeModelArrayLists.add(new CampTypeModel("Married", 1));
//                campTypeModelArrayLists.add(new CampTypeModel("UnMarried", 2));
//                //                campTypeModelArrayList.add(new CampTypeModel("CSC", 2));
//
//                showMatitalStatus(campTypeModelArrayLists);

                new GetMaritalStatusList().execute();


            }
        });


        edt_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context))
                    new GetTaluka().execute("2", distLgdCode);
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            }
        });
        edt_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context))
                    new GetDistrictList().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            }
        });
        btnSeeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(
                                context,
                                ABHAHealthIDActivity.class
                        )
                                .putExtra("verificationCard", whichABHACard)
                                .putExtra("accessToken", accessToken)
                                .putExtra("txnId", txnId)
                                .putExtra("authToken", authToken)
                                .putExtra("campId", getIntent().getStringExtra("campId"))
                                .putExtra("district", getIntent().getStringExtra("district"))
                                .putExtra("distlgdcode", getIntent().getStringExtra("distlgdcode"))
                                .putExtra("siteId", getIntent().getStringExtra("siteId"))
                                .putExtra("Latitude", getIntent().getStringExtra("Latitude"))
                                .putExtra("Longitude", getIntent().getStringExtra("Longitude"))
                                .putExtra("campType", getIntent().getStringExtra("campType"))
                );
            }
        });
        binding.edtABHAAadhaar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.rbFindABHA.isChecked() && rbValidateUsingAadhaar.isChecked()) {

                    if (binding.edtABHAAadhaar.getText().toString().isEmpty() == true) {
                        binding.ilABHAAadhaar.setError("Please enter aadhaar number");
                        return;

                    } else {
                        binding.ilABHAAadhaar.setError(null);
                    }

                    if (binding.edtABHAAadhaar.getText().toString().length() != 12) {
                        binding.ilABHAAadhaar.setError("Please enter aadhaar number");
                        return;

                    } else {
                        binding.ilABHAAadhaar.setError(null);
                    }
                } else {
                    binding.ilABHAAadhaar.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edtABHAMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.rbFindABHA.isChecked() && binding.rbValidateUsingMobile.isChecked()) {

                    if (binding.edtABHAMobile.getText().toString().isEmpty() == true) {
                        binding.ilABHAMobile.setError("Please enter mobile number");
                        return;

                    } else {
                        binding.ilABHAMobile.setError(null);
                    }

                    if (!Utilities.isValidMobileno(edtABHAMobile.getText().toString())) {
                        binding.ilABHAMobile.setError("Please enter mobile number");
                        return;

                    } else {
                        binding.ilABHAMobile.setError(null);
                    }
                } else {
                    binding.ilABHAMobile.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.btnClearABHAAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearABHA();
                clearPatientDetails(3);
                disableIfFilled();
                createABHASession();
            }
        });

        binding.tvSeeQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearABHA();
                clearPatientDetails(3);
                startActivity(new Intent(context, ABHAPatientQueueActivity.class).putExtra("campId", getIntent().getStringExtra("campId")));
//                finish();
            }
        });
        binding.btnCreateABHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPatientDetails(3);
                clearABHA();


                if (rg_dep_yes.isChecked()) {



                    if (binding.edtDependentList.getText().toString().trim().matches("")) {
                        binding.edtDependentList.setError("Please Select Dependent");
                        return;
                    }
                    /// shashank 18/06/26
                    if (edt_relation.getText().toString().isEmpty()) {
                        Utilities.showMessageString("Please select relation", context);
                        return;
                    }





                }


                if (rbCreateWithDemoAuth.isChecked()) {
                    startActivity(new Intent(context, ABHACreationViaDemoAuthActivity.class));
                } else {
                    startActivity(new Intent(context, ABHACreationActivity.class)
                            .putExtra("campId", getIntent().getStringExtra("campId"))
                            .putExtra("district", getIntent().getStringExtra("district"))
                            .putExtra("distlgdcode", getIntent().getStringExtra("distlgdcode"))
                            .putExtra("siteId", getIntent().getStringExtra("siteId"))
                            .putExtra("Latitude", getIntent().getStringExtra("Latitude"))
                            .putExtra("Longitude", getIntent().getStringExtra("Longitude"))
                            .putExtra("campType", getIntent().getStringExtra("campType")));
                }

            }
        });
        binding.edtABHANumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtABHAAddress.getText().clear();
                if (binding.edtABHAAddress.getText().toString().isEmpty()) {
                    btnSearchABHAAddress.setVisibility(View.GONE);
                    btnSearchABHA.setVisibility(View.VISIBLE);
                    binding.btnSearchABHA.setText("Search ABHA");

                    rgValidateOption.setVisibility(View.VISIBLE);

                } else {
                    btnSearchABHAAddress.setVisibility(View.VISIBLE);
                    btnSearchABHA.setVisibility(View.GONE);
                    rgValidateOption.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtABHAAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtABHANumber.getText().clear();
                if (binding.edtABHAAddress.getText().toString().isEmpty()) {
                    btnSearchABHAAddress.setVisibility(View.GONE);
                    btnSearchABHA.setVisibility(View.VISIBLE);
                    rgValidateOption.setVisibility(View.VISIBLE);
                    binding.btnSearchABHA.setText("Search ABHA");

                } else {
                    btnSearchABHAAddress.setVisibility(View.VISIBLE);
                    btnSearchABHA.setVisibility(View.GONE);
                    rgValidateOption.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendOTPCount == 3) {
                    Utilities.showAlertDialog(
                            context,
                            "Restricted",
                            "You have reached OTP resend limit",
                            false
                    );
                    return;
                }

                if (binding.rbFindABHA.isChecked() && rbValidateUsingMobile.isChecked()) {
//                    sendMobileOTPIndex(String.valueOf(selectedFindABHAIndex));
//                    sendMobileOTP();
                    findABHA();
                    return;
                }
                if (!binding.edtABHAAddress.getText().toString().isEmpty()) {
//                    searchByABHAAddress();
                    sendOTPABHAAddress();
                } else {
                    sendMobileOTP();
                }
            }
        });
        btnSearchABHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /// shashank 21/01/26

                if (rg_dep_yes.isChecked()) {
//                    if (txt_beneficiary_Fname.getText().toString().isEmpty()) {
//                        Utilities.showMessageString("Please enter first name", context);
//                        return;
//                    }
//
//                    if (txt_beneficiary_Mname.getText().toString().isEmpty()) {
//                        Utilities.showMessageString("Please enter middle name", context);
//                        return;
//                    }


                    if (isDependent.equals("1")) {
                        if (binding.edtDependentList.getText().toString().trim().matches("")) {
                            binding.edtDependentList.setError("Please Select Dependent");
                            return;
                        }
                    }

                    if (edt_relation.getText().toString().isEmpty()) {
                        Utilities.showMessageString("Please select relation", context);
                        return;
                    }


                }


                if (binding.rbABHAVerify.isChecked()) {
                    if (binding.edtABHAAddress.getText().toString().isEmpty() && binding.edtABHANumber.getText().toString().isEmpty()) {
                        Utilities.showMessageString("Please enter abha address or abha number", context);
                        return;
                    }
                }

                if (!rbValidateUsingAadhaar.isChecked() && !rbValidateUsingMobile.isChecked()) {
                    Utilities.showMessageString("Please select validation type Mobile or Aadhaar", context);
                    return;
                }

                if (binding.rbFindABHA.isChecked()) {
                    if (binding.rbValidateUsingAadhaar.isChecked() && binding.edtABHAAadhaar.getText().toString().length() != 12) {
                        Utilities.showMessageString("Please enter valid Aadhaar Number", context);
                        return;
                    }

                    if (rbValidateUsingMobile.isChecked() && !Utilities.isValidMobileno(edtABHAMobile.getText().toString())) {
                        Utilities.showMessageString("Please enter valid Mobile Number", context);

                        return;
                    }
                }


                if (!binding.edtABHAAddress.getText().toString().isEmpty()) {
//                    searchByABHAAddress();
                    sendOTPABHAAddress();

                } else if (binding.rbFindABHA.isChecked() && rbValidateUsingMobile.isChecked()) {
                    findABHA();
                } else {
                    sendMobileOTP();
                }
            }
        });

        binding.btnSearchABHAAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.edtABHAAddress.getText().toString().isEmpty()) {
                    searchByABHAAddress();
                }

            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.edtABHAAddress.getText().toString().isEmpty()) {
                    verifyOTPABHAAddress();
                } else {
                    verifyMobileOTP();
                }

            }
        });

        binding.rgFindOrVerifyABHA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.rbFindABHA.isChecked()) {
                    binding.btnSearchABHA.setText("Search ABHA");
                    btnSeeCard.setVisibility(View.GONE);

                    if (binding.rbValidateUsingMobile.isChecked()) {
                        binding.llVerifyABHA.setVisibility(View.GONE);
                        binding.ilABHAAadhaar.setVisibility(View.GONE);
                        binding.ilABHAMobile.setVisibility(View.VISIBLE);
                        binding.edtABHAMobile.setVisibility(View.VISIBLE);
                        binding.btnSearchABHA.setVisibility(View.VISIBLE);
                        binding.btnClearABHAAddress.setVisibility(View.VISIBLE);

                    } else {
                        binding.llVerifyABHA.setVisibility(View.GONE);
                        binding.ilABHAAadhaar.setVisibility(View.VISIBLE);
                        binding.edtABHAMobile.setVisibility(View.GONE);
                        binding.btnSearchABHA.setVisibility(View.VISIBLE);
                        binding.btnClearABHAAddress.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.btnSearchABHA.setText("Verify ABHA");
                    binding.llVerifyABHA.setVisibility(View.VISIBLE);
                    binding.edtABHAMobile.setVisibility(View.GONE);
                    binding.ilABHAAadhaar.setVisibility(View.GONE);
                    binding.btnClearABHAAddress.setVisibility(View.VISIBLE);
                    binding.edtABHANumber.setVisibility(View.VISIBLE);
                    binding.edtABHAAddress.setVisibility(View.VISIBLE);
                    binding.tvCMID.setVisibility(View.VISIBLE);
                    binding.btnSearchABHA.setVisibility(View.VISIBLE);
                    btnSeeCard.setVisibility(View.VISIBLE);

                }

                clearABHA();
                clearPatientDetails(3);
                resetOTPUI();
            }
        });

        binding.rgValidateOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.rbFindABHA.isChecked()) {
                    if (rbValidateUsingAadhaar.isChecked()) {
                        binding.ilABHAMobile.setVisibility(View.GONE);
                        binding.ilABHAAadhaar.setVisibility(View.VISIBLE);
                        binding.edtABHAAadhaar.setVisibility(View.VISIBLE);
                        binding.edtABHAAadhaar.setEnabled(true);
                        clearABHA();
                        clearPatientDetails(3);
                        resetOTPUI();
                    } else {
                        binding.ilABHAAadhaar.setVisibility(View.GONE);
                        binding.ilABHAMobile.setVisibility(View.VISIBLE);
                        binding.edtABHAMobile.setVisibility(View.VISIBLE);
                        binding.edtABHAMobile.setEnabled(true);
                        clearABHA();
                        clearPatientDetails(3);
                        resetOTPUI();
                    }
                } else {
                    binding.ilABHAMobile.setVisibility(View.GONE);
                    binding.ilABHAAadhaar.setVisibility(View.GONE);
                    clearABHA();
                    clearPatientDetails(1);
                    resetOTPUI();
                }
            }
        });
        imvQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(new Intent(context, BarcodeScanner_Activity.class), 1234);
            }
        });


        rgRegType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId == R.id.rbWithAbha) {

                    binding.btnRegisteredPatient.setVisibility(View.VISIBLE);
                    createABHASession();

                    binding.llABHA.setVisibility(View.VISIBLE);
                    isABHANoValid = false;
                    binding.rgFindOrVerifyABHA.setVisibility(View.VISIBLE);
                    binding.rgValidateOption.setVisibility(View.VISIBLE);
                    binding.edtABHAMobile.setVisibility(View.VISIBLE);
                    binding.rbFindABHA.setChecked(true);
//                    binding.rbValidateUsingMobile.setChecked(true);
                    binding.btnSearchABHA.setVisibility(View.VISIBLE);
//                    binding.edtABHAToken.setText("");
//                    binding.edtABHAToken.setVisibility(View.GONE);
                    clearPatientDetails();
                    clearPatientDetails(0);
                    disableABHAFormAfterFill();
                    binding.btnClearABHAAddress.setEnabled(false);


                    binding.rbOther.setVisibility(View.VISIBLE);


                    disableIfFilled();
//                    llPatientInfo.setVisibility(View.GONE);
//                    trFirstName.setVisibility(View.GONE);
//                    trMiddleName.setVisibility(View.GONE);
//                    trLastName.setVisibility(View.GONE);
//                    trFullName.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.rbWithoutAbha) {
//                    resetView();
                    isABHANoValid = true;

                    binding.rbOther.setVisibility(View.GONE);
                    binding.btnRegisteredPatient.setVisibility(View.GONE);


//                    llAuthModes.setVisibility(View.GONE);
                    binding.llABHA.setVisibility(View.GONE);
                    clearPatientDetails();
                    clearPatientDetails(0);
                    disableIfFilled();
//                    llPatientInfo.setVisibility(View.VISIBLE);
//                    trFirstName.setVisibility(View.VISIBLE);
//                    trMiddleName.setVisibility(View.VISIBLE);
//                    trLastName.setVisibility(View.VISIBLE);
//                    trFullName.setVisibility(View.GONE);
//                    btnSendOTP.setVisibility(View.GONE);
//                    btnVerify.setVisibility(View.GONE);
//                    llOTPTimer.setVisibility(View.GONE);

                }
            }
        });

        barCodeScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
                    btn_VerifyOtpContact.setVisibility(View.VISIBLE);
                    rbll.setVisibility(View.VISIBLE);
                    binding.llMainAlternate.setVisibility(View.VISIBLE);


                } else {

                    btn_VerifyOtp.setVisibility(View.VISIBLE);
                    edt_alternetMoblieno.setVisibility(View.VISIBLE);
                    btn_VerifyOtpContact.setVisibility(View.VISIBLE);
                    rbll.setVisibility(View.GONE);

                    binding.edtAlternetMoblieno.setText("");
                    binding.llMainAlternate.setVisibility(View.GONE);


                }

            }
        });

        btn_VerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //api call api success


                if (isDependent.equals("1")) {
                    if (binding.edtDependentList.getText().toString().trim().matches("")) {
                        binding.edtDependentList.setError("Please Select Dependent");
                        return;
                    }
                }


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


                Dialog dialog;
                dialog = new Dialog(D2DPatientRegistration_Activity
                        .this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_popup_dialoge_for_registration);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                dialog.show();


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                RadioButton radioButtonAlternate = dialog.findViewById(R.id.rg_Alternate_mobile_number);
                RadioButton radioButtonRegister = dialog.findViewById(R.id.rg_register_mobile_number);
                RadioButton radioButtonNoNumber = dialog.findViewById(R.id.rg_whatsApp_not_available);
                Button btnNo = dialog.findViewById(R.id.btnNo);
                TextView tv_message = dialog.findViewById(R.id.tv_message);


                isWhatsAppEnabled = "0";


                String mobileNo = edt_moblieno.getText().toString().trim();
                String textRegister = "Registered Mob No: <b>" + mobileNo + "</b>";
                radioButtonRegister.setText(Html.fromHtml(textRegister));


                String alternate = edt_alternetMoblieno.getText().toString().trim();
                String textAlternate = "Alternate Mob No: <b>" + alternate + "</b>";
                radioButtonAlternate.setText(Html.fromHtml(textAlternate));


                if (alternate.equals("")) {
                    radioButtonAlternate.setEnabled(false);
                }
//                radioButtonRegister.setText("Registered Mob No"+ ":"+ " " +  edt_moblieno.getText().toString().trim());
//                radioButtonAlternate.setText("Alternate Mob No"+ ":" +" "+  edt_alternetMoblieno.getText().toString().trim());
                radioButtonNoNumber.setText("WhatsApp Not Available On Both Number");

                radioButtonRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isWhatsAppEnabled = "1";
                    }
                });


                radioButtonAlternate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isWhatsAppEnabled = "2";
                    }
                });


                radioButtonNoNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isWhatsAppEnabled = "3";
                    }
                });


//                tv_message.setText("Is WhatsApp installed on the OTP-verified mobile number?");
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        isWhatsAppEnabled = "0";

                        if (isWhatsAppEnabled.equals("0")) {
                            Utilities.showAlertDialog(context, "Alert", "Please select field", false);
                            return;
                        }

                        if (radioButtonRegister.isChecked()) {
                            isWhatsAppEnabled = "1";
                        } else if (radioButtonAlternate.isChecked()) {
                            isWhatsAppEnabled = "2";
                        } else if (radioButtonNoNumber.isChecked()) {
                            isWhatsAppEnabled = "3";
                        }

                        dialog.dismiss();


                        new GetOtpForAlternate(1).execute(edt_alternetMoblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);


                    }
                });
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        isWhatsAppEnabled = "0";

                        dialog.dismiss();

                    }
                });


            }
        });

        btn_VerifyOtpContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                mainCbLL.setVisibility(View.VISIBLE);
                //api call api success


                if (isDependent.equals("1")) {
                    if (binding.edtDependentList.getText().toString().trim().matches("")) {
                        binding.edtDependentList.setError("Please Select Dependent");
                        return;
                    }
                }


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


                Dialog dialog;
                dialog = new Dialog(D2DPatientRegistration_Activity
                        .this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_popup_dialoge_for_registration);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                RadioButton radioButtonAlternate = dialog.findViewById(R.id.rg_Alternate_mobile_number);
                RadioButton radioButtonRegister = dialog.findViewById(R.id.rg_register_mobile_number);
                RadioButton radioButtonNoNumber = dialog.findViewById(R.id.rg_whatsApp_not_available);
                Button btnNo = dialog.findViewById(R.id.btnNo);
                TextView tv_message = dialog.findViewById(R.id.tv_message);

                isWhatsAppEnabled = "0";


                String mobileNo = edt_moblieno.getText().toString().trim();
                String textRegister = "Registered Mob No: <b>" + mobileNo + "</b>";
                radioButtonRegister.setText(Html.fromHtml(textRegister));


                String alternate = edt_alternetMoblieno.getText().toString().trim();
                String textAlternate = "Alternate Mob No: <b>" + alternate + "</b>";
                radioButtonAlternate.setText(Html.fromHtml(textAlternate));


                if (alternate.equals("")) {
                    radioButtonAlternate.setEnabled(false);
                }

//                radioButtonRegister.setText("Registered Mob No"+ ":"+ " " +  edt_moblieno.getText().toString().trim());
//                radioButtonAlternate.setText("Alternate Mob No"+ ":" +" "+  edt_alternetMoblieno.getText().toString().trim());
                radioButtonNoNumber.setText("WhatsApp Not Available On Both Number");


                radioButtonRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isWhatsAppEnabled = "1";
                    }
                });
                radioButtonAlternate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isWhatsAppEnabled = "2";
                    }
                });
                radioButtonNoNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isWhatsAppEnabled = "3";
                    }
                });


//                tv_message.setText("Is WhatsApp installed on the OTP-verified mobile number?");
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        isWhatsAppEnabled = "0";

                        if (isWhatsAppEnabled.equals("0")) {
                            Utilities.showAlertDialog(context, "Alert", "Please select field", false);
                            return;
                        }

                        if (radioButtonRegister.isChecked()) {
                            isWhatsAppEnabled = "1";
                        } else if (radioButtonAlternate.isChecked()) {
                            isWhatsAppEnabled = "2";
                        } else if (radioButtonNoNumber.isChecked()) {
                            isWhatsAppEnabled = "3";
                        }

                        dialog.dismiss();


                        new GetOtp(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);


                    }
                });
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        isWhatsAppEnabled = "0";

                        dialog.dismiss();

                    }
                });

            }
        });


        edt_relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rbWithoutAbha.isChecked()) {
                    if (edt_fname.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please select first name", false);
                        return;
                    }
                }


//                if (txt_beneficiary_Lname.getText().toString().isEmpty()) {
//                    Utilities.showAlertDialog(context, "Alert", "Please select last name", false);
//                    return;
//                }

                if (isDependent.equalsIgnoreCase("1")) {
                    if (txt_beneficiary_married_unmarried.getText().toString().matches("")) {
                        Utilities.showAlertDialog(context, "Alert", "Please select marital status", false);
                        return;
                    }

                    new GetRelationList().execute(maritalStatus, relationGenderId);

                }

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

                    /// shashank 21/01/26

                    btn_register.setVisibility(View.GONE);

                    btn_register.setText("Verify Beneficiary Details");


                    edt_current_address.setVisibility(View.VISIBLE);
                    edt_local_address.setVisibility(View.VISIBLE);
                    edt_landMark.setVisibility(View.VISIBLE);
                    edt_taluka.setVisibility(View.VISIBLE);
                    edt_taluka.setEnabled(true);
                    edt_district.setEnabled(true);
                    edt_district.setVisibility(View.VISIBLE);
//                    btn_VerifyOtp.setVisibility(View.VISIBLE);
                    btn_VerifyOtpContact.setVisibility(View.GONE);
//                    btn_register.setVisibility(View.VISIBLE);
//                    btn_VerifyOtp.setVisibility(View.GONE);
//                    edt_alternetMoblieno.setVisibility(View.VISIBLE);

                    rg_gender.setEnabled(true);
                    rb_male.setEnabled(true);
                    rb_female.setEnabled(true);
//                    rbll.setVisibility(View.VISIBLE);

                    /// Dependent change visible to Gone
                    mainCbLL.setVisibility(View.GONE);

                    txt_beneficiary_Fname.setText("");
                    txt_beneficiary_Fname.setEnabled(true);
                    txt_beneficiary_Mname.setText("");
                    txt_beneficiary_Mname.setEnabled(true);
                    txt_beneficiary_Lname.setText("");

                    /// Visible to Gone
                    txt_beneficiary_gender_change.setVisibility(View.GONE);
                    binding.edtDependentList.setVisibility(View.VISIBLE);
                    txt_beneficiary_married_unmarried.setVisibility(View.VISIBLE);


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
                    edt_aadhaarno.setHint("Aadhaar Number");

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


                    /// shashank 080626
                    btn_register.setText("Verify Beneficiary Details");
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

                    txt_beneficiary_age.setVisibility(View.GONE);
                    txt_beneficiary_gender_change.setVisibility(View.GONE);
                    binding.edtDependentList.setVisibility(View.GONE);
                    txt_beneficiary_married_unmarried.setVisibility(View.GONE);
                    txt_beneficiary_Fname.setText("");
                    txt_beneficiary_Mname.setText("");
                    txt_beneficiary_Lname.setText("");
                    binding.edtDependentList.setText("");


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

                    tvHealthCard.setText("Beneficiary Card");
                    edt_aadhaarno.setHint("Aadhaar Number*");
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
                edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim());
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
                edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim());
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
                edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim());
            }
        });

        /// dependent list

//        edt_dob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
////                        AgeCalculation age1 = new AgeCalculation();
////                        age1.setDateOfBirth(year, (month + 1), dayOfMonth);
////                        age1.setCurrentDate();
////
////                        age1.calcualteYear();
////                        age1.calcualteMonth();
////                        age1.calcualteDay();
//
//                        //   int age= Integer.parseInt(age1.getResYear());
////                        int age = Utilities.getAge(Integer.parseInt(String.format("%02d", year)),
////                                Integer.parseInt(String.format("%02d", month)),
////                                Integer.parseInt(String.format("%02d", dayOfMonth)));
////                        if (isDependent.equalsIgnoreCase("0")) {
////                            if (age < 18 || age > 60) {
////                                Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 18 years or more than 60 years", false);
////                                return;
////                            }
////                        } else {
////                            if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8") ||
////                                    relationId.equalsIgnoreCase("5") || relationId.equalsIgnoreCase("6")) {
////                                if (age < 1 || age > 100) {
////                                    Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 1 years or more than 100 years", false);
////                                    return;
////                                }
////                            } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
////                                    || relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
////                                    relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
////                                if (age < 18 || age > 100) {
////                                    Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 18 years or more than 100 years", false);
////                                    return;
////                                }
////                            }
////                        }
//
//                        edt_dob.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, month + 1, year));
//                        // edt_age.setText(String.valueOf(age));
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


                                new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());
//                                getWorkerInfo();
//                                new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());

                                if (isDependent.equalsIgnoreCase("0")) {
                                    if (Type.equalsIgnoreCase("0")) {
                                        new GetRejectionDetails().execute(edt_workerregno.getText().toString().trim(), "0", "0", "", "", "1");
                                    }

                                }


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


        //     edt_dob.addTextChangedListener(textWatcher);
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
//            @Override
//            public void afterTextChanged(Editable s) {
//                    if (!edt_workerregno.getText().toString().trim().equals("")) {
//                        if (edt_workerregno.getText().toString().trim().length() == 12) {
//                            if (isDependent.equalsIgnoreCase("0")) {
//                                if (Utilities.isNetworkAvailable(context)) {
//
//
//                                    if (rb_board.isChecked()){
////                                        new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
//
//                                    }else if (rb_manual.isChecked()){
//                                        new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());
//
//                                    }
//
////                                      new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
//
//
//
////                                if (isOld.equalsIgnoreCase("0")){
////                                    new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
////
////                                }else if (isOld.equalsIgnoreCase("1")){
////                                    new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());
////
////                                }
////                                new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
//                                    edt_workerregno.clearFocus();
//
//                                    //       new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
//
//
//                                } else {
//                                    isReregistration = false;
//                                    edt_workerregno.setText("");
//                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                                }
//                            }
//
////                        else {
////                            if (Utilities.isNetworkAvailable(context)) {
////                                new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());
////                                edt_workerregno.clearFocus();
////                            } else {
////                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
////                            }
////                        }
//                        } else {
//                            edt_workerregno.setError("Enter valid 12 digit worker registration number");
//                        }
//                    }
//
//
//            }
//        });


        binding.edtAlternetMoblieno.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                // Show only one time
                if (!isAlternateMessageShown && s.length() == 1) {

                    isAlternateMessageShown = true;

                    Utilities.showAlertDialog(
                            context,
                            "सूचना",
                            "स्क्रीनिंग प्रक्रियेसाठी पर्यायी मोबाईल क्रमांकाचा वापर केला जाणार नाही, याची नोंद घ्यावी.",
                            false
                    );
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
                                                               edt_dob.setError("Ivalid date of birth");
                                                               return;
                                                           }

                                                           if (clean.length() == 8) {
                                                               if (Utilities.isDateValid(dateStr)) {
                                                                   int year = Integer.parseInt(clean.substring(0, 4));
                                                                   int mon = Integer.parseInt(clean.substring(4, 6));
                                                                   int day = Integer.parseInt(clean.substring(6, 8));

                                                                   if (year < 1925) {
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
//
//                                                                   int age = Integer.parseInt(age1.getResYear());
//                                                                   edt_age.setText(String.valueOf(age));


                                                                   AgeCalculation age1 = new AgeCalculation();
                                                                   age1.setDateOfBirth(year, mon, day); // mon should be 1-based (e.g., Jan = 1, Dec = 12)
                                                                   age1.calculateAge();

                                                                   int age = Integer.parseInt(age1.getResYear());
                                                                   edt_age.setText(String.valueOf(age));


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

                                                                           int maxAllowedAge = 17;
                                                                           int minAge = 10;
//                                                                           int ageDifference = age <= Integer.parseInt(labourage) - 15;

                                                                           if (!(age >= minAge && age <= maxAllowedAge && age <= Integer.parseInt(labourage) - 15)) {
                                                                               Utilities.showAlertDialog(context, "Alert",
                                                                                       "१. नोंदणीकृत बांधकाम कामगाराच्या मुलगा/मुलीचे वय १० वर्षापेक्षा जास्त व १८ वर्षांपर्यंत असावे आणि\n" +
                                                                                               "२. मुलगा/मुलगी आणि नोंदणीकृत बांधकाम कामगार यांच्या वयातील फरक किमान १५ वर्ष असावा.", false);

                                                                               edt_age.setText("");

                                                                               clearDependentData();

//                                                                                    ****Dependent Change***

                                                                               if (rbWithAbha.isChecked()) {
                                                                                   clearPatientDetails();
                                                                                   clearABHA();

                                                                               }


                                                                           } else {
                                                                               edt_age.setText(String.valueOf(age));
                                                                           }
                                                                       } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
                                                                               ||
                                                                               relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
                                                                           if (age < 18 || age > 75) {
                                                                               Utilities.showAlertDialog(context, "Alert", "Dependent age should not be less than 18 years or more than 75 years", false);
                                                                               edt_age.setText("");


                                                                               clearDependentData();
//                                                                               ****Dependent Change***

                                                                               if (rbWithAbha.isChecked()) {
                                                                                   clearPatientDetails();
                                                                                   clearABHA();

                                                                               }


                                                                           } else {
                                                                               edt_age.setText(String.valueOf(age));
                                                                           }
                                                                       } else if (relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2")
                                                                               || relationId.equalsIgnoreCase("21") || relationId.equalsIgnoreCase("22")) {

                                                                           if (!(age >= 18 && age >= Integer.parseInt(labourage) + 1)) {

                                                                               // int a =  Integer.parseInt(labourage)-18;

                                                                               Utilities.showAlertDialog(context, "Alert",
                                                                                       "आई,वडील,सासू,सासरे यांचे वय नोंदणीकृत बांधकाम कामगारापेक्षा जास्त असावे.", false);

                                                                               edt_age.setText("");


                                                                               clearDependentData();
//                                                                                    ****Dependent Change***

                                                                               if (rbWithAbha.isChecked()) {
                                                                                   clearPatientDetails();
                                                                                   clearABHA();

                                                                               }

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


                        if (rb_board.isChecked()) {
//                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

                        } else if (rb_manual.isChecked()) {
                            new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());

                            if (isDependent.equalsIgnoreCase("0")) {
                                if (Type.equalsIgnoreCase("0")) {
                                    new GetRejectionDetails().execute(edt_workerregno.getText().toString().trim(), "0", "0", "", "", "1");

                                }

                            }

                        }
//                        new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

//
//                        if (isOld.equalsIgnoreCase("0")){
//                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
//
//                        }else {
//                            new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());
//
//                        }

//                        enableViewAfterFill();
                    } else {
                        isReregistration = false;
                        edt_workerregno.setText("");
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                } else {

                    if (rb_board.isChecked()) {
//                        new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
                    } else if (rb_manual.isChecked()) {
                        new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());
                    }


//                    if (isOld.equalsIgnoreCase("0")){
//                        new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
//
//                    }else {
//                        new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());
//
//                    }


                }
            }
        });

        imv_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /// shashank 20/01/26
                if (rbWithAbha.isChecked()) {
                    if (isDependent.equalsIgnoreCase("0")) {


//                        if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                            Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                            clearABHA();
////                        binding.btnClearABHAAddress.setEnabled(false);
//                            clearPatientDetails(1);
//                            clearPatientDetails(2);
//                            disableABHAFormAfterFill();
//
//                            ///shashank 20/01/26
//                            clearPatientDetails();
////                            disableAllViews();
//                            return;
//                        }


                        String abhaName = Utilities.normalize(binding.edtFname.getText().toString());
                        String boardName = Utilities.normalize(benefBoardName);

                        if (isDependent.equalsIgnoreCase("0")) {
                            if (!abhaName.equals(boardName)) {
                                Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                                clearABHA();
                                clearPatientDetails(1);
                                clearPatientDetails(2);
                                disableABHAFormAfterFill();

                                /// shashank 22/01/26
                                clearPatientDetails();
                                return;
                            }
                        }


                    } else if (isDependent.equalsIgnoreCase("1")) {

                        String depName = txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim();

                        String abhaName = binding.edtFname.getText().toString().trim();

                        if (!abhaName.equalsIgnoreCase(depName)) {
                            Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\nDetails:\n" + "dependent  Name: " + depName + "\n" + "ABHA Name: " + abhaName + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                            clearABHA();
//                        binding.btnClearABHAAddress.setEnabled(false);
                            clearPatientDetails(1);
                            clearPatientDetails(2);
                            disableABHAFormAfterFill();

                            ///shashank 20/01/26
                            clearPatientDetails();
//                            disableAllViews();
                            return;
                        }

                    }
                }

                imageType = 0;
                Intent cameraIntent = new Intent(context, CameraActivity.class);
                cameraIntent.putExtra("CAPTURE_MODE", CaptureMode.FACE.name());
                cameraLauncherForPatientPhoto.launch(cameraIntent);


//                try {
//                    Intent intent = new Intent(context, FaceDetectionActivity.class);
//                    intent.putExtra("campId", campId);
//                    intent.putExtra("userID", String.valueOf(session.getUserDetailsJson().getEmpCode()));
//                    faceDetectionResult.launch(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Utilities.showAlertDialog(context, "Face Recognition App not installed",
//                            "Please install Face Recognition App first \n" + e.getMessage(), false);
//                }
//                if (SDK_INT < Build.VERSION_CODES.S) {
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
//                        return;
//                    }
//
//                } else {
//                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
//                        return;
//                    }
//                }
//                int randomEndtNo = (int) (Math.random() * 99999 + 1);
//                if (SDK_INT >= Build.VERSION_CODES.Q) {
//                    ContentResolver resolver = context.getContentResolver();
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_PR.png");
//                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
//                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//                    patientURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, patientURI);
//                    startActivityForResult(intent, PATIENT_CAMERA_REQUEST);
//                } else {
//                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_PR.png");
//                    patientURI = Uri.fromFile(patientImageFile);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, patientURI);
//                    startActivityForResult(intent, PATIENT_CAMERA_REQUEST);
//                }

            }
        });


        binding.imvConsentForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent cameraIntent = new Intent(context, CameraActivity.class);
                cameraIntent.putExtra("CAPTURE_MODE", CaptureMode.DOCUMENT.name());

                cameraLauncherForConsentForm.launch(cameraIntent);
            }
        });

        imv_health_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /// shashank 20/01/26
                if (rbWithAbha.isChecked()) {


                    if (isDependent.equalsIgnoreCase("0")) {

//                        if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                            Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                            clearABHA();
////                        binding.btnClearABHAAddress.setEnabled(false);
//                            clearPatientDetails(1);
//                            clearPatientDetails(2);
//                            disableABHAFormAfterFill();
////                            disableAllViews();
//                            return;
//                        }


                        String abhaName = Utilities.normalize(binding.edtFname.getText().toString());
                        String boardName = Utilities.normalize(benefBoardName);

                        if (isDependent.equalsIgnoreCase("0")) {
                            if (!abhaName.equals(boardName)) {
                                Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                                clearABHA();
                                clearPatientDetails(1);
                                clearPatientDetails(2);
                                disableABHAFormAfterFill();

                                /// shashank 22/01/26
                                clearPatientDetails();
                                return;
                            }
                        }


                    } else if (isDependent.equalsIgnoreCase("1")) {

                        String depName = txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim();

                        String abhaName = binding.edtFname.getText().toString().trim();

                        if (!abhaName.equalsIgnoreCase(depName)) {
                            Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "dependent  Name: " + depName + "\n" + "ABHA Name: " + abhaName + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                            clearABHA();
//                        binding.btnClearABHAAddress.setEnabled(false);
                            clearPatientDetails(1);
                            clearPatientDetails(2);
                            disableABHAFormAfterFill();

                            ///shashank 20/01/26
                            clearPatientDetails();
//                            disableAllViews();
                            return;
                        }

                    }
                }

                imageType = 1;
                Intent cameraIntent = new Intent(context, CameraActivity.class);
                cameraIntent.putExtra("CAPTURE_MODE", CaptureMode.ID_CARD.name());
                cameraLauncherForPatientIDCard.launch(cameraIntent);

//
//                if (SDK_INT < Build.VERSION_CODES.S) {
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
//                        return;
//                    }
//
//                } else {
//                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
//                        return;
//                    }
//                }
//
////                int randomEndtNo = (int) (Math.random() * 99999 + 1);
//
//                int randomEndtNo = (int) (Math.random() * 999999 + 1);
//
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
//                    ContentResolver resolver = context.getContentResolver();
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
//                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
//                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//                    healthCardURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
//                    startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);
//                } else {
//                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_HC.png");
//                    healthCardURI = Uri.fromFile(patientImageFile);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
//                    startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);
//                }
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

        binding.imvSelfDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 1;


                if (rationCardPhotoList.size() >= 3) {

                    Utilities.showToastMessage("Maximum 3 photos allowed", context, false);

                    return;
                }


                Intent cameraIntent = new Intent(context, CameraActivity.class);
                cameraIntent.putExtra("CAPTURE_MODE", CaptureMode.DOCUMENT.name());

                cameraLauncherForRationCard.launch(cameraIntent);


//
//                if (SDK_INT < Build.VERSION_CODES.S) {
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
//                        return;
//                    }
//
//                } else {
//                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
//                        return;
//                    }
//                }
//
//                int randomEndtNo = (int) (Math.random() * 999999 + 1);
//
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
//                    ContentResolver resolver = context.getContentResolver();
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
//                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
//                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//                    healthCardURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
//                    startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);
//                } else {
//                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_HC.png");
//                    healthCardURI = Uri.fromFile(patientImageFile);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, healthCardURI);
//                    startActivityForResult(intent, HEALTHCARD_CAMERA_REQUEST);
//                }
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
        edt_renewalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edt_renewalDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, month + 1, year));
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

//
//                if (rbWithAbha.isChecked()){
//
//
//
//                    Dialog dialog;
//                    dialog = new Dialog(D2DPatientRegistration_Activity
//                            .this);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.alert_popup_dialoge_for_registration);
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.setCancelable(true);
//                    dialog.show();
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//                    ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
//                    RadioButton radioButtonAlternate = dialog.findViewById(R.id.rg_Alternate_mobile_number);
//                    RadioButton radioButtonRegister = dialog.findViewById(R.id.rg_register_mobile_number);
//                    RadioButton radioButtonNoNumber = dialog.findViewById(R.id.rg_whatsApp_not_available);
//                    Button btnNo = dialog.findViewById(R.id.btnNo);
//                    TextView tv_message = dialog.findViewById(R.id.tv_message);
//
//                    isWhatsAppEnabled = "0";
//
//
//                    String mobileNo = edt_moblieno.getText().toString().trim();
//                    String textRegister = "Registered Mob No: <b>" + mobileNo + "</b>";
//                    radioButtonRegister.setText(Html.fromHtml(textRegister));
//
//
//                    String alternate = edt_alternetMoblieno.getText().toString().trim();
//                    String textAlternate = "Alternate Mob No: <b>" + alternate + "</b>";
//                    radioButtonAlternate.setText(Html.fromHtml(textAlternate));
//
//
//                    if (alternate.equals("")) {
//                        radioButtonAlternate.setEnabled(false);
//                    }
//
////                radioButtonRegister.setText("Registered Mob No"+ ":"+ " " +  edt_moblieno.getText().toString().trim());
////                radioButtonAlternate.setText("Alternate Mob No"+ ":" +" "+  edt_alternetMoblieno.getText().toString().trim());
//                    radioButtonNoNumber.setText("WhatsApp Not Available On Both Number");
//
//
//                    radioButtonRegister.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            isWhatsAppEnabled = "1";
//                        }
//                    });
//                    radioButtonAlternate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            isWhatsAppEnabled = "2";
//                        }
//                    });
//                    radioButtonNoNumber.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            isWhatsAppEnabled = "3";
//                        }
//                    });
//
//
////                tv_message.setText("Is WhatsApp installed on the OTP-verified mobile number?");
//                    btnNo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                        isWhatsAppEnabled = "0";
//
//                            if (isWhatsAppEnabled.equals("0")) {
//                                Utilities.showAlertDialog(context, "Alert", "Please select field", false);
//                                return;
//                            }
//
//                            if (radioButtonRegister.isChecked()) {
//                                isWhatsAppEnabled = "1";
//                            } else if (radioButtonAlternate.isChecked()) {
//                                isWhatsAppEnabled = "2";
//                            } else if (radioButtonNoNumber.isChecked()) {
//                                isWhatsAppEnabled = "3";
//                            }
//
//                            dialog.dismiss();
//
//
//                            submitData();
//
//
////                    new GetOtp(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0");
//
//
//                        }
//                    });
//                    btnCloseDialog.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                        isWhatsAppEnabled = "0";
//
//                            dialog.dismiss();
//
//                        }
//                    });
//                }else {
//
//
//                    submitData();
//
//                }


                submitData();


            }
        });
    }

    BroadcastReceiver abhaTokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data) {
            try {
                Activity activity = HealthCheckup.getCurrentActivity();
                btnSeeCard.setVisibility(View.GONE);
                isRegUsingQueue = true;
                String token = data.getStringExtra("token");
                Integer identityID = data.getIntExtra("identityID", 0);
                String response = data.getStringExtra("response");
                if (response != null) {
                    Log.i(TAG, "onReceive: " + response);

//                createABHASession();
                    authToken = token;
                    try {

                        btn_register.setEnabled(true);
                        JSONObject responseObj = new JSONObject(response);
                        JSONObject profileObj = responseObj.getJSONObject("profile");
                        JSONObject patientObj = profileObj.getJSONObject("patient");

                        binding.llABHANumAndAddress.setVisibility(View.VISIBLE);
                        binding.edtABHANumber1.setText(patientObj.getString("abhaNumber"));
                        binding.edtABHAAddress1.setText(patientObj.getString("abhaAddress").replace(BuildConfig.CMID, ""));
                        if (!binding.edtABHANumber1.getText().toString().isEmpty()) {
                            binding.edtABHANumber1.setEnabled(false);
                        } else {
                            binding.edtABHANumber1.setEnabled(true);
                        }

                        if (!binding.edtABHAAddress1.getText().toString().isEmpty()) {
                            binding.edtABHAAddress1.setEnabled(false);
                        } else {
                            binding.edtABHAAddress1.setEnabled(true);
                        }
//                    if (binding.edtFname.getText().toString().isEmpty()) {
                        binding.edtFname.setText(patientObj.getString("name"));
//                    if (isDependent.equalsIgnoreCase("0")) {
//                        if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                            Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match", false);
//                            clearABHA();
//                            binding.btnClearABHAAddress.setEnabled(false);
//                            clearPatientDetails(1);
//                            clearPatientDetails(2);
//                            disableABHAFormAfterFill();
////                            disableAllViews();
//                            return;
//                        }
//                    }
//                    }
                        try {
                            String dob = patientObj.getString("yearOfBirth") + "/" + patientObj.getString("monthOfBirth") + "/" + patientObj.getString("dayOfBirth");
                            Date parseDOB = Utilities.dfDate4.parse(dob);
                            binding.edtDob.setText(Utilities.dfDate4.format(parseDOB));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        int age = Utilities.getAge(Integer.parseInt(Objects.requireNonNull(patientObj.getString("yearOfBirth"))), Integer.parseInt(Objects.requireNonNull(patientObj.getString("monthOfBirth"))), Integer.parseInt(Objects.requireNonNull(patientObj.getString("dayOfBirth"))));
                        binding.edtAge.setText(String.valueOf(age));
                        if (patientObj.getString("gender").equalsIgnoreCase("M")) {
                            binding.rbMale.setChecked(true);
                            binding.rgGender.setEnabled(false);
                        } else if (patientObj.getString("gender").equalsIgnoreCase("F")) {
                            binding.rbFemale.setChecked(true);
                            binding.rgGender.setEnabled(false);
                        } else {
//                        binding.rbOther.setChecked(true);
//                        binding.rgGender.setEnabled(false);
                        }

                        try {
                            binding.edtMoblieno.setText(patientObj.getString("phoneNumber"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        JSONObject addressObj = patientObj.getJSONObject("address");
                        if (addressObj.getString("line") != null) {
                            binding.edtAddress.setText(addressObj.getString("line"));
//                        binding.edtLocalAddress.setText(addressObj.getString("line"));
                        }
                        if (addressObj.getString("pincode") != null && !addressObj.getString("pincode").equalsIgnoreCase("null")) {
                            String pincode = addressObj.getString("pincode");
                            binding.edtPincode.setText(pincode);
                            binding.edtAddress.setText(binding.edtAddress.getText().toString() + ", " + pincode);

                        }


//                    if (!binding.edtFname.getText().toString().equalsIgnoreCase(patientObj.getString("name"))) {
//                        if (activity != null && !activity.isFinishing()) {
//                            Utilities.showAlertDialog(activity, "Board and ABHA Details Mismatch", "ABHA and Board details does not match", false);
//                        }
//                        binding.btnClearABHAAddress.setEnabled(false);
//                        clearABHA();
//                        clearPatientDetails(0);
//                        disableAllViews();
//                        disableABHAFormAfterFill();
//
//                    } else {

                        binding.rgFindOrVerifyABHA.setVisibility(View.GONE);
                        binding.rgValidateOption.setVisibility(View.GONE);
                        binding.ilABHAMobile.setVisibility(View.GONE);
                        binding.ilABHAAadhaar.setVisibility(View.GONE);
                        binding.llVerifyABHA.setVisibility(View.GONE);
                        binding.btnSearchABHA.setVisibility(View.GONE);

                        disableIfFilled();

//                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                binding.edtABHAToken.setText(String.valueOf(identityID));
//                binding.edtABHAToken.setEnabled(false);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };


    private String maskAadhaar(String aadhaar) {

        if (aadhaar == null || aadhaar.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        int length = aadhaar.length();

        for (int i = 0; i < length; i++) {

            if (i < 8) {
                builder.append("*");
            } else {
                builder.append(aadhaar.charAt(i));
            }
        }

        return builder.toString();
    }

    private void setMapInfo() {
        googleM.clear();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            tv_current_lat_long.setText(latitude + ", " + longitude + "\n" + address);
            tv_current_lat_long.setPadding(5, 5, 5, 5);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //
        tv_office_lat_long.setText(sessionLat + ", " + sessionLong);
        Log.d("lat-Longi", latitude + ", " + longitude);

        Location officeloc = new Location("");
        officeloc.setLatitude(Double.parseDouble(sessionLat));// sessionLat
        officeloc.setLongitude(Double.parseDouble(sessionLong));// sessionLong

        Location currloc = new Location("");
        currloc.setLatitude(latitude);
        currloc.setLongitude(longitude);

        double distanceinkm = Double.parseDouble(new DecimalFormat("##.##").format(officeloc.distanceTo(currloc) / 1000));
        if (distanceinkm > 1) {
//            tv_distance_in_km.setText(Html.fromHtml("Distance between current location and office location is <font color=\"#D01B31\"> <b> " + distanceinkm + "KM</b></font>"));
        } else {
//            tv_distance_in_km.setText(Html.fromHtml("Distance between current location and office location is <font color=\"#000000\"> <b> " + distanceinkm + "KM</b></font>"));
        }


        MarkerOptions officeMarker = new MarkerOptions();
        BitmapDrawable bitmapdraw1 = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_location_blue);
        LatLng officeLatLng = new LatLng(Double.parseDouble(sessionLat), Double.parseDouble(sessionLong));
        Bitmap smallMarker1 = Bitmap.createScaledBitmap(bitmapdraw1.getBitmap(), 100, 100, false);
        officeMarker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker1));
        officeMarker.position(officeLatLng);
        googleM.addMarker(officeMarker);

        MarkerOptions userMarker = new MarkerOptions();
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_location_yellow);
        LatLng userLatLng = new LatLng(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude)));
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 100, 100, false);
        userMarker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        userMarker.position(userLatLng);
        googleM.addMarker(userMarker);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(6).build();
        googleM.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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


                            /// need to revert


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


                txt_beneficiary_married_unmarried.setText("");
                maritalStatus = "";

                edt_relation.setText("");

                edt_dob.setText("");
                edt_age.setText("");

                clearDependentData();


                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }


    private void getWorkerInfo() {

        int retryMaxCount = 3;

        final ProgressDialog progressDialog = new ProgressDialog(D2DPatientRegistration_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//                .url("https://healthcamp.mahabocw.in/api/beneficiary-details-api/beneficiary-details/MH151600002521")
//                .method("GET", null)
//                .build();
//        okhttp3.Call call = client.newCall(request);
//
//        call.enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
//                progressDialog.dismiss();
//                Log.e("TAG", "onFailure: ", e);
//            }
//
//            @Override
//            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
//                progressDialog.dismiss();
//
//                Log.i("TAG", "onResponse: " + response.body().string());
//
//            }
//        });


        ApiInterface apiInterface = ApiClient.MahabocwAPICall().create(ApiInterface.class);
        apiInterface.getWorkerInfo("MH" + edt_workerregno.getText().toString()).enqueue(new retrofit2.Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                reTry = 0;

                try {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.i("TAG", "onResponse: " + string);

//                    WorkerList workerInfoModel = new Gson().fromJson(result, WorkerList.class);

                        JSONArray jsonArray = new JSONArray(string);

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
//


                                benefBoardGender = gender;

                                new InsertWorkerDetails().execute(jsonArray.toString());

//                                relationGenderId = gender;
//
//                                labourage = age;
//
//
//                                // new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());
//
//                                if (gender != null) {
//                                    if (gender.equalsIgnoreCase("male")) {
//                                        rb_male.setChecked(true);
//                                        rb_female.setChecked(false);
//                                        rb_mr.setChecked(true);
//                                        rb_mrs.setChecked(false);
//                                        rb_ms.setChecked(false);
//
//                                    } else {
//                                        rb_male.setChecked(false);
//                                        rb_female.setChecked(true);
//
//                                        rb_mrs.setChecked(true);
//                                        rb_mr.setChecked(false);
//                                        rb_ms.setChecked(false);
//
//                                    }
//                                }
//
//                                txt_beneficiary_name.setText(fName + " " + mName + " " + lName + " / " + age);
//                                txt_beneficiary_name.setVisibility(View.VISIBLE);
//                                //    txt_beneficiary_age.setText(age + "/" + gender);
//                                //  txt_beneficiary_age.setVisibility(View.VISIBLE);
//                                txt_beneficiary_gender_change.setVisibility(View.VISIBLE);
//                                txt_beneficiary_gender_change.setText(gender);
//
//                                txt_beneficiary_gender.setText(gender);
//                                // txt_beneficiary_gender.setVisibility(View.VISIBLE);
//
//                                if (gender.equalsIgnoreCase("Female")) {
//                                    txt_beneficiary_Mname.setText("");
//                                } else {
//                                    txt_beneficiary_Mname.setText(fName);
//                                }
//
//                                txt_beneficiary_Mname.setEnabled(true);
//                                //  txt_beneficiary_Lname.setVisibility(View.GONE);
//                                txt_beneficiary_Lname.setText(lName);
//                                txt_beneficiary_Lname.setEnabled(false);
//
//
//                                edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim());
//
//
//                                btn_register.setEnabled(true);
//                                edt_workerregno.setEnabled(false);
//
//
//                                edt_district.setText(district);
//                                edt_taluka.setVisibility(View.VISIBLE);
//                                edt_taluka.setText(takuka);
//                                String permAddress = permHouseNo + permArea + "," + permPostOffice + "," + permTaluka + "," + permDist + "," + permState + "," + permPincode;
//                                edt_address.setText(permAddress);
//                                String localAddress = localHouseNo + localArea + "," + localPostOffice + "," + localTaluka + "," + localDist + "," + localState + "," + localPincode;
//                                edt_local_address.setText(localAddress);
//                                edt_current_address.setText(localAddress);
//                                edt_moblieno.setText(mobile);
//                                edt_pincode.setText(localPincode);
//                                edt_landMark.setText(permArea);
//                                edt_postOffice.setText(postoffice);
//
//
//                                if (Utilities.isNetworkAvailable(context)) {
//                                    new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());
//                                } else {
//                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                                }


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
//                                edt_fname.setText(fName + " " + mName + " " + lName);


                                new InsertWorkerDetails().execute(jsonArray.toString());


//                                edt_moblieno.setText(mobile);
//                                edt_aadhaarno.setText(aadhar);
//                                edt_renewalDate.setVisibility(View.VISIBLE);
//                                edt_age.setText(age);
//                                edt_renewalDate.setText(renewalDate);
//                                edt_taluka.setText(takuka);
//                                edt_landMark.setText(permArea);
//                                btn_VerifyOtpContact.setVisibility(View.VISIBLE);
//                                //  rbll.setVisibility(View.VISIBLE);
//                                mainCbLL.setVisibility(View.VISIBLE);
//                                // btn_VerifyOtp.setVisibility(View.VISIBLE);
//
//
//                                txt_beneficiary_Fname.setText(fName);
//                                txt_beneficiary_Fname.setEnabled(false);
//                                txt_beneficiary_Mname.setText(mName);
//                                txt_beneficiary_Mname.setEnabled(false);
//                                // txt_beneficiary_Lname.setVisibility(View.GONE);
//                                txt_beneficiary_Lname.setText(lName);
//                                txt_beneficiary_Lname.setEnabled(false);
//
//
////                            edt_alternetMoblieno.setText(mobile);
//                                edt_postOffice.setText(postoffice);
//                                edt_postOffice.setVisibility(View.VISIBLE);
//                                edt_current_address.setVisibility(View.VISIBLE);
//                                //  edt_alternetMoblieno.setVisibility(View.VISIBLE);
//                                edt_landMark.setVisibility(View.VISIBLE);
//                                btn_VerifyOtp.setEnabled(true);
//                                edt_alternetMoblieno.setEnabled(true);
//                                edt_local_address.setVisibility(View.VISIBLE);
//                                edt_workerregno.setEnabled(false);
//
//
//                                edt_district.setText(district);
//                                edt_district.setVisibility(View.VISIBLE);
//
//                                if (district.equalsIgnoreCase("")) {
//                                    edt_district.setEnabled(true);
//                                } else {
//                                    edt_district.setEnabled(false);
//                                }
//
//                                edt_taluka.setVisibility(View.VISIBLE);
//
//                                if (takuka.equalsIgnoreCase("")) {
//                                    edt_taluka.setEnabled(true);
//                                } else {
//                                    edt_taluka.setEnabled(false);
//                                }
//
//
//                                if (localPincode.equalsIgnoreCase("")) {
//                                    edt_pincode.setEnabled(true);
//                                } else {
//                                    edt_pincode.setEnabled(false);
//                                }
//
//
//                                String permAddress = permHouseNo + permArea + "," + permPostOffice + "," + permTaluka + "," + permDist + "," + permState + "," + permPincode;
//                                edt_address.setText(permAddress);
//                                String localAddress = localHouseNo + localArea + "," + localPostOffice + "," + localTaluka + "," + localDist + "," + localState + "," + localPincode;
//                                edt_local_address.setText(localAddress);
//                                edt_current_address.setText(localAddress);
//                                edt_pincode.setText(localPincode);
//
//                                if (jsonArray.getJSONObject(0).getInt("age") != 0) {
//                                    Calendar calendar = Calendar.getInstance();
//                                    calendar.add(Calendar.YEAR, -jsonArray.getJSONObject(0).getInt("age"));
//                                    edt_dob.setText(new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()));
//                                }
//                                String gender = jsonArray.getJSONObject(0).getString("gender");
//
//                                if (gender != null) {
//                                    if (gender.equalsIgnoreCase("Male")) {
//                                        rb_male.setChecked(true);
//                                        rg_gender.setEnabled(true);
//                                        //  rb_female.setChecked(true);
////                                    rb_mr.setChecked(true);
////                                    rb_mrs.setChecked(false);
////                                    rb_ms.setChecked(false);
//
//                                    } else {
//                                        // rb_male.setChecked(true);
//                                        rb_female.setChecked(true);
//                                        rg_gender.setEnabled(true);
//
////                                    rb_mrs.setChecked(true);
////                                    rb_mr.setChecked(false);
////                                    rb_ms.setChecked(false);
//                                    }
//                                }
//
//                                new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
                            }
//                            if (isDependent.equalsIgnoreCase("0")) {
//                                disableViewAfterFill();
//                            }
                        } else {
                            clearPatientDetails();
                            disableViewAfterFill();
                            Utilities.showAlertDialogRequired(context, "Alert", "बांधकाम कामगार मंडळाकडून लाभार्थ्याची अद्ययावत माहिती प्राप्त झालेली नाही. \nत्यामुळे सध्या या लाभार्थ्याची नोंदणी करता येणार नाही याची नोंद घ्यावी", false, "Okay", new DialogInterface.OnClickListener() {
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
                        Utilities.showAlertDialogRequired(context, "Alert", "बांधकाम कामगार मंडळाकडून लाभार्थ्याची अद्ययावत माहिती प्राप्त झालेली नाही. \nत्यामुळे सध्या या लाभार्थ्याची नोंदणी करता येणार नाही याची नोंद घ्यावी", false, "Okay", new DialogInterface.OnClickListener() {
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

//
//                    if (response.body().getStatus().equalsIgnoreCase("success")) {
//
//
//                            }
//
//
//                  showTalukaListDialog(dependentListModels);
//
//                   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", "onFailure: ", t);

                if (reTry != retryMaxCount) {
                    reTry = reTry + 1;
                    getWorkerInfo();

                } else {
                    reTry = 0;
                    Utilities.showAlertDialog(context, "Connection Time Out", "Unable to get data from MAHABOC after several retries", false);
                }


            }
        });
    }

    private void getWorkerInfoForFlag() {


        final ProgressDialog progressDialog = new ProgressDialog(D2DPatientRegistration_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.MahabocwAPICall().create(ApiInterface.class);
        apiInterface.getWorkerInfo("MH" + edt_workerregno.getText().toString()).enqueue(new retrofit2.Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.i("TAG", "onResponse: " + string);


                        JSONArray jsonArray = new JSONArray(string);

                        if (jsonArray.length() > 0) {


                        } else {
                            clearPatientDetails();
                            disableViewAfterFill();
                            Utilities.showAlertDialogRequired(context, "Alert", "लाभार्थी सध्या निष्क्रिय आहे किंवा उपलब्ध नाही. कृपया नंतर पुन्हा प्रयत्न करा.", false, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    relationId = "20";
                                    edt_relation.setText("Self");
                                    btn_register.setEnabled(true);
                                }
                            });
                        }
                    } else {
//                    enableViewAfterFill();
                        clearPatientDetails();
                        disableViewAfterFill();
                        relationId = "20";
                        edt_relation.setText("Self");
                        Utilities.showAlertDialogRequired(context, "Alert", "लाभार्थी सध्या निष्क्रिय आहे किंवा उपलब्ध नाही. कृपया नंतर पुन्हा प्रयत्न करा.", false, "Okay", new DialogInterface.OnClickListener() {
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

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", "onFailure: ", t);


            }
        });
    }


    private class GetWorkerInfoNew extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . .");
            pd.setCancelable(false);
            pd.show();
        }


        @Override
        protected String doInBackground(String... params) {
            String res = "";

            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("regno", params[0]));

            //  res = WebServiceCall.MahabocwAPICall(ApplicationConstants.GetWorkerInfo + params[0]);
            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryRegistrationDetailsWithMaritalStatus_GP, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryRegistrationDetails, ApplicationConstants.webservice_d2d, param);
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

                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("Success")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("output");

                        if (jsonArray.length() > 0) {
                            if (rg_dep_yes.isChecked()) {

                                /// shashank 20/01/26

                                clearDependentData();
                                disableIfFilled();
                                enableABHAFormAfterFill();
                                binding.btnClearABHAAddress.setEnabled(true);


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

                                String maritalStatusId = jsonArray.getJSONObject(0).getString("MaritalStatusID") == "null" ? "" : jsonArray.getJSONObject(0).getString("MaritalStatusID");
                                String maritalStatusName = jsonArray.getJSONObject(0).getString("MARITALSTATUS") == "null" ? "" : jsonArray.getJSONObject(0).getString("MARITALSTATUS");
                                String tallGID = jsonArray.getJSONObject(0).getString("TALLGDCODE") == "null" ? "" : jsonArray.getJSONObject(0).getString("TALLGDCODE");
                                String distId = jsonArray.getJSONObject(0).getString("DISTLGDCODE") == "null" ? "" : jsonArray.getJSONObject(0).getString("DISTLGDCODE");
                                String GPLGDCODE = jsonArray.getJSONObject(0).getString("GPLGDCODE") == "null" ? "" : jsonArray.getJSONObject(0).getString("GPLGDCODE");
                                String GPName = jsonArray.getJSONObject(0).getString("GPName") == "null" ? "" : jsonArray.getJSONObject(0).getString("GPName");
                                String IsUrban = jsonArray.getJSONObject(0).getString("IsUrban") == "null" ? "" : jsonArray.getJSONObject(0).getString("IsUrban");


                                String gender = jsonArray.getJSONObject(0).getString("gender");


                                if (GPLGDCODE != null) {

                                    gpCode = GPLGDCODE;
                                }

                                if (GPName != null) {
                                    binding.edtGp.setText(GPName);

                                }

                                if (IsUrban != null) {
                                    if (IsUrban.equals("0")) {
                                        binding.radioRural.setChecked(true);
                                        binding.edtGp.setVisibility(View.VISIBLE);
                                    } else {
                                        binding.radioUrban.setChecked(true);
                                        binding.edtGp.setVisibility(View.GONE);

                                        gpCode = "0";
                                    }
                                }

                                relationGenderId = gender;


                                talukaId = tallGID;
                                distLgdCode = distId;


                                if (takuka.equalsIgnoreCase("") || talukaId.equalsIgnoreCase("")) {
                                    edt_taluka.setEnabled(true);
//
                                    edt_taluka.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_downarrowbk), null);

                                } else {

                                    edt_taluka.setEnabled(false);
                                    edt_taluka.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                }


                                if (district.equalsIgnoreCase("")) {
                                    edt_district.setEnabled(true);

                                    edt_district.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_downarrowbk), null);

                                } else {
                                    edt_district.setEnabled(false);
                                    edt_district.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                }


                                labourage = age;

                                isManual = "2";

                                if (maritalStatusId != null) {
                                    txt_beneficiary_married_unmarried.setText(maritalStatusName);
                                    maritalStatus = maritalStatusId;

                                } else {
                                    txt_beneficiary_married_unmarried.setText("Married");
                                    maritalStatus = "1";
                                }

                                // new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());

//                            if (gender != null) {
//                                if (gender.equalsIgnoreCase("male")) {
//                                    rb_male.setChecked(true);
//                                    rb_female.setChecked(false);
//                                    rb_mr.setChecked(true);
//                                    rb_mrs.setChecked(false);
//                                    rb_ms.setChecked(false);
//
//                                } else {
//                                    rb_male.setChecked(false);
//                                    rb_female.setChecked(true);
//
//                                    rb_mrs.setChecked(true);
//                                    rb_mr.setChecked(false);
//                                    rb_ms.setChecked(false);
//
//                                }
//                            }


                                txt_beneficiary_name.setText(fName + " " + mName + " " + lName);
                                txt_beneficiary_name.setVisibility(View.VISIBLE);
                                txt_beneficiary_age.setText(age + "/" + gender);
                                txt_beneficiary_age.setVisibility(View.VISIBLE);

                                /// Visible to Gone
                                txt_beneficiary_gender_change.setVisibility(View.GONE);
                                binding.edtDependentList.setVisibility(View.VISIBLE);
                                txt_beneficiary_married_unmarried.setVisibility(View.VISIBLE);
                                txt_beneficiary_gender_change.setText(gender);

//
//                                benefBoardName = fName + " " + mName + " " + lName;
//                                edt_fname.setText(benefBoardName);


                                txt_beneficiary_gender.setText(gender);
                                benefBoardGender = gender;
                                // txt_beneficiary_gender.setVisibility(View.VISIBLE);


                                txt_beneficiary_Mname.setEnabled(true);
                                //  txt_beneficiary_Lname.setVisibility(View.GONE);
                                if (Type.equalsIgnoreCase("0") || Type.equalsIgnoreCase("7") || Type.equalsIgnoreCase("6")) {
                                    txt_beneficiary_Lname.setText(lName);
                                    txt_beneficiary_Lname.setEnabled(false);

                                    if (gender.equalsIgnoreCase("Female")) {
                                        txt_beneficiary_Mname.setText("");
                                    } else {
                                        txt_beneficiary_Mname.setText(fName);
                                    }

                                    edt_fname.setText(txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim());

                                }

                                edt_relation.setVisibility(View.VISIBLE);

//                                if (gender.equalsIgnoreCase("Female")) {
//                                    txt_beneficiary_Mname.setText("");
//                                } else {
//                                    txt_beneficiary_Mname.setText(fName);
//                                }

//                                txt_beneficiary_Mname.setEnabled(true);
//                                //  txt_beneficiary_Lname.setVisibility(View.GONE);
//                                txt_beneficiary_Lname.setText(lName);
//                                txt_beneficiary_Lname.setEnabled(false);


                                /// need to check
                                benefBoardName = txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim();
                                edt_fname.setText(benefBoardName);


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


                                mobile = "7057394055";
//                                mobile = "8668258532";
//                                mobile = "9764568835";
//                                mobile = "9657365115";
//                                mobile = "9665253245";
//                                mobile = "9673974373";

//                                mobile = executivemobileNumber;
//

                                edt_moblieno.setText(mobile);

                                edt_moblieno.setEnabled(false);

                                edt_pincode.setText(localPincode);
                                edt_landMark.setText(permArea);
                                edt_postOffice.setText(postoffice);
                                edt_renewalDate.setText(renewalDate);


                                if (Utilities.isNetworkAvailable(context)) {
                                    new CheckRegidExist().execute(edt_workerregno.getText().toString().trim());
                                } else {
                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                }


                                /// dependent list
                                txt_beneficiary_Fname.setEnabled(false);
                                txt_beneficiary_Mname.setEnabled(false);
                                txt_beneficiary_Lname.setEnabled(false);


                                binding.edtRationCard.setVisibility(View.VISIBLE);


                                btn_VerifyOtpContact.setVisibility(View.VISIBLE);
                                btn_VerifyOtp.setVisibility(View.VISIBLE);


                                if (localPincode.equalsIgnoreCase("") || localPincode.equalsIgnoreCase("0")) {
                                    edt_pincode.setEnabled(true);
                                } else {
                                    edt_pincode.setEnabled(false);
                                }


                                if (Type.equalsIgnoreCase("5")) {
                                    getDependentRescreeningData();
                                }


                                /// for worker status flag
                                if (isBoardDataCompalsory.equalsIgnoreCase("1")) {
                                    getWorkerInfoForFlag();
                                }


                            } else {
                                disableIfFilled();
                                enableABHAFormAfterFill();
                                binding.btnClearABHAAddress.setEnabled(true);


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


                                String tallGID = jsonArray.getJSONObject(0).getString("TALLGDCODE") == "null" ? "" : jsonArray.getJSONObject(0).getString("TALLGDCODE");
                                String distId = jsonArray.getJSONObject(0).getString("DISTLGDCODE") == "null" ? "" : jsonArray.getJSONObject(0).getString("DISTLGDCODE");

                                String GPLGDCODE = jsonArray.getJSONObject(0).getString("GPLGDCODE") == "null" ? "" : jsonArray.getJSONObject(0).getString("GPLGDCODE");
                                String GPName = jsonArray.getJSONObject(0).getString("DISTLGDCODE") == "null" ? "" : jsonArray.getJSONObject(0).getString("GPName");
                                String IsUrban = jsonArray.getJSONObject(0).getString("IsUrban") == "null" ? "" : jsonArray.getJSONObject(0).getString("IsUrban");

                                identityId = "1";


                                talukaId = tallGID;
                                distLgdCode = distId;

                                maritalStatus = "1";

                                if (GPLGDCODE != null) {

                                    gpCode = GPLGDCODE;
                                }

                                if (GPName != null) {
                                    binding.edtGp.setText(GPName);
                                }

                                if (IsUrban != null) {
                                    if (IsUrban.equals("0")) {
                                        binding.radioRural.setChecked(true);
                                        binding.edtGp.setVisibility(View.VISIBLE);

                                    } else {
                                        binding.radioUrban.setChecked(true);
                                        binding.edtGp.setVisibility(View.GONE);
                                        binding.edtGp.setText("");
                                        gpCode = "0";

                                    }
                                }


                                if (takuka.equalsIgnoreCase("") || talukaId.equalsIgnoreCase("")) {
                                    edt_taluka.setEnabled(true);
//                                    edt_taluka.setClickable(false);
//                                    edt_taluka.setLongClickable(false);
                                    edt_taluka.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_downarrowbk), null);

                                } else {
                                    edt_taluka.setEnabled(false);
                                    edt_taluka.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                                }


                                benefBoardName = fName + " " + mName + " " + lName;
                                edt_fname.setText(benefBoardName);


                                /// new change 28/01/26
                                if (Type.equalsIgnoreCase("0")) {

                                    edt_fname.setText(fName + " " + mName + " " + lName);

                                }


                                originalAadhaar = aadhar;

                                edt_aadhaarno.setText(maskAadhaar(originalAadhaar));


                                mobile = "7057394055";
//                                mobile = "8668258532";
//                                mobile = "9764568835";
//                                mobile = "9657365115";
//                                mobile = "9665253245";
//                                mobile = "9673974373";

//                                mobile = executivemobileNumber;

                                edt_moblieno.setText(mobile);
                                edt_moblieno.setEnabled(false);
//                                edt_aadhaarno.setText(aadhar);
                                edt_renewalDate.setVisibility(View.VISIBLE);
                                edt_age.setText(age);
                                edt_renewalDate.setText(renewalDate);
                                edt_taluka.setText(takuka);
                                edt_landMark.setText(permArea);
                                btn_VerifyOtpContact.setVisibility(View.VISIBLE);
//                                btn_VerifyOtpContact.setEnabled(false);
                                btn_VerifyOtp.setVisibility(View.VISIBLE);
                                //  rbll.setVisibility(View.VISIBLE);


                                /// Dependent change visible to gone
                                mainCbLL.setVisibility(View.GONE);
                                // btn_VerifyOtp.setVisibility(View.VISIBLE);


                                isManual = "2";

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


                                if (district.equalsIgnoreCase("")) {
                                    edt_district.setEnabled(true);

                                    edt_district.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_downarrowbk), null);

                                } else {
                                    edt_district.setEnabled(false);
                                    edt_district.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                                }


//                                if (district.equalsIgnoreCase("")) {
//                                    edt_district.setEnabled(true);
//                                } else {
//                                    edt_district.setEnabled(false);
//                                }

                                edt_taluka.setVisibility(View.VISIBLE);

//                                if (takuka.equalsIgnoreCase("")) {
//                                    edt_taluka.setEnabled(true);
//                                } else {
//                                    edt_taluka.setEnabled(false);
//                                }


//                                if (localPincode.equalsIgnoreCase("")) {
//                                    edt_pincode.setEnabled(true);
//                                } else {
//                                    edt_pincode.setEnabled(false);
//                                }


                                if (localPincode.equalsIgnoreCase("") || localPincode.equalsIgnoreCase("0")) {
                                    edt_pincode.setEnabled(true);
                                } else {
                                    edt_pincode.setEnabled(false);
                                }


                                binding.edtRationCard.setVisibility(View.GONE);
                                binding.edtRationCard.setText("NA");


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


                                edt_age.setText(age);
                                String gender = jsonArray.getJSONObject(0).getString("gender");
                                benefBoardGender = gender;


                                /// for worker status flag

                                if (isBoardDataCompalsory.equalsIgnoreCase("1")) {
                                    getWorkerInfoForFlag();
                                }


                                /// New change by shashank 06/03/26
                                relationGenderId = gender;
                                labourage = age;


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
                        }
                    } else {
//                        clearPatientDetails();
//                        disableViewAfterFill();

                        if (Utilities.isNetworkAvailable(context)) {

                            getWorkerInfo();


//                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());

                        }

//                        Utilities.showAlertDialogRequired(context, "Alert", "बांधकाम कामगार मंडळाकडून लाभार्थ्याची अद्ययावत माहिती प्राप्त झालेली नाही. \nत्यामुळे सध्या या लाभार्थ्याची नोंदणी करता येणार नाही याची नोंद घ्यावी", false, "Okay", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//
//
//                                relationId = "20";
//                                edt_relation.setText("Self");
//                                btn_register.setEnabled(true);
//                            }
//                        });
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


                    Utilities.showAlertDialogRequired(context, "Alert", "बांधकाम कामगार मंडळाकडून लाभार्थ्याची अद्ययावत माहिती प्राप्त झालेली नाही. \nत्यामुळे सध्या या लाभार्थ्याची नोंदणी करता येणार नाही याची नोंद घ्यावी", false, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

//                            new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());


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
//        edt_moblieno.setEnabled(false);
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


//        binding.edtRationCard.setEnabled(false);


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

    void disableViewAfterFillNew() {
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
        if (edt_aadhaarno.getText().toString().isEmpty()) {
            edt_aadhaarno.setEnabled(true);
        } else {
            edt_aadhaarno.setEnabled(false);
        }

        if (edt_taluka.getText().toString().isEmpty()) {
            edt_taluka.setEnabled(true);
        } else {
            edt_taluka.setEnabled(false);
        }

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

        /// changed to false
        edt_fname.setEnabled(false);
//        edt_fname.setLongClickable(true);
//        edt_moblieno.setEnabled(true);
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

    void enableViewAfterFillNew() {
        edt_fname.setEnabled(true);
        edt_fname.setLongClickable(true);
//        edt_moblieno.setEnabled(true);
        edt_moblieno.setLongClickable(true);
        edt_aadhaarno.setEnabled(true);
        edt_education.setEnabled(true);

        edt_aadhaarno.setLongClickable(true);
        edt_age.setEnabled(true);
        edt_age.setLongClickable(true);
        edt_address.setEnabled(true);
        edt_address.setLongClickable(true);
        edt_local_address.setEnabled(true);
        edt_current_address.setEnabled(true);
        edt_alternetMoblieno.setEnabled(true);
        edt_landMark.setEnabled(true);
        edt_local_address.setLongClickable(true);
        edt_pincode.setEnabled(true);
        edt_pincode.setLongClickable(true);
        edt_dob.setEnabled(true);
        edt_dob.setLongClickable(true);
        rb_male.setEnabled(true);
        rb_female.setEnabled(true);
        rb_mrs.setEnabled(true);
        rb_ms.setEnabled(true);
        rb_mr.setEnabled(true);
        edt_relation.setEnabled(true);
        edt_relation.setLongClickable(true);
        relationId = "20";
        edt_relation.setText("Self");


        txt_beneficiary_gender_change.setVisibility(View.GONE);

        txt_beneficiary_Lname.setEnabled(true);


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


        /// Dependent change Visible to gone
        mainCbLL.setVisibility(View.GONE);

        txt_beneficiary_Fname.setText("");
        txt_beneficiary_Fname.setEnabled(true);
        txt_beneficiary_Mname.setText("");
        txt_beneficiary_Mname.setEnabled(true);
        txt_beneficiary_Lname.setText("");

//        edt_renewal_date.setVisibility(View.VISIBLE);
//        edt_renewal_date.setEnabled(true);
//        edt_renewal_date.setLongClickable(true);


        edt_renewalDate.setVisibility(View.VISIBLE);
        edt_renewalDate.setEnabled(true);
        edt_renewalDate.setLongClickable(true);

//        edt_workerregno.setText("");
        edt_workerregno.setEnabled(true);
//                    edt_workerregno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        edt_relation.setText("");
        relationId = "0";
//        rg_dep_yes.setChecked(true);
        rb_female.setEnabled(true);
        rb_male.setEnabled(true);
//        edtIdentity.setVisibility(View.VISIBLE);
//        edtIdentity.setEnabled(true);
        tvRegdNo.setVisibility(View.GONE);
//                    llSelfDeclaration.setVisibility(View.VISIBLE);
//                    llHealthCard.setVisibility(View.GONE);
        tvHealthCard.setText("Indentity Card");
        edt_aadhaarno.setHint("Aadhaar Number");

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
//                        Utilities.showAlertDialog(context, "Alert", "No dependent List found still you can register this Labour as dependant.", false);
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

                            if (Integer.parseInt(count) >= 5) {
                                Utilities.showAlertDialogRequired(context, "Max Dependant Count Reached", "You can't add dependant more than 5", false, "Okay", new DialogInterface.OnClickListener() {
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

//                            Utilities.showAlertDialog(context, "Alert", "Beneficiary not registered", false);
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

                // Create the request body with MARITALSTATUSID and GENDER as parameters
                RequestBody formBody = new FormBody.Builder()
                        .add("MARITALSTATUSID", strings[0])  // Assuming MARITALSTATUSID is passed in strings[0]
                        .add("GENDER", strings[1])           // Assuming GENDER is passed in strings[1]
                        .build();

                Request request = new Request.Builder()
                        .url(ApplicationConstants.webservice_d2d + "GetRelation_with_Marital_Status")
                        .post(formBody)  // POST request with formBody
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
                    case "22":
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
                    case "21":
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

                if (relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
                        relationId.equalsIgnoreCase("21") || relationId.equalsIgnoreCase("22")) {


                    txt_beneficiary_Mname.setText("");


                }

                if (Type.equalsIgnoreCase("0")) {
                    new GetRejectionDetails().execute(edt_workerregno.getText().toString().trim(), "1", relationId, txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim(), genderId, "2");
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
                idName = documentList.get(position).getDocumentName();
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
                    edt_aadhaarno.setMaxCharacters(8);
                    edt_aadhaarno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

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


//                        if (rbWithoutAbha.isChecked()) {
//                            btn_register.setVisibility(View.VISIBLE);
//                            btn_register.setEnabled(true);
//                        }


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
//                                    edt_moblieno.setEnabled(false);
//                                    btn_VerifyOtpContact.setEnabled(false);
//                                    btn_VerifyOtpContact.setText("Verified");


                                    /// changed to false to true
                                    cbIsNumberBgs.setEnabled(true);


                                    isOtpVerify = true;

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
                        btn_register.setEnabled(true);

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
//                                    btn_VerifyOtp.setText("Verified");
//                                    edt_alternetMoblieno.setEnabled(false);
//                                    btn_VerifyOtp.setEnabled(false);
                                    edt_moblieno.setEnabled(false);
                                    btn_VerifyOtpContact.setEnabled(false);
                                    btn_VerifyOtpContact.setText("Verified");


                                    /// changed false to true
                                    cbIsNumberBgs.setEnabled(true);
                                    mainCbLL.setVisibility(View.VISIBLE);


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

//        edt_aadhaarno.setText(patientDetails.getUID().replace("-", ""));

        originalAadhaar = patientDetails.getUID().replace("-", "");

        edt_aadhaarno.setText(maskAadhaar(originalAadhaar));

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

//        latitude = 0.0;
//        longitude = 0.0;

        if (latitude == 0.00 || longitude == 0.00) {
            Utilities.showAlertDialog(context, "Alert", "location not fetch properly please check mobile gps and try again", false);
            return;
        }


        if (isDependent.equalsIgnoreCase("1")) {

            if (binding.edtDependentList.getText().toString().trim().equals("")) {
                Utilities.showAlertDialog(context, "Alert", "Please select dependent first", false);
                return;
            }
        }

        if (rbWithAbha.isChecked()) {


            if (isDependent.equalsIgnoreCase("0")) {


//                if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                    clearABHA();
////                        binding.btnClearABHAAddress.setEnabled(false);
//                    clearPatientDetails(1);
//                    clearPatientDetails(2);
//                    disableABHAFormAfterFill();
//
//                    ///shashank 20/01/26
//                    clearPatientDetails();
////                            disableAllViews();
//                    return;
//                }


                String abhaName = Utilities.normalize(binding.edtFname.getText().toString());
                String boardName = Utilities.normalize(benefBoardName);

                if (isDependent.equalsIgnoreCase("0")) {
                    if (!abhaName.equals(boardName)) {
                        Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                        clearABHA();
                        clearPatientDetails(1);
                        clearPatientDetails(2);
                        disableABHAFormAfterFill();

                        /// shashank 22/01/26
                        clearPatientDetails();

                        return;
                    }
                }


            } else if (isDependent.equalsIgnoreCase("1")) {

                String depName = txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim();

                String abhaName = binding.edtFname.getText().toString().trim();

                if (!abhaName.equalsIgnoreCase(depName)) {
                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "dependent  Name: " + depName + "\n" + "ABHA Name: " + abhaName + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                    clearABHA();
//                        binding.btnClearABHAAddress.setEnabled(false);
                    clearPatientDetails(1);
                    clearPatientDetails(2);
                    disableABHAFormAfterFill();

                    ///shashank 20/01/26
                    clearPatientDetails();
//                            disableAllViews();
                    return;
                }

            }
        }


        /// shashank 20/01/26
//        if (rbWithAbha.isChecked()) {
//            if (isDependent.equalsIgnoreCase("1")) {
//
//                benefBoardName = "shashank dattatray kadam";
//
//                String dependentName =
//                        txt_beneficiary_Mname.getText().toString().trim() + " " +
//                                txt_beneficiary_Lname.getText().toString().trim();
//
//                String[] befName = benefBoardName.trim().split("\\s+");
//
//                StringBuilder remainingName = new StringBuilder();
//                for (int i = 1; i < befName.length; i++) {
//                    remainingName.append(befName[i]);
//                    if (i < befName.length - 1) {
//                        remainingName.append(" ");
//                    }
//                }
//
//                String boardMiddleLastName = remainingName.toString();
//
//
//                if (!dependentName.equalsIgnoreCase(boardMiddleLastName)) {
//                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " +  .getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                    clearABHA();
////                binding.btnClearABHAAddress.setEnabled(false);
//                    clearPatientDetails(1);
//                    clearPatientDetails(2);
//                    disableABHAFormAfterFill();
////                            disableAllViews();
//                    return;
//                }
//
//
////
////                if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
////                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
////                    clearABHA();
//////                binding.btnClearABHAAddress.setEnabled(false);
////                    clearPatientDetails(1);
////                    clearPatientDetails(2);
////                    disableABHAFormAfterFill();
//////                            disableAllViews();
////                    return;
////                }
//            }
//        }


//        if (latLng != null) {
//            Latitude = String.valueOf(latLng.latitude);
//            Longitude = String.valueOf(latLng.longitude);
//        }

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


//        if (txt_beneficiary_Lname.getText().toString().trim().isEmpty()) {
//            Utilities.showToastMessage("Please enter last name", context, false);
//            return;
//        }


        if (Type.equalsIgnoreCase("0")) {
            if (txt_beneficiary_Lname.getText().toString().trim().isEmpty()) {
                Utilities.showToastMessage("Please enter last name", context, false);
                return;
            }

        }

        if (!Utilities.isMobileNo(edt_moblieno.getText().toString().trim())) {
            edt_moblieno.setError("Please enter valid mobile");
            return;
        }


//        if (!Utilities.isMobileNo(edt_moblieno.getText().toString().trim())) {
//            edt_moblieno.setError("Please enter valid mobile");
//            return;
//        }


//        if (edt_alternetMoblieno.getVisibility() == View.VISIBLE) {
//            if (!edt_alternetMoblieno.getText().toString().trim().isEmpty()) {
//                edt_alternetMoblieno.setError("Please enter alternate mobile number");
//                return;
//            }
//        }

        if (!edt_alternetMoblieno.getText().toString().isEmpty()) {
            if (!(edt_alternetMoblieno.getText().toString().length() == 10)) {
                Toast.makeText(context, "Please Enter Valid Alternate Mobile Number", Toast.LENGTH_SHORT).show();
                return;
            }


            if (!isOtpVerify) {
                Utilities.showToastMessage("Please Verify alternate number", context, false);
                return;
            }
        }


//        if (edt_address.getText().toString().trim().isEmpty()) {
//            Utilities.showToastMessage("Please enter permanent address", context, false);
//            return;
//        }
//
//        if (edt_local_address.getText().toString().trim().isEmpty()) {
//            Utilities.showToastMessage("Please enter Local Address", context, false);
//            return;
//        }
//
//        if (edt_address.getText().toString().length() <= 25){
//            Utilities.showToastMessage("Permanent address should be grater than equal to 25 character length ", context, false);
//            return;
//        }


        if (rbWithoutAbha.isChecked()) {
            if (isDependent.equalsIgnoreCase("1")) {
//                if (identityId.equalsIgnoreCase("0")) {
//                    edtIdentity.setError("Please Select Identity Card");
//                    return;
//                }
            }
        }

        if (rbWithoutAbha.isChecked()) {
            if (isDependent.equalsIgnoreCase("0") || isDependent.equalsIgnoreCase("1")) {


                if (idName.equalsIgnoreCase("Adhar Card")) {

                    if (edt_aadhaarno.getText().toString().trim().length() != 12) {
                        edt_aadhaarno.setError("Please enter valid Aadhaar Card No.");
                        return;
                    }

                } else if (idName.equalsIgnoreCase("Pan Card")) {

                    if (edt_aadhaarno.getText().toString().trim().length() != 10) {
                        edt_aadhaarno.setError("Please enter valid Pan Card No.");
                        return;
                    }
                } else if (idName.equalsIgnoreCase("Driving Licence")) {
                    if (edt_aadhaarno.getText().toString().trim().length() != 16) {
                        edt_aadhaarno.setError("Please enter valid Driving Licence No.");
                        return;
                    }


                } else if (idName.equalsIgnoreCase("Passport")) {
                    if (edt_aadhaarno.getText().toString().trim().length() != 8) {
                        edt_aadhaarno.setError("Please enter valid Passport No.");
                        return;
                    }


                } else {
                    edt_aadhaarno.setError(null);
                }


//                if (edt_aadhaarno.getText().toString().trim().length() != 12) {
//                    edt_aadhaarno.setError("Please enter valid Aadhar Card No.");
//                    return;
//                } else {
//                    edt_aadhaarno.setError(null);
//                }
            }
        }


        Log.d(TAG, "submit aadhar :" + originalAadhaar);

        if (!Utilities.isaadharNumberValidate(originalAadhaar)) {
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

//        if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
//            Utilities.showAlertDialog(context, "Alert", "KBOCWWB Labour age should not be less than 18 years or more than 60 years", false);
//            return;
//        }


//        if (isDependent.equalsIgnoreCase("0")) {
//            if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
//                Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 60 years", false);
//                return;
//            }
//        } else {
//            if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")
//                    || relationId.equalsIgnoreCase("5") || relationId.equalsIgnoreCase("6")) {
//                if (Integer.parseInt(edt_age.getText().toString().trim()) < 10 || Integer.parseInt(edt_age.getText().toString().trim()) > 58) {
//                    Utilities.showAlertDialog(context, "Alert", "Dependent age should not be less than 10 years or more than 58 years", false);
//                    return;
//                }
//            } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
//                    || relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
//                    relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
//                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 75) {
//                    Utilities.showAlertDialog(context, "Alert", "Dependent age should not be less than 18 years or more than 60 years", false);
//                    return;
//                }
//            } else {
//                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 75) {
//                    Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 75 years", false);
//                    return;
//                }
//            }
//        }


        if (isDependent.equalsIgnoreCase("0")) {


            if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
                Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 60 years", false);
                return;
            }
        } else {
            if (relationId.equalsIgnoreCase("7") || relationId.equalsIgnoreCase("8")
                    || relationId.equalsIgnoreCase("5") || relationId.equalsIgnoreCase("6")) {
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 10 || Integer.parseInt(edt_age.getText().toString().trim()) > 18) {
                    Utilities.showAlertDialog(context, "Alert", "Dependent age should not be less than 10 years or more than 18 years", false);
                    return;
                }
            } else if (relationId.equalsIgnoreCase("17") || relationId.equalsIgnoreCase("18")
                    || relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
                    relationId.equalsIgnoreCase("9") || relationId.equalsIgnoreCase("10")) {
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 99) {
                    Utilities.showAlertDialog(context, "Alert", "Dependent age should not be less than 18 years or more than 99 years", false);
                    return;
                }
            } else {
                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 99) {
                    Utilities.showAlertDialog(context, "Alert", "Labour age should not be less than 18 years or more than 99 years", false);
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
            genderIdNew = "Male";
        } else if (rb_female.isChecked()) {
            genderId = "F";
            genderIdNew = "Female";

        } else {
            Utilities.showToastMessage("Please select gender", context, false);
            return;
        }


        if (edt_local_address.getVisibility() == View.VISIBLE) {
            if (edt_local_address.getText().toString().trim().isEmpty()) {
                Utilities.showToastMessage("Please enter local address", context, false);
                return;
            }

            if (edt_local_address.getText().toString().length() <= 10) {
                Utilities.showToastMessage("Local address should be grater than equal to 10 character length ", context, false);
                return;
            }


        }

        if (edt_current_address.getVisibility() == View.VISIBLE) {
            if (edt_current_address.getText().toString().trim().isEmpty()) {
                edt_current_address.setError("Please enter Current Address");
                return;
            }

            if (edt_current_address.getText().toString().length() <= 10) {
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
        if (isManual.equalsIgnoreCase("1")) {
            if (edt_renewalDate.getVisibility() == View.VISIBLE) {
                if (edt_renewalDate.getText().toString().trim().isEmpty()) {
                    edt_renewalDate.setError("Please enter renewal date");
                    return;
                }
            }
        }

        if (edt_taluka.getVisibility() == View.VISIBLE) {
            if (edt_taluka.getText().toString().trim().isEmpty()) {
                edt_taluka.setError("Please enter taluka");
                return;
            }
        }

        if (edt_district.getVisibility() == View.VISIBLE) {
            if (edt_district.getText().toString().trim().isEmpty()) {
                edt_district.setError("Please enter district");
                return;
            }
        }


//        if (!Utilities.isValidPincode(edt_pincode.getText().toString().trim())) {
//            edt_pincode.setError("Please enter valid pincode");
//            return;
//        }


        String rationCardNo =
                binding.edtRationCard.getText().toString().trim();

        if (rationCardNo.isEmpty()) {

            binding.edtRationCard.setError(
                    "Please enter ration card number"
            );

            return;
        }


        if (isDependent.equalsIgnoreCase("1")) {


            if (rationCardNo.length() < 3 ||
                    rationCardNo.length() > 15) {

                binding.edtRationCard.setError(
                        "Ration card number must be between 3 to 15 digits"
                );

                return;
            }


//            if (rationCardNo.length() < 3 || rationCardNo.length() > 12) {
//
//                binding.edtRationCard.setError(
//                        "Ration card number must be between 3 to 15 characters"
//                );
//                return;
//            }

            if (rationCardNo.matches("(\\d)\\1+")) {

                binding.edtRationCard.setError(
                        "Invalid ration card number"
                );
                return;
            }

        }


//        if (binding.edtRationCard.getText().toString().equals("")) {
//            binding.edtRationCard.setError("Please enter ration card");
//            return;
//        }

        if (binding.radioRural.isChecked()) {
            if (binding.edtGp.getText().toString().equals("")) {
                binding.edtGp.setError("Please select gp");
                return;
            }
        }

        if (edt_district.getVisibility() == View.VISIBLE) {
            if (edt_district.getText().toString().trim().isEmpty()) {
                edt_district.setError("Please enter district");
                return;
            }
        }


        if (isCellularPhone.equals("1")) {

            if (!isConsentAvailable) {
                if (consentPath.equals("")) {
                    Utilities.showToastMessage("Please click consent photo", context, false);
                    return;
                }
            }
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


        if (isDependent.equals("1") || isDependent.equals("0")) {
//            verifyBeneficiaryDetails("MH" + edt_workerregno.getText().toString().trim(),
//                    originalAadhaar,
//                    edt_fname.getText().toString().trim(),
//                    relationId,
//                    edt_pincode.getText().toString().trim(),
//                    edt_dob.getText().toString().trim(),
//                    binding.edtRationCard.getText().toString().trim()
//
//            );


            if (isCellularPhone.equals("0")) {
                getConsent("MH" + edt_workerregno.getText().toString().trim(),
                        originalAadhaar,
                        edt_fname.getText().toString().trim(),
                        relationId,
                        edt_pincode.getText().toString().trim(),
                        edt_dob.getText().toString().trim(),
                        binding.edtRationCard.getText().toString().trim()

                );
                return;

            } else if (isCellularPhone.equals("1")) {

                verifyBeneficiaryDetails("MH" + edt_workerregno.getText().toString().trim(),
                        originalAadhaar,
                        edt_fname.getText().toString().trim(),
                        relationId,
                        edt_pincode.getText().toString().trim(),
                        edt_dob.getText().toString().trim(),
                        binding.edtRationCard.getText().toString().trim()

                );

                return;

            }


        }


        if (Utilities.isNetworkAvailable(context)) {
            regdID = edt_workerregno.getText().toString().trim() + "" + count;


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert!");
            builder.setMessage("Please confirm the beneficiary's details before submitting");
            builder.setCancelable(false);
            builder.setPositiveButton("Proceed", (dialog, which) -> {

                if (isfaceDetection.equals("0")) {
                    if (Utilities.isNetworkAvailable(context))
                        new UploadPatientDetails().execute(
                                siteId,
                                campId,
                                regdID,
                                title,
                                txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim(),
                                edt_moblieno.getText().toString().trim(),
                                originalAadhaar,
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
                                "1",
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
                                rb_self.isChecked() ? "1" : rb_spouse.isChecked() ? "2" : "3",
                                isManual,
                                versionNumber,
                                isRecollectionFlag,
                                rejRegdID,
                                registerdCampId,
                                maritalStatus,
                                isfaceDetection,
                                talukaId,
                                distLgdCode,
                                IsRegdByCall,
                                binding.edtABHANumber1.getText().toString().trim(),
                                binding.edtABHAAddress1.getText().toString().trim().isEmpty() ? "" : binding.edtABHAAddress1.getText().toString().replace(" ", "") + BuildConfig.CMID,
                                isWhatsAppEnabled,
                                rg_dep_no.isChecked() ? genderIdNew : relationGenderId,
                                labourage,
                                isfaceDetection,
                                dependentBocId,
                                String.valueOf(latitude),
                                String.valueOf(longitude),
                                gpCode,
                                binding.edtRationCard.getText().toString().trim(),
                                isCellularPhone,
                                consentPath
                        );
                    else
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                } else if (isfaceDetection.equals("1")) {

                    if (Utilities.isNetworkAvailable(context))
                        new UploadPatientDetailsNew().execute(
                                siteId,
                                campId,
                                regdID,
                                title,
                                txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim(),
                                edt_moblieno.getText().toString().trim(),
                                originalAadhaar,
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
                                "1",
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
                                rb_self.isChecked() ? "1" : rb_spouse.isChecked() ? "2" : "3",
                                isManual,
                                versionNumber,
                                isRecollectionFlag,
                                rejRegdID,
                                registerdCampId,
                                maritalStatus,
                                isfaceDetection,
                                talukaId,
                                distLgdCode,
                                IsRegdByCall,
                                binding.edtABHANumber1.getText().toString().trim(),
                                binding.edtABHAAddress1.getText().toString().trim().isEmpty() ? "" : binding.edtABHAAddress1.getText().toString().replace(" ", "") + BuildConfig.CMID,
                                isWhatsAppEnabled,
                                rg_dep_no.isChecked() ? genderIdNew : relationGenderId,
                                labourage,
                                isfaceDetection,
                                dependentBocId,
                                String.valueOf(latitude),
                                String.valueOf(longitude),
                                gpCode,
                                binding.edtRationCard.getText().toString().trim(),
                                isCellularPhone,
                                consentPath


//                            rg_dep_no.isChecked() ? genderId : relationGenderId ,


                        );
                    else
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);


                }

            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();

        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

    }

    //    private class UploadPatientDetails extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please Wait . . .");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            Log.d("Reg Params", Arrays.toString(params));
//            try {
//                MultipartUtility multipart;
////                if(session.isHllUser()) {
//                multipart = new MultipartUtility(ApplicationConstants.DtoDBeneficiaryRe_RegistrationV8, "UTF-8");
////                }else{ 184346468686
////                    multipart = new MultipartUtility(ApplicationConstants.BeneficiaryRe_RegistrationOTP, "UTF-8");
////
////                }
//                multipart.addFormField("SiteId", params[0]);
//                multipart.addFormField("CampId", params[1]);
//                multipart.addFormField("RegdNo", params[2]);
//                multipart.addFormField("Title", params[3]);
//                multipart.addFormField("EnglishName", params[4]);
//                multipart.addFormField("MobileNo", params[5]);
//                multipart.addFormField("UID", params[6]);
//                multipart.addFormField("DOB", params[7]);
//                multipart.addFormField("Age", params[8]);
//                multipart.addFormField("Gender", params[9]);
//                multipart.addFormField("PermanentAddress", params[10]);
//                multipart.addFormField("LocalAddress", params[11]);
//                multipart.addFormField("PinCode", params[12]);
//                multipart.addFormField("CreatedBy", params[13]);
//                multipart.addFormField("IsHCRenewal", params[14]);
//                multipart.addFormField("RenewalDate", params[15]);
//                if (!params[16].equals(""))
//                    multipart.addFilePart("file1", new File(params[16]));
//                if (!params[17].equals(""))
//                    multipart.addFilePart("file2", new File(params[17]));
//                if (!params[18].equals(""))
//                    multipart.addFilePart("file3", new File(params[18]));
//                if (!params[19].equals(""))
//                    multipart.addFilePart("file4", new File(params[19]));
//                multipart.addFormField("IsDependent", params[20]);
//                multipart.addFormField("Education", params[21]);
//                multipart.addFormField("ReleationID", params[22]);
//                multipart.addFormField("DependREGID", params[23]);
//                multipart.addFormField("IndentityId", params[24]);
//                multipart.addFormField("CW_WorkerName", params[25]);
//                multipart.addFormField("next_renewal_date", params[26]);
//                multipart.addFormField("residential_address_postOffice", params[27]);
//                multipart.addFormField("residential_address_taluka", params[28]);
//                multipart.addFormField("residential_address_district", params[29]);
//                multipart.addFormField("CurrentAddress", params[30]);
//                multipart.addFormField("LandMark", params[31]);
//                multipart.addFormField("AlternateMobNo", params[32]);
//                multipart.addFormField("IsMobNoVerified", params[33]);
//                multipart.addFormField("IsSelfMobNo", params[34]);
//                multipart.addFormField("MobNoOf", params[35]);
//                multipart.addFormField("OptionMode", params[36]);
//                multipart.addFormField("VersionNo", params[37]);
////                multipart.addFormField("Lat", params[38]);
////                multipart.addFormField("Long", params[39]);
//                List<String> response = multipart.finish();
//                for (String line : response) {
//                    res = res + line;
//                }
//                return res;
//            } catch (IOException ex) {
//                return ex.toString();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//
//            Log.d("Reg Res ", result);
//            try {
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                    JSONObject obj1 = new JSONObject(result);
//                    String status = obj1.getString("status");
//                    String message = obj1.getString("message");
//
//                    edt_workerregno.setEnabled(true);
//
//                    if (status.equalsIgnoreCase("Success")) {
//                        registeredPatientregdid = obj1.getString("Regdid");
//
//                        //      if (isRTPCR.equals("0")) {
//                        openFingerPrintActivity();

    /// /                        } else {
    /// /                            String billString = CreateJsonString();
    /// /                            new UploadPatientDetailsToLab().execute(billString);
    /// /                        }
//                    } else {
//                        if (message.toLowerCase().contains("regd")) {
//                            Utilities.showAlertDialog(context, "Fail", "Labour No Already Exists", false);
//                        } else {
//                            Utilities.showAlertDialog(context, "Fail", message, false);
//                        }
//                    }
//                } else
//                    Utilities.showAlertDialog(context,
//                            "Please try again", "Server not responding.", false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


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

                multipart = new MultipartUtility(ApplicationConstants.DtoDBeneficiaryRe_RegistrationV20, "UTF-8");

//                }else{ 184346468686
//                    multipart = new MultipartUtility(ApplicationConstants.BeneficiaryRe_RegistrationOTP, "UTF-8");
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
                multipart.addFormField("OptionMode", params[36]);
                multipart.addFormField("VersionNo", params[37]);
                multipart.addFormField("Isrecollection", params[38]);
                multipart.addFormField("Rej_Regdid", params[39]);
                multipart.addFormField("Rej_CampID", params[40]);
                multipart.addFormField("MaritalStatusID", params[41]);
                multipart.addFormField("IsFaceDetectionEnabled", params[42]);
                multipart.addFormField("TALLGDCODE", params[43]);
                multipart.addFormField("DISTLGDCODE", params[44]);
                multipart.addFormField("IsRegdByCall", params[45]);
                multipart.addFormField("ABHANumber", params[46]);
                multipart.addFormField("ABHAAddress", params[47]);
                multipart.addFormField("IsWhatsAppNo", params[48]);
                multipart.addFormField("WorkerGenderByPhlebo", params[49]);
                multipart.addFormField("WorkerAgeByPhlebo", params[50]);
                multipart.addFormField("IsFaceMatchFlag", params[51]);
                multipart.addFormField("Bocw_idDepend", params[52]);
                multipart.addFormField("Latitude", params[53]);
                multipart.addFormField("Longitude", params[54]);
                multipart.addFormField("GPLGDCODE", params[55]);
                multipart.addFormField("RationCardNo", params[56]);
                multipart.addFormField("IsCellularPhone", params[57]);
                if (!params[58].equals(""))
                    multipart.addFilePart("ConsentFormPath", new File(params[58]));

                Log.i(TAG, "doInBackground: " + multipart.getFormFields());


//                multipart.addFormField("Lat", params[38]);
//                multipart.addFormField("Long", params[39]);
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
                        edt_workerregno.setEnabled(true);


                        //      if (isRTPCR.equals("0")) {
                        openFingerPrintActivity();

//                        } else {
//                            String billString = CreateJsonString();
//                            new UploadPatientDetailsToLab().execute(billString);
//                        }

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


    private class UploadPatientDetailsNew extends AsyncTask<String, Integer, String> {

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

                multipart = new MultipartUtility(ApplicationConstants.DtoDBeneficiaryRe_RegistrationV21, "UTF-8");

//                }else{ 184346468686
//                    multipart = new MultipartUtility(ApplicationConstants.BeneficiaryRe_RegistrationOTP, "UTF-8");
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
                multipart.addFormField("OptionMode", params[36]);
                multipart.addFormField("VersionNo", params[37]);
                multipart.addFormField("Isrecollection", params[38]);
                multipart.addFormField("Rej_Regdid", params[39]);
                multipart.addFormField("Rej_CampID", params[40]);
                multipart.addFormField("MaritalStatusID", params[41]);
                multipart.addFormField("IsFaceDetectionEnabled", params[42]);
                multipart.addFormField("TALLGDCODE", params[43]);
                multipart.addFormField("DISTLGDCODE", params[44]);
                multipart.addFormField("IsRegdByCall", params[45]);
                multipart.addFormField("ABHANumber", params[46]);
                multipart.addFormField("ABHAAddress", params[47]);
                multipart.addFormField("IsWhatsAppNo", params[48]);
                multipart.addFormField("WorkerGenderByPhlebo", params[49]);
                multipart.addFormField("WorkerAgeByPhlebo", params[50]);
                multipart.addFormField("IsFaceMatchFlag", params[51]);
                multipart.addFormField("Bocw_idDepend", params[52]);
                multipart.addFormField("Latitude", params[53]);
                multipart.addFormField("Longitude", params[54]);
                multipart.addFormField("GPLGDCODE", params[55]);
                multipart.addFormField("RationCardNo", params[56]);
                multipart.addFormField("IsCellularPhone", params[57]);
                if (!params[58].equals(""))
                    multipart.addFilePart("ConsentFormPath", new File(params[58]));

                Log.i(TAG, "doInBackground: " + multipart.getFormFields());


//                multipart.addFormField("Lat", params[38]);
//                multipart.addFormField("Long", params[39]);
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
//{"status":"fail","message":"Unable to save health card details"}
            //[309793, 246239, 324630051709, , Prashant Bhatu Patil, 9730484894, 895122109420, 1987/06/26, 37, M, Sr.No-131-132,Sneha Paradise,Flat No-E-205, , Near Police Station, Warje Malwadi, Pune City, Pune City, Pune, Maharashtra,411058, PUNE MAHARASHTRA, 411058, 4845, 0, , /storage/emulated/0/Android/data/com.myhindlab.abkat.beta/cache1745854555270_PR.jpg, /storage/emulated/0/Android/data/com.myhindlab.abkat.beta/cache62174_HC.png, , , 0, , 20, 0, 0, , 2024-09-20, Sindi S.O, PUNE, PUNE, PUNE MAHARASHTRA, PUNE, , 0, 1, 1, 2, 9.07, 0, 0, 0, 1, 0, 0, 0, 0, 91-1072-7143-8254, prashantrerwrw@sbx]
            Log.d("Reg Res ", result);
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");


                    if (status.equalsIgnoreCase("Success")) {
                        registeredPatientregdid = obj1.getString("Regdid");
                        edt_workerregno.setEnabled(true);


                        //      if (isRTPCR.equals("0")) {
                        openFingerPrintActivity();
//                        } else {
//                            String billString = CreateJsonString();
//                            new UploadPatientDetailsToLab().execute(billString);
//                        }
                    } else {
//                        if (message.toLowerCase().contains("regd")) {
//                            Utilities.showAlertDialog(context, "Fail", "Labour No Already Exists", false);
//                        } else {
//                            Utilities.showAlertDialog(context, "Fail", message, false);
//                        }
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
            msg = "Beneficiary details have been saved successfully.\n Please proceed with the registration by completing the remaining steps.";
        else
            msg = "Beneficiary details have been saved successfully. \nPlease proceed with the registration by completing the remaining steps.";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle("Success");
        builder.setIcon(R.drawable.icon_success);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

//                startActivity(new Intent(context, PatientFingerAndSignature_Activity.class)
//                        .putExtra("campId", campId)
//                        .putExtra("siteId", siteId)
//                        .putExtra("registrationNo", regdID));


                if (Type.equalsIgnoreCase("5")) {
                    Type = "0";
                }

                startActivity(new Intent(context, PatientFingerAndSignature_Activity.class)
                        .putExtra("campId", campId)
                        .putExtra("siteId", siteId)
                        .putExtra("registrationNo", regdID)
                        .putExtra("beneficiaryNo", beneficiaryNo)
                        .putExtra("relation", relation)
                        .putExtra("regId", rejRegdID)
                        .putExtra("rejCampId", registerdCampId)
                        .putExtra("beneficiarName", beneficaryName)
                        .putExtra("Type", Type)
                        .putExtra("dependentBocId", dependentBocId)
                        .putExtra("rationCardNumber", binding.edtRationCard.getText().toString().trim())
                        .putExtra("regIdAfterReg", registeredPatientregdid)


                );


                clearPatientDetails();
                edt_workerregno.setText("");

                finish();


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


    ActivityResultLauncher<Intent> cameraLauncherForPatientPhoto =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");
                            Log.d(TAG, ":fileURI " + fileURI);
                            Log.d(TAG, ":fName " + fName);
                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_PR";
                                String compressedFilePath = Utilities.compressImageNew(context, fileURI, filename);
                                saveFileAI(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


    ActivityResultLauncher<Intent> cameraLauncherForPatientIDCard =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");
                            Log.d(TAG, ":fileURI " + fileURI);
                            Log.d(TAG, ":fName " + fName);
                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_HC";
                                String compressedFilePath = Utilities.compressImageNew(context, fileURI, filename);
                                saveFileAIIDCard(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


    ActivityResultLauncher<Intent> cameraLauncherForConsentForm =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");
                            Log.d(TAG, ":fileURI " + fileURI);
                            Log.d(TAG, ":fName " + fName);
                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_CF";
                                String compressedFilePath = Utilities.compressImageNew(context, fileURI, filename);
                                saveFileAIConsent(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


    ActivityResultLauncher<Intent> cameraLauncherForRationCard =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");
                            Log.d(TAG, ":fileURI " + fileURI);
                            Log.d(TAG, ":fName " + fName);
                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_HC";
                                String compressedFilePath = Utilities.compressImageNew(context, fileURI, filename);
                                saveFileRationCard(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    // Use the cropped image URI.
                    String path = result.getUriFilePath(context, true);
                    savefile(result.getUriContent());

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
            if (requestCode == PATIENT_CAMERA_REQUEST) {
//                CropImage.activity(patientURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistrationActivity_Offline.this);
                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;
//                String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
//                destinationFilename = patientPicsFolder + filename;
//                cropImageOptions.customOutputUri = Uri.parse(destinationFilename);
                CropImageContractOptions options = new CropImageContractOptions(patientURI, cropImageOptions);
                cropImageLauncher.launch(options);
            }
            if (requestCode == HEALTHCARD_CAMERA_REQUEST) {
//                CropImage.activity(healthCardURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistrationActivity_Offline.this);
                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                CropImageContractOptions options = new CropImageContractOptions(healthCardURI, cropImageOptions);
                cropImageLauncher.launch(options);
            }
            if (requestCode == RENEWAL_CAMERA_REQUEST) {
//                CropImage.activity(renewalURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistrationActivity_Offline.this);
                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                CropImageContractOptions options = new CropImageContractOptions(renewalURI, cropImageOptions);
                cropImageLauncher.launch(options);
            }
            if (requestCode == HIV_CONCERN_CAMERA_REQUEST) {
//                CropImage.activity(hivConcernURI).setGuidelines(CropImageView.Guidelines.ON).start(D2DPatientRegistrationActivity_Offline.this);
                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                CropImageContractOptions options = new CropImageContractOptions(hivConcernURI, cropImageOptions);
                cropImageLauncher.launch(options);
            }

            if (requestCode == 10001) {
                IsAdharDataVerify = "0";
                String requiredValue = data.getStringExtra("key");
                Document doc = convertStringToDocument(requiredValue);

                edt_workerregno.setText(requiredValue);

                if (doc != null) {
                    IsAdharDataVerify = "1";
                    parseXmlDocument(doc);
                } else {
                    IsAdharDataVerify = "0";
//                    Utilities.showMessageString("Please try again", context);
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


    public class GetFlexiCampType extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.CampTypeFlexi, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    NewCampTypeModel newCampTypeModel = new Gson().fromJson(result, NewCampTypeModel.class);
                    type = newCampTypeModel.getStatus();
                    message = newCampTypeModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<NewCampTypeModel.Output> camptypelist = newCampTypeModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showCampTypeFlexiDialog(camptypelist);
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

        private void showCampTypeFlexiDialog(final List<NewCampTypeModel.Output> camptypelist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Camp Type");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < camptypelist.size(); i++) {
                arrayAdapter.add(String.valueOf(camptypelist.get(i).getCampTypeDescription()));
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

                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }


    public class GetOldNewMethod extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.CheckRegistrationMethod, ApplicationConstants.webservice_d2d, param);
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
                    List<HomeLabHublabOnLandingLabModel.Output> campList = new ArrayList<>();

                    HomeLabHublabOnLandingLabModel homeLabHublabOnLandingLabModel = new Gson().fromJson(result, HomeLabHublabOnLandingLabModel.class);
                    type = homeLabHublabOnLandingLabModel.getStatus();
                    message = homeLabHublabOnLandingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (homeLabHublabOnLandingLabModel.getOutput().size() > 0) {
                            HomeLabHublabOnLandingLabModel.Output output = homeLabHublabOnLandingLabModel.getOutput().get(0);
//
//                            edt_lab_1.setText(output.getHomeLab());
//                            edt_lab_2.setText(output.getHubLab());
//                            selectedHomeLabID = String.valueOf(output.getHomeLabcode());
//                            selectedHubLabID = String.valueOf(output.getHubLabcode());
//                            edt_lab_1.setEnabled(false);
//                            edt_lab_2.setEnabled(false);
                            isOld = output.getIsFlag();
                            isManual = output.getOptionFlag();


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Camp I'd not available", false);
            }
        }
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
            param.add(new ParamsPojo("BOCWRegNO", params[5]));
            param.add(new ParamsPojo("BeneficiaryName", params[6]));
            param.add(new ParamsPojo("ReleationID", params[7]));
            param.add(new ParamsPojo("SubOrgID", organizationId));

//            res = WebServiceCall.APICall(ApplicationConstants.GetOTPforRegistration_Org, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.SendRegistrationOTPWithDPDPConsent, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            Log.i(TAG, "onPostExecute: " + result);
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
//
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




    public class GetOtpForAbhaConsent extends AsyncTask<String, Void, String> {
        //ProgressDialog pd;

        int type;

        public GetOtpForAbhaConsent(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Sending Consent link . . . ");
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
            param.add(new ParamsPojo("BOCWRegNO", params[5]));
            param.add(new ParamsPojo("BeneficiaryName", params[6]));
            param.add(new ParamsPojo("ReleationID", params[7]));
            param.add(new ParamsPojo("SubOrgID", organizationId));

//            res = WebServiceCall.APICall(ApplicationConstants.GetOTPforRegistration_Org, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.SendRegistrationOTPWithDPDPConsent, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            Log.i(TAG, "onPostExecute: " + result);
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {

//                        if (type == 1) {
////
//                            verifyOtp(edt_alternetMoblieno.getText().toString(), otpnumber);
//                        }


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



    public class GetOtpForAlternate extends AsyncTask<String, Void, String> {
        //ProgressDialog pd;

        int type;

        public GetOtpForAlternate(int type) {
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
            param.add(new ParamsPojo("BOCWRegNO", params[5]));
            param.add(new ParamsPojo("BeneficiaryName", params[6]));
            param.add(new ParamsPojo("ReleationID", params[7]));
            param.add(new ParamsPojo("SubOrgID", organizationId));

//            res = WebServiceCall.APICall(ApplicationConstants.GetOTPforRegistration_Org, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.SendRegistrationOTPWithDPDPConsent, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            Log.i(TAG, "onPostExecute: " + result);
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
//
                            verifyOtpForAlternate(edt_alternetMoblieno.getText().toString(), otpnumber);
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

    //    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        if (imageType == 0) {
//            isPatientPhotoAvailable = false;
//            String filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
//            destinationFilename = patientPicsFolder + filename;
//            //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
//        } else if (imageType == 1) {
//            isHealthCardPhotoAvailable = false;
//            String filename = (int) (Math.random() * 99999 + 1) + "_HC.png";
//            destinationFilename = patientPicsFolder + filename;
//            //  destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
//        } else if (imageType == 2) {
//            isRenewalSlipPhotoAvailable = false;
//            String filename = (int) (Math.random() * 99999 + 1) + "_RHC.png";
//            destinationFilename = patientPicsFolder + filename;
//            // destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
//        } else if (imageType == 3) {
//            isHivConcernPhotoAvailable = false;
//            String filename = (int) (Math.random() * 99999 + 1) + "_HIV.png";
//            destinationFilename = patientPicsFolder + filename;
//            //destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Patient Photos/" + filename;
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
//        } else if (imageType == 1) {
//            healthCardPicBm = BitmapFactory.decodeFile(destinationFilename);
//            destinationFilename = compressImage(destinationFilename);
//            healthCardPicBm = Bitmap.createScaledBitmap(healthCardPicBm, 150, 150, false);
//            imv_health_card.setImageBitmap(healthCardPicBm);
//            healthCardImagePath = destinationFilename;
//        } else if (imageType == 2) {
//            renewalPicBm = BitmapFactory.decodeFile(destinationFilename);
//            destinationFilename = compressImage(destinationFilename);
//            renewalPicBm = Bitmap.createScaledBitmap(renewalPicBm, 150, 150, false);
//            imv_renewal_form.setImageBitmap(renewalPicBm);
//            renewalImagePath = destinationFilename;
//        } else if (imageType == 3) {
//            hivConcernBm = BitmapFactory.decodeFile(destinationFilename);
//            destinationFilename = compressImage(destinationFilename);
//            hivConcernBm = Bitmap.createScaledBitmap(hivConcernBm, 150, 150, false);
//            imv_hiv_concern.setImageBitmap(hivConcernBm);
//            hivletterPath = destinationFilename;
//        }
//    }
    String destinationFilename = "";

    public void savefile(Uri sourceuri) {
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


                    if (imageType == 0) {
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

                    } else if (imageType == 1) {
                        healthCardPicBm = BitmapFactory.decodeFile(destinationFilename);
                        Utilities.getBitmapAsByteArray(healthCardPicBm);
                        destinationFilename = compressImage(destinationFilename);
                        healthCardPicBm = Bitmap.createScaledBitmap(healthCardPicBm, 150, 150, false);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imv_health_card.setImageBitmap(healthCardPicBm);
                                Bundle bundle = new Bundle();
                                bundle.putString("message", "Success");
                                Message message = new Message();
                                message.setData(bundle);
                                pd.dismiss();
//                            handler.sendMessage(message);
//                            handler.removeCallbacks(this);
                            }
                        });

                        healthCardImagePath = destinationFilename;
                    } else if (imageType == 2) {
                        renewalPicBm = BitmapFactory.decodeFile(destinationFilename);
                        Utilities.getBitmapAsByteArray(renewalPicBm);

                        destinationFilename = compressImage(destinationFilename);
                        renewalPicBm = Bitmap.createScaledBitmap(renewalPicBm, 150, 150, false);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imv_renewal_form.setImageBitmap(renewalPicBm);
                                Bundle bundle = new Bundle();
                                bundle.putString("message", "Success");
                                Message message = new Message();
                                message.setData(bundle);
                                pd.dismiss();
//                            handler.sendMessage(message);
//                            handler.removeCallbacks(this);
                            }
                        });

                        renewalImagePath = destinationFilename;
                    } else if (imageType == 3) {
                        hivConcernBm = BitmapFactory.decodeFile(destinationFilename);
                        Utilities.getBitmapAsByteArray(hivConcernBm);
                        destinationFilename = compressImage(destinationFilename);
                        hivConcernBm = Bitmap.createScaledBitmap(hivConcernBm, 150, 150, false);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imv_hiv_concern.setImageBitmap(hivConcernBm);
                                Bundle bundle = new Bundle();
                                bundle.putString("message", "Success");
                                Message message = new Message();
                                message.setData(bundle);
                                pd.dismiss();
//                            handler.sendMessage(message);
//                            handler.removeCallbacks(this);
                            }
                        });

                        hivletterPath = destinationFilename;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };
//        handler.post(runnable);

        new Thread(runnable).start();

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

        if (session.isHllUser()) {
            new GetTeamId().execute();
        }


//        checkPlayServices();
//
//        // Resuming the periodic location updates
//        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }

//        new GetOldNewMethod().execute();

    }


    private void clearDependentData() {
        binding.edtDependentList.setText("");
        binding.edtRelation.setText("");
        binding.edtFname.setText("");
        binding.txtBeneficiaryFname.setText("");
        binding.txtBeneficiaryMname.setText("");
        binding.txtBeneficiaryLname.setText("");
        relation = "";
        binding.edtDob.setText("");
        binding.edtAge.setText("");
        genderIdNew = "";
        genderId = "";
        dependentBocId = "0";

        originalAadhaar = "";
        binding.edtAadhaarno.setText("");

    }


    private void clearPatientDetails() {


        binding.llConsentImage.setVisibility(View.GONE);
        isCellularPhone = "0";
        consentPath = "";
        binding.ConsentCheckbox.setChecked(false);


        registeredPatientregdid = "";
        dependentBocId = "0";

        /// shashank 08/06/26
        btn_register.setText("Verify Beneficiary Details");
        isAlternateMessageShown = false;


        originalAadhaar = "";

        binding.edtRationCard.setText("");
        binding.edtGp.setText("");
        gpCode = "0";

        isfaceDetection = "1";

        switch_button.setChecked(false);


        DependREGID = "0";
        flag = "0";
        isDependent = "0";

        identityId = "0";
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
        edt_alternetMoblieno.setVisibility(View.VISIBLE);
        edt_landMark.setVisibility(View.GONE);
        edt_taluka.setText("");
        edt_taluka.setVisibility(View.GONE);
        isWhatsAppEnabled = "0";
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
        genderIdNew = "";
        title = "";
        RegId = "";
        patientImagePath = "";
        healthCardImagePath = "";
        renewalImagePath = "";
        hivletterPath = "";
        isPatientPhotoAvailable = false;
        isConsentAvailable = false;
        isHealthCardPhotoAvailable = false;
        isRenewalSlipPhotoAvailable = false;
        btn_register.setEnabled(true);
        tvHealthCard.setText("Beneficiary Card");
        edt_aadhaarno.setHint("Aadhaar Number*");
        edtIdentity.setVisibility(View.GONE);
        tvRegdNo.setVisibility(View.GONE);

        isOtpVerify = false;


        btn_register.setVisibility(View.GONE);


        btn_VerifyOtpContact.setEnabled(true);
        btn_VerifyOtpContact.setVisibility(View.GONE);
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
        txt_beneficiary_married_unmarried.setText("");

        txt_beneficiary_name.setText("");
        txt_beneficiary_age.setVisibility(View.GONE);
        txt_beneficiary_gender_change.setVisibility(View.GONE);
        txt_beneficiary_married_unmarried.setVisibility(View.GONE);
        binding.edtDependentList.setVisibility(View.GONE);
        binding.edtDependentList.setText("");


        if (!session.isHllUser()) {
//            new GetOldNewMethod().execute();
        }


        binding.edtDistrict.setEnabled(true);

    }

    private void
    clearPatientDetailsNew() {
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
        edt_alternetMoblieno.setVisibility(View.VISIBLE);
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
        genderIdNew = "";
        title = "";
        RegId = "";
        patientImagePath = "";
        healthCardImagePath = "";
        renewalImagePath = "";
        hivletterPath = "";
        isPatientPhotoAvailable = false;
        isConsentAvailable = false;
        isHealthCardPhotoAvailable = false;
        isRenewalSlipPhotoAvailable = false;
        btn_register.setEnabled(true);

        tvHealthCard.setText("Beneficiary Card");
        edt_aadhaarno.setHint("Aadhaar Number*");
        edtIdentity.setVisibility(View.GONE);
        tvRegdNo.setVisibility(View.GONE);
        btn_register.setVisibility(View.VISIBLE);
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
//        edt_workerregno.setText("");
        edt_workerregno.setEnabled(true);

        rg_gender.setEnabled(true);
        rb_male.setEnabled(true);
        rb_female.setEnabled(true);

        txt_beneficiary_Fname.setText("");
        txt_beneficiary_Mname.setText("");
        txt_beneficiary_Lname.setText("");
        txt_beneficiary_gender_change.setText("");
        txt_beneficiary_age.setVisibility(View.GONE);
        txt_beneficiary_gender_change.setVisibility(View.GONE);


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");

        Button btnRegList = toolbar.findViewById(R.id.btnRegisteredPatient);
        btnRegList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
                        .putExtra("healthScreentype", "0")
                        .putExtra("campType", "1")
                        .putExtra("campId", campId));
            }
        });

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


    private class InsertWorkerDetails extends AsyncTask<String, Void, String> {

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
            Log.d("InsertTeamCampMapping", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("RegistrationDetails", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertRegistrationDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertTeamCampMapping", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());


//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setIcon(R.drawable.icon_success);
//                        builder.setTitle("Success");
//                        builder.setCancelable(false);
//                        builder.setMessage("Data Inserted successfully");
//                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // finish();
//
//                                new GetWorkerInfoNew().execute("MH" + edt_workerregno.getText().toString().trim());
//
//
//
//                                //  ll_assigned.setVisibility(View.VISIBLE);
//
//
//                            }
//                        });
//                        builder.show();


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


    private class GetRelation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("RegdNo", edt_workerregno.getText().toString()));
            param.add(new ParamsPojo("ReleationID", relationId));
            param.add(new ParamsPojo("Gender", relationGenderId));
            param.add(new ParamsPojo("MARITALSTATUSID", maritalStatus));
            res = WebServiceCall.APICall(ApplicationConstants.GetRelationWiseDependantCountwithMaritalStatus, ApplicationConstants.webservice_d2d, param);
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

                                clearPatientDetails();
                                clearDependentData();

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


    private class GetRejectionDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Regdno", params[0]));
            param.add(new ParamsPojo("IsDependent", params[1]));
            param.add(new ParamsPojo("ReleationID", params[2]));
            param.add(new ParamsPojo("BeneficiaryName", params[3]));
            param.add(new ParamsPojo("Gender", params[4]));
            param.add(new ParamsPojo("Type", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionIsrejected, ApplicationConstants.webservice_d2d, param);
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
                    List<RejectionDetailsModel.Output> campList = new ArrayList<>();

                    RejectionDetailsModel rejectionDetailsModel = new Gson().fromJson(result, RejectionDetailsModel.class);
                    type = rejectionDetailsModel.getStatus();
                    message = rejectionDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (rejectionDetailsModel.getOutput().size() > 0) {
                            RejectionDetailsModel.Output output = rejectionDetailsModel.getOutput().get(0);

//                            if (output.getColumn1() == 0) {
//
//                                Utilities.showAlertDialog(context, "Alert", "Selected Relation Wrong Or Relation Count Reached", false);
//                                edt_relation.setText("");
//                                relationId = "";
//
//                            }

                            rejRegdID = output.getRejRegdid();
                            registerdCampId = output.getRejCampID();
                            regdNo = output.getRegdNo();
                            registerdCampId = output.getRejCampID();

                            isRecollectionFlag = "1";


                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", message, false);
            }
        }
    }

    private class GetTeamId extends AsyncTask<String, Void, String> {

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
                            new GetTestCount().execute(campId, teamId, userId);
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
                            saveFileAI(filePath);
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


    @SuppressLint("MissingPermission")
    public void saveFileAIIDCard(String filePath) {
        isHealthCardPhotoAvailable = true;
        String destinationFilename = filePath;
        healthCardPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = System.currentTimeMillis() + "_HC.jpg";
        String outputFilePath = getExternalCacheDir() + fName;
        // Step 2: Compress the Bitmap and store it in a new file
        try {
            // Open a file output stream to save the compressed image
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            // Compress the bitmap as JPEG with 80% quality
            healthCardPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            // Close the output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imv_health_card.setImageBitmap(healthCardPicBm);
        healthCardImagePath = outputFilePath;
    }

    @SuppressLint("MissingPermission")
    public void saveFileAIConsent(String filePath) {
        isConsentAvailable = true;
        String destinationFilename = filePath;
        consentPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = System.currentTimeMillis() + "_CF.jpg";
        String outputFilePath = getExternalCacheDir() + fName;
        // Step 2: Compress the Bitmap and store it in a new file
        try {
            // Open a file output stream to save the compressed image
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            // Compress the bitmap as JPEG with 80% quality
            consentPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            // Close the output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding.imvConsentForm.setImageBitmap(consentPicBm);
        consentPath = outputFilePath;
    }


    @SuppressLint("MissingPermission")
    public void saveFileRationCard(String filePath) {
        isRationCardPhotoAvailable = true;
        String destinationFilename = filePath;
        rationCardPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = System.currentTimeMillis() + "_RC.jpg";
        String outputFilePath = getExternalCacheDir() + fName;
        // Step 2: Compress the Bitmap and store it in a new file
        try {
            // Open a file output stream to save the compressed image
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            // Compress the bitmap as JPEG with 80% quality
            rationCardPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            // Close the output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Add Image To List
        rationCardPhotoList.add(
                new RationCardPhotoModel(
                        outputFilePath,
                        fName
                )
        );

        adapter.notifyDataSetChanged();


        binding.imvSelfDeclaration.setImageResource(
                R.drawable.icon_colorcamera
        );


        Toast.makeText(
                this,
                "Photo Added",
                Toast.LENGTH_SHORT
        ).show();


//        imv_self_declaration.setImageBitmap(rationCardPicBm);
        rationCardImagePath = outputFilePath;
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("FACE_DETECTION");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
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
                    saveFileAI(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


//        fusedLocationClient.getLastLocation()
//                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            Location location = task.getResult();
//                            // Use the location object as needed
//                             latitude = location.getLatitude();
//                             longitude = location.getLongitude();
//                            // Display or use the latitude and longitude
//                            Geocoder geocoder = new Geocoder(context);
//                            try {
//                                List<Address> geocode = geocoder.getFromLocation(latitude, longitude, 1);
//
//                                String address = geocode.get(0).getAddressLine(0);
//
//                                tvGoogleLocation.setText(address);
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//
//                            Log.e("Get Location", "no location detected");
//                            Log.w("Get Location", "getLastLocation:exception", task.getException());
//                            // Handle location null case or task failure
//                        }
//                    }
//                });
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            } else {
//                // Permission denied, handle as necessary
//            }
//        }
//    }


//---------------------------------------------------------------------------Location On Map------------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------Location On Map------------------------------------------------------------------------------------------------------


    @RequiresApi(api = Build.VERSION_CODES.O)
    void setQRCodeData(String qrCodeData) {
//        llPatientInfo.setVisibility(View.GONE);
//        VRFY_ABHA_FLAG = 1;
        try {
            ABHAQRCodeResponse abhaqrCodeResponse = new Gson().fromJson(qrCodeData, ABHAQRCodeResponse.class);
//            edtABHANumber.setText(abhaqrCodeResponse.getHidn().replace("-", ""));
//            edt_adharno.setText(abhaqrCodeResponse.getHidn().replace("-", ""));
//            edt_adharno.setEnabled(false);
//
//            edtABHANumber.setEnabled(false);
//
//            edtABHAAddress.setText(abhaqrCodeResponse.getPhr().replace(BuildConfig.CMID, ""));
//            edtABHAAddress.setEnabled(false);
            if (abhaqrCodeResponse.getName() != null && !abhaqrCodeResponse.getName().isEmpty()) {
                String[] nameArr = abhaqrCodeResponse.getName().split(" ");
//                edtFullName.setText(abhaqrCodeResponse.getName());
//                edtFullName.setEnabled(false);
                if (nameArr.length == 3) {
                    edt_fname.setText(nameArr[0]);
//                    edt_mname.setText(nameArr[1]);
//                    edt_lname.setText(nameArr[2]);
                    edt_fname.setEnabled(false);
//                    edt_mname.setEnabled(false);
//                    edt_lname.setEnabled(false);
                } else if (nameArr.length == 2) {
                    edt_fname.setText(nameArr[0]);
//                    edt_lname.setText(nameArr[1]);
                    edt_fname.setEnabled(false);
//                    edt_lname.setEnabled(false);
                }


            }
            if (abhaqrCodeResponse.getMobile() != null) {
//                edt_mobno.setText(abhaqrCodeResponse.getMobile());
//                edt_mobno.setEnabled(false);
            }
            if (abhaqrCodeResponse.getGender() != null && !abhaqrCodeResponse.getGender().isEmpty()) {
                if (abhaqrCodeResponse.getGender().equalsIgnoreCase("m") || abhaqrCodeResponse.getGender().equalsIgnoreCase("Male")) {
                    genderId = "M";

                    rg_gender.setEnabled(false);
                    rb_male.setChecked(true);
//                    spi_gender.setEnabled(false);
                } else if (abhaqrCodeResponse.getGender().equalsIgnoreCase("f") || abhaqrCodeResponse.getGender().equalsIgnoreCase("Female")) {
                    genderId = "F";

                    rb_female.setChecked(true);
                    rg_gender.setEnabled(false);
                } else {
                    genderId = "O";

//                    rb_other.setChecked(true);
//                    rb_other.setEnabled(false);
                }

//                spi_gender.setEnabled(false);
            }

            try {
                String[] dobArr = abhaqrCodeResponse.getDob().split("/");
                LocalDate date = LocalDate.of(Integer.valueOf(dobArr[2]), Integer.valueOf(dobArr[1]), Integer.valueOf(dobArr[0]));
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String strDate = dateTimeFormatter.format(date);
                edt_dob.setText(strDate);
                edt_dob.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            edt_dob.setText(abhaqrCodeResponse.getDob());
            String[] dobArr = abhaqrCodeResponse.getDob().split("-");

            int yearOfBirth = Integer.parseInt(dobArr[dobArr.length - 1]);
            int monthOfBirth = Integer.parseInt(dobArr[dobArr.length - 2]);
            int dayOfBirth = Integer.parseInt(dobArr[dobArr.length - 3]);

            edt_dob.setEnabled(false);
//            if (selectedPatientTypeId.equals("2")) {
//                ageType = "DAYS";
//                tv_select_age_title.setText("Days");
//                tv_select_age_title.setClickable(false);
//                int days = Utilities.getAgeInDays(yearOfBirth,
//                        monthOfBirth,
//                        dayOfBirth);
//                if (days > 5 || days < 2) {
//                    Utilities.showMessageString("Age should be between 2 to 5 days", context);
//                    edt_dob.setText("");
//                    edt_age.setText("");
//                } else {
//                    edt_age.setText(days + "");
//                    edt_age.setError(null);
//                    edt_age.setFocusable(false);
//                    edt_age.setLongClickable(false);
//                    edt_age.setClickable(false);
//                    tv_select_age_title.setClickable(false);
//                }
//            } else {
//                tv_select_age_title.setClickable(true);
//                int ageInYears = Integer.parseInt(Utilities.getAge(yearOfBirth,
//                        monthOfBirth,
//                        dayOfBirth));
//
//                if (ageInYears == 0) {
//                    ageType = "DAYS";
//                    tv_select_age_title.setText("Days");
//                    edt_age.setText("");
//                    edt_age.setFocusableInTouchMode(true);
//                    edt_age.setFocusable(true);
//                    edt_age.setClickable(true);
//                    edt_age.setError(null);
//                } else {
//                    ageType = "YEAR";
//                    tv_select_age_title.setText("Years");
            edt_age.setText(Utilities.getAge(yearOfBirth,
                    monthOfBirth,
                    dayOfBirth));

            edt_age.setError(null);
            edt_age.setFocusable(false);
            edt_age.setLongClickable(false);
            edt_age.setClickable(false);

//
//                }


//            }

//            selectedAuthMode = "DEMOGRAPHIC";
//            initAuth();
//            fetchAuthModes();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    BroadcastReceiver abhaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data) {
            Activity activity = HealthCheckup.getCurrentActivity();

            String authToken = data.getStringExtra("authToken");
            Log.i(TAG, "onActivityResult: ABHA" + authToken);
            if (authToken != null) {
                rbValidateUsingMobile.setChecked(false);
                binding.ilABHAMobile.setVisibility(View.GONE);
                D2DPatientRegistration_Activity.this.authToken = authToken;
                getAccountProfileAfterCreation(activity);

            }

        }
    };


    private void createABHASession() {
        pd = new ProgressDialog(context);
        pd.setMessage("Creating session..");
        pd.setCancelable(false);
        pd.show();
        ABHASessionModel createSession = new ABHASessionModel(BuildConfig.ClientID, BuildConfig.SecretId, "client_credentials");
        HealthCheckup.abhaClient.createAbhaSession(createSession, Utilities.getCurrentTimeStamp(), UUID.randomUUID().toString(), BuildConfig.CMID.replace("@", ""))
                .enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        pd.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                if (json != null) {
                                    accessToken = json.getString("accessToken");
                                    getPublicCertificate();

//                                    btn_VerifyOtp.setEnabled(true);
//                                    btn_VerifyOtpContact.setEnabled(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utilities.showMessageString(
                                        "Unable to create session",
                                        context
                                );

                            }

                        } else {
                            try {
                                Utilities.showMessageString(
                                        "Unable to create session\n" + response.errorBody().string(),
                                        context
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pd.dismiss();
                        Utilities.showMessageString(
                                "Unable to create session\n" + t.getMessage(),
                                context
                        );
                    }
                });


    }

    void getPublicCertificate() {
        HealthCheckup.ABDMClient.getPublicCertificate(
                "Bearer " + accessToken,
                Utilities.getCurrentTimeStamp(),
                UUID.randomUUID().toString()
        ).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                String res = null;
                try {

                    if (response.isSuccessful()) {
                        res = response.body().string();
                        Log.i(TAG, "onResponse: ${res}" + res);
                        JSONObject parsedJson = new JSONObject(res);
                        var key = parsedJson.getString("publicKey");
                        publicKey = key;

                    } else {

//                        Utilities.showAlertDialog(
//                                context,
//                                "Unable to generate public certificate",
//                                response.errorBody().string(),
//                                false
//                        );

                        String errRes = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errRes);
                        if (jsonObject.has("error")) {
                            JSONObject errJ = jsonObject.getJSONObject("error");
                            Utilities.showAlertDialog(context, "Unable to generate public certificate", errJ.getString("message"), false);
                        } else {
                            Utilities.showAlertDialog(context, "Unable to generate public certificate", errRes, false);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Log.e(TAG, "onResponse: ${t.message}", t);
            }
        });
    }

    void searchByABHAAddress() {
        rbValidateUsingMobile.setChecked(false);
        rbValidateUsingAadhaar.setChecked(false);
        pd.setMessage("Searching ABHA..");
        pd.setCancelable(false);
        pd.show();
        String crrTimeStamp = Utilities.getCurrentTimeStamp();
        String requestId = UUID.randomUUID().toString();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("abhaAddress", binding.edtABHAAddress.getText().toString() + BuildConfig.CMID);


        /// If beta use ABDMClient for live use PHRSBXClient

        HealthCheckup.PHRSBXClient.searchABHAAddress("Bearer " + accessToken, crrTimeStamp, requestId, jsonObject).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String healthId = jsonObject.getString("healthIdNumber");
                        String abhaAddress = jsonObject.getString("abhaAddress");
                        JSONArray authArray = jsonObject.getJSONArray("authMethods");

                        binding.edtABHANumber1.setText(healthId);
                        binding.edtABHAAddress1.setText(abhaAddress);

                        boolean isMobileModeExist = false;
                        boolean isAadhaarModeExist = false;
                        for (int i = 0; i < authArray.length(); i++) {
                            if (authArray.getString(i).equalsIgnoreCase("MOBILE_OTP")) {
                                isMobileModeExist = true;
                            }

                            if (authArray.getString(i).equalsIgnoreCase("AADHAAR_OTP")) {
                                isAadhaarModeExist = true;
                            }
                        }


                        if (isMobileModeExist) {
                            binding.rbValidateUsingMobile.setVisibility(View.VISIBLE);
                        } else {
                            binding.rbValidateUsingMobile.setVisibility(View.GONE);
                        }

                        if (isAadhaarModeExist) {
                            binding.rbValidateUsingAadhaar.setVisibility(View.VISIBLE);
                        } else {
                            binding.rbValidateUsingAadhaar.setVisibility(View.GONE);
                        }

                        binding.rgValidateOption.setVisibility(View.VISIBLE);
                        binding.btnSearchABHA.setVisibility(View.VISIBLE);
                        binding.btnSearchABHA.setText("Send OTP");
                        binding.btnSearchABHAAddress.setVisibility(View.GONE);

                    } else {
                        String errRes = response.errorBody().string();
                        JSONObject errJson = new JSONObject(errRes);
                        if (errJson.getString("code").equalsIgnoreCase("ABDM-1211")) {
                            String msg = errJson.getString("message");
                            Utilities.showAlertDialog(context, "Unable to find abha address", msg, false);
                        } else {
                            Utilities.showAlertDialog(context, "Unable to search abha address", errRes, false);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Unable to search abha address", e.getMessage(), false);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Utilities.showAlertDialog(context, "Unable to search abha address", t.getMessage(), false);

            }
        });
    }

    void sendOTPABHAAddress() {
        pd.setMessage("Sending OTP..");
        pd.setCancelable(false);
        pd.show();
        String encryptedLoginId = null;

        String loginHint, otpSystem;
        ArrayList scope = new ArrayList<String>();
        scope.add("abha-address-login");
        String crrTimeStamp = Utilities.getCurrentTimeStamp();
        String requestId = UUID.randomUUID().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                encryptedLoginId = Base64.getEncoder().encodeToString(RSAUtil.encrypt(binding.edtABHAAddress.getText().toString() + BuildConfig.CMID, publicKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (binding.rbValidateUsingMobile.isChecked()) {
            scope.add("mobile-verify");
            loginHint = "abha-address";
            otpSystem = "abdm";
        } else {


            scope.add("aadhaar-verify");
            loginHint = "abha-address";
            otpSystem = "aadhaar";

        }
        SendOTPRequestModel payload = new SendOTPRequestModel(
                txnId, scope, loginHint, encryptedLoginId, otpSystem
        );

        /// if beta use ABDMClient for live PHRSBXClient

        HealthCheckup.PHRSBXClient.sendOTPABHAAddress("Bearer " + accessToken, crrTimeStamp, requestId, payload).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    if (response.isSuccessful()) {

                        Log.d(TAG, "checkAndGenerateMobileOTP: ${response.body()}");
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        txnId = jsonObject.getString("txnId");
                        if (txnId != null) {
                            String message = jsonObject.getString("message");
                            Utilities.showMessageString(
                                    message,
                                    context
                            );

                            resetOTPUI();

                            resendOTPCount += 1;
                            binding.tvOTPAttempts.setText((resendOTPCount - 1) + "/2");
                            binding.llOTPTimer.setVisibility(View.VISIBLE);
                            binding.llOTP1.setVisibility(View.VISIBLE);
                            cdt = new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    isTimerFinished = false;
                                    Long remainingSec = (millisUntilFinished / 1000);
                                    tvTimer.setText(remainingSec + " Sec");
                                    btnResend.setEnabled(isTimerFinished);
                                }

                                @Override
                                public void onFinish() {
                                    isTimerFinished = true;
                                    btnResend.setEnabled(isTimerFinished);

                                }
                            }.start();

                            llOTP1.setVisibility(View.VISIBLE);
//                            btnGetOTP.setVisibility(View.GONE);
                            btnResend.setVisibility(View.VISIBLE);
                            btnVerify.setVisibility(View.VISIBLE);

                        }

                    } else {
//                        Utilities.showAlertDialog(context, "Error", response.errorBody().string(), false);
                        String errRes = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errRes);
                        if (jsonObject.has("error")) {
                            JSONObject errJ = jsonObject.getJSONObject("error");
                            Utilities.showAlertDialog(context, "Unable to send OTP", errJ.getString("message"), false);
                        } else {
                            Utilities.showAlertDialog(context, "Unable to send OTP", errRes, false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();

            }
        });
    }

    void sendMobileOTP() {
        pd.setMessage("Sending OTP..");
        pd.setCancelable(false);
        pd.show();
        String encryptedLoginId = null;

        String loginHint, otpSystem;
        ArrayList scope = new ArrayList<String>();
        scope.add("abha-login");
        if (binding.rbFindABHA.isChecked()) {
            if (binding.rbValidateUsingMobile.isChecked()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        encryptedLoginId = Base64.getEncoder().encodeToString(RSAUtil.encrypt(binding.edtABHAMobile.getText().toString(), publicKey));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                scope.add("mobile-verify");
                loginHint = "mobile";
                otpSystem = "abdm";
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        encryptedLoginId = Base64.getEncoder().encodeToString(RSAUtil.encrypt(binding.edtABHAAadhaar.getText().toString(), publicKey));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                scope.add("aadhaar-verify");
                loginHint = "aadhaar";
                otpSystem = "aadhaar";

            }


        } else {
            if (binding.edtABHAAddress.getText().toString().isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        encryptedLoginId = Base64.getEncoder().encodeToString(RSAUtil.encrypt(binding.edtABHANumber.getText().toString(), publicKey));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (rbValidateUsingAadhaar.isChecked()) {
                    scope.add("aadhaar-verify");
                    loginHint = "abha-number";
                    otpSystem = "aadhaar";

                } else {
                    scope.add("mobile-verify");
                    loginHint = "abha-number";
                    otpSystem = "abdm";
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        encryptedLoginId = Base64.getEncoder().encodeToString(RSAUtil.encrypt(binding.edtABHAAddress.getText().toString(), publicKey));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (rbValidateUsingAadhaar.isChecked()) {
                    scope.add("aadhaar-verify");
                    loginHint = "abha-address";
                    otpSystem = "aadhaar";

                } else {
                    scope.add("mobile-verify");
                    loginHint = "abha-address";
                    otpSystem = "abdm";
                }
            }

        }


        SendOTPRequestModel payload = new SendOTPRequestModel(
                txnId, scope, loginHint, encryptedLoginId, otpSystem
        );

        HealthCheckup.ABDMClient.loginByMobileSendOTP("Bearer " + accessToken, Utilities.getCurrentTimeStamp(), UUID.randomUUID().toString(), payload).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    if (response.isSuccessful()) {

                        Log.d(TAG, "checkAndGenerateMobileOTP: ${response.body()}");
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        txnId = jsonObject.getString("txnId");
                        if (txnId != null) {
                            String msg = jsonObject.getString("message");

                            Utilities.showMessageString(
                                    msg,
                                    context
                            );


                            resetOTPUI();

                            resendOTPCount = resendOTPCount + 1;
                            binding.tvOTPAttempts.setText((resendOTPCount - 1) + "/2");

                            binding.llOTPTimer.setVisibility(View.VISIBLE);
                            binding.llOTP1.setVisibility(View.VISIBLE);
                            cdt = new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    isTimerFinished = false;
                                    Long remainingSec = (millisUntilFinished / 1000);
                                    tvTimer.setText(remainingSec + " Sec");
                                    btnResend.setEnabled(isTimerFinished);
                                }

                                @Override
                                public void onFinish() {
                                    isTimerFinished = true;
                                    btnResend.setEnabled(isTimerFinished);
                                }
                            }.start();


                            llOTP1.setVisibility(View.VISIBLE);
//                            btnGetOTP.setVisibility(View.GONE);
                            btnResend.setVisibility(View.VISIBLE);
                            btnVerify.setVisibility(View.VISIBLE);

                        } else {
                            Utilities.showAlertDialog(context, "ABHA Number not found",
                                    "We did not find any ABHA number linked to this mobile number.\n" +
                                            "Please use ABHA linked mobile number", false);

                        }

                    } else if (response.code() == 401) {
                        createABHASession();
                        sendMobileOTP();
                    } else {
                        String errRes = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errRes);
                        if (jsonObject.has("error")) {
                            JSONObject errJ = jsonObject.getJSONObject("error");
                            Utilities.showAlertDialog(context, "Unable to send OTP", errJ.getString("message"), false);
                        } else if (errRes.contains("Invalid LoginId")) {
                            Utilities.showAlertDialog(context, "Unable to send OTP", "Aadhaar number is incorrect, Resident shall use correct Aadhaar", false);
                        } else {
                            Utilities.showAlertDialog(context, "Unable to send OTP", errRes, false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Unable to send OTP", e.getMessage(), false);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Log.e(TAG, "onFailure: ", t);
                Utilities.showAlertDialog(context, "Unable to send OTP", t.getMessage(), false);

            }
        });
    }


    void verifyMobileOTP() {

        String otp =
                binding.otpView.getText().toString();
        if (otp.length() != 6) {
            Utilities.showMessageString("Please enter OTP", context);
            return;
        }
        pd.setMessage("Verifying OTP..");
        pd.setCancelable(false);
        pd.show();
        String crrTimeStamp = Utilities.getCurrentTimeStamp();

        // Generate a new request ID
        String requestId = UUID.randomUUID().toString();

        // Variable to store encrypted OTP
        String encryptedOTP = null;

        // Check if Android version is Oreo (API 26) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                // Encrypt OTP and encode in Base64
                encryptedOTP = Base64.getEncoder().encodeToString(RSAUtil.encrypt(otp, publicKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String loginHint, otpSystem;
        ArrayList scope = new ArrayList<String>();
        scope.add("abha-login");
        if (binding.rbFindABHA.isChecked()) {
            if (rbValidateUsingAadhaar.isChecked()) {
                scope.add("aadhaar-verify");
                loginHint = "aadhaar";
                otpSystem = "aadhaar";

            } else {
                scope.add("mobile-verify");
                loginHint = "mobile";
                otpSystem = "abdm";
            }
        } else {
            if (rbValidateUsingAadhaar.isChecked()) {
                scope.add("aadhaar-verify");
                loginHint = "abha-number";
                otpSystem = "aadhaar";

            } else {
                scope.add("mobile-verify");
                loginHint = "abha-number";
                otpSystem = "abdm";
            }
        }

        // Create the payload object
        VerifyMobileOTPRequestModel payload = new VerifyMobileOTPRequestModel(
                new ArrayList<>(scope),
                new com.myhindlab.abkat.abha.models.verify_mobile_otp.AuthData(
                        new ArrayList<>(Arrays.asList("otp")),
                        new com.myhindlab.abkat.abha.models.verify_mobile_otp.Otp(
                                crrTimeStamp,
                                txnId,
                                encryptedOTP
                        )
                )
        );

        HealthCheckup.ABDMClient.loginByMobileVerifyOTP("Bearer " + accessToken, crrTimeStamp, requestId, payload)
                .enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                        pd.dismiss();
                        try {
                            if (response.isSuccessful()) {

                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String authResult = jsonObject.getString("authResult");
                                String message = jsonObject.getString("message");
                                if (authResult.equalsIgnoreCase("success")) {
                                    txnId = jsonObject.getString("txnId");
                                    authToken = jsonObject.getString("token");
                                    JSONArray accountArr = jsonObject.getJSONArray("accounts");
                                    binding.edtABHAMobile.setEnabled(false);
                                    binding.llOTP1.setVisibility(View.GONE);
                                    resendOTPCount = 0;
                                    cdt.cancel();
//                        btnGetOTP.setVisibility(View.GONE);
//                        btnResend.setVisibility(View.VISIBLE);
//                        btnVerify.setVisibility(View.VISIBLE);

                                    if (binding.rbABHAVerify.isChecked() || (binding.rbFindABHA.isChecked() && (binding.rbValidateUsingAadhaar.isChecked() || binding.rbValidateUsingMobile.isChecked()))) {
                                        getAccountProfile();
                                    } else {
                                        verifyUser(accountArr);
                                    }

                                } else {
                                    binding.otpView.getText().clear();
                                    binding.edtABHAMobile.setEnabled(true);
                                    Utilities.showAlertDialog(
                                            context,
                                            "Unable to verify OTP",
                                            message,
                                            false
                                    );
                                }
                            } else {
                                binding.llOTP1.setVisibility(View.GONE);
                                binding.otpView.getText().clear();
                                binding.edtABHAMobile.setEnabled(true);
                                String errRes = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errRes);
                                if (jsonObject.has("error")) {
                                    JSONObject errJ = jsonObject.getJSONObject("error");
                                    Utilities.showAlertDialog(context, "Unable to verify OTP", errJ.getString("message"), false);
                                } else if (errRes.contains("Invalid LoginId")) {
                                    Utilities.showAlertDialog(context, "Unable to verify OTP", "Aadhaar number is incorrect, Resident shall use correct Aadhaar", false);
                                } else {
                                    Utilities.showAlertDialog(context, "Unable to verify OTP", errRes, false);
                                }
                            }
                        } catch (Exception e) {
                            binding.llOTP1.setVisibility(View.GONE);

                            binding.otpView.getText().clear();
                            binding.edtABHAMobile.setEnabled(true);
                            e.printStackTrace();
                            Utilities.showAlertDialog(context, "Unable to verify OTP", e.getMessage(), false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        binding.llOTP1.setVisibility(View.GONE);
                        binding.otpView.getText().clear();
                        binding.edtABHAMobile.setEnabled(true);
                        Utilities.showAlertDialog(
                                context,
                                "Unable to verify OTP",
                                t.getMessage(),
                                false
                        );
                    }
                });
    }

    void verifyOTPABHAAddress() {

        String otp =
                binding.otpView.getText().toString();
        if (otp.length() != 6) {
            Utilities.showMessageString("Please enter OTP", context);
            return;
        }
        pd.setMessage("Verifying OTP..");
        pd.setCancelable(false);
        pd.show();
        String crrTimeStamp = Utilities.getCurrentTimeStamp();

        // Generate a new request ID
        String requestId = UUID.randomUUID().toString();

        // Variable to store encrypted OTP
        String encryptedOTP = null;

        // Check if Android version is Oreo (API 26) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                // Encrypt OTP and encode in Base64
                encryptedOTP = Base64.getEncoder().encodeToString(RSAUtil.encrypt(otp, publicKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String loginHint, otpSystem;
        ArrayList scope = new ArrayList<String>();
        scope.add("abha-address-login");
        if (rbValidateUsingAadhaar.isChecked()) {
            scope.add("aadhaar-verify");
            loginHint = "aadhaar";
            otpSystem = "aadhaar";

        } else {
            scope.add("mobile-verify");
            loginHint = "mobile";
            otpSystem = "abdm";
        }


        // Create the payload object
        VerifyMobileOTPRequestModel payload = new VerifyMobileOTPRequestModel(
                new ArrayList<>(scope),
                new com.myhindlab.abkat.abha.models.verify_mobile_otp.AuthData(
                        new ArrayList<>(Arrays.asList("otp")),
                        new com.myhindlab.abkat.abha.models.verify_mobile_otp.Otp(
                                crrTimeStamp,
                                txnId,
                                encryptedOTP
                        )
                )
        );

        /// if beta use ABDMClient for live PHRSBXClient

        HealthCheckup.PHRSBXClient.verifyOTPABHAAddress("Bearer " + accessToken, crrTimeStamp, requestId, payload).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                pd.dismiss();
                try {
                    if (response.isSuccessful()) {

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String authResult = jsonObject.getString("authResult");
                        if (authResult.equalsIgnoreCase("success")) {
                            JSONObject tokenObj = jsonObject.getJSONObject("tokens");
                            authToken = tokenObj.getString("token");
                            binding.edtABHAMobile.setEnabled(false);
                            binding.llOTP1.setVisibility(View.GONE);
//                            binding.llSearchABHABtn.setVisibility(View.GONE);
                            resendOTPCount = 0;

                            cdt.cancel();
                            getAccountProfileABHAAddress();

                        } else {
                            String message = jsonObject.getString("message");
                            binding.otpView.getText().clear();
                            binding.edtABHAMobile.setEnabled(true);
                            Utilities.showAlertDialog(
                                    context,
                                    "Unable to verify OTP",
                                    message,
                                    false
                            );
                        }
                    } else {
                        binding.otpView.getText().clear();
                        binding.edtABHAMobile.setEnabled(true);
//                        ABHAErrorResponseModel abhaErrorResponseModel = new Gson().fromJson(response.errorBody().string(), ABHAErrorResponseModel.class);
//                        Utilities.showAlertDialog(context, "Unable to verify OTP", response.errorBody().string(), false);

                        String errRes = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errRes);
                        if (jsonObject.has("error")) {
                            JSONObject errJ = jsonObject.getJSONObject("error");
                            Utilities.showAlertDialog(context, "Unable to verify OTP", errJ.getString("message"), false);
                        } else {
                            Utilities.showAlertDialog(context, "Unable to verify OTP", errRes, false);
                        }
                    }
                } catch (Exception e) {
                    binding.otpView.getText().clear();
                    binding.edtABHAMobile.setEnabled(true);
                    e.printStackTrace();
                    Utilities.showAlertDialog(
                            context,
                            "Unable to verify OTP",
                            e.getMessage(),
                            false
                    );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.otpView.getText().clear();
                binding.edtABHAMobile.setEnabled(true);
                Utilities.showAlertDialog(
                        context,
                        "Unable to verify OTP",
                        t.getMessage(),
                        false
                );
            }
        });
    }


    void verifyUser(JSONArray accountArr) throws JSONException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        JSONObject accObj = accountArr.getJSONObject(0);
        String encodedNumber = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encodedNumber = Base64.getEncoder().encodeToString(RSAUtil.encrypt(accObj.getString("ABHANumber"), publicKey));
        }

        JsonObject payload = new JsonObject();
        payload.addProperty("ABHANumber", accObj.getString("ABHANumber"));

        payload.addProperty("txnId", txnId);
        HealthCheckup.ABDMClient.verifyLoginProfileUser("Bearer " + accessToken, Utilities.getCurrentTimeStamp(), UUID.randomUUID().toString(), "Bearer " + authToken, payload)
                .enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                binding.llOTP1.setVisibility(View.GONE);
//                                binding.llSearchABHABtn.setVisibility(View.GONE);
                                cdt.cancel();
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                authToken = jsonObject.getString("token");

                                getAccountProfile();

                            } else {
                                binding.llOTP1.setVisibility(View.GONE);

                                binding.otpView.getText().clear();
                                binding.edtABHAMobile.setEnabled(true);
//                                ABHAErrorResponseModel abhaErrorResponseModel = new Gson().fromJson(response.errorBody().string(), ABHAErrorResponseModel.class);
//                                Utilities.showAlertDialog(context, "Unable to verify user", response.errorBody().string(), false);
                                String errRes = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errRes);
                                if (jsonObject.has("error")) {
                                    JSONObject errJ = jsonObject.getJSONObject("error");
                                    Utilities.showAlertDialog(context, "Unable to verify user", errJ.getString("message"), false);
                                } else {
                                    Utilities.showAlertDialog(context, "Unable to verify user", errRes, false);
                                }
                            }
                        } catch (Exception e) {
                            binding.llOTP1.setVisibility(View.GONE);

                            binding.otpView.getText().clear();
                            binding.edtABHAMobile.setEnabled(true);
                            e.printStackTrace();
                            Utilities.showAlertDialog(
                                    context,
                                    "Unable to verify User",
                                    e.getMessage(),
                                    false
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        binding.llOTP1.setVisibility(View.GONE);

                        Utilities.showAlertDialog(
                                context,
                                "Unable to verify User",
                                t.getMessage(),
                                false
                        );
                    }
                });
    }


    public class GetMaritalStatusList extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetMaritalMaster, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    MaritalStatusModel maritalStatusModel = new Gson().fromJson(result, MaritalStatusModel.class);
                    type = maritalStatusModel.getStatus();
                    message = maritalStatusModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<MaritalStatusModel.Output> statusList = maritalStatusModel.getOutput();
                        if (statusList.size() > 0) {
                            // camptypelist.add(0, new TalukaModel().new Output("ALL", 0));
                            showStatusDialogue(statusList);
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

        private void showStatusDialogue(final List<MaritalStatusModel.Output> statusList) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Marital Status");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < statusList.size(); i++) {
                arrayAdapter.add(String.valueOf(statusList.get(i).getMaritalstatus()));
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
                    txt_beneficiary_married_unmarried.setText(statusList.get(which).getMaritalstatus());
                    maritalStatus = String.valueOf(statusList.get(which).getMaritalstatusid());

                    /// dependent list
                    clearDependentData();
                    edt_relation.setText("");

                }
            });
            builderSingle.show();

        }

    }

    private void getAccountProfile() {

        try {
            pd.setMessage("Getting Profile...");
            pd.setCancelable(false);
            pd.show();

            String timeStamp = Utilities.getCurrentTimeStamp();
            String requestId = UUID.randomUUID().toString();

            HealthCheckup.ABDMClient.accountProfile(
                    "Bearer " + accessToken,
                    timeStamp,
                    requestId,
                    "Bearer " + authToken
            ).enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {

                            binding.llABHANumAndAddress.setVisibility(View.VISIBLE);

                            AccountProfileResponseModel accountProfileRequestModel =
                                    new Gson().fromJson(response.body().string(), AccountProfileResponseModel.class);

//                            if (binding.edtFname.getText().toString().isEmpty()) {
                            binding.edtFname.setText(accountProfileRequestModel.getName());
//                            binding.txtBeneficiaryFname.setText(accountProfileRequestModel.getFirstName() == null ? "" : accountProfileRequestModel.getFirstName());
//                            binding.txtBeneficiaryMname.setText(accountProfileRequestModel.getMiddleName() == null ? "" : accountProfileRequestModel.getMiddleName());
//                            binding.txtBeneficiaryLname.setText(accountProfileRequestModel.getLastName() == null ? "" : accountProfileRequestModel.getLastName());
//                            binding.edtTaluka.setText(accountProfileRequestModel.getSubdistrictName() == null ? "" : accountProfileRequestModel.getSubdistrictName());

//                            talukaId = accountProfileRequestModel.getDistrictCode();
                            distLgdCode = accountProfileRequestModel.getDistrictCode();

                            if (distLgdCode == null || distLgdCode.isEmpty()) {
                                Utilities.showAlertDialog(context, "Alert", "Some of details not getting from abha u can register through without abha", false);

                                clearPatientDetails();
                                clearPatientDetails(1);
                                clearPatientDetails(2);
                                return;

                            }

                            Log.d(TAG, "taluka: " + talukaId + "district :" + distLgdCode);


                            binding.edtDistrict.setText(accountProfileRequestModel.getDistrictName() == null ? "" : accountProfileRequestModel.getDistrictName());
                            binding.edtDistrict.setEnabled(false);

                            if (distLgdCode != null && talukaId == null) {
                                binding.edtTaluka.setEnabled(true);

                            }

                            binding.edtCurrentAddress.setText("");
                            binding.edtLandMark.setText("");
                            binding.btnVerifyOtp.setEnabled(false);
                            binding.btnVerifyOtpContact.setEnabled(false);


                            /// shashank 21/01/26

                            if (rg_dep_no.isChecked()) {
                                edt_relation.setText("Self");
                            }


                            if (rg_dep_yes.isChecked()) {

                                binding.edtAddress.setEnabled(false);
                                binding.edtPincode.setEnabled(false);
                                binding.edtDob.setEnabled(false);
                                binding.edtFname.setEnabled(false);


                                binding.edtDistrict.setEnabled(false);


                                if (isDependent.equalsIgnoreCase("1")) {


                                    /// middle and last name logic
//                                    String depName = binding.edtFname.getText().toString().trim();
//
//                                    String[] dependentName =
//                                            depName.trim().split("\\s+");
//
//                                    StringBuilder remainingNameDependent = new StringBuilder();
//                                    for (int i = 1; i < dependentName.length; i++) {
//                                        remainingNameDependent.append(dependentName[i]);
//                                        if (i < dependentName.length - 1) {
//                                            remainingNameDependent.append(" ");
//                                        }
//                                    }
//
//                                    String dependentLastName = remainingNameDependent.toString();


                                    /// only last name logic
                                    String depName = binding.edtFname.getText().toString().trim();

                                    String[] dependentName = depName.split("\\s+");

                                    StringBuilder remainingNameDependent = new StringBuilder();


                                    // take ONLY the last word
                                    if (dependentName.length > 0) {
                                        remainingNameDependent.append(dependentName[dependentName.length - 1]);
                                    }

                                    String dependentLastName = remainingNameDependent.toString();


                                    /// For middle and last name logic
//                                    String[] befName = txt_beneficiary_name.getText().toString().trim().split("\\s+");
//
//                                    StringBuilder remainingName = new StringBuilder();
//                                    for (int i = 1; i < befName.length; i++) {
//                                        remainingName.append(befName[i]);
//                                        if (i < befName.length - 1) {
//                                            remainingName.append(" ");Create
//                                        }
//                                    }
//
//                                    String boardMiddleLastName = remainingName.toString();

                                    /// Only Last name logic

                                    String[] befName = txt_beneficiary_name
                                            .getText()
                                            .toString()
                                            .trim()
                                            .split("\\s+");

                                    StringBuilder remainingName = new StringBuilder();


                                    // take ONLY the last word
                                    if (befName.length > 0) {
                                        remainingName.append(befName[befName.length - 1]);
                                    }

                                    String boardMiddleLastName = remainingName.toString();


                                    if (!dependentLastName.equalsIgnoreCase(boardMiddleLastName)) {
                                        Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Worker Board Last Name: " + boardMiddleLastName + "\n" + "ABHA last Name: " + dependentLastName, false);
                                        clearABHA();
//                binding.btnClearABHAAddress.setEnabled(false);
                                        clearPatientDetails(1);
                                        clearPatientDetails(2);
                                        disableABHAFormAfterFill();

                                        /// shashank 21/01/26
                                        clearPatientDetails();
//                            disableAllViews();
                                        return;
                                    }else {

                                        Utilities.showAlertDialog(context,"Success","Details Matched\n" +
                                                "\n" +
                                                "ABHA and Board details match. The consent link has been shared to the registered mobile number. Kindly provide your consent to proceed further.",true);

                                        new GetOtpForAbhaConsent(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);


                                    }


                                    String depNameText = binding.edtFname.getText().toString().trim();


                                    // Split by spaces
                                    String[] nameParts = depNameText.split("\\s+");

                                    String firstName = "";
                                    String middleName = "";
                                    String lastName = "";

                                    if (nameParts.length > 0) {
                                        firstName = nameParts[0];              // Shashank
                                    }

                                    if (nameParts.length > 2) {
                                        middleName = nameParts[1];             // Dattatray
                                    }

                                    if (nameParts.length > 1) {
                                        lastName = nameParts[nameParts.length - 1]; // Kadam
                                    }


                                    // Set values
                                    txt_beneficiary_Fname.setText(firstName);
                                    txt_beneficiary_Mname.setText(middleName);
                                    txt_beneficiary_Lname.setText(lastName);

                                    txt_beneficiary_Fname.setEnabled(false);
                                    txt_beneficiary_Mname.setEnabled(false);
                                    txt_beneficiary_Lname.setEnabled(false);


//
//                if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                    clearABHA();
////                binding.btnClearABHAAddress.setEnabled(false);
//                    clearPatientDetails(1);
//                    clearPatientDetails(2);
//                    disableABHAFormAfterFill();
////                            disableAllViews();
//                    return;
//                }
                                }


                            }
                            ///

                            btn_register.setVisibility(View.VISIBLE);
                            btn_register.setEnabled(true);

//                            } else {
//
//                            }
                            binding.edtMoblieno.setText(accountProfileRequestModel.getMobile() != null ? accountProfileRequestModel.getMobile() : "");
                            int age = 0;
                            if (accountProfileRequestModel.getYearOfBirth() != null && accountProfileRequestModel.getMonthOfBirth() != null && accountProfileRequestModel.getDayOfBirth() != null) {
                                binding.edtDob.setText(accountProfileRequestModel.getYearOfBirth() + "/" + accountProfileRequestModel.getMonthOfBirth() + "/" + accountProfileRequestModel.getDayOfBirth());
                                age = Utilities.getAge(Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getYearOfBirth())), Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getMonthOfBirth())), Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getDayOfBirth())));
                                binding.edtAge.setText(String.valueOf(age));
                            } else {
                                binding.edtAge.setText("");
                                binding.edtDob.setText("");
                            }

                            if (accountProfileRequestModel.getGender().equalsIgnoreCase("M")) {
                                binding.rbMale.setChecked(true);
                                binding.rbMale.setEnabled(false);
                                binding.rbFemale.setEnabled(false);
                                abhaGender = "Male";
                            } else if (accountProfileRequestModel.getGender().equalsIgnoreCase("F")) {
                                binding.rbFemale.setChecked(true);
                                binding.rbMale.setEnabled(false);
                                binding.rbFemale.setEnabled(false);
                                abhaGender = "Female";
                            } else {
                                binding.rbOther.setChecked(true);
                                binding.rgGender.setEnabled(false);
                                abhaGender = "Other";
                            }
                            binding.edtAddress.setText(accountProfileRequestModel.getAddress() + "," + accountProfileRequestModel.getPincode());
                            binding.edtPincode.setText(accountProfileRequestModel.getPincode());
                            binding.edtABHAAddress.setText(accountProfileRequestModel.getPreferredAbhaAddress().replace(BuildConfig.CMID, ""));
                            binding.edtABHAAddress1.setText(accountProfileRequestModel.getPreferredAbhaAddress().replace(BuildConfig.CMID, ""));
                            binding.edtABHANumber1.setText(accountProfileRequestModel.getABHANumber());


//                            String aadhaar = binding.edtABHAAadhaar.getText().toString().trim();
//
//                            if (!aadhaar.isEmpty()) {
//                                edt_aadhaarno.setText(aadhaar);
//                            } else {
//                                edt_aadhaarno.setVisibility(View.GONE);
//                            }

                            String aadhaar = binding.edtABHAAadhaar.getText().toString().trim();

                            if (!aadhaar.isEmpty()) {

                                originalAadhaar = aadhaar.replace("-", "");

                                edt_aadhaarno.setText(maskAadhaar(originalAadhaar));

                            } else {

                                /// chnaged gone to visible
                                edt_aadhaarno.setVisibility(View.VISIBLE);
                            }


                            isABHANoValid = true;
                            btnSeeCard.setVisibility(View.VISIBLE);


//                            if (isDependent.equalsIgnoreCase("0")) {
//                                if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                                    clearABHA();
//                                    clearPatientDetails(1);
//                                    clearPatientDetails(2);
//                                    disableABHAFormAfterFill();
//
//                                    /// shashank 22/01/26
//                                    clearPatientDetails();
//                                }
//                            }


//
                            String abhaName = Utilities.normalize(binding.edtFname.getText().toString());
                            String boardName = Utilities.normalize(benefBoardName);


                            Log.d(TAG, "abha name :" + abhaName);
                            Log.d(TAG, "boardName :" + boardName);

                            if (isDependent.equalsIgnoreCase("0")) {
                                if (!abhaName.equals(boardName)) {
                                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                                    clearABHA();
                                    clearPatientDetails(1);
                                    clearPatientDetails(2);
                                    disableABHAFormAfterFill();

                                    /// shashank 22/01/26
                                    clearPatientDetails();
                                }else {
                                    Utilities.showAlertDialog(context,"Success","Details Matched\n" +
                                            "\n" +
                                            "ABHA and Board details match. The consent link has been shared to the registered mobile number. Kindly provide your consent to proceed further.",true);

                                    new GetOtpForAbhaConsent(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);

                                }
                            }

                            disableIfFilled();
                            disableABHAFormAfterFill();



                        } else {

//                        ABHAErrorResponseModel abhaErrorResponseModel = new Gson().fromJson(response.errorBody().string(), ABHAErrorResponseModel.class);
//                            Utilities.showAlertDialog(context, "Unable to get Account Profile", response.errorBody().string(), false);
                            String errRes = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errRes);
                            if (jsonObject.has("error")) {
                                JSONObject errJ = jsonObject.getJSONObject("error");
                                Utilities.showAlertDialog(context, "Unable to get Account Profile", errJ.getString("message"), false);
                            } else {
                                Utilities.showAlertDialog(context, "Unable to get Account Profile", errRes, false);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Unable to get Account Profile", e.getMessage(), false);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    Utilities.showAlertDialog(context, "Unable to get Account Profile", t.getMessage(), false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Utilities.showAlertDialog(context, "Unable to get Account Profile", e.getMessage(), false);
        }


    }


    private void getAccountProfileAfterCreation(Activity activity) {
        try {
            if (activity != null && !activity.isFinishing()) {
                pd = new ProgressDialog(activity);
                pd.setMessage("Getting Profile...");
                pd.setCancelable(false);
                pd.show();
            }
            String timeStamp = Utilities.getCurrentTimeStamp();
            String requestId = UUID.randomUUID().toString();

            HealthCheckup.ABDMClient.accountProfile(
                    "Bearer " + accessToken,
                    timeStamp,
                    requestId,
                    "Bearer " + authToken
            ).enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    if (pd != null) {
                        pd.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {

                            binding.llABHANumAndAddress.setVisibility(View.VISIBLE);

                            AccountProfileResponseModel accountProfileRequestModel =
                                    new Gson().fromJson(response.body().string(), AccountProfileResponseModel.class);

//                            if (binding.edtFname.getText().toString().isEmpty()) {
                            binding.edtFname.setText(accountProfileRequestModel.getName());
//                            binding.txtBeneficiaryFname.setText(accountProfileRequestModel.getFirstName() == null ? "" : accountProfileRequestModel.getFirstName());
//                            binding.txtBeneficiaryMname.setText(accountProfileRequestModel.getMiddleName() == null ? "" : accountProfileRequestModel.getMiddleName());
//                            binding.txtBeneficiaryLname.setText(accountProfileRequestModel.getLastName() == null ? "" : accountProfileRequestModel.getLastName());

//                            binding.edtTaluka.setText(accountProfileRequestModel.getSubdistrictName() == null ? "" : accountProfileRequestModel.getSubdistrictName());


//                            talukaId = accountProfileRequestModel.getDistrictCode();
                            distLgdCode = accountProfileRequestModel.getDistrictCode();


                            if (distLgdCode == null || distLgdCode.isEmpty()) {
                                Utilities.showAlertDialog(context, "Alert", "Some of details not getting from abha u can register through without abha", false);

                                clearPatientDetails();
                                clearPatientDetails(1);
                                clearPatientDetails(2);
                                return;

                            }

                            Log.d(TAG, "taluka: " + talukaId + "district :" + distLgdCode);


                            binding.edtDistrict.setText(accountProfileRequestModel.getDistrictName() == null ? "" : accountProfileRequestModel.getDistrictName());
                            binding.edtDistrict.setEnabled(false);

                            if (distLgdCode != null && talukaId == null) {
                                binding.edtTaluka.setEnabled(true);

                            }

                            binding.edtCurrentAddress.setText("");
                            binding.edtLandMark.setText("");

                            binding.btnVerifyOtp.setEnabled(false);
                            binding.btnVerifyOtpContact.setEnabled(false);
                            btn_register.setVisibility(View.VISIBLE);
                            btn_register.setEnabled(true);


//                            }
                            binding.edtMoblieno.setText(accountProfileRequestModel.getMobile() != null ? accountProfileRequestModel.getMobile() : "");
                            binding.edtDob.setText(accountProfileRequestModel.getYearOfBirth() + "/" + accountProfileRequestModel.getMonthOfBirth() + "/" + accountProfileRequestModel.getDayOfBirth());
                            int age = Utilities.getAge(Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getYearOfBirth())), Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getMonthOfBirth())), Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getDayOfBirth())));
                            binding.edtAge.setText(String.valueOf(age));


                            if (accountProfileRequestModel.getGender().equalsIgnoreCase("M")) {
                                binding.rbMale.setChecked(true);
                                binding.rbMale.setEnabled(false);
                                binding.rbFemale.setEnabled(false);
                                abhaGender = "Male";
                            } else if (accountProfileRequestModel.getGender().equalsIgnoreCase("F")) {
                                binding.rbFemale.setChecked(true);
                                binding.rbMale.setEnabled(false);
                                binding.rbFemale.setEnabled(false);
                                abhaGender = "Female";
                            } else {
                                binding.rbOther.setChecked(true);
                                binding.rgGender.setEnabled(false);
                                abhaGender = "Other";
                            }
                            binding.edtAddress.setText(accountProfileRequestModel.getAddress() + "," + accountProfileRequestModel.getPincode());

                            binding.edtABHAAddress.setText(accountProfileRequestModel.getPreferredAbhaAddress().replace(BuildConfig.CMID, ""));
                            binding.edtABHAAddress1.setText(accountProfileRequestModel.getPreferredAbhaAddress().replace(BuildConfig.CMID, ""));
                            binding.edtABHANumber1.setText(accountProfileRequestModel.getABHANumber());
                            binding.edtPincode.setText(accountProfileRequestModel.getPincode());


//                            String aadhaar = binding.edtABHAAadhaar.getText().toString().trim();
//
//                            if (!aadhaar.isEmpty()) {
//                                edt_aadhaarno.setText(aadhaar);
//                            } else {
//                                edt_aadhaarno.setVisibility(View.GONE);
//                            }


                            String aadhaar = binding.edtABHAAadhaar.getText().toString().trim();

                            if (!aadhaar.isEmpty()) {

                                originalAadhaar = aadhaar.replace("-", "");

                                edt_aadhaarno.setText(maskAadhaar(originalAadhaar));

                            } else {


                                /// changed Gone to visible
                                edt_aadhaarno.setVisibility(View.VISIBLE);
                            }


                            isABHANoValid = true;


                            /// shashank 21/01/26

                            if (rg_dep_no.isChecked()) {
                                edt_relation.setText("Self");
                            }


                            if (rg_dep_yes.isChecked()) {

                                binding.edtAddress.setEnabled(false);
                                binding.edtPincode.setEnabled(false);
                                binding.edtDob.setEnabled(false);
                                binding.edtFname.setEnabled(false);
                                binding.edtTaluka.setEnabled(false);
                                binding.edtDistrict.setEnabled(false);


                                if (isDependent.equalsIgnoreCase("1")) {


                                    /// middle and last name logic
//                                    String depName = binding.edtFname.getText().toString().trim();
//
//                                    String[] dependentName =
//                                            depName.trim().split("\\s+");
//
//                                    StringBuilder remainingNameDependent = new StringBuilder();
//                                    for (int i = 1; i < dependentName.length; i++) {
//                                        remainingNameDependent.append(dependentName[i]);
//                                        if (i < dependentName.length - 1) {
//                                            remainingNameDependent.append(" ");
//                                        }
//                                    }
//
//                                    String dependentLastName = remainingNameDependent.toString();


                                    /// only last name logic
                                    String depName = binding.edtFname.getText().toString().trim();

                                    String[] dependentName = depName.split("\\s+");

                                    StringBuilder remainingNameDependent = new StringBuilder();


                                    // take ONLY the last word
                                    if (dependentName.length > 0) {
                                        remainingNameDependent.append(dependentName[dependentName.length - 1]);
                                    }

                                    String dependentLastName = remainingNameDependent.toString();


                                    /// For middle and last name logic
//                                    String[] befName = txt_beneficiary_name.getText().toString().trim().split("\\s+");
//
//                                    StringBuilder remainingName = new StringBuilder();
//                                    for (int i = 1; i < befName.length; i++) {
//                                        remainingName.append(befName[i]);
//                                        if (i < befName.length - 1) {
//                                            remainingName.append(" ");
//                                        }
//                                    }
//
//                                    String boardMiddleLastName = remainingName.toString();

                                    /// Only Last name logic

                                    String[] befName = txt_beneficiary_name
                                            .getText()
                                            .toString()
                                            .trim()
                                            .split("\\s+");

                                    StringBuilder remainingName = new StringBuilder();


                                    // take ONLY the last word
                                    if (befName.length > 0) {
                                        remainingName.append(befName[befName.length - 1]);
                                    }

                                    String boardMiddleLastName = remainingName.toString();


                                    if (!dependentLastName.equalsIgnoreCase(boardMiddleLastName)) {
                                        Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Worker Board Last Name: " + boardMiddleLastName + "\n" + "ABHA last Name: " + dependentLastName, false);
                                        clearABHA();
//                binding.btnClearABHAAddress.setEnabled(false);
                                        clearPatientDetails(1);
                                        clearPatientDetails(2);
                                        disableABHAFormAfterFill();
                                        /// shashank 21/01/26
                                        clearPatientDetails();
//                            disableAllViews();
                                        return;
                                    }else {


                                        Utilities.showAlertDialog(context,"Success","Details Matched\n" +
                                                "\n" +
                                                "ABHA and Board details match. The consent link has been shared to the registered mobile number. Kindly provide your consent to proceed further.",true);

                                        new GetOtpForAbhaConsent(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);

                                    }


                                    String depNameText = binding.edtFname.getText().toString().trim();


                                    // Split by spaces
                                    String[] nameParts = depNameText.split("\\s+");

                                    String firstName = "";
                                    String middleName = "";
                                    String lastName = "";

                                    if (nameParts.length > 0) {
                                        firstName = nameParts[0];              // Shashank
                                    }

                                    if (nameParts.length > 2) {
                                        middleName = nameParts[1];             // Dattatray
                                    }

                                    if (nameParts.length > 1) {
                                        lastName = nameParts[nameParts.length - 1]; // Kadam
                                    }


                                    // Set values
                                    txt_beneficiary_Fname.setText(firstName);
                                    txt_beneficiary_Mname.setText(middleName);
                                    txt_beneficiary_Lname.setText(lastName);

                                    txt_beneficiary_Fname.setEnabled(false);
                                    txt_beneficiary_Mname.setEnabled(false);
                                    txt_beneficiary_Lname.setEnabled(false);


//
//                if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                    clearABHA();
////                binding.btnClearABHAAddress.setEnabled(false);
//                    clearPatientDetails(1);
//                    clearPatientDetails(2);
//                    disableABHAFormAfterFill();
////                            disableAllViews();
//                    return;
//                }
                                }


                            }

//                            **********************Dependent Change************************


//                            if (isDependent.equalsIgnoreCase("0")) {
//                                if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                                    clearABHA();
////                                binding.btnClearABHAAddress.setEnabled(false);
//                                    clearPatientDetails(1);
//                                    clearPatientDetails(2);
//                                    clearPatientDetails();
//
//                                }
//                            }


                            String abhaName = Utilities.normalize(binding.edtFname.getText().toString());
                            String boardName = Utilities.normalize(benefBoardName);

                            Log.d(TAG, "abha name :" + abhaName);
                            Log.d(TAG, "boardName :" + boardName);

                            if (isDependent.equalsIgnoreCase("0")) {
                                if (!abhaName.equals(boardName)) {
                                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                                    clearABHA();
                                    clearPatientDetails(1);
                                    clearPatientDetails(2);
                                    disableABHAFormAfterFill();

                                    /// shashank 22/01/26
                                    clearPatientDetails();
                                }else {

                                    Utilities.showAlertDialog(context,"Success","Details Matched\n" +
                                            "\n" +
                                            "ABHA and Board details match. The consent link has been shared to the registered mobile number. Kindly provide your consent to proceed further.",true);

                                    new GetOtpForAbhaConsent(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);

                                }
                            }


                            disableIfFilled();
                            disableABHAFormAfterFill();



                        } else {
//                        ABHAErrorResponseModel abhaErrorResponseModel = new Gson().fromJson(response.errorBody().string(), ABHAErrorResponseModel.class);
//                            Utilities.showAlertDialog(context, "Unable to get Account Profile", response.errorBody().string(), false);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        Utilities.showAlertDialog(context, "Unable to get Account Profile", e.getMessage(), false);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (pd != null) {
                        pd.dismiss();
                    }
//                    Utilities.showAlertDialog(context, "Unable to get Account Profile", t.getMessage(), false);
                }
            });

        } catch (Exception e) {
            if (pd != null) {
                pd.dismiss();
            }
            e.printStackTrace();
//            Utilities.showAlertDialog(context, "Unable to get Account Profile", e.getMessage(), false);

        }

    }


    private void getAccountProfileABHAAddress() {
        pd = new ProgressDialog(D2DPatientRegistration_Activity.this);
        pd.setMessage("Getting Profile...");
        pd.setCancelable(false);
        pd.show();

        String timeStamp = Utilities.getCurrentTimeStamp();
        String requestId = UUID.randomUUID().toString();


        /// if beta use ABDMClient for live PHRSBXClient

        HealthCheckup.PHRSBXClient.getABHAProfileAddress(
                "Bearer " + accessToken,
                timeStamp,
                requestId,
                "Bearer " + authToken
        ).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    if (response.isSuccessful()) {

                        binding.llABHANumAndAddress.setVisibility(View.VISIBLE);
                        AccountProfileABHAAddress accountProfileRequestModel =
                                new Gson().fromJson(response.body().string(), AccountProfileABHAAddress.class);
//                        if (binding.edtFname.getText().toString().isEmpty()) {
                        binding.edtFname.setText(accountProfileRequestModel.getFullName());
//                        binding.txtBeneficiaryFname.setText(accountProfileRequestModel.getFirstName() == null ? "" : accountProfileRequestModel.getFirstName());
//                        binding.txtBeneficiaryMname.setText(accountProfileRequestModel.getMiddleName() == null ? "" : accountProfileRequestModel.getMiddleName());
//                        binding.txtBeneficiaryLname.setText(accountProfileRequestModel.getLastName() == null ? "" : accountProfileRequestModel.getLastName());

//                        binding.edtTaluka.setText(accountProfileRequestModel.getSubDistrictName() == null ? "" : accountProfileRequestModel.getSubDistrictName());


//                        talukaId = accountProfileRequestModel.getDistrictCode();
                        distLgdCode = accountProfileRequestModel.getDistrictCode();

                        if (distLgdCode == null || distLgdCode.isEmpty()) {
                            Utilities.showAlertDialog(context, "Alert", "Some of details not getting from abha u can register through without abha", false);

                            clearPatientDetails();
                            clearPatientDetails(1);
                            clearPatientDetails(2);
                            return;

                        }

                        Log.d(TAG, "taluka: " + talukaId + "district :" + distLgdCode);


                        binding.edtDistrict.setText(accountProfileRequestModel.getDistrictName() == null ? "" : accountProfileRequestModel.getDistrictName());
                        binding.edtDistrict.setEnabled(false);

                        if (distLgdCode != null && talukaId == null) {
                            binding.edtTaluka.setEnabled(true);
                        }

                        binding.edtCurrentAddress.setText("");
                        binding.edtLandMark.setText("");
                        binding.btnVerifyOtp.setEnabled(false);
                        binding.btnVerifyOtpContact.setEnabled(false);
                        btn_register.setVisibility(View.VISIBLE);
                        btn_register.setEnabled(true);


//                        }
                        binding.edtMoblieno.setText(accountProfileRequestModel.getMobile() != null ? accountProfileRequestModel.getMobile() : "");
                        try {
                            String dob = accountProfileRequestModel.getYearOfBirth() + "/" + accountProfileRequestModel.getMonthOfBirth() + "/" + accountProfileRequestModel.getDayOfBirth();
                            Date parseDOB = Utilities.dfDate4.parse(dob);
                            binding.edtDob.setText(Utilities.dfDate4.format(parseDOB));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        int age = Utilities.getAge(Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getYearOfBirth())), Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getMonthOfBirth())), Integer.parseInt(Objects.requireNonNull(accountProfileRequestModel.getDayOfBirth())));
                        binding.edtAge.setText(String.valueOf(age));

                        if (accountProfileRequestModel.getGender().equalsIgnoreCase("M")) {
                            binding.rbMale.setChecked(true);
                            binding.rbMale.setEnabled(false);
                            binding.rbFemale.setEnabled(false);
                            abhaGender = "Male";
                        } else if (accountProfileRequestModel.getGender().equalsIgnoreCase("F")) {
                            binding.rbFemale.setChecked(true);
                            binding.rbMale.setEnabled(false);
                            binding.rbFemale.setEnabled(false);
                            abhaGender = "Female";
                        } else {
                            binding.rbOther.setChecked(true);
                            binding.rgGender.setEnabled(false);
                            abhaGender = "Other";
                        }


                        /// shashank 21/01/26

                        if (rg_dep_no.isChecked()) {
                            edt_relation.setText("Self");
                        }


                        //                                ********Dependent Change*****


//                        if (rg_dep_yes.isChecked()) {
//
//                            binding.edtAddress.setEnabled(false);
//                            binding.edtPincode.setEnabled(false);
//                            binding.edtDob.setEnabled(false);
//                            binding.edtFname.setEnabled(false);
//                            binding.edtTaluka.setEnabled(false);
//                            binding.edtDistrict.setEnabled(false);
//
//
//                            if (isDependent.equalsIgnoreCase("1")) {
//
//                                String depName = binding.edtFname.getText().toString().trim();
//
//                                String[] dependentName =
//                                        depName.trim().split("\\s+");
//
//                                StringBuilder remainingNameDependent = new StringBuilder();
//                                for (int i = 1; i < dependentName.length; i++) {
//                                    remainingNameDependent.append(dependentName[i]);
//                                    if (i < dependentName.length - 1) {
//                                        remainingNameDependent.append(" ");
//                                    }
//                                }
//
//                                String dependentLastName = remainingNameDependent.toString();
//
//
//                                String[] befName = txt_beneficiary_name.getText().toString().trim().split("\\s+");
//
//                                StringBuilder remainingName = new StringBuilder();
//                                for (int i = 1; i < befName.length; i++) {
//                                    remainingName.append(befName[i]);
//                                    if (i < befName.length - 1) {
//                                        remainingName.append(" ");
//                                    }
//                                }
//
//                                String boardMiddleLastName = remainingName.toString();
//
//
//
//                                if (!dependentLastName.equalsIgnoreCase(boardMiddleLastName)) {
//                                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Worker Board Name: " + boardMiddleLastName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                                    clearABHA();
////                binding.btnClearABHAAddress.setEnabled(false);
//                                    clearPatientDetails(1);
//                                    clearPatientDetails(2);
//                                    disableABHAFormAfterFill();
//
//                                    /// shashank 21/01/26
//                                    clearPatientDetails();
////                            disableAllViews();
//                                    return;
//                                }
//
//
////                if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
////                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
////                    clearABHA();
//////                binding.btnClearABHAAddress.setEnabled(false);
////                    clearPatientDetails(1);
////                    clearPatientDetails(2);
////                    disableABHAFormAfterFill();
//////                            disableAllViews();
////                    return;
////                }
//                            }
//
//
//                        }


//                                      ********Dependent Change  13/02/26***

                        if (rg_dep_yes.isChecked()) {

                            binding.edtAddress.setEnabled(false);
                            binding.edtPincode.setEnabled(false);
                            binding.edtDob.setEnabled(false);
                            binding.edtFname.setEnabled(false);
                            binding.edtTaluka.setEnabled(false);
                            binding.edtDistrict.setEnabled(false);


                            if (isDependent.equalsIgnoreCase("1")) {


                                /// middle and last name logic
//                                    String depName = binding.edtFname.getText().toString().trim();
//
//                                    String[] dependentName =
//                                            depName.trim().split("\\s+");
//
//                                    StringBuilder remainingNameDependent = new StringBuilder();
//                                    for (int i = 1; i < dependentName.length; i++) {
//                                        remainingNameDependent.append(dependentName[i]);
//                                        if (i < dependentName.length - 1) {
//                                            remainingNameDependent.append(" ");
//                                        }
//                                    }
//
//                                    String dependentLastName = remainingNameDependent.toString();


                                /// only last name logic
                                String depName = binding.edtFname.getText().toString().trim();

                                String[] dependentName = depName.split("\\s+");

                                StringBuilder remainingNameDependent = new StringBuilder();


                                // take ONLY the last word
                                if (dependentName.length > 0) {
                                    remainingNameDependent.append(dependentName[dependentName.length - 1]);
                                }

                                String dependentLastName = remainingNameDependent.toString();


                                /// For middle and last name logic
//                                    String[] befName = txt_beneficiary_name.getText().toString().trim().split("\\s+");
//
//                                    StringBuilder remainingName = new StringBuilder();
//                                    for (int i = 1; i < befName.length; i++) {
//                                        remainingName.append(befName[i]);
//                                        if (i < befName.length - 1) {
//                                            remainingName.append(" ");
//                                        }
//                                    }
//
//                                    String boardMiddleLastName = remainingName.toString();

                                /// Only Last name logic

                                String[] befName = txt_beneficiary_name
                                        .getText()
                                        .toString()
                                        .trim()
                                        .split("\\s+");

                                StringBuilder remainingName = new StringBuilder();


                                // take ONLY the last word
                                if (befName.length > 0) {
                                    remainingName.append(befName[befName.length - 1]);
                                }

                                String boardMiddleLastName = remainingName.toString();


                                if (!dependentLastName.equalsIgnoreCase(boardMiddleLastName)) {
                                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Worker Board Last Name: " + boardMiddleLastName + "\n" + "ABHA last Name: " + dependentLastName, false);
                                    clearABHA();
//                binding.btnClearABHAAddress.setEnabled(false);
                                    clearPatientDetails(1);
                                    clearPatientDetails(2);
                                    disableABHAFormAfterFill();

                                    /// shashank 21/01/26
                                    clearPatientDetails();
//                            disableAllViews();
                                    return;
                                }else {

                                    Utilities.showAlertDialog(context,"Success","Details Matched\n" +
                                            "\n" +
                                            "ABHA and Board details match. The consent link has been shared to the registered mobile number. Kindly provide your consent to proceed further.",true);

                                    new GetOtpForAbhaConsent(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);


                                }


                                String depNameText = binding.edtFname.getText().toString().trim();


                                // Split by spaces
                                String[] nameParts = depNameText.split("\\s+");

                                String firstName = "";
                                String middleName = "";
                                String lastName = "";

                                if (nameParts.length > 0) {
                                    firstName = nameParts[0];              // Shashank
                                }

                                if (nameParts.length > 2) {
                                    middleName = nameParts[1];             // Dattatray
                                }

                                if (nameParts.length > 1) {
                                    lastName = nameParts[nameParts.length - 1]; // Kadam
                                }


                                // Set values
                                txt_beneficiary_Fname.setText(firstName);
                                txt_beneficiary_Mname.setText(middleName);
                                txt_beneficiary_Lname.setText(lastName);

                                txt_beneficiary_Fname.setEnabled(false);
                                txt_beneficiary_Mname.setEnabled(false);
                                txt_beneficiary_Lname.setEnabled(false);


//
//                if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                    Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                    clearABHA();
////                binding.btnClearABHAAddress.setEnabled(false);
//                    clearPatientDetails(1);
//                    clearPatientDetails(2);
//                    disableABHAFormAfterFill();
////                            disableAllViews();
//                    return;
//                }
                            }


                        }


                        binding.edtAddress.setText(accountProfileRequestModel.getAddress() + "," + accountProfileRequestModel.getPinCode());
                        binding.edtABHAAddress1.setText(accountProfileRequestModel.getAbhaAddress().replace(BuildConfig.CMID, ""));
                        binding.edtABHANumber1.setText(accountProfileRequestModel.getAbhaNumber());
                        edt_pincode.setText(accountProfileRequestModel.getPinCode());


//                        String aadhaar = binding.edtABHAAadhaar.getText().toString().trim();
//
//                        if (!aadhaar.isEmpty()) {
//                            edt_aadhaarno.setText(aadhaar);
//                        } else {
//                            edt_aadhaarno.setVisibility(View.GONE);
//                        }


                        String aadhaar = binding.edtABHAAadhaar.getText().toString().trim();

                        if (!aadhaar.isEmpty()) {

                            originalAadhaar = aadhaar.replace("-", "");

                            edt_aadhaarno.setText(maskAadhaar(originalAadhaar));

                        } else {

                            /// changed got to visible
                            edt_aadhaarno.setVisibility(View.VISIBLE);

                        }

                        isABHANoValid = true;
                        btnSeeCard.setVisibility(View.VISIBLE);
                        whichABHACard = 2;


//                        if (isDependent.equalsIgnoreCase("0")) {
//                            if (!binding.edtFname.getText().toString().equalsIgnoreCase(benefBoardName)) {
//                                Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
//                                clearABHA();
////                            binding.btnClearABHAAddress.setEnabled(false);
//                                clearPatientDetails(1);
//                                clearPatientDetails(2);
//
//                                /// shashank 22/01/26
//
//                                clearPatientDetails();
//
//                            }
//                        }


                        String abhaName = Utilities.normalize(binding.edtFname.getText().toString());
                        String boardName = Utilities.normalize(benefBoardName);


                        Log.d(TAG, "abha name :" + abhaName);
                        Log.d(TAG, "boardName :" + boardName);

                        if (isDependent.equalsIgnoreCase("0")) {
                            if (!abhaName.equals(boardName)) {
                                Utilities.showAlertDialog(context, "Board and ABHA Details Mismatch", "ABHA and Board details does not match\n\nDetails:\n" + "Board Name: " + benefBoardName + "\n" + "ABHA Name: " + binding.edtFname.getText().toString() + "\nBoard Gender: " + benefBoardGender + "\nABHA Gender: " + abhaGender, false);
                                clearABHA();
                                clearPatientDetails(1);
                                clearPatientDetails(2);
                                disableABHAFormAfterFill();

                                /// shashank 22/01/26
                                clearPatientDetails();
                            }else {
                                Utilities.showAlertDialog(context,"Success","Details Matched\n" +
                                        "\n" +
                                        "ABHA and Board details match. The consent link has been shared to the registered mobile number. Kindly provide your consent to proceed further.",true);

                                new GetOtpForAbhaConsent(1).execute(edt_moblieno.getText().toString(), otpnumber, edt_workerregno.getText().toString(), userId, "0", "MH" + edt_workerregno.getText().toString().trim(), edt_fname.getText().toString().trim(), relationId);

                            }
                        }

                        disableIfFilled();

                        disableABHAFormAfterFill();




                    } else {
                        ABHAErrorResponseModel abhaErrorResponseModel = new Gson().fromJson(response.errorBody().string(), ABHAErrorResponseModel.class);

//                        Utilities.showAlertDialog(context, "Unable to get Account Profile", abhaErrorResponseModel.getError().getMessage(), false);

                        String errRes = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errRes);
                        if (jsonObject.has("error")) {
                            JSONObject errJ = jsonObject.getJSONObject("error");
                            Utilities.showAlertDialog(context, "Unable to get Account Profile", errJ.getString("message"), false);
                        } else {
                            Utilities.showAlertDialog(context, "Unable to get Account Profile", errRes, false);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Unable to get Account Profile", e.getMessage(), false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Utilities.showAlertDialog(context, "Unable to get Account Profile", t.getMessage(), false);
            }
        });
    }

    void updateABHAQueue(String abhaAddress, Integer campId) throws JSONException {
        pd.setMessage("Please wait..");
        pd.setCancelable(false);
        pd.show();
        HealthCheckup.abkatApiClient.updateQueFlag(abhaAddress, campId, 1).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    String res = response.body().string();

                    Log.i(TAG, "onResponse: " + res);
                    if (response.isSuccessful()) {


                    } else {
                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });

    }


    void resetOTPUI() {
        binding.otpView.getText().clear();
        svOTPMsg.setVerticalGravity(View.VISIBLE);
        tvTimer.setText("00:00");
        tvOTPMsg.setText("We just sent an OTP on the Mobile Number for communication. Enter the OTP below to proceed with ABHA creation");
        llOTPTimer.setVisibility(View.VISIBLE);

    }


    void clearABHA() {
        if (cdt != null) {
            cdt.cancel();
        }

        binding.rgFindOrVerifyABHA.setVisibility(View.VISIBLE);
        binding.rgValidateOption.setVisibility(View.VISIBLE);
//        edtABHAToken.setText("");
//        edtABHAToken.setVisibility(View.GONE);
        binding.edtABHAAddress1.setText("");
        binding.edtFname.setText("");
        binding.edtABHANumber1.setText("");
        binding.llABHANumAndAddress.setVisibility(View.GONE);
        binding.edtABHAMobile.setText("");
        if (binding.rbFindABHA.isChecked()) {
            binding.edtABHAAddress.setText("");
            binding.edtABHANumber.setText("");
        }
        binding.edtABHAAadhaar.setText("");
        binding.llOTP1.setVisibility(View.GONE);
        binding.llSearchABHABtn.setVisibility(View.VISIBLE);
        btnSearchABHA.setVisibility(View.VISIBLE);
        binding.btnClearABHAAddress.setVisibility(View.VISIBLE);
        enableABHAFormAfterFill();
        resendOTPCount = 0;
    }

    void disableABHAFormAfterFill() {
        binding.btnCreateABHA.setEnabled(false);
        binding.rgFindOrVerifyABHA.setEnabled(false);
        binding.rgValidateOption.setEnabled(false);
        binding.edtABHAMobile.setEnabled(false);
        binding.edtABHAAadhaar.setEnabled(false);
        binding.edtABHAAddress.setEnabled(false);
        binding.edtABHANumber.setEnabled(false);
        binding.edtABHAAddress1.setEnabled(false);
        binding.edtABHANumber1.setEnabled(false);
        binding.btnSearchABHAAddress.setEnabled(false);
        binding.btnSearchABHA.setEnabled(false);
        binding.tvSeeQueue.setEnabled(false);
        binding.imvQrCode.setEnabled(false);
        binding.rbABHAVerify.setEnabled(false);
        binding.rbFindABHA.setEnabled(false);
        binding.rbValidateUsingAadhaar.setEnabled(false);
        binding.rbValidateUsingMobile.setEnabled(false);

    }

    void enableABHAFormAfterFill() {
        binding.btnCreateABHA.setEnabled(true);
        binding.rgFindOrVerifyABHA.setEnabled(true);
        binding.rgValidateOption.setEnabled(true);
        binding.edtABHAMobile.setEnabled(true);
        binding.edtABHAAadhaar.setEnabled(true);
        binding.edtABHAAddress.setEnabled(true);
        binding.edtABHANumber.setEnabled(true);
        binding.edtABHAAddress1.setEnabled(true);
        binding.edtABHANumber1.setEnabled(true);
        binding.btnSearchABHA.setEnabled(true);
        binding.btnSearchABHAAddress.setEnabled(true);
        binding.tvSeeQueue.setEnabled(true);
        binding.imvQrCode.setEnabled(true);
        binding.rbABHAVerify.setEnabled(true);
        binding.rbFindABHA.setEnabled(true);
        binding.rbValidateUsingAadhaar.setEnabled(true);
        binding.rbValidateUsingMobile.setEnabled(true);

        binding.edtRationCard.setEnabled(true);


    }

    private void clearPatientDetails(int clearFlag) {
        if (clearFlag != 1) {
            binding.edtABHANumber.getText().clear();
            binding.edtABHAAddress.getText().clear();
            binding.edtABHAAddress1.setText("");
            binding.edtABHANumber1.setText("");
        }

        registeredPatientregdid = "";


//        originalAadhaar = "";

        /// SHASHANK 19/02/26
//        DependREGID = "0";


        /// shashank 20/01/26

//        flag = "0";

//        isDependent = "0";
//        rg_dep_no.setChecked(true);
//        rg_dep_yes.setChecked(false);


        /// Shashank 06/04/2026

//        count = "";
//        if (clearFlag != 2) {
//            regdID = "";
//        }


        isReregistration = false;
        if (clearFlag != 3) {
            edt_fname.setText("");
        }
        edt_moblieno.setText("");

        /// uncommit to commit

//        edt_aadhaarno.setText("");
//        edt_aadhaarno.setFocusable(true);
//        edt_aadhaarno.setFocusableInTouchMode(true);


        edt_dob.setText("");
        edt_age.setText("");
        edt_address.setText("");
        edt_local_address.setText("");
        edt_current_address.setText("");
        edt_pincode.setText("");
        edt_renewal_date.setText("");
        edt_education.setText("");


        /////shashank 12/02/26
//        edt_relation.setText("");


        sw_renewed.setChecked(false);
//        binding.edtAadhaarCard.setText("");
//        rb_mr.setChecked(true);
//        rb_mrs.setChecked(false);
//        rb_ms.setChecked(false);
//        rb_male.setEnabled(false);
//        rb_female.setEnabled(false);
//        rb_other.setEnabled(false);
        sw_renewed.setChecked(false);
        rg_gender.clearCheck();
        imv_patient.setImageDrawable(getResources().getDrawable(R.drawable.icon_patientcamera));
        imv_health_card.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
        imv_renewal_form.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
        imv_hiv_concern.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
//        imv_adhar_card.setImageDrawable(getResources().getDrawable(R.drawable.icon_colorcamera));
        patientPicBm = null;
        healthCardPicBm = null;
        renewalPicBm = null;
        hivConcernBm = null;
//        aadharBm = null;
//        addharletterPath = "";
        genderId = "";
        genderIdNew = "";
        title = "";
        RegId = "";
        patientImagePath = "";
        healthCardImagePath = "";
        renewalImagePath = "";
        hivletterPath = "";
        isPatientPhotoAvailable = false;
        isConsentAvailable = false;
        isHealthCardPhotoAvailable = false;
        isRenewalSlipPhotoAvailable = false;
        btn_register.setEnabled(false);

        tvHealthCard.setText("Beneficiary Card");
        edt_aadhaarno.setHint("Aadhaar Number");
        edtIdentity.setVisibility(View.GONE);
        tvRegdNo.setVisibility(View.GONE);

//        edtOTP1.getText().clear();
//        edtOTP2.getText().clear();
//        edtOTP3.getText().clear();
//        edtOTP4.getText().clear();
//        edtOTP5.getText().clear();
//        edtOTP6.getText().clear();

        binding.llOTP.setVisibility(View.GONE);
        binding.btnSubmitOTP.setVisibility(View.GONE);
//        rbNoAadhaar.setChecked(true);


    }

    public class GetTaluka extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetAllTalukaList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    TalukaModel talukaModel = new Gson().fromJson(result, TalukaModel.class);
                    type = talukaModel.getStatus();
                    message = talukaModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<TalukaModel.Output> camptypelist = talukaModel.getOutput();
                        if (camptypelist.size() > 0) {
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

        private void showTalukaDialogue(final List<TalukaModel.Output> talukalist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Taluka");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < talukalist.size(); i++) {
                arrayAdapter.add(String.valueOf(talukalist.get(i).gettALNAME()));
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
                    edt_taluka.setText(talukalist.get(which).gettALNAME());
                    talukaId = String.valueOf(talukalist.get(which).gettLLGDCODE());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }

    public class GetDistrictList extends AsyncTask<String, Void, String> {

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


            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
//                              districtList.add(0, new DistrictList_Model("0", "All"));

                            showDistrictListDialog(districtList);
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

    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < districtList.size(); i++) {
            arrayAdapter.add(String.valueOf(districtList.get(i).getDISTNAME()));
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
                edt_district.setText(districtList.get(which).getDISTNAME());
                distLgdCode = districtList.get(which).getDISTLGDCODE();
                //  refreshCalendar();
                edt_taluka.setText("");
                edt_taluka.setEnabled(true);
                edt_taluka.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_downarrowbk), null);

            }
        });
        builderSingle.show();

    }

    void findABHA() {
        pd.setMessage("Searching ABHA..");
        pd.setCancelable(false);
        pd.show();
        String encryptedLoginId = null;

        String crrTimeStamp = Utilities.getCurrentTimeStamp();
        String requestId = UUID.randomUUID().toString();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                encryptedLoginId = Base64.getEncoder().encodeToString(RSAUtil.encrypt(binding.edtABHAMobile.getText().toString(), publicKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        jsonElements.add("search-abha");
        jsonObject.add("scope", jsonElements);
        jsonObject.addProperty("mobile", encryptedLoginId);
        HealthCheckup.ABDMClient.findABHA("Bearer " + accessToken, crrTimeStamp, requestId, BuildConfig.BENEFIT_NAME, jsonObject).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.i(TAG, "onResponse: " + res);
                        java.lang.reflect.Type listType = new TypeToken<List<ABHASearchResponse>>() {
                        }.getType();
                        List<ABHASearchResponse> responseList = new Gson().fromJson(res, listType);
                        if (!responseList.isEmpty()) {
                            txnId = responseList.get(0).getTxnId();
                        }

                        Utility.Companion.showRecyclerListDialog(
                                context, // context
                                "Select ABHA Details",
                                responseList.get(0).getABHA(),
                                new Function1<ABHA, String>() {
                                    @Override
                                    public String invoke(ABHA abha) {
                                        return abha.getName() + "\n" + abha.getABHANumber() + "\n" + abha.getGender();
                                    }
                                },
                                new Function1<ABHA, Unit>() {
                                    @Override
                                    public Unit invoke(ABHA selected) {
                                        Log.d("TAG", "Selected abha: " + selected.getABHANumber());
                                        selectedFindABHAIndex = selected.getIndex();
                                        sendMobileOTPIndex(String.valueOf(selected.getIndex()));
                                        return Unit.INSTANCE;
                                    }
                                }
                        );
                    } else {
                        //{"error":{"code":"ABDM-1114","message":"User not found."}}
                        String res = response.errorBody().string();
                        if (res.contains("User not found.")) {
                            Utilities.showAlertDialog(context, "Not Found", "The mobile number you have entered does not match with any of the records. Please enter a different number.", false);
                        } else {
                            Utilities.showAlertDialog(context, "Not Found", res, false);
                        }
                        Log.e(TAG, "onResponse: " + response.body().string());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: ", e);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Log.e(TAG, "onFailure: ", t);
            }
        });


    }

    void sendMobileOTPIndex(String index) {
        pd.setMessage("Sending OTP..");
        pd.setCancelable(false);
        pd.show();
        String encryptedLoginId = null;

        String loginHint, otpSystem;
        ArrayList scope = new ArrayList<String>();
        scope.add("abha-login");
        scope.add("search-abha");
        scope.add("mobile-verify");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                encryptedLoginId = Base64.getEncoder().encodeToString(RSAUtil.encrypt(index, publicKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        loginHint = "index";
        otpSystem = "abdm";


        SendOTPRequestModel payload = new SendOTPRequestModel(
                txnId, scope, loginHint, encryptedLoginId, otpSystem
        );

        HealthCheckup.ABDMClient.loginByMobileSendOTP("Bearer " + accessToken, Utilities.getCurrentTimeStamp(), UUID.randomUUID().

                        toString(), payload).

                enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse
                            (Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        pd.dismiss();
                        try {
                            if (response.isSuccessful()) {

                                Log.d(TAG, "checkAndGenerateMobileOTP: ${response.body()}");
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                txnId = jsonObject.getString("txnId");
                                if (txnId != null) {
                                    String msg = jsonObject.getString("message");

                                    Utilities.showMessageString(
                                            msg,
                                            context
                                    );


                                    resetOTPUI();

                                    resendOTPCount = resendOTPCount + 1;
                                    binding.tvOTPAttempts.setText((resendOTPCount - 1) + "/2");

                                    binding.llOTPTimer.setVisibility(View.VISIBLE);
                                    binding.llOTP1.setVisibility(View.VISIBLE);
                                    cdt = new CountDownTimer(60000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            isTimerFinished = false;
                                            Long remainingSec = (millisUntilFinished / 1000);
                                            tvTimer.setText(remainingSec + " Sec");
                                            btnResend.setEnabled(isTimerFinished);
                                        }

                                        @Override
                                        public void onFinish() {
                                            isTimerFinished = true;
                                            btnResend.setEnabled(isTimerFinished);
                                        }
                                    }.start();


                                    llOTP1.setVisibility(View.VISIBLE);
//                            btnGetOTP.setVisibility(View.GONE);
                                    btnResend.setVisibility(View.VISIBLE);
                                    btnVerify.setVisibility(View.VISIBLE);

                                } else {
                                    Utilities.showAlertDialog(context, "ABHA Number not found",
                                            "We did not find any ABHA number linked to this mobile number.\n" +
                                                    "Please use ABHA linked mobile number", false);

                                }

                            } else if (response.code() == 401) {
                                createABHASession();
                                sendMobileOTP();
                            } else {
                                String errRes = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errRes);
                                if (jsonObject.has("error")) {
                                    JSONObject errJ = jsonObject.getJSONObject("error");
                                    Utilities.showAlertDialog(context, "Unable to send OTP", errJ.getString("message"), false);
                                } else if (errRes.contains("Invalid LoginId")) {
                                    Utilities.showAlertDialog(context, "Unable to send OTP", "Aadhaar number is incorrect, Resident shall use correct Aadhaar", false);
                                } else {
                                    Utilities.showAlertDialog(context, "Unable to send OTP", errRes, false);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Utilities.showAlertDialog(context, "Unable to send OTP", e.getMessage(), false);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pd.dismiss();
                        Log.e(TAG, "onFailure: ", t);
                        Utilities.showAlertDialog(context, "Unable to send OTP", t.getMessage(), false);

                    }
                });
    }


    private void getFaceDetectionFlag() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<FaceDetectionModel> call = apiService.getFaceDetectionData(userId);
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
                        isBoardDataCompalsory = String.valueOf(o.getActiveRegFlag());

                        if (faceDetectionCompulsory.equals("0")) {
                            LL_skipFaceDetection.setVisibility(View.VISIBLE);
                        } else {
                            LL_skipFaceDetection.setVisibility(View.GONE);
                        }


                        Log.d(TAG, "Face Detection Flag" + faceDetectionCompulsory + " " + isBoardDataCompalsory);

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


    private void getDependentList() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<GetDependentListModel> call = apiService.getDependentListNew("MH" + edt_workerregno.getText().toString().trim(), labourage, benefBoardGender, maritalStatus);
        call.enqueue(new retrofit2.Callback<GetDependentListModel>() {
            @Override
            public void onResponse(Call<GetDependentListModel> call, retrofit2.Response<GetDependentListModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<GetDependentListModel.Output> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showDependentListDialog(outputItems);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Alert", message, false);
                    }

                } else {
                    Utilities.showAlertDialog(context, "Alert", "Server not responding", false);

                }
            }

            @Override
            public void onFailure(Call<GetDependentListModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void getDependentRescreeningData() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<GetDependentListModel> call = apiService.getDependentRescreeningData(rejRegdID);
        call.enqueue(new retrofit2.Callback<GetDependentListModel>() {
            @Override
            public void onResponse(Call<GetDependentListModel> call, retrofit2.Response<GetDependentListModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<GetDependentListModel.Output> outputItems = response.body().getOutput();


                        if (outputItems != null && !outputItems.isEmpty()) {

                            GetDependentListModel.Output output = outputItems.get(0);

                            relationId = output.getRelId();
                            String dob = output.getdOB();
                            binding.edtRelation.setText(output.getRelName());
                            dependentBocId = output.getBocw_idDepend();
                            Log.d(TAG, "bocDependent: " + dependentBocId);


                            binding.edtDependentList.setText(output.getEnglishName());
                            binding.edtDependentList.setVisibility(View.GONE);


                            String dependentName = output.getEnglishName();


                            String[] nameParts = dependentName.split("\\s+");

                            String firstName = "";
                            String middleName = "";
                            String lastName = "";

                            if (nameParts.length > 0) {
                                firstName = nameParts[0];              // Shashank
                            }

                            if (nameParts.length > 2) {
                                middleName = nameParts[1];             // Dattatray
                            }

                            if (nameParts.length > 1) {
                                lastName = nameParts[nameParts.length - 1]; // Kadam
                            }


                            txt_beneficiary_Fname.setText(firstName);
                            txt_beneficiary_Mname.setText(middleName);
                            txt_beneficiary_Lname.setText(lastName);


                            new GetRelation().execute();


                            switch (relationId) {
                                case "1":
                                case "5":
                                case "7":
                                case "9":
                                case "17":
                                case "22":
                                    genderId = "M";
                                    rb_male.setChecked(true);
                                    rb_female.setChecked(false);
                                    rb_female.setEnabled(false); //Enabled On 05/02/2021 suggested by sumit
                                    rb_male.setEnabled(true);
                                    edt_dob.setText("");
                                    edt_age.setText("");

                                    binding.rbOther.setChecked(false);
                                    binding.rbOther.setEnabled(false);
                                    break;

                                case "2":
                                case "6":
                                case "8":
                                case "10":
                                case "18":
                                case "21":
                                    genderId = "F";
                                    rb_male.setChecked(false);
                                    rb_female.setChecked(true);
                                    rb_male.setEnabled(false); //Enabled On 05/02/2021 suggested by sumit
                                    rb_female.setEnabled(true);
                                    edt_dob.setText("");
                                    edt_age.setText("");

                                    binding.rbOther.setChecked(false);
                                    binding.rbOther.setEnabled(false);

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

                            if (relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
                                    relationId.equalsIgnoreCase("21") || relationId.equalsIgnoreCase("22")) {


                                /// rescreening
//                                txt_beneficiary_Mname.setText("");


                            }

                            if (Type.equalsIgnoreCase("0")) {
                                new GetRejectionDetails().execute(edt_workerregno.getText().toString().trim(), "1", relationId, txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim(), genderId, "2");
                            }


                            getDependentRegistrationStatus("MH" + edt_workerregno.getText().toString().trim(), output.getFullname());


                            if (output.getgPLGDCODE() != null) {

                                gpCode = output.getgPLGDCODE();
                            }

                            if (output.getgPNAME() != null) {
                                binding.edtGp.setText(output.getgPNAME());

                            }

                            if (output.getIsUrban() != null) {
                                if (output.getIsUrban().equals("0")) {
                                    binding.radioRural.setChecked(true);
                                    binding.edtGp.setVisibility(View.VISIBLE);
                                } else {
                                    binding.radioUrban.setChecked(true);
                                    binding.edtGp.setVisibility(View.GONE);
                                    binding.edtGp.setText("");

                                    gpCode = "0";
                                }
                            }

//                String dob = trenchList.get(which).getdOB();

//                            try {
//                                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//                                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
//
//                                Date date = inputFormat.parse(dob);
//
//                                if (date != null) {
//                                    binding.edtDob.setText(outputFormat.format(date));
//                                }
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }


                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

                                Date date = inputFormat.parse(dob);

                                if (date != null) {
                                    binding.edtDob.setText(outputFormat.format(date));
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }


//                        if (outputItems.size() > 0) {
//                            showDependentListDialog(outputItems);
//                        }


                    } else {
                        Utilities.showAlertDialog(context, "Alert", message, false);
                        binding.edtDependentList.setVisibility(View.GONE);

                    }

                } else {
                    Utilities.showAlertDialog(context, "Alert", "Server not responding", false);
                    binding.edtDependentList.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<GetDependentListModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void getGramPanchayat() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<GpModel> call = apiService.getGramPanchayat(talukaId);
        call.enqueue(new retrofit2.Callback<GpModel>() {
            @Override
            public void onResponse(Call<GpModel> call, retrofit2.Response<GpModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<GpModel.Output> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showGpList(outputItems);

                        }
                    } else {
                        Utilities.showAlertDialog(context, "Alert", message, false);
                    }

                } else {
                    Utilities.showAlertDialog(context, "Alert", "Server not responding", false);

                }
            }

            @Override
            public void onFailure(Call<GpModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void showDependentListDialog(final List<GetDependentListModel.Output> trenchList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Dependent");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getFullname());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                binding.edtDependentList.setText(trenchList.get(which).getFullname());
//                mobilenumber = trenchList.get(which).getMobileNo();


                relationId = trenchList.get(which).getRelId();

                String dob = trenchList.get(which).getdOB();

                binding.edtRelation.setText(trenchList.get(which).getRelation());

                dependentBocId = trenchList.get(which).getBocw_idDepend();


                Log.d(TAG, "bocDependent: " + dependentBocId);


                String dependentName = trenchList.get(which).getFullname();

                // Split by spaces
                String[] nameParts = dependentName.split("\\s+");

                String firstName = "";
                String middleName = "";
                String lastName = "";

                if (nameParts.length > 0) {
                    firstName = nameParts[0];              // Shashank
                }

                if (nameParts.length > 2) {
                    middleName = nameParts[1];             // Dattatray
                }

                if (nameParts.length > 1) {
                    lastName = nameParts[nameParts.length - 1]; // Kadam
                }


                // Set values
                txt_beneficiary_Fname.setText(firstName);
                txt_beneficiary_Mname.setText(middleName);
                txt_beneficiary_Lname.setText(lastName);


                new GetRelation().execute();


                switch (relationId) {
                    case "1":
                    case "5":
                    case "7":
                    case "9":
                    case "17":
                    case "22":
                        genderId = "M";
                        rb_male.setChecked(true);
                        rb_female.setChecked(false);
                        rb_female.setEnabled(false); //Enabled On 05/02/2021 suggested by sumit
                        rb_male.setEnabled(true);
                        edt_dob.setText("");
                        edt_age.setText("");

                        binding.rbOther.setChecked(false);
                        binding.rbOther.setEnabled(false);
                        break;

                    case "2":
                    case "6":
                    case "8":
                    case "10":
                    case "18":
                    case "21":
                        genderId = "F";
                        rb_male.setChecked(false);
                        rb_female.setChecked(true);
                        rb_male.setEnabled(false); //Enabled On 05/02/2021 suggested by sumit
                        rb_female.setEnabled(true);
                        edt_dob.setText("");
                        edt_age.setText("");

                        binding.rbOther.setChecked(false);
                        binding.rbOther.setEnabled(false);

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

                if (relationId.equalsIgnoreCase("1") || relationId.equalsIgnoreCase("2") ||
                        relationId.equalsIgnoreCase("21") || relationId.equalsIgnoreCase("22")) {

//                    txt_beneficiary_Mname.setText("");

                }

                if (Type.equalsIgnoreCase("0")) {
                    new GetRejectionDetails().execute(edt_workerregno.getText().toString().trim(), "1", relationId, txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim(), genderId, "2");
                }


                getDependentRegistrationStatus("MH" + edt_workerregno.getText().toString().trim(), trenchList.get(which).getFullname());


                if (trenchList.get(which).getgPLGDCODE() != null) {

                    gpCode = trenchList.get(which).getgPLGDCODE();
                }

                if (trenchList.get(which).getgPNAME() != null) {
                    binding.edtGp.setText(trenchList.get(which).getgPNAME());

                }

                if (trenchList.get(which).getIsUrban() != null) {
                    if (trenchList.get(which).getIsUrban().equals("0")) {
                        binding.radioRural.setChecked(true);
                        binding.edtGp.setVisibility(View.VISIBLE);
                    } else {
                        binding.radioUrban.setChecked(true);
                        binding.edtGp.setVisibility(View.GONE);
                        binding.edtGp.setText("");

                        gpCode = "0";
                    }
                }

//                String dob = trenchList.get(which).getdOB();

                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

                    Date date = inputFormat.parse(dob);

                    if (date != null) {
                        binding.edtDob.setText(outputFormat.format(date));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


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


//    private void showGpList(final List<GpModel.Output> trenchList) {
//
//        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setTitle("Select Gram-Panchayat");
//        builderSingle.setCancelable(false);
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
//        for (int i = 0; i < trenchList.size(); i++) {
//            arrayAdapter.add(trenchList.get(i).getGpname());
//        }
//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                binding.edtGp.setText(trenchList.get(which).getGpname());
//                gpCode = trenchList.get(which).getGplgdcode();
//
//
//            }
//        });
//
//        AlertDialog alertDialog = builderSingle.create();
//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        alertDialog.show();
//    }


    private void showGpList(final List<GpModel.Output> trenchList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Gram-Panchayat");

        View view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_search_list, null);

        builder.setView(view);

        EditText edtSearch = view.findViewById(R.id.edtSearch);
        ListView listView = view.findViewById(R.id.listView);

        ArrayList<GpModel.Output> filteredList = new ArrayList<>(trenchList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.list_row,
                new ArrayList<>()
        );

        for (GpModel.Output item : filteredList) {
            adapter.add(item.getGpname());
        }

        listView.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        // Search
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.clear();
                filteredList.clear();

                for (GpModel.Output item : trenchList) {

                    if (item.getGpname().toLowerCase()
                            .contains(s.toString().toLowerCase())) {

                        filteredList.add(item);
                        adapter.add(item.getGpname());
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Item Click
        listView.setOnItemClickListener((parent, view1, position, id) -> {

            binding.edtGp.setText(filteredList.get(position).getGpname());

            gpCode = filteredList.get(position).getGplgdcode();

            dialog.dismiss();
        });

        dialog.setCancelable(false);

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "Cancel",
                (dialogInterface, i) -> dialogInterface.dismiss());

        dialog.show();
    }


    private void getDependentRegistrationStatus(String workerNo, String name) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<BeneficiaryStatusModel> call = apiService.getDependentRegistrationStatus(workerNo, name);
        call.enqueue(new retrofit2.Callback<BeneficiaryStatusModel>() {
            @Override
            public void onResponse(Call<BeneficiaryStatusModel> call, retrofit2.Response<BeneficiaryStatusModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<BeneficiaryStatusModel.Output> campLocationDatalist = response.body().getOutput();


                        if (campLocationDatalist != null && !campLocationDatalist.isEmpty()) {
                            BeneficiaryStatusModel.Output output = campLocationDatalist.get(0);


//                            confirmationFlag = output.getIsConfirmed();

//                            Log.d(TAG, "Confirmation Flag: " + confirmationFlag);

                        }

                    } else {

                        Utilities.showAlertDialog(context, "Alert", message, false);

                        clearPatientDetails();
                        clearDependentData();
//                        btnCampClose.setEnabled(false);

                    }
                } else {
//                    Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<BeneficiaryStatusModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void verifyBeneficiaryDetails(String workerNo, String aadhar, String depName, String relationId, String pincode, String dob, String rationcardNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        Log.d(TAG, "Verify Details : " + workerNo + " " + aadhar + " " + depName + " " + relationId + " " + pincode + " " + dob + " " + rationcardNumber);
        progressDialog.setMessage("Verifying Details . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<BeneficiaryStatusModel> call = apiService.verifyBeneficiaryDetailsNew(workerNo, aadhar, depName, relationId, pincode, dob, rationcardNumber);
        call.enqueue(new retrofit2.Callback<BeneficiaryStatusModel>() {
            @Override
            public void onResponse(Call<BeneficiaryStatusModel> call, retrofit2.Response<BeneficiaryStatusModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    String messageId = response.body().getMessageId();
                    if (status.equalsIgnoreCase("Success")) {
                        List<BeneficiaryStatusModel.Output> campLocationDatalist = response.body().getOutput();


                        if (Utilities.isNetworkAvailable(context)) {
                            regdID = edt_workerregno.getText().toString().trim() + "" + count;


                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Alert!");
                            builder.setMessage("Please confirm the beneficiary's details before submitting");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Proceed", (dialog, which) -> {

                                if (isfaceDetection.equals("0")) {

                                    if (Utilities.isNetworkAvailable(context))
                                        new UploadPatientDetails().execute(
                                                siteId,
                                                campId,
                                                regdID,
                                                title,
                                                txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim(),
                                                edt_moblieno.getText().toString().trim(),
                                                originalAadhaar,
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
                                                "1",
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
                                                rb_self.isChecked() ? "1" : rb_spouse.isChecked() ? "2" : "3",
                                                isManual,
                                                versionNumber,
                                                isRecollectionFlag,
                                                rejRegdID,
                                                registerdCampId,
                                                maritalStatus,
                                                isfaceDetection,
                                                talukaId,
                                                distLgdCode,
                                                IsRegdByCall,
                                                binding.edtABHANumber1.getText().toString().trim(),
                                                binding.edtABHAAddress1.getText().toString().trim().isEmpty() ? "" : binding.edtABHAAddress1.getText().toString().replace(" ", "") + BuildConfig.CMID,
                                                isWhatsAppEnabled,
                                                rg_dep_no.isChecked() ? genderIdNew : relationGenderId,
                                                labourage,
                                                isfaceDetection,
                                                dependentBocId,
                                                String.valueOf(latitude),
                                                String.valueOf(longitude),
                                                gpCode,
                                                binding.edtRationCard.getText().toString().trim(),
                                                isCellularPhone,
                                                consentPath


                                        );
                                    else
                                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                } else if (isfaceDetection.equals("1")) {


                                    if (Utilities.isNetworkAvailable(context))
                                        new UploadPatientDetailsNew().execute(
                                                siteId,
                                                campId,
                                                regdID,
                                                title,
                                                txt_beneficiary_Fname.getText().toString().trim() + " " + txt_beneficiary_Mname.getText().toString().trim() + " " + txt_beneficiary_Lname.getText().toString().trim(),
                                                edt_moblieno.getText().toString().trim(),
                                                originalAadhaar,
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
                                                "1",
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
                                                rb_self.isChecked() ? "1" : rb_spouse.isChecked() ? "2" : "3",
                                                isManual,
                                                versionNumber,
                                                isRecollectionFlag,
                                                rejRegdID,
                                                registerdCampId,
                                                maritalStatus,
                                                isfaceDetection,
                                                talukaId,
                                                distLgdCode,
                                                IsRegdByCall,
                                                binding.edtABHANumber1.getText().toString().trim(),
                                                binding.edtABHAAddress1.getText().toString().trim().isEmpty() ? "" : binding.edtABHAAddress1.getText().toString().replace(" ", "") + BuildConfig.CMID,
                                                isWhatsAppEnabled,
                                                rg_dep_no.isChecked() ? genderIdNew : relationGenderId,
                                                labourage,
                                                isfaceDetection,
                                                dependentBocId,
                                                String.valueOf(latitude),
                                                String.valueOf(longitude),
                                                gpCode,
                                                binding.edtRationCard.getText().toString().trim(),
                                                isCellularPhone,
                                                consentPath

//                            rg_dep_no.isChecked() ? genderId : relationGenderId ,


                                        );
                                    else
                                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);


                                }


                            });
                            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                            builder.create().show();

                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }


                    } else {


                        if (isDependent.equals("1")) {
                            if (messageId.equals("2")) {
                                Utilities.showAlertDialog(context, "Alert", message, false);
                                edt_aadhaarno.setError("Please re enter aadhaar number");

                            } else {
                                Utilities.showAlertDialog(context, "Alert", message, false);

                                clearPatientDetails();
                                clearDependentData();
                            }
                        } else {
                            Utilities.showAlertDialog(context, "Alert", message, false);
                            clearPatientDetails();
                            clearDependentData();
                        }


                    }
                } else {
                    Utilities.showAlertDialog(context, "Alert", "Server Not Responding", false);
                }
            }

            @Override
            public void onFailure(Call<BeneficiaryStatusModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void getConsent(String workerNo, String aadhar, String depName, String relationId, String pincode, String dob, String rationcardNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        Log.d(TAG, "Consent Details : " + workerNo + " " + aadhar + " " + depName + " " + relationId + " " + pincode + " " + dob + " " + rationcardNumber);
        progressDialog.setMessage("Checking consent . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<BeneficiaryConsentModel> call = apiService.getConsent(workerNo, depName, relationId);
        call.enqueue(new retrofit2.Callback<BeneficiaryConsentModel>() {
            @Override
            public void onResponse(Call<BeneficiaryConsentModel> call, retrofit2.Response<BeneficiaryConsentModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    String messageId = response.body().getMessageId();
                    if (status.equalsIgnoreCase("Success")) {
                        List<BeneficiaryConsentModel.Output> campLocationDatalist = response.body().getOutput();


                        BeneficiaryConsentModel.Output o = campLocationDatalist.get(0);


                        String isConsentGiven = o.getIsConsent();

                        if (isConsentGiven == null) {
                            Utilities.showAlertDialog(context, "Alert",
                                    "Consent status is not available.", false);
                            return;
                        }

                        switch (isConsentGiven) {

                            case "0":
                                Utilities.showAlertDialog(context, "Alert",
                                        "या लाभार्थ्याकडून संमती (Consent) अदयाप प्राप्त झालेला नाही त्यामळे स्क्रीनिंग प्रक्रिया पुढे सुरू करण्यासाठी लाभार्थ्याला संमती सादर करण्यास सांगावे",
                                        false);
                                return;

                            case "2":
                                Utilities.showAlertDialog(context, "Alert",
                                        "या लाभार्थ्याकडून संमती (Consent) मागे घेण्यात आली आहे त्यामळे स्क्रीनिंग प्रक्रिया पुढे सुरू करण्यासाठी लाभार्थ्याला संमती सादर करण्यास सांगावे",
                                        false);
                                return;

                            case "1":
                                verifyBeneficiaryDetails(
                                        "MH" + edt_workerregno.getText().toString().trim(),
                                        originalAadhaar,
                                        edt_fname.getText().toString().trim(),
                                        relationId,
                                        edt_pincode.getText().toString().trim(),
                                        edt_dob.getText().toString().trim(),
                                        binding.edtRationCard.getText().toString().trim()
                                );
                                break;

                            default:
                                Utilities.showAlertDialog(context, "Alert",
                                        "Invalid consent status received.",
                                        false);
                                break;
                        }

                    } else {

                        Utilities.showAlertDialog(context, "Alert", message, false);


                    }
                } else {
                    Utilities.showAlertDialog(context, "Alert", "Server Not Responding", false);
                }
            }

            @Override
            public void onFailure(Call<BeneficiaryConsentModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


}
