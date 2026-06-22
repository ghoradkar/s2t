package com.myhindlab.abkat.expense_module.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import static com.myhindlab.abkat.utilities.Utilities.compressImage;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.TeamCampMappingActivity;
import com.myhindlab.abkat.activities.UploadPatientRenewalPhoto_Activity;
import com.myhindlab.abkat.activities.couriermodule.CourierSendStep1_Activity;
import com.myhindlab.abkat.activities.doortodoor.BeneficiaryVerificationActivity;
import com.myhindlab.abkat.activities.doortodoor.BeneficiaryVerificationActivityNew;
import com.myhindlab.abkat.adapters.BarcodeListNewAdapter;
import com.myhindlab.abkat.adapters.CampListCheckAdapter;
import com.myhindlab.abkat.adapters.CampListCheckAdapterNew;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedAdapterNew;
import com.myhindlab.abkat.expense_module.adapters.PostCampFileListAdapter;
import com.myhindlab.abkat.expense_module.models.ExpenseHeadModel;
import com.myhindlab.abkat.expense_module.models.PostCampFileListModel;
import com.myhindlab.abkat.expense_module.models.SubExpenseHeadModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.GetInitiatedByListForCampModel;
import com.myhindlab.abkat.models.PostCampListModel;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.TeamsDetailsModelNew;
import com.myhindlab.abkat.models.couriermodule.BarcodeListCountNewModel;
import com.myhindlab.abkat.models.couriermodule.BarcodeListNewModel;
import com.myhindlab.abkat.models.couriermodule.CamplistOnLandingLabModel;
import com.myhindlab.abkat.models.couriermodule.LandingLabNewModel;
import com.myhindlab.abkat.models.doortodoor.D2dWorkingTeamModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.FileUtils;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RealPathUtil;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class BillUploadActivity extends AppCompatActivity implements PostCampFileListAdapter.PostCampFileListEvents {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private TextView textView, tvDate, tv_patient_Image, tvOrganizedBy, tvSampleCount, tvFromDate, tvToDate, tvTeamHeader,
            tvCampId, tvTotalAmount, tvTeamNumber, tvSampleBarcode, tvExpenseHead, tvSubExpenseHead, tvPhleboHeader,
            tvDataEntryHeader, tvTeamId, tvCampType, tvDistrict, tvLandingLab, tvCampList, tvExternalPhlebo, tvAssigned, tvTeams;
    private Button btnAssign;
    private EditText edtRemark;
    private boolean isFileUploading = false;
    private PostCampListModel.OutputBean campDetails;


    private LinearLayout llteam;

    private int fileIndex = 0;
    private File file, serviceCertiFolder;
    private String filename, billId;
    private RecyclerView rvFileList;


    private static final int GALLERY_REQUEST = 200;
    private Bitmap letterPicBm, ackPicBm;
    private String filePath = null, ackImagePath, uploadFilePath = null;


    private Uri photoURI;


    private Uri letterUri, ackUri;
    private int imageType = 0;

    private File patientPicsFolder;
    private final int LETTER_CAMERA_REQUEST = 111;
    ImageView imv_patient;
    private int assignTypeId, selectectLabId;

    private ArrayList<PostCampFileListModel> postCampFileListModels;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private String DESGID, EmpCode, LabCode, expenseheadId, subexpenseheadId, CampDATE, DISTLGDCODE, district, TALLGDCODE, taluka, STATELGDCODE = "2",
            campTypeId, selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampToDate, CampId, CampDate, UserId, AssignedId = "",
            IsTeam, initiatedId;
    private ArrayList<CamplistOnLandingLabModel.Output> campList;
    private ArrayList<CampListModel.OutputBean> selectedCampList;
    private TeamsDetailsModel teamsDetailsModel;
    private ArrayList<TeamsDetailsModel> assignteamlist;
    private AlertDialog teamDialog;
    private ArrayList<BarcodeListNewModel.Output> barcodeList;
    private ArrayList<BarcodeListCountNewModel.Output> barcodecountList;
    private ArrayList<BarcodeListNewModel.Output> selectedBarcodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_upload);


        initView();
        setEventHandlers();
        getSessionData();
        setUpToolbar();
    }

    private void initView() {
        context = BillUploadActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        postCampFileListModels = new ArrayList<>();

        tvDate = findViewById(R.id.tvDate);
        tvCampType = findViewById(R.id.tvCampType);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvLandingLab = findViewById(R.id.tvLandingLab);
        tvCampList = findViewById(R.id.tvCampList);
        btnAssign = findViewById(R.id.btnAssign);
        tvExternalPhlebo = findViewById(R.id.tvExternalPhlebo);
        tvTeamId = findViewById(R.id.tvTeamId);
        tvAssigned = findViewById(R.id.tvAssigned);
        tvTeams = findViewById(R.id.tvTeams);
        tvTeamHeader = findViewById(R.id.tvTeamHeader);
        tvPhleboHeader = findViewById(R.id.tvPhleboHeader);
        tvDataEntryHeader = findViewById(R.id.tvDataEntryHeader);
        tvTeamNumber = findViewById(R.id.tvTeamNumber);
        tvSampleBarcode = findViewById(R.id.tvSampleBarcode);
        tvSampleCount = findViewById(R.id.tvSampleCount);
        edtRemark = findViewById(R.id.edtRemark);
        llteam = findViewById(R.id.llteam);
        tvExpenseHead = findViewById(R.id.tvExpenseHead);
        tvSubExpenseHead = findViewById(R.id.tvSubExpenseHead);
        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        tvCampId = findViewById(R.id.tvCampId);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tv_patient_Image = findViewById(R.id.tv_patient_Image);
        imv_patient = findViewById(R.id.imv_patient);
        rvFileList = findViewById(R.id.rvFileList);
        tvOrganizedBy = findViewById(R.id.tvOrganizedBy);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        if (SDK_INT >= Build.VERSION_CODES.Q) {
            patientPicsFolder = getExternalCacheDir();
        } else {
            patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Photos/");

            if (!patientPicsFolder.exists())
                patientPicsFolder.mkdirs();
        }




        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
                .setTitle("Alert")
                .setIcon(R.drawable.icon_alertred)
                .setMessage("कृपया नोंद घ्या बिल approve केल्यानंतर  camp यादीतून काढून टाकला जाईल. त्यामुळे तुम्ही सर्व आवश्यक उप-शीर्षांसाठी expnses टाकल्याची खात्री करा.\n" + "Please note after bill approve, camp will be removed from list. So make sure you have entered expenses for all sub-expense heads.")
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                                dialog.dismiss();

                            }
                        }

