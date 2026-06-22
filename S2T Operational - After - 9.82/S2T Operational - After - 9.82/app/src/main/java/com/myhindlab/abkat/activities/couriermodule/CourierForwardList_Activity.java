package com.myhindlab.abkat.activities.couriermodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.CenterListPojo;
import com.myhindlab.abkat.models.couriermodule.CourierForwardedModel;
import com.myhindlab.abkat.models.couriermodule.HLLDCWiseLab;
import com.myhindlab.abkat.models.couriermodule.LBMWiseLab;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CourierForwardList_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private TextView tv_select_from_lab, tv_from_lab, tv_select_to_lab, tv_from_date, tv_to_date;
    private RecyclerView rv_courier;
    private Button btn_submit;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private String userId, desgId, fromLabCode, fromLabName, toLabCode;
    private List<CourierForwardedModel.OutputBean> forwardCourierList;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_forward_list);

        init();
        getSessionDetails();
        setDefaults();
        setEventListener();
        setUpToolBar();
    }

    private void init() {
        context = CourierForwardList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        tv_select_from_lab = findViewById(R.id.tv_select_from_lab);
        tv_from_lab = findViewById(R.id.tv_from_lab);
        tv_select_to_lab = findViewById(R.id.tv_select_to_lab);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        rv_courier = findViewById(R.id.rv_courier);
        rv_courier.setLayoutManager(new LinearLayoutManager(context));
        btn_submit = findViewById(R.id.btn_submit);
        forwardCourierList = new ArrayList<>();
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                desgId = json.getString("DESGID");
                fromLabName = json.getString("LabName");
                fromLabCode = json.getString("MaplabCode");
            }
            tv_select_from_lab.setText(fromLabName);
            tv_from_lab.setText(fromLabName);

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

        if (desgId.equals("27") || desgId.equals("36") || desgId.equals("38") || desgId.equals("78") || desgId.equals("11") ||
                desgId.equals("12") || desgId.equals("18")) {
            tv_select_from_lab.setVisibility(View.GONE);
            tv_from_lab.setVisibility(View.VISIBLE);
        } else if (desgId.equals("10") || desgId.equals("23") || desgId.equals("84")) {
            tv_select_from_lab.setVisibility(View.VISIBLE);
            tv_from_lab.setVisibility(View.GONE);
        }

