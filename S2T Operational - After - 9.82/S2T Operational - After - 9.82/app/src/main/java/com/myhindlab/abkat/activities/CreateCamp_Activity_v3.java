package com.myhindlab.abkat.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

import android.Manifest;
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.attendance_details.CampClosingConfirmationActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.ConfirmatoryTestAssignTeamActivity;
import com.myhindlab.abkat.activities.payout.InvoiceGenModel;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceActivity;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceAdapter;
import com.myhindlab.abkat.activities.payout.adapter.LocationAdapter;
import com.myhindlab.abkat.activities.payout.adapter.PlaceAdapter;
import com.myhindlab.abkat.activities.payout.model.PaymentDetailsModel;
import com.myhindlab.abkat.appointment_confirmation.Expected_BeneficiariesActivity;
import com.myhindlab.abkat.models.CSCPreCampInfoForApprovalModel;
import com.myhindlab.abkat.models.CampNameModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.GetInitiatedByListForCampModel;
import com.myhindlab.abkat.models.HomeLabHublabOnLandingLabModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.MmuSchedulesDataModel;
import com.myhindlab.abkat.models.NewCampTypeModel;
import com.myhindlab.abkat.models.ScreeningTestModel;
import com.myhindlab.abkat.models.ServiceGroupModel;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.UniteIdModel;
import com.myhindlab.abkat.models.doortodoor.LocationModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;
import com.myhindlab.abkat.pojos.Lab_Pojo;
import com.myhindlab.abkat.pojos.SelectAddress_OutPut_pojo;
import com.myhindlab.abkat.pojos.SelectAddress_pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCamp_Activity_v3 extends AppCompatActivity implements PlaceAdapter.OnPlaceClickListener {

    private Context context;
    private UserSessionManager session;
    private static ProgressDialog pd;

    double campLat = 0.0, campLng = 0.0;


    private static final int PLACE_PICKER_REQUEST = 101;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    double campLatitude = 0.0;
    double campLongitude = 0.0;


    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private static final long DEBOUNCE_DELAY = 500; // 500 ms

    List<LocationModel> locationList = new ArrayList<>();
    //    PlaceAdapter adapter;
    PlaceAdapter placeAdapter;
    private MaterialEditText edt_district, edt_camp_address, edt_search_location, edt_camp_name, edt_expected_Beneficiary, edt_camp_date, edt_post_camp_date, edt_lab, edt_hospital, edt_aarogyamitra_name, edt_aarogyamitra_mobile, edt_screening_tests, edt_lab_1, edt_tests_for_lab_1, edt_lab_2,
            edt_tests_for_lab_2, edt_lab_3, edt_tests_for_lab_3, edt_camp_type, edt_initiated_by, edt_taluka, edt_Lab, edt_LandingLab, edt_unitName;
    private ImageView imv_camp_approval_letter;
    private Button btn_save;
    private Uri imageURI;
    private TextView tv_searchOnMap;


    double latitude = 0.0;
    double longitude = 0.0;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;


    private final int CAMERA_REQUEST = 100;
    private boolean isAntigenMapped, isRtpcrMapped = false;
    private List<ScreeningTestModel.OutputBean> screeningTestList;
    private List<ScreeningTestModel.OutputBean> selectedScreeningTestList;
    private List<ServiceGroupModel.OutputBean> serviceLabOneList, serviceLabTwoList, serviceLabThreeList;
    private ArrayList<Lab_OutPut_Pojo> labList, labsForRtpcrList;
    private CSCPreCampInfoForApprovalModel.Output intentRequest;
    private ArrayList<DistrictList_Model> districtList;


    private File patientPicsFolder;
    private String userId, uniteId = "0", currentAddress, mmuSceduleId = "0", UniteName, distLgdCode, DESGID, labCode, selectedHomeLabID, selectedHubLabID, labCodeOne, cscPreapprovedId, labCodeTwo, hospitalId, imagePath = "",
            labCodeThree, camptypeId, initiatedId, talukaId, STATELGDCODE = "2", landinglabId;
    private int mYear, mMonth, mDay;

    private RecyclerView rv_location;

    private CheckBox Checkbox;
    private TextView click_to_map, tv_current_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_camp__v3);

        init();
        getSessionDetails();
        setDefaults();
        if (session.isHllUser()) {
            getHLLSessionData();
        }
        setEventHandler();
        setUpToolBar();


        // 1. Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
        placesClient = Places.createClient(this);

        // 2. Start a Session
        sessionToken = AutocompleteSessionToken.newInstance();

    }

    private void init() {
        context = CreateCamp_Activity_v3.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);


        edt_district = findViewById(R.id.edt_district);
        rv_location = findViewById(R.id.rv_location);
        edt_camp_address = findViewById(R.id.edt_camp_address);
        edt_camp_name = findViewById(R.id.edt_camp_name);
        edt_camp_date = findViewById(R.id.edt_camp_date);
        edt_unitName = findViewById(R.id.edt_unitName);
        edt_post_camp_date = findViewById(R.id.edt_post_camp_date);
        edt_lab = findViewById(R.id.edt_lab);
        edt_hospital = findViewById(R.id.edt_hospital);
        edt_screening_tests = findViewById(R.id.edt_screening_tests);
        edt_lab_1 = findViewById(R.id.edt_lab_1);
        edt_tests_for_lab_1 = findViewById(R.id.edt_tests_for_lab_1);
        edt_lab_2 = findViewById(R.id.edt_lab_2);
        edt_tests_for_lab_2 = findViewById(R.id.edt_tests_for_lab_2);
        edt_aarogyamitra_name = findViewById(R.id.edt_aarogyamitra_name);
        edt_aarogyamitra_mobile = findViewById(R.id.edt_aarogyamitra_mobile);
        imv_camp_approval_letter = findViewById(R.id.imv_camp_approval_letter);
        btn_save = findViewById(R.id.btn_save);
        edt_lab_3 = findViewById(R.id.edt_lab_3);
        edt_tests_for_lab_3 = findViewById(R.id.edt_tests_for_lab_3);
        edt_camp_type = findViewById(R.id.edt_camp_type);
        edt_initiated_by = findViewById(R.id.edt_initiated_by);
        edt_taluka = findViewById(R.id.edt_taluka);
        edt_LandingLab = findViewById(R.id.edt_LandingLab);
        edt_expected_Beneficiary = findViewById(R.id.edt_expected_Beneficiary);
        edt_search_location = findViewById(R.id.edt_search_location);
        Checkbox = findViewById(R.id.Checkbox);
        click_to_map = findViewById(R.id.click_to_map);
        tv_current_location = findViewById(R.id.tv_current_location);
        tv_searchOnMap = findViewById(R.id.tv_searchOnMap);


        rv_location.setLayoutManager(new LinearLayoutManager(this));


//        adapter = new LocationAdapter(locationList, model -> {
////            edt_search_location.setText(model.displayName);
//            edt_camp_address.setText(model.displayName);
//
//            campLat = model.lat;
//            campLng = model.lon;
//
//            rv_location.setVisibility(View.GONE);
//
//            Log.d("OSM", "Lat: " + campLat + " Lng: " + campLng);
//        });
//
//        rv_location.setAdapter(adapter);


//        if (edt_camp_address.getText().toString().isEmpty()) {
//            Checkbox.setEnabled(false);
//
//        } else {
//            Checkbox.setEnabled(true);
//
//        }

        screeningTestList = new ArrayList<>();
        serviceLabOneList = new ArrayList<>();
        serviceLabTwoList = new ArrayList<>();
        serviceLabThreeList = new ArrayList<>();

        labList = new ArrayList<>();

        if (session.isHllUser()) {
            edt_initiated_by.setText("Internal");
            initiatedId = "1";
            edt_initiated_by.setEnabled(false);
//            edt_camp_address.setVisibility(View.GONE);
//            edt_camp_address.setText("NA");

            edt_district.setEnabled(true);

        } else {
            edt_camp_address.setVisibility(View.VISIBLE);
            edt_district.setEnabled(false);
        }

        edt_camp_address.setEnabled(false);


