package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.PostCampBeneficiaryAdapter;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostCampPatientList_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private TextView tv_block;
    private SearchView searchview_campname;
    private RecyclerView rv_patient_list;
    private LinearLayout ll_buttons;

    private String campId, callType, isAdmin,UserId,EmpCode;

    private LocalBroadcastManager localBroadcastManager;
    private ArrayList<PostCampBeneficiaryListModel.OutputBean> campBeneficiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_camp_patient_list);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = PostCampPatientList_Activity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

        searchview_campname = findViewById(R.id.searchview_campname);
        tv_block = findViewById(R.id.tv_block);
        ll_buttons = findViewById(R.id.ll_buttons);
        rv_patient_list = findViewById(R.id.rv_patient_list);
        rv_patient_list.setLayoutManager(new LinearLayoutManager(context));

        campBeneficiaryList = new ArrayList<>();

        new GetPostCampDetails().execute(
                "0",
                "0",
                "0",
                "3",
                "0",
                campId,
                callType,
                "0"
        );
    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
//                DESGID = json.getString("DESGID");
//                DISTLGDCODE = json.getString("DISTLGDCODE");
//                district = json.getString("district");
//                TALLGDCODE = json.getString("TALLGDCODE");
//                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                UserId = json.getString("UserId");
              //  LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
        callType = getIntent().getStringExtra("callType");
        isAdmin = getIntent().getStringExtra("isAdmin");

//        if (callType.equals("2"))
//            ll_buttons.setVisibility(View.GONE);

//        if (isAdmin.equals("1"))
//            ll_buttons.setVisibility(View.GONE);

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("PostCampPatientList_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        apiCall();
    }

    private void setEventHandler() {
        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<PostCampBeneficiaryListModel.OutputBean> searchPatientList = new ArrayList<>();

                if (campBeneficiaryList != null) {
                    if (campBeneficiaryList.size() > 0) {
                        for (PostCampBeneficiaryListModel.OutputBean pojo : campBeneficiaryList) {
                            String siteDetails = pojo.getBeneficiaryName();
                            if (siteDetails != null && siteDetails.toLowerCase().contains(query.toLowerCase())) {
                                searchPatientList.add(pojo);
                            }
                        }

                        if (searchPatientList.size() == 0) {
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);
                            searchPatientList.addAll(campBeneficiaryList);
                            rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                        } else {
                            rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<PostCampBeneficiaryListModel.OutputBean> searchPatientList = new ArrayList<>();

                if (campBeneficiaryList != null) {
                    if (newText.equals("")) {
                        searchPatientList.addAll(campBeneficiaryList);
                        rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                    } else {
                        if (campBeneficiaryList.size() > 0) {
                            for (PostCampBeneficiaryListModel.OutputBean pojo : campBeneficiaryList) {
                                String siteDetails = pojo.getBeneficiaryName();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(newText.toLowerCase())) {
                                    searchPatientList.add(pojo);
                                }
                            }

                            if (searchPatientList.size() == 0) {
                                searchPatientList.addAll(campBeneficiaryList);
                                rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            } else {
                                rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            }
                        }
                    }
                }
                return true;
            }
        });

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            apiCall();
        }
    };

    private void apiCall() {
        if (Utilities.isNetworkAvailable(context)) {
            new GetPostCampDetails().execute(
                    "0",
                    "0",
                    "0",
                    "3",
                    "0",
                    campId,
                    callType,
                    "0"


            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class GetPostCampDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("Month", params[2]));
            param.add(new ParamsPojo("TYPE", params[3]));
            param.add(new ParamsPojo("IsComplete", params[4]));
            param.add(new ParamsPojo("CAMPID", params[5]));
            param.add(new ParamsPojo("IsReferred", params[6]));
            param.add(new ParamsPojo("RegdID", params[7]));
            param.add(new ParamsPojo("UserID", EmpCode));
            param.add(new ParamsPojo("SearchRegdID", "0"));
           // res = WebServiceCall.APICall(ApplicationConstants.GetPostCampDetails, ApplicationConstants.webservice, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetPostCampDetails_V3, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("TAG", "onPostExecute: "+result);
            try {
                if (!result.equals("")) {

                    PostCampBeneficiaryListModel pojoDetails = new Gson().fromJson(result, PostCampBeneficiaryListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {
                            rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, campBeneficiaryList, isAdmin, callType));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}