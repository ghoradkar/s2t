package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.MasterModel;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.models.RemidioModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HealthScreeningVisualTest_Activity_Old extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private MaterialEditText edt_beneficiaryname, edt_gender,edt_jaegar_chart_right,edt_jaegar_chart_left, edt_age, edt_injury_right, edt_injury_left, edt_eyecolor_right, edt_eyecolor_left,
            edt_remark, edt_right_remark, edt_left_remark, edt_snellen_right, edt_snellen_left, edt_jaegar_chart, edt_height, edt_weight, edt_near_remark;
    private CheckBox cb_right, cb_left, cb_both;
    private RadioButton rb_glasses_yes, rb_glasses_no, rbSnellenChart, rbMachine;
    private RadioGroup rgTestType;
    private Button btn_register, btnSubmitMachineData;
    private ImageButton btnScanQRCOde;
    private PresentPatientList_Model patientDetails;
    private String userID, name, campId, rigthEyeColorId = "0", leftEyeColorId = "0", rigthDiseaseId = "0", leftDiseaseId = "0",
            remarkId, suggestionId, healthScreentype, glassesId;

    private LinearLayoutCompat llMachine, llMain, llScanner, llChart;

    private MaterialEditText edt_p1actual, edt_p2actual, edt_p3actual, edt_p4actual, edt_p5actual, edt_p6actual, edt_p7actual, edt_p8actual, edt_p9actual, edt_p10actual, edt_p11actual, edt_p12actual,
            edt_p13actual, edt_p14actual, edt_p15actual, edt_p16actual, edt_p17actual, edt_p18actual, edt_p19actual, edt_p20actual, edt_p21actual, edt_p22actual, edt_p23actual, edt_p24actual;
    private MaterialEditText edt_p1reported, edt_p2reported, edt_p3reported, edt_p4reported, edt_p5reported, edt_p6reported, edt_p7reported, edt_p8reported, edt_p9reported, edt_p10reported, edt_p11reported, edt_p12reported,
            edt_p13reported, edt_p14reported, edt_p15reported, edt_p16reported, edt_p17reported, edt_p18reported, edt_p19reported, edt_p20reported, edt_p21reported, edt_p22reported, edt_p23reported, edt_p24reported;
    private MaterialEditText edt_p1conclusion, edt_p2conclusion, edt_p3conclusion, edt_p4conclusion, edt_p5conclusion, edt_p6conclusion, edt_p7conclusion, edt_p8conclusion, edt_p9conclusion, edt_p10conclusion, edt_p11conclusion, edt_p12conclusion,
            edt_p13conclusion, edt_p14conclusion, edt_p15conclusion, edt_p16conclusion, edt_p17conclusion, edt_p18conclusion, edt_p19conclusion, edt_p20conclusion, edt_p21conclusion, edt_p22conclusion, edt_p23conclusion, edt_p24conclusion;

    private TextView tvSRight, tvSLeft, tvCRight, tvCLeft, tvARight, tvALeft, tvRescan;
    private RemidioModel remidioModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthscreening_visualtest);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
        setVisionEventHandlers();
    }

    private void init() {
        context = HealthScreeningVisualTest_Activity_Old.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_beneficiaryname = findViewById(R.id.edt_beneficiaryname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_age = findViewById(R.id.edt_age);
        edt_injury_right = findViewById(R.id.edt_injury_right);
        edt_injury_left = findViewById(R.id.edt_injury_left);
        edt_eyecolor_right = findViewById(R.id.edt_eyecolor_right);
        edt_eyecolor_left = findViewById(R.id.edt_eyecolor_left);
        edt_remark = findViewById(R.id.edt_remark);
        edt_right_remark = findViewById(R.id.edt_right_remark);
        edt_left_remark = findViewById(R.id.edt_left_remark);
        edt_snellen_right = findViewById(R.id.edt_snellen_right);
        edt_snellen_left = findViewById(R.id.edt_snellen_left);
        edt_jaegar_chart_right = findViewById(R.id.edt_jaegar_chart_right);
        edt_jaegar_chart_left = findViewById(R.id.edt_jaegar_chart_left);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        edt_near_remark = findViewById(R.id.edt_near_remark);

        cb_right = findViewById(R.id.cb_right);
        cb_left = findViewById(R.id.cb_left);
        cb_both = findViewById(R.id.cb_both);
        rb_glasses_yes = findViewById(R.id.rb_glasses_yes);
        rb_glasses_no = findViewById(R.id.rb_glasses_no);

        rgTestType = findViewById(R.id.rgTestType);
        rbSnellenChart = findViewById(R.id.rbSnellenChart);
        rbMachine = findViewById(R.id.rbMachine);

        tvSRight = findViewById(R.id.tvSRight);
        tvSLeft = findViewById(R.id.tvSLeft);
        tvCRight = findViewById(R.id.tvCRight);
        tvCLeft = findViewById(R.id.tvCLeft);
        tvARight = findViewById(R.id.tvARight);
        tvALeft = findViewById(R.id.tvALeft);
        tvRescan = findViewById(R.id.tvRescan);

        btnScanQRCOde = findViewById(R.id.btnScanQRCOde);
        btnSubmitMachineData = findViewById(R.id.btnSubmitMachineData);

        llMachine = findViewById(R.id.llMachine);
        llMain = findViewById(R.id.llMain);
        llScanner = findViewById(R.id.llScanner);
        llChart = findViewById(R.id.llChart);

        btn_register = findViewById(R.id.btn_register);

        edt_p1actual = findViewById(R.id.edt_p1actual);
        edt_p2actual = findViewById(R.id.edt_p2actual);
        edt_p3actual = findViewById(R.id.edt_p3actual);
        edt_p4actual = findViewById(R.id.edt_p4actual);
        edt_p5actual = findViewById(R.id.edt_p5actual);
        edt_p6actual = findViewById(R.id.edt_p6actual);
        edt_p7actual = findViewById(R.id.edt_p7actual);
        edt_p8actual = findViewById(R.id.edt_p8actual);
        edt_p9actual = findViewById(R.id.edt_p9actual);
        edt_p10actual = findViewById(R.id.edt_p10actual);
        edt_p11actual = findViewById(R.id.edt_p11actual);
        edt_p12actual = findViewById(R.id.edt_p12actual);
        edt_p13actual = findViewById(R.id.edt_p13actual);
        edt_p14actual = findViewById(R.id.edt_p14actual);
        edt_p15actual = findViewById(R.id.edt_p15actual);
        edt_p16actual = findViewById(R.id.edt_p16actual);
        edt_p17actual = findViewById(R.id.edt_p17actual);
        edt_p18actual = findViewById(R.id.edt_p18actual);
        edt_p19actual = findViewById(R.id.edt_p19actual);
        edt_p20actual = findViewById(R.id.edt_p20actual);
        edt_p21actual = findViewById(R.id.edt_p21actual);
        edt_p22actual = findViewById(R.id.edt_p22actual);
        edt_p23actual = findViewById(R.id.edt_p23actual);
        edt_p24actual = findViewById(R.id.edt_p24actual);
        edt_p1reported = findViewById(R.id.edt_p1reported);
        edt_p2reported = findViewById(R.id.edt_p2reported);
        edt_p3reported = findViewById(R.id.edt_p3reported);
        edt_p4reported = findViewById(R.id.edt_p4reported);
        edt_p5reported = findViewById(R.id.edt_p5reported);
        edt_p6reported = findViewById(R.id.edt_p6reported);
        edt_p7reported = findViewById(R.id.edt_p7reported);
        edt_p8reported = findViewById(R.id.edt_p8reported);
        edt_p9reported = findViewById(R.id.edt_p9reported);
        edt_p10reported = findViewById(R.id.edt_p10reported);
        edt_p11reported = findViewById(R.id.edt_p11reported);
        edt_p12reported = findViewById(R.id.edt_p12reported);
        edt_p13reported = findViewById(R.id.edt_p13reported);
        edt_p14reported = findViewById(R.id.edt_p14reported);
        edt_p15reported = findViewById(R.id.edt_p15reported);
        edt_p16reported = findViewById(R.id.edt_p16reported);
        edt_p17reported = findViewById(R.id.edt_p17reported);
        edt_p18reported = findViewById(R.id.edt_p18reported);
        edt_p19reported = findViewById(R.id.edt_p19reported);
        edt_p20reported = findViewById(R.id.edt_p20reported);
        edt_p21reported = findViewById(R.id.edt_p21reported);
        edt_p22reported = findViewById(R.id.edt_p22reported);
        edt_p23reported = findViewById(R.id.edt_p23reported);
        edt_p24reported = findViewById(R.id.edt_p24reported);
        edt_p1conclusion = findViewById(R.id.edt_p1conclusion);
        edt_p2conclusion = findViewById(R.id.edt_p2conclusion);
        edt_p3conclusion = findViewById(R.id.edt_p3conclusion);
        edt_p4conclusion = findViewById(R.id.edt_p4conclusion);
        edt_p5conclusion = findViewById(R.id.edt_p5conclusion);
        edt_p6conclusion = findViewById(R.id.edt_p6conclusion);
        edt_p7conclusion = findViewById(R.id.edt_p7conclusion);
        edt_p8conclusion = findViewById(R.id.edt_p8conclusion);
        edt_p9conclusion = findViewById(R.id.edt_p9conclusion);
        edt_p10conclusion = findViewById(R.id.edt_p10conclusion);
        edt_p11conclusion = findViewById(R.id.edt_p11conclusion);
        edt_p12conclusion = findViewById(R.id.edt_p12conclusion);
        edt_p13conclusion = findViewById(R.id.edt_p13conclusion);
        edt_p14conclusion = findViewById(R.id.edt_p14conclusion);
        edt_p15conclusion = findViewById(R.id.edt_p15conclusion);
        edt_p16conclusion = findViewById(R.id.edt_p16conclusion);
        edt_p17conclusion = findViewById(R.id.edt_p17conclusion);
        edt_p18conclusion = findViewById(R.id.edt_p18conclusion);
        edt_p19conclusion = findViewById(R.id.edt_p19conclusion);
        edt_p20conclusion = findViewById(R.id.edt_p20conclusion);
        edt_p21conclusion = findViewById(R.id.edt_p21conclusion);
        edt_p22conclusion = findViewById(R.id.edt_p22conclusion);
        edt_p23conclusion = findViewById(R.id.edt_p23conclusion);
        edt_p24conclusion = findViewById(R.id.edt_p24conclusion);

    }

    private void setDefaults() {
        patientDetails = (PresentPatientList_Model) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");
        healthScreentype = getIntent().getStringExtra("healthScreentype");

        edt_beneficiaryname.setText(patientDetails.getEnglishName());
        edt_age.setText(String.valueOf(patientDetails.getAge()));
        edt_height.setText(String.valueOf(patientDetails.getHeightCMs()));
        edt_weight.setText(String.valueOf(patientDetails.getWeightKGs()));

        if (patientDetails.getGender().equalsIgnoreCase("M")) {
            edt_gender.setText("Male");
        } else if (patientDetails.getGender().equalsIgnoreCase("F")) {
            edt_gender.setText("Female");
        } else if (patientDetails.getGender().equalsIgnoreCase("O")) {
            edt_gender.setText("Other");
        }
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
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

        rgTestType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rbMachine.getId()) {
                    llMain.setVisibility(View.GONE);
                    llMachine.setVisibility(View.VISIBLE);
                } else {
                    llMain.setVisibility(View.VISIBLE);
                    llMachine.setVisibility(View.GONE);
                }
            }
        });

        btnScanQRCOde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llChart.setVisibility(View.GONE);
                btnSubmitMachineData.setVisibility(View.GONE);

                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);


            }
        });
        tvRescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llChart.setVisibility(View.GONE);
                btnSubmitMachineData.setVisibility(View.GONE);

                Intent i = new Intent(context, BarcodeScanner_Activity.class);
                startActivityForResult(i, 10001);


            }
        });

        edt_injury_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> diseaseInjury = new ArrayList<>();

                diseaseInjury.add(new MasterModel("1", "Normal"));
