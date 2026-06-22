package com.myhindlab.abkat.activities.doortodoor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.doortodoor.PhysicalExaminationDetailsAdapter;
import com.myhindlab.abkat.adapters.doortodoor.PhysicalExaminationDetailsTeamWiseAdapter;
import com.myhindlab.abkat.adapters.doortodoor.TeamCallingAdapter;
import com.myhindlab.abkat.adapters.doortodoor.TeamCallingForPhysicalExaminationAdapter;
import com.myhindlab.abkat.fragments.CampBeneficiaryList_Fragment;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.NewCampTypeModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
import com.myhindlab.abkat.models.doortodoor.ChannelPartnerModel;
import com.myhindlab.abkat.models.doortodoor.DivisionModel;
import com.myhindlab.abkat.models.doortodoor.PhysicalExaminationDetailsModel;
import com.myhindlab.abkat.models.doortodoor.PhysicalExaminationDetailsTeamWiseModel;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class D2dPhysicalExaminationDetailsTeamWiseActivity extends AppCompatActivity implements PhysicalExaminationDetailsTeamWiseAdapter.PhysicalExminationTeamEvent  {
    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;


    private TextView WorkingTeamCount,tv_select_district,tv_select_campid,tvAssignedTotal,tvCallingPendingTotal,tvPhysicalExamPendingTotal, NotWorkingTeamCount,tv_from_date,tv_to_date, TotalTeamCount;
    private String DESGID, EmpCode, LabCode, DISTLGDCODE = "", district,campname,campdate, TALLGDCODE, taluka, STATELGDCODE = "2",
            divisioId = "0", camptypeId = "0", labId = "0",channekPartnerId,
            selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate,CampFromdate, UserId, AssignedId = "", IsTeam;
    private RecyclerView rv_d2dTeams;
    private PhysicalExaminationDetailsTeamWiseModel physicalExaminationDetailsModel;
    private List<PhysicalExaminationDetailsTeamWiseModel.Output> adminlist;
    private int flag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2d_physical_examination_details_team_wise);

        initView();
        getSessionData();
        setDefault();
        setEventHandlers();
        setUpToolbar();
    }


    private void initView() {

        context = D2dPhysicalExaminationDetailsTeamWiseActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        adminlist = new ArrayList<>();


        rv_d2dTeams = findViewById(R.id.rv_d2dTeams);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_select_district = findViewById(R.id.tv_select_district);
        tv_select_campid = findViewById(R.id.tv_select_campid);
        tvAssignedTotal = findViewById(R.id.tvAssignedTotal);
        tvCallingPendingTotal = findViewById(R.id.tvCallingPendingTotal);
        tvPhysicalExamPendingTotal = findViewById(R.id.tvPhysicalExamPendingTotal);
        rv_d2dTeams.setLayoutManager(new LinearLayoutManager(context));
        Intent intent = getIntent();





        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        tv_from_date.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tv_select_district.setBackgroundColor(Color.parseColor("#CCCCCC"));
        tv_select_campid.setBackgroundColor(Color.parseColor("#CCCCCC"));


        district = intent.getStringExtra("District");
        camptypeId = intent.getStringExtra("campId");
        campdate = intent.getStringExtra("campDate");


        tv_from_date.setText(campdate);
        tv_select_district.setText(district);
        tv_select_campid.setText(camptypeId);


//
//        tv_from_date.setText(Utilities.dfDate.format(new Date()));
//        CampDate = Utilities.dfDate.format(new Date());
//
//        tv_to_date.setText(Utilities.dfDate.format(new Date()));
//        CampFromdate = Utilities.dfDate.format(new Date());

    }


    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
             //   DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void setEventHandlers() {
//        tv_from_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog dpd1 = new DatePickerDialog(context,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
//
//
//                                //
//
//                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);
//
//
//
//                                //                                if (Utilities.isNetworkAvailable(context)) {
//                                //                                } else {
//                                //                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                                //                                }
//                            }
//
//                        }, mYear, mMonth, mDay);
//                try {
//                    dpd1.getDatePicker().setCalendarViewShown(false);
//
//                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                dpd1.show();
//            }
//        });

//        tv_select_district.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utilities.isNetworkAvailable(context)){
//                    new GetDistrictList().execute(CampDate,CampFromdate,EmpCode);
//                }else {
//                    Utilities.showAlertDialog(context,"Alert","Please check your internet connection",false);
//                }
//            }
//        });


//        tv_to_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog dpd1 = new DatePickerDialog(context,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
//
//
//                                //
//
//                                CampFromdate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);
//
//
//
//                                //                                if (Utilities.isNetworkAvailable(context)) {
//                                //                                } else {
//                                //                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                                //                                }
//                            }
//
//                        }, mYear, mMonth, mDay);
//                try {
//                    dpd1.getDatePicker().setCalendarViewShown(false);
//
//                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                dpd1.show();
//            }
//        });


