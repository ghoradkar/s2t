package com.myhindlab.abkat.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.ScrenningTestModel;
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails;
import com.myhindlab.abkat.models.doortodoor.GetCampIDWiseTeamDetailsResponseModel;
import com.myhindlab.abkat.pojos.ScreeningTestPojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.NetworkProgressDialog;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.myhindlab.abkat.utilities.network_monitor.ProgressListener;
import com.myhindlab.abkat.utilities.network_monitor.TrafficSpeedMonitor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampScreeningTestList_Fragment extends Fragment {
    private Context context;
    private UserSessionManager session;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvTeamNumber, tv_approveDetails, tv_rejectDetails,tv_reverification_bene, tvTeamMember1Mobile, tvTeamMember2Name, tvTeamMember1Name, tvTeamMember2Mobile, tv_facilitatedWorkers, tv_basicDetails, tv_physicalExam, tv_lungTest, tv_audioTest, tv_visionTest, tv_barcode, tvCampCoordinatorName, tvCampCoordinatorMobile, tvCampId, tvDistrict,
            tv_ppSample, tv_acknowlege, tv_urineSampleCollection, tv_totalTest, tv_breast_screening, tv_antigen, tv_rtpcr;
    private String Date, DISTLGDCODE;
    private static String userID = "", teamId = "0", DESGID;

    private int teamNumber = 0;
    private static ArrayList<ScrenningTestModel> screeningTestList;
    private CampCalendarModel.OutputBean campDetails;
    private D2DCampDetails.Output d2dCampDetails;
    private LinearLayoutCompat llCall;
    private TextInputEditText edtSelTeam;
    private ApiInterface apiInterface;
    private ProgressDialog pd;
    private CardView cvTeamDetails;

    private int campType = 0, campId = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camp_screeningtest, container, false);
        context = getActivity();
        init(rootView);
        getSessionData();
        setDefault();
        setEventHandlers();
        return rootView;
    }

    private void init(View rootView) {
        session = new UserSessionManager(context);
        tv_facilitatedWorkers = rootView.findViewById(R.id.tv_facilitatedWorkers);
        tv_basicDetails = rootView.findViewById(R.id.tv_basicDetails);
        tv_physicalExam = rootView.findViewById(R.id.tv_physicalExam);
        tv_lungTest = rootView.findViewById(R.id.tv_lungTest);
        tv_audioTest = rootView.findViewById(R.id.tv_audioTest);
        tv_visionTest = rootView.findViewById(R.id.tv_visionTest);
        tv_barcode = rootView.findViewById(R.id.tv_barcode);
        tv_ppSample = rootView.findViewById(R.id.tv_ppSample);
        tv_acknowlege = rootView.findViewById(R.id.tv_acknowlege);
        tv_urineSampleCollection = rootView.findViewById(R.id.tv_urineSampleCollection);
        tv_totalTest = rootView.findViewById(R.id.tv_totalTest);
        tv_breast_screening = rootView.findViewById(R.id.tv_breast_screening);
        tv_antigen = rootView.findViewById(R.id.tv_antigen);
        tv_rtpcr = rootView.findViewById(R.id.tv_rtpcr);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        llCall = rootView.findViewById(R.id.llCall);
        tvCampCoordinatorName = rootView.findViewById(R.id.tvCampCoordinatorName);
        tvCampCoordinatorMobile = rootView.findViewById(R.id.tvCampCoordinatorMobile);
        tvDistrict = rootView.findViewById(R.id.tvDistrict);
        tvCampId = rootView.findViewById(R.id.tvCampId);
        edtSelTeam = rootView.findViewById(R.id.edtSelTeam);
        tvTeamMember1Mobile = rootView.findViewById(R.id.tvTeamMember1Mobile);
        tvTeamMember2Mobile = rootView.findViewById(R.id.tvTeamMember2Mobile);
        tvTeamNumber = rootView.findViewById(R.id.tvTeamNumber);
        tvTeamMember1Name = rootView.findViewById(R.id.tvTeamMember1Name);
        tvTeamMember2Name = rootView.findViewById(R.id.tvTeamMember2Name);
        tv_rejectDetails = rootView.findViewById(R.id.tv_rejectDetails);
        tv_reverification_bene = rootView.findViewById(R.id.tv_reverification_bene);
        tv_approveDetails = rootView.findViewById(R.id.tv_approveDetails);

        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);


        CardView cvCoordinatorDetails = rootView.findViewById(R.id.cvCoordinatorDetails);
        cvTeamDetails = rootView.findViewById(R.id.cvTeamDetails);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        Date = bundle.getString("Date");
        if (Date != null && !Date.equals("")) {
            Date = Date.replace("-", "/");
        }

        campType = bundle.getInt("campTypeId");

        campId = Integer.parseInt(ConstantData.getInstance().getCalendarCampId());

        if ((campType == 3 || campType == 4) || session.isHllUser()) {
            edtSelTeam.setVisibility(View.VISIBLE);

            if (teamNumber != 0) {
                cvTeamDetails.setVisibility(View.VISIBLE);
            } else {
                cvTeamDetails.setVisibility(View.GONE);

            }
        } else {
            edtSelTeam.setVisibility(View.GONE);
            cvTeamDetails.setVisibility(View.GONE);

        }
        DISTLGDCODE = bundle.getString("DISTLGDCODE");
        try {
            campDetails = (CampCalendarModel.OutputBean) bundle.getSerializable("campDetails");

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            d2dCampDetails = (D2DCampDetails.Output) bundle.getSerializable("campDetails");


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (campDetails != null) {
            campId = Integer.parseInt(campDetails.getCampId());
            cvCoordinatorDetails.setVisibility(View.VISIBLE);

            if (campDetails.getCordinatorName() != null) {
                tvCampCoordinatorName.setText(campDetails.getCordinatorName());
            }
            tvCampCoordinatorMobile.setText(campDetails.getMOBNO());
            tvCampId.setText(campDetails.getCampId());
            tvDistrict.setText(campDetails.getDISTNAME());

        } else {
            cvCoordinatorDetails.setVisibility(View.GONE);
        }

        if (d2dCampDetails != null) {
            campId = d2dCampDetails.getCampId();
            //  campId = Integer.parseInt(ConstantData.getInstance().getCalendarCampId());
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


    private void setDefault() {
        if (session.isHllUser()) {

            if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29")|| (DESGID.equalsIgnoreCase("160")
                    || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162")
                    || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77")
                    || DESGID.equalsIgnoreCase("128")||DESGID.equalsIgnoreCase("30")
                    ||DESGID.equalsIgnoreCase("108")||DESGID.equalsIgnoreCase("84")||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("136")))))) {
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
                        ||(DESGID.equalsIgnoreCase("137")|| (DESGID.equalsIgnoreCase("169")
                        ||DESGID.equalsIgnoreCase("31")||DESGID.equalsIgnoreCase("176")
                        ||(DESGID.equalsIgnoreCase("177")))))))))) {
                    new GetTeamId().execute();
                } else {
                    new GetCampDetailsCount().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE, Date, Date, String.valueOf(teamNumber));
                }
            }
        } else {
            if (Utilities.isNetworkAvailable(context)) {
                new GetCampDetailsCount().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE, Date, Date, String.valueOf(teamNumber));
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        }
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
        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utilities.showAlertDialog(context, "Confirm!", "Do you really want to call " + campDetails.getCordinatorName() + "?", true, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (campDetails.getMOBNO() != null) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + campDetails.getMOBNO()));
                            startActivity(intent);
                        }

                    }
                }, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

            }
        });
        tvTeamMember1Mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utilities.showAlertDialog(context, "Confirm!", "Do you really want to call " + tvTeamMember1Mobile.getText().toString() + "?", true, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!tvTeamMember1Mobile.getText().toString().isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + tvTeamMember1Mobile.getText().toString()));
                            startActivity(intent);
                        }

                    }
                }, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

            }
        });

        tvTeamMember2Mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utilities.showAlertDialog(context, "Confirm!", "Do you really want to call " + tvTeamMember2Mobile.getText().toString() + "?", true, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!tvTeamMember2Mobile.getText().toString().isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + tvTeamMember2Mobile.getText().toString()));
                            startActivity(intent);
                        }

                    }
                }, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

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
                                ||DESGID.equalsIgnoreCase("31")||DESGID.equalsIgnoreCase("176")|| DESGID.equalsIgnoreCase("34")
                                || DESGID.equalsIgnoreCase("147") || DESGID.equalsIgnoreCase("130")
                                || DESGID.equalsIgnoreCase("141")
                                ||(DESGID.equalsIgnoreCase("177")))))))))) {
                            new GetTeamId().execute();
                        } else {

                            new GetCampDetailsCount().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE, Date, Date, String.valueOf(teamNumber));
                        }
                    }
                } else {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetCampDetailsCount().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE, Date, Date, String.valueOf(teamNumber));
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                }


