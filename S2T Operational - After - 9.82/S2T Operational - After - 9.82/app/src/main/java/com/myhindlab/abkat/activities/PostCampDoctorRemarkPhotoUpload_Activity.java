package com.myhindlab.abkat.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import static com.myhindlab.abkat.utilities.Utilities.compressImage;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DoctorRemarkModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
import com.myhindlab.abkat.models.PostCampDoctorRemarkListModel;
import com.myhindlab.abkat.models.ReferToModel;
import com.myhindlab.abkat.models.ResourcesListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
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

public class PostCampDoctorRemarkPhotoUpload_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private int referToId = 0;

    private EditText edt_beneficiaryname, edt_gender, edt_age, edt_doctor,edt_remarkRecommendation, edt_remark,edtReferTo, edt_mobile_no;
    private ImageView imv_photo;
    private TextView tv_not_uploaded_note;
    private Button btn_register, btn_sendOtp;

    private String userId,callType, districtId;

    private Bitmap letterPicBm = null;
    private final int LETTER_CAMERA_REQUEST = 100;
    private File letterPicsFolder;
    private Uri letterURI;
    private String letterPhotoPath;

    private PostCampBeneficiaryListModel.OutputBean beneficiaryDetails;

    private String isAdmin, url, doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_camp_doctor_remark_photo_upload);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();


        //  getOtp("88664774743");
    }

    private void getOtp(String mno) {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_get_otp, null);
        alertBuilder.setView(alertLayout);
        MaterialEditText edt_mobile_no = alertLayout.findViewById(R.id.edt_mobile_no);
        Button btn_sendOtp = alertLayout.findViewById(R.id.btn_sendOtp);
        edt_mobile_no.setText(mno);


        btn_sendOtp.setOnClickListener(view -> {
            //api call api success
            // verifyOtp();

        });
    }

    private void verifyOtp(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_verify_otp, null);
        alertBuilder.setView(alertLayout);
        MaterialEditText edt_Otp = alertLayout.findViewById(R.id.edt_Otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        verifyOtpBtn.setOnClickListener(view -> {
            //verify api call

            if (edt_Otp.getText().toString().trim().matches("")) {
                edt_Otp.setError("Please enter otp");
                return;
            }
//            if (edt_Otp.getText().toString().trim().length() != 10) {
//                edt_Otp.setError("Please enter valid otp");
//                return;
//            }

            if (!edt_Otp.getText().toString().trim().equals(otp)) {
                edt_Otp.setError("Entered OTP is not matched");
                return;
            }

            new VerifyOtp(alertDialog, edt_Otp.getText().toString().trim()).execute();

            //  alertDialog.dismiss();
        });

        resendOtpBtn.setOnClickListener(view -> {
            //verify api call
            // alertDialog.dismiss();

            new GetOtp(mno, 2).execute();

        });

    }

    private void init() {
        context = PostCampDoctorRemarkPhotoUpload_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        tv_not_uploaded_note = findViewById(R.id.tv_not_uploaded_note);
        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_mobile_no = findViewById(R.id.edt_mobile_no);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_remark = findViewById(R.id.edt_remark);
        edt_doctor = findViewById(R.id.edt_doctor);
        imv_photo = findViewById(R.id.imv_photo);
        btn_register = findViewById(R.id.btn_register);
        btn_sendOtp = findViewById(R.id.btn_sendOtp);
        edtReferTo = findViewById(R.id.edtReferTo);
        edt_remarkRecommendation = findViewById(R.id.edt_remarkRecommendation);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

//        letterPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Doctor Remark");
        letterPicsFolder = getExternalCacheDir();
//        if (!letterPicsFolder.exists())
//            letterPicsFolder.mkdirs();


    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                districtId = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        isAdmin = getIntent().getStringExtra("isAdmin");
        callType = getIntent().getStringExtra("callType");
        beneficiaryDetails = (PostCampBeneficiaryListModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");

        edt_beneficiaryname.setText(beneficiaryDetails.getBeneficiaryName());
        edt_gender.setText(beneficiaryDetails.getGender());
        edt_age.setText(beneficiaryDetails.getAge());

//        if (callType.equalsIgnoreCase("2")){
//            edtReferTo.setText("Not Applicable");
//            referToId = 1;
//        }else if (callType.equalsIgnoreCase("1")){
//
//            edtReferTo.setText("");


//
//        }


        if (!beneficiaryDetails.getPostFilePath().equals("NA")) {
            url = BuildConfig.domain + "/CampDocs/DoctorRemarkImage/" + beneficiaryDetails.getPostFilePath();
            Picasso.with(context)
                    .load(url)
                    .into(imv_photo);

            edt_remark.setText(beneficiaryDetails.getPostcampDocRemark());
            edt_doctor.setText(beneficiaryDetails.getDoctorName());
            edt_remarkRecommendation.setText(beneficiaryDetails.getReferTo());
            edt_doctor.setClickable(false);
            edt_remark.setFocusable(false);
            edt_remark.setClickable(false);
//            edtReferTo.setClickable(false);
            btn_register.setVisibility(View.GONE);
            imv_photo.setOnClickListener(viewPhotoClickListener);


        } else {
            if (isAdmin.equals("1")) {
                imv_photo.setVisibility(View.GONE);
                edt_doctor.setVisibility(View.GONE);
                edt_remark.setVisibility(View.GONE);
                tv_not_uploaded_note.setVisibility(View.VISIBLE);
            } else {
                imv_photo.setVisibility(View.VISIBLE);
                edt_doctor.setVisibility(View.VISIBLE);
                edt_remark.setVisibility(View.VISIBLE);
                btn_register.setVisibility(View.VISIBLE);
                imv_photo.setOnClickListener(openPhotoClickListener);

                edt_doctor.setOnClickListener(v -> {
                    if (Utilities.isNetworkAvailable(context))
                        new GetDoctorList().execute(districtId);
                    else
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                });
            }
        }

        if (isAdmin.equals("1")) {
            btn_register.setVisibility(View.GONE);
        }

        new GetRemark().execute();

    }

    private void setEventHandler() {
        btn_register.setOnClickListener(v -> {
            if (edt_doctor.getText().toString().trim().equals("")) {
                edt_doctor.setError("Please select doctor");
                return;
            }

            if (edt_remark.getText().toString().trim().equals("")) {
                edt_remark.setError("Please enter remark");
                return;
            }

            if (edt_remarkRecommendation.getText().toString().trim().equals("")) {
                edt_remarkRecommendation.setError("Please enter doctor recommendation");
                return;
            }

//            if (callType.equalsIgnoreCase("1")){
//                if (edtReferTo.getText().toString().trim().equals("")) {
//                    edtReferTo.setError("Please select refer to");
//                    return;
//                }
//
//            }

            if (letterPicBm == null) {
                Utilities.showMessageString("Please click letter photo", context);
                return;
            }

            if (Utilities.isNetworkAvailable(context))
                new UploadDoctorRemark().execute(
                        beneficiaryDetails.getRegdId(),
                        beneficiaryDetails.getCAMPID(),
                        edt_remark.getText().toString().trim(),
                        "",
                        doctorId,
                        userId,
                        letterPhotoPath,
                        edt_remarkRecommendation.getText().toString().trim()
                );
            else
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);

        });

        edtReferTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetReferList().execute();

//
//                ArrayList<String> arrayList = new ArrayList<>();
//                arrayList.add("Not Applicable");
//                arrayList.add("Government Hospital");
//                arrayList.add("Arogyashree");
//                arrayList.add("Ayushman Bharat");
//
//                ArrayAdapter arrayAdapter = new ArrayAdapter(PostCampDoctorRemarkPhotoUpload_Activity.this, R.layout.list_row);
//                arrayAdapter.addAll(arrayList);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(PostCampDoctorRemarkPhotoUpload_Activity.this);
//                builder.setCancelable(false)
//                        .setTitle("Select Refer To");

//                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        referToId = i;
//                        edtReferTo.setText(arrayAdapter.getItem(i).toString());
//                    }
//                });


//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });

             //   builder.show();

            }
        });

        btn_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //api call api success\

                if (edt_mobile_no.getText().toString().trim().matches("")) {
                    edt_mobile_no.setError("Please enter mobile number");
                    return;
                }
                if (edt_mobile_no.getText().toString().trim().length() != 10) {
                    edt_mobile_no.setError("Please enter valid 10 digit mobile number");
                    return;
                }
                new GetOtp(edt_mobile_no.getText().toString().trim(), 1).execute();
            }
        });

    }

    private View.OnClickListener openPhotoClickListener = v -> {
        if (SDK_INT < Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
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
//        letterPicsFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Doctor Remark");
//        if (!letterPicsFolder.exists())
//            letterPicsFolder.mkdirs();
//
//        int randomEndtNo = (int) (Math.random() * 99999 + 1);
//        File patientImageFile = new File(letterPicsFolder, randomEndtNo + ".png");
//        letterURI = Uri.fromFile(patientImageFile);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, letterURI);
//        startActivityForResult(intent, LETTER_CAMERA_REQUEST);

        letterPicsFolder = getExternalCacheDir();
        if (!letterPicsFolder.exists())
            letterPicsFolder.mkdirs();

        int randomEndtNo = (int) (Math.random() * 99999 + 1);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            File patientImageFile = new File(letterPicsFolder, randomEndtNo + ".png");
            letterURI = Uri.fromFile(patientImageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, letterURI);
            startActivityForResult(intent, LETTER_CAMERA_REQUEST);
        } else {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + ".png");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            letterURI = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, letterURI);
            startActivityForResult(intent, LETTER_CAMERA_REQUEST);
        }
    };

    private View.OnClickListener viewPhotoClickListener = v -> startActivity(new Intent(context, ZoomImageView_Activity.class)
            .putExtra("url", url));

    private class GetDoctorList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", districtId));
            res = WebServiceCall.APICall(ApplicationConstants.GetDoctorList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<DoctorRemarkModel.Output> doctorList = new ArrayList<>();
                    DoctorRemarkModel pojoDetails = new Gson().fromJson(result, DoctorRemarkModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        doctorList = pojoDetails.getOutput();
                        if (doctorList.size() > 0) {
                            showDoctorListDialog(doctorList);
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
    private class GetReferList extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetReferToList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<ReferToModel.Output> referList = new ArrayList<>();
                    ReferToModel pojoDetails = new Gson().fromJson(result, ReferToModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        referList = pojoDetails.getOutput();
                        if (referList.size() > 0) {


                            showReferListDialog(referList);

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

    private void showDoctorListDialog(final List<DoctorRemarkModel.Output> doctorList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Doctor");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < doctorList.size(); i++) {
            arrayAdapter.add(String.valueOf(doctorList.get(i).getName()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            doctorId = String.valueOf((doctorList.get(which).getUserid()));
            edt_doctor.setText(doctorList.get(which).getName());
        });
        builderSingle.show();
    }
    private void showReferListDialog(final List<ReferToModel.Output> doctorList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select ReferTo");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < doctorList.size(); i++) {
            arrayAdapter.add(String.valueOf(doctorList.get(i).getReferTo()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            referToId = Integer.parseInt(String.valueOf((doctorList.get(which).getId())));
            edtReferTo.setText(doctorList.get(which).getReferTo());
        });
        builderSingle.show();
    }



    private class GetRemark extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("REGDID", beneficiaryDetails.getRegdId()));

            res = WebServiceCall.APICall(ApplicationConstants.DoctorsScreeningRemark, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<PostCampDoctorRemarkListModel.Output> referList = new ArrayList<>();
                    PostCampDoctorRemarkListModel pojoDetails = new Gson().fromJson(result, PostCampDoctorRemarkListModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        referList = pojoDetails.getOutput();
                        if (referList.size() > 0) {

                            if (pojoDetails.getOutput().size() > 0){
                                PostCampDoctorRemarkListModel.Output output = pojoDetails.getOutput().get(0);
                                edt_remark.setText(output.getFinalRemark());
                                if (beneficiaryDetails.getPostFilePath().equals("NA")) {
                                    edt_remarkRecommendation.setText(output.getDocReccomandation());

                                }
                             }


                           // showReferListDialog(referList);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == LETTER_CAMERA_REQUEST) {
//                CropImage.activity(letterURI).setGuidelines(CropImageView.Guidelines.ON).start(PostCampDoctorRemarkPhotoUpload_Activity.this);


                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                CropImageContractOptions options = new CropImageContractOptions(letterURI, cropImageOptions);
                cropImageLauncher.launch(options);

            }
        }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                savefile(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
    }

//    private void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        String filename = String.valueOf((int) (Math.random() * 99999 + 1));
////        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Doctor Remark/" + filename + ".png";
//        destinationFilename = getExternalCacheDir() + "/" + filename + ".png";
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
//        destinationFilename = Utilities.compressImage(destinationFilename);
//        letterPhotoPath = destinationFilename;
//
//        letterPicBm = BitmapFactory.decodeFile(destinationFilename);
//        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
//        imv_photo.setImageBitmap(letterPicBm);
//    }



    String destinationFilename = "";

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
                String filename = String.valueOf((int) (Math.random() * 99999 + 1));
//        destinationFilename = Environment.getExternalStorageDirectory() + "/Health Checkup/" + "Doctor Remark/" + filename + ".png";
                destinationFilename = getExternalCacheDir() + "/" + filename + ".png";



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
                            imv_photo.setImageBitmap(letterPicBm);
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


                    letterPhotoPath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }
    private class UploadDoctorRemark extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please Wait . . .");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            try {
                MultipartUtility multipart = new MultipartUtility(ApplicationConstants.CW_UploadDoctorDetailsNewV1, "UTF-8");

                multipart.addFormField("RegdId", params[0]);
                multipart.addFormField("CampId", params[1]);
                multipart.addFormField("PostcampDocRemark", params[2]);
                multipart.addFormField("Remark", params[3]);
                multipart.addFormField("DoctorId", params[4]);
                multipart.addFormField("CreatedBy", params[5]);
                multipart.addFilePart("FileName", new File(params[6]));
                multipart.addFormField("ReferTo", params[7]);


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
            pd.dismiss();
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));
                    if (status.equalsIgnoreCase("Success")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setCancelable(false);
                        builder.setTitle("Success");
                        builder.setMessage(message);
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            finish();
                        });

                        builder.show();
                    } else {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetOtp extends AsyncTask<String, Void, String> {

        String mno;
        int type;

        public GetOtp(String mno, int type) {
            this.mno = mno;
            this.type = type;
        }

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
            res = WebServiceCall.APICall(ApplicationConstants.GetDesignationsForCampCreation, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
                    status = resourcesListModel.getStatus();
                    message = resourcesListModel.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        ArrayList<ResourcesListModel.OutputBean> resourceList = resourcesListModel.getOutput();
                        if (resourceList.size() > 0) {
                            if (type == 1)
                                verifyOtp(mno, resourceList.get(0).getDesgId());
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
    }

    public class VerifyOtp extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        String otp;

        public VerifyOtp(AlertDialog alertDialog, String otp) {
            this.alertDialog = alertDialog;
            this.otp = otp;
        }

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
            res = WebServiceCall.APICall(ApplicationConstants.GetDesignationsForCampCreation, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
                    type = resourcesListModel.getStatus();
                    message = resourcesListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        ArrayList<ResourcesListModel.OutputBean> resourceList = resourcesListModel.getOutput();
                        if (resourceList.size() > 0) {
                            alertDialog.dismiss();

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
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Doctor Remark Photo");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}