//                .setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                binding.etDate.setText("");
//
//
//                            }
//                        }
                );
        b.show();


//        patientSignFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Signature/");
//        patientSignFolder = getExternalCacheDir();
//        if (!patientSignFolder.exists())
//            patientSignFolder.mkdirs();

//        fingerPrintFolder = new File(Environment.getExternalStorageDirectory(), "/Health Checkup/ThumbData/");
//        fingerPrintFolder = getExternalCacheDir();
//        if (!fingerPrintFolder.exists()) {
//            fingerPrintFolder.mkdirs();
//        }


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        tvFromDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());

        tvToDate.setText(Utilities.dfDate4.format(new Date()));
        CampToDate = Utilities.dfDate4.format(new Date());


//        Calendar todayCal = Calendar.getInstance();
//        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//
//        Calendar toCal = Calendar.getInstance();
//        toCal.add(Calendar.DAY_OF_MONTH, -7);


//        tv_from_date.setText(Utilities.dfDate4.format(todayCal.getTime()));
//        tv_to_date.setText(Utilities.dfDate4.format(toCal.getTime()));
//        CampDate = Utilities.dfDate4.format(new Date());
//        CampDateToDate = Utilities.dfDate4.format(new Date());


    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                UserId = json.getString("UserId");
                LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void setEventHandlers() {


        imv_patient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (tvExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select expense head", context, false);
                    return;
                }

                if (tvSubExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select sub expense head", context, false);
                    return;
                }
                if (tvFromDate.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select from date", context, false);
                    return;
                }
                if (tvToDate.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select to date", context, false);
                    return;
                }

//                selectImage();


                imageType = 0;
                if (SDK_INT < Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                        return;
                    }

                } else {
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA}, 2);
                        return;
                    }
                }


                selectImage();


//                int randomEndtNo = (int) (Math.random() * 99999 + 1);
//                if (SDK_INT >= Build.VERSION_CODES.Q) {
//                    ContentResolver resolver = context.getContentResolver();
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_PR.png");
//                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
//                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//                    letterUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
//                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);
//                } else {
//                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_PR.png");
//                    letterUri = Uri.fromFile(patientImageFile);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
//                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);
//                }
            }
        });


        tvOrganizedBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetInitiatedByList().execute();
            }
        });

        tv_patient_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    postCampFileListModels.add(new PostCampFileListModel(filename, filePath));
                    setupRecyclerView();
                    filename = "filename";
                    filePath = null;