//        edt_lab_1.setEnabled(false)
//        edt_lab_2.setEnabled(false);
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
                edt_district.setText(json.getString("district"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getHLLSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                //  name = json.getString("name");
                distLgdCode = String.valueOf(json.getInt("DISTLGDCODE"));
                DESGID = String.valueOf(json.getInt("DESGID"));
                //Labcode = String.valueOf(json.getInt("Labcode"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefaults() {
//        Calendar calendar = Calendar.getInstance();
//        mYear = calendar.get(Calendar.YEAR);
//        mMonth = calendar.get(Calendar.MONTH);
//        mDay = calendar.get(Calendar.DAY_OF_MONTH);

//        getUniteId();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

//        patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Camp Approval Letter/");
//        if (!patientPicsFolder.exists())
//            patientPicsFolder.mkdirs();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            patientPicsFolder = getExternalCacheDir();
        } else {

            patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Camp Approval Letter/");
            if (!patientPicsFolder.exists()) patientPicsFolder.mkdirs();
        }

        if (getIntent() != null) {
            intentRequest = (CSCPreCampInfoForApprovalModel.Output) getIntent().getSerializableExtra("requestDetails");
            if (intentRequest != null) {
                edt_camp_name.setText(intentRequest.getCampName());
                cscPreapprovedId = String.valueOf(intentRequest.getCSCPreAprrovedid());
                edt_camp_address.setText(intentRequest.getAddress());
                edt_camp_type.setText(intentRequest.getCampTypeDescription());
                String date = Utilities.changeDateFormat("dd/MM/yyyy", "yyyy/MM/dd", intentRequest.getEstimatedCampDate());
                edt_camp_date.setText(date);

                // edt_camp_type.setText("CSC Camp");
                edt_initiated_by.setText("Internal");
                camptypeId = String.valueOf(intentRequest.getCampType());
                initiatedId = "1";
                edt_camp_address.setVisibility(View.VISIBLE);


                try {
                    Date selectedDate = new SimpleDateFormat("yyyy/MM/dd").parse(edt_camp_date.getText().toString().trim());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(selectedDate);
                    calendar.add(Calendar.DATE, 7);

                    int postCampYear = calendar.get(Calendar.YEAR);
                    int postCampMonth = calendar.get(Calendar.MONTH);
                    int postCampDay = calendar.get(Calendar.DAY_OF_MONTH);
                    edt_post_camp_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, postCampDay, postCampMonth + 1, postCampYear));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                edt_camp_name.setEnabled(false);
                edt_camp_address.setEnabled(false);
                edt_camp_date.setEnabled(false);
                edt_post_camp_date.setEnabled(false);
                edt_camp_type.setEnabled(false);


//                distLgdCode = String.valueOf(intentRequest.getDistlgdcode());


            }
        }

//        ******************************Location********************************

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//            return;
//        }
//
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(18000); // 8 minutes
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    // Update UI with location data
//                    latitude = location.getLatitude();
//                    longitude = location.getLongitude();
////                    Toast.makeText(context, "Lati"+latitude+" "+"Longi"+longitude, Toast.LENGTH_SHORT).show();
//                }
//
//                Geocoder geocoder = new Geocoder(context);
//                try {
//                    List<Address> geocode = geocoder.getFromLocation(latitude, longitude, 1);
//
//                    String address = geocode.get(0).getAddressLine(0);
//
//                    edt_camp_address.setText(address);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//


        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }


        mFusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                null
        ).addOnSuccessListener(location -> {

            if (location == null) {
                Toast.makeText(context, "Unable to fetch location", Toast.LENGTH_SHORT).show();
                return;
            }

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses =
                        geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && !addresses.isEmpty()) {
//                    edt_camp_address.setText(
//                            addresses.get(0).getAddressLine(0)
//
//
//                    );

                    currentAddress = addresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        //        ******************************Location********************************


    }

    private void setEventHandler() {


//        tv_searchOnMap.setOnClickListener(v -> openPlaceSearch());

        tv_searchOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checkbox.setChecked(false);
                edt_camp_address.setText("");


                Utilities.showAlertDialog(context, "Alert", "कृपया कॅम्पचा पत्ता फ्लेबोशी कन्फर्म करूनच टाका.", true, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openPlaceSearch();

                    }
                });


            }
        });

        tv_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checkbox.setChecked(false);
                edt_camp_address.setText("");


                if (currentAddress == null) {
                    Utilities.showAlertDialog(context, "Alert", "Current location not available", false);
                    return;
                }


                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateCamp_Activity_v3.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                    return;
                }

                mFusedLocationClient.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        null
                ).addOnSuccessListener(location -> {

                    if (location == null) {
                        Toast.makeText(context, "Unable to fetch current location", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    try {
                        List<Address> addresses =
                                geocoder.getFromLocation(latitude, longitude, 1);

                        if (addresses != null && !addresses.isEmpty()) {
//                    edt_camp_address.setText(
//                            addresses.get(0).getAddressLine(0)
//
//
//                    );

                            currentAddress = addresses.get(0).getAddressLine(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                edt_camp_address.setText(currentAddress);

                edt_camp_address.setEnabled(false);

                Checkbox.setEnabled(true);


            }
        });


        click_to_map.setOnClickListener(v -> {

            if (edt_camp_address.getText().toString().isEmpty()) {
                Utilities.showAlertDialog(context, "Alert", "Please search camp location or use current location", false);
                return;
            }

            openMapForConfirmation();

        });

        Checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilities.showAlertDialog(context, "Alert", "कृपया कॅम्पचा पत्ता फ्लेबोशी कन्फर्म करूनच टाका.", true, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        openPlaceSearch();

                    }
                });



                if (Checkbox.isChecked()) {

                    edt_camp_address.setEnabled(true);
                    edt_camp_address.setText("");


                    latitude = 0.00;
                    longitude = 0.00;

                    Log.d("OSM", "Lat: " + latitude + " Lng: " + longitude);


                } else if (!Checkbox.isChecked()) {

                    edt_camp_address.setEnabled(false);

                }

            }
        });


//        edt_search_location.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void afterTextChanged(Editable s) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 5) {
//                    searchLocation(s.toString());
//                }
//            }
//        });


//        edt_search_location.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void afterTextChanged(Editable s) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                // Cancel previous pending API call
//                if (searchRunnable != null) {
//                    searchHandler.removeCallbacks(searchRunnable);
//                }
//
//                // Minimum characters check
//                if (s.length() < 3) {
//                    rv_location.setVisibility(View.GONE);
//                    return;
//                }
//
//                // Create new runnable
//                searchRunnable = () -> searchLocation(s.toString());
//
//                // Execute after delay
//                searchHandler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
//            }
//        });
//
//        edt_search_location.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchHandler.removeCallbacks(searchRunnable);
//                searchRunnable = () -> {
//                    if (s.length() > 2) {
//                        searchPlaces(s.toString());
//                    } else {
//                        rv_location.setVisibility(View.GONE);
//                    }
//                };
//                searchHandler.postDelayed(searchRunnable, 500); // Wait 500ms after user stops typing
//            }
//            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
//            @Override public void afterTextChanged(Editable s) {}
//        });


        edt_camp_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.isNetworkAvailable(context)) if (session.isHllUser()) {

                    if (DESGID.equalsIgnoreCase("108")) {
                        new GetFlexiCampType().execute();
                    } else if (DESGID.equalsIgnoreCase("136") || DESGID.equalsIgnoreCase("139")) {
                        new GetCampTypeMMU().execute();
                    } else {
                        new GetCampType().execute();
                    }
                } else {
                    new GetCampTypeForRegular().execute();
                }
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            }
        });
        edt_initiated_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) new GetInitiatedByList().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            }
        });

        edt_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetDistrictList().execute(STATELGDCODE, userId);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });
        edt_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context))
                    new GetTaluka().execute(STATELGDCODE, distLgdCode);
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            }
        });
        edt_LandingLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) new GetLandingLab().execute(distLgdCode);
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            }
        });

        edt_tests_for_lab_3.setOnClickListener(v -> {
            if (serviceLabThreeList.size() == 0) {
                if (Utilities.isNetworkAvailable(context))
                    new GetServiceGroupListForRtpcr().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            } else {
                showServiceGroupDialogThree();
            }
        });

        edt_lab_3.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) new GetLabForRtpcr().execute();
            else {
                Utilities.showToastMessage("Please Check your internet connection", context, false);
            }
        });

        edt_camp_date.setOnClickListener(v -> {

            if (edt_camp_type.getText().toString().isEmpty()) {
                Utilities.showToastMessage("Please Select Camp Type", context, false);
                return;

            }

            DatePickerDialog dpd1 = new DatePickerDialog(context, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                edt_camp_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                try {
                    Date selectedDate = new SimpleDateFormat("yyyy/MM/dd").parse(edt_camp_date.getText().toString().trim());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(selectedDate);
                    calendar.add(Calendar.DATE, 7);

                    int postCampYear = calendar.get(Calendar.YEAR);
                    int postCampMonth = calendar.get(Calendar.MONTH);
                    int postCampDay = calendar.get(Calendar.DAY_OF_MONTH);

//                    if (camptypeId.equalsIgnoreCase("6")){
//                        getMMUDAtaCall();
//                    }

                    edt_post_camp_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, postCampDay, postCampMonth + 1, postCampYear));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }, mYear, mMonth, mDay);
            try {
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.MONTH, Calendar.JANUARY);
//                calendar.set(Calendar.DAY_OF_MONTH, 1);

                dpd1.getDatePicker().setCalendarViewShown(false);
                dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
//                dpd1.getDatePicker().setMinDate(calendar.getTimeInMillis());


            } catch (Exception e) {
                e.printStackTrace();
            }
            dpd1.show();
        });

        edt_post_camp_date.setOnClickListener(v -> {

        });

//        edt_lab.setOnClickListener(v -> {
//            if (labList.size() == 0) {
//                if (Utilities.isNetworkAvailable(context))
//                    new GetLab().execute();
//                else {
//                    Utilities.showToastMessage("Please Check your internet connection", context, false);
//                }
//            } else {
//                showLabListDialog();
//            }
//        });

