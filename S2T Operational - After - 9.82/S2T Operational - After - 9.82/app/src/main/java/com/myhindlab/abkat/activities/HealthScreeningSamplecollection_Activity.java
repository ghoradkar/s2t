package com.myhindlab.abkat.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.myhindlab.abkat.utilities.Utilities.isBarCodeValidWithThreeZero;
import static com.myhindlab.abkat.utilities.Utilities.sdfYear;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.HealthCheckup;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.abha.models.ABHASessionModel;
import com.myhindlab.abkat.abha.models.add_context.CareContextLinkingRequestModel;
import com.myhindlab.abkat.abha.models.add_context.CareContexts;
import com.myhindlab.abkat.abha.models.add_context.Patient;
import com.myhindlab.abkat.abha.models.context_notify.CareContext;
import com.myhindlab.abkat.abha.models.context_notify.CareContextNotifyRequestModel;
import com.myhindlab.abkat.abha.models.link_token.GetLinkTokenCallbackResponseModel;
import com.myhindlab.abkat.abha.models.sms_notify.Hip;
import com.myhindlab.abkat.abha.models.sms_notify.Notification;
import com.myhindlab.abkat.abha.models.sms_notify.SMSNotifyRequestModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.CustomerTestResponseModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ListTestDetails;
import com.myhindlab.abkat.activities.regularcampcreation.model.TestDetailsDto;
import com.myhindlab.abkat.adapters.LabListAdapter;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.models.SpecimenTypeListModel;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;
import com.myhindlab.abkat.pojos.Lab_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthScreeningSamplecollection_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private MaterialEditText edt_beneficiaryname, edt_lab, edt_gender, edt_age, edt_sample_count, edt_barcode1, edt_barcode2, edt_height, edt_weight, edt_specimen,
            edt_sample_collection_date, edt_sample_collection_time;
    private ImageView imv_barcode1, imv_barcode2;
    private String isScannedByScanner = "1", versionName;

    private List<TestDetailsDto> testList;


    int years = 0;
    int remainingDays = 0;

    int months = 0;
    int days = 0;


    private String firstName = "";
    private String sendToDisha = "0";
    private String cusomerId = "";

    private Switch switch_button;


    private String middleName = "";

    private TextView tvGoogleLocation;
    private String lastName = "";
    private String title = "";
    private String emailId = "";

    double latitude = 0.0;
    double longitude = 0.0;

    private String currentAddress;


    private FusedLocationProviderClient mFusedLocationClient;


    private String idToken;
    private Button btn_register;
    private PresentPatientList_Model patientDetails;
    private String userID, labCode = "0", Labcode = "", name, campId, healthScreentype, antigenResult, specimen_id, DISTLGDCODE, selectedLabID = "", selectedLabName = "";
    private int mYear1, mMonth1, mDay1;
    String date;
    private TextView tv_bloodsample, tv_test;

    private TextInputEditText edtABHANumber;
    private TextInputEditText edtABHAAddress;
    private LinearLayoutCompat llABHADetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_samplecollection);

        init();
        setUpToolbar();
        setDefaults();

        if (session.isHllUser()) {
            getHLLSessionData();
        } else {
            getSessionData();
        }

        setEventHandler();

    }

    private void init() {
        context = HealthScreeningSamplecollection_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);


        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_sample_count = findViewById(R.id.edt_sample_count);
        edt_barcode1 = findViewById(R.id.edt_barcode1);
        edt_barcode2 = findViewById(R.id.edt_barcode2);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        edt_lab = findViewById(R.id.edt_lab);
        edtABHANumber = findViewById(R.id.edtABHANumber);
        edtABHAAddress = findViewById(R.id.edtABHAAddress);
        imv_barcode1 = findViewById(R.id.imv_barcode1);
        imv_barcode2 = findViewById(R.id.imv_barcode2);

        btn_register = findViewById(R.id.btn_register);
        edt_specimen = findViewById(R.id.edt_specimen);
        edt_sample_collection_date = findViewById(R.id.edt_sample_collection_date);
        edt_sample_collection_time = findViewById(R.id.edt_sample_collection_time);
        tv_bloodsample = findViewById(R.id.tv_bloodsample);
        tv_test = findViewById(R.id.tv_test);
        llABHADetails = findViewById(R.id.llABHADetails);
        switch_button = findViewById(R.id.switch_button);
        tvGoogleLocation = findViewById(R.id.tvGoogleLocation);


    }

    private void setDefaults() {


//        ****************************************Current Location***********************************

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
                    tvGoogleLocation.setText(
                            addresses.get(0).getAddressLine(0)


                    );

                    currentAddress = addresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

//        ****************************************Current Location***********************************


        getAPIToken();


        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");
        healthScreentype = getIntent().getStringExtra("healthScreentype");

        antigenResult = patientDetails.getAntigenResult();

        if (antigenResult.equalsIgnoreCase("1")) {
            edt_specimen.setVisibility(View.VISIBLE);
            tv_test.setTextColor(getResources().getColor(R.color.red));
            tv_test.setText("RT-PCR Test Only");
            specimen_id = "3";

        } else {
            edt_specimen.setVisibility(View.GONE);
            tv_test.setText("All Test (except RT-PCR) ");
            specimen_id = "";
        }


        edt_beneficiaryname.setText(patientDetails.getEnglishName());

        Log.d(TAG, "Beneficairy Name:" + patientDetails.getEnglishName());


        String fullName = patientDetails.getEnglishName().trim();


        String dobString = patientDetails.getDob(); // "24/03/1967"


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dob = sdf.parse(dobString);

            Calendar dobCal = Calendar.getInstance();
            Calendar todayCal = Calendar.getInstance();

            dobCal.setTime(dob);

            years = todayCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);
            months = todayCal.get(Calendar.MONTH) - dobCal.get(Calendar.MONTH);
            days = todayCal.get(Calendar.DAY_OF_MONTH) - dobCal.get(Calendar.DAY_OF_MONTH);

            // Adjust days
            if (days < 0) {
                months--;
                todayCal.add(Calendar.MONTH, -1);
                days += todayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            // Adjust months
            if (months < 0) {
                years--;
                months += 12;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d(TAG, "Year - " + years + "\n" + "month :" + months + "\n" + "days :" + days);


        if (!fullName.isEmpty()) {

            String[] parts = fullName.split("\\s+"); // split by space

            if (parts.length == 1) {
                firstName = parts[0];
            } else if (parts.length == 2) {
                firstName = parts[0];
                lastName = parts[1];
            } else if (parts.length >= 3) {
                firstName = parts[0];
                lastName = parts[parts.length - 1];

                // join middle names if more than one
                StringBuilder middleBuilder = new StringBuilder();
                for (int i = 1; i < parts.length - 1; i++) {
                    middleBuilder.append(parts[i]).append(" ");
                }
                middleName = middleBuilder.toString().trim();
            }
        }


        Log.d(TAG, "fname :" + firstName + "\n" + "Mname :" + middleName + "\n" + "Lname:" + lastName);

        emailId = firstName + "@gmail.com";

        Log.d(TAG, "email:- " + emailId);

        edt_age.setText(patientDetails.getAge());
        edt_height.setText(String.valueOf(patientDetails.getHeightCMs()));
        edt_weight.setText(String.valueOf(patientDetails.getWeightKGs()));
        if (patientDetails.getAntiBarcode() != null) {
            edt_barcode1.setText(patientDetails.getAntiBarcode());
            edt_barcode1.setEnabled(false);
            imv_barcode1.setEnabled(false);
        }

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            edt_gender.setText("Male");

            title = "Mr";
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            edt_gender.setText("Female");
            title = "Ms";
        } else if (patientDetails.getGender().equalsIgnoreCase("O")) {
            edt_gender.setText("Other");
            title = "Mx";
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        edt_sample_collection_date.setText(format1.format(cal.getTime()));
        edt_sample_collection_time.setText(format2.format(cal.getTime()));
        date = format1.format(cal.getTime());
        edt_specimen.setText("NASOPHARYNGEAL AND OROPHARYNGEAL SWAB");

        createABHASession();


        if (patientDetails.getABHAAddress() != null || patientDetails.getABHANumber() != null) {
            if (patientDetails.getABHAAddress().isEmpty() || patientDetails.getABHANumber().isEmpty()) {
                llABHADetails.setVisibility(View.GONE);
            } else {
                edtABHAAddress.setText(patientDetails.getABHAAddress());
                edtABHANumber.setText(patientDetails.getABHANumber());
                edtABHAAddress.setEnabled(false);
                edtABHANumber.setEnabled(false);
                llABHADetails.setVisibility(View.VISIBLE);
            }
        } else {
            llABHADetails.setVisibility(View.GONE);
        }


//        if (session.isHllUser()) {
//            edt_lab.setVisibility(View.VISIBLE);
//        } else {
//            edt_lab.setVisibility(View.GONE);
//        }
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                name = json.getString("name");
                DISTLGDCODE = String.valueOf(json.getInt("DISTLGDCODE"));
                labCode = json.getString("LabCode");


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
                userID = json.getString("EmpCode");
                name = json.getString("name");
                DISTLGDCODE = String.valueOf(json.getInt("DISTLGDCODE"));
                Labcode = String.valueOf(json.getInt("Labcode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        edt_sample_collection_date.setOnClickListener(v -> {
            Calendar c1 = Calendar.getInstance();
            mYear1 = c1.get(Calendar.YEAR);
            mMonth1 = c1.get(Calendar.MONTH);
            mDay1 = c1.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(context,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        edt_sample_collection_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate2, dayOfMonth, monthOfYear + 1, year));
                        edt_sample_collection_time.setText("");
                        mYear1 = year;
                        mMonth1 = monthOfYear;
                        mDay1 = dayOfMonth;
                    }, mYear1, mMonth1, mDay1);
            Calendar c0 = Calendar.getInstance();
            c0.set(mYear1, mMonth1, mDay1);
            try {
                dpd.getDatePicker().setCalendarViewShown(false);
                c1.add(Calendar.DATE, -2);
                dpd.getDatePicker().setMinDate(c1.getTimeInMillis());
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dpd.show();
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


        switch_button.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch is ON
//                Toast.makeText(this, "Switch is ON", Toast.LENGTH_SHORT).show();
                sendToDisha = "1";

            } else {
//                // Switch is OFF
//                Toast.makeText(this, "Switch is OFF", Toast.LENGTH_SHORT).show();

                sendToDisha = "0";

            }
        });


        edt_barcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                isScannedByScanner = "1";

                Log.d("ScannedBy", isScannedByScanner);


            }


        });


        edt_sample_collection_time.setOnClickListener(v -> {
            try {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selTime = String.format("%02d",
                                Integer.valueOf(selectedHour)) + ":" + String.format("%02d",
                                Integer.valueOf(selectedMinute));
                        if ((edt_sample_collection_date.getText().toString().trim()).equals(date)) {
                            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                            try {
                                Date ten = parser.parse(+hour + ":" + minute);
                                Date eighteen = parser.parse(+selectedHour + ":" + selectedMinute);
                                if (eighteen != null && ten != null)
                                    if (eighteen.after(ten)) {
                                        Utilities.showAlertDialog(context, "Alert", "You can not select Future Time.", false);
                                        return;
                                    }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        edt_sample_collection_time.setText(selTime);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        edt_specimen.setOnClickListener(v -> {
            new GetSpecimenType().execute();
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


//        if (edt_sample_count.getText().toString().trim().isEmpty()) {
//            edt_sample_count.setError("Please enter sample count");
//            return;
//        }


        if (edt_sample_collection_time.getText().toString().trim().isEmpty()) {
            edt_sample_collection_time.setError("Please enter Sample collection Time.");
            return;
        }

        if (!isBarCodeValidWithThreeZero(edt_barcode1.getText().toString().trim())) {
            edt_barcode1.setError("Invalid Barcode... Please try with another barcode");
            return;
        }


        if (session.isHllUser()) {

        }

//        if (edt_lab.getVisibility() == View.VISIBLE)
//            if (labCode.isEmpty()) {
//                Utilities.showToastMessage("Please Select Lab", context, false);
//                return;
//            }
        if (Utilities.isNetworkAvailable(context)) {
            String formatedDate = Utilities.changeDateFormat("dd/MM/yyyy", "yyyy-MM-dd", edt_sample_collection_date.getText().toString());

            if (session.isHllUser()) {


//                if (patientDetails.getABHAAddress() == null || (patientDetails.getABHAAddress().replace("\r\n", "").isEmpty())) {
//                    smsNotify();
//                } else {
//                    generateLinkToken();
//                }


                if (sendToDisha.equals("1")) {


//                    String listTestDetailsJson = prepareListTestDetailsJson();
//
//                    Log.d(TAG, "TestJson : " + listTestDetailsJson);


                    insertDisha();

                } else if (sendToDisha.equals("0")) {


                    new D2DInsertCW_PatientBarcodeDetails().execute(
                            String.valueOf(patientDetails.getRegdId()),
                            "0",
                            campId,
                            edt_sample_count.getText().toString().trim().isEmpty() ? "3" : edt_sample_count.getText().toString().trim(),
                            edt_barcode1.getText().toString().trim(),
                            edt_barcode1.getText().toString().trim(),
                            userID,
                            "1",
                            formatedDate,
                            edt_sample_collection_time.getText().toString().trim(),
                            specimen_id,
                            "0",
                            versionName,
                            isScannedByScanner,
                            String.valueOf(latitude),
                            String.valueOf(longitude)
                    );
                }

            } else {

//                if (patientDetails.getABHAAddress() == null || (patientDetails.getABHAAddress().replace("\r\n", "").isEmpty())) {
//                    smsNotify();
//                } else {
//                    generateLinkToken();
//                }


                if (sendToDisha.equals("1")) {


//                    String listTestDetailsJson = prepareListTestDetailsJson();
//
//                    Log.d(TAG, "TestJson : " + listTestDetailsJson);


                    insertDisha();

                } else if (sendToDisha.equals("0")) {

                    new InsertCW_PatientBarcodeDetails().execute(
                            String.valueOf(patientDetails.getRegdId()),
                            "0",
                            campId,
                            edt_sample_count.getText().toString().trim().isEmpty() ? "3" : edt_sample_count.getText().toString().trim(),
                            edt_barcode1.getText().toString().trim(),
                            edt_barcode1.getText().toString().trim(),
                            userID,
                            "1",
                            formatedDate,
                            edt_sample_collection_time.getText().toString().trim(),
                            specimen_id,
                            versionName,
                            isScannedByScanner,
                            String.valueOf(latitude),
                            String.valueOf(longitude)
                    );

                }

            }
//            generateLinkToken();
//            try {
//                getLinkTokenCallback(patientDetails.getABHAAddress());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private JsonArray prepareListTestDetailsJson() {

        JsonArray jsonArray = new JsonArray();

        if (testList != null && !testList.isEmpty()) {

            String barcode = edt_barcode1.getText().toString().trim();

            for (TestDetailsDto item : testList) {

                JsonObject obj = new JsonObject();
                obj.addProperty("subServiceId", item.getSubServiceId());
                obj.addProperty("amount", 0);
                obj.addProperty("subServiceName", item.getSubServiceName());
                obj.addProperty("sampleTypeId", item.getSampleTypeId());
                obj.addProperty("barCode", barcode);
                obj.addProperty("quantity", 1);
                obj.addProperty("templateWise", item.getTemplateWise());

                jsonArray.add(obj);
            }

        } else {
            Utilities.showAlertDialog(context, "Alert",
                    "test list not found for disha you can try through old LIS", false);
        }

        return jsonArray;
    }

//    private String prepareListTestDetailsJson() {
//
//        if(testList!=null||!testList.isEmpty()){
//
//
//        }
//
//        List<ListTestDetails> list = new ArrayList<>();
//
//        String barcode = edt_barcode1.getText().toString().trim();
//
//        for (TestDetailsDto item : testList) {
//
//            ListTestDetails obj = new ListTestDetails(
//                    item.getSubServiceId(),
//                    0,
//                    item.getSubServiceName(),
//                    item.getSampleTypeId(),
//                    barcode,
//                    1,
//                    item.getTemplateWise()
//            );
//
//            list.add(obj);
//        }
//
//        return new Gson().toJson(list);
//
//
//    }


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
            param.add(new ParamsPojo("VersionNo", params[11]));
            param.add(new ParamsPojo("IsScannedBy", params[12]));
            param.add(new ParamsPojo("Latitude", params[13]));
            param.add(new ParamsPojo("Longitude", params[14]));


//            res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_WithUrineTest, ApplicationConstants.webservice, param);
            //  res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_WithUrineTest_V1, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_WithUrineTest_VersionNo_ScanningFlag, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_WithUrineTest_VersionNo_ScanningFlag_LatLong, ApplicationConstants.webservice_d2d, param);

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
                        if (antigenResult.equals("1")) {
                            new SubmitRTPCRData().execute(String.valueOf(patientDetails.getRegdId()), String.valueOf(patientDetails.getSiteId()), campId, userID);
                        }
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
//
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setIcon(R.drawable.icon_success);
//                        builder.setTitle("Success");
//                        builder.setCancelable(false);
//                        builder.setMessage("Sample collection details submitted successfully");
//                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        });
//                        builder.show();


                        if (patientDetails.getABHAAddress() == null || (patientDetails.getABHAAddress().replace("\r\n", "").isEmpty())) {
                            smsNotify();
                        } else {
                            generateLinkToken();
                        }

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
            param.add(new ParamsPojo("VersionNo", params[12]));
            param.add(new ParamsPojo("IsScannedBy", params[13]));
            param.add(new ParamsPojo("Latitude", params[14]));
            param.add(new ParamsPojo("Longitude", params[15]));

            // res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_New_D2D, ApplicationConstants.webservice, param);
//            res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_New_D2D_V1, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_New_D2D_VersionNo_ScanningFlag, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.InsertCW_PatientBarcodeDetails_New_D2D_VersionNo_ScanningFlag_LatLong, ApplicationConstants.webservice_d2d, param);

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

//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setIcon(R.drawable.icon_success);
//                        builder.setTitle("Success");
//                        builder.setCancelable(false);
//                        builder.setMessage("Sample collection details submitted successfully");
//                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        });
//                        builder.show();

                        if (patientDetails.getABHAAddress() == null || (patientDetails.getABHAAddress().replace("\r\n", "").isEmpty())) {
                            smsNotify();
                        } else {
                            generateLinkToken();
                        }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 10001) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                    edt_barcode1.setText(requiredValue);

                    isScannedByScanner = "2";

                    Log.d("ScannedBy ", isScannedByScanner);


                } else if (requestCode == 10002) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                    edt_barcode2.setText(requiredValue);
                }
            }

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

            res = WebServiceCall.APICall(ApplicationConstants.GetLab, ApplicationConstants.webservice_d2d, param);
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


    /**
     * ABHA Care Context Linking
     */

    private void createABHASession() {

        pd.setMessage("Creating session..");
        pd.setCancelable(false);
        pd.show();
        ABHASessionModel createSession = new ABHASessionModel(BuildConfig.ClientID, BuildConfig.SecretId, "client_credentials");
        HealthCheckup.abhaClient.createAbhaSession(createSession, Utilities.getCurrentTimeStamp(), UUID.randomUUID().toString(), BuildConfig.CMID.replace("@", ""))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        pd.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                if (json != null) {
                                    accessToken = json.getString("accessToken");
                                    getPublicCertificate();
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

    private String publicKey;
    private String accessToken;
    private String TAG = HealthScreeningSamplecollection_Activity.class.getSimpleName();

    void getPublicCertificate() {
        HealthCheckup.ABDMClient.getPublicCertificate(
                "Bearer " + accessToken,
                Utilities.getCurrentTimeStamp(),
                UUID.randomUUID().toString()
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                        Utilities.showAlertDialog(
                                context,
                                "Unable to generate public certificate",
                                response.errorBody().string(),
                                false
                        );
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

    void generateLinkToken() {
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
        try {
            String requestId = UUID.randomUUID().toString();
            String timestamp = Utilities.getCurrentTimeStamp();
            String cmId = BuildConfig.CMID.replace("@", "");
            String abhaAddress = patientDetails.getABHAAddress().replace("\r\n", "");
//            String abhaAddress = edtABHAAddress.getText().toString().trim();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("abhaNumber", patientDetails.getABHANumber().replace("-", ""));
//            jsonObject.addProperty("abhaNumber", edtABHANumber.getText().toString().trim().replace("-", ""));
            jsonObject.addProperty("abhaAddress", abhaAddress);
            jsonObject.addProperty("name", patientDetails.getEnglishName());
            jsonObject.addProperty("gender", patientDetails.getGender());
            String yearOfBirth = "";

            yearOfBirth = sdfYear.format(Utilities.dfDate2.parse(patientDetails.getDob()));


//            yearOfBirth = "1992";

            jsonObject.addProperty("yearOfBirth", yearOfBirth);

            HealthCheckup.abhaClient.generateLinkToken("Bearer " + accessToken, timestamp, requestId, BuildConfig.HIPID, cmId, jsonObject).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.code() == 202) {
                            String res = response.body().string();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();

                                    getLinkTokenCallback(abhaAddress);


                                }
                            }, 5000);
                        } else if (response.code() == 401) {
                            pd.dismiss();
                            createABHASession();
                            generateLinkToken();
                        } else if (response.code() == 400) {
                            pd.dismiss();
                            if (response.errorBody().string().contains("Duplicate Link token request")) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd.dismiss();

                                        getLinkTokenCallback(abhaAddress);


                                    }
                                }, 5000);
                            }
                        } else {
                            pd.dismiss();
                            Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);

                        }
                    } catch (Exception e) {
                        pd.dismiss();
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
        } catch (Exception e) {
            e.printStackTrace();
            pd.dismiss();
            Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);
        }

    }

    void getLinkTokenCallback(String abhaAddress) {
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait..");
        pd.setCancelable(false);
        pd.show();
        HealthCheckup.getD2DClient.getLinkTokenCallback(abhaAddress).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    String res = response.body().string();

                    if (response.isSuccessful()) {
                        GetLinkTokenCallbackResponseModel getLinkTokenCallbackResponseModel = new Gson().fromJson(res, GetLinkTokenCallbackResponseModel.class);
                        linkCareContext(getLinkTokenCallbackResponseModel);
                    } else {
//                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
                        Utilities.showAlertDialog(context, "Unable to get link token callback", "Retry", false, "Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getLinkTokenCallback(patientDetails.getABHAAddress());

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);
                    Utilities.showAlertDialog(context, "Unable to get link token callback", "Retry", false, "Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            getLinkTokenCallback(patientDetails.getABHAAddress());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Utilities.showAlertDialog(context, "Unable to get link token callback", "Retry", false, "Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        getLinkTokenCallback(patientDetails.getABHAAddress());

                    }
                });

            }
        });

    }

    void linkCareContext(GetLinkTokenCallbackResponseModel getLinkTokenCallbackResponseModel) {
        try {
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            String requestId = UUID.randomUUID().toString();
            String timeStamp = Utilities.getCurrentTimeStamp();

            ArrayList<CareContexts> careContexts = new ArrayList<>();
            CareContexts careContext = new CareContexts();
            careContext.setReferenceNumber(Objects.requireNonNull(edt_barcode1.getText()).toString());
            careContext.setDisplay(patientDetails.getEnglishName() + " Health Screening On " + Utilities.getCurrentDate());
            careContexts.add(careContext);

            ArrayList<Patient> patients = new ArrayList<>();
            Patient patient = new Patient();
            patient.setCareContexts(careContexts);
            patient.setReferenceNumber(edt_barcode1.getText().toString());
            patient.setDisplay(patientDetails.getEnglishName() + " Health Screening On " + Utilities.getCurrentDate());
            patient.setHiType("DiagnosticReport");
            patient.setCount(1);

            patients.add(patient);

            CareContextLinkingRequestModel careContextLinkingRequestModel = new CareContextLinkingRequestModel();
            careContextLinkingRequestModel.setAbhaAddress(getLinkTokenCallbackResponseModel.getOutput().get(0).getAbhaAddress());
            careContextLinkingRequestModel.setAbhaNumber(Long.valueOf(patientDetails.getABHANumber().replace("-", "")));
            careContextLinkingRequestModel.setPatient(patients);
            HealthCheckup.abhaClient.linkCareContext("Bearer " + accessToken, timeStamp, requestId, BuildConfig.HIPID, BuildConfig.CMID.replace("@", ""), getLinkTokenCallbackResponseModel.getOutput().get(0).getLinkToken()
                    , careContextLinkingRequestModel).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pd.dismiss();
                    try {
                        if (response.code() == 202) {
                            contextNotify();
                        } else {
                            Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
                        }
                    } catch (Exception e) {
                        Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                    Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pd.dismiss();
            Utilities.showAlertDialog(context, "Unable to get link token callback", "Retry", false, "Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    getLinkTokenCallback(patientDetails.getABHAAddress());

                }
            });

        }
    }

    void contextNotify() {
        String cmId = BuildConfig.CMID.replace("@", "");
        String abhaAddress = patientDetails.getABHAAddress().replace("\r\n", "");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
        String requestId = UUID.randomUUID().toString();
        String timeStamp = Utilities.getCurrentTimeStamp();

        com.myhindlab.abkat.abha.models.context_notify.Patient patient = new com.myhindlab.abkat.abha.models.context_notify.Patient();
        patient.setId(abhaAddress);
        CareContext careContext = new CareContext();
        careContext.setPatientReference(edt_barcode1.getText().toString());
        careContext.setCareContextReference(edt_barcode1.getText().toString());

        com.myhindlab.abkat.abha.models.context_notify.Notification notification = new com.myhindlab.abkat.abha.models.context_notify.Notification();
        notification.setPatient(patient);
        notification.setCareContext(careContext);
        notification.setDate(Utilities.getCurrentTimeStamp());

        ArrayList<String> hiTypes = new ArrayList<>();
        hiTypes.add("DiagnosticReport");
        notification.setHiTypes(hiTypes);

        com.myhindlab.abkat.abha.models.context_notify.Hip hip = new com.myhindlab.abkat.abha.models.context_notify.Hip(BuildConfig.HIPID);
        notification.setHip(hip);

        CareContextNotifyRequestModel careContextNotifyRequestModel = new CareContextNotifyRequestModel();
        careContextNotifyRequestModel.setNotification(notification);

        HealthCheckup.abhaClient.contextNotify("Bearer " + accessToken, timeStamp, requestId, BuildConfig.HIPID, cmId, careContextNotifyRequestModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    if (response.code() == 202) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Sample collection details submitted successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });
    }


    void smsNotify() {
        String cmId = BuildConfig.CMID.replace("@", "");
        String abhaAddress = patientDetails.getABHAAddress().replace("\r\n", "");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
        String requestId = UUID.randomUUID().toString();
        String timeStamp = Utilities.getCurrentTimeStamp();


        SMSNotifyRequestModel smsNotifyRequestModel = new SMSNotifyRequestModel();
        smsNotifyRequestModel.setRequestId(requestId);
        smsNotifyRequestModel.setTimestamp(timeStamp);
        Hip hip = new Hip();
        hip.setId(BuildConfig.HIPID);
        hip.setName(BuildConfig.HIPName);
        Notification notification = new Notification("+91-" + patientDetails.getMobileNo(), hip);
        smsNotifyRequestModel.setNotification(notification);

        HealthCheckup.abhaClient.smsNotify("Bearer " + accessToken, timeStamp, requestId, BuildConfig.HIPID, cmId, smsNotifyRequestModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    if (response.code() == 202) {
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Sample collection details submitted successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Sample collection details submitted successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });
    }


    private void getAPIToken() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface api = ApiClient.getDishaAPI().create(ApiInterface.class);

