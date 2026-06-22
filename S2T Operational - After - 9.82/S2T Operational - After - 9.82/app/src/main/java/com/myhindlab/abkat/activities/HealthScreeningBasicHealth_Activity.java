package com.myhindlab.abkat.activities;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.myhindlab.abkat.appointment_confirmation.Expected_BeneficiariesActivity;
import com.myhindlab.abkat.glucose.ui.GlucoseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
//import com.inuker.bluetooth.library.BluetoothClient;
//import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
//import com.inuker.bluetooth.library.model.BleGattProfile;
//import com.inuker.bluetooth.library.search.SearchRequest;
//import com.inuker.bluetooth.library.search.SearchResult;
//import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.myhindlab.abkat.HealthCheckup;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.ScanList;
import com.myhindlab.abkat.bioland.Constant;
import com.myhindlab.abkat.bioland.Interface.IBleIndicateListener;
//import com.myhindlab.abkat.bioland.Interface.IBleScanListener;
import com.myhindlab.abkat.bioland.Interface.IBleStateListener;
import com.myhindlab.abkat.bioland.Interface.IBleWriteListener;
import com.myhindlab.abkat.bioland.Interface.IPermissionsListener;
import com.myhindlab.abkat.bioland.adapter.DeviceListAdapter;
import com.myhindlab.abkat.bioland.entity.DeviceInfoDetailEntity;
import com.myhindlab.abkat.bioland.packet.BGMRecvPacket;
import com.myhindlab.abkat.bioland.packet.BGMSendPacket;
//import com.myhindlab.abkat.bioland.util.BLEManager;
//import com.myhindlab.abkat.bioland.util.ZToast;
import com.myhindlab.abkat.bluetooth.BTController;
import com.myhindlab.abkat.data.DataParser;
import com.myhindlab.abkat.data.Temp;
import com.myhindlab.abkat.dialog.BluetoothDeviceAdapter;
import com.myhindlab.abkat.dialog.SearchDevicesDialog;
import com.myhindlab.abkat.models.GetListOfLungandAudioImageDetails_Responce;
import com.myhindlab.abkat.models.GetListOfLungansAudioImageDetails_Model;
import com.myhindlab.abkat.models.HealthHistoryListModel;
import com.myhindlab.abkat.models.InsertBasicHealthInfoResponce;
import com.myhindlab.abkat.models.InsertHealthHistoryModel;
import com.myhindlab.abkat.models.MachineDataStatusModel;
import com.myhindlab.abkat.models.MasterModel;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.models.ProcessingLabCountModel;
import com.myhindlab.abkat.models.ResultsModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.omron.Database.OmronDBConstans;
import com.myhindlab.abkat.omron.activities.ScanActivity;
import com.myhindlab.abkat.omron.adapter.ScannedDevicesAdapter;
import com.myhindlab.abkat.omron.models.PairingDeviceData;
import com.myhindlab.abkat.omron.models.PersonalData;
import com.myhindlab.abkat.omron.utility.Constants;
import com.myhindlab.abkat.omron.utility.PreferencesManager;
import com.myhindlab.abkat.omron.utility.sampleLog;
import com.myhindlab.abkat.pojos.HealthHistoryListPojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.smart_scale.SmartScaleActivity;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.myhindlab.abkat.view.WaveformView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.maniteja.com.synclib.helper.HelperC;
import org.maniteja.com.synclib.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.DeviceConfiguration.OmronPeripheralManagerConfig;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerConnectListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerConnectStateListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerDataTransferListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerDisconnectListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerScanListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerStopScanListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.LibraryManager.OmronPeripheralManager;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.LibraryManager.SharedManager;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.OmronErrorInfo;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.OmronPeripheral;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.OmronUtility.OmronConstants;

public class HealthScreeningBasicHealth_Activity extends AppCompatActivity implements BTController.Listener, DataParser.onPackageReceivedListener, IPermissionsListener {

    private static final Logger log = LoggerFactory.getLogger(HealthScreeningBasicHealth_Activity.class);
    private Context context;
    private ProgressDialog pd;
    private String TAG = getClass().getName();
    private UserSessionManager session;
    private MaterialEditText edt_bloodgroup, edt_pulserate, edt_height, edt_weight, edt_bloodpressure, edt_bmi, edt_bloodsugarf, edt_bloodsugarpp, edt_bloodsugarr, edt_beneficiaryname, edt_gender, edt_age, edt_beneficiaryno, edt_mobileno, edt_noofchildren, edt_enter_hrs, edt_smoking, edt_alcohol, edt_tobacco, edt_systolic, edt_diastolic, edt_smoking_since_year, edt_smoking_since_months, edt_alcohol_since_year, edt_alcohol_since_months, edt_tobacco_since_year, edt_tobacco_since_months, edt_drugs_since_year, edt_drugs_since_months, edt_temperature, edt_spo2;
    private RadioButton rb_married, rb_unmarried, rb_fpoyes, rb_fpono, rb_smoking_yes, rb_smoking_no, rb_alcohol_yes, rb_alcohol_no, rb_less_than_12, rb_more_than_12, rb_tobacco_yes, rb_tobacco_no, rb_drugs_yes, rb_drugs_no;
    private LinearLayout ll_familyplanning, ll_weightMachine;
    private RecyclerView rv_health_history, rv_list;
    private Button btn_getfrom_syncdevice, btn_register;
    private String userId, campId, bmiStatus = "0", genderId, healthScreentype, MARITALSTATUSID, familyOperationStatus, smokingStatus, alcoholStatus, tobaccoStatus, isFromWeightMachineData = "0", isGlucoseMachineData = "0", glucoseDevice = "", weightDevice = "", bloodPressureDevice = "", drugsStatus;
    private PresentPatientList_Model patientDetails;
    private RadioButton rb_underweight, rb_normal, rb_overweight;
    String versionName;
    private Boolean isBPManual = true;

    private ArrayList<HealthHistoryListModel> healthHistoryList;
    private Button btnBtCtr;
    private Button btnViewHistory;
    private Button btnVideo;
    private Button btnSpoStart;
    private TextView tvBtinfo;
    private TextView tvECGinfo;
    private TextView tvSPO2info, tvSPO2info1;
    private TextView tvTEMPinfo;
    private TextView tvNIBPinfo;
    private WaveformView wfSpO2;
    private WaveformView wfECG;

    private RadioGroup rg_fasting;

    private Button btnScan;

    private XYSeries xySeries;
    private XYSeries xySeries2;
    private GraphicalView mChart;

    //Bluetooth
    BluetoothDeviceAdapter mBluetoothDeviceAdapter;
    SearchDevicesDialog mSearchDialog;
    ProgressDialog mConnectingDialog;
    ArrayList<BluetoothDevice> mBluetoothDevices;

    //data
    DataParser mDataParser;
    private BTController mBtController;
    private RadioGroup rgManualMachine;
    private RadioButton rbManual, rbMachine;
    private LinearLayoutCompat llMachine;
    private CardView cvBloodPressure;

    private String[] permissions = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};

    List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;

    private LocalBroadcastManager localBroadcastManager;
    private LocalBroadcastManager glucoseBroaBroadcastManager;
    private LocalBroadcastManager weightBroadcastManager;

    //DataParser implements

    double temperature;
    int bp_high, bp_low, bp_mean, SpO2, spo2_HeartRate, ecg_heartRate, ecg_restRate;
    String SpO2Status = "", SpO2FinalStatus = "", ECG = "0", ECGStatus = "", temp = "", temperatureStatus = "", NIBP = "", bloodPressureStatus = "", Firmware = "", Hardware = "";

    private com.myhindlab.abkat.data.SpO2 spData;
    private Button btnOmronBPSync;
    private TextView mTvDeviceInfo, mTvDeviceLocalName, mTvDeviceUuid, mTvStatusLabel, mTvErrorCode, mTvErrorDesc;
    private Boolean isScan;

    private final int TIME_INTERVAL = 1000;

    Handler mOmronHandler;
    Runnable mRunnable;
    private PreferencesManager preferencesManager = null;

    HashMap<String, String> device = null;
    HashMap<String, String> personalSettings = null;

    private ListView mLvScannedList;
    private ArrayList<OmronPeripheral> mPeripheralList;
    private ScannedDevicesAdapter mScannedDevicesAdapter;
    private OmronPeripheral mSelectedPeripheral;
    private JSONObject selectedPeripheralJson;


    private DeviceListAdapter mAdapter;
    private List<DeviceInfoDetailEntity> deviceInfoList;

    private ArrayList<Integer> selectedUsers = new ArrayList<>();
    private Button scanBtn;
    private Button transferBtn, btnGetBiolandSugarData, btn_scanWeightMachine, btn_transferDataWeightManine;
    private RelativeLayout mRlDeviceListView, mRlTransferView;
    private ProgressBar mProgressBar;
    private TextView mTvTImeStamp, mTvSystolic, mTvDiastolic, mTvPulseRate, mTvUserSelected, tvDeviceList;


    private int mDeviceType;
    private String mDeviceName;
    //    private BLEManager BLEMgr;
    private int mStatus;
    private boolean isReady;
    //
    private static final int Status_Init = 0x100;
    private static final int Status_Connecting = 0x101;
    private static final int Status_Connected = 0x102;
    private static final int Status_DisConnected = 0x103;
    private static final int Status_ConnectFail = 0x104;
    private String macAddress;

    DeviceInfoDetailEntity biolandDevice;
    private TextView tvBiolandMsg, tvBiolandDeviceInfo;

    public static PersonalData personalData;
    //Omron
    private String mSequenceNoString;
    private String IsWeightingMachineAvailable = "0";
    private String IsBPMachineAvailable = "0";
    private String IsSugarDeviceAvailable = "0";
    private final Integer connectStatus_Idle = 0;
    private final Integer connectStatus_Scanning = 1;
    private final Integer connectStatus_Connecting = 2;
    private Integer connectStatus = connectStatus_Idle;
    private static final String STR_DEVICE_INFO = "Device Information : ";
    private static final String STR_CONNECTING = "Connecting...";
    private boolean isReceiverRegistered = false;
    private boolean isDevelopmentTest = false;

    private ImageView iv_device_setting;
    private TextView omronMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_basichealth_new);

        init();
//        initData();
        initOmron();