//        edt_lab_1.setOnClickListener(v -> {
//            if (labList.size() == 0) {
//                if (Utilities.isNetworkAvailable(context)) new GetLab().execute();
//                else {
//                    Utilities.showToastMessage("Please Check your internet connection", context, false);
//                }
//            } else {
//                showLabListDialogOne();
//            }
//        });

//        edt_lab_2.setOnClickListener(v -> {
//            if (edt_lab_1.getText().toString().trim().equals("")) {
//                edt_lab_1.setError("Select Home Lab");
//                return;
//            }
//
//            if (edt_tests_for_lab_1.getText().toString().trim().equals("")) {
//                edt_tests_for_lab_1.setError("Select Tests for Mapping - Home Lab");
//                return;
//            }
//
//            int isSelected = 0;
//
//            for (int i = 0; i < serviceLabOneList.size(); i++)
//                if (serviceLabOneList.get(i).isCheckedInListOne()) isSelected = isSelected + 1;
//
//            if (serviceLabOneList.size() == isSelected) {
//                Utilities.showToastMessage("All test groups are mapped to Home Lab", context, true);
//                return;
//            }
//
//            ArrayList<Lab_OutPut_Pojo> labListTwo = new ArrayList<>();
//            for (int i = 0; i < labList.size(); i++) {
//                if (!labList.get(i).getLabCode().equals(labCodeOne) && labList.get(i).getIsHubLab().equals("1"))
//                    labListTwo.add(labList.get(i));
//            }
//
//            showLabListDialogTwo(labListTwo);
//        });

        edt_hospital.setOnClickListener(v -> {
            if (edt_district.getText().toString().trim().isEmpty()) {
                edt_district.setError("Select district");
                return;
            }

            if (Utilities.isNetworkAvailable(context)) new SelectHospital().execute();
            else Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        });

        edt_tests_for_lab_1.setOnClickListener(v -> {
            if (serviceLabOneList.size() == 0) {
                if (Utilities.isNetworkAvailable(context)) new GetServiceGroupList().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            } else {
                showServiceGroupDialogOne();
            }
        });

        edt_tests_for_lab_2.setOnClickListener(v -> {

            if (edt_tests_for_lab_1.getText().toString().trim().equals("")) {
                edt_tests_for_lab_1.setError("Select Tests for Mapping - Home Lab");
                return;
            }

            serviceLabTwoList = new ArrayList<>();

            List<ServiceGroupModel.OutputBean> serviceLabOneListDummy = new ArrayList<>(serviceLabOneList);

            for (int i = 0; i < serviceLabOneListDummy.size(); i++)
                if (!serviceLabOneListDummy.get(i).isCheckedInListOne())
                    serviceLabTwoList.add(serviceLabOneListDummy.get(i));

            if (serviceLabTwoList.size() != 0) showServiceGroupDialogTwo();
            else
                Utilities.showToastMessage("All test groups are mapped to Home Lab", context, true);
        });

        edt_screening_tests.setOnClickListener(v -> {
            if (screeningTestList.size() == 0) {
                if (Utilities.isNetworkAvailable(context)) new GetScreeningTest().execute();
                else {
                    Utilities.showToastMessage("Please Check your internet connection", context, false);
                }
            } else {
                showScreeningTestDialog();
            }
        });

        imv_camp_approval_letter.setOnClickListener(v -> {
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

//            int randomEndtNo = (int) (Math.random() * 999999 + 1);
//            File patientImageFile = new File(patientPicsFolder, randomEndtNo + ".png");
//            imageURI = Uri.fromFile(patientImageFile);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
//            startActivityForResult(intent, CAMERA_REQUEST);
            int randomEndtNo = (int) (Math.random() * 999999 + 1);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                imageURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                File patientImageFile = new File(patientPicsFolder, randomEndtNo + ".png");
                imageURI = Uri.fromFile(patientImageFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(intent, CAMERA_REQUEST);
            }


        });

        btn_save.setOnClickListener(v -> {
            submitData();
        });
    }


    RectangularBounds bounds = RectangularBounds.newInstance(
            new LatLng(15.6020, 72.6640),  // SW Maharashtra
            new LatLng(22.0280, 80.8900)   // NE Maharashtra
    );


    private void openPlaceSearch() {

        List<Place.Field> fields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
        );

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                fields
        )
                .setCountry("IN")   // 👈 Country code (ISO-2)
                .setLocationBias(bounds)   // 👈 State-like restriction

                .build(this);

        placeLauncher.launch(intent);
    }


    private void openMapForConfirmation() {

        if (latitude == 0 || longitude == 0) {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String uri = "geo:" + latitude + "," + longitude +
                "?q=" + latitude + "," + longitude + "(Selected Location)";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show();
        }
    }


    //    private void openPlaceSearch() {
//
//        List<Place.Field> fields = Arrays.asList(
//                Place.Field.ID,
//                Place.Field.NAME,
//                Place.Field.ADDRESS,
//                Place.Field.LAT_LNG
//        );
//
//        Intent intent = new Autocomplete.IntentBuilder(
//                AutocompleteActivityMode.OVERLAY, fields
//        ).build(this);
//
//        startActivityForResult(intent, PLACE_PICKER_REQUEST);
//    }
    private void searchPlaces(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(sessionToken)
                .setCountries("IN")
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
            if (!predictions.isEmpty()) {
                placeAdapter = new PlaceAdapter(predictions, this);
                rv_location.setAdapter(placeAdapter);
                rv_location.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(e -> Log.e("PLACES", "Error: " + e.getMessage()));
    }

    @Override
    public void onPlaceClick(String placeId) {
        rv_location.setVisibility(View.GONE);

        // 1. Add Place.Field.LAT_LNG to the list
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG  // <--- Added this field
        );

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                .setSessionToken(sessionToken)
                .build();

        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place = response.getPlace();

            // 2. Extract Latitude and Longitude
            if (place.getLatLng() != null) {
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

                // Log or Store them in variables
                Log.i("LOCATION_DATA", "Lat: " + latitude + ", Lng: " + longitude);

                // Example: Store in your database or local variables
                // this.savedLat = latitude;
                // this.savedLng = longitude;
            }

            // Set the final address to your EditText
            edt_camp_address.setText(place.getAddress());

            edt_search_location.clearFocus();
            edt_search_location.setText("");


            // Refresh token for next search
            sessionToken = AutocompleteSessionToken.newInstance();

        }).addOnFailureListener(e -> {
            Log.e("PLACES_ERROR", "Fetch Place failed: " + e.getMessage());
        });
    }


    private class GetServiceGroupListForRtpcr extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetTestOnlyCovid, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    ServiceGroupModel pojoDetails = new Gson().fromJson(result, ServiceGroupModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        serviceLabThreeList = pojoDetails.getOutput();
                        if (serviceLabThreeList.size() > 0) {
                            showServiceGroupDialogThree();
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


//    @Override
//    public void onPlaceClick(String placeId) {
//        Toast.makeText(this, "Place ID: " + placeId, Toast.LENGTH_SHORT).show();
//
//        // You can now call Places SDK to fetch place details using placeId
//        // fetchPlaceDetails(placeId);
//
//
//    }


    private void showServiceGroupDialogThree() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Tests for Mapping - RTPCR Lab");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));
        rv_checklist.setAdapter(new ServiceGroupAdapterThree());

        builder.setPositiveButton("Select", (dialog, which) -> {
            StringBuilder selectedSubCategories = new StringBuilder();

            for (ServiceGroupModel.OutputBean sample : serviceLabThreeList) {
                if (sample.isCheckedInListThree()) {
                    selectedSubCategories.append(sample.getGroupName()).append(", ");
                }
            }

            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_tests_for_lab_3.setText(selectedLabsStr);
            }

        });

        builder.create().show();
    }

    private class ServiceGroupAdapterThree extends RecyclerView.Adapter<ServiceGroupAdapterThree.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(serviceLabThreeList.get(position).getGroupName());

            if (serviceLabThreeList.get(position).isCheckedInListThree()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                serviceLabThreeList.get(position).setCheckedInListThree(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return serviceLabThreeList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


    private class GetLabForRtpcr extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetRtpcrLabName, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    Lab_Pojo pojoDetails = new Gson().fromJson(result, Lab_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        labsForRtpcrList = pojoDetails.getOutput();
                        if (labsForRtpcrList.size() > 0) {
                            showLabListDialogRtpcr();
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

    private void showLabListDialogRtpcr() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select RTPCR Lab");

        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : labsForRtpcrList) {
            arrayAdapter.add(subTrenchModel.getLabName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            labCodeThree = labsForRtpcrList.get(which).getLabCode();
            edt_lab_3.setText(labsForRtpcrList.get(which).getLabName());
        });
        builderSingle.show();
    }

    private void submitData() {

        if (edt_camp_type.getText().toString().trim().isEmpty()) {
            edt_camp_type.setError("Select Camp Type");
            return;
        }

        if (edt_initiated_by.getText().toString().trim().isEmpty()) {
            edt_initiated_by.setError("Select Initiated By");
            return;
        }


        if (edt_district.getText().toString().trim().isEmpty()) {
            edt_district.setError("Select district");
            return;
        }

        if (edt_taluka.getText().toString().trim().isEmpty()) {
            edt_taluka.setError("Select taluka");
            return;
        }

        if (edt_LandingLab.getText().toString().trim().isEmpty()) {
            edt_LandingLab.setError("Select Landing lab");
            return;
        }


        if (edt_camp_name.getText().toString().trim().isEmpty()) {
            edt_camp_name.setError("Enter camp name");
            return;
        }


        if (edt_camp_address.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please enter a valid camp address", context, false);
            return;
        }

        if (edt_camp_address.getText().toString().length() <= 15) {
            Utilities.showToastMessage("Camp address should be grater than or equal to 15 character length ", context, false);
            return;
        }


        if (edt_camp_date.getText().toString().trim().isEmpty()) {
            edt_camp_date.setError("Select camp date");
            return;
        }

        if (edt_expected_Beneficiary.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Enter Expected Beneficiary", context, false);
            return;
        }


//        if (edt_lab.getText().toString().trim().isEmpty()) {
//            edt_lab.setError("Select lab");
//            return;
//        }

//        if (edt_hospital.getText().toString().trim().isEmpty()) {
//            edt_hospital.setError("Select hospital");
//            return;
//        }

        if (edt_screening_tests.getText().toString().trim().isEmpty()) {
            edt_screening_tests.setError("Select screening tests");
            return;
        }

//        if (edt_lab_1.getText().toString().trim().equals("")) {
//            edt_lab_1.setError("Select Home Lab");
//            return;
//        }

//        if (edt_tests_for_lab_1.getText().toString().trim().equals("")) {
//            edt_tests_for_lab_1.setError("Select Tests for Mapping - Home Lab");
//            return;
//        }

//        if (!edt_tests_for_lab_2.getText().toString().trim().equals(""))
//            if (edt_lab_2.getText().toString().trim().equals("")) {
//                edt_lab_2.setError("Select Hub Lab");
//                return;
//            }
//
//        if (edt_tests_for_lab_2.getText().toString().trim().equals("")) {
//            edt_tests_for_lab_2.setError("Select Tests for Mapping - Lab 2");
//            return;
//        }
        int servicesSelectedCount = 0;

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabOneList) {
            if (serviceDetails.isCheckedInListOne()) {
                servicesSelectedCount = servicesSelectedCount + 1;
            }
        }

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabTwoList) {
            if (serviceDetails.isCheckedInListTwo()) {
                servicesSelectedCount = servicesSelectedCount + 1;
            }
        }

        if (servicesSelectedCount != serviceLabOneList.size()) {
            Utilities.showAlertDialog(context, "Alert", serviceLabOneList.size() - servicesSelectedCount + " test group mapping is pending. Please map all test groups to lab", false);
            return;
        }

        int servicesRtpcrSelectedCount = 0;

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabThreeList) {
            if (serviceDetails.isCheckedInListThree()) {
                servicesRtpcrSelectedCount = servicesRtpcrSelectedCount + 1;
            }
        }