//        // Create JSON body
//        JsonObject jsonBody = new JsonObject();
//        jsonBody.addProperty("username", "c2c_tn");
//        jsonBody.addProperty("password", "c2c_tn");

        // Call API
        Call<ResponseBody> call = api.getDishaAPIToken("Suvarna", "Suvarna@123");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {

                        String responseString = response.body().string();
                        Log.d("API_RESPONSE", responseString);

                        JSONObject jsonObject = new JSONObject(responseString);

                        idToken = jsonObject.optString("response");

                        if (!idToken.isEmpty()) {
                            Log.d("API_TOKEN", idToken);

                            getListDishaAPI();

                        } else {
                            Utilities.showAlertDialog(context, "Failed", "Token not found", true);
                        }

                    } else {
                        Log.e("API_ERROR", "Response failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Parsing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
    }

    private void getListDishaAPI() {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getDishaAPI().create(ApiInterface.class);
        Call<CustomerTestResponseModel> call =
                apiService.getTestListDisha("Bearer " + idToken, "NHO");
        call.enqueue(new Callback<CustomerTestResponseModel>() {
            @Override
            public void onResponse(Call<CustomerTestResponseModel> call, Response<CustomerTestResponseModel> response) {

                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {

                    cusomerId = response.body().getCustomerId();

                    testList = response.body().getListTestDetailsDto();
                }
            }

            @Override
            public void onFailure(Call<CustomerTestResponseModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }



    void insertDisha() {

        String formatedDate = edt_sample_collection_date.getText().toString().trim();

        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();

        JsonObject mainObj = new JsonObject();

        mainObj.addProperty("patientId", 0);
        mainObj.addProperty("title", title);
        mainObj.addProperty("fname", firstName);
        mainObj.addProperty("mname", middleName);
        mainObj.addProperty("lname", lastName);
        mainObj.addProperty("gender", patientDetails.getGender());
        mainObj.addProperty("mobile", patientDetails.getMobileNo());
        mainObj.addProperty("ageYears", String.valueOf(years));
        mainObj.addProperty("ageMonth", String.valueOf(months));
        mainObj.addProperty("ageDay", String.valueOf(days));
        mainObj.addProperty("address", patientDetails.getCurrentAddress());
        mainObj.addProperty("email", emailId);
        mainObj.addProperty("unitId", "38");
        mainObj.addProperty("createdBy", userID);
        mainObj.addProperty("weight", patientDetails.getWeightKGs());
        mainObj.addProperty("height", patientDetails.getHeightCMs());
        mainObj.addProperty("collectedDate", formatedDate);
        mainObj.addProperty("collectedTime",
                edt_sample_collection_time.getText().toString().trim());
        mainObj.addProperty("customerId", cusomerId);
        mainObj.addProperty("visitCode",
                String.valueOf(patientDetails.getRegdId()));
        mainObj.addProperty("campId",
                String.valueOf(patientDetails.getCampId()));
        mainObj.addProperty("hmisPatientId",
                patientDetails.getScreeningPatientID());
        mainObj.addProperty("totalAmount", "0");

        // ✅ Add listTestDetails correctly
        mainObj.add("listTestDetails", prepareListTestDetailsJson());

        ApiInterface apiInterface = ApiClient.getDishaAPI().create(ApiInterface.class);

        apiInterface.InsertDishaAPI("Bearer " + idToken, mainObj)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        pd.dismiss();

                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                Log.i("API_RESPONSE", result);

                                if (result != null && !result.equalsIgnoreCase("[]")) {

                                    JSONObject obj = new JSONObject(result);
                                    String status = obj.getString("status");
                                    String message = obj.getString("message");

                                    if (status.equalsIgnoreCase("Success")) {

                                        LocalBroadcastManager.getInstance(context)
                                                .sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

                                        new AlertDialog.Builder(context)
                                                .setIcon(R.drawable.icon_success)
                                                .setTitle("Success")
                                                .setMessage("Sample collection submitted successfully")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", (d, w) -> finish())
                                                .show();

                                    } else {
                                        Utilities.showAlertDialog(context, status, message, false);
                                    }

                                } else {
                                    Utilities.showAlertDialog(context,
                                            "Error", "Server not responding", false);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Utilities.showAlertDialog(context,
                                        "Error", e.getMessage(), false);
                            }

                        } else {
                            try {
                                Utilities.showAlertDialog(context,
                                        "Failure", response.errorBody().string(), false);
                            } catch (IOException e) {
                                e.printStackTrace();
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
//    void insertDisha(String listTestDetailsJson) {
//
//
//        String formatedDate = Utilities.changeDateFormat("dd/MM/yyyy", "yyyy-MM-dd", edt_sample_collection_date.getText().toString());
//
//
//        JsonObject mainObj = new JsonObject();
//
//        mainObj.addProperty("patientId", 1526);
//        mainObj.addProperty("title", "MR.");
//        mainObj.addProperty("fname", "Test");
//        mainObj.addProperty("mname", "For");
//        mainObj.addProperty("lname", "API");
//        mainObj.addProperty("gender", "Male");
//        mainObj.addProperty("mobile", "9130492931");
//        mainObj.addProperty("ageYears", "28");
//        mainObj.addProperty("ageMonth", "03");
//        mainObj.addProperty("ageDay", "22");
//        mainObj.addProperty("address", "Pune");
//        mainObj.addProperty("email", "test@test.com");
//
//// Array
//        JsonArray testArray = new JsonArray();
//
//        JsonObject testObj = new JsonObject();
//        testObj.addProperty("subServiceId", 53);
//        testObj.addProperty("amount", 0);
//        testObj.addProperty("subServiceName", "Lipid Profile");
//        testObj.addProperty("sampleTypeId", 1);
//        testObj.addProperty("barCode", "LC001654736754");
//        testObj.addProperty("quantity", 1);
//        testObj.addProperty("templateWise", "N");
//
//        testArray.add(testObj);
//
//        mainObj.add("listTestDetails", testArray);
//
//
//
//        pd.setMessage("Please wait . . . ");
//        pd.setCancelable(false);
//        pd.show();
//
//        ApiInterface apiInterface = ApiClient.getDishaAPI().create(ApiInterface.class);
//        apiInterface.InsertDishaAPI("Bearer " + idToken, 0, title, firstName, middleName, lastName,
//                patientDetails.getGender(), patientDetails.getMobileNo(), String.valueOf(years), String.valueOf(months), String.valueOf(days), patientDetails.getCurrentAddress(), emailId,
//                "38", userID, String.valueOf(patientDetails.getWeightKGs()), String.valueOf(patientDetails.getHeightCMs()), formatedDate, edt_sample_collection_time.getText().toString().trim(), cusomerId, String.valueOf(patientDetails.getRegdId()), String.valueOf(patientDetails.getCampId()), patientDetails.getScreeningPatientID(), "0", listTestDetailsJson).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                pd.dismiss();
//                if (response.isSuccessful()) {
//                    try {
//                        pd.dismiss();
//                        String result = response.body().string();
//                        Log.i("TAG", "onPostExecute: " + result);
//
//                        if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                            JSONObject obj = new JSONObject(result);
//                            String status = obj.getString("status");
//                            String message = obj.getString("message");
//                            if (status.equalsIgnoreCase("Success")) {
//
//                                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
//
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                builder.setIcon(R.drawable.icon_success);
//                                builder.setTitle("Success");
//                                builder.setCancelable(false);
//                                builder.setMessage("Sample collection details submitted successfully");
//                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        finish();
//                                    }
//                                });
//                                builder.show();
//
//
//                            } else {
//                                Utilities.showAlertDialog(context, status, message, false);
//                            }
//                        } else
//                            Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    try {
//                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                pd.dismiss();
//
//                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);
//
//            }
//        });
//    }

}
