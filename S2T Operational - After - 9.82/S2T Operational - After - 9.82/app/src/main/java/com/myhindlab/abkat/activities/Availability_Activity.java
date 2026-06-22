package com.myhindlab.abkat.activities;

import static com.myhindlab.abkat.utilities.PermissionUtil.PERMISSION_ALL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.AvailabilityAdapter;
import com.myhindlab.abkat.adapters.WeekAdapter;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.GPSTracker;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@SuppressLint("LongLogTag")
public class Availability_Activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Context context;
    private UserSessionManager session;
    private JSONArray user_info;
    private GPSTracker gps;

    private GridView week_grid, calender_grid;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView title, previous, next;
    private TextView colourDis, ViewOnMap;

    private Calendar month, year;
    private AvailabilityAdapter caladpt;
    private WeekAdapter weekadpt;
    private Handler handler;
    private String EmpCode, DesignationID, currentDate, Name, projectid = "", suborgid = "";
    private double latitude = 0.00, longitude = 0.00;
    private ArrayList<String> UserAttendanceitems;
    String[] weekday = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    String isinout = "";
    private boolean isCelDataAvailable = false;
    private String CurrentDate, PastDate;

    //_____________________________________________________
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    private FusedLocationProviderClient fusedLocationProviderClient;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}; // List of permissions required

    private String sessionLat = "", sessionLong = "", userID = "";
    int DESGID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        init();
        setDefaults();
        setUpToolBar();
    }

    private void init() {
        context = Availability_Activity.this;
        session = new UserSessionManager(context);
        gps = new GPSTracker(context);

        Calendar cal = Calendar.getInstance();
        CurrentDate = (Utilities.dfDate2).format(cal.getTime());
        month = Calendar.getInstance();
        year = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        PastDate = (Utilities.dfDate2).format(cal.getTime());

        UserAttendanceitems = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
//            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null);

        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    mLastLocation = task.getResult();
                    Calenderbind();
                }
//                else {
//                    Utilities.showAlertDialog(context, "Unable to get user location", "Please check Location Permission is given", false);
//                }
            }
        });
        // First we need to check availability of play services
//        if (mLastLocation != null) {
//
//        } else {
//
//        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        week_grid = (GridView) findViewById(R.id.gdv_week);
        calender_grid = (GridView) findViewById(R.id.gdv_cal);

        title = findViewById(R.id.title);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        colourDis = findViewById(R.id.text_colordescrip);
        ViewOnMap = findViewById(R.id.text_viewonmap);
        ViewOnMap.setVisibility(View.GONE);

        try {
            user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));

            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
//                EmpCode="1";
                EmpCode = json.getString("EmpCode");
                DesignationID = json.getString("DESGID");
                Name = json.getString("name").trim();
//                projectid="4";
//                suborgid="1";
                projectid = json.getString("ProjectId").trim();
                suborgid = json.getString("SubOrgId").trim();
                sessionLat = json.getString("Latitude").trim();
                sessionLong = json.getString("Langitude").trim();
