package com.myhindlab.abkat.activities;

import static com.myhindlab.abkat.utilities.Utilities.changeDateFormat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.HospitalListAdapter;
import com.myhindlab.abkat.models.BreastScreeningModel;
import com.myhindlab.abkat.models.HealthHistoryListModel;
import com.myhindlab.abkat.models.InsertHealthHistoryModel;
import com.myhindlab.abkat.models.MasterModel;
import com.myhindlab.abkat.models.PresentPatientList_Model;
import com.myhindlab.abkat.pojos.HealthHistoryListPojo;
import com.myhindlab.abkat.pojos.PatientMedicaleHistory_OutPut_Pojo;
import com.myhindlab.abkat.pojos.PatientMedicaleHistory_Pojo;
import com.myhindlab.abkat.pojos.SelectAddress_OutPut_pojo;
import com.myhindlab.abkat.pojos.SelectAddress_pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthScreeningPhysicalExaminationNewSecond_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private UserSessionManager userSessionManager;
    private ProgressDialog pd;

    private RadioButton rb_cad_yes, rb_cad_yes_family, rb_cancer_no_family, rb_cad_no, rb_DM_no_family, rb_DM_yes_family, rb_cad_no_family, rb_copd_yes, rb_copd_yes_family, rb_copd_no, rb_copd_no_family, rb_htn_yes, rb_htn_yes_family, rb_htn_no, rb_htn_no_family, rb_cancer_yes_family, rb_cva_yes, rb_cva_yes_family, rb_cva_no, rb_cva_no_family, rb_cancer_yes, rb_cancer_no, rb_anaemia_yes, rb_anaemia_no, rb_mi_yes, rb_mi_no, rb_ckd_yes, rb_ckd_no, rb_DM_yes, rb_DM_no, rb_tb_yes_family, rbIsCovid, rbIsCovidDose1, rbIsCovidDose2, rbIsBoosterDose;
    private PresentPatientList_Model patientDetails;
    private String userID, name, campId, healthScreentype, gneder;
    private String que1 = "", que2 = "", que3, que4, que5, que6, que7,
            que8, que9, que10, que11, que12, que13, que14, que15, RegdId = "";
    private EditText edt_lungsfield, edt_abnormalsound, edt_s1s2normal, edt_anyotherabnormalityormurmurs,
            edt_tenderness, edt_anyherina, edt_anymassperabdomen, edt_gastrocomment, edt_patientwelloriented,
            edt_sensory, edt_nrevoussystemcomment, edt_skin, edt_lastdate, edt_pmp, edt_gynacproblem, edt_breastcancer,
            edt_familyplanning, edt_gynccomment, edt_recentdelivery, edt_recentdeliverycomment, edt_familyplanningadvice,
            edt_finalcomment, edt_finalremark, edt_cardiocomment, edt_patientname, edt_beneficiaryno, edt_benefgender, edt_beneficiaryage, edt_height, edt_weight, edt_bmi, edt_category, edt_mobileno, edt_beneficiarymaritalstatus,
            edt_noofchild, edt_familyoperation, edt_smoking, edt_smoking_since_year, edt_smoking_since_months, edt_alcohol, edt_alcohol_since_year,
            edt_alcohol_since_months, edt_tobacco, edt_tobacco_since_year, edt_drugs, edt_tobacco_since_months, edt_drugs_since_year, edt_drugs_since_months,
            edt_systolic, edt_diastolic, edt_f, edt_pp, edt_r, edt_pulseratebpm, edt_blgr, edt_motor, edt_nolungs, edt_respiratorycomment,
            edt_recentdelivercomment, edt_abnormalcomment, edt_selecthospital,
            edt_otherrespiratorycomment, edt_othercardiocomment, edt_othergastrocomment, edt_othernrevoussystemcomment, edt_otherskin, edt_othergynccomment,
            edt_temperature, edt_spo2, edtSystolic, edtDiastolic, edtSugar, edt_beneficiary_dob;
    private MaterialEditText edt_cancer_history, edt_cad_month, edt_cad_month_family, edt_cad_year, edt_cad_year_family, edt_cva_month, edt_cva_month_family, edt_copd_month, edt_copd_month_family, edt_cva_year, edt_cva_year_family, edt_copd_year, edt_copd_year_family, edt_htn_month, edt_htn_month_family, edt_htn_year, edt_htn_year_family, edt_cancer_year, edt_cancer_month_family, edt_cancer_year_family, edt_cancer_month,
            edt_lumb_observed, edt_anaemia_month, edt_anaemia_year, edt_mi_month, edt_mi_year, edt_ckd_month, edt_ckd_year, edt_DM_month, edt_DM_month_family, edt_DM_year_family, edt_DM_year, edt_personal_observation, edt_comment, edt_final_remark, edt_tb_month_family, edt_tb_year_family;
    private CardView cv_breast_screening, cvBasicInfo, cvSocialHistory;


    private TextView tv_gynaecologicalexamination, tv_view_report, tvBasicTitle, tvSocialHistory;
    private CheckBox cb_lmp, cb_ispregnant;
    private Button btn_save;
    private RecyclerView rv_medicale_history;
    private ArrayList<HealthHistoryListModel> healthHistoryList;
    private ArrayList<PatientMedicaleHistory_OutPut_Pojo> patinetHistoryList;
    private LinearLayout ll_gynac1, ll_gynac2, ll_gyanc3, ll_gynac4;
    private String abnormalSoundId = "", normalAbnormalID = "", sensoryID = "", motorID = "";
    private String LungComment, AbnormalComment, OtherAbnormality, CVSComment, SkinInfection, CNSComment, LMPDate,
            Comment, FSHComment, DeliveryComment, FamillyAdvice;
    private int Motor = 0, Sensory = 0, PatwellOriented = 0, MassperAbdomen = 0, Anyhernia = 0, Palpationabdomen = 0,
            AnyMurmursID = 0, SIS2Normal = 0, AbnormalSound = 0, LungClear = 0, CampId = 0, LMP = 1, IsPregnant = 0, GynecProblem = 0, EvidanceThyroid = 0,
            ISBreastCancer = 0, FamillyPlaning = 0, FSH = 0, RecentDelivery = 0, HospitalId = 0, PMP = 0, finalRemarks = 0;
    ArrayList<InsertHealthHistoryModel> healthList;

    int regdId = 0, lungClear = 0, abnormalSound = 0, s1s2Normal = 0, anyMurmurs = 0, palpationabdomen = 0, anyhernia = 0,
            massperAbdomen = 0, patwellOriented = 0, sensory = 0, motor = 0, finalRemark = 0, Createdby = 0, lmp = 0, ispregnent = 0;
    String lungComment, abnormalComment, cvsComment = "", GITComment = "", skinInfection = "", gynaComment, cnsComment = "", finalComment, jsonstring, jsonStringFamily, dateString, distlgdcode;
    private int mYear, mMonth, mDay;
    ArrayList<SelectAddress_OutPut_pojo> hospitalList;
    private RecyclerView hospital_list_recycler_view;
    private RecyclerView.Adapter mAdapter, mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager;
    String selectedHospitalId = "0", versionName = "0";
    String respiratoryId, cardioId, gastroId, sevousSystemId, skinId, gyncId, reportUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_screening_physical_new_examination_second);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandlers();
        getPatientDetails();
        getMedicalehistory();

    }

    private void init() {
        context = HealthScreeningPhysicalExaminationNewSecond_Activity.this;
        userSessionManager = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        rv_medicale_history = findViewById(R.id.rv_medicale_history);
        edt_patientname = findViewById(R.id.edt_patientname);
        edt_beneficiaryno = findViewById(R.id.edt_beneficiaryno);
        edt_benefgender = findViewById(R.id.edt_benefgender);
        edt_beneficiaryage = findViewById(R.id.edt_beneficiaryage);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        edt_bmi = findViewById(R.id.edt_bmi);
        edt_category = findViewById(R.id.edt_category);
        edt_mobileno = findViewById(R.id.edt_mobileno);
        edt_blgr = findViewById(R.id.edt_blgr);
        edt_smoking = findViewById(R.id.edt_smoking);
        edt_smoking_since_year = findViewById(R.id.edt_smoking_since_year);
        edt_beneficiarymaritalstatus = findViewById(R.id.edt_beneficiarymaritalstatus);
        edt_noofchild = findViewById(R.id.edt_noofchild);
        edt_alcohol = findViewById(R.id.edt_alcohol);
        edt_selecthospital = findViewById(R.id.edt_selecthospital);
        edt_smoking_since_months = findViewById(R.id.edt_smoking_since_months);
        edt_alcohol_since_year = findViewById(R.id.edt_alcohol_since_year);
        edt_alcohol_since_months = findViewById(R.id.edt_alcohol_since_months);
        edt_tobacco = findViewById(R.id.edt_tobacco);
        edt_tobacco_since_year = findViewById(R.id.edt_tobacco_since_year);
        edt_tobacco_since_months = findViewById(R.id.edt_tobacco_since_months);
        edt_drugs = findViewById(R.id.edt_drugs);
        edt_drugs_since_year = findViewById(R.id.edt_drugs_since_year);
        edt_drugs_since_months = findViewById(R.id.edt_drugs_since_months);
        edt_systolic = findViewById(R.id.edt_systolic);
        edt_diastolic = findViewById(R.id.edt_diastolic);
        edt_f = findViewById(R.id.edt_f);
        edt_pp = findViewById(R.id.edt_pp);
        edt_r = findViewById(R.id.edt_r);
        tv_gynaecologicalexamination = findViewById(R.id.tv_gynaecologicalexamination);
        edt_pulseratebpm = findViewById(R.id.edt_pulseratebpm);
        rv_medicale_history.setLayoutManager(new GridLayoutManager(context, 2));
        edt_lungsfield = findViewById(R.id.edt_lungsfield);
        edt_nolungs = findViewById(R.id.edt_nolungs);
        edt_abnormalsound = findViewById(R.id.edt_abnormalsound);
        edt_s1s2normal = findViewById(R.id.edt_s1s2normal);
        edt_tenderness = findViewById(R.id.edt_tenderness);
        edt_anyherina = findViewById(R.id.edt_anyherina);
        edt_anyotherabnormalityormurmurs = findViewById(R.id.edt_anyotherabnormalityormurmurs);
        edt_anymassperabdomen = findViewById(R.id.edt_anymassperabdomen);
        edt_gastrocomment = findViewById(R.id.edt_gastrocomment);
        edt_patientwelloriented = findViewById(R.id.edt_patientwelloriented);
        edt_sensory = findViewById(R.id.edt_sensory);
        edt_motor = findViewById(R.id.edt_motor);
        edt_nrevoussystemcomment = findViewById(R.id.edt_nrevoussystemcomment);
        edt_skin = findViewById(R.id.edt_skin);
        cb_lmp = findViewById(R.id.cb_lmp);
        cb_ispregnant = findViewById(R.id.cb_ispregnant);
        edt_lastdate = findViewById(R.id.edt_lastdate);
        edt_pmp = findViewById(R.id.edt_pmp);
        edt_gynacproblem = findViewById(R.id.edt_gynacproblem);
        edt_breastcancer = findViewById(R.id.edt_breastcancer);
        edt_familyplanning = findViewById(R.id.edt_familyplanning);
        edt_gynccomment = findViewById(R.id.edt_gynccomment);
        edt_recentdelivery = findViewById(R.id.edt_recentdelivery);
        edt_recentdelivercomment = findViewById(R.id.edt_recentdelivercomment);
        edt_familyplanningadvice = findViewById(R.id.edt_familyplanningadvice);
        edt_finalcomment = findViewById(R.id.edt_finalcomment);
        edt_finalremark = findViewById(R.id.edt_finalremark);
        btn_save = findViewById(R.id.btn_save);
        edt_cardiocomment = findViewById(R.id.edt_cardiocomment);
        edt_temperature = findViewById(R.id.edt_temperature);
        edt_spo2 = findViewById(R.id.edt_spo2);
        ll_gynac1 = findViewById(R.id.ll_gynac1);
        ll_gynac2 = findViewById(R.id.ll_gynac2);
        ll_gyanc3 = findViewById(R.id.ll_gyanc3);
        ll_gynac4 = findViewById(R.id.ll_gynac4);
        edt_abnormalcomment = findViewById(R.id.edt_abnormalcomment);
        edt_selecthospital.setVisibility(View.GONE);

        cv_breast_screening = findViewById(R.id.cv_breast_screening);
        edt_cancer_history = findViewById(R.id.edt_cancer_history);
        edt_lumb_observed = findViewById(R.id.edt_lumb_observed);
        edt_personal_observation = findViewById(R.id.edt_personal_observation);
        edt_comment = findViewById(R.id.edt_comment);
        edt_final_remark = findViewById(R.id.edt_final_remark);
        tv_view_report = findViewById(R.id.tv_view_report);

        edt_otherrespiratorycomment = findViewById(R.id.edt_otherrespiratorycomment);
        edt_othercardiocomment = findViewById(R.id.edt_othercardiocomment);
        edt_othergastrocomment = findViewById(R.id.edt_othergastrocomment);
        edt_othernrevoussystemcomment = findViewById(R.id.edt_othernrevoussystemcomment);
        edt_otherskin = findViewById(R.id.edt_otherskin);
        edt_othergynccomment = findViewById(R.id.edt_othergynccomment);
        edt_cad_month = findViewById(R.id.edt_cad_month);
        edt_cad_month_family = findViewById(R.id.edt_cad_month_family);
        edt_cad_year = findViewById(R.id.edt_cad_year);
        edt_cad_year_family = findViewById(R.id.edt_cad_year_family);
        rb_cad_yes = findViewById(R.id.rb_cad_yes);
        rb_cad_yes_family = findViewById(R.id.rb_cad_yes_family);
        rb_cad_no = findViewById(R.id.rb_cad_no);
        rb_cad_no_family = findViewById(R.id.rb_cad_no_family);
        rb_copd_no = findViewById(R.id.rb_copd_no);
        rb_copd_no_family = findViewById(R.id.rb_copd_no_family);
        rb_copd_yes = findViewById(R.id.rb_copd_yes);
        rb_copd_yes_family = findViewById(R.id.rb_copd_yes_family);
        rb_htn_yes = findViewById(R.id.rb_htn_yes);
        rb_htn_yes_family = findViewById(R.id.rb_htn_yes_family);
        rb_htn_no = findViewById(R.id.rb_htn_no);
        rb_htn_no_family = findViewById(R.id.rb_htn_no_family);
        rb_cva_yes = findViewById(R.id.rb_cva_yes);
        rb_cva_yes_family = findViewById(R.id.rb_cva_yes_family);
        rb_cva_no = findViewById(R.id.rb_cva_no);
        rb_cva_no_family = findViewById(R.id.rb_cva_no_family);
        rb_cancer_yes = findViewById(R.id.rb_cancer_yes);
        rb_cancer_yes_family = findViewById(R.id.rb_cancer_yes_family);
        rb_cancer_no = findViewById(R.id.rb_cancer_no);
        rb_cancer_no_family = findViewById(R.id.rb_cancer_no_family);
        rb_anaemia_yes = findViewById(R.id.rb_anaemia_yes);
        rb_anaemia_no = findViewById(R.id.rb_anaemia_no);
        rb_mi_yes = findViewById(R.id.rb_mi_yes);
        rb_mi_no = findViewById(R.id.rb_mi_no);
        rb_ckd_yes = findViewById(R.id.rb_ckd_yes);
        rb_ckd_no = findViewById(R.id.rb_ckd_no);
        rb_DM_yes = findViewById(R.id.rb_DM_yes);
        rb_DM_yes_family = findViewById(R.id.rb_DM_yes_family);
        rb_DM_no = findViewById(R.id.rb_DM_no);
        rb_DM_no_family = findViewById(R.id.rb_DM_no_family);
        edt_cva_month = findViewById(R.id.edt_cva_month);
        edt_cva_month_family = findViewById(R.id.edt_cva_month_family);
        edt_copd_month = findViewById(R.id.edt_copd_month);
        edt_copd_month_family = findViewById(R.id.edt_copd_month_family);
        edt_cva_month = findViewById(R.id.edt_cva_month);
        edt_cva_year = findViewById(R.id.edt_cva_year);
        edt_cva_year_family = findViewById(R.id.edt_cva_year_family);
        edt_copd_year = findViewById(R.id.edt_copd_year);
        edt_copd_year_family = findViewById(R.id.edt_copd_year_family);
        edt_htn_month = findViewById(R.id.edt_htn_month);
        edt_htn_month_family = findViewById(R.id.edt_htn_month_family);
        edt_htn_year = findViewById(R.id.edt_htn_year);
        edt_htn_year_family = findViewById(R.id.edt_htn_year_family);
        edt_cancer_year = findViewById(R.id.edt_cancer_year);
        edt_cancer_year_family = findViewById(R.id.edt_cancer_year_family);
        edt_cancer_month = findViewById(R.id.edt_cancer_month);
        edt_cancer_month_family = findViewById(R.id.edt_cancer_month_family);
        edt_anaemia_month = findViewById(R.id.edt_anaemia_month);
        edt_anaemia_year = findViewById(R.id.edt_anaemia_year);
        edt_mi_month = findViewById(R.id.edt_mi_month);
        edt_mi_year = findViewById(R.id.edt_mi_year);
        edt_ckd_month = findViewById(R.id.edt_ckd_month);
        edt_ckd_year = findViewById(R.id.edt_ckd_year);
        edt_DM_month = findViewById(R.id.edt_DM_month);
        edt_DM_month_family = findViewById(R.id.edt_DM_month_family);
        edt_DM_year = findViewById(R.id.edt_DM_year);
        edt_DM_year_family = findViewById(R.id.edt_DM_year_family);
        edt_tb_month_family = findViewById(R.id.edt_tb_month_family);
        edt_tb_year_family = findViewById(R.id.edt_tb_year_family);
        rb_tb_yes_family = findViewById(R.id.rb_tb_yes_family);
        rbIsCovid = findViewById(R.id.rbIsCovid);
        rbIsCovidDose1 = findViewById(R.id.rbIsCovidDose1);
        rbIsCovidDose2 = findViewById(R.id.rbIsCovidDose2);
        rbIsBoosterDose = findViewById(R.id.rbIsBoosterDose);
        edtSystolic = findViewById(R.id.edtSystolic);
        edtDiastolic = findViewById(R.id.edtDiastolic);
        edtSugar = findViewById(R.id.edtSugar);
        tvBasicTitle = findViewById(R.id.tvBasicTitle);
        cvBasicInfo = findViewById(R.id.cvBasicInfo);
        cvSocialHistory = findViewById(R.id.cvSocialHistory);
        tvSocialHistory = findViewById(R.id.tvSocialHistory);
        edt_beneficiary_dob = findViewById(R.id.edt_beneficiary_dob);
    }

    private void setUpToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Physical Examination");

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
        RegdId = String.valueOf(patientDetails.getRegdId());
        CampId = Integer.parseInt(getIntent().getStringExtra("campId"));
        healthScreentype = getIntent().getStringExtra("healthScreentype");
        try {
            JSONArray user_info = new JSONArray(userSessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                name = json.getString("name");
                distlgdcode = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }


        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dateString = Utilities.ConvertDateFormatFacilityVisit(mYear, mMonth + 1, mDay);
        String[] separated = dateString.split("-");
        final String yearStr = separated[0];
        final String monthStr = separated[1];
        final String dayStr = separated[2];
        edt_lastdate.setText(dayStr + "/" + monthStr + "/" + yearStr);

    }

    private void setEventHandlers() {
        tvSocialHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (cvSocialHistory.getVisibility() == View.VISIBLE) {
////                    cvBasicInfo.setVisibility(View.GONE);
//                    cvSocialHistory.setVisibility(View.GONE);
//                    tvSocialHistory.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);
//                } else {
////                    cvBasicInfo.setVisibility(View.VISIBLE);
//                    cvSocialHistory.setVisibility(View.VISIBLE);
//                    tvSocialHistory.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
//
//                }
            }
        });
        cb_lmp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_lmp.isChecked()) {
                    lmp = 1;
                    // Utilities.showToastMessage(String.valueOf(lmp),context);
                } else {
                    lmp = 0;
                    //Utilities.showToastMessage(String.valueOf(lmp),context);
                }
            }
        });


        rb_cad_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_cad_year.setText("");
                edt_cad_month.setText("");
                edt_cad_year.setVisibility(View.VISIBLE);
                edt_cad_month.setVisibility(View.VISIBLE);
            }
        });
        rb_cad_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_cad_year.setText("0");
                edt_cad_month.setText("0");
                edt_cad_year.setVisibility(View.GONE);
                edt_cad_month.setVisibility(View.GONE);
            }
        });
        rb_copd_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_copd_year.setText("");
                edt_copd_month.setText("");
                edt_copd_year.setVisibility(View.VISIBLE);
                edt_copd_month.setVisibility(View.VISIBLE);
            }
        });
        rb_copd_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_copd_year.setText("0");
                edt_copd_month.setText("0");
                edt_copd_year.setVisibility(View.GONE);
                edt_copd_month.setVisibility(View.GONE);
            }
        });
        rb_copd_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_copd_year.setText("");
                edt_copd_month.setText("");
                edt_copd_year.setVisibility(View.VISIBLE);
                edt_copd_month.setVisibility(View.VISIBLE);
            }
        });
        rb_copd_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_copd_year.setText("0");
                edt_copd_month.setText("0");
                edt_copd_year.setVisibility(View.GONE);
                edt_copd_month.setVisibility(View.GONE);
            }
        });
        rb_htn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_htn_year.setText("");
                edt_htn_month.setText("");
                edt_htn_year.setVisibility(View.VISIBLE);
                edt_htn_month.setVisibility(View.VISIBLE);
            }
        });
        rb_htn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_htn_year.setText("0");
                edt_htn_month.setText("0");
                edt_htn_year.setVisibility(View.GONE);
                edt_htn_month.setVisibility(View.GONE);
            }
        });
        rb_cva_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_cva_year.setText("");
                edt_cva_month.setText("");
                edt_cva_year.setVisibility(View.VISIBLE);
                edt_cva_month.setVisibility(View.VISIBLE);
            }
        });
        rb_cva_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_cva_year.setText("0");
                edt_cva_month.setText("0");
                edt_cva_year.setVisibility(View.GONE);
                edt_cva_month.setVisibility(View.GONE);
            }
        });
        rb_cancer_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_cancer_year.setText("");
                edt_cancer_month.setText("");
                edt_cancer_year.setVisibility(View.VISIBLE);
                edt_cancer_month.setVisibility(View.VISIBLE);
            }
        });

        rb_cancer_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_cancer_year.setText("0");
                edt_cancer_month.setText("0");
                edt_cancer_year.setVisibility(View.GONE);
                edt_cancer_month.setVisibility(View.GONE);
            }
        });

        rb_anaemia_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_anaemia_year.setText("");
                edt_anaemia_month.setText("");
                edt_anaemia_year.setVisibility(View.VISIBLE);
                edt_anaemia_month.setVisibility(View.VISIBLE);
            }
        });

        rb_anaemia_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_anaemia_year.setText("0");
                edt_anaemia_month.setText("0");
                edt_anaemia_year.setVisibility(View.GONE);
                edt_anaemia_month.setVisibility(View.GONE);
            }
        });

        rb_mi_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_mi_year.setText("");
                edt_mi_month.setText("");
                edt_mi_year.setVisibility(View.VISIBLE);
                edt_mi_month.setVisibility(View.VISIBLE);
            }
        });

        rb_mi_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_mi_year.setText("0");
                edt_mi_month.setText("0");
                edt_mi_year.setVisibility(View.GONE);
                edt_mi_month.setVisibility(View.GONE);
            }
        });

        rb_ckd_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_ckd_year.setText("");
                edt_ckd_month.setText("");
                edt_ckd_year.setVisibility(View.VISIBLE);
                edt_ckd_month.setVisibility(View.VISIBLE);
            }
        });
        rb_DM_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_DM_year.setText("");
                edt_DM_month.setText("");
                edt_DM_year.setVisibility(View.VISIBLE);
                edt_DM_month.setVisibility(View.VISIBLE);
            }
        });
        rb_DM_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_DM_year.setText("0");
                edt_DM_month.setText("0");
                edt_DM_year.setVisibility(View.GONE);
                edt_DM_month.setVisibility(View.GONE);
            }
        });
        rb_ckd_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_ckd_year.setText("0");
                edt_ckd_month.setText("0");
                edt_ckd_year.setVisibility(View.GONE);
                edt_ckd_month.setVisibility(View.GONE);
            }
        });
        rb_cad_yes_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_cad_year_family.setText("0");
                    edt_cad_month_family.setText("0");
                    edt_cad_year_family.setVisibility(View.GONE);
                    edt_cad_month_family.setVisibility(View.GONE);
                } else {
                    edt_cad_year_family.setText("");
                    edt_cad_month_family.setText("");
                    edt_cad_year_family.setVisibility(View.VISIBLE);
                    edt_cad_month_family.setVisibility(View.VISIBLE);
                }
            }
        });
        rb_copd_yes_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_copd_year_family.setText("0");
                    edt_copd_month_family.setText("0");
                    edt_copd_year_family.setVisibility(View.GONE);
                    edt_copd_month_family.setVisibility(View.GONE);
                } else {
                    edt_copd_year_family.setText("");
                    edt_copd_month_family.setText("");
                    edt_copd_year_family.setVisibility(View.VISIBLE);
                    edt_copd_month_family.setVisibility(View.VISIBLE);
                }
            }
        });
        rb_htn_yes_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_htn_year_family.setText("0");
                    edt_htn_month_family.setText("0");
                    edt_htn_year_family.setVisibility(View.GONE);
                    edt_htn_month_family.setVisibility(View.GONE);
                } else {
                    edt_htn_year_family.setText("");
                    edt_htn_month_family.setText("");
                    edt_htn_year_family.setVisibility(View.VISIBLE);
                    edt_htn_month_family.setVisibility(View.VISIBLE);
                }
            }
        });
        rb_cva_yes_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_cva_year_family.setText("0");
                    edt_cva_month_family.setText("0");
                    edt_cva_year_family.setVisibility(View.GONE);
                    edt_cva_month_family.setVisibility(View.GONE);
                } else {
                    edt_cva_year_family.setText("");
                    edt_cva_month_family.setText("");
                    edt_cva_year_family.setVisibility(View.VISIBLE);
                    edt_cva_month_family.setVisibility(View.VISIBLE);
                }
            }
        });
        rb_cancer_yes_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_cancer_year_family.setText("0");
                    edt_cancer_month_family.setText("0");
                    edt_cancer_year_family.setVisibility(View.GONE);
                    edt_cancer_month_family.setVisibility(View.GONE);
                } else {
                    edt_cancer_year_family.setText("");
                    edt_cancer_month_family.setText("");
                    edt_cancer_year_family.setVisibility(View.VISIBLE);
                    edt_cancer_month_family.setVisibility(View.VISIBLE);
                }
            }
        });
        rb_anaemia_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_anaemia_year.setText("0");
                    edt_anaemia_month.setText("0");
                    edt_anaemia_year.setVisibility(View.GONE);
                    edt_anaemia_month.setVisibility(View.GONE);
                } else {
                    edt_anaemia_year.setText("");
                    edt_anaemia_month.setText("");
                    edt_anaemia_year.setVisibility(View.VISIBLE);
                    edt_anaemia_month.setVisibility(View.VISIBLE);
                }
            }
        });
        rb_DM_yes_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_DM_month_family.setText("0");
                    edt_DM_year_family.setText("0");
                    edt_DM_year_family.setVisibility(View.GONE);
                    edt_DM_month_family.setVisibility(View.GONE);
                } else {
                    edt_DM_month_family.setText("");
                    edt_DM_year_family.setText("");
                    edt_DM_year_family.setVisibility(View.VISIBLE);
                    edt_DM_month_family.setVisibility(View.VISIBLE);
                }
            }
        });
        rb_tb_yes_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    edt_tb_month_family.setText("0");
                    edt_tb_year_family.setText("0");
                    edt_tb_year_family.setVisibility(View.GONE);
                    edt_tb_month_family.setVisibility(View.GONE);
                } else {
                    edt_tb_month_family.setText("");
                    edt_tb_year_family.setText("");
                    edt_tb_year_family.setVisibility(View.VISIBLE);
                    edt_tb_month_family.setVisibility(View.VISIBLE);
                }
            }
        });


        cb_ispregnant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_ispregnant.isChecked()) {
                    ispregnent = 1;
                    edt_selecthospital.setVisibility(View.VISIBLE);
                    // Utilities.showToastMessage(String.valueOf(ispregnent),context);
                } else {
                    ispregnent = 0;
                    edt_selecthospital.setVisibility(View.GONE);
                    selectedHospitalId = "0";
                    // Utilities.showToastMessage(String.valueOf(ispregnent),context);

                }
            }
        });

        edt_lastdate.setOnClickListener(this);
        edt_selecthospital.setOnClickListener(this);
        edt_lungsfield.setOnClickListener(this);
        edt_abnormalsound.setOnClickListener(this);
        edt_abnormalcomment.setOnClickListener(this);
        edt_s1s2normal.setOnClickListener(this);
        edt_anyotherabnormalityormurmurs.setOnClickListener(this);
        edt_tenderness.setOnClickListener(this);
        edt_anyherina.setOnClickListener(this);
        edt_anymassperabdomen.setOnClickListener(this);
        edt_patientwelloriented.setOnClickListener(this);
        edt_sensory.setOnClickListener(this);
        edt_motor.setOnClickListener(this);
        edt_lastdate.setOnClickListener(this);
        edt_pmp.setOnClickListener(this);
        edt_gynacproblem.setOnClickListener(this);
        edt_breastcancer.setOnClickListener(this);
        edt_familyplanning.setOnClickListener(this);
        edt_recentdelivery.setOnClickListener(this);
        edt_finalremark.setOnClickListener(this);

        edt_cardiocomment.setOnClickListener(this);
        edt_gastrocomment.setOnClickListener(this);
        edt_nrevoussystemcomment.setOnClickListener(this);
        edt_gynccomment.setOnClickListener(this);
        edt_skin.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        tv_view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reportUrl.equals("")) {
                    showImageDialog();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_selecthospital: {
                if (Utilities.isNetworkAvailable(context))
                    new SelectHospital().execute();
                else
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                break;
            }

            case R.id.edt_lastdate: {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                edt_lastdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate2, dayOfMonth, monthOfYear + 1, year));
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
                break;
            }

            case R.id.edt_abnormalsound: {
                AbnormalSoundDialogCreater();
                break;
            }

            case R.id.edt_lungsfield: {
                openDialog("q1");
                break;
            }

            case R.id.edt_s1s2normal: {
                openDialog("q3");
                break;
            }

            case R.id.edt_anyotherabnormalityormurmurs: {
                openDialog("q4");
                break;
            }

            case R.id.edt_tenderness: {
                openDialog("q5");
                break;
            }

            case R.id.edt_anyherina: {
                openDialog("q6");
                break;
            }

            case R.id.edt_anymassperabdomen: {
                openDialog("q7");
                break;
            }

            case R.id.edt_patientwelloriented: {
                openDialog("q8");
                break;
            }

            case R.id.edt_sensory: {
                AbnormalNormalDialogCreater("Sensory");
                break;
            }

            case R.id.edt_motor: {
                AbnormalNormalDialogCreater("Motor");
                break;
            }

            case R.id.edt_pmp: {
                openDialog("q10");
                break;
            }

            case R.id.edt_gynacproblem: {
                openDialog("q11");
                break;
            }

            case R.id.edt_breastcancer: {
                openDialog("q12");
                break;
            }

            case R.id.edt_familyplanning: {
                openDialog("q13");
                break;
            }

            case R.id.edt_recentdelivery: {
                openDialog("q14");
                break;
            }

            case R.id.edt_finalremark: {
                FinalRemarkDialogCreater();
                break;
            }

            case R.id.edt_abnormalcomment: {
                ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", "No Abnormality Detected"));
                masterList.add(new MasterModel("2", "Other"));
                showResultDialog(masterList, 1);
                break;
            }

            case R.id.edt_cardiocomment: {
                ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", "No Abnormality Detected"));
                masterList.add(new MasterModel("2", "Other"));
                showResultDialog(masterList, 2);
                break;
            }

            case R.id.edt_gastrocomment: {
                ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", "No Abnormality Detected"));
                masterList.add(new MasterModel("2", "Other"));
                showResultDialog(masterList, 3);
                break;
            }

            case R.id.edt_nrevoussystemcomment: {
                ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", "No Abnormality Detected"));
                masterList.add(new MasterModel("2", "Other"));
                showResultDialog(masterList, 4);
                break;
            }

            case R.id.edt_skin: {
                ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", "No Abnormality Detected"));
                masterList.add(new MasterModel("2", "Other"));
                showResultDialog(masterList, 5);
                break;
            }

            case R.id.edt_gynccomment: {
                ArrayList<MasterModel> masterList = new ArrayList<>();
                masterList.add(new MasterModel("1", "No Abnormality Detected"));
                masterList.add(new MasterModel("2", "Other"));
                showResultDialog(masterList, 6);
                break;
            }

            case R.id.btn_save: {
                // Toast.makeText(context, "HealthList " + healthList.size(), Toast.LENGTH_LONG).show();

                ArrayList<InsertHealthHistoryModel> healthList = new ArrayList<>();

//                for (int i = 0; i < healthHistoryList.size(); i++) {
//                    String testStatus = "0", since = "0";
//
//                    HealthHistoryAdapter.ViewHolder myViewHolder =
//                            (HealthHistoryAdapter.ViewHolder) rv_medicale_history.findViewHolderForAdapterPosition(i);
//
//                    if (myViewHolder.cb_disease.isChecked()) {
//                        testStatus = "1";
//                    }
//
//                    if (!myViewHolder.edt_since.getText().toString().trim().equals("")) {
//                        since = myViewHolder.edt_since.getText().toString();
//                    }
//
//                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
//                    summary.setPE_TestID(healthHistoryList.get(i).getDiseaseid());
//                    summary.setTestStatus(testStatus);
//                    summary.setSince(since);
//                    summary.setSinceYear(since);
//                    summary.setSinceMonth(since);
//                    healthList.add(summary);
//                }
                if (rb_cad_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("12");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_cad_month.getText().toString().isEmpty()) {
                        edt_cad_month.setError("Enter Month");
                        return;
                    }
                    if (edt_cad_year.getText().toString().isEmpty()) {
                        edt_cad_year.setError("Enter Year");
                        return;
                    }
                    summary.setSinceYear(edt_cad_year.getText().toString());
                    summary.setSinceMonth(edt_cad_month.getText().toString());
                    healthList.add(summary);
                } else if (!rb_cad_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("12");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    healthList.add(summary);
                }
                if (rb_copd_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("13");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_copd_month.getText().toString().isEmpty()) {
                        edt_copd_month.setError("Enter Month");
                        return;
                    }
                    if (edt_copd_year.getText().toString().isEmpty()) {
                        edt_copd_year.setError("Enter Year");
                        return;
                    }
                    summary.setSinceYear(edt_copd_year.getText().toString());
                    summary.setSinceMonth(edt_copd_month.getText().toString());
                    healthList.add(summary);
                } else if (!rb_copd_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("13");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    healthList.add(summary);
                }

                if (rb_htn_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("14");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_htn_month.getText().toString().isEmpty()) {
                        edt_htn_month.setError("Enter Month");
                        return;
                    }
                    if (edt_htn_year.getText().toString().isEmpty()) {
                        edt_htn_year.setError("Enter Year");
                        return;
                    }
                    summary.setSinceYear(edt_htn_year.getText().toString());
                    summary.setSinceMonth(edt_htn_month.getText().toString());
                    healthList.add(summary);
                } else if (!rb_htn_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("14");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    healthList.add(summary);
                }

                if (rb_cva_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("15");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_cva_month.getText().toString().isEmpty()) {
                        edt_cva_month.setError("Enter Month");
                        return;
                    }
                    if (edt_cva_year.getText().toString().isEmpty()) {
                        edt_cva_year.setError("Enter Year");
                        return;
                    }
                    summary.setSinceYear(edt_cva_year.getText().toString());
                    summary.setSinceMonth(edt_cva_month.getText().toString());
                    healthList.add(summary);
                } else if (!rb_cva_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("15");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    healthList.add(summary);
                }
                if (rb_cancer_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("16");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_cancer_month.getText().toString().isEmpty()) {
                        edt_cancer_month.setError("Enter Month");
                        return;
                    }
                    if (edt_cancer_year.getText().toString().isEmpty()) {
                        edt_cancer_year.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_cancer_month.getText().toString());
                    summary.setSinceYear(edt_cancer_year.getText().toString());
                    healthList.add(summary);
                } else if (!rb_cancer_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("16");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    healthList.add(summary);
                }

                if (rb_DM_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("11");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_DM_month.getText().toString().isEmpty()) {
                        edt_DM_month.setError("Enter Month");
                        return;
                    }
                    if (edt_DM_year.getText().toString().isEmpty()) {
                        edt_DM_year.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_DM_month.getText().toString());
                    summary.setSinceYear(edt_DM_year.getText().toString());
                    healthList.add(summary);
                } else if (!rb_DM_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("11");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    healthList.add(summary);
                }
                Gson gson = new Gson();
                String jsonstrings = gson.toJson(healthList);

                /** Family History*/
                ArrayList<InsertHealthHistoryModel> familyHealthList = new ArrayList<>();

                if (rb_cad_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("12");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_cad_month_family.getText().toString().isEmpty()) {
                        edt_cad_month_family.setError("Enter Month");
                        return;
                    }
                    if (edt_cad_year_family.getText().toString().isEmpty()) {
                        edt_cad_year_family.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_cad_month_family.getText().toString());
                    summary.setSinceYear(edt_cad_year_family.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_cad_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("12");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }
                if (rb_copd_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("13");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_copd_month_family.getText().toString().isEmpty()) {
                        edt_copd_month_family.setError("Enter Month");
                        return;
                    }
                    if (edt_copd_year_family.getText().toString().isEmpty()) {
                        edt_copd_year_family.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_copd_month_family.getText().toString());
                    summary.setSinceYear(edt_copd_year_family.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_copd_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("13");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }

                if (rb_htn_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("14");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_htn_month_family.getText().toString().isEmpty()) {
                        edt_htn_month_family.setError("Enter Month");
                        return;
                    }
                    if (edt_htn_year_family.getText().toString().isEmpty()) {
                        edt_htn_year_family.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_htn_month_family.getText().toString());
                    summary.setSinceYear(edt_htn_year_family.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_htn_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("14");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }

                if (rb_cva_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("15");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_cva_month_family.getText().toString().isEmpty()) {
                        edt_cva_month_family.setError("Enter Month");
                        return;
                    }
                    if (edt_cva_year_family.getText().toString().isEmpty()) {
                        edt_cva_year_family.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_cva_month_family.getText().toString());
                    summary.setSinceYear(edt_cva_year_family.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_cva_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("15");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }
                if (rb_cancer_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("16");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_cancer_month_family.getText().toString().isEmpty()) {
                        edt_cancer_month_family.setError("Enter Month");
                        return;
                    }
                    if (edt_cancer_year_family.getText().toString().isEmpty()) {
                        edt_cancer_year_family.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_cancer_month_family.getText().toString());
                    summary.setSinceYear(edt_cancer_year_family.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_cancer_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("16");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }

                if (rb_DM_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("11");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_DM_month_family.getText().toString().isEmpty()) {
                        edt_DM_month_family.setError("Enter Month");
                        return;
                    }
                    if (edt_DM_year_family.getText().toString().isEmpty()) {
                        edt_DM_year_family.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_DM_month_family.getText().toString());
                    summary.setSinceYear(edt_DM_year_family.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_DM_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("11");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }
                if (rb_mi_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("18");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_mi_month.getText().toString().isEmpty()) {
                        edt_mi_month.setError("Enter Month");
                        return;
                    }
                    if (edt_mi_year.getText().toString().isEmpty()) {
                        edt_mi_year.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_mi_month.getText().toString());
                    summary.setSinceYear(edt_mi_year.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_mi_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("18");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }
                if (rb_anaemia_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("17");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_anaemia_month.getText().toString().isEmpty()) {
                        edt_anaemia_month.setError("Enter Month");
                        return;
                    }
                    if (edt_anaemia_year.getText().toString().isEmpty()) {
                        edt_anaemia_year.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_anaemia_month.getText().toString());
                    summary.setSinceYear(edt_anaemia_year.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_anaemia_yes.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("17");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }
                if (rb_tb_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("7");
                    summary.setTestStatus("1");
                    summary.setSince("0");
                    if (edt_tb_month_family.getText().toString().isEmpty()) {
                        edt_tb_month_family.setError("Enter Month");
                        return;
                    }
                    if (edt_tb_year_family.getText().toString().isEmpty()) {
                        edt_tb_year_family.setError("Enter Year");
                        return;
                    }
                    summary.setSinceMonth(edt_tb_month_family.getText().toString());
                    summary.setSinceYear(edt_tb_year_family.getText().toString());
                    familyHealthList.add(summary);
                } else if (!rb_tb_yes_family.isChecked()) {
                    InsertHealthHistoryModel summary = new InsertHealthHistoryModel();
                    summary.setPE_TestID("7");
                    summary.setTestStatus("0");
                    summary.setSince("0");
                    summary.setSinceYear("0");
                    summary.setSinceMonth("0");
                    familyHealthList.add(summary);
                }
                jsonStringFamily = gson.toJson(familyHealthList);

                /** Family History End */

                regdId = Integer.parseInt(RegdId);
                campId = String.valueOf(CampId);
                lungClear = LungClear;
                lungComment = edt_nolungs.getText().toString();
                OtherAbnormality = "";
                abnormalSound = AbnormalSound;
                abnormalComment = edt_abnormalcomment.getText().toString();
                s1s2Normal = SIS2Normal;
                anyMurmurs = AnyMurmursID;
                palpationabdomen = Palpationabdomen;
                anyhernia = Anyhernia;
                massperAbdomen = MassperAbdomen;
                patwellOriented = PatwellOriented;
                sensory = Sensory;
                motor = Motor;


                finalRemark = finalRemarks;
                finalComment = edt_finalcomment.getText().toString();
                Createdby = Integer.parseInt(userID);
                jsonstring = jsonstrings;

