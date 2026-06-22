package com.myhindlab.abkat.activities.couriermodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.couriermodule.CampInfoListAdapter;
import com.myhindlab.abkat.models.couriermodule.ClientListModel;
import com.myhindlab.abkat.models.couriermodule.CourierReceivedDetailsModel;
import com.myhindlab.abkat.models.couriermodule.GetInfoModel;
import com.myhindlab.abkat.models.couriermodule.HLLDCWiseLab;
import com.myhindlab.abkat.models.couriermodule.LBMWiseLab;
import com.myhindlab.abkat.models.couriermodule.LabListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CourierReceivedList_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private TextView tv_select_lab, tv_lab, tv_from_date, tv_to_date, tvDestClientList;
    private RecyclerView rv_courier;
    AlertDialog labDialog;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private String userId, desgId, labcode, labName, courierBarCode, destClientCode = "";
    private CourierReceivedDetailsModel.OutputBean selectedCourierDetails;
    private LocalBroadcastManager localBroadcastManager;
    private GetInfoModel getInfoModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_received_list);

        init();
        getSessionDetails();
        setDefaults();
        setEventListener();
        setUpToolBar();
    }

    private void init() {
        context = CourierReceivedList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        tvDestClientList = findViewById(R.id.tvDestClientList);
        tv_select_lab = findViewById(R.id.tv_select_lab);
        tv_lab = findViewById(R.id.tv_lab);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        rv_courier = findViewById(R.id.rv_courier);
        rv_courier.setLayoutManager(new LinearLayoutManager(context));
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                desgId = json.getString("DESGID");
                labcode = json.getString("MaplabCode");
                labName = json.getString("MaplabName");


            }
            tv_select_lab.setText(labName);
            tv_lab.setText(labName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        Calendar calendar = Calendar.getInstance();

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mYear1 = calendar.get(Calendar.YEAR);
        mMonth1 = calendar.get(Calendar.MONTH);
        mDay1 = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        tv_from_date.setText(mdformat.format(calendar.getTime()));
        tv_to_date.setText(mdformat.format(calendar.getTime()));

//        if (desgId.equals("27") || desgId.equals("36") || desgId.equals("38") || desgId.equals("78") || desgId.equals("11") ||
//                desgId.equals("12") || desgId.equals("18")) {
//            tv_select_lab.setVisibility(View.GONE);
//            tv_lab.setVisibility(View.VISIBLE);
//        } else if (desgId.equals("10") || desgId.equals("23") || desgId.equals("84")) {
//            tv_select_lab.setVisibility(View.VISIBLE);
//            tv_lab.setVisibility(View.GONE);
//        }
        destClientCode = ApplicationConstants.ClientCode;

        //getSentCourierApiCall();

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("CourierReceivedList_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private void setEventListener() {
        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);
        tv_select_lab.setOnClickListener(this);
        tvDestClientList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_from_date:
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        (view, year, monthOfYear, dayOfMonth) -> {
                            tv_to_date.setText("");
                            tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
                break;

            case R.id.tv_to_date:
                if (tv_from_date.getText().toString().equals(""))
                    Utilities.showMessageString("Please Select From Date", context);
                else {
                    DatePickerDialog dpd2 = new DatePickerDialog(context,
                            (view, year, monthOfYear, dayOfMonth) -> {
                                tv_to_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                                getSentCourierApiCall();
                                mYear1 = year;
                                mMonth1 = monthOfYear;
                                mDay1 = dayOfMonth;
                            }, mYear1, mMonth1, mDay1);
                    Calendar c = Calendar.getInstance();
                    c.set(mYear, mMonth, mDay);
                    try {
                        dpd2.getDatePicker().setCalendarViewShown(false);
                        dpd2.getDatePicker().setMinDate(c.getTimeInMillis());
                        dpd2.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dpd2.show();
                }
                break;
            case R.id.tv_select_lab: {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetFromLab().execute(userId);

                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
            case R.id.tvDestClientList: {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetClientlist("0").execute("0");
//                    new GetCenterList().execute("1");
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
        }

    }

    private class GetClientlist extends AsyncTask<String, Void, String> {

        private String TYPE = "";

        public GetClientlist(String TYPE) {
            this.TYPE = TYPE;
        }

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
            Log.d("ClientList Params", Arrays.toString(strings));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("ClientCode", strings[0]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.getClientlist, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            Log.d("ClientList Resp", result);

            try {
                pd.dismiss();
                if (!result.equals("")) {


                    ClientListModel clientListModel = new Gson().fromJson(result, ClientListModel.class);
                    if (clientListModel.getStatus().equalsIgnoreCase("success")) {
                        Collections.sort(clientListModel.getOutput(), (o1, o2) -> o1.getClientName().compareTo(o2.getClientName()));
                        listClientDialog(clientListModel.getOutput(), TYPE);
                    } else
                        Utilities.showAlertDialog(context, status, message, false);

                } else {
                    Utilities.showAlertDialog(context, "Fail", "Empty response", false);

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server Not Responding", false);
                e.printStackTrace();
            }
        }
    }

    private void listClientDialog(List<ClientListModel.Output> mainlist, String type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search_test, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Client");
        builder.setCancelable(false);

        final RecyclerView rv_testlist = view.findViewById(R.id.rv_testlist);
        EditText edt_search = view.findViewById(R.id.edt_search);
        rv_testlist.setLayoutManager(new LinearLayoutManager(context));
        rv_testlist.setAdapter(new ClientListAdapter(mainlist, type));


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rv_testlist.setAdapter(new ClientListAdapter(mainlist, type));
                    return;
                }

                if (mainlist.size() == 0) {
                    rv_testlist.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<ClientListModel.Output> searchedTestList = new ArrayList<>();
                    for (ClientListModel.Output clientDetails : mainlist) {

                        String countryToBeSearched = clientDetails.getClientName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rv_testlist.setAdapter(new ClientListAdapter(searchedTestList, type));
                } else {
                    rv_testlist.setAdapter(new ClientListAdapter(mainlist, type));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setNegativeButton("cancel", (dialog, which) -> {

        });


        labDialog = builder.create();
        labDialog.show();
    }

    private class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.MyViewHolder> {

        private List<ClientListModel.Output> labList;
        private String TYPE;

        public ClientListAdapter(List<ClientListModel.Output> labList, String TYPE) {
            this.labList = labList;
            this.TYPE = TYPE;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_1, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int pos) {
            final int position = holder.getAdapterPosition();

            holder.tv_name.setText(labList.get(position).getClientName());

            holder.tv_name.setOnClickListener(v -> {

                tvDestClientList.setText(labList.get(position).getClientName());
                destClientCode = String.valueOf(labList.get(position).getClientCode());
                getSentCourierApiCall();

                labDialog.dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return labList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_name = view.findViewById(R.id.tv_name);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


    private class GetFromLab extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("UserId", strings[0]));
            // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetFromLab, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetFromLab, ApplicationConstants.webservice_d2d, param);


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status, message;
            try {
                pd.dismiss();
                if (!result.equals("")) {

                    LabListModel labListModel = new Gson().fromJson(result, LabListModel.class);

                    if (labListModel.getStatus().equalsIgnoreCase("success")) {
                        LabListDialog(labListModel.getOutput());
                    } else {
                        Utilities.showAlertDialog(context, labListModel.getStatus(), labListModel.getMessage(), false);
                    }

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server not connected", false);
                e.printStackTrace();
            }
        }
    }

    private void LabListDialog(final List<LabListModel.Output> mainlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select From Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            LabListModel.Output objMain = mainlist.get(which);
            tv_select_lab.setText(objMain.getLabName());
            labcode = String.valueOf(objMain.getLabCode());
            //getSentCourierApiCall();


        });
        builderSingle.show();
    }

    public class GetLBMLabsDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("LMBUserid", strings[0]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetLBMLabsDetails, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status, message;
            try {
                pd.dismiss();
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        List<LBMWiseLab> mainlist = new ArrayList<>();
                        JSONArray jsonarr = mainObj.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {
                                LBMWiseLab summary = new LBMWiseLab();
                                JSONObject jsonObj = jsonarr.getJSONObject(i);
                                summary.setLabcode(jsonObj.getString("labcode"));
                                summary.setLabName(jsonObj.getString("LabName"));
                                summary.setDISTLGDCODE(jsonObj.getString("DISTLGDCODE"));
                                mainlist.add(summary);
                            }
                            listDialogCreater(mainlist);
                        }
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

    private void listDialogCreater(final List<LBMWiseLab> mainlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));
        }

        builderSingle.setNegativeButton(
                "Cancel",
                (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            LBMWiseLab objMain = mainlist.get(which);
            tv_select_lab.setText(objMain.getLabName());
            labcode = objMain.getLabcode();
            getSentCourierApiCall();
        });
        builderSingle.show();
    }

    public class GetDCToLabMapping extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", strings[0]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetDCToLabMapping, param);
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
                    if (status.equalsIgnoreCase("success")) {
                        List<HLLDCWiseLab> mainlist = new ArrayList<HLLDCWiseLab>();
                        JSONArray jsonarr = mainObj.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {
                                HLLDCWiseLab summary = new HLLDCWiseLab();
                                JSONObject jsonObj = jsonarr.getJSONObject(i);
                                summary.setLabCode(jsonObj.getString("LabCode"));
                                summary.setLabName(jsonObj.getString("LabName"));
                                mainlist.add(summary);
                            }

                            Collections.sort(mainlist, new Comparator<HLLDCWiseLab>() {
                                @Override
                                public int compare(HLLDCWiseLab o1, HLLDCWiseLab o2) {
                                    return o1.getLabName().compareTo(o2.getLabName());
                                }
                            });
                            listHLLDCLabDialogCreater(mainlist);
                        }
                    } else if (status.equalsIgnoreCase("fail")) {
                        if (message.equalsIgnoreCase("Lab Not Map For This User ")) {
                            Utilities.showAlertDialog(context, "Success", message, true);
                        } else {
                            Utilities.showAlertDialog(context, status, message, true);
                        }
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail", "Server not connected", false);
                e.printStackTrace();
            }
        }
    }

    private void listHLLDCLabDialogCreater(final List<HLLDCWiseLab> mainlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            HLLDCWiseLab objMain = mainlist.get(which);
            tv_select_lab.setText(objMain.getLabName());
            labcode = objMain.getLabCode();
            getSentCourierApiCall();

        });
        builderSingle.show();

    }

    private void getSentCourierApiCall() {
        if (Utilities.isNetworkAvailable(context)) {
            new GetReceivedCourierDetails().execute(labcode, desgId, userId, tv_to_date.getText().toString().trim(), tv_from_date.getText().toString().trim());
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }

    private class GetReceivedCourierDetails extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();

            param.add(new ParamsPojo("LabCode", params[0]));
            param.add(new ParamsPojo("DESGID", params[1]));
            param.add(new ParamsPojo("USERID", params[2]));
            param.add(new ParamsPojo("TODATE", params[3]));
            param.add(new ParamsPojo("FROMDATE", params[4]));
//            param.add(new ParamsPojo("ToClient", params[5]));
//            param.add(new ParamsPojo("FromClient", params[6]));
            // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierDetailsByReciever, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetCourierDetailsByReciever, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierReceivedDetailsModel pojoDetails = new Gson().fromJson(result, CourierReceivedDetailsModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (type.equalsIgnoreCase("success")) {
                        List<CourierReceivedDetailsModel.OutputBean> receivedCourierArray = pojoDetails.getOutput();
                        if (receivedCourierArray.size() > 0) {
                            rv_courier.setAdapter(new CourierReceivedAdapter(receivedCourierArray, context));
                        } else {
                            rv_courier.setAdapter(new CourierReceivedAdapter(receivedCourierArray, context));
                            Utilities.showAlertDialog(context, "Fail", "Entries not available for selected dates, you can check for another dates", false);
                        }
                    } else {
                        rv_courier.setAdapter(new CourierReceivedAdapter(new ArrayList<>(), context));
                        Utilities.showAlertDialog(context, "Fail", "Entries not available for selected dates, you can check for another dates", false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    EditText edt_barcode;

    private class CourierReceivedAdapter extends RecyclerView.Adapter<CourierReceivedAdapter.MyViewHolder> {

        private Context context;
        private List<CourierReceivedDetailsModel.OutputBean> receivedCourierList;

        public CourierReceivedAdapter(List<CourierReceivedDetailsModel.OutputBean> receivedCourierList, Context context) {
            this.context = context;
            this.receivedCourierList = receivedCourierList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_courier_receive, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            int position = holder.getAbsoluteAdapterPosition();
            CourierReceivedDetailsModel.OutputBean courierDetails = receivedCourierList.get(position);

//            holder.ll_barcode.setVisibility(View.GONE);


            holder.tv_campInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new GetCampInfo().execute(courierDetails.getCourierID());

                }
            });


            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCourierDetails = courierDetails;
                    courierBarCode = courierDetails.getBarcode();
                    startActivity(new Intent(context, CourierAccept2_Activity.class)
                            .putExtra("selectedCourierDetails", selectedCourierDetails));


                }
            });
            //   tv_received_date"

            holder.tv_status.setText(courierDetails.getStatus());
            holder.tv_from_lab.setText(courierDetails.getRegistrationLab());
            holder.tv_process_lab.setText(courierDetails.getProcessLab());
            holder.tv_sent_date.setText(courierDetails.getCourierDate() + "/" + courierDetails.getCourierTime());
            // holder.tv_sent_time.setText(courierDetails.getCourierTime());
            holder.tv_courier_type.setText(courierDetails.getCourierType());
            holder.tv_mode_of_transport.setText(courierDetails.getModeOfTransport());
            holder.tv_sent_by.setText(courierDetails.getSendBy());
