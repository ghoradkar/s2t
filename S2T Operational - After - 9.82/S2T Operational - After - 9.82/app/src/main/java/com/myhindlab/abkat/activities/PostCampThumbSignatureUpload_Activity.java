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
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.fingerprint.dao.AuthBfdCap;
import com.myhindlab.abkat.fingerprint.fps.MorphoTabletFPSensorDevice;
import com.myhindlab.abkat.models.GenerateOtpModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PostCampThumbSignatureUpload_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private TextInputEditText edt_mobile_no, edtVerifySuccess;

    private CardView cv_finger_print, cv_signature, cv_uploaded_details;
    private RadioGroup rg_selection;
    private RadioButton rb_thumb, rb_signature;
    private EditText edt_beneficiaryname, edt_gender, edt_age;
    private ImageView iv_biomatric, iv_uploaded_image, imvLetter, imvDeliveryAck;
    private TextView tv_finger_print_not_available, tvHealthCard, tvDeliveryAck;
    private Button btn_clearpad, btn_register, btn_sendOtp, btnSubmitPhoto, btnSubmitDeliveryAck;
    private Uri letterUri, ackUri;
    private final int LETTER_CAMERA_REQUEST = 111;
    private final int ACK_CAMERA_REQUEST = 112;
    private File patientPicsFolder;


    private PostCampBeneficiaryListModel.OutputBean beneficiaryDetails;

    private String userId, RegId, campId, isAdmin, otpnumber,organizationId="0", mobileno;
    private String DESGID, EmpCode, LabCode, CampDATE, DISTLGDCODE, BMobile, district, TALLGDCODE, taluka, STATELGDCODE = "2", selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId, AssignedId = "", IsTeam;

    private File signatureImageFile, patientSignFolder, fingerPrintFolder;

    private String TAG = getClass().getName();
    private String fingerImage = "";
    private boolean isFingerPrint = false;
    private boolean isSignedByUser = false;

    String deviceMan = Build.MANUFACTURER;
    private boolean isCaptureRunning = false;
    private boolean isCaptured = false;
    private String letterImagePath, ackImagePath;
    private Bitmap letterPicBm, ackPicBm;
    private int imageType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_camp_thumb_signature_upload);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void verifyOtp(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_verify_otp, null);
        alertBuilder.setView(alertLayout);
        TextInputEditText edt_Otp = alertLayout.findViewById(R.id.edt_Otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


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


//            if (!edt_Otp.getText().toString().trim().equals(otpnumber)) {
//                edt_Otp.setError("Entered OTP is not matched");
//                return;
//            }

            new VerifyOtp(alertDialog).execute(edt_mobile_no.getText().toString().trim(), edt_Otp.getText().toString().trim());
//            Utilities.showToastMessage("OTP Verified Successfully", context, true);
//            btn_register.setVisibility(View.VISIBLE);


            //
            // alertDialog.dismiss();
        });

        resendOtpBtn.setOnClickListener(view -> {
            //verify api call
            // alertDialog.dismiss();

            new GetOtp(2).execute(edt_mobile_no.getText().toString(), otpnumber, RegId, UserId, "1", session.getSessionMobile(), DISTLGDCODE);
          //  new GetOtp(2).execute(edt_mobile_no.getText().toString(), RegId, UserId, "1");
            edt_Otp.setText("");

        });

    }


    private void init() {
        context = PostCampThumbSignatureUpload_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        tvHealthCard = findViewById(R.id.tvHealthCard);
        cv_finger_print = findViewById(R.id.cv_finger_print);
        cv_signature = findViewById(R.id.cv_signature);
        cv_uploaded_details = findViewById(R.id.cv_uploaded_details);
        rg_selection = findViewById(R.id.rg_selection);
        rb_thumb = findViewById(R.id.rb_thumb);
        rb_signature = findViewById(R.id.rb_signature);
        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        iv_biomatric = findViewById(R.id.iv_biomatric);
        iv_uploaded_image = findViewById(R.id.iv_uploaded_image);
        imvLetter = findViewById(R.id.imvLetter);
        btnSubmitPhoto = findViewById(R.id.btnSubmitPhoto);
        btnSubmitDeliveryAck = findViewById(R.id.btnSubmitDeliveryAck);
        imvDeliveryAck = findViewById(R.id.imvDeliveryAck);
        tvDeliveryAck = findViewById(R.id.tvDeliveryAck);

        tv_finger_print_not_available = findViewById(R.id.tv_finger_print_not_available);
        btn_register = findViewById(R.id.btn_register);
        edt_mobile_no = findViewById(R.id.edt_mobile_no);
        btn_sendOtp = findViewById(R.id.btn_sendOtp);
        edtVerifySuccess = findViewById(R.id.edtVerifySuccess);


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

//        patientSignFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Patient Signature/");
        patientSignFolder = getExternalCacheDir();
//        if (!patientSignFolder.exists())
//            patientSignFolder.mkdirs();

//        fingerPrintFolder = new File(Environment.getExternalStorageDirectory(), "/Health Checkup/ThumbData/");
        fingerPrintFolder = getExternalCacheDir();
//        if (!fingerPrintFolder.exists()) {
//            fingerPrintFolder.mkdirs();
//        }

    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                UserId = json.getString("EmpCode");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                LabCode = json.getString("LabCode");
                BMobile = json.getString("BMobile");
                organizationId = json.getString("SubOrgId");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setDefaults() {
        isAdmin = getIntent().getStringExtra("isAdmin");
        beneficiaryDetails = (PostCampBeneficiaryListModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");
        RegId = beneficiaryDetails.getRegdId();
        mobileno = beneficiaryDetails.getMobileNo();
        campId = beneficiaryDetails.getCAMPID();
        if (beneficiaryDetails.getOTPStatus().equals("Success")) {
            btn_sendOtp.setVisibility(View.GONE);
            btnSubmitPhoto.setVisibility(View.GONE);
            btnSubmitDeliveryAck.setVisibility(View.GONE);
            tvHealthCard.setVisibility(View.GONE);
            tvDeliveryAck.setVisibility(View.GONE);
            imvLetter.setEnabled(false);
            imvDeliveryAck.setEnabled(false);
            edtVerifySuccess.setVisibility(View.VISIBLE);
            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/ReportDelivery/" + beneficiaryDetails.getPhotoPath();
                Picasso.with(context).load(url).into(imvLetter);

//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });


            } else {
                String url = BuildConfig.domain + "/ReportDelivery/" + beneficiaryDetails.getPhotoPath();
                Picasso.with(context).load(url).into(imvLetter);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });


            }

            if (BuildConfig.isBeta) {
                String url = BuildConfig.domain + "/ReportDeliveryAcknowledgement/" + beneficiaryDetails.getAckReceiptPath();
                Picasso.with(context).load(url).into(imvDeliveryAck);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });


            } else {
                String url = BuildConfig.domain + "/ReportDeliveryAcknowledgement/" + beneficiaryDetails.getAckReceiptPath();
                Picasso.with(context).load(url).into(imvDeliveryAck);
//                imvLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//                    }
//                });


            }


        } else {
            btn_sendOtp.setVisibility(View.VISIBLE);
            // btnSubmitPhoto.setVisibility(View.VISIBLE);
            tvHealthCard.setVisibility(View.VISIBLE);
            edtVerifySuccess.setVisibility(View.GONE);
        }


        edt_beneficiaryname.setText(beneficiaryDetails.getBeneficiaryName());
        edt_gender.setText(beneficiaryDetails.getGender());
        edt_age.setText(beneficiaryDetails.getAge());
        edt_mobile_no.setText(beneficiaryDetails.getMobileNo());
