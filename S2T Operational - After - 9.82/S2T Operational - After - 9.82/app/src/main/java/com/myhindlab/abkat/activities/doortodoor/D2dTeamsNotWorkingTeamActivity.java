package com.myhindlab.abkat.activities.doortodoor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.doortodoor.CampCoordinatorNotWorkingTeamAdapter;
import com.myhindlab.abkat.adapters.doortodoor.TeamCallingAdapter;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.NewCampTypeModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
import com.myhindlab.abkat.models.doortodoor.ChannelPartnerModel;
import com.myhindlab.abkat.models.doortodoor.D2dWorkingTeamModel;
import com.myhindlab.abkat.models.doortodoor.DivisionModel;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class D2dTeamsNotWorkingTeamActivity extends AppCompatActivity implements CampCoordinatorNotWorkingTeamAdapter.CampCoordinatorNotWorkingTeamEvent {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private TextView WorkingTeamCount, NotWorkingTeamCount, TotalTeamCount, tvTotalCount;
    private String DESGID, EmpCode, LabCode,organizationId= "0", CampDATE, DISTLGDCODE="0", district, TALLGDCODE, taluka, STATELGDCODE = "2",CampCoId,
            divisioId="0", camptypeId="0", labId="0",channekPartnerId = "0",
            selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId, AssignedId = "", IsTeam;
    private RecyclerView rv_NotWorkingList;
    private CampCoordinatorNotWorkingTeamAdapter campCoordinatorNotWorkingTeamAdapter;
    private List<D2dWorkingTeamModel.Output> notWorkingList;
    private D2dWorkingTeamModel d2dWorkingTeamModel;
    private AdminActiveInactiveModel.Output adminActiveInactiveModel;
    private LinearLayout llMain;

    private int flag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2d_not_working_teams);

        initView();
        getSessionData();
        setEventHandlers();
        setUpToolbar();
        setDefault();

    }

    private void initView() {

        context = D2dTeamsNotWorkingTeamActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        notWorkingList = new ArrayList<>();
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 1);
        rv_NotWorkingList = findViewById(R.id.rv_NotWorkingList);
        WorkingTeamCount = findViewById(R.id.WorkingTeamCount);
        NotWorkingTeamCount = findViewById(R.id.NotWorkingTeamCount);
        TotalTeamCount = findViewById(R.id.TotalTeamCount);
        llMain = findViewById(R.id.llMain);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        rv_NotWorkingList.setLayoutManager(new LinearLayoutManager(context));
    }
    private void setEventHandlers() {

        WorkingTeamCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.isNetworkAvailable(context)) {
                    new GetWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,"0");
                    new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,"0");
                    setUpToolbarNew();
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
        });

        NotWorkingTeamCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.isNetworkAvailable(context)) {
                    new GetNotWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID);

                    new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,DESGID);
                    setUpToolbar();

                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);


                }

            }
        });
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
//                UserId = json.getString("UserId");
                LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void setDefault() {


        if (flag == 1) {

            if (Utilities.isNetworkAvailable(context)) {
                new GetNotWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID);
            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);

            }


            if (Utilities.isNetworkAvailable(context)) {
                new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,"0");
            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);

            }


            new GetOrganizationNew().execute(EmpCode, DESGID);



        }


        Intent intent = getIntent();

        if (flag == 3) {

            DISTLGDCODE = intent.getStringExtra("District");
            camptypeId = intent.getStringExtra("CampType");
            divisioId = intent.getStringExtra("DivisionId");
            organizationId = intent.getStringExtra("subOrganization");
            labId = intent.getStringExtra("lab");
            new GetWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,"0");
            new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,"0");
            llMain.setVisibility(View.GONE);
            setUpToolbarNew();


        }
        if (flag == 4) {
            DISTLGDCODE = String.valueOf(intent.getSerializableExtra("district"));
            camptypeId = String.valueOf(intent.getSerializableExtra("campType"));
            divisioId = String.valueOf(intent.getSerializableExtra("divisionId"));
            labId = String.valueOf(intent.getSerializableExtra("Lab"));
            CampCoId = String.valueOf(intent.getSerializableExtra("CampCoId"));
            DESGID = String.valueOf(intent.getSerializableExtra("DesId"));
            new GetNotWorkingTamData().execute(CampCoId, camptypeId, divisioId, DISTLGDCODE, labId,DESGID);
//            new GetTeamsDataCount().execute(STATELGDCODE, camptypeId, divisioId, DISTLGDCODE, labId,CampCoId);
            llMain.setVisibility(View.GONE);
            setUpToolbarNew();

        }

        if (flag == 5) {

            DISTLGDCODE = String.valueOf(intent.getSerializableExtra("district"));
            camptypeId = String.valueOf(intent.getSerializableExtra("campType"));
            divisioId = String.valueOf(intent.getSerializableExtra("divisionId"));
            labId = String.valueOf(intent.getSerializableExtra("Lab"));
            CampCoId = String.valueOf(intent.getSerializableExtra("CampCoId"));
            DESGID = String.valueOf(intent.getSerializableExtra("DesId"));
            new GetWorkingTamData().execute(CampCoId, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,"0");
//            new GetTeamsDataCount().execute(STATELGDCODE, camptypeId, divisioId, DISTLGDCODE, labId,CampCoId);
            llMain.setVisibility(View.GONE);
            setUpToolbarNew();
        }

        if (flag == 2) {

            DISTLGDCODE = intent.getStringExtra("District");
            camptypeId = intent.getStringExtra("CampType");
            divisioId = intent.getStringExtra("DivisionId");
            labId = intent.getStringExtra("lab");
            organizationId = intent.getStringExtra("subOrganization");
            new GetNotWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID);
            new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,"0");
            llMain.setVisibility(View.GONE);
            setUpToolbar();
        }

    }

    private void setUpToolbarNew() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("D2D Working Teams");

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        if (flag==3 || flag==4 || flag==5){
        btn_save_accordian.setVisibility(View.GONE);
        }

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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog alertDialog = null;


                if (DESGID.equalsIgnoreCase("162")|| (DESGID.equalsIgnoreCase("92")
                        || (DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("160")||DESGID.equalsIgnoreCase("108")
                        ||DESGID.equalsIgnoreCase("136") || DESGID.equalsIgnoreCase("139")))) {
                    View v = LayoutInflater.from(context).inflate(R.layout.working_team_filter_dialog_d2d_campcoordinator, null, false);
                    builder.setView(v);
                    builder.setTitle("Filter");
//                    builder.setIcon(R.drawable.icon_campcreation);
                    // TextView titletextView = (TextView) v.findViewById(R.id.tv_alert_dialog_title);

//                    TextView tvChannelPartner = (TextView) v.findViewById(R.id.tvChannelPartner);
//                    TextView tvCampType = (TextView) v.findViewById(R.id.tvCampType);
                    TextView tvDivision = (TextView) v.findViewById(R.id.tvDivision);
                    TextView tvDistrict = (TextView) v.findViewById(R.id.tvDistrict);
                    TextView tvLab = (TextView) v.findViewById(R.id.tvLab);
                    Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);

                    alertDialog = builder.create();

                    AlertDialog finalAlertDialog = alertDialog;


                    tvDistrict.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            new GetDistrictListForCampCoordinator(tvDistrict).execute(STATELGDCODE, "0",EmpCode);
                            new GetDistrictListFor(tvDistrict).execute(organizationId,EmpCode,DESGID,divisioId,DISTLGDCODE);

                        }
                    });


                    tvLab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetLab(tvLab).execute(DISTLGDCODE,EmpCode);

                        }
                    });

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (finalAlertDialog != null) {
                                finalAlertDialog.dismiss();

                            }
                            new GetNotWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID);

                            new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,channekPartnerId);