//        NotWorkingTeamCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(context, D2dTeamsNotWorkingTeamActivity.class)
//                        .putExtra("District", DISTLGDCODE)
//                        .putExtra("CampType", camptypeId)
//                        .putExtra("DivisionId", divisioId)
//                        .putExtra("lab", labId)
//                        .putExtra("flag", 2)
//
//
//                );
//
//
//            }
//        });


    }


    private void setDefault() {





        if (Utilities.isNetworkAvailable(context)) {
            new GetTeamWiseData().execute("1", camptypeId, EmpCode,"0");
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);

//
        }


    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("D2D Physical Examination Details");

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        btn_save_accordian.setVisibility(View.GONE);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

    }

    @Override
    public void onCallClick(PhysicalExaminationDetailsTeamWiseModel.Output team) {

        Log.d("teamNumber", String.valueOf(team.getTeamid()));

        new GetTeamsCalling().execute("2",team.getCampId(), String.valueOf(team.getDoctorID()),team.getTeamid());


    }



    public class GetTeamWiseData extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("Type", params[0]));
            param.add(new ParamsPojo("CampID", params[1]));
            param.add(new ParamsPojo("DoctorID", params[2]));
            param.add(new ParamsPojo("TeamID", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.GetD2DTeamWisePhyExamDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    PhysicalExaminationDetailsTeamWiseModel physicalExaminationDetailsTeamWiseModel = new Gson().fromJson(result, PhysicalExaminationDetailsTeamWiseModel.class);
                    type = physicalExaminationDetailsTeamWiseModel.getStatus();
                    message = physicalExaminationDetailsTeamWiseModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminlist = physicalExaminationDetailsTeamWiseModel.getOutput();
                        if (adminlist != null && adminlist.size() > 0) {

                            rv_d2dTeams.setAdapter(new PhysicalExaminationDetailsTeamWiseAdapter(context, adminlist,D2dPhysicalExaminationDetailsTeamWiseActivity.this));

                            int t = 0;
                            int p = 0;
                            int q = 0;

                            for (PhysicalExaminationDetailsTeamWiseModel.Output output :
                                    adminlist) {

                                t = t + Integer.valueOf(output.getAssigned());
                                p = p + Integer.valueOf(output.getCallingPending());
                                q = q + Integer.valueOf(output.getPhyExamPending());

                            }

                            tvAssignedTotal.setText("" + t);
                            tvCallingPendingTotal.setText("" + p);
                            tvPhysicalExamPendingTotal.setText("" + q);



//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {

                            rv_d2dTeams.setAdapter(new PhysicalExaminationDetailsTeamWiseAdapter(context, new ArrayList<>(),D2dPhysicalExaminationDetailsTeamWiseActivity.this));
                           tvAssignedTotal.setText("");
                            tvCallingPendingTotal.setText("");
                            tvPhysicalExamPendingTotal.setText("");
                         //   Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {

                        rv_d2dTeams.setAdapter(new PhysicalExaminationDetailsTeamWiseAdapter(context, new ArrayList<>(),D2dPhysicalExaminationDetailsTeamWiseActivity.this));
                        tvAssignedTotal.setText("");
                        tvCallingPendingTotal.setText("");
                        tvPhysicalExamPendingTotal.setText("");


                      //  Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

             //   Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }

    public class GetTeamsCalling extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Type", params[0]));
            param.add(new ParamsPojo("CampID", params[1]));
            param.add(new ParamsPojo("DoctorID", params[2]));
            param.add(new ParamsPojo("TeamID", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.GetD2DTeamWisePhyExamDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    TeamCallingModel teamCallingModel = new Gson().fromJson(result, TeamCallingModel.class);
                    type = teamCallingModel.getStatus();
                    message = teamCallingModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View v = LayoutInflater.from(context).inflate(R.layout.d2d_team_calling, null, false);
                        builder.setView(v);
                        builder.setTitle("Call To Team");
//                        builder.setIcon(R.drawable.icon_campcreation);
                        RecyclerView recyclerView = v.findViewById(R.id.rv_d2dTeams);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setHasFixedSize(false);
                        TeamCallingForPhysicalExaminationAdapter teamCallingForPhysicalExaminationAdapter = new TeamCallingForPhysicalExaminationAdapter(context, teamCallingModel.getOutput());
                        recyclerView.setAdapter(teamCallingForPhysicalExaminationAdapter);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        alertDialog.show();
                    }


                } else {
                    Utilities.showAlertDialog(context, "Fail", message, false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

            if (Utilities.isNetworkAvailable(context)) {
                new GetTeamWiseData().execute("1", camptypeId, EmpCode,"0");
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);

        }


    }








}