//                if (respiratoryId == null) {
//                    edt_abnormalcomment.setError("Please select comment");
//                    edt_abnormalcomment.requestFocus();
//                    return;
//                }
//
//                if (respiratoryId.equals("1")) {
//                    if (edt_abnormalcomment.getText().toString().trim().isEmpty()) {
//                        edt_abnormalcomment.setError("Please select comment");
//                        edt_abnormalcomment.requestFocus();
//                        return;
//                    }
//                    abnormalComment = edt_abnormalcomment.getText().toString();
//                } else {
//                    if (edt_otherrespiratorycomment.getText().toString().trim().isEmpty()) {
//                        edt_otherrespiratorycomment.setError("Please enter other comment");
//                        edt_otherrespiratorycomment.requestFocus();
//                        return;
//                    }
//                    abnormalComment = edt_otherrespiratorycomment.getText().toString();
//                }
//
//                if (cardioId == null) {
//                    edt_cardiocomment.setError("Please select comment");
//                    edt_cardiocomment.requestFocus();
//                    return;
//                }
//
//                if (cardioId.equals("1")) {
//                    if (edt_cardiocomment.getText().toString().trim().isEmpty()) {
//                        edt_cardiocomment.setError("Please select comment");
//                        edt_cardiocomment.requestFocus();
//                        return;
//                    }
//                    cvsComment = edt_cardiocomment.getText().toString();
//                } else {
//                    if (edt_othercardiocomment.getText().toString().trim().isEmpty()) {
//                        edt_othercardiocomment.setError("Please enter other comment");
//                        edt_othercardiocomment.requestFocus();
//                        return;
//                    }
//                    cvsComment = edt_othercardiocomment.getText().toString();
//                }
//
//                if (gastroId == null) {
//                    edt_gastrocomment.setError("Please select comment");
//                    edt_gastrocomment.requestFocus();
//                    return;
//                }
//
//                if (gastroId.equals("1")) {
//                    if (edt_gastrocomment.getText().toString().trim().isEmpty()) {
//                        edt_gastrocomment.setError("Please select comment");
//                        edt_gastrocomment.requestFocus();
//                        return;
//                    }
//                    GITComment = edt_gastrocomment.getText().toString();
//                } else {
//                    if (edt_othergastrocomment.getText().toString().trim().isEmpty()) {
//                        edt_othergastrocomment.setError("Please enter other comment");
//                        edt_othergastrocomment.requestFocus();
//                        return;
//                    }
//                    GITComment = edt_othergastrocomment.getText().toString();
//                }
//
//                if (sevousSystemId == null) {
//                    edt_nrevoussystemcomment.setError("Please select comment");
//                    edt_nrevoussystemcomment.requestFocus();
//                    return;
//                }
//
//                if (sevousSystemId.equals("1")) {
//                    if (edt_nrevoussystemcomment.getText().toString().trim().isEmpty()) {
//                        edt_nrevoussystemcomment.setError("Please select comment");
//                        edt_nrevoussystemcomment.requestFocus();
//                        return;
//                    }
//                    cnsComment = edt_nrevoussystemcomment.getText().toString();
//                } else {
//                    if (edt_othernrevoussystemcomment.getText().toString().trim().isEmpty()) {
//                        edt_othernrevoussystemcomment.setError("Please enter other comment");
//                        edt_othernrevoussystemcomment.requestFocus();
//                        return;
//                    }
//                    cnsComment = edt_othernrevoussystemcomment.getText().toString();
//                }
//
//                if (skinId == null) {
//                    edt_skin.setError("Please select comment");
//                    edt_skin.requestFocus();
//                    return;
//                }
//
//                if (skinId.equals("1")) {
//                    if (edt_skin.getText().toString().trim().isEmpty()) {
//                        edt_skin.setError("Please select comment");
//                        edt_skin.requestFocus();
//                        return;
//                    }
//                    skinInfection = edt_skin.getText().toString();
//                } else {
//                    if (edt_otherskin.getText().toString().trim().isEmpty()) {
//                        edt_otherskin.setError("Please enter other comment");
//                        edt_otherskin.requestFocus();
//                        return;
//                    }
//                    skinInfection = edt_otherskin.getText().toString();
//                }
//
//                if (gneder.equalsIgnoreCase("female")) {
//
//                    if (skinId == null) {
//                        edt_skin.setError("Please select remark");
//                        edt_othergynccomment.requestFocus();
//                        return;
//                    }
//
//                    if (gyncId.equals("1")) {
//                        if (edt_gynccomment.getText().toString().trim().isEmpty()) {
//                            edt_gynccomment.setError("Please select comment");
//                            edt_gynccomment.requestFocus();
//                            return;
//                        }
//                        gynaComment = edt_gynccomment.getText().toString();
//                    } else {
//                        if (edt_othergynccomment.getText().toString().trim().isEmpty()) {
//                            edt_othergynccomment.setError("Please enter other comment");
//                            edt_othergynccomment.requestFocus();
//                            return;
//                        }
//                        gynaComment = edt_othergynccomment.getText().toString();
//                    }
//                }
//
//
//                if (edt_lungsfield.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select Clear Lung Fields", context, false);
//                    return;
//                } else if (edt_abnormalsound.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Abnormal Sound", context, false);
//                    return;
//                } else if (edt_abnormalcomment.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Enter Comment", context, false);
//                    return;
//                } else if (edt_s1s2normal.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select S1 S2 Normal", context, false);
//                    return;
//                } else if (edt_anyotherabnormalityormurmurs.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select Any Abnormality Or Any Numerous", context, false);
//                    return;
//                } else if (edt_cardiocomment.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Enter Cardio Vascular System Comment", context, false);
//                    return;
//                } else if (edt_tenderness.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select Tenderness on Palpation of Abdomen", context, false);
//                    return;
//                } else if (edt_anyherina.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select Any hernia", context, false);
//                    return;
//                } else if (edt_anymassperabdomen.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select Any Mass Per Abdomen", context, false);
//                    return;
//                } else if (edt_gastrocomment.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Enter Gastro Intestinal System Comment", context, false);
//                    return;
//                } else if (edt_patientwelloriented.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select Is Patinet well Oriented", context, false);
//                    return;
//                } else if (edt_sensory.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select Sensory", context, false);
//                    return;
//                } else if (edt_motor.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Select Motor", context, false);
//                    return;
//                } else if (edt_nrevoussystemcomment.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Enter Central Nervous System Comment", context, false);
//                    return;
//                } else if (edt_skin.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Enter Skin Infection", context, false);
//                    return;
//                } else if (edt_finalcomment.getText().toString().equals("")) {
//                    Utilities.showToastMessage("Please Enter final Comment", context, false);
//                    return;
//                } else if (finalRemarks == 0) {
//                    Utilities.showToastMessage("Please Select Final Remark", context, false);
//                    return;
//                } else if (s1s2Normal == 0) {
//                    Utilities.showToastMessage("Please Select S1S2Normal", context, false);
//                    return;
//                } else if (anyMurmurs == 0) {
//                    Utilities.showToastMessage("Please Select Any Murmurs", context, false);
//                    return;
//                } else if (palpationabdomen == 0) {
//                    Utilities.showToastMessage("Please Select Tenderness on Palpation of Abdomen", context, false);
//                    return;
//                } else if (anyhernia == 0) {
//                    Utilities.showToastMessage("Please Select Any Hernia", context, false);
//                    return;
//                } else if (massperAbdomen == 0) {
//                    Utilities.showToastMessage("Please Select Any Mass Per Abdomen", context, false);
//                    return;
//                } else if (patwellOriented == 0) {
//                    Utilities.showToastMessage("Please Select Is patient well oriented", context, false);
//                    return;
//                } else if (sensory == 0) {
//                    Utilities.showToastMessage("Please Select Sensory", context, false);
//                    return;
//                } else if (motor == 0) {
//                    Utilities.showToastMessage("Please Select Motor", context, false);
//                    return;
//                }

                if (edt_finalcomment.getText().toString().equals("")) {
                    Utilities.showToastMessage("Please Enter final Comment", context, false);
                    return;
                } else if (finalRemarks == 0) {
                    Utilities.showToastMessage("Please Select Final Remark", context, false);
                    return;
                }
                if (Utilities.isNetworkAvailable(context))
