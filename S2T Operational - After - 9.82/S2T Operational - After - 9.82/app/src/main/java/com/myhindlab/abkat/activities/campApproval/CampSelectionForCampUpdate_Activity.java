package com.myhindlab.abkat.activities.campApproval;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.CampSelectionForInstantPatientRegistration_Activity;
import com.myhindlab.abkat.activities.campApproval.models.GetCampAssignUserListModel;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.adapters.CampListAdapter;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
import com.myhindlab.abkat.models.UserAttendanceModel;
import com.myhindlab.abkat.pojos.GetApprovedCampListDetailsForApp_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampSelectionForCampUpdate_Activity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private MaterialEditText edt_campdate;
    private LinearLayout ll_campdetailslist;
    private String userId, userName, campDate = "", distLgdCode = "0",subOrgId = "0", designName, designId, DesgLevelId, selTaluka, selGp, labCode, SiteDetailId = "", campId, districtName;

    private SearchView searchview_campname;
    private RecyclerView recyclerview_campList;
    private ArrayList<GetApprovedCampListDetailsForAppList> campList;
    private ArrayList<GetApprovedCampListDetailsForAppList> searchCampList;
    private String userID = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campselection_forhealthscreeing);

        init();
        setUpToolbar();
        setDefaults();
        getABKATSessionDetails();
        setEventHandler();


    }

    private void init() {
        edt_campdate = findViewById(R.id.edt_campdate);
        ll_campdetailslist = findViewById(R.id.ll_campdetailslist);
        searchview_campname = findViewById(R.id.searchview_campname);
        recyclerview_campList = findViewById(R.id.recyclerview_campList);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Camp");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getABKATSessionDetails() {
        UserSessionManager session = new UserSessionManager(context);
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
                designId = json.getString("DESGID");
                labCode = json.getString("LabCode");
                subOrgId = json.getString("SubOrgId");
                labCode = "0";
//                distLgdCode = "472";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        context = CampSelectionForCampUpdate_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context, R.style.CustomDialogTheme);

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        edt_campdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));

        ll_campdetailslist.setVisibility(View.GONE);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                subOrgId = json.getString("SubOrgId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        edt_campdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_campdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                if (Utilities.isNetworkAvailable(context)) {
//                                    new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID,"0");

                                    getApprovedCampListDetails(
                                            edt_campdate.getText().toString().trim(),subOrgId,"0","0",userID,designId
                                    );



                                } else {
                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                }
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

        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCampList = new ArrayList<>();

                if (campList != null) {
                    if (campList.size() > 0) {
                        for (GetApprovedCampListDetailsForAppList pojo : campList) {
                            String siteDetails = pojo.getCampId();
                            if (siteDetails != null && siteDetails.toLowerCase().contains(query.toLowerCase())) {
                                searchCampList.add(pojo);
                            }
                        }

                        if (searchCampList.size() == 0) {
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);
                            searchCampList.addAll(campList);
                            showSiteListDialog(searchCampList);
                        } else {
                            showSiteListDialog(searchCampList);
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCampList = new ArrayList<>();

                if (campList != null) {
                    if (newText.equals("")) {
                        searchCampList.addAll(campList);
                        showSiteListDialog(searchCampList);
                    } else {
                        if (campList.size() > 0) {
                            for (GetApprovedCampListDetailsForAppList pojo : campList) {
                                String siteDetails = pojo.getCampId();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(newText.toLowerCase())) {
                                    searchCampList.add(pojo);
                                }
                            }

                            if (searchCampList.size() == 0) {
                                searchCampList.addAll(campList);
                                showSiteListDialog(searchCampList);
                            } else {
                                showSiteListDialog(searchCampList);
                            }
                        }
                    }
                }
                return true;
            }
        });

        recyclerview_campList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                ConstantData constantData = ConstantData.getInstance();


                                if (session.getUserDetailsJson().getDesgid() == 29 || session.getUserDetailsJson().getDesgid() == 103 || session.getUserDetailsJson().getDesgid() == 84 || session.getUserDetailsJson().getDesgid() == 100 || session.getUserDetailsJson().getDesgid() == 34 || session.getUserDetailsJson().getDesgid() == 104 || session.getUserDetailsJson().getDesgid() == 107 || session.getUserDetailsJson().getDesgid() == 113|| session.getUserDetailsJson().getDesgid() == 92|| session.getUserDetailsJson().getDesgid() == 162) { //Allow for camp coordinator and
                                    GetApprovedCampListDetailsForAppList campDetails = searchCampList.get(position);


                                    if (!userId.equalsIgnoreCase(campDetails.getCreatedBy())){

                                        Utilities.showAlertDialog(context,"Alert","This camp not created by you,please select created camp",false);
                                        return;

                                    }



                                        if (campDetails.getIsRegdDone().equalsIgnoreCase("1")){

                                            Utilities.showAlertDialog(context,"Alert","या कॅम्पमध्ये काही patient registration झालेले असल्यामुळे नवीन phlebotomist/doctor जोडण्यास किंवा हटवण्यास परवानगी नाही.",false);
                                            return;

                                        }





                                    new GetUserCampMappingAndAttendanceStatus(campDetails, constantData).execute(edt_campdate.getText().toString(), userId, String.valueOf(campList.get(position).getDISTLGDCODE()), campList.get(position).getCampType(), String.valueOf(campList.get(position).getCampId()),"1");


//                                    startActivity(new Intent(context, CampUpdateActivity.class)
//                                            .putExtra("campId", campDetails.getCampId())
//                                            .putExtra("campDate", edt_campdate.getText().toString().trim())
//                                            .putExtra("campDistLgdCode", campDetails.getDISTLGDCODE())
//                                            .putExtra("campType", campDetails.getISCampType())
//                                            .putExtra("labCode",campDetails.getLABCODE()));
                                } else {
                                    new GetCampAssignUserList(position).execute(searchCampList.get(position).getCampId(), String.valueOf(session.getUserDetailsJson().getEmpCode()), "0");
                                }
                            }
                        }));
    }

    private class GetApprovedCampListDetailsForAppAPICall extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("LabCode", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetApprovedCampListDetails_RegularCamp, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetApprovedCampListDetails_RegularCamp, ApplicationConstants.webservice_d2d, param);

          //  res = WebSe   rviceCall.APICall(ApplicationConstants.GetApprovedCampListDetails_RegularCamp, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Camp List", result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    GetApprovedCampListDetailsForApp_Pojo pojoDetails = new Gson().fromJson(result, GetApprovedCampListDetailsForApp_Pojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {

                        searchview_campname.clearFocus();

                        campList = new ArrayList<>();
                        campList = pojoDetails.getOutput();

                        searchCampList = new ArrayList<>();
                        searchCampList.addAll(campList);

                        if (searchCampList.size() > 0) {
                            showSiteListDialog(searchCampList);
                        } else {
                            ll_campdetailslist.setVisibility(View.GONE);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        ll_campdetailslist.setVisibility(View.GONE);
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    ll_campdetailslist.setVisibility(View.GONE);
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ll_campdetailslist.setVisibility(View.GONE);
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
            }
        }
    }


    private void getApprovedCampListDetails(String campDate, String subOrgId, String division, String distCode, String userId, String desgId) {
        final ProgressDialog progressDialog = new ProgressDialog(CampSelectionForCampUpdate_Activity.this); // Change to actual activity name
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);

        Call<GetApprovedCampListDetailsForApp_Pojo> call = apiService.getApprovedCampListDetails(
                campDate, subOrgId, division, distCode, userId, desgId
        );

        call.enqueue(new Callback<GetApprovedCampListDetailsForApp_Pojo>() {
            @Override
            public void onResponse(Call<GetApprovedCampListDetailsForApp_Pojo> call, Response<GetApprovedCampListDetailsForApp_Pojo> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    GetApprovedCampListDetailsForApp_Pojo pojoDetails = response.body();
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        searchview_campname.clearFocus();

                        campList = new ArrayList<>(pojoDetails.getOutput());
                        searchCampList = new ArrayList<>(campList);

                        if (searchCampList.size() > 0) {
                            showSiteListDialog(searchCampList);
                        } else {
                            ll_campdetailslist.setVisibility(View.GONE);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        ll_campdetailslist.setVisibility(View.GONE);
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    ll_campdetailslist.setVisibility(View.GONE);
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            }

            @Override
            public void onFailure(Call<GetApprovedCampListDetailsForApp_Pojo> call, Throwable t) {
                progressDialog.dismiss();
                ll_campdetailslist.setVisibility(View.GONE);
                Utilities.showAlertDialog(context, "Alert", t.getLocalizedMessage(), false);
            }
        });
    }




    private class GetUserCampMappingAndAttendanceStatus extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;
        private GetApprovedCampListDetailsForAppList campDetails;
        private ConstantData constantData;

        public GetUserCampMappingAndAttendanceStatus(GetApprovedCampListDetailsForAppList campDetails, ConstantData constantData) {
            this.campDetails = campDetails;
            this.constantData = constantData;
        }

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
            param.add(new ParamsPojo("CampDATE", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            param.add(new ParamsPojo("CampID", params[4]));
            param.add(new ParamsPojo("TestId", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.GetUserCampMappingAndAttendanceStatusForRegularCamp, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", "GetUserAttendanceStatus: " + result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    UserAttendanceModel userAttendanceModel = new Gson().fromJson(result, UserAttendanceModel.class);

                    if (userAttendanceModel.getStatus().equalsIgnoreCase("success")) {

                        if (userAttendanceModel.getOutput().get(0).getIsCampClosed() == 1){
                            Utilities.showAlertDialog(context, "Alert", "This camp is closed", false);
                            return;
                        } else {

                            startActivity(new Intent(context, CampUpdateActivity.class)
                                    .putExtra("campId", campDetails.getCampId())
                                    .putExtra("campDate", edt_campdate.getText().toString().trim())
                                    .putExtra("campDistLgdCode", campDetails.getDISTLGDCODE())
                                    .putExtra("campType", campDetails.getISCampType())
                                    .putExtra("labCode",campDetails.getLABCODE()));
                        }
//                        JSONArray jsonArray = jsonObject.getJSONArray("output");
//
//                        if (type.equalsIgnoreCase("1")) {
//                            rbOnline.setChecked(true);
//                            Utilities.showAlertDialog(context, "Online", "You are online now", true);
//
//                        } else {
//                            rbOffline.setChecked(true);
//                            Utilities.showAlertDialog(context, "Offline", "You are offline now", true);
//
//                        }

//                        new GetD2dDoctors().execute(userId);

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

    private class GetCampAssignUserList extends AsyncTask<String, Void, String> {

        private int position;

        public GetCampAssignUserList(int position) {
            this.position = position;
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
            param.add(new ParamsPojo("campid", params[0]));
            param.add(new ParamsPojo("ResourceUserId", params[1]));
            param.add(new ParamsPojo("TestID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampAssignUserList, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Camp List", result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    GetCampAssignUserListModel getCampAssignUserListModel = new Gson().fromJson(result, GetCampAssignUserListModel.class);
                    String status = getCampAssignUserListModel.getStatus();
                    String message = getCampAssignUserListModel.getMessage();
                    if (status.equalsIgnoreCase("success")) {

                        boolean allow = false;
                        if (getCampAssignUserListModel.getOutput().size() > 0) {
                            for (GetCampAssignUserListModel.Output o :
                                    getCampAssignUserListModel.getOutput()) {
                                if (Objects.equals(o.getResourceUserId(), session.getUserDetailsJson().getEmpCode())) {
                                    if (o.getStatusRes() == 1) {
                                        allow = true;
                                    }
                                }
                            }

                            if (allow) {
                                GetApprovedCampListDetailsForAppList campDetails = searchCampList.get(position);
                                startActivity(new Intent(context, CampUpdateActivity.class)
                                        .putExtra("campId", campDetails.getCampId())
                                        .putExtra("campDate", edt_campdate.getText().toString().trim())
                                        .putExtra("campDistLgdCode", campDetails.getDISTLGDCODE())
                                        .putExtra("campType", campDetails.getISCampType()));
                            } else {
                                Utilities.showAlertDialog(context, "Alert", "You are not authorise to Conduct this camp", false);
                            }
                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                            Utilities.showAlertDialog(context, "Alert", "You are not authorise to Conduct this camp", false);

                        }
                    } else {
//                        Utilities.showAlertDialog(context, status, message, false);
                        Utilities.showAlertDialog(context, "Alert", "You are not authorise to Conduct this camp", false);

                    }
                } else {
//                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                    Utilities.showAlertDialog(context, "Alert", "You are not authorise to Conduct this camp", false);

                }
            } catch (Exception e) {
                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
                Utilities.showAlertDialog(context, "Alert", "You are not authorise to Conduct this camp", false);

            }
        }
    }





    private void showSiteListDialog(ArrayList<GetApprovedCampListDetailsForAppList> camplist) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerview_campList.setLayoutManager(layoutManager);
        ll_campdetailslist.setVisibility(View.VISIBLE);
        recyclerview_campList.setAdapter(new CampListAdapter(camplist));
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchview_campname.clearFocus();

        if (Utilities.isNetworkAvailable(context)) {
//            new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID,"0");


            getApprovedCampListDetails(
                    edt_campdate.getText().toString().trim(),subOrgId,"0","0",userID,designId
            );

        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }
}
