package com.myhindlab.abkat.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.doortodoor.D2DAttendanceMarkedPatients_Activity;
import com.myhindlab.abkat.activities.doortodoor.D2DHealthScreeningPhysicalExamination_Activity;
import com.myhindlab.abkat.activities.doortodoor.D2dPhysicalExaminationDetailsActivity;
import com.myhindlab.abkat.adapters.HealthScreeningAdapter;
import com.myhindlab.abkat.models.HealthScreeningListModel;
import com.myhindlab.abkat.models.HomeLabHublabOnLandingLabModel;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HealthScreening_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private RecyclerView rv_healthscreeninglist;

    private String userId, userName, campDate = "", distLgdCode = "0", heathscreeningType, designName, designId, DesgLevelId, selTaluka, selGp, labCode, SiteDetailId = "", districtName;

    private ArrayList<HealthScreeningListModel> healthScreeningList;
    private PresentPatientList_Model patientDetails;
    private String campId, isTestMapped, CampType;
    private int DESGID;
    public static Activity fa1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_screening);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        fa1 = this;
        context = HealthScreening_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        rv_healthscreeninglist = findViewById(R.id.rv_healthscreeninglist);
        rv_healthscreeninglist.setLayoutManager(new GridLayoutManager(context, 2));

    }

    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
        CampType = getIntent().getStringExtra("CampType");

        session = new UserSessionManager(context);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getInt("DESGID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        healthScreeningList = new ArrayList<>();


        if (DESGID == 34 || DESGID == 147 || DESGID == 130 || DESGID == 141 || DESGID == 181) {

            if (CampType.equalsIgnoreCase("3")) { //d2d Camp
//                healthScreeningList.add(new HealthScreeningListModel("16", "D2D Physical Examination", R.drawable.icon_physical_examination_1, D2DHealthScreeningPhysicalExamination_Activity.class));
//                healthScreeningList.add(new HealthScreeningListModel("16", "D2D Physical Examination Test", R.drawable.icon_physical_examination_1, D2DHealthScreeningPhysicalExamination_Activity.class));
                healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));

            } else {
                healthScreeningList.add(new HealthScreeningListModel("3", "Physical Examination", R.drawable.icon_physical_examination_1, HealthScreeningPhysicalExamination_Activity.class));
                healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));

