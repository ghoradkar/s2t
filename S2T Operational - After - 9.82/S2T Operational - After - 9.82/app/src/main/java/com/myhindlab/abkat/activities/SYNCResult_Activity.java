package com.myhindlab.abkat.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.myhindlab.abkat.R;

import org.maniteja.com.synclib.helper.Communicator;
import org.maniteja.com.synclib.helper.HelperC;
import org.maniteja.com.synclib.helper.SerializeUUID;
import org.maniteja.com.synclib.helper.SyncLib;
import org.maniteja.com.synclib.helper.Util;

import java.io.InputStream;
import java.util.ArrayList;

public class SYNCResult_Activity extends AppCompatActivity implements Communicator {
//    ActionBar mActionBar;

    //Action bar
//    ImageView batteryIcon;
//    ImageView bluetoothIcon;

    Button startTest, stopTest;
    TextView logDisplay;

    InputStream ins;
    SerializeUUID serializeUUID;

    SYNCResult_Activity activity_home;

    public static boolean mConnected = false;
    public static boolean devTestStarted;

    SyncLib syncLib;

    Communicator communicator;
    private String mDeviceAddress;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    Util util;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_result);

        util = new Util(this, this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(null);
//        toolbar.setContentInsetsAbsolute(0, 0);
//        setSupportActionBar(toolbar);

//        toolbar.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                try
//                {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                } catch (Exception crap)
//                {
//                    crap.printStackTrace();
//
//                }
//                return false;
//            }
//        });

        ins = getResources().openRawResource(org.maniteja.com.synclib.R.raw.synclibserialize);

        serializeUUID = new SerializeUUID();
        serializeUUID.readFile(ins);//mainCore.getBasicUrl(), mainCore.getUsername(), mainCore.getPassword()

//        mActionBar = getSupportActionBar();
//        mActionBar.setDisplayShowTitleEnabled(false);
//        mActionBar.setDisplayShowCustomEnabled(true);
//        LayoutInflater mInflater = LayoutInflater.from(this);

//        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);

//        ImageView menuIcon = (ImageView) mCustomView.findViewById(R.id.menuIcon);
//        menuIcon.setVisibility(View.GONE);

//        batteryIcon = (ImageView) mCustomView.findViewById(R.id.batteryIcon);
//        bluetoothIcon = (ImageView) mCustomView.findViewById(R.id.bluetoothIcon);
//        bluetoothIcon.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                syncLib.setmDeviceAddress(mDeviceAddress);
//                syncLib.connectOrDisconnect();
//            }
//        });

//        mActionBar.setCustomView(mCustomView);

        activity_home = SYNCResult_Activity.this;

        communicator = activity_home;

        startTest = (Button) findViewById(R.id.startTest);

        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConnected) {
                    communicator.go("Start");
                } else {
                    Toast.makeText(getApplicationContext(), "Please Connect to Device!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopTest = (Button) findViewById(R.id.stopTest);
        stopTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConnected) {
                    if (devTestStarted) {
                        Communicator communicator = (Communicator) activity_home;
                        try {
                            communicator.stopNotiFication();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Test Not Started!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Connect to Device!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logDisplay = findViewById(R.id.logDisplay);

        final Intent intent = getIntent();
        if (intent.getStringExtra(EXTRAS_DEVICE_ADDRESS) != null) {
            mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
            util.putString(HelperC.key_mybluetoothaddress, mDeviceAddress);
        } else {
            mDeviceAddress = util.readString(HelperC.key_autoconnectaddress, "");
        }

        syncLib = new SyncLib(communicator, this, SYNCResult_Activity.this, serializeUUID, mDeviceAddress);

        util.print("Scan List Address :Main " + mDeviceAddress + "::" + util.readString(HelperC.key_mybluetoothaddress, "") + " - " + mDeviceAddress.length());
        setUpToolbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        System.out.println("device id");
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result == null) {
//
//                Toast.makeText(this, getResources().getString(R.string.canceled), Toast.LENGTH_LONG).show();
//            } else {
//                syncLib.writeCalibData(result.getContents());
//                Toast.makeText(this, "Scanned Data " + result.getContents(), Toast.LENGTH_LONG).show();
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*syncLib.startReceiver();
        if(!mConnected)
        {
            syncLib.setmDeviceAddress(mDeviceAddress);
            syncLib.connectOrDisconnect();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mConnected) {
            syncLib.startReceiver();
        }
        /*if(!mConnected)
        {
            syncLib.setmDeviceAddress(mDeviceAddress);
            syncLib.connectOrDisconnect();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        syncLib.stopReceiver();

        startTest = null;
        stopTest = null;
        logDisplay = null;
        ins = null;
        serializeUUID = null;
        activity_home = null;
        syncLib = null;
        communicator = null;
        mDeviceAddress = null;
        util = null;
        animation = null;
        mConnected = false;
        devTestStarted = false;


        /*if (mBound)
        {
            unbindService(mServiceConnection);
            mBound = false;
        }
        bluetoothLeService = null;*/
    }

    @Override
    public boolean go(String text) {
        try {
            if (mConnected) {
                // Starting Test
                if (text.equals("Start")) {
                    syncLib.startTest();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please connect the device first.", Toast.LENGTH_SHORT).show();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Please re-connect the device.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void stopNotiFication() {
        syncLib.stopTest();
    }

    @Override
    public void setConnectionStatus(String status, boolean connectionStatus) {
        mConnected = connectionStatus;
        if (mConnected) {
//            bluetoothIcon.setBackgroundResource(R.drawable.connect);
//            batteryIcon.setVisibility(View.VISIBLE);
        } else {
//            batteryIcon.setVisibility(View.INVISIBLE);
//            bluetoothIcon.setBackgroundResource(R.drawable.disconnect);
        }
    }

    @Override
    public void setSwitchActivity() {
//        Intent intent = new Intent(getApplicationContext(), Activity_ScanList.class);
//        intent.putExtra("flag", 2);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void setBatteryLevel(int value) {
//        if (value > 25 && value < 33) {
//            if (animation != null)
//                animation.cancel();
//            batteryIcon.setBackgroundResource(R.drawable.battery1);
//        } else if (value >= 33 && value < 50) {
//            if (animation != null)
//                animation.cancel();
//            batteryIcon.setBackgroundResource(R.drawable.battery2);
//        } else if (value >= 50) {
//            if (animation != null)
//                animation.cancel();
//            batteryIcon.setBackgroundResource(R.drawable.battery3);
//        } else if (value > 0 && value <= 25) {
//            batteryIcon.setBackgroundResource(R.drawable.battery0);
//            animation = new AlphaAnimation(1, 0);
//            animation.setDuration(400);
//            animation.setInterpolator(new LinearInterpolator());
//            animation.setRepeatCount(Animation.INFINITE);
//            animation.setRepeatMode(Animation.REVERSE);
//            batteryIcon.startAnimation(animation);
//        }
    }

    @Override
    public void setManufacturerName(String manufacturerName) {

    }

    @Override
    public void setSerialNumber(String serialNumber) {

    }

    @Override
    public void setModelNumber(String modelNumber) {

    }

    @Override
    public void getOfflineResults(ArrayList<String> arrayList) {
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                System.out.println("READING " + arrayList.get(i));
            }
        }
    }

    @Override
    public void setLog(String text) {
        logDisplay.setText(text);
//        if (text.contains("Result")) {
//            Intent intent = getIntent();
//            String result = text.replace("Result is ", "");
//            intent.putExtra("Result", result);
//            setResult(RESULT_OK, intent);
//            finish();
//        }
    }

    @Override
    public void testStarted(boolean testStarted) {
        devTestStarted = testStarted;
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SYNC+ Glucometer");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
