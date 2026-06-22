package com.myhindlab.abkat.activities.doortodoor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.DoctorListAdapter;
import com.myhindlab.abkat.adapters.WorkerDependentAdapter;
import com.myhindlab.abkat.models.CallTypeModel;
import com.myhindlab.abkat.models.DoctorMappingModel;
import com.myhindlab.abkat.models.ScreeningTestModel;
import com.myhindlab.abkat.models.UserAttendanceForPhysicalExamModel;
import com.myhindlab.abkat.models.WorkerDependentModel;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpectedBeneficiaryRemarkActivity extends AppCompatActivity {

    private EditText tvDoctor,tvRemarkDescription;
    private TextView tv_call_type,tv_username,tvRemark;
    private Context context;
    private ProgressDialog pd;

    private List<ScreeningTestModel.OutputBean> screeningTestList;


    private String DESGID, EmpCode, LabCode,ReasonId, CampDATE, DISTLGDCODE, district, TALLGDCODE, taluka, STATELGDCODE = "2", selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, UserId, AssignedId = "", IsTeam;

    private UserSessionManager session;
    private String callTypeId = "1";
    private ArrayList<WorkerDependentModel.Output> workerDependentModelArrayList;
    private ApiInterface apiInterface;
    private com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem patientDetails;
    private ProgressDialog progressDialog;
    private RecyclerView rv_worker;
    private int doctorId = 0;
    private Button btnAssign;
    private String campId = "", teamId = "";
    private LinearLayout mainll_Remark;


    private ArrayList<DoctorMappingModel.Output> doctorList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expected_beneficiary_remark);

        initView();
        getSessionData();
        setEventHandlers();
        setUpToolbar();
        setDefault();
    }

    private void initView() {
        context = ExpectedBeneficiaryRemarkActivity.this;
        session = new UserSessionManager(context);
        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(context);
        workerDependentModelArrayList = new ArrayList<>();
        pd = new ProgressDialog(context);
        tv_call_type = findViewById(R.id.tv_call_type);
        tv_username = findViewById(R.id.tv_username);
        btnAssign = findViewById(R.id.btnAssign);
        tvRemark = findViewById(R.id.tvRemark);
        tvRemarkDescription = findViewById(R.id.tvRemarkDescription);
        mainll_Remark = findViewById(R.id.mainll_Remark);

//        rv_worker.setHasFixedSize(true);
//        rv_worker.setLayoutManager(new LinearLayoutManager(context));

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
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    void setDefault() {
        patientDetails = (com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");

        AssignedId = String.valueOf(patientDetails.getAssignCallID());
        tv_username.setText(patientDetails.getBeneficiaryName());

        if (!patientDetails.getD2DCallingRemarkID().equalsIgnoreCase("0")){
            tvRemark.setEnabled(false);
            tvRemarkDescription.setEnabled(false);
            btnAssign.setVisibility(View.GONE);
            tvRemarkDescription.setText(patientDetails.getD2DCallingRemark());
           if (patientDetails.getD2DCallingRemarkID().equalsIgnoreCase("1")){
               mainll_Remark.setVisibility(View.GONE);
                tvRemark.setText("D2D Team Remark");

            }else {
               if (patientDetails.getD2DCallingRemarkID().equalsIgnoreCase("2")){
                   tvRemark.setText("Other");
                   mainll_Remark.setVisibility(View.VISIBLE);

               }

           }
        }

//        WorkerDependentModel workerDependentModel = new WorkerDependentModel();
//        WorkerDependentModel.Output output = workerDependentModel.new Output(Integer.valueOf(campId), "", patientDetails.getRegdId(), patientDetails.getEnglishName(), patientDetails.getRegdId(), "W", String.valueOf(patientDetails.getRegdNo()), 0, 0);
//        workerDependentModelArrayList.add(output);

      //  getDependents();
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Remark");

        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

//        btn_save_accordian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.list_info);
//                ;
//                dialog.show();
//            }
//
//        });
    }

    private void setEventHandlers() {


        tvRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetReason().execute();
            }
        });

        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (tvRemark.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select remark", context, false);
                    return;
                }
                if (mainll_Remark.getVisibility() == View.VISIBLE){
                    if (tvRemarkDescription.getText().toString().isEmpty()) {
                        Utilities.showToastMessage("Please enter remark", context, false);
                        return;
                    }

                }
                new InsertRemark().execute(AssignedId,ReasonId,tvRemarkDescription.getText().toString().trim(),EmpCode);
            }
        });