//                    if (gneder.equalsIgnoreCase("male"))
//                        new InsertBasicInfoMale().execute();

                    if (BuildConfig.isBeta) {

                        insertBasicInfoMaleV1VersionNumber();
                    } else {
                        insertBasicInfoMaleV1VersionNumber();

//                        insertBasicInfoMaleV1();


                    }
//                    else if (gneder.equalsIgnoreCase("female")) {
//                        if (edt_pmp.getText().toString().equals("")) {
//                            Utilities.showToastMessage("Please Select PMP", context, false);
//                            return;
//                        } else if (edt_gynacproblem.getText().toString().equals("")) {
//                            Utilities.showToastMessage("Please Select Any gnyaecological Problem", context, false);
//                            return;
//                        } else if (edt_breastcancer.getText().toString().equals("")) {
//                            Utilities.showToastMessage("Please Select Any Evidence of breasr Cancer", context, false);
//                            return;
//                        } else if (PMP == 0) {
//                            Utilities.showToastMessage("Please Select PMP", context, false);
//                            return;
//                        } else if (GynecProblem == 0) {
//                            Utilities.showToastMessage("Please Select Any gnyaecological Problem", context, false);
//                            return;
//                        } else if (ISBreastCancer == 0) {
//                            Utilities.showToastMessage("Please Select Any Evidence of breasr Cancer", context, false);
//                            return;
//                        } else if (FamillyPlaning == 0) {
//                            Utilities.showToastMessage("Please Select Any Family Planning Operation Done", context, false);
//                            return;
//                        } else {
//                            new InsertBasicInfoMale().execute();
//                            insertBasicInfoMaleV1();
//                            new InsertBasicInfoFeMale().execute();
//                        }

