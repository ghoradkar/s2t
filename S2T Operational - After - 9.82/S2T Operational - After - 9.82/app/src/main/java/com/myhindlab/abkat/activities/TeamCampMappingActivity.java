package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.DataEntryOperatorListAdapter;
import com.myhindlab.abkat.adapters.DataEntryOperatorListDetailsAdapter;
import com.myhindlab.abkat.adapters.DoctorMultipleSelectAdapter;
import com.myhindlab.abkat.adapters.DoctorMultipleSelectDetailsAdapter;
import com.myhindlab.abkat.adapters.FlexiDoctorMultipleSelectAdapter;
import com.myhindlab.abkat.adapters.FlexiDoctorMultipleSelectDetailsAdapter;
import com.myhindlab.abkat.adapters.FlexiPhleboListAdapter;
import com.myhindlab.abkat.adapters.FlexiPhleboListDetailsAdapter;
import com.myhindlab.abkat.adapters.LabListAdapter;
import com.myhindlab.abkat.adapters.MMUDoctorMultipleSelectAdapter;
import com.myhindlab.abkat.adapters.MMUDoctorMultipleSelectDetailsAdapter;
import com.myhindlab.abkat.adapters.PhleboListAdapter;
import com.myhindlab.abkat.adapters.PhleboListDetailsAdapter;
import com.myhindlab.abkat.adapters.TeamsAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedAdapter;
import com.myhindlab.abkat.models.AssignTypeModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DataEntryOperatorDetailsModel;
import com.myhindlab.abkat.models.DataEntryOperatorModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DoctorDetailsModel;
import com.myhindlab.abkat.models.DoctorModel;
import com.myhindlab.abkat.models.FlexiDoctorDetailsModel;
import com.myhindlab.abkat.models.FlexiDoctorModel;
import com.myhindlab.abkat.models.FlexiPhleboDetailsModel;
import com.myhindlab.abkat.models.FlexiPhleboModel;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.MMUDoctorDetailsModel;
import com.myhindlab.abkat.models.MMUDoctorModel;
import com.myhindlab.abkat.models.NewCampTypeModel;
import com.myhindlab.abkat.models.PhleboDetailsModel;
import com.myhindlab.abkat.models.PhleboModel;
import com.myhindlab.abkat.models.ResourcesListModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;
import com.myhindlab.abkat.pojos.Lab_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamCampMappingActivity extends AppCompatActivity implements TeamsAdapter.TeamEvents {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private TextView textView, tvDate, tvTeamHeader, tvDoctorHeader, tvmmuDoctorHeader, tvFlexiPhelboHeader, tvFlexiDoctorHeader, tvPhleboHeader, tvDataEntryHeader, tvTeamId, tvCampType, tvDistrict,
            tvLab, tvCampList, tvAssign, tvExternalPhlebo, tvAssigned, tvTeams;
    private Button btnAssign;
    private int campTypeId, assignTypeId;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private String DESGID, EmpCode, LabCode, CampDATE, DISTLGDCODE, district, clusterWiseFlag = "0", TALLGDCODE, taluka,
            STATELGDCODE = "2", selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID,
            CampId, CampDate, UserId, AssignedId = "", IsTeam;

    private ArrayList<GetTeamsModel.OutputBean> teamList;
    private ArrayList<DoctorModel.OutputBean> doctorList;
    private ArrayList<FlexiDoctorModel.OutputBean> flexidoctorList;
    private ArrayList<MMUDoctorModel.OutputBean> MMUdoctorList;
    private ArrayList<PhleboModel.OutputBean> phleboList;
    private ArrayList<FlexiPhleboModel.OutputBean> flexiPhleboList;
    private ArrayList<DataEntryOperatorModel.OutputBean> DeList;
    private ArrayList<CampTypeModel> campTypeModelsList;
    private ArrayList<Lab_OutPut_Pojo> lab_List;
    private ArrayList<DistrictList_Model> districtList;
    private ArrayList<CampListModel.OutputBean> campList;
    private ArrayList<TeamsDetailsModel> assignteamlist;
    private LinearLayout ll_assigned, Team_row, doctor_row, phlebo_row, mmuDoctor_row, flexiPhlebo_row, flexiDoctor_row, dataEntry_row, ll_main_select_team, ll_main_resource;


    private RecyclerView rv_phlebo, rv_deop, rv_doctor, rv_flexi, rv_flexiDoctor, rv_mmuDoctor, rv_teams;
    private final String TAG = TeamCampMappingdetailsActivity.class.getSimpleName();
    private PhleboDetailsModel phleboDetailsModel;
    private FlexiPhleboDetailsModel flexiPhleboDetailsModel;
    private DoctorDetailsModel doctorDetailsModel;
    private FlexiDoctorDetailsModel flexiDoctorDetailsModel;
    private MMUDoctorDetailsModel MMUDoctorDetailsModel;
    private DataEntryOperatorDetailsModel dataEntryOperatorDetailsModel;
    private TeamsDetailsModel teamsDetailsModel;


    // private TeamsDetailsModel teamsDetailsModel;

    private AlertDialog teamDialog;
    private PhleboListDetailsAdapter phleboListDetailsAdapter;
    private FlexiPhleboListDetailsAdapter flexiPhleboListDetailsAdapter;
    private DoctorMultipleSelectDetailsAdapter doctorMultipleSelectDetailsAdapter;
    private FlexiDoctorMultipleSelectDetailsAdapter flexiDoctorMultipleSelectDetailsAdapter;
    private MMUDoctorMultipleSelectDetailsAdapter mmuDoctorMultipleSelectDetailsAdapter;
    private TeamsDetailsAdapter teamsDetailsAdapter;
    private DataEntryOperatorListDetailsAdapter dataEntryOperatorListDetailsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_camp_mapping);


        initView();
        getSessionData();
        setEventHandlers();
        setUpToolbar();

    }


    private void initView() {
        context = TeamCampMappingActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        tvDate = findViewById(R.id.tvDate);
        tvCampType = findViewById(R.id.tvCampType);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvLab = findViewById(R.id.tvLab);
        tvCampList = findViewById(R.id.tvCampList);
        tvAssign = findViewById(R.id.tvAssign);
        tvExternalPhlebo = findViewById(R.id.tvExternalPhlebo);
        btnAssign = findViewById(R.id.btnAssign);
        tvTeamId = findViewById(R.id.tvTeamId);
        tvAssigned = findViewById(R.id.tvAssigned);
        ll_assigned = findViewById(R.id.ll_assigned);
        tvTeams = findViewById(R.id.tvTeams);
        tvTeamHeader = findViewById(R.id.tvTeamHeader);
        tvDoctorHeader = findViewById(R.id.tvDoctorHeader);
        flexiPhlebo_row = findViewById(R.id.flexiPhlebo_row);
        mmuDoctor_row = findViewById(R.id.mmuDoctor_row);
        tvPhleboHeader = findViewById(R.id.tvPhleboHeader);
        tvmmuDoctorHeader = findViewById(R.id.tvmmuDoctorHeader);
        tvFlexiPhelboHeader = findViewById(R.id.tvFlexiPhelboHeader);
        tvDataEntryHeader = findViewById(R.id.tvDataEntryHeader);
        tvFlexiDoctorHeader = findViewById(R.id.tvFlexiDoctorHeader);
        ll_main_select_team = findViewById(R.id.ll_main_select_team);
        ll_main_resource = findViewById(R.id.ll_main_resource);


        rv_teams = findViewById(R.id.rv_teams);
        rv_phlebo = findViewById(R.id.rv_phlebo);
        rv_flexi = findViewById(R.id.rv_flexi);
        rv_flexiDoctor = findViewById(R.id.rv_flexiDoctor);
        rv_mmuDoctor = findViewById(R.id.rv_mmuDoctor);
        rv_deop = findViewById(R.id.rv_deop);
        rv_doctor = findViewById(R.id.rv_doctor);

        Team_row = findViewById(R.id.Team_row);
        doctor_row = findViewById(R.id.doctor_row);
        phlebo_row = findViewById(R.id.phlebo_row);
        dataEntry_row = findViewById(R.id.dataEntry_row);
        flexiDoctor_row = findViewById(R.id.flexiDoctor_row);


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());

        getClusterWiseFlag();


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
                UserId = json.getString("UserId");
                LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void setEventHandlers() {

        Team_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_teams.setVisibility(rv_teams.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (rv_teams.getVisibility() == View.VISIBLE) {
                    tvTeamHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvTeamHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });

        doctor_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_doctor.setVisibility(rv_doctor.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (rv_doctor.getVisibility() == View.VISIBLE) {
                    tvDoctorHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvDoctorHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });


        flexiDoctor_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_flexiDoctor.setVisibility(rv_flexiDoctor.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (rv_flexiDoctor.getVisibility() == View.VISIBLE) {
                    tvFlexiDoctorHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvFlexiDoctorHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });



        mmuDoctor_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_mmuDoctor.setVisibility(rv_mmuDoctor.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (rv_mmuDoctor.getVisibility() == View.VISIBLE) {
                    tvmmuDoctorHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvmmuDoctorHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });


        phlebo_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_phlebo.setVisibility(rv_phlebo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (rv_phlebo.getVisibility() == View.VISIBLE) {
                    tvPhleboHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvPhleboHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });

        flexiPhlebo_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_flexi.setVisibility(rv_flexi.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (rv_flexi.getVisibility() == View.VISIBLE) {
                    tvFlexiPhelboHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvFlexiPhelboHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });

        dataEntry_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_deop.setVisibility(rv_deop.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (rv_deop.getVisibility() == View.VISIBLE) {
                    tvDataEntryHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
                } else {
                    tvDataEntryHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);

                }
            }
        });


        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));


                                //

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

                                if (Utilities.isDateBeforeCurrentDate(CampDate)) {
                                    btnAssign.setEnabled(true);
                                    ll_main_select_team.setVisibility(View.VISIBLE);
                                    ll_main_resource.setVisibility(View.VISIBLE);
                                    ll_assigned.setVisibility(View.VISIBLE);


                                    rv_teams.setClickable(true);
                                    rv_doctor.setClickable(true);
                                    rv_phlebo.setClickable(true);
                                    rv_deop.setClickable(true);
                                } else {
                                    btnAssign.setEnabled(false);
                                    ll_main_select_team.setVisibility(View.GONE);
                                    ll_main_resource.setVisibility(View.GONE);
                                    ll_assigned.setVisibility(View.GONE);

//                                    rv_teams.setLayoutManager(new LinearLayoutManager(context));
//                                    teamsDetailsAdapter = new TeamsDetailsAdapter(teamsDetailsModel.getOutput());
//                                    rv_teams.setAdapter(teamsDetailsAdapter);


                                    rv_teams.setClickable(false);
                                    rv_doctor.setClickable(false);
                                    rv_phlebo.setClickable(false);
                                    rv_deop.setClickable(false);
                                }

                                selectedLabID = "";
                                campTypeId = 0;
                                tvCampType.setText("");
                                tvDistrict.setText("");
                                tvLab.setText("");
                                tvCampList.setText("");
                                tvAssign.setText("");

                                if (phleboList != null) {
                                    phleboList.clear();
                                }
                                if (flexiPhleboList != null) {
                                    flexiPhleboList.clear();
                                }

                                if (teamList != null) {
                                    teamList.clear();
                                }
                                if (DeList != null) {
                                    DeList.clear();
                                }
                                if (doctorList != null) {
                                    doctorList.clear();
                                }
                                if (flexidoctorList != null) {
                                    flexidoctorList.clear();
                                }
                                if (MMUdoctorList != null) {
                                    MMUdoctorList.clear();
                                }
                                if (campTypeModelsList != null) {
                                    campTypeModelsList.clear();
                                }
                                if (lab_List != null) {
                                    lab_List.clear();
                                }
                                if (districtList != null) {
                                    districtList.clear();
                                }

                                try {


                                    if (phleboDetailsModel.getOutput() != null) {
                                        phleboDetailsModel.getOutput().clear();
                                        phleboListDetailsAdapter.notifyDataSetChanged();

                                    }
                                    if (doctorDetailsModel.getOutput() != null) {
                                        doctorDetailsModel.getOutput().clear();
                                        doctorMultipleSelectDetailsAdapter.notifyDataSetChanged();
                                    }
                                    if (flexiDoctorDetailsModel.getOutput() != null) {
                                        flexiDoctorDetailsModel.getOutput().clear();
                                        flexiDoctorMultipleSelectDetailsAdapter.notifyDataSetChanged();
                                    }
                                    if (MMUDoctorDetailsModel.getOutput() != null) {
                                        MMUDoctorDetailsModel.getOutput().clear();
                                        mmuDoctorMultipleSelectDetailsAdapter.notifyDataSetChanged();
                                    }
                                    if (dataEntryOperatorDetailsModel.getOutput() != null) {
                                        dataEntryOperatorDetailsModel.getOutput().clear();
                                        dataEntryOperatorListDetailsAdapter.notifyDataSetChanged();
                                    }
                                    if (teamsDetailsModel.getOutput() != null) {
                                        teamsDetailsModel.getOutput().clear();
                                        teamsDetailsAdapter.notifyDataSetChanged();
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                //                                if (Utilities.isNetworkAvailable(context)) {
                                //                                } else {
                                //                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                //                                }
                            }

                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        tvDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {

                    if (BuildConfig.isBeta) {
                        new GetDistrictListNew().execute(STATELGDCODE, EmpCode);

                    } else {
//                        new GetDistrictList().execute(STATELGDCODE, DISTLGDCODE);
                        new GetDistrictListNew().execute(STATELGDCODE, EmpCode);

                    }

                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });

        tvAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetTeamDetailsListForAssign().execute(CampId, CampDate, String.valueOf(campTypeId));
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });


        tvCampList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DISTLGDCODE == null) {
                    tvDistrict.setError("Select District");
                    return;
                }
                if (CampDate != null) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetCampList().execute(CampDate, EmpCode, DISTLGDCODE, String.valueOf(campTypeId), selectedLabID);
                    } else {
                        Utilities.showToastMessage("Please Check Your Connection", context, false);
                    }
                } else {
                    tvDate.setError("Select Camp Date");

                }


            }
        });


        tvExternalPhlebo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    //   new GetExternalPhleboList().execute("35", selectedLabID);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });


        tvLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetLab().execute(DISTLGDCODE);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                    return;
                }
            }
        });


        tvCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DESGID.equalsIgnoreCase("108")) {

                    new GetFlexiCampType().execute();

                } else if (DESGID.equalsIgnoreCase("136") || DESGID.equalsIgnoreCase("139")) {
                    new GetCampTypeMMU().execute();

                } else {

                    ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                    campTypeModelArrayList.add(new CampTypeModel("DOOR TO DOOR", 3));
//                    campTypeModelArrayList.add(new CampTypeModel("CSC D2D", 4));
                    //                campTypeModelArrayList.add(new CampTypeModel("CSC", 2));

                    showCampType(campTypeModelArrayList);

                }


                tvCampList.setText("");


                if (campList != null) {
                    campList.clear();
                }

                try {

//                    if (phleboDetailsModel.getOutput() != null) {
//                        phleboDetailsModel.getOutput().clear();
//                        phleboListDetailsAdapter.notifyDataSetChanged();
//
//                    }
                    if (MMUDoctorDetailsModel.getOutput() != null) {
                        MMUDoctorDetailsModel.getOutput().clear();
                        mmuDoctorMultipleSelectDetailsAdapter.notifyDataSetChanged();

                    }
                    if (doctorDetailsModel.getOutput() != null) {
                        doctorDetailsModel.getOutput().clear();
                        doctorMultipleSelectDetailsAdapter.notifyDataSetChanged();
                    }
                    if (dataEntryOperatorDetailsModel.getOutput() != null) {
                        dataEntryOperatorDetailsModel.getOutput().clear();
                        dataEntryOperatorListDetailsAdapter.notifyDataSetChanged();
                    }
                    if (teamsDetailsModel.getOutput() != null) {
                        teamsDetailsModel.getOutput().clear();
                        teamsDetailsAdapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        tvTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<AssignTypeModel> assignTypeModelArrayList = new ArrayList<>();
                assignTypeModelArrayList.add(new AssignTypeModel("Team", 1));
//                    assignTypeModelArrayList.add(new AssignTypeModel("Phlebotomist", 35));
//                    assignTypeModelArrayList.add(new AssignTypeModel("Data Entry Operator", 64));
//                    assignTypeModelArrayList.add(new AssignTypeModel("Doctor", 34));

                showAssignType(assignTypeModelArrayList);

            }
        });

        tvAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    if (!AssignedId.isEmpty()) {
                        new GetResouceList().execute(String.valueOf(campTypeId));
                    } else {
                        Utilities.showToastMessage("Please select team first", context, false);
                    }
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                    return;
                }
            }
        });

        btnAssign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AssignedId.isEmpty()) {
                    Utilities.showToastMessage("Please first select Team before adding resources ", context, false);
                    return;
                }


