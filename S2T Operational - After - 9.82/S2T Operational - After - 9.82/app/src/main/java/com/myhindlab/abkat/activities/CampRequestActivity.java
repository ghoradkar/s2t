package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CSCCampRequestListAdapter;
import com.myhindlab.abkat.models.CSCPreCampInfoForApprovalModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CampRequestActivity extends AppCompatActivity implements CSCCampRequestListAdapter.UpdateCamp {

    private RecyclerView recyclerView;
    private CSCPreCampInfoForApprovalModel cscPreCampInfoForApprovalModelList;
    private List<CSCPreCampInfoForApprovalModel.Output> filterCscPreCampInfoForApprovalModelList;
    private Context context;
    private AppCompatSpinner appCompatSpinner;
    private TextInputEditText edtFromDate, edtToDate;
    private String FromDate, ToDate;
    private String userId, userName, distLgdCode, labCode, labCodeOne, labCodeTwo, hospitalId, imagePath = "", labCodeThree, designName, designId, DesgLevelId, selTaluka, selGp;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_request);

        initView();
        setUpToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSessionDetails();

    }

    private void apiCall() {
        if (Utilities.isNetworkAvailable(context)) {
//            new GetRequestedCamp().execute(distLgdCode, edtFromDate.getText().toString(), edtToDate.getText().toString());
            new GETCSCPreCampInfoForApprovalListForDivisionalManager().execute(distLgdCode, edtFromDate.getText().toString(), edtToDate.getText().toString());
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }
    }

    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(new UserSessionManager(context).getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
                DesgLevelId = json.getString("DESGLEVELID");
                designId = json.getString("DESGID");
                designName = json.getString("Designation");
                userName = json.getString("name");
                apiCall();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Request");
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView() {

        context = CampRequestActivity.this;
        recyclerView = findViewById(R.id.rcv);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        appCompatSpinner = findViewById(R.id.edtCampStatus);
        appCompatSpinner.setSelection(0);

        textView = findViewById(R.id.tvError);

        edtFromDate = findViewById(R.id.edtFromDate);
        edtToDate = findViewById(R.id.edtToDate);
        appCompatSpinner.setSelection(0);

        Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);


        FromDate = mYear + "-" + new DecimalFormat("00").format((mMonth + 1)) + "-" + new DecimalFormat("00").format((mDay));
//        c.add(Calendar.DATE, 2);  // number of days to add

        ToDate = mYear + "-" + new DecimalFormat("00").format((mMonth + 1)) + "-" + new DecimalFormat("00").format((mDay));

        edtFromDate.setText(FromDate);
        edtToDate.setText(ToDate);

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                FromDate=year + "-"
                                        + new DecimalFormat("00").format((monthOfYear + 1))
                                        + "-" + new DecimalFormat("00").format(dayOfMonth);
                                edtFromDate.setText(FromDate);

//                                FromDate = year + "-" + monthOfYear + 1 + "-" + dayOfMonth;
                                ToDate = "";
                                edtToDate.setText(ToDate);
//                                getSessionDetails();
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.show();
            }
        });

//        String string_date = "12-December-2012";


        edtToDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                long milliseconds = System.currentTimeMillis();
                if (!FromDate.isEmpty()) {
                    try {
                        Date d = f.parse(FromDate);
                        milliseconds = d.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ToDate = year + "-"
                                        + new DecimalFormat("00").format((monthOfYear + 1))
                                        + "-" + new DecimalFormat("00").format(dayOfMonth);
                                edtToDate.setText(ToDate);

                                getSessionDetails();

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(milliseconds);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.show();
            }
        });

        edtFromDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (edtToDate.length() != 0) {
                        if (!Utilities.CheckDates(edtFromDate.getText().toString().trim(),
                                edtToDate.getText().toString().trim())) {
                            edtToDate.setText("");
                            ToDate = "";
                            Utilities.showMessageString("Starting date can not be greater then end date.",
                                    context);
                        }
                    } else
                        Utilities.showMessageString("Please select to date", context);
                } else
                    Utilities.showMessageString("Please select from date", context);
            }
        });

        edtToDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (edtFromDate.length() != 0) {
                        if (!Utilities.CheckDates(edtFromDate.getText().toString().trim(),
                                edtToDate.getText().toString().trim())) {
                            edtToDate.setText("");
                            ToDate = "";
                            Utilities.showMessageString("End date can not be less then starting date.",
                                    context);
                        }
                    } else
                        Utilities.showMessageString("Please select from date", context);
                } else
                    Utilities.showMessageString("Please select to date", context);
            }
        });
