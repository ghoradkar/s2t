package com.myhindlab.abkat.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampPatientStatusAdapter;
import com.myhindlab.abkat.models.PatientStatusModel;
import com.myhindlab.abkat.models.doortodoor.GetCampIDWiseTeamDetailsResponseModel;
import com.myhindlab.abkat.pojos.PatientStatusPojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampPatientStatusList_Fragment extends Fragment {
    private static Context context;
    private static UserSessionManager session;
    private static LinearLayout ll_nothingtoshow, ll_lables;
    private static RecyclerView rv_patientlist;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private int campType = 0, campId = 0;


    private ProgressDialog pd;

    private ApiInterface apiInterface;



    private static String userID = "", teamId = "0", DESGID;


    private static TextInputEditText edtSelTeam;

    private static TextView tv_message;
    private static ArrayList<PatientStatusModel> patientList;
    private static String DISTLGDCODE;
    private static SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camp_patienttest, container, false);
        context = getActivity();
        init(rootView);
        getSessionData();
        setDefault();
        setEventHandlers();
        return rootView;
    }

    private void init(View rootView) {
        session = new UserSessionManager(context);
        ll_nothingtoshow = rootView.findViewById(R.id.ll_nothingtoshow);
        rv_patientlist = rootView.findViewById(R.id.rv_patientlist);
        tv_message = rootView.findViewById(R.id.tv_message);
        ll_lables = rootView.findViewById(R.id.ll_labels);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        rv_patientlist.setLayoutManager(new LinearLayoutManager(context));
        patientList = new ArrayList<>();
        searchView = rootView.findViewById(R.id.searchView);
        edtSelTeam = rootView.findViewById(R.id.edtSelTeam);

        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);


        Bundle bundle = this.getArguments();
        assert bundle != null;
        DISTLGDCODE = bundle.getString("DISTLGDCODE");
        if (DISTLGDCODE == null || DISTLGDCODE.equals("")) {
            DISTLGDCODE = "0";
        }


        campId = Integer.parseInt(ConstantData.getInstance().getCalendarCampId());

        if ((campType == 3 || campType == 4) || session.isHllUser()) {
            //   edtSelTeam.setVisibility(View.VISIBLE);

        } else {
            // edtSelTeam.setVisibility(View.GONE);
            //   cvTeamDetails.setVisibility(View.GONE);

        }

    }

    private void getSessionData() {
        UserSessionManager session = new UserSessionManager(context);
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                DESGID = json.getString("DESGID");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDefault() {

        if (session.isHllUser()) {

            if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29")|| (DESGID.equalsIgnoreCase("160")
                    || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162")
                    || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77")
                    || DESGID.equalsIgnoreCase("128")||DESGID.equalsIgnoreCase("30")
                    ||DESGID.equalsIgnoreCase("108")||DESGID.equalsIgnoreCase("84")
                    ||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("136")))))) {
                edtSelTeam.setVisibility(View.VISIBLE);

            } else {
                edtSelTeam.setVisibility(View.GONE);

            }
        }


        if (session.isHllUser()) {
            if (Utilities.isNetworkAvailable(context)) {
                if (DESGID.equalsIgnoreCase("35") || (DESGID.equalsIgnoreCase("64")
                        || (DESGID.equalsIgnoreCase("86")||(DESGID.equalsIgnoreCase("146")
                        ||(DESGID.equalsIgnoreCase("129")||(DESGID.equalsIgnoreCase("138")
                        ||(DESGID.equalsIgnoreCase("137")||(DESGID.equalsIgnoreCase("169")
                        ||DESGID.equalsIgnoreCase("31")||DESGID.equalsIgnoreCase("176")
                        ||(DESGID.equalsIgnoreCase("177")))))))))) {
                    new GetTeamId().execute();

                } else {

                    new CAMPPatientCheckupAnalysis_Report().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE,teamId);

                }
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        } else {
            if (Utilities.isNetworkAvailable(context)) {
                new CAMPPatientCheckupAnalysis_Report().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE,"0");
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        }


