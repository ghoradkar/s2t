package com.myhindlab.abkat.activities.calling_dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.attendance_details.AttendanceDetailsActivity;
import com.myhindlab.abkat.activities.attendance_details.CampClosingConfirmationActivity;
import com.myhindlab.abkat.activities.attendance_details.adapter.AttendanceDetailsAdapter;
import com.myhindlab.abkat.activities.attendance_details.model.AttendanceDetailsModel;
import com.myhindlab.abkat.activities.attendance_details.model.AttendanceImageModel;
import com.myhindlab.abkat.activities.calling_dashboard.adapter.CallingDashboardAdapter;
import com.myhindlab.abkat.activities.calling_dashboard.model.CallingDashboardModel;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.activities.doortodoor.D2dPhysicalExaminationDetailsActivity;
import com.myhindlab.abkat.activities.payout.InvoiceGenModel;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceAdapter;
import com.myhindlab.abkat.activities.payout.model.YearModel;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedAdapter;
import com.myhindlab.abkat.appointment_confirmation.Expected_BeneficiariesActivity;
import com.myhindlab.abkat.appointment_confirmation.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.camera.CameraActivity;
import com.myhindlab.abkat.camera.utils.CaptureMode;
import com.myhindlab.abkat.databinding.ActivityAttendanceDetailsBinding;
import com.myhindlab.abkat.databinding.ActivityCallingDashboardBinding;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.AvailabilityTestModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CallingDashboardActivity extends AppCompatActivity {
    ActivityCallingDashboardBinding binding;
    private UserSessionManager sessionManager;

    private Bitmap patientPicBm = null, ackPicBm = null;

    private List<CallingDashboardModel.Output> attandanceDeatilsList;


    private List<AppointmentListModel.Output> districtList_models;

    private LocalBroadcastManager localBroadcastManager;


    private String TAG = D2DPatientRegistration_Activity.class.getSimpleName();


    private List<OutputItem> campBeneficiaryList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;
    private Calendar calMonth;


    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String CampDate, CampFromdate, otpnumber, CampDateToDate, type, toDate, yearid, monthId;

    private ProgressDialog pd;
    private AlertDialog teamDialog;
    private Context mContext;

    private ArrayList<TeamsDetailsModel> assignteamlist;

    private ArrayList<GetTeamsModel.OutputBean> teamList;


    private List<DistrictOrgModel.Output> districtList;
    private List<AreaListModel.Output> areaList;
    private List<PincodeListModel.Output> pincodeList;
    private List<SubDivisionModel.Output> divisionList;
    private String selectedApptDate;
    private String TeamName;
    private String campId;
    private String CampConfirmation = "0";
    private String inPhotoPath = "";
    private String outImagePath = "";
    private String patientImagePath = "";
    private String testFlag = "0";

    private ApiInterface apiInterface;

    private String ackImagePath = "";

    private List<SubOrganizationModel.Output> organizationList;
    private List<TalukaListForFilterModel.Output> talukaList;
    private List<LandingLabModel.Output> landingLabList;
    private String call_statusid = "1", teamId = "0", camptype, userInvoiceId, isPaymentNotReceived, isPaymentReceived, searchFilterType = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2", DISTLGDCODE = "0", agentId,
            oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode, statusType = "0";
    private UserSessionManager session;
    private SearchView searchView;

    private String isMarkInOut = "0";


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

        binding = ActivityCallingDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();
    }


    void init() {
        mContext = CallingDashboardActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);


        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


        binding.rvPatientList.setLayoutManager(new LinearLayoutManager(mContext));


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


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


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        binding.tvFromDate.setText(Utilities.dfDate.format(new Date()));
        CampDate = Utilities.dfDate.format(new Date());

        binding.tvToDate.setText(Utilities.dfDate.format(new Date()));
        CampFromdate = Utilities.dfDate.format(new Date());

        getDashboardCount();

    }


    private void eventListener() {


        binding.tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.tvFromDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));


                                //

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);


                                getDashboardCount();


                            }

                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        binding.tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.tvToDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));


                                CampFromdate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);


                                getDashboardCount();

                            }

                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        binding.tvTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getTeamData();


            }
        });


    }

    private void getTeamData() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Call<TeamsDetailsModel> call = apiService.getTeamDataForCCE(EmpCode);
        call.enqueue(new Callback<TeamsDetailsModel>() {
            @Override
            public void onResponse(Call<TeamsDetailsModel> call, Response<TeamsDetailsModel> response) {

                progressDialog.dismiss();   // Always dismiss first

                if (response.isSuccessful() && response.body() != null) {

                    String status = response.body().getStatus();
                    String message = response.body().getMessage();

                    if (status.equalsIgnoreCase("Success")) {


                        ArrayList<TeamsDetailsModel.OutputBean> outputItems = response.body().getOutput();


                        if (outputItems != null && outputItems.size() > 0) {

                            TeamsDetailsModel.OutputBean bean = new TeamsDetailsModel.OutputBean();
                            bean.setMember1("NA");
                            bean.setMember2("NA");
                            bean.setTeamName("All");
                            bean.setTeamid("0");

                            outputItems.add(0, bean);

                            showAssignedTeamListDialog(outputItems);

                        }

                    } else {
                        Utilities.showAlertDialog(mContext, "Alert", message, false);
                    }

                } else {
                    Utilities.showAlertDialog(mContext, "Error", "Server Error: " + response.code(), false);
                }
            }

            @Override
            public void onFailure(Call<TeamsDetailsModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String confirmation = intent.getStringExtra("confirmation");

            if ("1".equals(confirmation)) {
//                binding.llMainBtn.setVisibility(View.GONE);
            }

            Log.d("BroadcastReceiver", "onReceive: " + confirmation);
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

            getSupportActionBar().setTitle("Calling Summary");

        }
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog;
                dialog = new Dialog(CallingDashboardActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.attendance_mark_info_dialoge);
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


    private void getDashboardCount() {
        final ProgressDialog progressDialog = new ProgressDialog(CallingDashboardActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<CallingDashboardModel> call = apiService.getCallingDashboardCount(CampDate, CampFromdate, EmpCode, teamId);
        call.enqueue(new Callback<CallingDashboardModel>() {
            @Override
            public void onResponse(Call<CallingDashboardModel> call, Response<CallingDashboardModel> response) {
                progressDialog.dismiss();

                try {

                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    String time = response.body().getLastUpdatedOn();
                    if (response.isSuccessful()) {
                        if (status.equalsIgnoreCase("Success")) {

                            binding.tvNote.setText("Note : Real-time data will be updated at intervals of 4 hours" + "\n"+ "Data as of :-" + " "+ time);


                            attandanceDeatilsList = response.body().getOutput();
                            CallingDashboardAdapter callingDashboardAdapter = new CallingDashboardAdapter(CallingDashboardActivity.this, attandanceDeatilsList);
                            binding.rvPatientList.setAdapter(callingDashboardAdapter);

//
                        } else {
                            CallingDashboardAdapter callingDashboardAdapter = new CallingDashboardAdapter(CallingDashboardActivity.this, new ArrayList<>());
                            binding.rvPatientList.setAdapter(callingDashboardAdapter);
                            Utilities.showAlertDialog(mContext, "Alert", "Data Not Found", false);


                        }
                    } else {

                        Utilities.showAlertDialog(mContext, "Alert", message, false);

                    }
                } catch (Exception e) {

                    progressDialog.dismiss();
                    Utilities.showAlertDialog(mContext, "Alert", "Exception", false);
                }

            }

            @Override
            public void onFailure(Call<CallingDashboardModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();


            }
        });

    }


    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue_for_calling, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));

        rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                teamId = assignteamlist.get(position).getTeamid();

                                binding.tvTeam.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();

                                getDashboardCount();
                                Log.d(TAG, "teamId :-" + teamId);

                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));
                    return;
                }

                if (assignteamlist.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<TeamsDetailsModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (TeamsDetailsModel.OutputBean clientDetails : assignteamlist) {

                        String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                        String member2 = clientDetails.getMember2().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                member2.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        teamDialog = builderSingle.create();
        teamDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int numOfTeams = 0;
                for (TeamsDetailsModel.OutputBean team : assignteamlist
                ) {
                    if (team.isChecked()) {
                        numOfTeams += 1;
                    }
                }
                Log.d("TAG", "onClick: " + numOfTeams);

            }
        });


        teamDialog.show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver,
                        new IntentFilter("refresh_exp_benf_list"));
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        LocalBroadcastManager.getInstance(this)
//                .unregisterReceiver(broadcastReceiver);
//    }


}