//        if (cscPreCampInfoForApprovalModelList != null) {
//
//        }

    }


    private class GetRequestedCamp extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            List<ParamsPojo> paramsPojos = new ArrayList<>();
            paramsPojos.add(new ParamsPojo("DISTLGDCODE", params[0]));
            paramsPojos.add(new ParamsPojo("GP_ID", "0"));
            paramsPojos.add(new ParamsPojo("TALLGDCODE", "0"));
            paramsPojos.add(new ParamsPojo("FromDate", params[1]));
            paramsPojos.add(new ParamsPojo("TODate", params[2]));
           // res = WebServiceCall.APICall(ApplicationConstants.GETCSCPreCampInfoForApproval, ApplicationConstants.webservice, paramsPojos);
            res = WebServiceCall.APICall(ApplicationConstants.GETCSCPreCampInfoForApproval_V1, ApplicationConstants.webservice_d2d, paramsPojos);

            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("GETCSCPreCampInfo", "onPostExecute: " + result);
            progressDialog.dismiss();

            if (!result.equals("") || !result.equals("[]")) {

                try {
                    cscPreCampInfoForApprovalModelList = new CSCPreCampInfoForApprovalModel();
                    filterCscPreCampInfoForApprovalModelList = new ArrayList<CSCPreCampInfoForApprovalModel.Output>();
                    cscPreCampInfoForApprovalModelList = new Gson().fromJson(result, CSCPreCampInfoForApprovalModel.class);

                    if (cscPreCampInfoForApprovalModelList.getStatus().equalsIgnoreCase("Success")) {

                        if (cscPreCampInfoForApprovalModelList.getOutput().size() > 0) {
                            recyclerView.setAdapter(new CSCCampRequestListAdapter(context, cscPreCampInfoForApprovalModelList.getOutput(), CampRequestActivity.this::onUpdateCamp));
                            textView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    Log.d("TAG", "onItemSelected: " + i);
                                    if (i == 1) {
                                        List<CSCPreCampInfoForApprovalModel.Output> outputs = new ArrayList<>();

                                        for (CSCPreCampInfoForApprovalModel.Output cs : cscPreCampInfoForApprovalModelList.getOutput()) {

                                            if (cs.getCampStatus() == 0) {
                                                outputs.add(cs);
                                            }

                                        }
                                        filterCscPreCampInfoForApprovalModelList = null;
                                        filterCscPreCampInfoForApprovalModelList = outputs;
                                        recyclerView.setAdapter(new CSCCampRequestListAdapter(context, filterCscPreCampInfoForApprovalModelList, CampRequestActivity.this::onUpdateCamp));

                                    } else if (i == 2) {
                                        List<CSCPreCampInfoForApprovalModel.Output> outputs = new ArrayList<>();

                                        for (CSCPreCampInfoForApprovalModel.Output cs : cscPreCampInfoForApprovalModelList.getOutput()) {

                                            if (cs.getCampStatus() == 2) {
                                                outputs.add(cs);
                                            }
                                        }
                                        filterCscPreCampInfoForApprovalModelList = null;

                                        filterCscPreCampInfoForApprovalModelList = outputs;
                                        recyclerView.setAdapter(new CSCCampRequestListAdapter(context, filterCscPreCampInfoForApprovalModelList, CampRequestActivity.this::onUpdateCamp));

                                    } else if (i == 3) {
                                        List<CSCPreCampInfoForApprovalModel.Output> outputs = new ArrayList<>();

                                        for (CSCPreCampInfoForApprovalModel.Output cs : cscPreCampInfoForApprovalModelList.getOutput()) {

                                            if (cs.getCampStatus() == 1) {
                                                outputs.add(cs);
                                            }
                                        }
                                        filterCscPreCampInfoForApprovalModelList = null;

                                        filterCscPreCampInfoForApprovalModelList = outputs;
                                        recyclerView.setAdapter(new CSCCampRequestListAdapter(context, filterCscPreCampInfoForApprovalModelList, CampRequestActivity.this::onUpdateCamp));

                                    } else {
                                        recyclerView.setAdapter(new CSCCampRequestListAdapter(context, cscPreCampInfoForApprovalModelList.getOutput(), CampRequestActivity.this::onUpdateCamp));

                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        } else {

                            textView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (Exception jsonException) {
                    Log.e("TAG", "Response: " + jsonException.getMessage());
                }

            }


        }

    }
    private class GETCSCPreCampInfoForApprovalListForDivisionalManager extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            List<ParamsPojo> paramsPojos = new ArrayList<>();
            paramsPojos.add(new ParamsPojo("DISTLGDCODE", params[0]));
            paramsPojos.add(new ParamsPojo("GP_ID", "0"));
            paramsPojos.add(new ParamsPojo("TALLGDCODE", "0"));
            paramsPojos.add(new ParamsPojo("FromDate", params[1]));
            paramsPojos.add(new ParamsPojo("TODate", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.GETCSCPreCampInfoForApproval_V1, ApplicationConstants.webservice_d2d, paramsPojos);
//            res = WebServiceCall.APICall(ApplicationConstants.GETCSCPreCampInfoForApprovalListForDivisionalManager_V1, ApplicationConstants.webservice_d2d, paramsPojos);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("GETCSCPreCampInfo", "onPostExecute: " + result);
            progressDialog.dismiss();

            if (!result.equals("") || !result.equals("[]")) {

                try {
                    cscPreCampInfoForApprovalModelList = new CSCPreCampInfoForApprovalModel();
                    filterCscPreCampInfoForApprovalModelList = new ArrayList<CSCPreCampInfoForApprovalModel.Output>();
                    cscPreCampInfoForApprovalModelList = new Gson().fromJson(result, CSCPreCampInfoForApprovalModel.class);

                    if (cscPreCampInfoForApprovalModelList.getStatus().equalsIgnoreCase("Success")) {

                        if (cscPreCampInfoForApprovalModelList.getOutput().size() > 0) {
                            recyclerView.setAdapter(new CSCCampRequestListAdapter(context, cscPreCampInfoForApprovalModelList.getOutput(), CampRequestActivity.this::onUpdateCamp));
                            textView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    Log.d("TAG", "onItemSelected: " + i);
                                    if (i == 1) {
                                        List<CSCPreCampInfoForApprovalModel.Output> outputs = new ArrayList<>();

                                        for (CSCPreCampInfoForApprovalModel.Output cs : cscPreCampInfoForApprovalModelList.getOutput()) {

                                            if (cs.getCampStatus() == 0) {
                                                outputs.add(cs);
                                            }

                                        }
                                        filterCscPreCampInfoForApprovalModelList = null;
                                        filterCscPreCampInfoForApprovalModelList = outputs;
                                        recyclerView.setAdapter(new CSCCampRequestListAdapter(context, filterCscPreCampInfoForApprovalModelList, CampRequestActivity.this::onUpdateCamp));

                                    } else if (i == 2) {
                                        List<CSCPreCampInfoForApprovalModel.Output> outputs = new ArrayList<>();

                                        for (CSCPreCampInfoForApprovalModel.Output cs : cscPreCampInfoForApprovalModelList.getOutput()) {

                                            if (cs.getCampStatus() == 2) {
                                                outputs.add(cs);
                                            }
                                        }
                                        filterCscPreCampInfoForApprovalModelList = null;

                                        filterCscPreCampInfoForApprovalModelList = outputs;
                                        recyclerView.setAdapter(new CSCCampRequestListAdapter(context, filterCscPreCampInfoForApprovalModelList, CampRequestActivity.this::onUpdateCamp));

                                    } else if (i == 3) {
                                        List<CSCPreCampInfoForApprovalModel.Output> outputs = new ArrayList<>();

                                        for (CSCPreCampInfoForApprovalModel.Output cs : cscPreCampInfoForApprovalModelList.getOutput()) {

                                            if (cs.getCampStatus() == 1) {
                                                outputs.add(cs);
                                            }
                                        }
                                        filterCscPreCampInfoForApprovalModelList = null;

                                        filterCscPreCampInfoForApprovalModelList = outputs;
                                        recyclerView.setAdapter(new CSCCampRequestListAdapter(context, filterCscPreCampInfoForApprovalModelList, CampRequestActivity.this::onUpdateCamp));

                                    } else {
                                        recyclerView.setAdapter(new CSCCampRequestListAdapter(context, cscPreCampInfoForApprovalModelList.getOutput(), CampRequestActivity.this::onUpdateCamp));

                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        } else {

                            textView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (Exception jsonException) {
                    Log.e("TAG", "Response: " + jsonException.getMessage());
                }

            }


        }

    }

    @Override
    public void onUpdateCamp() {
        apiCall();
    }
}