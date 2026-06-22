package com.myhindlab.abkat.activities.couriermodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.couriermodule.SentCourierAdapter;
import com.myhindlab.abkat.models.couriermodule.HLLDCWiseLab;
import com.myhindlab.abkat.models.couriermodule.LBMWiseLab;
import com.myhindlab.abkat.models.couriermodule.ReceivedCourierModel;
import com.myhindlab.abkat.models.couriermodule.ReceivedCourierPojo;
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

public class SentCourier_Activity extends Activity implements View.OnClickListener {
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rv_courierList;
    Context context;
    UserSessionManager session;
    String userId, labcode, desgId, labName;
    private ArrayList<ReceivedCourierModel> sentCourierArray;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private ProgressDialog progressDialog;
    private TextView txt_filterFromdate, txt_filterTodate, tv_select_lab, tv_lab;
    List<LBMWiseLab> mainlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_courier);
        init();
        setEventHandler();
        setUpToolBar();
        setDefault();
    }

    private void init() {
        context = SentCourier_Activity.this;
        progressDialog = new ProgressDialog(context);
        session = new UserSessionManager(context);
        mLayoutManager = new LinearLayoutManager(context);
        rv_courierList = findViewById(R.id.list);
        txt_filterFromdate = findViewById(R.id.txt_filterFromdate);
        txt_filterTodate = findViewById(R.id.txt_filterTodate);
        tv_select_lab = findViewById(R.id.tv_select_lab);
        tv_lab = findViewById(R.id.tv_lab);
        rv_courierList.setLayoutManager(mLayoutManager);
    }

    private void setDefault() {

        Calendar calendar = Calendar.getInstance();

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mYear1 = calendar.get(Calendar.YEAR);
        mMonth1 = calendar.get(Calendar.MONTH);
        mDay1 = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        txt_filterFromdate.setText(mdformat.format(calendar.getTime()));
        txt_filterTodate.setText(mdformat.format(calendar.getTime()));
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                labcode = json.getString("MaplabCode");
                desgId = json.getString("DESGID");
                labName = json.getString("LabName");
            }

            tv_select_lab.setText(labName);
            tv_lab.setText(labName);
            getReceivedCourierApiCall();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (desgId.equals("27") || desgId.equals("36") || desgId.equals("38") || desgId.equals("78") || desgId.equals("11") ||
                desgId.equals("12") || desgId.equals("18")) {
            tv_select_lab.setVisibility(View.GONE);
            tv_lab.setVisibility(View.VISIBLE);
        } else if (desgId.equals("10") || desgId.equals("23")) {
            tv_select_lab.setVisibility(View.VISIBLE);
            tv_lab.setVisibility(View.GONE);
        }
    }

    private void getReceivedCourierApiCall() {
        if (Utilities.isNetworkAvailable(context)) {
            new GetSentCourierDetails().execute(
                    txt_filterFromdate.getText().toString().trim(),
                    txt_filterTodate.getText().toString().trim(), userId,
                    desgId,
                    labcode);
        } else
            Utilities.showMessage(R.string.msg_nointernetconnection, context);
    }

    private void setEventHandler() {
        txt_filterFromdate.setOnClickListener(this);
        txt_filterTodate.setOnClickListener(this);
        tv_select_lab.setOnClickListener(this);
    }

    private void setUpToolBar() {
        ImageButton back_btn = (ImageButton) findViewById(R.id.tool_leftbtn);
        TextView Title = (TextView) findViewById(R.id.tool_titile);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Sent Courier");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_filterFromdate:
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txt_filterTodate.setText("");
                                txt_filterFromdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
                break;

            case R.id.txt_filterTodate:
                if (txt_filterFromdate.getText().toString().equals(""))
                    Utilities.showMessageString("Please Select From Date", context);
                else {
                    DatePickerDialog dpd2 = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    txt_filterTodate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                    getReceivedCourierApiCall();
                                    mYear1 = year;
                                    mMonth1 = monthOfYear;
                                    mDay1 = dayOfMonth;
                                }
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
                    if (desgId.equals("23")) {
                        new GetLBMLabsDetails().execute(userId);
                    } else if (desgId.equals("10")) {
                        new GetDCToLabMapping().execute(userId);
                    }
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
        }
    }

    public class GetLBMLabsDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
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
                progressDialog.dismiss();
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    status = mainObj.getString("status");
                    message = mainObj.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        mainlist = new ArrayList<>();
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
            getReceivedCourierApiCall();
        });
        builderSingle.show();
    }

    public class GetDCToLabMapping extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
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
                progressDialog.dismiss();
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
                Utilities.showAlertDialog(context, "Fail",
                        "Server not connected", false);
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

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                HLLDCWiseLab objMain = mainlist.get(which);
                tv_select_lab.setText(objMain.getLabName());
                labcode = objMain.getLabCode();
                getReceivedCourierApiCall();

            }
        });
        builderSingle.show();

    }

    public class GetSentCourierDetails extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
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
            param.add(new ParamsPojo("UserID", params[2]));
            param.add(new ParamsPojo("DESGID", params[3]));
            param.add(new ParamsPojo("LabCode", params[4]));
            res = WebServiceCall.HLLAPICall(ApplicationConstants.GetSentCourierDetails, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    sentCourierArray = new ArrayList<>();
                    ReceivedCourierPojo pojoDetails = new Gson().fromJson(result, ReceivedCourierPojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (type.equalsIgnoreCase("success")) {
                        sentCourierArray = pojoDetails.getOutput();
                        if (sentCourierArray.size() > 0) {
                            rv_courierList.setAdapter(new SentCourierAdapter(sentCourierArray, context));
                        } else {
                            Utilities.showAlertDialog(context, "Fail", "Sent Courier Details Not Found", false);
                            rv_courierList.setAdapter(new SentCourierAdapter(sentCourierArray, context));
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", "Sent Courier Details Not Found", false);
                        rv_courierList.setAdapter(new SentCourierAdapter(sentCourierArray, context));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }
}
