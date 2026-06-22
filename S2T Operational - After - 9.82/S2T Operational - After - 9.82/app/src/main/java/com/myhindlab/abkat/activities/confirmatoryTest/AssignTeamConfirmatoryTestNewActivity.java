package com.myhindlab.abkat.activities.confirmatoryTest;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CTMedicineDetailsModel;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CommonBeneficiaryListModel;
import com.myhindlab.abkat.adapters.TeamsAdapter;
import com.myhindlab.abkat.adapters.doortodoor.ConfirmatoryTestAdapter;
import com.myhindlab.abkat.models.CampBeneficiaryListModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.models.doortodoor.TeamNumberModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignTeamConfirmatoryTestNewActivity extends AppCompatActivity implements TeamsAdapter.TeamEvents {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private CheckBox check_address;
    private RadioButton rb_manual,rb_board;

    private EditText edt_Address, edt_area, edt_district,edt_CampId;
    private TextView tv_block,tv_CampId,tv_viewdetails,tv_isTeamAssign,tv_camp_type,tv_camp_date, tv_from_date,edt_TeamNo,tvTeamId,tv_userAssign,tvMemberOne,tvMemberTwo, tv_total, tvDate, tv_teamNumber,tv_versionNumber, tvCamp, tvCampType, tv_District;
    private ArrayList<GetTeamsModel.OutputBean> teamList;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private SearchView searchview_campname;

    private RadioGroup rg_selection;

    private Button btnConfirm,btncall;
    private List<GetTeamsModel.OutputBean> dependentList;
    private RadioButton rb_regId, rb_campId;
    private AlertDialog teamDialog;
    private List<CampListModel.OutputBean> assignteamlist;
    private RecyclerView rv_dependent;
    ConfirmatoryTestAdapter confirmatoryTestAdapter;
    private ArrayList<DistrictList_Model> districtList;
    private ArrayList<TeamNumberModel.Output> teamlist;
    private LinearLayout ll_buttons, llTeamNumber,mainLL,mainLLUser, mainllCampId, mainLLDate, campDate, mainllCampType, llDistrict;
    private ImageView imvSearch;
    private CardView cardviewSearch;
    String fromDate, toDate;
    private ArrayList<CampListModel.OutputBean> campList;

    private CampBeneficiaryListModel.OutputBean beneficiary;
    private CommonBeneficiaryListModel.Output beneficiaryFromCommonbeneficiary;
    private RadioButton rb_teamId,rb_userId;
    private int flag = 1;




    private String beni_no,regId,reg_no,campId = "0",teamidForcamp,teamName,resourceId,assignedteamno,isteamAssign= "0", sAdmin,assignedUserId1,assignedUserId2, UserId, campTypeId, EmpCode, STATELGDCODE = "2", DESGID, teamId = "0",
            district, TALLGDCODE,CampId, regNo,mobileNumber, DISTLGDCODE, taluka,Regdid, LabCode, CampDate, CampDateToDate, AssignedId;

    private LocalBroadcastManager localBroadcastManager;
    private List<TeamCallingModel.Output> campBeneficiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_confirmatory_test_assign_team_new);

        initView();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();

    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        context = AssignTeamConfirmatoryTestNewActivity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 1);
        searchview_campname = findViewById(R.id.searchview_campname);
        tv_block = findViewById(R.id.tv_block);
        ll_buttons = findViewById(R.id.ll_buttons);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);
        tv_District = findViewById(R.id.tv_District);
        rv_dependent = findViewById(R.id.rv_dependent);
        rv_dependent.setLayoutManager(new LinearLayoutManager(context));
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_total = findViewById(R.id.tv_total);
        llTeamNumber = findViewById(R.id.llTeamNumber);
        llDistrict = findViewById(R.id.llDistrict);
        imvSearch = findViewById(R.id.imvSearch);
        rg_selection = findViewById(R.id.rg_selection);
        edt_area = findViewById(R.id.edt_area);
        tv_versionNumber = findViewById(R.id.ben_name);
        rb_regId = findViewById(R.id.rb_regId);
        rb_campId = findViewById(R.id.rb_campId);
        edt_Address = findViewById(R.id.edt_Address);
        edt_district = findViewById(R.id.edt_district);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvDate = findViewById(R.id.tvDate);
        tvCamp = findViewById(R.id.tvCamp);
        mainLLDate = findViewById(R.id.mainLLDate);
        edt_TeamNo = findViewById(R.id.edt_TeamNo);
        tvTeamId = findViewById(R.id.tvTeamId);
        cardviewSearch = findViewById(R.id.cardviewSearch);
        rb_campId = findViewById(R.id.rb_campId);
        rb_regId = findViewById(R.id.rb_regId);
        campDate = findViewById(R.id.campDate);
        mainllCampId = findViewById(R.id.mainllCampId);
        tvCampType = findViewById(R.id.tvCampType);
        mainllCampType = findViewById(R.id.mainllCampType);
        btncall = findViewById(R.id.btncall);
        tvMemberOne = findViewById(R.id.tvMemberOne);
        tvMemberTwo = findViewById(R.id.tvMemberTwo);
        edt_CampId = findViewById(R.id.edt_CampId);
        mainLL = findViewById(R.id.mainLL);
        mainLLUser = findViewById(R.id.mainLLUser);
        rb_manual = findViewById(R.id.rb_manual);
        rb_board = findViewById(R.id.rb_board);
        check_address = findViewById(R.id.check_address);
        tv_CampId = findViewById(R.id.tv_CampId);
        tv_camp_type = findViewById(R.id.tv_camp_type);
        tv_camp_date = findViewById(R.id.tv_camp_date);
        tv_isTeamAssign = findViewById(R.id.tv_isTeamAssign);
        rb_teamId = findViewById(R.id.rb_teamId);
        rb_userId = findViewById(R.id.rb_userId);
        tv_userAssign = findViewById(R.id.tv_userAssign);
        tv_viewdetails = findViewById(R.id.tv_viewdetails);


        if (flag == 2){
            beneficiaryFromCommonbeneficiary = (CommonBeneficiaryListModel.Output) getIntent().getSerializableExtra("beneficiaryDetails");

/// /Comment by shashank we need to uncomment when we have do common ben
//            getMedicineCTDetails(String.valueOf(beneficiaryFromCommonbeneficiary.getRegdid()));


//            Dialog dialog;
//            dialog = new Dialog(AssignTeamConfirmatoryTestActivity
//                    .this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.alert_popup_dialoge_for_ct);
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.show();
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//            ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
//            Button btnYes = dialog.findViewById(R.id.btnYes);
//            Button btnNo = dialog.findViewById(R.id.btnNo);
//            TextView tv_message = dialog.findViewById(R.id.tv_message);
//            btnNo.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//            btnYes.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
//
//
//
//            tv_message.setText("\"" +
//                    "Medicines are prescribed for this beneficiary.\n" +
//                    "User Assigned For Medicine Delivery is :" +""+beneficiaryFromCommonbeneficiary.getTeamid()+
//                    "Do you want to assign same team / user for CT sample collection?)");
//            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//
//                }
//            });
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//
//                }
//            });

        }else if (flag == 1){
            beneficiary = (CampBeneficiaryListModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");

        }

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        Calendar todayCal = Calendar.getInstance();
        todayCal.add(Calendar.DAY_OF_MONTH, -22);

        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -7);


