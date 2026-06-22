package com.myhindlab.abkat.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.ConstructionSitesList_Model;
import com.myhindlab.abkat.models.InsertConstructionWorkerDetails_Model;
import com.myhindlab.abkat.pojos.InsertConstructionWorkDetailsResponse;
import com.myhindlab.abkat.rest.HandlerClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;

@SuppressLint("SetTextI18n")
public class SiteSurvey_BuildingConstWorkerDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ProgressDialog pd;
    private String TAG = getClass().getName();
    private final int CAMERA_REQUEST = 100;

    //    private EditText edt_reranumber, edt_aarogyamittraname, edt_hospitalname;
    private EditText edt_numberofworkersreg, edt_numberofworkersnotreg;
    private RadioButton rb_slab, rb_plinth, rb_digging;
    //            rb_affiliatedhospital_no, rb_affiliatedhospital_yes,
//            rb_transport_no, rb_transport_yes;
    private RadioButton rb_camparrange_no, rb_camparrange_yes, rb_electricityavailable_no,
            rb_electricityavailable_yes, rb_toiletavailable_no, rb_toiletavailable_yes;
    private ImageView img_sitephoto1, img_sitephoto2, img_docphoto1, img_docphoto2;
    private Button btn_submit;
//    private Button btn_done;

    private File patientPicsFolder;
    private Uri photoURI;
    private ConstructionSitesList_Model siteDetails;
    //    private String hospitalID, hospitalName;
    private int photoType = 0; /* 100 for site photo1, 101 for site photo2
                                  200 for other photo1, 201 for other photo2*/
    private String sitePhotoPath1, sitePhotoPath2, docPhotoPath1, docPhotoPath2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitesurvey_buildingconstworkerdetails);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
//        edt_reranumber = findViewById(R.id.edt_reranumber);

        rb_slab = findViewById(R.id.rb_slab);
        rb_plinth = findViewById(R.id.rb_plinth);
        rb_digging = findViewById(R.id.rb_digging);

        edt_numberofworkersreg = findViewById(R.id.edt_numberofworkersreg);
        edt_numberofworkersnotreg = findViewById(R.id.edt_numberofworkersnotreg);
//
//        rb_affiliatedhospital_no = findViewById(R.id.rb_affiliatedhospital_no);
//        rb_affiliatedhospital_yes = findViewById(R.id.rb_affiliatedhospital_yes);
//        edt_hospitalname = findViewById(R.id.edt_hospitalname);
//
//        edt_aarogyamittraname = findViewById(R.id.edt_aarogyamittraname);
//
        rb_camparrange_no = findViewById(R.id.rb_camparrange_no);
        rb_camparrange_yes = findViewById(R.id.rb_camparrange_yes);
        rb_electricityavailable_no = findViewById(R.id.rb_electricityavailable_no);
        rb_electricityavailable_yes = findViewById(R.id.rb_electricityavailable_yes);
        rb_toiletavailable_no = findViewById(R.id.rb_toiletavailable_no);
        rb_toiletavailable_yes = findViewById(R.id.rb_toiletavailable_yes);
//
//        rb_transport_no = findViewById(R.id.rb_transport_no);
//        rb_transport_yes = findViewById(R.id.rb_transport_yes);
//
        img_sitephoto1 = findViewById(R.id.img_sitephoto1);
        img_sitephoto2 = findViewById(R.id.img_sitephoto2);

        img_docphoto1 = findViewById(R.id.img_docphoto1);
        img_docphoto2 = findViewById(R.id.img_docphoto2);

        btn_submit = findViewById(R.id.btn_submit);
//        btn_done = findViewById(R.id.btn_done);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConstantData constantData = ConstantData.getInstance();
        getSupportActionBar().setTitle(constantData.getSetSitetypeName() + " Site Survey");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        context = SiteSurvey_BuildingConstWorkerDetails_Activity.this;
        pd = new ProgressDialog(context);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        patientPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/SiteSurveyPhotos/");
        if (!patientPicsFolder.exists()) {
            patientPicsFolder.mkdirs();
        }

        siteDetails = new ConstructionSitesList_Model();
        siteDetails = (ConstructionSitesList_Model) getIntent().getSerializableExtra("siteDetails");
