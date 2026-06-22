package com.myhindlab.abkat.activities.attendance_details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.GetCampCloseDetailsModel;
import com.myhindlab.abkat.models.GetConsumableListDetailsModel;
import com.myhindlab.abkat.models.ScrenningTestModel;
import com.myhindlab.abkat.pojos.ScreeningTestPojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
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

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CampClosingConfirmationActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_facilitatedWorkers, tv_approvedBene, tv_reverification_bene, tv_rejectedBene, tv_basicDetails, tv_physicalExam, tv_lungTest, tv_audioTest, tv_visionTest, tv_barcode,
            tv_ppSample, tv_acknowlege, tv_urineSampleCollection, tv_totalTest, tv_breast_screening, tv_antigen, tv_rtpcr;
    private String Date, verifiedBebeficiary, DISTLGDCODE, userId = "",campId = "";
    private static ArrayList<ScrenningTestModel> screeningTestList;
    private EditText edtTotalBeneficiary, edtRejectedBene, edtRemark, edtSampleCollection, edtSampleToHublab, edtSampleToHomelab, edtPlainTube, edtGelTube, edtFloride, edtUrinContainer, edtUrineSampleCollection;
    private Button btnCampClose;
    private RecyclerView rvConsumable;
    private List<GetConsumableListDetailsModel.Output> consumableList;
    private LinearLayout llRemark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_closing_confirmation);
        context = CampClosingConfirmationActivity.this;
        init();
        setUpToolbar();
        getSessionData();
        setDefault();
        setEventHandlers();


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Closing Confirmation");
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        session = new UserSessionManager(context);
        tv_facilitatedWorkers = findViewById(R.id.tv_facilitatedWorkers);
        tv_basicDetails = findViewById(R.id.tv_basicDetails);
        tv_physicalExam = findViewById(R.id.tv_physicalExam);
        tv_lungTest = findViewById(R.id.tv_lungTest);
        tv_audioTest = findViewById(R.id.tv_audioTest);
        tv_visionTest = findViewById(R.id.tv_visionTest);
        tv_barcode = findViewById(R.id.tv_barcode);
        tv_ppSample = findViewById(R.id.tv_ppSample);
        tv_acknowlege = findViewById(R.id.tv_acknowlege);
        tv_approvedBene = findViewById(R.id.tv_approvedBene);
        tv_reverification_bene = findViewById(R.id.tv_reverification_bene);
        tv_urineSampleCollection = findViewById(R.id.tv_urineSampleCollection);
        tv_totalTest = findViewById(R.id.tv_totalTest);
        tv_breast_screening = findViewById(R.id.tv_breast_screening);
        tv_antigen = findViewById(R.id.tv_antigen);
        tv_rtpcr = findViewById(R.id.tv_rtpcr);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        btnCampClose = findViewById(R.id.btnCampClose);
        edtRejectedBene = findViewById(R.id.edtRejectedBene);
        edtTotalBeneficiary = findViewById(R.id.edtTotalBeneficiary);
        edtSampleCollection = findViewById(R.id.edtSampleCollection);
        edtSampleToHublab = findViewById(R.id.edtSampleToHublab);
        edtSampleToHomelab = findViewById(R.id.edtSampleToHomelab);
        edtPlainTube = findViewById(R.id.edtPlainTube);
        edtGelTube = findViewById(R.id.edtGelTube);
        edtFloride = findViewById(R.id.edtFloride);
        edtUrinContainer = findViewById(R.id.edtUrinContainer);
        rvConsumable = findViewById(R.id.rvConsumable);
        edtRemark = findViewById(R.id.edtRemark);
        llRemark = findViewById(R.id.llRemark);
        tv_rejectedBene = findViewById(R.id.tv_rejectedBene);
        edtUrineSampleCollection = findViewById(R.id.edtUrineSampleCollection);

        rvConsumable.setHasFixedSize(true);
        rvConsumable.setLayoutManager(new LinearLayoutManager(context));


        if (getIntent() != null) {
            Intent intent = getIntent();
            Date = intent.getStringExtra("Date");
            DISTLGDCODE = intent.getStringExtra("DISTLGDCODE");
            campId = intent.getStringExtra("campId");

        }


        edtTotalBeneficiary.setEnabled(false);
        edtRejectedBene.setEnabled(false);

    }


    private void setDefault() {
        if (Utilities.isNetworkAvailable(context)) {
            new GetCampDetailsCount().execute(campId, DISTLGDCODE, Date, Date);
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setEventHandlers() {
        btnCampClose.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetCampDetailsCount().execute(campId, DISTLGDCODE, Date, Date);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);

                }
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCampClose:

                if (consumableList == null || consumableList.isEmpty()) {
                    Utilities.showToastMessage("Please enter consumable details", context, false);
                    return;
                }

                int count = 0;

                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < consumableList.size(); i++) {

                    TubeCountAdapter.MyViewHolder myViewHolder = (TubeCountAdapter.MyViewHolder) rvConsumable.findViewHolderForAdapterPosition(i);

                    if (myViewHolder.edt_count.getText().toString().trim().isEmpty()) {
                        myViewHolder.edt_count.setError("Enter consumed count");
                        return;
                    } else {
//                        int consumableCount = Integer.parseInt(myViewHolder.edt_count.getText().toString());
//                        String consumableName = myViewHolder.tv_tube_name.getText().toString();
//                        if (consumableName.equalsIgnoreCase("Gloves") || consumableName.equalsIgnoreCase("Sanitizer Bottle")||consumableName.equalsIgnoreCase("Gel Tube")) {
//
//
//                            int totalBarcode = Integer.parseInt(tv_barcode.getText().toString());
//                            int twoTimes = totalBarcode * 2;
//                            if (consumableCount < twoTimes) {
//                                myViewHolder.edt_count.setError(consumableName + " count should two times of Total Sample Collection!");
//                                return;
//                            }
//
//
//                        }
//
//
//                        if (consumableName.equalsIgnoreCase("EDTA Tube") ){
//                            if (consumableCount > 0) {
//                                int totalBarcode = Integer.parseInt(tv_barcode.getText().toString());
//                                if (consumableCount < totalBarcode) {
//                                    myViewHolder.edt_count.setError(consumableName + " count should be greater or equal to Total Sample Collection!");
//                                    return;
//                                }
//                            }
//                        }


                        int consumableCount = Integer.parseInt(myViewHolder.edt_count.getText().toString());
                        String consumableName = myViewHolder.tv_tube_name.getText().toString();
                        int totalBarcode = Integer.parseInt(tv_barcode.getText().toString());

                        if (consumableName.equalsIgnoreCase("Gloves")
                                || consumableName.equalsIgnoreCase("Sanitizer Bottle")) {

                            int twoTimes = totalBarcode * 2;
                            if (consumableCount < twoTimes) {
                                myViewHolder.edt_count.setError(consumableName
                                        + " count should be two times of Total Sample Collection!");
                                return;
                            }

                        } else if (consumableName.equalsIgnoreCase("Gel Tube")) {

                            // Gel Tube specific logic
                            int twoTimes = totalBarcode * 2;
                            if (consumableCount != twoTimes) {
                                myViewHolder.edt_count.setError("Gel ट्यूब काउंट हा Rejected नसलेल्या सॅम्पल कलेक्शन झालेल्या लाभार्थ्यांच्या एकूण संख्येच्या दुप्पट असावा.");
                                return;
                            }

                        } else if (consumableName.equalsIgnoreCase("EDTA Tube")) {

                            // EDTA specific logic
                            if (consumableCount != totalBarcode) {
                                myViewHolder.edt_count.setError("EDTA ट्यूब काउंट हा Rejected नसलेल्या सॅम्पल कलेक्शन झालेल्या लाभार्थ्यांच्या एकूण संख्येएवढा असावा.");
                                return;
                            }
                        }
                    }


                    count = count + Integer.parseInt(consumableList.get(i).getTotalCount());
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("ConsumableId", consumableList.get(i).getConsumableID());
                    jsonObject.addProperty("TotalCount", consumableList.get(i).getTotalCount());
                    jsonObject.addProperty("CreatedBy", consumableList.get(i).getCreatedBy());
                    jsonArray.add(jsonObject);
                }