//                    tvFileName.setText("Capture Image");
                } else {
                    Utilities.showToastMessage("Please capture file", context, false);
                }
            }
        });


        tvCampId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tvExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select expense head", context, false);
                    return;
                }

                if (tvSubExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select sub expense head", context, false);
                    return;
                }

                if (tvFromDate.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select from date", context, false);
                    return;
                }

                if (tvToDate.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select to date", context, false);
                    return;
                }


                if (CampDate != null) {
                    if (Utilities.isNetworkAvailable(context)) {

                        if (campList != null && !campList.isEmpty()) {
                            showCampListDialog(campList);
                        } else {
                            new GetCampList().execute(CampDate, CampToDate, DISTLGDCODE,EmpCode,subexpenseheadId);
                        }

                    } else {
                        Utilities.showToastMessage("Please Check Your Connection", context, false);
                    }
                } else {
//                    tvDate.setError("Select Camp Date");

                }
            }
        });

        tvCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("DOOR TO DOOR", 3));
                campTypeModelArrayList.add(new CampTypeModel("CSC D2D", 4));
                //                campTypeModelArrayList.add(new CampTypeModel("CSC", 2));

                showCampType(campTypeModelArrayList);


//                tvCampList.setText("");
//                if (campList != null){
//                    campList.clear();
//                }


            }
        });


        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select expense head", context, false);
                    return;
                }

                if (tvSubExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select sub expense head", context, false);
                    return;
                }

                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvFromDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));


                                //

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

                                //  new GetCampList().execute(CampDate, EmpCode, DISTLGDCODE, String.valueOf(AssignedId), "0");

                            }

                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select expense head", context, false);
                    return;
                }

                if (tvSubExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select sub expense head", context, false);
                    return;
                }

                if (tvFromDate.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select sub expense head", context, false);
                    return;
                }


                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvToDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                CampToDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


                                //  new GetCampList().execute(CampDate, EmpCode, DISTLGDCODE, String.valueOf(AssignedId), "0");

                            }

                        }, mYear, mMonth, mDay);
                try {
//                    dpd1.getDatePicker().setCalendarViewShown(false);

                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        tvExpenseHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetExpenseHead().execute();
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });

        tvSubExpenseHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tvExpenseHead.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select expense head", context, false);
                    return;
                }

                if (Utilities.isNetworkAvailable(context)) {
                    new GetSubExpenseHead().execute(expenseheadId, "1", "1");
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });


//        tvCampList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (CampDate != null) {
//                    if (Utilities.isNetworkAvailable(context)) {
//                        new GetCampList().execute(CampDate, String.valueOf(campTypeId), selectedLabID);
//                    } else {
//                        Utilities.showToastMessage("Please Check Your Connection", context, false);
//                    }
//                } else {
//                    tvDate.setError("Select Camp Date");
//
//                }
//
//
//            }
//        });


        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });


//        tvSampleBarcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    if (barcodeList==null || barcodeList.size() == 0){
//                        if (campTypeId.equalsIgnoreCase("1")||campTypeId.equalsIgnoreCase("2")||campTypeId.equalsIgnoreCase("4")){
//                            new GetBarcodeList().execute(CampDate, String.valueOf(campTypeId), selectedLabID, CampId, "0", "1");
//                        }else if (campTypeId.equalsIgnoreCase("3")){
//                            new GetBarcodeList().execute(CampDate, String.valueOf(campTypeId), selectedLabID, CampId, AssignedId, "1");
//                        }
//
//                    }else{
//                        BarcodeListDialog(barcodeList);
//
//                    }
//                } else {
//                    Utilities.showToastMessage("Please Check Your Connection", context, false);
//                    return;
//                }
//            }
//        });


    }

    private void submitData() {

//        if (tvCampType.getText().toString().trim().isEmpty()) {
//            Utilities.showToastMessage("Please Select CampType", context, false);
//            return;
//        }
//        if (tvOrganizedBy.getText().toString().trim().isEmpty()) {
//            Utilities.showToastMessage("Please Select Organized By", context, false);
//            return;
//        }
        if (tvExpenseHead.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Expense Head", context, false);
            return;
        }

        if (tvSubExpenseHead.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Sub Expense Head", context, false);
            return;
        }
        if (tvFromDate.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select From Date", context, false);
            return;
        }
        if (tvToDate.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select To Date", context, false);
            return;
        }
        if (tvCampId.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Camps", context, false);
            return;
        }
//        if (tvTotalAmount.getText().toString().trim().isEmpty()) {
//            Utilities.showToastMessage("Please Enter Amount", context, false);
//            return;
//        }

        if (edtRemark.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Enter Amount", context, false);
            return;
        }



        if (!tvTotalAmount.getText().toString().isEmpty()){
            int totalamount = Integer.parseInt(String.valueOf(Integer.parseInt(tvTotalAmount.getText().toString())));
            int enteredAmount = Integer.parseInt(String.valueOf(Integer.parseInt(edtRemark.getText().toString())));


            if (!(enteredAmount == totalamount)){
                Utilities.showAlertDialog(context,"Alert","Bill amount is not matching with expenses entered for selected camps. Please verify camp wise entered expenses",false);{
                    return;
                }
            }
        }


        JsonArray campArr = new JsonArray();
        if (campList != null) {
            for (CamplistOnLandingLabModel.Output o : campList) {
                if (o.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("CampID", o.getCampid());
                    campArr.add(jsonObject);

                }
            }
        }


        if (postCampFileListModels.isEmpty()) {
            Utilities.showToastMessage("Choose files to upload", context, false);
            return;
        }
//        fileIndex = 0;

//        if (Utilities.isNetworkAvailable(context)) {
//            for (PostCampFileListModel p :
//                    postCampFileListModels) {
//                filePath = p.getFilePath();
////                new UploadFile().execute();
//
//            }
//        } else {
//            Utilities.showToastMessage(getString(R.string.msgt_nointernetconnection), context, false);
//        }


        if (Utilities.isNetworkAvailable(context)) {
            new InsertBillDetails().execute(campArr.toString(), "2", subexpenseheadId, edtRemark.getText().toString().trim(), EmpCode);

        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);

        }

    }

    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
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
                tvCampType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());

