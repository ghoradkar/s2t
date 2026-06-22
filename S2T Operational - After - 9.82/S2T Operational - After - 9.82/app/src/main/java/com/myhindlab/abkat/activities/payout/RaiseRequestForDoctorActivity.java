package com.myhindlab.abkat.activities.payout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceDetailsMonthWiseAdapter;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceDetailsMonthWiseForDoctorAdapter;
import com.myhindlab.abkat.activities.payout.model.InvoiceMonthWiseDetailsnModel;
import com.myhindlab.abkat.activities.payout.model.VerificationRemarkModel;
import com.myhindlab.abkat.activities.re_registration.adapter.RejectDetailsAdminAdapter;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.adapters.AreaListSelectAdapter;
import com.myhindlab.abkat.adapters.DistrictListSelectAdapter;
import com.myhindlab.abkat.adapters.DivisionListSelectAdapter;
import com.myhindlab.abkat.adapters.LabListListSelectAdapter;
import com.myhindlab.abkat.adapters.OrganizationListSelectAdapter;
import com.myhindlab.abkat.adapters.PincodeListSelectAdapter;
import com.myhindlab.abkat.adapters.TalukaListSelectAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.ReportlistResponse;
import com.myhindlab.abkat.databinding.ActivityRaiseRequestBinding;
import com.myhindlab.abkat.databinding.ActivityRaiseRequestDoctorBinding;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RaiseRequestForDoctorActivity extends AppCompatActivity {
    ActivityRaiseRequestDoctorBinding binding;
    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;

    private List<InvoiceMonthWiseDetailsnModel.Output> invoiceDetailsList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String CampDate, CampDateToDate, type, toDate;

    private ProgressDialog pd;
    private AlertDialog teamDialog;
    private Context mContext;
    private List<DistrictOrgModel.Output> districtList;
    private List<AreaListModel.Output> areaList;
    private List<PincodeListModel.Output> pincodeList;
    private List<SubDivisionModel.Output> divisionList;

    private List<SubOrganizationModel.Output> organizationList;
    private List<TalukaListForFilterModel.Output> talukaList;
    private List<LandingLabModel.Output> landingLabList;
    private String call_statusid = "1", monthId = "0", isSendForVerification, isInvoiceApproved, remarkId, otpnumber, yearId = "0", campTypeId = "0", teamId = "0", userInvoiceID = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2", DISTLGDCODE = "0", agentId, oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRaiseRequestDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();

    }


    void init() {
        mContext = RaiseRequestForDoctorActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


        binding.rvPatientList.setLayoutManager(new LinearLayoutManager(mContext));


//        Calendar cal = Calendar.getInstance();
//        mYear = cal.get(Calendar.YEAR);
//        mMonth = cal.get(Calendar.MONTH);
//        mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//
////        Calendar todayCal = Calendar.getInstance();
////        todayCal.add(Calendar.DAY_OF_MONTH, -22);
////
//        Calendar toCal = Calendar.getInstance();
//        toCal.add(Calendar.DAY_OF_MONTH, -10);
//
//
//        CampDate = Utilities.dfDate4.format(toCal.getTime());
//        toDate = Utilities.dfDate4.format(new Date());

    }


    private void verifyOtp(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_verify_otp, null);
        alertBuilder.setView(alertLayout);
        TextInputEditText edt_Otp = alertLayout.findViewById(R.id.edt_Otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        verifyOtpBtn.setOnClickListener(view -> {
            //verify api call

            if (edt_Otp.getText().toString().trim().matches("")) {
                edt_Otp.setError("Please enter otp");
                return;
            }


            if (!edt_Otp.getText().toString().trim().equals(otpnumber)) {
                edt_Otp.setError("Entered OTP is not matched");
                return;
            }


//            new InsertRaiseDetails(alertDialog).execute("2", EmpCode, userInvoiceID, edt_Otp.getText().toString().trim(), "0", "", EmpCode);
            new InsertRaiseDetails(alertDialog).execute("2", EmpCode, yearId,monthId, edt_Otp.getText().toString().trim(), "0",  EmpCode,"","0");


//            if (edt_Otp.getText().toString().trim().length() != 10) {
//                edt_Otp.setError("Please enter valid otp");
//                return;
//            }


//            if (!edt_Otp.getText().toString().trim().equals(otpnumber)) {
//                edt_Otp.setError("Entered OTP is not matched");
//                return;
//            }

//           new VerifyOtp(alertDialog).execute(edt_mobile_no.getText().toString().trim(), edt_Otp.getText().toString().trim());


        });

        resendOtpBtn.setOnClickListener(view -> {


//            new GetOtp(2).execute("2", EmpCode, userInvoiceID, EmpCode, mobileNumber);
            new GetOtp(2).execute(EmpCode, yearId,monthId, EmpCode, mobileNumber);


            Utilities.showAlertDialog(mContext, "Alert", "OTP resend successfully", true);

            edt_Otp.setText("");

        });

    }

    private void verificationRemark(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_verify_remark, null);
        alertBuilder.setView(alertLayout);
        EditText tv_remark = alertLayout.findViewById(R.id.tv_remark);
        EditText tv_otp = alertLayout.findViewById(R.id.tv_otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);
        EditText tv_remark_description = alertLayout.findViewById(R.id.tv_remark_description);
        LinearLayout ll_main_others = alertLayout.findViewById(R.id.ll_main_others);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();


        tv_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRemarklist(tv_remark, ll_main_others, tv_remark_description);
            }
        });
        resendOtpBtn.setOnClickListener(view -> {
            //verify api call

            if (tv_otp.getText().toString().trim().matches("")) {
                Utilities.showAlertDialog(mContext, "Alert", "Please enter otp", false);
                return;
            }

            if (tv_remark.getText().toString().trim().matches("")) {
                Utilities.showAlertDialog(mContext, "Alert", "Please select remark", false);
                return;
            }

            if (ll_main_others.getVisibility() == View.VISIBLE) {
                if (tv_remark_description.getText().toString().trim().matches("")) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please enter remark", false);
                    return;

                }

            }


            if (!tv_otp.getText().toString().trim().equals(otpnumber)) {
                tv_otp.setError("Entered OTP is not matched");
                return;
            }

            new InsertVerificationDetails(alertDialog).execute("3", EmpCode, yearId,monthId, tv_otp.getText().toString().trim(), remarkId, EmpCode,tv_remark_description.getText().toString().trim(),"0");


//            if (edt_Otp.getText().toString().trim().length() != 10) {
//                edt_Otp.setError("Please enter valid otp");
//                return;
//            }


//            if (!edt_Otp.getText().toString().trim().equals(otpnumber)) {
//                edt_Otp.setError("Entered OTP is not matched");
//                return;
//            }

//           new VerifyOtp(alertDialog).execute(edt_mobile_no.getText().toString().trim(), edt_Otp.getText().toString().trim());


        });

        verifyOtpBtn.setOnClickListener(view -> {


//            new GetOtp(2).execute("3", EmpCode, userInvoiceID, EmpCode, mobileNumber);
            new GetOtp(2).execute(EmpCode, yearId,monthId, EmpCode, mobileNumber);

            Utilities.showAlertDialog(mContext, "Alert", "OTP resend successfully", true);

//          new GetOtp(2).execute(edt_mobile_no.getText().toString(), otpnumber, RegId, UserId, UserId);

//            tv.setText("");

        });

    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                mobileNumber = json.getString("BMobile");
                LabCode = json.getString("LabCode");
                oganizationId = json.getString("SubOrgId");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault() {
        if (getIntent() != null) {
            monthId = getIntent().getStringExtra("month");
            yearId = getIntent().getStringExtra("year");
            userInvoiceID = getIntent().getStringExtra("userInvoice");
            isSendForVerification = getIntent().getStringExtra("isSendForVerification");
            isInvoiceApproved = getIntent().getStringExtra("raisedStatus");


            if (isSendForVerification != null) {
                if (isSendForVerification.equalsIgnoreCase("1") || isInvoiceApproved.equalsIgnoreCase("1") || isInvoiceApproved.equalsIgnoreCase("2")) {
                    binding.btnSendForVerification.setVisibility(View.GONE);
                }
            }

            if (isInvoiceApproved.equalsIgnoreCase("1") || isInvoiceApproved.equalsIgnoreCase("2")) {
                binding.btnRaiseInvoice.setVisibility(View.GONE);

            }

        }

        getInvoiceDetails();

//        mobileNumber = "8668258532";
//        mobileNumber = "9764568835";
//        mobileNumber = "8888658717";
//        mobileNumber = "8668258532";
//        mobileNumber = "9665253245";




        binding.btnRaiseInvoice.setBackgroundColor(Color.parseColor("#8B60C9"));
        binding.btnSendForVerification.setBackgroundColor(Color.parseColor("#D87430"));


    }

    private void eventListener() {


        binding.btnRaiseInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new GetOtp(1).execute("2", EmpCode, userInvoiceID, EmpCode, mobileNumber);
                new GetOtp(1).execute( EmpCode, yearId,monthId, EmpCode, mobileNumber);

            }
        });

        binding.btnSendForVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new GetOtp(3).execute("3", EmpCode, userInvoiceID, EmpCode, mobileNumber);
                new GetOtp(3).execute(EmpCode, yearId,monthId, EmpCode, mobileNumber);