//                diseaseInjury.add(new MasterModel("2", "Ocular Injuries"));
//                diseaseInjury.add(new MasterModel("3", "Foreign Bodies"));
//                diseaseInjury.add(new MasterModel("4", "Chemical and radiation injuries"));
//                diseaseInjury.add(new MasterModel("5", "Complications of systemic disease"));
//                diseaseInjury.add(new MasterModel("6", "Cataract Operation"));
                diseaseInjury.add(new MasterModel("7", "Any Injury"));
                diseaseInjury.add(new MasterModel("8", "Any Foreign Body"));
                diseaseInjury.add(new MasterModel("9", "Cataract"));
                diseaseInjury.add(new MasterModel("9", "Any Operation"));
                diseaseInjury.add(new MasterModel("10", "Other"));
                showDiseaseInjuryListDialog(diseaseInjury, "1");
            }
        });

        edt_injury_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> diseaseInjury = new ArrayList<>();

                diseaseInjury.add(new MasterModel("1", "Normal"));
//                diseaseInjury.add(new MasterModel("2", "Ocular Injuries"));
//                diseaseInjury.add(new MasterModel("3", "Foreign Bodies"));
//                diseaseInjury.add(new MasterModel("4", "Chemical and radiation injuries"));
//                diseaseInjury.add(new MasterModel("5", "Complications of systemic disease"));
//                diseaseInjury.add(new MasterModel("6", "Cataract Operation"));
                diseaseInjury.add(new MasterModel("7", "Any Injury"));
                diseaseInjury.add(new MasterModel("8", "Any Foreign Body"));
                diseaseInjury.add(new MasterModel("9", "Cataract"));
                diseaseInjury.add(new MasterModel("9", "Any Operation"));
                diseaseInjury.add(new MasterModel("10", "Other"));
                showDiseaseInjuryListDialog(diseaseInjury, "2");
            }
        });

        edt_eyecolor_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> eyeColorList = new ArrayList<>();
                eyeColorList.add(new MasterModel("1", "Normal Vision"));
                eyeColorList.add(new MasterModel("2", "Color Blind"));
                eyeColorList.add(new MasterModel("3", "Green & Red Color Blind"));
                eyeColorList.add(new MasterModel("4", "Green & Brown Color Blind"));
                eyeColorList.add(new MasterModel("5", "Blue & Purple Color Blind"));
                eyeColorList.add(new MasterModel("6", "Green & Blue Color Blind"));
                eyeColorList.add(new MasterModel("7", "Light Green & Yellow Color Blind"));
                eyeColorList.add(new MasterModel("8", "Blue & Grey Color Blind"));
                eyeColorList.add(new MasterModel("9", "Green & Grey Color Blind"));
                eyeColorList.add(new MasterModel("10", "Green & Black Color Blind"));
                showEyeColorListDialog(eyeColorList, "1");
            }
        });

        edt_eyecolor_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> eyeColorList = new ArrayList<>();
                eyeColorList.add(new MasterModel("1", "Normal Vision"));
                eyeColorList.add(new MasterModel("2", "Color Blind"));
                eyeColorList.add(new MasterModel("3", "Green & Red Color Blind"));
                eyeColorList.add(new MasterModel("4", "Green & Brown Color Blind"));
                eyeColorList.add(new MasterModel("5", "Blue & Purple Color Blind"));
                eyeColorList.add(new MasterModel("6", "Green & Blue Color Blind"));
                eyeColorList.add(new MasterModel("7", "Light Green & Yellow Color Blind"));
                eyeColorList.add(new MasterModel("8", "Blue & Grey Color Blind"));
                eyeColorList.add(new MasterModel("9", "Green & Grey Color Blind"));
                eyeColorList.add(new MasterModel("10", "Green & Black Color Blind"));
                showEyeColorListDialog(eyeColorList, "2");
            }
        });

        edt_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> remarkList = new ArrayList<>();

                remarkList.add(new MasterModel("1", "Normal vision"));
                remarkList.add(new MasterModel("2", "Red color blindness"));
                remarkList.add(new MasterModel("3", "Green color blindness"));
                remarkList.add(new MasterModel("4", "Complete color blindness"));
                showRemarkListDialog(remarkList);
            }
        });

        edt_snellen_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> snellenList = new ArrayList<>();
