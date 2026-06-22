package com.myhindlab.abkat.activities.payout;

import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceAdapter;
import com.myhindlab.abkat.activities.payout.model.MonthModel;
import com.myhindlab.abkat.activities.payout.model.PaymentDetailsModel;
import com.myhindlab.abkat.activities.payout.model.YearModel;
import com.myhindlab.abkat.activities.re_registration.adapter.RejectDetailsAdminAdapter;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.activities.re_registration.model.CountsForDailyWorkDashboardModel;
import com.myhindlab.abkat.adapters.AreaListSelectAdapter;
import com.myhindlab.abkat.adapters.DistrictListSelectAdapter;
import com.myhindlab.abkat.adapters.DivisionListSelectAdapter;
import com.myhindlab.abkat.adapters.LabListAdapter;
import com.myhindlab.abkat.adapters.LabListListSelectAdapter;
import com.myhindlab.abkat.adapters.OrganizationListSelectAdapter;
import com.myhindlab.abkat.adapters.PincodeListSelectAdapter;
import com.myhindlab.abkat.adapters.TalukaListSelectAdapter;
import com.myhindlab.abkat.appointment_confirmation.callstatus.CallingRemarkModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.databinding.ActivityPayoutInvoiceTrackingBinding;
import com.myhindlab.abkat.databinding.ActivityRejectedListDashboardTrackingBinding;
import com.myhindlab.abkat.models.AreaListModel;
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
import org.maniteja.com.synclib.helper.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PayoutInvoiceActivity extends AppCompatActivity implements InvoiceAdapter.onTouchListner {
    ActivityPayoutInvoiceTrackingBinding binding;
    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;

    private List<OutputItem> campBeneficiaryList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;
    private Calendar calMonth;


    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String CampDate, otpnumber, CampDateToDate, type, toDate, yearid, monthId;

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
    private String call_statusid = "1", teamId = "0", userInvoiceId, isPaymentNotReceived, isPaymentReceived, searchFilterType = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2", DISTLGDCODE = "0", agentId,
            oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode,statusType = "0";
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
//
//        // 2. Get the controller for system bars
//        WindowInsetsControllerCompat windowInsetsController =
//                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
//
//        if (windowInsetsController != null) {
//            // Hide the status bar and the navigation bar
//            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
//
//            // Optional: Specify how hidden bars behave when the user interacts
//            windowInsetsController.setSystemBarsBehavior(
//                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            );
//        }

        binding = ActivityPayoutInvoiceTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();

    }


    void init() {
        mContext = PayoutInvoiceActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        calMonth = Calendar.getInstance();


        binding.rvDependent.setLayoutManager(new LinearLayoutManager(mContext));


        binding.btnPaymentReceived.setBackgroundColor(Color.parseColor("#8B60C9"));
        binding.btnPaymentNotReceived.setBackgroundColor(Color.parseColor("#D87430"));


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


            new InsertRaiseDetails(alertDialog).execute("4", EmpCode, userInvoiceId, edt_Otp.getText().toString().trim(), "0", "", EmpCode);


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


            new GetOtp(2).execute("4", EmpCode, userInvoiceId, EmpCode, mobileNumber);

            Utilities.showToastMessage("OTP resend successfully",mContext,true);


            edt_Otp.setText("");

        });

    }

    private void verifyNotReceivedOtp(String mno, String otp) {
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


            new InsertPaymentNotReceivedDetails(alertDialog).execute("5", EmpCode, userInvoiceId, edt_Otp.getText().toString().trim(), "0", "", EmpCode);


//            if (edt_Otp.getText().toString().trim().length() != 10) {
//                edt_Otp.setError("Please enter valid otp");
//                return;
//            }



//           new VerifyOtp(alertDialog).execute(edt_mobile_no.getText().toString().trim(), edt_Otp.getText().toString().trim());


        });

        resendOtpBtn.setOnClickListener(view -> {


            new GetOtp(2).execute("5", EmpCode, userInvoiceId, EmpCode, mobileNumber);

            Utilities.showToastMessage("OTP resend successfully",mContext,true);

            edt_Otp.setText("");

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
            CampDate = getIntent().getStringExtra("FromDate");
            toDate = getIntent().getStringExtra("ToDate");
//            oganizationId = getIntent().getStringExtra("organizationId");
            divisionId = getIntent().getStringExtra("divisionId");
            DISTLGDCODE = getIntent().getStringExtra("distLgd");
            TALLGDCODE = getIntent().getStringExtra("talukaId");
            landingLabId = getIntent().getStringExtra("labcode");
            areaArray = getIntent().getStringExtra("areaArray");
            pincodeArray = getIntent().getStringExtra("pincodeArray");
            searchFilterType = getIntent().getStringExtra("filterType");




//            mobileNumber = "8888658717";




            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);

            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);


            String[] monthYear = (android.text.format.DateFormat.format("MM yyyy", cal)).toString().split(" ");

            monthId = monthYear[0];
            yearid = monthYear[1];


            binding.tvYear.setText(yearid);



            if (monthId != null) {

                switch (monthId) {
                    case "01":
                        binding.tvMonth.setText("January");
                        break;
                    case "02":
                        binding.tvMonth.setText("February");
                        break;
                    case "03":
                        binding.tvMonth.setText("March");
                        break;
                    case "04":

                        binding.tvMonth.setText("April");
                        break;

                    case "05":
                        binding.tvMonth.setText("May");
                        break;
                    case "06":

                        binding.tvMonth.setText("June");
                        break;
                    case "07":

                        binding.tvMonth.setText("July");
                        break;

                    case "08":

                        binding.tvMonth.setText("August");
                        break;
                    case "09":
                        binding.tvMonth.setText("September");
                        break;

                    case "10":
                        binding.tvMonth.setText("October");
                        break;

                    case "11":

                        binding.tvMonth.setText("November");
                        break;

                    case "12":


                        binding.tvMonth.setText("December");
                        break;

                }
            }


            if (DESGID.equals("34")){
                statusType = "2";
            }else {
                statusType = "1";

            }