//                if (campTypeId.equalsIgnoreCase("1") || (campTypeId.equalsIgnoreCase("2") || (campTypeId.equalsIgnoreCase("4")))) {
//                    llteam.setVisibility(View.GONE);
//                } else {
//                    llteam.setVisibility(View.VISIBLE);
//                }
//                if (barcodeList != null) {
//                    barcodeList.clear();
//                }
//                tvSampleBarcode.setText("");


                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }

    @Override
    public void onDelete(PostCampFileListModel postCampFileListModel, int pos) {
        postCampFileListModels.remove(postCampFileListModel);
        if (postCampFileListModels.isEmpty()) {
            rvFileList.setVisibility(View.GONE);
//            binding.tvError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    public class GetCampList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("FromReqDate", params[0]));
            param.add(new ParamsPojo("ToReqDate", params[1]));
            param.add(new ParamsPojo("distlgdcode", params[2]));
            param.add(new ParamsPojo("UserID", params[3]));
            param.add(new ParamsPojo("SubExpenseID", params[4]));

//            res = WebServiceCall.APICall(ApplicationConstants.GetExpenseCampIDList, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetExpenseCampIDList_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    //  selectedcamplist = new ArrayList<>();
                    //  Log.d("result ",result.toString());
                    CamplistOnLandingLabModel camplistOnLandingLabModel = new Gson().fromJson(result, CamplistOnLandingLabModel.class);
                    type = camplistOnLandingLabModel.getStatus();
                    message = camplistOnLandingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        if (camplistOnLandingLabModel.getOutput().size() > 0) {
                            campList.addAll(camplistOnLandingLabModel.getOutput());

                            showCampListDialog(campList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Camp I'd not available", false);
            }
        }

    }

    private void showCampListDialog(final ArrayList<CamplistOnLandingLabModel.Output> campList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select CampId");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        CampListCheckAdapterNew CampListCheckAdapterNew = new CampListCheckAdapterNew(campList);
        rvList.setAdapter(CampListCheckAdapterNew);

        //        rvList.addOnItemTouchListener(
        //                new RecyclerItemClickListener(context,
        //                        new RecyclerItemClickListener.OnItemClickListener() {
        //                            @Override
        //                            public void onItemClick(View view, final int position) {
        //                                GetTeamsModel.OutputBean team = teamList.get(position);
        //                                selectedTeam = new ArrayList<>();
        //                                selectedTeam.add(team);
        //                                tvTeamId.setText(selectedTeam.get(0).getTeamName());
        //                            }
        //                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new CampListCheckAdapterNew(campList));
                    return;
                }

                if (campList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<CamplistOnLandingLabModel.Output> searchedTestList = new ArrayList<>();
                    for (CamplistOnLandingLabModel.Output clientDetails : campList) {

                        String countryToBeSearched = clientDetails.getCampId().toString();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new CampListCheckAdapterNew(searchedTestList));
                } else {
                    rvList.setAdapter(new CampListCheckAdapterNew(campList));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        androidx.appcompat.app.AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int billamount = 0;
                int numOfTeams = 0;
                for (CamplistOnLandingLabModel.Output team : campList
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                        billamount += team.getExpenseAmount();


                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

                tvCampId.setText(String.valueOf("Selected Camp" + " " + numOfTeams));
                tvTotalAmount.setText("" + billamount);
//                edtRemark.setText("" + billamount);
//                edtRemark.setEnabled(false);

            }
        });