//                snellenList.add(new MasterModel("1", " 20/200"));
//                snellenList.add(new MasterModel("2", " 20/100"));
//                snellenList.add(new MasterModel("3", " 20/80"));
//                snellenList.add(new MasterModel("4", " 20/63"));
//                snellenList.add(new MasterModel("5", " 20/50"));
//                snellenList.add(new MasterModel("6", " 20/40"));
//                snellenList.add(new MasterModel("7", " 20/32"));
//                snellenList.add(new MasterModel("8", " 20/25"));
//                snellenList.add(new MasterModel("9", " 20/20"));
                snellenList.add(new MasterModel("1", " 6/60"));
                snellenList.add(new MasterModel("2", " 6/36"));
                snellenList.add(new MasterModel("3", " 6/24"));
                snellenList.add(new MasterModel("4", " 6/18"));
                snellenList.add(new MasterModel("5", " 6/12"));
                snellenList.add(new MasterModel("6", " 6/9"));
                snellenList.add(new MasterModel("7", " 6/6"));
                showSnellenChartListDialog(snellenList, "1");
            }
        });

        edt_snellen_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> snellenList = new ArrayList<>();
                snellenList.add(new MasterModel("1", " 6/60"));
                snellenList.add(new MasterModel("2", " 6/36"));
                snellenList.add(new MasterModel("3", " 6/24"));
                snellenList.add(new MasterModel("4", " 6/18"));
                snellenList.add(new MasterModel("5", " 6/12"));
                snellenList.add(new MasterModel("6", " 6/9"));
                snellenList.add(new MasterModel("7", " 6/6"));
                showSnellenChartListDialog(snellenList, "2");
            }
        });

        edt_jaegar_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_snellen_right.getText().toString().trim().isEmpty()) {
                    edt_snellen_right.setError("Please select this field");
                    edt_snellen_right.requestFocus();
                    return;
                }

                if (edt_snellen_left.getText().toString().trim().isEmpty()) {
                    edt_snellen_left.setError("Please select this field");
                    edt_snellen_left.requestFocus();
                    return;
                }

                ArrayList<MasterModel> jaegarchartList = new ArrayList<>();
                jaegarchartList.add(new MasterModel("1", "N5"));
                jaegarchartList.add(new MasterModel("2", "N6"));
                jaegarchartList.add(new MasterModel("3", "N8"));
                jaegarchartList.add(new MasterModel("4", "N10"));
                jaegarchartList.add(new MasterModel("5", "N12"));
                jaegarchartList.add(new MasterModel("6", "N18"));
                showJaegarChartListDialog(jaegarchartList);
            }
        });

        edt_right_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_snellen_right.getText().toString().trim().isEmpty()) {
                    edt_snellen_right.setError("Please select this field");
                    edt_snellen_right.requestFocus();
                    return;
                }
//                ArrayList<MasterModel> suggestionList = new ArrayList<>();
//                suggestionList.add(new MasterModel("1", "Normal Vision"));
//                suggestionList.add(new MasterModel("2", "To be referred to ophthalmologist"));
//                showSuggestionListDialog(suggestionList, "1");
            }
        });

        edt_left_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<MasterModel> suggestionList = new ArrayList<>();