//                verificationRemark("11", otpnumber);
            }
        });

        binding.tvServiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("REGULAR", 1));
                campTypeModelArrayList.add(new CampTypeModel("DOOR TO DOOR", 3));
//                campTypeModelArrayList.add(new CampTypeModel("FLEXI", 5));
//                campTypeModelArrayList.add(new CampTypeModel("MMU Camp", 6));
                // campTypeModelArrayList.add(new CampTypeModel("CSC", 2));

                showCampType(campTypeModelArrayList);

            }
        });
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getReportlist();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        ImageButton imageButton = findViewById(R.id.btn_save_accordian);

        imageButton.setVisibility(View.GONE);


        // Now set the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Raise Invoice");
        }
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog;
                dialog = new Dialog(RaiseRequestForDoctorActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.raise_info_dialoge);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


    }

    private void getReportlist() {
        final ProgressDialog progressDialog = new ProgressDialog(RaiseRequestForDoctorActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<ReportlistResponse> call = apiService.REPORTLIST_RESPONSE_CALL(sessionManager.getUserDetailsJson().getEmpCode(), "0", teamId, call_statusid);
        call.enqueue(new Callback<ReportlistResponse>() {
            @Override
            public void onResponse(Call<ReportlistResponse> call, Response<ReportlistResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
//                    if (status.equalsIgnoreCase("Success")) {
//                        invoiceDetails = response.body().getOutput();
//                        List<OutputItem> reportdatalist = response.body().getOutput();
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, reportdatalist, RejectedBeneficiaryListActivity.this);
////                        binding.rvReportlist.setAdapter(callListAdaptor);
//
//                    } else {
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, new ArrayList<>(), RejectedBeneficiaryListActivity.this);
////                        binding.rvReportlist.setAdapter(callListAdaptor);
//                    }
                }
            }

            @Override
            public void onFailure(Call<ReportlistResponse> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


//    private void getapicall() {
//        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
//        progressDialog.setMessage("Please wait . . . ");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
//        Call<AppointmentListModel> call = apiService.Appointment_List();
//        call.enqueue(new Callback<AppointmentListModel>() {
//            @Override
//            public void onResponse(Call<AppointmentListModel> call, Response<AppointmentListModel> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful()) {
//                    String status = response.body().getStatus();
//                    String message = response.body().getMessage();
//                    if (status.equalsIgnoreCase("Success")) {
//                        List<AppointmentListModel.Output> Output = new ArrayList<>();
//
//                        Output.add(0,"All");
//                        Output.addAll( response.body().getOutput());
//                        if (Output.size() > 0) {
//
//
//                            for (AppointmentListModel.Output o :
//                                    Output) {
//
//
////                                if (o.getAssignStatusID() == 1) {
////                                    outputItems.remove(o);
////                                    break;
////                                }
//
//
//                            }
//                            showTrenchListDialog(outputItems);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AppointmentListModel> call, Throwable t) {
//                progressDialog.dismiss();
//                t.getLocalizedMessage();
//
//            }
//        });
//    }


    private void showTrenchListDialog(final List<VerificationRemarkModel.Output> trenchList, EditText tv_remark, LinearLayout ll_main_others, EditText tv_remark_description) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RaiseRequestForDoctorActivity.this);
        builderSingle.setTitle("Select Remark");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RaiseRequestForDoctorActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getVerificationRemark());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_remark.setText(trenchList.get(which).getVerificationRemark());
                remarkId = String.valueOf(trenchList.get(which).getVerificationRemarkID());

                getReportlist();

                if (remarkId.equalsIgnoreCase("0")) {
                    ll_main_others.setVisibility(View.VISIBLE);
                } else {
                    ll_main_others.setVisibility(View.GONE);

                }
                tv_remark_description.setText("");

            }
        });

        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }


