package com.myhindlab.abkat.activities.couriermodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.BarcodeListModel;
import com.myhindlab.abkat.models.couriermodule.CamplistOnLandingLabModel;
import com.myhindlab.abkat.models.couriermodule.CourierForwardedModel;
import com.myhindlab.abkat.models.couriermodule.CourierSampleTemperatureModel;
import com.myhindlab.abkat.models.couriermodule.CourierTubesModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourierSendStep2_Activity extends Activity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private TextView tv_sample_temperature;
    private RecyclerView rv_tube_counts;
    private Button btn_submit;
    private EditText edt_sample_count;

    private String userId, projectId, fromlabId, toLabCode, sendcouriertolabId, selectedLabID, campTypeId, Type_CW_CourierCampID, categoryId, courierDate, courierTime,
            modeOfTransportId, docketNo, companyName, runnerBoyId, busNo, mobileNo, amount, sampleBox, sampleCount,
            expectedArrivalDate, expectedArrivalTime, barcode, fileName, temperatureId, forwardedCourierIds, sourceClientCode, destClientCode;

    private List<CourierTubesModel.OutputBean> tubeList;
    private ArrayList<BarcodeListModel.Output> barcodeList;
    private ArrayList<CamplistOnLandingLabModel.Output> campList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_send_step2);

        init();
        getSessionDetails();
        setDefaults();
        setEventListener();
        setUpToolBar();
    }

    private void init() {
        context = CourierSendStep2_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        rv_tube_counts = findViewById(R.id.rv_tube_counts);
        rv_tube_counts.setLayoutManager(new LinearLayoutManager(context));
        tv_sample_temperature = findViewById(R.id.tv_sample_temperature);
        btn_submit = findViewById(R.id.btn_submit);
        edt_sample_count = findViewById(R.id.edt_sample_count);

        tubeList = new ArrayList<>();
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                projectId = json.getString("ProjectId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDefaults() {


        fromlabId = getIntent().getStringExtra("fromLab");
//        sourceClientCode = getIntent().getStringExtra("FromClient");
//        destClientCode = getIntent().getStringExtra("ToClient");
        toLabCode = getIntent().getStringExtra("toLabCode");
        // processingLabCode = getIntent().getStringExtra("processingLabCode");
        categoryId = getIntent().getStringExtra("categoryId");
        courierDate = getIntent().getStringExtra("courierDate");
        courierTime = getIntent().getStringExtra("courierTime");
        modeOfTransportId = getIntent().getStringExtra("modeOfTransportId");
        docketNo = getIntent().getStringExtra("docketNo");
        companyName = getIntent().getStringExtra("companyName");
        runnerBoyId = getIntent().getStringExtra("runnerBoyId");
        busNo = getIntent().getStringExtra("busNo");
        mobileNo = getIntent().getStringExtra("mobileNo");
        amount = getIntent().getStringExtra("amount");
        sampleBox = getIntent().getStringExtra("sampleBox");
      //  sampleCount = getIntent().getStringExtra("sampleCount");
        expectedArrivalDate = getIntent().getStringExtra("expectedArrivalDate");
        expectedArrivalTime = getIntent().getStringExtra("expectedArrivalTime");
        barcode = getIntent().getStringExtra("barcode");
        fileName = getIntent().getStringExtra("fileName");
        forwardedCourierIds = getIntent().getStringExtra("forwardedCourierIds");
        barcodeList = (ArrayList<BarcodeListModel.Output>) getIntent().getSerializableExtra("selectedBarcodeList");
        sendcouriertolabId = getIntent().getStringExtra("sendcouriertolabId");
        selectedLabID = getIntent().getStringExtra("selectedLabID");
        campTypeId = getIntent().getStringExtra("CampType");
        Type_CW_CourierCampID = getIntent().getStringExtra("Type_CW_CourierCampID");
        campList = (ArrayList<CamplistOnLandingLabModel.Output>) getIntent().getSerializableExtra("campList");

        if (Utilities.isNetworkAvailable(context)) {
            new GetServiceTubes().execute();
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }

    private void setEventListener() {
        tv_sample_temperature.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetSampleTemperature().execute();
            } else {
                Utilities.showMessage(R.string.msgt_nointernetconnection, context);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDate();
            }
        });
    }

    private void submitDate() {
        int count = 0;

        if (tv_sample_temperature.getText().toString().trim().equals("")) {
            Utilities.showMessageString("Please select sample temperature", context);
            return;
        }
        String selectedBarcode = "[]";

        if (barcodeList.size() > 0) {
            selectedBarcode = new Gson().toJson(barcodeList);
        }

        JsonArray SampleDetailsJsonArray = new JsonArray();
        for (int i = 0; i < tubeList.size(); i++) {

            TubeCountAdapter.MyViewHolder myViewHolder = (TubeCountAdapter.MyViewHolder) rv_tube_counts.findViewHolderForAdapterPosition(i);

            if (myViewHolder.edt_count.getText().toString().equals("")) {
                myViewHolder.edt_count.setError("Enter tube count");
                return;
            }

//            if (myViewHolder.tv_tube_name.getText().toString().equalsIgnoreCase("Gel Tube")) {
//                int EDTATubeCount = 0, gelTubeCount = 0;
//
//                EDTATubeCount = Integer.parseInt(tubeList.get(0).getCount()) * 2;
//
//
//                gelTubeCount = Integer.parseInt(myViewHolder.edt_count.getText().toString());
//                if (gelTubeCount != EDTATubeCount) {
//                    myViewHolder.edt_count.setError("Gel Tube (" + gelTubeCount + ") count should be 2 * EDTA Tube (" + EDTATubeCount + ")");
//                    return;
//                }
//            }
            count = count + Integer.parseInt(tubeList.get(i).getCount());
            JsonObject tubeDetails = new JsonObject();
            tubeDetails.addProperty("TubeId", tubeList.get(i).getTubeId());
            tubeDetails.addProperty("TotalCount", tubeList.get(i).getCount());
            tubeDetails.addProperty("CreatedBy", userId);
            SampleDetailsJsonArray.add(tubeDetails);
        }

        JsonObject sampleDetails = new JsonObject();
        sampleDetails.add("input", SampleDetailsJsonArray);

        if (count == 0) {
            Utilities.showMessageString("All tube counts cannot be zero", context);
            return;
        }

        if (barcodeList.size() > 0) {
            if (count < barcodeList.size()) {
                Utilities.showMessageString("Enter units or tubes count greater than or equals to selected barcodes", context);
                return;
            }

        } else {
            if (count != Integer.parseInt(sampleCount)) {
                Utilities.showMessageString("Total no. of tubes should match with total addition of individual test counts", context);
                return;
            }
        }
//        String forwardedIds = "";
//
//        if (!forwardedCourierIds.equals("0")) {
//            JsonObject forwardedCouriers = new JsonObject();
//            String[] elements = forwardedCourierIds.split(",");
//            JsonArray jsonArray = new JsonArray();
//            for (String element : elements) {
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("RefCourierID", element);
//                jsonArray.add(jsonObject);
//            }
//            forwardedCouriers.add("input", jsonArray);
//            forwardedIds = forwardedCouriers.toString();
//        } else {
//            forwardedIds = "0";
//        }

        String forwardedIds = "";

        if (!forwardedCourierIds.equals("0")) {
            List<CourierForwardedModel.OutputBean> selectedCouriersList =
                    (List<CourierForwardedModel.OutputBean>) getIntent().getSerializableExtra("selectedCouriersList");
            JsonObject forwardedCouriers = new JsonObject();

            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < selectedCouriersList.size(); i++) {

                String[] elements = selectedCouriersList.get(i).getInitCourierID().split(",");
                for (String element : elements) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("RefCourierID", selectedCouriersList.get(i).getCourierID());
                    jsonObject.addProperty("InitCourierID", element);
                    jsonArray.add(jsonObject);
                }
            }
            forwardedCouriers.add("input", jsonArray);
            forwardedIds = forwardedCouriers.toString();
        } else {
            JsonObject forwardedCouriers = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("RefCourierID", "0");
            jsonObject.addProperty("InitCourierID", "0");
            jsonArray.add(jsonObject);
            forwardedCouriers.add("input", jsonArray);
            forwardedIds = forwardedCouriers.toString();
        }

        JsonObject barcodeDetails = new JsonObject();
        JsonArray barcodeArray = new JsonArray();


        for (BarcodeListModel.Output element : barcodeList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Barcode", element.getBarcode());
            jsonObject.addProperty("ServiceCode", element.getServiceCode());
            barcodeArray.add(jsonObject);
        }
        barcodeDetails.add("input", barcodeArray);


        JsonArray campArr = new JsonArray();


        if (campList != null) {
            for (CamplistOnLandingLabModel.Output o : campList
            ) {
                if (o.isChecked()) {
                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.addProperty("CampID", o.getCampId());
                    campArr.add(jsonObject1);

                }
            }
        }


        if (Utilities.isNetworkAvailable(context)) {
            new InsertCourierSentDetails_Updated().execute(
                    fromlabId,
                    sendcouriertolabId,
                    toLabCode,
                    categoryId,
                    courierDate,
                    courierTime,
                    modeOfTransportId,
                    companyName,
                    docketNo,
                    mobileNo,
                    amount,
                    sampleBox,
                    runnerBoyId,
                    edt_sample_count.getText().toString().trim(),
                    expectedArrivalDate,
                    expectedArrivalTime,
                    barcode,
                    temperatureId,
                    fileName,
                    busNo,
                    projectId,
                    forwardedIds,
                    userId,
                    sampleDetails.toString(),
                    barcodeDetails.toString(),
                    Type_CW_CourierCampID,
                    selectedLabID,
                    campTypeId


            );
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }

    }


    private class GetSampleTemperature extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetSampleTemperature, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetSampleTemperature, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierSampleTemperatureModel pojo = new Gson().fromJson(result, CourierSampleTemperatureModel.class);
                    type = pojo.getStatus();
                    message = pojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<CourierSampleTemperatureModel.OutputBean> temperatureList = pojo.getOutput();
                        listTemperatureDialog(temperatureList);
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }

        }
    }

    private void listTemperatureDialog(List<CourierSampleTemperatureModel.OutputBean> temperatureList) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Sample Temperature");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < temperatureList.size(); i++) {
            arrayAdapter.add(temperatureList.get(i).getSampleTempName());
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            CourierSampleTemperatureModel.OutputBean objMain = temperatureList.get(which);
            temperatureId = objMain.getSampleTempID();
            tv_sample_temperature.setText(objMain.getSampleTempName());
        });
        builderSingle.show();
    }

    private class GetServiceTubes extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetServiceTubes, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetServiceTubes, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierTubesModel pojo = new Gson().fromJson(result, CourierTubesModel.class);
                    type = pojo.getStatus();
                    message = pojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        tubeList = pojo.getOutput();

                        if (!forwardedCourierIds.equals("0")) {
                            List<CourierForwardedModel.OutputBean> selectedCouriersHavingTubeCountsList =
                                    (List<CourierForwardedModel.OutputBean>) getIntent().getSerializableExtra("selectedCouriersHavingTubeCountsList");

                            for (CourierForwardedModel.OutputBean courierList : selectedCouriersHavingTubeCountsList) {
                                for (CourierForwardedModel.OutputBean.TubeDetails tubeDetails : courierList.getTubeDetails()) {
                                    for (int i = 0; i < tubeList.size(); i++) {
                                        if (tubeList.get(i).getTubeId().equals(tubeDetails.getTubeId())) {
                                            tubeList.get(i).setCount(String.valueOf(Integer.parseInt(tubeList.get(i).getCount()) + Integer.parseInt(tubeDetails.getTotalCount())));
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < tubeList.size(); i++) {
                                tubeList.get(i).setCount("");
                            }
                        }
                        TubeCountAdapter tubeCountAdapter = new TubeCountAdapter(new TubeEvents() {
                            @Override
                            public void onChangeTubeCount(int position) {

//                                rv_tube_counts.setAdapter(new TubeCountAdapter(this));
                            }
                        });
                        rv_tube_counts.setAdapter(tubeCountAdapter);
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }

        }

    }


    interface TubeEvents {
        void onChangeTubeCount(int position);
    }

    private class TubeCountAdapter extends RecyclerView.Adapter<TubeCountAdapter.MyViewHolder> {

        TubeEvents tubeEvents;

        public TubeCountAdapter(TubeEvents tubeEvents) {
            this.tubeEvents = tubeEvents;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_tube_count_send, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int pos) {
            int position = holder.getAbsoluteAdapterPosition();

            holder.tv_tube_name.setText(tubeList.get(position).getTubeContent());
            holder.edt_count.setText("0");
            tubeList.get(position).setCount("0");

            holder.edt_count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
//                        Toast.makeText(getApplicationContext(), "Got the focus"+position, Toast.LENGTH_LONG).show();
                        if (position == 1) {
                            String count1 = String.valueOf(Integer.parseInt(tubeList.get(0).getCount()) * 2);
                            tubeList.get(1).setCount(count1);
                            tubeEvents.onChangeTubeCount(position);
                            holder.edt_count.setText(count1);

                            holder.edt_count.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void afterTextChanged(Editable s) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    try {

                                       // if (!(tvTotalCentifigeTube.getText().toString().isEmpty() && tvSampleRejected.getText().toString().isEmpty())){

                                        int num1 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(0).getCount())));
                                        int num2 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(1).getCount())));

                                        int addition = num1+num2;

                                        edt_sample_count.setText(String.valueOf(addition));
                                        edt_sample_count.setEnabled(false);



                                    }catch (Exception e){

                                        e.printStackTrace();
                                    }






