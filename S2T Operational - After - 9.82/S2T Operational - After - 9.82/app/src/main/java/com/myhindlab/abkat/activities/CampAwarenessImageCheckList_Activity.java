package com.myhindlab.abkat.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
import com.myhindlab.abkat.models.GetM_AdvertisementTypeList;
import com.myhindlab.abkat.models.GetM_AdvertisementTypeList_Responce;
import com.myhindlab.abkat.pojos.InsertAdvertiesmentImagesResponse;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.rest.HandlerClient;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import static com.myhindlab.abkat.utilities.Utilities.CAMERA_REQUEST;
import static com.myhindlab.abkat.utilities.Utilities.compressImage;



public class CampAwarenessImageCheckList_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private String TAG = getClass().getName();

    private GetApprovedCampListDetailsForAppList campDetails;
    private TextView tv_campnumber, tv_address,
            tv_district, tv_description;
    private MaterialEditText edt_selectchecklist, edt_remark;
    private ImageView imv_picphoto;
    private LinearLayout ll_checklistlayout;
    private Button btn_savechecklist;

    private ArrayList<GetApprovedCampListDetailsForAppList> campList;
    private String userID = "", ques = "", quesID = "";
    private Uri photoURI;
    private File awarenessImageFolder;
    private String destinationFilename = "";
    private Bitmap samplePicBm = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campawarenessimagechecklist);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        tv_campnumber = findViewById(R.id.tv_campnumber);
        tv_address = findViewById(R.id.tv_address);
        tv_district = findViewById(R.id.tv_district);
        tv_description = findViewById(R.id.tv_description);

        edt_selectchecklist = findViewById(R.id.edt_selectchecklist);
        ll_checklistlayout = findViewById(R.id.ll_checklistlayout);
        imv_picphoto = findViewById(R.id.imv_picphoto);
        edt_remark = findViewById(R.id.edt_remark);

        btn_savechecklist = findViewById(R.id.btn_savechecklist);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        awarenessImageFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/AwarenessImages/");
        if (!awarenessImageFolder.exists()) {
            awarenessImageFolder.mkdirs();
        }

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Awareness");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        context = CampAwarenessImageCheckList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        campDetails = new GetApprovedCampListDetailsForAppList();
        campDetails = (GetApprovedCampListDetailsForAppList) getIntent().getSerializableExtra("campDetails");

        tv_campnumber.setText(campDetails.getCampNo());
        tv_address.setText(campDetails.getCampLocation());
        tv_district.setText(campDetails.getDISTNAME());
        tv_description.setText(campDetails.getDescription());

        ll_checklistlayout.setVisibility(View.GONE);

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
        edt_selectchecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetM_AdvertisementTypeListAPICall().execute();
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        imv_picphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 2);
                    return;
                }

//                File patientImageFile = new File(awarenessImageFolder, "QID_" + quesID + "_awarenessImage.png");
//
//                photoURI = Uri.fromFile(patientImageFile);
//                Intent pickImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                pickImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(pickImage, CAMERA_REQUEST);


                String randomEndtNo = "QID_" + quesID + "_awarenessImage.png";

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
        });

        btn_savechecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_remark.getText().toString().trim().length() <= 0) {
                    edt_remark.setError("Please enter remark.");
                    edt_remark.requestFocus();
                    return;
                }

                if (destinationFilename.equalsIgnoreCase("") || destinationFilename.length() <= 0) {
                    Utilities.showToastMessage("Please capture awareness Image first.", context, false);
                    return;
                }

//                if (Utilities.isNetworkAvailable(context)) {
//                    Utilities.showToastMessage("Data saved successfully", context, true);
//                    ll_checklistlayout.setVisibility(View.GONE);
//                    edt_remark.setText("");
//                    edt_selectchecklist.setText("");
//                    imv_picphoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_addcamera));
////                    new GetM_AdvertisementTypeListAPICall().execute();
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                }

                String campID = campDetails.getCampId();
                InsertAdvertiesmentImages_HandlerAPICall(campID, quesID, userID, "1", userID, destinationFilename);
            }
        });
    }


    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    // Use the cropped image URI.
                    String path = result.getUriFilePath(context, true);
                    savefile(result.getUriContent());

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
//                    CropImage.activity(photoURI).setGuidelines(CropImageView.Guidelines.ON).start(CampAwarenessImageCheckList_Activity.this);


                    CropImageOptions cropImageOptions = new CropImageOptions();
                    cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                    CropImageContractOptions options = new CropImageContractOptions(photoURI, cropImageOptions);
                    cropImageLauncher.launch(options);



                }
            }