//        edt_mobile_no.setEnabled(false);


        if (beneficiaryDetails.getPostCampAcknowlegedmentFile() != null && !beneficiaryDetails.getPostCampAcknowlegedmentFile().equals("NA")) {
            String url = BuildConfig.domain + "/CampDocs/PostCampAcknowlegedmentImage/" + beneficiaryDetails.getPostCampAcknowlegedmentFile();
            Picasso.with(context)
                    .load(url)
                    .into(iv_uploaded_image);

            rb_thumb.setChecked(true);
            rg_selection.setVisibility(View.VISIBLE);
            cv_finger_print.setVisibility(View.VISIBLE);
            cv_signature.setVisibility(View.GONE);
            cv_uploaded_details.setVisibility(View.GONE);
            btn_register.setVisibility(View.GONE);
//            btn_sendOtp.setVisibility(View.VISIBLE);
            edt_mobile_no.setVisibility(View.VISIBLE);
            tv_finger_print_not_available.setVisibility(View.GONE);

        } else {
            if (isAdmin.equals("1")) {
                rg_selection.setVisibility(View.GONE);
                cv_finger_print.setVisibility(View.GONE);
                cv_signature.setVisibility(View.GONE);
                cv_uploaded_details.setVisibility(View.GONE);
                btn_register.setVisibility(View.GONE);
                tv_finger_print_not_available.setVisibility(View.VISIBLE);
            } else {

                rg_selection.setOnCheckedChangeListener((group, checkedId) -> {
                    switch (checkedId) {
                        case R.id.rb_thumb:
                            cv_finger_print.setVisibility(View.VISIBLE);
                            cv_signature.setVisibility(View.GONE);
                            edt_mobile_no.setVisibility(View.VISIBLE);
                            // btn_sendOtp.setVisibility(View.VISIBLE);
                            btn_register.setVisibility(View.GONE);

                            break;

                        case R.id.rb_signature:
                            cv_finger_print.setVisibility(View.GONE);
                            cv_signature.setVisibility(View.VISIBLE);
                            edt_mobile_no.setVisibility(View.GONE);
                            //  btn_sendOtp.setVisibility(View.GONE);
                            btn_register.setVisibility(View.VISIBLE);
                            break;
                    }
                });

                rg_selection.check(R.id.rb_thumb);
            }
        }

    }


    private void setEventHandler() {
        btnSubmitPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (letterImagePath == null) {
                    Utilities.showToastMessage("Please capture image", context, false);
                    return;
                }
                new UploadPatientDetails().execute(letterImagePath, beneficiaryDetails.getRegdId(), beneficiaryDetails.getMobileNo(), DISTLGDCODE);
            }
        });
        btnSubmitDeliveryAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (letterImagePath == null) {
                    Utilities.showToastMessage("Please capture beneficiary photo", context, false);
                    return;
                }

                if (ackImagePath == null) {
                    Utilities.showToastMessage("Please capture Acknowledgement photo", context, false);
                    return;
                }
                new ReportDeliveryAck().execute(beneficiaryDetails.getRegdId(), letterImagePath, ackImagePath, beneficiaryDetails.getMobileNo(), DISTLGDCODE);
            }
        });

        imvLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                int randomEndtNo = (int) (Math.random() * 99999 + 1);
                if (SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_PR.png");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    letterUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_PR.png");
                    letterUri = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, letterUri);
                    startActivityForResult(intent, LETTER_CAMERA_REQUEST);
                }


            }
        });
        imvDeliveryAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageType = 1;
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
                int randomEndtNo = (int) (Math.random() * 99999 + 1);
                if (SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo + "_ACK.png");
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                    ackUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, ackUri);
                    startActivityForResult(intent, ACK_CAMERA_REQUEST);
                } else {
                    File patientImageFile = new File(patientPicsFolder, randomEndtNo + "_ACK.png");
                    ackUri = Uri.fromFile(patientImageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, ackUri);
                    startActivityForResult(intent, ACK_CAMERA_REQUEST);
                }


            }
        });


        btn_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //api call api success
                if (edt_mobile_no.getText().toString().trim().matches("")) {
                    edt_mobile_no.setError("Please enter mobile number");
                    return;
                }
                if (edt_mobile_no.getText().toString().trim().length() != 10) {
                    edt_mobile_no.setError("Please enter valid 10 digit mobile number");
                    return;
                }
                Utilities.showToastMessage("OTP Send Successfully", context, true);