//        if (servicesRtpcrSelectedCount != serviceLabThreeList.size()) {
//            Utilities.showAlertDialog(context, "Alert", "Please map all RTPCR tests to lab", false);
//            return;
//        }

//        if (isAntigenMapped && isRtpcrMapped) {
//            if (servicesRtpcrSelectedCount == 0) {
//                Utilities.showAlertDialog(context, "Alert", "Please select RTPCR Lab", false);
//                return;
//            }
//        }


//        if (edt_aarogyamitra_name.getText().toString().trim().isEmpty()) {
//            edt_aarogyamitra_name.setError("Enter aarogyamitra name");
//            return;
//        }

//        if (edt_aarogyamitra_mobile.getText().toString().trim().isEmpty()) {
//            edt_aarogyamitra_mobile.setError("Enter aarogyamitra mobile");
//            return;
//        }

//        if (imagePath.equals("")) {
//            Utilities.showToastMessage("Please click camp approval letter", context, false);
//            return;
//        }


        JsonArray screeningTestJsonArray = new JsonArray();
        JsonArray serviceTestLabJsonArray = new JsonArray();

        for (ScreeningTestModel.OutputBean screeningDetails : screeningTestList) {
            JsonObject screeningTestJsonObj = new JsonObject();
            screeningTestJsonObj.addProperty("CampID", "0");
            screeningTestJsonObj.addProperty("TestID", screeningDetails.getTestId());
            if (screeningDetails.isChecked()) {
                screeningTestJsonObj.addProperty("IsTestProcess", "1");
            } else {
                screeningTestJsonObj.addProperty("IsTestProcess", "0");
            }
            screeningTestJsonObj.addProperty("IsActive", "1");
            screeningTestJsonObj.addProperty("CreatedBy", userId);
            screeningTestJsonArray.add(screeningTestJsonObj);


        }

