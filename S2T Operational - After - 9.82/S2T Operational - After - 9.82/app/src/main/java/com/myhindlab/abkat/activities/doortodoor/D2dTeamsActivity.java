package com.myhindlab.abkat.activities.doortodoor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.myhindlab.abkat.activities.CampCalendar_Activity;
import com.myhindlab.abkat.adapters.doortodoor.AdminActiveInactiveTeamAdapter;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.NewCampTypeModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
import com.myhindlab.abkat.models.doortodoor.ChannelPartnerModel;
import com.myhindlab.abkat.models.doortodoor.D2dWorkingTeamModel;
import com.myhindlab.abkat.models.doortodoor.DivisionModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class D2dTeamsActivity extends AppCompatActivity {
    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private Button btn_Submit;

    private LinearLayout llMainLLFilter;

    private TextView WorkingTeamCount,tvOrganization, tv_Working_TeamCount, tv_CampType, tv_Division, tv_District, tv_Lab, tv_DivisionName, tvDistrict, tv_NotWorkingCount, NotWorkingTeamCount, TotalTeamCount;
    private String DESGID, EmpCode, divisionName,organizationId = "0", LabCode, CampDATE, DISTLGDCODE = "0", district, TALLGDCODE, taluka, STATELGDCODE = "2",
            divisioId = "0", camptypeId = "0", labId = "0", channekPartnerId = "0",
            selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId, AssignedId = "", IsTeam;
    private RecyclerView rv_d2dTeams;
    private AdminActiveInactiveTeamAdapter adminActiveInactiveTeamAdapter;
    private List<AdminActiveInactiveModel.Output> adminlist;
    private int flag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2d_teams);

        initView();
        getSessionData();
        setDefault();
        setUpToolbar();
        setEventHandlers();

    }


    private void initView() {

        context = D2dTeamsActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        adminlist = new ArrayList<>();


        rv_d2dTeams = findViewById(R.id.rv_d2dTeams);
        WorkingTeamCount = findViewById(R.id.WorkingTeamCount);
        NotWorkingTeamCount = findViewById(R.id.NotWorkingTeamCount);
        TotalTeamCount = findViewById(R.id.TotalTeamCount);
        tv_Working_TeamCount = findViewById(R.id.tv_Working_TeamCount);
        tv_NotWorkingCount = findViewById(R.id.tv_NotWorkingCount);
        tv_CampType = findViewById(R.id.tv_CampType);
        tv_Division = findViewById(R.id.tv_Division);
        tv_DivisionName = findViewById(R.id.tv_DivisionName);
        tvDistrict = findViewById(R.id.tvDistrict);
        tv_Lab = findViewById(R.id.tv_Lab);
        tv_District = findViewById(R.id.tv_District);
        btn_Submit = findViewById(R.id.btn_Submit);
        llMainLLFilter = findViewById(R.id.llMainLLFilter);
        tvOrganization = findViewById(R.id.tvOrganization);
        rv_d2dTeams.setLayoutManager(new LinearLayoutManager(context));
    }


    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                LabCode = json.getString("LabCode");


                if (DESGID.equalsIgnoreCase("83")) {

                    divisioId = json.getString("divid");
                    divisionName = json.getString("DIVNAME");

                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void setEventHandlers() {
        WorkingTeamCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, D2dTeamsNotWorkingTeamActivity.class)
                        .putExtra("District", DISTLGDCODE)
                        .putExtra("CampType", camptypeId)
                        .putExtra("DivisionId", divisioId)
                        .putExtra("lab", labId)
                        .putExtra("subOrganization", organizationId)
                        .putExtra("flag", 3)
                );

            }
        });

        NotWorkingTeamCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, D2dTeamsNotWorkingTeamActivity.class)
                        .putExtra("District", DISTLGDCODE)
                        .putExtra("CampType", camptypeId)
                        .putExtra("DivisionId", divisioId)
                        .putExtra("lab", labId)
                        .putExtra("subOrganization", organizationId)
                        .putExtra("flag", 2)
                );
            }
        });


        tv_CampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetCampTypeNew().execute("1");

            }
        });

        tvOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetOrganization().execute(EmpCode, DESGID);
            }
        });


        tv_Division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DESGID.equals("171") || DESGID.equals("170") || DESGID.equals("182") || DESGID.equals("183")){
                    new GetDivisionForRMVM().execute(organizationId,EmpCode,DESGID);
                }else {
//                    new GetDivisionNew().execute(STATELGDCODE);

                    new GetDivisionForRMVM().execute(organizationId,EmpCode,DESGID);

                }
            }
        });


        tv_District.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_Division.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Please select division", false);
                    return;
                }
                if (DESGID.equals("171") || DESGID.equals("170") || DESGID.equals("182") || DESGID.equals("183")){
                    new GetDistrictListFor().execute(organizationId,EmpCode,DESGID,divisioId,DISTLGDCODE);
                }else {
//                    new GetDistrictListNew().execute(STATELGDCODE, divisioId);
                    new GetDistrictListFor().execute(organizationId,EmpCode,DESGID,divisioId,DISTLGDCODE);
                }
            }
        });

        tv_Lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_District.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Please select district", false);
                    return;
                }

                new GetLabNew().execute(DISTLGDCODE);

            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetAdminData().execute( camptypeId, divisioId, DISTLGDCODE, labId, channekPartnerId);
                new GetAdminDataCount().execute( camptypeId, divisioId, DISTLGDCODE, labId, "0", channekPartnerId);

            }
        });


    }


    private void setDefault() {

        tv_CampType.setText("All");

        if (!(DESGID.equals("51")|| DESGID.equals("201")|| DESGID.equals("53") || DESGID.equals("101") || DESGID.equals("102"))){
            new GetOrganizationNew().execute(EmpCode, DESGID);
        }


        if (DESGID.equalsIgnoreCase("83")) {
            tv_Division.setText(divisionName);
            tv_Division.setEnabled(false);
        }

        if (DESGID.equalsIgnoreCase("83")) {
            if (Utilities.isNetworkAvailable(context)) {
                new GetAdminData().execute( camptypeId, divisioId, "0", labId, "0");
            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);

            }
        } else {
            if (Utilities.isNetworkAvailable(context)) {
                new GetAdminData().execute( camptypeId, "0", "0", labId, "0");
            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);


            }
        }


        if (Utilities.isNetworkAvailable(context)) {
            new GetAdminDataCount().execute( camptypeId, divisioId, DISTLGDCODE, labId, "0", "0");
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("D2D Teams");

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


                llMainLLFilter.setVisibility(llMainLLFilter.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);


//
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                AlertDialog alertDialog = null;
//
//                if (DESGID.equalsIgnoreCase("83")) {
//                    View v = LayoutInflater.from(context).inflate(R.layout.working_team_filter_dialog, null, false);
//                    builder.setView(v);
//                    builder.setTitle("Filter");
////                    builder.setIcon(R.drawable.icon_campcreation);
//                    // TextView titletextView = (TextView) v.findViewById(R.id.tv_alert_dialog_title);
//
//                    TextView tvChannelPartner = (TextView) v.findViewById(R.id.tvChannelPartner);
//                    TextView tvCampType = (TextView) v.findViewById(R.id.tvCampType);
//                    TextView tvDivision = (TextView) v.findViewById(R.id.tvDivision);
//                    TextView tvDivisionName = (TextView) v.findViewById(R.id.tvDivisionName);
//                    TextView tvDistrict = (TextView) v.findViewById(R.id.tvDistrict);
//                    TextView tvLab = (TextView) v.findViewById(R.id.tvLab);
//                    Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
//                    alertDialog = builder.create();
//
//                    if (DESGID.equalsIgnoreCase("83")) {
////                        tvDivision.setEnabled(false);
////                        tvDivision.setText(divisioId);
////                        tvDivision.setVisibility(View.GONE);
////                        tvDivisionName.setVisibility(View.GONE);
//
//
//                        tvDivision.setText(divisionName);
//                        tvDivision.setEnabled(false);
//                    }
//
//
////                    tvChannelPartner.setText("All");
////                    tvCampType.setText("All");
////                    tvDivision.setText("All");
////                    tvDistrict.setText("All");
////                    tvLab.setText("All");
//
//
//                    AlertDialog finalAlertDialog = alertDialog;
//
//
//                    tvDistrict.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new GetDistrictList(tvDistrict).execute(STATELGDCODE, divisioId);
//
//                        }
//                    });
//
//
//                    tvLab.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new GetLab(tvLab).execute(DISTLGDCODE);
//
//                        }
//                    });
//
//
//                    tvChannelPartner.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            new GetChannelPartner(tvChannelPartner).execute();
//
//                        }
//                    });
//
//
//                    tvCampType.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new GetCampType(tvCampType).execute("1");
//
//                        }
//                    });
//
//
//                    tvDivision.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new GetDivision(tvDivision).execute(STATELGDCODE);
//                        }
//                    });
//
//                    btnSubmit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (finalAlertDialog != null) {
//                                finalAlertDialog.dismiss();
//                            }
//
//                            new GetAdminData().execute(STATELGDCODE, camptypeId, divisioId, DISTLGDCODE, labId, channekPartnerId);
//
//                            new GetAdminDataCount().execute(STATELGDCODE, camptypeId, divisioId, DISTLGDCODE, labId, "0", channekPartnerId);
//
//                        }
//                    });
//
//                } else {
//                    View v = LayoutInflater.from(context).inflate(R.layout.working_team_filter_dialog, null, false);
//                    builder.setView(v);
//                    builder.setTitle("Filter");
////                    builder.setIcon(R.drawable.icon_campcreation);
//                    // TextView titletextView = (TextView) v.findViewById(R.id.tv_alert_dialog_title);
//
//                    TextView tvChannelPartner = (TextView) v.findViewById(R.id.tvChannelPartner);
//                    TextView tvCampType = (TextView) v.findViewById(R.id.tvCampType);
//                    TextView tvDivision = (TextView) v.findViewById(R.id.tvDivision);
//                    TextView tvDivisionName = (TextView) v.findViewById(R.id.tvDivisionName);
//                    TextView tvDistrict = (TextView) v.findViewById(R.id.tvDistrict);
//                    TextView tvLab = (TextView) v.findViewById(R.id.tvLab);
//                    Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
//
//
////                    tvChannelPartner.setText("All");
////                    tvCampType.setText("All");
////                    tvDivision.setText("All");
////                    tvDistrict.setText("All");
////                    tvLab.setText("All");
////                    DISTLGDCODE = String.valueOf(0);\
//                    alertDialog = builder.create();
//
//
//                    AlertDialog finalAlertDialog = alertDialog;
//
//                    btnSubmit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (finalAlertDialog != null) {
//                                finalAlertDialog.dismiss();
//                            }
//
//                            new GetAdminData().execute(STATELGDCODE, camptypeId, divisioId, DISTLGDCODE, labId, channekPartnerId);
//                            new GetAdminDataCount().execute(STATELGDCODE, camptypeId, divisioId, DISTLGDCODE, labId, "0", channekPartnerId);
//
//                        }
//                    });
//
//
//                    tvDistrict.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new GetDistrictList(tvDistrict).execute(STATELGDCODE, divisioId);
//                        }
//                    });
//
//
//                    tvChannelPartner.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            new GetChannelPartner(tvChannelPartner).execute();
//                        }
//                    });
//
//                    tvCampType.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new GetCampType(tvCampType).execute("1");
//
//                        }
//                    });
//
//
//                    tvDivision.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new GetDivision(tvDivision).execute(STATELGDCODE);
//                        }
//                    });
//
//
//                    tvLab.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new GetLab(tvLab).execute(DISTLGDCODE);
//
//                        }
//                    });
//
//
//                }
//
//
////                tvCampType.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////
////                    }
////                });
//
//
////                AlertDialog alertDialog = builder.create();
////                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        dialogInterface.dismiss();
////                    }
////                });
//
//                alertDialog.show();

            }
        });
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

                            showDistrictListDialogNew(districtList);
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

    private void showDistrictListDialogNew(final ArrayList<DistrictList_Model> districtList) {
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
                tv_District.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
                //  refreshCalendar();

                tv_Lab.setText("");
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

    public class GetCampTypeNew extends AsyncTask<String, Void, String> {


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
                    tv_CampType.setText(camptypelist.get(which).getCampTypeDescription());
                    camptypeId = String.valueOf(camptypelist.get(which).getCamptype());
                    //  refreshCalendar();

                    if (DESGID.equalsIgnoreCase("83")){
                        tv_District.setText("");
                        tv_Lab.setText("");


                    }else {
                        tv_Division.setText("");
                        tv_District.setText("");
                        tv_Lab.setText("");

                    }




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

    public class GetDivisionNew extends AsyncTask<String, Void, String> {


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

                            showDivisionListDialogNew(divisionList);
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

    private void showDivisionListDialogNew(final List<DivisionModel.Output> divisionList) {
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
                tv_Division.setText(divisionList.get(which).getDivname());
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

    public class GetLabNew extends AsyncTask<String, Void, String> {


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

                    LandingLabModel landingLabModel = new Gson().fromJson(result, LandingLabModel.class);
                    type = landingLabModel.getStatus();
                    message = landingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<LandingLabModel.Output> landinglablist = landingLabModel.getOutput();
                        if (landinglablist.size() > 0) {
                            showLadingLabDialogue(landinglablist);
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

        private void showLadingLabDialogue(final List<LandingLabModel.Output> landinglablist) {
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
                    tv_Lab.setText(landinglablist.get(which).getLabName());
                    labId = String.valueOf(landinglablist.get(which).getLabCode());

                }
            });
            builderSingle.show();

        }
    }

    public class GetAdminData extends AsyncTask<String, Void, String> {


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
//            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("GLOUSERID", EmpCode));
            param.add(new ParamsPojo("CampType", params[0]));
            param.add(new ParamsPojo("DivId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("LabCode", params[3]));
            param.add(new ParamsPojo("DesgId", DESGID));
            param.add(new ParamsPojo("SubOrgId", organizationId));
//            param.add(new ParamsPojo("CatagoryID", params[5]));
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridData, ApplicationConstants.webservice_d2d, param);

            if (BuildConfig.isBeta){
                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridData_V2, ApplicationConstants.webservice_d2d, param);

            }else {
//                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridData_V1, ApplicationConstants.webservice_d2d, param);

                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridData_V2, ApplicationConstants.webservice_d2d, param);


            }

//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridData_V2, ApplicationConstants.webservice_d2d, param);
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
                        adminlist = adminActiveInactiveModel.getOutput();
                        if (adminlist != null && adminlist.size() > 0) {

                            rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context, adminlist));


                            int t = 0;
                            int p = 0;

                            for (AdminActiveInactiveModel.Output output :
                                    adminlist) {

                                t = t + (output.getWorkingTeamCount());
                                p = p + (output.getNonWorkingTeamCount());

                            }
                            tv_NotWorkingCount.setText("" + p);
                            tv_Working_TeamCount.setText("" + t);


//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {

                            rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context, new ArrayList<>()));
                            WorkingTeamCount.setText("");
                            NotWorkingTeamCount.setText("");
                            TotalTeamCount.setText("");

                            tv_NotWorkingCount.setText("");
                            tv_Working_TeamCount.setText("");

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {

                        rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context, new ArrayList<>()));
                        WorkingTeamCount.setText("");
                        NotWorkingTeamCount.setText("");
                        TotalTeamCount.setText("");

                        tv_NotWorkingCount.setText("");
                        tv_Working_TeamCount.setText("");

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


    public class GetAdminDataCount extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("GLOUSERID", EmpCode));
            param.add(new ParamsPojo("CampType", params[0]));
            param.add(new ParamsPojo("DivId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("LabCode", params[3]));
            param.add(new ParamsPojo("DesgId", DESGID));
            param.add(new ParamsPojo("SubOrgId", organizationId));
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsCount, ApplicationConstants.webservice_d2d, param);

            if (BuildConfig.isBeta){
                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsCount_V2, ApplicationConstants.webservice_d2d, param);

            }else {
//                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsCount_V1, ApplicationConstants.webservice_d2d, param);

                res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsCount_V2, ApplicationConstants.webservice_d2d, param);


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


    public class GetDivisionForRMVM extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDivision, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<SubDivisionModel.Output> divisionList = new ArrayList<>();
                    SubDivisionModel pojoDetails = new Gson().fromJson(result, SubDivisionModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = pojoDetails.getOutput();
                        if (divisionList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDivisionListDialogForVMRM(divisionList);
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


    private void showDivisionListDialogForVMRM(final List<SubDivisionModel.Output> divisionList) {
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
                tv_Division.setText(divisionList.get(which).getDivname());
                divisioId = String.valueOf(divisionList.get(which).getDivid());

//                edt_selectdistrict.setText("All");
                DISTLGDCODE ="0";
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
                                tvOrganization.setText(output.getSubOrgName());


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



    public class GetDistrictListFor extends AsyncTask<String, Void, String> {

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

                            showDistrictForListDialog(organizationList);
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


    private void showDistrictForListDialog(final List<DistrictOrgModel.Output> organizationList) {
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
                tv_District.setText(organizationList.get(which).getDistname());
                DISTLGDCODE = String.valueOf(organizationList.get(which).getDistlgdcode());
//                refreshCalendar();
            }
        });
        builderSingle.show();
    }


    public class GetOrganization extends AsyncTask<String, Void, String> {

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

//                            if (pojoDetails.getOutput().size() > 0) {
//                                SubOrganizationModel.Output output = pojoDetails.getOutput().get(0);
//
//                                organizationId = String.valueOf(output.getSubOrgId());
//
//
//                                if (organizationId!=null){
//                                    new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }
//
//
//                            }


                            showOrganizationListDialog(organizationList);
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


    private void showOrganizationListDialog(final List<SubOrganizationModel.Output> organizationList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Organization");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < organizationList.size(); i++) {
            arrayAdapter.add(String.valueOf(organizationList.get(i).getSubOrgName()));
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
                tvOrganization.setText(organizationList.get(which).getSubOrgName());
                organizationId = String.valueOf(organizationList.get(which).getSubOrgId());
                divisioId = "0";
                DISTLGDCODE = "0";

                tv_Division.setText("All");
                tv_District.setText("All");


            }
        });
        builderSingle.show();
    }

}