//                if (count == 0) {
//                    Utilities.showMessageString("All consumable counts cannot be zero", context);
//                    return;
//                }


                Utilities.showAlertDialog(context, "Confirm!", "कॅम्प closing confirmation देण्यापूर्वी Beneficiary आणि ट्यूबची माहिती बरोबर असल्याची खात्री करा.", true, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new InsertCampCloseActivity().execute(
                                campId,
                                userId,
                                jsonArray.toString(),
                                "",
                                "0",
                                "0",
                                "0",
                                "0",
                                edtRemark.getText().toString().trim()
                        );

                    }
                }, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        }
    }

    private class InsertCampCloseActivity extends AsyncTask<String, Void, String> {
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("ClosingDetails", result);
            pd.dismiss();
            if (!result.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (!status.equalsIgnoreCase("fail")) {
                        String msg = jsonObject.getString("message");


                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("refresh_exp_benf_list").putExtra("confirmation", "1"));


//                        Utilities.showAlertDialog(context, "Success", "Camp confirmation done submitted successfully", true);


                        Utilities.showAlertDialog(
                                CampClosingConfirmationActivity.this,
                                status,
                                "Camp confirmation done submitted successfully",
                                true,
                                "Okay",
                                (dialog, which) -> finish()
                        );

//                        new GetCampCloseDetails().execute(campId);

//                        finish();

                    } else {
                        String msg = jsonObject.getString("ExceptionValue");
//                        Utilities.showAlertDialog(context, "Fail", msg, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showAlertDialog(context, "Fail", e.getMessage(), false);

                }

            }
        }

        @Override
        protected String doInBackground(String... params) {
            MediaType JSON
                    = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
            OkHttpClient client = new OkHttpClient();

//            RequestBody body = RequestBody.create(json, JSON);
            RequestBody formBody = new FormBody.Builder()
                    .add("CampID", params[0])
                    .add("CampCloseUserid", params[1])
                    .add("JsonConsumableDetails", params[2])
                    .add("OtherRemark", params[3])
                    .add("TotalBenificiary", params[4])
                    .add("SampleCollectionCount", params[5])
                    .add("SampleSendToHubLabCount", params[6])
                    .add("SampleSendToHomeLabCount", params[7])
                    .add("OtherRemarkSummary", params[8])
                    .build();

            Log.d("ClosingDetails", Arrays.toString(params));


            Request request = new Request.Builder()
                    .url(ApplicationConstants.webservice_d2d + ApplicationConstants.InsertCampClosingConfirmation)
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "";
        }
    }


    private class GetCampDetailsCount extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(false);
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("FromDate", params[2]));
            param.add(new ParamsPojo("ToDate", params[3]));