//        Log.d("screeningTestJsonArray", screeningTestJsonArray.toString());

        Log.d("screeningTestJsonArray", screeningTestJsonArray.toString());


        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabOneList) {
            if (serviceDetails.isCheckedInListOne()) {
                JsonObject serviceTestJsonObj = new JsonObject();
                serviceTestJsonObj.addProperty("Labcode", labCodeOne);
                serviceTestJsonObj.addProperty("Groupid", serviceDetails.getGroupID());
                serviceTestJsonObj.addProperty("IsActive", "1");
                serviceTestLabJsonArray.add(serviceTestJsonObj);
            }
        }

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabTwoList) {
            if (serviceDetails.isCheckedInListTwo()) {
                JsonObject serviceTestJsonObj = new JsonObject();
                serviceTestJsonObj.addProperty("Labcode", labCodeTwo);
                serviceTestJsonObj.addProperty("Groupid", serviceDetails.getGroupID());
                serviceTestJsonObj.addProperty("IsActive", "1");
                serviceTestLabJsonArray.add(serviceTestJsonObj);
            }
        }

        for (ServiceGroupModel.OutputBean serviceDetails : serviceLabThreeList) {
            if (serviceDetails.isCheckedInListThree()) {
                JsonObject serviceTestJsonObj = new JsonObject();
                serviceTestJsonObj.addProperty("Labcode", labCodeThree);
                serviceTestJsonObj.addProperty("Groupid", serviceDetails.getGroupID());
                serviceTestJsonObj.addProperty("IsActive", "1");
                serviceTestLabJsonArray.add(serviceTestJsonObj);
            }
        }


        if (Utilities.isNetworkAvailable(context)) {

            if (getIntent() != null) {
                CSCPreCampInfoForApprovalModel.Output output = (CSCPreCampInfoForApprovalModel.Output) getIntent().getSerializableExtra("requestDetails");
                if (output != null) {
                    new CreateCampCSC(
                            edt_camp_name.getText().toString().trim(),
                            edt_camp_date.getText().toString().trim(),
                            edt_post_camp_date.getText().toString().trim(),
                            landinglabId,
                            "0",
                            distLgdCode,
                            userId,
                            edt_camp_address.getText().toString().trim(),
                            screeningTestJsonArray.toString(),
                            camptypeId,
                            initiatedId,
                            landinglabId,
                            cscPreapprovedId
                    ).execute();

                } else {

//                    Utilities.showAlertDialog(context, "Fail", "Creating Camp Please Wait", false);

                    new CreateCamp(
                            edt_expected_Beneficiary.getText().toString().trim(),
                            edt_camp_name.getText().toString().trim(),
                            edt_camp_date.getText().toString().trim(),
                            edt_post_camp_date.getText().toString().trim(),
                            landinglabId,
                            "0",
                            distLgdCode,
                            userId,
                            edt_camp_address.getText().toString().trim(),
                            screeningTestJsonArray.toString(),
                            camptypeId,
                            initiatedId,
                            landinglabId,
                            "0",
                            talukaId,
                            String.valueOf(latitude),
                            String.valueOf(longitude)

                    ).execute();
                }
            } else {
                new CreateCamp(
                        edt_expected_Beneficiary.getText().toString().trim(),
                        edt_camp_name.getText().toString().trim(),
                        edt_camp_date.getText().toString().trim(),
                        edt_post_camp_date.getText().toString().trim(),
                        landinglabId,
                        "0",
                        distLgdCode,
                        userId,
                        edt_camp_address.getText().toString().trim(),
                        screeningTestJsonArray.toString(),
                        camptypeId,
                        initiatedId,
                        landinglabId,
                        "0",
                        talukaId,
                        String.valueOf(latitude),
                        String.valueOf(longitude)

                ).execute(


                        //serviceTestLabJsonArray.toString(),
                        //  edt_aarogyamitra_mobile.getText().toString().trim(),

                        //    imagePath
                        //    talukaId,
                        //    edt_aarogyamitra_name.getText().toString().trim(),
                        //   edt_camp_address.getText().toString().trim(),


                );

            }
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

    }

    /// ///////////////////////////////// Lab, Lab 1 and Lab 2 ////////////////////////////////////

    private class GetLab extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetLab, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    Lab_Pojo pojoDetails = new Gson().fromJson(result, Lab_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        labList = pojoDetails.getOutput();
                        if (labList.size() > 0) {
                            showLabListDialogOne();
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

    private void showLabListDialog() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Lab");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : labList) {
            arrayAdapter.add(subTrenchModel.getLabName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            labCode = labList.get(which).getLabCode();
            edt_lab.setText(labList.get(which).getLabName());
        });
        builderSingle.show();
    }

    private void showLabListDialogOne() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Home Lab");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : labList) {
            arrayAdapter.add(subTrenchModel.getLabName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            labCodeOne = labList.get(which).getLabCode();
            edt_lab_1.setText(labList.get(which).getLabName());
            edt_lab_2.setText("");
        });
        builderSingle.show();
    }

    private void showLabListDialogTwo(ArrayList<Lab_OutPut_Pojo> labList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Hub Lab");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (Lab_OutPut_Pojo subTrenchModel : labList) {
            arrayAdapter.add(subTrenchModel.getLabName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            labCodeTwo = labList.get(which).getLabCode();
            edt_lab_2.setText(labList.get(which).getLabName());
        });
        builderSingle.show();
    }

    /// ///////////////////////////////// Hospital ////////////////////////////////////////////////

    private class SelectHospital extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", distLgdCode));
            res = WebServiceCall.APICall(ApplicationConstants.GetNearistHospitalList1, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    SelectAddress_pojo pojoDetails = new Gson().fromJson(result, SelectAddress_pojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        ArrayList<SelectAddress_OutPut_pojo> hospitalList = pojoDetails.getOutput();
                        if (hospitalList.size() > 0) {
                            Collections.sort(hospitalList, (o1, o2) -> o1.getHospitalName().compareTo(o2.getHospitalName()));
                            showHospitalListDialog(hospitalList);
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

    private void showHospitalListDialog(ArrayList<SelectAddress_OutPut_pojo> hospitalList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Hospital");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (SelectAddress_OutPut_pojo subTrenchModel : hospitalList) {
            arrayAdapter.add(subTrenchModel.getHospitalName());
        }
        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            hospitalId = hospitalList.get(which).getHospitalId();
            edt_hospital.setText(hospitalList.get(which).getHospitalName());
        });
        builderSingle.show();
    }

    /// ///////////////////////////////// Screening Test //////////////////////////////////////////

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
            res = WebServiceCall.APICall(ApplicationConstants.getTestList, ApplicationConstants.webservice, param);
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
                            showScreeningTestDialog();
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

    private void showScreeningTestDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Screening Test");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));

        isAntigenMapped = false;
        isRtpcrMapped = false;

        for (int i = 0; i < screeningTestList.size(); i++)
            if (screeningTestList.get(i).getIsCompulsary().equals("1"))
                screeningTestList.get(i).setChecked(true);

        rv_checklist.setAdapter(new ScreeningTestsAdapter());

        builder.setPositiveButton("Okay", (dialog, which) -> {
            edt_screening_tests.setText("");

            StringBuilder selectedSubCategories = new StringBuilder();
            selectedScreeningTestList = new ArrayList<>();
            for (ScreeningTestModel.OutputBean sample : screeningTestList) {
                if (sample.isChecked()) {
                    selectedScreeningTestList.add(sample);
                    selectedSubCategories.append(sample.getTestName()).append(", ");
                    if (sample.getTestId().equals("14")) isRtpcrMapped = true;
                    if (sample.getTestId().equals("15")) isAntigenMapped = true;
                }

            }

            if (isRtpcrMapped && isAntigenMapped) {
                edt_lab_3.setVisibility(View.VISIBLE);
                edt_tests_for_lab_3.setVisibility(View.VISIBLE);
            } else {
                edt_lab_3.setVisibility(View.GONE);
                edt_tests_for_lab_3.setVisibility(View.GONE);
            }

            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_screening_tests.setText(selectedLabsStr);
            }

        });

        builder.create().show();
    }

    private class ScreeningTestsAdapter extends RecyclerView.Adapter<ScreeningTestsAdapter.MyViewHolder> {

        @NonNull
        @Override
        public ScreeningTestsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new ScreeningTestsAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScreeningTestsAdapter.MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(screeningTestList.get(position).getTestName());

            if (screeningTestList.get(position).isChecked()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* if (screeningTestList.get(position).getTestName().equalsIgnoreCase("Lung Functioin Test")) {
                        screeningTestList.get(position).setChecked(false);
                        holder.cb_select.setChecked(false);
                        Utilities.showToastMessage("This test is not functional.", context, false);
                    } else*/
                    if (screeningTestList.get(position).getTestName().equalsIgnoreCase("Antigen")) {
                        screeningTestList.get(position).setChecked(false);
                        holder.cb_select.setChecked(false);
                        Utilities.showToastMessage("This test is not functional.", context, false);
                    } else if (screeningTestList.get(position).getTestName().equalsIgnoreCase("RTPCR")) {
                        screeningTestList.get(position).setChecked(false);
                        holder.cb_select.setChecked(false);
                        Utilities.showToastMessage("This test is not functional.", context, false);
                    } else if (screeningTestList.get(position).getTestName().equalsIgnoreCase("Breast Screening")) {
                        screeningTestList.get(position).setChecked(false);
                        holder.cb_select.setChecked(false);
                        Utilities.showToastMessage("This test is not functional.", context, false);
                    }
                }
            });

            if (screeningTestList.get(position).getIsCompulsary().equals("0"))
                holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    if (!screeningTestList.get(position).getTestName().equalsIgnoreCase("Lung Functioin Test") &&
//                            !screeningTestList.get(position).getTestName().equalsIgnoreCase("Antigen") &&
//                            !screeningTestList.get(position).getTestName().equalsIgnoreCase("RTPCR"))
//                    if (!screeningTestList.get(position).getTestName().equalsIgnoreCase("Lung Functioin Test"))
                    screeningTestList.get(position).setChecked(isChecked);
                });
            else holder.cb_select.setClickable(false);
        }

        @Override
        public int getItemCount() {
            return screeningTestList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    /// ///////////////////////////////// Service Group Mapping //////////////////////////////////////////

    private class GetServiceGroupList extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.getServiceGroupList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    ServiceGroupModel pojoDetails = new Gson().fromJson(result, ServiceGroupModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        serviceLabOneList = pojoDetails.getOutput();
                        if (serviceLabOneList.size() > 0) {
                            showServiceGroupDialogOne();
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

    private void showServiceGroupDialogOne() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Tests for Mapping - Home Lab");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));
        rv_checklist.setAdapter(new ServiceGroupAdapterOne());

        builder.setPositiveButton("Select", (dialog, which) -> {
            edt_tests_for_lab_1.setText("");
            edt_tests_for_lab_2.setText("");

            int checkedTestCount = 0;
            StringBuilder selectedSubCategories = new StringBuilder();

            for (ServiceGroupModel.OutputBean sample : serviceLabOneList) {
                if (sample.isCheckedInListOne()) {
                    selectedSubCategories.append(sample.getGroupName()).append(", ");
                    checkedTestCount = checkedTestCount + 1;
                }
            }

            if (serviceLabOneList.size() == checkedTestCount) edt_lab_2.setText("");

            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_tests_for_lab_1.setText(selectedLabsStr);
            }

            for (int i = 0; i < serviceLabTwoList.size(); i++) {
                serviceLabTwoList.get(i).setCheckedInListTwo(false);
            }

        });

        builder.create().show();
    }

    private class ServiceGroupAdapterOne extends RecyclerView.Adapter<ServiceGroupAdapterOne.MyViewHolder> {

        @NonNull
        @Override
        public ServiceGroupAdapterOne.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new ServiceGroupAdapterOne.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ServiceGroupAdapterOne.MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(serviceLabOneList.get(position).getGroupName());

            if (serviceLabOneList.get(position).isCheckedInListOne()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                serviceLabOneList.get(position).setCheckedInListOne(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return serviceLabOneList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private void showServiceGroupDialogTwo() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Tests for Mapping - Hub Lab");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(context));
        rv_checklist.setAdapter(new ServiceGroupAdapterTwo());

        builder.setPositiveButton("Select", (dialog, which) -> {
            edt_tests_for_lab_2.setText("");

            StringBuilder selectedSubCategories = new StringBuilder();

            for (ServiceGroupModel.OutputBean sample : serviceLabTwoList) {
                if (sample.isCheckedInListTwo()) {
                    selectedSubCategories.append(sample.getGroupName()).append(", ");
                }
            }


            if (selectedSubCategories.toString().length() != 0) {
                String selectedLabsStr = selectedSubCategories.substring(0, selectedSubCategories.toString().length() - 2);
                edt_tests_for_lab_2.setText(selectedLabsStr);
            }
        });

        builder.create().show();
    }

    private class ServiceGroupAdapterTwo extends RecyclerView.Adapter<ServiceGroupAdapterTwo.MyViewHolder> {

        @NonNull
        @Override
        public ServiceGroupAdapterTwo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new ServiceGroupAdapterTwo.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ServiceGroupAdapterTwo.MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(serviceLabTwoList.get(position).getGroupName());

            if (serviceLabTwoList.get(position).isCheckedInListTwo()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                serviceLabTwoList.get(position).setCheckedInListTwo(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return serviceLabTwoList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            if (requestCode == CAMERA_REQUEST) {
////                CropImage.activity(imageURI).setGuidelines(CropImageView.Guidelines.ON).start(CreateCamp_Activity_v3.this);
//            }
//        }
//

    /// /        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
    /// /            CropImage.ActivityResult result = CropImage.getActivityResult(data);
    /// /            if (resultCode == RESULT_OK) {
    /// /                Uri resultUri = result.getUri();
    /// /                savefile(resultUri);
    /// /            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
    /// /                Exception error = result.getError();
    /// /            }
    /// /        }
//    }
    public void savefile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String destinationFilename = "";
        String filename = (int) (Math.random() * 999999 + 1) + ".png";
//        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Camp Approval Letter/" + filename;
        destinationFilename = getExternalCacheDir() + "/" + filename;

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

        Bitmap imagePicBm = BitmapFactory.decodeFile(destinationFilename);
        destinationFilename = compressImage(destinationFilename);
        imagePicBm = Bitmap.createScaledBitmap(imagePicBm, 150, 150, false);
        imv_camp_approval_letter.setImageBitmap(imagePicBm);
        imagePath = destinationFilename;
    }

//    private class CreateCamp extends AsyncTask<String, Integer, String> {
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
//            try {
//
//                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.CW_CampCreationNewD2d, "UTF-8");
//                multipart.addFormField("CampName", params[0]);
//                multipart.addFormField("CampDate", params[1]);
//                multipart.addFormField("PostCampDate", params[2]);
//                multipart.addFormField("LABCODE", params[3]);
//                multipart.addFormField("AffilatedHospitalId", params[4]);
//                multipart.addFormField("ArogyaMitra", params[5]);
//                multipart.addFormField("Distlgdcode", params[6]);
//                multipart.addFormField("UserId", params[7]);
//                multipart.addFormField("CampLocation", params[8]);
//                multipart.addFormField("ArogyaMitraMOB", params[9]);
//                multipart.addFormField("CampTestMapping", params[10]);
//                multipart.addFormField("CampLabMapping", params[11]);
//                multipart.addFilePart("FileName", new File(params[12]));
//                multipart.addFormField("CampType", params[13]);
//                multipart.addFormField("InitiatedBy", params[14]);
//                multipart.addFormField("Taluka", params[15]);
//                multipart.addFormField("LandingLab", params[16]);
//
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
//            try {
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                    JSONObject obj1 = new JSONObject(result);
//                    String status = obj1.getString("status");
//                    String message = obj1.getString("message");
//                    if (status.equalsIgnoreCase("Success")) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setMessage("Camp created successfully");
//                        builder.setTitle("Success");
//                        builder.setIcon(R.drawable.icon_success);
//                        builder.setCancelable(false);
//                        builder.setPositiveButton("OK", (dialog, id) -> finish());
//                        AlertDialog alertD = builder.create();
//                        alertD.show();

    /// /                        startActivity(new Intent(CreateCamp_Activity_v3.this, ResourceAllocationActivity.class));
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                } else
//                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
//
//            }
//        }
//
//    }

    private class CreateCamp extends AsyncTask<String, Integer, String> {

        String ExpBenCount;
        String CampName;
        String CampDate;
        String PostCampDate;
        String LABCODE;
        String AffilatedHospitalId;
        String Distlgdcode;
        String UserId;
        String CampLocation;
        String CampTestMapping;
        String CampType;
        String InitiatedBy;
        String LandingLab;
        String CscpreapprovedId;

        String TalukaCode;
        String Lattitude;
        String Longitude;


        public CreateCamp(String expBenCount, String campName, String campDate, String postCampDate, String LABCODE, String affilatedHospitalId, String distlgdcode, String userId, String campLocation, String campTestMapping, String campType,
                          String initiatedBy, String landingLab, String cscpreapprovedId, String talukaCode, String lattitude, String longitude) {
            CampName = campName;
            CampDate = campDate;
            PostCampDate = postCampDate;
            this.LABCODE = LABCODE;
            AffilatedHospitalId = affilatedHospitalId;
            Distlgdcode = distlgdcode;
            UserId = userId;
            CampLocation = campLocation;
            CampTestMapping = campTestMapping;
            CampType = campType;
            InitiatedBy = initiatedBy;
            LandingLab = landingLab;
            CscpreapprovedId = cscpreapprovedId;
            TalukaCode = talukaCode;
            ExpBenCount = expBenCount;
            Lattitude = lattitude;
            Longitude = longitude;
        }

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
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("ExpBenCount", ExpBenCount));
            param.add(new ParamsPojo("CampName", CampName));
            param.add(new ParamsPojo("CampDate", CampDate));
            param.add(new ParamsPojo("PostCampDate", PostCampDate));
            param.add(new ParamsPojo("LABCODE", LABCODE));
            param.add(new ParamsPojo("AffilatedHospitalId", AffilatedHospitalId));
            param.add(new ParamsPojo("Distlgdcode", Distlgdcode));
            param.add(new ParamsPojo("UserId", UserId));
            param.add(new ParamsPojo("CampLocation", CampLocation));
            param.add(new ParamsPojo("CampTestMapping", CampTestMapping));
            param.add(new ParamsPojo("CampType", CampType));
            param.add(new ParamsPojo("InitiatedBy", InitiatedBy));
            param.add(new ParamsPojo("LandingLab", LandingLab));
            param.add(new ParamsPojo("CscpreapprovedId", CscpreapprovedId));
            param.add(new ParamsPojo("TalukaCode", TalukaCode));
            param.add(new ParamsPojo("Lattitude", Lattitude));
            param.add(new ParamsPojo("Longitude", Longitude));
//            param.add(new ParamsPojo("UnitID", uniteId));
//            param.add(new ParamsPojo("MMUScheduleId", mmuSceduleId));
//            res = WebServiceCall.APICall(ApplicationConstants.InsertCampCreation_V3, ApplicationConstants.webservice_d2d, param);

            if (BuildConfig.isBeta) {
                res = WebServiceCall.APICall(ApplicationConstants.InsertCampCreation_LatLong, ApplicationConstants.webservice_d2d, param);

            } else {
                res = WebServiceCall.APICall(ApplicationConstants.InsertCampCreation_LatLong, ApplicationConstants.webservice_d2d, param);
            }

            return res;
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
                        builder.setMessage("Camp created successfully. CampId " + message);
                        builder.setTitle(status);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", (dialog, id) -> finish());
                        AlertDialog alertD = builder.create();
                        alertD.show();
//                        startActivity(new Intent(CreateCamp_Activity_v3.this, ResourceAllocationActivity.class));
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode != PLACE_PICKER_REQUEST) return;

        if (resultCode == RESULT_OK && data != null) {

            Place place = Autocomplete.getPlaceFromIntent(data);

            Log.d("PLACE_OK", "Name: " + place.getName());
            Log.d("PLACE_OK", "Address: " + place.getAddress());


            edt_camp_address.setText(place.getAddress());

            Checkbox.setEnabled(true);
            edt_camp_address.setEnabled(false);


            if (place.getLatLng() != null) {
                Log.d("PLACE_OK", "Lat: " + place.getLatLng().latitude);
                Log.d("PLACE_OK", "Lng: " + place.getLatLng().longitude);

                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
            } else {
                Utilities.showAlertDialog(context, "Alert", "location not found.", false);

                edt_camp_address.setText("");


            }


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {

            Status status = Autocomplete.getStatusFromIntent(data);
            Log.e("PLACE_ERROR", status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {

            Log.w("PLACE_CANCEL", "User cancelled place search");
        }
    }


    private ActivityResultLauncher<Intent> placeLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                            Place place = Autocomplete.getPlaceFromIntent(result.getData());

                            Log.d("PLACE_OK", place.getAddress());

                        } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {

                            Status status = Autocomplete.getStatusFromIntent(result.getData());
                            Log.e("PLACE_ERROR", status.getStatusMessage());

                        }
                    });


    private class CreateCampNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampName", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("PostCampDate", params[2]));
            param.add(new ParamsPojo("LABCODE", params[3]));
            param.add(new ParamsPojo("AffilatedHospitalId", params[4]));
            param.add(new ParamsPojo("Distlgdcode", params[5]));
            param.add(new ParamsPojo("UserId", params[6]));
            param.add(new ParamsPojo("CampLocation", params[7]));
            param.add(new ParamsPojo("CampTestMapping", params[8]));
            param.add(new ParamsPojo("CampType", params[9]));
            param.add(new ParamsPojo("InitiatedBy", params[10]));
            param.add(new ParamsPojo("LandingLab", params[11]));
            param.add(new ParamsPojo("CscpreapprovedId", params[12]));
            param.add(new ParamsPojo("TalukaCode", params[13]));
            param.add(new ParamsPojo("Expectedbeneficiarycount", params[14]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertCampCreation_V2, ApplicationConstants.webservice_d2d, param);
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


    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Creation");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    private class CreateCampCSC extends AsyncTask<String, Integer, String> {

        String CampName;
        String CampDate;
        String PostCampDate;
        String LABCODE;
        String AffilatedHospitalId;
        String Distlgdcode;
        String UserId;
        String CampLocation;
        String CampTestMapping;
        String CampType;
        String InitiatedBy;
        String LandingLab;
        String CscpreapprovedId;

        public CreateCampCSC(String campName, String campDate, String postCampDate, String LABCODE, String affilatedHospitalId, String distlgdcode, String userId, String campLocation, String campTestMapping, String campType, String initiatedBy, String landingLab, String cscpreapprovedId) {
            CampName = campName;
            CampDate = campDate;
            PostCampDate = postCampDate;
            this.LABCODE = LABCODE;
            AffilatedHospitalId = affilatedHospitalId;
            Distlgdcode = distlgdcode;
            UserId = userId;
            CampLocation = campLocation;
            CampTestMapping = campTestMapping;
            CampType = campType;
            InitiatedBy = initiatedBy;
            LandingLab = landingLab;
            CscpreapprovedId = cscpreapprovedId;


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please Wait . . .");
            pd.setCancelable(false);
            pd.show();
        }

//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            try {
//
//                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.CW_CampCreationForCSCEgov, "UTF-8");
//                multipart.addFormField("CampName", params[0]);
//                multipart.addFormField("CampDate", params[1]);
//                multipart.addFormField("PostCampDate", params[2]);
//                multipart.addFormField("LABCODE", params[3]);
//                multipart.addFormField("AffilatedHospitalId", params[4]);
//                multipart.addFormField("ArogyaMitra", params[5]);
//                multipart.addFormField("Distlgdcode", params[6]);
//                multipart.addFormField("UserId", params[7]);
//                multipart.addFormField("CampLocation", params[8]);
//                multipart.addFormField("ArogyaMitraMOB", params[9]);
//                multipart.addFormField("CampTestMapping", params[10]);
//                multipart.addFormField("CampLabMapping", params[11]);
//                multipart.addFormField("CSCUserId", params[12]);
//                multipart.addFormField("CSCUsername", params[13]);
//                multipart.addFormField("CSCDesignationId", params[14]);
//                multipart.addFormField("CSCDesignationname", params[15]);
//                multipart.addFormField("ISCSCCamp", params[16]);
//                multipart.addFilePart("FileName", new File(params[17]));
//                List<String> response = multipart.finish();
//                for (String line : response) {
//                    res = res + line;
//                }
//                return res;
//            } catch (IOException ex) {
//                return ex.toString();
//            }
//        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampName", CampName));
            param.add(new ParamsPojo("CampDate", CampDate));
            param.add(new ParamsPojo("PostCampDate", PostCampDate));
            param.add(new ParamsPojo("LABCODE", LABCODE));
            param.add(new ParamsPojo("AffilatedHospitalId", AffilatedHospitalId));
            param.add(new ParamsPojo("Distlgdcode", Distlgdcode));
            param.add(new ParamsPojo("UserId", UserId));
            param.add(new ParamsPojo("CampLocation", CampLocation));
            param.add(new ParamsPojo("CampTestMapping", CampTestMapping));
            param.add(new ParamsPojo("CampType", CampType));
            param.add(new ParamsPojo("InitiatedBy", InitiatedBy));
            param.add(new ParamsPojo("LandingLab", LandingLab));
            param.add(new ParamsPojo("CscpreapprovedId", CscpreapprovedId));
            res = WebServiceCall.APICall(ApplicationConstants.InsertCampCreation, ApplicationConstants.webservice_d2d, param);
            return res;
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
                        builder.setMessage("Camp created successfully. CampId " + message);
                        builder.setTitle(status);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", (dialog, id) -> {
                            new CSCCampApprovalUpdate(intentRequest, false).execute();

                        });
                        AlertDialog alertD = builder.create();
                        alertD.show();
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);

            }
        }

    }

    private class CSCCampApprovalUpdate extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        CSCPreCampInfoForApprovalModel.Output output;
        boolean isRejected = false;

        public CSCCampApprovalUpdate(CSCPreCampInfoForApprovalModel.Output output, boolean isRejected) {
            this.output = output;
            this.isRejected = isRejected;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            List<ParamsPojo> paramsPojos = new ArrayList<>();
            paramsPojos.add(new ParamsPojo("CSCPreAprrovedid", String.valueOf(output.getCSCPreAprrovedid())));
            paramsPojos.add(new ParamsPojo("ApprovedBy", userId));
            paramsPojos.add(new ParamsPojo("CampStatus", "1"));
            paramsPojos.add(new ParamsPojo("Reason", "Approved"));

            res = WebServiceCall.APICall(ApplicationConstants.CSCCampApprovalUpdate, ApplicationConstants.webservice, paramsPojos);

            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("CSCCampApprovalUpdate", "onPostExecute: " + result);
            progressDialog.dismiss();

            if (!result.equals("") || !result.equals("[]")) {

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        Utilities.showAlertDialog(context, "Success", "Camp Request Approved Successfully!", true, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                dialogInterface.dismiss();
                            }
                        });


                    } else {
                        Utilities.showAlertDialog(context, "Failed", "Camp Request failed!", false, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                        updateCamp.onUpdateCamp();
                                dialogInterface.dismiss();

                            }
                        });

                    }

                } catch (Exception jsonException) {
                    Log.e("TAG", "Response: " + jsonException.getMessage());
                }

            }


        }

    }

    public class GetCampType extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.CampTypeD2D, ApplicationConstants.webservice_d2d, param);
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
                            showCampTypeDialog(camptypelist);
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

        private void showCampTypeDialog(final List<NewCampTypeModel.Output> camptypelist) {
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
                    edt_camp_type.setText(camptypelist.get(which).getCampTypeDescription());
                    camptypeId = String.valueOf(camptypelist.get(which).getCamptype());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }

    public class GetCampTypeMMU extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.CampTypeMMU, ApplicationConstants.webservice_d2d, param);
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
                            showCampTypeDialog(camptypelist);
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

        private void showCampTypeDialog(final List<NewCampTypeModel.Output> camptypelist) {
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
                    edt_camp_type.setText(camptypelist.get(which).getCampTypeDescription());
                    camptypeId = String.valueOf(camptypelist.get(which).getCamptype());
                    //  refreshCalendar();

//                    getUniteId();
                }
            });
            builderSingle.show();

        }

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
                    edt_camp_type.setText(camptypelist.get(which).getCampTypeDescription());
                    camptypeId = String.valueOf(camptypelist.get(which).getCamptype());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }


    public class GetInitiatedByList extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetInitiatedByListForCamp, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    GetInitiatedByListForCampModel getInitiatedByListForCampModel = new Gson().fromJson(result, GetInitiatedByListForCampModel.class);
                    type = getInitiatedByListForCampModel.getStatus();
                    message = getInitiatedByListForCampModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<GetInitiatedByListForCampModel.Output> camptypelist = getInitiatedByListForCampModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showInitiatedByDialog(camptypelist);
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

        private void showInitiatedByDialog(final List<GetInitiatedByListForCampModel.Output> initiatedlist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Initiated By");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < initiatedlist.size(); i++) {
                arrayAdapter.add(String.valueOf(initiatedlist.get(i).getInitiatedBy()));
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
                    edt_initiated_by.setText(initiatedlist.get(which).getInitiatedBy());
                    initiatedId = String.valueOf(initiatedlist.get(which).getId());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

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


    public class GetLandingLab extends AsyncTask<String, Void, String> {

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
            if (BuildConfig.isBeta) {
                //  res = WebServiceCall.APICall(ApplicationConstants.GetLabTalukaWise_V1, ApplicationConstants.webservice_d2d, param);
                res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);
            } else {
                res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);
                //  res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    LandingLabModel landingLabModel = new Gson().fromJson(result, LandingLabModel.class);
                    type = landingLabModel.getStatus();
                    message = landingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<LandingLabModel.Output> landinglablist = landingLabModel.getOutput();
                        if (landinglablist.size() > 0) {
                            showLadingLabDialogue(landinglablist);
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

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }

        private void showLadingLabDialogue(final List<LandingLabModel.Output> landinglablist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Lab");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < landinglablist.size(); i++) {
                arrayAdapter.add(String.valueOf(landinglablist.get(i).getLabName()));
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
                    edt_LandingLab.setText(landinglablist.get(which).getLabName());
                    landinglabId = String.valueOf(landinglablist.get(which).getLabCode());

                    edt_lab_1.setText("");
                    edt_lab_2.setText("");
                    //  refreshCalendar();
//                    new GetCamName().execute();
                    new GetHomeLabHubLabDetails().execute(landinglabId, "0");
                }
            });
            builderSingle.show();

        }
    }

    public class GetCampTypeForRegular extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.CampTypeNonD2D, ApplicationConstants.webservice_d2d, param);
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
                            showCampTypeNonDialog(camptypelist);
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

        private void showCampTypeNonDialog(final List<NewCampTypeModel.Output> camptypelist) {
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
                    edt_camp_type.setText(camptypelist.get(which).getCampTypeDescription());
                    camptypeId = String.valueOf(camptypelist.get(which).getCamptype());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }

    public class GetHomeLabHubLabDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("LabCode", params[0]));
            param.add(new ParamsPojo("TypeID", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetHomeAndHubLabNamesOfLandingLab, ApplicationConstants.webservice_d2d, param);
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

                            edt_lab_1.setText(output.getHomeLab());
                            edt_lab_2.setText(output.getHubLab());
                            selectedHomeLabID = String.valueOf(output.getHomeLabcode());
                            selectedHubLabID = String.valueOf(output.getHubLabcode());
//                            edt_lab_1.setEnabled(false);
//                            edt_lab_2.setEnabled(false);


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

    public class GetCamName extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", distLgdCode));
            param.add(new ParamsPojo("LabCode", landinglabId));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampName, ApplicationConstants.webservice_d2d, param);
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
                    List<CampNameModel.Output> campList = new ArrayList<>();

                    CampNameModel campNameModel = new Gson().fromJson(result, CampNameModel.class);
                    type = campNameModel.getStatus();
                    message = campNameModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (campNameModel.getOutput().size() > 0) {
                            CampNameModel.Output output = campNameModel.getOutput().get(0);

                            edt_camp_name.setText(output.getCampName());
                            // edt_camp_name.setEnabled(false);
//                            edt_lab_2.setText(output.getHubLab());
//                            selectedHomeLabID = String.valueOf(output.getHomeLabcode());
//                            selectedHubLabID = String.valueOf(output.getHubLabcode());
//                            edt_lab_1.setEnabled(false);
//                            edt_lab_2.setEnabled(false);


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


    private void getMMUDAtaCall() {
        final ProgressDialog progressDialog = new ProgressDialog(CreateCamp_Activity_v3.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.mmuCamp_call().create(ApiInterface.class);
        Call<MmuSchedulesDataModel> call = apiService.getMmuCamp_CALL(uniteId);
        call.enqueue(new Callback<MmuSchedulesDataModel>() {
            @Override
            public void onResponse(Call<MmuSchedulesDataModel> call, Response<MmuSchedulesDataModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getDateTime();

                    if (status.equalsIgnoreCase("1")) {
                        List<MmuSchedulesDataModel.Output> invoiceList = response.body().getDetails();
                        String campDate = edt_camp_date.getText().toString().trim(); // Example of camp date
                        Log.d("DEBUG", "Camp Date: " + campDate);

                        if (invoiceList != null && !invoiceList.isEmpty()) {
                            boolean isMatchFound = false; // Flag to track if a match is found

                            for (MmuSchedulesDataModel.Output o : invoiceList) {
                                String dateOfVisit = o.getDateOfVisit();
                                Log.d("DEBUG", "Date of Visit: " + dateOfVisit);

                                try {
                                    if (isDateEqualToCampDate(dateOfVisit, campDate)) {
                                        Log.d("DEBUG", "Date Match Found for: " + dateOfVisit);
                                        Utilities.showAlertDialog(context, "Success", "Camp Scheduled for this date", true);

                                        edt_camp_address.setText(o.getAddress());
                                        mmuSceduleId = String.valueOf(o.getMmuScheduleId());
                                        isMatchFound = true; // Set flag to true if match is found
                                        break; // Exit the loop as we've found a match
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Log.e("DEBUG", "Date parsing failed: " + e.getMessage());
                                }
                            }

                            // Show error message if no match was found
                            if (!isMatchFound) {
                                Utilities.showAlertDialog(context, "Alert", "No camp scheduled for this camp date", false);
                                edt_camp_date.setText("");
                                edt_post_camp_date.setText("");
                                edt_camp_address.setText("");
                                Log.d("DEBUG", "No matching date found for the selected camp date.");
                            }
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);
                    }
                } else {
                    Utilities.showAlertDialog(context, "Alert", "Invoice Details Not Found", false);
                }
            }

            @Override
            public void onFailure(Call<MmuSchedulesDataModel> call, Throwable t) {
                progressDialog.dismiss();
                t.getLocalizedMessage();
            }
        });
    }


    private void getUniteId() {
        final ProgressDialog progressDialog = new ProgressDialog(CreateCamp_Activity_v3.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<UniteIdModel> call = apiService.getUniteId(distLgdCode);
        call.enqueue(new Callback<UniteIdModel>() {
            @Override
            public void onResponse(Call<UniteIdModel> call, Response<UniteIdModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
//                        campBeneficiaryList = response.body().getOutput();
                        List<UniteIdModel.Output> invoiceList = response.body().getOutput();


                        if (invoiceList.size() > 0) {

                            for (UniteIdModel.Output o : invoiceList) {

                                uniteId = String.valueOf(o.getUnitID());
                                UniteName = o.getUnitName();

                                edt_unitName.setText(UniteName);
                                edt_unitName.setVisibility(View.VISIBLE);

                            }

                        }

                    } else {

                        Utilities.showAlertDialog(context, "Alert", message, false);

                    }
                } else {
                    Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<UniteIdModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private boolean isDateEqualToCampDate(String dateOfVisit, String campDate) throws ParseException {
        // Parse dateOfVisit in "dd/MM/yyyy" format
        SimpleDateFormat dateOfVisitFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date visitDate = dateOfVisitFormat.parse(dateOfVisit);

        // Parse campDate in "yyyy/MM/dd" format
        SimpleDateFormat campDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date campVisitDate = campDateFormat.parse(campDate);

        // Use date-only format for comparison
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String visitDateOnly = dateOnlyFormat.format(visitDate);
        String campVisitDateOnly = dateOnlyFormat.format(campVisitDate);

        // Compare date parts only
        return visitDateOnly.equals(campVisitDateOnly);
    }

    private void getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0); // Full address

                edt_camp_address.setText(addressLine);
                Log.d("Address", "Address: " + addressLine);
            } else {
                Log.d("Address", "No address found for the coordinates.");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("USERID", params[1]));


            //  res = WebServiceCall.APICall(ApplicationConstants.GetAllDistrictList, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictByUserID, ApplicationConstants.webservice_d2d, param);
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
                            //  districtList.add(0, new DistrictList_Model("0", "All"));
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
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

                edt_taluka.setText("");
                edt_LandingLab.setText("");
                //  refreshCalendar();

//                new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE,TALLGDCODE,"411025", "1");


            }
        });
        builderSingle.show();

    }