//                Log.d(TAG, "onClick: " + doctorDetailsModel.getOutput().size());
                //  if (doctorDetailsModel.getOutput() != null && doctorDetailsModel.getOutput().size() == 0) {

                if (campTypeId == 3 || campTypeId == 4) {


                    if (doctorList != null) {
                        //  Utilities.showToastMessage("Please select Doctor", context, false);

                        int doctorSelected = 0;
                        for (DoctorModel.OutputBean t :
                                doctorList) {
                            if (t.isChecked()) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("teamId", AssignedId);
                                jsonObject.addProperty("campId", CampId);
                                jsonObject.addProperty("userId", t.getUSERID());
                                //  teamUserJsonArray.add(jsonObject);
                                doctorSelected += 1;

                            }
                        }

                        if (doctorSelected == 0) {
                            Utilities.showToastMessage("Please select Doctor", context, false);
                            return;

                        }

                    } else {
                        Utilities.showToastMessage("Please select Doctor", context, false);
                        return;

                    }
                }

                submitData(0);
            }


            // }
        });

        ////////////////////////TeamCampMappingDetails/////////////////////


        rv_teams.addOnItemTouchListener(new

                RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (Utilities.isDateBeforeCurrentDate(CampDate)) {
                    if (teamsDetailsModel != null) {

                        Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + teamsDetailsModel.getOutput().get(position).getTeamName() + " Team?", true, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                new RemoveTeamMapping(0).execute(String.valueOf(CampId), EmpCode, "0", teamsDetailsModel.getOutput().get(position).getTeamNumber(), "1");

                            }
                        }, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });

                    }

                } else {

                    Utilities.showAlertDialog(context, "Alert", "The team/user camp mapping for backdated camps cannot be removed.", false);

                }


            }
        }));

        rv_phlebo.addOnItemTouchListener(new

                RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (phleboDetailsModel != null) {
                    Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + phleboDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            new RemoveTeamMapping(1).execute(String.valueOf(CampId), EmpCode, phleboDetailsModel.getOutput().get(position).getUserID(), "0", "0");

                        }
                    }, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });

                }
            }
        }));

        rv_flexi.addOnItemTouchListener(new

                RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (flexiPhleboDetailsModel != null) {
                    Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + flexiPhleboDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            new RemoveTeamMapping(4).execute(String.valueOf(CampId), EmpCode, flexiPhleboDetailsModel.getOutput().get(position).getUserID(), "0", "0");

                        }
                    }, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });

                }
            }
        }));

        rv_doctor.addOnItemTouchListener(new

                RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (Utilities.isDateBeforeCurrentDate(CampDate)) {
                    if (doctorDetailsModel != null) {
//                        if (doctorDetailsModel.getOutput().get(position).getIsActive().equalsIgnoreCase("0")){
//                            return;
//                        }

                        Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + doctorDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                new RemoveTeamMapping(2).execute(String.valueOf(CampId), EmpCode, doctorDetailsModel.getOutput().get(position).getUserID(), "0", "0");

                            }
                        }, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });

                    }

                } else {

                    Utilities.showAlertDialog(context, "Alert", "The team/user camp mapping for backdated camps cannot be removed.", false);

                }


            }
        }));

        rv_flexiDoctor.addOnItemTouchListener(new

                RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (Utilities.isDateBeforeCurrentDate(CampDate)) {
                    if (flexiDoctorDetailsModel != null) {
                        Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + flexiDoctorDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                new RemoveTeamMapping(5).execute(String.valueOf(CampId), EmpCode, flexiDoctorDetailsModel.getOutput().get(position).getUserID(), "0", "0");

                            }
                        }, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });

                    }

                } else {
                    Utilities.showAlertDialog(context, "Alert", " The team/user camp mapping for backdated camps cannot be removed.", false);

                }


            }
        }));


        rv_mmuDoctor.addOnItemTouchListener(new

                RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (Utilities.isDateBeforeCurrentDate(CampDate)) {
                    if (MMUDoctorDetailsModel != null) {
                        Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + MMUDoctorDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                new RemoveTeamMapping(6).execute(String.valueOf(CampId), EmpCode, MMUDoctorDetailsModel.getOutput().get(position).getUserID(), "0", "0");

                            }
                        }, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });

                    }

                } else {
                    Utilities.showAlertDialog(context, "Alert", " The team/user camp mapping for backdated camps cannot be removed.", false);

                }


            }
        }));

        rv_deop.addOnItemTouchListener(new

                RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (dataEntryOperatorDetailsModel != null) {
                    Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + dataEntryOperatorDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            new RemoveTeamMapping(3).execute(String.valueOf(CampId), EmpCode, dataEntryOperatorDetailsModel.getOutput().get(position).getUserID(), "0", "0");

                        }
                    }, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });

                }
            }
        }));


    }

    private void submitData(int refreshFlag) {

        if (tvDate.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Date", context, false);
            return;
        }
        if (tvCampType.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select CampType", context, false);
            return;
        }
        if (tvDistrict.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select District", context, false);
            return;
        }
        if (tvLab.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Lab", context, false);
            return;
        }
        if (tvCampList.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Camp", context, false);
            return;
        }


        if (teamList == null && phleboList == null && DeList == null && doctorList == null && flexidoctorList == null && MMUdoctorList == null) {
            Utilities.showToastMessage("Please Select Resource for Camp", context, false);

            return;
        }
        JsonArray teamUserJsonArray = new JsonArray();


        int teamSelected = 0;
        if (teamList != null) {
            for (GetTeamsModel.OutputBean t :
                    teamList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("teamId", t.getTeamid());
                    jsonObject.addProperty("campId", CampId);
                    jsonObject.addProperty("userId", t.getMemberUserID1());
                    jsonObject.addProperty("IsTeam", 1);
                    teamUserJsonArray.add(jsonObject);

                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.addProperty("teamId", t.getTeamid());
                    jsonObject1.addProperty("campId", CampId);
                    jsonObject1.addProperty("userId", t.getMemberUserID2());
                    jsonObject1.addProperty("IsTeam", 1);
                    teamUserJsonArray.add(jsonObject1);
                    // teamSelected += 1;
                }
            }
        }

        //        if (teamSelected == 0) {
        //            Utilities.showToastMessage("Please select at least One Team", context, false);
        //            return;
        //        }
        //
        if (phleboList != null) {
            //  Utilities.showToastMessage("Please select Phlebotomist", context, false);

            int phleboSelected = 0;
            for (PhleboModel.OutputBean t :
                    phleboList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("teamId", AssignedId);
                    jsonObject.addProperty("campId", CampId);
                    jsonObject.addProperty("userId", t.getUSERID());
                    jsonObject.addProperty("IsTeam", 0);

                    teamUserJsonArray.add(jsonObject);
                    //  phleboSelected += 1;
                }
            }
        }
        if (flexiPhleboList != null) {
            //  Utilities.showToastMessage("Please select Phlebotomist", context, false);

            int phleboSelected = 0;
            for (FlexiPhleboModel.OutputBean t :
                    flexiPhleboList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("teamId", AssignedId);
                    jsonObject.addProperty("campId", CampId);
                    jsonObject.addProperty("userId", t.getUSERID());
                    jsonObject.addProperty("IsTeam", 0);

                    teamUserJsonArray.add(jsonObject);
                    //  phleboSelected += 1;
                }
            }
        }

        //        if (phleboSelected == 0) {
        //            Utilities.showToastMessage("Please select at One Phlebotomist", context, false);
        //            return;
        //        }

        if (DeList != null) {
            //  Utilities.showToastMessage("Please select Data Entry Operator", context, false);

            int dataEntryOprSelected = 0;
            for (DataEntryOperatorModel.OutputBean t :
                    DeList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("teamId", AssignedId);
                    jsonObject.addProperty("campId", CampId);
                    jsonObject.addProperty("userId", t.getUSERID());
                    jsonObject.addProperty("IsTeam", 0);
                    teamUserJsonArray.add(jsonObject);
                    //  dataEntryOprSelected += 1;

                }
            }
        }


        //        if (dataEntryOprSelected == 0) {
        //            Utilities.showToastMessage("Please select at One Data Entry Operator", context, false);
        //            return;
        //        }

        if (doctorList != null) {
            //  Utilities.showToastMessage("Please select Doctor", context, false);

            int doctorSelected = 0;
            for (DoctorModel.OutputBean t :
                    doctorList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("teamId", AssignedId);
                    jsonObject.addProperty("campId", CampId);
                    jsonObject.addProperty("userId", t.getUSERID());
                    jsonObject.addProperty("IsTeam", 0);
                    teamUserJsonArray.add(jsonObject);
                    //  doctorSelected += 1;

                }
            }
        }

        if (flexidoctorList != null) {
            //  Utilities.showToastMessage("Please select Doctor", context, false);

            int doctorSelected = 0;
            for (FlexiDoctorModel.OutputBean t :
                    flexidoctorList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("teamId", AssignedId);
                    jsonObject.addProperty("campId", CampId);
                    jsonObject.addProperty("userId", t.getUSERID());
                    jsonObject.addProperty("IsTeam", 0);
                    teamUserJsonArray.add(jsonObject);
                    //  doctorSelected += 1;

                }
            }
        }
        if (MMUdoctorList != null) {
            //  Utilities.showToastMessage("Please select Doctor", context, false);

            int doctorSelected = 0;
            for (MMUDoctorModel.OutputBean t :
                    MMUdoctorList) {
                if (t.isChecked()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("teamId", AssignedId);
                    jsonObject.addProperty("campId", CampId);
                    jsonObject.addProperty("userId", t.getUSERID());
                    jsonObject.addProperty("IsTeam", 0);
                    teamUserJsonArray.add(jsonObject);
                    //  doctorSelected += 1;

                }
            }
        }