//            binding.tvMonth.setText(monthId);


            if (binding.rbBoard.isChecked()) {

                binding.llMain.setVisibility(View.GONE);
                binding.llInvoice.setVisibility(View.VISIBLE);
                binding.llMainMonth.setVisibility(View.GONE);
                binding.llMainBtn.setVisibility(View.GONE);

                binding.rbBoard.setTextColor(Color.parseColor("#FFFFFF"));
                binding.rbManual.setTextColor(Color.parseColor("#000000"));


                getInvoice();
            } else {
                binding.llMain.setVisibility(View.VISIBLE);
                binding.llInvoice.setVisibility(View.GONE);
                binding.llMainMonth.setVisibility(View.VISIBLE);
                binding.llMainBtn.setVisibility(View.VISIBLE);

                getPaymentDetails();

            }


//            if (DESGID.equals("60")){
//
//            }
//
//
//
//            new GetAdminData().execute(CampDate,toDate,oganizationId,divisionId,DISTLGDCODE,"0",EmpCode,DESGID,"1");
//


//            if (DESGID.equals("51") || DESGID.equals("166") || DESGID.equals("47")||DESGID.equals("83")) {
//                new GetAdminData().execute(CampDate,toDate,oganizationId,divisionId,DISTLGDCODE,"0",EmpCode,DESGID,"1");
//
//            } else {
////            edt_selectdistrict.setEnabled(false);
//
//                new GetOrganizationNew().execute(EmpCode, DESGID);
//
//
//            }


        }

    }

    private void eventListener() {

        binding.tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYearlist();

            }
        });
        binding.tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.tvYear.getText().toString().matches("")) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select year", false);
                    return;
                }

                getMonthlist();

            }
        });


        binding.btnPaymentReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                mobileNumber = "8888658717";

                new GetOtp(4).execute("4", EmpCode, userInvoiceId, EmpCode, mobileNumber);
            }
        });
        binding.btnPaymentNotReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mobileNumber = "8888658717";

                new GetOtp(5).execute("5", EmpCode, userInvoiceId, EmpCode, mobileNumber);

                getInvoice();

            }
        });
        binding.rbManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llMain.setVisibility(View.VISIBLE);
                binding.llInvoice.setVisibility(View.GONE);
                binding.llMainMonth.setVisibility(View.VISIBLE);
                binding.llMainBtn.setVisibility(View.VISIBLE);

                binding.rbManual.setTextColor(Color.parseColor("#FFFFFF"));
                binding.rbBoard.setTextColor(Color.parseColor("#000000"));

                setUpToolbar();


                getPaymentDetails();
            }
        });

        binding.rbBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llMain.setVisibility(View.GONE);
                binding.llInvoice.setVisibility(View.VISIBLE);
                binding.llMainMonth.setVisibility(View.GONE);
                binding.llMainBtn.setVisibility(View.GONE);

                binding.tvNote.setVisibility(View.GONE);

                binding.rbBoard.setTextColor(Color.parseColor("#FFFFFF"));
                binding.rbManual.setTextColor(Color.parseColor("#000000"));

                setUpToolbar();


            }
        });


    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getInvoice();
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



        if (getSupportActionBar() != null) {

            if (binding.rbBoard.isChecked()) {
                getSupportActionBar().setTitle("             Invoices & Payments");
                imageButton.setVisibility(View.VISIBLE);

            } else {
                getSupportActionBar().setTitle("    Payment Details & Confirmation");
                imageButton.setVisibility(View.GONE);

            }
        }
