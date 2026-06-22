package com.myhindlab.abkat.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.HealthCheckup;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.safey.TestResultAdapter;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.models.safey.AirGraphData;
import com.myhindlab.abkat.models.safey.AirTestResult;
import com.myhindlab.abkat.models.safey.TrialResult;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.myhindlab.abkat.utilities.safey.Constants;
import com.myhindlab.abkat.utilities.safey.Utility;
import com.myhindlab.abkat.view.CustomTextViewCircularStd;
import com.myhindlab.abkat.view.SafeyLiquidFillView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.danvantri.bolt.spirometer.Spirometer;
import in.danvantri.bolt.vitalsbt.OnVitalsBTDeviceListener;
import in.danvantri.bolt.vitalsbt.OnVitalsBTSpirometerListener;
import in.danvantri.bolt.vitalsbt.VitalsAdapter;
import in.danvantri.bolt.vitalsbt.VitalsBT;
import in.danvantri.bolt.vitalsbt.VitalsBTClient;
import in.danvantri.bolt.vitalslicense.VitalsLicenseException;
import in.danvantri.bolt.vitalsservice.OnMessageTaskListener;
import in.danvantri.bolt.vitalsservice.OnVitalsBTScanListener;
import in.danvantri.bolt.vitalsservice.VitalsBTScanDialog;
import in.danvantri.bolt.vitalsservice.VitalsClient;
import in.danvantri.bolt.vitalsservice.VitalsUtility;
import info.safey.graph.charts.LineChart;
import info.safey.graph.components.Legend;
import info.safey.graph.components.LegendEntry;
import info.safey.graph.components.XAxis;
import info.safey.graph.components.YAxis;
import info.safey.graph.data.Entry;
import info.safey.graph.data.LineData;
import info.safey.graph.data.LineDataSet;
import info.safey.safey_sdk.FlowVolumeData;
import info.safey.safey_sdk.IConnectionCallback;
import info.safey.safey_sdk.IDeviceCallback;
import info.safey.safey_sdk.IErrorCallback;
import info.safey.safey_sdk.IScannerCallback;
import info.safey.safey_sdk.ITestCallback;
import info.safey.safey_sdk.ITrialCallback;
import info.safey.safey_sdk.SafeyDeviceKit;
import info.safey.safey_sdk.SafeyPerson;
import info.safey.safey_sdk.TestData;
import info.safey.safey_sdk.TestResult;

public class HealthScreeningLFT__Safey_Activity extends AppCompatActivity implements OnVitalsBTScanListener, OnVitalsBTDeviceListener, OnVitalsBTSpirometerListener, View.OnClickListener, OnMessageTaskListener, IScannerCallback, IErrorCallback, IDeviceCallback, IConnectionCallback, ITrialCallback, ITestCallback {

    private Context context;
    private final String TAG = HealthScreeningLFT__Safey_Activity.class.getSimpleName();
    private UserSessionManager session;
    private ProgressDialog pd;
    private MaterialEditText edt_beneficiaryname, edt_gender, edt_age, edt_height, edt_weight;

    private Button btnScan, btnConnectSpiro, btnSpiro, btn_register;
    private TextView txtLicense, txtSpiro, txtSpiroId, tv_fcv, tv_fev1, tv_fev1_fcv_ratio, tv_pef,
            tv_fef_25_65, tv_fivc, tv_pif, tv_fet, tv_result;
    //    private LineChart chart_graph;
    private CardView cv_siprostatus;
    private String device, address;
    private boolean isVisible = false;

    private XYSeries xySeries;
    private XYSeries xySeries2;
    private GraphicalView mChart;
    private LinearLayout spiroLayout;


    private PresentPatientList_Model patientDetails;
    private VitalsBTScanDialog scanDialog = null;
    private VitalsClient vitalsClient = null;

    private String userID, name, campId, healthScreentype, deviceId = "";
    private XYMultipleSeriesRenderer multiRenderer;
    private int intialStart = 0;
    private boolean inhaleJoinCondStart;
    double lastVolume, lastFlow;


    //Safey
    SafeyDeviceKit safeyDeviceKit = null;

    BluetoothDevice bluetoothDevice = null;
    private int devicetype = 2;
    private int testType = 1;
    private RecyclerView recyclerView;
    private LineChart chart;
    int testNo = 0;
    int bestTestResult = 0;
    int trialCount = 0;
    boolean flagQuit = false;
    private AirTestResult airtestResult;
    private ConstraintLayout graph_Root, llChart;
    private SafeyLiquidFillView liquidFillView;
    private CustomTextViewCircularStd preTest, tvTime;
    private ConstraintLayout clAnimation;
    private String imagePath, versionName;

