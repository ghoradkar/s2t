package com.myhindlab.abkat.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.ScanList;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.models.ResultsModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.maniteja.com.synclib.helper.HelperC;
import org.maniteja.com.synclib.helper.Util;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;

public class HealthScreeningBloodSugarPP_Activity extends AppCompatActivity {
    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private MaterialEditText edt_beneficiaryname, edt_gender, edt_age, edt_blood_pp, edt_height, edt_weight;
    private Button btn_getfrom_syncdevice, btn_register;
    private PresentPatientList_Model patientDetails;
    private String userID, name, campId, healthScreentype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_bloodsugarpp);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = HealthScreeningBloodSugarPP_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_blood_pp = findViewById(R.id.edt_blood_pp);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        btn_getfrom_syncdevice = findViewById(R.id.btn_getfrom_syncdevice);
        btn_register = findViewById(R.id.btn_register);
    }

    private void setDefaults() {

        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");
        healthScreentype = getIntent().getStringExtra("healthScreentype");

        edt_beneficiaryname.setText(patientDetails.getEnglishName());
        edt_age.setText(patientDetails.getAge());
        edt_height.setText(String.valueOf(patientDetails.getHeightCMs()));
        edt_weight.setText(String.valueOf(patientDetails.getWeightKGs()));

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            edt_gender.setText("Male");
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            edt_gender.setText("Female");
        } else if (patientDetails.getGender().equalsIgnoreCase("O")) {
            edt_gender.setText("Other");
        }

    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                name = json.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        btn_getfrom_syncdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBluetoothSelectDeviceDialog();
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

    }

    private void submitData() {
        if (edt_blood_pp.getText().toString().trim().isEmpty()) {
            edt_blood_pp.setError("Please enter blood sugar (pp)");
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new UpdateBloodPP().execute(
                   String.valueOf(patientDetails.getRegdId()),
                    edt_blood_pp.getText().toString().trim()
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class UpdateBloodPP extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("BloodSugar_PP", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.UpdateBloodPP, ApplicationConstants.webservice, param);
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
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Blood Sugar (PP) details submitted successfully");
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

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Sugar (PP)");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ///////////////////////////////////////////////////////SYNC BLUETOOTH//////////////////////////////////////////////////////////////

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private final int REQUEST_ENABLE_BT = 1;
    private final long SCAN_PERIOD = 20000;
    List<ResultsModel> resultsListforAdapter = new ArrayList<>();
    private RecyclerView recyclerView;
    private ScanList adapter;
    Util util;
    Context c;
    Button btnScanList;
    String defname, defAddress;
    boolean autocoFlag = false;
    int flag = 2;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int SYCN_RESULT = 1002;
    TextView scaningtext;
    boolean isCampSelected = false;

    private void showBluetoothSelectDeviceDialog() {

        util = new Util(this, this);
        defname = util.readString(HelperC.key_autoconnecbtname, "");
        defAddress = util.readString(HelperC.key_autoconnectaddress, "");
        autocoFlag = util.readboolean(HelperC.key_autoconnectflag, false);
        mHandler = new Handler();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager;
        if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_layout_selectsyncdevice, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Select SYNC+ Device");
        alertDialogBuilder.setView(promptView);

        recyclerView = promptView.findViewById(R.id.resultsrecycler);
        btnScanList = promptView.findViewById(R.id.btnScan);

        scaningtext = promptView.findViewById(R.id.scaningtext);
        scaningtext.setText("Scaning Started");
        scanLeDevice(true);

        adapter = new ScanList(getApplicationContext(), resultsListforAdapter, true, 1, HealthScreeningBloodSugarPP_Activity.this);
        recyclerView.setAdapter(adapter);
        // adapter.setFilter(resultsListforAdapter);
        /* recyclerView.addItemDecoration(new DividerHelper(c.getResources(), c));*/
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getApplicationContext()); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final AlertDialog alertD = alertDialogBuilder.create();
        btnScanList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnScanList.getText().toString().equals("Scan")) {
                    btnScanList.setText("Stop Scan");
                    scaningtext.setText("Scaning Started");
                    scanLeDevice(true);
                } else {
                    btnScanList.setText("Scan");
                    scaningtext.setText("Scaning Stopped");
                    scanLeDevice(false);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @RequiresApi(api = JELLY_BEAN_MR2)
            @Override
            public void onItemClick(View view, int position) {
                try {

                    TextView name = (TextView) view.findViewById(R.id.btname);
                    final BluetoothDevice device = adapter.getDevice(position);
                    if (device == null) {
                        return;
                    }
                    util.putString(HelperC.key_autoconnecbtname, name.getText().toString());
                    util.putString(HelperC.key_autoconnectaddress, device.getAddress());
                    util.putBoolean(HelperC.key_autoconnectflag, true);
                    final Intent intent = new Intent(context, SYNCResult_Activity.class);
                    intent.putExtra(HelperC.EXTRAS_DEVICE_NAME, name.getText().toString());
                    intent.putExtra(HelperC.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                    if (mScanning) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mScanning = false;
                    }
                    startActivityForResult(intent, SYCN_RESULT);
                    alertD.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Please scan once again.", Toast.LENGTH_SHORT).show();
                }

            }
        }));
        alertD.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            Utilities.showToastMessage("Please turn on Bluetooth", context, false);
            return;
        }
        if (resultCode == RESULT_OK)
            if (requestCode == SYCN_RESULT) {
                String requiredValue = data.getStringExtra("Result");
                Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                edt_blood_pp.setText(requiredValue);
                edt_blood_pp.setFocusable(false);
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
                   /* IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    this.registerReceiver(mReceiver, filter);

                    // Register for broadcasts when discovery has finished
                    filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    this.registerReceiver(mReceiver, filter);*/
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    final BluetoothManager bluetoothManager;
//                    if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
//                        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//                        mBluetoothAdapter = bluetoothManager.getAdapter();
//                    }
//                    mScanning = false;
//                    if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                        scaningtext.setText("Scaning Stopped");
//                        btnScanList.setText("Scan");
//                    }
//                    invalidateOptionsMenu();
//                }
//            }, SCAN_PERIOD);
            final BluetoothManager bluetoothManager;
            if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
                bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = bluetoothManager.getAdapter();
            }
            mScanning = true;
            if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
                btnScanList.setText("Stop Scan");
            }
        } else {
            final BluetoothManager bluetoothManager;
            if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
                bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = bluetoothManager.getAdapter();
            }
            mScanning = false;
            if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                scaningtext.setText("Scaning Stopped");
                btnScanList.setText("Scan");
            }
        }
        invalidateOptionsMenu();
    }

    @RequiresApi(JELLY_BEAN_MR2)
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                              @RequiresApi(api = JELLY_BEAN_MR2)
                              @Override
                              public void run() {
                                  if (device != null) {
                                      Log.i("SCANNING ", util.readString(HelperC.key_devname, "Sync+"));
                                      System.out.println("SCANNING " + util.readString(HelperC.key_devname, "Sync+"));
                                      if ("Sync+".equals(device.getName()) || util.readString(HelperC.key_devname, "Sync+").equals(device.getName())) {
                                          System.out.println("Scan List : " + device.getAddress());
                                          if (!util.readString(HelperC.key_devname, "").equals("")) {
                                              System.out.println("Scan List : 1 " + device.getAddress());
                                              if (util.readString(HelperC.key_autoconnectaddress, "").equals(device.getAddress())) {
                                                  System.out.println("Scan List : 2 " + device.getAddress());
                                                  if (!util.readString(HelperC.key_devname, "").equals(device.getName())) {
                                                      System.out.println("Scan List : 3 " + device.getAddress());
                                                      addData(util.readString(HelperC.key_devname, ""), device.getAddress());
                                                  } else {
                                                      System.out.println("Scan List : 4 " + device.getAddress());
                                                      addData(device.getName(), device.getAddress());
                                                  }
                                              } else {
                                                  System.out.println("Scan List : 5 " + device.getAddress());
                                                  addData(device.getName(), device.getAddress());
                                              }
                                          } else {
                                              System.out.println("Scan List : 6 " + device.getAddress());
                                              addData(device.getName(), device.getAddress());
                                          }
                                    /*if(util.readString(HelperC.key_autoconnectaddress, "").equals(device.getAddress())) {
                                        addData(device.getOperatorName(), device.getAddress());
                                    }*/
                                          adapter.addDevice(device);
                                          adapter.notifyDataSetChanged();
//                                                  if (isCampSelected) {
//                                                      if (autocoFlag) {
//                                                          if (device == null) {
//                                                              System.out.println("Scan List :inside  ");
//                                                              return;
//                                                          }
//                                                          if (defAddress.equals(device.getAddress())) {
//                                                              if (flag != 2) {
//                                                                  final Intent intent = new Intent(getApplicationContext(), SYNCResult_Activity.class);
//                                                                  intent.putExtra(HelperC.EXTRAS_DEVICE_NAME, device.getName());
//                                                                  intent.putExtra(HelperC.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//                                                                  if (mScanning) {
//                                                                      mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                                                                      mScanning = false;
//                                                                  }
//                                                                  startActivity(intent);
//                                                                  finish();
//                                                              }
//                                                          }
//                                                      }
//                                                  }
                                      }
                                  }
                              }
                          }
            );
        }
    };

    public void addData(String name, String address) {

        ResultsModel resultsModel = new ResultsModel();
        resultsModel.setName(name);
        resultsModel.setBtadd(address);
        resultsListforAdapter.add(resultsModel);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBluetoothAdapter = null;
        mScanning = false;
        mHandler = null;
        resultsListforAdapter = null;
//        recyclerView = null;
        adapter = null;
        util = null;
        c = null;
//        btnScanList = null;
        defname = null;
        defAddress = null;
        autocoFlag = false;
        flag = 2;
//        scaningtext = null;
        isCampSelected = false;
    }
}