//                sessionLat = "18.5123781";
//                sessionLong = "73.9229391";
                DESGID = json.getInt("DESGID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressDialog pD = new ProgressDialog(context);
        pD.setMessage("Please wait ...");
        pD.setCancelable(false);
        pD.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    pD.dismiss();
                    Calenderbind();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1 * 5000);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    private void refreshItems() {
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        if (Utilities.isNetworkAvailable(context)) {
//            getLatLong();
            Calenderbind();
            mSwipeRefreshLayout.setRefreshing(false);
        } else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
    }

    private void Calenderbind() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        currentDate = df.format(c.getTime());

        month = Calendar.getInstance();

        String[] monthYear = (android.text.format.DateFormat.format("MM yyyy", month))
                .toString().split(" ");
        try {
            if (displayLocation()) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetAttendanceDate().execute(EmpCode, monthYear[1], monthYear[0], projectid, suborgid);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                    GPSsetting();
                else if (doesAppNeedPermissions()) {
                    askPermission();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDefaults() {
        caladpt = new AvailabilityAdapter(context, month);
        weekadpt = new WeekAdapter(context, weekday);
        week_grid.setAdapter(weekadpt);
        calender_grid.setAdapter(caladpt);
        handler = new Handler();
        handler.post(calendarUpdater);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    private void setEventHandlers() {
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        colourDis.setOnClickListener(this);
        ViewOnMap.setOnClickListener(this);

        calender_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView date = (TextView) v.findViewById(R.id.date);
                if (date instanceof TextView && !date.getText().equals("")) {

//                    Calendar month1 = Calendar.getInstance();
//                    SimpleDateFormat timeformat = new SimpleDateFormat("HH");
//                    int time1 = Integer.parseInt(timeformat.format(month1.getTime()));
//
//                    if (time1 > 10.15) {
//                        Utilities.showAlertDialog(context, "Alert", "You cannot mark availability after 10:15 am", false);
//                        return;
//                    }
                    String day = date.getText().toString();
                    if (day.length() == 1) {
                        day = "0" + day;
                    }

                    if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != Integer.parseInt(day)) {
                        return;
                    }

                    String[] monthYear = (android.text.format.DateFormat.format("MM yyyy", month))
                            .toString().split(" ");
                    String dateselected = monthYear[1] + "/" + monthYear[0] + "/" + day;            //yyyy/MM/dd

                    UserAttendanceitems = Utilities.roundOffValuesSizeTwo(UserAttendanceitems);
                    if (UserAttendanceitems.contains(day))
                        Utilities.showAlertDialog(context, "Alert",
                                "You have already marked your availability.", true);
                    else {
                        new InsertUserAttendance().execute(EmpCode, String.valueOf(latitude), String.valueOf(longitude), EmpCode, dateselected, "0");
                    }


                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR) - 1), month.getActualMaximum(Calendar.MONTH), 1);
                } else {
                    month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
                }
                Calenderbind();
                refreshCalendar();
                break;

            case R.id.next:
                if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR) + 1), month.getActualMinimum(Calendar.MONTH), 1);
                } else {
                    month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
                }
                refreshCalendar();
                break;

            case R.id.text_colordescrip:
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.prompt_availabilitycolordescription, null);
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog
                        .Builder(context);
                alertDialogBuilder.setView(promptView);
                alertDialogBuilder.setTitle("Color description");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int view) {
                                dialog.dismiss();
                            }
                        });
                android.app.AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
                break;

            case R.id.text_viewonmap:
                if (latitude != 0.00 && longitude != 0.00) {
//                    Intent intent = new Intent(context,
//                            AvailabilityCurrentLocationOnMap_Activity.class);
//                    intent.putExtra("Name", Name);
//                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.icon_fail)
                            .setTitle("Alert")
                            .setMessage("Your current position is not getting, " +
                                    "Please provide appropriate permission or check your GPS settings.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
//                                    gps.showSettingsAlert();
//                                    startActivity(new Intent(Availability_Activity.this,
//                                            MenuActivity.class));
                                }
                            }).create().show();
                }
                break;

            default:
                break;
        }
    }

    public void refreshCalendar() {
        caladpt.refreshDays();
        String[] monthYear = (android.text.format.DateFormat.format("MM yyyy", month))
                .toString().split(" ");

        if (Utilities.isNetworkAvailable(context))
//            Log.e("MSG","Internet Connection");
            new GetAttendanceDate().execute(EmpCode, monthYear[0], monthYear[1], projectid, suborgid);
        else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);

        caladpt.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some random calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {
        @Override
        public void run() {
            caladpt.setItems(UserAttendanceitems);
            caladpt.notifyDataSetChanged();
        }
    };

    public class GetAttendanceDate extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog != null) {
                dialog = null;
            }
            dialog = new ProgressDialog(context);
            dialog.setMessage(" please wait...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
//            String res = "[]";
//            res = WebServiceCall.GetUserAttendanceDays(params[0], params[1], params[2], params[3], params[4]);
//            return res;

            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("USERID", params[0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("Month", params[2]));
            param.add(new ParamsPojo("ProjectId", params[3]));
            param.add(new ParamsPojo("SubOrgId", params[4]));
            res = WebServiceCall.APICall(ApplicationConstants.GetuserAttendance, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    int c = 0;

                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");

                    if (status.equalsIgnoreCase("success")) {
                        isCelDataAvailable = true;
                        JSONArray jsonarr = obj1.getJSONArray("output");
                        if (jsonarr.length() > 0) {

                            for (int i = 0; i < jsonarr.length(); i++) {
                                JSONObject jsonObj = jsonarr.getJSONObject(i);

                                switch (jsonObj.getString("DAYTYPE")) {
                                    case "1":
                                        UserAttendanceitems.add(jsonObj.getString("Day"));
                                        break;
                                }
                            }
                        }
                    } else
                        isCelDataAvailable = false;
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            setDefaults();
            setEventHandlers();
            //checkRegWorkLoc();
        }
    }

    public class InsertUserAttendance extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog != null) {
                dialog = null;
            }
            dialog = new ProgressDialog(context);
            dialog.setMessage(" please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";

            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Userid", params[0]));
            param.add(new ParamsPojo("LATITUDE", params[1]));
            param.add(new ParamsPojo("LONGITUDE", params[2]));
            param.add(new ParamsPojo("ATTENDANCEMARKBY", params[3]));
            param.add(new ParamsPojo("ATTENDANCEDATE", params[4]));
            param.add(new ParamsPojo("MACID", params[5]));

            res = WebServiceCall.APICall(ApplicationConstants.InsertUserAttendance, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try {
                dialog.dismiss();
                if (!file_url.equals("")) {

                    JSONObject mainObject = new JSONObject(file_url);
                    String status = mainObject.getString("status");
                    String message = mainObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        onItemsLoadComplete();
                        Utilities.showAlertDialog(context, status, message, true);
                    } else if (status.equalsIgnoreCase("fail")) {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                    setDefaults();
                    setEventHandlers();
                } else
                    Utilities.showAlertDialog(context, "Server not connected", "Please try after some time", false);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        try {
//            if (checkPlayServices()) {
//                // Building the GoogleApi client
//                buildGoogleApiClient();
//                createLocationRequest();
//            }
//
//            // Resuming the periodic location updates
//            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//                startLocationUpdates();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (Utilities.isMockSettingsON(context))
            creatAlertMessage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        try {
//            stopLocationUpdates();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Method to display the location on UI
     */
    private boolean displayLocation() {
//        try {
//            mLastLocation = LocationServices.FusedLocationApi
//                    .getLastLocation(mGoogleApiClient);
//        } catch (SecurityException se) {
//            se.printStackTrace();
//        }

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            return true;

        } else {
            return false;
        }

    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            // Starting the location updates
            startLocationUpdates();
            Log.d("AVAAVAAVAAVAAVAAVAAVAAVAAVAAVA", "Periodic location updates started!");
        } else {
            mRequestingLocationUpdates = false;
            // Stopping the location updates
            stopLocationUpdates();
            Log.d("AVAAVAAVAAVAAVAAVAAVAAVAAVAAVA", "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(Availability_Activity.this)
                    .addConnectionCallbacks(Availability_Activity.this)
                    .addOnConnectionFailedListener(Availability_Activity.this)
                    .addApi(LocationServices.API).build();
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creating location request object
     */
    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        try {
            int resultCode = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(Availability_Activity.this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, Availability_Activity.this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                                    "This device is not supported.", Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, Availability_Activity.this);
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, Availability_Activity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        try {
            Log.i("AVAAVAAVAAVAAVAAVAAVAAVAAVAAVA", "Connection failed: ConnectionResult.getErrorCode() = "
                    + result.getErrorCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        try {
            // Once connected with google api, get the location
            displayLocation();

            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            mLastLocation = location;
            // Displaying the new location on UI
            displayLocation();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assign the new location

    }

    private void creatAlertMessage() {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("You are marking availability by fake method, you will be marked in our list, " +
                "Please disable Allow mock locations from setting, and try again.");
        alertDialog.setIcon(R.drawable.icon_fail);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(
                        new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS), 0);
                UserSessionManager session = new UserSessionManager(context);
                session.setFakeLocationFlag(1);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static boolean doesAppNeedPermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public void askPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, PERMISSION_ALL);
            }
            return;
        } else {
            // Calenderbind();
            GPSsetting();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Do your work.

                    //  startService(new Intent(context, ChecklistSyncServiceHLL.class));
                    @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null);

                    locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                mLastLocation = task.getResult();
                                Calenderbind();
                            } else {
                                Utilities.showAlertDialog(context, "Unable to get user location", "Please check Location Permission is given", false);
                            }
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Alert");
                    builder.setMessage("Please provide accessing location");
                    //no button dialog
                    // Add the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            dialog.dismiss();

                            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null)));                                   //
                            finish();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }

        }
    }

    private void GPSsetting() {

        new AlertDialog.Builder(context)
                .setIcon(R.drawable.icon_fail)
                .setTitle("Alert")
                .setMessage("Please provide permission for accessing your location, " +
                        "Otherwise you can not use some functionality in the application.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);

                            // Setting Dialog Title
                            alertDialog.setTitle("GPS is settings");
                            alertDialog.setCancelable(false);
                            // Setting Dialog Message
                            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

                            // On pressing Settings button
                            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    context.startActivity(intent);
                                    finish();
                                    //  Calenderbind();
                                }
                            });

                            // on pressing cancel button
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(context, SiteSurvey_Menu_Activity.class);
                                    startActivity(i);
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        } else {

                        }
                    }


                }).create().show();

    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Attendance");
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