    CountDownTimer mCountDownTimer = null;
    private CircularProgressIndicator circularProgressIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_lft);


        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
        setGraphView();
    }

    private void init() {
        context = HealthScreeningLFT__Safey_Activity.this;
        session = new UserSessionManager(context);

        pd = new ProgressDialog(context);

        getSafeyLungMonitor();


        recyclerView = findViewById(R.id.recyclerView);
        graph_Root = findViewById(R.id.graph_Root);
        chart = findViewById(R.id.chart);
        llChart = findViewById(R.id.llChart);
        liquidFillView = findViewById(R.id.liquidFillView);
        preTest = findViewById(R.id.pre_test);
        clAnimation = findViewById(R.id.clAnimation);
        circularProgressIndicator = findViewById(R.id.circularProgressIndicator);
        tvTime = findViewById(R.id.tvTime);


        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        btn_register = findViewById(R.id.btn_register);


        txtLicense = (TextView) findViewById(R.id.txtLicense);
        txtLicense.setVisibility(View.VISIBLE);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setVisibility(View.VISIBLE);
        btnScan.setOnClickListener(this);

        /* Spirometer */
        btnConnectSpiro = (Button) findViewById(R.id.btnConnectSpiro);
        btnConnectSpiro.setVisibility(View.INVISIBLE);
        btnConnectSpiro.setOnClickListener(this);

        btnSpiro = (Button) findViewById(R.id.btnSpiro);
        btnSpiro.setVisibility(View.INVISIBLE);
        btnSpiro.setOnClickListener(this);

        txtSpiroId = (TextView) findViewById(R.id.txtSpiroId);
        txtSpiroId.setText("");
        txtSpiroId.setVisibility(View.INVISIBLE);

        txtSpiro = (TextView) findViewById(R.id.txtSpiro);
        txtSpiro.setText("");
        txtSpiro.setVisibility(View.INVISIBLE);

        tv_fcv = findViewById(R.id.tv_fcv);
        tv_fev1 = findViewById(R.id.tv_fev1);
        tv_fev1_fcv_ratio = findViewById(R.id.tv_fev1_fcv_ratio);
        tv_pef = findViewById(R.id.tv_pef);
        tv_fef_25_65 = findViewById(R.id.tv_fef_25_65);
        tv_fivc = findViewById(R.id.tv_fivc);
        tv_pif = findViewById(R.id.tv_pif);
        tv_fet = findViewById(R.id.tv_fet);
        tv_result = findViewById(R.id.tv_result);
        spiroLayout = findViewById(R.id.spiroLayout);

        cv_siprostatus = findViewById(R.id.cv_siprostatus);
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

        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
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
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {
        if (Utilities.isNetworkAvailable(context)) {
            new InsertLFTDetails().execute(
                    String.valueOf(patientDetails.getRegdId()),
                    campId,
                    String.valueOf(HealthCheckup.firstTestResult.getTrialResult().get(0).getMesurementlist().get(0).getMeasuredValue()),//FEV
                    String.valueOf(HealthCheckup.firstTestResult.getTrialResult().get(0).getMesurementlist().get(1).getMeasuredValue()),//FEV1
                    String.valueOf(HealthCheckup.firstTestResult.getTrialResult().get(0).getMesurementlist().get(11).getMeasuredValue()),//FEV1/FVC
                    String.valueOf(HealthCheckup.firstTestResult.getTrialResult().get(0).getMesurementlist().get(2).getMeasuredValue()),//PEF
                    String.valueOf(HealthCheckup.firstTestResult.getTrialResult().get(0).getMesurementlist().get(14).getMeasuredValue()),//FEF25-75
                    tv_fivc.getText().toString().trim(),
                    tv_pif.getText().toString().trim(),
                    String.valueOf(HealthCheckup.firstTestResult.getTrialResult().get(0).getMesurementlist().get(25).getMeasuredValue()),//FET
                    HealthCheckup.firstTestResult.getSuggestedDiagnosis(),//Result
                    deviceId,
                    userID,
                    versionName
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lung Function Test");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getBluetoothDevice(@NonNull BluetoothDevice bluetoothDevice) {
        pd.dismiss();
        txtLicense.setText("Device Found\n" + bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());
        deviceId = bluetoothDevice.getAddress();
        Log.d(TAG, "getBluetoothDevice: " + bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());
        this.bluetoothDevice = bluetoothDevice;
        Log.d(TAG, "getBluetoothDevice: Connecting...");
        btnConnectSpiro.setText("Connect to " + bluetoothDevice.getName());
        btnConnectSpiro.setVisibility(View.VISIBLE);
    }

    @Override
    public void lastConnectedDeviceFound(@NonNull BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;

        safeyDeviceKit.connectDevice(bluetoothDevice);


    }

    @Override
    public void info(@NonNull String message) {
        Log.d(TAG, "info: " + message);
        switch (message) {
            case "INF_01": {
                pd.dismiss();
                Utilities.showAlertDialog(context, "Device Not Found", "Safey device not found around you.", false);
                break;
            }
            case "INF_02": {
//                listDevices.clear();
//                deviceAdapter.notifyDataSetChanged();
//                binding.progressbar.setVisibility(View.INVISIBLE);
//                CustomDialogs.dialogStyleDelete(new CustomDialogStyle1DataModel(requireActivity(), "", getString(R.string.saved_device_not_found), getString(R.string.tryagain), getString(R.string.cancel), this));
                break;

            }
            case "INF_18": {
//                if (dialogProgress != null)
//                    if (dialogProgress.isShowing() == true)
//                        dialogProgress.dismiss();
                pd.setMessage("Searching device around you");
                pd.setCancelable(false);
                pd.show();
                safeyDeviceKit.scanDevice();
                break;


            }
            case "INF_19": {
//                binding.layoutBattery.imageDevice.setImageResource(R.drawable.ic_peakflow_tick);
//                binding.layoutBattery.getRoot().setVisibility(View.VISIBLE);
//                deviceType = 1;
//                setSpirtoTestType();
//                binding.layoutBattery.safeySaveButton.buttonSave.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_green_round));
                break;
            }

            case "INF_20": {
                devicetype = 2;
                safeyDeviceKit.setSelectedTestType(1);
                break;
            }

            case "INF_22": {
                Utilities.showAlertDialog(context, "Something Wrong!", "Something went wrong, please try again", false, "Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (bluetoothDevice != null)
                            safeyDeviceKit.connectDevice(bluetoothDevice);
                        else
                            safeyDeviceKit.scanDevice();
                    }
                });
//                if (dialogProgress != null)
//                    if (dialogProgress.isShowing() == true)
//                        dialogProgress.dismiss();
//                CustomDialogs.dialogStyleSomethingWrong(new CustomDialogStyle1DataModel(requireActivity(), "", "", "", "", new DialogStyle1Click() {
//                    @Override
//                    public void positiveButtonClick() {
//                        NavHostFragment.findNavController(DeviceSetupFragment.this).popBackStack(R.id.nav_home, false);
//                    }
//
//                    @Override
//                    public void negativeButton() {
//                        if (bluetoothDevice != null)
//                            safeyDeviceKit.connectDevice(bluetoothDevice);
//                        else
//                            safeyDeviceKit.scanDevice();
//                    }
//                }));
                break;

            }

            case "INF_07": {
                pd.dismiss();
                Utilities.showToastMessage("Test is completed", context, true);
                clAnimation.setVisibility(View.GONE);

                break;
            }
            case "INF_03": {
                preTest.setText(getString(R.string.invalid_trail_plz_retry));
                preTest.setTextColor(Color.BLACK);
                break;
            }

            case "INF_05": {
                liquidFillView.setBottomTitle(
                        getString(R.string.start_blowing));
                preTest.setText("");
                pd.dismiss();
                int count = 20;
                circularProgressIndicator.setMax(20);


                circularProgressIndicator.setProgress(20);
                circularProgressIndicator.setProgress(count);
                mCountDownTimer = new CountDownTimer(20000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.v("Log_tag", "Tick of Progress$count$millisUntilFinished");
                        circularProgressIndicator.setProgress((int) (millisUntilFinished / 1000));
                        tvTime.setText("" + (millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {

                        tvTime.setText("20");
                        circularProgressIndicator.setProgress(20);
                        mCountDownTimer.cancel();
                    }


                };
                mCountDownTimer.start();
                break;
            }

            case "INF_10":
                liquidFillView.setBottomTitle(
                        getString(R.string.done));
                break;

            case "INF_11": {
                liquidFillView.setBottomTitle(
                        getString(R.string.start_blowing));
                liquidFillView.setCenterTitleColor(ContextCompat.getColor(context, R.color.colorAccent));
                break;
            }

            case "INF_12":
                liquidFillView.setBottomTitle(
                        getString(R.string.keep_blowing));
                break;

            case "INF_13": {
                preTest.setText(getString(R.string.insufficient_blow));
                liquidFillView.setProgress(0);

                preTest.setTextColor(Color.RED);
//                CustomDialogs.dialogStyleSuccess(CustomDialogStyle1DataModel(requireActivity(), "Error", getString(R.string.insufficient_blow),
//                        "Ok", null, object :DialogStyle1Click {
//                    override fun positiveButtonClick() {
//                        binding.layoutTestInstruction.root.visibility = View.VISIBLE
//                        binding.liquidviewPretest.root.visibility = View.GONE
//                    }
//
//                    override fun negativeButton() {
//                    }
//
//                }))
                //binding.liquidviewPretest.liquidFillView.mBottomTitle = getString(R.string.click_here_to_start)
                // binding.liquidviewPretest.liquidFillView.setCenterTitle(getString(R.string.start))
                break;
            }

            case "INF_16": {
                preTest.setText(getString(R.string.fan_moving));
                preTest.setTextColor(Color.BLACK);
                break;
            }

            case "INF_17": {
                liquidFillView.setProgress(0);
                mCountDownTimer.cancel();
                preTest.setText(getString(R.string.time_out));
                preTest.setTextColor(Color.RED);
                pd.dismiss();
//                Utilities.showAlertDialog(context, "Time Out!", "Time out please try again!", false);
//                binding.liquidviewPretest.liquidFillView.mBottomTitle = getString(R.string.click_here_to_start)
//                binding.liquidviewPretest.liquidFillView.setCenterTitle(getString(R.string.start))
//                CustomDialogs.dialogStyleSuccess(CustomDialogStyle1DataModel(requireActivity(), "Error", getString(R.string.time_out),
//                        "Ok", null, object :DialogStyle1Click {
//                    override fun positiveButtonClick() {
//                        binding.layoutTestInstruction.root.visibility = View.VISIBLE
//                        binding.liquidviewPretest.root.visibility = View.GONE
//                    }
//
//                    override fun negativeButton() {
//                    }
//
//                }))
                break;
            }

            default: {

            }


        }


    }

    @Override
    public void enableTest() {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void getBatteryStatus(@NonNull String s) {
        Log.d(TAG, "getBatteryStatus: " + s);
        safeyDeviceKit.setSelectedTestType(1);

        if (bluetoothDevice != null)
            txtLicense.setText("Device found:\n" + bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress() + "\n Batter Percentage:" + s);

    }

    @Override
    public void selectTestType() {
        safeyDeviceKit.setSelectedTestType(1);

    }

    @Override
    public void getConnected(boolean isConnected) {
        Log.d("TAG", "getConnected: " + isConnected);
        pd.dismiss();

        if (isConnected) {
            btnSpiro.setVisibility(View.VISIBLE);
            Utilities.showAlertDialog(context, "Connected", "Connected to device now you can start Test", true);
        } else {
            Utilities.showAlertDialog(context, "Not Connected", "Unable to connect to device, try again!", false);
            btnConnectSpiro.setVisibility(View.INVISIBLE);

        }


    }


    @Override
    public void enableTrial() {


    }

    @Override
    public void getTestResult(@NonNull FlowVolumeData flowVolumeData, int i) {
        Log.d(TAG, "getTestResult: " + flowVolumeData.getVolume());
        pd.dismiss();
    }

    @Override
    public void getTestResults(@NonNull String testResult, int trialCount, @NonNull String sessionScore) {
        Log.d(TAG, "getTestResults: " + testResult + " S1 " + sessionScore + " Trial Count" + trialCount);
        pd.dismiss();


        Constants.trialNo = trialCount;
//        binding.liquidviewPretest.liquidFillView.setProgress(0);
//        mCountDownTimer.cancel();
        TestData testData = new Gson().fromJson(testResult, TestData.class);
        List<TestResult> testResultList = testData.getTestResults();
        Utility.getTrialResult(testResultList, trialCount, context, devicetype, testData.getTestType(), testData.getSessionScore(), testData.getSuggestedDiagnosis(), testData.getVariance());

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        TestResultAdapter testResultAdapter = new TestResultAdapter(
                context,
                HealthCheckup.firstTestResult.getTrialResult().get(0).getMesurementlist()
        );

        recyclerView.setAdapter(testResultAdapter);
        airtestResult = new AirTestResult();
        if (HealthCheckup.firstTestResult != null) {
            airtestResult = HealthCheckup.firstTestResult;
            btnSpiro.setVisibility(View.GONE);
            btnScan.setVisibility(View.GONE);
            graph_Root.setVisibility(View.VISIBLE);
        }
//        if (HealthCheckup.firstTestResult.getTesttype() == 1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            chart.setVisibility(View.VISIBLE);
            setupCharts(chart, "Flow");
//            }

//            setupCharts(binding.volumechart, "Volume");
            showTrialResult(airtestResult);

        }


//        tv_fcv.setText("" + new DecimalFormat("##.###").format(HealthCheckup.firstTestResult.getTrialResult().get(0).getMesurementlist().get(0).getMeasuredValue()));
//        tv_fev1.setText("" + new DecimalFormat("##.###").format(FEV1));
//        tv_fev1_fcv_ratio.setText("" + new DecimalFormat("##.###").format(FEV1_FVC_ratio));
//        tv_pef.setText("" + new DecimalFormat("##.###").format(PEF));
//        tv_fef_25_65.setText("" + new DecimalFormat("##.###").format(FEF25_75));
//        tv_fivc.setText("" + new DecimalFormat("##.###").format(FIVC));
//        tv_pif.setText("" + new DecimalFormat("##.###").format(PIF));
//        tv_fet.setText("" + new DecimalFormat("##.###").format(FET));
//        tv_result.setText(result.toString().trim());
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//            binding.liquidviewPretest.preTest.setText("");
//            binding.liquidviewPretest.preTest.setTextColor(Color.BLACK);
        // binding.liquidviewPretest.liquidFillView.mBottomTitle = getString(R.string.click_here_to_start)


    }

    @Override
    public void invalidManeuver(int i) {
        Log.d(TAG, "invalidManeuver: " + i);
        pd.dismiss();


    }

    @Override
    public void onProgressChange(int progress) {
        Log.d(TAG, "onProgressChange: " + progress);
        liquidFillView.setProgress(progress);
        clAnimation.setVisibility(View.VISIBLE);


    }

    @Override
    public void testCompleted() {
        pd.dismiss();

        Log.d(TAG, "testCompleted: " + HealthCheckup.postTestResult.getTrialResult().get(0).getMesurementlist().get(0).getMeasuredValue());


    }

    private class InsertLFTDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Regid", params[0]));
            param.add(new ParamsPojo("CampId", params[1]));
            param.add(new ParamsPojo("FCV", params[2]));
            param.add(new ParamsPojo("FEV1", params[3]));
            param.add(new ParamsPojo("FEVI_FVC", params[4]));
            param.add(new ParamsPojo("PEF", params[5]));
            param.add(new ParamsPojo("FEF_25_75", params[6]));
            param.add(new ParamsPojo("FIVC", params[7]));
            param.add(new ParamsPojo("PIF", params[8]));
            param.add(new ParamsPojo("FET", params[9]));
            param.add(new ParamsPojo("Result", params[10]));
            param.add(new ParamsPojo("DeviceId", params[11]));
            param.add(new ParamsPojo("CreatedBy", params[12]));
            param.add(new ParamsPojo("VersionNo", params[13]));

            if (BuildConfig.isBeta) {

                res = WebServiceCall.APICall(ApplicationConstants.InsertLFTDetails_VersionNo, ApplicationConstants.webservice_d2d, param);

            } else {
//                res = WebServiceCall.APICall(ApplicationConstants.InsertLFTDetails, ApplicationConstants.webservice, param);

                res = WebServiceCall.APICall(ApplicationConstants.InsertLFTDetails_VersionNo, ApplicationConstants.webservice_d2d, param);


            }

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

//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setIcon(R.drawable.icon_success);
//                        builder.setTitle("Success");
//                        builder.setCancelable(false);
//                        builder.setMessage("LFT details submitted successfully");
//                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
                        getBitmapFromView(llChart);
                        new InsertLFTImages().execute(String.valueOf(patientDetails.getRegdId()),
                                userID, "1", campId,
                                "1",//Result
                                imagePath
                        );
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

    public class InsertLFTImages extends AsyncTask<String, Void, String> {

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
            try {
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.InsertLFTImages, "UTF-8");

                multipart.addFormField("RegdId", params[0]);
                multipart.addFormField("CreatedBy", params[1]);
                multipart.addFormField("IsImage", params[2]);
                multipart.addFormField("CampId", params[3]);
                multipart.addFormField("LFTFinalRemark", params[4]);
                multipart.addFilePart("File", new File(params[5]));

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
            Log.d(TAG, "onPostExecute: " + result);
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
                        builder.setMessage("LFT details submitted successfully");
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
                } else {
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);

            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onDestroy() {
        if (vitalsClient != null) {
            vitalsClient.close(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO);
            vitalsClient.doUnbind();
        }
        if (safeyDeviceKit != null) {
            safeyDeviceKit.disconnect();
            safeyDeviceKit.unregisterCallbacks();

        }


        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                startScan();
                break;
            case R.id.btnConnectSpiro:
                connectOrDisconnectSpiro();
                break;
            case R.id.btnSpiro:
                startSpiro();
                break;
        }
    }


    void startScan() {
        pd.setMessage("Searching for device . . . ");
        pd.setCancelable(false);
        pd.show();
        safeyDeviceKit.scanDevice();


//        try {
//            if (vitalsClient == null) {
//                txtLicense.setText(VitalsUtility.LicenseText(ApplicationConstants.LICENSE_KEY_SPIROMETER));
//                vitalsClient = new VitalsClient(this, ApplicationConstants.LICENSE_KEY_SPIROMETER);
//                vitalsClient.doBind();
//            }
//
//            if (scanDialog == null)
//                scanDialog = new VitalsBTScanDialog(HealthScreeningLFT_Activity.this, HealthScreeningLFT_Activity.this, ApplicationConstants.LICENSE_KEY_SPIROMETER);
//            if (scanDialog.show() == VitalsBTScanDialog.RESULT.ENABLING_BT) {
//                Toast.makeText(HealthScreeningLFT_Activity.this, "Enabling Bluetooth", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception ex) {
//            txtLicense.setText(ex.getMessage());
//        }
    }

    @Override
    public void onVitalsServiceListenerAttached() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(HealthScreeningLFT__Safey_Activity.this, "Attached to Vitals Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVitalsServiceListenerDetached() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(HealthScreeningLFT__Safey_Activity.this, "Detached from Vitals Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VitalsBTScanDialog.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            scanDialog.show();
            return;
        }
    }


    @Override
    public void OnVitalsDeviceScanned(String s, String s1) {

    }

    @Override
    public void OnVitalsDeviceSelected(String device, String address) {
        this.device = device;
        this.address = address;

        btnConnectSpiro.setText("Connect to Spirometer: " + device);
        btnConnectSpiro.setVisibility(View.VISIBLE);
    }

    void connectOrDisconnectSpiro() {
        pd.setMessage("Connecting to device . . . ");
        pd.setCancelable(false);
        pd.show();
        safeyDeviceKit.connectDevice(bluetoothDevice);

//        try {
//            String str = btnConnectSpiro.getText().toString();
//            if (str.startsWith("Disconnect")) {
//                vitalsClient.close(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO);
//            } else {
//                DeviceId = device;
//                vitalsClient.doConnect(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO, device, address);
//            }
//        } catch (VitalsLicenseException e) {
//            Toast.makeText(HealthScreeningLFT_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }

    void startSpiro() {
        pd.setTitle("Test Started");
        pd.setMessage("Test started please blow in the device, to take test and wait for result");
        pd.setCancelable(true);
        pd.show();
        btnConnectSpiro.setVisibility(View.GONE);
        Constants.isPost = false;
        HealthCheckup.firstTestResult = null;
        HealthCheckup.postTestResult = null;
        safeyDeviceKit.startTest(new SafeyPerson(4, patientDetails.getGender().equalsIgnoreCase("male") ? 1 : 2, (int) patientDetails.getWeightKGs(), Double.valueOf(patientDetails.getAge()), (int) patientDetails.getHeightCMs()));
        safeyDeviceKit.setTestStarted(true);
        safeyDeviceKit.startTrial(false);

//        try {
//            vitalsClient.startSpiro();
//            txtSpiro.setVisibility(View.VISIBLE);
//        } catch (VitalsLicenseException e) {
//            Toast.makeText(HealthScreeningLFT_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onVitalsConnected(final VitalsBTClient.DEVICE_TYPE type) {
        if (type != VitalsBTClient.DEVICE_TYPE.SPIRO) return;

        runOnUiThread(new Runnable() {
            public void run() {
                btnConnectSpiro.setText("Disconnect");
                btnSpiro.setVisibility(View.VISIBLE);
                txtSpiroId.setVisibility(View.VISIBLE);
                txtSpiro.setVisibility(View.VISIBLE);
                try {
                    vitalsClient.getFirmwareVersion(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO, VitalsBT.LICENSED_DEVICE_TYPE.SPIRO);
                    vitalsClient.getId(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO, VitalsBT.LICENSED_DEVICE_TYPE.SPIRO);
                    vitalsClient.getPowerStatus(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO);
                } catch (Exception e) {
                    Toast.makeText(HealthScreeningLFT__Safey_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(HealthScreeningLFT__Safey_Activity.this, "Spirometer Connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVitalsFirmwareVersion(final VitalsBTClient.DEVICE_TYPE baseType, final VitalsBTClient.DEVICE_TYPE type, final String version) {
        runOnUiThread(new Runnable() {
            public void run() {
//                Toast.makeText(HealthScreeningLFT_Activity.this, "Base Device: " + baseType.toString() + " Device: " + type.toString() + ", Version: " + version, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVitalsUniqueId(final VitalsBTClient.DEVICE_TYPE baseType, final VitalsBTClient.DEVICE_TYPE type, final String Id) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (type == VitalsBTClient.DEVICE_TYPE.SPIRO) {
                    txtSpiroId.setText("Id: " + Id);
                    return;
                }
            }
        });
    }

    @Override
    public void onVitalsDeviceAttached(final VitalsBTClient.DEVICE_TYPE baseType, final VitalsBTClient.DEVICE_TYPE type, final boolean isAttached) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (isAttached)
                        vitalsClient.getId(VitalsBT.LICENSED_DEVICE_TYPE.getType(baseType), VitalsBT.LICENSED_DEVICE_TYPE.getType(type));
                } catch (Exception e) {
                    Toast.makeText(HealthScreeningLFT__Safey_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onVitalsAdapterStatus(final VitalsBTClient.DEVICE_TYPE type, final VitalsAdapter adapter) {
        runOnUiThread(new Runnable() {
            public void run() {
                String batteryLevel = "Good";
                if (adapter.BatteryvoltagePercentage <= 49 && adapter.BatteryvoltagePercentage >= 21)
                    batteryLevel = "Low";
                else if (adapter.BatteryvoltagePercentage <= 20) batteryLevel = "Critically Low";
                DecimalFormat df = new DecimalFormat("#.#");
                Toast.makeText(HealthScreeningLFT__Safey_Activity.this, String.format("Device: %s, Battery Level: %s%% (%s)", type.toString(), df.format(adapter.BatteryvoltagePercentage), batteryLevel), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVitalsAdapterConnected(VitalsBTClient.DEVICE_TYPE device_type) {

    }

    @Override
    public void onVitalsAdapterDisconnected(VitalsBTClient.DEVICE_TYPE device_type) {

    }

    @Override
    public void onVitalsDeviceNameChanged(final VitalsBTClient.DEVICE_TYPE type, final String deviceName) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(HealthScreeningLFT__Safey_Activity.this, type.toString() + ", Name Changed to: " + deviceName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVitalsDisconnected(final VitalsBTClient.DEVICE_TYPE type) {
        if (type != VitalsBTClient.DEVICE_TYPE.SPIRO) return;

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    btnConnectSpiro.setText("Connect to Spriometer: " + vitalsClient.getDevice(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO));
                } catch (VitalsLicenseException e) {
                    Toast.makeText(HealthScreeningLFT__Safey_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                btnSpiro.setVisibility(View.INVISIBLE);
                txtSpiroId.setVisibility(View.INVISIBLE);
                txtSpiro.setVisibility(View.INVISIBLE);
                Toast.makeText(HealthScreeningLFT__Safey_Activity.this, "Spirometer Disconnected", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    @Override
    public void onVitalsError(final VitalsBTClient.ERROR_TYPE errCode, final VitalsBTClient.ERRMSG errMsg, final String message) {
        if (isVisible)
            VitalsUtility.showError(HealthScreeningLFT__Safey_Activity.this, errCode, message);
    }

    @Override
    public void onMessageTaskComplete(final boolean isSpuriousError, final boolean result) {

    }

    @Override
    public void onVitalsSpiroStarted() {

    }

    @Override
    public void onVitalsSpiroBTPS(int i, double v) {

    }

    @Override
    public void onVitalsSpiroProgress(final int index, final int volume, final int flow) {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                txtSpiro.setText(String.format("Receiving Spiro Data ") + index + "  " + volume);
//
////                Log.i("SPIRODATA", index + "  " + volume);
////                spirometerValuesList.add(new SpirometerValuesModel(index, volume));
//
////                Toast.makeText(HealthScreeningLFT_Activity.this, "Exhale heavily and Inhale", Toast.LENGTH_SHORT).show();
//            }
//        });

        runOnUiThread(new Runnable() {
            public void run() {
                txtSpiro.setText(String.format("Receiving Spiro Data ") + index + "  " + volume);
                Log.d("ABKIS", "Inside onVitalsSpiroProgress index=" + index + "\nflow=" + flow + "\n volume=" + volume);
                double mVolume = (double) volume / 1000;
                double mFlow = (double) flow / 1000;
                Log.d("ABKIS", "Inside onVitalsSpiroProgress index=" + index + "\nflow=" + mFlow + "\n volume=" + mVolume);
                if (mVolume >= 0) {

                    if (mFlow < 0.000) {
                        if (!inhaleJoinCondStart) {
                           /* for(int count=lastFlow;count>-1;count--)
                            {
                                xySeries.add(lastVolume,count);
                            }*/
                            xySeries.add(lastVolume, 0);
                            //mChart.repaint();
                            xySeries2.add(lastVolume, 0);
                            inhaleJoinCondStart = true;
                        } else
                            xySeries2.add(mVolume, mFlow);
                    } else {
                        if (intialStart == 0) {
                            xySeries.add(0, 0);
                            intialStart++;
//                            Log.d(TAG, "onVitalsSpiroProgress--INTIALSTART");
                        }

                        xySeries.add(mVolume, mFlow);

                    }
                    mChart.repaint();
                }
                lastFlow = mFlow;
                lastVolume = mVolume;
            }
        });
    }

    public void setGraphView() {

        xySeries = new XYSeries("");
        xySeries2 = new XYSeries("");

        XYMultipleSeriesDataset mulitpleDataset = new XYMultipleSeriesDataset();

        mulitpleDataset.addSeries(xySeries);
        mulitpleDataset.addSeries(xySeries2);

        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
        seriesRenderer.setPointStyle(PointStyle.POINT);
        seriesRenderer.setFillPoints(true);

        seriesRenderer.setLineWidth(2);
        seriesRenderer.setColor(Color.BLACK);

        XYSeriesRenderer seriesRenderer2 = new XYSeriesRenderer();
        seriesRenderer2.setPointStyle(PointStyle.POINT);
        seriesRenderer2.setFillPoints(true);
        seriesRenderer2.setLineWidth(2);
        seriesRenderer2.setColor(Color.BLACK);


        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
     /*   for(int count=500;count<=10500;)

        {
            multiRenderer.addYTextLabel(count,String.valueOf(count));
            count+=500;
        }
        multiRenderer.setYLabels(0);*/
        //multiRenderer.setYLabels(1000);
        //multiRenderer.setYAxisMax(10000);
        multiRenderer.setAxisTitleTextSize(22);
        multiRenderer.setLabelsTextSize(18);
        multiRenderer.setXTitle("Volume(L)");
        multiRenderer.setYTitle("Flow(L/Sec)");
        multiRenderer.setLegendTextSize(14);
        multiRenderer.setMargins(new int[]{30, 30, 30, 0});
        multiRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        multiRenderer.setShowLegend(false);
        multiRenderer.addSeriesRenderer(seriesRenderer);
        multiRenderer.addSeriesRenderer(seriesRenderer2);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setShowGrid(true);
        multiRenderer.setAxesColor(Color.BLACK);
        // multiRenderer.setApplyBackgroundColor(false);
        multiRenderer.setShowAxes(true);
        mChart = (GraphicalView) ChartFactory.getLineChartView(
                getBaseContext(), mulitpleDataset, multiRenderer);
        spiroLayout.addView(mChart);

    }

    @Override
    public void onVitalsSpiroMeasurmentComplete(final ArrayList<Double> volume, final ArrayList<Double> flow) {
        runOnUiThread(new Runnable() {
            public void run() {
                txtSpiro.setText(String.format("Spirometer measurment complete: " + volume.size()));
                Toast.makeText(HealthScreeningLFT__Safey_Activity.this, "Stop Blowing ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVitalsSpiroResult(final double FIVC, final double PIF, final double FVC,
                                    final double FEV1, final double PEF, final double FET, final double FEV1_FVC_ratio, final double FEF25_75) {
        runOnUiThread(new Runnable() {
            public void run() {
//                chart_graph.setVisibility(View.VISIBLE);
                cv_siprostatus.setVisibility(View.VISIBLE);
                Spirometer spiro = null;
                try {
                    if (patientDetails.getGender().equalsIgnoreCase("M")) {
                        spiro = new Spirometer(Spirometer.ETHINICITY.SOUTH_INDIAN, Spirometer.GENDER.MALE, Integer.parseInt(String.valueOf(patientDetails.getAge())),
                                (int) Float.parseFloat(String.valueOf(patientDetails.getHeightCMs())), (int) Float.parseFloat(String.valueOf(patientDetails.getWeightKGs())));
                    } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
                        spiro = new Spirometer(Spirometer.ETHINICITY.SOUTH_INDIAN, Spirometer.GENDER.FEMALE, Integer.parseInt(String.valueOf(patientDetails.getAge())),
                                (int) Float.parseFloat(String.valueOf(patientDetails.getHeightCMs())), (int) Float.parseFloat(String.valueOf(patientDetails.getWeightKGs())));
                    }
                    Spirometer.SPIROMETRY_RESULT result = spiro.SpiroResult(FVC, FEV1, PEF, FEV1_FVC_ratio);

                    tv_fcv.setText("" + new DecimalFormat("##.###").format(FVC));
                    tv_fev1.setText("" + new DecimalFormat("##.###").format(FEV1));
                    tv_fev1_fcv_ratio.setText("" + new DecimalFormat("##.###").format(FEV1_FVC_ratio));
                    tv_pef.setText("" + new DecimalFormat("##.###").format(PEF));
                    tv_fef_25_65.setText("" + new DecimalFormat("##.###").format(FEF25_75));
                    tv_fivc.setText("" + new DecimalFormat("##.###").format(FIVC));
                    tv_pif.setText("" + new DecimalFormat("##.###").format(PIF));
                    tv_fet.setText("" + new DecimalFormat("##.###").format(FET));
                    tv_result.setText(result.toString().trim());

                } catch (Exception e) {
                    Utilities.showAlertDialog(context, "Alert", "Please check if gender, age and height of beneficiary is in valid format", false);
                }
//                ArrayList<String> xVals = new ArrayList<String>();
//                ArrayList<Entry> yVals = new ArrayList<Entry>();
//
//                for (int i = 0; i < spirometerValuesList.size(); i++) {
//                    SpirometerValuesModel pojo = spirometerValuesList.get(i);
//                    String date = String.valueOf(pojo.getVolume());
//                    xVals.add(date);
//
//
//                    String index = String.valueOf(pojo.getIndex());
//                    if (index.equals("")) {
//                        index = "0";
//                    }
//
//                    float indexFlt = Float.parseFloat(index);
//                    yVals.add(new Entry(i, indexFlt));
//                }
//
//                final String[] mValues = new String[xVals.size()];
//                for (int i = 0; i < xVals.size(); i++) {
//                    mValues[i] = xVals.get(i);
//                }
//
//                chart_graph.setDrawGridBackground(false);
//                chart_graph.getDescription().setText("");
//
//                Legend l = chart_graph.getLegend();
//                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                l.setWordWrapEnabled(true);
//                l.setDrawInside(false);
//                l.setXEntrySpace(7f);
//                l.setYEntrySpace(0f);
//                l.setYOffset(0f);
//
//                YAxis rightAxis = chart_graph.getAxisRight();
//                rightAxis.setDrawGridLines(false);
//                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//                YAxis leftAxis = chart_graph.getAxisLeft();
//                leftAxis.setDrawGridLines(false);
//                leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//                XAxis xAxis = chart_graph.getXAxis();
//                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                xAxis.setAxisMinimum(0f);
//                xAxis.setGranularity(1f);
//                xAxis.setValueFormatter(new IAxisValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value, AxisBase axis) {
//                        return mValues[(int) value % mValues.length];
//
//                    }
//                });
//
//                LineData d = new LineData();
//
//                LineDataSet set = new LineDataSet(yVals, "");
//                set.setColors(ColorTemplate.rgb("#7cc576"));
//                set.setLineWidth(2.5f);
//                set.setCircleColor(Color.rgb(0, 0, 0));
//                set.setCircleRadius(3f);
//                set.setCircleColorHole(ColorTemplate.rgb("#000000"));
//                set.setFillColor(Color.rgb(0, 0, 0));
//                set.setMode(LineDataSet.Mode.LINEAR);
//                set.setDrawHighlightIndicators(false);
//                set.setDrawValues(true);
//                set.setValueTextSize(10f);
//                set.setValueTextColor(Color.rgb(0, 0, 0));
//                set.setAxisDependency(YAxis.AxisDependency.LEFT);
//                d.addDataSet(set);
//
//                chart_graph.setData(d);
//                chart_graph.invalidate();
            }
        });
    }

    @Override
    public void onVitalsSpiroMeasurmentFailed(final VitalsBTClient.SPIRO_ERROR spiroError) {
        runOnUiThread(new Runnable() {
            public void run() {
                txtSpiro.setVisibility(View.VISIBLE);
                txtSpiro.setText(String.format("Spirometer Failed: %s", spiroError.toString()));
            }
        });
    }


    void getSafeyLungMonitor() {
        safeyDeviceKit = SafeyDeviceKit.Companion.init(context, "7659-2779-4723-9301-2442");
//            if(safeyDeviceKit.isConnected())
//                safeyDeviceKit.disconnect();
        assert safeyDeviceKit != null;
        safeyDeviceKit.registerScannerCallback(this);
        safeyDeviceKit.registerErrorCallback(this);
        safeyDeviceKit.registerDeviceCallback(this);
        safeyDeviceKit.registerConnectionCallback(this);
        safeyDeviceKit.registerTrialCallback(this);
        safeyDeviceKit.registerTestCallback(this);

        pd.setMessage("Searching for device");
        pd.setCancelable(false);
        pd.show();
        if (bluetoothDevice != null)
            safeyDeviceKit.connectDevice(bluetoothDevice);
        else
            safeyDeviceKit.scanDevice();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NewApi")
    private void setupCharts(LineChart chart, String xAxisTitle) {
        chart.getDescription().setEnabled(false);
        chart.getDescription().setText(xAxisTitle);
        chart.getDescription().setTextSize(12F);
        chart.setGridBackgroundColor(Color.TRANSPARENT);
        chart.getLegend().setEnabled(false);
        chart.getLegend().setTextColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setPinchZoom(false);
        chart.setTouchEnabled(false);
        XAxis xl = chart.getXAxis();

        List<Double> listMaxVolume = new ArrayList();
        List<Double> listPostMaxVolume = new ArrayList();
        for (TrialResult trial : HealthCheckup.firstTestResult.getTrialResult()) {
            listMaxVolume.add(trial.getGraphDataList().stream().max(Comparator.comparing(AirGraphData::getVolume)).get().getVolume());

        }
        if (HealthCheckup.postTestResult != null) {
            listPostMaxVolume.add(HealthCheckup.postTestResult.getTrialResult().get(0).getGraphDataList().stream().max(Comparator.comparing(AirGraphData::getVolume)).get().getVolume());
        }

        List<Double> listMinVolume = new ArrayList();
        List<Double> listPostMinVolume = new ArrayList();
        for (TrialResult trial : HealthCheckup.firstTestResult.getTrialResult()) {
            listMinVolume.add(trial.getGraphDataList().stream().min(Comparator.comparing(AirGraphData::getVolume)).get().getVolume());
        }
        if (HealthCheckup.postTestResult != null) {
            listPostMinVolume.add(HealthCheckup.postTestResult.getTrialResult().get(0).getGraphDataList().stream().min(Comparator.comparing(AirGraphData::getVolume)).get().getVolume());
        }

        List<Double> listMaxFlow = new ArrayList();
        List<Double> listPostMaxFlow = new ArrayList();
        for (TrialResult trial : HealthCheckup.firstTestResult.getTrialResult()) {
            listMaxFlow.add(trial.getGraphDataList().stream().max(Comparator.comparing(AirGraphData::getFlow)).get().getFlow());
        }
        if (HealthCheckup.postTestResult != null) {
            listPostMaxFlow.add(HealthCheckup.postTestResult.getTrialResult().get(0).getGraphDataList().stream().max(Comparator.comparing(AirGraphData::getFlow)).get().getFlow());
        }

        List<Double> listMinFlow = new ArrayList();
        List<Double> listPostMinFlow = new ArrayList();
        for (TrialResult trial : HealthCheckup.firstTestResult.getTrialResult()) {
            listMinFlow.add(trial.getGraphDataList().stream().min(Comparator.comparing(AirGraphData::getFlow)).get().getFlow());
        }
        if (HealthCheckup.postTestResult != null) {
            listPostMinFlow.add(HealthCheckup.postTestResult.getTrialResult().get(0).getGraphDataList().stream().min(Comparator.comparing(AirGraphData::getFlow)).get().getFlow());
        }

        List<Double> listMaxSecond = new ArrayList();
        List<Double> listPostMaxSecond = new ArrayList();
        for (TrialResult trial : HealthCheckup.firstTestResult.getTrialResult()) {
            listMaxSecond.add(trial.getGraphDataList().stream().max(Comparator.comparing(AirGraphData::getSecond)).get().getSecond());
        }
        if (HealthCheckup.postTestResult != null) {
            listPostMaxSecond.add(HealthCheckup.postTestResult.getTrialResult().get(0).getGraphDataList().stream().max(Comparator.comparing(AirGraphData::getSecond)).get().getSecond());
        }

        Double flow = Collections.max(listMaxFlow);

        if (HealthCheckup.postTestResult != null) {
            Double flowPost = Collections.max(listPostMaxFlow);
            if (flowPost > flow)
                flow = flowPost;
        }
        int maxflow = (int) Math.round(flow);
        maxflow += 2;


        Double volume = Collections.max(listMaxVolume);
        if (HealthCheckup.postTestResult != null) {
            double volumePost = Collections.max(listPostMaxVolume);
            if (volumePost > volume)
                volume = volumePost;
        }
        int maxVolume = (int) Math.round(volume);
        maxVolume += 2;

        Double time = Collections.max(listMaxSecond);

        if (HealthCheckup.postTestResult != null) {
            Double timePost = Collections.max(listPostMaxSecond);
            if (timePost > volume)
                time = timePost;
        }
        int maxTime = (int) Math.round(time);
        maxTime += 2;

        double minflowval = Collections.min(listMinFlow);
        if (HealthCheckup.postTestResult != null) {
            double minflowvalPost = Collections.min(listPostMinFlow);
            if (minflowvalPost < minflowval)
                minflowval = minflowvalPost;
        }
        double minflow = Math.round(minflowval);
        minflow -= 2;

        double minVolumeval = Collections.min(listMinVolume);

        if (HealthCheckup.postTestResult != null) {
            double minVolumevalPost = Collections.min(listPostMinVolume);
            if (minVolumevalPost < minVolumeval)
                minVolumeval = minVolumevalPost;
        }
        double minVolume = Math.round(minVolumeval);


        if (xAxisTitle.equals("Flow")) {

            chart.getXAxis().setAxisMinimum(0f);
            chart.getXAxis().setAxisMaximum(maxVolume);
            chart.getXAxis().setAxisMaxLabels((maxVolume + 1));
            chart.getXAxis().setLabelCount(maxVolume + 1, true);
        } else {
            chart.getXAxis().setAxisMaxLabels(maxTime);
            chart.getXAxis().setAxisMaxValue(maxTime);
            chart.getXAxis().setLabelCount(maxTime, true);
        }
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(ContextCompat.getColor(context, R.color.white));
        xl.setEnabled(true);
        xl.setAxisLineColor(ContextCompat.getColor(context, R.color.white));
        xl.setAxisMinimum(0f);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(ContextCompat.getColor(context, R.color.white));

        if (xAxisTitle.equals("Flow")) {
            if (HealthCheckup.firstTestResult.getTesttype() == 1) {
                chart.getAxisLeft().setAxisMinValue(0.0f);
                chart.getAxisLeft().setAxisMaxValue(maxflow);
                leftAxis.setAxisMaxLabels(maxflow + 1);
                leftAxis.setLabelCount(maxflow + 1, true);


                chart.getXAxis().setAxisMinimum(0f);
                chart.getXAxis().setAxisMaximum(maxVolume);
                chart.getXAxis().setAxisMaxLabels(maxVolume + 1);
                chart.getXAxis().setLabelCount(maxVolume + 1, true);
            } else {
                chart.getAxisLeft().setAxisMinValue((float) minflow);
                chart.getAxisLeft().setAxisMaxValue(1.0f);

                double num = Math.abs(minflow);
                num += 2;
                leftAxis.setAxisMaxLabels((int) num);
                leftAxis.setLabelCount((int) num, true);
            }

        } else {
            if (HealthCheckup.firstTestResult.getTesttype() == 1)
                chart.getAxisLeft().setAxisMinValue(0.0f);
            chart.getAxisLeft().setAxisMaxValue(maxVolume);
            leftAxis.setAxisMaxLabels(maxVolume + 1);
            leftAxis.setLabelCount(maxVolume + 1, true);

            chart.getXAxis().setAxisMinimum(0f);
            chart.getXAxis().setAxisMaximum(maxVolume);
            chart.getXAxis().setAxisMaxLabels(maxVolume + 1);
            chart.getXAxis().setLabelCount(maxVolume + 1, true);
        }
        leftAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.graph_line_color));


        // leftAxis.axisMaximum = 7f
        leftAxis.setZeroLineColor(ContextCompat.getColor(context, R.color.white));
        leftAxis.setDrawZeroLine(true);
        if (HealthCheckup.firstTestResult.getTesttype() != 1) {
            // xl.setLabelCount(16, true)
            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
            xl.setAxisMinimum(0F);
            if (HealthCheckup.firstTestResult.getTesttype() == 3) {
                chart.getAxisRight().setDrawTopYLabelEntry(false);


                chart.getXAxis().setAxisMaxLabels(12);
                chart.getXAxis().setAxisMaxValue(12f);
                chart.getXAxis().setLabelCount(12, true);
            } else {
                chart.getXAxis().setAxisMaxLabels(16);
                chart.getXAxis().setAxisMaxValue(16f);
                chart.getXAxis().setLabelCount(16, true);

            }
            if (HealthCheckup.firstTestResult.getTesttype() == 2 || HealthCheckup.firstTestResult.getTesttype() == 4) {
                chart.getAxisLeft().setAxisMinValue(-8f);
                chart.getAxisLeft().setAxisMaxValue(9f);


                leftAxis.setAxisMaxLabels(17);
                leftAxis.setLabelCount(17, true);

            } else if (HealthCheckup.firstTestResult.getTesttype() == 3) {

                chart.getAxisLeft().setAxisMinValue(-10f);
                chart.getAxisLeft().setAxisMaxValue(12f);


                leftAxis.setAxisMaxLabels(23);
                leftAxis.setLabelCount(23, true);


            } else {
                leftAxis.setDrawZeroLine(true);
                leftAxis.setAxisMaximum(14F);
                chart.getAxisLeft().setLabelCount(14, true);
            }
            if (!xAxisTitle.equals("Flow")) {
                chart.getXAxis().setAxisMaxLabels(maxTime + 1);
                chart.getXAxis().setAxisMaxValue(maxTime);
                chart.getXAxis().setLabelCount(maxTime + 1, true);
                chart.getAxisLeft().setAxisMinValue((float) (minVolume - 2));
                chart.getAxisLeft().setAxisMaxValue(maxVolume);
                leftAxis.setAxisMaxLabels((int) (maxVolume + minVolume + 2 + 1));
                leftAxis.setLabelCount((int) (maxVolume + minVolume + 2 + 1), true);
            } else {
                if (HealthCheckup.firstTestResult.getTesttype() == 3) {
                    chart.getXAxis().setAxisMaxLabels((int) (maxVolume - minVolume + 1));
                    chart.getXAxis().setAxisMaxValue(maxVolume);
                    chart.getXAxis().setLabelCount((int) (maxVolume - minVolume + 1), true);

                    chart.getXAxis().setAxisMinValue((float) minVolumeval);
                }
                chart.getAxisLeft().setAxisMinValue((float) minflow);
                chart.getAxisLeft().setAxisMaxValue(maxflow);

                if (minflow < 0)
                    minflow *= -1;
                leftAxis.setAxisMaxLabels((int) (minflow + maxflow + 1));
                leftAxis.setLabelCount((int) (minflow + maxflow + 1), true);
            }
        }

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showTrialResult(AirTestResult testResult) {
        try {

            ArrayList<LegendEntry> legends = new ArrayList<LegendEntry>();
            chart.clear();
//            binding.volumechart.clear();

            if (HealthCheckup.firstTestResult.getTesttype() == 1) {
                setupCharts(chart, "Flow");
//                setupCharts(binding.volumechart, "Volume");
            }


            for (int i = 0; i < testResult.getTrialResult().size(); i++) {
                ArrayList<Entry> entries = new ArrayList<Entry>();
                ArrayList<Entry> entriesVS = new ArrayList<Entry>();
                LegendEntry legendEntry = new LegendEntry();
                for (AirGraphData graphData : testResult.getTrialResult().get(i).getGraphDataList()) {
                    entries.add(new Entry(graphData.getVolume().floatValue(), graphData.getFlow().floatValue()));
                    entriesVS.add(new Entry(graphData.getSecond().floatValue(), graphData.getVolume().floatValue()));
                }
                if (testResult.getTrialResult().get(i).getBest()) {
                    bestTestResult = i;
                    legendEntry.formColor = ContextCompat.getColor(context, R.color.graph_line_color);
                } else {
                    legendEntry.formColor = ContextCompat.getColor(context, R.color.white);
                }
                int tCount = i + 1;
                legendEntry.label = "Trial " + tCount;

                legends.add(legendEntry);
                setDatalineChart(entries, testResult.getTrialResult().get(i).getBest(), legends, chart);
//                setDatalineChart(entriesVS, testResult.getTrialResult().get(i).getBest(), legends, binding.volumechart);
//                switch (tCount) {
//                    case 1: {
//                        binding.text1st.setEnabled(true);
//                        break;
//                    }
//                    case 2: {
//                        binding.text2nd.setEnabled(true);
//                        break;
//                    }
//                    case 3: {
//                        binding.text3rd.setEnabled(true);
//                        break;
//                    }
//                    //  4 -> text_4th.isEnabled = true
//
//                }

            }


//            bestTest(bestTestResult);
//
//            isSelected(testResult.getTrialResult().size(), false, false);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDatalineChart(
            ArrayList<Entry> values,
            Boolean best,
            ArrayList<LegendEntry> legends,
            LineChart chart
    ) {

        if (HealthCheckup.firstTestResult.getTesttype() == 1 || HealthCheckup.firstTestResult.getTesttype() == 3 || HealthCheckup.firstTestResult.getTesttype() == 5)
            values.add(0, new Entry(0.0f, 0.0f));
        //val entry =  values.get(values.size-1)

        // values.add(values.size-1, Entry(entry.x,0.0f))
        //chart.getXAxis().setLabelCount(5,true)
        LineData data = chart.getData();
        LineDataSet set1 = new LineDataSet(values, "DataSet ");
        set1.setDrawValues(false);
        if (best) {
            set1.setColor(ContextCompat.getColor(context, R.color.graph_line_color));
            set1.setLineWidth(3.4f);
        } else {
            set1.setColor(ContextCompat.getColor(context, R.color.white));
            set1.setLineWidth(2.9f);
        }
        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setCubicIntensity(1f);
        set1.setDrawCircles(false);
        if (data == null) {
            data = new LineData(set1);
        } else {
            data.addDataSet(set1);
        }
        data.setValueTextSize(9f);

        Legend l = chart.getLegend();

        l.setCustom(legends);

        data.setDrawValues(false);
        data.setValueTextColor(ContextCompat.getColor(context, R.color.white));
        data.notifyDataChanged();
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();


    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void showReportResult(AirTestResult testResult) {
//        try {
//
//            ArrayList<LegendEntry> legends = new ArrayList<LegendEntry>();
//
//            setupCharts(binding.layoutReport.chartReport, "Flow", 1);
////            setupCharts(binding.layoutReport.chartReportvolume, "Volume", 1);
//
//            binding.layoutReport.chartReport.clear();
//            binding.layoutReport.chartReportvolume.clear();
//            String[] arrayColor = new String[]{"#8A53FF", "#FF53A5"};
//            var colorCount = 0;
//            for (int i = 0; i < testResult.getTrialResult().size(); i++) {
//                ArrayList<Entry> entries = new ArrayList<Entry>();
//                ArrayList<Entry> entriesVS = new ArrayList<Entry>();
//                LegendEntry legendEntry = new LegendEntry();
//                for (AirGraphData graphData : testResult.getTrialResult().get(i).getGraphDataList()) {
//                    entries.add(new Entry(graphData.getVolume().floatValue(), graphData.getFlow().floatValue()));
//                    if (graphData.getFlow() >= 0)
//                        entriesVS.add(new Entry(graphData.getSecond().floatValue(), graphData.getVolume().floatValue()));
//                }
//                if (testResult.getTrialResult().get(i).getBest()) {
//                    bestTestResult = i;
//                    legendEntry.formColor =
//                            ContextCompat.getColor(requireContext(), R.color.best_graph_line_color);
//
//                    legendEntry.label = "Best";
//                } else {
//                    legendEntry.formColor =
//                            Color.parseColor(arrayColor[colorCount]);
//                    colorCount++;
//                    int tCount = i + 1;
//                    legendEntry.label = "Trial " + tCount;
//                }
//
//                int tCount = i + 1;
//                legendEntry.label = "Trial " + tCount;
//
//                legends.add(legendEntry);
//                setReportDatalineChart(entries, testResult.getTrialResult().get(i).getBest(), legends, binding.layoutReport.chartReport);
//                setReportDatalineChart(entriesVS, testResult.getTrialResult().get(i).getBest(), legends, binding.layoutReport.chartReportvolume);
//            }
//            if (SafeyApplication.postTestResult != null) {
//                //for (i in SafeyApplication.postTestResult!!.trialResult!!.indices) {
//                ArrayList<Entry> entries = new ArrayList<Entry>();
//                ArrayList<Entry> entriesVS = new ArrayList<Entry>();
//                LegendEntry legendEntry = new LegendEntry();
//
//                for (AirGraphData graphData : SafeyApplication.postTestResult.getTrialResult().get(0).getGraphDataList()) {
//                    entries.add(new Entry(graphData.getVolume().floatValue(), graphData.getFlow().floatValue()));
//                    if (graphData.getFlow() >= 0)
//                        entriesVS.add(new Entry(graphData.getSecond().floatValue(), graphData.getVolume().floatValue()));
//                }
//
//                legendEntry.formColor =
//                        ContextCompat.getColor(requireContext(), R.color.rpt_line_color);
//
//                int tCount = 1;
//                legendEntry.label = "Post";
//
//
//                legends.add(legendEntry);
//                setReportDatalineChart(entries, false, legends, binding.layoutReport.chartReport);
//                setReportDatalineChart(entriesVS, false, legends, binding.layoutReport.chartReportvolume);
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        createPdf(returnedBitmap);
    }

    private void createPdf(Bitmap bitmap) {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        PdfDocument document = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#ffffff"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            document.finishPage(page);
            File patientPicsFolder;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                patientPicsFolder = getExternalCacheDir();
            } else {
                patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/LFTImage");
                if (!patientPicsFolder.exists()) {
                    patientPicsFolder.mkdirs();
                }
            }

            File filePath = new File(patientPicsFolder, userID + "_" + "494584" + ".pdf");
            try {
                document.writeTo(new FileOutputStream(filePath));
                imagePath = filePath.getPath();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            }

            // close the document
            document.close();
        }


    }

}