//        getForwardCourierApiCall();

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("CourierForwardList_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private void setEventListener() {
        tv_select_from_lab.setOnClickListener(this);
        tv_select_to_lab.setOnClickListener(this);
        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
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
                                getForwardCourierApiCall();
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
            case R.id.tv_select_from_lab: {
                if (Utilities.isNetworkAvailable(context)) {
                    if (desgId.equals("23")) {
                        new GetLBMLabsDetails().execute(userId);
                    } else if (desgId.equals("10") || desgId.equals("84")) {
                        new GetDCToLabMapping().execute(userId);
                    }
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
            case R.id.tv_select_to_lab: {
                if (tv_select_from_lab.getText().toString().trim().equals("")) {
                    Utilities.showMessageString("Please select forward from lab", context);
                }

                if (Utilities.isNetworkAvailable(context)) {
                    new GetCenterList().execute();
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
            case R.id.btn_submit: {
                submitData();
            }
            break;
        }

    }

    private void submitData() {
        StringBuilder courierIdsSb = new StringBuilder();
        List<CourierForwardedModel.OutputBean> selectedCouriersHavingTubeCountsList = new ArrayList<>();
        List<CourierForwardedModel.OutputBean> selectedCouriersList = new ArrayList<>();
        for (int i = 0; i < forwardCourierList.size(); i++) {
            if (forwardCourierList.get(i).isChecked()) {
                selectedCouriersList.add(forwardCourierList.get(i));
                courierIdsSb.append(forwardCourierList.get(i).getCourierID() + ",");
                if (forwardCourierList.get(i).getIsSampleType().equals("1"))
                    if (forwardCourierList.get(i).getTubeDetails() != null)
                        selectedCouriersHavingTubeCountsList.add(forwardCourierList.get(i));
            }
        }

        String courierIds = courierIdsSb.toString();
        if (courierIds.equals("")) {
            Utilities.showMessageString("Please select at least one courier", context);
        } else {
            courierIds = courierIds.substring(0, courierIds.length() - 1);
            startActivity(new Intent(context, CourierSendStep1_Activity.class)
                    .putExtra("forwardedCourierIds", courierIds)
                    .putExtra("fromLabCode", fromLabCode)
                    .putExtra("fromLabName", tv_select_from_lab.getText().toString())
                    .putExtra("processingLabCode", toLabCode)
                    .putExtra("processingLabName", tv_select_to_lab.getText().toString())
                    .putExtra("selectedCouriersHavingTubeCountsList", (Serializable) selectedCouriersHavingTubeCountsList)
                    .putExtra("selectedCouriersList", (Serializable) selectedCouriersList)
            );
        }
    }

    private class GetLBMLabsDetails extends AsyncTask<String, Void, String> {

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
                            listLBMDialogCreater(mainlist);
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

    private void listLBMDialogCreater(final List<LBMWiseLab> mainlist) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Lab");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row_city_state);

        for (int i = 0; i < mainlist.size(); i++) {
            arrayAdapter.add(String.valueOf(mainlist.get(i).getLabName()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            LBMWiseLab objMain = mainlist.get(which);
            tv_select_from_lab.setText(objMain.getLabName());
            fromLabCode = objMain.getLabcode();
            tv_select_to_lab.setText("");
            fromLabName = objMain.getLabName();
        });
        builderSingle.show();
    }

    private class GetDCToLabMapping extends AsyncTask<String, Void, String> {

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
            tv_select_from_lab.setText(objMain.getLabName());
            fromLabCode = objMain.getLabCode();
            tv_select_to_lab.setText("");
            fromLabName = objMain.getLabName();
        });
        builderSingle.show();

    }

    private class GetCenterList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserID", userId));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.getCenterList, param);
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
                        List<CenterListPojo> mainlist = new ArrayList<CenterListPojo>();
                        JSONArray jsonarr = mainObj.getJSONArray("output");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < jsonarr.length(); i++) {
                                CenterListPojo summary = new CenterListPojo();
                                JSONObject jsonObj = jsonarr.getJSONObject(i);
                                summary.setCenterId(jsonObj.getString("centerId"));
                                summary.setCenterName(jsonObj.getString("centername"));
                                if (!summary.getCenterId().equals(fromLabCode))
                                    mainlist.add(summary);
                            }

                            Collections.sort(mainlist, (o1, o2) -> o1.getCenterName().compareTo(o2.getCenterName()));

                            listLabDialogCreater(mainlist);
                        }
                    } else if (status.equalsIgnoreCase("fail")) {
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server Not Responding", false);
                e.printStackTrace();
            }
        }
    }

    AlertDialog labDialog;

    private void listLabDialogCreater(List<CenterListPojo> mainlist) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search_test, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Select Lab");
        builder.setCancelable(false);

        final RecyclerView rv_testlist = view.findViewById(R.id.rv_testlist);
        EditText edt_search = view.findViewById(R.id.edt_search);
        rv_testlist.setLayoutManager(new LinearLayoutManager(context));
        rv_testlist.setAdapter(new LabAdapter(mainlist));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rv_testlist.setAdapter(new LabAdapter(mainlist));
                    return;
                }

                if (mainlist.size() == 0) {
                    rv_testlist.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<CenterListPojo> searchedTestList = new ArrayList<>();
                    for (CenterListPojo labDetails : mainlist) {

                        String countryToBeSearched = labDetails.getCenterName().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(labDetails);
                        }
                    }
                    rv_testlist.setAdapter(new LabAdapter(searchedTestList));
                } else {
                    rv_testlist.setAdapter(new LabAdapter(mainlist));
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

    private class LabAdapter extends RecyclerView.Adapter<LabAdapter.MyViewHolder> {

        private List<CenterListPojo> labList;

        public LabAdapter(List<CenterListPojo> labList) {
            this.labList = labList;
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

            holder.tv_name.setText(labList.get(position).getCenterName());

            holder.tv_name.setOnClickListener(v -> {

                tv_select_to_lab.setText(labList.get(position).getCenterName());
                toLabCode = labList.get(position).getCenterId();
                getForwardCourierApiCall();
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

    private class GetForwardCourierDetails extends AsyncTask<String, Integer, String> {
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

            param.add(new ParamsPojo("FromLabCode", params[0]));
            param.add(new ParamsPojo("ToLabCode", params[1]));
            param.add(new ParamsPojo("TODATE", params[2]));
            param.add(new ParamsPojo("FROMDATE", params[3]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetForwardCourierDetails, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CourierForwardedModel pojoDetails = new Gson().fromJson(result, CourierForwardedModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (type.equalsIgnoreCase("success")) {
                        forwardCourierList = pojoDetails.getOutput();
                        if (forwardCourierList.size() > 0) {
                            rv_courier.setAdapter(new CourierForwardedAdapter());
                        } else {
                            forwardCourierList = new ArrayList<>();
                            rv_courier.setAdapter(new CourierForwardedAdapter());
                            Utilities.showAlertDialog(context, "Fail", message, false);
                        }
                    } else {
                        forwardCourierList = new ArrayList<>();
                        rv_courier.setAdapter(new CourierForwardedAdapter());
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private class CourierForwardedAdapter extends RecyclerView.Adapter<CourierForwardedAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_courier_forward, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();
            CourierForwardedModel.OutputBean courierDetails = forwardCourierList.get(position);

            holder.tv_status.setText(courierDetails.getStatus());
            holder.tv_from_lab.setText(courierDetails.getRegistrationLab());
            holder.tv_process_lab.setText(courierDetails.getProcessLab());
            holder.tv_barcode.setText(courierDetails.getBarcode());
            holder.cb_checked.setVisibility(View.GONE);

            if (courierDetails.getStatus().contains("Sent To")) {
                if (courierDetails.getStatus().contains(fromLabName)) {
                    holder.cb_checked.setVisibility(View.VISIBLE);
                    holder.tv_status.setBackgroundColor(getResources().getColor(R.color.orange));
                } else
                    holder.tv_status.setBackgroundColor(getResources().getColor(R.color.yellow));
            } else if (courierDetails.getStatus().contains("Received At")) {
                if (courierDetails.getStatus().contains(fromLabName)) {
                    holder.cb_checked.setVisibility(View.VISIBLE);
                    holder.tv_status.setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (courierDetails.getStatus().contains(courierDetails.getProcessLab()))
                    holder.tv_status.setBackgroundColor(getResources().getColor(R.color.green));
                else
                    holder.tv_status.setBackgroundColor(getResources().getColor(R.color.blue));
            }

            if (courierDetails.isChecked()) {
                holder.cb_checked.setChecked(true);
            } else {
                holder.cb_checked.setChecked(false);
            }

            holder.cb_checked.setOnClickListener(v -> {
                forwardCourierList.get(position).setChecked(holder.cb_checked.isChecked());
            });

        }

        @Override
        public int getItemCount() {
            return forwardCourierList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_status, tv_from_lab, tv_process_lab, tv_barcode;
            private CheckBox cb_checked;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_status = itemView.findViewById(R.id.tv_status);
                tv_from_lab = itemView.findViewById(R.id.tv_from_lab);
                tv_process_lab = itemView.findViewById(R.id.tv_process_lab);
                tv_barcode = itemView.findViewById(R.id.tv_barcode);
                cb_checked = itemView.findViewById(R.id.cb_checked);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private void getForwardCourierApiCall() {
        if (Utilities.isNetworkAvailable(context)) {
            new GetForwardCourierDetails().execute(fromLabCode, toLabCode, tv_to_date.getText().toString().trim(), tv_from_date.getText().toString().trim());
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }

    private void setUpToolBar() {
        ImageButton back_btn = (ImageButton) findViewById(R.id.tool_leftbtn);
        TextView Title = (TextView) findViewById(R.id.tool_titile);
        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Forward Courier");
        back_btn.setOnClickListener(view -> finish());
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getForwardCourierApiCall();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}