//        initBioland();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();
    }

    private void initOmron() {
//        Omr.getInstance().setUserHash("your_user_hash_here");
        if (personalData == null) {
            personalData = new PersonalData(getContentResolver());
            personalData.loadPersonalData();
        }

//        }
        isScan = false;

        if (preferencesManager == null)
            preferencesManager = new PreferencesManager(HealthScreeningBasicHealth_Activity.this);

        if (preferencesManager.getPartnerKey().isEmpty()) {
//            showLibraryKeyDialog();
        } else {
            reloadConfiguration();
        }

        device = (HashMap<String, String>) getIntent().getSerializableExtra(Constants.extraKeys.KEY_SELECTED_DEVICE);

        // Selected users
//        String user = device.get(Constants.deviceInfoKeys.KEY_SELECTED_USER);
//        if(user != null) {
        selectedUsers.add(1);
//        }
//        loadDeviceList();
        // Selected device
        device = new HashMap<String, String>();
//        device.put("modelName", "HEM-7140T1-AP");
//        device.put("deviceGrouplncludedGroupIDKey", "1402");
//        device.put("identifier", "HEM-7140T1-AP");
//        device.put("deviceProtocol", "OMRONWLPProtocol");
//        device.put("image", "hem7140t1");
//        device.put("modelSeries", "HEM-7156T-AP");
//        device.put("thumbnail", "hem7140t1_thumbnail");
//        device.put("deviceGroupIncludedGroupIDKey", "1150");
//        device.put("modelSeries", "HEM-7140T1-AP");
//        device.put("uuidKey", "FC:CA:D1:75:81:16");
//        device.put("deviceGroupIDKey", "0");
//        device.put("selectedUserKey", "1");
//        device.put("noOfUsers", "1");
//        device.put("id", "1150");
//        device.put("category", "0");
//        device.put("LocalNameKey", "BLESmart_0000047EFCCAD1758116");
//        device.put("modelDisplayName", "HEM-7140T1-AP");
//        identifier : HEM-7140T1-AP
//        deviceProtocol : OMRONWLPProtocol
//        image : hem7140t1
//        thumbnail : hem7140t1_thumbnail
//        sequenceNoKey : null
//        modelName : HEM-7140T1-AP
//        deviceGroupIncludedGroupIDKey : 1150
//        modelSeries : HEM-7140T1-AP
//        uuidKey : FC:CA:D1:75:81:16
//        deviceGroupIDKey : 0
//        selectedUserKey : 1
//        noOfUsers : 1
//        id : 1150
//        category : 0
//        LocalNameKey : BLESmart_0000047EFCCAD1758116
//        modelDisplayName : HEM-7140T1-AP


//        device.put("modelName", "HEM-7141T1-AP");
//        device.put("deviceGroupIncludedGroupIDKey", "1151");
//        device.put("identifier", "HEM-7141T1-AP");
//        device.put("deviceProtocol", "OMRONWLPProtocol");
//        device.put("image", "m2");
//        device.put("modelSeries", "HEM-7141T1-AP");
//        device.put("thumbnail", "m2_thumbnail");
//        device.put("deviceGroupIDKey", "0");
//        device.put("noOfUsers", "1");
//        device.put("id", "1151");
//        device.put("category", "0");
//        device.put("modelDisplayName", "HEM-7141T1-AP");


//        device.put("modelName", "HEM-7142T1-AP");
//        device.put("deviceGroupIncludedGroupIDKey", "1152");
//        device.put("identifier", "HEM-7142T1-AP");
//        device.put("deviceProtocol", "OMRONWLPProtocol");
//        device.put("image", "m2");
//        device.put("modelSeries", "HEM-7142T1-AP");
//        device.put("thumbnail", "m2_thumbnail");
//        device.put("deviceGroupIDKey", "0");
//        device.put("noOfUsers", "1");
//        device.put("id", "1152");
//        device.put("category", "0");
//        device.put("modelDisplayName", "HEM-7142T1-AP");

    }

    private void reloadConfiguration() {

        if (!preferencesManager.getPartnerKey().isEmpty()) {
            Log.d(TAG, "reloadConfiguration: " + preferencesManager.getPartnerKey());
            // OmronConnectivityLibrary initialization and Api key setup.
            OmronPeripheralManager.sharedManager(this).setAPIKey(preferencesManager.getPartnerKey(), null);

            // Notification Listener for Configuration Availability
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(OmronConstants.OMRONBLEConfigDeviceAvailabilityNotification));
        } else {
//            showLibraryKeyDialog();
        }
    }


    @Override
    protected void onPause() {

        super.onPause();

        enableDisableButton(true);
    }

    private void initBioland() {
        initList();

        initListener();

//        mBtClient = new BluetoothClient(this);
        //2.初始化 BLEManager
//        BLEMgr = BLEManager.getInstance();

//        BLEMgr.initBLEMgr(HealthScreeningBasicHealth_Activity.this);

        //3.连接蓝牙设备
        mStatus = Status_Init;
        isReady = false;

//        requestPermission();
    }

    private void init() {
        context = HealthScreeningBasicHealth_Activity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);


        glucoseBroaBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("GLUCOSE_DATA");
        glucoseBroaBroadcastManager.registerReceiver(glucoseBroadcastReceiver, intentFilter);


        weightBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilterForScale = new IntentFilter("WEIGHT_DATA");
        weightBroadcastManager.registerReceiver(weightBroadcastReceiver, intentFilterForScale);


        btnBtCtr = (Button) findViewById(R.id.btnBtCtr);
        tvBtinfo = (TextView) findViewById(R.id.tvbtinfo);
        tvECGinfo = (TextView) findViewById(R.id.tvECGinfo);
        tvSPO2info = (TextView) findViewById(R.id.tvSPO2info);
        tvSPO2info1 = (TextView) findViewById(R.id.tvSPO2info1);
        tvTEMPinfo = (TextView) findViewById(R.id.tvTEMPinfo);
        tvNIBPinfo = (TextView) findViewById(R.id.tvNIBPinfo);

        btnOmronBPSync = findViewById(R.id.btnOmronBPSync);
        mTvErrorCode = (TextView) findViewById(R.id.tv_error_value);
        mTvErrorDesc = (TextView) findViewById(R.id.tv_error_desc);
        scanBtn = (Button) findViewById(R.id.btn_scan);
        transferBtn = (Button) findViewById(R.id.btn_transfer);
        mRlDeviceListView = (RelativeLayout) findViewById(R.id.rl_device_list);
        mRlTransferView = (RelativeLayout) findViewById(R.id.rl_transfer_view);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_scan);
        mLvScannedList = (ListView) findViewById(R.id.lv_scannedlist);
        mTvTImeStamp = (TextView) findViewById(R.id.tv_timestamp_value);
        mTvSystolic = (TextView) findViewById(R.id.tv_sys_value);
        mTvDiastolic = (TextView) findViewById(R.id.tv_dia_value);
        mTvPulseRate = (TextView) findViewById(R.id.tv_pulse_value);
        mTvUserSelected = (TextView) findViewById(R.id.tv_userselected);
        tvDeviceList = (TextView) findViewById(R.id.tv_device_list);

        mTvDeviceInfo = (TextView) findViewById(R.id.device_info);
        mTvDeviceLocalName = (TextView) findViewById(R.id.tv_device_name);
        mTvDeviceUuid = (TextView) findViewById(R.id.tv_device_uuid);
        mTvStatusLabel = (TextView) findViewById(R.id.tv_status_value);
        mTvErrorCode = (TextView) findViewById(R.id.tv_error_value);


        //About Information
        //SpO2 & ECG waveform


        wfSpO2 = (WaveformView) findViewById(R.id.wfSpO2);
        wfECG = (WaveformView) findViewById(R.id.wfECG);


        //BerryMed
        rbMachine = findViewById(R.id.rbMachine);
        rbManual = findViewById(R.id.rbManual);
        rgManualMachine = findViewById(R.id.rgManualMachine);
        llMachine = findViewById(R.id.llMachine);
        cvBloodPressure = findViewById(R.id.cvBloodPressure);
        btnSpoStart = findViewById(R.id.btnSpoStart);


        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_beneficiaryno = findViewById(R.id.edt_beneficiaryno);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        edt_bmi = findViewById(R.id.edt_bmi);
        edt_pulserate = findViewById(R.id.edt_pulserate);
        edt_bloodgroup = findViewById(R.id.edt_bloodgroup);
        edt_bloodpressure = findViewById(R.id.edt_bloodpressure);
        edt_bloodsugarf = findViewById(R.id.edt_bloodsugarf);
        edt_bloodsugarpp = findViewById(R.id.edt_bloodsugarpp);
        edt_bloodsugarr = findViewById(R.id.edt_bloodsugarr);
        edt_mobileno = findViewById(R.id.edt_mobileno);
        edt_noofchildren = findViewById(R.id.edt_noofchildren);
        edt_smoking_since_year = findViewById(R.id.edt_smoking_since_year);
        edt_smoking_since_months = findViewById(R.id.edt_smoking_since_months);
        edt_alcohol_since_year = findViewById(R.id.edt_alcohol_since_year);
        edt_alcohol_since_months = findViewById(R.id.edt_alcohol_since_months);
        edt_tobacco_since_year = findViewById(R.id.edt_tobacco_since_year);
        edt_tobacco_since_months = findViewById(R.id.edt_tobacco_since_months);
        edt_drugs_since_year = findViewById(R.id.edt_drugs_since_year);
        edt_drugs_since_months = findViewById(R.id.edt_drugs_since_months);
        edt_smoking = findViewById(R.id.edt_smoking);
        edt_alcohol = findViewById(R.id.edt_alcohol);
        edt_tobacco = findViewById(R.id.edt_tobacco);
        edt_systolic = findViewById(R.id.edt_systolic);
        edt_diastolic = findViewById(R.id.edt_diastolic);
        edt_temperature = findViewById(R.id.edt_temperature);
        edt_spo2 = findViewById(R.id.edt_spo2);
        omronMsg = findViewById(R.id.omronMsg);
        iv_device_setting = findViewById(R.id.iv_device_setting);
        edt_enter_hrs = findViewById(R.id.edt_enter_hrs);
        rg_fasting = findViewById(R.id.rg_fasting);
        rb_less_than_12 = findViewById(R.id.rb_less_than_12);
        rb_more_than_12 = findViewById(R.id.rb_more_than_12);

        rb_married = findViewById(R.id.rb_married);
        rb_unmarried = findViewById(R.id.rb_unmarried);
        rb_fpoyes = findViewById(R.id.rb_fpoyes);
        rb_fpono = findViewById(R.id.rb_fpono);
        rb_underweight = findViewById(R.id.rb_underweight);
        rb_normal = findViewById(R.id.rb_normal);
        rb_overweight = findViewById(R.id.rb_overweight);
        rb_smoking_yes = findViewById(R.id.rb_smoking_yes);
        rb_smoking_no = findViewById(R.id.rb_smoking_no);
        rb_alcohol_yes = findViewById(R.id.rb_alcohol_yes);
        rb_alcohol_no = findViewById(R.id.rb_alcohol_no);
        rb_tobacco_yes = findViewById(R.id.rb_tobacco_yes);
        rb_tobacco_no = findViewById(R.id.rb_tobacco_no);
        rb_drugs_yes = findViewById(R.id.rb_drugs_yes);
        rb_drugs_no = findViewById(R.id.rb_drugs_no);
        ll_familyplanning = findViewById(R.id.ll_familyplanning);
        ll_weightMachine = findViewById(R.id.ll_weightMachine);
        rv_health_history = findViewById(R.id.rv_health_history);
        btnGetBiolandSugarData = findViewById(R.id.btnGetBiolandSugarData);
        btn_scanWeightMachine = findViewById(R.id.btn_scanWeightMachine);
        btn_transferDataWeightManine = findViewById(R.id.btn_transferDataWeightManine);
        tvBiolandMsg = findViewById(R.id.tvBiolandMsg);
        tvBiolandDeviceInfo = findViewById(R.id.tvBiolandDeviceInfo);
        rv_list = findViewById(R.id.rv_list);
        rv_health_history.setLayoutManager(new GridLayoutManager(context, 2));

        btn_getfrom_syncdevice = findViewById(R.id.btn_getfrom_syncdevice);
        btn_register = findViewById(R.id.btn_register);


        LinearLayoutManager listMgr = new LinearLayoutManager(this);
        rv_list.setLayoutManager(listMgr);

        //设置适配器
        mAdapter = new DeviceListAdapter(null);
        rv_list.setAdapter(mAdapter);


        healthHistoryList = new ArrayList<>();

        //Bluetooth Search Dialog
        mBluetoothDevices = new ArrayList<>();
        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(HealthScreeningBasicHealth_Activity.this, mBluetoothDevices);
        mSearchDialog = new SearchDevicesDialog(HealthScreeningBasicHealth_Activity.this, mBluetoothDeviceAdapter) {
            @Override
            public void onStartSearch() {
                mBtController.startScan(true);
            }

            @Override
            public void onClickDeviceItem(int pos) {
                BluetoothDevice device = mBluetoothDevices.get(pos);
                mBtController.startScan(false);
                mBtController.connect(HealthScreeningBasicHealth_Activity.this, device);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                tvBtinfo.setText(device.getName() + ": " + device.getAddress());
                session.setBluetoothDevice(device.getName(), device.getAddress());
                mConnectingDialog.show();
                mSearchDialog.dismiss();
            }
        };
        mSearchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBtController.startScan(false);
            }
        });

        mConnectingDialog = new ProgressDialog(HealthScreeningBasicHealth_Activity.this);
        mConnectingDialog.setMessage("Connecting...");
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBtController.write(DataParser.CMD_START_NIBP);
            Utilities.showToastMessage("Blood pressure measurement started", context, true);
        }
    };
    private BroadcastReceiver glucoseBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String glucoseData = intent.getStringExtra("glucose");
            String glucoseDeviceName = intent.getStringExtra("glucoseDevice");
//            edt_bloodsugarr.setText(glucoseData != null ? glucoseData : "No Data");

            if (glucoseData != null) {
                // Extract the first number from the string
                String[] parts = glucoseData.split(" ");
                if (parts.length > 0) {
                    edt_bloodsugarr.setText(parts[0]);// Set only the numeric value

                    isGlucoseMachineData = "1";
                }
            }


            if (glucoseDeviceName != null) {
                glucoseDevice = glucoseDeviceName;
            }
        }
    };


    private BroadcastReceiver weightBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String weightData = intent.getStringExtra("scaleData");
            String weightDataBMI = intent.getStringExtra("bmi");
            String weightmachinename = intent.getStringExtra("weightMachineName");

            isFromWeightMachineData = "1";
            if (weightData != null) {

                String integerPart = weightData.split("\\.")[0];
                edt_weight.setText(integerPart);

            } else {
                edt_weight.setText("No Data");
            }

            if (weightmachinename != null) {
                weightDevice = weightmachinename;
            }
            if (weightDataBMI != null) {
                // Extract the integer part before the decimal
//                String integerPart = weightData.split("\\.")[0];
                edt_bmi.setText(weightDataBMI);


            } else {
                edt_bmi.setText("No Data");
            }
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBtCtr:
                if (!mBtController.isBTConnected()) {
                    mSearchDialog.show();
                    mSearchDialog.startSearch();
                    mBtController.startScan(true);
                } else {
                    mBtController.disconnect();
                    tvBtinfo.setText("");
                }
                break;
            case R.id.btnNIBPStart:
                mBtController.write(DataParser.CMD_START_NIBP);
                break;
            case R.id.btnGetBiolandSugarData:
                sendInfoPacket();

                break;


        }
    }

    /**
     * Permissions for activity device
     */
    private void requestPermissions() {

        // Activity Tracker
        if (Integer.parseInt(device.get(OmronConstants.OMRONBLEConfigDevice.Category)) == OmronConstants.OMRONBLEDeviceCategory.ACTIVITY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        }
    }


    private void initData() {
        mBtController = BTController.getDefaultBTController(this);
        mBtController.registerBroadcastReceiver(this);
        mBtController.enableBtAdpter();

//        if (mBtController.isBTConnected()) {
//            mBtController.disconnect();
//            mBtController.startScan(true);
//            mBtController.unregisterBroadcastReceiver(this);
//            mBtController = BTController.getDefaultBTController(this);
//            mBtController.registerBroadcastReceiver(this);
//            mBtController.enableBtAdpter();
//        } else {
//            mBtController.startScan(true);
//        }

        mDataParser = new DataParser(this);
        mDataParser.start();

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("StartBpDevice");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


//        if (session.getBluetoothName().get(ApplicationConstants.KEY_BLUETOOTH_NAME) != null &&
//                session.getBluetoothMacAddress().get(ApplicationConstants.KEY_BLUETOOTH_MAC_ADDRESS) != null &&
//                !session.getBluetoothName().get(ApplicationConstants.KEY_BLUETOOTH_NAME).equals("") &&
//                !session.getBluetoothMacAddress().get(ApplicationConstants.KEY_BLUETOOTH_MAC_ADDRESS).equals("")
//        ) {

//        }


    }


    private void setDefaults() {
        if (getIntent() != null) {
            try {
                patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
                campId = getIntent().getStringExtra("campId");
                healthScreentype = getIntent().getStringExtra("healthScreentype");


                edt_beneficiaryname.setText(patientDetails.getEnglishName());
                edt_beneficiaryno.setText(String.valueOf(patientDetails.getRegdNo()));
                edt_mobileno.setText(patientDetails.getMobileNo());

                if (patientDetails.getGender().equalsIgnoreCase("M")) {
                    edt_gender.setText("Male");
                } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
                    edt_gender.setText("Female");
                } else {
                    edt_gender.setText("");
                }

                genderId = patientDetails.getGender();
                edt_age.setText(patientDetails.getAge());


                getMachineStatusList();

//                new GetMachineDataStatus().execute();


                if (Utilities.isNetworkAvailable(context)) {
//            new GetM_HealthHistoryDetails().execute();
                    GetListOfLungandAudioImageDetailsAPICall(String.valueOf(patientDetails.getRegdId()));
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Error", e.getMessage(), false, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
            }
        }

        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (!preferencesManager.getPairedOmronDevice().isEmpty()) {
//            scanBtn.setVisibility(View.GONE);
//            transferBtn.setVisibility(View.VISIBLE);
//            tvDeviceList.setVisibility(View.GONE);
//            mRlDeviceListView.setVisibility(View.GONE);
//
//            try {
//                selectedPeripheralJson = new JSONObject(preferencesManager.getPairedOmronDevice());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            scanBtn.setVisibility(View.VISIBLE);
//            transferBtn.setVisibility(View.GONE);
//        }
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {

        iv_device_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ScanActivity.class));
            }
        });
        btnGetBiolandSugarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInfoPacket();
            }
        });

        rb_less_than_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edt_enter_hrs.setVisibility(View.VISIBLE);

                edt_enter_hrs.setText("");

                Log.d(TAG, "onClick: " + edt_enter_hrs.getText().toString());

            }
        });


        rb_more_than_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edt_enter_hrs.setVisibility(View.GONE);
                edt_enter_hrs.setText("13");
                

               Log.d(TAG, "onClick: " + edt_enter_hrs.getText().toString());


            }
        });


        btn_scanWeightMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_gender.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Please select gender", false);
                    return;
                }
                if (edt_age.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Please select age", false);
                    return;
                }
                if (edt_height.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Please enter height", false);
                    return;
                }


                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    Toast.makeText(context, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(context, "Please enable Bluetooth to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check permissions for Android 12+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                        Activity activity = (Activity) view.getContext();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, 100);
                        return;
                    }
                } else { // For Android 11 and below (API < 31)
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {

                        Activity activity = (Activity) view.getContext();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 101);
                        return;
                    }
                }

                // If Bluetooth is enabled and permissions are granted, proceed
                context.startActivity(new Intent(context, SmartScaleActivity.class).putExtra("age", edt_age.getText().toString().trim()).putExtra("gender", edt_gender.getText().toString().trim()).putExtra("height", edt_height.getText().toString().trim()).putExtra("name", patientDetails.getEnglishName()).putExtra("beneficiaryNumber", String.valueOf(patientDetails.getRegdNo())));
            }
        });


//        edt_weight.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                isFromWeightMachineData = "0";
//            }
//        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (edt_height.getText().toString().isEmpty()) {
//                    Utilities.showAlertDialog(context, "Height missing", "Please enter height", false);
//                    return;
//                }
//
//                if (edt_weight.getText().toString().isEmpty()) {
//                    Utilities.showAlertDialog(context, "Weight missing", "Please enter weight", false);
//                    return;
//                }
//                SdkUserManager.getInstance().setUserHash("user12345");
//                OmronPeripheralManagerConfig peripheralConfig = OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).getConfiguration();
//                sampleLog.d(TAG, "Library Identifier : " + peripheralConfig.getLibraryIdentifier());
//                peripheralConfig.timeoutInterval = Constants.CONNECTION_TIMEOUT;
                // Set User Hash Id (mandatory)
//                peripheralConfig.userHashId = "15112321"; // Set logged in user email
                // Set configuration for OmronPeripheralManager
//                OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).setConfiguration(peripheralConfig);
//                int selectedUnitId = 0;
//                int selectedGenderId = Integer.valueOf(genderId.equals("F") ? 0 : 1);
//                personalData.setBirthday("1992/01/10");
//                personalData.setHeight(edt_height.getText().toString());
//                personalData.setWeight(edt_weight.getText().toString());
//                personalData.setStride(String.valueOf(80));
//                personalData.setUnitValue(selectedUnitId);
//                personalData.setGenderValue(selectedGenderId);
//                personalData.savePersonalData();
//                if (!personalData.isDataExists()) {
//                    showMessage("Info", "Please set personal setting", true, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//
//                    return;
//                }

                startActivity(new Intent(context, ScanActivity.class));
            }
        });

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferData();
            }
        });
        btnSpoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + session.getSPO());
                if (btnSpoStart.getText().equals("Start")) {
//                    mDataParser.start();
                    btnSpoStart.setText("Stop");
                    tvSPO2info.setVisibility(View.VISIBLE);
                } else {
//                    mDataParser.stop();
                    btnSpoStart.setText("Start");
                    tvSPO2info1.setVisibility(View.VISIBLE);
                    tvSPO2info.setVisibility(View.GONE);
                    if (SpO2 != 127 && spo2_HeartRate != 255 || (SpO2 != 0 && spo2_HeartRate != 0)) {
                        session.setSPO(new Gson().toJson(spData));
                    }

                    if (session.getSPO() != null || !session.getSPO().isEmpty()) {
                        com.myhindlab.abkat.data.SpO2 sp = new Gson().fromJson(session.getSPO(), com.myhindlab.abkat.data.SpO2.class);
                        tvSPO2info1.setText("SPO2:-" + sp.getSpO2() + " Heart Rate:-" + sp.getPulseRate());
                    }
                }

            }
        });

        rgManualMachine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbManual.isChecked()) {
                    llMachine.setVisibility(View.GONE);
                    cvBloodPressure.setVisibility(View.VISIBLE);
                } else {
                    llMachine.setVisibility(View.VISIBLE);
                    cvBloodPressure.setVisibility(View.GONE);

                    edt_systolic.setText("");
                    edt_diastolic.setText("");
                    edt_temperature.setText("");
                    edt_spo2.setText("");
                    edt_pulserate.setText("");
                }
            }
        });
        edt_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final ArrayList<MasterModel> genderList = new ArrayList<>();