//        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//        toolbar.setNavigationOnClickListener(v -> finish());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog;
                dialog = new Dialog(PayoutInvoiceActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.invoice_info_dialoge);
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

    private void getInvoice() {
        final ProgressDialog progressDialog = new ProgressDialog(PayoutInvoiceActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<InvoiceGenModel> call = apiService.getInvoice(Integer.parseInt(yearid), EmpCode,EmpCode,statusType);
        call.enqueue(new Callback<InvoiceGenModel>() {
            @Override
            public void onResponse(Call<InvoiceGenModel> call, Response<InvoiceGenModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
//                        campBeneficiaryList = response.body().getOutput();
                        List<InvoiceGenModel.Output> invoiceList = response.body().getOutput();
                        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(PayoutInvoiceActivity.this, invoiceList, PayoutInvoiceActivity.this);
                        binding.rvDependent.setAdapter(invoiceAdapter);

                    } else {
                        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(PayoutInvoiceActivity.this, new ArrayList<>(), PayoutInvoiceActivity.this);
                        binding.rvDependent.setAdapter(invoiceAdapter);
                        Utilities.showAlertDialog(mContext, "Alert", "Invoice Details Not Found", false);

                    }
                }else {
                    Utilities.showAlertDialog(mContext, "Alert", "Invoice Details Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<InvoiceGenModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }

    private void getPaymentDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(PayoutInvoiceActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<PaymentDetailsModel> call = apiService.getPaymentDetails(Integer.parseInt(yearid), monthId, EmpCode,EmpCode);
        call.enqueue(new Callback<PaymentDetailsModel>() {
            @Override
            public void onResponse(Call<PaymentDetailsModel> call, Response<PaymentDetailsModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {

                        binding.btnPaymentNotReceived.setVisibility(View.VISIBLE);
                        binding.btnPaymentReceived.setVisibility(View.VISIBLE);
//                        campBeneficiaryList = response.body().getOutput();
                        List<PaymentDetailsModel.Output> invoiceList = response.body().getOutput();
                        if (invoiceList.size() > 0) {

                            for (PaymentDetailsModel.Output o : invoiceList) {

                                userInvoiceId = String.valueOf(o.getUserInviceID());

                                binding.payableAmount.setText("" + o.getPayableAmount());
                                binding.tvPenaulty.setText("" + o.getPenaltyAmount());
                                binding.tvTDS.setText("" + o.getTDSAmount());
                                binding.tvFinalAmount.setText("" + o.getFinalPayableAmount());
                                binding.tvPaymentDate.setText("" + o.getTDSAmount());
                                binding.tvPaymentDate.setText("" + o.getPaymentDate());
                                binding.tvUTRNO.setText("" + o.getUTRNo());
                                binding.tvGrossPayable.setText(""+o.getGrossAmount());

                                isPaymentReceived = o.getPayementReceivedStatus();
                                isPaymentNotReceived = o.getPayementNotReceivedStatus();

                                if (isPaymentReceived.equalsIgnoreCase("1")) {
                                    binding.btnPaymentNotReceived.setVisibility(View.GONE);
                                    binding.btnPaymentReceived.setVisibility(View.GONE);
                                } else if (isPaymentNotReceived.equalsIgnoreCase("1")) {
                                    binding.btnPaymentNotReceived.setVisibility(View.GONE);
//                                    binding.btnPaymentReceived.setVisibility(View.GONE);
                                }

                                if (o.getUTRNo().equalsIgnoreCase("") || o.getPaymentDate().equalsIgnoreCase("")) {
                                    binding.btnPaymentNotReceived.setVisibility(View.GONE);
                                    binding.btnPaymentReceived.setVisibility(View.GONE);


                                    binding.tvNote.setVisibility(View.VISIBLE);

                                }else {

                                    binding.tvNote.setVisibility(View.GONE);

                                }

                            }

                        }



                    } else {

                        binding.payableAmount.setText("0");
                        binding.tvPenaulty.setText("0");
                        binding.tvTDS.setText("0");
                        binding.tvFinalAmount.setText("0");
                        binding.tvPaymentDate.setText("0");
                        binding.tvPaymentDate.setText("0");
                        binding.tvUTRNO.setText("0");
                        binding.tvGrossPayable.setText("0");

                        Utilities.showAlertDialog(mContext, "Alert", "Payment details will be available after Invoice is Generated", false);

                        binding.btnPaymentNotReceived.setVisibility(View.GONE);
                        binding.btnPaymentReceived.setVisibility(View.GONE);
                        binding.tvNote.setVisibility(View.GONE);


                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentDetailsModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();
                binding.btnPaymentNotReceived.setVisibility(View.GONE);
                binding.btnPaymentReceived.setVisibility(View.GONE);

            }
        });

    }

    private void getYearlist() {
        final ProgressDialog progressDialog = new ProgressDialog(PayoutInvoiceActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<YearModel> call = apiService.GET_YEAR_RESPONSE_CALL(EmpCode);
        call.enqueue(new Callback<YearModel>() {
            @Override
            public void onResponse(Call<YearModel> call, Response<YearModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<YearModel.Output> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showTrenchListDialog(outputItems);
                        }
                    }else {

                        Utilities.showAlertDialog(mContext, "Alert", message, false);

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
            public void onFailure(Call<YearModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }

    private void getMonthlist() {
        final ProgressDialog progressDialog = new ProgressDialog(PayoutInvoiceActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<MonthModel> call = apiService.GET_MONTH_RESPONSE_CALL(yearid,EmpCode);
        call.enqueue(new Callback<MonthModel>() {
            @Override
            public void onResponse(Call<MonthModel> call, Response<MonthModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<MonthModel.Output> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showMonthTrenchListDialog(outputItems);
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
            public void onFailure(Call<MonthModel> call, Throwable t) {
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


    private void showTrenchListDialog(final List<YearModel.Output> trenchList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PayoutInvoiceActivity.this);
        builderSingle.setTitle("Select Year");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PayoutInvoiceActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getYearName());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                binding.tvYear.setText(trenchList.get(which).getYearName());
                yearid = String.valueOf(trenchList.get(which).getYearID());
//                binding.inputLayoutCallStatus.setErrorEnabled(false);

                getInvoice();

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

    private void showMonthTrenchListDialog(final List<MonthModel.Output> trenchList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PayoutInvoiceActivity.this);
        builderSingle.setTitle("Select Month");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PayoutInvoiceActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getMonth_Name_Eng());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                binding.tvMonth.setText(trenchList.get(which).getMonth_Name_Eng());
                monthId = String.valueOf(trenchList.get(which).getMonth_id());

                getPaymentDetails();


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

    @Override
    public void onDataClick(InvoiceGenModel.Output item) {


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

                            binding.rvDependent.setAdapter(new RejectDetailsAdminAdapter(mContext, adminlist));


//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {

                            binding.rvDependent.setAdapter(new RejectDetailsAdminAdapter(mContext, new ArrayList<>()));


                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {

                        binding.rvDependent.setAdapter(new RejectDetailsAdminAdapter(mContext, new ArrayList<>()));


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
            param.add(new ParamsPojo("PaymentStatusID", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("UserInviceID", params[2]));
            Random r = new Random(System.currentTimeMillis());
            otpnumber = String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
            param.add(new ParamsPojo("OTP", otpnumber));
            param.add(new ParamsPojo("CreatedBy", params[3]));
            param.add(new ParamsPojo("MOBNO", params[4]));
            param.add(new ParamsPojo("SubOrgID", oganizationId));

            res = WebServiceCall.APICall(ApplicationConstants.InsertInvoiceOTPDetails_Org, ApplicationConstants.webservice_d2d, param);
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
                        Utilities.showToastMessage("OTP sent successfully on"+" "+mobileNumber,mContext,true);

                        if (type == 4) {
                            // verifyOtp(mno, resourceList.get(0).getDesgId());
                            verifyOtp(mobileNumber, otpnumber);
                        } else if (type == 5) {
                            verifyNotReceivedOtp("1", otpnumber);
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
            param.add(new ParamsPojo("UserInviceID", params[2]));
            param.add(new ParamsPojo("OTP", params[3]));
            param.add(new ParamsPojo("VerificationRemarkID", params[4]));
            param.add(new ParamsPojo("OtherRemark", params[5]));
            param.add(new ParamsPojo("CreatedBy", params[6]));

            res = WebServiceCall.APICall(ApplicationConstants.InsertInvoicePaymentStatus, ApplicationConstants.webservice_d2d, param);
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
                        builder.setMessage("You confirmed as payment received.\n" +
                                "\n" +
                                "Thanks!");
                        alertDialog.dismiss();


                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

                                getPaymentDetails();

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

    private class InsertPaymentNotReceivedDetails extends AsyncTask<String, Void, String> {


        AlertDialog alertDialog;

        public InsertPaymentNotReceivedDetails(AlertDialog alertDialog) {

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
            param.add(new ParamsPojo("UserInviceID", params[2]));
            param.add(new ParamsPojo("OTP", params[3]));
            param.add(new ParamsPojo("VerificationRemarkID", params[4]));
            param.add(new ParamsPojo("OtherRemark", params[5]));
            param.add(new ParamsPojo("CreatedBy", params[6]));

            res = WebServiceCall.APICall(ApplicationConstants.InsertInvoicePaymentStatus, ApplicationConstants.webservice_d2d, param);
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
                        builder.setMessage("We will revert back to you.\n" +
                                "\n" +
                                "Thanks!");

                        alertDialog.dismiss();


                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

                                getPaymentDetails();

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