//                            if (notWorkingList!= null){
//                                notWorkingList.clear();
//
//                            }
//                            try {
//
//
//                                if (d2dWorkingTeamModel.getOutput() != null) {
//                                    d2dWorkingTeamModel.getOutput().clear();
//                                    campCoordinatorNotWorkingTeamAdapter.notifyDataSetChanged();
//
//                                }
////
//
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }

                        }
                    });


                } else {
                    View v = LayoutInflater.from(context).inflate(R.layout.working_team_filter_dialog, null, false);
                    builder.setView(v);
                    builder.setTitle("Filter");
                    // TextView titletextView = (TextView) v.findViewById(R.id.tv_alert_dialog_title);

                    TextView tvChannelPartner = (TextView) v.findViewById(R.id.tvChannelPartner);
                    TextView tvCampType = (TextView) v.findViewById(R.id.tvCampType);
                    TextView tvDivision = (TextView) v.findViewById(R.id.tvDivision);
                    TextView tvDistrict = (TextView) v.findViewById(R.id.tvDistrict);
                    TextView tvLab = (TextView) v.findViewById(R.id.tvLab);
                    Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);

                    tvChannelPartner.setText("All");
                    tvCampType.setText("All");
                    tvDivision.setText("All");
                    tvDistrict.setText("All");