//                genderList.add(new MasterModel("M", " Male"));
//                genderList.add(new MasterModel("F", " Female"));
//                showGenderListDialog(genderList);
            }
        });

        edt_bloodgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> bloodGrpList = new ArrayList<>();
                bloodGrpList.add("A-");
                bloodGrpList.add("A+");
                bloodGrpList.add("B-");
                bloodGrpList.add("B+");
                bloodGrpList.add("AB-");
                bloodGrpList.add("AB+");
                bloodGrpList.add("O-");
                bloodGrpList.add("O+");
                bloodGrpList.add("Not Known");
                showBloodGrpDialog(bloodGrpList);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        edt_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edt_weight.getText().toString().equals("") && !edt_height.getText().toString().equals("")) {
                    float bmi = calculateBMI(Float.parseFloat(edt_weight.getText().toString().trim()), Float.parseFloat(edt_height.getText().toString().trim()));
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(1);
                    float roundBMI = Float.parseFloat(df.format(bmi));
                    edt_bmi.setText("" + bmi);
                    if (roundBMI < 18.5) {
                        rb_underweight.setChecked(true);
                        rb_normal.setChecked(false);
                        rb_overweight.setChecked(false);
                        bmiStatus = "1";
                    } else if (roundBMI > 18.5 && roundBMI < 24.9) {
                        rb_underweight.setChecked(false);
                        rb_normal.setChecked(true);
                        rb_overweight.setChecked(false);
                        bmiStatus = "2";
                    } else if (roundBMI > 24.9) {
                        rb_underweight.setChecked(false);
                        rb_normal.setChecked(false);
                        rb_overweight.setChecked(true);
                        bmiStatus = "3";
                    }
                } else {
                    edt_bmi.setText("");
                    rb_underweight.setChecked(false);
                    rb_normal.setChecked(false);
                    rb_overweight.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edt_weight.getText().toString().equals("") && !edt_height.getText().toString().equals("")) {
                    float bmi = calculateBMI(Float.parseFloat(edt_weight.getText().toString().trim()), Float.parseFloat(edt_height.getText().toString().trim()));
                    edt_bmi.setText("" + bmi);
                    if (bmi < 18.5) {
                        rb_underweight.setChecked(true);
                        rb_normal.setChecked(false);
                        rb_overweight.setChecked(false);
                        bmiStatus = "1";
                    } else if (bmi > 18.5 && bmi < 24.9) {
                        rb_underweight.setChecked(false);
                        rb_normal.setChecked(true);
                        rb_overweight.setChecked(false);
                        bmiStatus = "2";
                    } else if (bmi > 24.9) {
                        rb_underweight.setChecked(false);
                        rb_normal.setChecked(false);
                        rb_overweight.setChecked(true);
                        bmiStatus = "3";
                    }
                } else {
                    edt_bmi.setText("");
                    rb_underweight.setChecked(false);
                    rb_normal.setChecked(false);
                    rb_overweight.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rb_married.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_married.isChecked()) {
                    edt_noofchildren.setVisibility(View.VISIBLE);
                    ll_familyplanning.setVisibility(View.VISIBLE);
                } else if (rb_unmarried.isChecked()) {
                    edt_noofchildren.setText("");
                    edt_noofchildren.setVisibility(View.GONE);
                    ll_familyplanning.setVisibility(View.GONE);
                    rb_fpoyes.setChecked(false);
                    rb_fpono.setChecked(true);

                }
            }
        });

        rb_unmarried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_married.isChecked()) {
                    edt_noofchildren.setVisibility(View.VISIBLE);
                    ll_familyplanning.setVisibility(View.VISIBLE);
                } else if (rb_unmarried.isChecked()) {
                    edt_noofchildren.setText("");
                    edt_noofchildren.setVisibility(View.GONE);
                    ll_familyplanning.setVisibility(View.GONE);
                    rb_fpoyes.setChecked(false);
                    rb_fpono.setChecked(true);
                }
            }
        });

        rb_smoking_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_smoking_since_year.setText("0");
                edt_smoking_since_months.setText("0");
                edt_smoking_since_year.setVisibility(View.GONE);
                edt_smoking_since_months.setVisibility(View.GONE);
            }
        });
        rb_alcohol_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_alcohol_since_year.setText("0");
                edt_alcohol_since_months.setText("0");
                edt_alcohol_since_year.setVisibility(View.GONE);
                edt_alcohol_since_months.setVisibility(View.GONE);
            }
        });
        rb_tobacco_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_tobacco_since_year.setText("0");
                edt_tobacco_since_months.setText("0");
                edt_tobacco_since_year.setVisibility(View.GONE);
                edt_tobacco_since_months.setVisibility(View.GONE);
            }
        });
        rb_drugs_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_drugs_since_year.setText("0");
                edt_drugs_since_months.setText("0");
                edt_drugs_since_year.setVisibility(View.GONE);
                edt_drugs_since_months.setVisibility(View.GONE);
            }
        });
        rb_smoking_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_smoking_since_year.setText("");
                edt_smoking_since_months.setText("");
                edt_smoking_since_year.setVisibility(View.VISIBLE);
                edt_smoking_since_months.setVisibility(View.VISIBLE);
            }
        });
        rb_alcohol_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_alcohol_since_year.setText("");
                edt_alcohol_since_months.setText("");
                edt_alcohol_since_year.setVisibility(View.VISIBLE);
                edt_alcohol_since_months.setVisibility(View.VISIBLE);
            }
        });
        rb_tobacco_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_tobacco_since_year.setText("");
                edt_tobacco_since_months.setText("");
                edt_tobacco_since_year.setVisibility(View.VISIBLE);
                edt_tobacco_since_months.setVisibility(View.VISIBLE);
            }
        });
        rb_drugs_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_drugs_since_year.setText("");
                edt_drugs_since_months.setText("");
                edt_drugs_since_year.setVisibility(View.VISIBLE);
                edt_drugs_since_months.setVisibility(View.VISIBLE);
            }
        });

        btn_getfrom_syncdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
////                showBluetoothSelectDeviceDialog();
//                rv_list.setVisibility(View.VISIBLE);
//                btnGetBiolandSugarData.setVisibility(View.VISIBLE);

//                onStartScanClick();

//                pd.setMessage("Searching device around you");
//                pd.setCancelable(false);
//                pd.show();

                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    Toast.makeText(context, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(context, "Please enable Bluetooth to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check permissions for Android 12+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                        Activity activity = (Activity) v.getContext();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, 100);
                        return;
                    }
                } else { // For Android 11 and below (API < 31)
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {

                        Activity activity = (Activity) v.getContext();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 101);
                        return;
                    }
                }

                context.startActivity(new Intent(context, GlucoseActivity.class)

                        .putExtra("name", patientDetails.getEnglishName()).putExtra("beneficiaryNumber", String.valueOf(patientDetails.getRegdNo())));


            }
        });
    }

    private void submitData() {

        if (edt_gender.getText().toString().trim().isEmpty()) {
            edt_gender.setError("Please select gender");
            edt_gender.requestFocus();
            return;
        }

        if (edt_age.getText().toString().trim().isEmpty()) {
            edt_age.setError("Please enter age");
            edt_age.requestFocus();
            return;
        }

        if (edt_enter_hrs.getVisibility() == View.VISIBLE) {
            if (edt_enter_hrs.getText().toString().trim().isEmpty()) {
                edt_enter_hrs.setError("Please enter hours");
                edt_enter_hrs.requestFocus();
                return;
            }

            if (Integer.parseInt(edt_enter_hrs.getText().toString().trim()) > 12 || Integer.parseInt(edt_enter_hrs.getText().toString().trim()) == 0 ) {
                Utilities.showAlertDialog(context, "Alert", "Please enter hours in between 1-12 hrs", false);
                edt_enter_hrs.requestFocus();
                return;
            }
        }


//        if (edt_age.getText().toString().trim().isEmpty()) {
//            edt_age.setError("Please enter age");
//            return;
//        }

//        if (patientDetails.getIsDependent() == 0) {
//            if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 60) {
//                Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years or more than 60 years", false);
//                return;
//            }
//        } else {
//            if (patientDetails.getReleationID() == 9 || patientDetails.getReleationID() == 10) { // Husband || Wife
//                if (Integer.parseInt(edt_age.getText().toString().trim()) < 18 || Integer.parseInt(edt_age.getText().toString().trim()) > 100) {
//                    Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years or more than 100 years", false);
//                    return;
//                }
//            } else if (patientDetails.getReleationID() == 7 || patientDetails.getReleationID() == 8) {// Son || Daughter
//                if (Integer.parseInt(edt_age.getText().toString().trim()) < 10 || Integer.parseInt(edt_age.getText().toString().trim()) > 100) {
//                    Utilities.showAlertDialog(context, "Alert", "Son or Daughter age should not be less than 10 years or more than 100 years", false);
//                    return;
//                }
//            }
//        }

        if (edt_bloodgroup.getText().toString().trim().isEmpty()) {
            edt_bloodgroup.setError("Please select blood group");
            edt_bloodgroup.requestFocus();
            return;
        }


        if (edt_height.getText().toString().trim().isEmpty()) {
            edt_height.setError("Please enter height");
            edt_height.requestFocus();
            return;
        } else if (Double.parseDouble(edt_height.getText().toString()) < 61 || Double.parseDouble(edt_height.getText().toString()) > 243) {
            edt_height.setError("Please enter height between 61 to 243 cm");
            edt_height.requestFocus();
            return;
        }

        if (!session.isHllUser()) {
            if (edt_weight.getText().toString().trim().isEmpty()) {
                edt_weight.setError("Please enter weight");
                return;
            } else if (Double.parseDouble(edt_weight.getText().toString()) < 15 || Double.parseDouble(edt_weight.getText().toString()) > 170) {
                edt_weight.setError("Please enter weight between 15 to 170 KG");
                edt_weight.requestFocus();
                return;
            }
        } else {
            if (edt_weight.getText().toString().trim().isEmpty()) {
                edt_weight.setError("Please enter weight");
                return;
            } else if (Double.parseDouble(edt_weight.getText().toString()) < 15 || Double.parseDouble(edt_weight.getText().toString()) > 170) {
                edt_weight.setError("Please enter weight between 15 to 170 KG");
                edt_weight.requestFocus();
                return;
            }
        }

        if (rb_married.isChecked()) {
            MARITALSTATUSID = "1";
        } else if (rb_unmarried.isChecked()) {
            MARITALSTATUSID = "2";
        } else {
            Utilities.showToastMessage("Please select marital status", context, false);
            return;
        }


        if (rg_fasting.getCheckedRadioButtonId() == -1) {
            Utilities.showAlertDialog(context, "Alert", "Please select input for Fasting hrs", false);
            return;
        }

        if (rb_married.isChecked()) {

            if (edt_noofchildren.getText().toString().trim().isEmpty()) {
                edt_noofchildren.setError("Please enter no. of children");
                return;
            } else if (Integer.parseInt(edt_noofchildren.getText().toString().trim()) > 7) {
                edt_noofchildren.setError(" No. of children should be less than or equal to 7");
                return;
            }
            if (rb_fpoyes.isChecked()) {
                familyOperationStatus = "1";
            } else if (rb_fpono.isChecked()) {
                familyOperationStatus = "0";
            } else {
                Utilities.showToastMessage("Please select family planning operation status", context, false);
                return;
            }
        } else {
            familyOperationStatus = "0";
        }

        if (rb_smoking_yes.isChecked()) {
            if ((edt_smoking_since_year.getText().toString().trim().isEmpty() || Double.parseDouble(edt_smoking_since_year.getText().toString().trim()) == 0) && (edt_smoking_since_months.getText().toString().trim().isEmpty() || Double.parseDouble(edt_smoking_since_months.getText().toString().trim()) == 0)) {
                edt_smoking_since_year.setError("Please enter valid no. of years");
                edt_smoking_since_months.setError("Please enter valid no. of months");
                return;
            }

            if (!edt_smoking_since_year.getText().toString().trim().isEmpty()) {
                if (Double.parseDouble(edt_smoking_since_year.getText().toString().trim()) > 99) {
                    edt_smoking_since_year.setError("Please enter valid no. of years");
                    return;
                }
            } else edt_smoking_since_year.setText("0");

            if (!edt_smoking_since_months.getText().toString().trim().isEmpty()) {
                if (Double.parseDouble(edt_smoking_since_months.getText().toString().trim()) > 12) {
                    edt_smoking_since_months.setError("Please enter valid no. of months");
                    return;
                }
            } else edt_smoking_since_months.setText("0");

            smokingStatus = "1";
        } else if (rb_smoking_no.isChecked()) {
            smokingStatus = "0";
        }

        if (rb_alcohol_yes.isChecked()) {
            if ((edt_alcohol_since_year.getText().toString().trim().isEmpty() || Double.parseDouble(edt_alcohol_since_year.getText().toString().trim()) == 0) && (edt_alcohol_since_months.getText().toString().trim().isEmpty() || Double.parseDouble(edt_alcohol_since_months.getText().toString().trim()) == 0)) {
                edt_alcohol_since_year.setError("Please enter valid no. of years");
                edt_alcohol_since_months.setError("Please enter valid no. of months");
                return;

            }

            if (!edt_alcohol_since_year.getText().toString().trim().isEmpty()) {
                if (Double.parseDouble(edt_alcohol_since_year.getText().toString().trim()) > 99) {
                    edt_alcohol_since_year.setError("Please enter valid no. of years");
                    return;
                }
            } else edt_alcohol_since_year.setText("0");

            if (!edt_alcohol_since_months.getText().toString().trim().isEmpty()) {
                if (Double.parseDouble(edt_alcohol_since_months.getText().toString().trim()) > 12) {
                    edt_alcohol_since_months.setError("Please enter valid no. of months");
                    return;
                }
            } else edt_alcohol_since_months.setText("0");

            alcoholStatus = "1";
        } else if (rb_alcohol_no.isChecked()) {
            alcoholStatus = "0";
        }

        if (rb_tobacco_yes.isChecked()) {
            if ((edt_tobacco_since_year.getText().toString().trim().isEmpty() || Double.parseDouble(edt_tobacco_since_year.getText().toString().trim()) == 0) && (edt_tobacco_since_months.getText().toString().trim().isEmpty() || Double.parseDouble(edt_tobacco_since_months.getText().toString().trim()) == 0)) {
                edt_tobacco_since_year.setError("Please enter valid no. of years");
                edt_tobacco_since_months.setError("Please enter valid no. of months");
                return;
            }

            if (!edt_tobacco_since_year.getText().toString().trim().isEmpty()) {
                if (Double.parseDouble(edt_tobacco_since_year.getText().toString().trim()) > 99) {
                    edt_tobacco_since_year.setError("Please enter valid no. of years");
                    return;
                }
            } else edt_tobacco_since_year.setText("0");

            if (!edt_tobacco_since_months.getText().toString().trim().isEmpty()) {
                if (Double.parseDouble(edt_tobacco_since_months.getText().toString().trim()) > 12) {
                    edt_tobacco_since_months.setError("Please enter valid no. of months");
                    return;
                }
            } else edt_tobacco_since_months.setText("0");

            tobaccoStatus = "1";
        } else if (rb_tobacco_no.isChecked()) {
            tobaccoStatus = "0";
        }

        if (rb_drugs_yes.isChecked()) {
            if ((edt_drugs_since_year.getText().toString().trim().isEmpty() || Double.parseDouble(edt_drugs_since_year.getText().toString().trim()) == 0) && (edt_drugs_since_months.getText().toString().trim().isEmpty() || Double.parseDouble(edt_drugs_since_months.getText().toString().trim()) == 0)) {
                edt_drugs_since_year.setError("Please enter valid no. of years");
                edt_drugs_since_months.setError("Please enter valid no. of months");
                return;
            }

            if (!edt_drugs_since_year.getText().toString().trim().isEmpty()) {
                if (Double.parseDouble(edt_drugs_since_year.getText().toString().trim()) > 99) {
                    edt_drugs_since_year.setError("Please enter valid no. of years");
                    return;
                }
            } else edt_drugs_since_year.setText("0");

            if (!edt_drugs_since_months.getText().toString().trim().isEmpty()) {
                if (Double.parseDouble(edt_drugs_since_months.getText().toString().trim()) > 12) {
                    edt_drugs_since_months.setError("Please enter valid no. of months");
                    return;
                }
            } else edt_drugs_since_months.setText("0");

            drugsStatus = "1";
        } else if (rb_drugs_no.isChecked()) {
            drugsStatus = "0";
        }

        if (edt_bloodsugarr.getText().toString().trim().isEmpty()) {
            edt_bloodsugarr.setError("Please enter blood sugar R");
            edt_bloodsugarr.setFocusable(true);
            return;
        } else if (Double.parseDouble(edt_bloodsugarr.getText().toString().trim()) < 40 || Double.parseDouble(edt_bloodsugarr.getText().toString().trim()) > 700) {
            edt_bloodsugarr.setError("Blood Sugar R should be between 40 to 700");
            edt_bloodsugarr.setFocusable(true);
            return;
        }


        if (edt_systolic.getText().toString().trim().isEmpty()) {
            edt_systolic.setError("Please enter systolic");
            edt_systolic.setFocusable(true);
            return;
        } else if (Double.parseDouble(edt_systolic.getText().toString()) < 70 || Double.parseDouble(edt_systolic.getText().toString()) > 200) {
            edt_systolic.setError("systolic should be between 70 to 200");
            edt_systolic.setFocusable(true);
            return;
        }

        if (edt_diastolic.getText().toString().trim().isEmpty()) {
            edt_diastolic.setError("Please enter diastolic");
            edt_diastolic.setFocusable(true);
            return;

        } else if (Double.parseDouble(edt_diastolic.getText().toString()) < 40 || Double.parseDouble(edt_diastolic.getText().toString()) > 130) {
            edt_diastolic.setError("diastolic should be less than systolic or between 40 to 130");
            edt_diastolic.setFocusable(true);
            return;
        }


//        if (edt_temperature.getText().toString().trim().isEmpty() ||
//                Double.parseDouble(edt_temperature.getText().toString()) < 33 ||
//                Double.parseDouble(edt_temperature.getText().toString()) > 41) {
//
//            edt_temperature.setError("Temperature should be between 33 to 41 .");
//            edt_temperature.requestFocus();
//            return;
//            // edt_temperature.setText("0");
//        }

//        if (edt_spo2.getText().toString().trim().isEmpty() ||
//                Integer.parseInt(edt_spo2.getText().toString()) < 70 ||
//                Integer.parseInt(edt_spo2.getText().toString()) > 100) {
//            edt_spo2.setError("SPO2 should be between 70 to 100");
//            edt_spo2.requestFocus();
//            return;
//            // edt_spo2.setText("0");
//        }

//        if (edt_pulserate.getText().toString().trim().isEmpty()) {
//            edt_pulserate.setError("Please enter pulse rate");
//            edt_pulserate.requestFocus();
//            return;
//        } else if (Integer.parseInt(edt_pulserate.getText().toString()) < 60 ||
//                Integer.parseInt(edt_pulserate.getText().toString()) > 175) {
//
//            edt_pulserate.setError("pulse rate should not be less than 60 or more than 175");
//            edt_pulserate.requestFocus();
//            return;
//        }


//        if (edt_bloodsugarf.getText().toString().trim().isEmpty()) {
//            edt_bloodsugarf.setError("Please enter blood sugar F");
//            edt_bloodsugarf.setFocusable(true);
//            return;
//        }

//        if (edt_bloodsugarf.getText().toString().trim().isEmpty()) {
//            edt_bloodsugarf.setText("");
//        }

//        if (edt_bloodsugarpp.getText().toString().trim().isEmpty()) {
//            edt_bloodsugarpp.setText("");
//        }

        if (edt_noofchildren.getText().toString().trim().isEmpty()) {
            edt_noofchildren.setText("0");
        }

        ArrayList<InsertHealthHistoryModel> healthList = new ArrayList<>();

//        for (int i = 0; i < healthHistoryList.size(); i++) {
//            String testStatus = "0", since = "0";
//
//            HealthHistoryAdapter.ViewHolder myViewHolder =
//                    (HealthHistoryAdapter.ViewHolder) rv_health_history.findViewHolderForAdapterPosition(i);
//
//            if (myViewHolder.cb_disease.isChecked()) {
//                testStatus = "1";
//            }
//
//            if (!myViewHolder.edt_since.getText().toString().trim().equals("")) {
//                since = myViewHolder.edt_since.getText().toString();
//            }
//
//            InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
//            summary.setPE_TestID(healthHistoryList.get(i).getDiseaseid());
//            summary.setTestStatus(testStatus);
//            summary.setSince(since);
//            healthList.add(summary);
//        }

        Gson gson = new Gson();
        String jsonstring = gson.toJson(healthList);

        if (Utilities.isNetworkAvailable(context)) {
//            InsertBasicHealthInfoAPICall(patientDetails.getRegdId(), campId,
//                    edt_height.getText().toString().trim(),
//                    edt_weight.getText().toString().trim(),
//                    edt_bloodpressure.getText().toString().trim(),
//                    edt_bloodsugarf.getText().toString().trim(),
//                    edt_bloodsugarpp.getText().toString().trim(),
//                    edt_bloodsugarr.getText().toString().trim(),
//                    edt_bmi.getText().toString().trim(),
//                    bmiStatus,
//                    userId);
            new InsertBasicHealthInfo().execute(String.valueOf(patientDetails.getRegdId()), campId, edt_height.getText().toString().trim(), edt_weight.getText().toString().trim(), "0", edt_bloodsugarf.getText().toString().trim(), edt_bloodsugarpp.getText().toString().trim(), edt_bloodsugarr.getText().toString().trim(), edt_bmi.getText().toString().trim(), bmiStatus, edt_bloodgroup.getText().toString().trim(), MARITALSTATUSID, edt_noofchildren.getText().toString().trim(), familyOperationStatus, alcoholStatus, smokingStatus, tobaccoStatus, edt_age.getText().toString().trim(), genderId, edt_systolic.getText().toString().trim(), edt_diastolic.getText().toString().trim(), userId, jsonstring, edt_pulserate.getText().toString().trim().isEmpty() ? "0" : edt_pulserate.getText().toString().trim(), drugsStatus, edt_alcohol_since_months.getText().toString().trim(), edt_alcohol_since_year.getText().toString().trim(), edt_smoking_since_months.getText().toString().trim(), edt_smoking_since_year.getText().toString().trim(), edt_tobacco_since_months.getText().toString().trim(), edt_tobacco_since_year.getText().toString().trim(), edt_drugs_since_months.getText().toString().trim(), edt_drugs_since_year.getText().toString().trim(), edt_temperature.getText().toString().trim().isEmpty() ? "0" : edt_temperature.getText().toString().trim(), edt_spo2.getText().toString().trim().isEmpty() ? "0" : edt_spo2.getText().toString().trim()

//                    edt_temperature.getText().toString().trim(),
//                    edt_spo2.getText().toString().trim()

            );

        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

    }

    private void showBloodGrpDialog(final ArrayList<String> bloodGrpList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

        builderSingle.setTitle("Select Blood Group");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (String bloodGrp : bloodGrpList) {
            arrayAdapter.add(bloodGrp);
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
                edt_bloodgroup.setText(bloodGrpList.get(which));
            }
        });

        builderSingle.show();
    }

    private void showGenderListDialog(final ArrayList<MasterModel> genderList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

        builderSingle.setTitle("Select Gender");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel gender : genderList) {
            arrayAdapter.add(gender.getName());
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
                edt_gender.setText(genderList.get(which).getName());
                genderId = genderList.get(which).getId();
            }
        });
        builderSingle.show();
    }


    public class InsertBasicHealthInfo extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampId", params[1]));
            param.add(new ParamsPojo("Height_CMs", params[2]));
            param.add(new ParamsPojo("Weight_KGs", params[3]));
            param.add(new ParamsPojo("BloodPressure", params[4]));
            param.add(new ParamsPojo("BloodSugar_F", params[5]));
            param.add(new ParamsPojo("BloodSugar_PP", params[6]));
            param.add(new ParamsPojo("BloodSugar_R", params[7]));
            param.add(new ParamsPojo("BMI", params[8]));
            param.add(new ParamsPojo("BMIStatus", params[9]));
            param.add(new ParamsPojo("BloodGroup", params[10]));
            param.add(new ParamsPojo("MaritalStatus", params[11]));
            param.add(new ParamsPojo("NoOfChildren", params[12]));
            param.add(new ParamsPojo("FamilyPlanOperation", params[13]));
            param.add(new ParamsPojo("Alcohol", params[14]));
            param.add(new ParamsPojo("Smokin", params[15]));
            param.add(new ParamsPojo("Tobaco", params[16]));
            param.add(new ParamsPojo("Age", params[17]));
            param.add(new ParamsPojo("Gender", params[18]));
            param.add(new ParamsPojo("Systolic", params[19]));
            param.add(new ParamsPojo("Diastolic", params[20]));
            param.add(new ParamsPojo("CreatedBy", params[21]));
            param.add(new ParamsPojo("Tests_Details", params[22]));
            param.add(new ParamsPojo("PulseRate", params[23]));
            param.add(new ParamsPojo("Drugs", params[24]));
            param.add(new ParamsPojo("AlcoholSinceMonth", params[25]));
            param.add(new ParamsPojo("AlcoholSinceYear", params[26]));
            param.add(new ParamsPojo("SmokingSinceMonth", params[27]));
            param.add(new ParamsPojo("SmokingSinceYear", params[28]));
            param.add(new ParamsPojo("TobacoSinceMonth", params[29]));
            param.add(new ParamsPojo("TobacoSinceYear", params[30]));
            param.add(new ParamsPojo("DrugSinceMonth", params[31]));
            param.add(new ParamsPojo("DrugSinceYear", params[32]));
            param.add(new ParamsPojo("Temperature", params[33]));
            param.add(new ParamsPojo("SPO2", params[34]));
            param.add(new ParamsPojo("AppVersion", versionName));


            param.add(new ParamsPojo("isBPManual", isBPManual ? "1" : "0"));
            param.add(new ParamsPojo("IsFromWeightMachine", isFromWeightMachineData));
            param.add(new ParamsPojo("IsFromSugarDevice", isGlucoseMachineData));
            param.add(new ParamsPojo("IsFromBloodPressureDevice", isBPManual ? "1" : "0"));
            param.add(new ParamsPojo("NameOfWeightMachine", weightDevice));
            param.add(new ParamsPojo("NameOfSugarDevice", glucoseDevice));
            param.add(new ParamsPojo("NameOfBloodPressureDevice", bloodPressureDevice));
            param.add(new ParamsPojo("FastingHrs", edt_enter_hrs.getText().toString().trim()));


            Log.d(TAG, "doInBackground: " + new Gson().toJson(param));
            // res = WebServiceCall.APICall(ApplicationConstants.InsertBasicHealthInfo_New_WithVersion, param);