//
//        String reraNumber = siteDetails.getReraId();
//        edt_reranumber.setText(reraNumber);
//
//        if (Utilities.isNetworkAvailable(context)) {
//            new GetListOfWorkerDetailsAPICall().execute(siteDetails.getBuilderID());
//        } else {
//            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//        }
    }

    private void setEventHandler() {
        btn_submit.setOnClickListener(this);
//        btn_done.setOnClickListener(this);
//
//        edt_hospitalname.setOnClickListener(this);
//
        img_sitephoto1.setOnClickListener(this);
        img_sitephoto2.setOnClickListener(this);

        img_docphoto1.setOnClickListener(this);
        img_docphoto2.setOnClickListener(this);
//
//        rb_affiliatedhospital_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edt_hospitalname.setVisibility(View.GONE);
//                edt_hospitalname.setText("");
//                hospitalID = "";
//            }
//        });
//
//        rb_affiliatedhospital_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edt_hospitalname.setVisibility(View.VISIBLE);
//                edt_hospitalname.setText("");
//                hospitalID = "";
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_sitephoto1:
                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }

                photoType = 100;
                capturePhoto();
                break;

            case R.id.img_sitephoto2:
                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }

                photoType = 101;
                capturePhoto();
                break;

            case R.id.img_docphoto1:
                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }

                photoType = 200;
                capturePhoto();
                break;

            case R.id.img_docphoto2:
                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }

                photoType = 201;
                capturePhoto();
                break;
//
//            case R.id.edt_hospitalname:
//                if (!Utilities.isNetworkAvailable(context)) {
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                    return;
//                }
//
//                new GetNearistHospitalList().execute(siteDetails.getDISTLGDCODE());
//                break;
//
            case R.id.btn_submit:
                if (!Utilities.isNetworkAvailable(context)) {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    return;
                }
//
//                if (edt_aarogyamittraname.getText().toString().trim().isEmpty()) {
//                    edt_aarogyamittraname.setError("Required Aarogya Mittra Name");
//                    edt_aarogyamittraname.requestFocus();
//                    return;
//                }
//
                if (edt_numberofworkersreg.getText().toString().trim().isEmpty()) {
                    edt_numberofworkersreg.setError("Required number of construction workers registered.");
                    edt_numberofworkersreg.requestFocus();
                    return;
                } else {
                    int cnt = Integer.parseInt(edt_numberofworkersreg.getText().toString().trim());
                    if (cnt < 0) {
                        edt_numberofworkersreg.setError("Number of construction workers can not be zero or less then zero.");
                        edt_numberofworkersreg.requestFocus();
                        return;
                    }
                }

                if (edt_numberofworkersnotreg.getText().toString().trim().isEmpty()) {
                    edt_numberofworkersnotreg.setError("Required number of construction workers not registered.");
                    edt_numberofworkersnotreg.requestFocus();
                    return;
                } else {
                    int cnt = Integer.parseInt(edt_numberofworkersnotreg.getText().toString().trim());
                    if (cnt < 0) {
                        edt_numberofworkersnotreg.setError("Number of construction workers can not be zero or less then zero.");
                        edt_numberofworkersnotreg.requestFocus();
                        return;
                    }
                }

                int nonregw = Integer.parseInt(edt_numberofworkersnotreg.getText().toString().trim());
                int regw = Integer.parseInt(edt_numberofworkersreg.getText().toString().trim());

                if ((nonregw + regw) <= 0) {
                    edt_numberofworkersreg.setError("Total workers count could not be zero");
                    edt_numberofworkersreg.requestFocus();
                    edt_numberofworkersnotreg.setError("Total workers count could not be zero");
                    edt_numberofworkersnotreg.requestFocus();
                    Utilities.showToastMessage("Total worker count could not be zero.", context, false);
                    return;
                }
