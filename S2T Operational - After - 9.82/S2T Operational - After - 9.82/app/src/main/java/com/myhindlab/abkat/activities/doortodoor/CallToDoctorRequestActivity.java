package com.myhindlab.abkat.activities.doortodoor;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallToDoctorRequestActivity extends AppCompatActivity {

    private TextView tvDoctor, tv_call_type;
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private String callTypeId = "1";
    private ArrayList<WorkerDependentModel.Output> workerDependentModelArrayList;
    private ApiInterface apiInterface;
    private UserAttendanceForPhysicalExamModel.Output patientDetails;
    private ProgressDialog progressDialog;
    private RecyclerView rv_worker;
    private int doctorId = 0;
    private Button btnAssign;
    private String campId = "", teamId = "";


    private ArrayList<DoctorMappingModel.Output> doctorList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_doctor_request);


        initView();
        setEventHandlers();
        getSessionData();
        setUpToolbar();
        setDefault();

    }


    private void initView() {
        context = CallToDoctorRequestActivity.this;
        session = new UserSessionManager(context);
        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(context);
        workerDependentModelArrayList = new ArrayList<>();
        pd = new ProgressDialog(context);
        tvDoctor = findViewById(R.id.tvDoctor);
        tv_call_type = findViewById(R.id.tv_call_type);
        btnAssign = findViewById(R.id.btnAssign);
        rv_worker = findViewById(R.id.rv_worker);

        rv_worker.setHasFixedSize(true);
        rv_worker.setLayoutManager(new LinearLayoutManager(context));

    }

    private void getSessionData() {


    }

    void setDefault() {
        patientDetails = (UserAttendanceForPhysicalExamModel.Output) getIntent().getSerializableExtra("patientDetails");
        campId = getIntent().getStringExtra("campId");

//        WorkerDependentModel workerDependentModel = new WorkerDependentModel();
//        WorkerDependentModel.Output output = workerDependentModel.new Output(Integer.valueOf(campId), "", patientDetails.getRegdId(), patientDetails.getEnglishName(), patientDetails.getRegdId(), "W", String.valueOf(patientDetails.getRegdNo()), 0, 0);
//        workerDependentModelArrayList.add(output);

        getDependents();
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Call To Doctor");

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
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.list_info);
                ;
                dialog.show();
            }

        });
    }

    private void setEventHandlers() {

        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//has context menuComposeParagraphdtEntryStatus.Columns.Add("Regdid", typeof(string));
//dtEntryStatus.Columns.Add("DoctorID", typeof(string));
//dtEntryStatus.Columns.Add("CreatedBy", typeof(string));
//dtEntryStatus.Columns.Add("Createdon", typeof(string));
//dtEntryStatus.Columns.Add("CallType", typeof(string));

                if (doctorId == 0) {
                    tvDoctor.setError("Please select doctor");
                    return;
                }
                if (callTypeId.isEmpty()) {
                    tv_call_type.setError("Please select call type");
                    return;
                }
                JsonArray jsonArray = new JsonArray();

                for (WorkerDependentModel.Output o :
                        workerDependentModelArrayList) {
                    if (o.isChecked()) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("Regdid", String.valueOf(o.getRegdId()));
                        jsonObject.addProperty("DoctorID", String.valueOf(doctorId));
                        jsonObject.addProperty("CreatedBy", String.valueOf(session.getUserDetailsJson().getEmpCode()));
//                      jsonObject.addProperty("Createdon", Utilities.dfDate.format(new Date()));
                        jsonObject.addProperty("CallType", String.valueOf(callTypeId));
                        jsonObject.addProperty("RoomID", "");
                        jsonArray.add(jsonObject);
                    }
                }


                if (jsonArray.size() > 0) {
                    progressDialog.setMessage("Creating call request..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Log.d("TAG", "onClick: " + jsonArray.toString());
                    apiInterface.insertBeneficiaryDoctorMapping(jsonArray.toString()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    String status = jsonObject.getString("status");
                                    String msg = jsonObject.getString("message");
                                    if (status.equalsIgnoreCase("success")) {
                                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("D2DAttendanceMarkedPatients_Activity"));

                                        Utilities.showAlertDialog(context, status, "Call Request Submitted Successfully", true, "Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                    } else {
                                        Utilities.showAlertDialog(context, status, "Unable To Submit Call Request", false, "Try Again!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });

                                    }
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();

                            Utilities.showToastMessage(t.getMessage(), context, false);
                        }
                    });

                } else {
                    Utilities.showToastMessage("Please select worker", context, false);
                }


            }
        });

        tvDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetTeamId().execute();
            }
        });

        tv_call_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CallTypeModel> callTypeTypeModelArrayList = new ArrayList<>();
                callTypeTypeModelArrayList.add(new CallTypeModel("Audio", 1));
                callTypeTypeModelArrayList.add(new CallTypeModel("Video", 2));
//                    assignTypeModelArrayList.add(new AssignTypeModel("Phlebotomist", 35));
//                    assignTypeModelArrayList.add(new AssignTypeModel("Data Entry Operator", 64));
//                    assignTypeModelArrayList.add(new AssignTypeModel("Doctor", 34));

                showAssignType(callTypeTypeModelArrayList);

            }
        });

    }


    private void showAssignType(final ArrayList<CallTypeModel> callTypeModelArrayList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Choose Call Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < callTypeModelArrayList.size(); i++) {
            arrayAdapter.add(String.valueOf(callTypeModelArrayList.get(i).getTypeName()));
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
                tv_call_type.setText(callTypeModelArrayList.get(which).getTypeName());
                callTypeId = String.valueOf(callTypeModelArrayList.get(which).getTypeId());
                //  refreshCalendar();

            }
        });
        builderSingle.show();
    }

    void getDependents() {
        progressDialog.setMessage("Getting Dependent..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiInterface.getBeneficiaryListByRegID(patientDetails.getRegdId()).enqueue(new Callback<WorkerDependentModel>() {
            @Override
            public void onResponse(Call<WorkerDependentModel> call, Response<WorkerDependentModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        workerDependentModelArrayList.addAll(response.body().getOutput());
                        rv_worker.setAdapter(new WorkerDependentAdapter(workerDependentModelArrayList));

                    } else {
                        Utilities.showToastMessage(response.body().getMessage(), context, false);
                    }
                } else {
                    Utilities.showToastMessage("" + response.message(), context, false);

                }

            }

            @Override
            public void onFailure(Call<WorkerDependentModel> call, Throwable t) {
                progressDialog.dismiss();
                Utilities.showToastMessage(t.getMessage(), context, false);
            }
        });
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


        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
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

}