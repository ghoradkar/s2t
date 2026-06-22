package com.myhindlab.abkat.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.calling_dashboard.adapter.RationCardPhotoAdapter;
import com.myhindlab.abkat.camera.CameraActivity;
import com.myhindlab.abkat.camera.utils.CaptureMode;
import com.myhindlab.abkat.databinding.ActivityAttendanceDetailsForCcBinding;
import com.myhindlab.abkat.databinding.ActivityHealthScreeningRationCardConfirmationBinding;
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

public class HealthScreeningRationCard_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;

    private ProgressDialog pd;

    private Bitmap patientPicBm = null, rationCardPicBm = null;

    private String rationCardImagePath = "",EmpCode,dependentBocId;


    private ActivityHealthScreeningRationCardConfirmationBinding binding;

    private int uploadedCount = 0;
    private int totalUploadCount = 0;

    private ArrayList<RationCardPhotoModel> rationCardPhotoList =
            new ArrayList<>();

    private CircleImageView imv_patient;
    private ImageView imv_health_card, imv_renewal_form, imv_thumb_print;
    private TextView tv_audiomerty_file;
    private LinearLayout ll_renewal_photo;
    private Button btn_patient, btn_health_card, btn_renewal_slip, btn_audio_metry, btn_thumb, btn_next;
    private CheckBox cb_no_thumb_machine;




    private PresentPatientList_Model patientDetails;

    private File audioMetryFile, audioMetryfolder;
    private boolean isPatientPhoto, isHealthCardPhoto, isRenewalSlipPhoto, isAudiometryFile, isThumbPhoto;

    private LocalBroadcastManager localBroadcastManager;

    private RationCardPhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_health_screening_ration_card_confirmation);

        binding = ActivityHealthScreeningRationCardConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();

    }

    private void init() {
        context = HealthScreeningRationCard_Activity.this;
        session = new UserSessionManager(context);

        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);




        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("HealthScreeningRationCard_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");


        adapter = new RationCardPhotoAdapter(
                this,
                rationCardPhotoList
        );

        binding.recyclerPhotos.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.recyclerPhotos.setAdapter(adapter);


    }

    private void setDefaults() {
//        Log.d("patintDetails", patientDetails.getPatientPhoto());



        binding.edtName.setText(patientDetails.getEnglishName());
        binding.edtMcobcwwbno.setText(""+patientDetails.getRegdNo());
        binding.edtGender.setText(patientDetails.getGender());
        binding.edtAge.setText(patientDetails.getAge());
        binding.edtRcNumber.setText(patientDetails.getRationCardNo());

        dependentBocId = patientDetails.getBocw_idDepend();



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

    private void setEventHandler() {




        binding.btnUploadRationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (patientPicBm == null) {
//                    Utilities.showMessageString("Please capture thumb photo", context);
//                    return;
//                }


                if (binding.radioGroupArea.getCheckedRadioButtonId() == -1) {

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

                if (binding.radioManual.isChecked()) {

                    minPhotos = 2;

                } else if (binding.radioDigital.isChecked()) {

                    minPhotos = 1;
                }

                if (rationCardPhotoList.size() < minPhotos) {

                    if (binding.radioManual.isChecked()){
                        Toast.makeText(
                                context,
                                "Minimum " + "two" + " image" +
                                        (minPhotos > 1 ? "s" : "") + " required for manual ration card",
                                Toast.LENGTH_SHORT
                        ).show();

                    }else if (binding.radioDigital.isChecked()){
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


                        new InsertRationCardDetailsDetails().execute(
                                String.valueOf(patientDetails.getRegdId()),
                                EmpCode,
                                dependentBocId,
                                binding.edtRcNumber.getText().toString().trim(),
                                "0",
                                imagePath
                        );


                    }

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.create().show();


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

        binding.radioManual.setOnClickListener(radioClickListener);
        binding.radioDigital.setOnClickListener(radioClickListener);



        binding.imvSelfDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.radioGroupArea.getCheckedRadioButtonId() == -1) {

                    Utilities.showMessageString("Please select any option", context);

                    return;
                }

                int maxPhotos = 0;

                if (binding.radioManual.isChecked()) {

                    maxPhotos = 2;

                } else if (binding.radioDigital.isChecked()) {

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

                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

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
                                    (dialog, which) -> finish()
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


        binding.imvSelfDeclaration.setImageResource(
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
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ration Card Acknowledgement");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
