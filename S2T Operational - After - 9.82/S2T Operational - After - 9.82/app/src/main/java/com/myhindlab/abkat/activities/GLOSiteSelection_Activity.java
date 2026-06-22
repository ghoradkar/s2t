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
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.ConstructionSiteDataAdapter;
import com.myhindlab.abkat.models.ConstructionSitesList_Model;
import com.myhindlab.abkat.pojos.ConstructionSitesList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.myhindlab.abkat.utilities.Utilities.isLocationEnabled;
import static com.myhindlab.abkat.utilities.Utilities.provideCameraAndStorageAccess;
import static com.myhindlab.abkat.utilities.Utilities.provideLocationAccess;
import static com.myhindlab.abkat.utilities.Utilities.turnOnLocation;

public class GLOSiteSelection_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private LinearLayout ll_currentlayout;
    private EditText edt_locationaddress, edt_searchorselectdistrict;
    private LinearLayout ll_sitedetailslist;
    private SearchView searchview_pincode, searchview_sitename;
    private RecyclerView recycler_view_sitesurveyList;

    private ArrayList<ConstructionSitesList_Model> siteList;
    private ArrayList<ConstructionSitesList_Model> searchSiteList;
    private String userID = "", searchDTLGDCODE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glosite_selection);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        ll_currentlayout = findViewById(R.id.ll_currentlayout);
        edt_locationaddress = findViewById(R.id.edt_locationaddress);
        searchview_pincode = findViewById(R.id.searchview_pincode);

        ll_sitedetailslist = findViewById(R.id.ll_sitedetailslist);
        searchview_sitename = findViewById(R.id.searchview_sitename);
        recycler_view_sitesurveyList = findViewById(R.id.recycler_view_sitesurveyList);
    }

    private void setDefaults() {
        context = GLOSiteSelection_Activity.this;
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

        ll_sitedetailslist.setVisibility(View.GONE);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        searchview_pincode.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() == 6) {
                    if (!Utilities.isNetworkAvailable(context)) {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        return true;
                    }

                    new GetListOfConstructionSite().execute(query);

                } else {
                    Utilities.showToastMessage("Invalid PIN Code", context, false);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() == 6) {
                    if (!Utilities.isNetworkAvailable(context)) {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        return true;
                    }

                    new GetListOfConstructionSite().execute(newText);
                }
                return true;
            }
        });

        searchview_sitename.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSiteList = new ArrayList<>();

                if (siteList != null) {
                    if (siteList.size() > 0) {
                        for (ConstructionSitesList_Model pojo : siteList) {
                            String siteDetails = pojo.getSiteName() + pojo.getPinCode();
                            if (siteDetails != null && siteDetails.toLowerCase().contains(query.toLowerCase())) {
                                searchSiteList.add(pojo);
                            }
                        }

                        if (searchSiteList.size() == 0) {
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);
                            searchSiteList.addAll(siteList);
                            showSiteListDialog(searchSiteList);
                        } else {
                            showSiteListDialog(searchSiteList);
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSiteList = new ArrayList<>();

                if (siteList != null) {
                    if (newText.equals("")) {
                        searchSiteList.addAll(siteList);
                        showSiteListDialog(searchSiteList);
                    } else {
                        if (siteList.size() > 0) {
                            for (ConstructionSitesList_Model pojo : siteList) {
                                String siteDetails = pojo.getSiteName() + pojo.getPinCode();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(newText.toLowerCase())) {
                                    searchSiteList.add(pojo);
                                }
                            }

                            if (searchSiteList.size() == 0) {
                                searchSiteList.addAll(siteList);
                                showSiteListDialog(searchSiteList);
                            } else {
                                showSiteListDialog(searchSiteList);
                            }
                        }
                    }
                }
                return true;
            }
        });

        recycler_view_sitesurveyList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                ConstructionSitesList_Model siteDetails = new ConstructionSitesList_Model();
                                siteDetails = searchSiteList.get(position);
                                siteDetails.setUserID(userID);

                                startActivity(new Intent(context, UnregisteredRegisteredWorkerList_Activity.class)
                                        .putExtra("SiteDetailId", siteDetails.getSiteDetailId()));
                            }
                        }));
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Site");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class GetListOfConstructionSite extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", "0"));
            param.add(new ParamsPojo("PinCode", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetListOfConstructionSite, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    ConstructionSitesList_Pojo pojoDetails = new Gson().fromJson(result, ConstructionSitesList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        searchview_pincode.clearFocus();

                        siteList = new ArrayList<>();
                        siteList = pojoDetails.getOutput();

                        searchSiteList = new ArrayList<>();
                        searchSiteList.addAll(siteList);

                        if (siteList.size() > 0) {
                            showSiteListDialog(searchSiteList);
                        } else {
                            ll_sitedetailslist.setVisibility(View.GONE);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        ll_sitedetailslist.setVisibility(View.GONE);
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    ll_sitedetailslist.setVisibility(View.GONE);
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ll_sitedetailslist.setVisibility(View.GONE);
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void showSiteListDialog(ArrayList<ConstructionSitesList_Model> siteList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler_view_sitesurveyList.setLayoutManager(layoutManager);
        ll_sitedetailslist.setVisibility(View.VISIBLE);
        recycler_view_sitesurveyList.setAdapter(new ConstructionSiteDataAdapter(siteList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = GLOSiteSelection_Activity.this;
        startLocationUpdates();
        searchview_pincode.clearFocus();
        searchview_sitename.clearFocus();

        if (Utilities.isNetworkAvailable(context)) {
            String PINCode = searchview_pincode.getQuery().toString().trim();
            if (PINCode.length() == 6) {
                new GetListOfConstructionSite().execute(PINCode);
            }
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
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
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (searchview_pincode.getQuery().toString().trim().length() != 6) {
            new GetAddress().execute();
        }
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

                if (result != null) {
                    ll_currentlayout.setVisibility(View.VISIBLE);

                    String address = result.getAddressLine(0);
                    String pincode = result.getPostalCode();

                    edt_locationaddress.setText(address);
                    searchview_pincode.setQuery(pincode, false);

//                    if (Utilities.isNetworkAvailable(context)) {
//                        new GetListOfConstructionSite().execute(pincode);
//                    } else {
//                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                    }
                } else {
                    ll_currentlayout.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        context = null;
    }
}


