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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CourierSentList_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private TextView tv_select_lab, tv_lab, tv_from_date, tv_to_date;
    private RecyclerView rv_courier;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private String userId, desgId, labcode, labName;
    private LocalBroadcastManager localBroadcastManager;
    private GetInfoModel getInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_sent_list);

        init();
        getSessionDetails();
        setDefaults();
        setEventListener();
        setUpToolBar();
    }

    private void init() {
        context = CourierSentList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

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
                labName = json.getString("LabName");
             //   labcode = json.getString("MaplabCode");
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

      //  getSentCourierApiCall();

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("CourierSentList_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private void setEventListener() {
        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);
        tv_select_lab.setOnClickListener(this);
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
                               // getSentCourierApiCall();
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
                    new GetFromLab().execute(tv_from_date.getText().toString(),tv_to_date.getText().toString());

                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
            break;
        }

    }

    private class GetFromLab extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("FromDate", strings[0]));
            param.add(new ParamsPojo("ToDate", strings[1]));
           // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetFromLab, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetSentToLabList, ApplicationConstants.webservice_d2d, param);

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
            getSentCourierApiCall();


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
           // new GetCourierDetailsBySender().execute(labcode, desgId, userId, tv_to_date.getText().toString().trim(), tv_from_date.getText().toString().trim());
            new GetCourierDetailsBySender().execute( tv_from_date.getText().toString().trim(),tv_to_date.getText().toString().trim(),userId, desgId,labcode);

        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }

    private class GetCourierDetailsBySender extends AsyncTask<String, Integer, String> {
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

            param.add(new ParamsPojo("FromDate", params[0]));
            param.add(new ParamsPojo("ToDate", params[1]));
            param.add(new ParamsPojo("USERID", params[2]));
            param.add(new ParamsPojo("DESGID", params[3]));
            param.add(new ParamsPojo("LabCode", params[4]));
          //  res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierDetailsBySender, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetSentCourierDetails, ApplicationConstants.webservice_d2d, param);
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
                            rv_courier.setAdapter(new CourierSentAdapter(
                                    receivedCourierArray,
                                    context,
                                    userId,
                                    desgId,
                                    labcode,
                                    tv_from_date.getText().toString().trim(),
                                    tv_to_date.getText().toString().trim()));
                        } else {
                            rv_courier.setAdapter(new CourierSentAdapter(
                                    receivedCourierArray,
                                    context,
                                    userId,
                                    desgId,
                                    labcode,
                                    tv_from_date.getText().toString().trim(),
                                    tv_to_date.getText().toString().trim()));
                            Utilities.showAlertDialog(context, "Fail", "Entries not available for selected dates, you can check for another dates", false);
                        }
                    } else {
                        rv_courier.setAdapter(new CourierSentAdapter(
                                new ArrayList<>(),
                                context,
                                userId,
                                desgId,
                                labcode,
                                tv_from_date.getText().toString().trim(),
                                tv_to_date.getText().toString().trim()));
                        Utilities.showAlertDialog(context, "Fail", "Entries not available for selected dates, you can check for another dates", false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private class CourierSentAdapter extends RecyclerView.Adapter<CourierSentAdapter.MyViewHolder> {

        private Context context;
        private List<CourierReceivedDetailsModel.OutputBean> receivedCourierList;
        private String userId, desgId, labcode, fromDate, toDate;

        public CourierSentAdapter(List<CourierReceivedDetailsModel.OutputBean> receivedCourierList, Context context, String userId, String desgId, String labcode, String fromDate, String toDate) {
            this.context = context;
            this.receivedCourierList = receivedCourierList;
            this.userId = userId;
            this.desgId = desgId;
            this.labcode = labcode;
            this.fromDate = fromDate;
            this.toDate = toDate;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_courier_sent, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();
            CourierReceivedDetailsModel.OutputBean courierDetails = receivedCourierList.get(position);

            holder.tv_status.setText(courierDetails.getStatus());
            holder.tv_from_lab.setText(courierDetails.getCourierToLab());
            holder.tv_process_lab.setText(courierDetails.getProcessLab());
            holder.tv_sent_date.setText(courierDetails.getCourierDate() + "/" + courierDetails.getCourierTime());
           // holder.tv_sent_time.setText(courierDetails.getCourierTime());
            holder.tv_courier_type.setText(courierDetails.getCourierType());
            holder.tv_mode_of_transport.setText(courierDetails.getModeOfTransport());
            holder.tv_barcode.setText(courierDetails.getBarcode());


            holder.tv_CampInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new GetCampInfo().execute(courierDetails.getCourierID());

                }
            });






            holder.ll_received_datetime.setVisibility(View.GONE);

            if (courierDetails.getStatus().equals("Dispatched")) {
                holder.tv_status.setBackgroundColor(getResources().getColor(R.color.orange));
            } else if (courierDetails.getStatus().contains("Sent To")) {
                holder.tv_status.setBackgroundColor(getResources().getColor(R.color.yellow));
            } else if (courierDetails.getStatus().contains("Sent To")) {
                holder.tv_status.setBackgroundColor(getResources().getColor(R.color.yellow));
            } else if (courierDetails.getStatus().contains("Received At")) {
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
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + courierDetails.getContactNo()));
                    startActivity(intent);
                });
                alertDialogBuilder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert11 = alertDialogBuilder.create();
                alert11.show();
            });

        }

        @Override
        public int getItemCount() {
            return receivedCourierList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_status, tv_from_lab, tv_process_lab, tv_courier_type, tv_barcode,tv_CampInfo,
                    tv_mode_of_transport, tv_sent_date, tv_sent_time, tv_received_date, tv_received_time;
            private LinearLayout ll_received_datetime;
            private ImageView imv_call;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_status = itemView.findViewById(R.id.tv_status);
                tv_from_lab = itemView.findViewById(R.id.tv_from_lab);
                tv_process_lab = itemView.findViewById(R.id.tv_process_lab);
                tv_sent_date = itemView.findViewById(R.id.tv_sent_date);
               // tv_sent_time = itemView.findViewById(R.id.tv_sent_date);
                tv_courier_type = itemView.findViewById(R.id.tv_courier_type);
                tv_mode_of_transport = itemView.findViewById(R.id.tv_mode_of_transport);
                tv_barcode = itemView.findViewById(R.id.tv_barcode);
                tv_received_date = itemView.findViewById(R.id.tv_received_date);
               // tv_received_time = itemView.findViewById(R.id.tv_received_time);
                ll_received_datetime = itemView.findViewById(R.id.ll_received_datetime);
                imv_call = itemView.findViewById(R.id.imv_call);
                tv_CampInfo = itemView.findViewById(R.id.tv_CampInfo);


            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    private void setUpToolBar() {
        ImageButton back_btn = findViewById(R.id.tool_leftbtn);
        ImageButton btn_save_accordian = findViewById(R.id.btn_save_accordian);
        TextView Title = findViewById(R.id.tool_titile);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Sent Courier");
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