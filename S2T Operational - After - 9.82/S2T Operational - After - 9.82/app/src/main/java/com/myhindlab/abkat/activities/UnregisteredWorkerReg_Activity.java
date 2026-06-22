package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.morpho.morphosmart.sdk.ErrorCodes;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.fingerprint.dao.AuthBfdCap;
import com.myhindlab.abkat.fingerprint.fps.MorphoTabletFPSensorDevice;
import com.myhindlab.abkat.pojos.InsertFingerPrintDetailsResponse;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.rest.HandlerClient;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnregisteredWorkerReg_Activity extends AppCompatActivity implements AuthBfdCap {
    private Context context;
    private UserSessionManager session;
    private String TAG = "UnregisteredWorkerReg_Activity";
    private MaterialEditText edt_fname, edt_mname, edt_lname, edt_marathi_name, edt_aadhaarno, edt_moblieno, edt_dob, edt_age, edt_permanant_address,
            edt_local_address, edt_city, edt_pincode;
    private RadioButton rb_mr, rb_mrs, rb_ms, rb_male, rb_female;
    private Button btn_register;
    private ProgressDialog pd;
    private String USERID = "", SiteDetailId = "", genderId = "", title = "", DISTLGDCODE = "";
    private int mYear, mMonth, mDay;

    private boolean isFingerScanner = false;
    private CardView cd_thumbuploadlayout;
    private ImageView iv_biomatric;
    private String fingerImage = "",
            registerTemp = "";
    private File folder;
    private String RegId = "";
//    private String RegId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregistered_worker_reg);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();

    }

    private void init() {
        context = UnregisteredWorkerReg_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        edt_fname = findViewById(R.id.edt_fname);
        edt_mname = findViewById(R.id.edt_mname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_marathi_name = findViewById(R.id.edt_marathi_name);
        edt_aadhaarno = findViewById(R.id.edt_aadhaarno);
        edt_moblieno = findViewById(R.id.edt_moblieno);
        edt_age = findViewById(R.id.edt_age);
        edt_dob = findViewById(R.id.edt_dob);
        edt_permanant_address = findViewById(R.id.edt_permanant_address);
        edt_local_address = findViewById(R.id.edt_local_address);
        edt_city = findViewById(R.id.edt_city);
        edt_pincode = findViewById(R.id.edt_pincode);
        rb_mr = findViewById(R.id.rb_mr);
        rb_mrs = findViewById(R.id.rb_mrs);
        rb_ms = findViewById(R.id.rb_ms);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        btn_register = findViewById(R.id.btn_register);

        cd_thumbuploadlayout = findViewById(R.id.cd_thumbuploadlayout);
        iv_biomatric = findViewById(R.id.iv_biomatric);
    }

    private void setDefaults() {
        SiteDetailId = getIntent().getStringExtra("SiteDetailId");

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        folder = new File(Environment.getExternalStorageDirectory(),
                "/Health Checkup/ThumbData/");
        if (!folder.exists()) {
            // Make it, if it doesn't exit
            folder.mkdirs();
        }
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                USERID = json.getString("EmpCode");
                DISTLGDCODE = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        edt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int age = Integer.parseInt(getAge(Integer.parseInt(String.format("%02d", year)),
                                        Integer.parseInt(String.format("%02d", monthOfYear)),
                                        Integer.parseInt(String.format("%02d", dayOfMonth))));

                                if (age < 18) {
                                    Utilities.showAlertDialog(context, "Alert", "Beneficiary age should not be less than 18 years", false);
                                    return;
                                }
                                edt_dob.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                edt_age.setText(getAge(Integer.parseInt(String.format("%02d", year)),
                                        Integer.parseInt(String.format("%02d", monthOfYear)),
                                        Integer.parseInt(String.format("%02d", dayOfMonth))));

                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dpd1.getDatePicker().setCalendarViewShown(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        iv_biomatric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegId.equalsIgnoreCase("")) {
                    Utilities.showToastMessage("Worker registration ID not found.", context, false);
                    return;
                }

                if (isWorking) {
                    Utilities.showToastMessage("Please let process to complete first", context, false);

                } else {
//                    fingerImage = RegId + "_1_" + USERID + "_fingerImage.png";
//                    registerTemp = RegId + "_2_" + USERID + "_registerTemp.raw";
                    fingerImage = "1_" + USERID + "_fingerImage.png";
                    registerTemp = "2_" + USERID + "_registerTemp.raw";

                    setButtonEnabled(false);
                    capture();
                    Utilities.showToastMessage("Place your finger on the sensor", context, false);
                }
            }
        });
    }

    private void submitData() {
        if (rb_mr.isChecked()) {
            title = "Mr.";
        } else if (rb_mrs.isChecked()) {
            title = "Mrs.";
        } else if (rb_ms.isChecked()) {
            title = "Ms.";
        } else {
            Utilities.showToastMessage("Please select title", context, false);
            return;
        }

        if (edt_fname.getText().toString().trim().isEmpty()) {
            edt_fname.setError("Please enter first name");
            return;
        }

        if (edt_lname.getText().toString().trim().isEmpty()) {
            edt_lname.setError("Please enter last name");
            return;
        }

        if (edt_marathi_name.getText().toString().trim().isEmpty()) {
            edt_marathi_name.setError("Please enter marathi name");
            return;
        }

        if (edt_aadhaarno.getText().toString().trim().isEmpty()) {
            edt_aadhaarno.setError("Please enter aadhar number");
            return;
        }

        if (!Utilities.isaadharNumberValidate(edt_aadhaarno.getText().toString().trim())) {
            edt_aadhaarno.setError("Please enter valid aadhar number");
            return;
        }

        if (edt_dob.getText().toString().trim().isEmpty()) {
            edt_dob.setError("Please select date of birth");
            return;
        }

        if (edt_age.getText().toString().trim().isEmpty()) {
            edt_age.setError("Please enter age");
            return;
        }

        if (edt_moblieno.getText().toString().trim().isEmpty()) {
            edt_moblieno.setError("Please enter mobile no.");
            return;
        }

        if (!Utilities.isMobileNo(edt_moblieno.getText().toString().trim())) {
            edt_moblieno.setError("Please enter valid mobile no.");
            return;
        }

        if (rb_male.isChecked()) {
            genderId = "M";
        } else if (rb_female.isChecked()) {
            genderId = "F";
        } else {
            Utilities.showToastMessage("Please select gender", context, false);
            return;
        }

        if (edt_permanant_address.getText().toString().trim().isEmpty()) {
            edt_permanant_address.setError("Please enter permanant address");
            return;
        }

        if (edt_local_address.getText().toString().trim().isEmpty()) {
            edt_local_address.setError("Please enter local address");
            return;
        }

        if (edt_city.getText().toString().trim().isEmpty()) {
            edt_city.setError("Please enter locality");
            return;
        }

        if (edt_pincode.getText().toString().trim().isEmpty()) {
            edt_pincode.setError("Please enter pincode");
            return;
        }

        if (!Utilities.isValidPincode(edt_pincode.getText().toString().trim())) {
            edt_pincode.setError("Please enter valid pincode");
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new InsCW_UnRegisterWorkerDetails().execute(
                    SiteDetailId,
                    DISTLGDCODE,
                    title,
                    edt_fname.getText().toString().trim(),
                    edt_mname.getText().toString().trim(),
                    edt_lname.getText().toString().trim(),
                    edt_marathi_name.getText().toString().trim(),
                    edt_moblieno.getText().toString().trim(),
                    edt_dob.getText().toString().trim(),
                    edt_age.getText().toString().trim(),
                    genderId,
                    edt_permanant_address.getText().toString().trim(),
                    edt_local_address.getText().toString().trim(),
                    edt_city.getText().toString().trim(),
                    edt_pincode.getText().toString().trim(),
                    edt_aadhaarno.getText().toString().trim(),
                    USERID
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    public class InsCW_UnRegisterWorkerDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("SiteId", params[0]));
            param.add(new ParamsPojo("DistrictId", params[1]));
            param.add(new ParamsPojo("Title", params[2]));
            param.add(new ParamsPojo("Fname", params[3]));
            param.add(new ParamsPojo("Mname", params[4]));
            param.add(new ParamsPojo("Lname", params[5]));
            param.add(new ParamsPojo("MaratiName", params[6]));
            param.add(new ParamsPojo("MobileNo", params[7]));
            param.add(new ParamsPojo("Dob", params[8]));
            param.add(new ParamsPojo("Age", params[9]));
            param.add(new ParamsPojo("Gender", params[10]));
            param.add(new ParamsPojo("PermanentAddress", params[11]));
            param.add(new ParamsPojo("LocalAddress", params[12]));
            param.add(new ParamsPojo("Location", params[13]));
            param.add(new ParamsPojo("PinCode", params[14]));
            param.add(new ParamsPojo("UID", params[15]));
            param.add(new ParamsPojo("CreatedBy", params[16]));
            res = WebServiceCall.APICall(ApplicationConstants.InsCW_UnRegisterWorkerDetails, ApplicationConstants.webservice, param);
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
                        String output = obj.getString("output");
//                        Utilities.showToastMessage(output, context, false);

                        if (!isFingerScanner) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.icon_success);
                            builder.setTitle("Success");
                            builder.setCancelable(false);
                            builder.setMessage("Worker registered successfully");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    edt_fname.setText("");
                                    edt_mname.setText("");
                                    edt_lname.setText("");
                                    edt_marathi_name.setText("");
                                    edt_aadhaarno.setText("");
                                    edt_moblieno.setText("");
                                    edt_dob.setText("");
                                    edt_age.setText("");
                                    edt_permanant_address.setText("");
                                    edt_local_address.setText("");
                                    edt_city.setText("");
                                    edt_pincode.setText("");

                                    rb_mr.setChecked(true);
                                    rb_mrs.setChecked(false);
                                    rb_ms.setChecked(false);
                                    rb_male.setChecked(true);
                                    rb_female.setChecked(false);
                                }
                            });
                            builder.show();

                        } else {
                            Utilities.showToastMessage("Worker registered successfully, Get worker thumb to get register in system",
                                    context, true);

                            edt_fname.setClickable(false);
                            edt_mname.setClickable(false);
                            edt_lname.setClickable(false);
                            edt_marathi_name.setClickable(false);
                            edt_aadhaarno.setClickable(false);
                            edt_moblieno.setClickable(false);
                            edt_dob.setClickable(false);
                            edt_age.setClickable(false);
                            edt_permanant_address.setClickable(false);
                            edt_local_address.setClickable(false);
                            edt_city.setClickable(false);
                            edt_pincode.setClickable(false);

                            rb_mr.setClickable(false);
                            rb_mrs.setClickable(false);
                            rb_ms.setClickable(false);
                            rb_male.setClickable(false);
                            rb_female.setClickable(false);

                            btn_register.setVisibility(View.GONE);

                            cd_thumbuploadlayout.setVisibility(View.VISIBLE);
                            cd_thumbuploadlayout.setFocusable(true);
                            RegId = output;
                        }

                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration of Unregistered Workers");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);

        return ageInt.toString();
    }

    /*---------------------------- DataMini New Code ---------------------------------------------*/

    private static MorphoTabletFPSensorDevice fpSensorCap;

    @Override
    protected void onResume() {
        super.onResume();
        setDetaminiParams();
    }

    @Override
    public void onPause() {
        super.onPause();
        fpSensorCap.cancelLiveAcquisition();
        fpSensorCap.release();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fpSensorCap.release();
    }

    private void setDetaminiParams() {
        fpSensorCap = new MorphoTabletFPSensorDevice(this);
        Log.i("initFP", "Object Created");

        int i = fpSensorCap.open(this);
        if (i != 0) {
            isFingerScanner = false;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Alert");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Finger print scanner is not detected, Please remove all connected cables, go back and try again.");
            alertDialog.setIcon(R.drawable.icon_alertred);
            alertDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        } else {
            isFingerScanner = true;
        }
    }

    /**To start capture. */
    /**
     * updateImageView gets called on completion of capture.
     */
    private void capture() {
        try {
            fpSensorCap.startCapture();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().toString(), "capture", e);
        }
    }

    private boolean isWorking = false;

    public void setButtonEnabled(boolean enabled) {
        isWorking = !enabled;
        iv_biomatric.setEnabled(enabled);
    }

    @Override
    public void updateImageView(final ImageView imgPreview,
                                final Bitmap previewBitmap,
                                final String message,
                                final boolean flagComplete,
                                final int captureError) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                if (imgPreview != null) {
//                    imgPreview.setImageBitmap(previewBitmap);
//                }

                if (captureError == ErrorCodes.MORPHOERR_TIMEOUT) {
                    Utilities.showToastMessage("Capture Timeout", context, false);
//                    tost = Toast.makeText(getApplicationContext(), "Capture Timeout", Toast.LENGTH_SHORT);
//                    tost.show();

                    setButtonEnabled(true);
                    Log.e(this.getClass().toString(), "Capture Timeout ErrorCodes = " + captureError);
                    return;

                } else if (captureError == ErrorCodes.MORPHOERR_CMDE_ABORTED) {
                    Log.e(this.getClass().toString(), "MORPHOERR_CMDE_ABORTED ErrorCodes = " + captureError);
                    setButtonEnabled(true);
                    return;
                }

                if (flagComplete && captureError == ErrorCodes.MORPHO_OK) {
//                    Utilities.showToastMessage("Thumb saved successfully", context, true);
                    setButtonEnabled(true);
//                    saveToDb(fpSensorCap.templateBuffer);

                    if (storeImage(fingerImage, previewBitmap)) {
                        if (DumpFile(registerTemp, fpSensorCap.templateBuffer)) {

                            File file = new File(folder, registerTemp);

                            InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "2", USERID,
                                    file.toString());
                        }
                    }
                }
            }
        });
    }

    private boolean storeImage(String fileName, Bitmap image) {
        File file = new File(folder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            return false;
        }
    }

    private boolean DumpFile(String fileName, byte[] buffer) {
        File file = new File(folder, fileName);
        // String imgString = Base64.encodeToString(buffer, Base64.NO_WRAP);
        // Save your stream, don't forget to flush() it before closing it.
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(buffer);
            bos.flush();
            bos.close();

//            Utilities.showToastMessage("File write success to : " + file, context, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.showToastMessage("File write failed: " + e.toString(), context, true);
            return false;
        }
    }