//                    DISTLGDCODE = String.valueOf(0);\
                    alertDialog = builder.create();

                    AlertDialog finalAlertDialog = alertDialog;
                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (finalAlertDialog != null) {
                                finalAlertDialog.dismiss();

                            }
                            new GetNotWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID);

                            new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,channekPartnerId);

//                            if (notWorkingList!= null){
//                                notWorkingList.clear();
//
//                            }
//                            try {
//
//
//                                if (d2dWorkingTeamModel.getOutput() != null) {
//                                    d2dWorkingTeamModel.getOutput().clear();
//                                    campCoordinatorNotWorkingTeamAdapter.notifyDataSetChanged();
//
//                                }
////
//
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }

                        }
                    });


                    tvDistrict.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetDistrictList(tvDistrict).execute(STATELGDCODE, divisioId);

                        }
                    });


                    tvChannelPartner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new GetChannelPartner(tvChannelPartner).execute();

                        }
                    });

                    tvCampType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetCampType(tvCampType).execute(channekPartnerId);

                        }
                    });


                    tvDivision.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetDivision(tvDivision).execute(STATELGDCODE);

                        }
                    });


                    tvLab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetLab(tvLab).execute(DISTLGDCODE,EmpCode);

                        }
                    });


                }


//                tvCampType.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });

//                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });

                alertDialog.show();

            }
        });
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (flag == 3 || flag == 5) {
            getSupportActionBar().setTitle("D2D Working Teams");

        } else {
            getSupportActionBar().setTitle("D2D Not Working Teams");

        }

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        if (flag==2 ||flag == 4 || flag ==5){
            btn_save_accordian.setVisibility(View.GONE);
        }


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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog alertDialog = null;

                if (DESGID.equalsIgnoreCase("162")|| (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29")
                        ||DESGID.equalsIgnoreCase("160")||DESGID.equalsIgnoreCase("108")
                        ||DESGID.equalsIgnoreCase("136") || DESGID.equalsIgnoreCase("139")))) {
                    View v = LayoutInflater.from(context).inflate(R.layout.working_team_filter_dialog_d2d_campcoordinator, null, false);
                    builder.setView(v);
                    builder.setTitle("Search Team Status");
//                    builder.setIcon(R.drawable.icon_campcreation);
                    // TextView titletextView = (TextView) v.findViewById(R.id.tv_alert_dialog_title);

//                    TextView tvChannelPartner = (TextView) v.findViewById(R.id.tvChannelPartner);
//                    TextView tvCampType = (TextView) v.findViewById(R.id.tvCampType);
                    TextView tvDivision = (TextView) v.findViewById(R.id.tvDivision);
                    TextView tvDistrict = (TextView) v.findViewById(R.id.tvDistrict);
                    TextView tvLab = (TextView) v.findViewById(R.id.tvLab);
                    Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
                    alertDialog = builder.create();

                    AlertDialog finalAlertDialog = alertDialog;


                    tvDistrict.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            new GetDistrictListForCampCoordinator(tvDistrict).execute(STATELGDCODE, "0",EmpCode);

                            new GetDistrictListFor(tvDistrict).execute(organizationId,EmpCode,DESGID,divisioId,DISTLGDCODE);


                        }
                    });

                    tvLab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetLab(tvLab).execute(DISTLGDCODE,EmpCode);

                        }
                    });

                    tvDivision.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetDivision(tvDivision).execute(STATELGDCODE);

                        }
                    });


                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (finalAlertDialog != null) {
                                finalAlertDialog.dismiss();

                            }
                            new GetNotWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID);

                            new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,channekPartnerId);