//        if (flexidoctorList != null) {
//            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//            int doctorSelected = 0;
//            for (FlexiDoctorModel.OutputBean t :
//                    flexidoctorList) {
//                if (t.isChecked()) {
//                    JsonObject jsonObject = new JsonObject();
//                    jsonObject.addProperty("teamId", AssignedId);
//                    jsonObject.addProperty("campId", CampId);
//                    jsonObject.addProperty("userId", t.getUSERID());
//                    jsonObject.addProperty("IsTeam", 0);
//                    teamUserJsonArray.add(jsonObject);
//                    //  doctorSelected += 1;
//
//                }
//            }
//        }
        //        if (doctorSelected == 0) {
        //            Utilities.showToastMessage("Please select at One Doctor", context, false);
        //            return;
        //        }


        //        if (tvExternalPhlebo.getText().toString().trim().isEmpty()) {
        //            Utilities.showToastMessage("Please Select External Phlebo", context, false);
        //            return;
        //        }

        Log.d("Submit", "submitData: " + teamUserJsonArray.toString());

        if (Utilities.isNetworkAvailable(context)) {
            new InsertTeamCampMappingDetails(refreshFlag).execute(String.valueOf(CampId), EmpCode, teamUserJsonArray.toString(), EmpCode);
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }


    }


    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
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
                tvCampType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = campTypeModelsList.get(which).getCampTypeId();


                if (teamList != null) {
                    teamList.clear();
                }


                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }

    private void showAssignType(final ArrayList<AssignTypeModel> assignTypeModelArrayList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Choose Team");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < assignTypeModelArrayList.size(); i++) {
            arrayAdapter.add(String.valueOf(assignTypeModelArrayList.get(i).getTypeName()));
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
                //  tvAssign.setText(assignTypeModelArrayList.get(which).getTypeName());
                assignTypeId = assignTypeModelArrayList.get(which).getTypeId();

                if (assignTypeId == 1) {
//                    if (teamList == null || teamList.isEmpty()) {
//                        new GetTeamsList().execute(selectedLabID, CampDate, String.valueOf(campTypeId));
//                    } else {
//                        showTeamsListDialog(
//                                teamList
//                        );
//                    }

                    new GetTeamsList().execute(selectedLabID, CampDate, String.valueOf(campTypeId));

                }
                //  refreshCalendar();

            }
        });
        builderSingle.show();
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team-Camp Mapping");

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        btn_save_accordian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, TeamCampMappingdetailsActivity.class));
                finish();
            }
        });
    }


    public class GetDistrictList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAllDistrictList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