//    /*-------------------------------- DataMini Code ---------------------------------------------*/
//
//    private JSGFPLib sgfplib;
//
//    //RILEY
//    //This broadcast receiver is necessary to get user permissions to access the attached USB device
//    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
//    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            //DEBUG Log.d(TAG,"Enter mUsbReceiver.onReceive()");
//            if (ACTION_USB_PERMISSION.equals(action)) {
//                synchronized (this) {
//                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                        if (device != null) {
//                            Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
//                            Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
////                            debugMessage("Vendor ID : " + device.getVendorId() + "\n");
////                            debugMessage("Product ID: " + device.getProductId() + "\n");
//                        } else
//                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
//                    } else
//                        Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);
//                }
//            }
//        }
//    };
//
//    private PendingIntent mPermissionIntent;
//    private IntentFilter filter;
//
//    private int[] mMaxTemplateSize;
//    private int mImageWidth;
//    private int mImageHeight;
//    private byte[] mRegisterImage;
//    private byte[] mRegisterTemplate;
//
//    private String fingerImage = "fingerImage.png",
//            registerImg = "registerImg.raw",
//            registerTemp = "registerTemp.raw";
//
//    private void setDetaminiParams() {
//        mMaxTemplateSize = new int[1];
//
//        //USB Permissions
//        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
//        filter = new IntentFilter(ACTION_USB_PERMISSION);
//        registerReceiver(mUsbReceiver, filter);
//        sgfplib = new JSGFPLib((UsbManager) getSystemService(Context.USB_SERVICE));
//    }
//
//    @Override
//    protected void onResume() {
//        Log.d(TAG, "onResume()");
//        super.onResume();
//
//        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            provideLocationAccess(context);
//        } else {
//            if (!isLocationEnabled(context)) {
//                turnOnLocation(context);
//            } else {
//                startLocationUpdates();
//            }
//        }
//
//        registerReceiver(mUsbReceiver, filter);
//        long error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
//        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE) {
//            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(this);
//            if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND) {
//                dlgAlert.setMessage("The attached fingerprint device is not supported on Android");
////                dlgAlert.setMessage("संलग्न फिंगरप्रिंट साधन Android समर्थित नाही");
//            } else {
//                dlgAlert.setMessage("Fingerprint device initialization failed?!");
////                dlgAlert.setMessage("बोटाचा ठसा साधन प्रारंभ अयशस्वी झाला !");
//            }
//            dlgAlert.setTitle("SecuGen Fingerprint SDK");
////            dlgAlert.setTitle("SecuGen बोटाचा ठसा SDK सुचना");
//            dlgAlert.setPositiveButton(
//                    "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
////                            finish();
//                            return;
//                        }
//                    });
//            dlgAlert.setCancelable(false);
//            dlgAlert.create().show();
//        } else {
//            UsbDevice usbDevice = sgfplib.GetUsbDevice();
//            if (usbDevice == null) {
//                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(this);
//                dlgAlert.setMessage("SDU04P or SDU03P fingerprint sensor not found!");
//                dlgAlert.setTitle("SecuGen Fingerprint SDK");
////                dlgAlert.setMessage("SDU04P किंवा SDU03P फिंगरप्रिंट सेन्सर आढळला नाही!");
////                dlgAlert.setTitle("SecuGen बोटाचा ठसा SDK सुचना");
//                dlgAlert.setPositiveButton(
//                        "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
////                                finish();/////////////////////////////
//                                return;
//                            }
//                        });
//                dlgAlert.setCancelable(false);
//                dlgAlert.create().show();
//            } else {
//                sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
//                error = sgfplib.OpenDevice(0);
//                SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
//                error = sgfplib.GetDeviceInfo(deviceInfo);
//                mImageWidth = deviceInfo.imageWidth;
//                mImageHeight = deviceInfo.imageHeight;
//                sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
//                sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
//
//                long sgfdiso = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
//                Log.i("sgfdiso", String.valueOf(sgfdiso));
//
//                mRegisterTemplate = new byte[mMaxTemplateSize[0]];
////                mVerifyTemplate = new byte[mMaxTemplateSize[0]];
//                sgfplib.WriteData((byte) 5, (byte) 1);
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        Log.d(TAG, "onPause()");
//        sgfplib.CloseDevice();
//        unregisterReceiver(mUsbReceiver);
//        mRegisterImage = null;
//        mRegisterTemplate = null;
////        mVerifyImage = null;
////        mVerifyTemplate = null;
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy()");
//        sgfplib.CloseDevice();
//        mRegisterImage = null;
//        mRegisterTemplate = null;
////        mVerifyImage = null;
////        mVerifyTemplate = null;
//        sgfplib.Close();
//        super.onDestroy();
//    }
//
//    private void getFingerPrintCapture() {
//        Log.d(TAG, "Clicked REGISTER");
//
//        if (mRegisterImage != null) {
//            mRegisterImage = null;
//        }
//        mRegisterImage = new byte[mImageWidth * mImageHeight];
//
//        ByteBuffer byteBuf = ByteBuffer.allocate(mImageWidth * mImageHeight);
//        long result = sgfplib.GetImage(mRegisterImage);
//        // --- Getting finger print image
//        DumpFile(registerImg, mRegisterImage);
//
//        Bitmap b = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
//        byteBuf.put(mRegisterImage);
//        int[] intbuffer = new int[mImageWidth * mImageHeight];
//        for (int i = 0; i < intbuffer.length; ++i) {
//            intbuffer[i] = (int) mRegisterImage[i];
//        }
//        b.setPixels(intbuffer, 0, mImageWidth, 0, 0, mImageWidth, mImageHeight);
//        result = sgfplib.SetTemplateFormat(SecuGen.FDxSDKPro.SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
//        SGFingerInfo fpInfo = new SGFingerInfo();
//        for (int i = 0; i < mRegisterTemplate.length; ++i) {
//            mRegisterTemplate[i] = 0;
//        }
//        result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate);
////        DumpFile("registerTemp.raw", mRegisterTemplate);
//
//        if (storeImage(fingerImage, this.toGrayscale(b))) {
//            if (DumpFile(registerTemp, mRegisterTemplate)) {
//
//                InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "2", EmpCode,
//                        folder.toString() + registerTemp);
//            }
//        }
//
////        iv_biomatric.setImageBitmap(this.toGrayscale(b));
//    }
//
//    private Bitmap toGrayscale(Bitmap bmpOriginal) {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        for (int y = 0; y < height; ++y) {
//            for (int x = 0; x < width; ++x) {
//                int color = bmpOriginal.getPixel(x, y);
//                int r = (color >> 16) & 0xFF;
//                int g = (color >> 8) & 0xFF;
//                int b = color & 0xFF;
//                int gray = (r + g + b) / 3;
//                color = Color.rgb(gray, gray, gray);
//                //color = Color.rgb(r/3, g/3, b/3);
//                bmpGrayscale.setPixel(x, y, color);
//            }
//        }
//        return bmpGrayscale;
//    }
//
//    private boolean storeImage(String fileName, Bitmap image) {
//        File file = new File(folder, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//            return true;
//        } catch (FileNotFoundException e) {
//            Log.d(TAG, "File not found: " + e.getMessage());
//            return false;
//        } catch (IOException e) {
//            Log.d(TAG, "Error accessing file: " + e.getMessage());
//            return false;
//        }
//    }
//
//    private boolean DumpFile(String fileName, byte[] buffer) {
//        File file = new File(folder, fileName);
//        // String imgString = Base64.encodeToString(buffer, Base64.NO_WRAP);
//        // Save your stream, don't forget to flush() it before closing it.
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            bos.write(buffer);
//            bos.flush();
//            bos.close();
//
//            Utilities.showToastMessage("File write success to : " + file, context, true);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Utilities.showToastMessage("File write failed: " + e.toString(), context, true);
//            return false;
//        }
//    }
//
    /*------------------------------------ API Call ----------------------------------------------*/

    private void InsertConstructionWorkDetailsImage_HandlerAPICall(String regId, final String fileType,
                                                                   String createdBy, final String filePath) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait . . .");
        progressDialog.show();

        ApiInterface apiService = HandlerClient.getClient().create(ApiInterface.class);

        final File fPath = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fPath);
        MultipartBody.Part imageFile = MultipartBody.Part.createFormData("file", fPath.getName(), requestBody);

