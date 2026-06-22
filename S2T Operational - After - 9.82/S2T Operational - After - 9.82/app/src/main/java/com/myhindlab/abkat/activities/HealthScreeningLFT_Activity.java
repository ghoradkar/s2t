package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
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

public class HealthScreeningLFT_Activity extends AppCompatActivity
        implements OnVitalsBTScanListener, OnVitalsBTDeviceListener, OnVitalsBTSpirometerListener, View.OnClickListener, OnMessageTaskListener {

    private Context context;
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

    private String userID, name, campId, healthScreentype, DeviceId = "";
    private XYMultipleSeriesRenderer multiRenderer;
    private int intialStart = 0;
    private boolean inhaleJoinCondStart;
    double lastVolume, lastFlow;

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
        context = HealthScreeningLFT_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
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
                    tv_fcv.getText().toString().trim(),
                    tv_fev1.getText().toString().trim(),
                    tv_fev1_fcv_ratio.getText().toString().trim(),
                    tv_pef.getText().toString().trim(),
                    tv_fef_25_65.getText().toString().trim(),
                    tv_fivc.getText().toString().trim(),
                    tv_pif.getText().toString().trim(),
                    tv_fet.getText().toString().trim(),
                    tv_result.getText().toString().trim(),
                    DeviceId,
                    userID
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

            res = WebServiceCall.APICall(ApplicationConstants.InsertLFTDetails, param);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
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
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
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
        try {
            if (vitalsClient == null) {
                txtLicense.setText(VitalsUtility.LicenseText(ApplicationConstants.LICENSE_KEY_SPIROMETER));
                vitalsClient = new VitalsClient(this, ApplicationConstants.LICENSE_KEY_SPIROMETER);
                vitalsClient.doBind();
            }

            if (scanDialog == null)
                scanDialog = new VitalsBTScanDialog(HealthScreeningLFT_Activity.this, HealthScreeningLFT_Activity.this, ApplicationConstants.LICENSE_KEY_SPIROMETER);
            if (scanDialog.show() == VitalsBTScanDialog.RESULT.ENABLING_BT) {
                Toast.makeText(HealthScreeningLFT_Activity.this, "Enabling Bluetooth", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            txtLicense.setText(ex.getMessage());
        }
    }

    @Override
    public void onVitalsServiceListenerAttached() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(HealthScreeningLFT_Activity.this, "Attached to Vitals Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVitalsServiceListenerDetached() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(HealthScreeningLFT_Activity.this, "Detached from Vitals Service", Toast.LENGTH_SHORT).show();
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
        try {
            String str = btnConnectSpiro.getText().toString();
            if (str.startsWith("Disconnect")) {
                vitalsClient.close(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO);
            } else {
                DeviceId = device;
                vitalsClient.doConnect(VitalsBT.LICENSED_DEVICE_TYPE.SPIRO, device, address);
            }
        } catch (VitalsLicenseException e) {
            Toast.makeText(HealthScreeningLFT_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void startSpiro() {
        try {
            vitalsClient.startSpiro();
            txtSpiro.setVisibility(View.VISIBLE);
        } catch (VitalsLicenseException e) {
            Toast.makeText(HealthScreeningLFT_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                    Toast.makeText(HealthScreeningLFT_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(HealthScreeningLFT_Activity.this, "Spirometer Connected", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(HealthScreeningLFT_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HealthScreeningLFT_Activity.this, String.format("Device: %s, Battery Level: %s%% (%s)", type.toString(), df.format(adapter.BatteryvoltagePercentage), batteryLevel), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HealthScreeningLFT_Activity.this, type.toString() + ", Name Changed to: " + deviceName, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(HealthScreeningLFT_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                btnSpiro.setVisibility(View.INVISIBLE);
                txtSpiroId.setVisibility(View.INVISIBLE);
                txtSpiro.setVisibility(View.INVISIBLE);
                Toast.makeText(HealthScreeningLFT_Activity.this, "Spirometer Disconnected", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    @Override
    public void onVitalsError(final VitalsBTClient.ERROR_TYPE errCode, final VitalsBTClient.ERRMSG errMsg, final String message) {
        if (isVisible) VitalsUtility.showError(HealthScreeningLFT_Activity.this, errCode, message);
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
                Toast.makeText(HealthScreeningLFT_Activity.this, "Stop Blowing ", Toast.LENGTH_SHORT).show();
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


}