//            res = WebServiceCall.APICall(ApplicationConstants.InsertBasicHealthInfo_New_WithVersion_V2, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.InsertBasicHealthInfo_New_WithVersion_FastingHrs, ApplicationConstants.webservice_d2d, param);
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

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Basic Health Information");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private float calculateBMI(float weight, float height) {
        float bmi = (weight) / ((height / 100) * (height / 100));
        return bmi;

    }

    private class GetM_HealthHistoryDetails extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetM_HealthHistoryDetails, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    HealthHistoryListPojo pojoDetails = new Gson().fromJson(result, HealthHistoryListPojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        healthHistoryList = pojoDetails.getOutput();
                        rv_health_history.setAdapter(new HealthHistoryAdapter(context, healthHistoryList));
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

    private class HealthHistoryAdapter extends RecyclerView.Adapter<HealthHistoryAdapter.ViewHolder> {

        private ArrayList<HealthHistoryListModel> resultList;
        private Context context;

        public HealthHistoryAdapter(Context context, ArrayList<HealthHistoryListModel> toolMenus) {
            this.context = context;
            this.resultList = toolMenus;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_row_health_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            HealthHistoryListModel diseaseDetails = resultList.get(position);

            holder.cb_disease.setText(diseaseDetails.getDiseaseName());
            if (diseaseDetails.getIsSince().equals("1")) {
                holder.edt_since.setVisibility(View.VISIBLE);
            } else {
                holder.edt_since.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if (resultList == null) return 0;
            return resultList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CheckBox cb_disease;
            private TextView edt_since;

            public ViewHolder(View view) {
                super(view);
                cb_disease = view.findViewById(R.id.cb_disease);
                edt_since = view.findViewById(R.id.edt_since);
            }
        }


    }

    private void InsertBasicHealthInfoAPICall(String RegdId, String CampId, String Height_CMs, String Weight_KGs, String BloodPressure, String BloodSugar_F, String BloodSugar_PP, String BloodSugar_R, String BMI, String BMIStatus, String CreatedBy) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait . . .");
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<InsertBasicHealthInfoResponce> call = apiService.InsertBasicHealthInfo(RegdId, CampId, Height_CMs, Weight_KGs, BloodPressure, BloodSugar_F, BloodSugar_PP, BloodSugar_R, BMI, BMIStatus, CreatedBy);

        HttpUrl url = call.request().url();
        String s = url.toString();

        call.enqueue(new Callback<InsertBasicHealthInfoResponce>() {
            @Override
            public void onResponse(Call<InsertBasicHealthInfoResponce> call, Response<InsertBasicHealthInfoResponce> response) {
                progressDialog.dismiss();

                int surverCode = response.code();
                String surverMessage = response.message();

                if (surverCode == ApplicationConstants.OKSUCCESS) {
                    try {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (status.equalsIgnoreCase("Success")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_success);
                            builder.setTitle(status);
                            builder.setCancelable(false);
                            builder.setMessage(message);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();

                        } else {
                            Utilities.showAlertDialog(context, status, message, false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Alert", "Exception " + e.toString(), false);
                    }
                } else {
                    Utilities.showAlertDialog(context, "Alert", "Server returns : " + surverCode + " " + surverMessage, false);
                }
            }

            @Override
            public void onFailure(Call<InsertBasicHealthInfoResponce> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                Utilities.showAlertDialog(context, "Please try again", t.toString(), false);
            }
        });
    }

    private void GetListOfLungandAudioImageDetailsAPICall(String RegdId) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait . . .");
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<GetListOfLungandAudioImageDetails_Responce> call = apiService.GetListOfLungandAudioImageDetails(RegdId);

        HttpUrl url = call.request().url();
        String s = url.toString();

        call.enqueue(new Callback<GetListOfLungandAudioImageDetails_Responce>() {
            @Override
            public void onResponse(Call<GetListOfLungandAudioImageDetails_Responce> call, Response<GetListOfLungandAudioImageDetails_Responce> response) {
                progressDialog.dismiss();

                int surverCode = response.code();
                String surverMessage = response.message();

                if (surverCode == ApplicationConstants.OKSUCCESS) {
                    try {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (status.equalsIgnoreCase("Success")) {

                            GetListOfLungansAudioImageDetails_Model imageData = response.body().getOutput().get(0);

                            String h = imageData.getHeight_CMs();
                            String w = imageData.getWeight_KGs();
                            String bmiString = imageData.getBMI();
                            String bmiStatus = imageData.getBMIStatus();
                            String bp = imageData.getBloodPressure();
                            String bsf = imageData.getBloodSugar_F();
                            String bspp = imageData.getBloodSugar_PP();
                            String bsr = imageData.getBloodSugar_R();

                            edt_height.setText(h);
                            edt_weight.setText(w);
                            edt_bmi.setText(bmiString);
                            edt_bloodpressure.setText(bp);
                            edt_bloodsugarf.setText(bsf);
                            edt_bloodsugarpp.setText(bspp);
                            edt_bloodsugarr.setText(bsr);

                            if (bmiStatus.equalsIgnoreCase("1")) {
                                rb_underweight.setChecked(true);
                                rb_normal.setChecked(false);
                                rb_overweight.setChecked(false);

                            } else if (bmiStatus.equalsIgnoreCase("2")) {
                                rb_underweight.setChecked(false);
                                rb_normal.setChecked(true);
                                rb_overweight.setChecked(false);

                            } else if (bmiStatus.equalsIgnoreCase("3")) {
                                rb_underweight.setChecked(false);
                                rb_normal.setChecked(false);
                                rb_overweight.setChecked(true);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        Utilities.showAlertDialog(context,
//                                "Alert", "Exception " + e.toString(), false);
                    }
                } else {
                    Utilities.showAlertDialog(context, "Alert", "Server returns : " + surverCode + " " + surverMessage, false);
                }
            }

            @Override
            public void onFailure(Call<GetListOfLungandAudioImageDetails_Responce> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
//                Utilities.showAlertDialog(context,
//                        "Please try again", t.toString(), false);
            }
        });
    }


    /// ////////////////////////////////////////////////////SYNC BLUETOOTH//////////////////////////////////////////////////////////////

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
        if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
        alertDialogBuilder.setTitle("Select SYNC+ Device");
        alertDialogBuilder.setView(promptView);

        recyclerView = promptView.findViewById(R.id.resultsrecycler);
        btnScanList = promptView.findViewById(R.id.btnScan);

        scaningtext = promptView.findViewById(R.id.scaningtext);
        scaningtext.setText("Scaning Started");
        scanLeDevice(true);

        adapter = new ScanList(getApplicationContext(), resultsListforAdapter, true, 1, HealthScreeningBasicHealth_Activity.this);
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
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
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
        if (resultCode == RESULT_OK) if (requestCode == SYCN_RESULT) {
            String requiredValue = data.getStringExtra("Result");
            Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
            edt_bloodsugarr.setText(requiredValue);
            edt_bloodsugarr.setFocusable(false);
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
            if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
                bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = bluetoothManager.getAdapter();
            }
            mScanning = true;
            if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mBluetoothAdapter.startLeScan(mLeScanCallback);
                btnScanList.setText("Stop Scan");
            }
        } else {
            final BluetoothManager bluetoothManager;
            if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
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
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
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
            });
        }
    };

    public void addData(String name, String address) {

        ResultsModel resultsModel = new ResultsModel();
        resultsModel.setName(name);
        resultsModel.setBtadd(address);
        resultsListforAdapter.add(resultsModel);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
//            case PERMISSION_REQUEST_COARSE_LOCATION: {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //Log.d(TAG, "coarse location permission granted");
//                } else {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Functionality limited");
//                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
//                    builder.setPositiveButton(android.R.string.ok, null);
//                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                        }
//
//                    });
//                    builder.show();
//                }
//                return;
//            }
//            break;


            case 100: // Bluetooth Permissions
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Bluetooth Permission Granted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, SmartScaleActivity.class));
                } else {
                    Toast.makeText(this, "Bluetooth Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, 2);
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 3);
                }
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_SMS}, 4);
                }
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 5);
                }
                break;
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        stopScanBle();
//        mBtClient.unregisterBluetoothStateListener(mBtStateListener);
//        mBtStateListener = null;
//        mBtClient = null;
////        mBluetoothAdapter = null;
////        mScanning = false;
////        mHandler = null;
////        resultsListforAdapter = null;
////        recyclerView = null;
////        adapter = null;
////        util = null;
////        c = null;
////        btnScanList = null;
////        defname = null;
////        defAddress = null;
////        autocoFlag = false;
////        flag = 2;
////        scaningtext = null;
////        isCampSelected = false;
//
//

    /// /        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    /// /        mBtController.unregisterBroadcastReceiver(this);
    /// /        if (mBtController.isBTConnected())
    /// /            mBtController.disconnect();