//                healthScreeningList.add(new HealthScreeningListModel("3", "Physical Examination Test", R.drawable.icon_physical_examination_1, HealthScreeningPhysicalExamination_Activity.class));
            }

        } else if (DESGID == 92 || DESGID == 104 || DESGID == 108 || DESGID == 160 || DESGID == 139 || DESGID == 136) {

            if (CampType.equalsIgnoreCase("3")) { //d2d Camp
                healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));
                healthScreeningList.add(new HealthScreeningListModel("0", "Camp Closing", R.drawable.icon_closing, CampClosingActivity.class));

            } else {
                healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));
                healthScreeningList.add(new HealthScreeningListModel("0", "Camp Closing", R.drawable.icon_closing, CampClosingActivity.class));
                //  healthScreeningList.add(new HealthScreeningListModel("3", "Physical Examination", R.drawable.icon_physical_examination_1, HealthScreeningPhysicalExamination_Activity.class));
            }


        } else if (DESGID == 104 || DESGID == 105 || DESGID == 77 || DESGID == 84 || DESGID == 30) {

            if (CampType.equalsIgnoreCase("3")) {

                healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));

            } else {
                healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));

            }
        } else if (DESGID == 35 || DESGID == 4 || DESGID == 105 || DESGID == 146 || DESGID == 129 || DESGID == 138 || DESGID == 137 || DESGID == 169 || DESGID == 177 || DESGID == 31 || DESGID == 176) {
            healthScreeningList.add(new HealthScreeningListModel("7", "Sample Collection", R.drawable.icon_bloodsample, HealthScreeningSamplecollection_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("11", "Urine Sample Collection", R.drawable.icon_urine, HealthScreeningUrineSampleCollection_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("2", "Basic Health Info", R.drawable.icon_basichealth, HealthScreeningBasicHealth_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("15", "Antigen Test", R.drawable.icon_swab_collection, HealthscreeningAntigenTest_Activity.class));
            if (CampType.equalsIgnoreCase("3")) { //d2d Camp
                healthScreeningList.add(new HealthScreeningListModel("16", "D2D Physical Examination", R.drawable.icon_physical_examination_1, D2DHealthScreeningPhysicalExamination_Activity.class));
//                healthScreeningList.add(new HealthScreeningListModel("16", "D2D Physical Examination Test", R.drawable.icon_physical_examination_1, D2DHealthScreeningPhysicalExamination_Activity.class));

            } else {
                //  healthScreeningList.add(new HealthScreeningListModel("3", "Physical Examination", R.drawable.icon_physical_examination_1, HealthScreeningPhysicalExamination_Activity.class));
            }
            if (CampType.equals("2"))
                healthScreeningList.add(new HealthScreeningListModel("12", "Mini-camp Questionnaire", R.drawable.icon_minicamp_questionary, HealthScreeningMiniCamp_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("8", "Blood Sugar (PP)", R.drawable.icon_blood_sugar_pp, HealthScreeningBloodSugarPP_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("4", "Lung Function Test", R.drawable.icon_lft, HealthScreeningLFT__Safey_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("5", "Audio Screening Test", R.drawable.icon_ast, HealthScreeningAudioTest_Activity_New.class));
            healthScreeningList.add(new HealthScreeningListModel("6", "Visual Screening Test", R.drawable.icon_vst, HealthScreeningVisualTest_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("13", "Breast Screening", R.drawable.icon_female, HealthScreeningBreast_Activity_v3.class));
            healthScreeningList.add(new HealthScreeningListModel("9", "Acknowledgement", R.drawable.icon_acknowledgement, HealthScreeningAcknowledgementConfirmation_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("100", "Ration card upload", R.drawable.icon_acknowledgement, HealthScreeningRationCard_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("0", "Sample Processing Dashboard", R.drawable.icon_sample_processing, CampDetails_Activity.class));
        } else if (DESGID == 86) {

            // healthScreeningList.add(new HealthScreeningListModel("11", "Urine Sample Collection", R.drawable.icon_urine, HealthScreeningUrineSampleCollection_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("2", "Basic Health Info", R.drawable.icon_basichealth, HealthScreeningBasicHealth_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("15", "Antigen Test", R.drawable.icon_swab_collection, HealthscreeningAntigenTest_Activity.class));
            if (CampType.equalsIgnoreCase("3")) { //d2d Camp
                healthScreeningList.add(new HealthScreeningListModel("16", "D2D Physical Examination", R.drawable.icon_physical_examination_1, D2DHealthScreeningPhysicalExamination_Activity.class));
//                healthScreeningList.add(new HealthScreeningListModel("16", "D2D Physical Examination Test", R.drawable.icon_physical_examination_1, D2DHealthScreeningPhysicalExamination_Activity.class));

            } else {
                //  healthScreeningList.add(new HealthScreeningListModel("3", "Physical Examination", R.drawable.icon_physical_examination_1, HealthScreeningPhysicalExamination_Activity.class));
            }
            if (CampType.equals("2"))
                healthScreeningList.add(new HealthScreeningListModel("12", "Mini-camp Questionnaire", R.drawable.icon_minicamp_questionary, HealthScreeningMiniCamp_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("8", "Blood Sugar (PP)", R.drawable.icon_blood_sugar_pp, HealthScreeningBloodSugarPP_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("4", "Lung Function Test", R.drawable.icon_lft, HealthScreeningLFT__Safey_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("5", "Audio Screening Test", R.drawable.icon_ast, HealthScreeningAudioTest_Activity_New.class));
            healthScreeningList.add(new HealthScreeningListModel("6", "Visual Screening Test", R.drawable.icon_vst, HealthScreeningVisualTest_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("13", "Breast Screening", R.drawable.icon_female, HealthScreeningBreast_Activity_v3.class));
            healthScreeningList.add(new HealthScreeningListModel("9", "Acknowledgement", R.drawable.icon_acknowledgement, HealthScreeningAcknowledgementConfirmation_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("100", "Ration card upload", R.drawable.icon_acknowledgement, HealthScreeningRationCard_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("0", "Sample Processing Dashboard", R.drawable.icon_sample_processing, CampDetails_Activity.class));

        } else {
            // healthScreeningList.add(new HealthScreeningListModel("2", "Basic Health Info", R.drawable.icon_basichealth, HealthScreeningBasicHealth_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("15", "Antigen Test", R.drawable.icon_swab_collection, HealthscreeningAntigenTest_Activity.class));
            if (CampType.equalsIgnoreCase("3")) { //d2d Camp
//                  healthScreeningList.add(new HealthScreeningListModel("16", "D2D Physical Examination", R.drawable.icon_physical_examination_1, D2DHealthScreeningPhysicalExamination_Activity.class));
            } else {
//                   healthScreeningList.add(new HealthScreeningListModel("3", "Physical Examination", R.drawable.icon_physical_examination_1, HealthScreeningPhysicalExamination_Activity.class));
            }
            if (CampType.equals("2"))
                healthScreeningList.add(new HealthScreeningListModel("12", "Mini-camp Questionnaire", R.drawable.icon_minicamp_questionary, HealthScreeningMiniCamp_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("8", "Blood Sugar (PP)", R.drawable.icon_blood_sugar_pp, HealthScreeningBloodSugarPP_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("4", "Lung Function Test", R.drawable.icon_lft, HealthScreeningLFT__Safey_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("5", "Audio Screening Test", R.drawable.icon_ast, HealthScreeningAudioTest_Activity_New.class));
////            healthScreeningList.add(new HealthScreeningListModel("13", "Breast Screening", R.drawable.icon_female, HealthScreeningBreast_Activity_v3.class));
//            healthScreeningList.add(new HealthScreeningListModel("6", "Visual Screening Test", R.drawable.icon_vst, HealthScreeningVisualTest_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("9", "Acknowledgment", R.drawable.icon_acknowledgement, HealthScreeningAcknowledgementConfirmation_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("0", "Screening Status", R.drawable.icon_screeningdetails, CampDetails_Activity.class));
//            healthScreeningList.add(new HealthScreeningListModel("0", "Sample Processing Dashboard", R.drawable.icon_sample_processing, CampDetails_Activity.class));
            healthScreeningList.add(new HealthScreeningListModel("0", "Camp Closing", R.drawable.icon_closing, CampClosingActivity.class));
        }

        rv_healthscreeninglist.setAdapter(new HealthScreeningAdapter(context, healthScreeningList));

    }

    private void getSessionData() {
        UserSessionManager session = new UserSessionManager(context);
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
                designId = json.getString("DESGID");
                labCode = json.getString("LabCode");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        rv_healthscreeninglist.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        HealthScreeningListModel element = healthScreeningList.get(position);
                        if (element.getClassName() != null) {

                            ConstantData constantData = ConstantData.getInstance();
                            constantData.setCalendarCampId(campId);


//                            if (element.getType().equals("10")) {
//                                startActivity(new Intent(context, HealthScreeningRationCard_Activity.class)
//                                        .putExtra("healthScreentype", element.getType())
//                                        .putExtra("campId", campId));
//
//                                return;
//
//                            }
//
//
//                            if (element.getType().equals("9")) {
//                                startActivity(new Intent(context, HealthScreeningAcknowledgementConfirmation_Activity.class)
//                                        .putExtra("healthScreentype", element.getType())
//                                        .putExtra("campId", campId));
//
//                                return;
//
//                            }


                            if (element.getMenuName().equals("Screening Status")) {
                                context.startActivity(new Intent(context, CampDetails_Activity.class)
                                        .putExtra("Date", getIntent().getStringExtra("Date"))
                                        .putExtra("DISTLGDCODE", getIntent().getStringExtra("DISTLGDCODE"))
                                        .putExtra("campDetails", getIntent().getSerializableExtra("campDetails"))
                                        .putExtra("campId", campId)
                                        .putExtra("Type", "2"));
                            } else if (element.getMenuName().equals("Camp Closing")) {
                                context.startActivity(new Intent(context, CampClosingActivity.class)
                                        .putExtra("Date", getIntent().getStringExtra("Date"))
                                        .putExtra("campId", campId)
                                        .putExtra("DISTLGDCODE", getIntent().getStringExtra("DISTLGDCODE"))
                                        .putExtra("Type", "2"));
                            } else if (element.getMenuName().equals("Sample Processing Dashboard")) {
                                context.startActivity(new Intent(context, CampSampleProcessing_Activity.class)
                                        .putExtra("campId", campId));
                            } else if (element.getMenuName().equals("Blood Sugar (PP)")) {
                                Utilities.showMessageString("Blood Sugar PP is not in operation", context);
                            } else {
                                Log.d("TAG", "onItemClick: " + session.getUserDetailsJson().getDesgid());
                                if (session.isHllUser() && session.getUserDetailsJson().getDesgid() == 35 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 86 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 146 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 129 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 138 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 137 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 176 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 31 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 169 && element.getType().equalsIgnoreCase("16")
                                        || session.isHllUser() && session.getUserDetailsJson().getDesgid() == 177 && element.getType().equalsIgnoreCase("16")) {
                                    startActivity(new Intent(context, D2DAttendanceMarkedPatients_Activity.class)
                                            .putExtra("healthScreentype", element.getType())
                                            .putExtra("campId", campId));

                                } else {

                                    heathscreeningType = element.getType();


                                    startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
                                            .putExtra("healthScreentype", element.getType())
                                            .putExtra("campId", campId));


//                                    if (CampType.equals("1") || CampType.equals("2")) {
//
//                                        if (Utilities.isNetworkAvailable(context)) {
//                                            new GetTestMap().execute(campId, userId, element.getType());
//
//                                        } else {
//                                            Utilities.showAlertDialog(context, "Alert", "Please check internet connection", false);
//                                        }
//                                    } else {
//
//                                        startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
//                                                .putExtra("healthScreentype", element.getType())
//                                                .putExtra("campId", campId));
//
//                                    }


//                                    if (Utilities.isNetworkAvailable(context)){
//                                        new GetTestMap().execute(campId,userId,element.getType());
//
//                                    }else {
//                                        Utilities.showAlertDialog(context,"Alert","Please check internet connection",false);
//                                    }


                                }
                            }
                        } else {
                            Utilities.showToastMessage("Coming Soon...", context, false);
                        }
                    }
                })
        );
    }


    public class GetTestMap extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("ResourceUserId", params[1]));
            param.add(new ParamsPojo("TestId", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetTestMappedtoUser, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // campList = new ArrayList<>();
                    // selectedCampList = new ArrayList<>();
                    List<HomeLabHublabOnLandingLabModel.Output> campList = new ArrayList<>();

                    HomeLabHublabOnLandingLabModel homeLabHublabOnLandingLabModel = new Gson().fromJson(result, HomeLabHublabOnLandingLabModel.class);
                    type = homeLabHublabOnLandingLabModel.getStatus();
                    message = homeLabHublabOnLandingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (homeLabHublabOnLandingLabModel.getOutput().size() > 0) {
                            HomeLabHublabOnLandingLabModel.Output output = homeLabHublabOnLandingLabModel.getOutput().get(0);

                            isTestMapped = output.getTestId();

                            if (isTestMapped != null) {
                                if (isTestMapped.equalsIgnoreCase("0")) {
                                    Utilities.showAlertDialog(context, "Alert", "You are not mapped for this test", false);
                                    return;
                                } else {
                                    startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
                                            .putExtra("healthScreentype", heathscreeningType)
                                            .putExtra("campId", campId));
                                }
                            }


//                            if (isTestMapped.equalsIgnoreCase("0")) {
//                                Utilities.showAlertDialog(context, "Alert", "You are not mapped for this test", false);
//                                return;
//                            }else {
//
//                            }


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

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Health Screening");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
