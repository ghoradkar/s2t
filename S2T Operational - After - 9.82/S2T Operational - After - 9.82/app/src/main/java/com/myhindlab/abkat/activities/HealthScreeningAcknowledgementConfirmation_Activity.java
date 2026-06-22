package com.myhindlab.abkat.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.calling_dashboard.adapter.RationCardPhotoAdapter;
import com.myhindlab.abkat.camera.CameraActivity;
import com.myhindlab.abkat.camera.utils.CaptureMode;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.models.RationCardPhotoModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.MultipartUtility;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HealthScreeningAcknowledgementConfirmation_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;

    private ProgressDialog pd;

    private CircleImageView imv_patient;
    private ImageView imv_self_declaration;

    private Bitmap patientPicBm = null, rationCardPicBm = null;

    private ImageView imv_health_card, imv_renewal_form, imv_thumb_print;
    private TextView tv_audiomerty_file;
    private LinearLayout ll_renewal_photo;
    private RadioGroup radioGroupArea;

    private String rationCardImagePath = "",EmpCode,fingerPrintPath = "";



    private int uploadedCount = 0;
    private int totalUploadCount = 0;

    private RadioButton radioManual, radioDigital;

    private Button btn_patient, btn_health_card, btn_renewal_slip, btn_audio_metry, btn_thumb, btn_next,btn_UploadRationCard;
    private CheckBox cb_no_thumb_machine;

    private PresentPatientList_Model patientDetails;

    private RecyclerView recyclerPhotos;

    private ArrayList<RationCardPhotoModel> rationCardPhotoList =
            new ArrayList<>();

    private RationCardPhotoAdapter adapter;

    private File audioMetryFile, audioMetryfolder;
    private boolean isPatientPhoto, isHealthCardPhoto, isRenewalSlipPhoto, isAudiometryFile, isThumbPhoto;

    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_screening_acknowledgement_confirmation);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = HealthScreeningAcknowledgementConfirmation_Activity.this;
        session = new UserSessionManager(context);

        pd = new ProgressDialog(context);

        imv_patient = findViewById(R.id.imv_patient);
        imv_health_card = findViewById(R.id.imv_health_card);
        imv_renewal_form = findViewById(R.id.imv_renewal_form);
        imv_thumb_print = findViewById(R.id.imv_thumb_print);
        tv_audiomerty_file = findViewById(R.id.tv_audiomerty_file);
        cb_no_thumb_machine = findViewById(R.id.cb_no_thumb_machine);
        ll_renewal_photo = findViewById(R.id.ll_renewal_photo);
        btn_patient = findViewById(R.id.btn_patient);
        btn_health_card = findViewById(R.id.btn_health_card);
        btn_renewal_slip = findViewById(R.id.btn_renewal_slip);
        btn_audio_metry = findViewById(R.id.btn_audio_metry);
        btn_thumb = findViewById(R.id.btn_thumb);
        btn_next = findViewById(R.id.btn_next);
        imv_self_declaration = findViewById(R.id.imv_self_declaration);

        radioGroupArea = findViewById(R.id.radioGroupArea);
        radioManual = findViewById(R.id.radioManual);
        radioDigital = findViewById(R.id.radioDigital);


        btn_UploadRationCard = findViewById(R.id.btn_UploadRationCard);
        recyclerPhotos = findViewById(R.id.recyclerPhotos);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            audioMetryfolder = getExternalCacheDir();
        } else {
            audioMetryfolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/AudiometerImage");
            if (!audioMetryfolder.exists()) {
                audioMetryfolder.mkdirs();
            }
        }


        adapter = new RationCardPhotoAdapter(
                this,
                rationCardPhotoList
        );

        recyclerPhotos.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerPhotos.setAdapter(adapter);


        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("HealthScreeningAcknowledgementConfirmation_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
    }



    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                EmpCode = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        Log.d("patintDetails", patientDetails.getPatientPhoto());
        if (!patientDetails.getPatientPhoto().trim().isEmpty()) {
            Picasso.with(context)
                    .load(patientDetails.getPatientPhoto())
                    .placeholder(R.drawable.icon_patientcamera)
                    .into(imv_patient, new Callback() {
                        @Override
                        public void onSuccess() {
                            isPatientPhoto = true;
                            btn_patient.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            isPatientPhoto = false;
                            btn_patient.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            isPatientPhoto = false;
            btn_patient.setVisibility(View.VISIBLE);
        }

        if (!patientDetails.getHealthCardPath().trim().isEmpty()) {
            Picasso.with(context)
                    .load(patientDetails.getHealthCardPath())
                    .placeholder(R.drawable.icon_colorcamera)
                    .into(imv_health_card, new Callback() {
                        @Override
                        public void onSuccess() {
                            isHealthCardPhoto = true;
                            btn_health_card.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            isHealthCardPhoto = false;
                            btn_health_card.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            isHealthCardPhoto = false;
            btn_health_card.setVisibility(View.VISIBLE);
        }

        if (patientDetails.getIsHCRenewal().equalsIgnoreCase("yes")) {
            ll_renewal_photo.setVisibility(View.VISIBLE);

            if (!patientDetails.getHCRenewalFilePath().trim().isEmpty()) {
                Picasso.with(context)
                        .load(patientDetails.getHCRenewalFilePath())
                        .placeholder(R.drawable.icon_colorcamera)
                        .into(imv_renewal_form, new Callback() {
                            @Override
                            public void onSuccess() {
                                isRenewalSlipPhoto = true;
                                btn_renewal_slip.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                isRenewalSlipPhoto = false;
                                btn_renewal_slip.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                isRenewalSlipPhoto = false;
                btn_renewal_slip.setVisibility(View.VISIBLE);
            }
        } else if (patientDetails.getIsHCRenewal().equalsIgnoreCase("no")) {
            ll_renewal_photo.setVisibility(View.GONE);
            isRenewalSlipPhoto = true;
        }

        if (!patientDetails.getAudioImage().trim().isEmpty()) {
            new DownloadAudimetryFile().execute(patientDetails.getAudioImage());
        } else {
            isAudiometryFile = false;
            tv_audiomerty_file.setVisibility(View.GONE);
            btn_audio_metry.setVisibility(View.VISIBLE);
        }

//        if (!patientDetails.getUserThumbPath().trim().isEmpty()) {
//            Picasso.with(context)
//                    .load(patientDetails.getUserThumbPath())
//                    .into(imv_thumb_print, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            isThumbPhoto = true;
//                            btn_thumb.setVisibility(View.GONE);
//                            cb_no_thumb_machine.setVisibility(View.GONE);
//                            imv_thumb_print.setVisibility(View.VISIBLE);
//                            cb_no_thumb_machine.setChecked(false);
//                        }
//
//                        @Override
//                        public void onError() {
//                            isThumbPhoto = false;
//                            btn_thumb.setVisibility(View.VISIBLE);
//                            cb_no_thumb_machine.setVisibility(View.VISIBLE);
//                            imv_thumb_print.setVisibility(View.VISIBLE);
//                            cb_no_thumb_machine.setChecked(false);
//                        }
//                    });
//        } else {
//            isThumbPhoto = false;
//            btn_thumb.setVisibility(View.VISIBLE);
//            cb_no_thumb_machine.setVisibility(View.VISIBLE);
//            imv_thumb_print.setVisibility(View.VISIBLE);
//            cb_no_thumb_machine.setChecked(false);
//        }
    }

    private class DownloadAudimetryFile extends AsyncTask<String, Integer, Boolean> {
        int lenghtOfFile = -1;
        int count = 0;
        int content = -1;
        int counter = 0;
        int progress = 0;
        URL downloadurl = null;
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMessage("Downloading Audiometry File");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean success = false;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            int read = -1;
            byte[] buffer = new byte[1024];
            FileOutputStream fileOutputStream = null;
            long total = 0;

            try {
                downloadurl = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) downloadurl.openConnection();
                lenghtOfFile = httpURLConnection.getContentLength();
                inputStream = httpURLConnection.getInputStream();

                audioMetryFile = new File(audioMetryfolder, Uri.parse(params[0]).getLastPathSegment());
                fileOutputStream = new FileOutputStream(audioMetryFile);
                while ((read = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                    counter = counter + read;
                    publishProgress(counter);
                }
                success = true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress = (int) (((double) values[0] / lenghtOfFile) * 100);
            pd.setProgress(progress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            pd.dismiss();
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                isAudiometryFile = true;
                tv_audiomerty_file.setVisibility(View.VISIBLE);
                btn_audio_metry.setVisibility(View.GONE);
            } else {
                isAudiometryFile = false;
                tv_audiomerty_file.setVisibility(View.GONE);
                btn_audio_metry.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setEventHandler() {

        imv_thumb_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                Intent cameraIntent = new Intent(context, CameraActivity.class);
                cameraIntent.putExtra("CAPTURE_MODE", CaptureMode.ID_CARD.name());
                cameraLauncherForThumbImage.launch(cameraIntent);


            }
        });



        btn_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UploadPatientRenewalPhoto_Activity.class)
                        .putExtra("campId", getIntent().getStringExtra("campId"))
                        .putExtra("siteId", String
                                .valueOf(patientDetails.getSiteId()))
                        .putExtra("resigNo", String.valueOf(patientDetails.getRegdNo()))
                        .putExtra("Type", "_PR.png"));
            }
        });

        btn_health_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UploadHealthCardPhoto_Activity.class)
                        .putExtra("campId", getIntent().getStringExtra("campId"))
                        .putExtra("siteId", String.valueOf(patientDetails.getSiteId()))
                        .putExtra("resigNo", String
                                .valueOf(patientDetails.getRegdNo())));
            }
        });

        btn_renewal_slip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UploadPatientRenewalPhoto_Activity.class)
                        .putExtra("campId", getIntent().getStringExtra("campId"))
                        .putExtra("siteId", patientDetails.getSiteId())
                        .putExtra("resigNo", patientDetails.getRegdNo())
                        .putExtra("Type", "_RHC.png"));
            }
        });


        View.OnClickListener radioClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rationCardPhotoList.clear();

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        };

        radioManual.setOnClickListener(radioClickListener);
        radioDigital.setOnClickListener(radioClickListener);

        imv_self_declaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (radioGroupArea.getCheckedRadioButtonId() == -1) {

                    Utilities.showMessageString("Please select any option", context);

                    return;
                }

                int maxPhotos = 0;

                if (radioManual.isChecked()) {

                    maxPhotos = 2;

                } else if (radioDigital.isChecked()) {

                    maxPhotos = 1;
                }

                if (rationCardPhotoList.size() >= maxPhotos) {

                    Utilities.showToastMessage(
                            "Maximum " + maxPhotos + " photo" +
                                    (maxPhotos > 1 ? "s" : "") + " allowed",
                            context,
                            false
                    );

                    return;
                }


                Intent cameraIntent = new Intent(context, CameraActivity.class);
                cameraIntent.putExtra("CAPTURE_MODE", CaptureMode.DOCUMENT.name());
                cameraLauncherForRationCard.launch(cameraIntent);


            }
        });


        btn_UploadRationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (patientPicBm == null) {
//                    Utilities.showMessageString("Please capture thumb photo", context);
//                    return;
//                }


                if (radioGroupArea.getCheckedRadioButtonId() == -1) {

                    Utilities.showMessageString("Please select any option", context);

                    return;
                }


//
//                if (radioManual.isChecked()){
//                    if (rationCardPhotoList.size() < 2) {
//
//                        Toast.makeText(
//                                context,
//                                "Minimum 2 images required",
//                                Toast.LENGTH_SHORT
//                        ).show();
//
//                        return;
//                    }
//
//                }else if (radioDigital.isChecked()){
//
//                    if (rationCardPhotoList.size() < 1 ) {
//
//                        Toast.makeText(
//                                context,
//                                "Minimum 1 images required",
//                                Toast.LENGTH_SHORT
//                        ).show();
//
//                        return;
//                    }
//                }


                int minPhotos = 0;

                if (radioManual.isChecked()) {

                    minPhotos = 2;

                } else if (radioDigital.isChecked()) {

                    minPhotos = 1;
                }

                if (rationCardPhotoList.size() < minPhotos) {

                    if (radioManual.isChecked()){
                        Toast.makeText(
                                context,
                                "Minimum " + "two" + " image" +
                                        (minPhotos > 1 ? "s" : "") + " required for manual ration card",
                                Toast.LENGTH_SHORT
                        ).show();

                    }else if (radioDigital.isChecked()){
                        Toast.makeText(
                                context,
                                "Minimum " + "one" + " image" +
                                        (minPhotos > 1 ? "s" : "") + " required for digital ration card",
                                Toast.LENGTH_SHORT
                        ).show();


                    }

                    return;
                }


                if (!Utilities.isNetworkAvailable(context)) {

                    Utilities.showToastMessage(
                            R.string.msgt_nointernetconnection,
                            context,
                            false
                    );

                    return;
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert!");
                builder.setMessage("Please confirm the beneficiary's details before submitting");
                builder.setCancelable(false);
                builder.setPositiveButton("Proceed", (dialog, which) -> {

                    uploadedCount = 0;

                    totalUploadCount = rationCardPhotoList.size();

                    for (int i = 0; i < rationCardPhotoList.size(); i++) {

                        String imagePath =
                                rationCardPhotoList.get(i).getImagePath();


//                        new InsertRationCardDetailsDetails().execute(
//                                regIdAfterReg,
//                                EmpCode,
//                                dependentBocId,
//                                rationCardNumber,
//                                "0",
//                                imagePath
//                        );


                    }

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.create().show();


            }
        });

        tv_audiomerty_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(patientDetails.getAudioImage()));