//
//    }


    //BTController implements
    @Override
    public void onFoundDevice(BluetoothDevice device) {
//+
//        if (session.getBluetoothName().get(ApplicationConstants.KEY_BLUETOOTH_NAME) == null ||
//                session.getBluetoothMacAddress().get(ApplicationConstants.KEY_BLUETOOTH_MAC_ADDRESS) == null ||
//                session.getBluetoothName().get(ApplicationConstants.KEY_BLUETOOTH_NAME).equals("") ||
//                session.getBluetoothMacAddress().get(ApplicationConstants.KEY_BLUETOOTH_MAC_ADDRESS).equals("")) {
        if (mBluetoothDevices.contains(device)) return;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d(TAG, "onFoundDevice: " + device.getName() + " " + device.getAddress());
        mBluetoothDevices.add(device);
        mBluetoothDeviceAdapter.notifyDataSetChanged();

//        } else {
//        if (device.getAddress().equals(session.getBluetoothMacAddress().get(ApplicationConstants.KEY_BLUETOOTH_MAC_ADDRESS))) {
//
////            mSearchDialog.dismiss();
////            Toast.makeText(context, device.getName() + ": " + device.getAddress(), Toast.LENGTH_SHORT).show();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tvBtinfo.setText(device.getName() + ": " + device.getAddress());
//                    mSearchDialog.dismiss();
//
//                }
//            });
//////
////            new Handler(Looper.getMainLooper()).post(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////                        tvBtinfo.setText(device.getName() + ": " + device.getAddress());
////                        mSearchDialog.hide();
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                        Log.e(TAG, "run: "+e.getMessage());
////                    }
////
////
////                }
////            });
//
////            Log.d("Device Info", "run: Connected " + device.getName());
////            mBtController.startScan(false);
////            mBtController.connect(HealthScreeningBasicHealth_Activity.this, device);
//
////            }
//        }
    }

    @Override
    public void onStopScan() {
        mSearchDialog.stopSearch();
    }

    @Override
    public void onStartScan() {
        mBluetoothDevices.clear();
        mBluetoothDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConnected() {
        mConnectingDialog.setMessage("Connected √");

//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        Date date = new Date();
//        System.out.println(formatter.format(date));
//        new CHMSDeviceStatusAPI().execute(
//                userDetails.getOutput().get(0).getEmpCode(),
//                userDetails.getOutput().get(0).getDeviceTypeID(),
//                "1",
//                formatter.format(date),
//                userDetails.getOutput().get(0).getEmpCode()
//        );

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectingDialog.dismiss();
                    }
                });
            }
        }, 800);

        btnBtCtr.setText("Disconnect");
    }
//
//    public void startService() {
//        Intent serviceIntent = new Intent(this, ForegroundService.class);
//        serviceIntent.putExtra("inputExtra", "Do not close the application");
//        ContextCompat.startForegroundService(this, serviceIntent);
//    }
//
//    public void stopService() {
//        Intent serviceIntent = new Intent(this, ForegroundService.class);
//        stopService(serviceIntent);
//    }

    @Override
    public void onDisconnected() {
        btnBtCtr.setText("Search Devices");

//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        Date date = new Date();
//        System.out.println(formatter.format(date));
//        new CHMSDeviceStatusAPI().execute(
//                userDetails.getOutput().get(0).getEmpCode(),
//                userDetails.getOutput().get(0).getDeviceTypeID(),
//                "0",
//                formatter.format(date),
//                userDetails.getOutput().get(0).getEmpCode()
//        );

    }

    @Override
    public void onReceiveData(byte[] dat) {
//        if (!mDataParser.isStart())
        mDataParser.add(dat);
    }


    @Override
    public void onSpO2WaveReceived(int dat) {
        wfSpO2.addAmp(dat);
    }

    @Override
    public void onSpO2Received(final com.myhindlab.abkat.data.SpO2 spo2) {
        SpO2 = spo2.getSpO2() != 127 ? spo2.getSpO2() : 00;
        spo2_HeartRate = spo2.getPulseRate() != 255 ? spo2.getPulseRate() : 00;
        SpO2Status = spo2.toString();
        Log.d(TAG, "onSpO2Received: " + spo2.toString());
        spData = spo2;


//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tvSPO2info.setText(spo2.toString());
//            }
//        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
//                if (spo2_HeartRate != 255) {
//                    new UpdateTempUI().execute(String.valueOf(spo2_HeartRate));
//                }
                Log.d("UI thread", "I am the UI thread");
//                if (session.getSPO() == null || session.getSPO().isEmpty()) {
//                    com.myhindlab.abkat.data.SpO2 sp = new Gson().fromJson(session.getSPO(), com.myhindlab.abkat.data.SpO2.class);
//
//                    tvSPO2info.setText("SPO2:-" + sp.getSpO2() + " Heart Rate:-" + sp.getPulseRate());
//                } else {
                tvSPO2info.setText("SPO2:-" + SpO2 + " Heart Rate:-" + spo2_HeartRate);
//                }

            }
        });

    }

//    public static class UpdateSpo2UI extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            if (tvSPO2info != null) {
//                tvSPO2info.setText(s);
//                session.setSPO(s);
//
//
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            return strings[0];
//        }
//    }