//                              districtList.add(0, new DistrictList_Model("0", "All"));

                            showDistrictListDialog(districtList);
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


    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < districtList.size(); i++) {
            arrayAdapter.add(String.valueOf(districtList.get(i).getDISTNAME()));
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
                tvDistrict.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


    public class GetLab extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetLab, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    lab_List = new ArrayList<>();

                    Lab_Pojo pojoDetails = new Gson().fromJson(result, Lab_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        ArrayList<Lab_OutPut_Pojo> labList = pojoDetails.getOutput();
                        if (labList.size() > 0) {
                            showLabListDialog(labList);
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


    private void showLabListDialog(final ArrayList<Lab_OutPut_Pojo> lab_List) {

        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);


        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        LabListAdapter labListAdapter = new LabListAdapter(lab_List);
        rvList.setAdapter(labListAdapter);

        ArrayList<Lab_OutPut_Pojo> filteredList = new ArrayList<>();

        //        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        //        for (Lab_OutPut_Pojo subTrenchModel : lab_List) {
        //            arrayAdapter.add(subTrenchModel.getLabName());
        //        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (filteredList != null)
                    filteredList.clear();

                if (lab_List != null) {
                    if (edt_search.getText().toString().equals("")) {
                        filteredList.addAll(lab_List);
                        rvList.setAdapter(new LabListAdapter(filteredList));

                    } else {
                        if (lab_List.size() > 0) {
                            for (Lab_OutPut_Pojo pojo : lab_List) {
                                String siteDetails = pojo.getLabName();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
                                    filteredList.add(pojo);
                                }
                            }

                            if (filteredList.size() == 0) {
                                filteredList.addAll(lab_List);
                                rvList.setAdapter(new LabListAdapter(filteredList));
                            } else {
                                rvList.setAdapter(new LabListAdapter(filteredList));

                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog alertDialog = builderSingle.create();

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


//                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        selectedLabID = lab_List.get(which).getLabCode();
//                        selectedLabName = lab_List.get(which).getLabName();
//                        tvLab.setText(selectedLabName);
//                    }
//                });

        rvList.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (filteredList.size() > 0) {
                    selectedLabID = filteredList.get(position).getLabCode();
                    selectedLabName = filteredList.get(position).getLabName();
                    tvLab.setText(selectedLabName);
                } else {
                    selectedLabID = lab_List.get(position).getLabCode();
                    selectedLabName = lab_List.get(position).getLabName();
                    tvLab.setText(selectedLabName);
                }
                alertDialog.dismiss();

            }
        }));

        alertDialog.show();

    }


//    public class GetExternalPhleboList extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("DESGID", params[0]));
//            param.add(new ParamsPojo("LabCode", params[1]));
//
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetExternalPhleboList, ApplicationConstants.TeamCampMapping, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (!result.equals("")) {
//                    List<ExtenalPhleboModel.OutputBean> extenalPhebolist = new ArrayList<>();
//                    ExtenalPhleboModel extenalPhleboModel = new Gson().fromJson(result, ExtenalPhleboModel.class);
//                    type = extenalPhleboModel.getStatus();
//                    message = extenalPhleboModel.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        extenalPhebolist = extenalPhleboModel.getOutput();
//                        if (extenalPhebolist.size() > 0) {
//                            //extenalPhebolist.add(0, new ExtenalPhleboModel("0", "All"));
//
//                            showExtenalPhleboListDialog(extenalPhebolist);
//                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
//    }

//    private void showExtenalPhleboListDialog(final List<ExtenalPhleboModel.OutputBean> extenalPhebolist) {
//        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
//        builderSingle.setTitle("Select External Phlebo");
//        builderSingle.setCancelable(false);
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
//
//        for (int i = 0; i < extenalPhebolist.size(); i++) {
//            arrayAdapter.add(String.valueOf(extenalPhebolist.get(i).getUsername()));
//        }
//
//        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                tvExternalPhlebo.setText(extenalPhebolist.get(which).getUsername());
//                exernalPhleboId = extenalPhebolist.get(which).getUserid();
//
//                //  refreshCalendar();
//            }
//        });
//        builderSingle.show();
//
//    }


    public class GetPhleboList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DesgId", params[0]));
            param.add(new ParamsPojo("LabCode", params[1]));
            param.add(new ParamsPojo("CampDate", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    phleboList = new ArrayList<>();
                    PhleboModel phleboModel = new Gson().fromJson(result, PhleboModel.class);
                    type = phleboModel.getStatus();
                    message = phleboModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        phleboList = phleboModel.getOutput();
                        if (phleboList.size() > 0) {
                            //extenalPhebolist.add(0, new ExtenalPhleboModel("0", "All"));

                            showPhleboListDialog(phleboList);
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

    public class GetFlexiPhleboList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DesgId", params[0]));
            param.add(new ParamsPojo("LabCode", params[1]));
            param.add(new ParamsPojo("CampDate", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    flexiPhleboList = new ArrayList<>();
                    FlexiPhleboModel flexiPhleboModel = new Gson().fromJson(result, FlexiPhleboModel.class);
                    type = flexiPhleboModel.getStatus();
                    message = flexiPhleboModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        flexiPhleboList = flexiPhleboModel.getOutput();
                        if (flexiPhleboList.size() > 0) {
                            //extenalPhebolist.add(0, new ExtenalPhleboModel("0", "All"));

                            showFlexiPhleboListDialog(flexiPhleboList);
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


    public class GetDeOpList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DesgId", params[0]));
            param.add(new ParamsPojo("LabCode", params[1]));
            param.add(new ParamsPojo("CampDate", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    DeList = new ArrayList<>();
                    DataEntryOperatorModel dataEntryOperatorModel = new Gson().fromJson(result, DataEntryOperatorModel.class);
                    type = dataEntryOperatorModel.getStatus();
                    message = dataEntryOperatorModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        DeList = dataEntryOperatorModel.getOutput();
                        if (DeList.size() > 0) {
                            //extenalPhebolist.add(0, new ExtenalPhleboModel("0", "All"));

                            showDeopList(DeList);
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
            param.add(new ParamsPojo("LabCode", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("CampType", params[2]));

            //  res = WebServiceCall.APICall(ApplicationConstants.GetTeamsCampTypeWise, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamsCampTypeWise_RegularCamp, ApplicationConstants.webservice_d2d, param);
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
            param.add(new ParamsPojo("DesgId", params[0]));
            param.add(new ParamsPojo("LabCode", params[1]));
            param.add(new ParamsPojo("CampDate", params[2]));
            param.add(new ParamsPojo("DISTLGDCODE", params[3]));
            param.add(new ParamsPojo("USERID",EmpCode));

            if (BuildConfig.isBeta) {
                if (clusterWiseFlag != null) {
                    if (clusterWiseFlag.equalsIgnoreCase("1")) {
                        res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);

                    } else if (clusterWiseFlag.equalsIgnoreCase("2")) {

                        res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation_Cluster, ApplicationConstants.webservice_d2d, param);

                    }

                }

            } else {

                if (clusterWiseFlag != null) {
                    if (clusterWiseFlag.equalsIgnoreCase("1")) {
                        res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);

                    } else if (clusterWiseFlag.equalsIgnoreCase("2")) {

                        res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation_Cluster, ApplicationConstants.webservice_d2d, param);

                    }

                }

//                res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    doctorList = new ArrayList<>();
                    DoctorModel pojoDetails = new Gson().fromJson(result, DoctorModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        doctorList.addAll(pojoDetails.getOutput());
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

    private class GetFlexiDoctorList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DesgId", params[0]));
            param.add(new ParamsPojo("LabCode", params[1]));
            param.add(new ParamsPojo("CampDate", params[2]));


//            if (BuildConfig.isBeta) {
//                if (clusterWiseFlag != null) {
//                    if (clusterWiseFlag.equalsIgnoreCase("1")) {
//                        res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);
//
//                    } else if (clusterWiseFlag.equalsIgnoreCase("2")) {
//
//                        res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation_Cluster, ApplicationConstants.webservice_d2d, param);
//
//                    }
//
//                }
//
//            } else {
//                res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);
//            }


            res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    flexidoctorList = new ArrayList<>();
                    FlexiDoctorModel pojoDetails = new Gson().fromJson(result, FlexiDoctorModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        flexidoctorList.addAll(pojoDetails.getOutput());
                        if (flexidoctorList.size() > 0) {
                            showFlexiDoctorListDialog(flexidoctorList);
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

    private class GetMMUDoctorList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DesgId", params[0]));
            param.add(new ParamsPojo("LabCode", params[1]));
            param.add(new ParamsPojo("CampDate", params[2]));
//            res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation, ApplicationConstants.webservice_d2d, param);




            res = WebServiceCall.APICall(ApplicationConstants.GetResourceFromDesignation_MMU, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    MMUdoctorList = new ArrayList<>();
                    MMUDoctorModel pojoDetails = new Gson().fromJson(result, MMUDoctorModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        MMUdoctorList.addAll(pojoDetails.getOutput());
                        if (MMUdoctorList.size() > 0) {
                            showMMUDoctorListDialog(MMUdoctorList);
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

    @Override
    public void onTeamSelected(GetTeamsModel.OutputBean outputBean) {
        teamDialog.dismiss();

//            if (doctorList==null){
//                Utilities.showToastMessage("Please select doctor first",context,false);
//                return;
//            }
        submitData(1);

        new GetTeamDetailsList().execute(CampId, CampDate, "0");


    }


    private void showTeamsListDialog(final ArrayList<GetTeamsModel.OutputBean> teamList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
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
                    rvList.setAdapter(new TeamsAdapter(teamList, TeamCampMappingActivity.this));
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
                    rvList.setAdapter(new TeamsAdapter(searchedTestList, TeamCampMappingActivity.this));
                } else {
                    rvList.setAdapter(new TeamsAdapter(teamList, TeamCampMappingActivity.this));
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

    private void showDoctorListDialog(final ArrayList<DoctorModel.OutputBean> doctors) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Doctor");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        DoctorMultipleSelectAdapter doctorMultipleSelectAdapter = new DoctorMultipleSelectAdapter(doctors);
        rvList.setAdapter(doctorMultipleSelectAdapter);

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
                    rvList.setAdapter(new DoctorMultipleSelectAdapter(doctors));
                    return;
                }

                if (doctorList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<DoctorModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (DoctorModel.OutputBean clientDetails : doctorList) {

                        String countryToBeSearched = clientDetails.getResourceName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new DoctorMultipleSelectAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new DoctorMultipleSelectAdapter(doctorList));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (DoctorModel.OutputBean team : doctors
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        alertDialog.show();

    }

    private void showFlexiDoctorListDialog(final ArrayList<FlexiDoctorModel.OutputBean> doctors) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Doctor");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        FlexiDoctorMultipleSelectAdapter flexiDoctorMultipleSelectAdapter = new FlexiDoctorMultipleSelectAdapter(doctors);
        rvList.setAdapter(flexiDoctorMultipleSelectAdapter);

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
                    rvList.setAdapter(new FlexiDoctorMultipleSelectAdapter(doctors));
                    return;
                }

                if (flexidoctorList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<FlexiDoctorModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (FlexiDoctorModel.OutputBean clientDetails : flexidoctorList) {

                        String countryToBeSearched = clientDetails.getResourceName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new FlexiDoctorMultipleSelectAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new FlexiDoctorMultipleSelectAdapter(flexidoctorList));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (FlexiDoctorModel.OutputBean team : doctors
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });
        alertDialog.show();
    }

    private void showMMUDoctorListDialog(final ArrayList<MMUDoctorModel.OutputBean> doctors) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Doctor");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        MMUDoctorMultipleSelectAdapter mmuDoctorMultipleSelectAdapter = new MMUDoctorMultipleSelectAdapter(doctors);
        rvList.setAdapter(mmuDoctorMultipleSelectAdapter);

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
                    rvList.setAdapter(new MMUDoctorMultipleSelectAdapter(doctors));
                    return;
                }

                if (MMUdoctorList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<MMUDoctorModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (MMUDoctorModel.OutputBean clientDetails : MMUdoctorList) {

                        String countryToBeSearched = clientDetails.getResourceName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new MMUDoctorMultipleSelectAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new MMUDoctorMultipleSelectAdapter(MMUdoctorList));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (MMUDoctorModel.OutputBean team : doctors
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        alertDialog.show();

    }

    private void showPhleboListDialog(final ArrayList<PhleboModel.OutputBean> phlebo) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Phlebo");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        PhleboListAdapter phleboListAdapter = new PhleboListAdapter(phlebo);
        rvList.setAdapter(phleboListAdapter);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new PhleboListAdapter(phleboList));
                    return;
                }

                if (phlebo.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<PhleboModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (PhleboModel.OutputBean clientDetails : phleboList) {

                        String countryToBeSearched = clientDetails.getResourceName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new PhleboListAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new PhleboListAdapter(phleboList));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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


        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (PhleboModel.OutputBean team : phlebo
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        alertDialog.show();

    }

    private void showFlexiPhleboListDialog(final ArrayList<FlexiPhleboModel.OutputBean> phlebo) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Flexi Phlebo");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        FlexiPhleboListAdapter flexiPhleboListAdapter = new FlexiPhleboListAdapter(phlebo);
        rvList.setAdapter(flexiPhleboListAdapter);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new FlexiPhleboListAdapter(flexiPhleboList));
                    return;
                }

                if (phlebo.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<FlexiPhleboModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (FlexiPhleboModel.OutputBean clientDetails : flexiPhleboList) {

                        String countryToBeSearched = clientDetails.getResourceName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new FlexiPhleboListAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new FlexiPhleboListAdapter(flexiPhleboList));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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


        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (FlexiPhleboModel.OutputBean team : phlebo
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        alertDialog.show();

    }

    private void showDeopList(final ArrayList<DataEntryOperatorModel.OutputBean> deop) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Data Entry Operator");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        DataEntryOperatorListAdapter dataEntryOperatorListAdapter = new DataEntryOperatorListAdapter(deop);
        rvList.setAdapter(dataEntryOperatorListAdapter);

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
                    rvList.setAdapter(new DataEntryOperatorListAdapter(deop));
                    return;
                }

                if (deop.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<DataEntryOperatorModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (DataEntryOperatorModel.OutputBean clientDetails : DeList) {

                        String countryToBeSearched = clientDetails.getResourceName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new DataEntryOperatorListAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new DataEntryOperatorListAdapter(DeList));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (DataEntryOperatorModel.OutputBean team : deop
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        alertDialog.show();

    }

    public class GetCampList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampDATE", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            param.add(new ParamsPojo("LABCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampList, ApplicationConstants.webservice_d2d, param);
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

                            showCampListDialog(campList);
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


    private void showCampListDialog(final List<CampListModel.OutputBean> campList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campList.size(); i++) {
            arrayAdapter.add(String.valueOf(campList.get(i).getCampId() + "(" + campList.get(i).getCampCreatedBy() + ")"));

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
                tvCampList.setText(campList.get(which).getCampId());
                CampId = campList.get(which).getCampId();
                CampDate = campList.get(which).getCampDate();


                new GetPhleboDetailsList().execute(CampId, CampDate, "35");
                new GetDeopDetailsList().execute(CampId, CampDate, "64");
                new GetDoctorDetailsList().execute(CampId, CampDate, "34");
                new GetTeamDetailsList().execute(CampId, CampDate, "0");

                if (BuildConfig.isBeta) {
                    new GetFlexiPhleboDetailsList().execute(CampId, CampDate, "129");
                    new GetFlexiDoctorDetailsList().execute(CampId, CampDate, "147");
                    new GetMMUDoctorDetailsList().execute(CampId, CampDate, "141");
                } else {
                    new GetFlexiPhleboDetailsList().execute(CampId, CampDate, "146");
                    new GetFlexiDoctorDetailsList().execute(CampId, CampDate, "147");
                    new GetMMUDoctorDetailsList().execute(CampId, CampDate, "141");

                }


                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


    public class GetFlexiCampType extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.CampTypeFlexi, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    NewCampTypeModel newCampTypeModel = new Gson().fromJson(result, NewCampTypeModel.class);
                    type = newCampTypeModel.getStatus();
                    message = newCampTypeModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<NewCampTypeModel.Output> camptypelist = newCampTypeModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showCampTypeDialog(camptypelist);
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

        private void showCampTypeDialog(final List<NewCampTypeModel.Output> camptypelist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Camp Type");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < camptypelist.size(); i++) {
                arrayAdapter.add(String.valueOf(camptypelist.get(i).getCampTypeDescription()));
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
                    tvCampType.setText(camptypelist.get(which).getCampTypeDescription());
                    campTypeId = camptypelist.get(which).getCamptype();
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }


    private class InsertTeamCampMappingDetails extends AsyncTask<String, Void, String> {

        private int refreshFlag = 0;

        public InsertTeamCampMappingDetails(int refreshFlag) {
            this.refreshFlag = refreshFlag;
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
            Log.d("InsertTeamCampMapping", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampID", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("TeamNumber", params[2]));
            param.add(new ParamsPojo("CreatedBy", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertTeamCampMapping, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertTeamCampMapping", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {


                        if (refreshFlag == 0) {
                            AssignedId = "";

                            tvAssigned.setText("");
                            tvAssign.setText("");
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Teams Assign successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                                if (phleboList != null) {
                                    phleboList.clear();
                                }
                                if (teamList != null) {
                                    teamList.clear();
                                }
                                if (DeList != null) {
                                    DeList.clear();
                                }
                                if (doctorList != null) {
                                    doctorList.clear();
                                }

                                if (refreshFlag == 1) {
                                    new GetTeamsList().execute(selectedLabID, CampDate, String.valueOf(campTypeId));

                                }


                                new GetPhleboDetailsList().execute(CampId, CampDate, "35");
                                new GetDeopDetailsList().execute(CampId, CampDate, "64");
                                new GetDoctorDetailsList().execute(CampId, CampDate, "34");
                                if (BuildConfig.isBeta) {
                                    new GetFlexiPhleboDetailsList().execute(CampId, CampDate, "129");
                                    new GetFlexiDoctorDetailsList().execute(CampId, CampDate, "147");
                                    new GetMMUDoctorDetailsList().execute(CampId, CampDate, "141");
                                } else {
                                    new GetFlexiPhleboDetailsList().execute(CampId, CampDate, "146");
                                    new GetFlexiDoctorDetailsList().execute(CampId, CampDate, "147");
                                    new GetMMUDoctorDetailsList().execute(CampId, CampDate, "141");

                                }


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

    public class GetResouceList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampType", params[0]));
            //  res = WebServiceCall.APICall(ApplicationConstants.GetDesignationsForCampCreation, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetDesignationsForCampCreationFlexi, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetDesignationsForCampCreationOnlyoctor, ApplicationConstants.webservice_d2d, param);
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


//                           for (ResourcesListModel.OutputBean output :
//                                  resourceList ){
//
//                               if (campTypeId == 5){
//                                 if (output.getDesgId().equalsIgnoreCase("35")||(output.getDesgId().equalsIgnoreCase("86"))) {
//                                   resourceList.remove(output);
//                                   break;
//                                 }
//                               }
////                               if (campTypeId == 5){
////                                 if (output.getDesgId().equalsIgnoreCase("86")) {
////                                   resourceList.remove(output);
////                                   break;
////                                 }
////                               }
//
//                           }

                            showResourceListDialog(resourceList);

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

    private void showResourceListDialog(final List<ResourcesListModel.OutputBean> resourcelist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < resourcelist.size(); i++) {
            arrayAdapter.add(String.valueOf(resourcelist.get(i).getDesgName()));
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
                tvAssign.setText(resourcelist.get(which).getDesgName());
                resorceId = resourcelist.get(which).getDesgId();
                if (resorceId.equalsIgnoreCase("0")) {
                    if (teamList == null || teamList.isEmpty()) {
                        // new GetTeamsList().execute(selectedLabID, CampDate);
                    } else {
                        showTeamsListDialog(
                                teamList
                        );
                    }
                } else if (resorceId.equalsIgnoreCase("34")) {
                    if (AssignedId == null) {
                        Utilities.showToastMessage("Please Select Assigned Team Before Adding Resource", context, false);
                        return;
                    }

                    if (doctorList == null || doctorList.isEmpty()) {
                        new GetDoctorList().execute(resorceId, "0", CampDate,DISTLGDCODE);
                    } else {

                        showDoctorListDialog(doctorList);
                    }


                } else if (resorceId.equalsIgnoreCase("35")) {
                    if (AssignedId == null) {
                        Utilities.showToastMessage("Please Select Assigned Team Before Adding Resource", context, false);
                        return;
                    }
//                    if (doctorDetailsModel.getOutput() == null) {
//                        Utilities.showToastMessage("Please assign a doctor to this team first", context, false);
//                        return;
//                    }

                    if (phleboList == null || phleboList.isEmpty()) {
                        new GetPhleboList().execute(resorceId, selectedLabID, CampDate);
                    } else {
                        showPhleboListDialog(phleboList);
                    }
                    // new GetAdditionalPhleboList().execute
                } else if (resorceId.equalsIgnoreCase("86")) {
                    if (AssignedId == null) {
                        Utilities.showToastMessage("Please Select Assigned Team Before Adding Resource", context, false);
                        return;
                    }
                    if (doctorDetailsModel.getOutput() == null) {
                        Utilities.showToastMessage("Please assign a doctor to this team first", context, false);
                        return;
                    }


                    if (DeList == null || DeList.isEmpty()) {
                        new GetDeOpList().execute(resorceId, selectedLabID, CampDate);
                    } else {
                        showDeopList(DeList);
                    }
                } else if (resorceId.equalsIgnoreCase("146") || resorceId.equalsIgnoreCase("129")) {

//                    if (doctorDetailsModel.getOutput() == null) {
//                        Utilities.showToastMessage("Please assign a doctor to this team first", context, false);
//                        return;
//                    }


                    if (flexiPhleboList == null || flexiPhleboList.isEmpty()) {
                        new GetFlexiPhleboList().execute(resorceId, selectedLabID, CampDate);
                    } else {
                        showFlexiPhleboListDialog(flexiPhleboList);
                    }

                } else if (resorceId.equalsIgnoreCase("147") || resorceId.equalsIgnoreCase("147")) {

                    if (flexidoctorList == null || flexidoctorList.isEmpty()) {
                        new GetFlexiDoctorList().execute(resorceId, "0", CampDate);

                    } else {
                        showFlexiDoctorListDialog(flexidoctorList);
                    }
                } else if (resorceId.equalsIgnoreCase("141")) {

                    if (MMUdoctorList == null || MMUdoctorList.isEmpty()) {
                        new GetMMUDoctorList().execute(resorceId, "0", CampDate);

                    } else {
                        showMMUDoctorListDialog(MMUdoctorList);
                    }

                }
                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }

    public class GetTeamDetailsListForAssign extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("CampType", params[2]));


            // res = WebServiceCall.APICall(ApplicationConstants.GetAssignedTeamDetails, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedTeamDetailsFlexi, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    assignteamlist = new ArrayList<>();
                    ArrayList<TeamsDetailsModel.OutputBean> assignteamlist = new ArrayList<>();
                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        assignteamlist = teamsDetailsModel.getOutput();
                        if (assignteamlist.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showAssignedTeamListDialog(assignteamlist);

                            // ll_assigned.setVisibility(View.VISIBLE);


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", "Team not assigned for selected campId, assign team first", false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please select CampId", "You have to select campId", false);
            }
        }
    }


    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        rvList.setAdapter(new TeamsDetailsAssignedAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                AssignedId = assignteamlist.get(position).getTeamNumber();

                                tvAssigned.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();
                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new TeamsDetailsAssignedAdapter(assignteamlist));
                    return;
                }

                if (teamList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<TeamsDetailsModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (TeamsDetailsModel.OutputBean clientDetails : assignteamlist) {

                        String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                        String member2 = clientDetails.getMember2().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                member2.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new TeamsDetailsAssignedAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new TeamsDetailsAssignedAdapter(assignteamlist));
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
                for (TeamsDetailsModel.OutputBean team : assignteamlist
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

//    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
//        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
//        builderSingle.setTitle("Select Assigned Team");
//        builderSingle.setCancelable(false);
//
//
//
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
//
//
//
//
//        for (int i = 0; i < assignteamlist.size(); i++) {
//            arrayAdapter.add(String.valueOf(assignteamlist.get(i).getTeamID()));
//        }
//
//        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//
//
//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                tvAssigned.setText(assignteamlist.get(which).getTeamID());
//                TeamID = assignteamlist.get(which).getTeamID();
//                //  refreshCalendar();
//            }
//        });
//        builderSingle.show();
//
//    }


////////////Remove in TeamCamp mapping//////////////

    class RemoveTeamMapping extends AsyncTask<String, Void, String> {

        private int type;

        public RemoveTeamMapping(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Removing Resource..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            ArrayList<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("RemovedBy", params[1]));
            param.add(new ParamsPojo("Userid", params[2]));
            param.add(new ParamsPojo("TeamId", params[3]));
            param.add(new ParamsPojo("IsTeam", params[4]));

            Log.d(TAG, "doInBackground: " + Arrays.toString(params));
            res = WebServiceCall.APICall(ApplicationConstants.RemoveTeamCampMapping, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            pd.dismiss();
            Log.d(TAG, "onPostExecute: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("message");
                if (status.equalsIgnoreCase("success")) {
                    if (type == 0) {
                        Utilities.showToastMessage("Team removed successfully", context, true);

                        if (teamList != null) {
                            teamList.clear();
                        }

                        new GetTeamDetailsList().execute(CampId, CampDate, "0");
                        new GetPhleboDetailsList().execute(CampId, CampDate, "35");
                        new GetDeopDetailsList().execute(CampId, CampDate, "64");
                        new GetDoctorDetailsList().execute(CampId, CampDate, "34");
                        if (BuildConfig.isBeta) {
                            new GetFlexiPhleboDetailsList().execute(CampId, CampDate, "129");
                            new GetFlexiDoctorDetailsList().execute(CampId, CampDate, "147");
                            new GetMMUDoctorDetailsList().execute(CampId, CampDate, "141");
                        } else {
                            new GetFlexiPhleboDetailsList().execute(CampId, CampDate, "146");
                            new GetFlexiDoctorDetailsList().execute(CampId, CampDate, "147");
                            new GetMMUDoctorDetailsList().execute(CampId, CampDate, "141");
                        }


                    } else if (type == 1) {
                        Utilities.showToastMessage("Phlebo removed successfully", context, true);

                        if (phleboList != null) {
                            phleboList.clear();
                        }

                        new GetPhleboDetailsList().execute(CampId, CampDate, "35");
                    } else if (type == 2) {
                        Utilities.showToastMessage("Doctor removed successfully", context, true);

                        if (doctorList != null) {
                            doctorList.clear();
                        }

                        new GetDoctorDetailsList().execute(CampId, CampDate, "34");
                    } else if (type == 3) {
                        Utilities.showToastMessage("Data entry operator removed successfully", context, true);

                        if (DeList != null) {
                            DeList.clear();
                        }

                        new GetDeopDetailsList().execute(CampId, CampDate, "64");
                    } else if (type == 4) {

                        Utilities.showToastMessage("Flexi phlebo removed successfully", context, true);

                        if (flexiPhleboList != null) {
                            flexiPhleboList.clear();
                        }

                        new GetFlexiPhleboDetailsList().execute(CampId, CampDate, "146");

                    } else if (type == 5) {

                        Utilities.showToastMessage("Flexi Doctor removed successfully", context, true);

                        if (flexidoctorList != null) {
                            flexidoctorList.clear();
                        }

                        new GetFlexiDoctorDetailsList().execute(CampId, CampDate, "146");

                    } else if (type == 6) {

                        Utilities.showToastMessage("MMU Doctor removed successfully", context, true);

                        if (MMUdoctorList != null) {
                            MMUdoctorList.clear();
                        }

                        new GetMMUDoctorDetailsList().execute(CampId, CampDate, "141");

                    }
                } else {

                    if (type == 2||type == 6){
                        Utilities.showToastMessage("This doctor has already performed some physical examinations, so deletion is not allowed.", context, false);

                    }else {
                        Utilities.showToastMessage("This team can not be removed as beneficiaries are registered by team member", context, false);
                    }

                    new GetDoctorDetailsList().execute(CampId, CampDate, "34");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class GetPhleboDetailsList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedExternalResourceDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<PhleboDetailsModel.OutputBean> Phlebolist = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    phleboDetailsModel = new Gson().fromJson(result, PhleboDetailsModel.class);
                    type = phleboDetailsModel.getStatus();
                    message = phleboDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        Phlebolist = phleboDetailsModel.getOutput();
                        if (Phlebolist.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            // showCampListDialog(campList);

                            rv_phlebo.setLayoutManager(new LinearLayoutManager(context));
                            phleboListDetailsAdapter = new PhleboListDetailsAdapter(phleboDetailsModel.getOutput());
                            rv_phlebo.setAdapter(phleboListDetailsAdapter);
                        } else {
                            rv_phlebo.setLayoutManager(new LinearLayoutManager(context));
                            phleboListDetailsAdapter = new PhleboListDetailsAdapter(phleboDetailsModel.getOutput());
                            rv_phlebo.setAdapter(phleboListDetailsAdapter);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        //  Utilities.showAlertDialog(context, "Fail", "Phlebo Data Not Found", false);
                        if (phleboDetailsModel != null) {
                            rv_phlebo.setLayoutManager(new LinearLayoutManager(context));
                            rv_phlebo.setAdapter(new PhleboListDetailsAdapter(phleboDetailsModel.getOutput()));
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class GetFlexiPhleboDetailsList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedExternalResourceDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<FlexiPhleboDetailsModel.OutputBean> Phlebolist = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    flexiPhleboDetailsModel = new Gson().fromJson(result, FlexiPhleboDetailsModel.class);
                    type = flexiPhleboDetailsModel.getStatus();
                    message = flexiPhleboDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        Phlebolist = flexiPhleboDetailsModel.getOutput();
                        if (Phlebolist.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            // showCampListDialog(campList);

                            rv_flexi.setLayoutManager(new LinearLayoutManager(context));
                            flexiPhleboListDetailsAdapter = new FlexiPhleboListDetailsAdapter(flexiPhleboDetailsModel.getOutput());
                            rv_flexi.setAdapter(flexiPhleboListDetailsAdapter);
                        } else {
                            rv_flexi.setLayoutManager(new LinearLayoutManager(context));
                            flexiPhleboListDetailsAdapter = new FlexiPhleboListDetailsAdapter(flexiPhleboDetailsModel.getOutput());
                            rv_flexi.setAdapter(flexiPhleboListDetailsAdapter);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        //  Utilities.showAlertDialog(context, "Fail", "Phlebo Data Not Found", false);
                        if (flexiPhleboDetailsModel != null) {
                            rv_flexi.setLayoutManager(new LinearLayoutManager(context));
                            rv_flexi.setAdapter(new FlexiPhleboListDetailsAdapter(flexiPhleboDetailsModel.getOutput()));
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class GetDoctorDetailsList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedExternalResourceDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<DoctorDetailsModel.OutputBean> doctorlist = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    doctorDetailsModel = new Gson().fromJson(result, DoctorDetailsModel.class);
                    type = doctorDetailsModel.getStatus();
                    message = doctorDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        doctorlist = doctorDetailsModel.getOutput();
                        if (doctorlist.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            // showCampListDialog(campList);

                            rv_doctor.setLayoutManager(new LinearLayoutManager(context));
                            doctorMultipleSelectDetailsAdapter = new DoctorMultipleSelectDetailsAdapter(doctorDetailsModel.getOutput());

                            rv_doctor.setAdapter(doctorMultipleSelectDetailsAdapter);

                        } else {

                            rv_doctor.setLayoutManager(new LinearLayoutManager(context));
                            doctorMultipleSelectDetailsAdapter = new DoctorMultipleSelectDetailsAdapter(doctorDetailsModel.getOutput());
                            rv_doctor.setAdapter(doctorMultipleSelectDetailsAdapter);

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);

                        }
                    } else {
                        //   Utilities.showAlertDialog(context, "Fail", "Doctor List not found", false);
                        if (doctorDetailsModel != null) {
                            rv_doctor.setLayoutManager(new LinearLayoutManager(context));
                            rv_doctor.setAdapter(new DoctorMultipleSelectDetailsAdapter(doctorDetailsModel.getOutput()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class GetFlexiDoctorDetailsList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedExternalResourceDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<FlexiDoctorDetailsModel.OutputBean> doctorlist = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    flexiDoctorDetailsModel = new Gson().fromJson(result, FlexiDoctorDetailsModel.class);
                    type = flexiDoctorDetailsModel.getStatus();
                    message = flexiDoctorDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        doctorlist = flexiDoctorDetailsModel.getOutput();
                        if (doctorlist.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            // showCampListDialog(campList);

                            rv_flexiDoctor.setLayoutManager(new LinearLayoutManager(context));
                            flexiDoctorMultipleSelectDetailsAdapter = new FlexiDoctorMultipleSelectDetailsAdapter(flexiDoctorDetailsModel.getOutput());

                            rv_flexiDoctor.setAdapter(flexiDoctorMultipleSelectDetailsAdapter);
                        } else {
                            rv_flexiDoctor.setLayoutManager(new LinearLayoutManager(context));
                            flexiDoctorMultipleSelectDetailsAdapter = new FlexiDoctorMultipleSelectDetailsAdapter(flexiDoctorDetailsModel.getOutput());
                            rv_flexiDoctor.setAdapter(doctorMultipleSelectDetailsAdapter);

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        //   Utilities.showAlertDialog(context, "Fail", "Doctor List not found", false);
                        if (flexiDoctorDetailsModel != null) {
                            rv_flexiDoctor.setLayoutManager(new LinearLayoutManager(context));
                            rv_flexiDoctor.setAdapter(new FlexiDoctorMultipleSelectDetailsAdapter(flexiDoctorDetailsModel.getOutput()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class GetMMUDoctorDetailsList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedExternalResourceDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<MMUDoctorDetailsModel.OutputBean> doctorlist = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    MMUDoctorDetailsModel = new Gson().fromJson(result, MMUDoctorDetailsModel.class);
                    type = MMUDoctorDetailsModel.getStatus();
                    message = MMUDoctorDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        doctorlist = MMUDoctorDetailsModel.getOutput();
                        if (doctorlist.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            // showCampListDialog(campList);

                            rv_mmuDoctor.setLayoutManager(new LinearLayoutManager(context));
                            mmuDoctorMultipleSelectDetailsAdapter = new MMUDoctorMultipleSelectDetailsAdapter(MMUDoctorDetailsModel.getOutput());

                            rv_mmuDoctor.setAdapter(mmuDoctorMultipleSelectDetailsAdapter);
                        } else {
                            rv_mmuDoctor.setLayoutManager(new LinearLayoutManager(context));
                            mmuDoctorMultipleSelectDetailsAdapter = new MMUDoctorMultipleSelectDetailsAdapter(MMUDoctorDetailsModel.getOutput());
                            rv_mmuDoctor.setAdapter(mmuDoctorMultipleSelectDetailsAdapter);

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        //   Utilities.showAlertDialog(context, "Fail", "Doctor List not found", false);
                        if (MMUDoctorDetailsModel != null) {
                            rv_mmuDoctor.setLayoutManager(new LinearLayoutManager(context));
                            rv_mmuDoctor.setAdapter(new MMUDoctorMultipleSelectDetailsAdapter(MMUDoctorDetailsModel.getOutput()));
                        }


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    public class GetDeopDetailsList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedExternalResourceDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<DataEntryOperatorDetailsModel.OutputBean> DeopList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    dataEntryOperatorDetailsModel = new Gson().fromJson(result, DataEntryOperatorDetailsModel.class);
                    type = dataEntryOperatorDetailsModel.getStatus();
                    message = dataEntryOperatorDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        DeopList = dataEntryOperatorDetailsModel.getOutput();
                        if (DeopList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));


                            rv_deop.setLayoutManager(new LinearLayoutManager(context));
                            DataEntryOperatorListDetailsAdapter dataEntryOperatorListDetailsAdapter = new DataEntryOperatorListDetailsAdapter(dataEntryOperatorDetailsModel.getOutput());
                            rv_deop.setAdapter(dataEntryOperatorListDetailsAdapter);


                            // showCampListDialog(campList);
                        } else {
                            rv_deop.setLayoutManager(new LinearLayoutManager(context));
                            dataEntryOperatorListDetailsAdapter = new DataEntryOperatorListDetailsAdapter(dataEntryOperatorDetailsModel.getOutput());
                            rv_deop.setAdapter(dataEntryOperatorListDetailsAdapter);


                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        // Utilities.showAlertDialog(context, "Fail", "DataEntryOperator Data Not Found", false);
                        if (dataEntryOperatorDetailsModel != null) {
                            rv_deop.setLayoutManager(new LinearLayoutManager(context));
                            rv_deop.setAdapter(new DataEntryOperatorListDetailsAdapter(dataEntryOperatorDetailsModel.getOutput()));


                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    public class GetTeamDetailsList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("CampDate", params[1]));


            res = WebServiceCall.APICall(ApplicationConstants.GetAssignedTeamDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<TeamsDetailsModel.OutputBean> campList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        campList = teamsDetailsModel.getOutput();
                        if (campList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            // showCampListDialog(campList);


                            rv_teams.setLayoutManager(new LinearLayoutManager(context));
                            teamsDetailsAdapter = new TeamsDetailsAdapter(teamsDetailsModel.getOutput());
                            rv_teams.setAdapter(teamsDetailsAdapter);
                        } else {
                            rv_teams.setLayoutManager(new LinearLayoutManager(context));
                            TeamsDetailsAdapter teamsDetailsAdapter = new TeamsDetailsAdapter(teamsDetailsModel.getOutput());
                            rv_teams.setAdapter(teamsDetailsAdapter);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        // Utilities.showAlertDialog(context, "Fail", "Team Data Not Found", false);
                        if (teamsDetailsModel != null) {

                            rv_teams.setLayoutManager(new LinearLayoutManager(context));
                            rv_teams.setAdapter(new TeamsDetailsAdapter(teamsDetailsModel.getOutput()));

                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    public class GetCampTypeMMU extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.CampTypeMMU, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    NewCampTypeModel newCampTypeModel = new Gson().fromJson(result, NewCampTypeModel.class);
                    type = newCampTypeModel.getStatus();
                    message = newCampTypeModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<NewCampTypeModel.Output> camptypelist = newCampTypeModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showCampTypeDialog(camptypelist);
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

        private void showCampTypeDialog(final List<NewCampTypeModel.Output> camptypelist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Camp Type");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < camptypelist.size(); i++) {
                arrayAdapter.add(String.valueOf(camptypelist.get(i).getCampTypeDescription()));
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
                    tvCampType.setText(camptypelist.get(which).getCampTypeDescription());
                    campTypeId = camptypelist.get(which).getCamptype();
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }


    public class GetDistrictListNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("USERID", params[1]));


            //  res = WebServiceCall.APICall(ApplicationConstants.GetAllDistrictList, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictByUserID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));
                            showDistrictListNewDialog(districtList);
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

    private void showDistrictListNewDialog(final ArrayList<DistrictList_Model> districtList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < districtList.size(); i++) {
            arrayAdapter.add(String.valueOf(districtList.get(i).getDISTNAME()));
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
                tvDistrict.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();

                tvCampList.setText("");
                //  refreshCalendar();

//                new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE,TALLGDCODE,"411025", "1");


            }
        });
        builderSingle.show();

    }

    private void getClusterWiseFlag() {
        final ProgressDialog progressDialog = new ProgressDialog(TeamCampMappingActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);

        Call<AdminActiveInactiveModel> call = apiService.getClusterWiseFlagForMapping(

        );

        call.enqueue(new Callback<AdminActiveInactiveModel>() {
            @Override
            public void onResponse(Call<AdminActiveInactiveModel> call, Response<AdminActiveInactiveModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    AdminActiveInactiveModel model = response.body();
                    String status = model.getStatus();
                    String message = model.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        List<AdminActiveInactiveModel.Output> adminList = model.getOutput();

                        if (adminList != null && !adminList.isEmpty()) {
                            AdminActiveInactiveModel.Output output = adminList.get(0);

                            clusterWiseFlag = output.getDataFlag();
//                            clusterWiseFlag = "1";


                            if (clusterWiseFlag != null) {


                            } else {
                                Utilities.showAlertDialog(context, "Alert", "Cluster wise data flag getting null", false);
                            }

                        } else {
//                            clearCampTextViews();
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);

//                            tv_total.setText("NA");
//                            tv_months_beneficiary.setText("NA");
//                            tv_todays_beneficiary.setText("NA");
//                            tvZeroCampCount.setText("NA");
//


                        }
                    } else {
//                        clearCampTextViews();
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
//                    clearCampTextViews();
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            }

            @Override
            public void onFailure(Call<AdminActiveInactiveModel> call, Throwable t) {
                progressDialog.dismiss();
//                clearCampTextViews();
                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        });
    }


}