//        if (Utilities.isNetworkAvailable(context)) {
//            new CAMPPatientCheckupAnalysis_Report().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE);
//        } else {
//            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//        }
    }

    private void setEventHandlers() {

        edtSelTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.isNetworkAvailable(context)) {
                    getCampWiseTeam();
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }

            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (patientList.size() > 0) {
                    if (!query.equals("")) {
                        ArrayList<PatientStatusModel> searchedPatientList = new ArrayList<>();
                        for (PatientStatusModel distDetails : patientList) {
                            String patientToBeSearched = distDetails.getPatientName().toLowerCase() + distDetails.getRegdNo().toLowerCase();
                            if (patientToBeSearched.contains(query.toLowerCase())) {
                                searchedPatientList.add(distDetails);
                            }
                        }
                        rv_patientlist.setAdapter(new CampPatientStatusAdapter(context, searchedPatientList));
                    } else {
                        rv_patientlist.setAdapter(new CampPatientStatusAdapter(context, patientList));
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (patientList.size() > 0) {
                    if (!newText.equals("")) {
                        ArrayList<PatientStatusModel> searchedPatientList = new ArrayList<>();
                        for (PatientStatusModel distDetails : patientList) {
                            String patientToBeSearched = distDetails.getPatientName().toLowerCase() + distDetails.getRegdNo().toLowerCase();
                            if (patientToBeSearched.contains(newText.toLowerCase())) {
                                searchedPatientList.add(distDetails);
                            }
                        }
                        rv_patientlist.setAdapter(new CampPatientStatusAdapter(context, searchedPatientList));
                    } else {
                        rv_patientlist.setAdapter(new CampPatientStatusAdapter(context, patientList));
                    }
                }
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (session.isHllUser()) {
                    if (Utilities.isNetworkAvailable(context)) {
                        if (DESGID.equalsIgnoreCase("35") || (DESGID.equalsIgnoreCase("64")
                                || (DESGID.equalsIgnoreCase("86")||(DESGID.equalsIgnoreCase("146")
                                ||(DESGID.equalsIgnoreCase("129")||(DESGID.equalsIgnoreCase("138")
                                ||(DESGID.equalsIgnoreCase("137")||(DESGID.equalsIgnoreCase("169")
                                ||DESGID.equalsIgnoreCase("31")||DESGID.equalsIgnoreCase("176")
                                ||(DESGID.equalsIgnoreCase("177")))))))))) {
                            new GetTeamId().execute();

                        } else  {

                            new CAMPPatientCheckupAnalysis_Report().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE,teamId);

                        }
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }

                } else {
                    if (Utilities.isNetworkAvailable(context)) {
                        new CAMPPatientCheckupAnalysis_Report().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE,"0");
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                }



//                if (Utilities.isNetworkAvailable(context)) {
//                    new CAMPPatientCheckupAnalysis_Report().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE);
//                } else {
//                    swipeRefreshLayout.setRefreshing(false);
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                }
            }
        });
    }

    private static class CAMPPatientCheckupAnalysis_Report extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(false);
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("TeamId", params[2]));
          //  res = WebServiceCall.APICall(ApplicationConstants.CAMPPatientCheckupAnalysis_Report, ApplicationConstants.webservice, param);
            res = WebServiceCall.APICall(ApplicationConstants.CAMPPatientCheckupAnalysis_Report_New, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            try {
                if (!result.equals("")) {
                    PatientStatusPojo pojoDetails = new Gson().fromJson(result, PatientStatusPojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        patientList = pojoDetails.getOutput();
                        if (patientList.size() > 0) {
                            rv_patientlist.setVisibility(View.VISIBLE);
                            ll_lables.setVisibility(View.VISIBLE);
                            ll_nothingtoshow.setVisibility(View.GONE);
                            rv_patientlist.setAdapter(new CampPatientStatusAdapter(context, patientList));
                        } else {
                            rv_patientlist.setVisibility(View.GONE);
                            ll_lables.setVisibility(View.GONE);
                            ll_nothingtoshow.setVisibility(View.VISIBLE);
                            tv_message.setText("Beneficiary not mapped with this camp");
                        }
                    } else {
                        rv_patientlist.setVisibility(View.GONE);
                        ll_lables.setVisibility(View.GONE);
                        ll_nothingtoshow.setVisibility(View.VISIBLE);
                        tv_message.setText("Beneficiary not mapped with this camp");
                    }
                } else {
                    rv_patientlist.setVisibility(View.GONE);
                    ll_lables.setVisibility(View.GONE);
                    ll_nothingtoshow.setVisibility(View.VISIBLE);
                    tv_message.setText("Beneficiary not mapped with this camp");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            searchView.clearFocus();
        }
    }


    void getCampWiseTeam() {
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();

        apiInterface.getCampIDWiseTeamDetails(campId).enqueue(new Callback<GetCampIDWiseTeamDetailsResponseModel>() {
            @Override
            public void onResponse(Call<GetCampIDWiseTeamDetailsResponseModel> call, Response<GetCampIDWiseTeamDetailsResponseModel> response) {
                pd.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        ArrayList<GetCampIDWiseTeamDetailsResponseModel.Output> teamList = new ArrayList();
                        GetCampIDWiseTeamDetailsResponseModel campIDWiseTeamDetailsResponseModel = new GetCampIDWiseTeamDetailsResponseModel();
                        teamList.add(campIDWiseTeamDetailsResponseModel.new Output("All", "0", campId, 0, "0", "0"));
                        teamList.addAll(response.body().getOutput());
                        showTeamListDialog(teamList);
                    } else {
                        Utilities.showToastMessage(response.body().getMessage(), context, false);

                    }
                }
            }

            @Override
            public void onFailure(Call<GetCampIDWiseTeamDetailsResponseModel> call, Throwable t) {
                pd.dismiss();

                Utilities.showToastMessage(t.getMessage(), context, false);
            }
        });
    }
    private void showTeamListDialog(final ArrayList<GetCampIDWiseTeamDetailsResponseModel.Output> teamList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < teamList.size(); i++) {
            arrayAdapter.add(String.valueOf(teamList.get(i).getTeamNumber()));
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
//                edt_selectdistrict.setText(districtList.get(which).getDISTNAME());
                teamId = String.valueOf(teamList.get(which).getTeamId());

//                if (teamNumber == 0) {
                    //  cvTeamDetails.setVisibility(View.GONE);
//                } else {
                    // cvTeamDetails.setVisibility(View.VISIBLE);
//                }
//                tvTeamMember1Mobile.setText(teamList.get(which).getMember1ContactNo());
//                tvTeamMember2Mobile.setText(teamList.get(which).getMember2ContactNo());
//                tvTeamNumber.setText(teamList.get(which).getTeamNumber());
//                tvTeamMember1Name.setText(teamList.get(which).getMember1());
//                tvTeamMember2Name.setText(teamList.get(which).getMember2());

                edtSelTeam.setText(teamList.get(which).getTeamNumber());
                if (Utilities.isNetworkAvailable(context)) {
                    new CAMPPatientCheckupAnalysis_Report().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE,teamId);
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
        builderSingle.show();
    }


    private static class GetTeamId extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("CampId", ConstantData.getInstance().getCalendarCampId()));
            param.add(new ParamsPojo("UserID", userID));
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);

            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            try {
                if (!result.equals("")) {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        teamId = jsonObject1.getString("TeamNumber");
                        if (Utilities.isNetworkAvailable(context)) {
                            new CAMPPatientCheckupAnalysis_Report().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE,teamId);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }

                    } else {
                        Utilities.showAlertDialog(context, "Error", " Your Selected Camp Not Mapped To You", false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }



}