//                    } else
//                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);

                break;
            }

        }
    }

    private void showResultDialog(ArrayList<MasterModel> masterList, int type) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Comment");
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
                MasterModel masterDetails = masterList.get(which);
                switch (type) {
                    case 1:
                        respiratoryId = masterDetails.getId();
                        if (masterDetails.getId().equals("1")) {
                            edt_abnormalcomment.setText(masterDetails.getName());
                            edt_otherrespiratorycomment.setVisibility(View.GONE);
                        } else {
                            edt_abnormalcomment.setText(masterDetails.getName());
                            edt_otherrespiratorycomment.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        cardioId = masterDetails.getId();
                        if (masterDetails.getId().equals("1")) {
                            edt_cardiocomment.setText(masterDetails.getName());
                            edt_othercardiocomment.setVisibility(View.GONE);
                        } else {
                            edt_cardiocomment.setText(masterDetails.getName());
                            edt_othercardiocomment.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 3:
                        gastroId = masterDetails.getId();
                        if (masterDetails.getId().equals("1")) {
                            edt_gastrocomment.setText(masterDetails.getName());
                            edt_othergastrocomment.setVisibility(View.GONE);
                        } else {
                            edt_gastrocomment.setText(masterDetails.getName());
                            edt_othergastrocomment.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 4:
                        sevousSystemId = masterDetails.getId();
                        if (masterDetails.getId().equals("1")) {
                            edt_nrevoussystemcomment.setText(masterDetails.getName());
                            edt_othernrevoussystemcomment.setVisibility(View.GONE);
                        } else {
                            edt_nrevoussystemcomment.setText(masterDetails.getName());
                            edt_othernrevoussystemcomment.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 5:
                        skinId = masterDetails.getId();
                        if (masterDetails.getId().equals("1")) {
                            edt_skin.setText(masterDetails.getName());
                            edt_otherskin.setVisibility(View.GONE);
                        } else {
                            edt_skin.setText(masterDetails.getName());
                            edt_otherskin.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 6:
                        gyncId = masterDetails.getId();
                        if (masterDetails.getId().equals("1")) {
                            edt_gynccomment.setText(masterDetails.getName());
                            edt_othergynccomment.setVisibility(View.GONE);
                        } else {
                            edt_gynccomment.setText(masterDetails.getName());
                            edt_othergynccomment.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        });
        builderSingle.show();
    }

    private void openDialog(String que) {
        String[] popupList = {"Yes", "No"};

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context, 1);
        builderSingle.setTitle("Please Select");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        for (String s : popupList) {
            arrayAdapter.add(s);
        }

        builderSingle.setNegativeButton(
                "Cancel",
                (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            switch (que) {
                case "q1":
                    que1 = popupList[which];
                    edt_lungsfield.setText(que1);
                    if (que1.equalsIgnoreCase("Yes")) {
                        LungClear = 1;
                        edt_lungsfield.setText(que1);
                        edt_nolungs.setVisibility(View.GONE);
                    } else if (que1.equalsIgnoreCase("No")) {
                        LungClear = 2;
                        edt_lungsfield.setText(que1);
                        edt_nolungs.setVisibility(View.VISIBLE);
                    } else
                        edt_nolungs.setVisibility(View.GONE);
                    break;
                case "q2":
                    que2 = popupList[which];
                    break;
                case "q3":
                    que3 = popupList[which];
                    edt_s1s2normal.setText(que3);
                    if (que3.equalsIgnoreCase("Yes"))
                        SIS2Normal = 1;
                    else
                        SIS2Normal = 2;
                    break;
                case "q4":
                    que4 = popupList[which];
                    edt_anyotherabnormalityormurmurs.setText(que4);
                    if (que4.equalsIgnoreCase("Yes")) {
                        AnyMurmursID = 1;
                        edt_anyotherabnormalityormurmurs.setText(que4);
                    } else {
                        AnyMurmursID = 2;
                        edt_anyotherabnormalityormurmurs.setText(que4);
                    }
                    break;
                case "q5":
                    que5 = popupList[which];
                    edt_tenderness.setText(que5);
                    if (que5.equalsIgnoreCase("yes")) {
                        Palpationabdomen = 1;
                        edt_tenderness.setText(que5);
                    } else {
                        Palpationabdomen = 2;
                        edt_tenderness.setText(que5);
                    }
                    break;
                case "q6":
                    que6 = popupList[which];
                    if (que6.equalsIgnoreCase("Yes")) {
                        Anyhernia = 1;
                        edt_anyherina.setText(que6);
                    } else {
                        Anyhernia = 2;
                        edt_anyherina.setText(que6);
                    }
                    break;
                case "q7":
                    que7 = popupList[which];
                    if (que7.equalsIgnoreCase("Yes")) {
                        MassperAbdomen = 1;
                        edt_anymassperabdomen.setText(que7);
                    } else {
                        MassperAbdomen = 2;
                        edt_anymassperabdomen.setText(que7);
                    }
                    break;
                case "q8":
                    que8 = popupList[which];
                    if (que8.equalsIgnoreCase("Yes")) {
                        PatwellOriented = 1;
                        edt_patientwelloriented.setText(que8);
                    } else {
                        PatwellOriented = 2;
                        edt_patientwelloriented.setText(que8);
                    }
                    break;
                case "q9":
                    que9 = popupList[which];
                    break;
                case "q10":
                    que10 = popupList[which];
                    if (que10.equalsIgnoreCase("Yes")) {
                        PMP = 1;
                        edt_pmp.setText(que10);
                    } else {
                        PMP = 2;
                        edt_pmp.setText(que10);
                    }
                    break;
                case "q11":
                    que11 = popupList[which];
                    if (que11.equalsIgnoreCase("yes")) {
                        GynecProblem = 1;
                        edt_gynacproblem.setText(que11);
                    } else {
                        GynecProblem = 2;
                        edt_gynacproblem.setText(que11);
                    }
                    break;
                case "q12":
                    que12 = popupList[which];
                    if (que12.equalsIgnoreCase("Yes")) {
                        ISBreastCancer = 1;
                        edt_breastcancer.setText(que12);
                    } else {
                        ISBreastCancer = 2;
                        edt_breastcancer.setText(que12);
                    }
                    break;
                case "q13":
                    que13 = popupList[which];
                    if (que13.equalsIgnoreCase("Yes")) {
                        FamillyPlaning = 1;
                        edt_familyplanning.setText(que13);
                    } else {
                        FamillyPlaning = 2;
                        edt_familyplanning.setText(que13);
                    }
                    break;
                case "q14":
                    que14 = popupList[which];
                    if (que14.equalsIgnoreCase("Yes")) {
                        RecentDelivery = 1;
                        edt_recentdelivery.setText(que14);
                    } else {
                        RecentDelivery = 2;
                        edt_recentdelivery.setText(que14);
                    }
                    break;
                case "q15":
                    que15 = popupList[which];
                    break;
            }
        });
        builderSingle.show();
    }

    public void AbnormalSoundDialogCreater() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog_title, null);
        builderSingle.setTitle("Select Any one");
        builderSingle.setCustomTitle(view);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
        arrayAdapter.add("Normal");
        arrayAdapter.add("Crepitation");
        arrayAdapter.add("Wheezing");
        arrayAdapter.add("Rhonchi");

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Normal")) {
                            AbnormalSound = 1;
                            edt_abnormalsound.setText(strName);
                        } else if (strName.equalsIgnoreCase("Crepitation")) {
                            AbnormalSound = 2;
                            edt_abnormalsound.setText(strName);
                        } else if (strName.equalsIgnoreCase("Wheezing")) {
                            AbnormalSound = 3;
                            edt_abnormalsound.setText(strName);
                        } else if (strName.equalsIgnoreCase("Rhonchi")) {
                            AbnormalSound = 4;
                            edt_abnormalsound.setText(strName);
                        }
//                        Toast.makeText(context,strName+" "+AbnormalSound,Toast.LENGTH_LONG).show();
                    }
                });
        builderSingle.show();
    }

    public void AbnormalNormalDialogCreater(String name) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog_title, null);
        builderSingle.setTitle("Select Any one");
        builderSingle.setCustomTitle(view);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
        arrayAdapter.add("Normal");
        arrayAdapter.add("Abnormal");

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (name.equalsIgnoreCase("Sensory")) {
                            if (strName.equalsIgnoreCase("Normal")) {
                                Sensory = 1;
                                edt_sensory.setText(strName);
                            } else if (strName.equalsIgnoreCase("Abnormal")) {
                                Sensory = 2;
                                edt_sensory.setText(strName);
                            }
                        } else if (name.equalsIgnoreCase("Motor")) {
                            if (strName.equalsIgnoreCase("Normal")) {
                                Motor = 1;
                                edt_motor.setText(strName);
                            } else if (strName.equalsIgnoreCase("Abnormal")) {
                                Motor = 2;
                                edt_motor.setText(strName);
                            }

                        }