//    public static class UpdateTempUI extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            if (tvTEMPinfo != null) {
//                tvTEMPinfo.setText(s);
//                session.setTemp(s);
//
//
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            return strings[0];
//        }
//    }


    @Override
    public void onECGWaveReceived(int dat) {
        ECG = String.valueOf(dat);
        wfECG.addAmp(dat);
    }

    @Override
    public void onECGReceived(final com.myhindlab.abkat.data.ECG ecg) {
        ECGStatus = ecg.toString();
        ecg_heartRate = ecg.getHeartRate();
        ecg_restRate = ecg.getRestRate();
        Log.d(TAG, "onECGReceived: " + ecg.toString());

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tvECGinfo.setText(ecg.toString());
//            }
//        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread");
                tvECGinfo.setText(ecg.toString());

            }
        });

    }

    @Override
    public void onTempReceived(final Temp tempObj) {
        temp = tempObj.toString();
        temperature = tempObj.getTemperature();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tvTEMPinfo.setText(temp.toString());
//            }
//        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                session.setTemp(new Gson().toJson(tempObj));

                Log.d("UI thread", "I am the UI thread");
                tvTEMPinfo.setText(temp.toString());

            }
        });
    }

    @Override
    public void onNIBPReceived(final com.myhindlab.abkat.data.NIBP nibp) {
        bp_high = nibp.getHighPressure();
        bp_low = nibp.getLowPressure();
        bp_mean = nibp.getMeanPressure();
        NIBP = nibp.toString();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tvNIBPinfo.setText(nibp.toString());
//                edt_diastolic.setText(String.valueOf(nibp.getLowPressure()));
//                edt_systolic.setText(String.valueOf(nibp.getHighPressure()));
//            }
//        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread");
                tvNIBPinfo.setText(nibp.toString());
                edt_diastolic.setText(String.valueOf(nibp.getLowPressure()));
                edt_systolic.setText(String.valueOf(nibp.getHighPressure()));


                isBPManual = false;
                edt_systolic.setEnabled(false);
                edt_diastolic.setEnabled(false);


            }
        });
    }


    public class GetMachineDataStatus extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("USERID", userId));

            res = WebServiceCall.APICall(ApplicationConstants.GetMachineAvailabilityFlag_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<MachineDataStatusModel.Output> organizationList = new ArrayList<>();
                    MachineDataStatusModel pojoDetails = new Gson().fromJson(result, MachineDataStatusModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            if (pojoDetails.getOutput().size() > 0) {
                                MachineDataStatusModel.Output output = pojoDetails.getOutput().get(0);

                                IsWeightingMachineAvailable = output.getIsWeightingMachineAvailable();
                                IsBPMachineAvailable = output.getIsBPMachineAvailable();
                                IsSugarDeviceAvailable = output.getIsSugarDeviceAvailable();


                                if (isFromWeightMachineData.equals("1")) {
                                    edt_weight.setEnabled(false);
                                }


                                if (IsBPMachineAvailable.equals("1")) {
                                    edt_systolic.setEnabled(false);
                                    edt_diastolic.setEnabled(false);
                                }


                                if (IsSugarDeviceAvailable.equals("1")) {
                                    edt_bloodsugarr.setEnabled(false);
                                }


//                                edt_organization.setText(output.getSubOrgName());


//                                if (organizationId!=null){
//                                    new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new CampCalendar_Activity.GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }


                            }


//                            showOrganizationListDialog(organizationList);
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


    private void getMachineStatusList() {
        final ProgressDialog progressDialog = new ProgressDialog(HealthScreeningBasicHealth_Activity.this);
        Log.d(TAG, "getMachineStatusList: " + userId);
        progressDialog.setMessage("Please wait . . . " + userId);
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<MachineDataStatusModel> call = apiService.getMachineStatus(userId);
        call.enqueue(new Callback<MachineDataStatusModel>() {
            @Override
            public void onResponse(Call<MachineDataStatusModel> call, Response<MachineDataStatusModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<MachineDataStatusModel.Output> machineStatusList = response.body().getOutput();


                        if (machineStatusList != null && !machineStatusList.isEmpty()) {
                            MachineDataStatusModel.Output output = machineStatusList.get(0);


                            IsWeightingMachineAvailable = output.getIsWeightingMachineAvailable();
                            IsBPMachineAvailable = output.getIsBPMachineAvailable();
                            IsSugarDeviceAvailable = output.getIsSugarDeviceAvailable();


//                            IsWeightingMachineAvailable = "1";
//                            IsBPMachineAvailable = "1";
//                            IsSugarDeviceAvailable = "1";


                            if (IsWeightingMachineAvailable.equals("1")) {
                                edt_weight.setEnabled(false);
                            }


                            if (IsBPMachineAvailable.equals("1")) {
                                edt_systolic.setEnabled(false);
                                edt_diastolic.setEnabled(false);
                            }


                            if (IsSugarDeviceAvailable.equals("1")) {
                                edt_bloodsugarr.setEnabled(false);
                            }


                        }


                    } else {

//                        Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);


                    }
                } else {
//                    Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<MachineDataStatusModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


//    @Override
//    protected void onStop() {
//        localBroadcastManager.unregisterReceiver(broadcastReceiver);
//        mBtController.unregisterBroadcastReceiver(this);
//        super.onStop();
//    }

    @Override
    public void onFirmwareReceived(final String str) {
        Firmware = str;
    }

    @Override
    public void onHardwareReceived(final String str) {
        Hardware = str;
    }

    @Override
    public void onBackPressed() {
//        localBroadcastManager.unregisterReceiver(broadcastReceiver);
//        mBtController.unregisterBroadcastReceiver(this);
//        if (mBtController.isBTConnected())
//            mBtController.disconnect();
        finish();
        super.onBackPressed();
    }

    /**
     * Configure library functionalities
     */
    private void startOmronPeripheralManager(boolean isHistoricDataRead) {

        OmronPeripheralManagerConfig peripheralConfig = OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).getConfiguration();
        sampleLog.d(TAG, "Library Identifier : " + peripheralConfig.getLibraryIdentifier());

        // Filter device to scan and connect (optional)
        if (device != null && device.get(OmronConstants.OMRONBLEConfigDevice.GroupID) != null && device.get(OmronConstants.OMRONBLEConfigDevice.GroupIncludedGroupID) != null) {
            // Add item
            List<HashMap<String, String>> filterDevices = new ArrayList<>();
            filterDevices.add((HashMap<String, String>) device);
            peripheralConfig.deviceFilters = filterDevices;
        }

        ArrayList<HashMap> deviceSettings = new ArrayList<>();

        // Personal device settings (optional)
        deviceSettings = (ArrayList<HashMap>) getPersonalSettings(deviceSettings);

        // Scan settings (optional)
        deviceSettings = (ArrayList<HashMap>) getScanSettings(deviceSettings);

        peripheralConfig.deviceSettings = deviceSettings;
        // Set Scan timeout interval (optional)
        peripheralConfig.timeoutInterval = Constants.CONNECTION_TIMEOUT;
        // Set User Hash Id (mandatory)
        peripheralConfig.userHashId = "dk.dishank123@gmail.com"; // Set logged in user email
        // Disclaimer: Read definition before usage
        peripheralConfig.enableAllDataRead = isHistoricDataRead;
        // Set configuration for OmronPeripheralManager
        OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).setConfiguration(peripheralConfig);

        //Initialize the connection process.
        OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).startManager();

        // Notification Listener for BLE State Change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(mMessageReceiver, new IntentFilter(OmronConstants.OMRONBLEBluetoothStateNotification), RECEIVER_NOT_EXPORTED);
        } else {
            context.registerReceiver(mMessageReceiver, new IntentFilter(OmronConstants.OMRONBLEBluetoothStateNotification));
        }
        //Track instances of BroadcastReceiver.
        isReceiverRegistered = true;
    }


    private List<HashMap> getScanSettings(List<HashMap> deviceSettings) {

        // Scan Settings
        HashMap<String, Object> ScanModeSettings = new HashMap<>();
        HashMap<String, HashMap> ScanSettings = new HashMap<>();
        ScanModeSettings.put(OmronConstants.OMRONDeviceScanSettings.ModeKey, OmronConstants.OMRONDeviceScanSettingsMode.MismatchSequence);
        ScanSettings.put(OmronConstants.OMRONDeviceScanSettingsKey, ScanModeSettings);

        deviceSettings.add(ScanSettings);

        return deviceSettings;
    }

    private void transferData() {

//        identifier : HEM-7140T1-AP
//        deviceProtocol : OMRONWLPProtocol
//        image : hem7140t1
//        thumbnail : hem7140t1_thumbnail
//        sequenceNoKey : null
//        modelName : HEM-7140T1-AP
//        deviceGroupIncludedGroupIDKey : 1150
//        modelSeries : HEM-7140T1-AP
//        uuidKey : FC:CA:D1:75:81:16
//        deviceGroupIDKey : 0
//        selectedUserKey : 1
//        noOfUsers : 1
//        id : 1150
//        category : 0
//        LocalNameKey : BLESmart_0000047EFCCAD1758116
//        modelDisplayName : HEM-7140T1-AP

        String omronBPDetails = session.getOmronBPMac();
        Boolean isOmronBPPaired = session.isOmronBPPaired();


        if (!isOmronBPPaired) {
            if (omronBPDetails.equals("") && omronBPDetails.isEmpty()) {

                Utilities.showAlertDialog(context, "Alert", "कृपया प्रथम तुमचे Blood Pressure डिव्हाइस पेअर करा.\n" + "पेअरिंग मोड सक्षम करण्यासाठी डिव्हाइसवरील Bluetooth बटण दाबून धरून ठेवा, आणि नंतर \n" + "ॲपमधील \"Scan\" बटणावर टॅप करा.डिवाइस पेयर झाल्या नंतर \"tranfer\" बटणावर्ती क्लिक करा. ", false);
            }
            return;
        }


        mSelectedPeripheral = PairingDeviceData.changePeripheralObject(device);

        if (mSelectedPeripheral == null) {
            mTvErrorDesc.setText("Device Not Paired");
            return;
        }
        Log.i(TAG, "transferData: " + new Gson().toJson(mSelectedPeripheral));

        resetErrorMessage();
        enableDisableButton(false);
        resetVitalDataResult();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Transfer");
        alertDialogBuilder.setMessage("Do you want to transfer all historic readings from device?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                transferUsersDataWithPeripheral(true);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                transferUsersDataWithPeripheral(false);
            }
        });
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private ArrayList<HashMap> getBloodPressureSettings(ArrayList<HashMap> deviceSettings, boolean isPairing) {

        // Blood Pressure
        if (Integer.parseInt(device.get(OmronConstants.OMRONBLEConfigDevice.Category)) == OmronConstants.OMRONBLEDeviceCategory.BLOODPRESSURE) {
            HashMap<String, Object> bloodPressurePersonalSettings = new HashMap<>();
            bloodPressurePersonalSettings.put(OmronConstants.OMRONDevicePersonalSettings.BloodPressureTruReadEnableKey, OmronConstants.OMRONDevicePersonalSettingsBloodPressureTruReadStatus.On);
            bloodPressurePersonalSettings.put(OmronConstants.OMRONDevicePersonalSettings.BloodPressureTruReadIntervalKey, OmronConstants.OMRONDevicePersonalSettingsBloodPressureTruReadInterval.Interval30);
            HashMap<String, Object> settings = new HashMap<>();
            settings.put(OmronConstants.OMRONDevicePersonalSettings.BloodPressureKey, bloodPressurePersonalSettings);
            HashMap<String, HashMap> personalSettings = new HashMap<>();
            personalSettings.put(OmronConstants.OMRONDevicePersonalSettingsKey, settings);

            HashMap<String, Object> transferModeSettings = new HashMap<>();
            HashMap<String, HashMap> transferSettings = new HashMap<>();
            if (isPairing) {
                transferModeSettings.put(OmronConstants.OMRONDeviceScanSettings.ModeKey, OmronConstants.OMRONDeviceScanSettingsMode.Pairing);
            } else {
                transferModeSettings.put(OmronConstants.OMRONDeviceScanSettings.ModeKey, OmronConstants.OMRONDeviceScanSettingsMode.MismatchSequence);
            }
            transferSettings.put(OmronConstants.OMRONDeviceScanSettingsKey, transferModeSettings);

            // Personal settings for device
            deviceSettings.add(personalSettings);

            deviceSettings.add(transferSettings);
        }

        return deviceSettings;
    }

    private ArrayList<HashMap> getActivitySettings(ArrayList<HashMap> deviceSettings) {

        // Activity Tracker
        if (Integer.parseInt(device.get(OmronConstants.OMRONBLEConfigDevice.Category)) == OmronConstants.OMRONBLEDeviceCategory.ACTIVITY) {

            // Set Personal Settings in Configuration (mandatory for Activity devices)
            if (personalSettings != null) {

                HashMap<String, String> settingsModel = new HashMap<String, String>();
                settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.UserHeightKey, personalSettings.get("personalHeight"));
                settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.UserWeightKey, personalSettings.get("personalWeight"));
                settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.UserStrideKey, personalSettings.get("personalStride"));
                settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.TargetSleepKey, "120");
                settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.TargetStepsKey, "2000");

                HashMap<String, HashMap> userSettings = new HashMap<>();
                userSettings.put(OmronConstants.OMRONDevicePersonalSettingsKey, settingsModel);

                // Notification settings
                ArrayList<String> notificationsAvailable = new ArrayList<>();
                notificationsAvailable.add("android.intent.action.PHONE_STATE");
                notificationsAvailable.add("com.google.android.gm");
                notificationsAvailable.add("android.provider.Telephony.SMS_RECEIVED");
                notificationsAvailable.add("com.omronhealthcare.OmronConnectivitySample");
                HashMap<String, Object> notificationSettings = new HashMap<String, Object>();
                notificationSettings.put(OmronConstants.OMRONDeviceNotificationSettingsKey, notificationsAvailable);

                // Time Format
                HashMap<String, Object> timeFormatSettings = new HashMap<String, Object>();
                timeFormatSettings.put(OmronConstants.OMRONDeviceTimeSettings.FormatKey, OmronConstants.OMRONDeviceTimeFormat.Time12Hour);
                HashMap<String, HashMap> timeSettings = new HashMap<>();
                timeSettings.put(OmronConstants.OMRONDeviceTimeSettingsKey, timeFormatSettings);


                // Sleep Settings
                HashMap<String, Object> sleepTimeSettings = new HashMap<String, Object>();
                sleepTimeSettings.put(OmronConstants.OMRONDeviceSleepSettings.AutomaticKey, OmronConstants.OMRONDeviceSleepAutomatic.Off);
                sleepTimeSettings.put(OmronConstants.OMRONDeviceSleepSettings.StartTimeKey, "19");
                sleepTimeSettings.put(OmronConstants.OMRONDeviceSleepSettings.StopTimeKey, "20");
                HashMap<String, HashMap> sleepSettings = new HashMap<>();
                sleepSettings.put(OmronConstants.OMRONDeviceSleepSettingsKey, sleepTimeSettings);


                // Alarm Settings
                // Alarm 1 Time
                HashMap<String, Object> alarmTime1 = new HashMap<String, Object>();
                alarmTime1.put(OmronConstants.OMRONDeviceAlarmSettings.HourKey, "15");
                alarmTime1.put(OmronConstants.OMRONDeviceAlarmSettings.MinuteKey, "33");
                // Alarm 1 Day (SUN-SAT)
                HashMap<String, Object> alarmDays1 = new HashMap<String, Object>();
                alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.SundayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.MondayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.TuesdayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.WednesdayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.ThursdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
                alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.FridayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.SaturdayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                HashMap<String, Object> alarm1 = new HashMap<>();
                alarm1.put(OmronConstants.OMRONDeviceAlarmSettings.DaysKey, alarmDays1);
                alarm1.put(OmronConstants.OMRONDeviceAlarmSettings.TimeKey, alarmTime1);
                alarm1.put(OmronConstants.OMRONDeviceAlarmSettings.TypeKey, OmronConstants.OMRONDeviceAlarmType.Measure);


                // Alarm 2 Time
                HashMap<String, Object> alarmTime2 = new HashMap<String, Object>();
                alarmTime2.put(OmronConstants.OMRONDeviceAlarmSettings.HourKey, "15");
                alarmTime2.put(OmronConstants.OMRONDeviceAlarmSettings.MinuteKey, "34");
                // Alarm 2 Day (SUN-SAT)
                HashMap<String, Object> alarmDays2 = new HashMap<String, Object>();
                alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.SundayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.MondayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.TuesdayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.WednesdayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.ThursdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
                alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.FridayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.SaturdayKey, OmronConstants.OMRONDeviceAlarmStatus.Off);
                HashMap<String, Object> alarm2 = new HashMap<>();
                alarm2.put(OmronConstants.OMRONDeviceAlarmSettings.DaysKey, alarmDays2);
                alarm2.put(OmronConstants.OMRONDeviceAlarmSettings.TimeKey, alarmTime2);
                alarm2.put(OmronConstants.OMRONDeviceAlarmSettings.TypeKey, OmronConstants.OMRONDeviceAlarmType.Medication);

                // Add Alarm1, Alarm2, Alarm3 to List
                ArrayList<HashMap> alarms = new ArrayList<>();
                alarms.add(alarm1);
                alarms.add(alarm2);
                HashMap<String, Object> alarmSettings = new HashMap<>();
                alarmSettings.put(OmronConstants.OMRONDeviceAlarmSettingsKey, alarms);


                // Notification enable settings
                HashMap<String, Object> notificationEnableSettings = new HashMap<String, Object>();
                notificationEnableSettings.put(OmronConstants.OMRONDeviceNotificationStatusKey, OmronConstants.OMRONDeviceNotificationStatus.On);
                HashMap<String, HashMap> notificationStatusSettings = new HashMap<>();
                notificationStatusSettings.put(OmronConstants.OMRONDeviceNotificationEnableSettingsKey, notificationEnableSettings);


                deviceSettings.add(userSettings);
                deviceSettings.add(notificationSettings);
                deviceSettings.add(alarmSettings);
                deviceSettings.add(timeSettings);
                deviceSettings.add(sleepSettings);
                deviceSettings.add(notificationStatusSettings);
            }
        }

        return deviceSettings;
    }

    private ArrayList<HashMap> getBCMSettings(ArrayList<HashMap> deviceSettings) {

        // body composition
        if (Integer.parseInt(device.get(OmronConstants.OMRONBLEConfigDevice.Category)) == OmronConstants.OMRONBLEDeviceCategory.BODYCOMPOSITION) {

            //Weight settings
            HashMap<String, Object> weightPersonalSettings = new HashMap<>();
            weightPersonalSettings.put(OmronConstants.OMRONDevicePersonalSettings.WeightDCIKey, 100);

            HashMap<String, Object> settings = new HashMap<>();
            settings.put(OmronConstants.OMRONDevicePersonalSettings.UserHeightKey, "17000");
            settings.put(OmronConstants.OMRONDevicePersonalSettings.UserGenderKey, OmronConstants.OMRONDevicePersonalSettingsUserGenderType.Male);
            settings.put(OmronConstants.OMRONDevicePersonalSettings.UserDateOfBirthKey, "19001010");
            settings.put(OmronConstants.OMRONDevicePersonalSettings.WeightKey, weightPersonalSettings);

            HashMap<String, HashMap> personalSettings = new HashMap<>();
            personalSettings.put(OmronConstants.OMRONDevicePersonalSettingsKey, settings);

            // Weight Settings
            // Add other weight common settings if any
            HashMap<String, Object> weightCommonSettings = new HashMap<>();
            weightCommonSettings.put(OmronConstants.OMRONDeviceWeightSettings.UnitKey, OmronConstants.OMRONDeviceWeightUnit.Lbs);
            HashMap<String, Object> weightSettings = new HashMap<>();
            weightSettings.put(OmronConstants.OMRONDeviceWeightSettingsKey, weightCommonSettings);

            deviceSettings.add(personalSettings);
            deviceSettings.add(weightSettings);
        }

        return deviceSettings;
    }


    // Single User data transfer
    private void transferUsersDataWithPeripheral(final boolean isHistoricDataRead) {
        startOmronPeripheralManager(isHistoricDataRead);
        // Set State Change Listener
        setStateChanges();
        connectStatus = connectStatus_Scanning;
        OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).startDataTransferFromPeripheral(mSelectedPeripheral, selectedUsers, true, new OmronPeripheralManagerDataTransferListener() {
            @Override
            public void onDataTransferCompleted(OmronPeripheral peripheral, final OmronErrorInfo resultInfo) {
                if (resultInfo.isSuccess() && peripheral != null) {
                    mSelectedPeripheral = peripheral; // Saving for Transfer Function
                    OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).endDataTransferFromPeripheral(new OmronPeripheralManagerDataTransferListener() {
                        @Override
                        public void onDataTransferCompleted(final OmronPeripheral peripheral, final OmronErrorInfo resultInfo2) {
                            if (resultInfo2.isSuccess() && peripheral != null) {
                                ArrayList<HashMap<String, Object>> vitalDataList = null;
                                HashMap<String, Object> vitalData = (HashMap<String, Object>) peripheral.getVitalData();
                                HashMap<String, String> deviceInfo = (HashMap<String, String>) peripheral.getDeviceInformation();


                                String uuid = peripheral.getUuid();

                                if (uuid != null && !uuid.isEmpty()) {
                                    bloodPressureDevice = uuid;

                                    isBPManual = false;
                                    Log.d(TAG, "UUID: " + uuid);
                                } else {
                                    Log.d(TAG, "UUID not found or is empty in device information");
                                }


                                if (vitalData != null) {
                                    // VitalData Data
                                    vitalDataList = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataBloodPressureKey);
                                    preferencesManager.addDataStoredDeviceList(device.get(Constants.deviceInfoKeys.KEY_LOCAL_NAME), Integer.parseInt(device.get(OmronConstants.OMRONBLEConfigDevice.Category)), peripheral.getModelName(), device.get(OmronConstants.OMRONBLEConfigDevice.Identifier));
                                    insertVitalDataToDB(vitalDataList, deviceInfo);
                                }
                                // Setting Data
                                Map<String, Object> personalSettingsItem = null;
                                Object objectItem = peripheral.getDeviceSettingsWithUser(selectedUsers.get(0));
                                if (!(objectItem instanceof OmronErrorInfo)) {
                                    personalSettingsItem = (Map<String, Object>) objectItem;
                                }
                                showVitalDataResult(vitalDataList, personalSettingsItem, deviceInfo);
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setStatus("-");
                            mTvErrorCode.setText(resultInfo.getDetailInfo());
                            mTvErrorDesc.setText(resultInfo.getMessageInfo());
                            enableDisableButton(true);
                        }
                    });
                }
            }

        });
    }

    //individual setting
    private List<HashMap> getPersonalSettings(List<HashMap> deviceSettings) {
        HashMap<String, Object> bloodPressurePersonalSettings = new HashMap<>();
        HashMap<String, Object> settings = new HashMap<>();
        settings.put(OmronConstants.OMRONDevicePersonalSettings.BloodPressureKey, bloodPressurePersonalSettings);
        settings.put(OmronConstants.OMRONDevicePersonalSettings.UserDateOfBirthKey, personalData.getBirthdayNum());
        HashMap<String, HashMap> _personalSettings = new HashMap<>();
        _personalSettings.put(OmronConstants.OMRONDevicePersonalSettingsKey, settings);

        // Personal settings for device
        deviceSettings.add(_personalSettings);
        return deviceSettings;
    }


    private void disconnectDevice() {

        // Disconnect device using OmronPeripheralManager
        OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).disconnectPeripheral(mSelectedPeripheral, new OmronPeripheralManagerDisconnectListener() {
            @Override
            public void onDisconnectCompleted(OmronPeripheral peripheral, OmronErrorInfo resultInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HealthScreeningBasicHealth_Activity.this, "Device disconnected", Toast.LENGTH_SHORT).show();

                        enableDisableButton(true);
                    }
                });
            }
        });
    }


    /*******************************************************************************************/
    /************************ Section for Activity Device / HeartVue **************************/
    /*******************************************************************************************/

    /**
     * Insert Activity data
     */