//                            if (notWorkingList!= null){
//                                notWorkingList.clear();
//
//                            }
//                            try {
//
//
//                                if (d2dWorkingTeamModel.getOutput() != null) {
//                                    d2dWorkingTeamModel.getOutput().clear();
//                                    campCoordinatorNotWorkingTeamAdapter.notifyDataSetChanged();
//
//                                }
////
//
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }

                        }
                    });

                } else {
                    View v = LayoutInflater.from(context).inflate(R.layout.working_team_filter_dialog, null, false);
                    builder.setView(v);
                    builder.setTitle("Filter");
//                    builder.setIcon(R.drawable.icon_campcreation);
                    // TextView titletextView = (TextView) v.findViewById(R.id.tv_alert_dialog_title);

                    TextView tvChannelPartner = (TextView) v.findViewById(R.id.tvChannelPartner);
                    TextView tvCampType = (TextView) v.findViewById(R.id.tvCampType);
                    TextView tvDivision = (TextView) v.findViewById(R.id.tvDivision);
                    TextView tvDistrict = (TextView) v.findViewById(R.id.tvDistrict);
                    TextView tvLab = (TextView) v.findViewById(R.id.tvLab);
                    Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
//
//                    tvChannelPartner.setText("All");
//                    tvCampType.setText("All");
//                    tvDivision.setText("All");
//                    tvDistrict.setText("All");
//                    DISTLGDCODE = String.valueOf(0);\
                    alertDialog = builder.create();

                    AlertDialog finalAlertDialog = alertDialog;
                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (finalAlertDialog != null) {
                                finalAlertDialog.dismiss();

                            }
                            new GetNotWorkingTamData().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID);

                            new GetTeamsDataCount().execute(EmpCode, camptypeId, divisioId, DISTLGDCODE, labId,DESGID,channekPartnerId);

//                            if (notWorkingList!= null){
//                                notWorkingList.clear();
//
//                            }
//                            try {
//
//
//                                if (d2dWorkingTeamModel.getOutput() != null) {
//                                    d2dWorkingTeamModel.getOutput().clear();
//                                    campCoordinatorNotWorkingTeamAdapter.notifyDataSetChanged();
//
//                                }
////
//
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }

                        }
                    });


                    tvDistrict.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetDistrictList(tvDistrict).execute(STATELGDCODE, divisioId);

                        }
                    });


                    tvChannelPartner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new GetChannelPartner(tvChannelPartner).execute();

                        }
                    });

                    tvCampType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetCampType(tvCampType).execute(channekPartnerId);

                        }
                    });


                    tvDivision.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetDivision(tvDivision).execute(STATELGDCODE);

                        }
                    });


                    tvLab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetLab(tvLab).execute(DISTLGDCODE,EmpCode);

                        }
                    });


                }


//                tvCampType.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });

//                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });

                alertDialog.show();

            }
        });
    }

    @Override
    public void onCallClick(D2dWorkingTeamModel.Output team) {

        Log.d("teamNumber", String.valueOf(team.getTeamid()));

        new GetTeamsCalling().execute(String.valueOf(team.getTeamid()));

    }

    public class GetNotWorkingTamData extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("GLOUSERID", params[0]));
            param.add(new ParamsPojo("CampType", params[1]));
            param.add(new ParamsPojo("DivId", params[2]));
            param.add(new ParamsPojo("DISTLGDCODE", params[3]));
            param.add(new ParamsPojo("LabCode", params[4]));
            param.add(new ParamsPojo("DesgId", params[5]));
            param.add(new ParamsPojo("SubOrgId", organizationId));


            if (BuildConfig.isBeta){
                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DNonWorkingTeams_V2, ApplicationConstants.webservice_d2d, param);

            }else {
//                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DNonWorkingTeams_V1, ApplicationConstants.webservice_d2d, param);
                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DNonWorkingTeams_V2, ApplicationConstants.webservice_d2d, param);


            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    D2dWorkingTeamModel d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);