//                        Toast.makeText(context,strName+" "+abnormalSoundId,Toast.LENGTH_LONG).show();
                    }
                });
        builderSingle.show();
    }

    public void HospitalDialogCreater(ArrayList<SelectAddress_OutPut_pojo> hospitalList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog_title, null);
        builderSingle.setTitle("Select Hospital");
        builderSingle.setCustomTitle(view);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
        arrayAdapter.add("Normal");
        arrayAdapter.add("Moderate");
        arrayAdapter.add("Mild");
        arrayAdapter.add("Serve");

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Normal")) {
                            finalRemarks = 1;
                            edt_finalremark.setText(strName);
                        } else if (strName.equalsIgnoreCase("Moderate")) {
                            finalRemarks = 2;
                            edt_finalremark.setText(strName);
                        } else if (strName.equalsIgnoreCase("Mild")) {
                            finalRemarks = 3;
                            edt_finalremark.setText(strName);
                        } else if (strName.equalsIgnoreCase("Serve")) {
                            finalRemarks = 4;
                            edt_finalremark.setText(strName);
                        }
//                        Toast.makeText(context,strName+" "+abnormalSoundId,Toast.LENGTH_LONG).show();
                    }
                });
        builderSingle.show();
    }

    public void FinalRemarkDialogCreater() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog_title, null);
        builderSingle.setTitle("Select Any one");
        builderSingle.setCustomTitle(view);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
        arrayAdapter.add("Normal");
        arrayAdapter.add("Moderate");
        arrayAdapter.add("Mild");
        arrayAdapter.add("Severe");

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Normal")) {
                            finalRemarks = 1;
                            edt_finalremark.setText(strName);
                        } else if (strName.equalsIgnoreCase("Moderate")) {
                            finalRemarks = 2;
                            edt_finalremark.setText(strName);
                        } else if (strName.equalsIgnoreCase("Mild")) {
                            finalRemarks = 3;
                            edt_finalremark.setText(strName);
                        } else if (strName.equalsIgnoreCase("Severe")) {
                            finalRemarks = 4;
                            edt_finalremark.setText(strName);
                        }