//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetCampDetailsCount().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE, Date, Date, String.valueOf(teamNumber));
//                } else {
//                    swipeRefreshLayout.setRefreshing(false);
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                }
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
                teamNumber = teamList.get(which).getTeamId();

                if (teamNumber == 0) {
                    cvTeamDetails.setVisibility(View.GONE);
                } else {
                    cvTeamDetails.setVisibility(View.VISIBLE);
                }
                tvTeamMember1Mobile.setText(teamList.get(which).getMember1ContactNo());
                tvTeamMember2Mobile.setText(teamList.get(which).getMember2ContactNo());
                tvTeamNumber.setText(teamList.get(which).getTeamNumber());
                tvTeamMember1Name.setText(teamList.get(which).getMember1());
                tvTeamMember2Name.setText(teamList.get(which).getMember2());

                edtSelTeam.setText(teamList.get(which).getTeamNumber());
                if (Utilities.isNetworkAvailable(context)) {
                    new GetCampDetailsCount().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE, Date, Date, String.valueOf(teamNumber));
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
        builderSingle.show();
    }


    private class GetCampDetailsCount extends AsyncTask<String, Void, String> {


        private NetworkProgressDialog netDialog;
        private TrafficSpeedMonitor speedMonitor;
        // track per-request totals if you want
        private volatile long lastRespBytes = 0L;
        private volatile long lastReqBytes = 0L;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(false);
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();



//            netDialog = new NetworkProgressDialog((Activity) context);
//            netDialog.setMessage("Contacting server...");
//            netDialog.show();
//
//            // start system-wide speed monitor
//            // start system-wide or app-only speed monitor (prefer app-only)
//            speedMonitor = new TrafficSpeedMonitor((sessionTx, sessionRx, upBps, downBps) -> {
//                final String up = humanReadableSpeed(upBps);
//                final String down = humanReadableSpeed(downBps);
//                // sessionTx/sessionRx are baseline-subtracted totals for this session
//                ((Activity) context).runOnUiThread(() -> {
//                    netDialog.updateUploadSpeed(up + ""); // per second
//                    netDialog.updateDownloadSpeed(down + "");
//                    netDialog.updateSentBytes(sessionTx);    // session total sent (B)
//                    netDialog.updateReceivedBytes(sessionRx); // session total received (B)
//                });
//            }, true); // <-- pass true to try per-UID (app-only)
//            speedMonitor.start();


        }

        @Override
        protected String doInBackground(String... params) {

//            ProgressListener pl = new ProgressListener() {
//                @Override
//                public void onRequestProgress(long bytesWritten, long contentLength) {
//                    lastReqBytes = bytesWritten;
//                    // you can publish progress or update UI via runOnUiThread if needed
//                }
//
//                @Override
//                public void onResponseProgress(long bytesRead, long contentLength) {
//                    lastRespBytes = bytesRead;
//                    // update netDialog from background via runOnUiThread
//                    ((Activity) context).runOnUiThread(() -> {
//                        netDialog.updateReceivedBytes(bytesRead);
//                        // if contentLength > 0 you can show percent
//                        if (contentLength > 0) {
//                            // optional: netDialog.setProgress((int)((bytesRead*100)/contentLength));
//                        }
//                    });
//                }
//            };


            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("FromDate", params[2]));
            param.add(new ParamsPojo("ToDate", params[3]));

            if ((campType == 3 || campType == 4) || session.isHllUser()) {
                param.add(new ParamsPojo("TeamID", params[4]));
//                res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsCount_V1, ApplicationConstants.webservice_d2d, param);
                res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsCount_InCampTest, ApplicationConstants.webservice_d2d, param);
            } else {
//                res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsCount, ApplicationConstants.webservice, param);
                res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsCountRegular_InCampTest, ApplicationConstants.webservice_d2d, param);
            }
            return res;
        }

        //49526 39
        @Override protected void onPostExecute(String result) {
            super.onPostExecute(result);


//            if (speedMonitor != null) speedMonitor.stop();
//            if (netDialog != null && netDialog.isShowing()) netDialog.dismiss();

            pd.dismiss();

            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    screeningTestList = new ArrayList<>();
                    ScreeningTestPojo pojoDetails = new Gson().fromJson(result, ScreeningTestPojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    JSONObject jsonObject = new JSONObject(result);
                    if (type.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");
                        screeningTestList = pojoDetails.getOutput();
                        if (screeningTestList.size() > 0) {
                            ScrenningTestModel screnningTestModel = screeningTestList.get(0);
                            setData(screnningTestModel);
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


        private String humanReadableSpeed(double bytesPerSec) {
            if (bytesPerSec < 1024) return String.format(Locale.getDefault(), "%.0f B/s", bytesPerSec);
            double kb = bytesPerSec / 1024.0;
            if (kb < 1024) return String.format(Locale.getDefault(), "%.2f KB/s", kb);
            return String.format(Locale.getDefault(), "%.2f MB/s", kb / 1024.0);
        }
    }

    private void setData(ScrenningTestModel screnningTestModel) {
        tv_facilitatedWorkers.setText(screnningTestModel.getFacilitatedWorkers());
        tv_basicDetails.setText(screnningTestModel.getBasicDetails());
        tv_physicalExam.setText(screnningTestModel.getPhysicalExamination());
        tv_lungTest.setText(screnningTestModel.getLungFunctioinTest());
        tv_audioTest.setText(screnningTestModel.getAudioScreeningTest());
        tv_visionTest.setText(screnningTestModel.getVisionScreening());
        tv_barcode.setText(screnningTestModel.getBarcode());
//        tv_ppSample.setText(screnningTestModel.getPPSampleCollection());
        tv_acknowlege.setText(screnningTestModel.getAckowledgement());
//        tv_urineSampleCollection.setText(screnningTestModel.getUrineSampleCollection());
        tv_totalTest.setText(screnningTestModel.getTotalTests());
        tv_breast_screening.setText(screnningTestModel.getBreastScreening());
        tv_antigen.setText(screnningTestModel.getAntigen());
        tv_rtpcr.setText(screnningTestModel.getRTPCR());
        tv_approveDetails.setText(screnningTestModel.getApprovedBeneficiaries());
        tv_rejectDetails.setText(screnningTestModel.getRejectedBeneficiaries());
        tv_reverification_bene.setText(screnningTestModel.getVerifiedBeneficiaries());
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


    private class GetTeamId extends AsyncTask<String, Void, String> {

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
                        teamNumber = Integer.parseInt(jsonObject1.getString("TeamNumber"));
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetCampDetailsCount().execute(ConstantData.getInstance().getCalendarCampId(), DISTLGDCODE, Date, Date, String.valueOf(teamNumber));
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