//                     int substraction = Integer.parseInt(tvSampleSubmitted.getText().toString()) - Integer.parseInt(tvTotalCentifigeTube.getText().toString());
//                    tvSampleRejected.setText(String.valueOf(substraction));

                                }
                            });


                            int num1 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(0).getCount())));
                            int num2 = Integer.parseInt(String.valueOf(Integer.parseInt(tubeList.get(1).getCount())));

                            int addition = num1+num2;

                            edt_sample_count.setText(String.valueOf(addition));
                        }
                    } else {
//                        Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                    }
                }
            });

            holder.edt_count.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equalsIgnoreCase("")) {
                        tubeList.get(position).setCount(s.toString());


                    } else tubeList.get(position).setCount("0");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return tubeList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_tube_name;
            private EditText edt_count;

            MyViewHolder(final View view) {
                super(view);
                tv_tube_name = view.findViewById(R.id.tv_tube_name);
                edt_count = view.findViewById(R.id.edt_count);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private class InsertCourierSentDetails_Updated extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";

            Log.d("InsertDetails", "params" + Arrays.toString(params));
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("RegistrationLabID", params[0])
                        .add("CourierToLabID", params[1])
                        .add("ProcessLabID", params[2])
                        .add("CategoryID", params[3])
                        .add("CourierDate", params[4])
                        .add("CourierTime", params[5])
                        .add("ModeId", params[6])
                        .add("CompanyName", params[7])
                        .add("DocketNo", params[8])
                        .add("ContactNo", params[9])
                        .add("AmountPaidbySender", params[10])
                        .add("NoOfBoxes", params[11])
                        .add("RunnerBoySendUserID", params[12])
                        .add("NoOfSamples", params[13])
                        .add("ExpArrivalDate", params[14])
                        .add("ExpArrivalTime", params[15])
                        .add("Barcode", params[16])
                        .add("SampleTempID", params[17])
                        .add("PhotoPath", params[18])
                        .add("BusVehicleNumber", params[19])
                        .add("ProjectID", params[20])
                        .add("ForwerdedCourierID", params[21])
                        .add("CreatedBy", params[22])
                        .add("SampleDetails", params[23])
                        .add("BarcodeServices", params[24])
                        .add("Type_CW_CourierCampID", params[25])
                        .add("LandingLab", params[26])
                        .add("CampType", params[27])

                        .build();

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.MINUTES)
                        .writeTimeout(5, TimeUnit.MINUTES)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .build();
                String url = ApplicationConstants.webservice_d2d + ApplicationConstants.InsertCourierSentDetails_Updated;
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                res = response.body().string();

            } catch (SocketTimeoutException ste) {
                ste.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            try {
                pd.dismiss();
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("FinishCourierSend1_Activity"));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("CourierSentList_Activity"));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("CourierForwardList_Activity"));

                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Courier sent successfully!");
                        alertDialog.setIcon((R.drawable.icon_success));
                        alertDialog.setButton("OK", (dialog, which) -> {
                            finish();
                        });
                        alertDialog.show();
                    } else if (status.equalsIgnoreCase("fail")) {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail", "Server not connected", false);
                e.printStackTrace();
            }
        }

    }

    private void setUpToolBar() {
        ImageButton back_btn = findViewById(R.id.tool_leftbtn);
        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        TextView Title = findViewById(R.id.tool_titile);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Send Courier");

        back_btn.setOnClickListener(view -> finish());
        btn_save_accordian.setVisibility(View.GONE);

    }


}

//  CourierDetails:{"RegistrationLabID":"22","CourierToLabID":"39","ProcessLabID":"29","CategoryID":"1","CourierDate":"2020/09/11","CourierTime":"12:56 PM","ModeId":"3","CompanyName":"","DocketNo":"","ContactNo":"9762365252","AmountPaidbySender":"100","NoOfBoxes":"5","NoOfSamples":"25","ExpArrivalDate":"2020/09/12","ExpArrivalTime":"12:57 PM","Barcode":"6565766","SampleTempID":"1","PhotoPath":"Send_22_39_29_4146347","CreatedBy":"437"}
//  SampleDetails:[{"TubeId":1,"TotalCount":"2","CreatedBy":"437"},{"TubeId":2,"TotalCount":"5","CreatedBy":"437"},{"TubeId":3,"TotalCount":"4","CreatedBy":"437"},{"TubeId":4,"TotalCount":"8","CreatedBy":"437"}]