//            res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsCount, ApplicationConstants.webservice, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsCountRegular_InCampTest, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing())
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
                    new GetCampCloseDetails().execute(campId);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private class GetCampCloseDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(false);
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampCloseDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    GetCampCloseDetailsModel getCampCloseDetailsModel = new Gson().fromJson(result, GetCampCloseDetailsModel.class);

                    if (!getCampCloseDetailsModel.getStatus().equalsIgnoreCase("fail")) {
                        edtTotalBeneficiary.setText(String.valueOf(getCampCloseDetailsModel.getOutput().get(0).getTotalBenificiary()));
                        edtSampleCollection.setText(String.valueOf(getCampCloseDetailsModel.getOutput().get(0).getSampleCollectionCount()));
                        edtSampleToHublab.setText(String.valueOf(getCampCloseDetailsModel.getOutput().get(0).getSampleSendToHubLabCount()));
                        edtSampleToHomelab.setText(String.valueOf(getCampCloseDetailsModel.getOutput().get(0).getSampleSendToHomeLabCount()));
                        edtPlainTube.setText(String.valueOf(getCampCloseDetailsModel.getOutput().get(0).getPlainTubeCount()));
                        edtGelTube.setText(String.valueOf(getCampCloseDetailsModel.getOutput().get(0).getGelTubeCount()));
                        edtFloride.setText(String.valueOf(getCampCloseDetailsModel.getOutput().get(0).getFlorideCount()));
                        edtUrinContainer.setText(String.valueOf(getCampCloseDetailsModel.getOutput().get(0).getUrineContainer()));
                        btnCampClose.setEnabled(false);
                    } else {
//                        Utilities.showAlertDialog(context, "Fail", getCampCloseDetailsModel.getMessage(), false);

                    }
                }
                new GetConsumableListDetails().execute(campId);

            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private class GetConsumableListDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(false);
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampId", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetConsumableListDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    GetConsumableListDetailsModel getCampCloseDetailsModel = new Gson().fromJson(result, GetConsumableListDetailsModel.class);

                    if (!getCampCloseDetailsModel.getStatus().equalsIgnoreCase("fail")) {
                        consumableList = new ArrayList<>();
                        consumableList = getCampCloseDetailsModel.getOutput();
                        rvConsumable.setAdapter(new TubeCountAdapter());

                    } else {
                        Utilities.showAlertDialog(context, "Fail", getCampCloseDetailsModel.getMessage(), false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void setData(ScrenningTestModel screnningTestModel) {
        tv_facilitatedWorkers.setText(screnningTestModel.getFacilitatedWorkers());
        edtTotalBeneficiary.setText(screnningTestModel.getApprovedBeneficiaries());
        tv_basicDetails.setText(screnningTestModel.getBasicDetails());
        tv_physicalExam.setText(screnningTestModel.getPhysicalExamination());
        tv_lungTest.setText(screnningTestModel.getLungFunctioinTest());
        tv_audioTest.setText(screnningTestModel.getAudioScreeningTest());
        tv_visionTest.setText(screnningTestModel.getVisionScreening());
        tv_barcode.setText(screnningTestModel.getBarcode());
        edtSampleCollection.setText(screnningTestModel.getBarcode());
        tv_ppSample.setText(screnningTestModel.getPPSampleCollection());
        tv_acknowlege.setText(screnningTestModel.getAckowledgement());
        tv_urineSampleCollection.setText(screnningTestModel.getUrineSampleCollection());
        tv_totalTest.setText(screnningTestModel.getTotalTests());
        tv_breast_screening.setText(screnningTestModel.getBreastScreening());
        tv_antigen.setText(screnningTestModel.getAntigen());
        tv_rtpcr.setText(screnningTestModel.getRTPCR());
        edtRemark.setText(screnningTestModel.getOtherRemark1());
        tv_approvedBene.setText(screnningTestModel.getApprovedBeneficiaries());
        tv_rejectedBene.setText(screnningTestModel.getRejectedBeneficiaries());
        edtRejectedBene.setText(screnningTestModel.getRejectedBeneficiaries());
        tv_reverification_bene.setText(screnningTestModel.getVerifiedBeneficiaries());

        verifiedBebeficiary = screnningTestModel.getVerifiedBeneficiaries();


        int totalSampleScreened = Integer.parseInt(tv_barcode.getText().toString());
        int totalBasicDetails = Integer.parseInt(tv_basicDetails.getText().toString());
        int totalPhysicalExam = Integer.parseInt(tv_physicalExam.getText().toString());
        int totalLungTest = Integer.parseInt(tv_lungTest.getText().toString());
        int totalAudioTest = Integer.parseInt(tv_audioTest.getText().toString());
        int totalVisionTest = Integer.parseInt(tv_visionTest.getText().toString());
        int totalAcknowlege = Integer.parseInt(tv_acknowlege.getText().toString());
        int totalBreastScreening = Integer.parseInt(tv_breast_screening.getText().toString());
        int totalUrineCount = Integer.parseInt(tv_urineSampleCollection.getText().toString());
        int totalBene = Integer.parseInt(tv_facilitatedWorkers.getText().toString());

        if (totalLungTest < totalBene) {
            tv_lungTest.setTextColor(context.getColor(R.color.chartyellow));
        } else {
            tv_lungTest.setTextColor(context.getColor(R.color.black));

        }

        if (totalUrineCount < totalBene) {
            tv_urineSampleCollection.setTextColor(context.getColor(R.color.chartyellow));
        } else {
            tv_urineSampleCollection.setTextColor(context.getColor(R.color.black));

        }

        if (totalPhysicalExam < totalBene) {
            tv_physicalExam.setTextColor(context.getColor(R.color.chartyellow));
        } else {
            tv_physicalExam.setTextColor(context.getColor(R.color.black));

        }

        if (totalVisionTest < totalBene) {
            tv_visionTest.setTextColor(context.getColor(R.color.chartyellow));
        } else {
            tv_visionTest.setTextColor(context.getColor(R.color.black));

        }

        if (totalAudioTest < totalBene) {
            tv_audioTest.setTextColor(context.getColor(R.color.chartyellow));
        } else {
            tv_audioTest.setTextColor(context.getColor(R.color.black));

        }
    }

    private class TubeCountAdapter extends RecyclerView.Adapter<TubeCountAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumable_list_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();

            holder.tv_tube_name.setText(consumableList.get(position).getConsumableName());
            holder.edt_count.setText(consumableList.get(position).getTotalCount());

            holder.edt_count.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    consumableList.get(position).setTotalCount(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
//            holder.edt_count.setText(consumableList.get(position).getCount());
        }

        @Override
        public int getItemCount() {
            return consumableList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_tube_name;
            private EditText edt_count;

            MyViewHolder(final View view) {
                super(view);
                tv_tube_name = view.findViewById(R.id.tvName);
                edt_count = view.findViewById(R.id.edtCount);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


}