//                    intent.setAction(Intent.ACTION_VIEW,Uri.parse(patientDetails.getAudioImage()));
//                    Uri uri = Uri.parse("file://" + audioMetryFile);
//                    intent.setDataAndType(uri, "application/pdf");
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e("Audiometry err", e.getMessage());
                }
            }
        });

        btn_audio_metry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, HealthScreeningAudioTest_Activity_New.class)
                        .putExtra("patientDetails", patientDetails)
                        .putExtra("healthScreentype", "9")
                        .putExtra("campId", getIntent().getStringExtra("campId")));
            }
        });

        btn_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UploadFingerPrint_Activity.class)
                        .putExtra("resigNo", patientDetails.getRegdNo()));
            }
        });

        cb_no_thumb_machine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_thumb.setVisibility(View.GONE);
                } else {
                    btn_thumb.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {
//        if (!isPatientPhoto) {
//            Utilities.showToastMessage("Upload patient photo", context, false);
//            return;
//        }
//
//        if (!isHealthCardPhoto) {
//            Utilities.showToastMessage("Upload health card photo", context, false);
//            return;
//        }
//
//        if (!isRenewalSlipPhoto) {
//            Utilities.showToastMessage("Upload renewal slip photo", context, false);
//            return;
//        }

//        if (!isAudiometryFile) {
//            Utilities.showToastMessage("Upload audiometry details", context, false);
//            return;
//        }
//
//        if (!cb_no_thumb_machine.isChecked()) {
//            if (!isThumbPhoto) {
//                Utilities.showToastMessage("Upload thumb print photo", context, false);
//                return;
//            }
//        }


        if (patientPicBm == null){
            Utilities.showAlertDialog(context,"Alert","Please capture thumb photo",false);
            return;
        }

        String IsDeviceAvilaible = "0";

        if (cb_no_thumb_machine.isChecked()) {
            IsDeviceAvilaible = "0";
        } else {
            IsDeviceAvilaible = "1";
        }

        context.startActivity(new Intent(context, HealthScreeningAcknowledgement_Activity.class)
                .putExtra("patientDetails", patientDetails)
                .putExtra("healthScreentype", "9")
                .putExtra("campId", getIntent().getStringExtra("campId"))
                .putExtra("IsDeviceAvilaible", IsDeviceAvilaible)
                .putExtra("fingerPrintPath", fingerPrintPath)
        );
        finish();

    }




    public class InsertRationCardDetailsDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd.setMessage("Uploading " + rationCardPhotoList.size() + " files..");
            pd.setCancelable(false);
            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {

            String res = "";

            try {

                MultipartUtility multipart =
                        new MultipartUtility(
                                ApplicationConstants.InsertRationCardDetails,
                                "UTF-8"
                        );

                multipart.addFormField("RegdID", params[0]);

                multipart.addFormField("UserId", params[1]);

                multipart.addFormField("Bocw_Dependent_Id", params[2]);

                multipart.addFormField("RationCardNo", params[3]);
                multipart.addFormField("RCID", params[4]);


                if (!params[4].equals("")) {

                    multipart.addFilePart(
                            "RationCardImage",
                            new File(params[5])
                    );
                }

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
            Log.d("InsertRationCard", result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        uploadedCount++;


                        if (uploadedCount == totalUploadCount) {

                            pd.dismiss();

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(context);

                            builder.setIcon(R.drawable.icon_success);

                            builder.setTitle("Success");

                            builder.setCancelable(false);

                            builder.setMessage(
                                    "Photos Uploaded Successfully."
                            );

                            builder.setPositiveButton(
                                    "OK",
                                    (dialog, which) -> submitData()
                            );

                            builder.show();
                        }

                    } else {

                        pd.dismiss();

                        Utilities.showAlertDialog(
                                context,
                                status,
                                message,
                                false
                        );

                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient Details Confirmation");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }








    ActivityResultLauncher<Intent> cameraLauncherForRationCard =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");

                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_RC";
                                String compressedFilePath = Utilities.compressImageNew(context, fileURI, filename);
                                saveFileRationCard(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });




    ActivityResultLauncher<Intent> cameraLauncherForThumbImage =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String fileURI = data.getStringExtra("fileURI");
                            String fName = data.getStringExtra("fName");

                            try {

                                String filename = (int) (Math.random() * 99999 + 1) + "_FR";
                                String compressedFilePath = Utilities.compressImageNew(context, fileURI, filename);
                                saveFileThumbImage(compressedFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });





    @SuppressLint("MissingPermission")
    public void saveFileThumbImage(String filePath) {
        String destinationFilename = filePath;
        patientPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = patientDetails.getRegdId() + "_1_" + EmpCode + "_FP.jpg";
        String outputFilePath = getExternalCacheDir() + fName;
        try {
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            patientPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imv_thumb_print.setImageBitmap(patientPicBm);
        fingerPrintPath = outputFilePath;
    }


    @SuppressLint("MissingPermission")
    public void saveFileRationCard(String filePath) {
        String destinationFilename = filePath;
        rationCardPicBm = BitmapFactory.decodeFile(destinationFilename);
        String fName = System.currentTimeMillis() + "_RC.jpg";
        String outputFilePath = getExternalCacheDir() + fName;
        // Step 2: Compress the Bitmap and store it in a new file
        try {
            // Open a file output stream to save the compressed image
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            // Compress the bitmap as JPEG with 80% quality
            rationCardPicBm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            // Close the output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Add Image To List
        rationCardPhotoList.add(
                new RationCardPhotoModel(
                        outputFilePath,
                        fName
                )
        );

        adapter.notifyDataSetChanged();


        imv_self_declaration.setImageResource(
                R.drawable.icon_colorcamera
        );


        Toast.makeText(
                this,
                "Photo Added",
                Toast.LENGTH_SHORT
        ).show();


//        imv_self_declaration.setImageBitmap(rationCardPicBm);
        rationCardImagePath = outputFilePath;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            patientDetails = (PresentPatientList_Model) intent.getSerializableExtra("patientDetails");
//            setDefaults();
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }


}
