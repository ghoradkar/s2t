package com.myhindlab.abkat.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.ConstructionSitesList_Model;
import com.myhindlab.abkat.models.InsertConstructionSiteDetails_Model;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.myhindlab.abkat.utilities.Utilities.isLocationEnabled;
import static com.myhindlab.abkat.utilities.Utilities.provideCameraAndStorageAccess;
import static com.myhindlab.abkat.utilities.Utilities.provideLocationAccess;
import static com.myhindlab.abkat.utilities.Utilities.turnOnLocation;

public class SiteSurvey_RERASiteDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private TextView tv_constructiontitle;
    private EditText edt_reranumber, edt_sitename, edt_siteaddress,
            edt_pincode, edt_city, edt_latlong, edt_locationaddress, edt_locationpincode;
    private Button btn_proceed, btn_next;

    private ConstructionSitesList_Model siteDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitesurvey_rerasitedetails);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        tv_constructiontitle = findViewById(R.id.tv_constructiontitle);

        edt_reranumber = findViewById(R.id.edt_reranumber);
        edt_sitename = findViewById(R.id.edt_sitename);
        edt_siteaddress = findViewById(R.id.edt_siteaddress);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_city = findViewById(R.id.edt_city);

        edt_latlong = findViewById(R.id.edt_latlong);
        edt_locationaddress = findViewById(R.id.edt_locationaddress);
        edt_locationpincode = findViewById(R.id.edt_locationpincode);

        btn_proceed = findViewById(R.id.btn_proceed);
        btn_next = findViewById(R.id.btn_next);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Site Survey");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        context = SiteSurvey_RERASiteDetails_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
            provideLocationAccess(context);
        } else {
            if (!isLocationEnabled(context)) {
                turnOnLocation(context);
            } else {
                startLocationUpdates();
            }
        }

        siteDetails = new ConstructionSitesList_Model();
        siteDetails = (ConstructionSitesList_Model) getIntent().getSerializableExtra("siteDetails");

        String reraNumber = siteDetails.getReraId();
        String siteName = siteDetails.getSiteName();
        String siteAddress = siteDetails.getSiteAddress();
        String sitePINCode = siteDetails.getPinCode();
        String siteCity = siteDetails.getCity();

        edt_reranumber.setText(reraNumber);
        edt_sitename.setText(siteName);
        edt_siteaddress.setText(siteAddress == null ? "" : siteAddress);
        edt_pincode.setText(sitePINCode == null ? "" : sitePINCode);
        edt_city.setText(siteCity == null ? "" : siteCity);

        if (Integer.parseInt(siteDetails.getSiteDetailId()) > 0) {
            tv_constructiontitle.setText("Construction Site Details (Provided by Surveyor)");
            edt_sitename.setClickable(false);
            edt_siteaddress.setClickable(false);
            edt_pincode.setClickable(false);
            edt_city.setClickable(false);

            edt_sitename.setFocusable(false);
            edt_siteaddress.setFocusable(false);
            edt_pincode.setFocusable(false);
            edt_city.setFocusable(false);

            btn_next.setVisibility(View.VISIBLE);
            btn_proceed.setVisibility(View.GONE);

        } else {
            tv_constructiontitle.setText("Construction Site Details (Provided by RERA Data)");
            edt_sitename.setClickable(true);
            edt_siteaddress.setClickable(true);
            edt_pincode.setClickable(true);
            edt_city.setClickable(true);

            btn_next.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.VISIBLE);
        }
    }

    private void setEventHandler() {
        btn_proceed.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceed:
                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }

                if (latLng == null) {
                    edt_latlong.setError("Please wait, We are getting your current location");
                    edt_latlong.requestFocus();
                    Utilities.showToastMessage("Please wait, We are getting your current location",
                            context, false);
                    return;
                }

                if (edt_siteaddress.getText().toString().trim().isEmpty()) {
                    edt_siteaddress.setError("Required Address");
                    edt_siteaddress.requestFocus();
                    return;
                }

                if (edt_pincode.getText().toString().trim().isEmpty()) {
                    edt_pincode.setError("Required PIN Code");
                    edt_pincode.requestFocus();
                    return;
                }

                if (edt_pincode.getText().toString().trim().length() < 6) {
                    edt_pincode.setError("Invalid PIN Code");
                    edt_pincode.requestFocus();
                    return;
                }

                if (edt_city.getText().toString().trim().isEmpty()) {
                    edt_city.setError("Required City");
                    edt_city.requestFocus();
                    return;
                }

                insertAPICall();
                break;

            case R.id.btn_next:
                startActivity(new Intent(context, SiteSurvey_RERASiteBuilderDetails_Activity.class)
                        .putExtra("siteDetails", siteDetails));
                finish();
                break;

            default:
                break;
        }
    }

    private void insertAPICall() {
        ConstantData constantData = ConstantData.getInstance();

        siteDetails.setSitetypeid(constantData.getSetSitetypeid());
        siteDetails.setSiteAddress(edt_siteaddress.getText().toString().trim());
        siteDetails.setPinCode(edt_pincode.getText().toString().trim());
        siteDetails.setCity(edt_city.getText().toString().trim());
        siteDetails.setCurrentLatitude(String.valueOf(latLng.latitude));
        siteDetails.setCurrentLongitude(String.valueOf(latLng.longitude));

        InsertConstructionSiteDetails_Model data = new InsertConstructionSiteDetails_Model();
        data.setSitetypeid(siteDetails.getSitetypeid());

        data.setDISTLGDCODE(siteDetails.getDISTLGDCODE());
        data.setTALLGDCODE(siteDetails.getTALLGDCODE());
        data.setArea(siteDetails.getSiteAddress());
        data.setSiteName(siteDetails.getSiteName());
        data.setSiteAddress(siteDetails.getSiteAddress());
        data.setPinCode(siteDetails.getPinCode());
        data.setCity(siteDetails.getCity());
        data.setLatitude(siteDetails.getCurrentLatitude());
        data.setLongitude(siteDetails.getCurrentLongitude());

        data.setCreatedBy(siteDetails.getUserID());
        data.setReraId(siteDetails.getReraId());
        data.setReraNo(siteDetails.getReraNo());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String inputString = gson.toJson(data);

        new InsertConstructionSiteDetails().execute(inputString);
    }

    public class InsertConstructionSiteDetails extends AsyncTask<String, Integer, String> {

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
            param.add(new ParamsPojo("ConstructionSiteJSON", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.InsertConstructionSiteDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                try {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        if (!message.equalsIgnoreCase("")) {
                            Utilities.showToastMessage("Data save successfully", context, true);
                            siteDetails.setSiteDetailId(message);
                            startActivity(new Intent(context, SiteSurvey_RERASiteBuilderDetails_Activity.class)
                                    .putExtra("siteDetails", siteDetails));
                            finish();
                        } else {
                            Utilities.showAlertDialog(context, "Fail",
                                    "Failed to get site details. Please try again after some time.",
                                    false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
                }
            } else {
                Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = SiteSurvey_RERASiteDetails_Activity.this;
        startLocationUpdates();
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

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 30 * 1000;  /* 30 secs */
    private long FASTEST_INTERVAL = 30000; /* 30 sec */
    private LatLng latLng = null;

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

        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, new LocationCallback() {
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
//        String msg = "Updated Location: " +
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        edt_latlong.setHint("Current location *");
        edt_latlong.setText(location.getLatitude() + ", " + location.getLongitude());

        if (edt_siteaddress.getText().toString().trim().length() <= 0 ||
                edt_pincode.getText().toString().trim().length() <= 0 ||
                edt_city.getText().toString().trim().length() <= 0) {
            new GetAddress().execute();
        }
//
//        try {
//            Geocoder geoCoder = new Geocoder(context);
//            List<Address> matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
//
//            if (bestMatch != null) {
//                String address = bestMatch.getAddressLine(0);
//                String pincode = bestMatch.getPostalCode();
//                String city = bestMatch.getSubAdminArea();
//
//                if (Integer.parseInt(siteDetails.getSiteDetailId()) <= 0) {
//                    edt_siteaddress.setText(address);
//                    edt_pincode.setText(pincode);
//                    edt_city.setText(city);
//                }
//
//                edt_locationaddress.setVisibility(View.VISIBLE);
//                edt_locationpincode.setVisibility(View.VISIBLE);
//                edt_locationaddress.setText(address);
//                edt_locationpincode.setText(pincode);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private class GetAddress extends AsyncTask<String, String, Address> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (context != null) {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Please Wait . . .");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }

        @Override
        protected Address doInBackground(String... params) {
            Address bestMatch = null;
            try {
                Geocoder geoCoder = new Geocoder(context);
                List<Address> matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                bestMatch = (matches.isEmpty() ? null : matches.get(0));

                if (bestMatch != null) {
                    return bestMatch;
                } else {
                    return bestMatch;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return bestMatch;
            }
        }

        @Override
        protected void onPostExecute(Address result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (context != null) {
                pDialog.dismiss();
            }

            if (result != null) {
                String address = result.getAddressLine(0);
                String pincode = result.getPostalCode();
                String city = result.getSubAdminArea();

//                if (Integer.parseInt(siteDetails.getSiteDetailId()) <= 0) {
//                    edt_siteaddress.setText(address);
//                    edt_pincode.setText(pincode);
//                    edt_city.setText(city);
//                }

                if (Integer.parseInt(siteDetails.getSiteDetailId()) <= 0) {
                    if (edt_siteaddress.getText().toString().trim().length() <= 0
                            && edt_pincode.getText().toString().trim().length() <= 0
                            && edt_city.getText().toString().trim().length() <= 0) {
                        edt_siteaddress.setText(address);
                        edt_pincode.setText(pincode);
                        edt_city.setText(city);
                    }
                }

                edt_locationaddress.setVisibility(View.VISIBLE);
                edt_locationpincode.setVisibility(View.VISIBLE);
                edt_locationaddress.setText(address);
                edt_locationpincode.setText(pincode);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        context = null;
    }
}