//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Select All", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                int billamount = 0;
//                int numOfTeams = 0;
//                for (CamplistOnLandingLabModel.Output team : campList
//                ) {
//
//                    team.setChecked(true);
//
//                    if (team.isChecked()) {
//                        numOfTeams += 1;
//                        billamount += team.getExpenseAmount();
//
//
//                    }
//                }
//                Log.d("TAG", "onClick: " + numOfTeams);
//
//                tvCampId.setText(String.valueOf("Selected Camp" + " " + numOfTeams));
//                tvTotalAmount.setText("" + billamount);
////                edtRemark.setText("" + billamount);
////                edtRemark.setEnabled(false);
//
//            }
//        });


        alertDialog.show();

    }


    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    // Use the cropped image URI.
                    String path = result.getUriFilePath(context, true);


//                    try {
//                        createPdf(getThumbnail(resultUri));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    savefile(result.getUriContent());

//                    if (imageType == 0) {
//                        savefile(result.getUriContent());
//                    } else if (imageType == 1) {
//                        saveAckfile(result.getUriContent());
//                    }

                    Log.i("cropImageLauncher", ": " + path);
                    // Process the cropped image URI as needed.
                } else {
                    // An error occurred.
                    Exception exception = result.getError();
                    // Handle the error.
                }
            }
    );

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                Uri imageUri = data.getData();
//                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).start(BillUploadActivity.this);

                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
//                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                CropImageContractOptions options = new CropImageContractOptions(imageUri, cropImageOptions);
                cropImageLauncher.launch(options);



            }

            if (requestCode == 12) {
                Uri pdfUri = data.getData();
                Log.d("ActivityResult", pdfUri.getPath());
                savePdfFile(pdfUri);
//                if (filePath != null) {
//                    postCampFileListModels.add(new PostCampFileListModel(filename, filePath));
//                    setupRecyclerView();
//                    filename = "filename";
//                    filePath = null;
////                    tvFileName.setText("Capture Image");
//                } else {
//                    Utilities.showToastMessage("Please capture file", context, false);
//                }
            }

            if (resultCode == RESULT_OK) {
                if (requestCode == LETTER_CAMERA_REQUEST) {
//                    CropImage.activity(photoURI).setGuidelines(CropImageView.Guidelines.ON).start(BillUploadActivity.this);


                    CropImageOptions cropImageOptions = new CropImageOptions();
                    cropImageOptions.guidelines = CropImageView.Guidelines.ON;
//                    cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                    CropImageContractOptions options = new CropImageContractOptions(photoURI, cropImageOptions);
                    cropImageLauncher.launch(options);

                }

            }

        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//