//                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);


                    type = d2dWorkingTeamModel.getStatus();
                    message = d2dWorkingTeamModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        notWorkingList = d2dWorkingTeamModel.getOutput();
                        if (notWorkingList != null && notWorkingList.size() > 0) {


//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());

                            rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, notWorkingList, D2dTeamsNotWorkingTeamActivity.this::onCallClick));
                            int t = 0;

                            for (D2dWorkingTeamModel.Output output :
                                    notWorkingList) {

                                t = t + Integer.valueOf(output.getRegBeneficieries());


                            }

                            tvTotalCount.setText("" + t);


////                            if (adminActiveInactiveModel.getOutput().size() > 0) {
////                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
////                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
////                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
////                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {
                            rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
                            tvTotalCount.setText("");
                            WorkingTeamCount.setText("");
                            NotWorkingTeamCount.setText("");
                            TotalTeamCount.setText("");
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
                        tvTotalCount.setText("");
                        WorkingTeamCount.setText("");
                        NotWorkingTeamCount.setText("");
                        TotalTeamCount.setText("");
                        Utilities.showAlertDialog(context, "Fail", "Team data not found", false);
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


    public class GetWorkingTamData extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("GLOUSERID", params[0]));
            param.add(new ParamsPojo("CampType", params[1]));
            param.add(new ParamsPojo("DivId", params[2]));
            param.add(new ParamsPojo("DISTLGDCODE", params[3]));
            param.add(new ParamsPojo("LabCode", params[4]));
            param.add(new ParamsPojo("DesgId", params[5]));
            param.add(new ParamsPojo("SubOrgId", organizationId));
          //  res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeamsWithCatagoryID, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeams, ApplicationConstants.webservice_d2d, param);

            if (BuildConfig.isBeta){
                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeams_V2, ApplicationConstants.webservice_d2d, param);

            }else {
//                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeams_V1, ApplicationConstants.webservice_d2d, param);

                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DWorkingTeams_V2, ApplicationConstants.webservice_d2d, param);


            }


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    D2dWorkingTeamModel d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);
                    type = d2dWorkingTeamModel.getStatus();
                    message = d2dWorkingTeamModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        notWorkingList = d2dWorkingTeamModel.getOutput();
                        if (notWorkingList != null && notWorkingList.size() > 0) {


                            rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, notWorkingList, D2dTeamsNotWorkingTeamActivity.this::onCallClick));

                            int t = 0;
                            for (D2dWorkingTeamModel.Output output :
                                    notWorkingList) {

                                t = t + Integer.valueOf(output.getRegBeneficieries());


                            }
                            tvTotalCount.setText("" + t);

                            setUpToolbarNew();


