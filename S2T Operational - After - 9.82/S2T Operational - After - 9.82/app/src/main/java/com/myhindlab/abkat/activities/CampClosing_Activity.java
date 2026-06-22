package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.PatientStatusModel;
import com.myhindlab.abkat.pojos.CampClosedbyAdminResponse;
import com.myhindlab.abkat.pojos.PatientStatusPojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CampClosing_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private TextView tv_refresh, tv_registered_workers, tv_basic_health_info, tv_physical_exam, tv_sample_collection, tv_lung_screening, tv_audio_screening, tv_vision_screening,
            tv_breast_screening, tv_blood_sugar_pp, tv_acknowledgement, tv_urine_sample, tv_total_pending;
    private LinearLayout ll_registered_workers, ll_basic_health_info, ll_physical_exam, ll_sample_collection, ll_lung_screening, ll_audio_screening, ll_vision_screening,
            ll_breast_screening, ll_blood_sugar_pp, ll_acknowledgement, ll_urine_sample;
    private Button btn_close_camp;

    private String userId, campId, DISTLGDCODE;
    private int mYear, mMonth;
    private LocalBroadcastManager localBroadcastManager;

    private ArrayList<PatientStatusModel> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_closing);

        init();
        getSessionDetails();
        setDefaults();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = CampClosing_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        tv_refresh = findViewById(R.id.tv_refresh);
        tv_registered_workers = findViewById(R.id.tv_registered_workers);
        tv_basic_health_info = findViewById(R.id.tv_basic_health_info);
        tv_physical_exam = findViewById(R.id.tv_physical_exam);
        tv_sample_collection = findViewById(R.id.tv_sample_collection);
        tv_lung_screening = findViewById(R.id.tv_lung_screening);
        tv_audio_screening = findViewById(R.id.tv_audio_screening);
        tv_vision_screening = findViewById(R.id.tv_vision_screening);
        tv_breast_screening = findViewById(R.id.tv_breast_screening);
        tv_blood_sugar_pp = findViewById(R.id.tv_blood_sugar_pp);
        tv_acknowledgement = findViewById(R.id.tv_acknowledgement);
        tv_urine_sample = findViewById(R.id.tv_urine_sample);
        tv_total_pending = findViewById(R.id.tv_total_pending);
        ll_registered_workers = findViewById(R.id.ll_registered_workers);
        ll_basic_health_info = findViewById(R.id.ll_basic_health_info);
        ll_physical_exam = findViewById(R.id.ll_physical_exam);
        ll_sample_collection = findViewById(R.id.ll_sample_collection);
        ll_lung_screening = findViewById(R.id.ll_lung_screening);
        ll_audio_screening = findViewById(R.id.ll_audio_screening);
        ll_vision_screening = findViewById(R.id.ll_vision_screening);
        ll_breast_screening = findViewById(R.id.ll_breast_screening);
        ll_blood_sugar_pp = findViewById(R.id.ll_blood_sugar_pp);
        ll_acknowledgement = findViewById(R.id.ll_acknowledgement);
        ll_urine_sample = findViewById(R.id.ll_urine_sample);
        btn_close_camp = findViewById(R.id.btn_close_camp);

        patientList = new ArrayList<>();
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
        DISTLGDCODE = getIntent().getStringExtra("DISTLGDCODE");
        mYear = getIntent().getIntExtra("mYear", 0);
        mMonth = getIntent().getIntExtra("mMonth", 0);

        if (Utilities.isNetworkAvailable(context))
            new CAMPPatientCheckupAnalysis_Report().execute(campId, DISTLGDCODE);
        else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("CampClosing_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private void setEventHandler() {
        tv_refresh.setOnClickListener(this);
        ll_registered_workers.setOnClickListener(this);
        ll_basic_health_info.setOnClickListener(this);
        ll_physical_exam.setOnClickListener(this);
        ll_sample_collection.setOnClickListener(this);
        ll_lung_screening.setOnClickListener(this);
        ll_audio_screening.setOnClickListener(this);
        ll_vision_screening.setOnClickListener(this);
        ll_breast_screening.setOnClickListener(this);
        ll_blood_sugar_pp.setOnClickListener(this);
        ll_acknowledgement.setOnClickListener(this);
        ll_urine_sample.setOnClickListener(this);
        btn_close_camp.setOnClickListener(this);
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Closing");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_refresh: {
                if (Utilities.isNetworkAvailable(context))
                    new CAMPPatientCheckupAnalysis_Report().execute(campId, DISTLGDCODE);
                else
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
            break;

            case R.id.ll_registered_workers: {
                if (tv_registered_workers.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are registered beneficiaries", context, false);
                }
            }
            break;

            case R.id.ll_basic_health_info: {
                if (tv_basic_health_info.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getBasicDetails().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 1));
            }
            break;

            case R.id.ll_physical_exam: {
                if (tv_physical_exam.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getPhysicalExamination().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 2));
            }
            break;

            case R.id.ll_sample_collection: {
                if (tv_sample_collection.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getBarcode().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 3));
            }
            break;

            case R.id.ll_lung_screening: {
                if (tv_lung_screening.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getLungFunctioinTest().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 4));
            }
            break;

            case R.id.ll_audio_screening: {
                if (tv_audio_screening.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getAudioScreeningTest().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 5));
            }
            break;

            case R.id.ll_vision_screening: {
                if (tv_vision_screening.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getVisionScreening().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 6));
            }
            break;

            case R.id.ll_breast_screening: {
                if (tv_breast_screening.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getGender().equalsIgnoreCase("F"))
                        if (model.getBreastScreening().equals("0"))
                            filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 7));
            }
            break;

            case R.id.ll_blood_sugar_pp: {
                if (tv_blood_sugar_pp.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getPPSampleCollection().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 8));
            }
            break;

            case R.id.ll_acknowledgement: {
                if (tv_acknowledgement.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getAckowledgement().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 9));
            }
            break;

            case R.id.ll_urine_sample: {
                if (tv_urine_sample.getText().toString().trim().equals("0")) {
                    Utilities.showToastMessage("There are no pending beneficiaries", context, false);
                    return;
                }

                ArrayList<PatientStatusModel> filteredPatientsList = new ArrayList<>();

                for (PatientStatusModel model : patientList)
                    if (model.getUrineSampleCollection().equals("0"))
                        filteredPatientsList.add(model);

                startActivity(new Intent(context, CampClosingPendingPatients_Activity.class)
                        .putExtra("patientList", filteredPatientsList)
                        .putExtra("mYear", mYear)
                        .putExtra("mMonth", mMonth)
                        .putExtra("type", 10));
            }
            break;

            case R.id.btn_close_camp: {
                if (!tv_total_pending.getText().toString().equals("0")) {
                    return;
                }
                openRemarkDialog();
            }
            break;
        }
    }

    private class CAMPPatientCheckupAnalysis_Report extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.CAMPPatientCheckupAnalysis_Report, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {
                    PatientStatusPojo pojoDetails = new Gson().fromJson(result, PatientStatusPojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        patientList = pojoDetails.getOutput();
                        if (patientList.size() > 0) {
                            getPendingCounts();
                        } else {
                            Utilities.showAlertDialog(context, "Fail", "Beneficiary not registered", false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showAlertDialog(context, "Fail", "Server not responding", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Fail", "Server not responding", false);
            }
        }
    }

    private void openRemarkDialog() {
        final MaterialEditText edt_remark = new MaterialEditText(context);
        edt_remark.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edt_remark.setHighlightColor(getResources().getColor(R.color.colorPrimary));
        float dpi = context.getResources().getDisplayMetrics().density;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Enter Remark");

        alertDialogBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Utilities.isNetworkAvailable(context)) {
                    new CampClosedbyAdminAPICall().execute(campId, userId, edt_remark.getText().toString().trim());
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.setView(edt_remark, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
        alertD.show();
        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        edt_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    private void getPendingCounts() {

        int registered_workers = 0, basic_health_info = 0, physical_exam = 0, sample_collection = 0, lung_screening = 0, audio_screening = 0,
                vision_screening = 0, breast_screening = 0, blood_sugar_pp = 0, acknowledgement = 0, urine_sample = 0, total_pending = 0;

        registered_workers = patientList.size();
        for (PatientStatusModel model : patientList) {
            if (model.getBasicDetails().equals("0"))
                basic_health_info = basic_health_info + 1;

            if (model.getPhysicalExamination().equals("0"))
                physical_exam = physical_exam + 1;

            if (model.getBarcode().equals("0"))
                sample_collection = sample_collection + 1;

            if (model.getLungFunctioinTest().equals("0"))
                lung_screening = lung_screening + 1;

            if (model.getAudioScreeningTest().equals("0"))
                audio_screening = audio_screening + 1;

            if (model.getVisionScreening().equals("0"))
                vision_screening = vision_screening + 1;

            if (model.getAckowledgement().equals("0"))
                acknowledgement = acknowledgement + 1;

//            if (model.getUrineSampleCollection().equals("0"))
//                urine_sample = urine_sample + 1;

            if (model.getPPSampleCollection().equals("0"))
                blood_sugar_pp = blood_sugar_pp + 1;

            if (model.getGender().equalsIgnoreCase("F"))
                if (model.getBreastScreening().equals("0"))
                    breast_screening = breast_screening + 1;
        }

        total_pending = basic_health_info + physical_exam + sample_collection + lung_screening + audio_screening +
                vision_screening + acknowledgement + urine_sample + blood_sugar_pp + breast_screening;

        if (total_pending == 0) {
            btn_close_camp.setEnabled(true);
        } else {
            btn_close_camp.setEnabled(false);
        }

        tv_registered_workers.setText(String.valueOf(registered_workers));
        tv_basic_health_info.setText(String.valueOf(basic_health_info));
        tv_physical_exam.setText(String.valueOf(physical_exam));
        tv_sample_collection.setText(String.valueOf(sample_collection));
        tv_lung_screening.setText(String.valueOf(lung_screening));
        tv_audio_screening.setText(String.valueOf(audio_screening));
        tv_vision_screening.setText(String.valueOf(vision_screening));
        tv_breast_screening.setText(String.valueOf(breast_screening));
        tv_blood_sugar_pp.setText(String.valueOf(blood_sugar_pp));
        tv_acknowledgement.setText(String.valueOf(acknowledgement));
        tv_urine_sample.setText(String.valueOf(urine_sample));
        tv_total_pending.setText(String.valueOf(total_pending));


    }

    private class CampClosedbyAdminAPICall extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampId", params[0]));
            param.add(new ParamsPojo("CreatedBy", params[1]));
            param.add(new ParamsPojo("Remark", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.CampClosedbyAdmin, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    CampClosedbyAdminResponse pojoDetails = new Gson().fromJson(result, CampClosedbyAdminResponse.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        Utilities.showAlertDialog(context, "Success", "Camp closed successfully!", true, "OK", (dialog, which) -> finish());
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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utilities.isNetworkAvailable(context))
                new CAMPPatientCheckupAnalysis_Report().execute(campId, DISTLGDCODE);
            else
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