//        tv_call_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<CallTypeModel> callTypeTypeModelArrayList = new ArrayList<>();
//                callTypeTypeModelArrayList.add(new CallTypeModel("Audio", 1));
//                callTypeTypeModelArrayList.add(new CallTypeModel("Video", 2));
////                    assignTypeModelArrayList.add(new AssignTypeModel("Phlebotomist", 35));
////                    assignTypeModelArrayList.add(new AssignTypeModel("Data Entry Operator", 64));
////                    assignTypeModelArrayList.add(new AssignTypeModel("Doctor", 34));
//
//                showAssignType(callTypeTypeModelArrayList);
//
//            }
//        });


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
            param.add(new ParamsPojo("campid", campId));
            param.add(new ParamsPojo("TeamId", teamId));
            res = WebServiceCall.APICall(ApplicationConstants.GetD2DCampMappedDoctorList, ApplicationConstants.webservice_d2d, param);

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
                    DoctorMappingModel pojoDetails = new Gson().fromJson(result, DoctorMappingModel.class);
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


    private void showDoctorListDialog(final ArrayList<DoctorMappingModel.Output> doctorList) {


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Doctor");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);


        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        DoctorListAdapter doctorListAdapter = new DoctorListAdapter(doctorList);
        rvList.setAdapter(doctorListAdapter);

        ArrayList<DoctorMappingModel.Output> filteredList = new ArrayList<>();

        //        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        //        for (Lab_OutPut_Pojo subTrenchModel : lab_List) {
        //            arrayAdapter.add(subTrenchModel.getLabName());
        //        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

//            @Override
//            public void onTextChanged(CharSequence query, int start, int before, int count) {
//
//                if (filteredList != null)
//                    filteredList.clear();
//
//                if (doctorList != null) {
//                    if (edt_search.getText().toString().equals("")) {
//                        filteredList.addAll(doctorList);
//                        rvList.setAdapter(new DoctorListAdapter(filteredList));
//
//                    } else {
//                        if (doctorList.size() > 0) {
//                            for (DoctorMappingModel.Output pojo : doctorList) {
//                                String siteDetails = pojo.getFullname();
//                                if (siteDetails != null && siteDetails.toLowerCase().contains(edt_search.getText().toString().toLowerCase())) {
//                                    filteredList.add(pojo);
//                                }
//                            }
//
//                            if (filteredList.size() == 0) {
//                                filteredList.addAll(doctorList);
//                                rvList.setAdapter(new DoctorListAdapter(filteredList));
//                            } else {
//                                rvList.setAdapter(new DoctorListAdapter(filteredList));
//
//                            }
//                        }
//                    }
//                }
//            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new DoctorListAdapter(doctorList));
                    return;
                }

                if (doctorList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<DoctorMappingModel.Output> searchedTestList = new ArrayList<>();
                    for (DoctorMappingModel.Output clientDetails : doctorList) {

                        String countryToBeSearched = clientDetails.getFullname().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new DoctorListAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new DoctorListAdapter(doctorList));
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
//                if (filteredList.size() > 0) {
                tvDoctor.setText(doctorList.get(position).getFullname());

                doctorId = doctorList.get(position).getUserid();
//                }
                alertDialog.dismiss();

            }
        }));

        alertDialog.show();

    }


    private class GetTeamId extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("campid", campId));
            param.add(new ParamsPojo("UserID", String.valueOf(session.getUserDetailsJson().getEmpCode())));
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        teamId = jsonObject1.getString("TeamNumber");
                        new GetDoctorList().execute();
                    } else {
                        Utilities.showAlertDialog(context, "Error", "Unable to get team id", false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private class GetReason extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetD2DCallingRemark, ApplicationConstants.webservice_forcallist, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    ScreeningTestModel pojoDetails = new Gson().fromJson(result, ScreeningTestModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        screeningTestList = pojoDetails.getOutput();
                        if (screeningTestList.size() > 0) {
                            showReasonDialog(screeningTestList);
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

    private void showReasonDialog(final List<ScreeningTestModel.OutputBean> landinglablist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Remark");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < landinglablist.size(); i++) {
            arrayAdapter.add(String.valueOf(landinglablist.get(i).getD2DCallingRemark()));
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
                tvRemark.setText(landinglablist.get(which).getD2DCallingRemark());
                ReasonId = String.valueOf(landinglablist.get(which).getD2DCRemarkID());



//
                if (ReasonId.equalsIgnoreCase("2")){
                    mainll_Remark.setVisibility(View.VISIBLE);
                }else {
                    mainll_Remark.setVisibility(View.GONE);
                }
            }
        });
        builderSingle.show();

    }

    private class InsertRemark extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("AssignCallID", params[0]));
            param.add(new ParamsPojo("D2DCallingRemarkID", params[1]));
            param.add(new ParamsPojo("Remark", params[2]));
            param.add(new ParamsPojo("CReatedBy", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertD2DCallingRemark, ApplicationConstants.webservice_forcallist, param);
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







}