//        RequestBody reraID_body = RequestBody.create(MediaType.parse("text/plain"), reraID);
//        RequestBody siteDetailId_body = RequestBody.create(MediaType.parse("text/plain"), siteDetailId);
//        RequestBody userID_body = RequestBody.create(MediaType.parse("text/plain"), userID);
//        RequestBody fileType_body = RequestBody.create(MediaType.parse("text/plain"), fileType);
//        RequestBody builderID_body = RequestBody.create(MediaType.parse("text/plain"), builderID);
//
//        Call<InsertConstructionWorkDetailsResponse> call = apiService.InsertConstructionWorkDetails(reraID_body, siteDetailId_body, userID_body,
//                fileType_body, builderID_body, imageFile);

        Call<InsertFingerPrintDetailsResponse> call = apiService.InsertFingerPrintDetails("2", regId, fileType,
                createdBy, imageFile);
        call.enqueue(new Callback<InsertFingerPrintDetailsResponse>() {
            @Override
            public void onResponse(Call<InsertFingerPrintDetailsResponse> call, Response<InsertFingerPrintDetailsResponse> response) {
                progressDialog.dismiss();

                int surverCode = response.code();
                String surverMessage = response.message();

                if (surverCode == ApplicationConstants.OKSUCCESS) {
                    try {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (status.equalsIgnoreCase("Success")) {

                            if (fileType.equalsIgnoreCase("2")) {

                                File file = new File(folder, fingerImage);
                                InsertConstructionWorkDetailsImage_HandlerAPICall(RegId, "1", USERID,
                                        file.toString());

                            } else {
                                Bitmap bmp = BitmapFactory.decodeFile(folder.toString() + "/" + fingerImage);
                                bmp = Bitmap.createScaledBitmap(bmp, 150, 150, false);
                                iv_biomatric.setImageBitmap(bmp);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.icon_success);
                                builder.setTitle("Success");
                                builder.setCancelable(false);
                                builder.setMessage("Worker registered successfully");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        edt_fname.setClickable(true);
                                        edt_mname.setClickable(true);
                                        edt_lname.setClickable(true);
                                        edt_marathi_name.setClickable(true);
                                        edt_aadhaarno.setClickable(true);
                                        edt_moblieno.setClickable(true);
                                        edt_dob.setClickable(true);
                                        edt_age.setClickable(true);
                                        edt_permanant_address.setClickable(true);
                                        edt_local_address.setClickable(true);
                                        edt_city.setClickable(true);
                                        edt_pincode.setClickable(true);

                                        rb_mr.setClickable(true);
                                        rb_mrs.setClickable(true);
                                        rb_ms.setClickable(true);
                                        rb_male.setClickable(true);
                                        rb_female.setClickable(true);

                                        btn_register.setVisibility(View.VISIBLE);
                                        cd_thumbuploadlayout.setVisibility(View.GONE);
                                        iv_biomatric.setImageDrawable(getResources().getDrawable(R.drawable.vector_fingerprint));

                                        edt_fname.setText("");
                                        edt_mname.setText("");
                                        edt_lname.setText("");
                                        edt_marathi_name.setText("");
                                        edt_aadhaarno.setText("");
                                        edt_moblieno.setText("");
                                        edt_dob.setText("");
                                        edt_age.setText("");
                                        edt_permanant_address.setText("");
                                        edt_local_address.setText("");
                                        edt_city.setText("");
                                        edt_pincode.setText("");

                                        rb_mr.setChecked(true);
                                        rb_mrs.setChecked(false);
                                        rb_ms.setChecked(false);
                                        rb_male.setChecked(true);
                                        rb_female.setChecked(false);

                                        fingerImage = "";
                                        registerTemp = "";
                                        RegId = "";

                                    }
                                });
                                builder.show();
                            }

                        } else {
                            Utilities.showAlertDialog(context, status, message, false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context,
                                "Alert", "Exception " + e.toString(), false);
                    }
                } else {
                    Utilities.showAlertDialog(context,
                            "Alert", "Server returns : " + surverCode + " " + surverMessage, false);
                }
            }

            @Override
            public void onFailure(Call<InsertFingerPrintDetailsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                Utilities.showAlertDialog(context,
                        "Alert", t.toString(), false);
            }
        });
    }
}