//                try {
//                    createPdf(getThumbnail(resultUri));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (imageType == 0) {
//                    savefile(resultUri);
//
//
//                } else if (imageType == 1) {
////                    saveAckfile(resultUri);
//
//
//                }
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode ==12) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                try {
//                    createPdf(getThumbnail(resultUri));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
////                saveImageFile(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
//
//    }


    public class GetLandingLab extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetUserMappedLabList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    //  campList = new ArrayList<>();
                    List<LandingLabNewModel.Output> labList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    LandingLabNewModel landingLabNewModel = new Gson().fromJson(result, LandingLabNewModel.class);
                    type = landingLabNewModel.getStatus();
                    message = landingLabNewModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        labList = landingLabNewModel.getOutput();

                        if (labList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showLandingDialog(labList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showLandingDialog(final List<LandingLabNewModel.Output> labList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Landing Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < labList.size(); i++) {
            arrayAdapter.add(String.valueOf(labList.get(i).getLabName()));

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
                tvLandingLab.setText(labList.get(which).getLabName());
                selectedLabID = String.valueOf(labList.get(which).getLabCode());
                //  CampDate = labList.get(which).getCampDate();


                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Bill");

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

//        btn_save_accordian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(context, TeamCampMappingdetailsActivity.class));
//                finish();
//            }
//        });
    }


    private void selectImage() {


        final CharSequence[] options = {"Take a Photo", "Choose from Gallery", "PDF"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Select Photo");
        builder.setCancelable(false);
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take a Photo")) {
                file = new File(patientPicsFolder, "_PR.png");
                photoURI = Uri.fromFile(file);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                int randomEndtNo = (int) (Math.random() * 99999 + 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_PR.png");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    photoURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_PR.png");
                    letterUri = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);

                }

            } else if (options[item].equals("Choose from Gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            } else if (options[item].equals("PDF")) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12);

            }
        });
        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public class GetTeamDetailsListForAssign extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));


            res = WebServiceCall.APICall(ApplicationConstants.GetTeamDetailsForSampleAcceptance, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    assignteamlist = new ArrayList<>();
                    ArrayList<TeamsDetailsModelNew.OutputBean> assignteamlist = new ArrayList<>();
                    TeamsDetailsModelNew teamsDetailsModelNew = new Gson().fromJson(result, TeamsDetailsModelNew.class);
                    type = teamsDetailsModelNew.getStatus();
                    message = teamsDetailsModelNew.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        assignteamlist = teamsDetailsModelNew.getOutput();
                        if (assignteamlist.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showAssignedTeamListDialog(assignteamlist);

                            // ll_assigned.setVisibility(View.VISIBLE);


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", "Team not assigned for selected campId, assign team first", false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please select CampId", "You have to select campId", false);
            }
        }
    }

    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModelNew.OutputBean> assignteamlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        rvList.setAdapter(new TeamsDetailsAssignedAdapterNew(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModelNew.OutputBean team = assignteamlist.get(position);
                                AssignedId = assignteamlist.get(position).getTeamNumber();

                                tvTeamNumber.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();

                                if (barcodeList != null) {
                                    barcodeList.clear();
                                }
                                tvSampleBarcode.setText("");


                                new GetBarcodeCount().execute(CampDate, String.valueOf(campTypeId), selectedLabID, CampId, AssignedId, "2");

                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new TeamsDetailsAssignedAdapterNew(assignteamlist));
                    return;
                }

                if (assignteamlist.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<TeamsDetailsModelNew.OutputBean> searchedTestList = new ArrayList<>();
                    for (TeamsDetailsModelNew.OutputBean clientDetails : assignteamlist) {

                        String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                        String member2 = clientDetails.getMember2().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                member2.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new TeamsDetailsAssignedAdapterNew(searchedTestList));
                } else {
                    rvList.setAdapter(new TeamsDetailsAssignedAdapterNew(assignteamlist));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        teamDialog = builderSingle.create();
        teamDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (TeamsDetailsModelNew.OutputBean team : assignteamlist
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        teamDialog.show();

    }


    void setupRecyclerView() {
        rvFileList.setVisibility(View.VISIBLE);
//        tvError.setVisibility(View.GONE);
        rvFileList.setHasFixedSize(false);
        rvFileList.setLayoutManager(new LinearLayoutManager(context));
        rvFileList.setAdapter(new PostCampFileListAdapter(postCampFileListModels, this));
    }


    android.app.AlertDialog labDialog;



//
//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//
//        filename = (int) (Math.random() * 99999 + 1) + "_PR.png";
//
//        destinationFilename = patientPicsFolder + filename;
//
//
//        String sourceFilename = sourceuri.getPath();
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//
//        try {
//            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
//            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
//            byte[] buf = new byte[1024];
//            bis.read(buf);
//            do {
//                bos.write(buf);
//            } while (bis.read(buf) != -1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bis != null) bis.close();
//                if (bos != null) bos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        letterPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
//        imv_patient.setImageBitmap(letterPicBm);
//        filePath = destinationFilename;
//
////        new BeneficiaryVerificationActivity.ApproveBeneficiary().execute(filePath,"1");
//
//
//    }


    private void savefile(Uri sourceuri) {
        pd.setMessage("Saving Image,\n Please wait...");
        pd.setCancelable(false);
        pd.show();



        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
//                Log.i("TAG", "handleMessage: " + new Gson().toJson(message));
                pd.dismiss();
                return false;
            }
        });


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("sourceuri1", "" + sourceuri);
                String destinationFilename = "";

                filename = (int) (Math.random() * 99999 + 1) + "_PR.png";

                destinationFilename = patientPicsFolder + filename;




                String sourceFilename = sourceuri.getPath();
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                Log.i("sourceFilename", "run: " + sourceFilename);
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(sourceuri);

                    bis = new BufferedInputStream(inputStream);
                    bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
                    byte[] buf = new byte[1024];
                    bis.read(buf);
                    do {
                        bos.write(buf);
                    } while (bis.read(buf) != -1);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bis != null) bis.close();
                        if (bos != null) bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {

                    letterPicBm = BitmapFactory.decodeFile(destinationFilename);
                    Utilities.getBitmapAsByteArray(letterPicBm);
                    destinationFilename = compressImage(destinationFilename);
                    letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imv_patient.setImageBitmap(letterPicBm);
                            pd.dismiss();

                            Bundle bundle = new Bundle();
                            bundle.putString("message", "Success");
                            Message message = new Message();
                            message.setData(bundle);
//                            handler.sendMessage(message);
//
//
//                            handler.removeCallbacks(this);

                        }
                    });


                    filePath = destinationFilename;




                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }




    public void savePdfFile(Uri sourceuri) {
        Log.i("sourceuri1", "" + sourceuri);
        String path = FileUtils.getPathFromURI(context, sourceuri);
        String destinationFilename = "";

        filename = (int) (Math.random() * 99999 + 1) + "_PR.pdf";

        destinationFilename = patientPicsFolder + "/" + filename;


        String sourceFilename = path;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        letterPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
//        imv_patient.setImageBitmap(letterPicBm);
        filePath = destinationFilename;
        File file1 = new File(filePath);
        long sizeInKB = file1.length() / 1024;

        if (sizeInKB > 2000){
            Utilities.showAlertDialog(context,"Alert","File Size should be under 2MB please check file size",false);
            return;
        }

        if (filePath != null) {
            postCampFileListModels.add(new PostCampFileListModel(filename, filePath));
            setupRecyclerView();
            filename = "filename";
            filePath = null;
//                    tvFileName.setText("Capture Image");
        } else {
            Utilities.showToastMessage("Please capture file", context, false);
        }
//        new BeneficiaryVerificationActivity.ApproveBeneficiary().execute(filePath,"1");


    }

    private class GetBarcodeCount extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campdate", params[0]));
            param.add(new ParamsPojo("camptype", params[1]));
            param.add(new ParamsPojo("LandingLab", params[2]));
            param.add(new ParamsPojo("campID", params[3]));
            param.add(new ParamsPojo("Teamid", params[4]));
            param.add(new ParamsPojo("type", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.GetSampleBarcodeList, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            Log.d("GetBarcodelist Resp", result);

            try {
                pd.dismiss();
                if (!result.equals("")) {
                    BarcodeListCountNewModel barcodeListCountNewModel = new Gson().fromJson(result, BarcodeListCountNewModel.class);
                    if (barcodeListCountNewModel.getStatus().equalsIgnoreCase("success")) {
//                        Collections.sort(clientListModel.getOutput(), (o1, o2) -> o1.getClientName().compareTo(o2.getClientName()));
                        if (barcodeListCountNewModel.getOutput().size() > 0) {
                            BarcodeListCountNewModel.Output output = barcodeListCountNewModel.getOutput().get(0);


                            tvSampleCount.setText(output.getTotalBarcode());

//                            barcodeList = new ArrayList<>();
                            // selectedBarcodeList = new ArrayList<>();
                            //  barcodecountList = barcodeListCountNewModel.getOutput();


//                             ArrayList<BarcodeListCountNewModel.Output> count =barcodeListCountNewModel.getOutput();

//                            BarcodeListDialog(barcodeList);


                        } else {
                            Utilities.showAlertDialog(context, "No Barcodes Available", "", false);
                        }
                    } else
                        Utilities.showAlertDialog(context, "Fail", "Barcode Not Found", false);

                } else {
                    Utilities.showAlertDialog(context, "Fail", "Empty response", false);

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Data Not Found", false);
                e.printStackTrace();
            }
        }
    }


    private class InsertBillDetails extends AsyncTask<String, Void, String> {

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
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("TYPE_MultipleBillCampDetails", params[0]));
            param.add(new ParamsPojo("RequestType", params[1]));
            param.add(new ParamsPojo("SubExpenseID", params[2]));
            param.add(new ParamsPojo("AmountOnBill", params[3]));
            param.add(new ParamsPojo("CreatedBy", params[4]));
//            res = WebServiceCall.APICall(ApplicationConstants.InsertMultipleCampID, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.InsertMultipleCampID_V2, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertSampleAccept", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        billId = message;

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Bill Details Saved successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();


                                fileIndex = 0;

                                if (Utilities.isNetworkAvailable(context)) {
                                    for (PostCampFileListModel p :
                                            postCampFileListModels) {
                                        uploadFilePath = p.getFilePath();
                                        Log.d("TAG", "sent File: " + uploadFilePath);

                                        new UploadFile(p.getFilePath()).execute();

                                    }
                                }

//                                finish();

                                //  ll_assigned.setVisibility(View.VISIBLE);

                            }
                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(context, status, message, false);


                        postCampFileListModels.clear();
                        rvFileList.setVisibility(View.GONE);

                        Drawable drawable = ContextCompat.getDrawable(BillUploadActivity.this, R.drawable.icon_colorcamera);
                        imv_patient.setImageDrawable(drawable);