//    private class BeneficiaryListAdapter extends RecyclerView.Adapter<BeneficiaryListAdapter.MyViewHolder> {
//
//        @NonNull
//        @Override
//        public BeneficiaryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.list_row_reject_list, parent, false);
//            return new BeneficiaryListAdapter.MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull BeneficiaryListAdapter.MyViewHolder holder, int pos) {
//            final int position = holder.getAdapterPosition();
//
//            holder.cb_select.setText(resourceListModel.getOutput().get(position).getResourceName());
//
//            if (resourceListModel.getOutput().get(position).isChecked()) {
//                holder.cb_select.setChecked(true);
//            }
//
//            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                resourceListModel.getOutput().get(position).setChecked(isChecked);
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return resourceListModel.getOutput().size();
//        }
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//
//            private CheckBox cb_select;
//            private TextView benName,tv_relation_with_worker,tv_pincode,tv_area,tv_address,member1,tv_member2,tv_select_team,
//                    tv_campId,campType,campDate,tv_receivedAt,tv_rejected_date,Reject_Reason;
//            private Button btn_submit;
//
//            public MyViewHolder(@NonNull View view) {
//                super(view);
//                cb_select = view.findViewById(R.id.cb_select);
//                benName = view.findViewById(R.id.benName);
//                tv_relation_with_worker = view.findViewById(R.id.tv_relation_with_worker);
//                tv_pincode = view.findViewById(R.id.tv_pincode);
//                tv_area = view.findViewById(R.id.tv_area);
//                tv_address = view.findViewById(R.id.tv_address);
//                tv_campId = view.findViewById(R.id.tv_campId);
//                campType = view.findViewById(R.id.campType);
//                campDate = view.findViewById(R.id.campDate);
//                tv_receivedAt = view.findViewById(R.id.tv_receivedAt);
//                tv_rejected_date = view.findViewById(R.id.tv_rejected_date);
//                Reject_Reason = view.findViewById(R.id.Reject_Reason);
//                member1 = view.findViewById(R.id.member1);
//                tv_member2 = view.findViewById(R.id.tv_member2);
//                tv_select_team = view.findViewById(R.id.tv_select_team);
//                btn_submit = view.findViewById(R.id.btn_submit);
//            }
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return position;
//        }
//    }


    public class GetAdminData extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("Fromdate", params[0]));
            param.add(new ParamsPojo("Todate", params[1]));
            param.add(new ParamsPojo("SubOrgId", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));
            param.add(new ParamsPojo("Arid", params[5]));
            param.add(new ParamsPojo("UserId", params[6]));
            param.add(new ParamsPojo("DESGID", params[7]));
            param.add(new ParamsPojo("Type", params[8]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionBeneficiaryDashboardForMob, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridDataWithCatagoryID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    BeneficiaryCountForPageLoadModel adminActiveInactiveModel = new Gson().fromJson(result, BeneficiaryCountForPageLoadModel.class);
                    type = adminActiveInactiveModel.getStatus();
                    message = adminActiveInactiveModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminlist = adminActiveInactiveModel.getOutput();
                        if (adminlist != null && adminlist.size() > 0) {

                            binding.rvPatientList.setAdapter(new RejectDetailsAdminAdapter(mContext, adminlist));


//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {

                            binding.rvPatientList.setAdapter(new RejectDetailsAdminAdapter(mContext, new ArrayList<>()));


                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {

                        binding.rvPatientList.setAdapter(new RejectDetailsAdminAdapter(mContext, new ArrayList<>()));


                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        }
    }


    public class GetDistrictList extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public GetDistrictList(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDistrict, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    districtList = new ArrayList<>();
                    DistrictOrgModel pojoDetails = new Gson().fromJson(result, DistrictOrgModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
//                              districtList.add(0, new DistrictList_Model("0", "All"));

//                            showDistrictListDialog(districtList);


                            DistrictListSelectAdapter districtListSelectAdapter1 = new DistrictListSelectAdapter(districtList);
                            recyclerView.setAdapter(districtListSelectAdapter1);

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    public class GetOrganizationNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DESGID", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.BindOrg, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<SubOrganizationModel.Output> organizationList = new ArrayList<>();
                    SubOrganizationModel pojoDetails = new Gson().fromJson(result, SubOrganizationModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            if (pojoDetails.getOutput().size() > 0) {
                                SubOrganizationModel.Output output = pojoDetails.getOutput().get(0);

                                oganizationId = String.valueOf(output.getSubOrgId());
//                                edt_organization.setText(output.getSubOrgName());


                                new GetAdminData().execute(CampDate, toDate, oganizationId, divisionId, DISTLGDCODE, "0", EmpCode, DESGID, "1");

//                                if (organizationId!=null){
//                                    new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new CampCalendar_Activity.GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }


                            }


//                            showOrganizationListDialog(organizationList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    private void getPincode(String area, RecyclerView recyclerView) {
        // Get the API interface from the ApiClient
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);

        // Make the API call asynchronously
        apiInterface.getPincodeList(area).enqueue(new Callback<PincodeListModel>() {
            @Override
            public void onResponse(Call<PincodeListModel> call, Response<PincodeListModel> response) {
                // Check if the response is successful
                if (response.isSuccessful() && response.body() != null) {
                    // Assign the response to pojoDetails
                    PincodeListModel pojoDetails = response.body();

                    // Check if the response status is "success"
                    if (pojoDetails.getStatus().equalsIgnoreCase("success")) {
                        // Update the global pincodeList variable
                        pincodeList = pojoDetails.getOutput();

//                        PincodeListModel.Output selectAll = new PincodeListModel.Output();
//                        selectAll.setPincode("Select All");
//                        selectAll.setChecked(false); // Initially, "Select All" is not selected
//                        pincodeList.add(0, selectAll); // Add at the beginning


                        // Check if pincodeList has data
                        if (pincodeList != null && pincodeList.size() > 0) {
                            // Update the RecyclerView adapter with the new data


                            PincodeListSelectAdapter adapter = new PincodeListSelectAdapter(pincodeList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            // Handle empty list case
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        // Handle non-success response status
                        Utilities.showAlertDialog(mContext, "Fail", pojoDetails.getMessage(), false);
                    }
                } else {
                    // Handle unsuccessful response or null body
                    Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
                }
            }

            @Override
            public void onFailure(Call<PincodeListModel> call, Throwable t) {
                // Handle failure of the API call
                t.printStackTrace(); // Log the error
                Utilities.showAlertDialog(mContext, "Error", t.getLocalizedMessage(), false);
            }
        });
    }


    public class GetLandingLab extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public GetLandingLab(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);
            //  res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    landingLabList = new ArrayList<>();

                    LandingLabModel landingLabModel = new Gson().fromJson(result, LandingLabModel.class);
                    type = landingLabModel.getStatus();
                    message = landingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        landingLabList = landingLabModel.getOutput();

                        if (landingLabList.size() > 0) {

                            LabListListSelectAdapter labListListSelectAdapter = new LabListListSelectAdapter(landingLabList);
                            recyclerView.setAdapter(labListListSelectAdapter);

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        }

//        private void showLadingLabDialogue(final List<LandingLabModel.Output> landinglablist) {
//            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//            builderSingle.setTitle("Select Lab");
//            builderSingle.setCancelable(false);
//
//            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
//
//            for (int i = 0; i < landinglablist.size(); i++) {
//                arrayAdapter.add(String.valueOf(landinglablist.get(i).getLabName()));
//            }
//
//            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    tv_lab.setText(landinglablist.get(which).getLabName());
//                    landinglabId = String.valueOf(landinglablist.get(which).getLabCode());
//
//
//                    if (Utilities.isNetworkAvailable(context)) {
//                        new PacketCollectionActivity.GetPostCampDetails().execute(
//                                DISTLGDCODE,
//                                fromDate,
//                                CampDateToDate,
//                                landinglabId,
//                                EmpCode
//
//                        );
//                    }
//
//
//
//
//                    tv_teamNumber.setText("");
//
//
////                    new GetCamName().execute();
//                }
//            });
//            builderSingle.show();
//
//        }
    }


    public class GetOrganization extends AsyncTask<String, Void, String> {


        RecyclerView recyclerView;

        public GetOrganization(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DESGID", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.BindOrg, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    organizationList = new ArrayList<>();
                    SubOrganizationModel pojoDetails = new Gson().fromJson(result, SubOrganizationModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {


                            OrganizationListSelectAdapter organizationListSelectAdapter = new OrganizationListSelectAdapter(organizationList);
                            recyclerView.setAdapter(organizationListSelectAdapter);


//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

//                            if (pojoDetails.getOutput().size() > 0) {
//                                SubOrganizationModel.Output output = pojoDetails.getOutput().get(0);
//
//                                organizationId = String.valueOf(output.getSubOrgId());
//
//
//                                if (organizationId!=null){
//                                    new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }
//
//
//                            }


//                            showOrganizationListDialog(organizationList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class GetDivision extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public GetDivision(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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

            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDivision, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    divisionList = new ArrayList<>();
                    SubDivisionModel pojoDetails = new Gson().fromJson(result, SubDivisionModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = pojoDetails.getOutput();
                        if (divisionList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

//                            showDivisionListDialog(divisionList);

                            DivisionListSelectAdapter divisionListSelectAdapter = new DivisionListSelectAdapter(divisionList);
                            recyclerView.setAdapter(divisionListSelectAdapter);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class GetTaluka extends AsyncTask<String, Void, String> {


        RecyclerView recyclerView;

        public GetTaluka(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetUserMappedTaluka, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    talukaList = new ArrayList<>();
                    TalukaListForFilterModel talukaModel = new Gson().fromJson(result, TalukaListForFilterModel.class);
                    type = talukaModel.getStatus();
                    message = talukaModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        talukaList = talukaModel.getOutput();
                        if (talukaList.size() > 0) {
//

                            TalukaListSelectAdapter talukaListSelectAdapter = new TalukaListSelectAdapter(talukaList);
                            recyclerView.setAdapter(talukaListSelectAdapter);

//                            showTalukaDialogue(camptypelist);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }

//        private void showTalukaDialogue(final List<TalukaModel.Output> talukalist) {
//            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
//            builderSingle.setTitle("Select Taluka");
//            builderSingle.setCancelable(false);
//
//            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);
//
//            for (int i = 0; i < talukalist.size(); i++) {
//                arrayAdapter.add(String.valueOf(talukalist.get(i).gettALNAME()));
//            }
//
//            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    tv_taluka.setText(talukalist.get(which).gettALNAME());
//                    talukaId = String.valueOf(talukalist.get(which).gettLLGDCODE());
//
//                    new AcceptMedicinePacketActivity.GetPostCampDetails().execute(
//                            fromDate,
//                            CampDateToDate,
//                            landinglabId,
//                            talukaId,
//                            EmpCode
//                    );
//
//                    //  refreshCalendar();
//                }
//            });
//            builderSingle.show();
//
//        }

    }


    public class GetAreaList extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public GetAreaList(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("DIVID", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("TALLGDCODE", params[3]));

            res = WebServiceCall.APICall(ApplicationConstants.GetArea, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    areaList = new ArrayList<>();
                    AreaListModel pojoDetails = new Gson().fromJson(result, AreaListModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        areaList = pojoDetails.getOutput();
                        if (areaList.size() > 0) {
//                              districtList.add(0, new DistrictList_Model("0", "All"));

//                            showDistrictListDialog(districtList);


                            AreaListSelectAdapter areaListSelectAdapter = new AreaListSelectAdapter(areaList);
                            recyclerView.setAdapter(areaListSelectAdapter);

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    private void getInvoiceDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(RaiseRequestForDoctorActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<InvoiceMonthWiseDetailsnModel> call = apiService.getInvoiceDetailsForDoctor("0", yearId, monthId, EmpCode, campTypeId, EmpCode);
        call.enqueue(new Callback<InvoiceMonthWiseDetailsnModel>() {
            @Override
            public void onResponse(Call<InvoiceMonthWiseDetailsnModel> call, Response<InvoiceMonthWiseDetailsnModel> response) {

                try {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        if (status.equalsIgnoreCase("Success")) {


                            binding.btnRaiseInvoice.setVisibility(View.VISIBLE);
                            binding.btnSendForVerification.setVisibility(View.VISIBLE);
//                        invoiceDetails = response.body().getOutput();
                            invoiceDetailsList = response.body().getOutput();
                            InvoiceDetailsMonthWiseForDoctorAdapter invoiceDetailsMonthWiseForDoctorAdapter = new InvoiceDetailsMonthWiseForDoctorAdapter(RaiseRequestForDoctorActivity.this, invoiceDetailsList);
                            binding.rvPatientList.setAdapter(invoiceDetailsMonthWiseForDoctorAdapter);


                            double t = 0;
                            double p = 0;
                            double q = 0;
                            double r = 0;
                            double s = 0;

                            for (InvoiceMonthWiseDetailsnModel.Output output :
                                    invoiceDetailsList) {

                                t = t + (output.getTotalBeneficiaries());
                                p = p + (output.getTotalIndividualRejected());
                                q = q + (output.getBillablesSameDay());
                                r = r + (output.getBillablesAnotherDay());
                                s = s + (output.getIndividualPenaltyAmount());

                            }


                            String formattedt = String.format("%.2f", t);

                            binding.tvRegisterBeneficiaryTotal.setText(formattedt);

                            String formattedp = String.format("%.2f", p);

                            binding.tvRejectedBeneFiTotal.setText(formattedp);

                            String formattedQ = String.format("%.2f", q);

                            binding.tvWorkerCountTotal.setText(formattedQ);

                            String formattedQd = String.format("%.2f", r);

                            binding.tvTotalDependentCount.setText(formattedQd);


                            String formattedQs = String.format("%.2f", s);

//                            binding.tvPenaulty.setText(formattedQs);


                            if (isSendForVerification != null) {
                                if (isSendForVerification.equalsIgnoreCase("1") || isInvoiceApproved.equalsIgnoreCase("1") || isInvoiceApproved.equalsIgnoreCase("2")) {
                                    binding.btnSendForVerification.setVisibility(View.GONE);
                                }
                            }

                            if (isInvoiceApproved.equalsIgnoreCase("1") || isInvoiceApproved.equalsIgnoreCase("2")) {
                                binding.btnRaiseInvoice.setVisibility(View.GONE);

                            }

                        } else {
                            InvoiceDetailsMonthWiseAdapter invoiceDetailsMonthWiseAdapter = new InvoiceDetailsMonthWiseAdapter(RaiseRequestForDoctorActivity.this, new ArrayList<>());
                            binding.rvPatientList.setAdapter(invoiceDetailsMonthWiseAdapter);
                            Utilities.showAlertDialog(mContext, "Alert", "Data Not Found", false);


                            binding.tvRegisterBeneficiaryTotal.setText("");
                            binding.tvRejectedBeneFiTotal.setText("");


                            binding.tvWorkerCountTotal.setText("");
                            binding.tvTotalDependentCount.setText("");
//                            binding.tvPenaulty.setText("");

                            binding.btnRaiseInvoice.setVisibility(View.GONE);
                            binding.btnSendForVerification.setVisibility(View.GONE);

                        }
                    } else {

                        Utilities.showAlertDialog(mContext, "Alert", message, false);

                    }
                } catch (Exception e) {

                    Utilities.showAlertDialog(mContext, "Alert", "Exception", false);
                }

            }

            @Override
            public void onFailure(Call<InvoiceMonthWiseDetailsnModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();


            }
        });

    }

    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Service Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
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
                binding.tvServiceType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());

                getInvoiceDetails();


            }
        });
        builderSingle.show();
    }


    private void getRemarklist(EditText tv_remark, LinearLayout ll_main_others, EditText tv_remark_description) {
        final ProgressDialog progressDialog = new ProgressDialog(RaiseRequestForDoctorActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<VerificationRemarkModel> call = apiService.getRemark();
        call.enqueue(new Callback<VerificationRemarkModel>() {
            @Override
            public void onResponse(Call<VerificationRemarkModel> call, Response<VerificationRemarkModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<VerificationRemarkModel.Output> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showTrenchListDialog(outputItems, tv_remark, ll_main_others, tv_remark_description);
                        }
                    }
//                    if (status.equalsIgnoreCase("Success")) {
//                        campBeneficiaryList = response.body().getOutput();
//                        List<OutputItem> reportdatalist = response.body().getOutput();
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, reportdatalist, RejectedBeneficiaryListActivity.this);
////                        binding.rvReportlist.setAdapter(callListAdaptor);
//
//                    } else {
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, new ArrayList<>(), RejectedBeneficiaryListActivity.this);
////                        binding.rvReportlist.setAdapter(callListAdaptor);
//                    }
                }
            }

            @Override
            public void onFailure(Call<VerificationRemarkModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    public class GetOtp extends AsyncTask<String, Void, String> {
        //ProgressDialog pd;

        int type;

        public GetOtp(int type) {
            this.type = type;
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
//            param.add(new ParamsPojo("PaymentStatusID", params[0]));
//            param.add(new ParamsPojo("UserID", params[1]));
//            param.add(new ParamsPojo("UserInviceID", params[2]));
//            Random r = new Random(System.currentTimeMillis());
//            otpnumber = String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
//            param.add(new ParamsPojo("OTP", otpnumber));
//            param.add(new ParamsPojo("CreatedBy", params[3]));
//            param.add(new ParamsPojo("MOBNO", params[4]));
//            param.add(new ParamsPojo("SubOrgID", oganizationId));




//            param.add(new ParamsPojo("PaymentStatusID", params[0]));

            param.add(new ParamsPojo("UserID", params[0]));
            param.add(new ParamsPojo("InvoiceYear", params[1]));
            param.add(new ParamsPojo("InvoiceMonth", params[2]));
            Random r = new Random(System.currentTimeMillis());
            otpnumber = String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
            param.add(new ParamsPojo("OTP", otpnumber));
            param.add(new ParamsPojo("CreatedBy", params[3]));
            param.add(new ParamsPojo("MOBNO", params[4]));
            param.add(new ParamsPojo("SubOrgID", oganizationId));

//            res = WebServiceCall.APICall(ApplicationConstants.InsertInvoiceOTPDetails_Org, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.InsertDoctorInvoiceOTPDetails, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
//                        ArrayList<ResourcesListModel.OutputBean> resourceList = resourcesListModel.getOutput();
//                        if (resourceList.size() > 0) {

                        Utilities.showToastMessage("OTP sent successfully on" + " " + mobileNumber, mContext, true);

                        if (type == 1) {
                            // verifyOtp(mno, resourceList.get(0).getDesgId());
                            verifyOtp(mobileNumber, otpnumber);
                        } else if (type == 3) {
                            verificationRemark("1", otpnumber);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Alert", message, false);

                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    private class InsertRaiseDetails extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;

        public InsertRaiseDetails(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
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
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("PaymentStatusID", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("InvoiceYear", params[2]));
            param.add(new ParamsPojo("InvoiceMonth", params[3]));
            param.add(new ParamsPojo("OTP", params[4]));
            param.add(new ParamsPojo("VerificationRemarkID", params[5]));
            param.add(new ParamsPojo("CreatedBy", params[6]));
            param.add(new ParamsPojo("OtherRemark", params[7]));
            param.add(new ParamsPojo("UserInviceID", params[8]));

//            res = WebServiceCall.APICall(ApplicationConstants.InsertInvoicePaymentStatus, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.InsertDoctorInvoicePaymentStatus, ApplicationConstants.webservice_d2d, param);

            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertSampleAccept", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");

                    if (status.equalsIgnoreCase("Success")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Invoice Raised Successfully");

                        alertDialog.dismiss();
                        LocalBroadcastManager.getInstance(RaiseRequestForDoctorActivity.this).sendBroadcast(new Intent("refresh_exp_benf_list"));


                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

                                finish();


                            }


                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(mContext, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(mContext, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class InsertVerificationDetails extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;

        public InsertVerificationDetails(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
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
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("PaymentStatusID", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("InvoiceYear", params[2]));
            param.add(new ParamsPojo("InvoiceMonth", params[3]));
            param.add(new ParamsPojo("OTP", params[4]));
            param.add(new ParamsPojo("VerificationRemarkID", params[5]));
            param.add(new ParamsPojo("CreatedBy", params[6]));
            param.add(new ParamsPojo("OtherRemark", params[7]));
            param.add(new ParamsPojo("UserInviceID", params[8]));

//            res = WebServiceCall.APICall(ApplicationConstants.InsertInvoicePaymentStatus, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.InsertDoctorInvoicePaymentStatus, ApplicationConstants.webservice_d2d, param);
            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertSampleAccept", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");

                    if (status.equalsIgnoreCase("Success")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);

                        builder.setMessage("Send For Verification successfully");

                        alertDialog.dismiss();

                        LocalBroadcastManager.getInstance(RaiseRequestForDoctorActivity.this).sendBroadcast(new Intent("refresh_exp_benf_list"));


                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

                                finish();


                            }


                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(mContext, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(mContext, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}