//    private void insertActivityToDB(HashMap<String, Object> stepData, HashMap<String, String> deviceInfo, String type) {
//
//        ContentValues cv = new ContentValues();
//
//        cv.put(OmronDBConstans.ACTIVITY_DATA_StartDateUTCKey, String.valueOf(stepData.get(OmronConstants.OMRONActivityData.StartDateKey)));
//        cv.put(OmronDBConstans.ACTIVITY_DATA_EndDateUTCKey, String.valueOf(stepData.get(OmronConstants.OMRONActivityData.EndDateKey)));
//        cv.put(OmronDBConstans.ACTIVITY_DATA_MeasurementValueKey, String.valueOf(stepData.get(OmronConstants.OMRONActivityData.MeasurementKey)));
//        cv.put(OmronDBConstans.ACTIVITY_DATA_SeqNumKey, String.valueOf(stepData.get(OmronConstants.OMRONActivityData.SequenceKey)));
//        cv.put(OmronDBConstans.ACTIVITY_DATA_Type, type);
//
//        cv.put(OmronDBConstans.DEVICE_LOCAL_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.LocalNameKey).toLowerCase());
//        cv.put(OmronDBConstans.DEVICE_DISPLAY_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.DisplayNameKey));
//        cv.put(OmronDBConstans.DEVICE_IDENTITY_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.IdentityNameKey));
//        cv.put(OmronDBConstans.DEVICE_CATEGORY, device.get(OmronConstants.OMRONBLEConfigDevice.Category));
//
//        Uri uri = getContentResolver().insert(OmronDBConstans.ACTIVITY_DATA_CONTENT_URI, cv);
//        if (uri != null) {
//
//            ArrayList<HashMap<String, Object>> individualData = (ArrayList<HashMap<String, Object>>) stepData.get(OmronConstants.OMRONActivityData.DividedDataKey);
//            if (individualData != null) {
//                for (HashMap<String, Object> activityIndividual : individualData) {
//
//                    ContentValues dividedCV = new ContentValues();
//
//                    dividedCV.put(OmronDBConstans.ACTIVITY_DIVIDED_DATA_MainStartDateUTCKey, String.valueOf(stepData.get(OmronConstants.OMRONActivityData.StartDateKey)));
//                    dividedCV.put(OmronDBConstans.ACTIVITY_DIVIDED_DATA_StartDateUTCKey, String.valueOf(activityIndividual.get(OmronConstants.OMRONActivityData.DividedDataStartDateKey)));
//                    dividedCV.put(OmronDBConstans.ACTIVITY_DIVIDED_DATA_StartDateUTCKey, String.valueOf(activityIndividual.get(OmronConstants.OMRONActivityData.DividedDataStartDateKey)));
//                    dividedCV.put(OmronDBConstans.ACTIVITY_DIVIDED_DATA_MeasurementValueKey, String.valueOf(activityIndividual.get(OmronConstants.OMRONActivityData.DividedDataMeasurementKey)));
//                    dividedCV.put(OmronDBConstans.ACTIVITY_DIVIDED_DATA_SeqNumKey, String.valueOf(stepData.get(OmronConstants.OMRONActivityData.SequenceKey)));
//                    dividedCV.put(OmronDBConstans.ACTIVITY_DIVIDED_DATA_Type, type);
//                    dividedCV.put(OmronDBConstans.DEVICE_LOCAL_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.LocalNameKey).toLowerCase());
//
//                    Uri uriDivided = getContentResolver().insert(OmronDBConstans.ACTIVITY_DIVIDED_DATA_CONTENT_URI, dividedCV);
//
//                    if (uriDivided != null) {
//
//                    }
//                }
//            }
//        }
//    }
    private void insertVitalDataToDB(List<HashMap<String, Object>> dataList, Map<String, String> deviceInfo) {
        if (!dataList.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();

            for (HashMap<String, Object> bloodPressureItem : dataList) {

                ContentValues cv = new ContentValues();

                calendar.setTimeInMillis((Long) bloodPressureItem.get(OmronConstants.OMRONVitalData.StartDateKey));

                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataCuffFlagKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.CuffFlagKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataDiastolicKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.DiastolicKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataIrregularFlagKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.IrregularFlagKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataMeasurementDateKey, format.format(calendar.getTime()));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataMovementFlagKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.MovementFlagKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataPulseKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.PulseKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataSystolicKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.SystolicKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataMeasurementDateUTCKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.StartDateKey)));

                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataAtrialFibrillationDetectionFlagKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.AtrialFibrillationDetectionFlagKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataMeasurementModeKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.MeasurementModeKey)));

                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataPositionIndicatorKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.PositioningIndicatorKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataConsecutiveMeasurementKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.ConsecutiveMeasurementKey)));
                cv.put(OmronDBConstans.VITAL_DATA_OMRONVitalDataIrregularHeartBeatCountKey, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.IrregularHeartBeatCountKey)));

                cv.put(OmronDBConstans.DEVICE_SELECTED_USER, String.valueOf(bloodPressureItem.get(OmronConstants.OMRONVitalData.UserIdKey)));
                cv.put(OmronDBConstans.DEVICE_LOCAL_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.LocalNameKey).toLowerCase());

                getContentResolver().insert(OmronDBConstans.VITAL_DATA_CONTENT_URI, cv);
            }
        }
    }

    /**
     * Insert sleep data
     */
    private void insertSleepToDB(ArrayList<HashMap<String, Object>> dataList, HashMap<String, String> deviceInfo) {

        for (HashMap<String, Object> sleepingDataItem : dataList) {

            ContentValues cv = new ContentValues();

            cv.put(OmronDBConstans.SLEEP_DATA_SleepStartTimeKey, String.valueOf(sleepingDataItem.get(OmronConstants.OMRONSleepData.TimeInBedKey)));
            cv.put(OmronDBConstans.SLEEP_DATA_SleepOnSetTimeKey, String.valueOf(sleepingDataItem.get(OmronConstants.OMRONSleepData.SleepOnsetTimeKey)));
            cv.put(OmronDBConstans.SLEEP_DATA_WakeUpTimeKey, String.valueOf(sleepingDataItem.get(OmronConstants.OMRONSleepData.WakeTimeKey)));
            cv.put(OmronDBConstans.SLEEP_DATA_SleepingTimeKey, String.valueOf(sleepingDataItem.get(OmronConstants.OMRONSleepData.TotalSleepTimeKey)));
            cv.put(OmronDBConstans.SLEEP_DATA_SleepEfficiencyKey, String.valueOf(sleepingDataItem.get(OmronConstants.OMRONSleepData.SleepEfficiencyKey)));
            cv.put(OmronDBConstans.SLEEP_DATA_SleepArousalTimeKey, String.valueOf(sleepingDataItem.get(OmronConstants.OMRONSleepData.ArousalDuringSleepTimeKey)));
            cv.put(OmronDBConstans.SLEEP_DATA_SleepBodyMovementKey, sleepingDataItem.get(OmronConstants.OMRONSleepData.BodyMotionLevelKey).toString());

            cv.put(OmronDBConstans.SLEEP_DATA_StartDateUTCKey, String.valueOf(sleepingDataItem.get(OmronConstants.OMRONSleepData.StartDateKey)));
            cv.put(OmronDBConstans.SLEEP_DATA_StartEndDateUTCKey, String.valueOf(sleepingDataItem.get(OmronConstants.OMRONSleepData.EndDateKey)));


            cv.put(OmronDBConstans.DEVICE_LOCAL_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.LocalNameKey).toLowerCase());
            cv.put(OmronDBConstans.DEVICE_DISPLAY_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.DisplayNameKey));
            cv.put(OmronDBConstans.DEVICE_IDENTITY_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.IdentityNameKey));
            cv.put(OmronDBConstans.DEVICE_CATEGORY, device.get(OmronConstants.OMRONBLEConfigDevice.Category));

            Uri uri = getContentResolver().insert(OmronDBConstans.SLEEP_DATA_CONTENT_URI, cv);
            if (uri != null) {
                //TODO successful insert
            }
        }
    }


    private void showVitalDataResult(final List<HashMap<String, Object>> vitalData, final Map<String, Object> personalSettingsItem, final Map<String, String> deviceInfo) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                resetVitalDataResult();

                if (vitalData.size() == 0) {

                    mTvErrorDesc.setText("No New readings transferred");

                } else {
                    HashMap<String, Object> vitalDataItem = vitalData.get(vitalData.size() - 1);

                    mTvErrorDesc.setText("-");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis((Long) vitalDataItem.get(OmronConstants.OMRONVitalData.StartDateKey));
                    mTvTImeStamp.setText(com.myhindlab.abkat.omron.utility.Utilities.getDateTime(calendar.getTime()));
                    setEditTextView(edt_systolic, vitalDataItem, OmronConstants.OMRONVitalData.SystolicKey, "\t mmHg");
                    setEditTextView(edt_diastolic, vitalDataItem, OmronConstants.OMRONVitalData.DiastolicKey, "\t mmHg");
                    setTextView(edt_systolic, vitalDataItem, OmronConstants.OMRONVitalData.SystolicKey, "\t mmHg");
                    setTextView(mTvDiastolic, vitalDataItem, OmronConstants.OMRONVitalData.DiastolicKey, "\t mmHg");
                    setTextView(mTvPulseRate, vitalDataItem, OmronConstants.OMRONVitalData.PulseKey, "\t bpm");

                    Object objectData;
                    objectData = vitalDataItem.get(OmronConstants.OMRONVitalData.UserIdKey);
                    if (objectData != null) {
                        mTvUserSelected.setText("User " + objectData);
                    }

                    objectData = vitalDataItem.get(OmronConstants.OMRONVitalData.SequenceKey);
                    if (objectData != null) {
                        mSequenceNoString = String.valueOf(objectData);
                        if (isDevelopmentTest) {
//                            mTvSeqNum.setText(mSequenceNoString);
                        }
                    }
//                    PairingDeviceData.addPairingDataToDB(mSelectedPeripheral, mSequenceNoString);
                }
                Map<String, Object> _info = new HashMap<String, Object>(deviceInfo);
//                setTextView(mTvBatteryRem,_info,OmronConstants.OMRONDeviceInformation.BatteryRemainingKey,"%");
                if (personalSettingsItem != null) {

                    String stringDateOfBirth = personalSettingsItem.get(OmronConstants.OMRONDevicePersonalSettings.UserDateOfBirthKey).toString();
                    Date date = com.myhindlab.abkat.omron.utility.Utilities.getDateOfBirthDateType(stringDateOfBirth, "yyyyMMdd");
                    stringDateOfBirth = com.myhindlab.abkat.omron.utility.Utilities.getDate(date);
//                    mTvDateOfBirth.setText(stringDateOfBirth);                    if(isDevelopmentTest) {
                    HashMap<String, Object> bloodPressurePersonalItem = (HashMap<String, Object>) personalSettingsItem.get(OmronConstants.OMRONDevicePersonalSettings.BloodPressureKey);
//                        if (bloodPressurePersonalItem != null) {
//                            setTextView(mTvTruReadEnable,bloodPressurePersonalItem,OmronConstants.OMRONDevicePersonalSettings.BloodPressureTruReadEnableKey,"");
//                            setTextView(mTvTruReadInterval,bloodPressurePersonalItem,OmronConstants.OMRONDevicePersonalSettings.BloodPressureTruReadIntervalKey,"");
//                        }
                }
            }
//            }
        });

    }

    private void resetVitalDataResult() {
        mTvTImeStamp.setText("-");
        mTvSystolic.setText("-");
        mTvDiastolic.setText("-");
        mTvPulseRate.setText("-");
        edt_systolic.setText("-");
        edt_diastolic.setText("-");
//        mTvDateOfBirth.setText("-");
//        mTvBatteryRem.setText("-");
//        if(isDevelopmentTest){
//            mTvTruReadEnable.setText("-");
//            mTvTruReadInterval.setText("-");
//            mTvSeqNum.setText("-");
//        }
        mTvUserSelected.setText("-");
    }

    private void setTextView(TextView view, Map<String, Object> dataList, String key, String addText) {
        Object objectData = dataList.get(key);
        if (objectData != null) {
            view.setText(objectData + addText);
        }
    }

    private void setEditTextView(MaterialEditText view, Map<String, Object> dataList, String key, String addText) {
        Object objectData = dataList.get(key);
        if (objectData != null) {
            view.setText(objectData + addText);
            view.setEnabled(false);
        } else {
            view.setEnabled(true);

        }
    }


    private void insertRecordToDB(ArrayList<HashMap<String, Object>> dataList, HashMap<String, String> deviceInfo) {

        for (HashMap<String, Object> recordDataItem : dataList) {

            ContentValues cv = new ContentValues();
            cv.put(OmronDBConstans.RECORD_DATA_StartDateUTCKey, String.valueOf(recordDataItem.get(OmronConstants.OMRONRecordData.DateKey)));

            cv.put(OmronDBConstans.DEVICE_LOCAL_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.LocalNameKey).toLowerCase());
            cv.put(OmronDBConstans.DEVICE_DISPLAY_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.DisplayNameKey));
            cv.put(OmronDBConstans.DEVICE_IDENTITY_NAME, deviceInfo.get(OmronConstants.OMRONDeviceInformation.IdentityNameKey));
            cv.put(OmronDBConstans.DEVICE_CATEGORY, device.get(OmronConstants.OMRONBLEConfigDevice.Category));

            Uri uri = getContentResolver().insert(OmronDBConstans.RECORD_DATA_CONTENT_URI, cv);
            if (uri != null) {
                //TODO successful insert
            }
        }
    }


    private void setStateChanges() {

        // Listen to Device state changes using OmronPeripheralManager
        OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).onConnectStateChange(new OmronPeripheralManagerConnectStateListener() {

            @Override
            public void onConnectStateChange(final int state) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String status = "-";

                        if (state == OmronConstants.OMRONBLEConnectionState.CONNECTING) {
                            status = "Connecting...";
                        } else if (state == OmronConstants.OMRONBLEConnectionState.CONNECTED) {
                            status = "Connected";
                        } else if (state == OmronConstants.OMRONBLEConnectionState.DISCONNECTING) {
                            status = "Disconnecting...";
                        } else if (state == OmronConstants.OMRONBLEConnectionState.DISCONNECTED) {
                            status = "Disconnected";
                        }
                        setStatus(status);
                    }
                });
            }
        });
    }

    private void setStatus(String statusMessage) {
        mTvStatusLabel.setText(statusMessage);
    }


    private void resetErrorMessage() {

        mTvErrorCode.setText("-");
        mTvErrorDesc.setText("-");
        mTvStatusLabel.setText("-");

    }

    private void enableDisableButton(boolean enable) {

//        findViewById(R.id.iv_vital_data).setEnabled(enable);
//        btnOmronBPSync.setEnabled(enable);
//        transferBtn.setEnabled(enable);
    }


    private void resetDeviceList() {

        if (mScannedDevicesAdapter != null) {
            mProgressBar.setVisibility(View.GONE);
            mPeripheralList = new ArrayList<OmronPeripheral>();
            mScannedDevicesAdapter.setPeripheralList(mPeripheralList);
            mScannedDevicesAdapter.notifyDataSetChanged();
        }
    }

    private void showDeviceListView() {
        mRlTransferView.setVisibility(View.GONE);
        mRlDeviceListView.setVisibility(View.VISIBLE);
    }

    private void showTransferView() {
        mRlDeviceListView.setVisibility(View.GONE);
        mRlTransferView.setVisibility(View.VISIBLE);
        setDeviceInformation();
    }

    private void setDeviceInformation() {
        if (null != mSelectedPeripheral) {
            if (null != mSelectedPeripheral.getModelName()) {
                mTvDeviceInfo.setText(mSelectedPeripheral.getModelName() + " - " + getString(R.string.device_information));
            } else {
                mTvDeviceInfo.setText(getString(R.string.device_information));

            }
        }
    }

    private void stopOmronPeripheralManager() {

        OmronPeripheralManager.sharedManager(HealthCheckup.getInstance().getApplicationContext()).stopManager();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get extra data included in the Intent
            int status = intent.getIntExtra(OmronConstants.OMRONBLEBluetoothStateKey, 0);

            if (status == OmronConstants.OMRONBLEBluetoothState.OMRONBLEBluetoothStateUnknown) {

                Log.d(TAG, "Bluetooth is in unknown state");

            } else if (status == OmronConstants.OMRONBLEBluetoothState.OMRONBLEBluetoothStateOff) {

                Log.d(TAG, "Bluetooth is currently powered off");

            } else if (status == OmronConstants.OMRONBLEBluetoothState.OMRONBLEBluetoothStateOn) {

                Log.d(TAG, "Bluetooth is currently powered on");
            }
        }
    };

    public void showMessage(String title, String message) {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });


        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /// //////////////   监听相关代码 Begin   /////////////////

    private IBleStateListener mIBleStateListener = new IBleStateListener() {

        @Override
        public void onBleOpen() {
//            startScanBle();
        }
    };


//    private IBleScanListener mScanListener = new IBleScanListener() {
//
//        @Override
//        public void onScanStarted() {
//            Log.e(TAG, "onScanStarted");
//
//            //1.清除列表视图
//            mAdapter.setNewData(null);
//
////            //2.
////            clStartScan.setVisibility(View.GONE);
////            clStopScan.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onScanCanceled() {
//            Log.e(TAG, "onScanCanceled");
//
////            clStopScan.setVisibility(View.GONE);
////            clStartScan.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onScanStop() {
//            Log.e(TAG, "onScanStop");
//
////            clStopScan.setVisibility(View.GONE);
////            clStartScan.setVisibility(View.VISIBLE);
//        }
//
////        @Override
////        public void onDeviceFounded(SearchResult device) {
////            Log.e(TAG, "onDeviceFounded");
////
////            if (null == device) {
////                return;
////            }
////
////
////            try {
////
////
////                if (!device.getName().contains(mDeviceName)) {
////                    return;
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            DeviceInfoDetailEntity deviceInfo = new DeviceInfoDetailEntity();
////
////            deviceInfo.setDeviceName(device.getName());
////            deviceInfo.setMacAddress(device.getAddress());
////            List<DeviceInfoDetailEntity> entities = mAdapter.getData();
////
////            boolean isExist = false;
////
////            for (int i = 0; i < entities.size(); i++) {
////                DeviceInfoDetailEntity entity = entities.get(i);
////                String macAddress = entity.getMacAddress();
////
////                if (macAddress.equals(device.getAddress())) {
////                    isExist = true;
////                    break;
////                }
////            }
////
////            if (isExist) {
////                return;
////            }
////
////            mAdapter.addData(deviceInfo);
////            scrollBottom();
////        }
//
//    };


    /////////////////   监听相关代码 End     /////////////////

    ///////////////////////////////////////////////////////////