//            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == RESULT_OK) {
//                    Uri resultUri = result.getUri();
//                    savefile(resultUri);
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
//                }
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//
//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String sourceFilename = sourceuri.getPath();
//        String filename = "QID_" + quesID + "_" + (int) (Math.random() * 99999 + 1) + "_.png";
////        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/AwarenessImages/" + filename;
//        destinationFilename = getExternalCacheDir()+"/" + filename + ".png";
//
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
//        Bitmap samplePicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        samplePicBm = Bitmap.createScaledBitmap(samplePicBm, 150, 150, false);
//        imv_picphoto.setImageBitmap(samplePicBm);
//    }




//    String destinationFilename = "";

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
        String sourceFilename = sourceuri.getPath();
        String filename = "QID_" + quesID + "_" + (int) (Math.random() * 99999 + 1) + "_.png";
//        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/AwarenessImages/" + filename;
        destinationFilename = getExternalCacheDir()+"/" + filename + ".png";
//
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


                     samplePicBm = BitmapFactory.decodeFile(destinationFilename);
                    Utilities.getBitmapAsByteArray(samplePicBm);
                    destinationFilename = compressImage(destinationFilename);
                    samplePicBm = Bitmap.createScaledBitmap(samplePicBm, 150, 150, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imv_picphoto.setImageBitmap(samplePicBm);
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


//                    letterPhotoPath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }
    private class GetM_AdvertisementTypeListAPICall extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetM_AdvertisementTypeList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    GetM_AdvertisementTypeList_Responce pojoDetails = new Gson().fromJson(result, GetM_AdvertisementTypeList_Responce.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {

                        ArrayList<GetM_AdvertisementTypeList> checkList = new ArrayList<>();
                        checkList = pojoDetails.getOutput();

                        if (checkList.size() > 0) {
                            showDistrictListDialog(checkList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
            }
        }
    }

    private void showDistrictListDialog(final ArrayList<GetM_AdvertisementTypeList> checkList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Question");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < checkList.size(); i++) {
            arrayAdapter.add(String.valueOf(checkList.get(i).getAdvType()));
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                ll_checklistlayout.setVisibility(View.VISIBLE);
                edt_selectchecklist.setText(checkList.get(pos).getAdvType());
                quesID = checkList.get(pos).getAdvTypeId();
                ques = checkList.get(pos).getAdvType();
            }
        });
        builderSingle.show();
    }

    private void InsertAdvertiesmentImages_HandlerAPICall(String CampId, String AdvTypeId,
                                                          String AdvertisementBy, String IsActive,
                                                          String CreatedBy, final String filePath) {
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

        Call<InsertAdvertiesmentImagesResponse> call = apiService.InsertAdvertiesmentImages(CampId, AdvTypeId, AdvertisementBy,
                IsActive, CreatedBy, imageFile);
        call.enqueue(new Callback<InsertAdvertiesmentImagesResponse>() {
            @Override
            public void onResponse(Call<InsertAdvertiesmentImagesResponse> call, Response<InsertAdvertiesmentImagesResponse> response) {
                progressDialog.dismiss();

                int surverCode = response.code();
                String surverMessage = response.message();

                if (surverCode == ApplicationConstants.OKSUCCESS) {
                    try {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if (status.equalsIgnoreCase("Success")) {
//
////                            Bitmap patientPicBm = null;
////                            patientPicBm = BitmapFactory.decodeFile(filePath);
////                            patientPicBm = Bitmap.createScaledBitmap(patientPicBm, 150, 150, false);
////
////                            if (photoType == 100) {
////                                img_sitephoto1.setImageBitmap(patientPicBm);
////                                sitePhotoPath1 = filePath;
////                            } else if (photoType == 101) {
////                                img_sitephoto2.setImageBitmap(patientPicBm);
////                                sitePhotoPath2 = filePath;
////                            } else if (photoType == 200) {
////                                img_docphoto1.setImageBitmap(patientPicBm);
////                                docPhotoPath1 = filePath;
////                            } else if (photoType == 201) {
////                                img_docphoto2.setImageBitmap(patientPicBm);
////                                docPhotoPath2 = filePath;
////                            }
//                            Utilities.showAlertDialog(context, status, message, true,
//                                    "Ok", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            edt_remark.setText("");
//                                            edt_selectchecklist.setText("");
//                                            destinationFilename = "";
//                                            imv_picphoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_addcamera));
//                                            ll_checklistlayout.setVisibility(View.GONE);
//                                        }
//                                    });
                            Utilities.showAlertDialog(context, "Success", "Image uploaded successfully.", true,
                                    "Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            edt_remark.setText("");
                                            edt_selectchecklist.setText("");
                                            destinationFilename = "";
                                            imv_picphoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_addcamera));
                                            ll_checklistlayout.setVisibility(View.GONE);
                                        }
                                    });
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
            public void onFailure(Call<InsertAdvertiesmentImagesResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                Utilities.showAlertDialog(context,
                        "Please try again", t.toString(), false);
            }
        });
    }
}