//
//                if (rb_affiliatedhospital_yes.isChecked()) {
//                    if (hospitalID == null || hospitalID.equalsIgnoreCase("")) {
//                        Utilities.showToastMessage("Please select Hospital name.", context, false);
//                        edt_hospitalname.requestFocus();
//                        return;
//                    }
//                }
//
                if (sitePhotoPath1 == null || sitePhotoPath1.equalsIgnoreCase("")) {
                    Utilities.showToastMessage("Please take site photo 1.", context, false);
                    return;
                }

                if (sitePhotoPath2 == null || sitePhotoPath2.equalsIgnoreCase("")) {
                    Utilities.showToastMessage("Please take site photo 2.", context, false);
                    return;
                }

                insertAPICall();
                break;
//
//            case R.id.btn_done:
//                finish();
//                break;
//
            default:
                break;
        }
    }

    private void capturePhoto() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
            return;
        }

//        File tempFile = new File(patientPicsFolder, "TempFile.png");
//        photoURI = Uri.fromFile(tempFile);
//        Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        pickImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//        startActivityForResult(pickImage, CAMERA_REQUEST);

        int randomEndtNo = (int) (Math.random() * 999999 + 1);

        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        photoURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
//                CropImage.activity(photoURI)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(SiteSurvey_BuildingConstWorkerDetails_Activity.this);
            }
        }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                savefile(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Utilities.showToastMessage(error.toString(), context, false);
//            }
//        }
    }

    public void savefile(Uri sourceuri) {
//        String sourceFilename = sourceuri.getPath();
        String sourceFilename = Utilities.compressImage(sourceuri.getPath());
//        String filename = sourceFilename.substring(sourceFilename.lastIndexOf("/") + 1);
//        String destinationFilename = Environment.getExternalStorageDirectory()
//                + "/Health Checkup/SiteSurveyPhotos/";
        String destinationFilename = getExternalCacheDir()+"/";
        String fileType = "0";
        if (photoType == 100) {
            destinationFilename = destinationFilename + "SitePhoto1.png";
            fileType = "1";
        } else if (photoType == 101) {
            destinationFilename = destinationFilename + "SitePhoto2.png";
            fileType = "2";
        } else if (photoType == 200) {
            destinationFilename = destinationFilename + "DocPhoto1.png";
            fileType = "3";
        } else if (photoType == 201) {
            destinationFilename = destinationFilename + "DocPhoto2.png";
            fileType = "4";
        } else {
            destinationFilename = destinationFilename + "TempFile.png";
            fileType = "0";
        }

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

        destinationFilename = compressImage(destinationFilename);
        InsertConstructionWorkDetailsImage_HandlerAPICall("0",
                siteDetails.getSiteDetailId(), siteDetails.getUserID(), fileType,
                siteDetails.getBuilderID(), destinationFilename);
//        new InsertConstructionWorkDetailsImage().execute(siteDetails.getReraId(),
//                siteDetails.getSiteDetailId(), siteDetails.getUserID(), fileType,
//                siteDetails.getBuilderID(), destinationFilename);
    }

    //    private class GetNearistHospitalList extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetNearistHospitalList, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                    ArrayList<GetNearistHospitalListPojo> hospitalList = new ArrayList<>();