//                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdId()));
//
//                finish();
//                new GenerateOtp().execute();
//              Live  new GetOtp(1).execute(edt_mobile_no.getText().toString(), otpnumber, RegId, userId, "1", session.getSessionMobile(), session.getSessionDistrict());
                new GetOtp(1).execute(edt_mobile_no.getText().toString(), otpnumber, RegId, UserId, "1", session.getSessionMobile(), DISTLGDCODE);
               // new GetOtp(2).execute(edt_mobile_no.getText().toString(), RegId, UserId, "1");


            }
        });


    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Report Delivery Acknowledgement");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


////////////////////////////////////////////Signature Pad///////////////////////////////////////////////////////////////

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        signatureImageFile = new File(patientSignFolder,
                RegId + "_0_" + campId + "_SG.png");

        try {
            saveBitmapToJPG(signature, signatureImageFile);
            scanMediaFile(signatureImageFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        stream.close();
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

//////////////////////////////////////////MORPH FINGER PRINT////////////////////////////////////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (fpSensorCap != null) {
//            fpSensorCap.cancelLiveAcquisition();
//            fpSensorCap.release();
//        }
//        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (tost != null)
//            tost.cancel();
    }


    private boolean isWorking = false;

    public void setButtonEnabled(boolean enabled) {
        isWorking = !enabled;
        iv_biomatric.setEnabled(enabled);
    }


    private boolean storeImage(String fileName, Bitmap image) {
        File file = new File(fingerPrintFolder, fileName);
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
        File file = new File(fingerPrintFolder, fileName);
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

//////////////////////////////////////////MANTRA FINGETPRINT////////////////////////////////////////////////////////////

    private long mLastAttTime = 0l;


    long mLastDttTime = 0l;


    private void WriteFileImage(String fingerImage, Bitmap bitmap) {
        File file = new File(fingerPrintFolder, fingerImage);
        try {
            isCaptured = true;
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            isCaptured = false;
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            isCaptured = true;
        }
    }

    private void WriteFileRaw(String filename, byte[] rawData) {
        File file = new File(fingerPrintFolder, filename);
        isCaptured = true;
        // String imgString = Base64.encodeToString(buffer, Base64.NO_WRAP);
        // Save your stream, don't forget to flush() it before closing it.
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(rawData);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
            isCaptured = false;
            Utilities.showToastMessage("File write failed: " + e.toString(), context, true);
        }
    }

    private void SetTextOnUIThread(String capture_success) {
//        Toast.makeText(context, capture_success, Toast.LENGTH_SHORT).show();
    }

    public class GetOtp extends AsyncTask<String, Void, String> {
        //ProgressDialog pd;

        int type;

        public GetOtp(int type) {
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
            param.add(new ParamsPojo("MOBNO", params[0]));
            Random r = new Random(System.currentTimeMillis());
            otpnumber = String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
            param.add(new ParamsPojo("OTP", otpnumber));
            param.add(new ParamsPojo("RegdId", params[2]));
            param.add(new ParamsPojo("CreatedBy", params[3]));
            param.add(new ParamsPojo("ackType", params[4]));
            param.add(new ParamsPojo("ReportDeliveredBY", params[5]));
            param.add(new ParamsPojo("LGDCode", params[6]));
            param.add(new ParamsPojo("SubOrgID", organizationId));

            Log.d(TAG, "doInBackground: " + new Gson().toJson(param));
          //  res = WebServiceCall.APICall(ApplicationConstants.GenerateAndSendOTP, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetOTPWithLGDCode_Org, ApplicationConstants.webservice_d2d, param);
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
//                        ArrayList<ResourcesListModel.OutputBean> resourceList = resourcesListModel.getOutput();
//                        if (resourceList.size() > 0) {
                        if (type == 1) {
//                                // verifyOtp(mno, resourceList.get(0).getDesgId());
                            verifyOtp(edt_mobile_no.getText().toString(), otpnumber);

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
        // String otp;

        public VerifyOtp(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
            //  this.otp = otp;
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
            param.add(new ParamsPojo("MOBNO", params[0]));
            param.add(new ParamsPojo("OTP", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.VerifyOTP, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
//                    type = resourcesListModel.getStatus();
//                    message = resourcesListModel.getMessage();
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdNo()).putExtra("campId",beneficiaryDetails.getCAMPID()));


                        alertDialog.dismiss();

                        //  btn_register.setVisibility(View.VISIBLE);

                        {
                            // Utilities.showAlertDialog(context, "Alert", message, true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.icon_success);
                            builder.setTitle("Success");
                            builder.setCancelable(false);
                            builder.setMessage("OTP Verification successfully");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();

                                }
                            });
                            builder.show();
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

    public class GenerateOtp extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GenerateOTP, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "", output = "";
            try {
                if (!result.equals("")) {

                    GenerateOtpModel generateOtpModel = new Gson().fromJson(result, GenerateOtpModel.class);
                    type = generateOtpModel.getStatus();
                    message = generateOtpModel.getMessage();
                    otpnumber = generateOtpModel.getOutput();
                    if (type.equalsIgnoreCase("success")) {

                        new GetOtp(1).execute(edt_mobile_no.getText().toString().trim(), otpnumber, RegId, UserId, "1", session.getSessionMobile(), DISTLGDCODE);
                        ;


                        //  otpnumber = generateOtpModel.getOutput();
                        // ArrayList<GenerateOtpModel> resourceList = generateOtpModel.getOutput();

                        {
                            //  Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        //  Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    // Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private class InsertBeneficiary extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", params[0]));
            param.add(new ParamsPojo("ackType", params[1]));
            param.add(new ParamsPojo("CreatedBy", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.Insert_PostCampAcknowledgement, ApplicationConstants.webservice_d2d, param);
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


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Acknowledgement done successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                                //  ll_assigned.setVisibility(View.VISIBLE);


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


    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    // Use the cropped image URI.
                    String path = result.getUriFilePath(context, true);

//                    savefile(result.getUriContent());

                    if (imageType == 0) {
                        savefile(result.getUriContent());
                    } else if (imageType == 1) {
                        saveAckfile(result.getUriContent());
                    }

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
//                CropImage.activity(letterUri).setGuidelines(CropImageView.Guidelines.ON).start(PostCampThumbSignatureUpload_Activity.this);

                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                CropImageContractOptions options = new CropImageContractOptions(letterUri, cropImageOptions);
                cropImageLauncher.launch(options);

            }
            if (requestCode == ACK_CAMERA_REQUEST) {
//                CropImage.activity(ackUri).setGuidelines(CropImageView.Guidelines.ON).start(PostCampThumbSignatureUpload_Activity.this);

                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG;

                CropImageContractOptions options = new CropImageContractOptions(ackUri, cropImageOptions);
                cropImageLauncher.launch(options);

            }


        }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                if (imageType == 0) {
//                    savefile(resultUri);
//                } else if (imageType == 1) {
//                    saveAckfile(resultUri);
//                }
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
    }



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

                String filename = (int) (Math.random() * 99999 + 1) + "_PO.png";
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
                            imvLetter.setImageBitmap(letterPicBm);
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


                    letterImagePath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }
//
//    public void savefile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        String filename = (int) (Math.random() * 99999 + 1) + "_PO.png";
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
//        imvLetter.setImageBitmap(letterPicBm);
//        letterImagePath = destinationFilename;
//
//
//    }




    private void saveAckfile(Uri sourceuri) {
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
                String filename = (int) (Math.random() * 99999 + 1) + "_PT.png";
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

                    ackPicBm = BitmapFactory.decodeFile(destinationFilename);
                    Utilities.getBitmapAsByteArray(ackPicBm);
                    destinationFilename = compressImage(destinationFilename);
                    ackPicBm = Bitmap.createScaledBitmap(ackPicBm, 150, 150, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imvDeliveryAck.setImageBitmap(ackPicBm);
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


                    ackImagePath = destinationFilename;


                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        };

        new Thread(runnable).start();

    }
//    public void saveAckfile(Uri sourceuri) {
//        Log.i("sourceuri1", "" + sourceuri);
//        String destinationFilename = "";
//        String filename = (int) (Math.random() * 99999 + 1) + "_PT.png";
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
//        ackPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        ackPicBm = Bitmap.createScaledBitmap(ackPicBm, 150, 150, false);
//        imvDeliveryAck.setImageBitmap(ackPicBm);
//        ackImagePath = destinationFilename;
//
//
//    }

    private class UploadPatientDetails extends AsyncTask<String, Integer, String> {

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
            Log.d("Reg Params", Arrays.toString(params));
            try {
                MultipartUtility multipart;
                multipart = new MultipartUtility(ApplicationConstants.CW_ReportDelivery_V1, "UTF-8");

                if (!params[0].isEmpty())
                    multipart.addFilePart("PhotoPath", new File(params[0]));

                multipart.addFormField("UserId", UserId);
                multipart.addFormField("RegdID", params[1]);
                multipart.addFormField("MOBNO", params[2]);
                multipart.addFormField("ReportDeliveredBY", BMobile == null ? userId : BMobile);
                multipart.addFormField("LGDCode", params[3]);

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

            Log.d("Reg Res ", result);
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdNo()));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));

                        Utilities.showAlertDialog(context, "Success", "Photo uploaded successfully", true, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
                                dialogInterface.dismiss();

                            }
                        });

                    } else {

                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class ReportDeliveryAck extends AsyncTask<String, Integer, String> {

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
            Log.d("Reg Params", Arrays.toString(params));
            try {
                MultipartUtility multipart;
                multipart = new MultipartUtility(ApplicationConstants.CW_ReportDeliveryAckNew, "UTF-8");

                multipart.addFormField("RegdID", params[0]);
                multipart.addFormField("UserId", UserId);
                if (!params[1].isEmpty())
                    multipart.addFilePart("PhotoPath", new File(params[1]));
                if (!params[2].isEmpty())
                    multipart.addFilePart("PhotoPathAck", new File(params[2]));
                multipart.addFormField("MOBNO", params[3]);
                multipart.addFormField("ReportDeliveredBY", BMobile == null ? UserId : BMobile);
                multipart.addFormField("LGDCode", params[4]);


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

            Log.d("Reg Res ", result);
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj1 = new JSONObject(result);
                    String status = obj1.getString("status");
                    String message = obj1.getString("message");

                    if (status.equalsIgnoreCase("Success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("ReportDeliveryActivity").putExtra("regId", beneficiaryDetails.getRegdNo()).putExtra("campId", beneficiaryDetails.getCAMPID()));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("PostCampPatientList_Activity"));

                        Utilities.showAlertDialog(context, "Success", "Photo uploaded successfully", true, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();

                            }
                        });

                    } else {

                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}