//    private BluetoothClient mBtClient;
//
//
//    /**
//     * 开启蓝牙
//     */
//    private void openBluetooth() {
//        if (null == mBtClient) {
//            return;
//        }
//
//        mBtClient.openBluetooth();
//        mBtClient.registerBluetoothStateListener(mBtStateListener);
//
//    }
//
//    /**
//     * 搜索蓝牙外设
//     */
//    private void startScanBle() {
//        if (null == mBtClient) {
//            return;
//        }
//
//        //以下为测试
//        /*
//        mAdapter.setNewData(null);
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run()
//            {
//                mBtClient.search(mSearchRequest, mSearchResponse);
//            }
//        }, 100);//1秒后执行Runnable中的run方法
//        */
//
//
//
//        mBtClient.search(mSearchRequest, mSearchResponse);
//
//    }
//
//    private void stopScanBle() {
//        if (null == mBtClient) {
//            return;
//        }
//
//        mBtClient.stopSearch();
//    }
//
//    private void scrollBottom() {
//        rv_list.scrollToPosition(mAdapter.getData().size() - 1);
//    }
//
//    /////////////////   开启蓝牙相关代码 Begin   /////////////////
//    private BluetoothStateListener mBtStateListener = new BluetoothStateListener() {
//
//        @Override
//        public void onBluetoothStateChanged(boolean openOrClosed) {
//            if (null != mIBleStateListener && openOrClosed) {
//                mIBleStateListener.onBleOpen();
//            }
//
//        }
//    };
//
//    /////////////////   开启蓝牙相关代码 End     /////////////////
//
//    /////////////////   搜索蓝牙外设相关代码 Begin   /////////////////
//    private SearchRequest mSearchRequest = new SearchRequest.Builder()
//            .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
//            .searchBluetoothClassicDevice(9000) // 再扫经典蓝牙5s,在实际工作中没用到经典蓝牙的扫描
//            .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
//            .build();
//
//    private SearchResponse mSearchResponse = new SearchResponse() {
//
//        //开始搜素
//        @Override
//        public void onSearchStarted() {
//            if (null != mScanListener) {
//                mScanListener.onScanStarted();
//            }
//        }
//
//        //找到设备 可通过manufacture过滤
//        @Override
//        public void onDeviceFounded(SearchResult device) {
//            if (null != mScanListener) {
//                mScanListener.onDeviceFounded(device);
//            }
//        }
//
//        //搜索停止
//        @Override
//        public void onSearchStopped() {
//            if (null != mScanListener) {
//                mScanListener.onScanStop();
//                pd.dismiss();
//
//            }
//        }
//
//        //搜索取消
//        @Override
//        public void onSearchCanceled() {
//            if (null != mScanListener) {
//                mScanListener.onScanCanceled();
//                pd.dismiss();
//
//            }
//        }
//    };

    /// //////////////   搜索蓝牙外设相关代码 End     /////////////////

    //private function implementation
    private String getToolbarTitle() {
        String titleString = "";

        if (Constant.DeviceType_BSugar == mDeviceType) {
            titleString = "Blood Glucose Meter";
        } else if (Constant.DeviceType_BPres == mDeviceType) {
            titleString = "Blood Pressure Monitor";
        } else if (Constant.DeviceType_themo == mDeviceType) {
            titleString = "Forehead Thermometer";
        } else if (Constant.DeviceType_UricAcid == mDeviceType) {
            titleString = "Uric Acid Meter";
        } else if (Constant.DeviceType_LosonBSugar == mDeviceType) {
            titleString = "Lesheng Blood Glucose Meter";
        } else if (Constant.DeviceType_TAIDOCBGM == mDeviceType) {
            titleString = "Taibo Blood Glucose Meter";
        } else if (Constant.DeviceType_BSugarAndUric == mDeviceType) {
            titleString = "Aiole Blood Sugar and Uric Acid 2-in-1";
        }

        return titleString;
    }

    private String getDeviceName() {
        String deviceName = "";

        if (Constant.DeviceType_BSugar == mDeviceType) {
            deviceName = "Bioland-BGM";
        } else if (Constant.DeviceType_BPres == mDeviceType) {
            deviceName = "Bioland-BPM";
        } else if (Constant.DeviceType_themo == mDeviceType) {
            deviceName = "Bioland-IT";
        } else if (Constant.DeviceType_UricAcid == mDeviceType) {
            deviceName = "Bioland-BUM";
        } else if (Constant.DeviceType_LosonBSugar == mDeviceType) {
            deviceName = "DS5";
        } else if (Constant.DeviceType_TAIDOCBGM == mDeviceType) {
            deviceName = "TAIDOC TD4286";
        } else if (Constant.DeviceType_BSugarAndUric == mDeviceType) {
            deviceName = "Bioland-BMM";
        }

        return deviceName;
    }


//    private void onBackClick() {
//        stopScanBle();
//        finish();
//    }


//    private void onStartScanClick() {
//        if (!mBtClient.isBluetoothOpened()) {
//            openBluetooth();
//            return;
//        }
//        startScanBle();
//    }

//    private void onStopScanClick() {
//        stopScanBle();
//    }

    private void onItemClickHandler(int position) {

        biolandDevice = mAdapter.getItem(position);

        Log.d(TAG, "onItemClickHandler: " + new Gson().toJson(biolandDevice));

        macAddress = biolandDevice.getMacAddress();
        Utilities.showAlertDialog(context, "Confirm!", "Do you want to connect to " + biolandDevice.getDeviceName(), true, "Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


//                BLEMgr.connect(macAddress, mConnectListener);
            }
        }, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
//        if (Constant.DeviceType_BSugar == mDeviceType) {
//            GotoActivity(BloodSugarActivity.class,data);
//        } else if (Constant.DeviceType_BPres == mDeviceType) {
//            GotoActivity(BldPressureActivity.class,data);
//        } else if (Constant.DeviceType_themo == mDeviceType) {
//
//        } else if (Constant.DeviceType_UricAcid == mDeviceType) {
//            GotoActivity(UricAcidActivity.class,data);
//        } else if (Constant.DeviceType_LosonBSugar == mDeviceType) {
//            GotoActivity(LosonBSugarActivity.class,data);
//        } else if (Constant.DeviceType_TAIDOCBGM == mDeviceType) {
//            GotoActivity(TaiDocBGMActivity.class,data);
//        }else if (Constant.DeviceType_BSugarAndUric == mDeviceType) {
//            GotoActivity(SugarAndUricActivity.class,data);
//        }
    }


    private void GotoActivity(Class mClass, DeviceInfoDetailEntity data) {
        if (null == data) {
            return;
        }

        Intent intent = new Intent(this, mClass);

        Bundle bundle = new Bundle();
        bundle.putString("MacAddress", data.getMacAddress());

        intent.putExtras(bundle);
        startActivity(intent);

//        stopScanBle();
        finish();
    }


    /**
     * 请求位置权限
     */
//    private void requestPermission() {
//        final String[] permissionsGroup = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//
//        if (ZPermissionUtil.getInstance().isPermissions(HealthScreeningBasicHealth_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) &&
//                ZPermissionUtil.getInstance().isPermissions(HealthScreeningBasicHealth_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            onStartScanClick();
//        } else {
//            //
//            ZPermissionUtil.getInstance().requestPermissions(HealthScreeningBasicHealth_Activity.this, HealthScreeningBasicHealth_Activity.this, permissionsGroup);
//        }
//
//
//    }
    @Override
    public void onPermissionsSuccess() {
//        onStartScanClick();
    }

    @Override
    public void onPermissionsFail() {
//        ZToast.show(getString(R.string.location_permission_tip));
        finish();
    }

    private void initListener() {
        rv_list.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClickHandler(position);
            }
        });
    }

    private void initList() {
        //设置布局管理器
        LinearLayoutManager listMgr = new LinearLayoutManager(this);
        rv_list.setLayoutManager(listMgr);

        //设置适配器
        mAdapter = new DeviceListAdapter(null);
        rv_list.setAdapter(mAdapter);

    }


    /**
     * 连接血糖仪相关代码 Begin
     */

//    private IBleConnectListener mConnectListener = new IBleConnectListener() {
//
//        @Override
//        public void onStartConnect() {
//            Log.e("BloodSugarActivity", ">>>IBleConnectListener::onStartConnect");
//            //1.
//            mStatus = Status_Connecting;
//            tvBiolandMsg.setText("Bluetooth device connecting...");
//
//
//            //2.
////            clDisConnect.setVisibility(View.INVISIBLE);
////            clConnect.setVisibility(View.INVISIBLE);
//
//            //3.
//            isReady = false;
//        }
//
//        @Override
//        public void onConnectFail() {
//            Log.e("BloodSugarActivity", ">>>IBleConnectListener::onConnectFail");
//
//            //1.
//            mStatus = Status_ConnectFail;
//            tvBiolandMsg.setText("Bluetooth device connection failed！");
//
//            //2.
////            clConnect.setVisibility(View.VISIBLE);
////            clDisConnect.setVisibility(View.GONE);
//
//            //3.
//            isReady = false;
//        }
//
////        @Override
////        public void onConnectSuccess(int code, BleGattProfile data, String mac) {
////            Log.e("BloodSugarActivity", ">>>IBleConnectListener::onConnectSuccess");
////
////            //1.
////            mStatus = Status_Connected;
////            tvBiolandMsg.setText("Bluetooth device connected successfully！");
////            rv_list.setVisibility(View.GONE);
////            btnGetBiolandSugarData.setVisibility(View.VISIBLE);
////            tvBiolandDeviceInfo.setVisibility(View.VISIBLE);
////            tvBiolandDeviceInfo.setText("Name:- " + biolandDevice.getDeviceName() + "\nMac Address:-" + biolandDevice.getMacAddress());
////
////            //2.
//////            clDisConnect.setVisibility(View.VISIBLE);
//////            clConnect.setVisibility(View.GONE);
////
////            //3.订阅通知
////            if (!isReady) {
////                indicate();
////            }
////        }
//
//        @Override
//        public void onDisConnected(String mac) {
//            Log.e("BloodSugarActivity", ">>>IBleConnectListener::onDisConnected");
//
//            if (Status_Connected == mStatus) {
//                //1.
//                mStatus = Status_DisConnected;
//                tvBiolandMsg.setText("Bluetooth device connection has been disconnected！");
//                tvBiolandDeviceInfo.setVisibility(View.GONE);
//                //2.
////                clConnect.setVisibility(View.VISIBLE);
////                clDisConnect.setVisibility(View.GONE);
//
//                //3.
//                isReady = false;
//            }
//
//        }
//    };
    private void sendInfoPacket() {
        BGMSendPacket sendPacket;

        sendPacket = new BGMSendPacket(BGMSendPacket.Packet_Type_Info);

        write(macAddress, sendPacket.getData());

        String displayStr;

        displayStr = "Send Packet: [" + sendPacket.getHexData() + "]";

        tvBiolandMsg.setText(displayStr);
//        showContent(displayStr, 1);

        sendDataPacket();
    }

    private void sendDataPacket() {
        BGMSendPacket sendPacket;

        sendPacket = new BGMSendPacket(BGMSendPacket.Packet_Type_Data);

        write(macAddress, sendPacket.getData());

        String displayStr;

        displayStr = "Send packet: [" + sendPacket.getHexData() + "]";

//        showContent(displayStr, 1);
    }

    //

    private void showContent(String content, int which) {
//        if (0 == which) {
//            //1.
//            mUpAdapter.addData(content);
//
//            //2.滚动到底部
//            rvUpList.scrollToPosition(mUpAdapter.getData().size() - 1);
//        } else if (1 == which) {
//            //1.
//            mDownAdapter.addData(content);
//
//            //2.滚动到底部
//            rvDownList.scrollToPosition(mDownAdapter.getData().size() - 1);
//        }

        edt_bloodsugarr.setText(content);

        btnGetBiolandSugarData.setVisibility(View.GONE);
    }

    private String bytes2Hex(byte[] data, int length) {
        String hexString = "";

        for (int i = 0; i < length; i++) {
            String temp = Integer.toHexString(data[i] & 0xFF);
            if (1 == temp.length()) {
                temp = "0" + temp;
            }
            hexString += temp.toUpperCase();
        }

        return hexString;
    }


    /**
     * 订阅通知相关代码 Begin
     */
    private IBleIndicateListener mIndicateListener = new IBleIndicateListener() {

        @Override
        public void onIndicateSuccess() {
            Log.e("BloodSugarActivity", ">>>IBleIndicateListener::onIndicateSuccess");

            isReady = true;

        }

        @Override
        public void onIndicateFailure(int code) {
            Log.e("BloodSugarActivity", ">>>IBleIndicateListener::onIndicateFailure");

            showContent("Failed to send data！", 1);
        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            Log.e("BloodSugarActivity", ">>>IBleIndicateListener::onCharacteristicChanged");
            Log.e("BloodSugarActivity", "RecvData = [" + bytes2Hex(data, data.length) + "]");


            //
            BGMRecvPacket recvPacket = new BGMRecvPacket(data);

            String displayStr;

            //
            displayStr = "data received:";
            showContent(displayStr, 1);

            displayStr = "[" + recvPacket.getHexData() + "]";
            showContent(displayStr, 1);

            Log.e("BloodSugarActivity", "type = [" + recvPacket.getType() + "]");

            if (BGMRecvPacket.Packet_Type_Info == recvPacket.getType()) {
                displayStr = "serial number: " + recvPacket.getDeviceSN();

                showContent(displayStr, 0);

            } else if (BGMRecvPacket.Packet_Type_CountDown == recvPacket.getType()) {
                displayStr = "Measure countdown: " + recvPacket.getCountDown() + "秒";

                showContent(displayStr, 0);
            } else if (BGMRecvPacket.Packet_Type_Data == recvPacket.getType()) {
                double mgdlData = (18.018 * Double.parseDouble(recvPacket.getDataOfmmol()));
//                displayStr = "Blood glucose measurement data: " +mgdlData +" mmol/L" + " at " + recvPacket.getMeasureTime();
                displayStr = mgdlData + "";

                showContent(displayStr, 0);

            } else if (BGMRecvPacket.Packet_Type_Over == recvPacket.getType()) {
                //do nothing
                displayStr = "No historical data！";

                showContent(displayStr, 0);

            }

        }
    };


    //private function implementation
    private void indicate() {
//        BLEMgr.indicate(macAddress, Constant.GATT_SERVICE_PRIMARY, Constant.GATT_CHAR_READ, mIndicateListener);
    }

    private void unIndicate() {
//        BLEMgr.unIndicate(macAddress, Constant.GATT_SERVICE_PRIMARY, Constant.GATT_CHAR_READ);
    }


    /**
     * 发送数据相关代码 Begin
     */
    private IBleWriteListener mWriteListener = new IBleWriteListener() {

        @Override
        public void onWriteSuccess() {
            Log.e("BloodSugarActivity", ">>>IBleWriteListener::onWriteSuccess");
        }

        @Override
        public void onWriteFailure(int code) {
            Log.e("BloodSugarActivity", ">>>IBleWriteListener::onWriteFailure");
        }
    };


    //private function implementation
    private void write(final String mac, final byte[] data) {
//        BLEMgr.write(mac, Constant.GATT_SERVICE_PRIMARY, Constant.GATT_CHAR_WRITE, data, mWriteListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String omronBPDetails = session.getOmronBPMac();
        Boolean isOmronBPPaired = session.isOmronBPPaired();
        Log.i(TAG, "onResume: " + isOmronBPPaired);
        if (isOmronBPPaired) {
            if (omronBPDetails != null && !omronBPDetails.isEmpty()) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, String> map = gson.fromJson(omronBPDetails, type);

                    Log.d(TAG, "onResume: " + omronBPDetails);
                    System.out.println(map);
                    device = map;
                    scanBtn.setVisibility(View.GONE);
                    omronMsg.setText("Note:- You have already paired " + device.get(Constants.deviceInfoKeys.KEY_LOCAL_NAME) + " Mac:" + device.get(Constants.deviceInfoKeys.KEY_UUID) + " device\nIf you want change the device click setting Icon.");

//                    Utilities.showAlertDialog(context,"Alert","तुम्ही डिव्हाइस यशस्वीरित्या जोडले आहे, डिव्हाइसमधून डेटा मिळविण्यासाठी ट्रान्सफर बटणावर टॅप करा.",false);
//                    transferData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                omronMsg.setText("Note:- You dont have any OMRON Blood Pressure device paired, please scan to pair.");
            }
        } else {
            omronMsg.setText("Note:- You dont have any OMRON Blood Pressure device paired, please scan to pair.");

//            Utilities.showAlertDialog(context, "Alert", "कृपया प्रथम तुमचे Blood Pressure डिव्हाइस पेअर करा.\n" +
//                    "पेअरिंग मोड सक्षम करण्यासाठी डिव्हाइसवरील Bluetooth बटण दाबून धरून ठेवा, आणि नंतर \n" +
//                    "ॲपमधील \"Scan\" बटणावर टॅप करा.डिवाइस पेयर झाल्या नंतर \"tranfer\" बटणावर्ती क्लिक करा. ", false);

        }

    }

}