//            holder.llBarcode.setVisibility(View.GONE);
            holder.tv_barcode.setText(courierDetails.getBarcode());

            holder.ll_received_datetime.setVisibility(View.GONE);
            if (courierDetails.getStatus().equals("Dispatched")) {
                if (!tv_select_lab.getText().equals(courierDetails.getCourierToLab()))
                    holder.ll_scan_barcode.setVisibility(View.VISIBLE);

                holder.tv_status.setBackgroundColor(getResources().getColor(R.color.orange));
            } else if (courierDetails.getStatus().contains("Sent To")) {
                holder.ll_scan_barcode.setVisibility(View.GONE);
                holder.ll_sent_by.setVisibility(View.GONE);
                holder.imv_call.setVisibility(View.INVISIBLE);
                holder.tv_status.setBackgroundColor(getResources().getColor(R.color.yellow));
            } else if (courierDetails.getStatus().contains("Received At")) {
                holder.ll_scan_barcode.setVisibility(View.GONE);
                holder.ll_sent_by.setVisibility(View.GONE);
                holder.ll_barcode.setVisibility(View.VISIBLE);
                holder.imv_call.setVisibility(View.INVISIBLE);
                holder.llBarcode.setVisibility(View.VISIBLE);
                holder.tv_barcode.setText(courierDetails.getBarcode());
                holder.ll_received_datetime.setVisibility(View.VISIBLE);
                holder.tv_received_date.setText(courierDetails.getReceivedDate() + "/" + courierDetails.getReceivedTime());
                // holder.tv_received_time.setText(courierDetails.getReceivedTime());
                if (courierDetails.getStatus().contains(courierDetails.getProcessLab()))
                    holder.tv_status.setBackgroundColor(getResources().getColor(R.color.green));
                else
                    holder.tv_status.setBackgroundColor(getResources().getColor(R.color.blue));
            }

            holder.imv_call.setOnClickListener(v -> {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Alert");
                alertDialogBuilder.setMessage("Are you sure, You want to make a call?");
                alertDialogBuilder.setCancelable(true);

                alertDialogBuilder.setPositiveButton("Yes", (dialog, id) -> {
                    dialog.cancel();
                    try {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + courierDetails.getContactNo()));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
                alertDialogBuilder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert11 = alertDialogBuilder.create();
                alert11.show();
            });

            holder.imv_barcode_scan.setOnClickListener(view -> {
                selectedCourierDetails = courierDetails;
                courierBarCode = courierDetails.getBarcode();
//                Intent i = new Intent(context, BarcodeScannerZxingActivity.class);
//                startActivityForResult(i, 101);

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.dialog_courier_barcode, null);

                edt_barcode = promptView.findViewById(R.id.edt_barcode);
                edt_barcode.setText(courierBarCode);
                ImageView imv_barcode_scan = promptView.findViewById(R.id.imv_barcode_scan);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setTitle("Courier Barcode");
                alertDialogBuilder.setPositiveButton("Next", null);
                alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                });

                imv_barcode_scan.setOnClickListener(v -> {
                    Intent i = new Intent(context, BarcodeScannerZxingActivity.class);
                    startActivityForResult(i, 101);
                });

                final AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
                Button collectBtn = alertD.getButton(DialogInterface.BUTTON_POSITIVE);
                collectBtn.setOnClickListener(v -> {
                    if (edt_barcode.getText().toString().trim().equals("")) {
                        edt_barcode.setError("Please enter barcode");
                        return;
                    }

                    if (edt_barcode.getText().toString().trim().equals(courierBarCode)) {
                        startActivity(new Intent(context, CourierAccept1_Activity.class)
                                .putExtra("selectedCourierDetails", selectedCourierDetails));
                        alertD.dismiss();
                    } else
                        Utilities.showMessageString("Invalid barcode", context);
                });


            });
        }

        @Override
        public int getItemCount() {
            return receivedCourierList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_status, tv_from_lab, tv_process_lab, tv_sent_date, tv_sent_time, tv_campInfo, tv_courier_type, tv_mode_of_transport,
                    tv_sent_by, tv_barcode, tv_received_date, tv_received_time;
            private ImageView imv_call, imv_barcode_scan;
            private LinearLayout ll_scan_barcode, ll_sent_by, ll_barcode, ll_received_datetime, llBarcode, llMain;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_status = itemView.findViewById(R.id.tv_status);
                tv_from_lab = itemView.findViewById(R.id.tv_from_lab);
                tv_process_lab = itemView.findViewById(R.id.tv_process_lab);
                tv_sent_date = itemView.findViewById(R.id.tv_sent_date);
                //  tv_sent_time = itemView.findViewById(R.id.tv_sent_time);
                tv_courier_type = itemView.findViewById(R.id.tv_courier_type);
                tv_mode_of_transport = itemView.findViewById(R.id.tv_mode_of_transport);
                tv_sent_by = itemView.findViewById(R.id.tv_sent_by);
                tv_barcode = itemView.findViewById(R.id.tv_barcode);
                imv_call = itemView.findViewById(R.id.imv_call);
                imv_barcode_scan = itemView.findViewById(R.id.imv_barcode_scan);
                ll_scan_barcode = itemView.findViewById(R.id.ll_scan_barcode);
                ll_sent_by = itemView.findViewById(R.id.ll_sent_by);
                ll_barcode = itemView.findViewById(R.id.ll_barcode);
                tv_received_date = itemView.findViewById(R.id.tv_received_date);
                // tv_received_time = itemView.findViewById(R.id.tv_received_time);
                ll_received_datetime = itemView.findViewById(R.id.ll_received_datetime);
                llBarcode = itemView.findViewById(R.id.llBarcode);
                llMain = itemView.findViewById(R.id.llMain);
                tv_campInfo = itemView.findViewById(R.id.tv_campInfo);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                String requiredValue = data.getStringExtra("key");
                edt_barcode.setText(requiredValue);
            }
        }
    }


    private void setUpToolBar() {
        ImageButton back_btn = (ImageButton) findViewById(R.id.tool_leftbtn);
        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        TextView Title = (TextView) findViewById(R.id.tool_titile);
        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Receive Courier");
        back_btn.setOnClickListener(view -> finish());

        btn_save_accordian.setVisibility(View.GONE);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getSentCourierApiCall();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private class GetCampInfo extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("CourierID", strings[0]));
            // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetFromLab, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetCourierCampDetailsByCourierID, ApplicationConstants.webservice_d2d, param);


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", "onPostExecute: " + result);
            String status, message;
            try {
                pd.dismiss();
                if (!result.equals("")) {

                    getInfoModel = new Gson().fromJson(result, GetInfoModel.class);

                    if (getInfoModel.getStatus().equalsIgnoreCase("success")) {

                        if (getInfoModel.getOutput().size() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_dialogue, null, false);
                            builder.setView(v);
                            builder.setTitle("Camp Info");
                            builder.setIcon(R.drawable.icon_campcreation);
                            RecyclerView recyclerView = v.findViewById(R.id.rvList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setHasFixedSize(false);
                            CampInfoListAdapter campInfoListAdapter = new CampInfoListAdapter(getInfoModel.getOutput());
                            recyclerView.setAdapter(campInfoListAdapter);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            alertDialog.show();
                        }


                    } else {
                        Utilities.showAlertDialog(context, getInfoModel.getStatus(), getInfoModel.getMessage(), false);
                    }

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server not connected", false);
                e.printStackTrace();
            }
        }
    }


}