//                        Toast.makeText(context,strName+" "+abnormalSoundId,Toast.LENGTH_LONG).show();
                    }
                });
        builderSingle.show();
    }

    private void getPatientDetails() {
        if (Utilities.isNetworkAvailable(context)) {
            if (patientDetails.getGender().equalsIgnoreCase("F")) {
                new GetBreastScreeningDetails().execute(String.valueOf(patientDetails.getRegdId()));
            } else {
                cv_breast_screening.setVisibility(View.GONE);
            }
            new GetM_PatinetHealthHistoryDetails().execute();
        } else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
    }

    private void getMedicalehistory() {
        if (Utilities.isNetworkAvailable(context)) {
            new GetM_HealthHistoryDetails().execute();
        } else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        return;
    }

    private class GetM_PatinetHealthHistoryDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", RegdId));
            res = WebServiceCall.APICall(ApplicationConstants.GetUserDataforPhysicalExamnination, ApplicationConstants.webservice, param);
            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    PatientMedicaleHistory_Pojo pojoDetails = new Gson().fromJson(result, PatientMedicaleHistory_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        patinetHistoryList = pojoDetails.getOutput();
                        gneder = patinetHistoryList.get(0).getGender();
//                        if (gneder.equalsIgnoreCase("Male")) {
//                            ll_gynac1.setVisibility(View.GONE);
//                            ll_gynac2.setVisibility(View.GONE);
//                            ll_gyanc3.setVisibility(View.GONE);
//                            ll_gynac4.setVisibility(View.GONE);
//                            tv_gynaecologicalexamination.setVisibility(View.GONE);
//                            edt_recentdelivery.setVisibility(View.GONE);
//                            edt_recentdelivercomment.setVisibility(View.GONE);
//                            edt_familyplanningadvice.setVisibility(View.GONE);
//                        } else {
//                            ll_gynac1.setVisibility(View.VISIBLE);
//                            ll_gynac2.setVisibility(View.VISIBLE);
//                            ll_gyanc3.setVisibility(View.VISIBLE);
//                            ll_gynac4.setVisibility(View.VISIBLE);
//                            tv_gynaecologicalexamination.setVisibility(View.VISIBLE);
//                            edt_recentdelivery.setVisibility(View.VISIBLE);
//                            edt_recentdelivercomment.setVisibility(View.VISIBLE);
//                            edt_familyplanningadvice.setVisibility(View.VISIBLE);
//                        }
                        edt_patientname.setText(patinetHistoryList.get(0).getBeneficiaryName());
                        edt_beneficiaryno.setText(patinetHistoryList.get(0).getBeneficiaryRegdNo());
                        edt_benefgender.setText(patinetHistoryList.get(0).getGender());
                        edt_beneficiaryage.setText(patinetHistoryList.get(0).getAge());
                        edt_height.setText(patinetHistoryList.get(0).getHeight_CMs().replace(".00", ""));
                        edt_weight.setText(patinetHistoryList.get(0).getWeight_KGs().replace(".00", ""));
                        edt_bmi.setText(patinetHistoryList.get(0).getBMI());
                        edt_category.setText(patinetHistoryList.get(0).getBMIStatus());
                        edt_mobileno.setText(patinetHistoryList.get(0).getMobNo());
                        edt_beneficiarymaritalstatus.setText(patinetHistoryList.get(0).getMaritalStatus());
                        edt_noofchild.setText(patinetHistoryList.get(0).getNoOfChildren());
                        edt_blgr.setText(patinetHistoryList.get(0).getBloodGroup());
                        edt_smoking.setText(patinetHistoryList.get(0).getSmoking());
                        edt_smoking_since_year.setText(patinetHistoryList.get(0).getSmokingSinceYear());
                        edt_smoking_since_months.setText(patinetHistoryList.get(0).getSmokingSinceMonth());
                        edt_alcohol.setText(patinetHistoryList.get(0).getAlcohol());
                        edt_alcohol_since_year.setText(patinetHistoryList.get(0).getAlcoholSinceYear());
                        edt_alcohol_since_months.setText(patinetHistoryList.get(0).getAlcoholSinceMonth());
                        edt_tobacco.setText(patinetHistoryList.get(0).getTobaco());
                        edt_tobacco_since_year.setText(patinetHistoryList.get(0).getTobacoSinceYear());
                        edt_tobacco_since_months.setText(patinetHistoryList.get(0).getTobacoSinceMonth());
                        edt_drugs.setText(patinetHistoryList.get(0).getIsDrug());
                        edt_drugs_since_year.setText(patinetHistoryList.get(0).getDrugSinceYear());
                        edt_drugs_since_months.setText(patinetHistoryList.get(0).getDrugSinceMonth());
                        edtSystolic.setText(patinetHistoryList.get(0).getSystolic());
                        edtDiastolic.setText(patinetHistoryList.get(0).getDiastolic());
                        edt_f.setText(patinetHistoryList.get(0).getBloodSugar_F());
                        edt_pp.setText(patinetHistoryList.get(0).getBloodSugar_PP());
                        edtSugar.setText(patinetHistoryList.get(0).getBloodSugar_R());
                        edt_pulseratebpm.setText(patinetHistoryList.get(0).getPulseRate());
                        edt_temperature.setText(patinetHistoryList.get(0).getTemperature());
                        edt_spo2.setText(patinetHistoryList.get(0).getSPO2());
                        try {
                            // Get the current date
                            Calendar currentDate = Calendar.getInstance(Locale.ENGLISH);
                            int age = Integer.parseInt(patinetHistoryList.get(0).getAge());
                            // Subtract the age from the current date to get the date of birth
                            currentDate.add(Calendar.YEAR, -age);
                            // Get the date of birth as a Date object
                            Date dateOfBirth = currentDate.getTime();
                            edt_beneficiary_dob.setText(Utilities.dfDate2.format(dateOfBirth));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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

    private class GetBreastScreeningDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", RegdId));
            res = WebServiceCall.APICall(ApplicationConstants.GetBreastScreeningDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "";
            try {
                if (!result.equals("")) {
                    BreastScreeningModel pojoDetails = new Gson().fromJson(result, BreastScreeningModel.class);
                    type = pojoDetails.getStatus();
                    if (type.equalsIgnoreCase("success")) {
                        List<BreastScreeningModel.OutputBean> patinetHistoryList = pojoDetails.getOutput();
                        reportUrl = patinetHistoryList.get(0).getReportPhoto();
                        cv_breast_screening.setVisibility(View.VISIBLE);
                        edt_cancer_history.setText(patinetHistoryList.get(0).getCancerHisComment());

                        edt_lumb_observed.setText(patinetHistoryList.get(0).getLumpObservation());

                        edt_personal_observation.setText(patinetHistoryList.get(0).getPSObservation());

                        if (patinetHistoryList.get(0).getPSObservation().equalsIgnoreCase("Yes")) {
                            edt_comment.setVisibility(View.VISIBLE);
                            edt_comment.setText(patinetHistoryList.get(0).getPSComment());
                        } else if (patinetHistoryList.get(0).getPSObservation().equalsIgnoreCase("No")) {
                            edt_comment.setVisibility(View.GONE);
                            edt_comment.setText("");
                        }

                        if (reportUrl.equalsIgnoreCase("")) {
                            tv_view_report.setVisibility(View.GONE);
                        }

                        edt_final_remark.setText(patinetHistoryList.get(0).getFinalRemark());
                    } else {
                        cv_breast_screening.setVisibility(View.GONE);
                    }
                } else {
                    cv_breast_screening.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
                cv_breast_screening.setVisibility(View.GONE);
            }
        }
    }

    void insertBasicInfoMaleV1() {
        List<ParamsPojo> param = new ArrayList<ParamsPojo>();
        param.add(new ParamsPojo("RegdId", String.valueOf(regdId)));
        param.add(new ParamsPojo("CampId", campId));
        param.add(new ParamsPojo("LungClear", String.valueOf(lungClear)));
        param.add(new ParamsPojo("LungComment", lungComment));
        param.add(new ParamsPojo("AbnormalSound", String.valueOf(abnormalSound)));
        param.add(new ParamsPojo("AbnormalComment", abnormalComment));
        param.add(new ParamsPojo("OtherAbnormality", ""));
        param.add(new ParamsPojo("SIS2Normal", String.valueOf(s1s2Normal)));
        param.add(new ParamsPojo("AnyMurmurs", String.valueOf(anyMurmurs)));
        param.add(new ParamsPojo("CVSComment", cvsComment));
        param.add(new ParamsPojo("Palpationabdomen", String.valueOf(palpationabdomen)));
        param.add(new ParamsPojo("Anyhernia", String.valueOf(anyhernia)));
        param.add(new ParamsPojo("MassperAbdomen", String.valueOf(massperAbdomen)));
        param.add(new ParamsPojo("GITComment", GITComment));
        param.add(new ParamsPojo("PatwellOriented", String.valueOf(patwellOriented)));
        param.add(new ParamsPojo("Sensory", String.valueOf(sensory)));
        param.add(new ParamsPojo("Motor", String.valueOf(motor)));
        param.add(new ParamsPojo("SkinInfection", skinInfection));
        param.add(new ParamsPojo("CNSComment", cnsComment));
        param.add(new ParamsPojo("FinalRemark", String.valueOf(finalRemark)));
        param.add(new ParamsPojo("FinalComment", finalComment));
        param.add(new ParamsPojo("Createdby", userID));
        param.add(new ParamsPojo("jsonstring", jsonstring));
        param.add(new ParamsPojo("IsCovid", rbIsCovid.isChecked() ? "1" : "0"));
        param.add(new ParamsPojo("IsDose1", rbIsCovidDose1.isChecked() ? "1" : "0"));
        param.add(new ParamsPojo("IsDose2", rbIsCovidDose1.isChecked() ? "1" : "0"));
        param.add(new ParamsPojo("IsBoosterDose", rbIsBoosterDose.isChecked() ? "1" : "0"));
        param.add(new ParamsPojo("jsonstringFamily", jsonStringFamily));
        Log.i("Param", "jsonstring: " + jsonstring);
        Log.i("Param", "jsonStringFamily: " + jsonStringFamily);
        Log.i("Param", "doInBackground: " + new Gson().toJson(param));
        skinInfection = "";
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();

//        versionName
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.insertBasicInfoMale_V1(regdId, Integer.parseInt(campId), lungClear, lungComment, abnormalSound, abnormalComment, 0, s1s2Normal, anyMurmurs, cvsComment, palpationabdomen, anyhernia, massperAbdomen, GITComment, patwellOriented, sensory, motor, skinInfection, cnsComment, finalRemark, finalComment, Integer.parseInt(userID), jsonstring, rbIsCovid.isChecked() ? 1 : 0, rbIsCovidDose1.isChecked() ? 1 : 0, rbIsCovidDose1.isChecked() ? 1 : 0, rbIsBoosterDose.isChecked() ? 1 : 0, jsonStringFamily).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    try {
                        pd.dismiss();
                        String result = response.body().string();
                        Log.i("TAG", "onPostExecute: " + result);

                        if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("Success")) {
//                                if (gneder.equalsIgnoreCase("female")) {
//                                    new InsertBasicInfoFeMale().execute();
//                                } else {
                                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.icon_success);
                                builder.setTitle("Success");
                                builder.setCancelable(false);
                                builder.setMessage(message);
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

                } else {
                    try {
                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();

                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });
    }

    void insertBasicInfoMaleV1VersionNumber() {
        List<ParamsPojo> param = new ArrayList<ParamsPojo>();
        param.add(new ParamsPojo("RegdId", String.valueOf(regdId)));
        param.add(new ParamsPojo("CampId", campId));
        param.add(new ParamsPojo("LungClear", String.valueOf(lungClear)));
        param.add(new ParamsPojo("LungComment", lungComment));
        param.add(new ParamsPojo("AbnormalSound", String.valueOf(abnormalSound)));
        param.add(new ParamsPojo("AbnormalComment", abnormalComment));
        param.add(new ParamsPojo("OtherAbnormality", ""));
        param.add(new ParamsPojo("SIS2Normal", String.valueOf(s1s2Normal)));
        param.add(new ParamsPojo("AnyMurmurs", String.valueOf(anyMurmurs)));
        param.add(new ParamsPojo("CVSComment", cvsComment));
        param.add(new ParamsPojo("Palpationabdomen", String.valueOf(palpationabdomen)));
        param.add(new ParamsPojo("Anyhernia", String.valueOf(anyhernia)));
        param.add(new ParamsPojo("MassperAbdomen", String.valueOf(massperAbdomen)));
        param.add(new ParamsPojo("GITComment", GITComment));
        param.add(new ParamsPojo("PatwellOriented", String.valueOf(patwellOriented)));
        param.add(new ParamsPojo("Sensory", String.valueOf(sensory)));
        param.add(new ParamsPojo("Motor", String.valueOf(motor)));
        param.add(new ParamsPojo("SkinInfection", skinInfection));
        param.add(new ParamsPojo("CNSComment", cnsComment));
        param.add(new ParamsPojo("FinalRemark", String.valueOf(finalRemark)));
        param.add(new ParamsPojo("FinalComment", finalComment));
        param.add(new ParamsPojo("Createdby", userID));
        param.add(new ParamsPojo("jsonstring", jsonstring));
        param.add(new ParamsPojo("IsCovid", rbIsCovid.isChecked() ? "1" : "0"));
        param.add(new ParamsPojo("IsDose1", rbIsCovidDose1.isChecked() ? "1" : "0"));
        param.add(new ParamsPojo("IsDose2", rbIsCovidDose1.isChecked() ? "1" : "0"));
        param.add(new ParamsPojo("IsBoosterDose", rbIsBoosterDose.isChecked() ? "1" : "0"));
        param.add(new ParamsPojo("VersionNo", versionName));
        param.add(new ParamsPojo("jsonstringFamily", jsonStringFamily));
        Log.i("Param", "jsonstring: " + jsonstring);
        Log.i("Param", "jsonStringFamily: " + jsonStringFamily);
        Log.i("Param", "doInBackground: " + new Gson().toJson(param));
        skinInfection = "";
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();

//        versionName
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.insertBasicInfoMale_V1VersionNumber(regdId, Integer.parseInt(campId), lungClear, lungComment, abnormalSound, abnormalComment, 0, s1s2Normal, anyMurmurs, cvsComment, palpationabdomen, anyhernia, massperAbdomen, GITComment, patwellOriented, sensory, motor, skinInfection, cnsComment, finalRemark, finalComment, Integer.parseInt(userID), jsonstring, rbIsCovid.isChecked() ? 1 : 0, rbIsCovidDose1.isChecked() ? 1 : 0, rbIsCovidDose1.isChecked() ? 1 : 0, rbIsBoosterDose.isChecked() ? 1 : 0, jsonStringFamily,versionName).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    try {
                        pd.dismiss();
                        String result = response.body().string();
                        Log.i("TAG", "onPostExecute: " + result);

                        if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("Success")) {
//                                if (gneder.equalsIgnoreCase("female")) {
//                                    new InsertBasicInfoFeMale().execute();
//                                } else {
                                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.icon_success);
                                builder.setTitle("Success");
                                builder.setCancelable(false);
                                builder.setMessage(message);
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

                } else {
                    try {
                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();

                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });
    }

    public class InsertBasicInfoMale extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", String.valueOf(regdId)));
            param.add(new ParamsPojo("CampId", campId));
            param.add(new ParamsPojo("LungClear", String.valueOf(lungClear)));
            param.add(new ParamsPojo("LungComment", lungComment));
            param.add(new ParamsPojo("AbnormalSound", String.valueOf(abnormalSound)));
            param.add(new ParamsPojo("AbnormalComment", abnormalComment));
            param.add(new ParamsPojo("OtherAbnormality", ""));
            param.add(new ParamsPojo("SIS2Normal", String.valueOf(s1s2Normal)));
            param.add(new ParamsPojo("AnyMurmurs", String.valueOf(anyMurmurs)));
            param.add(new ParamsPojo("CVSComment", cvsComment));
            param.add(new ParamsPojo("Palpationabdomen", String.valueOf(palpationabdomen)));
            param.add(new ParamsPojo("Anyhernia", String.valueOf(anyhernia)));
            param.add(new ParamsPojo("MassperAbdomen", String.valueOf(massperAbdomen)));
            param.add(new ParamsPojo("GITComment", GITComment));
            param.add(new ParamsPojo("PatwellOriented", String.valueOf(patwellOriented)));
            param.add(new ParamsPojo("Sensory", String.valueOf(sensory)));
            param.add(new ParamsPojo("Motor", String.valueOf(motor)));
            param.add(new ParamsPojo("SkinInfection", skinInfection));
            param.add(new ParamsPojo("CNSComment", cnsComment));
            param.add(new ParamsPojo("FinalRemark", String.valueOf(finalRemark)));
            param.add(new ParamsPojo("FinalComment", finalComment));
            param.add(new ParamsPojo("Createdby", userID));
            param.add(new ParamsPojo("jsonstring", jsonstring));
            param.add(new ParamsPojo("IsCovid", rbIsCovid.isChecked() ? "1" : "0"));
            param.add(new ParamsPojo("IsDose1", rbIsCovidDose1.isChecked() ? "1" : "0"));
            param.add(new ParamsPojo("IsDose2", rbIsCovidDose1.isChecked() ? "1" : "0"));
            param.add(new ParamsPojo("IsBoosterDose", rbIsBoosterDose.isChecked() ? "1" : "0"));
            param.add(new ParamsPojo("jsonstringFamily", jsonStringFamily));
            Log.i("Param", "doInBackground: " + jsonstring);
            Log.i("Param", "doInBackground: " + jsonStringFamily);
            Log.i("Param", "doInBackground: " + new Gson().toJson(param));
            res = WebServiceCall.APICall(ApplicationConstants.InsertBasicInfoMale_V1, ApplicationConstants.webservice_d2d, param);


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                Log.i("TAG", "onPostExecute: " + result);

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        if (gneder.equalsIgnoreCase("female")) {
                            new InsertBasicInfoFeMale().execute();
                        } else {
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.icon_success);
                            builder.setTitle("Success");
                            builder.setCancelable(false);
                            builder.setMessage(message);
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        }


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

    public class InsertBasicInfoFeMale extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", String.valueOf(regdId)));
            param.add(new ParamsPojo("LMP", String.valueOf(lmp)));
            param.add(new ParamsPojo("IsPregnant", String.valueOf(ispregnent)));
            param.add(new ParamsPojo("LMPDate", changeDateFormat("dd/MM/yyyy", "yyyy/MM/dd", edt_lastdate.getText().toString().trim())));
            param.add(new ParamsPojo("GynecProblem", String.valueOf(GynecProblem)));
            param.add(new ParamsPojo("EvidanceThyroid", String.valueOf(EvidanceThyroid)));
            param.add(new ParamsPojo("ISBreastCancer", String.valueOf(ISBreastCancer)));
            param.add(new ParamsPojo("FamillyPlaning", String.valueOf(FamillyPlaning)));
            param.add(new ParamsPojo("Comment", gynaComment));
            param.add(new ParamsPojo("FSH", "1"));
            param.add(new ParamsPojo("FSHComment", "1"));
            param.add(new ParamsPojo("RecentDelivery", String.valueOf(RecentDelivery)));
            param.add(new ParamsPojo("DeliveryComment", edt_recentdelivercomment.getText().toString().trim()));
            param.add(new ParamsPojo("FamillyAdvice", edt_familyplanningadvice.getText().toString().trim()));
            param.add(new ParamsPojo("FinalComment", finalComment));
            param.add(new ParamsPojo("Createdby", userID));
            param.add(new ParamsPojo("HospitalId", selectedHospitalId));
            param.add(new ParamsPojo("PMP", String.valueOf(PMP)));
            res = WebServiceCall.APICall(ApplicationConstants.InsertBasicInfoFemale, ApplicationConstants.webservice, param);
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
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage(message);
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

    private class GetM_HealthHistoryDetails extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetM_HealthHistoryDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    HealthHistoryListPojo pojoDetails = new Gson().fromJson(result, HealthHistoryListPojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        healthHistoryList = pojoDetails.getOutput();
                        rv_medicale_history.setAdapter(new HealthHistoryAdapter(context, healthHistoryList));
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

    private class HealthHistoryAdapter extends RecyclerView.Adapter<HealthHistoryAdapter.ViewHolder> {

        private ArrayList<HealthHistoryListModel> resultList;
        private Context context;

        public HealthHistoryAdapter(Context context, ArrayList<HealthHistoryListModel> toolMenus) {
            this.context = context;
            this.resultList = toolMenus;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_row_health_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            HealthHistoryListModel diseaseDetails = resultList.get(position);

            holder.cb_disease.setText(diseaseDetails.getDiseaseName());
            if (diseaseDetails.getIsSince().equals("1")) {
                holder.edt_since.setVisibility(View.VISIBLE);
            } else {
                holder.edt_since.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if (resultList == null)
                return 0;
            return resultList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CheckBox cb_disease;
            private TextView edt_since;

            public ViewHolder(View view) {
                super(view);
                cb_disease = view.findViewById(R.id.cb_disease);
                edt_since = view.findViewById(R.id.edt_since);
            }
        }
    }

    public class SelectHospital extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", distlgdcode));
            res = WebServiceCall.APICall(ApplicationConstants.GetNearistHospitalList1, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    SelectAddress_pojo pojoDetails = new Gson().fromJson(result, SelectAddress_pojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        hospitalList = pojoDetails.getOutput();
                        if (hospitalList.size() > 0) {
//                            HospitalDialogCreater(hospitalList);
                            CustomHospitalDialog(hospitalList);
                        }
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

    private void CustomHospitalDialog(final ArrayList<SelectAddress_OutPut_pojo> patinet_List) {

        final Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_patientlist_dialog);
        TextView dialog_title_tv = dialog.findViewById(R.id.dialog_title_tv);
        hospital_list_recycler_view = dialog.findViewById(R.id.hospital_list_recycler_view);

        dialog_title_tv.setText("Select Hospital");
        setHospitalRecyclerView(patinet_List);
        dialog.show();

        hospital_list_recycler_view.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                SelectAddress_OutPut_pojo type = new SelectAddress_OutPut_pojo();
                                type = patinet_List.get(position);
                                String selectedHsopitalName = type.getHospitalName();
                                selectedHospitalId = type.getHospitalId();
                                //  Toast.makeText(context, "selected patient is " + selectedHsopitalName+" "+selectedHospitalId, Toast.LENGTH_LONG).show();
                                edt_selecthospital.setText(selectedHsopitalName);
                                dialog.dismiss();

                            }
                        }));
    }

    private void setHospitalRecyclerView(ArrayList<SelectAddress_OutPut_pojo> hospiList) {
        hospital_list_recycler_view.setVisibility(View.VISIBLE);
        hospital_list_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        hospital_list_recycler_view.setLayoutManager(mLayoutManager);
        mAdapter = new HospitalListAdapter(context, hospiList);
        hospital_list_recycler_view.setAdapter(mAdapter);
    }

    private void showImageDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_layout_offeriamge, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        final ImageView imv_offer = promptView.findViewById(R.id.imv_offer);
        final Button btn_close = promptView.findViewById(R.id.btn_close);

        Picasso.with(context)
                .load(reportUrl)
                .into(imv_offer);

        final AlertDialog dialog = alertDialogBuilder.create();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