//
//    private void searchLocation(String query) {
//
//        new Thread(() -> {
//            try {
//                String urlStr = "https://nominatim.openstreetmap.org/search" +
//                        "?q=" + URLEncoder.encode(query, "UTF-8") +
//                        "&format=json&addressdetails=1&limit=5";
//
//                URL url = new URL(urlStr);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("User-Agent", "Android-App");
//                conn.connect();
//
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(conn.getInputStream())
//                );
//
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                JSONArray array = new JSONArray(sb.toString());
//
//                locationList.clear();
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject obj = array.getJSONObject(i);
//
//                    locationList.add(new LocationModel(
//                            obj.getString("display_name"),
//                            obj.getDouble("lat"),
//                            obj.getDouble("lon")
//                    ));
//                }
//
//                runOnUiThread(() -> {
//                    adapter.notifyDataSetChanged();
//                    rv_location.setVisibility(View.VISIBLE);
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }


    private void searchLocation(String query) {

        new Thread(() -> {
            try {

                String refinedQuery = query + ", Maharashtra, India";

                String urlStr = "https://nominatim.openstreetmap.org/search" +
                        "?q=" + URLEncoder.encode(refinedQuery, "UTF-8") +
                        "&format=json&addressdetails=1&limit=5&countrycodes=in";


//
//                String urlStr = "https://nominatim.openstreetmap.org/search" +
//                        "?q=" + URLEncoder.encode(query, "UTF-8") +
//                        "&format=json" +
//                        "&addressdetails=1" +
//                        "&limit=5" +
//                        "&countrycodes=in";   // 🇮🇳 INDIA ONLY

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "YourAppName/1.0 (your@email.com)");
                conn.connect();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONArray array = new JSONArray(sb.toString());

                locationList.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    locationList.add(new LocationModel(
                            obj.getString("display_name"),
                            obj.getDouble("lat"),
                            obj.getDouble("lon")
                    ));
                }

                runOnUiThread(() -> {
//                    adapter.notifyDataSetChanged();
                    rv_location.setVisibility(View.VISIBLE);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }


}

//0 = "19"
//1 = "Test Camp Name"
//2 = "2020/06/11"
//3 = "2020/06/18"
//4 = "29"
//5 = "378"
//6 = "Manish Kale"
//7 = "490"
//8 = "1219"
//9 = "29/2,Shantikunj,Somwar Peth,Pune 411011"
//10 = "8646494949"
//11 = "/storage/emulated/0/Health Checkup/Camp Approval Letter/760854.png"