//                suggestionList.add(new MasterModel("1", "Normal Vision"));
//                suggestionList.add(new MasterModel("2", "To be referred to ophthalmologist"));
//                showSuggestionListDialog(suggestionList, "2");
                if (edt_snellen_left.getText().toString().trim().isEmpty()) {
                    edt_snellen_left.setError("Please select this field");
                    edt_snellen_left.requestFocus();
                    return;
                }
            }
        });

        edt_near_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MasterModel> suggestionList = new ArrayList<>();
                suggestionList.add(new MasterModel("1", "Normal Vision"));
                suggestionList.add(new MasterModel("2", "To be referred to ophthalmologist"));
                showSuggestionListDialog(suggestionList, "3");
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
        btnSubmitMachineData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remidioModel != null) {
                    new InsertEyeScreeningMachineData().execute(new Gson().toJson(remidioModel));
                }

//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setIcon(R.drawable.icon_success);
//                builder.setTitle("Success");
//                builder.setCancelable(false);
//                builder.setMessage("Visual screening test details submitted successfully");
//                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
//                builder.show();
            }
        });
    }

    private void showDiseaseInjuryListDialog(final ArrayList<MasterModel> pipeTypeList, final String type) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        if (type.equals("1")) {
            builderSingle.setTitle("Evidence of Disease or Injury Right Eye");
        } else if (type.equals("2")) {
            builderSingle.setTitle("Evidence of Disease or Injury Left Eye");
        }
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel subTrenchModel : pipeTypeList) {
            arrayAdapter.add(subTrenchModel.getName());
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
                if (type.equals("1")) {
                    rigthDiseaseId = pipeTypeList.get(which).getId();
                    edt_injury_right.setText(pipeTypeList.get(which).getName());
                } else if (type.equals("2")) {
                    leftDiseaseId = pipeTypeList.get(which).getId();
                    edt_injury_left.setText(pipeTypeList.get(which).getName());
                }
            }
        });
        builderSingle.show();
    }

    private void showEyeColorListDialog(final ArrayList<MasterModel> pipeTypeList, final String type) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        if (type.equals("1")) {
            builderSingle.setTitle("Right Eye Color Test");
        } else if (type.equals("2")) {
            builderSingle.setTitle("Left Eye Color Test");
        }
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel subTrenchModel : pipeTypeList) {
            arrayAdapter.add(subTrenchModel.getName());
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
                if (type.equals("1")) {
                    rigthEyeColorId = pipeTypeList.get(which).getId();
                    edt_eyecolor_right.setText(pipeTypeList.get(which).getName());
                } else if (type.equals("2")) {
                    leftEyeColorId = pipeTypeList.get(which).getId();
                    edt_eyecolor_left.setText(pipeTypeList.get(which).getName());
                }
            }
        });
        builderSingle.show();
    }

    private void showSnellenChartListDialog(final ArrayList<MasterModel> pipeTypeList, final String type) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        if (type.equals("1")) {
            builderSingle.setTitle("Right Eye Snellen Chart");
        } else if (type.equals("2")) {
            builderSingle.setTitle("Left Eye Snellen Chart");
        }
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel subTrenchModel : pipeTypeList) {
            arrayAdapter.add(subTrenchModel.getName());
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
                if (type.equals("1")) {
                    edt_snellen_right.setText(pipeTypeList.get(which).getName());
                    if (edt_snellen_right.getText().toString().trim().equals("6/6")) {
                        edt_right_remark.setText("Normal Vision");
                    } else {
                        edt_right_remark.setText("To be referred to ophthalmologist");
                    }
                } else if (type.equals("2")) {
                    edt_snellen_left.setText(pipeTypeList.get(which).getName());
                    if (edt_snellen_left.getText().toString().trim().equals("6/6")) {
                        edt_left_remark.setText("Normal Vision");
                    } else {
                        edt_left_remark.setText("To be referred to ophthalmologist");
                    }
                }
            }
        });
        builderSingle.show();
    }

    private void showRemarkListDialog(final ArrayList<MasterModel> pipeTypeList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Remark");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel subTrenchModel : pipeTypeList) {
            arrayAdapter.add(subTrenchModel.getName());
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
                remarkId = pipeTypeList.get(which).getId();
                edt_remark.setText(pipeTypeList.get(which).getName());
            }
        });
        builderSingle.show();
    }

    private void showSuggestionListDialog(final ArrayList<MasterModel> pipeTypeList, String type) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        if (type.equals("1")) {
            builderSingle.setTitle("Right Eye Remark");
        } else if (type.equals("2")) {
            builderSingle.setTitle("Left Eye Remark");
        } else if (type.equals("3")) {
            builderSingle.setTitle("Near Vision Remark");
        }

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel subTrenchModel : pipeTypeList) {
            arrayAdapter.add(subTrenchModel.getName());
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
                if (type.equals("1")) {
                    edt_right_remark.setText(pipeTypeList.get(which).getName());
                } else if (type.equals("2")) {
                    edt_left_remark.setText(pipeTypeList.get(which).getName());
                } else if (type.equals("3")) {
                    edt_near_remark.setText(pipeTypeList.get(which).getName());
                }
            }
        });
        builderSingle.show();
    }

    private void showJaegarChartListDialog(final ArrayList<MasterModel> pipeTypeList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        builderSingle.setTitle("Select Near Vision Chart Report");

        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (MasterModel subTrenchModel : pipeTypeList) {
            arrayAdapter.add(subTrenchModel.getName());
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
                edt_jaegar_chart.setText(pipeTypeList.get(which).getName());

                if (Integer.parseInt(pipeTypeList.get(which).getId()) <= 3) {
                    edt_near_remark.setText("Normal Vision");
                } else {
                    edt_near_remark.setText("To be referred to ophthalmologist");
                }
            }
        });
        builderSingle.show();
    }

    private void submitData() {
        String forDistanceRight = "0", forDistanceLeft = "0", forDistanceBoth = "0";

        if (cb_right.isChecked()) {
            forDistanceRight = "1";
        }

        if (cb_left.isChecked()) {
            forDistanceLeft = "1";
        }

        if (cb_both.isChecked()) {
            forDistanceBoth = "1";
        }

        if (edt_injury_right.getText().toString().trim().isEmpty()) {
            edt_injury_right.setError("Please select this field");
            edt_injury_right.requestFocus();
            return;
        }

        if (edt_injury_left.getText().toString().trim().isEmpty()) {
            edt_injury_left.setError("Please select this field");
            edt_injury_left.requestFocus();
            return;
        }

        if (edt_snellen_right.getText().toString().trim().isEmpty()) {
            edt_snellen_right.setError("Please select this field");
            edt_snellen_right.requestFocus();
            return;
        }

        if (edt_snellen_left.getText().toString().trim().isEmpty()) {
            edt_snellen_left.setError("Please select this field");
            edt_snellen_left.requestFocus();
            return;
        }

        if (edt_jaegar_chart.getText().toString().trim().isEmpty()) {
            edt_jaegar_chart.setError("Please select this field");
            edt_jaegar_chart.requestFocus();
            return;
        }

        if (rb_glasses_yes.isChecked()) {
            glassesId = "1";
        } else if (rb_glasses_no.isChecked()) {
            glassesId = "0";
        }

        if (edt_right_remark.getText().toString().trim().isEmpty()) {
            edt_right_remark.setError("Please select this field");
            edt_right_remark.requestFocus();
            return;
        }

        if (edt_left_remark.getText().toString().trim().isEmpty()) {
            edt_left_remark.setError("Please select this field");
            edt_left_remark.requestFocus();
            return;
        }

        if (edt_near_remark.getText().toString().trim().isEmpty()) {
            edt_near_remark.setError("Please select this field");
            edt_near_remark.requestFocus();
            return;
        }

        JsonArray plateArray = new JsonArray();

//        JsonObject plateObj1 = new JsonObject();
//        plateObj1.addProperty("RegID", patientDetails.getRegdId());
//        plateObj1.addProperty("PalatNo", "1");
//        plateObj1.addProperty("ReportedNo", edt_p1reported.getText().toString().trim());
//        plateObj1.addProperty("Conclusion", edt_p1conclusion.getText().toString().trim());
//        plateObj1.addProperty("Distance_R_P", "1");
//        plateObj1.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj1);
//
//        JsonObject plateObj2 = new JsonObject();
//        plateObj2.addProperty("RegID", patientDetails.getRegdId());
//        plateObj2.addProperty("PalatNo", "2");
//        plateObj2.addProperty("ReportedNo", edt_p2reported.getText().toString().trim());
//        plateObj2.addProperty("Conclusion", edt_p2conclusion.getText().toString().trim());
//        plateObj2.addProperty("Distance_R_P", "1");
//        plateObj2.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj2);
//
//
//        JsonObject plateObj3 = new JsonObject();
//        plateObj3.addProperty("RegID", patientDetails.getRegdId());
//        plateObj3.addProperty("PalatNo", "3");
//        plateObj3.addProperty("ReportedNo", edt_p3reported.getText().toString().trim());
//        plateObj3.addProperty("Conclusion", edt_p3conclusion.getText().toString().trim());
//        plateObj3.addProperty("Distance_R_P", "1");
//        plateObj3.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj3);
//
//        JsonObject plateObj4 = new JsonObject();
//        plateObj4.addProperty("RegID", patientDetails.getRegdId());
//        plateObj4.addProperty("PalatNo", "4");
//        plateObj4.addProperty("ReportedNo", edt_p4reported.getText().toString().trim());
//        plateObj4.addProperty("Conclusion", edt_p4conclusion.getText().toString().trim());
//        plateObj4.addProperty("Distance_R_P", "1");
//        plateObj4.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj4);
//
//        JsonObject plateObj5 = new JsonObject();
//        plateObj5.addProperty("RegID", patientDetails.getRegdId());
//        plateObj5.addProperty("PalatNo", "5");
//        plateObj5.addProperty("ReportedNo", edt_p5reported.getText().toString().trim());
//        plateObj5.addProperty("Conclusion", edt_p5conclusion.getText().toString().trim());
//        plateObj5.addProperty("Distance_R_P", "1");
//        plateObj5.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj5);
//
//        JsonObject plateObj6 = new JsonObject();
//        plateObj6.addProperty("RegID", patientDetails.getRegdId());
//        plateObj6.addProperty("PalatNo", "6");
//        plateObj6.addProperty("ReportedNo", edt_p6reported.getText().toString().trim());
//        plateObj6.addProperty("Conclusion", edt_p6conclusion.getText().toString().trim());
//        plateObj6.addProperty("Distance_R_P", "1");
//        plateObj6.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj6);
//
//        JsonObject plateObj7 = new JsonObject();
//        plateObj7.addProperty("RegID", patientDetails.getRegdId());
//        plateObj7.addProperty("PalatNo", "7");
//        plateObj7.addProperty("ReportedNo", edt_p7reported.getText().toString().trim());
//        plateObj7.addProperty("Conclusion", edt_p7conclusion.getText().toString().trim());
//        plateObj7.addProperty("Distance_R_P", "1");
//        plateObj7.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj7);
//
//        JsonObject plateObj8 = new JsonObject();
//        plateObj8.addProperty("RegID", patientDetails.getRegdId());
//        plateObj8.addProperty("PalatNo", "8");
//        plateObj8.addProperty("ReportedNo", edt_p8reported.getText().toString().trim());
//        plateObj8.addProperty("Conclusion", edt_p8conclusion.getText().toString().trim());
//        plateObj8.addProperty("Distance_R_P", "1");
//        plateObj8.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj8);
//
//        JsonObject plateObj9 = new JsonObject();
//        plateObj9.addProperty("RegID", patientDetails.getRegdId());
//        plateObj9.addProperty("PalatNo", "9");
//        plateObj9.addProperty("ReportedNo", edt_p9reported.getText().toString().trim());
//        plateObj9.addProperty("Conclusion", edt_p9conclusion.getText().toString().trim());
//        plateObj9.addProperty("Distance_R_P", "1");
//        plateObj9.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj9);
//
//        JsonObject plateObj10 = new JsonObject();
//        plateObj10.addProperty("RegID", patientDetails.getRegdId());
//        plateObj10.addProperty("PalatNo", "10");
//        plateObj10.addProperty("ReportedNo", edt_p10reported.getText().toString().trim());
//        plateObj10.addProperty("Conclusion", edt_p10conclusion.getText().toString().trim());
//        plateObj10.addProperty("Distance_R_P", "1");
//        plateObj10.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj10);
//
//        JsonObject plateObj11 = new JsonObject();
//        plateObj11.addProperty("RegID", patientDetails.getRegdId());
//        plateObj11.addProperty("PalatNo", "11");
//        plateObj11.addProperty("ReportedNo", edt_p11reported.getText().toString().trim());
//        plateObj11.addProperty("Conclusion", edt_p11conclusion.getText().toString().trim());
//        plateObj11.addProperty("Distance_R_P", "1");
//        plateObj11.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj11);
//
//        JsonObject plateObj12 = new JsonObject();
//        plateObj12.addProperty("RegID", patientDetails.getRegdId());
//        plateObj12.addProperty("PalatNo", "12");
//        plateObj12.addProperty("ReportedNo", edt_p12reported.getText().toString().trim());
//        plateObj12.addProperty("Conclusion", edt_p12conclusion.getText().toString().trim());
//        plateObj12.addProperty("Distance_R_P", "1");
//        plateObj12.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj12);
//
//        JsonObject plateObj13 = new JsonObject();
//        plateObj13.addProperty("RegID", patientDetails.getRegdId());
//        plateObj13.addProperty("PalatNo", "13");
//        plateObj13.addProperty("ReportedNo", edt_p13reported.getText().toString().trim());
//        plateObj13.addProperty("Conclusion", edt_p13conclusion.getText().toString().trim());
//        plateObj13.addProperty("Distance_R_P", "1");
//        plateObj13.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj13);
//
//        JsonObject plateObj14 = new JsonObject();
//        plateObj14.addProperty("RegID", patientDetails.getRegdId());
//        plateObj14.addProperty("PalatNo", "14");
//        plateObj14.addProperty("ReportedNo", edt_p14reported.getText().toString().trim());
//        plateObj14.addProperty("Conclusion", edt_p14conclusion.getText().toString().trim());
//        plateObj14.addProperty("Distance_R_P", "1");
//        plateObj14.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj14);
//
//        JsonObject plateObj15 = new JsonObject();
//        plateObj15.addProperty("RegID", patientDetails.getRegdId());
//        plateObj15.addProperty("PalatNo", "15");
//        plateObj15.addProperty("ReportedNo", edt_p15reported.getText().toString().trim());
//        plateObj15.addProperty("Conclusion", edt_p15conclusion.getText().toString().trim());
//        plateObj15.addProperty("Distance_R_P", "1");
//        plateObj15.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj15);
//
//        JsonObject plateObj16 = new JsonObject();
//        plateObj16.addProperty("RegID", patientDetails.getRegdId());
//        plateObj16.addProperty("PalatNo", "16");
//        plateObj16.addProperty("ReportedNo", edt_p16reported.getText().toString().trim());
//        plateObj16.addProperty("Conclusion", edt_p16conclusion.getText().toString().trim());
//        plateObj16.addProperty("Distance_R_P", "1");
//        plateObj16.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj16);
//
//        JsonObject plateObj17 = new JsonObject();
//        plateObj17.addProperty("RegID", patientDetails.getRegdId());
//        plateObj17.addProperty("PalatNo", "17");
//        plateObj17.addProperty("ReportedNo", edt_p17reported.getText().toString().trim());
//        plateObj17.addProperty("Conclusion", edt_p17conclusion.getText().toString().trim());
//        plateObj17.addProperty("Distance_R_P", "1");
//        plateObj17.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj17);
//
//        JsonObject plateObj18 = new JsonObject();
//        plateObj18.addProperty("RegID", patientDetails.getRegdId());
//        plateObj18.addProperty("PalatNo", "18");
//        plateObj18.addProperty("ReportedNo", edt_p18reported.getText().toString().trim());
//        plateObj18.addProperty("Conclusion", edt_p18conclusion.getText().toString().trim());
//        plateObj18.addProperty("Distance_R_P", "1");
//        plateObj18.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj18);
//
//        JsonObject plateObj19 = new JsonObject();
//        plateObj19.addProperty("RegID", patientDetails.getRegdId());
//        plateObj19.addProperty("PalatNo", "19");
//        plateObj19.addProperty("ReportedNo", edt_p19reported.getText().toString().trim());
//        plateObj19.addProperty("Conclusion", edt_p19conclusion.getText().toString().trim());
//        plateObj19.addProperty("Distance_R_P", "1");
//        plateObj19.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj19);
//
//        JsonObject plateObj20 = new JsonObject();
//        plateObj20.addProperty("RegID", patientDetails.getRegdId());
//        plateObj20.addProperty("PalatNo", "20");
//        plateObj20.addProperty("ReportedNo", edt_p20reported.getText().toString().trim());
//        plateObj20.addProperty("Conclusion", edt_p20conclusion.getText().toString().trim());
//        plateObj20.addProperty("Distance_R_P", "1");
//        plateObj20.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj20);
//
//        JsonObject plateObj21 = new JsonObject();
//        plateObj21.addProperty("RegID", patientDetails.getRegdId());
//        plateObj21.addProperty("PalatNo", "21");
//        plateObj21.addProperty("ReportedNo", edt_p21reported.getText().toString().trim());
//        plateObj21.addProperty("Conclusion", edt_p21conclusion.getText().toString().trim());
//        plateObj21.addProperty("Distance_R_P", "1");
//        plateObj21.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj21);
//
//        JsonObject plateObj22 = new JsonObject();
//        plateObj22.addProperty("RegID", patientDetails.getRegdId());
//        plateObj22.addProperty("PalatNo", "22");
//        plateObj22.addProperty("ReportedNo", edt_p22reported.getText().toString().trim());
//        plateObj22.addProperty("Conclusion", edt_p22conclusion.getText().toString().trim());
//        plateObj22.addProperty("Distance_R_P", "1");
//        plateObj22.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj22);
//
//        JsonObject plateObj23 = new JsonObject();
//        plateObj23.addProperty("RegID", patientDetails.getRegdId());
//        plateObj23.addProperty("PalatNo", "23");
//        plateObj23.addProperty("ReportedNo", edt_p23reported.getText().toString().trim());
//        plateObj23.addProperty("Conclusion", edt_p23conclusion.getText().toString().trim());
//        plateObj23.addProperty("Distance_R_P", "1");
//        plateObj23.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj23);
//
//        JsonObject plateObj24 = new JsonObject();
//        plateObj24.addProperty("RegID", patientDetails.getRegdId());
//        plateObj24.addProperty("PalatNo", "24");
//        plateObj24.addProperty("ReportedNo", edt_p24reported.getText().toString().trim());
//        plateObj24.addProperty("Conclusion", edt_p24conclusion.getText().toString().trim());
//        plateObj24.addProperty("Distance_R_P", "1");
//        plateObj24.addProperty("CreatedBy", userID);
//        plateArray.add(plateObj24);

        String jsonstring = plateArray.toString();

        if (Utilities.isNetworkAvailable(context)) {
            new InsertEyeScreeningDetails_New().execute(
                    userID,
                    String.valueOf(patientDetails.getRegdId()),
                    campId,
                    userID,
                    userID,
                    name,
                    "1",
                    edt_right_remark.getText().toString().trim(),                /// Right Eye Remark
                    edt_injury_right.getText().toString().trim(),
                    rigthDiseaseId,
                    edt_near_remark.getText().toString().trim(),
                    edt_injury_left.getText().toString().trim(),
                    leftDiseaseId,
                    edt_left_remark.getText().toString().trim(),              /// Left Eye Remark
                    edt_snellen_right.getText().toString().trim(),
                    edt_snellen_left.getText().toString().trim(),
                    edt_jaegar_chart_right.getText().toString().trim(),
                    jsonstring,
                    glassesId

            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class InsertEyeScreeningDetails_New extends AsyncTask<String, Void, String> {

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
            try {

                RequestBody formBody = new FormBody.Builder()
                        .add("DoctorID", params[0])
                        .add("RegdID", params[1])
                        .add("CampID", params[2])
                        .add("USERID", params[3])
                        .add("CHECKEDBY", params[4])
                        .add("CHECKEDBYNAME", params[5])
                        .add("Remarks", params[6])
                        .add("Suggestion", params[7])
                        .add("ForDistanceID_right", "0")
                        .add("Diesease_Injury_Evidence_right", params[8])
                        .add("Diesease_Injury_EvidenceId_right", params[9])
                        .add("ColorTest_right", params[10])
                        .add("ColorTestId_right", "0")
                        .add("ForDistanceID_left", "0")
                        .add("ForDistanceID_both", "0")
                        .add("Diesease_Injury_Evidence_left", params[11])
                        .add("Diesease_Injury_EvidenceId_left", params[12])
                        .add("ColorTest_left", "")
                        .add("ColorTestId_left", "0")
                        .add("other_remark", params[13])
                        .add("Snellelchart_R", params[14])
                        .add("Snellelchart_L", params[15])
                        .add("GagerChartReport", params[16])
                        .add("jsonstring", params[17])
                        .add("idGlasses", params[18])
                        .build();

                OkHttpClient client = new OkHttpClient();
                String url = ApplicationConstants.webservice + ApplicationConstants.InsertEyeScreeningDetails_New;
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                res = response.body().string();

            } catch (SocketTimeoutException ste) {
                ste.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Visual screening test details submitted successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(context, status, "Please contact to support team", false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Fail", "Server not responding", false);
            }
        }
    }

    private class InsertEyeScreeningMachineData extends AsyncTask<String, Void, String> {

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
            try {
                //131650000000
                Log.d("TAG", "doInBackground: " + Arrays.toString(params));
                RequestBody formBody = new FormBody.Builder()
                        .add("jsonstring", params[0])
                        .build();

                OkHttpClient client = new OkHttpClient();
                String url = ApplicationConstants.webservice + ApplicationConstants.InsertVisionScreeningJSONByMachine;
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                res = response.body().string();

            } catch (SocketTimeoutException ste) {
                ste.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Visual screening test details submitted successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(context, status, "Please contact to support team", false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Fail", "Server not responding", false);
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Visual Screening Test");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setVisionEventHandlers() {

        edt_p1reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("12")) {
                    edt_p1conclusion.setText("Not Fibbling");
                } else {
                    edt_p1conclusion.setText("Fibbling");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p2reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("8")) {
                    edt_p2conclusion.setText("Normal color vision");
                } else if (s.toString().equalsIgnoreCase("3")) {
                    edt_p2conclusion.setText("Green color blindness");
                } else {
                    edt_p2conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p3reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("29")) {
                    edt_p3conclusion.setText("Normal color vision");
                } else if (s.toString().equalsIgnoreCase("70")) {
                    edt_p3conclusion.setText("Red and green color blindness");
                } else {
                    edt_p3conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p4reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("5")) {
                    edt_p4conclusion.setText("Normal color vision");
                } else if (s.toString().equalsIgnoreCase("2")) {
                    edt_p4conclusion.setText("Red and green color blindness");
                } else {
                    edt_p4conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p5reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("3")) {
                    edt_p5conclusion.setText("Normal color vision");
                } else if (s.toString().equalsIgnoreCase("5")) {
                    edt_p5conclusion.setText("Red and green color blindness");
                } else {
                    edt_p5conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p6reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("15")) {
                    edt_p6conclusion.setText("Normal color vision");
                } else if (s.toString().equalsIgnoreCase("17")) {
                    edt_p6conclusion.setText("Red and green color blindness");
                }
//                else {
//                    edt_p5conclusion.setText("Complete color blindness");
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p7reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("74")) {
                    edt_p7conclusion.setText("Normal color vision");
                } else if (s.toString().equalsIgnoreCase("21")) {
                    edt_p7conclusion.setText("Red and green color blindness");
                }
//                else {
//                    edt_p5conclusion.setText("Complete color blindness");
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p8reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("6")) {
                    edt_p8conclusion.setText("Normal color vision");
                } else {
                    edt_p8conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p9reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("45")) {
                    edt_p9conclusion.setText("Normal color vision");
                } else {
                    edt_p9conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p10reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("5")) {
                    edt_p10conclusion.setText("Normal color vision");
                } else {
                    edt_p10conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p11reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("7")) {
                    edt_p11conclusion.setText("Normal color vision");
                } else {
                    edt_p11conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p12reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("16")) {
                    edt_p12conclusion.setText("Normal color vision");
                } else {
                    edt_p12conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p13reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("73")) {
                    edt_p13conclusion.setText("Normal color vision");
                } else {
                    edt_p13conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p14reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("5")) {
                    edt_p14conclusion.setText("Red and green color blindness");
                } else {
                    edt_p14conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p15reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("45")) {
                    edt_p15conclusion.setText("Red and green color blindness");
                } else {
                    edt_p15conclusion.setText("Complete color blindness");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p16reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("6")) {
                    edt_p16conclusion.setText("Green color blindness");
                } else if (s.toString().equalsIgnoreCase("2")) {
                    edt_p16conclusion.setText("Red color blindness");
                } else /*if (s.toString().equalsIgnoreCase("26"))*/ {
                    edt_p16conclusion.setText("Normal color vision");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p17reported.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("4")) {
                    edt_p17conclusion.setText("Green color blindness");
                } else if (s.toString().equalsIgnoreCase("2")) {
                    edt_p17conclusion.setText("Red color blindness");
                } else /*if (s.toString().equalsIgnoreCase("26"))*/ {
                    edt_p17conclusion.setText("Normal color vision");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_p18reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", " Purple and red lines"));
                masterList.add(new MasterModel("1", " Purple lines"));
                masterList.add(new MasterModel("2", " Red lines"));

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                builderSingle.setTitle("Select");

                builderSingle.setCancelable(false);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

                for (MasterModel subTrenchModel : masterList) {
                    arrayAdapter.add(subTrenchModel.getName());
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
                        MasterModel master = masterList.get(which);
                        edt_p18reported.setText(master.getName());
                        if (master.getId().equals("1")) {
                            edt_p18conclusion.setText("Normal color vision");
                        } else if (master.getId().equals("2")) {
                            edt_p18conclusion.setText("Red color blindness");
                        } else if (master.getId().equals("3")) {
                            edt_p18conclusion.setText("Purple color blindness");
                        }
                    }
                });
                builderSingle.show();
            }
        });

        edt_p19reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", " Wiggly line"));
                masterList.add(new MasterModel("2", " Nothing"));

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                builderSingle.setTitle("Select");

                builderSingle.setCancelable(false);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

                for (MasterModel subTrenchModel : masterList) {
                    arrayAdapter.add(subTrenchModel.getName());
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
                        MasterModel master = masterList.get(which);

                        edt_p19reported.setText(master.getName());
                        if (master.getId().equals("1")) {
                            edt_p19conclusion.setText("Normal color vision");
                        } else if (master.getId().equals("2")) {
                            edt_p19conclusion.setText("Complete color blindness");
                        }
                    }
                });
                builderSingle.show();
            }
        });

        edt_p20reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", " Green Wiggly Line"));
                masterList.add(new MasterModel("2", " Nothing"));

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                builderSingle.setTitle("Select");

                builderSingle.setCancelable(false);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

                for (MasterModel subTrenchModel : masterList) {
                    arrayAdapter.add(subTrenchModel.getName());
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
                        MasterModel master = masterList.get(which);

                        edt_p20reported.setText(master.getName());
                        if (master.getId().equals("1")) {
                            edt_p20conclusion.setText("Normal color vision");
                        } else if (master.getId().equals("2")) {
                            edt_p20conclusion.setText("Color blindness");
                        }
                    }
                });
                builderSingle.show();
            }
        });

        edt_p21reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", " Oriang Wiggly Line"));
                masterList.add(new MasterModel("2", " Nothing"));

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                builderSingle.setTitle("Select");

                builderSingle.setCancelable(false);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

                for (MasterModel subTrenchModel : masterList) {
                    arrayAdapter.add(subTrenchModel.getName());
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
                        MasterModel master = masterList.get(which);

                        edt_p21reported.setText(master.getName());
                        if (master.getId().equals("1")) {
                            edt_p21conclusion.setText("Normal color vision");
                        } else if (master.getId().equals("2")) {
                            edt_p21conclusion.setText("Color blindness");
                        }
                    }
                });
                builderSingle.show();
            }
        });

        edt_p22reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", " Yellow-green Wiggly Line"));
                masterList.add(new MasterModel("2", " Blue-Green Wiggly Line"));
                masterList.add(new MasterModel("3", " Nothing"));

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                builderSingle.setTitle("Select");

                builderSingle.setCancelable(false);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

                for (MasterModel subTrenchModel : masterList) {
                    arrayAdapter.add(subTrenchModel.getName());
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
                        MasterModel master = masterList.get(which);
                        edt_p22reported.setText(master.getName());

                        if (master.getId().equals("1")) {
                            edt_p22conclusion.setText("Normal color vision");
                        } else if (master.getId().equals("2")) {
                            edt_p22conclusion.setText("Red color blindness");
                        } else if (master.getId().equals("2")) {
                            edt_p22conclusion.setText("Color blindness");
                        }
                    }
                });
                builderSingle.show();
            }
        });

        edt_p23reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", " Red and orange wiggly line"));
                masterList.add(new MasterModel("2", " Blue-green wiggly line"));
                masterList.add(new MasterModel("3", " Nothing"));

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                builderSingle.setTitle("Select");

                builderSingle.setCancelable(false);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

                for (MasterModel subTrenchModel : masterList) {
                    arrayAdapter.add(subTrenchModel.getName());
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
                        MasterModel master = masterList.get(which);
                        edt_p23reported.setText(master.getName());

                        if (master.getId().equals("1")) {
                            edt_p23conclusion.setText("Normal color vision");
                        } else if (master.getId().equals("2")) {
                            edt_p23conclusion.setText("Complete color blindness");
                        } else if (master.getId().equals("3")) {
                            edt_p23conclusion.setText("Complete color blindness");
                        }
                    }
                });
                builderSingle.show();
            }
        });

        edt_p24reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", " Wiggly line"));
                masterList.add(new MasterModel("2", " Nothing"));

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                builderSingle.setTitle("Select");

                builderSingle.setCancelable(false);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

                for (MasterModel subTrenchModel : masterList) {
                    arrayAdapter.add(subTrenchModel.getName());
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
                        MasterModel master = masterList.get(which);
                        edt_p24reported.setText(master.getName());

                        if (master.getId().equals("1")) {
                            edt_p24conclusion.setText("Normal color vision");
                        } else if (master.getId().equals("2")) {
                            edt_p24conclusion.setText("Complete color blindness");
                        }
                    }
                });
                builderSingle.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 10001) {
                    String requiredValue = data.getStringExtra("key");
                    if (!requiredValue.isEmpty())
                        setVisualDataToUI(requiredValue);
                    else
                        llScanner.setVisibility(View.VISIBLE);
                }
            } else {
                llScanner.setVisibility(View.VISIBLE);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    void setVisualDataToUI(String visionJson) {
//        String visionJson = "{\"examID\":\"13080011\",\"patientAge\":20,\"result\":{\"L1\":{\"A\":\"103\",\"C\":\"-0.25\",\"S\":\"0.50\"},\"R1\":{\"A\":\"85\",\"C\":\"-1.00\",\"S\":\"1.00\"},\"L-Avg\":{\"A\":\"90\",\"C\":\"-0.25\",\"S\":\"0.75\"},\"R3\":{\"A\":\"81\",\"C\":\"-1.00\",\"S\":\"1.00\"},\"L2\":{\"A\":\"90\",\"C\":\"-0.25\",\"S\":\"0.75\"},\"R2\":{\"A\":\"78\",\"C\":\"-1.25\",\"S\":\"1.00\"},\"L3\":{\"A\":\"77\",\"C\":\"-0.25\",\"S\":\"0.75\"},\"R-Avg\":{\"A\":\"81\",\"C\":\"-1.00\",\"S\":\"1.00\"}}}";


        llChart.setVisibility(View.VISIBLE);
        btnSubmitMachineData.setVisibility(View.VISIBLE);
//        tvRescan.setVisibility(View.VISIBLE);
        llScanner.setVisibility(View.GONE);

        remidioModel = new Gson().fromJson(visionJson, RemidioModel.class);

        remidioModel.setUserId(patientDetails.getRegdId());

        tvSRight.setText(remidioModel.getResult().getRAvg().getS());
        tvSLeft.setText(remidioModel.getResult().getLAvg().getS());

        tvCRight.setText(remidioModel.getResult().getRAvg().getC());
        tvCLeft.setText(remidioModel.getResult().getLAvg().getC());

        tvARight.setText(remidioModel.getResult().getRAvg().getA());
        tvALeft.setText(remidioModel.getResult().getLAvg().getA());
    }
}