//                        imv_patient.setImageDrawable(R.drawable.icon_colorcamera);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetExpenseHead extends AsyncTask<String, Void, String> {

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
//            param.add(new ParamsPojo("STATELGDCODE", params[0]));
//            param.add(new ParamsPojo("DISTLGDCODE", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetExpensesMasterData, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    ExpenseHeadModel expenseHeadModel = new Gson().fromJson(result, ExpenseHeadModel.class);
                    type = expenseHeadModel.getStatus();
                    message = expenseHeadModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<ExpenseHeadModel.Output> camptypelist = expenseHeadModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showExpenseHeadDialogue(camptypelist);
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

        private void showExpenseHeadDialogue(final List<ExpenseHeadModel.Output> expenseHeadList) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Expense Head");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < expenseHeadList.size(); i++) {
                arrayAdapter.add(String.valueOf(expenseHeadList.get(i).getExpenseHeadName()));
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
                    tvExpenseHead.setText(expenseHeadList.get(which).getExpenseHeadName());
                    expenseheadId = String.valueOf(expenseHeadList.get(which).getExpenseHead());

                    tvSubExpenseHead.setText("");
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }

    public class GetSubExpenseHead extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("ExpenseHead", params[0]));
            param.add(new ParamsPojo("OrganisedBy", params[1]));
            param.add(new ParamsPojo("CampType", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetSubExpensesMasterData_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    SubExpenseHeadModel subExpenseHeadModel = new Gson().fromJson(result, SubExpenseHeadModel.class);
                    type = subExpenseHeadModel.getStatus();
                    message = subExpenseHeadModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<SubExpenseHeadModel.Output> camptypelist = subExpenseHeadModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showSubExpenseHeadDialogue(camptypelist);
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

        private void showSubExpenseHeadDialogue(final List<SubExpenseHeadModel.Output> subexpenseHeadList) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Sub Expense Head");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < subexpenseHeadList.size(); i++) {
                arrayAdapter.add(String.valueOf(subexpenseHeadList.get(i).getSubexpenseName()));
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
                    tvSubExpenseHead.setText(subexpenseHeadList.get(which).getSubexpenseName());
                    subexpenseheadId = String.valueOf(subexpenseHeadList.get(which).getSubExpenseIDPk());
                    //  refreshCalendar();

                    if (campList!=null){
                        campList.clear();
                    }

                    tvCampId.setText("");
                }
            });
            builderSingle.show();

        }

    }



    class UploadFile extends AsyncTask<String, Void, String> {
        String filePath;

        public UploadFile(String filePath) {
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isFileUploading = true;
            pd.setMessage("Uploading " + postCampFileListModels.size() + " files..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
//            Log.d(TAG, "onPostExecute: " + response);
            isFileUploading = false;

            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
//                        postCampFileListModels.remove(fileIndex);
                        fileIndex = fileIndex + 1;
                        if (fileIndex == postCampFileListModels.size()) {
                            pd.dismiss();

                            Utilities.showAlertDialog(context, "Uploaded Successfully", "File uploaded successfully", true, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                        }

                    } else {
                        pd.dismiss();

//                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

                        Utilities.showAlertDialog(context, "Failed", msg, false, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
//                                finish();
                            }
                        });
                    }

                } catch (JSONException e) {
                    pd.dismiss();

                    e.printStackTrace();
                    Utilities.showToastMessage(response, context, false);
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            JsonArray campArr = new JsonArray();
            if (campList != null) {
                for (CamplistOnLandingLabModel.Output o : campList) {
                    if (o.isChecked()) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("CampID", o.getCampid());
                        campArr.add(jsonObject);

                    }
                }
            }

            File uploadFile = new File(filePath);
            Log.d("TAG", "doInBackground: " + uploadFile.getName() + " " + filePath);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("Billid", billId)
                    .addFormDataPart("createdBy", EmpCode)
                    .addFormDataPart("ExpenseHead", expenseheadId)
                    .addFormDataPart("AttachementProof", uploadFile.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    uploadFile))
                    .addFormDataPart("TYPE_MultipleBillCampDetails",campArr.toString())
                    .addFormDataPart("SubExpenseID",subexpenseheadId)
                    .build();
            Request request = new Request.Builder()
                    .url(ApplicationConstants.Upload_Post_Camp_File_Handler)
                    .method("POST", body)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }

        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 1080) ? (originalSize / 1080) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }


    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }


    private void createPdf(Bitmap bitmap) {
        String fname = (int) (Math.random() * 99999 + 1) + "_postCamp" + ".pdf";

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHeight = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
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


        // write the document content
        String path = getExternalCacheDir() + "/" + fname;
        File file = new File(path);
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();

        filePath = path;
        filename = fname;
//        binding.tvFileName.setText(fname);
//        Log.d(TAG, "createPdf: " + path);
    }


    public class GetInitiatedByList extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetInitiatedByListForCamp, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    GetInitiatedByListForCampModel getInitiatedByListForCampModel = new Gson().fromJson(result, GetInitiatedByListForCampModel.class);
                    type = getInitiatedByListForCampModel.getStatus();
                    message = getInitiatedByListForCampModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<GetInitiatedByListForCampModel.Output> camptypelist = getInitiatedByListForCampModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showInitiatedByDialog(camptypelist);
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

        private void showInitiatedByDialog(final List<GetInitiatedByListForCampModel.Output> initiatedlist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Initiated By");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < initiatedlist.size(); i++) {
                arrayAdapter.add(String.valueOf(initiatedlist.get(i).getInitiatedBy()));
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
                    tvOrganizedBy.setText(initiatedlist.get(which).getInitiatedBy());
                    initiatedId = String.valueOf(initiatedlist.get(which).getId());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }

}