//                    GetNearistHospitalList_Responce responce = new Gson().fromJson(result, GetNearistHospitalList_Responce.class);
//                    type = responce.getStatus();
//                    message = responce.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        hospitalList = responce.getOutput();
//                        if (hospitalList.size() > 0) {
//                            showHospitalListDialog(hospitalList);
//                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
//    }
//
//    private void showHospitalListDialog(final ArrayList<GetNearistHospitalListPojo> hospitalList) {
//        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setTitle("Select Hospital");
//        builderSingle.setCancelable(false);
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
//
//        for (int i = 0; i < hospitalList.size(); i++) {
//            arrayAdapter.add(String.valueOf(hospitalList.get(i).getHospitalName()));
//        }
//
//        builderSingle.setNegativeButton("Cancel", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builderSingle.setAdapter(arrayAdapter, new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int pos) {
//                edt_hospitalname.setText(hospitalList.get(pos).getHospitalName());
//                hospitalName = hospitalList.get(pos).getHospitalName();
//                hospitalID = hospitalList.get(pos).getHospitalId();
//            }
//        });
//        builderSingle.show();
//    }
//
    private void insertAPICall() {
        InsertConstructionWorkerDetails_Model data = new InsertConstructionWorkerDetails_Model();

//        String aName = edt_aarogyamittraname.getText().toString().trim();
        String countworkersreg = edt_numberofworkersreg.getText().toString().trim();
        String countworkersnotreg = edt_numberofworkersnotreg.getText().toString().trim();

//        String isHospitalExist;
//        if (rb_affiliatedhospital_yes.isChecked()) {
//            isHospitalExist = "1";
//        } else {
//            isHospitalExist = "0";
//            hospitalID = "";
//            hospitalName = "";
//        }
        String stageOfConstruction;
        if (rb_slab.isChecked()) {
            stageOfConstruction = "1";
        } else if (rb_plinth.isChecked()) {
            stageOfConstruction = "2";
        } else if (rb_digging.isChecked()) {
            stageOfConstruction = "3";
        } else {
            stageOfConstruction = "0";
        }

        String canArrangeCamp;
        if (rb_camparrange_yes.isChecked()) {
            canArrangeCamp = "1";
        } else {
            canArrangeCamp = "0";
        }

        String isElectricityAvailable;
        if (rb_electricityavailable_yes.isChecked()) {
            isElectricityAvailable = "1";
        } else {
            isElectricityAvailable = "0";
        }

        String isToiletAvailable;
        if (rb_toiletavailable_yes.isChecked()) {
            isToiletAvailable = "1";
        } else {
            isToiletAvailable = "0";
        }
//        String isTransportsurviceNecessary;
//        if (rb_transport_yes.isChecked()) {
//            isTransportsurviceNecessary = "1";
//        } else {
//            isTransportsurviceNecessary = "0";
//        }
//
        data.setBuilderDetailId(siteDetails.getBuilderID());
        data.setConstructionStageId(stageOfConstruction);
        data.setNoWorkers(countworkersreg);
        data.setNoWorkerNonRegister(countworkersnotreg);
        data.setHavingNearestHospital("0");
//        data.setHavingNearestHospital(isHospitalExist);
//        data.setHospitalName(hospitalID);
        data.setHospitalName("0");
        data.setCanArrangeCamp(canArrangeCamp);
        data.setIsTransportNecessary("0");
        data.setIsElectricityAvailable(isElectricityAvailable);
        data.setIsToiletAvailable(isToiletAvailable);
//        data.setIsTransportNecessary(isTransportsurviceNecessary);
        data.setCreatedBy(siteDetails.getUserID());
        data.setArogyaMitraName("");
//        data.setArogyaMitraName(aName);

//        data.setSiteDetailId(siteDetails.getSiteDetailId());
//        data.setReraId(siteDetails.getReraId());
//        data.setReraNo(siteDetails.getReraNo());
//        data.setBuilderDetailID(siteDetails.getBuilderID());
//        data.setDISTLGDCODE(siteDetails.getDISTLGDCODE());
//        data.setCreatedBy(siteDetails.getUserID());
//        data.setAarogyaMittraName(aName);
//        data.setNumbersOfConstWorkers(countworkers);
//        data.setIsAffiliatedHospitalExist(isHospitalExist);
//        data.setHospitalID(hospitalID);
//        data.setStageOfConstruction(stageOfConstruction);
//        data.setCanCampArranged(canArrangeCamp);
//        data.setIstransportExist(isTransportsurviceNecessary);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String inputString = gson.toJson(data);

        new InsertConstructionWorkerDetails().execute(inputString);
    }

    public class InsertConstructionWorkerDetails extends AsyncTask<String, Integer, String> {

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
            param.add(new ParamsPojo("ConstructionWorkerJSON", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.InsertConstructionWorkerDetails, ApplicationConstants.webservice, param);
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
                        Utilities.showAlertDialog(context, "Success", "Data Saved Successfully.", true, null,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
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

//    private class InsertConstructionWorkDetailsImage extends AsyncTask<String, Integer, String> {
//        String destinationFilename;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please Wait . . .");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            destinationFilename = params[5];
//            try {
//                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.InsertConstructionWorkDetails, "UTF-8");
//
//                multipart.addFormField("ReraId", params[0]);
//                multipart.addFormField("SiteDetailId", params[1]);
//                multipart.addFormField("Userid", params[2]);
//                multipart.addFormField("FileType", params[3]);
//                multipart.addFormField("BuilderDetailID", params[4]);
//                multipart.addFilePart("File", new File(params[5]));
//
//                List<String> response = multipart.finish();
//                for (String line : response) {
//                    res = res + line;
//                }
//                return res;
//            } catch (IOException ex) {
//                return ex.toString();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            try {
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                    JSONObject obj1 = new JSONObject(result);
//                    String status = obj1.getString("status");
//                    String message = obj1.getString("message");
//
//                    if (status.equalsIgnoreCase("Success")) {
//
//                        Bitmap patientPicBm = null;
//                        patientPicBm = BitmapFactory.decodeFile(destinationFilename);
//                        patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
//
//                        if (photoType == 100) {
//                            img_sitephoto1.setImageBitmap(patientPicBm);
//                            sitePhotoPath1 = destinationFilename;
//                        } else if (photoType == 101) {
//                            img_sitephoto2.setImageBitmap(patientPicBm);
//                            sitePhotoPath2 = destinationFilename;
//                        } else if (photoType == 200) {
//                            img_docphoto1.setImageBitmap(patientPicBm);
//                            docPhotoPath1 = destinationFilename;
//                        } else if (photoType == 201) {
//                            img_docphoto2.setImageBitmap(patientPicBm);
//                            docPhotoPath2 = destinationFilename;
//                        }
//                        Utilities.showToastMessage("File saved successfully", context, true);
//
//                    } else {
//                        Utilities.showAlertDialog(context, status, message, false);
//                    }
//                } else {
//                    Utilities.showAlertDialog(context,
//                            "Please try again", "Server not responding.", false);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context,
//                        "Exception", e.getMessage(), false);
//            }
//        }
//    }

    private void InsertConstructionWorkDetailsImage_HandlerAPICall(String reraID, String siteDetailId,
                                                                   String userID, String fileType,
                                                                   String builderID, final String filePath) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait . . .");
        progressDialog.show();

        ApiInterface apiService = HandlerClient.getClient().create(ApiInterface.class);

        File fPath = new File(filePath);

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

        Call<InsertConstructionWorkDetailsResponse> call = apiService.InsertConstructionWorkDetails(reraID, siteDetailId, userID,
                fileType, builderID, imageFile);
        call.enqueue(new Callback<InsertConstructionWorkDetailsResponse>() {
            @Override
            public void onResponse(Call<InsertConstructionWorkDetailsResponse> call, Response<InsertConstructionWorkDetailsResponse> response) {
                progressDialog.dismiss();

                int surverCode = response.code();
                String surverMessage = response.message();

                if (surverCode == ApplicationConstants.OKSUCCESS) {
                    try {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (status.equalsIgnoreCase("Success")) {

                            Bitmap patientPicBm = null;
                            patientPicBm = BitmapFactory.decodeFile(filePath);
                            patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);

                            if (photoType == 100) {
                                img_sitephoto1.setImageBitmap(patientPicBm);
                                sitePhotoPath1 = filePath;
                            } else if (photoType == 101) {
                                img_sitephoto2.setImageBitmap(patientPicBm);
                                sitePhotoPath2 = filePath;
                            } else if (photoType == 200) {
                                img_docphoto1.setImageBitmap(patientPicBm);
                                docPhotoPath1 = filePath;
                            } else if (photoType == 201) {
                                img_docphoto2.setImageBitmap(patientPicBm);
                                docPhotoPath2 = filePath;
                            }
                            Utilities.showToastMessage("File saved successfully", context, true);

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
            public void onFailure(Call<InsertConstructionWorkDetailsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                Utilities.showAlertDialog(context,
                        "Please try again", t.toString(), false);
            }
        });
    }
//
//    private class GetListOfWorkerDetailsAPICall extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("Id", params[0]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetListOfWorkerDetails, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//
//            if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//                try {
//                    Gson gson = new Gson();
//                    GetListOfWorkerDetails_Responce responce = gson.fromJson(result, GetListOfWorkerDetails_Responce.class);
//                    String status = responce.getStatus();
//                    String message = responce.getMessage();
//
//                    if (status.equalsIgnoreCase("Success")) {
//                        ArrayList<GetListOfWorkerDetails> workerDetailsList = new ArrayList<>();
//                        workerDetailsList = responce.getOutput();
//
//                        if (workerDetailsList.size() > 0) {
//                            GetListOfWorkerDetails data = new GetListOfWorkerDetails();
//                            data = workerDetailsList.get(0);
//
//                            int stageOfConstruction = Integer.parseInt(data.getConstructionStageId());
//                            String numberofworkersreg = (data.getNoWorkersRegister() == null ? "" : data.getNoWorkersRegister());
//                            String numberofworkersnotreg = (data.getNoWorkerNonRegister() == null ? "" : data.getNoWorkerNonRegister());
//                            String havingNearestHospital = data.getHavingNearestHospital();
//                            String hospitalName = data.getHospitalName();
//                            String arogyaMitraName = data.getArogyaMitraName();
//                            String canArrangeCamp = data.getCanArrangeCamp();
//                            String isTransportNecessary = data.getIsTransportNecessary();
//
//                            String photo1 = data.getFile1();
//                            String photo2 = data.getFile2();
//                            String photo3 = data.getFile3();
//                            String photo4 = data.getFile4();
//
//                            if (stageOfConstruction == 1) {
//                                rb_slab.setChecked(true);
//                                rb_plinth.setChecked(false);
//                                rb_digging.setChecked(false);
//                            } else if (stageOfConstruction == 2) {
//                                rb_slab.setChecked(false);
//                                rb_plinth.setChecked(true);
//                                rb_digging.setChecked(false);
//                            } else if (stageOfConstruction == 3) {
//                                rb_slab.setChecked(false);
//                                rb_plinth.setChecked(false);
//                                rb_digging.setChecked(true);
//                            }
//
//                            edt_numberofworkersreg.setText(numberofworkersreg);
//                            edt_numberofworkersnotreg.setText(numberofworkersnotreg);
//
//                            if (havingNearestHospital.equalsIgnoreCase("true")) {
//                                rb_affiliatedhospital_yes.setChecked(true);
//                                rb_affiliatedhospital_no.setChecked(false);
//                                edt_hospitalname.setVisibility(View.VISIBLE);
//                                edt_hospitalname.setText(hospitalName);
//                            } else {
//                                rb_affiliatedhospital_yes.setChecked(false);
//                                rb_affiliatedhospital_no.setChecked(true);
//                                edt_hospitalname.setVisibility(View.GONE);
//                                edt_hospitalname.setText("");
//                            }
//
//                            edt_aarogyamittraname.setText(arogyaMitraName);
//
//                            if (canArrangeCamp.equalsIgnoreCase("true")) {
//                                rb_camparrange_yes.setChecked(true);
//                                rb_camparrange_no.setChecked(false);
//                            } else {
//                                rb_camparrange_yes.setChecked(false);
//                                rb_camparrange_no.setChecked(true);
//                            }
//
//                            if (isTransportNecessary.equalsIgnoreCase("true")) {
//                                rb_camparrange_yes.setChecked(true);
//                                rb_transport_no.setChecked(false);
//                            } else {
//                                rb_camparrange_yes.setChecked(false);
//                                rb_transport_no.setChecked(true);
//                            }
//
//                            Picasso.with(context)
//                                    .load(photo1)
//                                    .placeholder(R.drawable.icon_colorcamera)
//                                    .error(R.drawable.icon_colorcamera)
//                                    .into(img_sitephoto1);
//                            Picasso.with(context)
//                                    .load(photo2)
//                                    .placeholder(R.drawable.icon_colorcamera)
//                                    .error(R.drawable.icon_colorcamera)
//                                    .into(img_sitephoto2);
//                            Picasso.with(context)
//                                    .load(photo3)
//                                    .placeholder(R.drawable.icon_colorcamera)
//                                    .error(R.drawable.icon_colorcamera)
//                                    .into(img_docphoto1);
//                            Picasso.with(context)
//                                    .load(photo4)
//                                    .placeholder(R.drawable.icon_colorcamera)
//                                    .error(R.drawable.icon_colorcamera)
//                                    .into(img_docphoto2);
//
//                            img_sitephoto1.setClickable(false);
//                            img_sitephoto2.setClickable(false);
//                            img_docphoto1.setClickable(false);
//                            img_docphoto2.setClickable(false);
//
//                            rb_slab.setClickable(false);
//                            rb_plinth.setClickable(false);
//                            rb_digging.setClickable(false);
//                            edt_numberofworkersreg.setClickable(false);
//                            edt_numberofworkersnotreg.setClickable(false);
//                            rb_affiliatedhospital_no.setClickable(false);
//                            rb_affiliatedhospital_yes.setClickable(false);
//                            edt_hospitalname.setClickable(false);
//                            edt_aarogyamittraname.setClickable(false);
//                            rb_camparrange_no.setClickable(false);
//                            rb_camparrange_yes.setClickable(false);
//                            rb_transport_no.setClickable(false);
//                            rb_transport_yes.setClickable(false);
//                            img_sitephoto1.setClickable(false);
//                            img_sitephoto2.setClickable(false);
//                            img_docphoto1.setClickable(false);
//                            img_docphoto2.setClickable(false);
//
//                            rb_slab.setFocusable(false);
//                            rb_plinth.setFocusable(false);
//                            rb_digging.setFocusable(false);
//                            edt_numberofworkersreg.setFocusable(false);
//                            edt_numberofworkersnotreg.setFocusable(false);
//                            rb_affiliatedhospital_no.setFocusable(false);
//                            rb_affiliatedhospital_yes.setFocusable(false);
//                            edt_hospitalname.setFocusable(false);
//                            edt_aarogyamittraname.setFocusable(false);
//                            rb_camparrange_no.setFocusable(false);
//                            rb_camparrange_yes.setFocusable(false);
//                            rb_transport_no.setFocusable(false);
//                            rb_transport_yes.setFocusable(false);
//                            img_sitephoto1.setFocusable(false);
//                            img_sitephoto2.setFocusable(false);
//                            img_docphoto1.setFocusable(false);
//                            img_docphoto2.setFocusable(false);
//
//                            btn_submit.setVisibility(View.GONE);
//                            btn_done.setVisibility(View.VISIBLE);
//
//                        } else {
//                            btn_submit.setVisibility(View.VISIBLE);
//                            btn_done.setVisibility(View.GONE);
//                        }
//                    } else {
//                        btn_submit.setVisibility(View.VISIBLE);
//                        btn_done.setVisibility(View.GONE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    btn_submit.setVisibility(View.VISIBLE);
//                    btn_done.setVisibility(View.GONE);
//                }
//            } else {
//                btn_submit.setVisibility(View.VISIBLE);
//                btn_done.setVisibility(View.GONE);
//            }
//        }
//    }
}