//        rb_teamId.setEnabled(false);

        check_address.setVisibility(View.GONE);
        mainLL.setVisibility(View.GONE);


//        tv_from_date.setText(Utilities.dfDate6.format(todayCal.getTime()));
//        CampDate = Utilities.dfDate6.format(new Date());
//        CampDateToDate = Utilities.dfDate6.format(new Date());

        CampDate = Utilities.dfDate6.format(new Date());


        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyy");
//        tv_from_date.setText(Utilities.getPreviousDate());
//        tv_to_date.setText(mdformat.format(calendar.getTime()));

        campBeneficiaryList = new ArrayList<>();

    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                LabCode = json.getString("LabCode");
            }

            tv_District.setText(district);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assign Team for CT");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            apiCall();
//            String regId = intent.getStringExtra("regId");
            String regNo = intent.getStringExtra("regNo");
//            Log.d("BroadcastReceiver", "onReceive: " + regId);



//            new GetDependentInfo().execute("0", "0", "0");



//            new GetPostCampDetails().execute(
//                    DISTLGDCODE,
//                    "0",
//                    "0",
//                    "3",
//                    "0",
//                    "0",
//                    callType,
//                    "0",
//                    regId.substring(regId.length() - 4)
//
//
//            );

        }
    };


    private void setDefaults() {
//        campId = getIntent().getStringExtra("campId");
//        callType = getIntent().getStringExtra("callType");
//        isAdmin = getIntent().getStringExtra("isAdmin");


        Intent intent = getIntent();
        if (flag == 2) {


            reg_no = beneficiaryFromCommonbeneficiary.getRegdno();
            beni_no = String.valueOf(beneficiaryFromCommonbeneficiary.getRegdid());

            edt_district.setText(beneficiaryFromCommonbeneficiary.getPincode());
            edt_area.setText(beneficiaryFromCommonbeneficiary.getArea());
//            edt_Address.setText(beneficiaryFromCommonbeneficiary.geta());
            teamidForcamp = beneficiaryFromCommonbeneficiary.getTeamid();



            new GetDependentInfo().execute(beni_no,reg_no);


        }else if (flag == 1) {


            beneficiary = (CampBeneficiaryListModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");


            if (beneficiary.getSampleCollection().equalsIgnoreCase("Yes")) {
                btnConfirm.setVisibility(View.GONE);
                mainLL.setVisibility(View.GONE);
                mainLLUser.setVisibility(View.GONE);
                check_address.setVisibility(View.GONE);
                Utilities.showAlertDialog(context, "Alert", "Sample collection done for this beneficiary", false);
            }


            tv_versionNumber.setText(beneficiary.getBeneficiaryName());
            edt_Address.setText(beneficiary.getAddress());
            edt_district.setText(beneficiary.getPinCode());
            edt_area.setText(beneficiary.getArea());
            //beni_no=beneficiary.getRegdId();
            beni_no = beneficiary.getT2T_Order_Id();
            reg_no = beneficiary.getRegdno();


            localBroadcastManager = LocalBroadcastManager.getInstance(context);
            IntentFilter intentFilter = new IntentFilter("AppointmentConfirmationConfirmatoryActivity");
            localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


            new GetDependentInfo().execute(beneficiary.getT2T_Order_Id(), beneficiary.getRegdno());
//        new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");


            // apiCall();

            if (beneficiary.getArId() != null) {
                if (beneficiary.getArId().equalsIgnoreCase("4")) {
                    Utilities.showAlertDialog(context, "Alert", "You are not allowed to change the status as the beneficiary is not interested in CT.", false);
                    btnConfirm.setEnabled(false);
                    tv_userAssign.setEnabled(false);
                    rg_selection.setVisibility(View.GONE);
                    return;
                }
            }

        }

    }

    private void setEventHandler() {
        tv_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);

                            }

                        }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                //  c.add(Calendar.DAY_OF_MONTH, 15);


                try {
//                    dpd1.getDatePicker().setCalendarViewShown(false);

//                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
//                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }

                dpd1.show();

            }
        });



        tv_userAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetResourceList().execute(DISTLGDCODE,"0");
            }
        });

        edt_TeamNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (check_address.isChecked()){
                    new GetTeamsList().execute("0",
                            EmpCode);
                }else if (!check_address.isChecked()){

                    new GetTeamsList().execute(beneficiary.getPinCode(),
                            EmpCode);
                }

            }
        });



        rb_userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_userId.isChecked()) {
                    check_address.setVisibility(View.GONE);
                    mainLL.setVisibility(View.GONE);
                    mainLLUser.setVisibility(View.VISIBLE);


                }

            }
        });


        rb_teamId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_teamId.isChecked()) {
                    check_address.setVisibility(View.VISIBLE);
                    mainLLUser.setVisibility(View.GONE);
                    mainLL.setVisibility(View.VISIBLE);
                }

            }
        });



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                submitData();

            }
        });


        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ mobileNumber));
                startActivity(intent);
            }
        });

        tv_viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(AssignTeamConfirmatoryTestNewActivity.this, HealthScreeningSamplecollectionConfirmatoryEmergencyActivity.class);
                intent.putExtra("beni_no",beni_no);
                intent.putExtra("reg_no",reg_no);
                intent.putExtra("flag",2);

                startActivity(intent);
            }
        });


    }


    private void submitData() {



        if (rb_teamId.isChecked()){
            if (isteamAssign.equalsIgnoreCase("0")){
                if (edt_TeamNo.getText().toString().trim().isEmpty()) {
                    Utilities.showToastMessage("Please select team", context, false);
                    return;
                }

            }

        }
//
//        if (dependentList==null){
//            Utilities.showToastMessage("Dependent details not found", context, false);
//            return;
//        }






        if (rb_teamId.isChecked()){

            if (isteamAssign.equalsIgnoreCase("0")){
                JsonArray teamUserJsonArray = new JsonArray();


                int teamSelected = 0;
                if (teamidForcamp != null) {


                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("USERID", assignedUserId1);
                    jsonObject.addProperty("Teamid", teamidForcamp);
                    teamUserJsonArray.add(jsonObject);

                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.addProperty("USERID", assignedUserId2);
                    jsonObject1.addProperty("Teamid",teamidForcamp);
                    teamUserJsonArray.add(jsonObject1);
                    // teamSelected += 1;


                }

                if (Utilities.isNetworkAvailable(context)) {
                    new InsertCTData().execute(Regdid,campId,teamUserJsonArray.toString(),EmpCode,beneficiary.getT2T_Order_Id(),beneficiary.getTreatmentID());

                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);

                }
            } else if (isteamAssign.equalsIgnoreCase("1")) {


                JsonArray teamUserJsonArray = new JsonArray();


                int teamSelected = 0;
                if (teamList != null) {
                    for (GetTeamsModel.OutputBean t :
                            teamList) {
                        if (t.isChecked()) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("USERID", t.getMemberUserID1());
                            jsonObject.addProperty("Teamid", t.getTeamid());
                            teamUserJsonArray.add(jsonObject);

                            JsonObject jsonObject1 = new JsonObject();
                            jsonObject1.addProperty("USERID", t.getMemberUserID2());
                            jsonObject1.addProperty("Teamid", t.getTeamid());
                            teamUserJsonArray.add(jsonObject1);
                            // teamSelected += 1;
                        }
                    }
                }

                Log.d("teamUserJsonArray", "submitData: " + teamUserJsonArray.toString());


//        JsonArray campArr = new JsonArray();
//        if (dependentList != null) {
//            for (DependentForSearchBeneficiaryModel o : dependentList) {
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("Regdid", o.getRegdid());
//                jsonObject.addProperty("Regdno", o.getRegdNo());
//                jsonObject.addProperty("AppointmentDate", tv_from_date.getText().toString().trim());
//                campArr.add(jsonObject);
//
//            }
//        }


                if (Utilities.isNetworkAvailable(context)) {
                    new InsertCTData().execute(Regdid,campId,teamUserJsonArray.toString(),EmpCode,beneficiary.getT2T_Order_Id(),beneficiary.getTreatmentID());

                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);

                }

            }
        }else if (rb_userId.isChecked()){

            if (tv_userAssign.getText().toString().isEmpty()){
                Utilities.showAlertDialog(context,"Alert","Please Select Resource",false);
                return;
            }


            JsonArray teamUserJsonArray = new JsonArray();


            int teamSelected = 0;
            if (teamidForcamp != null) {


                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("USERID", resourceId);
                jsonObject.addProperty("Teamid", "0");
                teamUserJsonArray.add(jsonObject);


            }

            if (Utilities.isNetworkAvailable(context)) {
                new InsertCTData().execute(Regdid,campId,teamUserJsonArray.toString(),EmpCode, beneficiary.getT2T_Order_Id(),beneficiary.getTreatmentID());

            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);

            }

        }





    }


    @Override
    public void onTeamSelected(GetTeamsModel.OutputBean outputBean) {

        teamDialog.dismiss();

//            if (doctorList==null){
//                Utilities.showToastMessage("Please select doctor first",context,false);
//                return;
//            }


        if (teamidForcamp!=null ||outputBean.getTeamid()!=null){

            if (!teamidForcamp.equalsIgnoreCase(outputBean.getTeamid())){
                //
//            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle("Success");
//            alertDialog.setMessage("Do You Want To Skip Courier Details!");
//            alertDialog.setIcon((R.drawable.icon_success));
//            alertDialog.setButton("OK", (dialog, which) -> {
//                finish();
//            });
//            alertDialog.show();

                android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(context)
                        .setMessage("You have selected different team for sample collection")
                        .setIcon(R.drawable.icon_success)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // do something...

                                        isteamAssign= "1";

                                        submitData();

                                    }
                                }
                        )
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if (teamName.equalsIgnoreCase("NA")){
                                            edt_TeamNo.setText("");

                                        }else {
                                            edt_TeamNo.setText(teamName);

                                        }

                                        // finish();

                                        isteamAssign= "0";


                                        dialog.dismiss();


                                    }
                                }
                        );
                b.show();


            }

        }


    }


    public class GetDependentInfo extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("T2T_Order_Id", params[0]));
            param.add(new ParamsPojo("Regdno", params[1]));


            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));
            res = WebServiceCall.APICall(ApplicationConstants.GetT2T_CT_BeneficiaryDetailsforAssignTeamid_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    GetTeamsModel dependentForSearchBeneficiaryModel = new Gson().fromJson(result, GetTeamsModel.class);
//                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);


                    type = dependentForSearchBeneficiaryModel.getStatus();
                    message = dependentForSearchBeneficiaryModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        dependentList = dependentForSearchBeneficiaryModel.getOutput();
                        if (dependentList != null && dependentList.size() > 0) {


                            if (dependentForSearchBeneficiaryModel.getOutput().size() > 0) {
                                GetTeamsModel.OutputBean output = dependentForSearchBeneficiaryModel.getOutput().get(0);
                                tvTeamId.setText(output.getTeamname()+" / " +"IsTeamActive"+ " - " +output.getIsTeamActive());
                                tvMemberOne.setText(String.valueOf(output.getMember1())+" ("+output.getMember1MOB()+") ");
                                tvMemberTwo.setText(String.valueOf(output.getMember2())+"("+output.getMember2MOB()+")");
                                edt_CampId.setText(output.getCampId() +" / "+output.getCampType());
                                tv_CampId.setText(output.getCampId());

                                campId = output.getCampId();
                                tv_camp_type.setText(output.getCampType());
                                tv_camp_date.setText(output.getCampDate());
                                assignedteamno = output.getAssignTeamName();

                                Regdid = output.getRegdid();


                                if (output.getIsTeamActive().equalsIgnoreCase("Yes")){

                                }

                                if (output.getTeamname().equalsIgnoreCase("NA")){
                                    edt_TeamNo.setText("");

                                }else {
                                    edt_TeamNo.setText(output.getTeamname());

                                }

                                teamName = output.getTeamname();

                                tv_userAssign.setText(output.getUSERNAME());
                                resourceId = output.getUSERID();



                                if (output.getIsTeamAssign().equalsIgnoreCase("Yes")){
                                    edt_TeamNo.setText(assignedteamno);
                                    tv_isTeamAssign.setVisibility(View.VISIBLE);

                                }


                                teamidForcamp = output.getTeamid();
                                assignedUserId1 = output.getMemberUserID1();
                                assignedUserId2 = output.getMemberUserID2();


//                                if (output.getIsAppointmentDone().equalsIgnoreCase("Y")){
//                                    btnConfirm.setVisibility(View.GONE);
//
//                                }

                            }

//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());

//                            rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary_Confirmatory(context, dependentList, AssignTeamConfirmatoryTestActivity.this::onBeneficiaryClick));

                            int t = 0;

//                            for (DependentForSearchBeneficiaryModel output :
//                                    dependentList) {
//
////                                 t = t + dependentList.;
//
//
//                                if (output.getSampleCollection()!=null){
//
//                                    if (output.getSampleCollection().equalsIgnoreCase("Y")){
////                                    finish();
//                                        t  =  t + 1;
//
//                                        if (t == dependentList.size()){
//                                            finish();
//                                        }
//                                    }
//
//                                }
//                            }

                            //   tvTotalCount.setText("" + t);


                        } else {
//                            rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary_Confirmatory(context, new ArrayList<>(), AssignTeamConfirmatoryTestActivity.this::onBeneficiaryClick));

                            tvTeamId.setText("NA");
                            tvMemberOne.setText("NA");
                            tvMemberTwo.setText("NA");

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {

                        tvTeamId.setText("NA");
                        tvMemberOne.setText("NA");
                        tvMemberTwo.setText("NA");
//                        rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary_Confirmatory(context, new ArrayList<>(), AssignTeamConfirmatoryTestActivity.this::onBeneficiaryClick));

                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }

    private class InsertCTData extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("Regdid", params[0]));
            param.add(new ParamsPojo("Campid", params[1]));
            param.add(new ParamsPojo("T2T_CT_TeamBene", params[2]));
            param.add(new ParamsPojo("AssignedBy", params[3]));
            param.add(new ParamsPojo("T2T_Order_Id", params[4]));
            param.add(new ParamsPojo("TreatmentID", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.Insert_T2T_CT_TeamandBeneficiaryMapping_V1, ApplicationConstants.webservice_d2d, param);
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
                        builder.setMessage("User Assigned successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

//                                new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");


                                    finish();



                            }

//                                finish();

                            //  ll_assigned.setVisibility(View.VISIBLE);


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


    public class GetTeamsList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Pincode", params[0]));
            param.add(new ParamsPojo("USERID", params[1]));

            //  res = WebServiceCall.APICall(ApplicationConstants.GetTeamsCampTypeWise, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetT2TTeamDetailsByPincode, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    teamList = new ArrayList<>();
                    GetTeamsModel getTeamsModel = new Gson().fromJson(result, GetTeamsModel.class);
                    type = getTeamsModel.getStatus();
                    message = getTeamsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        teamList = getTeamsModel.getOutput();
                        if (teamList.size() > 0) {
                            //extenalPhebolist.add(0, new ExtenalPhleboModel("0", "All"));

                            showTeamsListDialog(teamList);
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


    private void showTeamsListDialog(final ArrayList<GetTeamsModel.OutputBean> teamList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        rvList.setAdapter(new TeamsAdapter(teamList, this::onTeamSelected));

        //        rvList.addOnItemTouchListener(
        //                new RecyclerItemClickListener(context,
        //                        new RecyclerItemClickListener.OnItemClickListener() {
        //                            @Override
        //                            public void onItemClick(View view, final int position) {
        //                                GetTeamsModel.OutputBean team = teamList.get(position);
        //                                selectedTeam = new ArrayList<>();
        //                                selectedTeam.add(team);
        //                                tvTeamId.setText(selectedTeam.get(0).getTeamName());
        //                            }
        //                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new TeamsAdapter(teamList, AssignTeamConfirmatoryTestNewActivity.this));
                    return;
                }

                if (teamList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<GetTeamsModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (GetTeamsModel.OutputBean clientDetails : teamList) {

                        String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                        String member2 = clientDetails.getMember2().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                member2.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new TeamsAdapter(searchedTestList, AssignTeamConfirmatoryTestNewActivity.this));
                } else {
                    rvList.setAdapter(new TeamsAdapter(teamList, AssignTeamConfirmatoryTestNewActivity.this));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        teamDialog = builderSingle.create();
        teamDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (GetTeamsModel.OutputBean team : teamList
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        teamDialog.show();

    }


    public class GetResourceList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("Pincode", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.GetT2T_CT_UserDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    List<CampListModel.OutputBean> campList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    CampListModel campListModel = new Gson().fromJson(result, CampListModel.class);
                    type = campListModel.getStatus();
                    message = campListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        campList = campListModel.getOutput();

                        if (campList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showResourceListDialog(campList);
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


    private void showResourceListDialog(final List<CampListModel.OutputBean> campList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select User");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campList.size(); i++) {
            arrayAdapter.add(String.valueOf(campList.get(i).getUSERNAME()));

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
                tv_userAssign.setText(campList.get(which).getUSERNAME());
                resourceId = campList.get(which).getUSERID();
            }
        });
        builderSingle.show();
    }



//    private void getMedicineCTDetails(String regno) {
//        ProgressDialog progressDialog = new ProgressDialog(AssignTeamConfirmatoryTestNewActivity.this);
//        progressDialog.setMessage("Please wait . . . ");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
//        Call<CTMedicineDetailsModel> call = apiService.getCTMedicineDetails(regno);
//
//        call.enqueue(new Callback<CTMedicineDetailsModel>() {
//            @Override
//            public void onResponse(Call<CTMedicineDetailsModel> call, Response<CTMedicineDetailsModel> response) {
//                progressDialog.dismiss();
//
//                if (response.isSuccessful() && response.body() != null) {
//                    String status = response.body().getStatus();
//                    String message = response.body().getMessage();
//
//                    if ("success".equalsIgnoreCase(status)) {
//                        List<CTMedicineDetailsModel.Output> ctmedecineList = response.body().getOutput();
//
//
//
//
//
////                        AlertDialog.Builder builder = new AlertDialog.Builder(CTAndedicineCommonBeneficiaryListActivity.this);
////                        View dialogView = LayoutInflater.from(CTAndedicineCommonBeneficiaryListActivity.this)
////                                .inflate(R.layout.medicine_and_ct_details, null, false);
////                        builder.setView(dialogView);
////                        TextView tv_ct_prescription_date = dialogView.findViewById(R.id.tv_ct_prescription_date);
////                        TextView tv_medicine_prescription_date = dialogView.findViewById(R.id.tv_medicine_prescription_date);
////                        TextView tv_ct_assignment_team = dialogView.findViewById(R.id.tv_ct_assignment_team);
////                        TextView tv_assigned_team_text = dialogView.findViewById(R.id.tv_assigned_team_text);
////                        TextView tv_medicine_assignment_team = dialogView.findViewById(R.id.tv_medicine_assignment_team);
////                        TextView tv_ct_status = dialogView.findViewById(R.id.tv_ct_status);
////                        TextView tv_medicine_status = dialogView.findViewById(R.id.tv_medicine_status);
////                        TextView tv_address = dialogView.findViewById(R.id.tv_address);
//
//                        if (ctmedecineList != null && !ctmedecineList.isEmpty()) {
//                            CTMedicineDetailsModel.Output output = ctmedecineList.get(0);
//
//
//
//                            Dialog dialog;
//                            dialog = new Dialog(AssignTeamConfirmatoryTestNewActivity
//                                    .this);
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.setContentView(R.layout.alert_popup_dialoge_for_ct);
//                            dialog.setCanceledOnTouchOutside(true);
//                            dialog.show();
//                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//                            ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
//                            Button btnYes = dialog.findViewById(R.id.btnYes);
//                            Button btnNo = dialog.findViewById(R.id.btnNo);
//                            TextView tv_message = dialog.findViewById(R.id.tv_message);
//                            btnNo.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
//                            btnYes.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
//
//
//
//                            if (!output.getmDDeliveryExecutiveName().equals("")){
//
//                                tv_message.setText("\"" +
//                                        "Medicines are prescribed for this beneficiary.\n" +
//                                        "User Assigned For Medicine Delivery is :" +""+output.getmDDeliveryExecutiveName()+ " " +
//                                        "Do you want to assign same team / user for CT sample collection?)");
//
//
//                            }else if (!output.getMDTeamName().equals("NA")){
//
//                                tv_message.setText("\"" +
//                                        "Medicines are prescribed for this beneficiary.\n" +
//                                        "User Assigned For Medicine Delivery is :" +""+beneficiaryFromCommonbeneficiary.getTeamid()+ " " +
//                                        "Do you want to assign same team / user for CT sample collection?)");
//
//                            }else {
//
//                                Utilities.showAlertDialog(context,"Alert","Medicine not assigned to this beneficiary",false);
//
//                                dialog.dismiss();
//
//                            }
//
//
//
//                            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//
//
////                                    dialog.dismiss();
//                                }
//                            });
//
//                            btnYes.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//
//
//                                }
//                            });
//
//
//
//                            btnNo.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//
//                                    JsonArray teamUserJsonArray = new JsonArray();
//                                    if (!output.getmDDeliveryExecutiveName().equals("")){
//
//
//                                        //                                    int teamSelected = 0;
//
//
//                                            JsonObject jsonObject = new JsonObject();
//                                            jsonObject.addProperty("USERID", output.getmDDeliveryExecutiveID());
//                                            jsonObject.addProperty("Teamid", "0");
//                                            teamUserJsonArray.add(jsonObject);
//
//
//
//
//
//
//                                        new InsertCTData().execute(String.valueOf(beneficiaryFromCommonbeneficiary.getRegdid()),campId,teamUserJsonArray.toString(),EmpCode);
//
//
//                                    }else {
//
//
////                                        if (isteamAssign.equalsIgnoreCase("0")){
//
//
//                                        int teamSelected = 0;
////                                            if (teamidForcamp != null) {
//
//
//                                            JsonObject jsonObject = new JsonObject();
//                                            jsonObject.addProperty("USERID", assignedUserId1);
//                                            jsonObject.addProperty("Teamid", teamidForcamp);
//                                            teamUserJsonArray.add(jsonObject);
//
//                                            JsonObject jsonObject1 = new JsonObject();
//                                            jsonObject1.addProperty("USERID", assignedUserId2);
//                                            jsonObject1.addProperty("Teamid",teamidForcamp);
//                                            teamUserJsonArray.add(jsonObject1);
//                                            // teamSelected += 1;
//
//
////                                            }
//
//                                            if (Utilities.isNetworkAvailable(context)) {
////                                                new InsertCTData().execute(beneficiary.getRegdid(),beneficiary.getCampId(),teamUserJsonArray.toString(),EmpCode);
//
//                                                new InsertCTData().execute(String.valueOf(beneficiaryFromCommonbeneficiary.getRegdid()),campId,teamUserJsonArray.toString(),EmpCode);
//
//
//                                            } else {
//                                                Utilities.showMessage(R.string.msgt_nointernetconnection, context);
//
//                                            }
////                                        }
//
//
//                                    }
//
//
//
//
//                                    dialog.dismiss();
//
//                                }
//                            });
//
//
//
//                        }
//
////                        AlertDialog alertDialog = builder.create();
////                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", (dialog, which) -> dialog.dismiss());
////                        alertDialog.show();
//                    } else {
//                        Utilities.showAlertDialog(AssignTeamConfirmatoryTestNewActivity.this, "Fail", message, false);
//                    }
//                } else {
//                    Utilities.showAlertDialog(AssignTeamConfirmatoryTestNewActivity.this, "Fail", "Details Not Found", false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CTMedicineDetailsModel> call, Throwable t) {
//                progressDialog.dismiss();
//                Utilities.showAlertDialog(AssignTeamConfirmatoryTestNewActivity.this, "Please Try Again", "Server not responding", false);
//            }
//        });
//    }




}