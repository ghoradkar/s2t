package com.myhindlab.abkat.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.DataEntryOperatorListDetailsAdapter;
import com.myhindlab.abkat.adapters.DoctorMultipleSelectDetailsAdapter;
import com.myhindlab.abkat.adapters.LabListAdapter;
import com.myhindlab.abkat.adapters.PhleboListDetailsAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAdapter;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.DataEntryOperatorDetailsModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DoctorDetailsModel;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.PhleboDetailsModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.pojos.Lab_OutPut_Pojo;
import com.myhindlab.abkat.pojos.Lab_Pojo;
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

public class TeamCampMappingdetailsActivity extends AppCompatActivity {

    private TextView tvDate, tvCampList, tvDistrict,tvLab;
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private RecyclerView rv_phlebo, rv_deop, rv_doctor, rv_teams;
    private Button btnAssign;
    private int campTypeId = 3, assignTypeId;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private String DESGID, EmpCode, DISTLGDCODE, district, TALLGDCODE, taluka, STATELGDCODE = "2", selectedLabID = "", selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId;
    private ArrayList<GetTeamsModel.OutputBean> teamList;
    //    private ArrayList<DoctorModel.OutputBean> doctorList;
//    private ArrayList<PhleboModel.OutputBean> phleboList;
//    private ArrayList<DataEntryOperatorModel.OutputBean> DeList;
    private final String TAG = TeamCampMappingdetailsActivity.class.getSimpleName();
    private PhleboDetailsModel phleboDetailsModel;
    private DoctorDetailsModel doctorDetailsModel;
    private DataEntryOperatorDetailsModel dataEntryOperatorDetailsModel;
    private TeamsDetailsModel teamsDetailsModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_camp_mappingdetails);

        initView();
        setEventHandlers();
        getSessionData();
        setUpToolbar();

    }


    private void initView() {
        context = TeamCampMappingdetailsActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        tvDate = findViewById(R.id.tvDate);
        tvCampList = findViewById(R.id.tvCampList);
        rv_teams = findViewById(R.id.rv_teams);
        rv_phlebo = findViewById(R.id.rv_phlebo);
        rv_deop = findViewById(R.id.rv_deop);
        rv_doctor = findViewById(R.id.rv_doctor);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvLab = findViewById(R.id.tvLab);

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());
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
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setEventHandlers() {

        tvDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetDistrictList().execute(STATELGDCODE,DISTLGDCODE);
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
//                                if (Utilities.isNetworkAvailable(context)) {
//                                } else {
//                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                                }
                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        tvCampList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CampDate != null) {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetCampList().execute(CampDate, EmpCode, DISTLGDCODE, String.valueOf(campTypeId),selectedLabID);
                    } else {
                        Utilities.showToastMessage("Please Check Your Connection", context, false);
                    }
                } else {
                    tvDate.setError("Select Camp Date");
                }

            }
        });

        rv_teams.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (teamsDetailsModel != null) {
                    Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + teamsDetailsModel.getOutput().get(position).getTeamName() + " Team?", true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            new RemoveTeamMapping(0).execute(String.valueOf(CampId), EmpCode, "0", teamsDetailsModel.getOutput().get(position).getTeamNumber());

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


        rv_phlebo.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (phleboDetailsModel != null) {
                    Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + phleboDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            new RemoveTeamMapping(1).execute(String.valueOf(CampId), EmpCode, phleboDetailsModel.getOutput().get(position).getUserID(), "0");

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
        rv_doctor.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (doctorDetailsModel != null) {
                    Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + doctorDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            new RemoveTeamMapping(2).execute(String.valueOf(CampId), EmpCode, doctorDetailsModel.getOutput().get(position).getUserID(), "0");

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
        rv_deop.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (dataEntryOperatorDetailsModel != null) {
                    Utilities.showAlertDialog(context, "Confirmation", "Do you really want to remove " + dataEntryOperatorDetailsModel.getOutput().get(position).getMemberName(), true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            new RemoveTeamMapping(3).execute(String.valueOf(CampId), EmpCode, dataEntryOperatorDetailsModel.getOutput().get(position).getUserID(), "0");

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


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team-Camp Mapping Details");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void showCampListDialog(final List<CampListModel.OutputBean> campList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team List");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campList.size(); i++) {
            arrayAdapter.add(String.valueOf(campList.get(i).getCampId()));
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

                //  CampDate = "2022/07/17";
                new GetPhleboDetailsList().execute(CampId, CampDate, "35");
                new GetDeopDetailsList().execute(CampId, CampDate, "64");
                new GetDoctorDetailsList().execute(CampId, CampDate, "34");
                new GetTeamDetailsList().execute(CampId, CampDate,"0");


                //  refreshCalendar();
            }
        });
        builderSingle.show();

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
                            rv_teams.setAdapter(new TeamsDetailsAdapter(teamsDetailsModel.getOutput()));
                        } else {
                            rv_teams.setLayoutManager(new LinearLayoutManager(context));
                            rv_teams.setAdapter(new TeamsDetailsAdapter(teamsDetailsModel.getOutput()));
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                       // Utilities.showAlertDialog(context, "Fail", "Team Data Not Found", false);
                        if (teamsDetailsModel!=null){

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
                            rv_deop.setAdapter(new DataEntryOperatorListDetailsAdapter(dataEntryOperatorDetailsModel.getOutput()));


                            // showCampListDialog(campList);
                        } else {
                            rv_deop.setLayoutManager(new LinearLayoutManager(context));
                            rv_deop.setAdapter(new DataEntryOperatorListDetailsAdapter(dataEntryOperatorDetailsModel.getOutput()));


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
                            rv_phlebo.setAdapter(new PhleboListDetailsAdapter(phleboDetailsModel.getOutput()));
                        } else {
                            rv_phlebo.setLayoutManager(new LinearLayoutManager(context));
                            rv_phlebo.setAdapter(new PhleboListDetailsAdapter(phleboDetailsModel.getOutput()));
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                      //  Utilities.showAlertDialog(context, "Fail", "Phlebo Data Not Found", false);
                        if (phleboDetailsModel!=null){
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
                            rv_doctor.setAdapter(new DoctorMultipleSelectDetailsAdapter(doctorDetailsModel.getOutput()));
                        } else {
                            rv_doctor.setLayoutManager(new LinearLayoutManager(context));
                            rv_doctor.setAdapter(new DoctorMultipleSelectDetailsAdapter(doctorDetailsModel.getOutput()));

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                     //   Utilities.showAlertDialog(context, "Fail", "Doctor List not found", false);
                        if (doctorDetailsModel!=null){
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
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                          //  districtList.add(0, new DistrictList_Model("0", "All"));

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


//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                selectedLabID = lab_List.get(which).getLabCode();
//                selectedLabName = lab_List.get(which).getLabName();
//                tvLab.setText(selectedLabName);
//            }
//        });

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

                        new GetTeamDetailsList().execute(CampId, CampDate, "0");
                    } else if (type == 1) {
                        Utilities.showToastMessage("Resource removed successfully", context, true);

                        new GetPhleboDetailsList().execute(CampId, CampDate, "35");
                    } else if (type == 2) {
                        Utilities.showToastMessage("Resource removed successfully", context, true);

                        new GetDoctorDetailsList().execute(CampId, CampDate, "34");
                    } else if (type == 3) {
                        Utilities.showToastMessage("Resource removed successfully", context, true);

                        new GetDeopDetailsList().execute(CampId, CampDate, "64");
                    }
                } else {
                    Utilities.showToastMessage("Unable to remove, please try again", context, false);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


//    public class GetResouceList extends AsyncTask<String, Void, String> {
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
//            res = WebServiceCall.APICall(ApplicationConstants.GetDesignationsForCampCreation, ApplicationConstants.TeamCampMapping, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (!result.equals("")) {
//
//                    Lab_Pojo pojoDetails = new Gson().fromJson(result, Lab_Pojo.class);
//                    type = pojoDetails.getStatus();
//                    message = pojoDetails.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        ArrayList<Lab_OutPut_Pojo> labList = pojoDetails.getOutput();
//                        if (labList.size() > 0) {
//                            showLabListDialog(labList);
//                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
//    }



}