//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {
                            rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
                            tvTotalCount.setText("");
                            WorkingTeamCount.setText("");
                            NotWorkingTeamCount.setText("");
                            TotalTeamCount.setText("");

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {

                        rv_NotWorkingList.setAdapter(new CampCoordinatorNotWorkingTeamAdapter(context, new ArrayList<>(), D2dTeamsNotWorkingTeamActivity.this::onCallClick));
                        tvTotalCount.setText("");
                        WorkingTeamCount.setText("");
                        NotWorkingTeamCount.setText("");
                        TotalTeamCount.setText("");
                        Utilities.showAlertDialog(context, "Fail", "Team data not found", false);
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


    public class GetDistrictList extends AsyncTask<String, Void, String> {
        TextView tvDistrict;

        public GetDistrictList(TextView tvDistrict) {
            this.tvDistrict = tvDistrict;
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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("DivId", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetDivisionWiseDistrict, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    //    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showDistrictListDialog(districtList, tvDistrict);
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

    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList, TextView tvDistrict) {
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



    public class GetDistrictListForCampCoordinator extends AsyncTask<String, Void, String> {
        TextView tvDistrict;

        public GetDistrictListForCampCoordinator(TextView tvDistrict) {
            this.tvDistrict = tvDistrict;
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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("DivId", params[1]));
            param.add(new ParamsPojo("USERID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetDivisionWiseDistrictAndUserID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    //    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showDistrictListDialogForCamp(districtList, tvDistrict);
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


    private void showDistrictListDialogForCamp(final ArrayList<DistrictList_Model> districtList, TextView tvDistrict) {
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




    public class GetChannelPartner extends AsyncTask<String, Void, String> {

        TextView tvChannelPartner;

        public GetChannelPartner(TextView tvChannelPartner) {

            this.tvChannelPartner = tvChannelPartner;

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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            res = WebServiceCall.APICall(ApplicationConstants.GetChannelPartnerList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    //    districtList = new ArrayList<>();
                    List<ChannelPartnerModel.Output> partnerList = new ArrayList<>();
                    ChannelPartnerModel channelPartnerModel = new Gson().fromJson(result, ChannelPartnerModel.class);
                    type = channelPartnerModel.getStatus();
                    message = channelPartnerModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        partnerList = channelPartnerModel.getOutput();
                        if (partnerList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showChannelParnerListDialog(partnerList, tvChannelPartner);
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


    private void showChannelParnerListDialog(final List<ChannelPartnerModel.Output> partnerList, TextView tvChannelPartner) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Channel Partner");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < partnerList.size(); i++) {
            arrayAdapter.add(String.valueOf(partnerList.get(i).getCatagoryName()));
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
                tvChannelPartner.setText(partnerList.get(which).getCatagoryName());
                channekPartnerId = String.valueOf(partnerList.get(which).getCatagoryID());
                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


    public class GetCampType extends AsyncTask<String, Void, String> {
        TextView tvCampType;

        public GetCampType(TextView tvCampType) {
            this.tvCampType = tvCampType;

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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CatagoryID", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampTypeByChannelPartner, ApplicationConstants.webservice_d2d, param);
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
                            showCampTypeDialog(camptypelist, tvCampType);
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

        private void showCampTypeDialog(final List<NewCampTypeModel.Output> camptypelist, TextView tvCampType) {
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
                    camptypeId = String.valueOf(camptypelist.get(which).getCamptype());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

    }

    public class GetDivision extends AsyncTask<String, Void, String> {

        TextView tvDivision;

        public GetDivision(TextView tvDivision) {
            this.tvDivision = tvDivision;

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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("STATELGDCODE", params[0]));


            res = WebServiceCall.APICall(ApplicationConstants.GetDivision, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    //    districtList = new ArrayList<>();
                    List<DivisionModel.Output> divisionList = new ArrayList<>();
                    DivisionModel divisionModel = new Gson().fromJson(result, DivisionModel.class);
                    type = divisionModel.getStatus();
                    message = divisionModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = divisionModel.getOutput();
                        if (divisionList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showDivisionListDialog(divisionList, tvDivision);
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


    private void showDivisionListDialog(final List<DivisionModel.Output> divisionList, TextView tvDivision) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Division");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < divisionList.size(); i++) {
            arrayAdapter.add(String.valueOf(divisionList.get(i).getDivname()));
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
                tvDivision.setText(divisionList.get(which).getDivname());
                divisioId = String.valueOf(divisionList.get(which).getDivid());
                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }

    public class GetLab extends AsyncTask<String, Void, String> {
        TextView tvLab;

        public GetLab(TextView tvLab) {
            this.tvLab = tvLab;
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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("USERID", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.GetLabByUserID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    LandingLabModel landingLabModel = new Gson().fromJson(result, LandingLabModel.class);
                    type = landingLabModel.getStatus();
                    message = landingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<LandingLabModel.Output> landinglablist = landingLabModel.getOutput();
                        if (landinglablist.size() > 0) {
                            showLadingLabDialogue(landinglablist, tvLab);
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

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }

        private void showLadingLabDialogue(final List<LandingLabModel.Output> landinglablist, TextView tvLab) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Lab");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

            for (int i = 0; i < landinglablist.size(); i++) {
                arrayAdapter.add(String.valueOf(landinglablist.get(i).getLabName()));
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
                    tvLab.setText(landinglablist.get(which).getLabName());
                    labId = String.valueOf(landinglablist.get(which).getLabCode());

                }
            });
            builderSingle.show();

        }
    }

    public class GetTeamsDataCount extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("GLOUSERID", params[0]));
            param.add(new ParamsPojo("CampType", params[1]));
            param.add(new ParamsPojo("DivId", params[2]));
            param.add(new ParamsPojo("DISTLGDCODE", params[3]));
            param.add(new ParamsPojo("LabCode", params[4]));
            param.add(new ParamsPojo("DesgId", params[5]));
         //   param.add(new ParamsPojo("CatagoryID", params[6]));
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsCount, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsCount_V1, ApplicationConstants.webservice_d2d, param);
          //  res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsCountWithCatagoryID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    AdminActiveInactiveModel adminActiveInactiveModel = new Gson().fromJson(result, AdminActiveInactiveModel.class);
                    type = adminActiveInactiveModel.getStatus();
                    message = adminActiveInactiveModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
//                        adminlist =adminActiveInactiveModel.getOutput();
//                        if (adminlist != null && adminlist.size() > 0){
//
//                            rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context,adminlist));


                        if (adminActiveInactiveModel.getOutput().size() > 0) {
                            AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
                            WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
                            NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
                            TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {

                        WorkingTeamCount.setText("");
                        NotWorkingTeamCount.setText("");
                        TotalTeamCount.setText("");
                        Utilities.showAlertDialog(context, "Fail", "Team data not found", false);
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
            param.add(new ParamsPojo("Teamid", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamMembersDetailsForCalling, ApplicationConstants.webservice_d2d, param);
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
                        TeamCallingAdapter teamCallingAdapter = new TeamCallingAdapter(context, teamCallingModel.getOutput());
                        recyclerView.setAdapter(teamCallingAdapter);

//                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                TeamCallingModel.Output campDetails = campList.get(position);
//                            }
//                        }));

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


    public class GetDistrictListFor extends AsyncTask<String, Void, String> {


        TextView tvDistrict;
        public GetDistrictListFor(TextView tvDistrict) {
            this.tvDistrict = tvDistrict;

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
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();

            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDistrict, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<DistrictOrgModel.Output> organizationList = new ArrayList<>();
                    DistrictOrgModel pojoDetails = new Gson().fromJson(result, DistrictOrgModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDistrictForListDialog(organizationList,tvDistrict);
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


    private void showDistrictForListDialog(final List<DistrictOrgModel.Output> organizationList,TextView tvDistrict) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < organizationList.size(); i++) {
            arrayAdapter.add(String.valueOf(organizationList.get(i).getDistname()));
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
                tvDistrict.setText(organizationList.get(which).getDistname());
                DISTLGDCODE = String.valueOf(organizationList.get(which).getDistlgdcode());
//                refreshCalendar();
            }
        });
        builderSingle.show();
    }


    public class GetOrganizationNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DESGID", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.BindOrg, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<SubOrganizationModel.Output> organizationList = new ArrayList<>();
                    SubOrganizationModel pojoDetails = new Gson().fromJson(result, SubOrganizationModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            if (pojoDetails.getOutput().size() > 0) {
                                SubOrganizationModel.Output output = pojoDetails.getOutput().get(0);

                                organizationId = String.valueOf(output.getSubOrgId());
//                                tvOrganization.setText(output.getSubOrgName());


//                                if (organizationId!=null){
//                                    new CampCalendar_Activity.GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new CampCalendar_Activity.GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }

                            }


//                            showOrganizationListDialog(organizationList);
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

}