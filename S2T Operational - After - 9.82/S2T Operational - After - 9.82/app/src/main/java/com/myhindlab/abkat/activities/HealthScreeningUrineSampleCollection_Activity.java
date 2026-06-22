package com.myhindlab.abkat.activities;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myhindlab.abkat.utilities.Utilities.isBarCodeValid;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HealthScreeningUrineSampleCollection_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    UserSessionManager userSessionManager;

    private EditText edt_beneficiaryname, edt_gender, edt_age, edt_height, edt_weight, edt_barcode1, edt_blood_pp;
    private TextInputLayout remarkTextInputLayout;
    private TextInputEditText remarkTextInputEditText;
    private Button btn_register;
    private PresentPatientList_Model patientDetails;
    private String userID, name, campId, healthScreentype;
    private ImageView imv_barcode1;
    private ProgressDialog pd;
    private RadioGroup rgSampleStatus;
    private RadioButton rbSampleCollected, rbSampleNotCollected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_screening_urine_sample_collection);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = HealthScreeningUrineSampleCollection_Activity.this;
        userSessionManager = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        edt_blood_pp = findViewById(R.id.edt_blood_pp);
        btn_register = findViewById(R.id.btn_register);
        imv_barcode1 = findViewById(R.id.imv_barcode1);
        edt_barcode1 = findViewById(R.id.edt_barcode1);
        rgSampleStatus = findViewById(R.id.rgSampleStatus);
        rbSampleNotCollected = findViewById(R.id.rbSampleNotCollected);
        rbSampleCollected = findViewById(R.id.rbSampleCollected);
        remarkTextInputEditText = findViewById(R.id.remarkTextInputEditText);
        remarkTextInputLayout = findViewById(R.id.remarkTextInputLayout);

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Urine Sample Collection");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        String json = new Gson().toJson(patientDetails);
        Log.d(HealthScreeningUrineSampleCollection_Activity.class.getSimpleName(), "setDefaults: " + json);
        edt_barcode1.setEnabled(false);
        if (!patientDetails.getBarcode1().equalsIgnoreCase("null")) {
            edt_barcode1.setText(patientDetails.getBarcode1());
        }

    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(userSessionManager.getUserDetails().get(
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
        btn_register.setOnClickListener(this);
        imv_barcode1.setOnClickListener(this);

        rgSampleStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                btn_register.setVisibility(View.VISIBLE);
                if (rbSampleCollected.getId() == i) {
                    remarkTextInputLayout.setVisibility(View.GONE);

                } else {
                    remarkTextInputLayout.setVisibility(View.VISIBLE);
                }
                Log.d(HealthScreeningUrineSampleCollection_Activity.class.getSimpleName(), "onCheckedChanged: " + i);
            }
        });

        remarkTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 10) {
                    remarkTextInputLayout.setErrorEnabled(false);
                } else {
                    remarkTextInputLayout.setErrorEnabled(true);
                    remarkTextInputLayout.setError("Please enter reason, for not collecting urine sample.");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_register:

                if (edt_blood_pp.getText().toString().trim().isEmpty()) {
                    edt_blood_pp.setError("Please enter sample count");
                    return;
                }

                if (!isBarCodeValid(edt_barcode1.getText().toString().trim())) {
                    edt_barcode1.setError("Please enter valid barcode");
                    return;
                }

                if (rbSampleNotCollected.isChecked() && remarkTextInputEditText.getText().toString().isEmpty()) {

                    remarkTextInputLayout.setErrorEnabled(true);
                    remarkTextInputLayout.setError("Please enter reason, for not collecting urine sample.");
                    return;
                } else {
                    remarkTextInputLayout.setErrorEnabled(false);
                }

                Utilities.showAlertDialog(context, "Confirm", "Are you sure," + (rbSampleCollected.isChecked() ? "you have Collected Sample" : "You have Not Collected Sample"), true, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Utilities.isNetworkAvailable(context)) {
                            new InsertCW_PatientBarcodeDetails().execute(
                                    String.valueOf(patientDetails.getRegdId()),
                                    campId,
                                    edt_barcode1.getText().toString().trim(),
                                    rbSampleCollected.isChecked() ? "1" : "2",
                                    userID,
                                    rbSampleNotCollected.isChecked() ? remarkTextInputEditText.getText().toString().trim() : ""
                            );
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
                    }
                }, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                break;

            case R.id.imv_barcode1:
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
                    return;
                }

                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 10001) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                    edt_barcode1.setText(requiredValue);
                } else if (requestCode == 10002) {
                    String requiredValue = data.getStringExtra("key");
                    Toast.makeText(context, requiredValue, Toast.LENGTH_SHORT).show();
                    //edt_barcode2.setText(requiredValue);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private class InsertCW_PatientBarcodeDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Barcode1", params[2]));
            param.add(new ParamsPojo("SampleReciveFlag", params[3]));
            param.add(new ParamsPojo("CreatedBy", params[4]));
            param.add(new ParamsPojo("Remark", params[5]));
            Log.d("TAG", "doInBackground: " + Arrays.toString(params));
                res = WebServiceCall.APICall(ApplicationConstants.InsertCW_UrineSampleRecived, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Insert Urine", "onPostExecute: "+result);
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
                        builder.setMessage("Sample collection details submitted successfully");
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

}
